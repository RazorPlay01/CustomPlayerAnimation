package com.github.razorplay01.customplayeranimation.mixin;

import com.github.razorplay01.customplayeranimation.util.interfaces.PlayerRenderStateAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @WrapWithCondition(
            method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 1
            )
    )
    private boolean disableDeathRotationForPlayer(PoseStack instance, Quaternionfc quaternionfc, S livingEntityRenderState) {
        LivingEntity livingEntity = ((PlayerRenderStateAccessor) livingEntityRenderState).getPlayer();
        return !(livingEntity instanceof Player && CONFIG.deathAnimations.isEnabled());
    }
}
