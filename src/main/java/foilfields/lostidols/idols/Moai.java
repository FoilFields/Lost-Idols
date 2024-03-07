package foilfields.lostidols.idols;

import com.mojang.serialization.MapCodec;
import foilfields.lostidols.init.Sounds;
import foilfields.lostidols.init.Statistics;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
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

/**
 * Moai Idol is a block that grows an egg, which can be removed by destroying, interaction and redstone.
 */
public class Moai extends AbstractIdol {
    /**
     * Boolean property indicating whether the Moai block is powered.
     */
    public static final BooleanProperty POWERED = Properties.POWERED;

    /**
     * Constructs a Moai block with the given settings.
     *
     * @param settings The settings for the Moai block.
     */
    public Moai(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    /**
     * Called every game tick on the client and server side.
     *
     * @param state    The current state of the Moai block.
     * @param world    The world where the Moai block exists.
     * @param position The position of the Moai block in the world.
     */
    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        // Ignore
    }

    /**
     * Gets the block state to be set when the Moai block is placed.
     *
     * @param ctx The context of the item placement.
     * @return The block state to be set when placing the Moai block.
     */
    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    /**
     * Appends additional properties to the state manager for the Moai block.
     *
     * @param builder The state manager builder for the Moai block.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    /**
     * Returns whether the Moai block has random ticks.
     *
     * @param state The current state of the Moai block.
     * @return True.
     */
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    /**
     * Plays a sound and spawns particles and an item entity.
     *
     * @param world The world where the Moai block exists.
     * @param pos   The position of the Moai block in the world.
     */
    public void spit(World world, BlockPos pos) {
        world.playSound(null, pos, Sounds.MOAI_SPIT_EVENT, SoundCategory.BLOCKS);

        Vec3i direction = world.getBlockState(pos).get(FACING).getVector();

        Vec3d spitPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ())
                .add(0.5f, 0.5f, 0.5f)
                .add(direction.getX() / 2.0f, direction.getY() / 2.0f, direction.getZ() / 2.0f);

        ((ServerWorld) world).spawnParticles(ParticleTypes.SPIT, spitPos.getX(), spitPos.getY(), spitPos.getZ(), 1, 0, 0, 0, 0);

        ItemEntity itemEntity = new ItemEntity(world, spitPos.getX(), spitPos.getY(), spitPos.getZ(), new ItemStack(Items.EGG));
        itemEntity.setVelocity(direction.getX() / 3.0f, world.random.nextTriangular(0.2D, 0.05D), direction.getZ() / 3.0f);
        world.spawnEntity(itemEntity);
    }

    /**
     * Handles random ticks for the Moai block. Causes the Moai to grow an egg.
     *
     * @param state  The current state of the Moai block.
     * @param world  The world where the Moai block exists.
     * @param pos    The position of the Moai block in the world.
     * @param random A random number generator.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient && !state.get(CHARGED)) {
            world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            world.playSound(null, pos, Sounds.MOAI_GROW_EVENT, SoundCategory.BLOCKS);
        }

        super.randomTick(state, world, pos, random);
    }

    /**
     * Handles actions when the Moai block is broken. Spits out an egg if needed when broken.
     *
     * @param world  The world where the Moai block exists.
     * @param pos    The position of the Moai block in the world.
     * @param state  The current state of the Moai block.
     * @param player The player who broke the Moai block.
     * @return
     */
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && state.get(CHARGED)) {
            spit(world, pos);
            player.incrementStat(Statistics.POP_MOAI_IDOL);
        }

        return super.onBreak(world, pos, state, player);
    }

    /**
     * Handles neighbor updates for the Moai block. Specifically handles redstone powering.
     *
     * @param state       The current state of the Moai block.
     * @param world       The world where the Moai block exists.
     * @param pos         The position of the Moai block in the world.
     * @param sourceBlock The neighbor block that triggered the update.
     * @param sourcePos   The position of the neighbor block that triggered the update.
     * @param notify      Whether to notify neighbors of the update.
     */
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

    /**
     * Gets the outline shape of the Moai block.
     *
     * @param state   The current state of the Moai block.
     * @param view    The view of the Moai block.
     * @param pos     The position of the Moai block in the world.
     * @param context The shape context for the Moai block.
     * @return The outline shape of the Moai block.
     */
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1.0f, 0.75f);
    }

    /**
     * Handles actions when the Moai block is used. Spits the egg if
     *
     * @param state  The current state of the Moai block.
     * @param world  The world where the Moai block exists.
     * @param pos    The position of the Moai block in the world.
     * @param player The player using the Moai block.
     * @param hand   The hand used to activate the Moai block.
     * @param hit    The hit result of the activation.
     * @return The result of the Moai block activation.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && state.get(CHARGED)) {
            spit(world, pos);
            world.setBlockState(pos, state.with(CHARGED, false));
            player.incrementStat(Statistics.POP_MOAI_IDOL);
        }

        return ActionResult.SUCCESS;
    }
}
