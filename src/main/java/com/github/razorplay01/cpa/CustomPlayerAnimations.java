package com.github.razorplay01.cpa;

import com.github.razorplay01.cpa.config.ClientConfig;
import com.github.razorplay01.cpa.config.ConfigWrapper;
import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.enums.PlayState;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPlayerAnimations implements ClientModInitializer {
    public static final String MOD_ID = "cpa";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ClientConfig CONFIG;
    public static final ResourceLocation MAIN_ANIMATION_CONTAINER_LAYER_ID = of("main_animation_container");
    public static final ResourceLocation OVERLAY_ANIMATION_CONTAINER_LAYER_ID = of("overlay_animation_container");
    public static final ResourceLocation SPECIAL_ANIMATION_CONTAINER_LAYER_ID = of("special_animation_container");


    @Override
    public void onInitializeClient() {
        AutoConfig.register(ConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().client;

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(MAIN_ANIMATION_CONTAINER_LAYER_ID, 1,
                player -> new PlayerAnimationController(player,
                        (controller, state, animSetter) -> PlayState.STOP
                )
        );
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(OVERLAY_ANIMATION_CONTAINER_LAYER_ID, 2,
                player -> new PlayerAnimationController(player,
                        (controller, state, animSetter) -> PlayState.STOP
                )
        );
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPECIAL_ANIMATION_CONTAINER_LAYER_ID, 3,
                player -> new PlayerAnimationController(player,
                        (controller, state, animSetter) -> PlayState.STOP
                )
        );

        LOGGER.info("Custom Player Animations initialized.");
    }

    public static Animation getAnimation(String animationId) {
        return PlayerAnimResources.getAnimation(of(animationId));
    }

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
