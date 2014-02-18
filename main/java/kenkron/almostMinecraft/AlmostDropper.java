package kenkron.almostMinecraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

/**Most of this class is an exact duplicate of BlockDropper.
 * The only reason it does not extend BlockDropper is because
 * dispenseBehavior (aka field_149947_P) was private, not protected.
 * I wish people used protected more often...*/
public class AlmostDropper extends BlockDispenser
{
    private final IBehaviorDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
    private static final String __OBFID = "CL_00000233";

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("furnace_side");
        this.field_149944_M = p_149651_1_.registerIcon("furnace_top");
        this.field_149945_N = p_149651_1_.registerIcon(this.getTextureName() + "_front_horizontal");
        this.field_149946_O = p_149651_1_.registerIcon(this.getTextureName() + "_front_vertical");
    }

    protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_)
    {
        return this.dispenseBehavior;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityDropper();
    }

    /**this is surely the dispense function, and the only function I will edit.*/
    protected void func_149941_e(World world, int x, int y, int z)
    {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, x, y, z);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();

        if (tileentitydispenser != null)
        {
            int l = tileentitydispenser.func_146017_i();

            if (l < 0)
            {
                world.playAuxSFX(1001, x, y, z, 0);
            }
            else
            {
                ItemStack inventoryStack = tileentitydispenser.getStackInSlot(l);
                int orientation = world.getBlockMetadata(x, y, z) & 7;
                IInventory iinventory = TileEntityHopper.func_145893_b(world, (double)(x + Facing.offsetsXForSide[orientation]), (double)(y + Facing.offsetsYForSide[orientation]), (double)(z + Facing.offsetsZForSide[orientation]));
                ItemStack newInventoryStack=null;

                //if there is an inventory in front of the dropper
                if (iinventory != null)
                {
                    newInventoryStack = TileEntityHopper.func_145889_a(iinventory, inventoryStack.copy().splitStack(1), Facing.oppositeSide[orientation]);

                    if (newInventoryStack == null)
                    {
                        newInventoryStack = inventoryStack.copy();

                        if (--newInventoryStack.stackSize == 0)
                        {
                            newInventoryStack = null;
                        }
                    }
                    else
                    {
                        newInventoryStack = inventoryStack.copy();
                    }
                }
                //otherwise
                else
                {
                	//the contents of this else statement are customized
                	
                	//a flag marking whether the plant was successful
                	boolean planted=false;
                	
                	if (inventoryStack.getItem() instanceof IPlantable){
                		IPlantable seed=(IPlantable)inventoryStack.getItem();
                		
                		//get the block in front of the dropper
                    	Block facingBlock = world.getBlock(
                    			x+Facing.offsetsXForSide[orientation], 
                    			y+Facing.offsetsYForSide[orientation], 
                    			z+Facing.offsetsZForSide[orientation]);
                    	
                    	if (facingBlock.canSustainPlant(world, x, y, z, ForgeDirection.DOWN, seed)){
                    		
                    		world.setBlock(x, y, z, seed.getPlant(world,x,y,z));
                    		
                    		//take one off of this stack.
                    		newInventoryStack = inventoryStack.splitStack(1);
                    		
                    		planted=true;
                    	}
                	}
                	
                	//if no planting was done, use the default dropper behavior
                	if (!planted){
                		newInventoryStack = this.dispenseBehavior.dispense(blocksourceimpl, inventoryStack);
                	}
                		
                    if (newInventoryStack != null && newInventoryStack.stackSize == 0)
                    {
                        newInventoryStack = null;
                    }
                }

                tileentitydispenser.setInventorySlotContents(l, newInventoryStack);
            }
        }
    }
}