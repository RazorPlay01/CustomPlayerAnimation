package dev.razorplay.customplayeranimations.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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

    public static void renderFirstPersonMap(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ItemStack stack) {
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
            client.gameRenderer.getMapRenderer().render(matrices, vertexConsumers, mapid, mapState, false, light);
        }
    }

    private static void addVertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, float x, float y, float z, float u, float v, int lightmapUV) {
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(-1).setUv(u, v).setLight(lightmapUV);
    }
}