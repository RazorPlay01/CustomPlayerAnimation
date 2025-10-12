package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.util.interfaces.PlayerRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerRenderState.class)
public class PlayerRenderStateMixin implements PlayerRenderStateAccessor {
    @Unique
    private Player player;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
