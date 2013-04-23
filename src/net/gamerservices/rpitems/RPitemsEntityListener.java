package net.gamerservices.rpitems;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
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
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if ((event.getEntity() instanceof Player))
		{
			if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
			{
				Player player = (Player)event.getEntity();
				if (this.plugin.isGliding(player))
				{
					event.setCancelled(true);
					event.setDamage(0);
				}
			}
			
		}
		
		if (event.getEntity() instanceof Fireball)
		{
			event.setCancelled(true);
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityExplodeEvent(EntityExplodeEvent event)
	{
			if (event.getEntity() instanceof Fireball)
			{
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onExplosionPrime(ExplosionPrimeEvent event)
	{
			if (event.getEntity() instanceof Fireball)
			{
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if ((event.getEntity() instanceof Monster) || (event.getEntity() instanceof Squid))
		{
			if(event.getEntity().getKiller() != null)
			{
				Player attacker = event.getEntity().getKiller();
				LivingEntity victim = (LivingEntity)event.getEntity();
				int chance = 1 + (int)(Math.random() * ((10 - 1) + 1));
				if (chance > 9)
				{
					this.plugin.droporgiveRandomAbilityScrollByMonster(event.getEntityType(),event.getEntity().getLocation(),attacker);
					
				}
			}
			/* disabled for economy changes
			if(event.getEntity().getKiller() != null)
			{
				Player attacker = event.getEntity().getKiller();
				ItemStack is = new ItemStack(Material.GOLD_NUGGET);
				is.setAmount(2);
				event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), is);
			}
			*/
		}
		
		
	}
}
