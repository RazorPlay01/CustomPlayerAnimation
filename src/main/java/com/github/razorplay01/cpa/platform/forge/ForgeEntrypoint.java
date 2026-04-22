package com.github.razorplay01.cpa.platform.forge;

//? forge {

/*import com.github.razorplay01.cpa.ModTemplate;
import com.github.razorplay01.cpa.platform.common.config.ConfigWrapper;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.ConfigScreenHandler;

@Mod(ModTemplate.MOD_ID)
public class ForgeEntrypoint {

	public ForgeEntrypoint() {
		ModTemplate.onInitialize();
		ModLoadingContext.get().registerExtensionPoint(
				ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(
						(minecraft, parentScreen) ->
								me.shedaniel.autoconfig.AutoConfig.getConfigScreen(ConfigWrapper.class, parentScreen).get()
				)
		);
	}
}
*///?}
