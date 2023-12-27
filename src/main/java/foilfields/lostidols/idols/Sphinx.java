package foilfields.lostidols.idols;

import foilfields.lostidols.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.joml.Vector3d;

import java.util.List;

import static foilfields.lostidols.init.Sounds.UNDYING_CHARGE;

public class Sphinx extends AbstractIdol {
    public Sphinx(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            Box area = new Box(new Vec3d(position.getX() - 50, position.getY() - 50, position.getZ() - 50), new Vec3d(position.getX() + 50, position.getY() + 50, position.getZ() + 50));
            List<Entity> entities = world.getOtherEntities(null, area);

            for(Entity ent: entities){
                if(ent instanceof LivingEntity livingEntity){
                    if (ent.getPos().distanceTo(position.toCenterPos()) > 50) continue;

                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 10, 5), null);
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

        if (!state.get(CHARGED) && itemStack.isOf(Items.GOLD_BLOCK)) {
            itemStack.decrement(1);

            if (!world.isClient()) {
                Vec3d center = pos.toCenterPos();
                world.playSound(null, center.getX(), center.getY(), center.getZ(), UNDYING_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                ((ServerWorld) world).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, center.getX(), center.getY(), center.getZ(), 10, 0, 0, 0, 0.1);
                world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
                Utils.ExplodeThrow(center, (ServerWorld) world, 10);
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
