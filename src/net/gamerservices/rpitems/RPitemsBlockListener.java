package net.gamerservices.rpitems;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RPitemsBlockListener implements Listener {

	rpitems plugin;
	public RPitemsBlockListener(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		this.plugin = rpitems;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event){
		if (!event.isCancelled())
		{
			if (event.getBlock().getType().equals(Material.DIAMOND_ORE))
			{
				int index = plugin.RandomNumber(1,20);
				if (index > 15)
				{
					ItemStack ingot = new ItemStack(Material.DIAMOND);
					// Give it a name
					ItemMeta im = ingot.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(this.plugin.customore + " infused ore");
					im.setLore(lore);
					// Reset the name to default
					//namedItemStack.setName(null);
					// Get the item's name (returns null if it hasn't been renamed)
					//namedItemStack.getName();
					ingot.setItemMeta(im);
					event.getPlayer().sendMessage("Within the encrusted diamond, you found some rare ore ("+this.plugin.customore+")!");
					event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), ingot);
				}
			}
		} 
		
	}

}
