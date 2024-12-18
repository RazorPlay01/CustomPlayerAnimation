package dev.razorplay.customplayeranimations.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.*;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.animation.PlayerAnimations;
import dev.razorplay.customplayeranimations.compat.CarryOnCompat;
import dev.razorplay.customplayeranimations.compat.SupplementariesCompat;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.util.ArmsEnum;
import dev.razorplay.customplayeranimations.util.ICustomAnimatedPlayer;
import dev.razorplay.customplayeranimations.util.ITorsoControl;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.kosmx.playerAnim.core.util.Ease.INOUTSINE;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.*;
import static dev.razorplay.customplayeranimations.animation.PlayerAnimations.Animations.*;
import static dev.razorplay.customplayeranimations.util.Util.*;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static net.minecraft.world.InteractionHand.*;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player implements ITorsoControl, ICustomAnimatedPlayer {
    @Shadow
    @Final
    public ClientLevel clientLevel;

    @Unique
    private final FirstPersonModifier firstPersonModifier = new FirstPersonModifier();

    @Unique
    private final ModifierLayer<IAnimation> modBaseAnimationContainer = new ModifierLayer<>();
    @Unique
    private final MirrorModifier animationMirrorModifier = new MirrorModifier();
    @Unique
    private final SpeedModifier animationSpeedModifier = new SpeedModifier();
    @Unique
    private KeyframeAnimation currentAnimation = null;
    @Unique
    private String currentAnimationId = "";
    @Unique
    private String prevAnimationId = "";
    @Unique
    private int fadeTime = 0;
    @Unique
    private int priority = 0;
    @Unique
    private int prevPriority = 0;
    @Unique
    private float animationSpeed;

    @Unique
    private final ModifierLayer<IAnimation> modOverlayAnimationContainer = new ModifierLayer<>();
    @Unique
    private final MirrorModifier overlayMirrorModifier = new MirrorModifier();
    @Unique
    private final SpeedModifier overlaySpeedModifier = new SpeedModifier();
    @Unique
    private final AdjustmentModifier rightBowModifier = createBowModifier(true);
    @Unique
    private final AdjustmentModifier leftBowModifier = createBowModifier(false);
    @Unique
    private KeyframeAnimation currentOverlayAnimation = null;
    @Unique
    private String currentOverlayId = "";
    @Unique
    private String prevOverlayId = "";
    @Unique
    private int overlayFadeTime = 0;
    @Unique
    private int overlayPriority = 0;
    @Unique
    private int prevOverlayPriority = 0;
    @Unique
    private float overlayAnimationSpeed = 1;

    @Unique
    private final AnimationContainer upHandAnimationContainer = new AnimationContainer(
            new ModifierLayer<>(),
            List.of(new MirrorModifier(),
                    createUpHandModifier(true),
                    createUpHandModifier(false),
                    firstPersonModifier),
            null,
            "",
            "",
            0,
            0,
            0
    );

    @Unique
    private KeyframeAnimation.AnimationBuilder builder = null;
    @Unique
    private InteractionHand rightHand = MAIN_HAND;
    @Unique
    private InteractionHand leftHand = OFF_HAND;


    @Unique
    private int flyChecker = 0;
    @Unique
    private float playerBodyYaw = 0;
    @Unique
    private float playerHeadYaw = 0;
    @Unique
    private float vectorX = 0;
    @Unique
    private float vectorY = 0;
    @Unique
    private float vectorZ = 0;
    @Unique
    private float bodyYawDelta = 0;
    @Unique
    private float prevBodyYaw = 0;

    @Unique
    private double moveSpeed = 0;

    @Unique
    private boolean isMovingBackwards = false;

    @Unique
    private boolean isOnFence = false;
    @Unique
    private boolean isOnEdge = false;


    @Unique
    private Vec3 playerPosition;
    @Unique
    private Vec3 lastPlayerPosition = new Vec3(0, 0, 0);

    @Unique
    private HumanoidModel.ArmPose mainArmPosition = HumanoidModel.ArmPose.EMPTY;
    @Unique
    private HumanoidModel.ArmPose offArmPosition = HumanoidModel.ArmPose.EMPTY;

    protected AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel world, GameProfile profile, CallbackInfo info) {
        initializeAnimationLayers();
    }

    @Unique
    private void initializeAnimationLayers() {

        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1, modBaseAnimationContainer);
        modBaseAnimationContainer.addModifierLast(animationSpeedModifier);
        modBaseAnimationContainer.addModifierLast(animationMirrorModifier);
        animationMirrorModifier.setEnabled(false);

        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(2, modOverlayAnimationContainer);
        modOverlayAnimationContainer.addModifierLast(rightBowModifier);
        rightBowModifier.enabled = false;
        modOverlayAnimationContainer.addModifierLast(leftBowModifier);
        leftBowModifier.enabled = false;

        //PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(3, upHandAnimationContainer.getAnimationModifierLayer());
        for (AbstractModifier modifier : upHandAnimationContainer.getAnimationModifiers()) {
            upHandAnimationContainer.getAnimationModifierLayer().addModifierLast(modifier);
            if (modifier instanceof MirrorModifier mirrorModifier) {
                mirrorModifier.setEnabled(false);
            }
            if (modifier instanceof AdjustmentModifier adjustmentModifier) {
                adjustmentModifier.enabled = false;
            }
        }
        upHandAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());

        currentAnimation = IDLE_STANDING_ANIMATION.getAnimation();
        currentOverlayAnimation = BLANK_LOOP_ANIMATION.getAnimation();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        animatePlayer();
    }

    @Override
    public ModifierLayer<IAnimation> customPlayerAnimations_getModAnimation() {
        return modBaseAnimationContainer;
    }

    @Override
    public void setMainArmPosition(HumanoidModel.ArmPose armPosition) {
        this.mainArmPosition = armPosition;
    }

    @Override
    public void setOffArmPosition(HumanoidModel.ArmPose armPosition) {
        this.offArmPosition = armPosition;
    }

    @Override
    public void disableArmsAnimation(boolean b) {
        if (b) {
            disableBothArms();
        }
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
    public void animatePlayer() {
        updateHandOrientation();
        updatePlayerPosition();
        updateMovementInfo();
        updateEnvironmentInfo();

        disableAnimationOverlay();
        //disableAnimationUpHand();
        resetOverlayProperties();

        playAnimationSequence();
        updateAnimationSpeeds();

        if (IS_CARRYON_LOADED) {
            CarryOnCompat.check((AbstractClientPlayer) (Object) this);
        }

        handleArmDisabling();
        updateAnimationContainers();

        lastPlayerPosition = playerPosition;
        prevBodyYaw = playerBodyYaw;
    }

    @Unique
    private void updateHandOrientation() {
        if (getMainArm() == HumanoidArm.LEFT) {
            rightHand = OFF_HAND;
            leftHand = MAIN_HAND;
        } else {
            rightHand = MAIN_HAND;
            leftHand = OFF_HAND;
        }
    }

    @Unique
    private void updateAnimationSpeeds() {
        animationSpeedModifier.speed = animationSpeed * CONFIG.getAnimationSpeedMultiplier();
        overlaySpeedModifier.speed = overlayAnimationSpeed * CONFIG.getAnimationSpeedMultiplier();
    }

    @Unique
    private void updatePlayerPosition() {
        playerBodyYaw = getVisualRotationYInDegrees();
        playerHeadYaw = getYHeadRot();
        playerPosition = position();
        vectorX = (float) (playerPosition.x - lastPlayerPosition.x);
        vectorY = (float) (playerPosition.y - lastPlayerPosition.y);
        vectorZ = (float) (playerPosition.z - lastPlayerPosition.z);
        moveSpeed = sqrt(vectorX * vectorX + vectorZ * vectorZ);
        bodyYawDelta = playerBodyYaw - prevBodyYaw;
    }

    @Unique
    private void updateMovementInfo() {
        double bodyYawRadians = toRadians(yBodyRot + 90);
        Vector3f movementVector = new Vector3f(vectorX, 0, vectorZ);
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        isMovingBackwards = movementVector.length() > 0 && movementVector.dot(lookVector) < 0;
    }

    @Unique
    private void updateEnvironmentInfo() {
        Block standingBlock = this.level().getBlockState(blockPosition().below()).getBlock();
        isOnFence = (standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof IronBarsBlock) && onGround();
        isOnEdge = standingBlock instanceof AirBlock && onGround();
    }

    @Unique
    private void resetOverlayProperties() {
        overlayFadeTime = 10;
        overlayAnimationSpeed = 1;
    }

    @Unique
    private void playAnimationSequence() {
        playBaseAnimations();
        playFlyAnimation();
        playFallAnimation();
        getClimbAnimationStatus();
        playCrawlAnimation();
        playInWaterAnimations();
        playRidingAnimations();
        playElytraAnimation();
        playHandSwingAnimations();
        playSleepAnimation();
        playUseItemAnimations();
        //playUpHandAnimation();
    }

    @Unique
    private boolean lastMainHandState = false;
    @Unique
    private boolean lastOffHandState = false;

    @Unique
    private void playUpHandAnimation() {
        boolean isMainHandUp = getMainHandItem().getItem() == Items.TORCH ||
                getMainHandItem().getItem() == Items.SOUL_TORCH ||
                getMainHandItem().getItem() == Items.REDSTONE_TORCH ||
                getMainHandItem().getItem() == Items.FILLED_MAP ||
                getMainHandItem().getItem() == Items.RECOVERY_COMPASS ||
                getMainHandItem().getItem() == Items.COMPASS;
        boolean isOffHandUp = getOffhandItem().getItem() == Items.TORCH ||
                getOffhandItem().getItem() == Items.SOUL_TORCH ||
                getOffhandItem().getItem() == Items.REDSTONE_TORCH ||
                getOffhandItem().getItem() == Items.FILLED_MAP ||
                getOffhandItem().getItem() == Items.RECOVERY_COMPASS ||
                getOffhandItem().getItem() == Items.COMPASS;

        // Detectar cambios de estado
        boolean handStateChanged = (lastMainHandState != isMainHandUp) || (lastOffHandState != isOffHandUp);

        if (handStateChanged) {
            disableAnimation();
            disableAnimationOverlay();
        }

        if (isMainHandUp) {
            upHandAnimationContainer.setCurrentAnimation(UP_HAND_ANIMATION.getAnimation());
            upHandAnimationContainer.setCurrentAnimationId(RIGHT_PREFIX + UP_HAND_ANIMATION.getAnimationId());
            disableUpHandPos(ArmsEnum.RIGHT_ARM);
            ((MirrorModifier) upHandAnimationContainer.getAnimationModifiers().get(0)).setEnabled(true);
            ((AdjustmentModifier) upHandAnimationContainer.getAnimationModifiers().get(1)).enabled = true;
        }
        if (isOffHandUp) {
            upHandAnimationContainer.setCurrentAnimation(UP_HAND_ANIMATION.getAnimation());
            upHandAnimationContainer.setCurrentAnimationId(LEFT_PREFIX + UP_HAND_ANIMATION.getAnimationId());
            disableUpHandPos(ArmsEnum.LEFT_ARM);
            ((MirrorModifier) upHandAnimationContainer.getAnimationModifiers().get(0)).setEnabled(false);
            ((AdjustmentModifier) upHandAnimationContainer.getAnimationModifiers().get(2)).enabled = true;
        }
        lastMainHandState = isMainHandUp;
        lastOffHandState = isOffHandUp;
    }

    @Unique
    private void updateAnimationContainers() {
        updateMainAnimationContainer();
        updateOverlayAnimationContainer();
        updateUpHandAnimationContainer();
    }

    @Unique
    private void updateUpHandAnimationContainer() {
        if (!Objects.equals(upHandAnimationContainer.getCurrentAnimationId(), upHandAnimationContainer.getPrevAnimationId()) ||
                !upHandAnimationContainer.getAnimationModifierLayer().isActive()) {

            if (!upHandAnimationContainer.getPrevAnimationId().equals(upHandAnimationContainer.getCurrentAnimationId())) {
                playCurrentAnimation(upHandAnimationContainer.getAnimationModifierLayer(), upHandAnimationContainer.getCurrentAnimation());
            } else {
                upHandAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.getAnimationId());
                upHandAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
                playCurrentAnimation(upHandAnimationContainer.getAnimationModifierLayer(), upHandAnimationContainer.getCurrentAnimation());
            }

            upHandAnimationContainer.setPrevAnimationId(upHandAnimationContainer.getCurrentAnimationId());
        }
    }

    @Unique
    private void updateOverlayAnimationContainer() {
        if ((!Objects.equals(currentOverlayId, prevOverlayId) && overlayPriority >= prevOverlayPriority) ||
                !modOverlayAnimationContainer.isActive()) {

            rightBowModifier.enabled = false;
            leftBowModifier.enabled = false;

            if (prevOverlayId.contains("trident") && !currentOverlayId.contains("trident")) {
                overlayFadeTime = 10;
            }

            if (!prevOverlayId.equals(currentOverlayId)) {
                playCurrentAnimation(modOverlayAnimationContainer, currentOverlayAnimation);
            } else {
                currentOverlayId = BLANK_LOOP_ANIMATION.getAnimationId();
                currentOverlayAnimation = BLANK_LOOP_ANIMATION.getAnimation();
                playCurrentAnimation(modOverlayAnimationContainer, currentOverlayAnimation);
            }

            prevOverlayId = currentOverlayId;
            prevOverlayPriority = overlayPriority;
        }
    }

    @Unique
    private int currentComboCount = 0;
    @Unique
    private long lastSwingTick = 0;
    @Unique
    private static final int COMBO_RESET_TICKS = 50;

    @Unique
    private void playHandSwingAnimations() {
        if (swinging) {
            //sword attack
            if ((getMainHandItem().getItem() instanceof SwordItem || getMainHandItem().getItem() instanceof TridentItem) && !isUsingItem() && swingingArm.equals(MAIN_HAND)) {
                /*Item swordItem = getMainHandItem().getItem();
                swordItem.components().get(DataComponents.CUSTOM_MODEL_DATA).equals(100);*/

                handleSwordComboAnimation();

                //pickaxe
            } else if (getMainHandItem().getItem() instanceof PickaxeItem && swingingArm.equals(MAIN_HAND)) {
                loopedToolAnimation(PICKAXE_ANIMATION, PICKAXE_SNEAK_ANIMATION, CONFIG.getPickaxeAnimationsConfig(), 10, 0);
                //axe
            } else if (getMainHandItem().getItem() instanceof AxeItem && swingingArm.equals(MAIN_HAND)) {
                loopedToolAnimation(AXE_ANIMATION, AXE_SNEAK_ANIMATION, CONFIG.getAxeAnimationsConfig(), 10, 0);
                //shovel
            } else if (getMainHandItem().getItem() instanceof ShovelItem && swingingArm.equals(MAIN_HAND)) {
                loopedToolAnimation(SHOVEL_ANIMATION, SHOVEL_SNEAK_ANIMATION, CONFIG.getShovelAnimationsConfig(), 10, 0);
            } else {
                genericHandswing();
            }
        }
    }

    @Unique
    private void handleSwordComboAnimation() {
        long currentTick = level().getGameTime();
        if (currentTick - lastSwingTick > COMBO_RESET_TICKS) {
            currentComboCount = 0;
        }

        if (modOverlayAnimationContainer.getAnimation().isActive() &&
                ((KeyframeAnimationPlayer) modOverlayAnimationContainer.getAnimation()).getData().getName().equalsIgnoreCase(BLANK_LOOP_ANIMATION.getAnimationId())) {
            if (currentComboCount < 2) {
                currentComboCount++;
            } else {
                currentComboCount = 0;
            }
        }

        lastSwingTick = currentTick;

        overlayAnimationSpeed = CONFIG.getSwordAttackAnimationsConfig().getSpeedMultiplier();
        overlayFadeTime = 0;
        overlayPriority = 1;
        overlayMirrorModifier.setEnabled(rightHand != MAIN_HAND);

        selectComboAnimation();

        if (!CONFIG.getSwordAttackAnimationsConfig().isEnabled()) {
            disableAnimationOverlay();
            genericHandswing();
        }
    }

    @Unique
    private void selectComboAnimation() {
        boolean isSneaking = isCrouching();

        switch (currentComboCount) {
            case 1 -> {
                currentOverlayAnimation = isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimation() : SWORD_ATTACK_1_ANIMATION.getAnimation();
                currentOverlayId = isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId() : SWORD_ATTACK_1_ANIMATION.getAnimationId();
            }
            case 2 -> {
                currentOverlayAnimation = isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimation() : SWORD_ATTACK_2_ANIMATION.getAnimation();
                currentOverlayId = isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId() : SWORD_ATTACK_2_ANIMATION.getAnimationId();
            }
            default -> {
                currentOverlayAnimation = isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimation() : SWORD_ATTACK_3_ANIMATION.getAnimation();
                currentOverlayId = isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId() : SWORD_ATTACK_3_ANIMATION.getAnimationId();
            }
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
            // Check if the offhand item is a map
            boolean isMap = getMainHandItem().getItem() instanceof MapItem;
            boolean isInstrument = getMainHandItem().getItem() instanceof InstrumentItem;
            boolean isBrush = getMainHandItem().getItem() instanceof BrushItem;

            // Check if the arm pose is for a crossbow or bow, and if the offhand item is not a bow
            boolean condition = mainArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                    offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    (offArmPosition.equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getMainHandItem().getItem() instanceof BowItem)) ||
                    isScoping() || isInstrument || isBrush;

            if (condition || (IS_NEA_LOADED && isMap)) {
                disableArm(ArmsEnum.RIGHT_ARM);
            }
        }
    }

    @Unique
    private void checkOffHandItemForArmDisabling() {
        if (!getOffhandItem().isEmpty()) {
            boolean isMap = getMainHandItem().getItem() instanceof MapItem;
            boolean isInstrument = getMainHandItem().getItem() instanceof InstrumentItem;
            boolean isBrush = getMainHandItem().getItem() instanceof BrushItem;

            boolean condition = offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                    offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    (offArmPosition.equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getOffhandItem().getItem() instanceof BowItem) ||
                            isScoping() || isInstrument || isBrush);

            if (condition || (IS_NEA_LOADED && isMap)) {
                disableArm(ArmsEnum.LEFT_ARM);
            }
        }
    }

    @Unique
    private void updateMainAnimationContainer() {
        if ((!Objects.equals(currentAnimationId, prevAnimationId) && priority >= prevPriority) ||
                !modBaseAnimationContainer.isActive()) {

            playCurrentAnimation(modBaseAnimationContainer, currentAnimation);

            prevAnimationId = currentAnimationId;
            prevPriority = priority;
        }
    }

    @Unique
    public void playCurrentAnimation(ModifierLayer<IAnimation> animationContainer, KeyframeAnimation animation) {
        modifyFirstPersonConfig();
        this.builder = animation.mutableCopy();

        animationContainer.replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(fadeTime, INOUTSINE),
                new KeyframeAnimationPlayer(animation).setFirstPersonMode(firstPersonModifier.getFirstPersonMode(0)),
                true
        );
    }

    @Unique
    private void modifyFirstPersonConfig() {
        boolean condition = currentOverlayId.contains("trident") ||
                currentOverlayId.contains("bow") ||
                currentAnimationId.contains("climbing") ||
                currentAnimationId.contains("boat") ||
                currentAnimationId.contains("horse") ||
                currentAnimationId.contains("minecart") ||
                currentAnimationId.contains("water") ||
                getOffhandItem().getItem() != Items.AIR ||
                offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                mainArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE);

        firstPersonModifier.setCurrentFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);

        if (condition) {
            firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.ENABLE_BOTH_ARMS);
        } else {
            firstPersonModifier.setCurrentFirstPersonConfig(getMainArm().equals(HumanoidArm.RIGHT) ? FirstPersonModifier.FirstPersonConfigEnum.ONLY_RIGHT_ARM_AND_ITEM : FirstPersonModifier.FirstPersonConfigEnum.ONLY_LEFT_ARM_AND_ITEM);
        }
        if (isScoping()) {
            firstPersonModifier.setCurrentFirstPersonConfig(FirstPersonModifier.FirstPersonConfigEnum.DISABLE_BOTH_ARMS);
        }
    }

    @Unique
    private void playUseItemAnimations() {
        if (isUsingItem()) {
            Item activeItem = getUseItem().getItem();
            if (activeItem.components().get(DataComponents.FOOD) != null || activeItem instanceof PotionItem) {
                playEatingAnimation();
            } else if (activeItem instanceof TridentItem) {
                playTridentAnimation();
            } else if (activeItem instanceof BowItem) {
                playBowAnimation();
            } else if (activeItem instanceof ShieldItem) {
                playShieldAnimation();
            } else if (activeItem instanceof CrossbowItem) {
                disableBothArms();
            } else if (IS_SUPPLEMENTARIES_LOADED) {
                if (SupplementariesCompat.checkFluteItem(activeItem)) {
                    disableBothArms();
                }
            }
        }
    }

    @Unique
    private void playEatingAnimation() {
        if (CONFIG.getEatingAnimationsConfig().isEnabled()) {
            overlayFadeTime = 10;
            overlayAnimationSpeed = CONFIG.getEatingAnimationsConfig().getSpeedMultiplier();
            overlayPriority = 0;

            if (getUsedItemHand().equals(rightHand)) {
                setEatingAnimation(ArmsEnum.RIGHT_ARM, false);
            } else if (getUsedItemHand().equals(leftHand)) {
                setEatingAnimation(ArmsEnum.LEFT_ARM, true);
            }
        }
    }

    @Unique
    private void setEatingAnimation(ArmsEnum arm, boolean mirror) {
        currentOverlayAnimation = EATINHG_ANIMATION.getAnimation();
        currentOverlayId = (mirror ? LEFT_PREFIX : RIGHT_PREFIX) + EATINHG_ANIMATION.getAnimationId();
        disableArmOverlayPos(arm);
        overlayMirrorModifier.setEnabled(mirror);
    }


    @Unique
    private void playTridentAnimation() {
        if (CONFIG.getTridentAnimationConfig().isEnabled()) {
            overlayFadeTime = 5;
            overlayAnimationSpeed = CONFIG.getTridentAnimationConfig().getSpeedMultiplier();
            overlayPriority = 0;

            if (getUsedItemHand().equals(rightHand)) {
                setTridentAnimation(true, 55);
            } else if (getUsedItemHand().equals(leftHand)) {
                setTridentAnimation(false, -55);
            }
        } else {
            disableActiveArm();
            priority = 0;
            fadeTime = 1;
        }
    }

    @Unique
    private void setTridentAnimation(boolean isRightHand, int yawOffset) {
        if (isCrouching()) {
            disableArmOverlayPos(ArmsEnum.RIGHT_ARM);
            disableArmOverlayPos(ArmsEnum.LEFT_ARM);
        } else {
            currentOverlayAnimation = TRIDENT_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + TRIDENT_ANIMATION.getAnimationId();
        }
        setYBodyRot(playerHeadYaw + yawOffset);
        overlayMirrorModifier.setEnabled(!isRightHand);
    }

    @Unique
    private void playBowAnimation() {
        if (isPassenger() || isVisuallyCrawling() || !CONFIG.getBowAnimationsConfig().isEnabled()) {
            disableBowArms();
        } else {
            setBowAnimation();
        }
    }

    @Unique
    private void disableBowArms() {
        disableArm(ArmsEnum.RIGHT_ARM);
        disableArm(ArmsEnum.LEFT_ARM);
        fadeTime = 1;
    }

    @Unique
    private void setBowAnimation() {
        overlayFadeTime = 10;
        overlayAnimationSpeed = CONFIG.getBowAnimationsConfig().getSpeedMultiplier();
        overlayPriority = 0;

        if (getUsedItemHand().equals(rightHand)) {
            setBowAnimationForHand(true);
        } else if (getUsedItemHand().equals(leftHand)) {
            setBowAnimationForHand(false);
        }
    }

    @Unique
    private void setBowAnimationForHand(boolean isRightHand) {
        if (isCrouching()) {
            currentOverlayAnimation = BOW_SNEAK_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_SNEAK_ANIMATION.getAnimationId();
            overlayFadeTime = 1;
        } else {
            currentOverlayAnimation = BOW_IDLE_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_IDLE_ANIMATION.getAnimationId();
        }
        disableArmOverlayPos(isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);

        if (isRightHand) {
            rightBowModifier.enabled = true;
            setYBodyRot(playerHeadYaw - 90);
        } else {
            leftBowModifier.enabled = true;
            setYBodyRot(playerHeadYaw + 90);
        }
        overlayMirrorModifier.setEnabled(!isRightHand);
    }

    @Unique
    private void playShieldAnimation() {
        if (CONFIG.getShieldAnimationConfig().isEnabled()) {
            overlayFadeTime = 10;
            overlayAnimationSpeed = CONFIG.getShieldAnimationConfig().getSpeedMultiplier();
            overlayPriority = 0;

            if (getUsedItemHand().equals(rightHand)) {
                setShieldAnimation(true);
            } else if (getUsedItemHand().equals(leftHand)) {
                setShieldAnimation(false);
            }
        } else {
            disableActiveArm();
            priority = 0;
            fadeTime = 5;
        }
    }

    @Unique
    private void setShieldAnimation(boolean isRightHand) {
        if (isCrouching()) {
            currentOverlayAnimation = SHIELD_SNEAK_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + SHIELD_SNEAK_ANIMATION.getAnimationId();
        } else {
            currentOverlayAnimation = SHIELD_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + SHIELD_ANIMATION.getAnimationId();
        }
        disableArmOverlayPos(isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        overlayMirrorModifier.setEnabled(!isRightHand);
    }


    @Unique
    private void disableArmBasedOnHand(InteractionHand hand) {
        builder = currentAnimation.mutableCopy();
        disableArmInBuilder(hand == rightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        currentAnimation = builder.build();
    }


    @Unique
    public void disableArm(ArmsEnum arm) {
        builder = currentAnimation.mutableCopy();
        disableArmInBuilder(arm);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableBothArms() {
        builder = currentAnimation.mutableCopy();
        disableArmInBuilder(ArmsEnum.RIGHT_ARM);
        disableArmInBuilder(ArmsEnum.LEFT_ARM);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableArmOverlayPos(ArmsEnum arm) {
        builder = currentAnimation.mutableCopy();
        var currentArm = builder.getPart(arm.getArmId());
        if (currentArm != null) {
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        currentAnimation = builder.build();
    }

    @Unique
    public void disableUpHandPos(ArmsEnum arm) {
        KeyframeAnimation.StateCollection currentArm;
        builder = currentAnimation.mutableCopy();
        currentArm = builder.getPart(arm.getArmId());
        if (currentArm != null) {
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        currentAnimation = builder.build();
        builder = currentOverlayAnimation.mutableCopy();
        currentArm = builder.getPart(arm.getArmId());
        if (currentArm != null) {
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        currentOverlayAnimation = builder.build();
    }

    @Unique
    public void disableAnimation() {
        currentAnimation = BLANK_LOOP_ANIMATION.getAnimation();
        currentAnimationId = BLANK_LOOP_ANIMATION.getAnimationId();
    }

    @Unique
    public void disableAnimationOverlay() {
        currentOverlayAnimation = BLANK_LOOP_ANIMATION.getAnimation();
        currentOverlayId = BLANK_LOOP_ANIMATION.getAnimationId();
    }

    @Unique
    public void disableAnimationUpHand() {
        upHandAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
        upHandAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.getAnimationId());
    }

    @Unique
    public void loopedToolAnimation(PlayerAnimations.Animations animation, PlayerAnimations.Animations
            sneakAnimation, ClientConfig.AnimationConfig config, int fade, int priority) {
        if (config.isEnabled()) {
            overlayFadeTime = fade;
            overlayAnimationSpeed = config.getSpeedMultiplier();
            overlayPriority = priority;
            overlayMirrorModifier.setEnabled(rightHand != MAIN_HAND);

            currentOverlayAnimation = isCrouching() ? sneakAnimation.getAnimation() : animation.getAnimation();
            currentOverlayId = isCrouching() ? sneakAnimation.getAnimationId() : animation.getAnimationId();
        } else {
            genericHandswing();
        }
    }

    @Unique
    public void genericHandswing() {
        disableArmBasedOnHand(swingingArm);
        currentAnimationId = "handswinging" + currentAnimationId;
        fadeTime = 0;
        priority = 0;
    }

    @Unique
    private void playWalkingAnimation() {
        if (!CONFIG.getWalkingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = (float) (moveSpeed * CONFIG.getWalkingAnimationConfig().getSpeedMultiplier());

            currentAnimation = WALKING_ANIMATION.getAnimation();
            currentAnimationId = WALKING_ANIMATION.getAnimationId();
        }

        fadeTime = 0;
        priority = 0;
    }

    @Unique
    private void playWalkingBackwardsAnimation() {
        if (!CONFIG.getWalkingBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = (float) (moveSpeed * CONFIG.getWalkingBackwardsAnimationConfig().getSpeedMultiplier());

            currentAnimation = WALKING_BACKWARDS_ANIMATION.getAnimation();
            currentAnimationId = WALKING_BACKWARDS_ANIMATION.getAnimationId();
        }

        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playWalkingSneakAnimation() {
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = (float) (moveSpeed * CONFIG.getWalkingSneakAnimationConfig().getSpeedMultiplier());

            currentAnimation = WALKING_SNEAK_ANIMATION.getAnimation();
            currentAnimationId = WALKING_SNEAK_ANIMATION.getAnimationId();
        }

        if (prevAnimationId.equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || prevAnimationId.equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            fadeTime = 10;
        } else {
            fadeTime = 5;
        }
        priority = 0;
    }

    @Unique
    private void playWalkingSneakBackwardsAnimation() {
        if (!CONFIG.getWalkingSneakBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = (float) (moveSpeed * CONFIG.getWalkingSneakBackwardsAnimationConfig().getSpeedMultiplier());

            currentAnimation = WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimation();
            currentAnimationId = WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId();
        }

        if (prevAnimationId.equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId())) {
            fadeTime = 10;
        } else {
            fadeTime = 5;
        }
        priority = 0;
    }

    @Unique
    private void playRunningAnimation() {
        if (!CONFIG.getRunningAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = (float) (moveSpeed * CONFIG.getRunningAnimationConfig().getSpeedMultiplier());

            currentAnimation = RUNNING_ANIMATION.getAnimation();
            currentAnimationId = RUNNING_ANIMATION.getAnimationId();
        }
        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playTurnLeftAndRightAnimation() {
        if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = (bodyYawDelta < 0) ? TURN_LEFT_ANIMATION.getAnimation() : TURN_RIGHT_ANIMATION.getAnimation();
            currentAnimationId = (bodyYawDelta < 0) ? TURN_LEFT_ANIMATION.getAnimationId() : TURN_RIGHT_ANIMATION.getAnimationId();

            if ((((float) 1 / 2) * bodyYawDelta) > 2 || (((float) 1 / 2) * bodyYawDelta) < 2) {
                animationSpeed = CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier();
            } else {
                animationSpeed = abs((((float) 1 / 2) * bodyYawDelta) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
            }
        }

        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playIdleStandingAnimation() {
        if (!CONFIG.getIdleStandingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = IDLE_STANDING_ANIMATION.getAnimation();
            currentAnimationId = IDLE_STANDING_ANIMATION.getAnimationId();

            animationSpeed = CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier();
        }
        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playIdleSneakAnimation() {
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = IDLE_SNEAK_ANIMATION.getAnimation();
            currentAnimationId = IDLE_SNEAK_ANIMATION.getAnimationId();

            animationSpeed = CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier();

        }

        if (prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId()) || prevAnimationId.equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            fadeTime = 10;
        } else {
            fadeTime = 5;
        }
        priority = 0;
    }

    @Unique
    private void playFlyIdleCreativeAnimation() {
        if (!CONFIG.getIdleCreativeFlyingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = IDLE_CREATIVE_FLYING_ANIMATION.getAnimation();
            currentAnimationId = IDLE_CREATIVE_FLYING_ANIMATION.getAnimationId();

            animationSpeed = CONFIG.getIdleCreativeFlyingAnimationConfig().getSpeedMultiplier();
        }
        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playFallAnimation() {
        if (vectorY < -0.6 && !isPassenger() && !onGround()) {
            if (!CONFIG.getFallingAnimationConfig().isEnabled()) {
                disableAnimation();
            } else {
                currentAnimation = FALLING_ANIMATION.getAnimation();
                currentAnimationId = FALLING_ANIMATION.getAnimationId();
                animationSpeed = CONFIG.getFallingAnimationConfig().getSpeedMultiplier();
            }
            fadeTime = 10;
            priority = 0;
        }
    }

    @Unique
    private void getClimbAnimationStatus() {
        if (!CONFIG.getClimbingAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            if (!onGround() && !isPassenger()) {
                animationSpeed = CONFIG.getClimbingAnimationsConfig().getSpeedMultiplier();

                Block block = this.clientLevel.getBlockState(blockPosition()).getBlock();
                if ((block instanceof LadderBlock || block instanceof VineBlock)) {
                    fadeTime = 10;
                    priority = 0;
                    setBodyRotationInLeadderAndVineBlocks();
                    playClimbingAnimation();
                } else if ((block instanceof TwistingVinesPlantBlock
                        || block instanceof WeepingVinesPlantBlock
                        || block instanceof TwistingVinesBlock
                        || block instanceof WeepingVinesBlock
                        || block instanceof ScaffoldingBlock)) {
                    fadeTime = 10;
                    priority = 0;
                    setBodyRotationOnClimbableBlocks();
                    playClimbingAnimation();
                } else if (block instanceof PowderSnowBlock) {
                    fadeTime = 10;
                    priority = 0;

                    if ((String.valueOf(getArmorSlots())).contains("leather_boots")) {
                        playClimbingAnimation();
                    }
                }
            }
        }
    }

    @Unique
    private void playClimbingAnimation() {
        if (onClimbable()) {
            if (vectorY > 0) {
                currentAnimation = isCrouching() ? CLIMBING_SNEAK_ANIMATION.getAnimation() : CLIMBING_ANIMATION.getAnimation();
                currentAnimationId = isCrouching() ? CLIMBING_SNEAK_ANIMATION.getAnimationId() : CLIMBING_ANIMATION.getAnimationId();
            } else if (vectorY < 0) {
                currentAnimation = CLIMBING_BACKWARDS_ANIMATION.getAnimation();
                currentAnimationId = CLIMBING_BACKWARDS_ANIMATION.getAnimationId();
            } else {
                currentAnimation = isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimation() : CLIMBING_IDLE_ANIMATION.getAnimation();
                currentAnimationId = isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId() : CLIMBING_IDLE_ANIMATION.getAnimationId();
            }
        }
    }

    @Unique
    private void setBodyRotationOnClimbableBlocks() {
        if (!(getUseItem().getItem() instanceof BowItem)) {
            playerBodyYaw = ((float) toDegrees(atan2((blockPosition().getZ() + 0.5 - playerPosition.z), (blockPosition().getX()) + 0.5 - playerPosition.x)) - 90);
            playerHeadYaw = getYHeadRot();
            playerBodyYaw = ((playerBodyYaw % 360) + 360) % 360;
            playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;
            setYBodyRot(playerBodyYaw);
            playerHeadYaw = playerHeadYaw - playerBodyYaw;
            playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;

            if (playerHeadYaw > 90 && playerHeadYaw <= 180) {
                setYHeadRot(playerBodyYaw + 90);
            } else if (playerHeadYaw > 180 && playerHeadYaw < 270) {
                setYHeadRot(playerBodyYaw + 270);
            }
        }
    }

    @Unique
    private void setBodyRotationInLeadderAndVineBlocks() {
        if (!(getUseItem().getItem() instanceof BowItem)) {
            String blockStateString = String.valueOf(this.clientLevel.getBlockState(blockPosition()));
            playerBodyYaw = getVisualRotationYInDegrees();
            playerHeadYaw = getYHeadRot();
            if (blockStateString.contains("facing=north") || blockStateString.contains("south=true")) {
                playerBodyYaw = 0;
            } else if (blockStateString.contains("facing=south") || blockStateString.contains("north=true")) {
                playerBodyYaw = 180;
            } else if (blockStateString.contains("facing=west") || blockStateString.contains("east=true")) {
                playerBodyYaw = 270;
            } else if (blockStateString.contains("facing=east") || blockStateString.contains("west=true")) {
                playerBodyYaw = 90;
            }

            playerBodyYaw = ((playerBodyYaw % 360) + 360) % 360;
            playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;
            setYBodyRot(playerBodyYaw);
            playerHeadYaw = playerHeadYaw - playerBodyYaw;
            playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;

            if (playerHeadYaw > 90 && playerHeadYaw <= 180) {
                setYHeadRot(playerBodyYaw + 90);
            } else if (playerHeadYaw > 180 && playerHeadYaw < 270) {
                setYHeadRot(playerBodyYaw + 270);
            }
        }
    }

    @Unique
    private void playCrawlAnimation() {
        if (!CONFIG.getCrawlingAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            if (isVisuallyCrawling()) {
                animationSpeed = CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier();
                if (moveSpeed > 0.0649) {
                    animationSpeed += (float) moveSpeed;
                }
                if (moveSpeed > 0 && !isMovingBackwards) {
                    currentAnimation = CRAWLING_ANIMATION.getAnimation();
                    currentAnimationId = CRAWLING_ANIMATION.getAnimationId();
                } else if (moveSpeed > 0) {
                    currentAnimation = CRAWLING_BACKWARDS_ANIMATION.getAnimation();
                    currentAnimationId = CRAWLING_BACKWARDS_ANIMATION.getAnimationId();
                } else {
                    currentAnimation = CRAWLING_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = CRAWLING_IDLE_ANIMATION.getAnimationId();
                }
                fadeTime = 10;
                priority = 0;
            }
        }
    }

    @Unique
    private void playInWaterAnimations() {
        if (!CONFIG.getInWaterAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier();
            fadeTime = 10;
            priority = 0;
            if ((isInWaterOrBubble() || isInLava()) && !onGround() && !isVisuallySwimming()) {
                if (moveSpeed > 0 && !isMovingBackwards) {
                    currentAnimation = IN_WATER_FORWARD_ANIMATION.getAnimation();
                    currentAnimationId = IN_WATER_FORWARD_ANIMATION.getAnimationId();
                } else if (moveSpeed > 0) {
                    currentAnimation = IN_WATER_BACKWARDS_ANIMATION.getAnimation();
                    currentAnimationId = IN_WATER_BACKWARDS_ANIMATION.getAnimationId();
                } else if (vectorY > 0) {
                    currentAnimation = IN_WATER_UP_ANIMATION.getAnimation();
                    currentAnimationId = IN_WATER_UP_ANIMATION.getAnimationId();
                } else {
                    currentAnimation = IN_WATER_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = IN_WATER_IDLE_ANIMATION.getAnimationId();
                }
            } else if (isInWaterOrBubble() && isVisuallySwimming()) {
                currentAnimation = IN_WATER_SWIMMING_ANIMATION.getAnimation();
                currentAnimationId = IN_WATER_SWIMMING_ANIMATION.getAnimationId();
            }
        }
    }

    @Unique
    private void playRidingAnimations() {
        if (isPassenger()) {
            var vehicle = getVehicle();
            if (vehicle instanceof Minecart) {
                if (!CONFIG.getMinecartAnimationsConfig().isEnabled()) {
                    disableAnimation();
                } else {
                    currentAnimation = MINECART_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = MINECART_IDLE_ANIMATION.getAnimationId();
                    animationSpeed = CONFIG.getMinecartAnimationsConfig().getSpeedMultiplier();
                    fadeTime = 10;
                    priority = 0;
                }
            } else if (vehicle instanceof Horse
                    || vehicle instanceof SkeletonHorse
                    || vehicle instanceof ZombieHorse
                    || vehicle instanceof Donkey
                    || vehicle instanceof Mule) {
                if (moveSpeed > 0 && !isMovingBackwards) {
                    if (!CONFIG.getHorseRunningAnimationConfig().isEnabled()) {
                        disableAnimation();
                    } else {
                        animationSpeed = CONFIG.getHorseRunningAnimationConfig().getSpeedMultiplier();
                        fadeTime = 10;
                        priority = 0;

                        currentAnimation = HORSE_RUNNING_ANIMATION.getAnimation();
                        currentAnimationId = HORSE_RUNNING_ANIMATION.getAnimationId();
                    }
                } else {
                    if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                        disableAnimation();
                    } else {
                        animationSpeed = CONFIG.getHorseIdleAnimationConfig().getSpeedMultiplier();
                        fadeTime = 10;

                        currentAnimation = HORSE_IDLE_ANIMATION.getAnimation();
                        currentAnimationId = HORSE_IDLE_ANIMATION.getAnimationId();
                    }
                }
                if (isUsingItem()) {
                    if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                        disableAnimation();
                    } else {
                        currentAnimation = HORSE_IDLE_ANIMATION.getAnimation();
                        currentAnimationId = HORSE_IDLE_ANIMATION.getAnimationId();
                    }
                }
            } else if (vehicle instanceof Boat || vehicle instanceof ChestBoat) {
                if (!CONFIG.getBoatAnimationsConfig().isEnabled()) {
                    disableAnimation();
                } else {
                    animationSpeed = CONFIG.getBoatAnimationsConfig().getSpeedMultiplier();

                    currentAnimation = BOAT_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = BOAT_IDLE_ANIMATION.getAnimationId();

                    boolean isLeftPaddleMoving = ((Boat) getVehicle()).getPaddleState(0);
                    boolean isRightPaddleMoving = ((Boat) getVehicle()).getPaddleState(1);

                    if (moveSpeed > 0 && !isMovingBackwards) {
                        if (isLeftPaddleMoving && isRightPaddleMoving) {
                            currentAnimation = BOAT_FORWARD_ANIMATION.getAnimation();
                            currentAnimationId = BOAT_FORWARD_ANIMATION.getAnimationId();
                        } else if (isLeftPaddleMoving) {
                            currentAnimation = BOAT_TURN_LEFT_ANIMATION.getAnimation();
                            currentAnimationId = BOAT_TURN_LEFT_ANIMATION.getAnimationId();
                        } else if (isRightPaddleMoving) {
                            currentAnimation = BOAT_TURN_RIGHT_ANIMATION.getAnimation();
                            currentAnimationId = BOAT_TURN_RIGHT_ANIMATION.getAnimationId();
                        }
                    }

                    fadeTime = 10;
                    priority = 0;
                }
            } else {
                if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                    disableAnimation();
                } else {
                    animationSpeed = CONFIG.getHorseIdleAnimationConfig().getSpeedMultiplier();
                    currentAnimation = HORSE_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = HORSE_IDLE_ANIMATION.getAnimationId();
                }
                fadeTime = 10;
                priority = 0;
            }
        }
    }

    @Unique
    private void playSleepAnimation() {
        if (isSleeping()) {
            if (!CONFIG.getSleepingAnimationsConfig().isEnabled()) {
                disableAnimationOverlay();
                disableAnimationUpHand();
            } else {
                fadeTime = 10;
                animationSpeed = CONFIG.getSleepingAnimationsConfig().getSpeedMultiplier();
                priority = 0;
                disableAnimation();

                currentOverlayAnimation = SLEEPING_ANIMATION.getAnimation();
                currentOverlayId = SLEEPING_ANIMATION.getAnimationId();
            }
        }
    }

    @Unique
    private void playElytraAnimation() {
        if (!CONFIG.getElytraAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            if (isFallFlying()) {
                currentAnimation = ELYTRA_ANIMATION.getAnimation();
                currentAnimationId = ELYTRA_ANIMATION.getAnimationId();

                animationSpeed = CONFIG.getElytraAnimationsConfig().getSpeedMultiplier();
                priority = 0;
                fadeTime = 10;
            }
        }
    }

    @Unique
    private void playFlyAnimation() {
        double vyfly = Math.round(vectorY * 1000.0) / 1000.0;
        if ((vyfly == 0.0 || Math.abs(vyfly) == 0.375) && !onGround() && !isInWaterOrBubble()) {
            flyChecker++;
        } else if (Math.abs(vyfly) > 0.375 || onGround()) {
            flyChecker = 0;
        }

        if (flyChecker > 10) {
            playFlyIdleCreativeAnimation();
        }
    }

    @Unique
    private void playBaseAnimations() {
        if (moveSpeed < 0.23 && moveSpeed > 0 && !isMovingBackwards && !isCrouching()) {
            if (isOnFence) {
                playOnFenceAnimation();
            } else {
                playWalkingAnimation();
            }
        } else if (isMovingBackwards && !isCrouching()) {
            playWalkingBackwardsAnimation();
        } else if (moveSpeed > 0.23 && isSprinting() && !isMovingBackwards && !isCrouching()) {
            playRunningAnimation();
        } else if (moveSpeed == 0 && !isCrouching() && !isSprinting()) {
            playTurningStandingAnimation();
        } else if (isCrouching() && moveSpeed == 0 && !isMovingBackwards) {
            playSneakingAnimation();
        } else if (isCrouching() && moveSpeed > 0 && !isMovingBackwards) {
            playWalkingSneakAnimation();
        } else if (isCrouching() && moveSpeed > 0) {
            playWalkingSneakBackwardsAnimation();
        }
    }

    @Unique
    private void playOnFenceAnimation() {
        if (!CONFIG.getOnFenceAnimationConfig().isEnabled()) {
            playWalkingAnimation();
        } else {
            animationSpeed = (float) moveSpeed * CONFIG.getOnFenceAnimationConfig().getSpeedMultiplier();

            currentAnimation = ON_FENCE_WALKING_ANIMATION.getAnimation();
            currentAnimationId = ON_FENCE_WALKING_ANIMATION.getAnimationId();

            fadeTime = 10;
            priority = 0;
        }
    }

    @Unique
    private void playSneakingAnimation() {
        if (bodyYawDelta != 0) {
            playWalkingSneakAnimation();
            if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
                disableAnimation();
            } else {
                if ((((float) 1 / 2) * bodyYawDelta) > 1.5 || (((float) 1 / 2) * bodyYawDelta) < -1.5) {
                    animationSpeed = CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier();
                } else {
                    animationSpeed = abs((((float) 1 / 2) * bodyYawDelta) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
                }
            }
        } else {
            playIdleSneakAnimation();
        }
    }

    @Unique
    private void playTurningStandingAnimation() {
        if (bodyYawDelta != 0) {
            playTurnLeftAndRightAnimation();
        } else {
            if (CONFIG.onFenceAnimationConfig.isEnabled() && isOnFence) {
                currentAnimation = ON_FENCE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = ON_FENCE_IDLE_ANIMATION.getAnimationId();
            } else if (CONFIG.onEdgeAnimationConfig.isEnabled() && isOnEdge) {
                currentAnimation = ON_EDGE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = ON_EDGE_IDLE_ANIMATION.getAnimationId();
            } else {
                playIdleStandingAnimation();
            }

            if (prevAnimationId.equals(IDLE_STANDING_ANIMATION.getAnimationId())
                    || prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId())) {
                fadeTime = 5;
            } else {
                fadeTime = 10;
            }
            priority = 0;
        }
    }

    @Unique
    private AdjustmentModifier createBowModifier(boolean isRight) {
        return new AdjustmentModifier(partName -> {
            float pitch = (float) Math.toRadians(getXRot());
            String mainArm = isRight ? "rightArm" : "leftArm";
            String offArm = isRight ? "leftArm" : "rightArm";

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
    private AdjustmentModifier createUpHandModifier(boolean isRight) {
        return new AdjustmentModifier(partName -> {
            float limitedPitch = Math.clamp(getXRot(), -45, 45);
            float pitch = (float) Math.toRadians(limitedPitch) * 0.5f;
            String mainArm = isRight ? "rightArm" : "leftArm";
            if (partName.equals(mainArm)) {
                Vec3f currentTransform = modOverlayAnimationContainer.get3DTransform(
                        mainArm,
                        TransformType.POSITION,
                        0f,
                        new Vec3f(0, 0, 0)
                );

                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(pitch, 0, 0),                   //rotation
                        currentTransform                                //position
                ));
            }

            return Optional.empty();
        });
    }
}