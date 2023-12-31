package foilfields.lostidols.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Sounds {
    public static SoundEvent MOAI_SPIT_EVENT = SoundEvent.of(GetIdentifier("moai_spit"));
    public static SoundEvent MOAI_GROW_EVENT = SoundEvent.of(GetIdentifier("moai_grow"));
    public static SoundEvent PROMISE_CHARGE = SoundEvent.of(GetIdentifier("promise_charge"));
    public static SoundEvent UNDYING_CHARGE = SoundEvent.of(GetIdentifier("undying_charge"));
    public static SoundEvent UNDYING_ACTIVATE = SoundEvent.of(GetIdentifier("undying_activate"));
    public static SoundEvent SHULKER_CHARGE = SoundEvent.of(GetIdentifier("shulker_charge"));
    public static SoundEvent SPHINX_CHARGE = SoundEvent.of(GetIdentifier("sphinx_charge"));

    public static void init() {
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("moai_spit"), MOAI_SPIT_EVENT);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("moai_grow"), MOAI_GROW_EVENT);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("promise_charge"), PROMISE_CHARGE);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("undying_charge"), UNDYING_CHARGE);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("undying_activate"), UNDYING_ACTIVATE);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("shulker_charge"), SHULKER_CHARGE);
        Registry.register(Registries.SOUND_EVENT, GetIdentifier("sphinx_charge"), SPHINX_CHARGE);
    }
}
