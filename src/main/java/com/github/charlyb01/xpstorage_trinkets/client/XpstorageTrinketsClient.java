package com.github.charlyb01.xpstorage_trinkets.client;

import com.github.charlyb01.xpstorage_trinkets.XpstorageTrinkets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class XpstorageTrinketsClient implements ClientModInitializer {
    public static final Identifier IS_DAMAGED_PREDICATE_ID = XpstorageTrinkets.id("is_damaged");

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(XpstorageTrinkets.xp_saver, IS_DAMAGED_PREDICATE_ID,
                (itemStack, clientWorld, livingEntity, i) -> (itemStack.getDamage() == itemStack.getMaxDamage() ? 1.0F : 0.0F));
        ModelPredicateProviderRegistry.register(XpstorageTrinkets.xp_conduit, IS_DAMAGED_PREDICATE_ID,
                (itemStack, clientWorld, livingEntity, i) -> (itemStack.getDamage() == itemStack.getMaxDamage() ? 1.0F : 0.0F));
    }
}
