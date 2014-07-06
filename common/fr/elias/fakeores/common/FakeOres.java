package fr.elias.fakeores.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.elias.fakeores.client.TickClientHandlerEvent;
import fr.elias.fakeores.dimension.BiomeGenOreDimension;
import fr.elias.fakeores.dimension.BlockBase;
import fr.elias.fakeores.dimension.FD_BlockGrass;
import fr.elias.fakeores.dimension.FD_BlockPortal;
import fr.elias.fakeores.dimension.FD_WorldProvider;
import fr.elias.fakeores.dimension.SecondBiomeGenOreDimension;

@Mod(modid = "fakeores", name = "Fake Ores", version = "@VERSION@") // build.gradle
public class FakeOres
{
	@SidedProxy(clientSide = "fr.elias.fakeores.client.ClientProxy", serverSide = "fr.elias.fakeores.common.StaticProxy")
	public static StaticProxy proxy;

	@Instance("fakeores")
	public static FakeOres instance;

	public static Fluid strange = new Fluid("strange").setLuminosity(25).setViscosity(650).setUnlocalizedName("strangeLiquid");
	
	public static DamageSource antiorestone;
	public static final CreativeTabs fakeOresTab = new CreativeTabs("fakeorestab")
	{
	    public Item getTabIconItem()
	    {
	        return FakeOres.antiOresBlade;
	    }
	};
	
	protected static final BiomeGenBase.Height height_fd_Biome = new BiomeGenBase.Height(0.0F, 0.5F);
	
	public static Item texture_CustomSmokeFX_particle,
					   texture_BloodFX_particle,
					   texture_NopeFX_particle;
	
	public static FD_BlockGrass fd_grass;
	
	public static Block fakeOreVanilla,
	 					fakeCopper, 
	 					fakeTin, 
						fakeUranium,
						fd_dirt, 
						fd_stone, 
						fd_cobblestone,
						fd_redobsidian,
						fd_portal,
						fd_blurk_ore,
						fd_blurk_block,
						fd_daren_ore,
						fd_daren_block,
						fd_purlight_ore,
						fd_purlight_block,
						fd_rainbow_ore,
						fd_rainbow_block,
						antiOreStone,
						fd_second_grass,
						fd_strangeliquid;
	public static Item antiOresBlade, 
					   fragment_part1, 
					   fragment_part2,
					   fragment_part3,
					   fragment_part4,
					   fragment_boss, 
					   oreBoss_Star,
					   fakeOres_finder,
					   
					   fd_blurk_ingot,
					   fd_daren_ingot,
					   fd_purlight_ingot,
					   fd_rainbow_ingot,
					   fd_passcard_schaza,
						
					   letter_n,
					   letter_o,
					   letter_p,
					   letter_e,
					   nope_word,
					   nopeGrenade,
	
					   fd_stone_pickaxe,
					   fd_stone_axe,
					   fd_stone_sword,
					   fd_stone_hoe,
					   fd_stone_spade,
					   fd_strangeliquid_bucket;

	public static int dimID;
	public int mainBiomeID,
			   secondBiomeID;
	
	public static BiomeGenBase mainODBiome, secondODBiome;
	
