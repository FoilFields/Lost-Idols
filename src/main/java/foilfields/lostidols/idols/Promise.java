package foilfields.lostidols.idols;

import foilfields.lostidols.init.Particles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Promise extends AbstractIdol {
    public Promise(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient && Math.random() < 0.1f) {
            Vec3d particlePosition = position.toCenterPos();
            ((ServerWorld) world).spawnParticles(Particles.FIREFLY, particlePosition.getX(), particlePosition.getY(), particlePosition.getZ(), 1, 5, 2, 5, 0.01);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(5D, 0D, 5D, 11D, 9D, 11D);
    }
}
