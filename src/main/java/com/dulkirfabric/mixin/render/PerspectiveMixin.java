package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Perspective.class)
public abstract class PerspectiveMixin {
    @Shadow public abstract boolean isFirstPerson();

    @Inject(
            method = "next",
            at = @At("HEAD"),
            cancellable = true
    )
    public void skipPerspective(CallbackInfoReturnable<Perspective> cir) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getIgnoreReverseThirdPerson()) {
            if (this.isFirstPerson()) cir.setReturnValue(Perspective.THIRD_PERSON_BACK);
            else cir.setReturnValue(Perspective.FIRST_PERSON);
        }
    }
}
