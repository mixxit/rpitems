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
				this.parent.queryItem(player,itemid);
				
				return true;
			}
		}
		if (player.getItemInHand().getData().getItemType() == Material.WRITTEN_BOOK)
		{
			int itemid = 0;
			itemid = this.parent.getItemIdFromItemStack(player.getItemInHand());
			
			if (itemid > 0)
			{
				this.parent.queryItem(player,itemid);
				
				return true;
			}
		}
		
		
		return false;
	}

}
