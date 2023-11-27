package foilfields.lostidols.mixin;

import foilfields.lostidols.init.StatusEffects;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(at = @At("RETURN"), method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (!source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && !cir.getReturnValue()) {
            StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffects.UNDYING);

            if (statusEffectInstance != null) {
                this.setHealth(1.0F);
                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.REGENERATION, 900, 1));
                this.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.ABSORPTION, 100, 1));
                this.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE, 800, 0));
                cir.setReturnValue(true);
            }
        }
    }
}
