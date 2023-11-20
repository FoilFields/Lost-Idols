package foilfields.lostidols.idols;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;
import java.util.logging.ConsoleHandler;

public class Jungle extends AbstractIdol {
    public Jungle(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED)) {
            Box area = new Box(new Vec3d(position.getX() - 10, position.getY() - 10, position.getZ() - 10), new Vec3d(position.getX() + 10, position.getY() + 10, position.getZ() + 10));
            List<Entity> entities = world.getOtherEntities(null, area);

            for(Entity ent: entities){
                if(ent instanceof MobEntity){
                    ((MobEntity) ent).getNavigation().startMovingTo(position.getX(), position.getY(), position.getZ(), 1);
                }
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.3f, 0f, 0.3f, 0.7f, 1.0f, 0.7f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && player.getStackInHand(hand).getItem().equals(Items.FLINT_AND_STEEL) && CheckForCandles(state, world, pos)) {
            world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS);
            Vec3d dir = new Vec3d(player.getPos().x - pos.getX(), 2, player.getPos().z - pos.getZ()).normalize();
            player.velocityModified = true;
            player.setVelocity(dir);
            ((ServerWorld) world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX(), pos.getY(), pos.getZ(), 20, 0.5, 1.5, 0.5, 0.5);
        }
        return ActionResult.SUCCESS;
    }

    public boolean CheckForCandles(BlockState state, World world, BlockPos pos){
        BlockState topleft_bs = world.getBlockState(pos.add(-1, 0, 1));
        BlockState topright_bs = world.getBlockState(pos.add(1, 0, 1));
        BlockState bottomleft_bs = world.getBlockState(pos.add(-1, 0, -1));
        BlockState bottomright_bs = world.getBlockState(pos.add(1, 0, -1));

        if(CandleBlock.isLitCandle(topleft_bs) && CandleBlock.isLitCandle(topright_bs) && CandleBlock.isLitCandle(bottomleft_bs) && CandleBlock.isLitCandle(bottomright_bs)) {
            return true;
        }
        return false;
    }
}
