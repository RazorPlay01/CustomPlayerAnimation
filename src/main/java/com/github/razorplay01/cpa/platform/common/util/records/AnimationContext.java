package com.github.razorplay01.cpa.platform.common.util.records;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.PlayerData;
import com.github.razorplay01.cpa.platform.common.util.interfaces.IAnimationControl;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimatedPlayer;
/*? if <=1.21.8 {*/
/*import net.minecraft.client.player.AbstractClientPlayer;
*//*?} else {*/
import net.minecraft.world.entity.Avatar;
/*?}*/

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
							   AnimationContainer specialAnimationContainer, /*? if <=1.21.8 {*//*AbstractClientPlayer*//*?} else {*/Avatar/*?}*/ player,
							   PlayerData playerData, IAnimationControl iAnimationControl, ICustomAnimatedPlayer iCustomAnimatedPlayer) {
}
