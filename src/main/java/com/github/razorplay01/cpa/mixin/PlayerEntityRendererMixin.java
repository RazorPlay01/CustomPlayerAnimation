package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.util.interfaces.HumanoidRenderStateAccessor;
import com.github.razorplay01.cpa.util.interfaces.IAnimationControl;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    protected PlayerEntityRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
                    shift = At.Shift.BY))
    private void setModelPose(AvatarlikeEntity avatarlikeEntity, AvatarRenderState avatarRenderState, float f, CallbackInfo ci) {
        if (avatarlikeEntity instanceof Avatar avatar) {
            ((IAnimationControl) avatar).setMainArmPose(PlayerRendererAccesor.getArmPose(avatarlikeEntity, avatarlikeEntity.getMainHandItem(), InteractionHand.MAIN_HAND));
            ((IAnimationControl) avatar).setOffArmPose(PlayerRendererAccesor.getArmPose(avatarlikeEntity, avatarlikeEntity.getOffhandItem(), InteractionHand.OFF_HAND));
        }
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("RETURN"))
    private void onExtractRenderState(AvatarlikeEntity avatar, AvatarRenderState avatarRenderState, float f, CallbackInfo ci) {
        ((HumanoidRenderStateAccessor) avatarRenderState).setLivingEntity(avatar);
    }
}
