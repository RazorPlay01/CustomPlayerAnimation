package com.github.razorplay01.cpa.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
//? if <=1.21.8 {
/*import net.minecraft.client.renderer.entity.player.PlayerRenderer;
*///?} else {
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
//?}

//? if <=1.21.8 {
/*@Mixin(PlayerRenderer.class)
public interface PlayerRendererAccesor {
	@Invoker("getArmPose")
	static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer abstractClientPlayer, HumanoidArm humanoidArm) {
		throw new AssertionError();
	}
}
*///?} else {
@Mixin(AvatarRenderer.class)
public interface PlayerRendererAccesor {
	@Invoker("getArmPose")
	static HumanoidModel.ArmPose getArmPose(Avatar avatar, ItemStack itemStack, InteractionHand interactionHand) {
		throw new AssertionError();
	}
}
//?}

