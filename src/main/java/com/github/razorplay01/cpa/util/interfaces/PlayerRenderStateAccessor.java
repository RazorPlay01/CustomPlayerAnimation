package com.github.razorplay01.cpa.util.interfaces;

import net.minecraft.world.entity.player.Player;

public interface PlayerRenderStateAccessor {
    void setPlayer(Player entity);

    Player getPlayer();
}
