/*
package dev.razorplay.customplayeranimations.animation.animations.compat;

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import dev.razorplay.customplayeranimations.util.FirstPersonConditionRegistry;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.fabricmc.loader.api.FabricLoader;
import net.mehvahdjukaar.supplementaries.common.items.BubbleBlowerItem;
import net.mehvahdjukaar.supplementaries.common.items.FluteItem;
import net.mehvahdjukaar.supplementaries.common.items.SlingshotItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static dev.razorplay.customplayeranimations.util.Util.disableBothArms;

public class SupplementariesCompatAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        if (!FirstPersonConditionRegistry.hasCondition(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "flute_item"))) {
            FirstPersonConditionRegistry.register(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "flute_item"), avatar ->
                    context.avatar().getUseItem().getItem() instanceof FluteItem
            );
        }
        if (context.avatar().isUsingItem()) {
            if (checkFluteItem(context.avatar().getUseItem().getItem())) {
                disableBothArms(context);
            }
            if (checkSlingShotItem(context.avatar().getUseItem().getItem()) || checkBubbleBlowerItem(context.avatar().getUseItem().getItem())) {
                context.avatar().disableActiveArm(context.mainAnimationContainer());
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
