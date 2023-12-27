package foilfields.lostidols.idols;

import foilfields.lostidols.init.Particles;
import net.minecraft.block.*;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class Bird extends AbstractIdol implements LandingBlock {
    public Bird(Settings settings) {
        super(settings);
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, 2);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        world.scheduleBlockTick(pos, this, 2);
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()) {
            FallingBlockEntity.spawnFromBlock(world, pos, state).setHurtEntities(2.0F, 40);
        }
    }

    public static boolean canFallThrough(BlockState state) {
        return state.isAir() || state.isIn(BlockTags.FIRE) || state.isLiquid() || state.isReplaceable();
    }

    // Charge vfx
    private void charge(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        if (!fallingBlockEntity.isSilent()) {
            world.playSound(null, pos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS);
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS);
        }

        Vec3d center = Vec3d.ofCenter(pos);

        BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState());
        ((ServerWorld) world).spawnParticles(particleEffect, center.getX(), center.getY(), center.getZ(), 10, 0.25, 0.25, 0.25, 0);
    }

    // Sounds for landing and triggers charging if had a fall time of over 35
    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
        if (fallingBlockEntity.timeFalling > 35 && !fallingBlockState.get(CHARGED)) {
            world.setBlockState(pos, fallingBlockState.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            if (!world.isClient) {
                charge(world, pos, fallingBlockEntity);
            }
        }

        if (!world.isClient && !fallingBlockEntity.isSilent()) {
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS);
        }
    }

    // Sound effect when landing and breaking
    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        if (!world.isClient && !fallingBlockEntity.isSilent()) {
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS);
        }
    }

    // Particle effects when floating
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.down();
            if (canFallThrough(world.getBlockState(blockPos))) {
                ParticleUtil.spawnParticle(world, pos, random, new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()));
            }
        }
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            PlayerEntity playerEntity = world.getClosestPlayer(position.getX(), position.getY(), position.getZ(), 200, false);
            if (playerEntity == null) return;

            Box area = new Box(new Vec3d(position.getX() - 50, position.getY() - 50, position.getZ() - 50), new Vec3d(position.getX() + 50, position.getY() + 50, position.getZ() + 50));
            List<Entity> entities = world.getOtherEntities(null, area);

            for (Entity entity : entities) {
                if (entity.getType().equals(EntityType.PHANTOM)) {
                    PhantomEntity phantomEntity = (PhantomEntity) entity;

                    // Damage effects
                    if (phantomEntity.timeUntilRegen <= 10.0F && phantomEntity.getHealth() > 0.0F) { // Attack cooldown
                        phantomEntity.damage(world.getDamageSources().indirectMagic(playerEntity, playerEntity), 3);
                        world.playSound(null, phantomEntity.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.BLOCKS);
                        ((ServerWorld) world).spawnParticles(ParticleTypes.SWEEP_ATTACK, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
                    }

                    // Beam to entity
                    Vec3d sourcePosition = position.toCenterPos();
                    Vec3d difference = phantomEntity.getPos().subtract(sourcePosition);
                    int iterations = MathHelper.clamp((int) difference.length() / 10, 1, 3);
                    Random random = Random.create();
                    for (int i = 0; i < iterations; i++) {
                        double proportion = random.nextDouble();
                        Vec3d particlePosition = sourcePosition.add(difference.multiply(proportion));
                        ((ServerWorld) world).spawnParticles(Particles.PHANTOM_RAY, particlePosition.getX(), particlePosition.getY(), particlePosition.getZ(), 1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    // General shape of idol, box for plate and box for bird on top
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.combineAndSimplify(createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D), createCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 16.0D, 14.0D), BooleanBiFunction.OR);
    }
}
