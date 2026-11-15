package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.CustomProjectileEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CustomProjectileEntity.class)
public abstract class CustomProjectileEntity_NameLookupMixin extends BaseProjectileEntity {

    public CustomProjectileEntity_NameLookupMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        else {
            String langKey = "entity." + this.entityName;
            if(!I18n.canTranslate(langKey)) {
                langKey += ".name";
            }
            return I18n.translateToLocal(langKey);
        }
    }

    @Unique
    @Override
    protected HoverEvent getHoverEvent() {
        HoverEvent hoverEvent = super.getHoverEvent();

        ResourceLocation mappedID = EntityList.getKey(this);
        if(mappedID != null) {
            String rawMessage = hoverEvent.getValue().getUnformattedComponentText();
            return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponentString(
                    rawMessage.replace(mappedID.getPath(), this.entityName)
            ));
        }

        return hoverEvent;
    }
}
