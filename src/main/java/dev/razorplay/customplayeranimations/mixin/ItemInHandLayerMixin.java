package dev.razorplay.customplayeranimations.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.razorplay.customplayeranimations.util.MapRenderer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {

    protected ItemInHandLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        onRenderItem(livingEntity, this.getParentModel(), itemStack, humanoidArm, poseStack, multiBufferSource, i, ci);
    }

    @Unique
    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
                             PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {

        if (model instanceof ArmedModel armedModel && model instanceof HumanoidModel<?> humanoid
                && ((arm == HumanoidArm.RIGHT && humanoid.rightArm.visible) || (arm == HumanoidArm.LEFT && humanoid.leftArm.visible))) {
            if (arm == entity.getMainArm() && entity.getMainHandItem().getItem().equals(Items.FILLED_MAP)) { // Mainhand
                matrices.pushPose();
                armedModel.translateToHand(arm, matrices);
                matrices.mulPose(Axis.XP.rotationDegrees(-90.0f));
                matrices.mulPose(Axis.YP.rotationDegrees(200.0f));
                boolean bl = arm == HumanoidArm.LEFT;
                matrices.translate((bl ? -1 : 1) / 16.0f, 0.125, -0.625);
                MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,true);
                matrices.popPose();
                info.cancel();
                return;
            }
            if (arm != entity.getMainArm() && entity.getOffhandItem().getItem().equals(Items.FILLED_MAP)) { // Only
                // offhand
                matrices.pushPose();
                armedModel.translateToHand(arm, matrices);
                matrices.mulPose(Axis.XP.rotationDegrees(-90.0f));
                matrices.mulPose(Axis.YP.rotationDegrees(200.0f));
                boolean bl = arm == HumanoidArm.LEFT;
                matrices.translate((bl ? -1 : 1) / 16.0f, 0.125, -0.625);
                MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,true);
                matrices.popPose();
                info.cancel();
            }
        }

    }
}
