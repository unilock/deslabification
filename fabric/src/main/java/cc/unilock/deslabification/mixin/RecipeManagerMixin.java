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
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "NEW", target = "net/minecraft/world/item/crafting/RecipeHolder"))
    private void apply(CallbackInfo ci, @Local(ordinal = 0) ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder1, @Local(ordinal = 0) ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder2, @Local(ordinal = 0) Recipe<?> recipe) {
        Deslabification.process(builder1, builder2, recipe);
    }
}
