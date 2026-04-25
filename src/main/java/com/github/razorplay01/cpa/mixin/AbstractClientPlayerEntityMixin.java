package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.animation.animations.AnimationProvider;
import com.github.razorplay01.cpa.platform.common.util.PlayerData;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.BodyParts;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.IAnimationControl;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimatedPlayer;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import com.mojang.authlib.GameProfile;
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

import static com.github.razorplay01.cpa.ModTemplate.*;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.CustomModifiers.*;
import static com.github.razorplay01.cpa.platform.common.util.Util.*;
import static net.minecraft.world.InteractionHand.*;

//? if >=1.21.1{
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import com.zigythebird.playeranimcore.animation.Animation;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.api.layered.IAnimation;
*///?}

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

	//? if < 1.21.2 {
	/*protected AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
		super(level, blockPos, f, gameProfile);
	}
	*///?}

	//? if >= 1.21.2 {
	protected AbstractClientPlayerEntityMixin(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}
	//?}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo ci) {
		//? if >=1.21.1{
		this.mainAnimationContainer = new AnimationContainer(
				(AnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((AbstractClientPlayer) (Object) this, MAIN_ANIMATION_CONTAINER_LAYER_ID),
				new HashMap<>(
						Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
								Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f),
								Modifiers.ADJUSTMENT_MODIFIER.getModifierId(), createLeanModifier((AbstractClientPlayer) (Object) this))),
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
						Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
								Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f),
								Modifiers.SHIELD_MODIFIER.getModifierId(), createShieldModifier((AbstractClientPlayer) (Object) this),
								Modifiers.HAND_SWING_MODIFIER.getModifierId(), createSwingModifier((AbstractClientPlayer) (Object) this, mainAnimationContainer),
								Modifiers.BOW_MODIFIER.getModifierId(), createBowModifier((AbstractClientPlayer) (Object) this)
						)),
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
		//?}
		//? if <1.21.1{
		/*this.mainAnimationContainer = new AnimationContainer(
				new ModifierLayer<>(),
				new HashMap<>(
						Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
								Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f),
								Modifiers.ADJUSTMENT_MODIFIER.getModifierId(), createLeanModifier((AbstractClientPlayer) (Object) this))),
				getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
				AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
				"",
				0,
				0,
				0,
				0);
		this.overlayAnimationContainer = new AnimationContainer(
				new ModifierLayer<>(),
				new HashMap<>(
						Map.of(Modifiers.MIRROR_MODIFIER.getModifierId(), new MirrorModifier(),
								Modifiers.SPEED_MODIFIER.getModifierId(), new SpeedModifier(1.0f),
								Modifiers.SHIELD_MODIFIER.getModifierId(), createShieldModifier((AbstractClientPlayer) (Object) this),
								Modifiers.HAND_SWING_MODIFIER.getModifierId(), createSwingModifier((AbstractClientPlayer) (Object) this, mainAnimationContainer),
								Modifiers.BOW_MODIFIER.getModifierId(), createBowModifier((AbstractClientPlayer) (Object) this)
						)),
				getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()),
				AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId(),
				"",
				0,
				0,
				0,
				0);
		this.specialAnimationContainer = new AnimationContainer(
				new ModifierLayer<>(),
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
		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1, mainAnimationContainer.getAnimationController());
		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(2, overlayAnimationContainer.getAnimationController());
		PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(3, specialAnimationContainer.getAnimationController());
		*///?}

		addModifiersToContainer(mainAnimationContainer);
		addModifiersToContainer(overlayAnimationContainer);
		addModifiersToContainer(specialAnimationContainer);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		if (mainAnimationContainer == null || overlayAnimationContainer == null || specialAnimationContainer == null)
			return;
		this.playerData.update((AbstractClientPlayer) (Object) this);

		enableAllBodyPartsInAllContainers();

		overlayAnimationContainer.resetAnimationProperties();

		this.actualAnimationContext = new AnimationContext(mainAnimationContainer, overlayAnimationContainer, specialAnimationContainer, (AbstractClientPlayer) (Object) this, playerData, this, this);

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

	@Override
	public void disableBodyPartAnimationInAllContainers(BodyParts bodyPart) {
		this.disableBodyPartAnimation(mainAnimationContainer, bodyPart);
		this.disableBodyPartAnimation(overlayAnimationContainer, bodyPart);
		this.disableBodyPartAnimation(specialAnimationContainer, bodyPart);
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
	public void enableBodyPartAnimation(AnimationContainer animationContainer, BodyParts bodyPart) {
		animationContainer.getDisabledBoneIds().remove(bodyPart.getPartId());
	}

	@Override
	public void enableBodyPartAnimationInAllContainers(BodyParts bodyPart) {
		this.enableBodyPartAnimation(mainAnimationContainer, bodyPart);
		this.enableBodyPartAnimation(overlayAnimationContainer, bodyPart);
		this.enableBodyPartAnimation(specialAnimationContainer, bodyPart);
	}

	@Override
	public void forceEnableBodyPart(BodyParts bodyPart) {
		String partIdToEnable = bodyPart.getPartId();

		// Remover la parte del cuerpo de la lista de partes desactivadas en todos los contenedores
		enableBodyPartAnimationInAllContainers(bodyPart);

		// Forzar actualización de los controladores de animación
		cpa$updateAnimationControllerForEnable(mainAnimationContainer, partIdToEnable);
		cpa$updateAnimationControllerForEnable(overlayAnimationContainer, partIdToEnable);
		cpa$updateAnimationControllerForEnable(specialAnimationContainer, partIdToEnable);

		// Forzar un cambio de animación para asegurar que se actualice el estado
		cpa$forceAnimationRefresh();
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
		cpa$enabledAllBodyPartsAnimation(mainAnimationContainer);
		cpa$enabledAllBodyPartsAnimation(overlayAnimationContainer);
		cpa$enabledAllBodyPartsAnimation(specialAnimationContainer);
	}

	@Unique
	private void cpa$enabledAllBodyPartsAnimation(AnimationContainer animationContainer) {
		//? if >=1.21.1{
		animationContainer.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {});
		 //?}

		//? if <1.21.1{
		/*ModifierLayer<?> layer = animationContainer.getAnimationController();
		IAnimation animation = layer.getAnimation();

		if (animation instanceof KeyframeAnimationPlayer player) {
			com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension extension = (com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension) player;
			extension.cpa$clearDisabledBones();
		}
		*///?}

		animationContainer.getDisabledBoneIds().clear();
	}

	@Unique
	private void applyDisables() {
		cpa$applyDisableToContainer(mainAnimationContainer);
		cpa$applyDisableToContainer(overlayAnimationContainer);
		cpa$applyDisableToContainer(specialAnimationContainer);
	}

	@Unique
	private void cpa$applyDisableToContainer(AnimationContainer container) {
		Set<String> disabledIds = container.getDisabledBoneIds();

		//? if >=1.21.1{
	if (disabledIds.isEmpty()) {
		container.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {});
	} else {
		container.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {
			for (String boneId : disabledIds) {
				getBoneFunc.apply(boneId).setEnabled(false);
			}
		});
	}
	//?}

		//? if <1.21.1{
		/*ModifierLayer<?> layer = container.getAnimationController();
		IAnimation animation = layer.getAnimation();

		if (animation instanceof KeyframeAnimationPlayer player) {
			com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension extension = (com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension) player;
			extension.cpa$setDisabledBones(disabledIds);
		}
		*///?}
	}

	/**
	 * Actualiza el controlador de animación para asegurar que la parte del cuerpo especificada
	 * esté habilitada, incluso después de que se apliquen otras deshabilitaciones
	 */
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

	/**
	 * Fuerza un cambio de animación para asegurar que se actualice el estado
	 * Guarda las animaciones actuales, aplica una animación en blanco y luego restaura las originales
	 */
	@Unique
	private void cpa$forceAnimationRefresh() {
		// Guardar las animaciones actuales de cada contenedor

		/*? if >=1.21.1 {*/Animation/*?} else {*/
		/*IAnimation*//*?}*/ currentMainAnimation = cpa$getCurrentAnimation(mainAnimationContainer);
		/*? if >=1.21.1 {*/Animation/*?} else {*/
		/*IAnimation*//*?}*/ currentOverlayAnimation = cpa$getCurrentAnimation(overlayAnimationContainer);
		/*? if >=1.21.1 {*/Animation/*?} else {*/
		/*IAnimation*//*?}*/ currentSpecialAnimation = cpa$getCurrentAnimation(specialAnimationContainer);

		// Crear animación en blanco
		/*? if >=1.21.1 {*/Animation/*?} else {*/
		/*IAnimation*//*?}*/ blankAnimation =
				//? if >=1.21.1{
				getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId());
				//?}
				//? if <1.21.1{
				/*new KeyframeAnimationPlayer(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
		*///?}

		// Aplicar la animación en blanco con una transición muy rápida
		cpa$replaceAnimationSafely(mainAnimationContainer, blankAnimation, 1);
		cpa$replaceAnimationSafely(overlayAnimationContainer, blankAnimation, 1);
		cpa$replaceAnimationSafely(specialAnimationContainer, blankAnimation, 1);

		// Restaurar las animaciones originales
		if (currentMainAnimation != null) {
			cpa$replaceAnimationSafely(mainAnimationContainer, currentMainAnimation, 2);
		}
		if (currentOverlayAnimation != null) {
			cpa$replaceAnimationSafely(overlayAnimationContainer, currentOverlayAnimation, 2);
		}
		if (currentSpecialAnimation != null) {
			cpa$replaceAnimationSafely(specialAnimationContainer, currentSpecialAnimation, 2);
		}
	}

	/**
	 * Obtiene la animación actual de un contenedor de manera segura
	 */
	@Unique
	private /*? if >=1.21.1 {*/Animation/*?} else {*//*IAnimation*//*?}*/ cpa$getCurrentAnimation(AnimationContainer container) {
		var queued = container.getAnimationController()./*? if >=1.21.1 {*/getCurrentAnimation()/*?} else {*//*getAnimation()*//*?}*/;
		if (queued != null) {
			//? if >=1.21.1{
			return queued.animation();
			//?}
			//? if <1.21.1{
			/*return queued;
			*///?}
		} else {
			return null;
		}
	}

	/**
	 * Reemplaza una animación de manera segura
	 */
	@Unique
	private void cpa$replaceAnimationSafely(AnimationContainer container, /*? if >=1.21.1 {*/Animation/*?} else {*//*IAnimation*//*?}*/ animation, int fadeTime) {
		container.getAnimationController().replaceAnimationWithFade(
				AbstractFadeModifier.standardFadeIn(fadeTime, /*? if >=1.21.1 {*/EasingType.EASE_IN_OUT_SINE/*?} else {*/ /*Ease.INOUTSINE*//*?}*/),
				animation, false
		);
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
				AbstractFadeModifier.standardFadeIn((int) (animationContainer.getAnimationFadeTime() * CONFIG.getGeneral().getAnimationFadeTimeMultiplier()), /*? if >=1.21.1 {*/EasingType.EASE_IN_OUT_SINE/*?} else {*/ /*Ease.INOUTSINE*//*?}*/),
				//? if >=1.21.1{
				RawAnimation.begin().thenPlay(animationContainer.getCurrentAnimation())
				 //?}
				//? if <1.21.1{
				/*new KeyframeAnimationPlayer(animationContainer.getCurrentAnimation())
				*///?}
				, false
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
		if (!getOffhandItem().isEmpty() && isUsingItem() && (isScoping() || getOffhandItem().getItem() instanceof InstrumentItem || getOffhandItem().getItem() instanceof BrushItem)) {
			this.disableBodyPartAnimationInAllContainers(getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
		}
	}
}
