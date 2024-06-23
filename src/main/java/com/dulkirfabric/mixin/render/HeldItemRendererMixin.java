package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


// SOURCE FOR A LOT OF THIS: https://github.com/cosrnic/smallviewmodel/blob/main/src/main/java/uk/cosrnic/smallviewmodel/mixin/MixinHeldItemRenderer.java
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow public abstract void resetEquipProgress(Hand hand);

    @Shadow private float equipProgressMainHand;

    @Shadow private float equipProgressOffHand;

    @Inject(
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/" +
                    "minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/" +
                    "MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft" +
                            "/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render" +
                            "/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack" +
                            ";Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            )
    )
    public void onRenderHeldItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
                                 float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
                                 VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (hand == Hand.MAIN_HAND) {
            float rotX = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotX();
            float rotY = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotY();
            float rotZ = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotZ();
            float posX = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getPosX() / 100f;
            float posY = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getPosY() / 100f;
            float posZ = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getPosZ() / 100f;

            float scale = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getScale();
            matrices.translate(posX, posY, posZ);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));
            matrices.scale(scale, scale, scale);
        }
    }

    /**
     * Stop the weird mid-swing bobbing animation from happening
     * @param original
     * @return 1f if enabled, else original
     */
    @ModifyExpressionValue(
            method = "updateHeldItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"
            )
    )
    public float attackCooldown(float original) {
        return 1f;
    }

    @Inject(
            method = "applyEquipOffset",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onApplyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getCancelReEquip()) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            matrices.translate((float)i * 0.56f, -0.52f, -0.72f);
            ci.cancel();
        }
    }

    @Inject(
            method = "applyEatOrDrinkTransformation",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;pow(DD)D",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void onDrink(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        ci.cancel();
    }
}
