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
            // TODO: Particles!

            BlockPos columnPos = position.add(0, 1, 0);
            while (!world.isOutOfHeightLimit(columnPos) && !world.getBlockState(columnPos).isSolid()) {
                columnPos = columnPos.add(0, 1, 0);
            }

            Box area = new Box(new Vec3d(position.getX(), position.getY(), position.getZ()), new Vec3d(columnPos.getX() + 1, columnPos.getY(), columnPos.getZ() + 1));
            List<Entity> entities = world.getOtherEntities(null, area);

            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity) {
                    ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(
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
            Vec3d center = pos.toCenterPos();

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
