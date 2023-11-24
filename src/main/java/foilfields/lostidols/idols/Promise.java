package foilfields.lostidols.idols;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Promise extends AbstractIdol {
    protected Promise(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {

    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(5D, 0D, 5D, 11D, 10D, 11D);
    }
}
