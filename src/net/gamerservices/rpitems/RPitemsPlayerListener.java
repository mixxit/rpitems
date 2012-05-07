package net.gamerservices.rpitems;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class RPitemsPlayerListener implements Listener {

	rpitems parent;
	public RPitemsPlayerListener(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		this.parent = rpitems;
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if (player.getItemInHand().getData().getItemType() == Material.PAPER)
		{
			int itemid = 0;
			
			for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
			{
				itemid = e.getValue();
			}
			
			if (itemid > 0)
			{
				//
				this.parent.useItem(itemid, player);
			}
			
			
		}

	}	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		int itemid = 0;
		if (event.getItem().getItemStack().getData().getItemType() == Material.PAPER)
		{
			for (Map.Entry<Enchantment,Integer> e : event.getItem().getItemStack().getEnchantments().entrySet())
			{
				itemid = e.getValue();
			}
			if (itemid > 0)
			{
				System.out.println("Picked up an item ID of: " + itemid);
				event.getPlayer().sendMessage("You picked up " + parent.getItemName(itemid));
			}
		}
		
		
		
		/*
		 * 
		 * Playing around storing data on enchantments (probably not usable)
		boolean hasData = false;
		
		for (Map.Entry<Enchantment,Integer> e : event.getItem().getItemStack().getEnchantments().entrySet())
		{
			System.out.println("Item Pickup was: " + e.getKey() + " " + e.getValue());
			hasData = true;
		}
		if (hasData == false)
		{
			Enchantment ench = new EnchantmentWrapper(5); 
			
			int level = 10000;
			event.getItem().getItemStack().addUnsafeEnchantment(ench, level);
			System.out.println("Added ID to item type of: " + level); 
		}	
		
		*/
	}

}
