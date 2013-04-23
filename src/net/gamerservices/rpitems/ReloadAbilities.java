package net.gamerservices.rpitems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadAbilities implements CommandExecutor {

	rpitems parent;
	public ReloadAbilities(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		parent = rpitems;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
	
		if (!(arg0 instanceof Player)) {
			this.parent.loadAbilities();
			arg0.sendMessage("Abilities Reloaded");
			return true;
		}
		Player player = (Player)arg0;
		
		if (player.isOp() || this.parent.hasPerm(player, "rpitems.admin"))
		{
			this.parent.loadAbilities();
			player.sendMessage("Abilities Reloaded");
			return true;
		}
		
		return false;
		
	}

}