	public int mob_diamondOreID,
			   mob_emeraldOreID,
			   mob_goldOreID,
			   mob_ironOreID,
			   mob_coalOreID,
			   mob_redstoneOreID,
			   mob_quartzOreID,
			   mob_lapisOreID,
			   /** IC2 ID ORES **/
			   mob_copperOreID,
			   mob_tinOreID,
			   mob_uraniumOreID,
			   /** ORES BOSS UTILS ID **/
			   mob_OreBOSSID,
			   mob_BOSS_TELEPORTER_ID,
			   /** OTHER MOBS ID **/
			   mob_PLAYER_HUNTER_ID,
			   mob_FLYER_ID,
			   mob_FLYER_SHIELD_ID,
			   mob_SCHAZA_ID,
			   mob_BLACK_MAGE_ID,
			   mob_MAGE_SPELL_ID,
			   mob_REGEN_EGG_ID,
			   mob_MUTANT_MONSTER_ID,
			   mob_NOPE_GUY_ID,
			   mob_NOPE_GRENADE_ID,
			   mob_DANGEROUS_PLANT_ID,
			   mob_STALKER_ID;
	public static boolean spawn_PlayerHunter,
				   		  spawn_Flyer,
				   		  spawn_Schaza,
				   		  spawn_RegenEgg,
				   		  spawn_BlackMage,
				   		  spawn_MutantMonster,
				   		  spawn_NopeGuy,
				   		  spawn_DangerousPlant,
				   		  spawn_Stalker;
	public static boolean enableFakeOres;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		try {
			config.load();
			dimID = config.get("World", "Dimension ID", 4).getInt();
			mainBiomeID = config.get("World", "Main Biome ID (MAX : 128)", 75).getInt();
			secondBiomeID = config.get("World", "Second Biome ID (MAX : 128)", 76).getInt();
			
			mob_diamondOreID =  config.get("Entity", "Fake Diamond Ore ID", 360).getInt();
			mob_emeraldOreID =  config.get("Entity", "Fake Emerald Ore ID", 361).getInt();
			mob_goldOreID =  config.get("Entity", "Fake Gold Ore ID", 362).getInt();
			mob_ironOreID =  config.get("Entity", "Fake Iron Ore ID", 363).getInt();
			mob_coalOreID =  config.get("Entity", "Fake Coal Ore ID", 364).getInt();
			mob_redstoneOreID =  config.get("Entity", "Fake Redstone Ore ID", 365).getInt();
			mob_quartzOreID =  config.get("Entity", "Fake Nether Quartz Ore ID", 366).getInt();
			mob_lapisOreID =  config.get("Entity", "Fake Lapis Lazuli Ore ID", 367).getInt();

			mob_copperOreID =  config.get("Entity", "Fake Copper Ore ID", 368).getInt();
			mob_tinOreID =  config.get("Entity", "Fake Tin Ore ID", 369).getInt();
			mob_uraniumOreID =  config.get("Entity", "Fake Uranium Ore ID", 370).getInt();

			mob_OreBOSSID =  config.get("Entity", "Ore BOSS Mob ID", 371).getInt();
			mob_BOSS_TELEPORTER_ID =  config.get("Entity", "Entity Teleporter Boss ID", 372).getInt();

			mob_PLAYER_HUNTER_ID =  config.get("Entity", "Player Hunter Mob ID", 373).getInt();
			mob_FLYER_ID = config.get("Entity", "Flyer ID", 374).getInt();
			mob_FLYER_SHIELD_ID = config.get("Entity", "Flyer Shield ID", 375).getInt();
			mob_SCHAZA_ID = config.get("Entity", "Schaza ID", 376).getInt();
			mob_BLACK_MAGE_ID = config.get("Entity", "Black Mage ID", 377).getInt();
			mob_MAGE_SPELL_ID = config.get("Entity", "Mage Spell ID", 378).getInt();
			mob_REGEN_EGG_ID = config.get("Entity", "Regeneration Egg ID", 379).getInt();
			mob_MUTANT_MONSTER_ID = config.get("Entity", "Mutant Monster ID", 380).getInt();
			mob_NOPE_GUY_ID = config.get("Entity", "Nope Guy ID", 381).getInt();
			mob_NOPE_GRENADE_ID = config.get("Entity", "Nope Grenade ID", 382).getInt();
			mob_DANGEROUS_PLANT_ID = config.get("Entity", "Dangerous Plant ID", 383).getInt();
			mob_STALKER_ID = config.get("Entity", "Stalker ID", 384).getInt();
			
			spawn_PlayerHunter = config.get("Spawn", "Spawn Player Hunter", true).getBoolean(true);
			spawn_Flyer = config.get("Spawn", "Spawn Flyer", true).getBoolean(true);
			spawn_Schaza = config.get("Spawn", "Spawn Schaza", true).getBoolean(true);
			spawn_RegenEgg = config.get("Spawn", "Spawn Regeneration Egg", true).getBoolean(true);
			spawn_BlackMage = config.get("Spawn", "Spawn Black Mage", true).getBoolean(true);
			spawn_MutantMonster = config.get("Spawn", "Spawn Mutant Monster", true).getBoolean(true);
			spawn_NopeGuy = config.get("Spawn", "Spawn Nope Guy", true).getBoolean(true);
			spawn_DangerousPlant = config.get("Spawn", "Spawn Dangerous Plant", true).getBoolean(true);
			spawn_Stalker = config.get("Spawn", "Spawn Stalker", true).getBoolean(true);
			
			enableFakeOres = config.get("Spawn", "Enable Fake Ores", true).getBoolean(true);
			
			config.save();
		} finally {
			if(config.hasChanged())
			{
				config.save();
			}
		}
		if(Loader.isModLoaded("IC2"))
		{
			System.out.print("[FAKE ORES] INDUSTRIAL CRAFT� DETECTED ! LOADING FAKE IC2 ORES");
		}
		FluidRegistry.registerFluid(strange);

