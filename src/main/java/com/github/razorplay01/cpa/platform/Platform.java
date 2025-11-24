package com.github.razorplay01.cpa.platform;

public interface Platform {
	boolean isModLoaded(String modId);

	ModLoader loader();

	enum ModLoader {
		FABRIC, NEOFORGE, FORGE, QUILT
	}
}
