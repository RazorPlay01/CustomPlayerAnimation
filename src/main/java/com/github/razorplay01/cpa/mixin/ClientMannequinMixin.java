package com.github.razorplay01.cpa.mixin;

//? if >=1.21.9 {

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.animation.animations.AnimationProvider;
import com.github.razorplay01.cpa.util.PlayerData;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.IAnimationControl;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimatedPlayer;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.github.razorplay01.cpa.ModTemplate.*;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.util.CustomModifiers.*;
import static com.github.razorplay01.cpa.util.Util.*;
import static net.minecraft.world.InteractionHand.*;

@Mixin(ClientMannequin.class)
public abstract class ClientMannequinMixin extends Mannequin implements IAnimationControl, ICustomAnimatedPlayer {
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

	protected ClientMannequinMixin(EntityType<Mannequin> entityType, Level level) {
		super(entityType, level);
	}


	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(Level level, PlayerSkinRenderCache playerSkinRenderCache, CallbackInfo ci) {
		this.mainAnimationContainer = new AnimationContainer(
				(AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((ClientMannequin) (Object) this, MAIN_ANIMATION_CONTAINER_LAYER_ID),
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
		this.overlayAnimationContainer = new AnimationContainer(
				(AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((ClientMannequin) (Object) this, OVERLAY_ANIMATION_CONTAINER_LAYER_ID),
				new HashMap<>(
						Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
								Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f),
								Modifiers.SHIELD_MODIFIER.getModifierId(), createShieldModifier((ClientMannequin) (Object) this),
								Modifiers.HAND_SWING_MODIFIER.getModifierId(), createSwingModifier((ClientMannequin) (Object) this, mainAnimationContainer),
								Modifiers.BOW_MODIFIER.getModifierId(), createBowModifier((ClientMannequin) (Object) this)
						)),
				getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
				AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
				"",
				0,
				0,
				0,
				0);
		this.specialAnimationContainer = new AnimationContainer(
				(AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((ClientMannequin) (Object) this, SPECIAL_ANIMATION_CONTAINER_LAYER_ID),
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

	@Inject(method = "tick()V", at = @At(value = "TAIL"))
	public void tick(CallbackInfo ci) {
		super.tick();
		if (mainAnimationContainer == null || overlayAnimationContainer == null || specialAnimationContainer == null)
			return;
		this.playerData.update((ClientMannequin) (Object) this);

		enableAllBodyPartsInAllContainers();

		overlayAnimationContainer.resetAnimationProperties();

		this.actualAnimationContext = new AnimationContext(mainAnimationContainer, overlayAnimationContainer, specialAnimationContainer, (ClientMannequin) (Object) this, playerData, this, this);

		playAnimations();
		updateAnimationSpeeds();

		checkMainHandItemForArmDisabling();
		checkOffHandItemForArmDisabling();

		applyDisables();

		updateAnimationContainers();

		this.playerData.setPrevPlayerPosition(this.playerData.getPlayerPosition());
		this.playerData.setPrevPlayerBodyYaw(this.playerData.getPlayerBodyYaw());
		this.playerData.setPrevOnGround(onGround());
	}

	@Override
	public AnimationContainer getMainAnimationCPA() {
		return this.mainAnimationContainer;
	}

	@Override
	public AnimationContainer getOverlayAnimationCPA() {
		return this.overlayAnimationContainer;
	}

	@Override
	public AnimationContainer getSpecialAnimationCPA() {
		return this.specialAnimationContainer;
	}

	@Override
	public AnimationContext getAnimationContext() {
		return this.actualAnimationContext;
	}

	@Override
	public void disableBodyPartAnimation(AnimationContainer animationContainer, BodyParts bodyPart) {
		animationContainer.getDisabledBoneIds().add(bodyPart.getPartId());
	}

	@Unique
	public void enabledAllBodyPartsAnimation(AnimationContainer animationContainer) {
		animationContainer.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {
			//[]
		});
		animationContainer.getDisabledBoneIds().clear();
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
		((SpeedModifier) mainAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = mainAnimationContainer.getAnimationSpeed() * CONFIG.getGeneral().getAnimationSpeedMultiplier();
		((SpeedModifier) overlayAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = overlayAnimationContainer.getAnimationSpeed() * CONFIG.getGeneral().getAnimationSpeedMultiplier();
		((SpeedModifier) specialAnimationContainer.getAnimationModifiers().get(Modifiers.SPEED_MODIFIER.getModifierId())).speed = specialAnimationContainer.getAnimationSpeed() * CONFIG.getGeneral().getAnimationSpeedMultiplier();
	}

	@Unique
	private void enableAllBodyPartsInAllContainers() {
		enabledAllBodyPartsAnimation(mainAnimationContainer);
		enabledAllBodyPartsAnimation(overlayAnimationContainer);
		enabledAllBodyPartsAnimation(specialAnimationContainer);
	}

	@Unique
	private void applyDisables() {
		cpa$applyDisableToContainer(mainAnimationContainer);
		cpa$applyDisableToContainer(overlayAnimationContainer);
		cpa$applyDisableToContainer(specialAnimationContainer);
	}

	//? if >=1.21.1{
	@Unique
	private void cpa$preserveOnlyHeadRoll(java.util.function.Function<String, com.zigythebird.playeranimcore.bones.AdvancedPlayerAnimBone> getBoneFunc, Set<String> disabledIds) {
		String headId = BodyParts.HEAD.getPartId();

		if (disabledIds.contains(headId)) {
			return;
		}

		com.zigythebird.playeranimcore.bones.AdvancedPlayerAnimBone head = getBoneFunc.apply(headId);
		if (head == null) {
			return;
		}

		head.rotXEnabled = false;
		head.rotYEnabled = false;
		head.rotZEnabled = true;
	}
	//?}
	//? if <1.21.1{
	/*@Unique
	private void cpa$preserveOnlyHeadRollLegacy(com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension extension, Set<String> disabledIds) {
		String headId = BodyParts.HEAD.getPartId();

		if (disabledIds.contains(headId)) {
			return;
		}

		Set<String> disabledChannels = new HashSet<>();
		disabledChannels.add("rotX");
		disabledChannels.add("rotY");

		extension.cpa$setDisabledBoneChannels(headId, disabledChannels);
	}
	*///?}

	@Unique
	private void cpa$applyDisableToContainer(AnimationContainer container) {
		Set<String> disabledIds = container.getDisabledBoneIds();

		//? if >=1.21.1{
		if (disabledIds.isEmpty()) {
			container.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {
				cpa$preserveOnlyHeadRoll(getBoneFunc, disabledIds);
			});
		} else {
			container.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {
				for (String boneId : disabledIds) {
					getBoneFunc.apply(boneId).setEnabled(false);
					cpa$preserveOnlyHeadRoll(getBoneFunc, disabledIds);
				}
			});
		}
		//?}

		//? if <1.21.1{
		/*ModifierLayer<?> layer = container.getAnimationController();
		IAnimation animation = layer.getAnimation();

		if (animation instanceof KeyframeAnimationPlayer player) {
			com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension extension = (com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension) player;
			extension.cpa$clearDisabledBones();
			extension.cpa$clearAllDisabledChannels();
			extension.cpa$setDisabledBones(disabledIds);
			cpa$preserveOnlyHeadRollLegacy(extension, disabledIds);
		}
		*///?}
	}

	@Unique
	private void cpa$updateAnimationControllerForEnable(AnimationContainer container, String partIdToEnable) {
		//? if >=1.21.1{
		container.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {
			getBoneFunc.apply(partIdToEnable).setEnabled(true);
			for (String boneId : container.getDisabledBoneIds()) {
				if (!boneId.equals(partIdToEnable)) {
					getBoneFunc.apply(boneId).setEnabled(false);
				}
			}
		});
		//?}

		//? if <1.21.1{
		/*Set<String> remainingDisabled = new HashSet<>(container.getDisabledBoneIds());
		remainingDisabled.remove(partIdToEnable);

		ModifierLayer<?> layer = container.getAnimationController();
		IAnimation animation = layer.getAnimation();

		if (animation instanceof KeyframeAnimationPlayer player) {
			com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension extension = (com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension) player;
			extension.cpa$setDisabledBones(remainingDisabled);
		}
		*///?}
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
		updateAnimationContainer(mainAnimationContainer);
		updateAnimationContainer(overlayAnimationContainer);
		updateAnimationContainer(specialAnimationContainer);
	}

	@Unique
	private void updateAnimationContainer(AnimationContainer animationContainer) {
		if (!animationContainer.getAnimationController().isActive() &&
				!animationContainer.getCurrentAnimationId().equals(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId())) {
			animationContainer.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
			animationContainer.setCurrentAnimationId(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId());
			animationContainer.setAnimationPriority(0);
			animationContainer.setAnimationFadeTime(0);
			animationContainer.setAnimationSpeed(1.0f);
		}

		if ((!Objects.equals(animationContainer.getCurrentAnimationId(), animationContainer.getPrevAnimationId())
				&& animationContainer.getAnimationPriority() >= animationContainer.getPrevAnimationPriority())
				|| !animationContainer.getAnimationController().isActive()) {

			playCurrentAnimation(animationContainer);

			animationContainer.setPrevAnimationId(animationContainer.getCurrentAnimationId());
			animationContainer.setPrevAnimationPriority(animationContainer.getAnimationPriority());
		}
	}

	@Unique
	public void playCurrentAnimation(AnimationContainer animationContainer) {
		animationContainer.getAnimationController().replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn((int) (animationContainer.getAnimationFadeTime() * CONFIG.getGeneral().getAnimationFadeTimeMultiplier()), EasingType.EASE_IN_OUT_SINE),
				RawAnimation.begin().thenPlay(animationContainer.getCurrentAnimation()), false
		);
	}

	@Unique
	private void checkMainHandItemForArmDisabling() {
		if (!getMainHandItem().isEmpty() && isUsingItem() &&
				(((Player) (Object) this).isScoping() || getMainHandItem().getItem() instanceof InstrumentItem || getMainHandItem().getItem() instanceof BrushItem)) {
			this.disableBodyPartAnimationInAllContainers(getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
		}
	}

	@Unique
	private void checkOffHandItemForArmDisabling() {
		if (!getOffhandItem().isEmpty() && isUsingItem() &&
				(((Player) (Object) this).isScoping() || getOffhandItem().getItem() instanceof InstrumentItem || getOffhandItem().getItem() instanceof BrushItem)) {
			this.disableBodyPartAnimationInAllContainers(getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
		}
	}
}
//?}
