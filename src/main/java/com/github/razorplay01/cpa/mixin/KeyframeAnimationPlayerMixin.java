package com.github.razorplay01.cpa.mixin;

//? if <1.21.1 {
/*import com.github.razorplay01.cpa.platform.common.util.interfaces.IKeyframeAnimationPlayerExtension;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@IfModLoaded(value = "player-animator")
@Mixin(value = KeyframeAnimationPlayer.class, remap = false)
public class KeyframeAnimationPlayerMixin implements IKeyframeAnimationPlayerExtension {
	@Unique
	private final Set<String> cpa$disabledBones = new HashSet<>();

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

	/^*
	 * Intercepta get3DTransform para deshabilitar huesos antes de que se procesen
	 ^/
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
		// Si el hueso está deshabilitado, retornar el valor vanilla directamente
		if (this.cpa$disabledBones.contains(modelName)) {
			cir.setReturnValue(value0);
		}
	}
}
*///?}
