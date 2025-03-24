package cc.unilock.deslabification;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.SlabBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Deslabification {
    public static final String MOD_ID = "deslabification";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void process(ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder1, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder2, Recipe<?> recipe) {
        if (recipe instanceof ShapedRecipe shaped && shaped.getResultItem(null).getItem() instanceof BlockItem bi && bi.getBlock() instanceof SlabBlock slab && shaped.getResultItem(null).getCount() == 6) {
            List<Ingredient> ingredients = shaped.getIngredients().stream().filter(i -> !i.isEmpty()).toList();
            if (ingredients.size() == 3 && ingredients.stream().allMatch(ingredients.getFirst()::equals)) {
                ItemStack[] inputs = ingredients.getFirst().getItems();
                if (inputs.length == 0) {
                    LOGGER.error("[Deslabification] Attempted to process suspicious block-to-slab recipe, it will be skipped: "+recipe.getId());
                    return;
                }
                ItemStack input = inputs[0];

                ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(input.getItem());
                ResourceLocation slabId = BuiltInRegistries.BLOCK.getKey(slab);
                ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(MOD_ID, blockId.getNamespace()+"_"+blockId.getPath() + "/" +slabId.getNamespace()+"_"+slabId.getPath());

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

                builder1.put(RecipeType.CRAFTING, recipeHolder);
                builder2.put(recipeId, recipeHolder);
            }
        }
    }
}
