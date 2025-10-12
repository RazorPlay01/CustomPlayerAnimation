package com.github.razorplay01.cpa.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerRenderer.class)
public interface PlayerRendererAccesor {
    @Invoker("getArmPose")
    static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer abstractClientPlayer, HumanoidArm humanoidArm) {
        throw new AssertionError();
    }
}
