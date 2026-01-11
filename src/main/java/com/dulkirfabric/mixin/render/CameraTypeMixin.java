package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.CameraType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CameraType.class)
public abstract class CameraTypeMixin {
    @Shadow public abstract boolean isFirstPerson();

    @Inject(
            method = "cycle",
            at = @At("HEAD"),
            cancellable = true
    )
    public void skipPerspective(CallbackInfoReturnable<CameraType> cir) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getIgnoreReverseThirdPerson()) {
            cir.setReturnValue(this.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : CameraType.FIRST_PERSON);
        }
    }

}
