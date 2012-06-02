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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.sun.corba.se.spi.orbutil.fsm.Action;

public class RPitemsPlayerListener implements Listener {

	rpitems parent;
	public RPitemsPlayerListener(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		this.parent = rpitems;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEntityEvent event)
	{
		if(!event.isCancelled()){
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
					this.parent.useItem(itemid, player,event.getRightClicked());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.isCancelled()){ 
		
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
					this.parent.useItem(itemid, player,event.getAction());
				}
			}
		}

	}	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if(!event.isCancelled()){
			int nextfree = event.getPlayer().getInventory().firstEmpty();
			
			if (nextfree != -1)
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
						System.out.println(event.getPlayer().getName() + " picked up : " + parent.getItemName(itemid));
						event.getPlayer().sendMessage("You picked up " + parent.getItemName(itemid) + " (/queryitem for more info)");
					}
				}
			} else {
				// work around for full inventory
			}
		}
	}

}
