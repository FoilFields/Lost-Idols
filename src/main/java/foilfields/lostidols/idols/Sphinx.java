package foilfields.lostidols.idols;

import foilfields.lostidols.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

import static foilfields.lostidols.init.Sounds.SPHINX_CHARGE;

public class Sphinx extends AbstractIdol {
    public Sphinx(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            Box area = new Box(new Vec3d(position.getX() - 50, position.getY() - 50, position.getZ() - 50), new Vec3d(position.getX() + 50, position.getY() + 50, position.getZ() + 50));
            List<Entity> entities = world.getOtherEntities(null, area);

            Random random = Random.create();

            Vec3d center = Vec3d.ofCenter(position);
            if (random.nextFloat() < 0.04f) ((ServerWorld) world).spawnParticles(ParticleTypes.HEART, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0.2);

            for(Entity ent: entities){
                if(ent instanceof LivingEntity livingEntity){
                    if (ent.getPos().distanceTo(Vec3d.ofCenter(position)) > 50) continue;

                    if (random.nextFloat() < 0.02f) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20, 4, false, false, true), null);
                        ((ServerWorld) world).spawnParticles(ParticleTypes.HEART, livingEntity.getX(), livingEntity.getY() + 2.2, livingEntity.getZ(), 1, 0, 0, 0, 0.5);
                    }

                }
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.3f, 0f, 0.3f, 0.7f, 1.0f, 0.7f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (!state.get(CHARGED) && itemStack.isOf(Items.GOLD_BLOCK)) {
            itemStack.decrement(1);

            if (!world.isClient()) {
                Vec3d center = Vec3d.ofCenter(pos);

                world.playSound(null, center.getX(), center.getY(), center.getZ(), SPHINX_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GOLD_BLOCK.getDefaultState());
                ((ServerWorld) world).spawnParticles(particleEffect, center.getX(), center.getY(), center.getZ(), 10, 0.25, 0.25, 0.25, 0);

                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                Utils.ExplodeThrow(center, (ServerWorld) world, 10);
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
