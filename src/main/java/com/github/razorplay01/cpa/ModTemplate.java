package com.github.razorplay01.cpa;

import com.github.razorplay01.cpa.platform.Platform;

import com.github.razorplay01.cpa.platform.common.config.ClientConfig;
import com.github.razorplay01.cpa.platform.common.config.ConfigWrapper;
import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.enums.PlayState;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import com.github.razorplay01.cpa.platform.fabric.FabricPlatform;
//?} neoforge {
/*import com.github.razorplay01.cpa.platform.neoforge.NeoforgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ModTemplate {
	public static final String MOD_ID = /*$ mod_id*/ "cpa";
	public static final String MOD_VERSION = /*$ mod_version*/ "5.6.1";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Custom Player Animations";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ClientConfig CONFIG;

	public static final /*? if <=1.21.10 {*//*net.minecraft.resources.ResourceLocation*//*?} else {*/ net.minecraft.resources.Identifier/*?}*/ MAIN_ANIMATION_CONTAINER_LAYER_ID = of("main_animation_container");
	public static final /*? if <=1.21.10 {*//*net.minecraft.resources.ResourceLocation*//*?} else {*/ net.minecraft.resources.Identifier/*?}*/ OVERLAY_ANIMATION_CONTAINER_LAYER_ID = of("overlay_animation_container");
	public static final /*? if <=1.21.10 {*//*net.minecraft.resources.ResourceLocation*//*?} else {*/ net.minecraft.resources.Identifier/*?}*/ SPECIAL_ANIMATION_CONTAINER_LAYER_ID = of("special_animation_container");

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, ModTemplate.xplat().loader());
	}

	public static void onInitializeClient() {
		LOGGER.info("Initializing {} Client on {}", MOD_ID, ModTemplate.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);

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
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?}
	}

	public static Animation getAnimation(String animationId) {
		return PlayerAnimResources.getAnimation(of(animationId));
	}

	public static /*? if <=1.21.10 {*//*net.minecraft.resources.ResourceLocation*//*?} else {*/ net.minecraft.resources.Identifier/*?}*/ of(String path) {
		return /*? if <=1.21.10 {*//*net.minecraft.resources.ResourceLocation*//*?} else {*/ net.minecraft.resources.Identifier/*?}*/.fromNamespaceAndPath(MOD_ID, path);
	}
}
