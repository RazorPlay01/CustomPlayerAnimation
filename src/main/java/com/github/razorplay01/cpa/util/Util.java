package com.github.razorplay01.cpa.util;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.config.ClientConfig;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.*;

//? if >= 1.21.2 {
import net.minecraft.world.item.component.Weapon;
		//?}
//? if <= 1.21.10 {
/*import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.vehicle.Boat;
*///?}else{
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.animal.equine.*;
//?}

//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.math.Vec3f;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomModelData;

//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
*///?}

import java.util.List;
import java.util.Optional;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class Util {
	public static final String RIGHT_PREFIX = "right_";
	public static final String LEFT_PREFIX = "left_";

	private Util() {
		//[]
	}

	//? if 1.21.1 {
	/*public static int getCustomModelDataId(ItemStack itemStack) {
		return Optional.of(itemStack.getComponentsPatch())
				.map(componentsPatch -> (Optional<CustomModelData>) componentsPatch.get(DataComponents.CUSTOM_MODEL_DATA))
				.flatMap(optional -> optional.map(CustomModelData::value))
				.orElse(0);
	}
	*///?}
	//? if >= 1.21.2 && < 26 {
	/*public static List<Float> getCustomModelDataId(ItemStack itemStack) {
		return Optional.of(itemStack.getComponentsPatch())
				.map(componentsPatch -> (Optional<CustomModelData>) componentsPatch.get(DataComponents.CUSTOM_MODEL_DATA))
				.flatMap(optional -> optional.map(CustomModelData::floats))
				.orElse(List.of(0.0f));
	}
	*///?}

	public static double getPlayerScale(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ net.minecraft.world.entity.Avatar player/*?}*/) {
		//? if >=1.21.1{
		return player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.SCALE).getBaseValue();
		//?}
		//? if <1.21.1{
		/*return 1.0;
		*///?}
	}

	/**
	 * Calcula el multiplicador de velocidad basado en la escala del jugador.
	 *
	 * @param player El jugador
	 *               CONFIG.getMainAnimations().moveAnimations.animationMoveSpeedScaleMultiplier Valor configurable por el usuario que define cuánto
	 *               afecta la escala a la velocidad de animación:
	 *               0.0 = la escala NO afecta la animación
	 *               0.5 = la escala afecta parcialmente
	 *               1.0 = la escala afecta completamente (por defecto)
	 *               2.0 = la escala afecta el doble de lo normal
	 */
	public static double getScaleSpeedMultiplier(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ net.minecraft.world.entity.Avatar player/*?}*/) {
		double scale = getPlayerScale(player);
		if (scale <= 0) scale = 1.0;

		// Interpolar entre 1.0 (sin efecto) y 1/scale (efecto completo)
		// influence = 0 → retorna 1.0
		// influence = 1 → retorna 1/scale
		return 1.0 + (1.0 / scale - 1.0) * CONFIG.getMainAnimations().moveAnimations.animationMoveSpeedScaleMultiplier;
	}

	public static float getAnimationSpeedMultiplier(double baseMultiplier, AnimationContext animationContext, ClientConfig.AnimationConfig animationConfig) {
		double baseSpeed = (baseMultiplier * animationContext.playerData().getMovementSpeed()
				* CONFIG.getMainAnimations().moveAnimations.getAnimationMoveSpeedMultiplier()
				* animationConfig.getSpeedMultiplier());
		double scaleMultiplier = Util.getScaleSpeedMultiplier(animationContext.player());
		return (float) (baseSpeed * scaleMultiplier);
	}

	public static boolean isBoat(Object vehicle) {
		return vehicle instanceof Boat;
	}

	public static boolean isHorse(Object vehicle) {
		return vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse || vehicle instanceof Donkey || vehicle instanceof Mule;
	}

	public static void configureAnimationContainer(ClientConfig.AnimationConfigInterface config, AnimationContainer animationContainer) {
		animationContainer.setAnimationSpeed(config.getSpeedMultiplier());
		animationContainer.setAnimationFadeTime(config.getFadeTime());
		animationContainer.setAnimationPriority(config.getPriority());
	}

	public static boolean containsAnyAnimation(AnimationContainer container, List<String> animations) {
		String currentAnimation = container.getCurrentAnimationId();
		if (animations == null || animations.isEmpty()) {
			return false;
		}
		return animations.stream().anyMatch(currentAnimation::contains);
	}

	public static void disableBothArms(AnimationContext context) {
		context.iAnimationControl().disableBodyPartAnimationInAllContainers(BodyParts.LEFT_ARM);
		context.iAnimationControl().disableBodyPartAnimationInAllContainers(BodyParts.RIGHT_ARM);
	}

	public static void addModifiersToContainer(AnimationContainer container) {
		for (AbstractModifier modifier : container.getAnimationModifiers().values()) {
			container.getAnimationController().addModifierLast(modifier);
			if (modifier instanceof MirrorModifier mirrorModifier) {
				/*? if >=1.21.1 {*/mirrorModifier.enabled = false/*?} else {*/ /*mirrorModifier.setEnabled(false)*//*?}*/;
			}
		}
	}

	public static boolean isSwingingSwordOrTools(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ net.minecraft.world.entity.Avatar player/*?}*/, AnimationContainer animationContainer) {
		return player.swinging &&
				(player.getMainHandItem().getItem() instanceof net.minecraft.world.item.ShovelItem ||
						//? if >=1.21.1{
						player.getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.TOOL) ||
						//?}
						//? if <1.21.1{
						/*player.getMainHandItem().getItem() instanceof net.minecraft.world.item.PickaxeItem ||
						*///?}
						player.getMainHandItem().getItem() instanceof net.minecraft.world.item.AxeItem ||

						//? if <= 1.21.1 {
						/*player.getMainHandItem().getItem() instanceof net.minecraft.world.item.SwordItem ||
						 *///?}
						//? if >= 1.21.2 {
						player.getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.WEAPON) ||
						//?}


						player.getMainHandItem().getItem() instanceof TridentItem) &&
				player.swingingArm.equals(MAIN_HAND) &&
				!animationContainer.getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
	}

	public static Optional<AdjustmentModifier.PartModifier> handleFirstPersonPass(String partName, float pitchRadians) {
		float xRot = 0;
		float offsetY = 0;
		float offsetZ = 0;

		if (partName.equals("body")) {
			if (pitchRadians < 0) {
				xRot -= pitchRadians;
				float offset = Math.abs((float) Math.sin(pitchRadians));
				offsetY += offset * 0.5f;
				offsetZ -= offset;
			}
		} else if (partName.equals("right_arm") || partName.equals("left_arm")) {
			xRot = pitchRadians;
		} else {
			return Optional.empty();
		}

		return Optional.of(new AdjustmentModifier.PartModifier(
				new Vec3f(xRot, 0, 0),
				new Vec3f(0, offsetY, offsetZ))
		);
	}

	public static Optional<AdjustmentModifier.PartModifier> handleThirdPersonPass(String partName, float pitchRadians) {
		float xRot = 0;

		switch (partName) {
			case "right_arm", "left_arm" -> xRot += pitchRadians * 0.5F;
			default -> {
				return Optional.empty();
			}
		}

		return Optional.of(new AdjustmentModifier.PartModifier(
				new Vec3f(xRot, 0, 0),
				new Vec3f(0, 0, 0))
		);
	}

	/*? if >= 1.21.11 {*/
	public static boolean isSpear(ItemStack itemStack) {
		return itemStack.get(DataComponents.KINETIC_WEAPON) != null && !(itemStack.getItem() instanceof TridentItem);
	}
	/*?}*/

	public static boolean isSword(ItemStack itemStack) {
		//? if <= 1.21.1 {
		/*return itemStack.getItem() instanceof net.minecraft.world.item.SwordItem;
		 *///?}
		//? if >= 1.21.2 {
		Weapon weapon = itemStack.get(DataComponents.WEAPON);
		if (weapon != null) {
			// Opcionalmente, verifica los modificadores de atributos para confirmar que es una espada
			net.minecraft.world.item.component.ItemAttributeModifiers attributes = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
			if (attributes != null) {
				for (net.minecraft.world.item.component.ItemAttributeModifiers.Entry entry : attributes.modifiers()) {
					if (entry.attribute().equals(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)) {
						return true; // Es probable que sea una espada
					}
				}
			}
			return true; // Si tiene el componente WEAPON, es una espada u otra arma cuerpo a cuerpo
		}
		return false;
		//?}
	}

	public static boolean isShovel(ItemStack itemStack) {
		//? if >=1.21.1{
		net.minecraft.world.item.component.Tool tool = itemStack.get(DataComponents.TOOL);
		if (tool != null) {
			// Usa un BlockState representativo que pertenezca a BlockTags.MINEABLE_WITH_SHOVEL
			net.minecraft.world.level.block.state.BlockState dirtState = net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState();
			return tool.isCorrectForDrops(dirtState);
		}
		return false;
		//?}
		//? if <1.21.1{
		/*return itemStack.getItem() instanceof net.minecraft.world.item.ShovelItem;
		*///?}
	}

	public static boolean isPickaxe(ItemStack itemStack) {
		//? if >=1.21.1{
		net.minecraft.world.item.component.Tool tool = itemStack.get(DataComponents.TOOL);
		if (tool != null) {
			// Usa un BlockState representativo que pertenezca a BlockTags.MINEABLE_WITH_PICKAXE
			net.minecraft.world.level.block.state.BlockState stoneState = net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
			return tool.isCorrectForDrops(stoneState);
		}
		return false;
		//?}
		//? if <1.21.1{
		/*return itemStack.getItem() instanceof net.minecraft.world.item.PickaxeItem;
		*///?}
	}

	public static boolean isAxe(ItemStack itemStack) {
		//? if >=1.21.1{
		net.minecraft.world.item.component.Tool tool = itemStack.get(DataComponents.TOOL);
		if (tool != null) {
			// Usa un BlockState representativo que pertenezca a BlockTags.MINEABLE_WITH_AXE
			net.minecraft.world.level.block.state.BlockState oakLogState = net.minecraft.world.level.block.Blocks.OAK_LOG.defaultBlockState();
			return tool.isCorrectForDrops(oakLogState);
		}
		return false;
		//?}
		//? if <1.21.1{
		/*return itemStack.getItem() instanceof net.minecraft.world.item.AxeItem;
		*///?}
	}
}
