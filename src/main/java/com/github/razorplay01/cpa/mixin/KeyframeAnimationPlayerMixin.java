package com.github.razorplay01.cpa.mixin;

//? if <1.21.1 {

/*import com.github.razorplay01.cpa.ModTemplate;
import com.github.razorplay01.cpa.util.interfaces.IKeyframeAnimationPlayerExtension;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(value = KeyframeAnimationPlayer.class, remap = false)
public class KeyframeAnimationPlayerMixin implements IKeyframeAnimationPlayerExtension {
	@Unique
	private final Set<String> cpa$disabledBones = new HashSet<>();

	@Unique
	private final Map<String, Set<String>> cpa$disabledChannels = new HashMap<>();

	@Unique
	private static final String CHANNEL_ROT_X = "rotX";
	@Unique
	private static final String CHANNEL_ROT_Y = "rotY";
	@Unique
	private static final String CHANNEL_ROT_Z = "rotZ";

	@Override
	public void cpa$setDisabledBones(Set<String> boneIds) {
		this.cpa$disabledBones.clear();
		this.cpa$disabledBones.addAll(boneIds);
	}

	@Override
	public void cpa$clearDisabledBones() {
		this.cpa$disabledBones.clear();
	}

	@Override
	public Set<String> cpa$getDisabledBones() {
		return new HashSet<>(this.cpa$disabledBones);
	}

	@Override
	public boolean cpa$isBoneDisabled(String boneName) {
		return this.cpa$disabledBones.contains(boneName);
	}

	@Override
	public void cpa$setDisabledBoneChannels(String boneId, Set<String> channels) {
		if (channels == null || channels.isEmpty()) {
			this.cpa$disabledChannels.remove(boneId);
		} else {
			this.cpa$disabledChannels.put(boneId, new HashSet<>(channels));
		}
	}

	@Override
	public void cpa$clearDisabledBoneChannels(String boneId) {
		this.cpa$disabledChannels.remove(boneId);
	}

	@Override
	public void cpa$clearAllDisabledChannels() {
		this.cpa$disabledChannels.clear();
	}

	@Override
	public boolean cpa$isBoneChannelDisabled(String boneId, String channel) {
		Set<String> channels = this.cpa$disabledChannels.get(boneId);
		return channels != null && channels.contains(channel);
	}

	@Inject(
			method = "get3DTransform",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	private void cpa$onGet3DTransform(
			@NotNull String modelName,
			@NotNull TransformType type,
			float tickDelta,
			@NotNull Vec3f value0,
			CallbackInfoReturnable<Vec3f> cir
	) {
		if (this.cpa$disabledBones.contains(modelName)) {
			cir.setReturnValue(value0);
			return;
		}
		Set<String> disabledChannelsForBone = this.cpa$disabledChannels.get(modelName);
		if (disabledChannelsForBone == null || disabledChannelsForBone.isEmpty()) {
			return;
		}
		if (type == TransformType.ROTATION) {
			boolean disableX = disabledChannelsForBone.contains(CHANNEL_ROT_X);
			boolean disableY = disabledChannelsForBone.contains(CHANNEL_ROT_Y);
			boolean disableZ = disabledChannelsForBone.contains(CHANNEL_ROT_Z);

			if (disableX && disableY && disableZ) {
				cir.setReturnValue(value0);
				return;
			}

			if (disableX || disableY || disableZ) {
				KeyframeAnimationPlayer self = (KeyframeAnimationPlayer) (Object) this;
				Vec3f animatedValue = cpa$getOriginalTransform(self, modelName, type, tickDelta, value0);

				float x = disableX ? value0.getX() : animatedValue.getX();
				float y = disableY ? value0.getY() : animatedValue.getY();
				float z = disableZ ? value0.getZ() : animatedValue.getZ();

				cir.setReturnValue(new Vec3f(x, y, z));
			}
		}
	}

	@Unique
	private Vec3f cpa$getOriginalTransform(
			KeyframeAnimationPlayer player,
			String modelName,
			TransformType type,
			float tickDelta,
			Vec3f value0
	) {
		Set<String> savedChannels = this.cpa$disabledChannels.remove(modelName);

		try {
			return player.get3DTransform(modelName, type, tickDelta, value0);
		} finally {
			if (savedChannels != null) {
				this.cpa$disabledChannels.put(modelName, savedChannels);
			}
		}
	}
}
*///?}
