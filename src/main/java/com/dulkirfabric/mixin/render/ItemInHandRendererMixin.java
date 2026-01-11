package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


// SOURCE FOR A LOT OF THIS: https://github.com/cosrnic/smallviewmodel/blob/main/src/main/java/uk/cosrnic/smallviewmodel/mixin/MixinHeldItemRenderer.java
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Inject(
            method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;" +
                    "FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;" +
                    "FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;render" +
                            "Item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;" +
                            "Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;" +
                            "Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
            )
    )
    public void onRenderHeldItem(AbstractClientPlayer abstractClientPlayer, float f, float g,
                                 InteractionHand interactionHand, float h, ItemStack itemStack, float i,
                                 PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int j, CallbackInfo ci) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            float rotX = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotX();
            float rotY = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotY();
            float rotZ = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotZ();
            float posX = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getPosX() / 100f;
            float posY = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getPosY() / 100f;
            float posZ = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getPosZ() / 100f;

            float scale = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getScale();
            poseStack.translate(posX, posY, posZ);

            poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
            poseStack.scale(scale, scale, scale);
        }
    }

    /**
     * Stop the weird mid-swing bobbing animation from happening
     * @param original
     * @return 1f if enabled, else original
     */
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"
            )
    )
    public float dulkir$attackCooldown(float original) {
        return DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getCancelReEquip() ? 1f : original;
    }

    @Inject(
            method = "applyItemArmTransform",
            at = @At("HEAD"),
            cancellable = true
    )
    public void dulkir$onApplyEquipOffset(PoseStack poseStack, HumanoidArm humanoidArm, float f, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getCancelReEquip()) {
            int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate((float)i * 0.56f, -0.52f, -0.72f);
            ci.cancel();
        }
    }

    @Inject(
            method = "applyEatTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;pow(DD)D",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void onDrink(PoseStack poseStack, float f, HumanoidArm humanoidArm, ItemStack itemStack,
                        Player player, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getRotationlessDrink()){
            ci.cancel();
        }
    }
}
