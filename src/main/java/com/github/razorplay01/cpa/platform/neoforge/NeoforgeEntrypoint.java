package com.github.razorplay01.cpa.platform.neoforge;

//? neoforge {

/*import com.github.razorplay01.cpa.ModTemplate;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import com.github.razorplay01.cpa.platform.common.config.ConfigWrapper;

import java.util.function.Supplier;

/^? if >= 1.21.11 {^/
import me.shedaniel.autoconfig.AutoConfigClient;
/^?} else {^/
/^import me.shedaniel.autoconfig.AutoConfig;
^//^?}^/

@Mod(ModTemplate.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint(IEventBus modEventBus, ModContainer modContainer) {
		ModTemplate.onInitialize();
		modContainer.registerExtensionPoint(
				IConfigScreenFactory.class,
				(Supplier<IConfigScreenFactory>) () -> (client, parent) ->
						/^? if >= 1.21.11 {^/AutoConfigClient/^?} else {^//^AutoConfig^//^?}^/
								.getConfigScreen(ConfigWrapper.class, parent).get()
		);
	}
}
*///?}
