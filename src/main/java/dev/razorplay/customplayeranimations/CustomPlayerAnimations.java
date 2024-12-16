package dev.razorplay.customplayeranimations;

import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.config.ConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.razorplay.customplayeranimations.animation.PlayerAnimations.loadAnimationsList;

public class CustomPlayerAnimations implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "custom_player_animations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ClientConfig CONFIG;

    public static final boolean IS_CARRYON_LOADED = FabricLoader.getInstance().isModLoaded("carryon");
    public static final boolean IS_SWORDBLOCKING_LOADED = FabricLoader.getInstance().isModLoaded("swordblocking");
    public static final boolean IS_OLDCOMBATMOD_LOADED = FabricLoader.getInstance().isModLoaded("oldcombatmod");
    public static final boolean IS_SUPPLEMENTARIES_LOADED = FabricLoader.getInstance().isModLoaded("supplementaries");
    public static final boolean IS_NEA_LOADED = FabricLoader.getInstance().isModLoaded("notenoughanimations");

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().client;
        loadAnimationsList();
        LOGGER.info("Custom Player Animations initialized.");
    }
}