		/** INIT PART [ITEMS] **/
		antiOresBlade = new ItemSword(Item.ToolMaterial.WOOD).setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("antiOresBlade").setTextureName("fakeores:antiOresBlade");
		fragment_part1 = new Item().setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("bossframe_part1").setTextureName("fakeores:frame_part1");
		fragment_part2 = new Item().setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("bossframe_part2").setTextureName("fakeores:frame_part2");
		fragment_part3 = new Item().setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("bossframe_part3").setTextureName("fakeores:frame_part3");
		fragment_part4 = new Item().setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("bossframe_part4").setTextureName("fakeores:frame_part4");
		fragment_boss = new ItemFragmentBoss().setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("boss_frame").setTextureName("fakeores:boss_frame").setFull3D();
		oreBoss_Star = new ItemBossStar().setCreativeTab(FakeOres.fakeOresTab).setUnlocalizedName("oreBoss_Star").setTextureName("fakeores:oreBoss_Star");
		fakeOres_finder = new ItemFakeOresFinder().setCreativeTab(fakeOresTab).setUnlocalizedName("fakeOres_finder").setTextureName("fakeores:fakeOres_finder");
		fd_blurk_ingot = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("fd_blurk_ingot").setTextureName("fakeores:blurk_ingot");
		fd_daren_ingot = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("fd_daren_ingot").setTextureName("fakeores:daren_ingot");
		fd_purlight_ingot = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("fd_purlight_ingot").setTextureName("fakeores:purlight_ingot");
		fd_rainbow_ingot = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("fd_rainbow_ingot").setTextureName("fakeores:rainbow_ingot");
		fd_passcard_schaza = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("fd_passcard_schaza").setTextureName("fakeores:passcard_schaza");
		letter_n = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("letter_n").setTextureName("fakeores:n");
		letter_o = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("letter_o").setTextureName("fakeores:o");
		letter_p = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("letter_p").setTextureName("fakeores:p");
		letter_e = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("letter_e").setTextureName("fakeores:e");
		nope_word = new Item().setCreativeTab(fakeOresTab).setUnlocalizedName("nope_word").setTextureName("fakeores:nope_word");
		nopeGrenade = new ItemNopeGrenade().setCreativeTab(fakeOresTab).setUnlocalizedName("nopeGrenade").setTextureName("fakeores:nopeGrenade");
		fd_stone_pickaxe = new CUSTOM_ItemPickaxe(Item.ToolMaterial.STONE).setUnlocalizedName("fd_stone_pickaxe").setTextureName("fakeores:fd_stone_pickaxe");
		fd_stone_axe = new CUSTOM_ItemAxe(Item.ToolMaterial.STONE).setUnlocalizedName("fd_stone_axe").setTextureName("fakeores:fd_stone_axe");
		fd_stone_sword = new ItemSword(Item.ToolMaterial.STONE).setCreativeTab(fakeOresTab).setUnlocalizedName("fd_stone_sword").setTextureName("fakeores:fd_stone_sword");
		fd_stone_hoe = new ItemHoe(Item.ToolMaterial.STONE).setCreativeTab(fakeOresTab).setUnlocalizedName("fd_stone_hoe").setTextureName("fakeores:fd_stone_hoe");
		fd_stone_spade = new ItemSpade(Item.ToolMaterial.STONE).setCreativeTab(fakeOresTab).setUnlocalizedName("fd_stone_spade").setTextureName("fakeores:fd_stone_spade");
		/** INIT PART [BLOCKS] **/
		fakeOreVanilla = new BlockFakeOresVanilla().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockName("fakeOre");
		fd_grass = (FD_BlockGrass) new FD_BlockGrass("fakeores:oredimension_grass_top", "fakeores:oredimension_grass_side", this.fd_dirt, this.fd_grass).setHardness(0.6F).setStepSound(Block.soundTypeGrass).setBlockName("fd_grass");
		fd_second_grass = new FD_BlockGrass("fakeores:oredimension_second_grass_top", "fakeores:oredimension_second_grass_side", this.fd_dirt, this.fd_second_grass).setHardness(0.6F).setStepSound(Block.soundTypeGrass).setBlockName("fd_second_grass");
		fd_dirt = new BlockBase(Material.ground).setHardness(0.5F).setStepSound(Block.soundTypeGravel).setBlockName("fd_dirt").setBlockTextureName("fakeores:oredimension_dirt");
		fd_stone =  new BlockBase(Material.rock).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setBlockName("fd_stone").setBlockTextureName("fakeores:oredimension_stone");
		fd_cobblestone = new BlockBase(Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setBlockName("fd_cobblestone").setBlockTextureName("fakeores:oredimension_cobblestone");
		fd_redobsidian = new BlockBase(Material.rock).setHardness(3.0F).setResistance(35F).setBlockName("fd_redobsidian").setBlockTextureName("fakeores:oredimension_redObsidian");
		fd_portal = new FD_BlockPortal().setBlockUnbreakable().setBlockName("fd_portal").setBlockTextureName("fakeores:oredimension_portal");
		fd_blurk_ore = new BlockFakeOresDimension("fd_blurk_ore", "blurk_ore");
		fd_daren_ore = new BlockFakeOresDimension("fd_daren_ore", "daren_ore");
		fd_purlight_ore = new BlockFakeOresDimension("fd_purlight_ore", "purlight_ore");
		fd_rainbow_ore = new BlockFakeOresDimension("fd_rainbow_ore", "rainbow_ore");
		fd_blurk_block = new BlockBase(Material.iron).setHardness(3.5F).setBlockName("fd_blurk_block").setBlockTextureName("fakeores:blurk_block");
		fd_daren_block = new BlockBase(Material.iron).setHardness(3.5F).setBlockName("fd_daren_block").setBlockTextureName("fakeores:daren_block");
		fd_purlight_block = new BlockBase(Material.iron).setHardness(3.5F).setBlockName("fd_purlight_block").setBlockTextureName("fakeores:purlight_block");
		fd_rainbow_block = new BlockBase(Material.iron).setHardness(3.5F).setBlockName("fd_rainbow_block").setBlockTextureName("fakeores:rainbow_block");		
		antiOreStone = new BlockAntiOreStone().setHardness(4.0F).setResistance(6F).setStepSound(Block.soundTypeStone).setBlockTextureName("fakeores:antiorestone").setBlockName("antiOreStone");
		fd_strangeliquid = new BlockStrangeLiquid(strange, Material.water).setBlockName("fd_strangeliquid");
		fd_strangeliquid_bucket = new ItemStrangeLiquidBucket(fd_strangeliquid).setUnlocalizedName("fd_strangeliquid_bucket").setCreativeTab(fakeOresTab).setTextureName("fakeores:strangeLiquidBucket").setContainerItem(Items.bucket);
		if(Loader.isModLoaded("IC2"))
		{
			fakeCopper = new BlockFakeOres().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockTextureName("ic2:blockOreCopper").setBlockName("fakeCopper");
			fakeTin = new BlockFakeOres().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockTextureName("ic2:blockOreTin").setBlockName("fakeTin");
			fakeUranium = new BlockFakeOres().setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone).setBlockTextureName("ic2:blockOreUran").setBlockName("fakeUranium");
		}
		/** REGISTRY PART **/
		// REGISTRY [BLOCKS]
		GameRegistry.registerBlock(fakeOreVanilla, ItemBlockFakeOreVanilla.class, "fakeores");
		GameRegistry.registerBlock(fd_grass, "fd_grass");
		GameRegistry.registerBlock(fd_dirt, "fd_dirt");
		GameRegistry.registerBlock(fd_stone, "fd_stone");
		GameRegistry.registerBlock(fd_cobblestone, "fd_cobblestone");
		GameRegistry.registerBlock(fd_redobsidian, "fd_redobsidian");
		GameRegistry.registerBlock(fd_portal, "fd_portal");
		GameRegistry.registerBlock(fd_blurk_ore, "fd_blurk_ore");
		GameRegistry.registerBlock(fd_daren_ore, "fd_daren_ore");
		GameRegistry.registerBlock(fd_purlight_ore, "fd_purlight_ore");
		GameRegistry.registerBlock(fd_rainbow_ore, "fd_rainbow_ore");
		GameRegistry.registerBlock(fd_blurk_block, "fd_blurk_block");
		GameRegistry.registerBlock(fd_daren_block, "fd_daren_block");
		GameRegistry.registerBlock(fd_purlight_block, "fd_purlight_block");
		GameRegistry.registerBlock(fd_rainbow_block, "fd_rainbow_block");
		GameRegistry.registerBlock(antiOreStone, "antiOreStone");
		GameRegistry.registerBlock(fd_second_grass, "fd_second_grass");
		GameRegistry.registerBlock(fd_strangeliquid, "fd_strangeliquid");
		if(Loader.isModLoaded("IC2"))
		{
			GameRegistry.registerBlock(fakeCopper, "fakeCopper");
			GameRegistry.registerBlock(fakeTin, "fakeTin");
			GameRegistry.registerBlock(fakeUranium, "fakeUranium");
		}
		// REGISTRY [ITEMS]
		GameRegistry.registerItem(antiOresBlade, "antiOresBlade", "fakeores");
		GameRegistry.registerItem(fragment_part1, "bossframe_part1", "fakeores");
		GameRegistry.registerItem(fragment_part2, "bossframe_part2", "fakeores");
		GameRegistry.registerItem(fragment_part3, "bossframe_part3", "fakeores");
		GameRegistry.registerItem(fragment_part4, "bossframe_part4", "fakeores");
		GameRegistry.registerItem(fragment_boss, "boss_frame", "fakeores");
		GameRegistry.registerItem(oreBoss_Star, "oreBoss_Star", "fakeores");
		GameRegistry.registerItem(fakeOres_finder, "fakeOres_finder", "fakeores");
		GameRegistry.registerItem(fd_blurk_ingot, "fd_blurk_ingot", "fakeores");
		GameRegistry.registerItem(fd_daren_ingot, "fd_daren_ingot", "fakeores");
		GameRegistry.registerItem(fd_purlight_ingot, "fd_purlight_ingot", "fakeores");
		GameRegistry.registerItem(fd_rainbow_ingot, "fd_rainbow_ingot", "fakeores");
		GameRegistry.registerItem(fd_passcard_schaza, "fd_passcard_schaza", "fakeores");
		GameRegistry.registerItem(letter_n, "letter_n", "fakeores");
		GameRegistry.registerItem(letter_o, "letter_o", "fakeores");
		GameRegistry.registerItem(letter_p, "letter_p", "fakeores");
		GameRegistry.registerItem(letter_e, "letter_e", "fakeores");
		GameRegistry.registerItem(nope_word, "nope_word", "fakeores");
		GameRegistry.registerItem(nopeGrenade, "nopeGrenade", "fakeores");
		GameRegistry.registerItem(fd_strangeliquid_bucket, "fd_strangeliquid_bucket", "fakeores");
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("strange", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(fd_strangeliquid_bucket), new ItemStack(Items.bucket));
		GameRegistry.registerItem(fd_stone_pickaxe, "fd_stone_pickaxe", "fakeores");
		GameRegistry.registerItem(fd_stone_axe, "fd_stone_axe", "fakeores");
		GameRegistry.registerItem(fd_stone_sword, "fd_stone_sword", "fakeores");
		GameRegistry.registerItem(fd_stone_spade, "fd_stone_spade", "fakeores");
		GameRegistry.registerItem(fd_stone_hoe, "fd_stone_hoe", "fakeores");
		
