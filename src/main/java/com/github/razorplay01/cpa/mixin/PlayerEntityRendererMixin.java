package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.platform.common.util.interfaces.HumanoidRenderStateAccessor;
import com.github.razorplay01.cpa.platform.common.util.interfaces.IAnimationControl;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >= 1.21.2 && <=1.21.8 {
/*import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
*///?}
//? if > 1.21.8 {
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.InteractionHand;
//?}
//? if <= 1.21.10 {
/*import net.minecraft.client.model.PlayerModel;
 *///?}else{
import net.minecraft.client.model.player.PlayerModel;
//?}

//? if < 1.21.2 {
/*@Mixin(net.minecraft.client.renderer.entity.player.PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin {

	@Shadow
	private static net.minecraft.client.model.HumanoidModel.ArmPose getArmPose(AbstractClientPlayer player, net.minecraft.world.InteractionHand hand) {
		return null;
	}

	@Inject(method = {"setModelProperties"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", shift = At.Shift.BY, by = 2)})
	private void setModelPose(AbstractClientPlayer player, CallbackInfo ci) {
		((IAnimationControl) player).setMainArmPose(getArmPose(player, net.minecraft.world.InteractionHand.MAIN_HAND));
		((IAnimationControl) player).setOffArmPose(getArmPose(player, net.minecraft.world.InteractionHand.OFF_HAND));
	}
}

*///?}

//? if >= 1.21.2 && <=1.21.8 {
/*@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
					shift = At.Shift.BY))
	private void setModelPose(AbstractClientPlayer abstractClientPlayer, PlayerRenderState playerRenderState, float f, CallbackInfo ci) {
		((IAnimationControl) abstractClientPlayer).setMainArmPose(PlayerRendererAccesor.getArmPose(abstractClientPlayer, abstractClientPlayer.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.RIGHT : HumanoidArm.LEFT));
		((IAnimationControl) abstractClientPlayer).setOffArmPose(PlayerRendererAccesor.getArmPose(abstractClientPlayer, abstractClientPlayer.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("RETURN"))
	private void onExtractRenderState(AbstractClientPlayer abstractClientPlayer, PlayerRenderState playerRenderState, float f, CallbackInfo ci) {
		((HumanoidRenderStateAccessor) playerRenderState).setLivingEntity(abstractClientPlayer);
	}
}
*///?}
//? if > 1.21.8 {
@Mixin(AvatarRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
	protected PlayerEntityRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
					shift = At.Shift.BY))
	private void setModelPose(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		if (entity instanceof Avatar avatar) {
			((IAnimationControl) avatar).setMainArmPose(PlayerRendererAccesor.getArmPose(entity, entity.getMainHandItem(), InteractionHand.MAIN_HAND));
			((IAnimationControl) avatar).setOffArmPose(PlayerRendererAccesor.getArmPose(entity, entity.getOffhandItem(), InteractionHand.OFF_HAND));
		}
	}

	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At("RETURN"))
	private void onExtractRenderState(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		((HumanoidRenderStateAccessor) state).setLivingEntity(entity);
	}
}
		//?}
