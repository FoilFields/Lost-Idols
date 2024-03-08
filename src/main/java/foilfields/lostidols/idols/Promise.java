package foilfields.lostidols.idols;

import foilfields.lostidols.init.Particles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static foilfields.lostidols.init.Sounds.PROMISE_CHARGE;

public class Promise extends AbstractIdol {
    public Promise(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient && Math.random() < 0.1f) {
            Vec3d particlePosition = Vec3d.ofCenter(position);
            ((ServerWorld) world).spawnParticles(Particles.FIREFLY, particlePosition.getX(), particlePosition.getY(), particlePosition.getZ(), 1, 5, 2, 5, 0.01);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!state.get(CHARGED) && itemStack.isOf(Items.FLINT_AND_STEEL)) {
            Vec3d center = Vec3d.ofCenter(pos);
            world.playSound(player, center.getX(), center.getY(), center.getZ(), PROMISE_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);

            itemStack.damage(1, player, (playerx) -> {
                playerx.sendToolBreakStatus(hand);
            });

            if (!world.isClient()) {
                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
            }
            
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(5D, 0D, 5D, 11D, 9D, 11D);
    }
}
