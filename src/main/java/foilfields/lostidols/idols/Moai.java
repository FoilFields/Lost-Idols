package foilfields.lostidols.idols;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Moai extends AbstractIdol {
    public static final BooleanProperty POWERED = Properties.POWERED;

    public Moai(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {

    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void spit(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_FROGSPAWN_HATCH, SoundCategory.BLOCKS);

        Vec3i direction = world.getBlockState(pos).get(FACING).getVector();
        
        Vec3d spitPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        spitPos.add(0.5f, 0.5f, 0.5f); // Offset to center
        spitPos.add(direction.getX() / 2.0f, direction.getY() / 2.0f, direction.getZ() / 2.0f);

        ((ServerWorld) world).spawnParticles(ParticleTypes.SPIT, spitPos.getX(), spitPos.getY(), spitPos.getZ(), 1, 0, 0, 0, 0);
        world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.75f, pos.getZ() + 0.5f, new ItemStack(Items.EGG)));
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
        if (!world.isClient && state.get(CHARGED)) {
            spit(world, pos);
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        boolean charged = state.get(CHARGED);
        if (powered != state.get(POWERED) && !world.isClient) {
            if (powered && charged) {
                spit(world, pos);
                charged = false;
            }

            world.setBlockState(pos, state.with(POWERED, powered).with(CHARGED, charged));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1.0f, 0.75f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && state.get(CHARGED)) {
            spit(world, pos);
            world.setBlockState(pos, state.with(CHARGED, false));
        }

        return ActionResult.SUCCESS;
    }
}
