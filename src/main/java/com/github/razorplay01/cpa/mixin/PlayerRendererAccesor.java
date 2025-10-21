package com.github.razorplay01.cpa.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AvatarRenderer.class)
public interface PlayerRendererAccesor {
    @Invoker("getArmPose")
    static HumanoidModel.ArmPose getArmPose(Avatar avatar, ItemStack itemStack, InteractionHand interactionHand) {
        throw new AssertionError();
    }
}
