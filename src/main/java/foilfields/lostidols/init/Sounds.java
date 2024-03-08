package foilfields.lostidols.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class Sounds {

    public static SoundEvent MOAI_SPIT_EVENT = new SoundEvent(GetIdentifier("moai_spit"));
    public static SoundEvent MOAI_GROW_EVENT = new SoundEvent(GetIdentifier("moai_grow"));
    public static SoundEvent PROMISE_CHARGE = new SoundEvent(GetIdentifier("promise_charge"));
    public static SoundEvent UNDYING_CHARGE = new SoundEvent(GetIdentifier("undying_charge"));
    public static SoundEvent UNDYING_ACTIVATE = new SoundEvent(GetIdentifier("undying_activate"));
    public static SoundEvent SHULKER_CHARGE = new SoundEvent(GetIdentifier("shulker_charge"));
    public static SoundEvent SPHINX_CHARGE = new SoundEvent(GetIdentifier("sphinx_charge"));

    public static void init() {
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("moai_spit"), MOAI_SPIT_EVENT);
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("moai_grow"), MOAI_GROW_EVENT);
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("promise_charge"), PROMISE_CHARGE);
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("undying_charge"), UNDYING_CHARGE);
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("undying_activate"), UNDYING_ACTIVATE);
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("shulker_charge"), SHULKER_CHARGE);
        Registry.register(Registry.SOUND_EVENT, GetIdentifier("sphinx_charge"), SPHINX_CHARGE);
    }
}
