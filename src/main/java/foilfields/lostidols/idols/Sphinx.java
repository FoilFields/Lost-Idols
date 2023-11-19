package foilfields.lostidols.idols;

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
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

public class Sphinx extends AbstractIdol {
    public Sphinx(Settings settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos position) {
        if (state.get(CHARGED) && !world.isClient) {
            //world.playSound(null, position, SoundEvents.ENTITY_CAT_PURREOW, SoundCategory.BLOCKS);
            Box area = new Box(new Vec3d(position.getX() - 50, position.getY() - 50, position.getZ() - 50), new Vec3d(position.getX() + 50, position.getY() + 50, position.getZ() + 50));
            List<Entity> entities = world.getOtherEntities(null, area);

            for(Entity ent: entities){
                if(ent instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity) ent;
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 10, 0), null);
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
        if (!world.isClient && player.getStackInHand(hand).getItem().equals(Items.FLINT_AND_STEEL)) {
            world.setBlockState(pos, state.cycle(CHARGED), Block.NOTIFY_LISTENERS);
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS);
            Vec3d dir = new Vec3d(player.getPos().x - pos.getX(), 2, player.getPos().z - pos.getZ()).normalize();
            player.velocityModified = true;
            player.setVelocity(dir);
        }

        return ActionResult.SUCCESS;
    }
}
