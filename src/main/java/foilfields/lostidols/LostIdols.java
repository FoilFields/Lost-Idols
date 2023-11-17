package foilfields.lostidols;

import foilfields.lostidols.blockentity.IdolBlockEntity;
import foilfields.lostidols.idols.Sphinx;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LostIdols implements ModInitializer {
    public static final Sphinx SPHINX_IDOL = new Sphinx(AbstractBlock.Settings.create().mapColor(MapColor.GOLD).instrument(Instrument.COW_BELL).requiresTool().strength(3.5F));

    public static final BlockEntityType<IdolBlockEntity> IDOL_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            GetIdentifier("idol_block_entity"),
            FabricBlockEntityTypeBuilder.create(IdolBlockEntity::new, SPHINX_IDOL).build()
    );

    @Override
    public void onInitialize() {

    }

    public static Identifier GetIdentifier(String name) {
        return new Identifier("lost_idols", name);
    }
}
