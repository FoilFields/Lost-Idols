package foilfields.lostidols.init;

import foilfields.lostidols.LostIdols;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class Particles {
    public static final DefaultParticleType PHANTOM_RAY = FabricParticleTypes.simple();
    public static final DefaultParticleType FIREFLY = FabricParticleTypes.simple();
    public static final DefaultParticleType UNDYING = FabricParticleTypes.simple();
    public static final DefaultParticleType STREAM = FabricParticleTypes.simple();

    public static void init() {
        Registry.register(Registry.PARTICLE_TYPE, LostIdols.GetIdentifier("phantom_ray"), PHANTOM_RAY);
        Registry.register(Registry.PARTICLE_TYPE, LostIdols.GetIdentifier("firefly"), FIREFLY);
        Registry.register(Registry.PARTICLE_TYPE, LostIdols.GetIdentifier("undying"), UNDYING);
        Registry.register(Registry.PARTICLE_TYPE, LostIdols.GetIdentifier("stream"), STREAM);
    }
}
