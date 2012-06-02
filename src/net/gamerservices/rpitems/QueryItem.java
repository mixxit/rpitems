package net.gamerservices.rpitems;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class QueryItem implements CommandExecutor {

	rpitems parent;
	public QueryItem(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		parent = rpitems;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Player)) {
			return false;
		}
		Player player = (Player)arg0;
		
		if (player.getItemInHand().getData().getItemType() == Material.PAPER)
		{
			int itemid = 0;
			
			for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
			{
				itemid = e.getValue();
			}
			
			if (itemid > 0)
			{
				player.sendMessage("Item Name: " + this.parent.getItemName(itemid));
				player.sendMessage("Item Lore: " + this.parent.getItemLore(itemid));
				player.sendMessage("Races Req: " + this.parent.getItemRaces(itemid));
				player.sendMessage("Alliance Req: " + this.parent.getItemAlliances(itemid));
				player.sendMessage("Power Cost: " + this.parent.getSpellPowerCost(itemid));
				player.sendMessage("Com: " + this.parent.getCombatItemLevel(itemid) + " Ran: " + this.parent.getRangedItemLevel(itemid) + " Scho: "+ this.parent.getScholarlyItemLevel(itemid)+ " Nat: " + this.parent.getNaturalItemLevel(itemid));
				
				return true;
			}
		}
		
		
		
		return false;
	}

}
