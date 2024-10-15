package com.github.charlyb01.xpstorage_trinkets.data;

import com.github.charlyb01.xpstorage.Xpstorage;
import com.github.charlyb01.xpstorage_trinkets.XpStorageTrinkets;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, XpStorageTrinkets.xp_conduit)
                .pattern(" l ")
                .pattern("c c")
                .pattern(" c ")
                .input('l', Xpstorage.CRYSTALLIZED_LAPIS)
                .input('c', Items.COPPER_INGOT)
                .criterion(FabricRecipeProvider.hasItem(Xpstorage.CRYSTALLIZED_LAPIS),
                        FabricRecipeProvider.conditionsFromItem(Xpstorage.CRYSTALLIZED_LAPIS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, XpStorageTrinkets.xp_conduit)
                .input(Xpstorage.CRYSTALLIZED_LAPIS)
                .input(XpStorageTrinkets.xp_conduit)
                .criterion(FabricRecipeProvider.hasItem(XpStorageTrinkets.xp_conduit),
                        FabricRecipeProvider.conditionsFromItem(XpStorageTrinkets.xp_conduit))
                .offerTo(exporter, getItemRepairId(XpStorageTrinkets.xp_conduit));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, XpStorageTrinkets.xp_saver)
                .pattern("s s")
                .pattern(" s ")
                .pattern(" l ")
                .input('l', Xpstorage.CRYSTALLIZED_LAPIS)
                .input('s', Items.STRING)
                .criterion(FabricRecipeProvider.hasItem(Xpstorage.CRYSTALLIZED_LAPIS),
                        FabricRecipeProvider.conditionsFromItem(Xpstorage.CRYSTALLIZED_LAPIS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, XpStorageTrinkets.xp_saver)
                .input(Xpstorage.CRYSTALLIZED_LAPIS)
                .input(XpStorageTrinkets.xp_saver)
                .criterion(FabricRecipeProvider.hasItem(XpStorageTrinkets.xp_saver),
                        FabricRecipeProvider.conditionsFromItem(XpStorageTrinkets.xp_saver))
                .offerTo(exporter, getItemRepairId(XpStorageTrinkets.xp_saver));
    }

    private static Identifier getItemRepairId(final ItemConvertible item) {
        Identifier id = CraftingRecipeJsonBuilder.getItemId(item);
        return id.withSuffixedPath("_repair");
    }
}
