package foilfields.lostidols.idols;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Repulsion extends AbstractIdol {
    public static final BooleanProperty POWERED = Properties.POWERED;

    public Repulsion(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (!world.isClient && state.get(POWERED)) {
            Vec3d particlePosition = Vec3d.ofCenter(position);

            ((ServerWorld) world).spawnParticles(new DustParticleEffect(new Vec3f(0.0f, 0.0f, 1.0f), 1.0f), particlePosition.getX(), particlePosition.getY(), particlePosition.getZ(), 1, 0.25, 0.25, 0.25, 0.2);

            Random random = Random.create();
            for (int i = 0; i < 3; i++) {
                particlePosition = new Vec3d(12, 0, 0);
                particlePosition = particlePosition.rotateY(random.nextFloat() * 6.28318530718f);
                particlePosition = particlePosition.add(Vec3d.ofCenter(position));

                ((ServerWorld) world).spawnParticles(new DustParticleEffect(new Vec3f(0.0f, 0.0f, 1.0f), 1.0f), particlePosition.getX(), particlePosition.getY(), particlePosition.getZ(), 1, 0, 0, 0, 0);
            }

            Box area = new Box(new Vec3d(position.getX() - 12, position.getY() - 12, position.getZ() - 12), new Vec3d(position.getX() + 12, position.getY() + 12, position.getZ() + 12));

            for(Entity entity : world.getOtherEntities(null, area)) {
                if(!(entity instanceof MobEntity && entity.getPos().distanceTo(Vec3d.ofCenter(position)) < 12)) continue;
                if (random.nextFloat() < 0.3) ((ServerWorld) world).spawnParticles(new DustParticleEffect(new Vec3f(0.0f, 0.0f, 1.0f), 1.0f), entity.getX(), entity.getY(), entity.getZ(), 1, 0.25, 0.25, 0.25, 0.2);
                Vec3d target = entity.getPos().subtract(Vec3d.ofCenter(position)).normalize().multiply(15);
                target = target.add(Vec3d.ofCenter(position));
                ((MobEntity) entity).getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), 1);
            }
        }
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
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        world.setBlockState(pos, state.with(POWERED, world.isReceivingRedstonePower(pos)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1.0f, 0.75f);
    }
}
