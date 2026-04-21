package com.github.razorplay01.cpa.mixin;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Inventory.class)
public interface InventoryAccessor {
//? if >= 1.21.2 {

	/*@org.spongepowered.asm.mixin.gen.Accessor("equipment")
	net.minecraft.world.entity.EntityEquipment getEquipment();
*///?}
}
