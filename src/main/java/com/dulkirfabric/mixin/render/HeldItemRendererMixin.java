package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


// SOURCE FOR A LOT OF THIS: https://github.com/cosrnic/smallviewmodel/blob/main/src/main/java/uk/cosrnic/smallviewmodel/mixin/MixinHeldItemRenderer.java
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Inject(method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void onRenderHeldItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (hand == Hand.MAIN_HAND) {
            float rotX = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemRotX();
            float rotY = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemRotY();
            float rotZ = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemRotZ();

            float posX = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemPosX() / 1000f;
            float posY = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemPosY() / 1000f;
            float posZ = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemPosZ() / 1000f;

            float scale = DulkirConfig.ConfigVars.getConfigOptions().getHeldItemScale();

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotX));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotY));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotZ));
            matrices.scale(scale, scale, scale);
            matrices.translate(posX, posY, posZ);
        }
    }
}
