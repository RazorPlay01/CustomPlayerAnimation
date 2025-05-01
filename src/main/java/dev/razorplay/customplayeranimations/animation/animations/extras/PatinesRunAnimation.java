package dev.razorplay.customplayeranimations.animation.animations.extras;

import dev.razorplay.customplayeranimations.util.Util;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class PatinesRunAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.moveAnimations.runningAnimationConfig.getSpeedMultiplier()));
        context.mainAnimationContainer().setAnimationFadeTime(CONFIG.moveAnimations.runningAnimationConfig.getFadeTime());
        context.mainAnimationContainer().setAnimationPriority(CONFIG.moveAnimations.runningAnimationConfig.getPriority());

        context.mainAnimationContainer().setCurrentAnimation(getAnimation("patines"));
        context.mainAnimationContainer().setCurrentAnimationId("patines");
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        ItemStack playerBoots = context.player().getItemBySlot(EquipmentSlot.FEET);
        int customModelDataId = Util.getCustomModelDataId(playerBoots);
        return context.playerData().getMovementSpeed() > 0 &&
                context.player().isSprinting() &&
                !context.playerData().isMovingBackwards() &&
                !context.player().isCrouching() &&
                !context.player().isPassenger() &&
                (customModelDataId == 9000010 && playerBoots.is(Items.GOLDEN_BOOTS));
    }
}
