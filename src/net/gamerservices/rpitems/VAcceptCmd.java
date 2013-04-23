package net.gamerservices.rpitems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VAcceptCmd implements CommandExecutor{

	rpitems parent;
	public VAcceptCmd(rpitems rpitems) {
		
		this.parent = rpitems;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		if (this.parent.isBuffed(player,"turnvampire"))
		{
			player.sendMessage("You feel an overwhelming sense of sorrow as your life seeps away from you.");
			this.parent.turnVampire(player);
			return true;
		} else {
			player.sendMessage("You must have the buff Gift of Darkness to use this command");
			return true;
		}
	}

	
}
