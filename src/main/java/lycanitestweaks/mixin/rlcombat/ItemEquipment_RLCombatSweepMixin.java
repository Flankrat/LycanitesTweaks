package lycanitestweaks.mixin.rlcombat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_RLCombatSweepMixin extends ItemBase {

    @ModifyExpressionValue(
            method = "hitEntity",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z")
    )
    private boolean lycanitesTweaks_lycanitesItemEquipment_hitEntityOriginalSweep(boolean original, @Local(name = "attacker") EntityLivingBase attacker){
        // TODO Structure Item.hitEntity for non Players in a not stupid way
        return original || attacker instanceof EntityPlayer; // RLCombat for players
    }
}
