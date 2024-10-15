package com.github.charlyb01.xpstorage_trinkets.item;

import com.github.charlyb01.xpstorage_trinkets.XpStorageTrinkets;
import com.github.charlyb01.xpstorage_trinkets.config.ModConfig;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItemRegistry {
    public static final Item XP_CONDUIT = new TrinketItem(new Item.Settings().maxDamage(ModConfig.get().xpConduitMaxDamage));
    public static final Item XP_SAVER = new TrinketItem(new Item.Settings().maxDamage(ModConfig.get().xpSaverMaxDamage));

    public static void init() {
        Registry.register(Registries.ITEM, XpStorageTrinkets.id("xp_conduit"), XP_CONDUIT);
        Registry.register(Registries.ITEM, XpStorageTrinkets.id("xp_saver"), XP_SAVER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(XP_CONDUIT);
            entries.add(XP_SAVER);
        });
    }
}
