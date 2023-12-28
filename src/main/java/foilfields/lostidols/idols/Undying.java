package foilfields.lostidols.idols;

import foilfields.lostidols.init.Particles;
import foilfields.lostidols.init.StatusEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

import static foilfields.lostidols.init.Sounds.UNDYING_CHARGE;

public class Undying extends AbstractIdol {
    public Undying(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            Vec3d center = position.toCenterPos();
            Random random = Random.create();
            for (int i = 0; i < 5; i++) {
                Vec3d particlePosition = new Vec3d(20, 0, 0);
                particlePosition = particlePosition.rotateY(random.nextFloat() * 180f);
                particlePosition = particlePosition.rotateX(random.nextFloat() * 180f);
                particlePosition = particlePosition.add(center);

                ((ServerWorld) world).spawnParticles(Particles.UNDYING, particlePosition.getX(), particlePosition.getY(), particlePosition.getZ(), 1, 0, 0, 0, 0);
            }

            Box area = new Box(new Vec3d(position.getX() - 20, position.getY() - 20, position.getZ() - 20), new Vec3d(position.getX() + 20, position.getY() + 20, position.getZ() + 20));
            List<Entity> entities = world.getOtherEntities(null, area);

            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity) {
                    if (entity.getPos().distanceTo(position.toCenterPos()) < 20.0f)
                        ((PlayerEntity) entity).addStatusEffect(
                                new StatusEffectInstance(
                                        StatusEffects.UNDYING,
                                        10,
                                        0,
                                        false,
                                        false,
                                        true),
                                null);
                }
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.combineAndSimplify(
                VoxelShapes.combineAndSimplify(
                        createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
                        createCuboidShape(5.0D, 9.0D, 5.0D, 11.0D, 16.0D, 11.0D),
                        BooleanBiFunction.OR
                ),
                state.get(FACING) == Direction.SOUTH || state.get(FACING) == Direction.NORTH ? createCuboidShape(5.5D, 2.0D, 6.0D, 10.5D, 10.0D, 10.0D) : createCuboidShape(6.0D, 2.0D, 5.5D, 10.0D, 10.0D, 10.5D),
                BooleanBiFunction.OR
        );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!state.get(CHARGED) && itemStack.isOf(Items.EMERALD)) {
            Vec3d center = pos.toCenterPos();

            itemStack.decrement(1);

            if (!world.isClient()) {
                world.playSound(null, center.getX(), center.getY(), center.getZ(), UNDYING_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                ((ServerWorld) world).spawnParticles(Particles.UNDYING, center.getX(), center.getY(), center.getZ(), 10, 0.2, 0.2, 0.2, 0.2);
                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem())); // This is kind of funny
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
