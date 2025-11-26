package com.github.razorplay01.cpa.platform.neoforge;

//? neoforge {
/*
import com.github.razorplay01.cpa.ModTemplate;
import com.github.razorplay01.cpa.platform.common.config.ConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.function.Supplier;

@Mod(ModTemplate.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint() {
		ModLoadingContext.get().getActiveContainer().registerExtensionPoint(
				IConfigScreenFactory.class,
				(Supplier<IConfigScreenFactory>) () -> (minecraft, parentScreen) ->
						AutoConfig.getConfigScreen(ConfigWrapper.class, parentScreen).get()
		);
		ModTemplate.onInitialize();
	}
}
*///?}
