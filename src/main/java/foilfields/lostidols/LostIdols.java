package foilfields.lostidols;

import foilfields.lostidols.blockentity.IdolBlockEntity;
import foilfields.lostidols.idols.Sphinx;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LostIdols implements ModInitializer {
    public static final Sphinx SPHINX_IDOL = new Sphinx(AbstractBlock.Settings.create().mapColor(MapColor.GOLD).instrument(Instrument.COW_BELL).requiresTool().strength(3.5F));

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, GetIdentifier("lost_idols_group"));

    public static final BlockEntityType<IdolBlockEntity> IDOL_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            GetIdentifier("idol_block_entity"),
            FabricBlockEntityTypeBuilder.create(IdolBlockEntity::new, SPHINX_IDOL).build()
    );

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.lost_idols.lost_idols"))
                .icon(() -> new ItemStack(SPHINX_IDOL))
                .build());

        Registry.register(Registries.BLOCK, GetIdentifier("sphinx_idol"), SPHINX_IDOL);
        Registry.register(Registries.ITEM, GetIdentifier("sphinx_idol"), new BlockItem(SPHINX_IDOL, new FabricItemSettings()));

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register((entries -> {
            entries.add(SPHINX_IDOL);
        }));
    }

    public static Identifier GetIdentifier(String name) {
        return new Identifier("lost_idols", name);
    }
}
