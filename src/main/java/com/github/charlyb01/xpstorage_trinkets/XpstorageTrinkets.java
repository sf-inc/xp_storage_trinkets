package com.github.charlyb01.xpstorage_trinkets;

import com.github.charlyb01.xpstorage_trinkets.config.ModConfig;
import dev.emi.trinkets.api.TrinketItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class XpstorageTrinkets implements ModInitializer {
    public static final String MOD_ID = "xp_storage_trinkets";

    public static Item xp_conduit;
    public static Item xp_saver;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        if (ModConfig.get().xpConduitAllowed) {
            xp_conduit = new TrinketItem(new Item.Settings().maxDamage(ModConfig.get().xpConduitMaxDamage));
            Registry.register(Registries.ITEM, id("xp_conduit"), xp_conduit);
        }
        if (ModConfig.get().xpSaverAllowed) {
            xp_saver = new TrinketItem(new Item.Settings().maxDamage(ModConfig.get().xpSaverMaxDamage));
            Registry.register(Registries.ITEM, id("xp_saver"), xp_saver);
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            if (ModConfig.get().xpConduitAllowed) {
                entries.add(xp_conduit);
            }
            if (ModConfig.get().xpSaverAllowed) {
                entries.add(xp_saver);
            }
        });
    }

    public static Identifier id(final String path) {
        return Identifier.of(MOD_ID, path);
    }
}
