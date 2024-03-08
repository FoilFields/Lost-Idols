package foilfields.lostidols.idols;

import foilfields.lostidols.init.Particles;
import foilfields.lostidols.init.StatusEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

import static foilfields.lostidols.init.Sounds.SHULKER_CHARGE;
import static foilfields.lostidols.init.Sounds.UNDYING_CHARGE;
import static net.minecraft.entity.effect.StatusEffects.LEVITATION;

public class Shulker extends AbstractIdol {
    public Shulker(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {

            BlockPos columnPos = position.add(0, 1, 0);

            while (!world.isOutOfHeightLimit(columnPos) && !world.getBlockState(columnPos).getMaterial().isSolid()) {
                columnPos = columnPos.add(0, 1, 0);
            }

            Vec3d lowCorner = new Vec3d(position.getX(), position.getY(), position.getZ());
            Vec3d highCorner = new Vec3d(columnPos.getX() + 1, columnPos.getY(), columnPos.getZ() + 1);

            Vec3d difference = highCorner.subtract(lowCorner);

            Random random = Random.create();

            // Base
            for (int i = 0; i < 3; i++) {
                Vec3d particlePos = lowCorner.add(random.nextFloat(), 0, random.nextFloat());
                ((ServerWorld) world).spawnParticles(Particles.STREAM, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0, 0, 0, 0);
            }

            // Column
            for (int i = 0; i < difference.getY() / 2; i++) {
                if (random.nextFloat() < 0.8F) continue;
                Vec3d particlePos = lowCorner.add(difference.multiply(random.nextFloat(), random.nextFloat(), random.nextFloat()));
                ((ServerWorld) world).spawnParticles(Particles.STREAM, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0, 0, 0, 0);
            }

            Box area = new Box(lowCorner, highCorner);
            List<Entity> entities = world.getOtherEntities(null, area);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(
                            LEVITATION,
                            2,
                            10,
                            false,
                            false
                    ));
                }
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!state.get(CHARGED) && itemStack.isOf(Items.PHANTOM_MEMBRANE)) {
            Vec3d center = Vec3d.ofCenter(pos);

            itemStack.decrement(1);

            if (!world.isClient()) {
                world.playSound(null, center.getX(), center.getY(), center.getZ(), SHULKER_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, center.getX(), center.getY(), center.getZ(), 10, 0, 0, 0, 0.1);
                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