		/** DIMENSION PART **/
		mainODBiome = new BiomeGenOreDimension(mainBiomeID).setBiomeName("FakeOresBiome").setHeight(height_fd_Biome).setDisableRain();
		secondODBiome = new SecondBiomeGenOreDimension(secondBiomeID).setBiomeName("FakeOresBiome_2").setHeight(height_fd_Biome).setDisableRain();
		DimensionManager.registerProviderType(dimID, FD_WorldProvider.class, false);
		DimensionManager.registerDimension(dimID, dimID);
		if(event.getSide().isClient())
		{
			FMLCommonHandler.instance().bus().register(new TickClientHandlerEvent());
			MinecraftForge.EVENT_BUS.register(new TickClientHandlerEvent());
		}
		
		antiorestone = new AntiOreStoneDamageSource("destroyedby");
		GameRegistry.registerWorldGenerator(new WorldGenFakeOres(), 0);
		
		/** PARTICLE PART **/
		texture_CustomSmokeFX_particle = new ItemParticleTexture().setUnlocalizedName("texture_CustomSmokeFX_particle").setTextureName("fakeores:customsmoke");
		texture_BloodFX_particle = new ItemParticleTexture().setUnlocalizedName("texture_BloodFX_particle").setTextureName("fakeores:bloodfx");
		texture_NopeFX_particle = new ItemParticleTexture().setUnlocalizedName("texture_NopeFX_particle").setTextureName("fakeores:nope");
		GameRegistry.registerItem(texture_CustomSmokeFX_particle, "texture_CustomSmokeFX_particle");
		GameRegistry.registerItem(texture_BloodFX_particle, "texture_BloodFX_particle");
		GameRegistry.registerItem(texture_NopeFX_particle, "texture_NopeFX_particle");
		
