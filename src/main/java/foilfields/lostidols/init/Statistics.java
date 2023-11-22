package foilfields.lostidols.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Statistics {
    public static final Identifier POP_MOAI_IDOL = GetIdentifier("pop_moai_idol");

    public static void init() {
        Registry.register(Registries.CUSTOM_STAT, "pop_moai_idol", POP_MOAI_IDOL);
        Stats.CUSTOM.getOrCreateStat(POP_MOAI_IDOL, StatFormatter.DEFAULT);
    }
}
