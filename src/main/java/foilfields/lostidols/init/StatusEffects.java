package foilfields.lostidols.init;

import foilfields.lostidols.effects.UndyingEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;

import static foilfields.lostidols.LostIdols.GetIdentifier;

public class StatusEffects {
    public static final StatusEffect UNDYING = new UndyingEffect();
    public static void init() {

        Registry.register(Registry.STATUS_EFFECT, GetIdentifier("undying"), UNDYING);
    }
}
