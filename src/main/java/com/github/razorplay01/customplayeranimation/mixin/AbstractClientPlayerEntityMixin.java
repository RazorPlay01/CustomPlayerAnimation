package com.github.razorplay01.customplayeranimation.mixin;

import com.github.razorplay01.customplayeranimation.animation.AnimationContainer;
import com.github.razorplay01.customplayeranimation.animation.animations.AnimationProvider;
import com.github.razorplay01.customplayeranimation.util.PlayerData;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.BodyParts;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.IAnimationControl;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimatedPlayer;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.mojang.authlib.GameProfile;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.*;
import static com.github.razorplay01.customplayeranimation.util.CustomModifiers.*;
import static com.github.razorplay01.customplayeranimation.util.Util.addModifiersToContainer;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player implements IAnimationControl, ICustomAnimatedPlayer {
    @Unique
    private AnimationContainer mainAnimationContainer;

    @Unique
    private AnimationContainer overlayAnimationContainer;

    @Unique
    private AnimationContainer specialAnimationContainer;

    @Unique
    private final PlayerData playerData = new PlayerData();

    @Unique
    private AnimationContext actualAnimationContext;

    protected AbstractClientPlayerEntityMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo ci) {
        this.mainAnimationContainer = new AnimationContainer(
                (AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((AbstractClientPlayer) (Object) this, MAIN_ANIMATION_CONTAINER_LAYER_ID),
                new HashMap<>(
                        Map.of(Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f))),
                getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
                AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
                "",
                0,
                0,
                0,
                0);
        this.overlayAnimationContainer = new AnimationContainer(
                (AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((AbstractClientPlayer) (Object) this, OVERLAY_ANIMATION_CONTAINER_LAYER_ID),
                new HashMap<>(
                        Map.of(Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f),
                                Modifiers.SHIELD_MODIFIER.getModifierId(), createShieldModifier((AbstractClientPlayer) (Object) this),
                                Modifiers.HAND_SWING_MODIFIER.getModifierId(), createSwingModifier((AbstractClientPlayer) (Object) this, mainAnimationContainer),
                                Modifiers.BOW_MODIFIER.getModifierId(), createBowModifier((AbstractClientPlayer) (Object) this))),
                getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
                AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
                "",
                0,
                0,
                0,
                0);
        this.specialAnimationContainer = new AnimationContainer(
                (AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((AbstractClientPlayer) (Object) this, SPECIAL_ANIMATION_CONTAINER_LAYER_ID),
                new HashMap<>(
                        Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                                Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f))),
                getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
                AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
                "",
                0,
                0,
                0,
                0);
        // Main Animation Container
        addModifiersToContainer(mainAnimationContainer);

        // Overlay Animation Container
        addModifiersToContainer(overlayAnimationContainer);

        // UpHand Animation Container
        addModifiersToContainer(specialAnimationContainer);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (mainAnimationContainer == null || overlayAnimationContainer == null || specialAnimationContainer == null)
            return;
        this.playerData.update((AbstractClientPlayer) (Object) this);

        overlayAnimationContainer.resetAnimationProperties();

        this.actualAnimationContext = new AnimationContext(mainAnimationContainer, overlayAnimationContainer, specialAnimationContainer, (AbstractClientPlayer) (Object) this, playerData);

        playAnimations();
        updateAnimationSpeeds();

        checkMainHandItemForArmDisabling();
        checkOffHandItemForArmDisabling();

        updateAnimationContainers();

        this.playerData.setPrevPlayerPosition(this.playerData.getPlayerPosition());
        this.playerData.setPrevPlayerBodyYaw(this.playerData.getPlayerBodyYaw());
        this.playerData.setPrevOnGround(onGround());
    }

    @Override
    public AnimationContainer getMainAnimationCPA() {
        return mainAnimationContainer;
    }

    @Override
    public AnimationContainer getOverlayAnimationCPA() {
        return overlayAnimationContainer;
    }

    @Override
    public AnimationContainer getSpecialAnimationCPA() {
        return specialAnimationContainer;
    }

    @Override
    public AnimationContext getAnimationContext() {
        return this.actualAnimationContext;
    }

    @Override
    public void disableBodyPartAnimation(AnimationContainer animationContainer, BodyParts bodyPart) {
        animationContainer.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> getBoneFunc.apply(bodyPart.getPartId()).setEnabled(false));
    }

    @Override
    public void disableBodyPartAnimationInAllContainers(BodyParts bodyPart) {
        this.disableBodyPartAnimation(actualAnimationContext.mainAnimationContainer(), bodyPart);
        this.disableBodyPartAnimation(actualAnimationContext.overlayAnimationContainer(), bodyPart);
        this.disableBodyPartAnimation(actualAnimationContext.specialAnimationContainer(), bodyPart);
    }

    @Override
    public void disableActiveArm(AnimationContainer animationContainer) {
        if (actualAnimationContext.player().getUsedItemHand().equals(MAIN_HAND)) {
            this.disableBodyPartAnimation(animationContainer, actualAnimationContext.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        } else {
            this.disableBodyPartAnimation(animationContainer, actualAnimationContext.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
        }
    }

    @Override
    public HumanoidModel.ArmPose getMainArmPose() {
        return this.playerData.getMainArmPose();
    }

    @Override
    public void setMainArmPose(HumanoidModel.ArmPose armPosition) {
        this.playerData.setMainArmPose(armPosition);
    }

    @Override
    public HumanoidModel.ArmPose getOffArmPose() {
        return this.playerData.getOffArmPose();
    }

    @Override
    public void setOffArmPose(HumanoidModel.ArmPose armPosition) {
        this.playerData.setOffArmPose(armPosition);
    }

    @Unique
    private void updateAnimationSpeeds() {
        ((SpeedModifier) mainAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = mainAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
        ((SpeedModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = overlayAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
        ((SpeedModifier) specialAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = specialAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
    }

    @Unique
    private void playAnimations() {
        // Main Animations
        for (ICustomAnimation animation : AnimationProvider.MAIN_ANIMATIONS) {
            if (animation.shouldPlayAnimation(actualAnimationContext)) {
                animation.playAnimation(actualAnimationContext);
            }
        }
        // Overlay Animations
        for (ICustomAnimation animation : AnimationProvider.OVERLAY_ANIMATIONS) {
            if (animation.shouldPlayAnimation(actualAnimationContext)) {
                animation.playAnimation(actualAnimationContext);
            }
        }
        // Special Animations
        for (ICustomAnimation animation : AnimationProvider.SPECIAL_ANIMATIONS) {
            if (animation.shouldPlayAnimation(actualAnimationContext)) {
                animation.playAnimation(actualAnimationContext);
            }
        }
    }

    @Unique
    private void updateAnimationContainers() {
        updateMainAnimationContainer();
        updateOverlayAnimationContainer();
        updateUpHandAnimationContainer();
    }

    @Unique
    private void updateMainAnimationContainer() {
        if ((!Objects.equals(mainAnimationContainer.getCurrentAnimationId(), mainAnimationContainer.getPrevAnimationId()) && mainAnimationContainer.getAnimationPriority() >= mainAnimationContainer.getPrevAnimationPriority()) ||
                !mainAnimationContainer.getAnimationController().isActive()) {

            playCurrentAnimation(mainAnimationContainer.getAnimationController(), mainAnimationContainer.getCurrentAnimation());

            mainAnimationContainer.setPrevAnimationId(mainAnimationContainer.getCurrentAnimationId());
            mainAnimationContainer.setPrevAnimationPriority(mainAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    private void updateOverlayAnimationContainer() {
        if ((!Objects.equals(overlayAnimationContainer.getCurrentAnimationId(), overlayAnimationContainer.getPrevAnimationId()) && overlayAnimationContainer.getAnimationPriority() >= overlayAnimationContainer.getPrevAnimationPriority()) ||
                !overlayAnimationContainer.getAnimationController().isActive()) {

            if (!overlayAnimationContainer.getPrevAnimationId().equals(overlayAnimationContainer.getCurrentAnimationId())) {
                playCurrentAnimation(overlayAnimationContainer.getAnimationController(), overlayAnimationContainer.getCurrentAnimation());
            } else {
                overlayAnimationContainer.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
                overlayAnimationContainer.setCurrentAnimationId(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId());
                playCurrentAnimation(overlayAnimationContainer.getAnimationController(), overlayAnimationContainer.getCurrentAnimation());
            }

            overlayAnimationContainer.setPrevAnimationId(overlayAnimationContainer.getCurrentAnimationId());
            overlayAnimationContainer.setPrevAnimationPriority(overlayAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    private void updateUpHandAnimationContainer() {
        if ((!Objects.equals(specialAnimationContainer.getCurrentAnimationId(), specialAnimationContainer.getPrevAnimationId()) && specialAnimationContainer.getAnimationPriority() >= overlayAnimationContainer.getPrevAnimationPriority()) ||
                !specialAnimationContainer.getAnimationController().isActive()) {

            playCurrentAnimation(specialAnimationContainer.getAnimationController(), specialAnimationContainer.getCurrentAnimation());

            specialAnimationContainer.setPrevAnimationId(specialAnimationContainer.getCurrentAnimationId());
            specialAnimationContainer.setPrevAnimationPriority(specialAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    public void playCurrentAnimation(AnimationController animationController, Animation animation) {
        animationController.replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(mainAnimationContainer.getAnimationFadeTime(), EasingType.EASE_IN_OUT_SINE),
                RawAnimation.begin().thenPlay(animation), false
        );
    }

    @Unique
    private void checkMainHandItemForArmDisabling() {
        if (!getMainHandItem().isEmpty() && isUsingItem() && (isScoping() || getMainHandItem().getItem() instanceof InstrumentItem || getMainHandItem().getItem() instanceof BrushItem)) {
            this.disableBodyPartAnimationInAllContainers(getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        }
    }

    @Unique
    private void checkOffHandItemForArmDisabling() {
        if (!getOffhandItem().isEmpty() && isUsingItem() && (isScoping() || getMainHandItem().getItem() instanceof InstrumentItem || getMainHandItem().getItem() instanceof BrushItem)) {
            this.disableBodyPartAnimationInAllContainers(getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
        }
    }
}