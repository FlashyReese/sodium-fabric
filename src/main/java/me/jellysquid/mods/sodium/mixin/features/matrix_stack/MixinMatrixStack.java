package me.jellysquid.mods.sodium.mixin.features.matrix_stack;

import net.minecraft.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MatrixStack.class)
public class MixinMatrixStack {
    //fixme:
    /*@Shadow
    @Final
    private Deque<MatrixStack.Entry> stack;

    *//**
     * @reason Use our faster specialized function
     * @author JellySquid
     *//*
    @Overwrite
    public void translate(double x, double y, double z) {
        MatrixStack.Entry entry = this.stack.getLast();

        Matrix4fExtended mat = MatrixUtil.getExtendedMatrix(entry.method_23761());
        mat.translate((float) x, (float) y, (float) z);
    }

    *//**
     * @reason Use our faster specialized function
     * @author JellySquid
     *//*
    @Overwrite
    public void multiply(Quaternion q) {
        MatrixStack.Entry entry = this.stack.getLast();

        Matrix4fExtended mat4 = MatrixUtil.getExtendedMatrix(entry.method_23761());
        mat4.rotate(q);

        Matrix3fExtended mat3 = MatrixUtil.getExtendedMatrix(entry.method_23762());
        mat3.rotate(q);
    }*/
}
