package foilfields.lostidols;

import foilfields.lostidols.blockentity.IdolBlockEntity;
import foilfields.lostidols.idols.Bird;
import foilfields.lostidols.idols.Jungle;
import foilfields.lostidols.idols.Moai;
import foilfields.lostidols.idols.Sphinx;
import foilfields.lostidols.init.Features;
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
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LostIdols implements ModInitializer {
    public static final Sphinx SPHINX_IDOL = new Sphinx(AbstractBlock.Settings.create().mapColor(MapColor.GOLD).instrument(Instrument.COW_BELL).requiresTool().strength(3.5F));
    public static final Moai MOAI_IDOL = new Moai(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.COW_BELL).requiresTool().strength(3.5F));
    public static final Bird BIRD_IDOL = new Bird(AbstractBlock.Settings.create().mapColor(MapColor.DEEPSLATE_GRAY).instrument(Instrument.COW_BELL).requiresTool().strength(3.5F));
    public static final Jungle JUNGLE_IDOL = new Jungle(AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(Instrument.COW_BELL).requiresTool().strength(3.5F));

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, GetIdentifier("lost_idols_group"));

    public static final BlockEntityType<IdolBlockEntity> IDOL_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            GetIdentifier("idol_block_entity"),
            FabricBlockEntityTypeBuilder.create(IdolBlockEntity::new, SPHINX_IDOL, BIRD_IDOL, MOAI_IDOL, JUNGLE_IDOL).build()
    );

    @Override
    public void onInitialize() {
        Features.init();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.lost_idols.lost_idols"))
                .icon(() -> new ItemStack(MOAI_IDOL))
                .build());

        Registry.register(Registries.BLOCK, GetIdentifier("sphinx_idol"), SPHINX_IDOL);
        Registry.register(Registries.BLOCK, GetIdentifier("moai_idol"), MOAI_IDOL);
        Registry.register(Registries.BLOCK, GetIdentifier("bird_idol"), BIRD_IDOL);
        Registry.register(Registries.BLOCK, GetIdentifier("jungle_idol"), JUNGLE_IDOL);
        Registry.register(Registries.ITEM, GetIdentifier("sphinx_idol"), new BlockItem(SPHINX_IDOL, new FabricItemSettings()));
        Registry.register(Registries.ITEM, GetIdentifier("moai_idol"), new BlockItem(MOAI_IDOL, new FabricItemSettings()));
        Registry.register(Registries.ITEM, GetIdentifier("bird_idol"), new BlockItem(BIRD_IDOL, new FabricItemSettings()));
        Registry.register(Registries.ITEM, GetIdentifier("jungle_idol"), new BlockItem(JUNGLE_IDOL, new FabricItemSettings()));

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register((entries -> {
            entries.add(SPHINX_IDOL);
            entries.add(BIRD_IDOL);
            entries.add(MOAI_IDOL);
            entries.add(JUNGLE_IDOL);
        }));
    }

    public static Identifier GetIdentifier(String name) {
        return new Identifier("lost_idols", name);
    }
}
