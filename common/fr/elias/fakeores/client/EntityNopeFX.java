package fr.elias.fakeores.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityNopeFX extends EntityFX 
{

    public EntityNopeFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float f, float red, float green, float blue)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        setSize(0.7F, 0.7F);
        particleRed = red * par1World.rand.nextFloat();
        particleBlue = blue * par1World.rand.nextFloat();
        particleGreen = green * par1World.rand.nextFloat();
        particleGravity = -0.12F;
        particleScale = 4.0F * par1World.rand.nextFloat();
        particleMaxAge = 20;
    }
	public int getFXLayer()
	{
		return 3;
	}
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("fakeores:textures/items/nope.png"));
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		tessellator.startDrawingQuads();
		tessellator.setBrightness(getBrightnessForRender(f));
		float scale = 0.1F*particleScale;
		float x = (float) (prevPosX + (prevPosX - posX) * f - interpPosX),
			  y = (float) (prevPosY + (prevPosY - posY) * f - interpPosY),
			  z = (float) (prevPosZ + (prevPosZ - posZ) * f - interpPosZ);
		float f14 = getBrightness(f);
		tessellator.setTranslation(0F, 0F, 0F);
		tessellator.setColorOpaque_F(f14 * particleRed, f14 * particleGreen, f14 * particleBlue);
		tessellator.addVertexWithUV(x - f1 * scale - f4 * scale, y - f2 * scale, z - f3 * scale - f5 * scale, 1, 1);
		tessellator.addVertexWithUV(x - f1 * scale + f4 * scale, y + f2 * scale, z - f3 * scale + f5 * scale, 1, 0);
		tessellator.addVertexWithUV(x + f1 * scale + f4 * scale, y + f2 * scale, z + f3 * scale + f5 * scale, 0, 0);
		tessellator.addVertexWithUV(x + f1 * scale - f4 * scale, y - f2 * scale, z + f3 * scale - f5 * scale, 0, 1);
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
        super.onUpdate();
    }
}