package com.github.razorplay01.cpa.platform.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;

import static com.mojang.math.Axis.YP;
import static com.mojang.math.Axis.ZP;

public class MapRenderer {

	private MapRenderer() {
		//[]
	}

	private static final RenderType MAP_BACKGROUND = RenderType
			.text(ResourceLocation.withDefaultNamespace("textures/map/map_background.png"));
	private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType
			.text(ResourceLocation.withDefaultNamespace("textures/map/map_background_checkerboard.png"));

	//? if <=1.21.8 {
	/*public static void renderFirstPersonMap(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack) {
		Minecraft client = Minecraft.getInstance();
		matrices.mulPose(YP.rotationDegrees(160.0f));
		matrices.mulPose(ZP.rotationDegrees(180.0f));
		matrices.scale(0.38f, 0.38f, 0.38f);

		matrices.translate(-0.1, -1.2, 0.0);
		matrices.scale(0.0098125f, 0.0098125f, 0.0098125f);
		MapId mapid = stack.get(DataComponents.MAP_ID);

		MapItemSavedData mapState = MapItem.getSavedData(stack, client.level);
		VertexConsumer vertexConsumer = vertexConsumers
				.getBuffer(mapState == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
		Matrix4f matrix4f = matrices.last().pose();
		addVertex(vertexConsumer, matrix4f, -7.0f, 135.0f, 0.0f, 0, 1, light);
		addVertex(vertexConsumer, matrix4f, 135.0f, 135.0f, 0.0f, 1, 1, light);
		addVertex(vertexConsumer, matrix4f, 135.0f, -7.0f, 0.0f, 1, 0, light);
		addVertex(vertexConsumer, matrix4f, -7.0f, -7.0f, 0.0f, 0, 0, light);
		// mirrored back site
		vertexConsumer = vertexConsumers.getBuffer(MAP_BACKGROUND);
		addVertex(vertexConsumer, matrix4f, -7.0f, -7.0f, 0.0f, 0, 0, light);
		addVertex(vertexConsumer, matrix4f, 135.0f, -7.0f, 0.0f, 1, 0, light);
		addVertex(vertexConsumer, matrix4f, 135.0f, 135.0f, 0.0f, 1, 1, light);
		addVertex(vertexConsumer, matrix4f, -7.0f, 135.0f, 0.0f, 0, 1, light);

		if (mapState != null) {
			MapRenderState mapRenderState = new MapRenderState();
			client.getMapRenderer().extractRenderState(mapid, mapState, mapRenderState);
			client.getMapRenderer().render(mapRenderState, matrices, vertexConsumers, false, light);
		}
	}

	private static void addVertex(VertexConsumer cons, Matrix4f matrix4f, float x, float y, float z, float u, float v, int lightmapUV) {
		cons.addVertex(matrix4f, x, y, z).setColor(-1).setUv(u, v).setLight(lightmapUV);
	}
	*///?} else {
	public static void renderFirstPersonMap(PoseStack poseStack,
											net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int light, ItemStack stack,
											boolean small, boolean leftHanded) {

		Minecraft client = Minecraft.getInstance();

		if (small) {
			poseStack.mulPose(YP.rotationDegrees(160.0f));
			poseStack.mulPose(ZP.rotationDegrees(180.0f));
			poseStack.scale(0.38f, 0.38f, 0.38f);

			poseStack.translate(-0.1, -1.2, 0.0);
			poseStack.scale(0.0098125f, 0.0098125f, 0.0098125f);
		} else { // The values are now much better than they were before, but it could still be perfected.
			if (leftHanded) {
				poseStack.mulPose(YP.rotationDegrees(154.5f));
				poseStack.mulPose(ZP.rotationDegrees(166.5f));
				poseStack.scale(0.38f, 0.38f, 0.38f);
				// positive x = move right
				poseStack.translate(+0.585, -1.225, +0.15);
			} else {
				poseStack.mulPose(YP.rotationDegrees(155.0f));
				poseStack.mulPose(ZP.rotationDegrees(213.5f));
				poseStack.scale(0.38f, 0.38f, 0.38f);
				// negative x = move left
				poseStack.translate(-0.955, -1.8, 0.0);
			}

			poseStack.scale(0.0138125f, 0.0138125f, 0.0138125f);
		}

		MapId mapid = stack.get(DataComponents.MAP_ID);
		MapItemSavedData mapState = MapItem.getSavedData(stack, client.level);
		RenderType renderType = mapState == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD;
		submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
			vertexConsumer.addVertex(pose, -7.0F, 135.0F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setLight(light);
			vertexConsumer.addVertex(pose, 135.0F, 135.0F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setLight(light);
			vertexConsumer.addVertex(pose, 135.0F, -7.0F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setLight(light);
			vertexConsumer.addVertex(pose, -7.0F, -7.0F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setLight(light);
		});
		submitNodeCollector.submitCustomGeometry(poseStack, MAP_BACKGROUND, (pose, vertexConsumer) -> {
			vertexConsumer.addVertex(pose, -7.0f, -7.0f, 0.0f).setColor(-1).setUv(0, 0).setLight(light);
			vertexConsumer.addVertex(pose, 135.0f, -7.0f, 0.0f).setColor(-1).setUv(1, 0).setLight(light);
			vertexConsumer.addVertex(pose, 135.0f, 135.0f, 0.0f).setColor(-1).setUv(1, 1).setLight(light);
			vertexConsumer.addVertex(pose, -7.0f, 135.0f, 0.0f).setColor(-1).setUv(0, 1).setLight(light);
		});

		if (mapState != null) {
			MapRenderState mapRenderState = new MapRenderState();
			client.getMapRenderer().extractRenderState(mapid, mapState, mapRenderState);
			client.getMapRenderer().render(mapRenderState, poseStack, submitNodeCollector, false, light);
		}
	}
	//?}
}
