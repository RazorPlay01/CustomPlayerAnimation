package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.platform.common.util.MapRenderer;
import com.github.razorplay01.cpa.platform.common.util.interfaces.HumanoidRenderStateAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <=1.21.8 {
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.MultiBufferSource;*/
//?} else {
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ExtendedItemStackRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//?}


@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel> extends RenderLayer<S, M> {

	protected ItemInHandLayerMixin(RenderLayerParent<S, M> renderLayerParent) {
		super(renderLayerParent);
	}

	//? if <=1.21.8 {
	/*@Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
	private void renderArmWithItem(S armedEntityRenderState, ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		if (!(armedEntityRenderState instanceof PlayerRenderState playerRenderState)) return;
		LivingEntity livingEntity = ((HumanoidRenderStateAccessor) playerRenderState).getLivingEntity();
		if (!(livingEntity instanceof Player)) {
			return;
		}
		this.onRenderItem(livingEntity, this.getParentModel(), humanoidArm, poseStack, multiBufferSource, i, ci);
	}

	@Unique
	public void onRenderItem(LivingEntity entity, EntityModel<?> model, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
		if (!(model instanceof HumanoidModel<?> humanoid)) {
			return;
		}
		if (!isArmVisible(humanoid, arm)) {
			return;
		}

		boolean isMainHand = arm == entity.getMainArm();
		ItemStack heldItem = isMainHand ? entity.getMainHandItem() : entity.getOffhandItem();

		if (heldItem.getItem().equals(Items.FILLED_MAP)) {
			renderMapInHand(humanoid, arm, matrices, vertexConsumers, light, heldItem);
			info.cancel();
		}
	}

	@Unique
	private boolean isArmVisible(HumanoidModel<?> humanoid, HumanoidArm arm) {
		return (arm == HumanoidArm.RIGHT && humanoid.rightArm.visible) ||
				(arm == HumanoidArm.LEFT && humanoid.leftArm.visible);
	}

	@Unique
	private void renderMapInHand(HumanoidModel<?> humanoid, HumanoidArm arm, PoseStack matrices,
								 MultiBufferSource vertexConsumers, int light, ItemStack itemStack) {
		matrices.pushPose();
		humanoid.translateToHand(arm, matrices);

		matrices.mulPose(Axis.XP.rotationDegrees(-90.0f));
		matrices.mulPose(Axis.YP.rotationDegrees(200.0f));

		boolean isLeftHand = arm == HumanoidArm.LEFT;
		matrices.translate((isLeftHand ? -1 : 1) / 16.0f, 0.125, -0.625);

		MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack);

		matrices.popPose();
	}*/
	//?} else {
	@Inject(method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"), cancellable = true)
	private void renderArmWithItem(S armedEntityRenderState, ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
		if (!(armedEntityRenderState instanceof AvatarRenderState playerRenderState)) return;
		LivingEntity livingEntity = ((HumanoidRenderStateAccessor) playerRenderState).getLivingEntity();
		if (!(livingEntity instanceof Player player)) {
			return;
		}
		ItemStack itemStack = null;
		if (itemStackRenderState instanceof ExtendedItemStackRenderState ext && ext.getItemStack() != null) {
			itemStack = ext.getItemStack();
		} else {
			return;
		}
		this.onRenderItem(player, this.getParentModel(), itemStack,
				humanoidArm, poseStack, submitNodeCollector, armedEntityRenderState, i, ci);
	}

	@Unique
	public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
							 PoseStack poseStack,
							 SubmitNodeCollector submitNodeCollector,
							 net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState,
							 int light, CallbackInfo info) {
		if (!(model instanceof HumanoidModel<?> humanoid)) {
			return;
		}
		if (!isArmVisible(humanoid, arm)) {
			return;
		}

		boolean isMainHand = arm == entity.getMainArm();
		ItemStack heldItem = isMainHand ? entity.getMainHandItem() : entity.getOffhandItem();

		if (heldItem.getItem().equals(Items.FILLED_MAP)) {
			renderMapInHand(entity, humanoid, itemStack, arm, poseStack, submitNodeCollector, livingEntityRenderState, light);
			info.cancel();
		}
	}

	@Unique
	private boolean isArmVisible(HumanoidModel<?> humanoid, HumanoidArm arm) {
		return (arm == HumanoidArm.RIGHT && humanoid.rightArm.visible) ||
				(arm == HumanoidArm.LEFT && humanoid.leftArm.visible);
	}

	@Unique
	private void renderMapInHand(LivingEntity entity, HumanoidModel<?> humanoid, ItemStack itemStack, HumanoidArm arm,
								 PoseStack poseStack,
								 SubmitNodeCollector submitNodeCollector,
								 net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState,
								 int light) {
		poseStack.pushPose();
		humanoid.translateToHand((HumanoidRenderState) livingEntityRenderState, arm, poseStack);

		poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
		poseStack.mulPose(Axis.YP.rotationDegrees(200.0f));

		boolean isLeftHand = arm == HumanoidArm.LEFT;
		poseStack.translate((isLeftHand ? -1 : 1) / 16.0f, 0.125, -0.625);

		MapRenderer.renderFirstPersonMap(poseStack, submitNodeCollector, light, itemStack, !entity.getOffhandItem().isEmpty(), entity.getMainArm() == HumanoidArm.LEFT);

		poseStack.popPose();
	}
	//?}
}
