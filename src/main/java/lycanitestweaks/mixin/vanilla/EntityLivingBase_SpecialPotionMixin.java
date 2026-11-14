package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lycanitestweaks.util.IPotionEffect_ExtraPropertiesMixin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBase_SpecialPotionMixin {

    @Shadow @Final private Map<Potion, PotionEffect> activePotionsMap;

    @Inject(
            method = "clearActivePotions",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onFinishedPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"),
            cancellable = true
    )
    private void lycanitesTweaks_vanillaEntityLivingBase_clearActivePotionsPurgeImmune(CallbackInfo ci, @Local PotionEffect effect){
        if(effect instanceof IPotionEffect_ExtraPropertiesMixin) {
            if(!((IPotionEffect_ExtraPropertiesMixin) effect).lycanitesTweaks$getPurgeable()) {
                ci.cancel();
            }
        }
    }

    @WrapOperation(
            method = "removePotionEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;removeActivePotionEffect(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;")
    )
    private PotionEffect lycanitesTweaks_vanillaEntityLivingBase_removePotionEffectCleanseImmune(EntityLivingBase instance, Potion potioneffectin, Operation<PotionEffect> original){
        PotionEffect potionEffect = instance.getActivePotionEffect(potioneffectin);
        if(potionEffect instanceof IPotionEffect_ExtraPropertiesMixin) {
            if(!((IPotionEffect_ExtraPropertiesMixin) potionEffect).lycanitesTweaks$getCleansable()) {
                return null;
            }
        }
        return original.call(instance, potioneffectin);
    }

    @Inject(
            method = "curePotionEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onFinishedPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"),
            cancellable = true
    )
    private void lycanitesTweaks_vanillaEntityLivingBase_curePotionEffectsCleanseImmune(CallbackInfo ci, @Local PotionEffect effect){
        if(effect instanceof IPotionEffect_ExtraPropertiesMixin) {
            if(!((IPotionEffect_ExtraPropertiesMixin) effect).lycanitesTweaks$getCleansable()) {
                ci.cancel();
            }
        }
    }
}
