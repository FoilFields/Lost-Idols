package foilfields.lostidols;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Utils {
    public static void ExplodeThrow(Vec3d origin, ServerWorld world, float radius) {
        Box area = new Box(new Vec3d(origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius), new Vec3d(origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius));
        List<Entity> entities = world.getOtherEntities(null, area);

        for(Entity ent: entities){
            if(ent instanceof LivingEntity livingEntity){
                double effect = 1 - (ent.getPos().distanceTo(origin) / radius);
                if (effect <= 0) continue;

                world.spawnParticles(ParticleTypes.EXPLOSION, origin.getX(), origin.getY(), origin.getZ(), 1, 0, 0, 0, 0);
                Vec3d dir = new Vec3d(livingEntity.getPos().x - origin.getX(), 2, livingEntity.getPos().z - origin.getZ()).normalize();
                livingEntity.velocityModified = true;
                livingEntity.setVelocity(dir.multiply(effect));
            }
        }
    }
}
