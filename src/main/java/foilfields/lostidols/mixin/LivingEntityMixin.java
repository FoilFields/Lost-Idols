package foilfields.lostidols.mixin;

import foilfields.lostidols.init.Particles;
import foilfields.lostidols.init.Statistics;
import foilfields.lostidols.init.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static foilfields.lostidols.init.Sounds.UNDYING_ACTIVATE;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Inject(at = @At("RETURN"), method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        if (!this.isInvulnerableTo(source) && !cir.getReturnValue()) {
            StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffects.UNDYING);

            if (((LivingEntity)(Object)this) instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)(Object)this;
                serverPlayerEntity.incrementStat(Statistics.UNDYING_IDOL);
            }

            if (statusEffectInstance != null) {
                this.setHealth(1.0F);
                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.REGENERATION, 100, 1));

                Vec3d position = this.getPos();
                if (!getWorld().isClient) {
                    ((ServerWorld) getWorld()).spawnParticles(Particles.UNDYING, position.getX(), position.getY(), position.getZ(), 20, 0.1, 0.1, 0.1, 1.0F);
                    getWorld().playSound(null, position.getX(), position.getY(), position.getZ(), UNDYING_ACTIVATE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                cir.setReturnValue(true);
            }
        }
    }
}
