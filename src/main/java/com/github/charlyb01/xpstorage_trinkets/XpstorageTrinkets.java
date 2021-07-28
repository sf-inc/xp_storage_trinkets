package com.github.charlyb01.xpstorage_trinkets;

import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class XpstorageTrinkets implements ModInitializer {
    public static final Item.Settings settings = new Item.Settings().group(ItemGroup.MISC);

    public static final Item cristalisLazuli = new Item(settings);
    public static final Item xp_saver = new TrinketItem(settings.maxDamage(1));
    public static final Item xp_conduit = new TrinketItem(settings.maxDamage(1000));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("xp_storage_trinkets", "cristalis_lazuli"), cristalisLazuli);
        Registry.register(Registry.ITEM, new Identifier("xp_storage_trinkets", "xp_saver"), xp_saver);
        Registry.register(Registry.ITEM, new Identifier("xp_storage_trinkets", "xp_conduit"), xp_conduit);
    }
}
