package com.github.razorplay01.cpa.platform.common.config;

//? fabric {

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuConfig implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		//? if < 1.21.11 {
		/*return parent -> me.shedaniel.autoconfig.AutoConfig.getConfigScreen(ConfigWrapper.class, parent).get();
		*///?}
		//? if >= 1.21.11 {
		return parent -> me.shedaniel.autoconfig.AutoConfigClient.getConfigScreen(ConfigWrapper.class, parent).get();
		//?}
	}
}
//?}
