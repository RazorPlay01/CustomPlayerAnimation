package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.platform.common.util.interfaces.HumanoidRenderStateAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if <=1.21.8 {
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.player.Player;
*///?} else {
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
//?}

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

	//? if <=1.21.8 {
	/*@WrapWithCondition(
			method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
					ordinal = 1
			)
	)
	private boolean disableDeathRotationForPlayer(PoseStack instance, Quaternionfc quaternionfc, S livingEntityRenderState) {
		if (!(livingEntityRenderState instanceof PlayerRenderState playerRenderState)) return true;
		if (!(((HumanoidRenderStateAccessor) playerRenderState).getLivingEntity() instanceof Player)) return true;
		return CONFIG.getMainAnimations().deathAnimations.isEnabled();
	}
*///?} else {
@WrapWithCondition(
		method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
		at = @At(
				value = "INVOKE",
				target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
				ordinal = 1
		)
)
private boolean disableDeathRotationForPlayer(PoseStack instance, Quaternionfc quaternionfc, S livingEntityRenderState) {
	if (!(livingEntityRenderState instanceof AvatarRenderState playerRenderState)) return true;
	if (!(((HumanoidRenderStateAccessor) playerRenderState).getLivingEntity() instanceof Avatar)) return true;
	return CONFIG.getMainAnimations().deathAnimations.isEnabled();
}
	//?}
}
