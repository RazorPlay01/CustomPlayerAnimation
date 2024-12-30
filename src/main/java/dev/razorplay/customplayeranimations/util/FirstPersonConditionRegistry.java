package dev.razorplay.customplayeranimations.util;

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import dev.razorplay.customplayeranimations.animation.animations.compat.CarryOnCompatAnimation;
import net.mehvahdjukaar.supplementaries.common.items.FluteItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.util.Util.containsAnyAnimation;

/**
 * Registry for managing conditions that determine when both arms should be rendered
 * in first-person view.
 *
 * This class provides a centralized system for registering, managing, and checking
 * conditions that affect the first-person rendering of player arms.
 */
public class FirstPersonConditionRegistry {
    /**
     * Private constructor to prevent instantiation as this is a utility class.
     */
    private FirstPersonConditionRegistry() {
        //[]
    }

    /**
     * Map storing all registered conditions with their corresponding identifiers.
     */
    private static final Map<ResourceLocation, Predicate<AbstractClientPlayer>> CONDITIONS = new HashMap<>();

    /**
     * Registers a new condition with the specified identifier.
     *
     * @param id The unique identifier for the condition
     * @param condition The predicate that defines the condition
     */
    public static void register(ResourceLocation id, Predicate<AbstractClientPlayer> condition) {
        CONDITIONS.put(id, condition);
    }

    /**
     * Removes a condition from the registry.
     *
     * @param id The identifier of the condition to remove
     */
    public static void unregister(ResourceLocation id) {
        CONDITIONS.remove(id);
    }

    /**
     * Checks if any registered condition is met for the given player.
     *
     * @param player The player to check conditions against
     * @return true if any condition is met, false otherwise
     */
    public static boolean checkConditions(AbstractClientPlayer player) {
        return CONDITIONS.values().stream().anyMatch(condition -> condition.test(player));
    }

    /**
     * Retrieves a specific condition by its identifier.
     *
     * @param id The identifier of the condition to retrieve
     * @return An Optional containing the condition if found, empty Optional otherwise
     */
    public static Optional<Predicate<AbstractClientPlayer>> getCondition(ResourceLocation id) {
        return Optional.ofNullable(CONDITIONS.get(id));
    }

    /**
     * Checks if a condition with the specified identifier exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the condition exists, false otherwise
     */
    public static boolean hasCondition(ResourceLocation id) {
        return CONDITIONS.containsKey(id);
    }

    /**
     * Registers the default set of conditions for first-person arm rendering.
     * These conditions include:
     * - Specific animations that require both hands
     * - When holding an item in the off-hand
     * - When using a crossbow
     * - When using a flute item
     * - When is carring something
     */
    public static void registerDefaultConditions() {
        register(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "animations"), player ->
                containsAnyAnimation(player.getOverlayAnimationCPA(), CONFIG.getAnimationsThatShowBothHand()) ||
                        containsAnyAnimation(player.getMainAnimationCPA(), CONFIG.getAnimationsThatShowBothHand())
        );

        register(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "offhand_item"), player ->
                player.getOffhandItem().getItem() != Items.AIR
        );

        register(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "crossbow_pose"), player ->
                player.getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                        player.getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)
        );
    }
}