package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lycanitestweaks.util.IPotionEffect_ExtraPropertiesMixin;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionEffect.class)
public abstract class PotionEffect_ExtraPropertiesMixin implements IPotionEffect_ExtraPropertiesMixin {

    @Unique
    private boolean lycanitesTweaks$isCleansable = true;
    @Unique
    private boolean lycanitesTweaks$isPurgeable = true;
    @Unique
    private boolean lycanitesTweaks$isNeutral = false;

    @Inject(
            method = "combine",
            at = @At(value = "FIELD", target = "Lnet/minecraft/potion/PotionEffect;duration:I", ordinal = 0)
    )
    private void lycanitesTweaks_vanillaPotionEffect_combineAmplifier(PotionEffect other, CallbackInfo ci){
        if(other instanceof IPotionEffect_ExtraPropertiesMixin) {
            IPotionEffect_ExtraPropertiesMixin.lycanitesTweaks$setFromOther(this, (IPotionEffect_ExtraPropertiesMixin) other);
        }
    }

    @Inject(
            method = "combine",
            at = @At(value = "FIELD", target = "Lnet/minecraft/potion/PotionEffect;duration:I", ordinal = 4)
    )
    private void lycanitesTweaks_vanillaPotionEffect_combineDuration(PotionEffect other, CallbackInfo ci){
        if(other instanceof IPotionEffect_ExtraPropertiesMixin) {
            IPotionEffect_ExtraPropertiesMixin.lycanitesTweaks$setFromOther(this, (IPotionEffect_ExtraPropertiesMixin) other);
        }
    }

    @ModifyReturnValue(
            method = "toString",
            at = @At("RETURN")
    )
    private String lycanitesTweaks_vanillaPotionEffect_toString(String s){
        if (!this.lycanitesTweaks$isCleansable) {
            s = s + ", Cleansable: false";
        }

        if (!this.lycanitesTweaks$isPurgeable) {
            s = s + ", Purgeable: false";
        }

        if (this.lycanitesTweaks$isNeutral) {
            s = s + ", Neutral: true";
        }

        return s;
    }

    @ModifyReturnValue(
            method = "equals",
            at = @At("RETURN")
    )
    private boolean lycanitesTweaks_vanillaPotionEffect_equals(boolean original, Object other){
        if(original && other instanceof IPotionEffect_ExtraPropertiesMixin) {
            IPotionEffect_ExtraPropertiesMixin extraEffect = (IPotionEffect_ExtraPropertiesMixin) other;
            return this.lycanitesTweaks$isCleansable == extraEffect.lycanitesTweaks$getCleansable()
                    && this.lycanitesTweaks$isPurgeable == extraEffect.lycanitesTweaks$getPurgeable()
                    && this.lycanitesTweaks$isNeutral == extraEffect.lycanitesTweaks$getNeutral();
        }
        return original;
    }

    @ModifyReturnValue(
            method = "hashCode",
            at = @At("RETURN")
    )
    private int lycanitesTweaks_vanillaPotionEffect_hashCode(int original){
        int i = original;
        i = 31 * i + (this.lycanitesTweaks$isCleansable ? 1 : 0);
        i = 31 * i + (this.lycanitesTweaks$isPurgeable ? 1 : 0);
        i = 31 * i + (this.lycanitesTweaks$isNeutral ? 1 : 0);
        return i;
    }

    @Inject(
            method = "writeCustomPotionEffectToNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_vanillaPotionEffect_writeCustomPotionEffectToNBT(NBTTagCompound nbt, CallbackInfoReturnable<NBTTagCompound> cir){
        nbt.setBoolean("Cleansable", this.lycanitesTweaks$getCleansable());
        nbt.setBoolean("Purgeable", this.lycanitesTweaks$getPurgeable());
        nbt.setBoolean("Neutral", this.lycanitesTweaks$getNeutral());
    }

    @Override
    public boolean lycanitesTweaks$getCleansable() {
        return this.lycanitesTweaks$isCleansable;
    }

    @Override
    public boolean lycanitesTweaks$getPurgeable() {
        return this.lycanitesTweaks$isPurgeable;
    }

    @Override
    public boolean lycanitesTweaks$getNeutral() {
        return this.lycanitesTweaks$isNeutral;
    }

    @Override
    public PotionEffect lycanitesTweaks$setCleansable(boolean cleansable) {
        this.lycanitesTweaks$isCleansable = cleansable;
        return (PotionEffect) (Object) this;
    }

    @Override
    public PotionEffect lycanitesTweaks$setPurgeable(boolean purgeable) {
        this.lycanitesTweaks$isPurgeable = purgeable;
        return (PotionEffect) (Object) this;
    }

    @Override
    public PotionEffect lycanitesTweaks$setNeutral(boolean neutral) {
        this.lycanitesTweaks$isNeutral = neutral;
        return (PotionEffect) (Object) this;
    }
}
