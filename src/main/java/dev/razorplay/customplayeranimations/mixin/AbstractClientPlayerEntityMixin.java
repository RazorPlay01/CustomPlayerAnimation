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
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimatedPlayer;
import dev.razorplay.customplayeranimations.util.interfaces.ITorsoControl;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import dev.razorplay.customplayeranimations.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static dev.kosmx.playerAnim.core.util.Ease.INOUTSINE;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.*;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;
import static java.lang.Math.*;
import static net.minecraft.world.InteractionHand.*;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player implements ITorsoControl, ICustomAnimatedPlayer {
    @Unique
    private KeyframeAnimation.AnimationBuilder builder = null;

    @Unique
    private final AnimationContainer mainAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(ModifiersEnum.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                            ModifiersEnum.SPEED_MODIFIER.getModifierId(), new SpeedModifier())),
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
                    Map.of(ModifiersEnum.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
                            ModifiersEnum.SPEED_MODIFIER.getModifierId(), new SpeedModifier(),
                            ModifiersEnum.RIGHT_BOW_MODIFIER.getModifierId(), createBowModifier(true),
                            ModifiersEnum.LEFT_BOW_MODIFIER.getModifierId(), createBowModifier(false))),
            null,
            "",
            "",
            0,
            0,
            0,
            1);
    @Unique
    private final AnimationContainer upHandAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            new HashMap<>(
                    Map.of(ModifiersEnum.MIRROR_MODIFIER.getModifierId(), new MirrorModifier())),
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
        initializeAnimationLayers();
    }

    @Unique
    private void initializeAnimationLayers() {
        // Main Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1, mainAnimationContainer.getAnimationModifierLayer());
        mainAnimationContainer.getAnimationModifierLayer().addModifierLast(mainAnimationContainer.getAnimationModifiers().get(ModifiersEnum.SPEED_MODIFIER.getModifierId()));
        mainAnimationContainer.getAnimationModifierLayer().addModifierLast(mainAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId()));
        ((MirrorModifier) mainAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(false);

        // Overlay Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(2, overlayAnimationContainer.getAnimationModifierLayer());
        //Bow Modifier
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.RIGHT_BOW_MODIFIER.getModifierId()));
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.LEFT_BOW_MODIFIER.getModifierId()));
        ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.RIGHT_BOW_MODIFIER.getModifierId())).enabled = false;
        ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.LEFT_BOW_MODIFIER.getModifierId())).enabled = false;
        //Speed and Mirror Modifier
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.SPEED_MODIFIER.getModifierId()));
        overlayAnimationContainer.getAnimationModifierLayer().addModifierLast(overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId()));
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(false);

        // UpHand Animation Container
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(3, upHandAnimationContainer.getAnimationModifierLayer());
        upHandAnimationContainer.getAnimationModifierLayer().addModifierLast(upHandAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId()));
        ((MirrorModifier) upHandAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(false);

        mainAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.animation());
        overlayAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.animation());
        upHandAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.animation());

        // FirstPerson Modifier
        upHandAnimationContainer.getAnimationModifierLayer().addModifierBefore(firstPersonModifier);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        animatePlayer();
    }

    @Override
    public ModifierLayer<IAnimation> customPlayerAnimations_getModAnimation() {
        return mainAnimationContainer.getAnimationModifierLayer();
    }

    @Override
    public void disableArmsAnimation(boolean disableArms) {
        if (disableArms) {
            disableBothArms();
        }
    }

    @Override
    public void disableRightArmAnimation(boolean disableRightArm) {
        if (disableRightArm) {
            disableArmInBuilder(ArmsEnum.RIGHT_ARM);
        }
    }

    @Override
    public void disableLeftArmAnimation(boolean disableLeftArm) {
        if (disableLeftArm) {
            disableArmInBuilder(ArmsEnum.LEFT_ARM);
        }
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
    private void disableActiveArm() {
        if (getUsedItemHand().equals(MAIN_HAND)) {
            if (getMainArm() == HumanoidArm.RIGHT) {
                disableArmInBuilder(ArmsEnum.RIGHT_ARM);
            } else {
                disableArmInBuilder(ArmsEnum.LEFT_ARM);
            }
        } else {
            if (getMainArm() == HumanoidArm.RIGHT) {
                disableArmInBuilder(ArmsEnum.LEFT_ARM);
            } else {
                disableArmInBuilder(ArmsEnum.RIGHT_ARM);
            }
        }
    }

    @Unique
    private void disableArmInBuilder(ArmsEnum arm) {
        var armPart = builder.getPart(arm.getArmId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
        }
    }

    @Unique
    private AdjustmentModifier createBowModifier(boolean isRight) {
        return new AdjustmentModifier(partName -> {
            float pitch = (float) Math.toRadians(getXRot());
            String mainArm = isRight ? ArmsEnum.RIGHT_ARM.getArmId() : ArmsEnum.LEFT_ARM.getArmId();
            String offArm = isRight ? ArmsEnum.LEFT_ARM.getArmId() : ArmsEnum.RIGHT_ARM.getArmId();

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
    public void animatePlayer() {
        LocalPlayer player = Minecraft.getInstance().player;
        player.displayClientMessage(Component.literal(String.valueOf(playerData.getMovementSpeed())), true);
        updateHandOrientation();
        updatePlayerPosition();
        updateMovementInfo();
        updateEnvironmentInfo();

        resetOverlayAnimationProperties();

        playAnimationSequence();
        updateAnimationSpeeds();

        handleArmDisabling();
        updateAnimationContainers();

        playerData.setPrevPlayerPosition(playerData.getPlayerPosition());
        playerData.setPrevPlayerBodyYaw(playerData.getPlayerBodyYaw());
    }

    @Unique
    private void updateHandOrientation() {
        if (getMainArm() == HumanoidArm.RIGHT) {
            playerData.setRightHand(MAIN_HAND);
            playerData.setLeftHand(OFF_HAND);
        } else {
            playerData.setRightHand(OFF_HAND);
            playerData.setLeftHand(MAIN_HAND);
        }
    }

    @Unique
    private void updateAnimationSpeeds() {
        ((SpeedModifier) mainAnimationContainer.getAnimationModifiers().get(ModifiersEnum.SPEED_MODIFIER.getModifierId())).speed = mainAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
        ((SpeedModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.SPEED_MODIFIER.getModifierId())).speed = overlayAnimationContainer.getAnimationSpeed() * CONFIG.getAnimationSpeedMultiplier();
    }

    @Unique
    private void updatePlayerPosition() {
        playerData.setPlayerBodyYaw(getVisualRotationYInDegrees());
        playerData.setPlayerHeadYaw(getYHeadRot());
        playerData.setPlayerPosition(position());
        playerData.setVectorX((float) (playerData.getPlayerPosition().x - playerData.getPrevPlayerPosition().x));
        playerData.setVectorY((float) (playerData.getPlayerPosition().y - playerData.getPrevPlayerPosition().y));
        playerData.setVectorZ((float) (playerData.getPlayerPosition().z - playerData.getPrevPlayerPosition().z));
        playerData.setMovementSpeed(sqrt(playerData.getVectorX() * playerData.getVectorX() + playerData.getVectorZ() * playerData.getVectorZ()));
        playerData.setBodyYawDelta(playerData.getPlayerBodyYaw() - playerData.getPrevPlayerBodyYaw());
    }

    @Unique
    private void updateMovementInfo() {
        double bodyYawRadians = toRadians(yBodyRot + 90);
        Vector3f movementVector = new Vector3f(playerData.getVectorX(), 0, playerData.getVectorZ());
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        playerData.setMovingBackwards(movementVector.length() > 0 && movementVector.dot(lookVector) < 0);
    }

    @Unique
    private void updateEnvironmentInfo() {
        Block standingBlock = this.level().getBlockState(blockPosition().below()).getBlock();
        playerData.setOnFence((standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof IronBarsBlock) && onGround());
        playerData.setOnEdge(standingBlock instanceof AirBlock && onGround());
    }

    @Unique
    private void resetOverlayAnimationProperties() {
        overlayAnimationContainer.disableAnimation();
        overlayAnimationContainer.setAnimationFadeTime(10);
        overlayAnimationContainer.setAnimationSpeed(1);
        overlayAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playAnimationSequence() {
        // Main Animations
        IdleStandingAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        IdleSneakAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        OnEdgeIdleAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        OnFenceIdleAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        TurnAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        TurnSneakAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        WalkAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        WalkBackwardsAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        RunAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        OnFenceWalkAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        WalkSneakAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        WalkSneakBackwardsAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        FlyAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        FallAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        ClimbAnimations.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        CrawlAnimations.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        InWaterForwardAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        InWaterBackwardsAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        InWaterUpAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        InWaterIdleAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        InWaterSwimAnimations.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        MountAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        MinecartAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        HorseIdleAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        HorseRunningAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        BoatTurnAnimations.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        BoatForwardAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        BoatIdleAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        ElytraAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        SleepAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        // Overlay Animations
        // Use Items
        EatAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        TridentAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        BowAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        ShieldAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        CrossbowAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        CompatAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        // Handswing
        GenericHandswingAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        PickaxeAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        AxeAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        ShovelAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        SwordAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
        // UpHand Animation
        UpHandAnimation.playAnimation(new AnimationContext(mainAnimationContainer, overlayAnimationContainer, upHandAnimationContainer, (AbstractClientPlayer) (Object) this, playerData));
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

            ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.RIGHT_BOW_MODIFIER.getModifierId())).enabled = false;
            ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.LEFT_BOW_MODIFIER.getModifierId())).enabled = false;

            if (!overlayAnimationContainer.getPrevAnimationId().equals(overlayAnimationContainer.getCurrentAnimationId())) {
                playCurrentAnimation(overlayAnimationContainer.getAnimationModifierLayer(), overlayAnimationContainer.getCurrentAnimation());
            } else {
                overlayAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.animation());
                overlayAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.animationId());
                playCurrentAnimation(overlayAnimationContainer.getAnimationModifierLayer(), overlayAnimationContainer.getCurrentAnimation());
            }

            overlayAnimationContainer.setPrevAnimationId(overlayAnimationContainer.getCurrentAnimationId());
            overlayAnimationContainer.setPrevAnimationPriority(overlayAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    private void updateUpHandAnimationContainer() {
        if ((!Objects.equals(upHandAnimationContainer.getCurrentAnimationId(), upHandAnimationContainer.getPrevAnimationId()) && upHandAnimationContainer.getAnimationPriority() >= overlayAnimationContainer.getPrevAnimationPriority()) ||
                !upHandAnimationContainer.getAnimationModifierLayer().isActive()) {

            playCurrentAnimation(upHandAnimationContainer.getAnimationModifierLayer(), upHandAnimationContainer.getCurrentAnimation());

            upHandAnimationContainer.setPrevAnimationId(upHandAnimationContainer.getCurrentAnimationId());
            upHandAnimationContainer.setPrevAnimationPriority(upHandAnimationContainer.getAnimationPriority());
        }
    }

    @Unique
    private void handleArmDisabling() {
        checkMainHandItemForArmDisabling();
        checkOffHandItemForArmDisabling();
    }

    @Unique
    private void checkMainHandItemForArmDisabling() {
        if (!getMainHandItem().isEmpty()) {
            boolean isInstrument = getMainHandItem().getItem() instanceof InstrumentItem;
            boolean isBrush = getMainHandItem().getItem() instanceof BrushItem;

            // Check if the arm pose is for a crossbow or bow, and if the offhand item is not a bow
            boolean condition = isScoping() || isInstrument || isBrush;

            if (condition) {
                disableArm(ArmsEnum.RIGHT_ARM);
            }
        }
    }

    @Unique
    private void checkOffHandItemForArmDisabling() {
        if (!getOffhandItem().isEmpty()) {
            boolean isInstrument = getMainHandItem().getItem() instanceof InstrumentItem;
            boolean isBrush = getMainHandItem().getItem() instanceof BrushItem;

            boolean condition = isScoping() || isInstrument || isBrush;

            if (condition) {
                disableArm(ArmsEnum.LEFT_ARM);
            }
        }
    }

    @Unique
    public void playCurrentAnimation(ModifierLayer<IAnimation> animationContainer, KeyframeAnimation animation) {
        modifyFirstPersonConfig();
        this.builder = animation.mutableCopy();

        animationContainer.replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(mainAnimationContainer.getAnimationFadeTime(), INOUTSINE),
                new KeyframeAnimationPlayer(animation).setFirstPersonMode(firstPersonModifier.getFirstPersonMode(0)),
                true
        );
    }

    @Unique
    private void modifyFirstPersonConfig() {
        boolean condition = overlayAnimationContainer.getCurrentAnimationId().contains("trident") ||
                overlayAnimationContainer.getCurrentAnimationId().contains("bow") ||
                mainAnimationContainer.getCurrentAnimationId().contains("climbing") ||
                mainAnimationContainer.getCurrentAnimationId().contains("boat") ||
                mainAnimationContainer.getCurrentAnimationId().contains("horse") ||
                mainAnimationContainer.getCurrentAnimationId().contains("minecart") ||
                mainAnimationContainer.getCurrentAnimationId().contains("water") ||
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
    }

    @Unique
    public void disableArm(ArmsEnum arm) {
        builder = mainAnimationContainer.getCurrentAnimation().mutableCopy();
        disableArmInBuilder(arm);
        mainAnimationContainer.setCurrentAnimation(builder.build());
    }

    @Unique
    public void disableBothArms() {
        builder = mainAnimationContainer.getCurrentAnimation().mutableCopy();
        disableArmInBuilder(ArmsEnum.RIGHT_ARM);
        disableArmInBuilder(ArmsEnum.LEFT_ARM);
        mainAnimationContainer.setCurrentAnimation(builder.build());
    }
}