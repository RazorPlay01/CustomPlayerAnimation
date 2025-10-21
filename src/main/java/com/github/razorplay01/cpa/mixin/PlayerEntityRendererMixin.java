package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.util.interfaces.HumanoidRenderStateAccessor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
                    shift = At.Shift.BY))
    private void setModelPose(AbstractClientPlayer abstractClientPlayer, PlayerRenderState playerRenderState, float f, CallbackInfo ci) {
        abstractClientPlayer.setMainArmPose(PlayerRendererAccesor.getArmPose(abstractClientPlayer, abstractClientPlayer.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.RIGHT : HumanoidArm.LEFT));
        abstractClientPlayer.setOffArmPose(PlayerRendererAccesor.getArmPose(abstractClientPlayer, abstractClientPlayer.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT));
    }
    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("RETURN"))
    private void onExtractRenderState(AbstractClientPlayer abstractClientPlayer, net.minecraft.client.renderer.entity.state.PlayerRenderState playerRenderState, float f, CallbackInfo ci) {
        ((HumanoidRenderStateAccessor) playerRenderState).setLivingEntity(abstractClientPlayer);
    }
}
