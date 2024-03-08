package foilfields.lostidols.init;

import foilfields.lostidols.LostIdols;
import foilfields.lostidols.blockentity.IdolBlockEntity;
import foilfields.lostidols.idols.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Blocks {
    public static final Sphinx SPHINX_IDOL = new Sphinx(AbstractBlock.Settings.of(Material.STONE).mapColor(MapColor.GOLD).requiresTool().strength(3.5F));
    public static final Moai MOAI_IDOL = new Moai(AbstractBlock.Settings.of(Material.STONE).mapColor(MapColor.STONE_GRAY).requiresTool().strength(3.5F));
    public static final Bird BIRD_IDOL = new Bird(AbstractBlock.Settings.of(Material.STONE).mapColor(MapColor.DEEPSLATE_GRAY).requiresTool().strength(3.5F));
    public static final Promise PROMISE_IDOL = new Promise(AbstractBlock.Settings.of(Material.GLASS).mapColor(MapColor.LIGHT_GRAY).strength(0.3F).luminance((state) -> !state.get(Promise.CHARGED) ? 0 : 15).sounds(BlockSoundGroup.GLASS));
    public static final Undying UNDYING_IDOL = new Undying(AbstractBlock.Settings.of(Material.METAL).mapColor(MapColor.GOLD).requiresTool().strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL).luminance((state) -> !state.get(Undying.CHARGED) ? 2 : 6));
    public static final Shulker SHULKER_IDOL = new Shulker(AbstractBlock.Settings.of(Material.METAL).mapColor(MapColor.GOLD).requiresTool().strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL).luminance((state) -> !state.get(Undying.CHARGED) ? 0 : 3));
    public static final Attraction ATTRACTION_IDOL = new Attraction(AbstractBlock.Settings.of(Material.METAL).mapColor(MapColor.STONE_GRAY).requiresTool().strength(1.5F, 6.0F));
    public static final Repulsion REPULSION_IDOL = new Repulsion(AbstractBlock.Settings.of(Material.METAL).mapColor(MapColor.STONE_GRAY).requiresTool().strength(1.5F, 6.0F));
    public static final BlockEntityType<IdolBlockEntity> IDOL_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            GetIdentifier("idol_block_entity"),
            FabricBlockEntityTypeBuilder.create(IdolBlockEntity::new, SPHINX_IDOL, BIRD_IDOL, MOAI_IDOL, PROMISE_IDOL, UNDYING_IDOL, SHULKER_IDOL, ATTRACTION_IDOL, REPULSION_IDOL).build()
    );

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(GetIdentifier("lost_idols_group"))
            .icon(() -> new ItemStack(MOAI_IDOL))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(SPHINX_IDOL));
                stacks.add(new ItemStack(BIRD_IDOL));
                stacks.add(new ItemStack(MOAI_IDOL));
                stacks.add(new ItemStack(PROMISE_IDOL));
                stacks.add(new ItemStack(UNDYING_IDOL));
                stacks.add(new ItemStack(SHULKER_IDOL));
                stacks.add(new ItemStack(ATTRACTION_IDOL));
                stacks.add(new ItemStack(REPULSION_IDOL));
            })
            .build();

    public static void init() {
        Registry.register(Registry.BLOCK, GetIdentifier("sphinx_idol"), SPHINX_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("moai_idol"), MOAI_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("bird_idol"), BIRD_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("promise_idol"), PROMISE_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("undying_idol"), UNDYING_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("shulker_idol"), SHULKER_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("attraction_idol"), ATTRACTION_IDOL);
        Registry.register(Registry.BLOCK, GetIdentifier("repulsion_idol"), REPULSION_IDOL);

        Registry.register(Registry.ITEM, GetIdentifier("sphinx_idol"), new BlockItem(SPHINX_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("moai_idol"), new BlockItem(MOAI_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("bird_idol"), new BlockItem(BIRD_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("promise_idol"), new BlockItem(PROMISE_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("undying_idol"), new BlockItem(UNDYING_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("shulker_idol"), new BlockItem(SHULKER_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("attraction_idol"), new BlockItem(ATTRACTION_IDOL, new FabricItemSettings()));
        Registry.register(Registry.ITEM, GetIdentifier("repulsion_idol"), new BlockItem(REPULSION_IDOL, new FabricItemSettings()));
    }
}