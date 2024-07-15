package cc.unilock.deslabification.mixin;

import cc.unilock.deslabification.Deslabification;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(method = "lambda$apply$0", at = @At(value = "NEW", target = "net/minecraft/world/item/crafting/RecipeHolder"))
    private static void lambda(CallbackInfo ci, @Local(argsOnly = true) ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder1, @Local(argsOnly = true) ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder2, @Local(ordinal = 0) Recipe<?> recipe) {
        Deslabification.process(builder1, builder2, recipe);
    }
}
