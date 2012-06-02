package net.gamerservices.rpitems;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
 
public class RPitemsEntityListener implements Listener
{
	private rpitems plugin;
	
	public RPitemsEntityListener(rpitems rpitems)
	{
		this.plugin = rpitems;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if ((event.getEntity() instanceof Monster))
		{
			if(event.getEntity().getKiller() != null)
			{
				Player attacker = event.getEntity().getKiller();
				Monster victim = (Monster)event.getEntity();
				int chance = 1 + (int)(Math.random() * ((1000 - 1) + 1));
				if (chance > 950)
				{
					ItemStack ability = this.plugin.getRandomAbilityScroll();
					event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), ability);
				}
			}
		}
	}
}
