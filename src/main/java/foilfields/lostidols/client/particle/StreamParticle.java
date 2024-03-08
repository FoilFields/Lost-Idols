package foilfields.lostidols.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class StreamParticle extends AnimatedParticle {

    StreamParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0);
        this.velocityMultiplier = 1.0F;
        this.velocityX = 0;
        this.velocityY = 0.5F;
        this.velocityZ = 0;
        this.maxAge = 5 + this.random.nextInt(5);
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new StreamParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

