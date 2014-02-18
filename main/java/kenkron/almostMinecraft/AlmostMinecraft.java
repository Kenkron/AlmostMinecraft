package kenkron.almostMinecraft;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid=AlmostMinecraft.MODID,version=AlmostMinecraft.VERSION)
public class AlmostMinecraft {

	public static final String MODID="AlmostMinecraft";
	public static final String VERSION="1.0";
	
	@EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.func_149730_j());
    }
}
