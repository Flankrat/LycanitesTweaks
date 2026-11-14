package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BaseProjectileEntity.class)
public abstract class BaseProjectileEntity_ProjectilesMeleeEnchMixin extends EntityThrowable {

    public BaseProjectileEntity_ProjectilesMeleeEnchMixin(World world) {
        super(world);
    }

    @ModifyVariable(
            method = "onImpact",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;pierce:I", remap = false),
            name = "damage"
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageCalcByCreature(float damage, @Local EntityLivingBase target){
        if(this.getThrower() instanceof EntityLivingBase) {
            damage += EnchantmentHelper.getModifierForCreature(this.getThrower().getHeldItemMainhand(), target.getCreatureAttribute());
        }
        return damage;
    }

    @WrapOperation(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;onDamage(Lnet/minecraft/entity/EntityLivingBase;FZ)V", remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageKnockbackEnchantment(BaseProjectileEntity instance, EntityLivingBase target, float damage, boolean attackSuccess, Operation<Void> original){
        original.call(instance, target, damage, attackSuccess);
        if(!attackSuccess) return;

        if(this.getThrower() == null) return;
        EntityLivingBase attacker = this.getThrower();

        int enchantmentKnockback = EnchantmentHelper.getKnockbackModifier(attacker);
        if(enchantmentKnockback > 0) {
            target.knockBack(
                    attacker,
                    enchantmentKnockback * 0.5F,
                    MathHelper.sin(attacker.rotationYaw * (float) Math.PI / 180.F),
                    -MathHelper.cos(attacker.rotationYaw * (float) Math.PI / 180.F)
            );
        }
        // Fire Enchanted Held Weapons:
        int fireEnchantDuration = EnchantmentHelper.getFireAspectModifier(attacker);
        if(fireEnchantDuration > 0)
            target.setFire(fireEnchantDuration * 4);
        this.applyEnchantments(attacker, target);
    }
}
