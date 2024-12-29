package dev.razorplay.customplayeranimations.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.*;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.animation.animations.AnimationProvider;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimatedPlayer;
import dev.razorplay.customplayeranimations.util.interfaces.IAnimationControl;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import dev.razorplay.customplayeranimations.util.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
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

import static dev.kosmx.playerAnim.core.util.Ease.INOUTSINE;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.*;
import static dev.razorplay.customplayeranimations.util.CustomModifiers.*;
import static dev.razorplay.customplayeranimations.util.Util.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player implements IAnimationControl, ICustomAnimatedPlayer {
    @Unique
    private final FirstPersonModifier firstPersonModifier = new FirstPersonModifier();

    @Unique
    private final AnimationContainer mainAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                            Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier())),
            getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
            AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
            "",
            0,
            0,
            0,
            0);

    @Unique
    private final AnimationContainer overlayAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                            Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(),
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

    @Unique
    private final AnimationContainer specialAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(),
                            Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                            Modifiers.FIRST_PERSON_MODIFIER.getModifierId(), firstPersonModifier)),
            getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
            AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
            "",
            0,
            0,
            0,
            0);

    @Unique
    private final PlayerData playerData = new PlayerData();

    @Unique
    private AnimationContext actualAnimationContext;

    protected AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel world, GameProfile profile, CallbackInfo info) {
        // Main Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1, mainAnimationContainer.getAnimationModifierLayer());
        addModifiersToContainer(mainAnimationContainer);

        // Overlay Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(2, overlayAnimationContainer.getAnimationModifierLayer());
        addModifiersToContainer(overlayAnimationContainer);

        // UpHand Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(3, specialAnimationContainer.getAnimationModifierLayer());
        addModifiersToContainer(specialAnimationContainer);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        // Update Player Data
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
    public void disableBodyPartAnimation(AnimationContainer animationContainer, BodyParts bodyPart) {
        KeyframeAnimation.AnimationBuilder internalBuilder = animationContainer.getCurrentAnimation().mutableCopy();
        var part = internalBuilder.getPart(bodyPart.getPartId());
        if (part != null) {
            part.setEnabled(false);
        }
        animationContainer.setCurrentAnimation(internalBuilder.build());
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
    public void setMainArmPose(HumanoidModel.ArmPose armPosition) {
        this.playerData.setMainArmPose(armPosition);
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
                !mainAnimationContainer.getAnimationModifierLayer().isActive()) {

            playCurrentAnimation(mainAnimationContainer.getAnimationModifierLayer(), mainAnimationContainer.getCurrentAnimation());

            mainAnimationContainer.setPrevAnimationId(mainAnimationContainer.getCurrentAnimationId());
            mainAnimationContainer.setPrevAnimationPriority(mainAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    private void updateOverlayAnimationContainer() {
        if ((!Objects.equals(overlayAnimationContainer.getCurrentAnimationId(), overlayAnimationContainer.getPrevAnimationId()) && overlayAnimationContainer.getAnimationPriority() >= overlayAnimationContainer.getPrevAnimationPriority()) ||
                !overlayAnimationContainer.getAnimationModifierLayer().isActive()) {

            if (!overlayAnimationContainer.getPrevAnimationId().equals(overlayAnimationContainer.getCurrentAnimationId())) {
                playCurrentAnimation(overlayAnimationContainer.getAnimationModifierLayer(), overlayAnimationContainer.getCurrentAnimation());
            } else {
                overlayAnimationContainer.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
                overlayAnimationContainer.setCurrentAnimationId(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId());
                playCurrentAnimation(overlayAnimationContainer.getAnimationModifierLayer(), overlayAnimationContainer.getCurrentAnimation());
            }

            overlayAnimationContainer.setPrevAnimationId(overlayAnimationContainer.getCurrentAnimationId());
            overlayAnimationContainer.setPrevAnimationPriority(overlayAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    private void updateUpHandAnimationContainer() {
        if ((!Objects.equals(specialAnimationContainer.getCurrentAnimationId(), specialAnimationContainer.getPrevAnimationId()) && specialAnimationContainer.getAnimationPriority() >= overlayAnimationContainer.getPrevAnimationPriority()) ||
                !specialAnimationContainer.getAnimationModifierLayer().isActive()) {

            playCurrentAnimation(specialAnimationContainer.getAnimationModifierLayer(), specialAnimationContainer.getCurrentAnimation());

            specialAnimationContainer.setPrevAnimationId(specialAnimationContainer.getCurrentAnimationId());
            specialAnimationContainer.setPrevAnimationPriority(specialAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    public void playCurrentAnimation(ModifierLayer<IAnimation> animationContainer, KeyframeAnimation animation) {
        modifyFirstPersonConfig();
        animationContainer.replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(mainAnimationContainer.getAnimationFadeTime(), INOUTSINE),
                new KeyframeAnimationPlayer(animation).setFirstPersonMode(firstPersonModifier.getFirstPersonMode(0)),
                true
        );
    }

    @Unique
    private void modifyFirstPersonConfig() {
        if (CONFIG.isCustomFirstPersonEnable()) {
            boolean condition = containsAnyAnimation(overlayAnimationContainer, CONFIG.getAnimationsThatShowBothHand()) ||
                    containsAnyAnimation(mainAnimationContainer, CONFIG.getAnimationsThatShowBothHand()) ||
                    getOffhandItem().getItem() != Items.AIR ||
                    this.playerData.getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    this.playerData.getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE);

            firstPersonModifier.setCurrentFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);

            if (condition) {
                firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.ENABLE_BOTH_ARMS);
            } else {
                firstPersonModifier.setCurrentFirstPersonConfig(getMainArm() == HumanoidArm.RIGHT ? FirstPersonModifier.FirstPersonConfigEnum.ONLY_RIGHT_ARM_AND_ITEM : FirstPersonModifier.FirstPersonConfigEnum.ONLY_LEFT_ARM_AND_ITEM);
            }
            if (isScoping() || isSpectator()) {
                firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.DISABLE_BOTH_ARMS);
            }
        } else {
            firstPersonModifier.setCurrentFirstPersonMode(FirstPersonMode.DISABLED);
            firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.ENABLE_BOTH_ARMS);
        }
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