/*
package com.github.razorplay01.cpa.platform.common.util;

import com.github.razorplay01.cpa.platform.common.util.enums.BodyParts;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Avatar;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.Set;

public class HeadLookAdjustmentModifier extends AdjustmentModifier {
	private static final Set<String> DEFAULT_BONES = Set.of(BodyParts.HEAD.getPartId());
	private final Avatar player;
	private final Set<String> affectedBones;

	private AnimationData currentData;

	public HeadLookAdjustmentModifier(Avatar player) {
		this(player, DEFAULT_BONES);
	}

	public HeadLookAdjustmentModifier(Avatar player, Set<String> affectedBones) {
		// No usamos PartModifier normal porque aquí no queremos sumar rotación,
		// sino recomponer la rotación final de la cabeza después de la animación.
		super((name, data) -> Optional.empty());
		this.player = player;
		this.affectedBones = affectedBones;
	}

	@Override
	public void tick(AnimationData state) {
		super.tick(state);
		this.currentData = state;
	}

	@Override
	public void setupAnim(AnimationData state) {
		super.setupAnim(state);
		this.currentData = state;
	}

	@Override
	public void get3DTransform(@NotNull PlayerAnimBone bone) {
		// Primero deja que la animación y los demás modifiers previos calculen la pose base
		super.get3DTransform(bone);

		if (!enabled || currentData == null) {
			return;
		}

		if (!affectedBones.contains(bone.getName())) {
			return;
		}

		float fade = getFadeIn() * getFadeOut(currentData.getPartialTick());
		if (fade <= 0.0F) {
			return;
		}

		float partialTick = currentData.getPartialTick();

		float pitch = Mth.lerp(partialTick, player.xRotO, player.getXRot()) * Mth.DEG_TO_RAD;

		float interpHeadYaw = Mth.rotLerp(partialTick, player.yHeadRotO, player.getYHeadRot());
		float interpBodyYaw = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
		float yaw = Mth.wrapDegrees(interpHeadYaw - interpBodyYaw) * Mth.DEG_TO_RAD;

		applyHeadLookComposition(bone, pitch, yaw, fade);
	}

	private void applyHeadLookComposition(PlayerAnimBone bone, float pitch, float yaw, float fade) {
		*/
/*
		 * OJO:
		 * Aquí asumo que bone.rotation expone x/y/z y add(dx,dy,dz),
		 * como sugiere tu AdjustmentModifier base.
		 *
		 * Si en tu fork la API usa getters distintos, adapta estas líneas.
		 *//*

		float animX = bone.rotation.x;
		float animY = bone.rotation.y;
		float animZ = bone.rotation.z;

		// Rotación actual de la animación en formato de quaternion
		Quaternionf animQ = fromModelEuler(animX, animY, animZ);

		// Rotación de mirada vanilla del jugador
		Quaternionf lookQ = fromModelEuler(pitch, yaw, 0.0F);

		*/
/*
		 * Orden importante:
		 * lookQ * animQ
		 *
		 * Esto aplica primero la orientación de mirada y luego el offset local
		 * de la animación, que normalmente es lo que se quiere para la cabeza.
		 *//*

		Quaternionf composedQ = new Quaternionf(lookQ).mul(animQ);

		// Fade real del modifier: mezclamos entre la rotación animada sola y la compuesta
		Quaternionf finalQ = new Quaternionf(animQ).slerp(composedQ, fade);

		Vector3f finalEuler = toModelEuler(finalQ);

		// Como sabemos que add(...) existe, aplicamos delta en vez de set(...)
		bone.rotation.add(
				finalEuler.x - animX,
				finalEuler.y - animY,
				finalEuler.z - animZ
		);
	}

	*/
/**
	 * Minecraft ModelPart aplica rotación en orden Z -> Y -> X,
	 * así que usamos rotationZYX(z, y, x).
	 *//*

	private static Quaternionf fromModelEuler(float xRot, float yRot, float zRot) {
		return new Quaternionf().rotationZYX(zRot, yRot, xRot);
	}

	*/
/**
	 * Convierte quaternion a Euler compatible con el orden del modelo: ZYX.
	 *//*

	private static Vector3f toModelEuler(Quaternionf q) {
		float x = q.x;
		float y = q.y;
		float z = q.z;
		float w = q.w;

		float m00 = 1.0F - 2.0F * (y * y + z * z);
		float m10 = 2.0F * (x * y + w * z);
		float m11 = 1.0F - 2.0F * (x * x + z * z);
		float m12 = 2.0F * (y * z - w * x);
		float m20 = 2.0F * (x * z - w * y);
		float m21 = 2.0F * (y * z + w * x);
		float m22 = 1.0F - 2.0F * (x * x + y * y);

		float yRot = (float) Math.asin(Mth.clamp(-m20, -1.0F, 1.0F));
		float cy = (float) Math.cos(yRot);

		float xRot;
		float zRot;

		if (Math.abs(cy) > 1.0E-5F) {
			xRot = (float) Math.atan2(m21, m22);
			zRot = (float) Math.atan2(m10, m00);
		} else {
			// Gimbal lock fallback
			xRot = (float) Math.atan2(-m12, m11);
			zRot = 0.0F;
		}

		return new Vector3f(xRot, yRot, zRot);
	}
}
*/
