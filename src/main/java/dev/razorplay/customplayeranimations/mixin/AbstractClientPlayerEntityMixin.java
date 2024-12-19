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
import dev.razorplay.customplayeranimations.util.*;
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
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

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

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

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
            0,
            0,
            0,
            1);

    @Unique
    private final PlayerData playerData = new PlayerData();

    @Unique
    private final FirstPersonModifier firstPersonModifier = new FirstPersonModifier();

    @Unique
    private boolean lastMainHandState = false;
    @Unique
    private boolean lastOffHandState = false;

    @Unique
    private static final Set<Item> UP_HAND_ITEMS = Set.of(
            Items.TORCH, Items.SOUL_TORCH, Items.REDSTONE_TORCH,
            Items.FILLED_MAP, Items.RECOVERY_COMPASS, Items.COMPASS
    );

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

        mainAnimationContainer.setCurrentAnimation(IDLE_STANDING_ANIMATION.getAnimation());
        overlayAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
        upHandAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());

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
        updateHandOrientation();
        updatePlayerPosition();
        updateMovementInfo();
        updateEnvironmentInfo();

        resetOverlayAnimationProperties();

        playAnimationSequence();
        updateAnimationSpeeds();

        if (IS_CARRYON_LOADED) {
            CarryOnCompat.check((AbstractClientPlayer) (Object) this);
        }

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
        disableAnimationOverlay();
        overlayAnimationContainer.setAnimationFadeTime(10);
        overlayAnimationContainer.setAnimationSpeed(1);
        overlayAnimationContainer.setAnimationPriority(0);
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
        playUpHandAnimation();
    }

    @Unique
    private boolean isHandUp(ItemStack itemStack) {
        return UP_HAND_ITEMS.contains(itemStack.getItem());
    }

    @Unique
    private void setUpHandAnimation(HumanoidArm arm) {
        upHandAnimationContainer.setAnimationFadeTime(10);
        upHandAnimationContainer.setCurrentAnimation(UP_HAND_ANIMATION.getAnimation());
        String animationId = (arm == HumanoidArm.RIGHT ? RIGHT_PREFIX : LEFT_PREFIX) + UP_HAND_ANIMATION.getAnimationId();
        upHandAnimationContainer.setCurrentAnimationId(animationId);
        disableArmOverlayPos(arm == HumanoidArm.RIGHT ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        ((MirrorModifier) upHandAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId()))
                .setEnabled(arm == HumanoidArm.RIGHT);
    }

    @Unique
    private void playUpHandAnimation() {
        boolean isMainHandUp = isHandUp(getMainHandItem());
        boolean isOffHandUp = isHandUp(getOffhandItem());
        boolean handStateChanged = (lastMainHandState != isMainHandUp) || (lastOffHandState != isOffHandUp);

        if (handStateChanged) {
            disableAnimation();
        }

        boolean shouldPlayAnimation = (isMainHandUp || isOffHandUp) &&
                !(overlayAnimationContainer.getCurrentAnimationId().contains("bow")) &&
                !(mainAnimationContainer.getCurrentAnimationId().contains("water")) &&
                !(mainAnimationContainer.getCurrentAnimationId().contains("climbing"));

        if (shouldPlayAnimation) {
            if (isMainHandUp) {
                setUpHandAnimation(getMainArm());
            }
            if (isOffHandUp) {
                setUpHandAnimation(getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
            }
        } else {
            disableUpHandAnimation();
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

            if (overlayAnimationContainer.getPrevAnimationId().contains("trident") && !overlayAnimationContainer.getCurrentAnimationId().contains("trident")) {
                overlayAnimationContainer.setAnimationFadeTime(10);
            }

            if (!overlayAnimationContainer.getPrevAnimationId().equals(overlayAnimationContainer.getCurrentAnimationId())) {
                playCurrentAnimation(overlayAnimationContainer.getAnimationModifierLayer(), overlayAnimationContainer.getCurrentAnimation());
            } else {
                overlayAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
                overlayAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.getAnimationId());
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

        if (overlayAnimationContainer.getAnimationModifierLayer().getAnimation().isActive() &&
                ((KeyframeAnimationPlayer) overlayAnimationContainer.getAnimationModifierLayer().getAnimation()).getData().getName().equalsIgnoreCase(BLANK_LOOP_ANIMATION.getAnimationId())) {
            if (currentComboCount < 2) {
                currentComboCount++;
            } else {
                currentComboCount = 0;
            }
        }

        lastSwingTick = currentTick;

        overlayAnimationContainer.setAnimationSpeed(CONFIG.getSwordAttackAnimationsConfig().getSpeedMultiplier());
        overlayAnimationContainer.setAnimationFadeTime(0);
        overlayAnimationContainer.setAnimationPriority(1);
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(playerData.getRightHand() != MAIN_HAND);

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
                overlayAnimationContainer.setCurrentAnimation(isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimation() : SWORD_ATTACK_1_ANIMATION.getAnimation());
                overlayAnimationContainer.setCurrentAnimationId(isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId() : SWORD_ATTACK_1_ANIMATION.getAnimationId());
            }
            case 2 -> {
                overlayAnimationContainer.setCurrentAnimation(isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimation() : SWORD_ATTACK_2_ANIMATION.getAnimation());
                overlayAnimationContainer.setCurrentAnimationId(isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId() : SWORD_ATTACK_2_ANIMATION.getAnimationId());
            }
            default -> {
                overlayAnimationContainer.setCurrentAnimation(isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimation() : SWORD_ATTACK_3_ANIMATION.getAnimation());
                overlayAnimationContainer.setCurrentAnimationId(isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId() : SWORD_ATTACK_3_ANIMATION.getAnimationId());
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
            boolean condition = playerData.getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                    playerData.getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    (playerData.getMainArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getMainHandItem().getItem() instanceof BowItem)) ||
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

            boolean condition = playerData.getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                    playerData.getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    (playerData.getOffArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getOffhandItem().getItem() instanceof BowItem) ||
                            isScoping() || isInstrument || isBrush);

            if (condition || (IS_NEA_LOADED && isMap)) {
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
            overlayAnimationContainer.setAnimationFadeTime(10);
            overlayAnimationContainer.setAnimationSpeed(CONFIG.getEatingAnimationsConfig().getSpeedMultiplier());
            overlayAnimationContainer.setAnimationPriority(0);

            if (getUsedItemHand().equals(playerData.getRightHand())) {
                setEatingAnimation(ArmsEnum.RIGHT_ARM, false);
            } else if (getUsedItemHand().equals(playerData.getLeftHand())) {
                setEatingAnimation(ArmsEnum.LEFT_ARM, true);
            }
        }
    }

    @Unique
    private void setEatingAnimation(ArmsEnum arm, boolean mirror) {
        overlayAnimationContainer.setCurrentAnimation(EATINHG_ANIMATION.getAnimation());
        overlayAnimationContainer.setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + EATINHG_ANIMATION.getAnimationId());
        disableArmOverlayPos(arm);
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(mirror);
    }


    @Unique
    private void playTridentAnimation() {
        if (CONFIG.getTridentAnimationConfig().isEnabled()) {
            overlayAnimationContainer.setAnimationFadeTime(10);
            overlayAnimationContainer.setAnimationSpeed(CONFIG.getTridentAnimationConfig().getSpeedMultiplier());
            overlayAnimationContainer.setAnimationPriority(0);

            if (getUsedItemHand().equals(playerData.getRightHand())) {
                setTridentAnimation(true, 55);
            } else if (getUsedItemHand().equals(playerData.getLeftHand())) {
                setTridentAnimation(false, -55);
            }
        } else {
            disableActiveArm();
            mainAnimationContainer.setAnimationPriority(0);
            mainAnimationContainer.setAnimationFadeTime(1);
        }
    }

    @Unique
    private void setTridentAnimation(boolean isRightHand, int yawOffset) {
        if (isCrouching()) {
            disableArmOverlayPos(ArmsEnum.RIGHT_ARM);
            disableArmOverlayPos(ArmsEnum.LEFT_ARM);
        } else {
            overlayAnimationContainer.setCurrentAnimation(TRIDENT_ANIMATION.getAnimation());
            overlayAnimationContainer.setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + TRIDENT_ANIMATION.getAnimationId());
        }
        setYBodyRot(playerData.getPlayerHeadYaw() + yawOffset);
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
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
        mainAnimationContainer.setAnimationFadeTime(1);
    }

    @Unique
    private void setBowAnimation() {
        overlayAnimationContainer.setAnimationFadeTime(10);
        overlayAnimationContainer.setAnimationSpeed(CONFIG.getBowAnimationsConfig().getSpeedMultiplier());
        overlayAnimationContainer.setAnimationPriority(0);

        if (getUsedItemHand().equals(playerData.getRightHand())) {
            setBowAnimationForHand(true);
        } else if (getUsedItemHand().equals(playerData.getLeftHand())) {
            setBowAnimationForHand(false);
        }
    }

    @Unique
    private void setBowAnimationForHand(boolean isRightHand) {
        if (isCrouching()) {
            overlayAnimationContainer.setCurrentAnimation(BOW_SNEAK_ANIMATION.getAnimation());
            overlayAnimationContainer.setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_SNEAK_ANIMATION.getAnimationId());
            overlayAnimationContainer.setAnimationFadeTime(1);
        } else {
            overlayAnimationContainer.setCurrentAnimation(BOW_IDLE_ANIMATION.getAnimation());
            overlayAnimationContainer.setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_IDLE_ANIMATION.getAnimationId());
        }
        disableArmOverlayPos(isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);

        if (isRightHand) {
            ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.RIGHT_BOW_MODIFIER.getModifierId())).enabled = true;
            setYBodyRot(playerData.getPlayerHeadYaw() - 90);
        } else {
            ((AdjustmentModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.LEFT_BOW_MODIFIER.getModifierId())).enabled = true;
            setYBodyRot(playerData.getPlayerHeadYaw() + 90);
        }
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
    }

    @Unique
    private void playShieldAnimation() {
        if (CONFIG.getShieldAnimationConfig().isEnabled()) {
            overlayAnimationContainer.setAnimationFadeTime(10);
            overlayAnimationContainer.setAnimationSpeed(CONFIG.getShieldAnimationConfig().getSpeedMultiplier());
            overlayAnimationContainer.setAnimationPriority(0);

            if (getUsedItemHand().equals(playerData.getRightHand())) {
                setShieldAnimation(true);
            } else if (getUsedItemHand().equals(playerData.getLeftHand())) {
                setShieldAnimation(false);
            }
        } else {
            disableActiveArm();
            mainAnimationContainer.setAnimationPriority(0);
            mainAnimationContainer.setAnimationFadeTime(5);
        }
    }

    @Unique
    private void setShieldAnimation(boolean isRightHand) {
        if (isCrouching()) {
            overlayAnimationContainer.setCurrentAnimation(SHIELD_SNEAK_ANIMATION.getAnimation());
            overlayAnimationContainer.setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + SHIELD_SNEAK_ANIMATION.getAnimationId());
        } else {
            overlayAnimationContainer.setCurrentAnimation(SHIELD_ANIMATION.getAnimation());
            overlayAnimationContainer.setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + SHIELD_ANIMATION.getAnimationId());
        }
        disableArmOverlayPos(isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
    }


    @Unique
    private void disableArmBasedOnHand(InteractionHand hand) {
        builder = mainAnimationContainer.getCurrentAnimation().mutableCopy();
        disableArmInBuilder(hand == playerData.getRightHand() ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        mainAnimationContainer.setCurrentAnimation(builder.build());
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

    @Unique
    public void disableArmOverlayPos(ArmsEnum arm) {
        builder = mainAnimationContainer.getCurrentAnimation().mutableCopy();
        var currentArm = builder.getPart(arm.getArmId());
        if (currentArm != null) {
            currentArm.setEnabled(false);
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        mainAnimationContainer.setCurrentAnimation(builder.build());
    }

    @Unique
    public void disableAnimation() {
        mainAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
        mainAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.getAnimationId());
    }

    @Unique
    public void disableAnimationOverlay() {
        overlayAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
        overlayAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.getAnimationId());
    }

    @Unique
    public void disableUpHandAnimation() {
        upHandAnimationContainer.setCurrentAnimation(BLANK_LOOP_ANIMATION.getAnimation());
        upHandAnimationContainer.setCurrentAnimationId(BLANK_LOOP_ANIMATION.getAnimationId());
    }

    @Unique
    public void loopedToolAnimation(PlayerAnimations.Animations animation, PlayerAnimations.Animations
            sneakAnimation, ClientConfig.AnimationConfig config, int fade, int priority) {
        if (config.isEnabled()) {
            overlayAnimationContainer.setAnimationFadeTime(fade);
            overlayAnimationContainer.setAnimationSpeed(config.getSpeedMultiplier());
            overlayAnimationContainer.setAnimationPriority(priority);
            ((MirrorModifier) overlayAnimationContainer.getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(playerData.getRightHand() != MAIN_HAND);

            overlayAnimationContainer.setCurrentAnimation(isCrouching() ? sneakAnimation.getAnimation() : animation.getAnimation());
            overlayAnimationContainer.setCurrentAnimationId(isCrouching() ? sneakAnimation.getAnimationId() : animation.getAnimationId());
        } else {
            genericHandswing();
        }
    }

    @Unique
    public void genericHandswing() {
        disableArmBasedOnHand(swingingArm);
        mainAnimationContainer.setCurrentAnimationId("handswinging" + mainAnimationContainer.getCurrentAnimationId());
        mainAnimationContainer.setAnimationFadeTime(0);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playWalkingAnimation() {
        if (!CONFIG.getWalkingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed((float) (playerData.getMovementSpeed() * CONFIG.getWalkingAnimationConfig().getSpeedMultiplier()));

            mainAnimationContainer.setCurrentAnimation(WALKING_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(WALKING_ANIMATION.getAnimationId());
        }

        mainAnimationContainer.setAnimationFadeTime(0);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playWalkingBackwardsAnimation() {
        if (!CONFIG.getWalkingBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed((float) (playerData.getMovementSpeed() * CONFIG.getWalkingBackwardsAnimationConfig().getSpeedMultiplier()));

            mainAnimationContainer.setCurrentAnimation(WALKING_BACKWARDS_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(WALKING_BACKWARDS_ANIMATION.getAnimationId());
        }

        mainAnimationContainer.setAnimationFadeTime(10);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playWalkingSneakAnimation() {
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed((float) (playerData.getMovementSpeed() * CONFIG.getWalkingSneakAnimationConfig().getSpeedMultiplier()));

            mainAnimationContainer.setCurrentAnimation(WALKING_SNEAK_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(WALKING_SNEAK_ANIMATION.getAnimationId());
        }

        if (mainAnimationContainer.getPrevAnimationId().equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || mainAnimationContainer.getPrevAnimationId().equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            mainAnimationContainer.setAnimationFadeTime(10);
        } else {
            mainAnimationContainer.setAnimationFadeTime(5);
        }
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playWalkingSneakBackwardsAnimation() {
        if (!CONFIG.getWalkingSneakBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed((float) (playerData.getMovementSpeed() * CONFIG.getWalkingSneakBackwardsAnimationConfig().getSpeedMultiplier()));

            mainAnimationContainer.setCurrentAnimation(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId());
        }

        if (mainAnimationContainer.getPrevAnimationId().equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || mainAnimationContainer.getPrevAnimationId().equals(WALKING_SNEAK_ANIMATION.getAnimationId())) {
            mainAnimationContainer.setAnimationFadeTime(10);
        } else {
            mainAnimationContainer.setAnimationFadeTime(5);
        }
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playRunningAnimation() {
        if (!CONFIG.getRunningAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed((float) (playerData.getMovementSpeed() * CONFIG.getRunningAnimationConfig().getSpeedMultiplier()));

            mainAnimationContainer.setCurrentAnimation(RUNNING_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(RUNNING_ANIMATION.getAnimationId());
        }
        mainAnimationContainer.setAnimationFadeTime(10);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playTurnLeftAndRightAnimation() {
        if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setCurrentAnimation((playerData.getBodyYawDelta() < 0) ? TURN_LEFT_ANIMATION.getAnimation() : TURN_RIGHT_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId((playerData.getBodyYawDelta() < 0) ? TURN_LEFT_ANIMATION.getAnimationId() : TURN_RIGHT_ANIMATION.getAnimationId());

            if ((((float) 1 / 2) * playerData.getBodyYawDelta()) > 2 || (((float) 1 / 2) * playerData.getBodyYawDelta()) < 2) {
                mainAnimationContainer.setAnimationSpeed(CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
            } else {
                mainAnimationContainer.setAnimationSpeed(abs((((float) 1 / 2) * playerData.getBodyYawDelta()) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier()));
            }
        }

        mainAnimationContainer.setAnimationFadeTime(10);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playIdleStandingAnimation() {
        if (!CONFIG.getIdleStandingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setCurrentAnimation(IDLE_STANDING_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(IDLE_STANDING_ANIMATION.getAnimationId());

            mainAnimationContainer.setAnimationSpeed(CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier());
        }
        mainAnimationContainer.setAnimationFadeTime(10);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playIdleSneakAnimation() {
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setCurrentAnimation(IDLE_SNEAK_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(IDLE_SNEAK_ANIMATION.getAnimationId());

            mainAnimationContainer.setAnimationSpeed(CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier());

        }

        if (mainAnimationContainer.getPrevAnimationId().equals(WALKING_SNEAK_ANIMATION.getAnimationId()) || mainAnimationContainer.getPrevAnimationId().equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            mainAnimationContainer.setAnimationFadeTime(10);
        } else {
            mainAnimationContainer.setAnimationFadeTime(5);
        }
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playFlyIdleCreativeAnimation() {
        if (!CONFIG.getIdleCreativeFlyingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setCurrentAnimation(IDLE_CREATIVE_FLYING_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(IDLE_CREATIVE_FLYING_ANIMATION.getAnimationId());

            mainAnimationContainer.setAnimationSpeed(CONFIG.getIdleCreativeFlyingAnimationConfig().getSpeedMultiplier());
        }
        mainAnimationContainer.setAnimationFadeTime(10);
        mainAnimationContainer.setAnimationPriority(0);
    }

    @Unique
    private void playFallAnimation() {
        if (playerData.getVectorY() < -0.6 && !isPassenger() && !onGround()) {
            if (!CONFIG.getFallingAnimationConfig().isEnabled()) {
                disableAnimation();
            } else {
                mainAnimationContainer.setCurrentAnimation(FALLING_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(FALLING_ANIMATION.getAnimationId());
                mainAnimationContainer.setAnimationSpeed(CONFIG.getFallingAnimationConfig().getSpeedMultiplier());
            }
            mainAnimationContainer.setAnimationFadeTime(10);
            mainAnimationContainer.setAnimationPriority(0);
        }
    }

    @Unique
    private void getClimbAnimationStatus() {
        if (!CONFIG.getClimbingAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            if (!onGround() && !isPassenger()) {
                mainAnimationContainer.setAnimationSpeed(CONFIG.getClimbingAnimationsConfig().getSpeedMultiplier());

                Block block = this.clientLevel.getBlockState(blockPosition()).getBlock();
                if ((block instanceof LadderBlock || block instanceof VineBlock)) {
                    mainAnimationContainer.setAnimationFadeTime(10);
                    mainAnimationContainer.setAnimationPriority(0);
                    setBodyRotationInLeadderAndVineBlocks();
                    playClimbingAnimation();
                } else if ((block instanceof TwistingVinesPlantBlock
                        || block instanceof WeepingVinesPlantBlock
                        || block instanceof TwistingVinesBlock
                        || block instanceof WeepingVinesBlock
                        || block instanceof ScaffoldingBlock)) {
                    mainAnimationContainer.setAnimationFadeTime(10);
                    mainAnimationContainer.setAnimationPriority(0);
                    setBodyRotationOnClimbableBlocks();
                    playClimbingAnimation();
                } else if (block instanceof PowderSnowBlock) {
                    mainAnimationContainer.setAnimationFadeTime(10);
                    mainAnimationContainer.setAnimationPriority(0);

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
            if (playerData.getVectorY() > 0) {
                mainAnimationContainer.setCurrentAnimation(isCrouching() ? CLIMBING_SNEAK_ANIMATION.getAnimation() : CLIMBING_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(isCrouching() ? CLIMBING_SNEAK_ANIMATION.getAnimationId() : CLIMBING_ANIMATION.getAnimationId());
            } else if (playerData.getVectorY() < 0) {
                mainAnimationContainer.setCurrentAnimation(CLIMBING_BACKWARDS_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(CLIMBING_BACKWARDS_ANIMATION.getAnimationId());
            } else {
                mainAnimationContainer.setCurrentAnimation(isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimation() : CLIMBING_IDLE_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId() : CLIMBING_IDLE_ANIMATION.getAnimationId());
            }
        }
    }

    @Unique
    private void setBodyRotationOnClimbableBlocks() {
        if (!(getUseItem().getItem() instanceof BowItem)) {
            playerData.setPrevPlayerBodyYaw(((float) toDegrees(atan2((blockPosition().getZ() + 0.5 - playerData.getPlayerPosition().z), (blockPosition().getX()) + 0.5 - playerData.getPlayerPosition().x)) - 90));
            playerData.setPlayerHeadYaw(getYHeadRot());
            playerData.setPrevPlayerBodyYaw(((playerData.getPlayerBodyYaw() % 360) + 360) % 360);
            playerData.setPlayerHeadYaw(((playerData.getPlayerHeadYaw() % 360) + 360) % 360);
            setYBodyRot(playerData.getPlayerBodyYaw());
            playerData.setPlayerHeadYaw(playerData.getPlayerHeadYaw() - playerData.getPlayerBodyYaw());
            playerData.setPlayerHeadYaw(((playerData.getPlayerHeadYaw() % 360) + 360) % 360);

            if (playerData.getPlayerHeadYaw() > 90 && playerData.getPlayerHeadYaw() <= 180) {
                setYHeadRot(playerData.getPlayerBodyYaw() + 90);
            } else if (playerData.getPlayerHeadYaw() > 180 && playerData.getPlayerHeadYaw() < 270) {
                setYHeadRot(playerData.getPlayerBodyYaw() + 270);
            }
        }
    }

    @Unique
    private void setBodyRotationInLeadderAndVineBlocks() {
        if (!(getUseItem().getItem() instanceof BowItem)) {
            String blockStateString = String.valueOf(this.clientLevel.getBlockState(blockPosition()));
            playerData.setPlayerBodyYaw(getVisualRotationYInDegrees());
            playerData.setPlayerHeadYaw(getYHeadRot());
            if (blockStateString.contains("facing=north") || blockStateString.contains("south=true")) {
                playerData.setPlayerBodyYaw(0);
            } else if (blockStateString.contains("facing=south") || blockStateString.contains("north=true")) {
                playerData.setPlayerBodyYaw(180);
            } else if (blockStateString.contains("facing=west") || blockStateString.contains("east=true")) {
                playerData.setPlayerBodyYaw(270);
            } else if (blockStateString.contains("facing=east") || blockStateString.contains("west=true")) {
                playerData.setPlayerBodyYaw(90);
            }

            playerData.setPlayerBodyYaw(((playerData.getPlayerBodyYaw() % 360) + 360) % 360);
            playerData.setPlayerHeadYaw(((playerData.getPlayerHeadYaw() % 360) + 360) % 360);
            setYBodyRot(playerData.getPlayerBodyYaw());
            playerData.setPlayerHeadYaw(playerData.getPlayerHeadYaw() - playerData.getPlayerBodyYaw());
            playerData.setPlayerHeadYaw(((playerData.getPlayerHeadYaw() % 360) + 360) % 360);

            if (playerData.getPlayerHeadYaw() > 90 && playerData.getPlayerHeadYaw() <= 180) {
                setYHeadRot(playerData.getPlayerBodyYaw() + 90);
            } else if (playerData.getPlayerHeadYaw() > 180 && playerData.getPlayerHeadYaw() < 270) {
                setYHeadRot(playerData.getPlayerBodyYaw() + 270);
            }
        }
    }

    @Unique
    private void playCrawlAnimation() {
        if (!CONFIG.getCrawlingAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            if (isVisuallyCrawling()) {
                mainAnimationContainer.setAnimationSpeed(CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier());
                if (playerData.getMovementSpeed() > 0.0649) {
                    mainAnimationContainer.setAnimationSpeed(mainAnimationContainer.getAnimationSpeed() + (float) playerData.getMovementSpeed());
                }
                if (playerData.getMovementSpeed() > 0 && !playerData.isMovingBackwards()) {
                    mainAnimationContainer.setCurrentAnimation(CRAWLING_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(CRAWLING_ANIMATION.getAnimationId());
                } else if (playerData.getMovementSpeed() > 0) {
                    mainAnimationContainer.setCurrentAnimation(CRAWLING_BACKWARDS_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(CRAWLING_BACKWARDS_ANIMATION.getAnimationId());
                } else {
                    mainAnimationContainer.setCurrentAnimation(CRAWLING_IDLE_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(CRAWLING_IDLE_ANIMATION.getAnimationId());
                }
                mainAnimationContainer.setAnimationFadeTime(10);
                mainAnimationContainer.setAnimationPriority(0);
            }
        }
    }

    @Unique
    private void playInWaterAnimations() {
        if (!CONFIG.getInWaterAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed(CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier());
            mainAnimationContainer.setAnimationFadeTime(10);
            mainAnimationContainer.setAnimationPriority(0);
            if ((isInWaterOrBubble() || isInLava()) && !onGround() && !isVisuallySwimming()) {
                if (playerData.getMovementSpeed() > 0 && !playerData.isMovingBackwards()) {
                    mainAnimationContainer.setCurrentAnimation(IN_WATER_FORWARD_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(IN_WATER_FORWARD_ANIMATION.getAnimationId());
                } else if (playerData.getMovementSpeed() > 0) {
                    mainAnimationContainer.setCurrentAnimation(IN_WATER_BACKWARDS_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(IN_WATER_BACKWARDS_ANIMATION.getAnimationId());
                } else if (playerData.getVectorY() > 0) {
                    mainAnimationContainer.setCurrentAnimation(IN_WATER_UP_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(IN_WATER_UP_ANIMATION.getAnimationId());
                } else {
                    mainAnimationContainer.setCurrentAnimation(IN_WATER_IDLE_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(IN_WATER_IDLE_ANIMATION.getAnimationId());
                }
            } else if (isInWaterOrBubble() && isVisuallySwimming()) {
                mainAnimationContainer.setCurrentAnimation(IN_WATER_SWIMMING_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(IN_WATER_SWIMMING_ANIMATION.getAnimationId());
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
                    mainAnimationContainer.setCurrentAnimation(MINECART_IDLE_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(MINECART_IDLE_ANIMATION.getAnimationId());
                    mainAnimationContainer.setAnimationSpeed(CONFIG.getMinecartAnimationsConfig().getSpeedMultiplier());
                    mainAnimationContainer.setAnimationFadeTime(10);
                    mainAnimationContainer.setAnimationPriority(0);
                }
            } else if (vehicle instanceof Horse
                    || vehicle instanceof SkeletonHorse
                    || vehicle instanceof ZombieHorse
                    || vehicle instanceof Donkey
                    || vehicle instanceof Mule) {
                if (playerData.getMovementSpeed() > 0 && !playerData.isMovingBackwards()) {
                    if (!CONFIG.getHorseRunningAnimationConfig().isEnabled()) {
                        disableAnimation();
                    } else {
                        mainAnimationContainer.setAnimationSpeed(CONFIG.getHorseRunningAnimationConfig().getSpeedMultiplier());
                        mainAnimationContainer.setAnimationFadeTime(10);
                        mainAnimationContainer.setAnimationPriority(0);

                        mainAnimationContainer.setCurrentAnimation(HORSE_RUNNING_ANIMATION.getAnimation());
                        mainAnimationContainer.setCurrentAnimationId(HORSE_RUNNING_ANIMATION.getAnimationId());
                    }
                } else {
                    if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                        disableAnimation();
                    } else {
                        mainAnimationContainer.setAnimationSpeed(CONFIG.getHorseIdleAnimationConfig().getSpeedMultiplier());
                        mainAnimationContainer.setAnimationFadeTime(10);

                        mainAnimationContainer.setCurrentAnimation(HORSE_IDLE_ANIMATION.getAnimation());
                        mainAnimationContainer.setCurrentAnimationId(HORSE_IDLE_ANIMATION.getAnimationId());
                    }
                }
                if (isUsingItem()) {
                    if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                        disableAnimation();
                    } else {
                        mainAnimationContainer.setCurrentAnimation(HORSE_IDLE_ANIMATION.getAnimation());
                        mainAnimationContainer.setCurrentAnimationId(HORSE_IDLE_ANIMATION.getAnimationId());
                    }
                }
            } else if (vehicle instanceof Boat || vehicle instanceof ChestBoat) {
                if (!CONFIG.getBoatAnimationsConfig().isEnabled()) {
                    disableAnimation();
                } else {
                    mainAnimationContainer.setAnimationSpeed(CONFIG.getBoatAnimationsConfig().getSpeedMultiplier());

                    mainAnimationContainer.setCurrentAnimation(BOAT_IDLE_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(BOAT_IDLE_ANIMATION.getAnimationId());

                    boolean isLeftPaddleMoving = ((Boat) getVehicle()).getPaddleState(0);
                    boolean isRightPaddleMoving = ((Boat) getVehicle()).getPaddleState(1);

                    if (playerData.getMovementSpeed() > 0 && !playerData.isMovingBackwards()) {
                        if (isLeftPaddleMoving && isRightPaddleMoving) {
                            mainAnimationContainer.setCurrentAnimation(BOAT_FORWARD_ANIMATION.getAnimation());
                            mainAnimationContainer.setCurrentAnimationId(BOAT_FORWARD_ANIMATION.getAnimationId());
                        } else if (isLeftPaddleMoving) {
                            mainAnimationContainer.setCurrentAnimation(BOAT_TURN_LEFT_ANIMATION.getAnimation());
                            mainAnimationContainer.setCurrentAnimationId(BOAT_TURN_LEFT_ANIMATION.getAnimationId());
                        } else if (isRightPaddleMoving) {
                            mainAnimationContainer.setCurrentAnimation(BOAT_TURN_RIGHT_ANIMATION.getAnimation());
                            mainAnimationContainer.setCurrentAnimationId(BOAT_TURN_RIGHT_ANIMATION.getAnimationId());
                        }
                    }

                    mainAnimationContainer.setAnimationFadeTime(10);
                    mainAnimationContainer.setAnimationPriority(0);
                }
            } else {
                if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                    disableAnimation();
                } else {
                    mainAnimationContainer.setAnimationSpeed(CONFIG.getHorseIdleAnimationConfig().getSpeedMultiplier());
                    mainAnimationContainer.setCurrentAnimation(HORSE_IDLE_ANIMATION.getAnimation());
                    mainAnimationContainer.setCurrentAnimationId(HORSE_IDLE_ANIMATION.getAnimationId());
                }
                mainAnimationContainer.setAnimationFadeTime(10);
                mainAnimationContainer.setAnimationPriority(0);
            }
        }
    }

    @Unique
    private void playSleepAnimation() {
        if (isSleeping()) {
            if (!CONFIG.getSleepingAnimationsConfig().isEnabled()) {
                disableAnimationOverlay();
                disableUpHandAnimation();
            } else {
                mainAnimationContainer.setAnimationFadeTime(10);
                mainAnimationContainer.setAnimationSpeed(CONFIG.getSleepingAnimationsConfig().getSpeedMultiplier());
                mainAnimationContainer.setAnimationPriority(0);
                disableAnimation();

                overlayAnimationContainer.setCurrentAnimation(SLEEPING_ANIMATION.getAnimation());
                overlayAnimationContainer.setCurrentAnimationId(SLEEPING_ANIMATION.getAnimationId());
            }
        }
    }

    @Unique
    private void playElytraAnimation() {
        if (!CONFIG.getElytraAnimationsConfig().isEnabled()) {
            disableAnimation();
        } else {
            if (isFallFlying()) {
                mainAnimationContainer.setCurrentAnimation(ELYTRA_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(ELYTRA_ANIMATION.getAnimationId());

                mainAnimationContainer.setAnimationSpeed(CONFIG.getElytraAnimationsConfig().getSpeedMultiplier());
                mainAnimationContainer.setAnimationPriority(0);
                mainAnimationContainer.setAnimationFadeTime(10);
            }
        }
    }

    @Unique
    private void playFlyAnimation() {
        double vyfly = Math.round(playerData.getVectorY() * 1000.0) / 1000.0;
        if ((vyfly == 0.0 || Math.abs(vyfly) == 0.375) && !onGround() && !isInWaterOrBubble()) {
            playerData.setFlychecker(playerData.getFlychecker() + 1);
        } else if (Math.abs(vyfly) > 0.375 || onGround()) {
            playerData.setFlychecker(0);
        }

        if (playerData.getFlychecker() > 10) {
            playFlyIdleCreativeAnimation();
        }
    }

    @Unique
    private void playBaseAnimations() {
        if (playerData.getMovementSpeed() < 0.23 && playerData.getMovementSpeed() > 0 && !playerData.isMovingBackwards() && !isCrouching()) {
            if (playerData.isOnFence()) {
                playOnFenceAnimation();
            } else {
                playWalkingAnimation();
            }
        } else if (playerData.isMovingBackwards() && !isCrouching()) {
            playWalkingBackwardsAnimation();
        } else if (playerData.getMovementSpeed() > 0.23 && isSprinting() && !playerData.isMovingBackwards() && !isCrouching()) {
            playRunningAnimation();
        } else if (playerData.getMovementSpeed() == 0 && !isCrouching() && !isSprinting()) {
            playTurningStandingAnimation();
        } else if (isCrouching() && playerData.getMovementSpeed() == 0 && !playerData.isMovingBackwards()) {
            playSneakingAnimation();
        } else if (isCrouching() && playerData.getMovementSpeed() > 0 && !playerData.isMovingBackwards()) {
            playWalkingSneakAnimation();
        } else if (isCrouching() && playerData.getMovementSpeed() > 0) {
            playWalkingSneakBackwardsAnimation();
        }
    }

    @Unique
    private void playOnFenceAnimation() {
        if (!CONFIG.getOnFenceAnimationConfig().isEnabled()) {
            playWalkingAnimation();
        } else {
            mainAnimationContainer.setAnimationSpeed((float) playerData.getMovementSpeed() * CONFIG.getOnFenceAnimationConfig().getSpeedMultiplier());

            mainAnimationContainer.setCurrentAnimation(ON_FENCE_WALKING_ANIMATION.getAnimation());
            mainAnimationContainer.setCurrentAnimationId(ON_FENCE_WALKING_ANIMATION.getAnimationId());

            mainAnimationContainer.setAnimationFadeTime(10);
            mainAnimationContainer.setAnimationPriority(0);
        }
    }

    @Unique
    private void playSneakingAnimation() {
        if (playerData.getBodyYawDelta() != 0) {
            playWalkingSneakAnimation();
            if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
                disableAnimation();
            } else {
                if ((((float) 1 / 2) * playerData.getBodyYawDelta()) > 1.5 || (((float) 1 / 2) * playerData.getBodyYawDelta()) < -1.5) {
                    mainAnimationContainer.setAnimationSpeed(CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
                } else {
                    mainAnimationContainer.setAnimationSpeed(abs((((float) 1 / 2) * playerData.getBodyYawDelta()) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier()));
                }
            }
        } else {
            playIdleSneakAnimation();
        }
    }

    @Unique
    private void playTurningStandingAnimation() {
        if (playerData.getBodyYawDelta() != 0) {
            playTurnLeftAndRightAnimation();
        } else {
            if (CONFIG.onFenceAnimationConfig.isEnabled() && playerData.isOnFence()) {
                mainAnimationContainer.setCurrentAnimation(ON_FENCE_IDLE_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(ON_FENCE_IDLE_ANIMATION.getAnimationId());
            } else if (CONFIG.onEdgeAnimationConfig.isEnabled() && playerData.isOnEdge()) {
                mainAnimationContainer.setCurrentAnimation(ON_EDGE_IDLE_ANIMATION.getAnimation());
                mainAnimationContainer.setCurrentAnimationId(ON_EDGE_IDLE_ANIMATION.getAnimationId());
            } else {
                playIdleStandingAnimation();
            }

            if (mainAnimationContainer.getPrevAnimationId().equals(IDLE_STANDING_ANIMATION.getAnimationId())
                    || mainAnimationContainer.getPrevAnimationId().equals(WALKING_SNEAK_ANIMATION.getAnimationId())) {
                mainAnimationContainer.setAnimationFadeTime(5);
            } else {
                mainAnimationContainer.setAnimationFadeTime(10);
            }
            mainAnimationContainer.setAnimationPriority(0);
        }
    }
}