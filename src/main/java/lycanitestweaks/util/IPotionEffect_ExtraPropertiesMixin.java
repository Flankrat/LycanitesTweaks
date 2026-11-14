package lycanitestweaks.util;

import net.minecraft.potion.PotionEffect;

public interface IPotionEffect_ExtraPropertiesMixin {

    boolean lycanitesTweaks$getCleansable();
    boolean lycanitesTweaks$getPurgeable();
    boolean lycanitesTweaks$getNeutral();

    PotionEffect lycanitesTweaks$setCleansable(boolean cleansable);
    PotionEffect lycanitesTweaks$setPurgeable(boolean purgeable);
    PotionEffect lycanitesTweaks$setNeutral(boolean neutral);

    static void lycanitesTweaks$setFromOther(IPotionEffect_ExtraPropertiesMixin current, IPotionEffect_ExtraPropertiesMixin other) {
        current.lycanitesTweaks$setCleansable(other.lycanitesTweaks$getCleansable());
        current.lycanitesTweaks$setPurgeable(other.lycanitesTweaks$getPurgeable());
        current.lycanitesTweaks$setNeutral(other.lycanitesTweaks$getNeutral());
    }
}
