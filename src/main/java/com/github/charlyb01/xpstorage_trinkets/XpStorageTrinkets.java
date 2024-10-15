package com.github.charlyb01.xpstorage_trinkets;

import com.github.charlyb01.xpstorage_trinkets.config.ModConfig;
import com.github.charlyb01.xpstorage_trinkets.item.ItemRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class XpStorageTrinkets implements ModInitializer {
    public static final String MOD_ID = "xp_storage_trinkets";

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        ItemRegistry.init();
    }

    public static Identifier id(final String path) {
        return Identifier.of(MOD_ID, path);
    }
}
