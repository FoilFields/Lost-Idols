package foilfields.lostidols.client;

import foilfields.lostidols.client.particle.FireflyParticle;
import foilfields.lostidols.client.particle.StreamParticle;
import foilfields.lostidols.client.particle.UndyingParticle;
import foilfields.lostidols.init.Particles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SonicBoomParticle;
import net.minecraft.client.render.RenderLayer;

import static foilfields.lostidols.init.Blocks.PROMISE_IDOL;

@Environment(EnvType.CLIENT)
public class LostIdolsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(Particles.PHANTOM_RAY, SonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.STREAM, StreamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.UNDYING, UndyingParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FIREFLY, FireflyParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(PROMISE_IDOL, RenderLayer.getTranslucent());
    }
}
