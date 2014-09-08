package kenkron.almostMinecraft;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
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
		System.out.println("Initializing AlmostPiston");
        
        //only sticky pistons can build block breakers right now
        
        //AlmostPiston almostPiston = new AlmostPiston(false);
        //almostPiston.setBlockName("pistonBase");
        //Block.blockRegistry.addObject(33, "piston", almostPiston);
        
        AlmostPiston almostStickyPiston = new AlmostPiston(true);
        almostStickyPiston.setBlockName("almostPistonStickyBase");
        GameRegistry.registerBlock(almostStickyPiston, "sticky_piston");
        ItemStack pistonStack=new ItemStack(almostStickyPiston);
        ItemStack oldStickyPiston = new ItemStack((Block)Block.blockRegistry.getObject("sticky_piston"));
        RemoveRecipe(oldStickyPiston);
        //a recipe to convert vanilla pistons to the new pistons
        GameRegistry.addShapelessRecipe(pistonStack, oldStickyPiston);
        //replace the vanilla recipe so that it creates an almost-piston instead of a sticky piston.
        GameRegistry.addShapelessRecipe(pistonStack, Block.blockRegistry.getObject("piston"), Item.itemRegistry.getObject("slime_ball"));
        LanguageRegistry.addName(almostStickyPiston, "Almost Sticky Piston");
        ////////////////Breeding Changes:////////////////
        
    }
	
	private static void RemoveRecipe(ItemStack resultItem)
	{
	ItemStack recipeResult = null;
	ArrayList recipes = (ArrayList) CraftingManager.getInstance().getRecipeList();
	for (int scan = 0; scan < recipes.size(); scan++)
	{
		 IRecipe tmpRecipe = (IRecipe) recipes.get(scan);
		 String tmpName = "none";
		 if (tmpRecipe!=null && tmpRecipe.getRecipeOutput()!=null && tmpRecipe.getRecipeOutput().getDisplayName()!=null){
			 tmpName=(tmpRecipe.getRecipeOutput().getDisplayName());
		 }
		 System.out.println(tmpName);
		 if (tmpRecipe !=null)
		 {
			 recipeResult = tmpRecipe.getRecipeOutput();
		 }
		 if (resultItem!=null&&recipeResult!=null){
			System.out.println("match "+resultItem.getDisplayName()+", "+recipeResult.getDisplayName());
			 if(resultItem.getDisplayName().equals(recipeResult.getDisplayName()))
			 {
				 System.out.println("AlmostMinecraft Removed Recipe: " + recipes.get(scan) + " -> " + recipeResult);
				 recipes.remove(scan);
			 }
		 }
	}
	}
}
