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
import org.bukkit.inventory.meta.ItemMeta;

public class AssignItem implements CommandExecutor {

	rpitems parent;
	public AssignItem(rpitems rpitems) {
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
		if (args.length == 0)
		{
			return false;
		}
		if (player.isOp())
		{
			if (player.getItemInHand().getData().getItemType() == Material.MONSTER_EGG)
			{
				if (player.getItemInHand().getAmount() == 1)
				{
					if (Integer.parseInt(args[0]) > 0)
					{
						int id = Integer.parseInt(args[0]);
						String effect = this.parent.getItemEffect(id);
						if (effect.equals("questitem"))
						{
							Ability ability = this.parent.getAbilityByID(id);
							ArrayList<String> lore = new ArrayList<String>();
							lore.add(ability.lore);
							String name = ability.name;
							
							ItemStack egg = new ItemStack(Material.MONSTER_EGG);
							egg.addUnsafeEnchantment(Enchantment.OXYGEN, id);
							// Give it a name
							ItemMeta im = egg.getItemMeta();
							im.setDisplayName(name);
							im.setLore(lore);
							// Reset the name to default
							//namedItemStack.setName(null);
							// Get the item's name (returns null if it hasn't been renamed)
							//namedItemStack.getName();
							egg.setItemMeta(im);
							player.getInventory().setItemInHand(egg);
							player.updateInventory();
							
							player.sendMessage("Item assigned");
							return true;
						} else {
							player.sendMessage("This particular item id is not a quest item");
						}
					} else {
						player.sendMessage("Incorrect quest item id");
					}
					
				} else {
					player.sendMessage("Too many items in stack (max 1)");
					return true;
				}
				
			}
			
			
			if (player.getItemInHand().getData().getItemType() == Material.PAPER)
			{
				if (player.getItemInHand().getAmount() == 1)
				{
					if (Integer.parseInt(args[0]) < 20000 && Integer.parseInt(args[0]) > 0)
					{
						String effect = this.parent.getItemEffect(Integer.parseInt(args[0]));
						if (effect.equals("questitem"))
						{
							player.sendMessage("This item id is a quest item and can only be assigned to quest objects");
							return true;
						}
						for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
						{
							player.getItemInHand().removeEnchantment(e.getKey());
						}
						
						player.getItemInHand().addUnsafeEnchantment(Enchantment.OXYGEN, Integer.parseInt(args[0]));
						
						
						
						player.sendMessage("Item assigned");
						return true;
					} else {
						if (Integer.parseInt(args[0]) == 0)
						{
							int id = 1;
							for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
							{
								id = e.getValue();
							}
							
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
							
						}
					}
				} else {
					player.sendMessage("Too many items in stack (max 1)");
				}
			} else {
				player.sendMessage("Item is not book");
			}
		}
		
		
		return false;
		
		
	}

}
