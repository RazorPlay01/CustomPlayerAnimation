/*
package com.github.razorplay01.customplayeranimation.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {

    @WrapWithCondition(
            method = "setupRotations",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1
            )
    )
    private boolean disableDeathRotationForPlayer(PoseStack instance, Quaternionf quaternionf, T livingEntity) {
        return !(livingEntity instanceof Player && CONFIG.deathAnimations.isEnabled());
    }
}
*/
