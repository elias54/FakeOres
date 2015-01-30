package fr.elias.fakeores.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class ItemFireOrb extends Item
{
    public ItemFireOrb()
    {
        super();
        setMaxDamage(256);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            list.add("Just hold item in your hand");
            list.add("and move near any entity, ENJOY ! :D");
        }
        else
        {
            list.add("Press 'SHIFT' for more information.");
        }
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean isHeldedByPlayer)
    {
        EntityPlayer player = (EntityPlayer)entity;
        super.onUpdate(itemstack, world, entity, slot, isHeldedByPlayer);
        List list = world.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.fromBounds(player.posX, player.posY, player.posZ, player.posX + 1, player.posY + 1, player.posZ + 1).expand(6D, 4D, 6D));
        if(player != null)
        {
            for(int i = 0; i < list.size(); i++)
            {
                Entity entity2 = (Entity)list.get(i);
                if(!list.isEmpty() && !(entity2 instanceof EntityPlayer) && isHeldedByPlayer)
                {
                    if(player.inventory.hasItem(Items.fire_charge))
                    {
                        entity2.setFire(5);
                        if(world.rand.nextInt(10) == 0)
                        {
                            player.inventory.consumeInventoryItem(Items.fire_charge);
                            itemstack.damageItem(2, player);
                        }
                    }
                }
            }
        }
    }
}