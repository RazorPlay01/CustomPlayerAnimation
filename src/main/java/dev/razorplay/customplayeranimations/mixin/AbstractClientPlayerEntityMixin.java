package dev.razorplay.customplayeranimations.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.*;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.animation.animations.CompatAnimation;
import dev.razorplay.customplayeranimations.animation.animations.base.*;
import dev.razorplay.customplayeranimations.animation.animations.overlay.*;
import dev.razorplay.customplayeranimations.animation.animations.special.ItemSwapAnimation;
import dev.razorplay.customplayeranimations.animation.animations.special.UpHandAnimation;
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
import static dev.razorplay.customplayeranimations.util.Util.disableArmInBuilder;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player implements IAnimationControl, ICustomAnimatedPlayer {
    @Unique
    private final AnimationContainer mainAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                            Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier())),
            null,
            "",
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
                            Modifiers.RIGHT_BOW_MODIFIER.getModifierId(), createBowModifier(true),
                            Modifiers.LEFT_BOW_MODIFIER.getModifierId(), createBowModifier(false))),
            null,
            "",
            "",
            0,
            0,
            0,
            1);

    @Unique
    private final AnimationContainer specialAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier())),
            null,
            "",
            "",
            10,
            0,
            0,
            1);

    @Unique
    private final PlayerData playerData = new PlayerData();

    @Unique
    private final FirstPersonModifier firstPersonModifier = new FirstPersonModifier();

    protected AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel world, GameProfile profile, CallbackInfo info) {
        // Main Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1, mainAnimationContainer.getAnimationModifierLayer());
        mainAnimationContainer.getAnimationModifierLayer().addModifierLast(mainAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId()));
        mainAnimationContainer.getAnimationModifierLayer().addModifierLast(mainAnimationContainer.getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()));
        ((MirrorModifier) mainAnimationContainer.getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(false);

        // Overlay Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(2, overlayAnimationContainer.getAnimationModifierLayer());
        //Bow Modifier
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(Modifiers.RIGHT_BOW_MODIFIER.getModifierId()));
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(Modifiers.LEFT_BOW_MODIFIER.getModifierId()));
        ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.RIGHT_BOW_MODIFIER.getModifierId())).enabled = false;
        ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.LEFT_BOW_MODIFIER.getModifierId())).enabled = false;
        //Speed and Mirror Modifier
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId()));
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()));
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(false);

        // UpHand Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(3, specialAnimationContainer.getAnimationModifierLayer());
        specialAnimationContainer.getAnimationModifierLayer().addModifierLast(specialAnimationContainer.getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()));
        ((MirrorModifier) specialAnimationContainer.getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(false);

        mainAnimationContainer.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
        overlayAnimationContainer.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
        specialAnimationContainer.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));

        // FirstPerson Modifier
        specialAnimationContainer.getAnimationModifierLayer().addModifierBefore(firstPersonModifier);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        // Update Player Data
        playerData.update((AbstractClientPlayer) (Object) this);

        overlayAnimationContainer.resetAnimationProperties();

        playAnimationSequence();
        updateAnimationSpeeds();

        checkMainHandItemForArmDisabling();
        checkOffHandItemForArmDisabling();

        updateAnimationContainers();

        playerData.setPrevPlayerPosition(playerData.getPlayerPosition());
        playerData.setPrevPlayerBodyYaw(playerData.getPlayerBodyYaw());
        playerData.setPrevOnGround(onGround());
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
    public void setMainArmPose(HumanoidModel.ArmPose armPosition) {
        playerData.setMainArmPose(armPosition);
    }

    @Override
    public void setOffArmPose(HumanoidModel.ArmPose armPosition) {
        playerData.setOffArmPose(armPosition);
    }

    @Unique
    private AdjustmentModifier createBowModifier(boolean isRight) {
        return new AdjustmentModifier(partName -> {
            float pitch = (float) Math.toRadians(getXRot());
            String mainArm = isRight ? BodyParts.RIGHT_ARM.getPartId() : BodyParts.LEFT_ARM.getPartId();
            String offArm = isRight ? BodyParts.LEFT_ARM.getPartId() : BodyParts.RIGHT_ARM.getPartId();

            if (partName.equals(mainArm)) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(0, 0, isRight ? -pitch : pitch),
                        new Vec3f(0, pitch, 0))
                );
            } else if (partName.equals(offArm)) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(0, 0, isRight ? pitch * 0.25f : -pitch * 0.25f),
                        new Vec3f(0, -pitch, 0))
                );
            }

            return Optional.empty();
        });
    }

    @Unique
    private void updateAnimationSpeeds() {
        ((SpeedModifier) mainAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = mainAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
        ((SpeedModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = overlayAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
    }

    @Unique
    private void playAnimationSequence() {
        AnimationContext actualAnimationContext = new AnimationContext(mainAnimationContainer, overlayAnimationContainer, specialAnimationContainer, (AbstractClientPlayer) (Object) this, playerData);
        // Main Animations
        IdleStandingAnimation.playAnimation(actualAnimationContext);
        IdleSneakAnimation.playAnimation(actualAnimationContext);
        OnEdgeIdleAnimation.playAnimation(actualAnimationContext);
        OnFenceIdleAnimation.playAnimation(actualAnimationContext);
        TurnAnimation.playAnimation(actualAnimationContext);
        TurnSneakAnimation.playAnimation(actualAnimationContext);
        WalkAnimation.playAnimation(actualAnimationContext);
        WalkBackwardsAnimation.playAnimation(actualAnimationContext);
        RunAnimation.playAnimation(actualAnimationContext);
        OnFenceWalkAnimation.playAnimation(actualAnimationContext);
        WalkSneakAnimation.playAnimation(actualAnimationContext);
        WalkSneakBackwardsAnimation.playAnimation(actualAnimationContext);
        FlyAnimation.playAnimation(actualAnimationContext);
        FallAnimation.playAnimation(actualAnimationContext);
        ClimbAnimations.playAnimation(actualAnimationContext);
        CrawlAnimations.playAnimation(actualAnimationContext);
        InWaterIdleAnimation.playAnimation(actualAnimationContext);
        InWaterForwardAnimation.playAnimation(actualAnimationContext);
        InWaterBackwardsAnimation.playAnimation(actualAnimationContext);
        InWaterUpAnimation.playAnimation(actualAnimationContext);
        InWaterSwimAnimation.playAnimation(actualAnimationContext);
        MountAnimation.playAnimation(actualAnimationContext);
        MinecartAnimation.playAnimation(actualAnimationContext);
        HorseIdleAnimation.playAnimation(actualAnimationContext);
        HorseRunningAnimation.playAnimation(actualAnimationContext);
        HorseRunningBackwardsAnimation.playAnimation(actualAnimationContext);
        BoatTurnAnimations.playAnimation(actualAnimationContext);
        BoatForwardAnimation.playAnimation(actualAnimationContext);
        BoatIdleAnimation.playAnimation(actualAnimationContext);
        ElytraAnimation.playAnimation(actualAnimationContext);
        SleepAnimation.playAnimation(actualAnimationContext);
        // Overlay Animations
        // Use Items
        EatAnimation.playAnimation(actualAnimationContext);
        TridentAnimation.playAnimation(actualAnimationContext);
        BowAnimation.playAnimation(actualAnimationContext);
        ShieldAnimation.playAnimation(actualAnimationContext);
        CrossbowAnimation.playAnimation(actualAnimationContext);
        CompatAnimation.playAnimation(actualAnimationContext);
        // Handswing
        GenericHandSwingAnimation.playAnimation(actualAnimationContext);
        PickaxeAnimation.playAnimation(actualAnimationContext);
        AxeAnimation.playAnimation(actualAnimationContext);
        ShovelAnimation.playAnimation(actualAnimationContext);
        SwordAnimation.playAnimation(actualAnimationContext);
        // UpHand Animation
        UpHandAnimation.playAnimation(actualAnimationContext);
        ItemSwapAnimation.playAnimation(actualAnimationContext);
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

            ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.RIGHT_BOW_MODIFIER.getModifierId())).enabled = false;
            ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.LEFT_BOW_MODIFIER.getModifierId())).enabled = false;

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
            boolean condition = overlayAnimationContainer.getCurrentAnimationId().contains("trident") ||
                    overlayAnimationContainer.getCurrentAnimationId().contains("bow") ||
                    mainAnimationContainer.getCurrentAnimationId().contains("climbing") ||
                    mainAnimationContainer.getCurrentAnimationId().contains("boat") ||
                    mainAnimationContainer.getCurrentAnimationId().contains("horse") ||
                    mainAnimationContainer.getCurrentAnimationId().contains("minecart") ||
                    mainAnimationContainer.getCurrentAnimationId().contains("water") ||
                    mainAnimationContainer.getCurrentAnimationId().contains("crawl") ||
                    getOffhandItem().getItem() != Items.AIR ||
                    playerData.getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    playerData.getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE);

            firstPersonModifier.setCurrentFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);

            if (condition) {
                firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.ENABLE_BOTH_ARMS);
            } else {
                firstPersonModifier.setCurrentFirstPersonConfig(getMainArm() == HumanoidArm.RIGHT ? FirstPersonModifier.FirstPersonConfigEnum.ONLY_RIGHT_ARM_AND_ITEM : FirstPersonModifier.FirstPersonConfigEnum.ONLY_LEFT_ARM_AND_ITEM);
            }
            if (isScoping()) {
                firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.DISABLE_BOTH_ARMS);
            }
        } else {
            firstPersonModifier.setCurrentFirstPersonMode(FirstPersonMode.DISABLED);
            firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.ENABLE_BOTH_ARMS);
        }
    }

    @Unique
    private void checkMainHandItemForArmDisabling() {
        if (!getMainHandItem().isEmpty() && (isScoping() || getMainHandItem().getItem() instanceof InstrumentItem || getMainHandItem().getItem() instanceof BrushItem)) {
            disableArmInBuilder(mainAnimationContainer, overlayAnimationContainer, specialAnimationContainer, BodyParts.RIGHT_ARM);
        }
    }

    @Unique
    private void checkOffHandItemForArmDisabling() {
        if (!getOffhandItem().isEmpty() && (isScoping() || getMainHandItem().getItem() instanceof InstrumentItem || getMainHandItem().getItem() instanceof BrushItem)) {
            disableArmInBuilder(mainAnimationContainer, overlayAnimationContainer, specialAnimationContainer, BodyParts.LEFT_ARM);
        }
    }
}