package net.gamerservices.rpitems;

import java.util.ArrayList;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SpellKitCommand implements CommandExecutor{

	rpitems parent;
	public SpellKitCommand(rpitems rpitems) {
		
		this.parent = rpitems;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		if (this.parent.kittedplayers.contains(player))
		{
			player.sendMessage("You cannot use the spell kit until next server restart");
			return true;
		}
		int id = 1;
		// tome format
		// scholarly
		id = 7;
		ItemStack is = new ItemStack(387,1);
		BookMeta meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.parent.getItemName(id));
		ArrayList<String> pages = new ArrayList<String>();
		pages.add(this.parent.getItemLore(id));
		meta.setPages(pages);
		is.setItemMeta(meta);
		Item droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
		
		// nature
		id = 8;
		is = new ItemStack(387,1);
		meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.parent.getItemName(id));
		pages = new ArrayList<String>();
		pages.add(this.parent.getItemLore(id));
		meta.setPages(pages);
		is.setItemMeta(meta);
		 droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
		
		// combat
		id = 28;
		is = new ItemStack(387,1);
		meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.parent.getItemName(id));
		pages = new ArrayList<String>();
		pages.add(this.parent.getItemLore(id));
		meta.setPages(pages);
		is.setItemMeta(meta);
		 droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
		
		// range
		id = 31;
		is = new ItemStack(387,1);
		meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.parent.getItemName(id));
		pages = new ArrayList<String>();
		pages.add(this.parent.getItemLore(id));
		meta.setPages(pages);
		is.setItemMeta(meta);
		 droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
			
		// range
		id = 80;
		is = new ItemStack(387,1);
		meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.parent.getItemName(id));
		pages = new ArrayList<String>();
		pages.add(this.parent.getItemLore(id));
		meta.setPages(pages);
		is.setItemMeta(meta);
		 droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
		
   		player.sendMessage("A batch of level 1 spells have been dropped at your feet!");
   		
   		this.parent.kittedplayers.add(player);
   		return true;

	}

	
}
