package net.gamerservices.rpitems;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

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
			if (player.getItemInHand().getData().getItemType() == Material.PAPER)
			{
				if (player.getItemInHand().getAmount() == 1)
				{
					if (Integer.parseInt(args[0]) < 20000 && Integer.parseInt(args[0]) > 0)
					{
						for (Map.Entry<Enchantment,Integer> e : player.getItemInHand().getEnchantments().entrySet())
						{
							player.getItemInHand().removeEnchantment(e.getKey());
						}
						
						player.getItemInHand().addUnsafeEnchantment(Enchantment.OXYGEN, Integer.parseInt(args[0]));
						player.sendMessage("Item assigned");
						return true;
					} else {
						player.sendMessage("Item ID must be below 20000");
					}
				} else {
					player.sendMessage("Too many items in stack (max 1)");
				}
			} else {
				player.sendMessage("Item is not paper");
			}
		}
		
		
		return false;
	}

}
