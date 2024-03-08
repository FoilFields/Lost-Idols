package foilfields.lostidols.init;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Statistics {
    public static final Identifier POP_MOAI_IDOL = GetIdentifier("pop_moai_idol");
    public static final Identifier UNDYING_IDOL = GetIdentifier("undying_idol_saves");

    public static void init() {
        Registry.register(Registry.CUSTOM_STAT, "pop_moai_idol", POP_MOAI_IDOL);
        Registry.register(Registry.CUSTOM_STAT, "undying_idol_saves", UNDYING_IDOL);
        Stats.CUSTOM.getOrCreateStat(POP_MOAI_IDOL, StatFormatter.DEFAULT);
        Stats.CUSTOM.getOrCreateStat(UNDYING_IDOL, StatFormatter.DEFAULT);
    }
}
