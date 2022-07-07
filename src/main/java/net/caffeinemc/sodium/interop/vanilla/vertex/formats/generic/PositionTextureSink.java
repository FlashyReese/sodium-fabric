package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic;

import net.caffeinemc.sodium.interop.vanilla.math.matrix.Matrix4fExtended;
import net.caffeinemc.sodium.interop.vanilla.math.matrix.MatrixUtil;
import net.caffeinemc.sodium.render.vertex.VertexSink;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Matrix4f;

public interface PositionTextureSink extends VertexSink {
    VertexFormat VERTEX_FORMAT = VertexFormats.POSITION_TEXTURE;

    default void writeVertex(Matrix4f matrix, float x, float y, float z, float u, float v) {
        Matrix4fExtended modelMatrix = MatrixUtil.getExtendedMatrix(matrix);

        float x2 = modelMatrix.transformVecX(x, y, z);
        float y2 = modelMatrix.transformVecY(x, y, z);
        float z2 = modelMatrix.transformVecZ(x, y, z);

        this.writeVertex(x2, y2, z2, u, v);
    }

    void writeVertex(float x, float y, float z, float u, float v);
}
