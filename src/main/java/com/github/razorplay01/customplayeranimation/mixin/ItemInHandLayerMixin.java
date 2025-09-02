package com.github.razorplay01.customplayeranimation.mixin;

import com.github.razorplay01.customplayeranimation.util.MapRenderer;
import com.github.razorplay01.customplayeranimation.util.interfaces.PlayerRenderStateAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel> extends RenderLayer<S, M> {

    protected ItemInHandLayerMixin(RenderLayerParent<S, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void renderArmWithItem(S armedEntityRenderState, ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        LivingEntity livingEntity = ((PlayerRenderStateAccessor) armedEntityRenderState).getPlayer();
        onRenderItem(livingEntity, this.getParentModel(), humanoidArm, poseStack, multiBufferSource, i, ci);
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
    }
}
