package foilfields.lostidols.idols;

import foilfields.lostidols.init.Sounds;
import foilfields.lostidols.init.Statistics;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static foilfields.lostidols.init.Sounds.PROMISE_CHARGE;
import static foilfields.lostidols.init.Sounds.UNDYING_CHARGE;

public class Undying extends AbstractIdol {
    public Undying(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.combineAndSimplify(createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D), createCuboidShape(2.0D, 3.0D, 2.0D, 14.0D, 16.0D, 14.0D), BooleanBiFunction.OR);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!state.get(CHARGED) && itemStack.isOf(Items.EMERALD)) {
            Vec3d center = pos.toCenterPos();
            world.playSound(player, center.getX(), center.getY(), center.getZ(), UNDYING_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!world.isClient()) {
                ((ServerWorld) world).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, center.getX(), center.getY(), center.getZ(), 10, 0, 0, 0, 0.1);
                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem())); // This is kind of funny
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
