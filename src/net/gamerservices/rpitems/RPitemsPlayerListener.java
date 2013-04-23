package net.gamerservices.rpitems;

import java.util.Map;


import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;


public class RPitemsPlayerListener implements Listener {

	rpitems parent;
	public RPitemsPlayerListener(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		this.parent = rpitems;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		this.parent.setFloating(event.getPlayer(), false);
	}
	

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
        if (event.getItem().getItemStack().getType().equals(Material.PAPER))
        {
        	int id = 0;
			for (Map.Entry<Enchantment,Integer> e : event.getItem().getItemStack().getEnchantments().entrySet())
			{
				
			}
			if (id > 0)
			{
				player.sendMessage(ChatColor.GRAY+"* You have picked up a broker tradeable "+this.parent.getItemName(id)+" - Convert it to a usable spell with /cspell");
			}
        }
        
        if (event.getItem().getItemStack().getType().equals(Material.WRITTEN_BOOK))
        {
        	boolean resurrect = false;
        	int id = 0;
			for (Map.Entry<Enchantment,Integer> e : event.getItem().getItemStack().getEnchantments().entrySet())
			{
				if (e.getKey().equals(Enchantment.PROTECTION_ENVIRONMENTAL))
				{
					resurrect = true;
				}
			}
			
			id = this.parent.getItemIdFromItemStack(event.getItem().getItemStack());
			if (id > 0)
			{
				player.sendMessage(ChatColor.GRAY+"* You have picked up "+this.parent.getItemName(id));
				player.sendMessage(ChatColor.GRAY+"Hold it in your hand and left click to cast it - You can make it broker tradeable with /cspell");
			}
			
			if (resurrect == true)
			{
				player.sendMessage(ChatColor.GRAY+"* You have picked up a resurrection tome!");
				
			}
        }
		
	}

	/*
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() instanceof LivingEntity)
		{
			Player player = event.getPlayer();
			if (player.getItemInHand().getData().getItemType() == Material.WRITTEN_BOOK)
			{
				Interact i = new Interact();
				i.playername = player.getName();
				i.interacting = true;
				this.parent.interacting.add(i);
	
				int itemid = 0;
	
				for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
				{
					itemid = e.getValue();
				}
	
				if (itemid > 0)
				{
					// work around for right click entity bug..
					this.parent.useItem(itemid, player,event.getRightClicked());
				}
			}
		}
	}

	*/
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			if (this.parent.isPlayerSentry(player))
			{
				return;
			} else {
				// drop rez scroll
				this.parent.dropResurrectScroll(player,player.getLocation());
				
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if ((action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR))) {
			Player player = event.getPlayer();
			if (player.getItemInHand().getData().getItemType() == Material.WRITTEN_BOOK)
			{
				boolean resurrect = false;
				int itemid = 0;
				for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
				{
					if (e.getKey().equals(Enchantment.PROTECTION_ENVIRONMENTAL))
					{
						resurrect = true;
					}
				}
				
				itemid = this.parent.getItemIdFromItemStack(player.getItemInHand());

				if (itemid > 0)
				{
					// check if entity there
					this.parent.useItem(itemid, player,player.getTargetBlock(null, 200));
				}
				
				if (resurrect == true)
				{
					this.parent.useResurrectItem(player,player.getItemInHand());
				}
			}
		}
	}	

	
	


}
