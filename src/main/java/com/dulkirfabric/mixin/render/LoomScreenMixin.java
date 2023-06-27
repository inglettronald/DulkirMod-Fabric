package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.InventoryScale;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {
    @Inject(method = "drawBanner", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onCreateMatrix(DrawContext context, RegistryEntry<BannerPattern> pattern, int x, int y, CallbackInfo ci, NbtCompound nbtCompound, NbtList nbtList, ItemStack itemStack, MatrixStack matrixStack) {
        matrixStack.scale(InventoryScale.INSTANCE.getScale(), InventoryScale.INSTANCE.getScale(), 1F);
    }
}

