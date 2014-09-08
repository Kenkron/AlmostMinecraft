package kenkron.almostMinecraft;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class AlmostPiston extends BlockPistonBase{

	public AlmostPiston(boolean truth) {
		super(truth);
	}

    /**
     * handles attempts to extend or retract the piston.
     */
    private void updatePistonState(World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        int i1 = getPistonOrientation(l);
        System.out.println("It's happening");
        if (i1 != 7)
        {
            boolean flag = this.isIndirectlyPowered(world, x, y, z, i1);

            if (flag && !isExtended(l))
            {
                if (canExtend(world, x, y, z, i1))
                {
                	//this is the code that actually extends... I think
                    world.addBlockEvent(x, y, z, this, 0, i1);
                    
                    //this is my own check
                    Block[] blocks = new Block[4];
                    int[][] coords=new int[4][3];
                    
                    for (int i=0;i<blocks.length;i++){
                    	coords[i][0]=x+Facing.offsetsXForSide[i1]*(i+1); 
                    	coords[i][1]=y+Facing.offsetsYForSide[i1]*(i+1);
                    	coords[i][2]=z+Facing.offsetsZForSide[i1]*(i+1);
                    	
                    	blocks[i]=world.getBlock(
                    			coords[i][0], 
                    			coords[i][1], 
                    			coords[i][2]);
                    	
                    }

                	int tx1=coords[1][0], ty1=coords[1][1], tz1=coords[1][2];
                	int tx2=coords[2][0], ty2=coords[2][1], tz2=coords[2][2];
                	
                	Block iron_block = (Block)Block.blockRegistry.getObject("iron_block");
                    //if the piston pinches something between two iron blocks
                    if (blocks[0] == Block.blockRegistry.getObject("iron_block") && blocks[3] == Block.blockRegistry.getObject("iron_block")){
                    	//two possible breaking methods:
                    	if (blocks[1].isAir(world, tx1, ty1, tz1)){
                    		System.out.println("breaking: "+tx2+", "+ty2+", "+tz2);
                    		breakBlock(world, blocks[2], tx2, ty2, tz2);
                    		//blocks[3].dropBlockAsItem(world, x, y, z, l, 0);
                    	}else if (blocks[2].isAir(world, tx2, ty2, tz2)){
                    		System.out.println("breaking: "+tx1+", "+ty1+", "+tz1);
                    		breakBlock(world, blocks[1], tx1, ty1, tz1);
                    		//blocks[3].dropBlockAsItem(world, x, y, z, l, 0);
                    	}
                    }
                }
            }
            else if (!flag && isExtended(l))
            {
                world.setBlockMetadataWithNotify(x, y, z, i1, 2);
                world.addBlockEvent(x, y, z, this, 1, i1);
            }
        }
    }
	
    /**block breaking method*/
    public void breakBlock(World world, Block b, int x,int y,int z){
    	System.out.println("breakingBlock: "+x+", "+y+", "+z+": "+b.getLocalizedName());
    	
    	ArrayList<ItemStack> drops=b.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
    	for (ItemStack stack:drops){
    		EntityItem entityitem = new EntityItem(world, x, y, z, stack);
    		world.spawnEntityInWorld(entityitem);
    	}
    	
    		IInventory inventory = TileEntityHopper.func_145893_b(world, x, y, z);
    		if (inventory!=null){
    			for (int i = 0;i<inventory.getSizeInventory();i++){
    				ItemStack stack = inventory.getStackInSlot(i);
    				if (stack!=null){
    					EntityItem entityitem = new EntityItem(world, x, y, z, stack);
    	    			world.spawnEntityInWorld(entityitem);
    				}
    			}
    		}
    	
    	world.removeTileEntity(x, y, z);
    	world.setBlockToAir(x, y, z);
    }
    
    //*******************************************************************************//
    //the following is copied from the Block class, and edited to not require players//
    //*******************************************************************************//
    
    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
/*    public void harvestBlock(World p_149636_1_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_)
    {

        if (this.canSilkHarvest(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_) && EnchantmentHelper.getSilkTouchModifier(p_149636_2_))
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(p_149636_6_);

            if (itemstack != null)
            {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, p_149636_1_, this, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, 0, 1.0f, true, p_149636_2_);
            for (ItemStack is : items)
            {
                this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, is);
            }
        }
        else
        {
            harvesters.set(p_149636_2_);
            int i1 = EnchantmentHelper.getFortuneModifier(p_149636_2_);
            this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, i1);
            harvesters.set(null);
        }
    }*/
    
    //*******************************************************************//
    //methods changed to use the new UpdatePistonState method            //
    //*******************************************************************//
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);

        if (!p_149689_1_.isRemote)
        {
            this.updatePistonState(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    @Override
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isRemote)
        {
            this.updatePistonState(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }
    
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        if (!p_149726_1_.isRemote && p_149726_1_.getTileEntity(p_149726_2_, p_149726_3_, p_149726_4_) == null)
        {
            this.updatePistonState(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        }
    }
    
    //*******************************************************************//
    //all of the following methods are exact copies from BlockPistonBase,// 
    //except that they are now protected, not private.                   //
    //*******************************************************************//
    
    
    protected boolean isIndirectlyPowered(World p_150072_1_, int p_150072_2_, int p_150072_3_, int p_150072_4_, int p_150072_5_)
    {
        return p_150072_5_ != 0 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ - 1, p_150072_4_, 0) ? true : (p_150072_5_ != 1 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_, 1) ? true : (p_150072_5_ != 2 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ - 1, 2) ? true : (p_150072_5_ != 3 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ + 1, 3) ? true : (p_150072_5_ != 5 && p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_, p_150072_4_, 5) ? true : (p_150072_5_ != 4 && p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_, p_150072_4_, 4) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_, 0) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 2, p_150072_4_, 1) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ - 1, 2) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ + 1, 3) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_ + 1, p_150072_4_, 4) ? true : p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_ + 1, p_150072_4_, 5)))))))))));
    }
    

    /**
     * checks to see if this piston could push the blocks in front of it.
     */
    protected static boolean canExtend(World p_150077_0_, int p_150077_1_, int p_150077_2_, int p_150077_3_, int p_150077_4_)
    {
        int i1 = p_150077_1_ + Facing.offsetsXForSide[p_150077_4_];
        int j1 = p_150077_2_ + Facing.offsetsYForSide[p_150077_4_];
        int k1 = p_150077_3_ + Facing.offsetsZForSide[p_150077_4_];
        int l1 = 0;

        while (true)
        {
            if (l1 < 13)
            {
                if (j1 <= 0 || j1 >= p_150077_0_.getHeight())
                {
                    return false;
                }

                Block block = p_150077_0_.getBlock(i1, j1, k1);

                if (!block.isAir(p_150077_0_, i1, j1, k1))
                {
                    if (!canPushBlock(block, p_150077_0_, i1, j1, k1, true))
                    {
                        return false;
                    }

                    if (block.getMobilityFlag() != 1)
                    {
                        if (l1 == 12)
                        {
                            return false;
                        }

                        i1 += Facing.offsetsXForSide[p_150077_4_];
                        j1 += Facing.offsetsYForSide[p_150077_4_];
                        k1 += Facing.offsetsZForSide[p_150077_4_];
                        ++l1;
                        continue;
                    }
                }
            }

            return true;
        }
    }

    /**
     * returns true if the piston can push the specified block
     */
    protected static boolean canPushBlock(Block p_150080_0_, World p_150080_1_, int p_150080_2_, int p_150080_3_, int p_150080_4_, boolean p_150080_5_)
    {
        if (p_150080_0_ == Blocks.obsidian)
        {
            return false;
        }
        else
        {
            if (p_150080_0_ != Blocks.piston && p_150080_0_ != Blocks.sticky_piston)
            {
                if (p_150080_0_.getBlockHardness(p_150080_1_, p_150080_2_, p_150080_3_, p_150080_4_) == -1.0F)
                {
                    return false;
                }

                if (p_150080_0_.getMobilityFlag() == 2)
                {
                    return false;
                }

                if (p_150080_0_.getMobilityFlag() == 1)
                {
                    if (!p_150080_5_)
                    {
                        return false;
                    }

                    return true;
                }
            }
            else if (isExtended(p_150080_1_.getBlockMetadata(p_150080_2_, p_150080_3_, p_150080_4_)))
            {
                return false;
            }

            return !(p_150080_1_.getBlock(p_150080_2_, p_150080_3_, p_150080_4_).hasTileEntity(p_150080_1_.getBlockMetadata(p_150080_2_, p_150080_3_, p_150080_4_)));
            
        }
    }

}
