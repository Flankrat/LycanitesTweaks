package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviourLaser;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Based on EntityPlayer.attackTargetEntityWithCurrentItem
@Mixin(ProjectileBehaviourLaser.class)
public abstract class ProjectileBehaviourLaser_ProjectilesMeleeEnchMixin {

    @ModifyVariable(
            method = "updateDamage",
            at = @At(value = "STORE", ordinal = 0),
            name = "damage",
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageCalcByCreature(float damage, @Local(argsOnly = true) BaseProjectileEntity projectile, @Local(argsOnly = true) Entity target){
        if(projectile.getThrower() != null) {
            EntityLivingBase attacker = projectile.getThrower();
            if(target instanceof EntityLivingBase) {
                damage += EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), ((EntityLivingBase) target).getCreatureAttribute());
            }

            int fireEnchantDuration = EnchantmentHelper.getFireAspectModifier(attacker);
            if(fireEnchantDuration > 0)
                target.setFire(fireEnchantDuration * 4);
        }
        return damage;
    }

    @Inject(
            method = "updateDamage",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageKnockbackEnchantment(BaseProjectileEntity projectile, Entity target, CallbackInfoReturnable<Boolean> cir, @Local boolean attackSuccess) {
        if(!attackSuccess) return;

        if(projectile.getThrower() == null) return;
        EntityLivingBase attacker = projectile.getThrower();

        if(target instanceof EntityLivingBase) {
            int enchantmentKnockback = EnchantmentHelper.getKnockbackModifier(attacker);
            if (enchantmentKnockback > 0) {
                ((EntityLivingBase) target).knockBack(
                        attacker,
                        enchantmentKnockback * 0.5F,
                        MathHelper.sin(attacker.rotationYaw * (float) Math.PI / 180.F),
                        -MathHelper.cos(attacker.rotationYaw * (float) Math.PI / 180.F)
                );
            }
        }
        EnchantmentHelper.applyThornEnchantments(attacker, target);
        EnchantmentHelper.applyArthropodEnchantments(attacker, target);

        int fireEnchantDuration = EnchantmentHelper.getFireAspectModifier(attacker);
        if(fireEnchantDuration > 0)
            target.setFire(fireEnchantDuration * 4);
    }
}
