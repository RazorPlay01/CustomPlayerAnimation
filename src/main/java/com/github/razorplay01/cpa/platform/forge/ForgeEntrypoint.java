package com.github.razorplay01.cpa.platform.forge;

//? forge {

/*import com.github.razorplay01.cpa.ModTemplate;
import com.github.razorplay01.cpa.config.ConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ModTemplate.MOD_ID)
public class ForgeEntrypoint {

	public ForgeEntrypoint() {
		ModTemplate.onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModLoadingContext.get().registerExtensionPoint(
				ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(
						(client, parent) -> AutoConfig.getConfigScreen(ConfigWrapper.class, parent).get()
				)
		));
	}
}
*///?}
