package com.github.charlyb01.xpstorage_trinkets.data;

import com.github.charlyb01.xpstorage_trinkets.item.ItemRegistry;
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

import static com.github.charlyb01.xpstorage.item.ItemRegistry.CRYSTALLIZED_LAPIS;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.XP_CONDUIT)
                .pattern(" l ")
                .pattern("c c")
                .pattern(" c ")
                .input('l', CRYSTALLIZED_LAPIS)
                .input('c', Items.COPPER_INGOT)
                .criterion(FabricRecipeProvider.hasItem(CRYSTALLIZED_LAPIS),
                        FabricRecipeProvider.conditionsFromItem(CRYSTALLIZED_LAPIS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.XP_CONDUIT)
                .input(CRYSTALLIZED_LAPIS)
                .input(ItemRegistry.XP_CONDUIT)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.XP_CONDUIT),
                        FabricRecipeProvider.conditionsFromItem(ItemRegistry.XP_CONDUIT))
                .offerTo(exporter, getItemRepairId(ItemRegistry.XP_CONDUIT));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.XP_SAVER)
                .pattern("s s")
                .pattern(" s ")
                .pattern(" l ")
                .input('l', CRYSTALLIZED_LAPIS)
                .input('s', Items.STRING)
                .criterion(FabricRecipeProvider.hasItem(CRYSTALLIZED_LAPIS),
                        FabricRecipeProvider.conditionsFromItem(CRYSTALLIZED_LAPIS))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.XP_SAVER)
                .input(CRYSTALLIZED_LAPIS)
                .input(ItemRegistry.XP_SAVER)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.XP_SAVER),
                        FabricRecipeProvider.conditionsFromItem(ItemRegistry.XP_SAVER))
                .offerTo(exporter, getItemRepairId(ItemRegistry.XP_SAVER));
    }

    private static Identifier getItemRepairId(final ItemConvertible item) {
        Identifier id = CraftingRecipeJsonBuilder.getItemId(item);
        return id.withSuffixedPath("_repair");
    }
}
