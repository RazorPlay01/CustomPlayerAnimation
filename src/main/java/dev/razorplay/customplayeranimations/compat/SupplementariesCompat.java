package dev.razorplay.customplayeranimations.compat;

import net.mehvahdjukaar.supplementaries.common.items.FluteItem;
import net.minecraft.world.item.Item;

public class SupplementariesCompat {
    private SupplementariesCompat() {
        // []
    }

    public static boolean checkFluteItem(Item item) {
        return item instanceof FluteItem;
    }
}
