package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.platform.common.util.Util.isPickaxe;
import static net.minecraft.world.InteractionHand.MAIN_HAND;
//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.Animation;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
*///?}
public class PickaxeAnimation implements ICustomAnimation {
    private final Map<UUID, Boolean> animationsInProgress = new HashMap<>();
    private final Map<UUID, Long> animationStartTimes = new HashMap<>();
    private final Map<UUID, Float> animationDurations = new HashMap<>();

    @Override
    public void playAnimation(AnimationContext context) {
        UUID uuid = context.player().getUUID();
        if (!CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            animationsInProgress.put(uuid, false);
        } else {
            // Si el jugador está balanceando el pico o la animación está en progreso
            if (context.player().swinging &&
                    isPickaxe(context.player().getMainHandItem()) &&
					//? if >=1.21.1{
                    context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(net.minecraft.core.component.DataComponents.TOOL) &&
					//?}
					//? if <1.21.1{
					/*context.player().getMainHandItem().getItem() instanceof net.minecraft.world.item.PickaxeItem &&
					*///?}
                    context.player().swingingArm.equals(MAIN_HAND)) {

                // Iniciar una nueva animación
				/*? if >=1.21.1 {*/Animation/*?} else {*/ /*KeyframeAnimation*//*?}*/ animation = context.player().isCrouching() ?
                        getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) :
                        getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId());

                animationDurations.put(uuid, (float) (animation/*? if >=1.21.1 {*/.length()/*?} else {*/ /*.getLength()*//*?}*/ - 3)); // Obtener la duración real de la animación
                animationStartTimes.put(uuid, context.player().level().getGameTime());
                animationsInProgress.put(uuid, true);

                configureAnimationContainer(CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig, context.overlayAnimationContainer());
                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.PICKAXE_ANIMATION.getAnimationId());
                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = (context.playerData().getRightHand() != MAIN_HAND)/*?} else {*/ /*setEnabled((context.playerData().getRightHand() != MAIN_HAND))*//*?}*/;
            } else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
                // Continuar la animación si no ha pasado el tiempo mínimo
                long currentTime = context.player().level().getGameTime();
                if (currentTime - animationStartTimes.getOrDefault(uuid, 0L) < animationDurations.getOrDefault(uuid, 0f)) {
                    configureAnimationContainer(CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig, context.overlayAnimationContainer());
                    context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId()));
                    context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.PICKAXE_ANIMATION.getAnimationId());
                    ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = (context.playerData().getRightHand() != MAIN_HAND)/*?} else {*/ /*setEnabled((context.playerData().getRightHand() != MAIN_HAND))*//*?}*/;
                } else {
                    // La animación ha terminado
                    animationsInProgress.put(uuid, false);
                }
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        UUID uuid = context.player().getUUID();
        // Reproducir la animación si el jugador está balanceando el pico o si una animación está en progreso
        return ((context.player().swinging &&
                isPickaxe(context.player().getMainHandItem()) &&
				//? if >=1.21.1{
				context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(net.minecraft.core.component.DataComponents.TOOL) &&
				//?}
				//? if <1.21.1{
				/*context.player().getMainHandItem().getItem() instanceof net.minecraft.world.item.PickaxeItem &&
				*///?}
                context.player().swingingArm.equals(MAIN_HAND)) || Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }
}
