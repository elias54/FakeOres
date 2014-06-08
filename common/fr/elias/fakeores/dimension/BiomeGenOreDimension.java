package fr.elias.fakeores.dimension;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import fr.elias.fakeores.common.EntityFlyer;
import fr.elias.fakeores.common.EntityPlayerHunter;
import fr.elias.fakeores.common.EntityRegenEgg;
import fr.elias.fakeores.common.EntitySchaza;
import fr.elias.fakeores.common.EntitySwimmerAnimal;
import fr.elias.fakeores.common.FakeOres;

public class BiomeGenOreDimension extends BiomeGenBase
{

	public BiomeGenOreDimension(int par1)
	{
		super(par1);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityPlayerHunter.class, 12, 1, 2));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityFlyer.class, 12, 2, 5));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySchaza.class, 12, 2, 8));
		this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityRegenEgg.class, 5, 1, 1));
		this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySwimmerAnimal.class, 12, 8, 16));
		this.topBlock = FakeOres.fd_grass;
		this.fillerBlock = FakeOres.fd_dirt;
		this.setBiomeName("FakeOresBiome");
		this.rainfall = 0.0F;
		this.temperature = 1F;
	}
    public void generateTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_, int p_150573_6_, double p_150573_7_)
    {
        this.generateBiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }
    public final void generateBiomeTerrain(World p_150560_1_, Random p_150560_2_, Block[] p_150560_3_, byte[] p_150560_4_, int p_150560_5_, int p_150560_6_, double p_150560_7_)
    {
    	super.genBiomeTerrain(p_150560_1_, p_150560_2_, p_150560_3_, p_150560_4_, p_150560_5_, p_150560_6_, p_150560_7_);
        boolean flag = true;
        Block block = this.topBlock;
        byte b0 = (byte)(this.field_150604_aj & 255);
        Block block1 = this.fillerBlock;
        int k = -1;
        int l = (int)(p_150560_7_ / 3.0D + 3.0D + p_150560_2_.nextDouble() * 0.25D);
        int i1 = p_150560_5_ & 15;
        int j1 = p_150560_6_ & 15;
        int k1 = p_150560_3_.length / 256;

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (j1 * 16 + i1) * k1 + l1;

            if (l1 <= 0 + p_150560_2_.nextInt(5))
            {
                p_150560_3_[i2] = Blocks.bedrock;
            }
            else
            {
                Block block2 = p_150560_3_[i2];

                if (block2 != null && block2.getMaterial() != Material.air)
                {
                    if (block2 == FakeOres.fd_stone)
                    {
                        if (k == -1)
                        {
                            if (l <= 0)
                            {
                                block = null;
                                b0 = 0;
                                block1 = FakeOres.fd_stone;
                            }
                            else if (l1 >= 59 && l1 <= 64)
                            {
                                block = this.topBlock;
                                b0 = (byte)(this.field_150604_aj & 255);
                                block1 = this.fillerBlock;
                            }

                            if (l1 < 63 && (block == null || block.getMaterial() == Material.air))
                            {
                                if (this.getFloatTemperature(p_150560_5_, l1, p_150560_6_) < 0.15F)
                                {
                                    block = Blocks.ice;
                                    b0 = 0;
                                }
                                else
                                {
                                    block = Blocks.water;
                                    b0 = 0;
                                }
                            }

                            k = l;

                            if (l1 >= 62)
                            {
                                p_150560_3_[i2] = block;
                                p_150560_4_[i2] = b0;
                            }
                            else if (l1 < 56 - l)
                            {
                                block = null;
                                block1 = FakeOres.fd_stone;
                                p_150560_3_[i2] = FakeOres.fd_stone;
                            }
                            else
                            {
                                p_150560_3_[i2] = block1;
                            }
                        }
                        else if (k > 0)
                        {
                            --k;
                            p_150560_3_[i2] = block1;

                            if (k == 0 && block1 == Blocks.sand)
                            {
                                k = p_150560_2_.nextInt(4) + Math.max(0, l1 - 63);
                                block1 = Blocks.sandstone;
                            }
                        }
                    }
                }
                else
                {
                    k = -1;
                }
            }
        }
    }
}
