package foilfields.lostidols.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

// Functional part of code is done through mixins on the player
public class UndyingEffect extends StatusEffect {
    public UndyingEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x98D982);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // Do nothing
    }
}