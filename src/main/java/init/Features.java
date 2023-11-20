package init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Features {
    public static final RegistryKey<PlacedFeature> DISK_FEATURE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, GetIdentifier("moai_beach"));

    public static void init() {
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_BEACH), GenerationStep.Feature.VEGETAL_DECORATION, DISK_FEATURE);
    }
}
