package me.jellysquid.mods.sodium.mixin.core.frustum;

import me.jellysquid.mods.sodium.client.util.math.FrustumExtended;
import net.minecraft.client.render.FrustumWithOrigin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//:concern:
@Mixin(FrustumWithOrigin.class)
public abstract class MixinFrustum implements FrustumExtended {
    /*private float xF, yF, zF;

    private float nxX, nxY, nxZ, nxW;
    private float pxX, pxY, pxZ, pxW;
    private float nyX, nyY, nyZ, nyW;
    private float pyX, pyY, pyZ, pyW;
    private float nzX, nzY, nzZ, nzW;
    private float pzX, pzY, pzZ, pzW;

    @Inject(method = "method_23088", at = @At("HEAD"))
    private void prePositionUpdate(double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        this.xF = (float) cameraX;
        this.yF = (float) cameraY;
        this.zF = (float) cameraZ;
    }

    @Inject(method = "method_23091", at = @At("HEAD"))
    private void transform(Matrix4f mat, int x, int y, int z, int index, CallbackInfo ci) {
        Vector4f vec = new Vector4f((float) x, (float) y, (float) z, 1.0F);
        vec.method_22674(mat);
        vec.method_23218();

        switch (index) {
            case 0:
                this.nxX = vec.getX();
                this.nxY = vec.getY();
                this.nxZ = vec.getZ();
                this.nxW = ((Vector4fExtended)vec).getW();
                break;
            case 1:
                this.pxX = vec.getX();
                this.pxY = vec.getY();
                this.pxZ = vec.getZ();
                this.pxW = ((Vector4fExtended)vec).getW();
                break;
            case 2:
                this.nyX = vec.getX();
                this.nyY = vec.getY();
                this.nyZ = vec.getZ();
                this.nyW = ((Vector4fExtended)vec).getW();
                break;
            case 3:
                this.pyX = vec.getX();
                this.pyY = vec.getY();
                this.pyZ = vec.getZ();
                this.pyW = ((Vector4fExtended)vec).getW();
                break;
            case 4:
                this.nzX = vec.getX();
                this.nzY = vec.getY();
                this.nzZ = vec.getZ();
                this.nzW = ((Vector4fExtended)vec).getW();
                break;
            case 5:
                this.pzX = vec.getX();
                this.pzY = vec.getY();
                this.pzZ = vec.getZ();
                this.pzW = ((Vector4fExtended)vec).getW();
                break;
            default:
                throw new IllegalArgumentException("Invalid index");
        }
    }*/

    @Override
    public boolean fastAabbTest(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return this.intersects(minX, minY, minZ,
                maxX, maxY, maxZ);
    }

    /**
     * @author JellySquid
     * @reason Optimize away object allocations and for-loop
     */
    @Shadow
    public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        return true;
    }
}
