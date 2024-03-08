package foilfields.lostidols;

import foilfields.lostidols.init.*;
import net.fabricmc.api.ModInitializer;
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
