package net.gamerservices.rpitems;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class ConvertSpell implements CommandExecutor {

	rpitems parent;
	public ConvertSpell(rpitems rpitems) {
		this.parent = rpitems;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		// TODO Auto-generated method stub
		
		
		if (!(sender instanceof Player))
		{
			return false;
		}
		
		Player player = (Player)sender;
		
		if (player.getItemInHand().getData().getItemType() == Material.PAPER)
		{
			if (player.getItemInHand().getAmount() == 1)
			{
				int id = 0;
				for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
				{
					id = e.getValue();
				}
				if (id > 0)
				{
					
					ItemStack is = new ItemStack(387,1);
					BookMeta meta =  (BookMeta)is.getItemMeta();
					meta.setAuthor("illusiak the geomancer");
					meta.setTitle(this.parent.getItemName(id));
					ArrayList<String> pages = new ArrayList<String>();
					pages.add(this.parent.getItemLore(id));
					meta.setPages(pages);
					is.setItemMeta(meta);
					player.getInventory().setItemInHand(is);
					player.updateInventory();
					player.sendMessage("Spell Scroll converted to Spell Tome");
					return true;
				} else {
					player.sendMessage("Item is not a spell");
				}
			} else {
				player.sendMessage("Too many items in stack (max 1)");
			}
		} else {
			
			if (player.getItemInHand().getData().getItemType() == Material.WRITTEN_BOOK)
			{
				if (player.getItemInHand().getAmount() == 1)
				{
					int id = 0;
					id = this.parent.getItemIdFromItemStack(player.getItemInHand());
					if (id > 0)
					{
						ItemStack is = new ItemStack(Material.PAPER);
						is.setAmount(1);
						is.addUnsafeEnchantment(Enchantment.OXYGEN, id);
						player.getInventory().setItemInHand(is);
						
						player.updateInventory();
						player.sendMessage("Spell Tome converted to Spell Scroll");
						return true;
					} else {
						player.sendMessage("Item is not a spell");
					}
				} else {
					player.sendMessage("Too many items in stack (max 1)");
				}
			} else {
				player.sendMessage("Item is not a piece of paper");
			}
		}
		
		return false;
		
		
	}

}
