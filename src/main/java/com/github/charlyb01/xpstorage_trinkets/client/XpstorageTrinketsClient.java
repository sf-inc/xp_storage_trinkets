package com.github.charlyb01.xpstorage_trinkets.client;

import com.github.charlyb01.xpstorage_trinkets.XpstorageTrinkets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class XpstorageTrinketsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(XpstorageTrinkets.xp_saver, new Identifier("xp_storage_trinkets", "is_damaged"),
                (itemStack, clientWorld, livingEntity, i) -> (itemStack.getDamage() == itemStack.getMaxDamage() ? 1.0F : 0.0F));
        FabricModelPredicateProviderRegistry.register(XpstorageTrinkets.xp_conduit, new Identifier("xp_storage_trinkets", "is_damaged"),
                (itemStack, clientWorld, livingEntity, i) -> (itemStack.getDamage() == itemStack.getMaxDamage() ? 1.0F : 0.0F));
    }
}
