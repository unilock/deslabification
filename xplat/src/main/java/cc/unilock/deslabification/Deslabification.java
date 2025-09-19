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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Deslabification {
    public static final String MOD_ID = "deslabification";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Set<ResourceLocation> processed = new HashSet<>();

    public static void process(ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder1, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder2, ResourceLocation blockToSlabId, Recipe<?> recipe) {
        if (recipe instanceof ShapedRecipe shaped && shaped.getResultItem(null).getItem() instanceof BlockItem bi && bi.getBlock() instanceof SlabBlock slab && shaped.getResultItem(null).getCount() == 6) {
            List<Ingredient> ingredients = shaped.getIngredients().stream().filter(i -> !i.isEmpty()).toList();
            if (ingredients.size() == 3 && ingredients.stream().allMatch(ingredients.getFirst()::equals)) {
                ItemStack[] inputs = ingredients.getFirst().getItems();
                if (inputs.length == 0) {
                    LOGGER.error("[Deslabification] Attempted to process suspicious block-to-slab recipe, it will be skipped: "+blockToSlabId.toString());
                    return;
                }
                ItemStack input = inputs[0];

                ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(input.getItem());
                ResourceLocation slabId = BuiltInRegistries.BLOCK.getKey(slab);
                ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(MOD_ID, blockId.getNamespace()+"_"+blockId.getPath() + "/" +slabId.getNamespace()+"_"+slabId.getPath());

				if (processed.contains(recipeId)) {
					LOGGER.error("[Deslabification] Attempted to process duplicate block-to-slab recipe, it will be skipped: "+blockToSlabId.toString());
					return;
				}

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
				processed.add(recipeId);
            }
        }
    }

	public static void post() {
		LOGGER.info("[Deslabification] Added {} slab-to-block recipes", processed.size());
		processed.clear();
	}
}
