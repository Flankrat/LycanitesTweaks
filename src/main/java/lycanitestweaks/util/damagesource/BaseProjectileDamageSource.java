package lycanitestweaks.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class BaseProjectileDamageSource extends EntityDamageSourceIndirect {

    public BaseProjectileDamageSource(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
        super(damageTypeIn, source, indirectEntityIn);
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
        // Modify super for compat with Scaling Health Issues
        Entity indirectKiller = this.getImmediateSource();
        String originalDamageType = this.damageType;
        if(indirectKiller instanceof EntityThrowable) {
            this.damageType = "thrown";
        }
        ITextComponent baseMessage = super.getDeathMessage(entityLivingBaseIn);
        this.damageType = originalDamageType;

        if(this.getTrueSource() != null) {
            TextComponentTranslation translatedMessage = null;
            if(baseMessage instanceof TextComponentTranslation) {
                translatedMessage = (TextComponentTranslation) baseMessage;
            }
            else {
                // Mistake in SHI, accidental wrapping
                for(ITextComponent component : baseMessage.getSiblings()) {
                    if(component instanceof TextComponentTranslation) {
                        translatedMessage = (TextComponentTranslation) component;
                        break;
                    }
                }
            }
            if(translatedMessage != null) {
                if(translatedMessage.getFormatArgs().length > 1) {
                    if(indirectKiller != null && translatedMessage.getFormatArgs()[1] instanceof ITextComponent) {
                        ITextComponent killerDisplayName = (ITextComponent) translatedMessage.getFormatArgs()[1];
                        killerDisplayName.appendText(" [").appendSibling(indirectKiller.getDisplayName()).appendText("]");
                    }
                }
            }
        }
        return baseMessage;
    }
}
