package com.github.charlyb01.xpstorage_trinkets;

import com.github.charlyb01.xpstorage_trinkets.config.ModConfig;
import dev.emi.trinkets.api.TrinketItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class XpstorageTrinkets implements ModInitializer {
    public static final Item.Settings settings = new Item.Settings().group(ItemGroup.MISC);

    public static final Item cristalisLazuli = new Item(settings);
    public static Item xp_conduit;
    public static Item xp_saver;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        Registry.register(Registry.ITEM, new Identifier("xp_storage_trinkets", "cristalis_lazuli"), cristalisLazuli);

        if (ModConfig.get().xpConduitAllowed) {
            xp_conduit = new TrinketItem(settings.maxDamage(ModConfig.get().xpConduitMaxDamage));
            Registry.register(Registry.ITEM, new Identifier("xp_storage_trinkets", "xp_conduit"), xp_conduit);
        }
        if (ModConfig.get().xpSaverAllowed) {
            xp_saver = new TrinketItem(settings.maxDamage(ModConfig.get().xpSaverMaxDamage));
            Registry.register(Registry.ITEM, new Identifier("xp_storage_trinkets", "xp_saver"), xp_saver);
        }
    }
}
