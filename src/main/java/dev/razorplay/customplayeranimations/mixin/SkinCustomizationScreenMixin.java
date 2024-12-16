package dev.razorplay.customplayeranimations.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SkinCustomizationScreen.class)
public abstract class SkinCustomizationScreenMixin extends OptionsSubScreen {
    protected SkinCustomizationScreenMixin(Screen screen, Options options, Component component) {
        super(screen, options, component);
    }

    @Inject(at = @At("HEAD"), method = "addOptions", cancellable = true)
    protected void addOptions(CallbackInfo ci) {
        ci.cancel();
        List<AbstractWidget> list = new ArrayList<>();

        for(PlayerModelPart playerModelPart : PlayerModelPart.values()) {
            list.add(CycleButton.onOffBuilder(this.options.isModelPartEnabled(playerModelPart)).create(playerModelPart.getName(), (cycleButton, boolean_) -> this.options.toggleModelPart(playerModelPart, boolean_)));
        }

        this.list.addSmall(list);
    }
}
