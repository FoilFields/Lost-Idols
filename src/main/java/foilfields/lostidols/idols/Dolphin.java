package foilfields.lostidols.idols;

import com.mojang.serialization.MapCodec;
import foilfields.lostidols.Utils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

import static foilfields.lostidols.init.Sounds.SPHINX_CHARGE;

public class Dolphin extends AbstractIdol {

    public Dolphin(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            Box area = new Box(new Vec3d(position.getX() - 50, position.getY() - 50, position.getZ() - 50), new Vec3d(position.getX() + 50, position.getY() + 50, position.getZ() + 50));
            List<Entity> entities = world.getOtherEntities(null, area);

            Random random = Random.create();

            Vec3d center = position.toCenterPos();
            if (random.nextFloat() < 0.02f) ((ServerWorld) world).spawnParticles(ParticleTypes.DOLPHIN, center.getX(), center.getY(), center.getZ(), 1, 0.25, 0.25, 0.25, 0.2);

            for(Entity ent: entities){
                if(ent instanceof LivingEntity livingEntity){
                    if (ent.getPos().distanceTo(position.toCenterPos()) > 50) continue;

                    if (random.nextFloat() < 0.01f) ((ServerWorld) world).spawnParticles(ParticleTypes.DOLPHIN, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1, 0.1, 0.1, 0.1, 0);
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 10, 0, false, false, true), null);
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
        ItemStack itemStack = player.getStackInHand(hand);

        if (!state.get(CHARGED) && itemStack.isOf(Items.NAUTILUS_SHELL) && state.get(WATERLOGGED)) {
            itemStack.decrement(1);

            if (!world.isClient()) {
                Vec3d center = pos.toCenterPos();

                world.playSound(null, center.getX(), center.getY(), center.getZ(), SPHINX_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GOLD_BLOCK.getDefaultState());
                ((ServerWorld) world).spawnParticles(particleEffect, center.getX(), center.getY(), center.getZ(), 10, 0.25, 0.25, 0.25, 0);

                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                Utils.ExplodeThrow(center, (ServerWorld) world, 10);
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
