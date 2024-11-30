package dev.razorplay.customplayeranimations.mixin.NEA.hands;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.hands.NarutoRunningAnimation;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NarutoRunningAnimation.class)
public class NEANarutoRunningMixin {

    @Inject(method = "apply", at = @At("HEAD"), remap = false)
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model, BodyPart part, float delta, float tickCounter, CallbackInfo ci){
        entity.disableArmsAnimation(true);

    }
}