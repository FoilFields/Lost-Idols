package foilfields.lostidols.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Sounds {
    public static SoundEvent MOAI_SPIT_EVENT = SoundEvent.of(GetIdentifier("moai_spit"));
    public static SoundEvent MOAI_GROW_EVENT = SoundEvent.of(GetIdentifier("moai_grow"));

    public static void init() {
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("moai_spit"), MOAI_SPIT_EVENT);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("moai_grow"), MOAI_GROW_EVENT);
    }
}
