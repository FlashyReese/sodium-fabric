package me.jellysquid.mods.sodium.client.util.math;

import me.jellysquid.mods.sodium.client.util.Norm3b;
import net.minecraft.client.util.math.Matrix4f;

@SuppressWarnings("ConstantConditions")
public class MatrixUtil {

    public static Matrix4fExtended getExtendedMatrix(Matrix4f matrix) {
        return (Matrix4fExtended) (Object) matrix;
    }

    public static int transformPackedNormal(int norm) {

        float normX1 = Norm3b.unpackX(norm);
        float normY1 = Norm3b.unpackY(norm);
        float normZ1 = Norm3b.unpackZ(norm);

        return Norm3b.pack(normX1, normY1, normZ1);
    }
}
