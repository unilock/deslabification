package cc.unilock.deslabification;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.SlabBlock;

import java.util.List;
import java.util.Map;

public class Deslabification {
    public static final String MOD_ID = "deslabification";

    public static void process(Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>>> map, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder, RecipeHolder<?> holder) {
        if (holder.value() instanceof ShapedRecipe recipe && recipe.getResultItem(null).getCount() == 6) {
            List<Ingredient> ingredients = recipe.getIngredients().stream().filter(i -> !i.isEmpty()).toList();
            if (ingredients.size() == 3 && ingredients.stream().allMatch(ingredients.get(0)::equals)) {
                if (recipe.getResultItem(null).getItem() instanceof BlockItem bi && bi.getBlock() instanceof SlabBlock slab) {
                    ItemStack input = ingredients.get(0).getItems()[0];

                    ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(input.getItem());
                    ResourceLocation slabId = BuiltInRegistries.BLOCK.getKey(slab);
                    ResourceLocation recipeId = new ResourceLocation(MOD_ID, blockId.getNamespace()+"_"+blockId.getPath() + "/" +slabId.getNamespace()+"_"+slabId.getPath());

                    RecipeHolder<ShapedRecipe> recipeHolder = new RecipeHolder<>(recipeId, new ShapedRecipe(
                            "",
                            CraftingBookCategory.BUILDING,
                            ShapedRecipePattern.of(
                                    Map.of(
                                            '#',
                                            Ingredient.of(slab)
                                    ),
                                    "##"
                            ),
                            input
                    ));

                    map.computeIfAbsent(RecipeType.CRAFTING, p -> ImmutableMap.builder()).put(recipeId, recipeHolder);
                    builder.put(recipeId, recipeHolder);
                }
            }
        }
    }
}
