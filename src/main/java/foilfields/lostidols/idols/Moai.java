package foilfields.lostidols.idols;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Moai extends AbstractIdol {
    public Moai(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {

    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void spit(World world, BlockState state, BlockPos pos) {
        if (!world.isClient && state.get(CHARGED)) {
            world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            world.playSound(null, pos, SoundEvents.BLOCK_FROGSPAWN_HATCH, SoundCategory.BLOCKS);
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.75f, pos.getZ() + 0.5f, new ItemStack(Items.EGG)));
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient && !state.get(CHARGED)) {
            world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            world.playSound(null, pos, SoundEvents.BLOCK_FROGSPAWN_PLACE, SoundCategory.BLOCKS);
        }

        super.randomTick(state, world, pos, random);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        spit(world, state, pos);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean redstoned = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        if (redstoned && state.get(CHARGED)) {
            spit(world, state, pos);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1.0f, 0.75f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        spit(world, state, pos);


        return ActionResult.SUCCESS;
    }
}
