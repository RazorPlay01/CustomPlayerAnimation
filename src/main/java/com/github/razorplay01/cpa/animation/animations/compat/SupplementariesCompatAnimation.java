package com.github.razorplay01.cpa.animation.animations.compat;/*

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import dev.razorplay.customplayeranimations.util.FirstPersonConditionRegistry;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.fabricmc.loader.api.FabricLoader;
import net.mehvahdjukaar.supplementaries.common.items.BubbleBlowerItem;
import net.mehvahdjukaar.supplementaries.common.items.FluteItem;
import net.mehvahdjukaar.supplementaries.common.items.SlingshotItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import static dev.razorplay.customplayeranimations.util.Util.disableBothArms;

public class SupplementariesCompatAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        if (!FirstPersonConditionRegistry.hasCondition(Identifier.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "flute_item"))) {
            FirstPersonConditionRegistry.register(Identifier.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "flute_item"), player ->
                    context.player().getUseItem().getItem() instanceof FluteItem
            );
        }
        if (context.player().isUsingItem()) {
            if (checkFluteItem(context.player().getUseItem().getItem())) {
                disableBothArms(context);
            }
            if (checkSlingShotItem(context.player().getUseItem().getItem()) || checkBubbleBlowerItem(context.player().getUseItem().getItem())) {
                context.player().disableActiveArm(context.mainAnimationContainer());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return FabricLoader.getInstance().isModLoaded("supplementaries");
    }

    public static boolean checkFluteItem(Item item) {
        return item instanceof FluteItem;
    }

    public static boolean checkSlingShotItem(Item item) {
        return item instanceof SlingshotItem;
    }

    public static boolean checkBubbleBlowerItem(Item item) {
        return item instanceof BubbleBlowerItem;
    }
}
*/
