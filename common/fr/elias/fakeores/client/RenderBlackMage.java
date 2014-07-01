package fr.elias.fakeores.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import fr.elias.fakeores.common.EntityBlackMage;

public class RenderBlackMage extends RenderLiving {

	public static final ResourceLocation texture1 = new ResourceLocation("fakeores:textures/entity/BlackMage1.png");
	public static final ResourceLocation texture2 = new ResourceLocation("fakeores:textures/entity/BlackMage2.png");
	public static final ResourceLocation texture3 = new ResourceLocation("fakeores:textures/entity/BlackMage3.png");
	
	public RenderBlackMage() 
	{
		super(new ModelBlackMage(), 0.5F);
	}

	
	protected ResourceLocation setEntityTexture(EntityBlackMage var1) 
	{
		return var1.getRNG().nextInt(2) == 0 ? texture3 : (var1.getRNG().nextInt(1) == 0 ? texture2 : texture1);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity var1) 
	{
		return setEntityTexture((EntityBlackMage) var1);
	}

}
