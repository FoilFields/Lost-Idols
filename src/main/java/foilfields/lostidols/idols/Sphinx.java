package foilfields.lostidols.idols;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Sphinx extends AbstractIdol {
    public Sphinx(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(World world, BlockPos position) {
        world.playSound(null, position, SoundEvents.ENTITY_CAT_PURREOW, SoundCategory.BLOCKS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.3f, 0f, 0.3f, 0.7f, 1.0f, 0.7f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(Text.of("Hello, world!"), false);
        }

        return ActionResult.SUCCESS;
    }
}