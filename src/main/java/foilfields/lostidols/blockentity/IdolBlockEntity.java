package foilfields.lostidols.blockentity;

import foilfields.lostidols.LostIdols;
import foilfields.lostidols.idols.AbstractIdol;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static foilfields.lostidols.init.Blocks.IDOL_BLOCK_ENTITY;

public class IdolBlockEntity extends BlockEntity {
    public IdolBlockEntity(BlockPos pos, BlockState state) {
        super(IDOL_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos position, BlockState state, IdolBlockEntity be) {
        AbstractIdol idol = ((AbstractIdol) state.getBlock());
        idol.tick(state, world, position);
    }
}