		BucketHandler.INSTANCE.buckets.put(fd_strangeliquid, fd_strangeliquid_bucket);
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		addEntity(EntityDiamondOre.class, "DiamondOre", mob_diamondOreID);
		addEntity(EntityEmeraldOre.class, "EmeraldOre", mob_emeraldOreID);
		addEntity(EntityGoldOre.class, "GoldOre", mob_goldOreID);
		addEntity(EntityIronOre.class, "IronOre", mob_ironOreID);
		addEntity(EntityCoalOre.class, "CoalOre", mob_coalOreID);
		addEntity(EntityRedstoneOre.class, "RedstoneOre", mob_redstoneOreID);
		addEntity(EntityNetherQuartzOre.class, "NetherQuartzOre", mob_quartzOreID);
		addEntity(EntityLapisLazuliOre.class, "LapisLazuliOre", mob_lapisOreID);
		addEntity(EntityMutantMonster.class, "MutantMonster", mob_MUTANT_MONSTER_ID);
		addEntity(EntityNopeGuy.class, "NopeGuy", mob_NOPE_GUY_ID);
		if(Loader.isModLoaded("IC2"))
		{
			addEntity(EntityCopperOre.class, "CopperOre", mob_copperOreID);
			addEntity(EntityTinOre.class, "TinOre", mob_tinOreID);
			addEntity(EntityUraniumOre.class, "UraniumOre", mob_uraniumOreID);
		}
		addEntity(EntityOresBoss.class, "OresBoss", mob_OreBOSSID);
		EntityRegistry.registerModEntity(EntityBossTeleporter.class, "BossTeleporter", mob_BOSS_TELEPORTER_ID, this, 40, 1, true);
		addEntity(EntityPlayerHunter.class, "PlayerHunter", mob_PLAYER_HUNTER_ID);
		addEntity(EntityFlyer.class, "Flyer", mob_FLYER_ID);
		EntityRegistry.registerModEntity(EntityFlyer_Shield.class, "Flyer_Shield", mob_FLYER_SHIELD_ID, this, 40, 1, true);
		addEntity(EntitySchaza.class, "Schaza", mob_SCHAZA_ID);
		addEntity(EntityBlackMage.class, "BlackMage", mob_BLACK_MAGE_ID);
		EntityRegistry.registerModEntity(EntityMageSpell.class, "MageSpell", mob_MAGE_SPELL_ID, this, 40, 1, true);
		addEntity(EntityRegenEgg.class, "RegenEgg", mob_REGEN_EGG_ID);
		addEntity(EntityDangerousPlant.class, "DangerousPlant", mob_DANGEROUS_PLANT_ID);
		EntityRegistry.registerModEntity(EntityNopeGrenade.class, "NopeGrenade", mob_NOPE_GRENADE_ID, this, 40, 1, true);
		addEntity(EntityStalker.class, "Stalker", mob_STALKER_ID);
		proxy.loadRender();
		GameRegistry.addRecipe(new ItemStack(this.antiOresBlade, 1), new Object[] {"X", "X", "B", 'X', this.antiOreStone, 'B', Items.stick});
		GameRegistry.addRecipe(new ItemStack(this.fragment_boss, 1), new Object[] {"XSB", "SFS", "CSD", 'X', this.fragment_part1, 'B', this.fragment_part2, 'F', Blocks.iron_block, 'C', this.fragment_part4, 'D', this.fragment_part3, 'S', Items.gold_ingot});
		GameRegistry.addRecipe(new ItemStack(this.fakeOres_finder, 1), new Object[] {"XBX", "XSX", "XBX", 'X', Blocks.planks, 'B', Items.diamond, 'S', this.antiOresBlade});
		GameRegistry.addSmelting(Blocks.obsidian, new ItemStack(this.fd_redobsidian, 1, 0), 5F);
		GameRegistry.addSmelting(this.fd_blurk_ore, new ItemStack(this.fd_blurk_ingot), 5F);
		GameRegistry.addSmelting(this.fd_daren_ore, new ItemStack(this.fd_daren_ingot), 5F);
		GameRegistry.addSmelting(this.fd_purlight_ore, new ItemStack(this.fd_purlight_ingot), 5F);
		GameRegistry.addSmelting(this.fd_rainbow_ore, new ItemStack(this.fd_rainbow_ingot), 45F);
		GameRegistry.addRecipe(new ItemStack(this.fd_blurk_block, 1), new Object[] {"XXX", "XXX", "XXX", 'X', this.fd_blurk_ingot});
		GameRegistry.addRecipe(new ItemStack(this.fd_daren_block, 1), new Object[] {"XXX", "XXX", "XXX", 'X', this.fd_daren_ingot});
		GameRegistry.addRecipe(new ItemStack(this.fd_purlight_block, 1), new Object[] {"XXX", "XXX", "XXX", 'X', this.fd_purlight_ingot});
		GameRegistry.addRecipe(new ItemStack(this.fd_rainbow_block, 1), new Object[] {"XXX", "XXX", "XXX", 'X', this.fd_rainbow_ingot});
		GameRegistry.addRecipe(new ItemStack(this.fd_blurk_ingot, 9), new Object[] {"X", 'X', this.fd_blurk_block});
		GameRegistry.addRecipe(new ItemStack(this.fd_daren_ingot, 9), new Object[] {"X", 'X', this.fd_daren_block});
		GameRegistry.addRecipe(new ItemStack(this.fd_purlight_ingot, 9), new Object[] {"X", 'X', this.fd_purlight_block});
		GameRegistry.addRecipe(new ItemStack(this.fd_rainbow_ingot, 9), new Object[] {"X", 'X', this.fd_rainbow_block});
		GameRegistry.addRecipe(new ItemStack(this.fd_passcard_schaza, 1), new Object[] {"XXX", "XSX", "XXX", 'X', Items.paper, 'S', new ItemStack(Items.dye, 1, 0)});
		GameRegistry.addShapelessRecipe(new ItemStack(this.nope_word, 1), new Object[] {this.letter_n, this.letter_o, this.letter_p, this.letter_e});
		GameRegistry.addRecipe(new ItemStack(this.nopeGrenade, 1), new Object[] {"RXR", "XBX", "RXR", 'X', Blocks.tnt, 'B', this.nope_word, 'R', this.fd_rainbow_ingot});
		(new FakeOres_RecipeTools()).addRecipes(CraftingManager.getInstance());
		(new FakeOres_RecipeWeapons()).addRecipes(CraftingManager.getInstance());
	}

	public void addEntity(Class<? extends Entity> entityClass, String entityName, int id)
	{
		EntityRegistry.registerGlobalEntityID(entityClass, entityName, EntityRegistry.findGlobalUniqueEntityId(), 0, 0);
		EntityRegistry.registerModEntity(entityClass, entityName, id, this, 40, 1, true);
	}
}