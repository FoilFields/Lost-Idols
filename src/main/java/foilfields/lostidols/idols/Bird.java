package foilfields.lostidols.idols;

import net.minecraft.block.*;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
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
import org.joml.Vector3d;
import org.joml.Vector3f;

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

    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
        if (fallingBlockEntity.timeFalling > 70 && !fallingBlockState.get(CHARGED)) {
            world.setBlockState(pos, fallingBlockState.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            if (!world.isClient()) {
                ((ServerWorld) world).spawnParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 3, 0.25, 0.25, 0.25, 0);
            }
        }

        if (!fallingBlockEntity.isSilent()) {
            world.syncWorldEvent(1031, pos, 0);
        }

    }

    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        if (!fallingBlockEntity.isSilent()) {
            world.syncWorldEvent(1029, pos, 0);
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.down();
            if (canFallThrough(world.getBlockState(blockPos))) {
                ParticleUtil.spawnParticle(world, pos, random, new BlockStateParticleEffect(ParticleTypes.BLOCK, state));
            }
        }
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            Box area = new Box(new Vec3d(position.getX() - 50, position.getY() - 50, position.getZ() - 50), new Vec3d(position.getX() + 50, position.getY() + 50, position.getZ() + 50));
            List<Entity> entities = world.getOtherEntities(null, area);

            for (Entity ent : entities) {
                if (ent.getType().equals(EntityType.PHANTOM)) {
                    Vec3d dir = new Vec3d(ent.getPos().x - position.getX(), ent.getPos().y - position.getY(), ent.getPos().z - position.getZ()).normalize();
                    Vec3d pos = new Vec3d(position.getX(), position.getY(), position.getZ());
                    ent.damage(world.getDamageSources().indirectMagic(world.getClosestPlayer(ent, 200), world.getClosestPlayer(ent, 200)), 3);
                    ((ServerWorld) world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, ent.getX(), ent.getY(), ent.getZ(), 1, 0, 0, 0, 1);
                }
            }
        }

    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.combineAndSimplify(createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D), createCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 16.0D, 14.0D), BooleanBiFunction.OR);
    }
}
