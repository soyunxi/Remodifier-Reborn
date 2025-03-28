package org.yunxi.remodifier.plugin.jei;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.yunxi.remodifier.Remodifier;

@SuppressWarnings("removal")
@JeiPlugin
public class remodifierJei implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("remodifier", "jei");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(Remodifier.MODIFIER_BOOK.get());
    }
}
