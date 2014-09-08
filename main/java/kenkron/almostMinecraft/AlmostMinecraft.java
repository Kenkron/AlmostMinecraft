package kenkron.almostMinecraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**This mod is designed to add functionality to Minecraft, without adding new blocks.
 * 
 * This includes three primary changes:
 *   1. The dropper is now able to plant on the block it is facing.
 *   2. The piston can now break blocks if, after extension, it has
 *      pinned the block between two iron blocks.
 *   3. Animals can now eat dropped food.
 *   
 * There are other possible changes that may come about, but are not
 * core features:
 *   1. Compass and Clock now appear in HUD while in inventory.
 *   2. Beacons may now be used as chunk loaders.
 *      (implementing a chunk loader may be difficult)
 *   */
@Mod(modid=AlmostMinecraft.MODID,version=AlmostMinecraft.VERSION)
public class AlmostMinecraft {

	public static final String MODID="AlmostMinecraft";
	public static final String VERSION="1.0";
	
	@EventHandler
    public void init(FMLInitializationEvent event)
    {
		System.out.println("Initializing AlmostMinecraft");
		
		////////////////Dropper changes:////////////////
		System.out.println("Initializing AlmostDropper");
		Block almostDropper = new BlockAlmostDropper()
		.setHardness(3.5f)
    	.setStepSound(Block.soundTypePiston)
    	.setBlockName("almost-dropper")
    	.setBlockTextureName("dropper")
    	.setCreativeTab(CreativeTabs.tabRedstone);
        GameRegistry.registerBlock(almostDropper, "almost_dropper");
        LanguageRegistry.addName(almostDropper, "Almost Dropper");
        ItemStack dropperStack = new ItemStack(almostDropper);
        //GameRegistry.addRecipe(dropperStack, "xxx", "x x","xrx",'x',Block.blockRegistry.getObject("cobblestone"),'r',Item.itemRegistry.getObject("redstone"));
        
        ////////////////Piston Changes:////////////////
		System.out.println("Initializing AlmostPiston1");
        
        //only sticky pistons can build block breakers right now
        
        //AlmostPiston almostPiston = new AlmostPiston(false);
        //almostPiston.setBlockName("pistonBase");
        //Block.blockRegistry.addObject(33, "piston", almostPiston);
        
        AlmostPiston almostStickyPiston = new AlmostPiston(true);
        almostStickyPiston.setBlockName("almostPistonStickyBase");
        GameRegistry.registerBlock(almostStickyPiston, "almost_sticky_piston");
        ItemStack pistonStack=new ItemStack(almostStickyPiston);
        GameRegistry.addShapelessRecipe(pistonStack, Block.blockRegistry.getObject("piston"), Item.itemRegistry.getObject("slime_ball"));
        LanguageRegistry.addName(almostStickyPiston, "Almost Sticky Piston");
//        GameRegistry.addRecipe(pistonStack, "www","xix","xrx",
//        		'w',Block.blockRegistry.getObject("planks"),
//        		'x',Block.blockRegistry.getObject("cobblestone"),
//        		'i',Item.itemRegistry.getObject("iron_ingot"),
//        		'r',Item.itemRegistry.getObject("redstone"));
        ////////////////Breeding Changes:////////////////
        
    }
	
}
