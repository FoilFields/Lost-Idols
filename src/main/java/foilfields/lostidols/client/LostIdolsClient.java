package foilfields.lostidols.client;

import foilfields.lostidols.LostIdols;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class LostIdolsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Set the render layer for each ray block to translucent.
        BlockRenderLayerMap.INSTANCE.putBlock(LostIdols.SPHINX_IDOL, RenderLayer.getTranslucent());
    }
}
