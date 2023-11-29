package foilfields.lostidols;

import foilfields.lostidols.blockentity.IdolBlockEntity;
import foilfields.lostidols.idols.*;
import foilfields.lostidols.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LostIdols implements ModInitializer {
    @Override
    public void onInitialize() {
        Blocks.init();
        Features.init();
        Sounds.init();
        Statistics.init();
        Particles.init();
        StatusEffects.init();
    }

    public static Identifier GetIdentifier(String name) {
        return new Identifier("lost_idols", name);
    }
}
