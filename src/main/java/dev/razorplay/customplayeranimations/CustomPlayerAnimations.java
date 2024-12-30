package dev.razorplay.customplayeranimations;

import dev.kosmx.playerAnim.api.IPlayable;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.config.ConfigWrapper;
import dev.razorplay.customplayeranimations.util.FirstPersonConditionRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPlayerAnimations implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "custom_player_animations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ClientConfig CONFIG;

    @Override
    public void onInitialize() {
        // []
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().client;
        FirstPersonConditionRegistry.registerDefaultConditions();
        LOGGER.info("Custom Player Animations initialized.");
    }

    public static KeyframeAnimation getAnimation(String animationId) {
        IPlayable playable = PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, animationId));
        KeyframeAnimation anim = playable instanceof IPlayable ? (KeyframeAnimation) playable : null;
        if (anim == null) {
            LOGGER.error("Animation {} not found.", animationId);
        }
        return anim;
    }
}