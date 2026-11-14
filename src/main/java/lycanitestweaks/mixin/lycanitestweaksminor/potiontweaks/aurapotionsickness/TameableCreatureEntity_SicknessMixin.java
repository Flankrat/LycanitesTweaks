package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks.aurapotionsickness;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.util.IPotionEffect_ExtraPropertiesMixin;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_SicknessMixin {

    @ModifyArg(
            method = "ownerEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsTameableCreatureEntity_ownerEffectsNoPotionSickness(PotionEffect potioneffectIn){
        if(potioneffectIn instanceof IPotionEffect_ExtraPropertiesMixin) {
            ((IPotionEffect_ExtraPropertiesMixin) potioneffectIn).lycanitesTweaks$setNeutral(true);
        }
        return potioneffectIn;
    }
}
