package com.github.razorplay01.cpa.config;

//? fabric {

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/*? if >= 1.21.11 {*/
import me.shedaniel.autoconfig.AutoConfigClient;
/*?} else {*/
/*import me.shedaniel.autoconfig.AutoConfig;
*//*?}*/

@Environment(EnvType.CLIENT)
public class ModMenuConfig implements ModMenuApi {


	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent ->
				/*? if >= 1.21.11 {*/AutoConfigClient/*?} else {*//*AutoConfig*//*?}*/
						.getConfigScreen(ConfigWrapper.class, parent).get();
	}
}
//?}
