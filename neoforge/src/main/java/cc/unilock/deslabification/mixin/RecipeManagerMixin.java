package cc.unilock.deslabification.mixin;

import cc.unilock.deslabification.Deslabification;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(method = "lambda$apply$1", at = @At("HEAD"))
    private static void lambda(CallbackInfo ci, @Local(argsOnly = true) Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>>> map, @Local(argsOnly = true) ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder, @Local(argsOnly = true) RecipeHolder<?> holder) {
        Deslabification.process(map, builder, holder);
    }
}
