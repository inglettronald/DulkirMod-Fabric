package com.dulkirfabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(YggdrasilMinecraftSessionService.class)
public class YggdrasilMinecraftSessionServiceMixin {

    @ModifyExpressionValue(
            method = "getPropertySignatureState",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/authlib/properties/Property;hasSignature()Z"
            ),
            remap = false
    )
    private boolean fixBlankSignatureLogSpam(boolean original, @Local(argsOnly = true) Property property) {
        if (property.hasSignature() && property.signature().isEmpty()) {
            return false;
        }
        return original;
    }
}
