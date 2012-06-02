package net.gamerservices.rpitems;

import com.earth2me.essentials.Essentials;
import com.palmergames.bukkit.towny.Towny;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.PersistenceException;

import net.gamerservices.rpchat.rpchat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class rpitems extends JavaPlugin
{
  private PluginManager pm;
  private Towny towny = null;
  public static Permission permission;
  private Essentials essentials = null;
  public boolean isEssentials = false;
  private Listener rpitemsPlayerListener;
  private RPitemsEntityListener rpitemsEntityListener;
  static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
  private rpchat Rpchat = null;
  static
  {
     permission = null;
  }

  public boolean isMuted(Player player)
  {
     if (this.isEssentials)
    {
       return this.essentials.getUser(player).isMuted();
    }

     return false;
  }

  public void onDisable()
  {
     PluginDescriptionFile desc = getDescription();
     System.out.println(desc.getFullName() + " has been disabled");
  }

  private Boolean setupPermissions()
  {
     RegisteredServiceProvider permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
     if (permissionProvider != null) {
       permission = (Permission)permissionProvider.getProvider();
    }
     if (permission != null) return Boolean.valueOf(true); return Boolean.valueOf(false);
  }
  
  public boolean hasPerm(Player player, String string)
  {
     return permission.has(player, string);
  }

  public String formatString(String string) {
     String s = string;
     for (ChatColor color : ChatColor.values()) {
       s = s.replaceAll("(&([a-f0-9]))", "§$2");
    }
     return s;
  }

  public void onEnable()
  {
     PluginDescriptionFile desc = getDescription();
     System.out.println(desc.getFullName() + " has been enabled");

     this.pm = getServer().getPluginManager();
     checkPlugins();
     setupPermissions();

     if ((this.towny == null) || (getServer().getScheduler().scheduleSyncDelayedTask(this, new onLoadedTask(this), 1L) == -1))
     {
       System.out.println("SEVERE - Could not schedule onLoadedTask.");
       this.pm.disablePlugin(this);
     }


	 getCommand("queryitem").setExecutor(new QueryItem(this));
	 getCommand("assignitem").setExecutor(new AssignItem(this));
     registerEvents();
  }

  public void registerEvents()
  {
			  this.rpitemsPlayerListener = new RPitemsPlayerListener(this);
			  getServer().getPluginManager().registerEvents(this.rpitemsPlayerListener, this);
			  
			  this.rpitemsEntityListener = new RPitemsEntityListener(this);
			  getServer().getPluginManager().registerEvents(this.rpitemsEntityListener, this);
			  

  }

  public void sendMessageToAll(String message)
  {
     for (World w : getServer().getWorlds())
    {
       for (Player p : w.getPlayers())
      {
         p.sendMessage(message);
      }
    }
  }

  public static String capitalizeFirstLetters(String s)
  {
     for (int i = 0; i < s.length(); i++)
    {
       if (i == 0)
      {
         s = String.format("%s%s", new Object[] { 
           Character.valueOf(Character.toUpperCase(s.charAt(0))), 
           s.substring(1) });
      }

       if ((Character.isLetterOrDigit(s.charAt(i))) || 
         (i + 1 >= s.length())) continue;
       s = String.format("%s%s%s", new Object[] { 
         s.subSequence(0, i + 1), 
         Character.valueOf(Character.toUpperCase(s.charAt(i + 1))), 
         s.substring(i + 2) });
    }

     return s;
  }

  protected Towny getTowny()
  {
     return this.towny;
  }

  boolean onlyletters(String string) {
     return string.matches("^[a-zA-Z]+$");
  }
  boolean onlylettersorspace(String string) {
	 return string.matches("^[a-zA-Z\\s]+$");
  }
  
  private void checkPlugins() {
     Plugin testessentials = getServer().getPluginManager().getPlugin("Essentials");
     if (testessentials == null)
    {
       this.isEssentials = false;
    } else {
       this.isEssentials = true;
       this.essentials = ((Essentials)testessentials);
    }
     
     Plugin Rpchat = this.pm.getPlugin("rpchat");
     if ((Rpchat != null) && ((Rpchat instanceof rpchat)))
       this.Rpchat = ((rpchat)Rpchat);

     Plugin test = this.pm.getPlugin("Towny");
     if ((test != null) && ((test instanceof Towny)))
       this.towny = ((Towny)test);
  }

  public String capitalise(String string)
  {
     if (string == null)
       throw new NullPointerException("string");
     if (string.equals(""))
       throw new NullPointerException("string");
     return Character.toUpperCase(string.charAt(0)) + string.substring(1);
  }


  public static String encode(long number) {
     StringBuilder b = new StringBuilder();
     assert (number > 0L);
    do {
       long div = number / 26L;
       int mod = (int)(number % 26L);
       b.append("abcdefghijklmnopqrstuvwxyz".charAt(mod));
       number = div;
     }while (number != 0L);
     return b.toString();
  }
  
  static int error(String s) {
     throw new RuntimeException(s);
  }
  
  public static long decode(String string) {
     int l = string.length();
     long answer = 0L;
     long mul = 1L;
     for (int i = 0; i < l; i++) {
       int val = string.charAt(i);
       int num = (val >= 65) && (val <= 90) ? val - 65 : (val >= 97) && (val <= 122) ? val - 97 : error("bad format");
       answer += num * mul; mul *= 26L;
    }
     return answer;
  }

  public String getItemName(int itemid) {
		// TODO Auto-generated method stub
	
	if (itemid == 1) {
		return "a faded scroll";
	}
	if (itemid == 2) {
		return "Spell: Translocate: Meropis";
	}
	if (itemid == 3) {
		return "Ability: Tending of the Battle Cleric";
	}
	if (itemid == 4) {
		return "Spell: Ring of the North";
	}
	if (itemid == 5) {
		return "Spell: Ring of the South";
	}
	if (itemid == 6) {
		return "Spell: Ring of the West";
	}
	if (itemid == 7) {
		return "Spell: Conjure Fire";
	}
	if (itemid == 8) {
		return "Spell: Strike of Methabeht";
	}
	if (itemid == 9) {
		return "Spell: Ring of Frost";
	}
	if (itemid == 10) {
		return "Spell: Succor: Meropis";
	}
	
	if (itemid == 11) {
		return "Spell: Translocate: Ash";
	}
	
	if (itemid == 12) {
		return "Spell: Succor: Ash";
	}
	
	// everything else
	return "a faded scroll";
  }
  
  	// Entity Interacts
	public void useItem(int itemid, Player player, Action action) {
		
		if (this.Rpchat.getPlayerPower(player) < 1)
		{
			player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			return;
		}
		
		if (itemid == 1)
		{
			useSpell(player,"effect_none");
		}
		
		if (itemid == 2)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid)) {
				useSpell(player,"teleport_market");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 3)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"healing_light");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 4)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_north");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 5)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_south");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 6)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_west");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 9)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"ddaoe_frost");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 10)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"gteleport_market");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 11)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_ash");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 12)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"gteleport_ash");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
	}
	
	// Entity Interacts
	public void useItem(int itemid, Player player, Entity rightClicked) {
		// TODO Auto-generated method stub
		if (this.Rpchat.getPlayerPower(player) < 1)
		{
			player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			return;
		}
		
		if (itemid == 1)
		{
			useSpell(player,"effect_none");
		}
		
		if (itemid == 2)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_market");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 3)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			
			if (checkPowerToCastAndRemove(player,itemid))		
			{
				useSpellOnEntity(player,"healing_light",rightClicked);
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 4)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{ 
				useSpell(player,"teleport_north");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 5)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_south");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 6)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
			useSpell(player,"teleport_west");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 7)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpellOnEntity(player,"dd_fireball",rightClicked);
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 8)
		{
			if (!isMinNaturallevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{ 
				useSpellOnEntity(player,"dd_lightning",rightClicked);
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		
		if (itemid == 9)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"ddaoe_frost");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		if (itemid == 10)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"gteleport_market");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 11)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"gteleport_ash");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
		
		if (itemid == 12)
		{
			if (!isMinScholarlylevel(player,itemid))
			{
				player.sendMessage("You are not high enough level to use this ability");
				return;
			}
			if (checkPowerToCastAndRemove(player,itemid))
			{
				useSpell(player,"teleport_ash");
			} else {
				player.sendMessage("You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		}
	}
	
	private boolean checkPowerToCastAndRemove(Player player, int itemid) {
		// TODO Auto-generated method stub
		if (this.getSpellPowerCost(itemid) <= this.Rpchat.getPlayerPower(player))
		{
			int newpower = this.Rpchat.getPlayerPower(player) - this.getSpellPowerCost(itemid);
			this.Rpchat.setPlayerPower(player, newpower);
			return true;
		}
		return false;
	}

	private void addSkillExperience(Player player,String type,int amount)
	{
		int maxexperience = this.Rpchat.getMaxExperience();
		int playertotalexperience = this.Rpchat.getTotalSkillExperience(player);
		if (playertotalexperience < maxexperience)
		{
			if (type.equals("combat"))
			{
				int newtotalexp = playertotalexperience + amount;
				if (newtotalexp <= maxexperience)
				{
					int newexp = this.Rpchat.getPlayerCombatExp(player) + amount;
					this.Rpchat.setPlayerCombatExp(player, newexp);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					int overload = newtotalexp - maxexperience;
					int newamount = amount - overload;
					int newexp = this.Rpchat.getPlayerCombatExp(player) + newamount;
					this.Rpchat.setPlayerCombatExp(player, newexp);
				}
			}
			
			if (type.equals("ranged"))
			{
				int newtotalexp = playertotalexperience + amount;
				if (newtotalexp <= maxexperience)
				{
					int newexp = this.Rpchat.getPlayerRangedExp(player) + amount;
					this.Rpchat.setPlayerRangedExp(player, newexp);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					int overload = newtotalexp - maxexperience;
					int newamount = amount - overload;
					int newexp = this.Rpchat.getPlayerRangedExp(player) + newamount;
					this.Rpchat.setPlayerRangedExp(player, newexp);
				}
			}
			
			if (type.equals("scholarly"))
			{
				int newtotalexp = playertotalexperience + amount;
				if (newtotalexp <= maxexperience)
				{
					int newexp = this.Rpchat.getPlayerScholarlyExp(player) + amount;
					this.Rpchat.setPlayerScholarlyExp(player, newexp);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					int overload = newtotalexp - maxexperience;
					int newamount = amount - overload;
					int newexp = this.Rpchat.getPlayerScholarlyExp(player) + newamount;
					this.Rpchat.setPlayerScholarlyExp(player, newexp);
				}
			}
			
			if (type.equals("natural"))
			{
				int newtotalexp = playertotalexperience + amount;
				if (newtotalexp <= maxexperience)
				{
					int newexp = this.Rpchat.getPlayerNaturalExp(player) + amount;
					this.Rpchat.setPlayerNaturalExp(player, newexp);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					int overload = newtotalexp - maxexperience;
					int newamount = amount - overload;
					int newexp = this.Rpchat.getPlayerNaturalExp(player) + newamount;
					this.Rpchat.setPlayerNaturalExp(player, newexp);
				}
			}
		}
	}
	
	private int getPlayerLevel(Player player)
	{
		return this.Rpchat.getPlayerLevel(player);
	}
	
	private boolean isMinlevel(Player player, int itemid) {
		// TODO Auto-generated method stub
		if (player.isOp())
		{
			return true;
		}
		
		int itemlevel = getItemLevel(itemid);
		if (itemlevel <= getPlayerLevel(player))
		{
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isMinCombatlevel(Player player, int itemid) {
		// TODO Auto-generated method stub
		if (player.isOp())
		{
			return true;
		}
		
		int itemlevel = getCombatItemLevel(itemid);
		if (itemlevel <= this.Rpchat.getPlayerCombatLevel(player))
		{
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isMinRangedlevel(Player player, int itemid) {
		// TODO Auto-generated method stub
		if (player.isOp())
		{
			return true;
		}
		
		int itemlevel = getRangedItemLevel(itemid);
		if (itemlevel <= this.Rpchat.getPlayerRangedLevel(player))
		{
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isMinScholarlylevel(Player player, int itemid) {
		// TODO Auto-generated method stub
		if (player.isOp())
		{
			return true;
		}
		
		int itemlevel = getScholarlyItemLevel(itemid);
		if (itemlevel <= this.Rpchat.getScholarlyMagicLevel(player))
		{
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isMinNaturallevel(Player player, int itemid) {
		// TODO Auto-generated method stub
		if (player.isOp())
		{
			return true;
		}
		
		int itemlevel = getNaturalItemLevel(itemid);
		if (itemlevel <= this.Rpchat.getNaturalMagicLevel(player))
		{
			return true;
		} else {
			return false;
		}
	}
	
	public int getCombatItemLevel(int itemid)
	{
		return 1;
	}
	
	public int getRangedItemLevel(int itemid)
	{
		return 1;
	}
	
	public int getScholarlyItemLevel(int itemid)
	{
		if (itemid == 2)
		{
			return 4;
		}
		
		if (itemid == 7)
		{
			return 1;
		}
		if (itemid == 9)
		{
			return 3;
		}
		
		if (itemid == 10)
		{
			return 6;
		}
		
		if (itemid == 11)
		{
			return 7;
		}
		
		if (itemid == 12)
		{
			return 11;
		}
		return 1;
	}
	
	public int getNaturalItemLevel(int itemid)
	{
		if (itemid == 3)
		{
			return 3;
		}
		if (itemid == 4)
		{
			return 10;
		}
		if (itemid == 5)
		{
			return 10;
		}
		if (itemid == 6)
		{
			return 10;
		}
		if (itemid == 8)
		{
			return 1;
		}
		
		return 1;
	}
	


	private void useSpell(Player player, String ability) {
		// TODO Auto-generated method stub
		if (ability.equals("effect_none"))
		{
			player.sendMessage("This appears to have no use");
			
		}
		
		if (ability.equals("teleport_market"))
		{
			player.sendMessage("Speaking the incantations on the scroll opens a portal in front of you");
			Location loc = new Location(this.getServer().getWorld("world"), -251,71,-113);
			this.addSkillExperience(player, "scholarly", 40);
			player.teleport(loc);
		}
		
		if (ability.equals("teleport_ash"))
		{
			player.sendMessage("Speaking the incantations on the scroll opens a portal in front of you");
			Location loc = new Location(this.getServer().getWorld("world"), 1399,74,214);
			this.addSkillExperience(player, "scholarly", 40);
			player.teleport(loc);
		}
		
		if (ability.equals("healing_light"))
		{
			if (player.getHealth() < (player.getMaxHealth() - 2))
			{
				player.sendMessage("You feel relief as you tend to your own wounds");
				player.setHealth(player.getHealth() + 2);
				this.addSkillExperience(player, "natural", 40);
			} else {
				player.sendMessage("Your attempt to heal makes no difference");
			}
		}
		
		if (ability.equals("gteleport_market"))
		{
			player.sendMessage("Speaking the incantations on the scroll opens a portal in front of your group");
			
			Location loc = new Location(this.getServer().getWorld("world"), -251,71,-113);
			this.addSkillExperience(player, "scholarly", 40);
			
			
			List<Player> pes = this.getPlayersNearPlayer(player);
			for (Player p : pes)
			{
				p.sendMessage("You are ripped away by the portal");
				p.teleport(loc);
			}
			
			player.teleport(loc);
		}
		
		if (ability.equals("gteleport_ash"))
		{
			player.sendMessage("Speaking the incantations on the scroll opens a portal in front of your group");
			
			Location loc = new Location(this.getServer().getWorld("world"), 1399,74,214);
			this.addSkillExperience(player, "scholarly", 40);
			
			
			List<Player> pes = this.getPlayersNearPlayer(player);
			for (Player p : pes)
			{
				p.sendMessage("You are ripped away by the portal");
				p.teleport(loc);
			}
			
			player.teleport(loc);
		}
		

		
		if (ability.equals("teleport_west"))
		{
			player.sendMessage("You speak the ancient druidic prayer to Methabeht");
			Location loc = new Location(this.getServer().getWorld("world"), -2004.00, 77.00, 170.00);
			this.addSkillExperience(player, "natural", 40);
			player.teleport(loc);
		}
		
		if (ability.equals("teleport_north"))
		{
			player.sendMessage("You speak the ancient druidic prayer to Methabeht");
			Location loc = new Location(this.getServer().getWorld("world"), 742.00, 64.00, -1382.00);
			this.addSkillExperience(player, "natural", 40);
			player.teleport(loc);
		}
		if (ability.equals("teleport_south"))
		{
			player.sendMessage("You speak the ancient druidic prayer to Methabeht");
			Location loc = new Location(this.getServer().getWorld("world"), 1259.00, 79.00, 2029.00);
			this.addSkillExperience(player, "natural", 40);
			player.teleport(loc);
		}
		
		if (ability.equals("ddaoe_frost"))
		{
			for (LivingEntity entity : this.getEntitiesNearPlayer(player))
			{
				player.sendMessage("You summon a ring of deadly frost at your location!");
				if (entity instanceof Player)
				{
					Player p = (Player)entity;
					if (isPlayerInPVPArea(p))
					{
						
						p.sendMessage("You are struck by frozen ice!");
						p.damage(2,player);
						this.addSkillExperience(player, "scholarly", 40);
					} else {
						player.sendMessage("This area is pvp protected");
					}
				} else {
					if (entity instanceof LivingEntity)
					{
						
						LivingEntity m = (LivingEntity)entity;
						m.damage(10,player);
						this.addSkillExperience(player, "scholarly", 40);
					}
				}
			}
		}
	}
	
	private List<Player> getPlayersNearPlayer(Player player) {
		// TODO Auto-generated method stub
		List<Player> allle = player.getWorld().getPlayers();
		List<Player> les = new ArrayList<Player>();
		
		double x2 = player.getLocation().getX();
        double y2 = player.getLocation().getY();
        double z2 = player.getLocation().getZ();
		
		for (Player le : allle)
		{
			// exclude bots
			if (!this.Rpchat.getPlayerAlliance(le).equals(""))
			{
			 double x1 = le.getLocation().getX();
	         double y1 = le.getLocation().getY();
	         double z1 = le.getLocation().getZ();
	 
	         int xdist = (int)(x1 - x2);
	         int ydist = (int)(y1 - y2);
	         int zdist = (int)(z1 - z2);
	 
	         if ((xdist < -5) || (xdist > 5) || (ydist < -5) || (ydist > 5) || (zdist < -5) || (zdist > 5))
	         {
	           continue;
	         }
	         les.add(le);
			}
		}
		return les;
	}
	
	private List<LivingEntity> getEntitiesNearPlayer(Player player) {
		// TODO Auto-generated method stub
		List<LivingEntity> allle = player.getWorld().getLivingEntities();
		List<LivingEntity> les = new ArrayList<LivingEntity>();
		
		double x2 = player.getLocation().getX();
        double y2 = player.getLocation().getY();
        double z2 = player.getLocation().getZ();
		
		for (LivingEntity le : allle)
		{
			 double x1 = le.getLocation().getX();
	         double y1 = le.getLocation().getY();
	         double z1 = le.getLocation().getZ();
	 
	         int xdist = (int)(x1 - x2);
	         int ydist = (int)(y1 - y2);
	         int zdist = (int)(z1 - z2);
	 
	         if ((xdist < -5) || (xdist > 5) || (ydist < -5) || (ydist > 5) || (zdist < -5) || (zdist > 5))
	         {
	           continue;
	         }
	         les.add(le);
		}
		return les;
	}

	private boolean isPlayerInPVPArea(Player player)
	{
		return this.Rpchat.isPlayerInPVPArea(player);
	}
	
	private void useSpellOnEntity(Player player, String ability, Entity entity) {
		// TODO Auto-generated method stub
		
		if (ability.equals("healing_light"))
		{
			if (entity instanceof Player)
			{
				Player p = (Player)entity;
				
				if (p.getHealth() < (p.getMaxHealth() - 2))
				{
					p.sendMessage("You feel relief as your wounds are tended to");
					p.setHealth(p.getHealth() + 2);
					this.addSkillExperience(player, "natural", 40);
				} else {
					player.sendMessage("Your attempt to heal makes no difference");
				}
			}
		}
		
		if (ability.equals("dd_fireball"))
		{
			if (entity instanceof Player)
			{
				Player p = (Player)entity;
				if (isPlayerInPVPArea(p))
				{
					player.sendMessage("You struck your target with lashing flames!");
					p.sendMessage("You are struck by lashing flames!");
					System.out.println("Target max health was: "+ p.getMaxHealth());
					p.damage(2,player);
					this.addSkillExperience(player, "scholarly", 40);
				} else {
					player.sendMessage("This area is pvp protected");
				}
			} else {
				if (entity instanceof LivingEntity)
				{
					player.sendMessage("You struck your target with lashing flames!");
					LivingEntity m = (LivingEntity)entity;
					m.damage(10,player);
					this.addSkillExperience(player, "scholarly", 40);
				}
			}
		}
		
		if (ability.equals("dd_lightning"))
		{
			if (entity instanceof Player)
			{
				Player p = (Player)entity;
				
				if (isPlayerInPVPArea(p))
				{
					player.sendMessage("Your target is struck by the gods!");
					p.sendMessage("You are struck by the gods!");
					p.damage(2,player);
					this.addSkillExperience(player, "natural", 40);
				} else {
					player.sendMessage("This area is pvp protected");
				}
			} else {
				if (entity instanceof LivingEntity)
				{
					player.sendMessage("Your target is struck by the gods!");
					LivingEntity m = (LivingEntity)entity;
					m.damage(10,player);
					this.addSkillExperience(player, "natural", 40);
					
				
				}
			}
		}
	}

	public int RandomNumber(int start, int end)
	{
		Random rand = new Random();
		return rand.nextInt(end)+start;
	}

	public ItemStack getRandomAbilityScroll() {
		// TODO Auto-generated method stub
		
		ItemStack is = new ItemStack(Material.PAPER);
		is.setAmount(1);
		
		int itemid = RandomNumber(1,12);
		
		is.addUnsafeEnchantment(Enchantment.OXYGEN, itemid);
		return is;
	}
	
	public int getSpellPowerCost(int itemid)
	{
		if (itemid == 2)
		{
			return 50;
		}
		if (itemid == 3)
		{
			return 25;
		}
		if (itemid == 4)
		{
			return 50;
		}
		if (itemid == 5)
		{
			return 50;
		}
		if (itemid == 6)
		{
			return 50;
		}
		if (itemid == 7)
		{
			return 25;
		}
		if (itemid == 8)
		{
			return 25;
		}
		if (itemid == 9)
		{
			return 75;
		}
		
		if (itemid == 10)
		{
			return 75;
		}
		if (itemid == 11)
		{
			return 50;
		}
		if (itemid == 12)
		{
			return 100;
		}
		return 1;
	}

	public int getItemLevel(int itemid) {
		// TODO Auto-generated method stub

		return 1;
	}

	public String getItemAlliances(int itemid) {
		// TODO Auto-generated method stub
		return "ALL";
	}

	public String getItemRaces(int itemid) {
		// TODO Auto-generated method stub
		
		
		return "ALL";
	}

	public String getItemLore(int itemid) {
		// TODO Auto-generated method stub
		if (itemid == 1)
		{
			return "This ancient scroll seems faded and no longer of use to anyone";
		}
		if (itemid == 2)
		{
			return "Magically bound to a location during the construction of the Trident Vault";
		}
		if (itemid == 3)
		{
			return "Upon the page you see various healing techniques of ancient battle clerics";
		}
		
		if (itemid == 4)
		{
			return "An ancient page of a druidic prayer book that appears to sparkle with alteration magic";
		}
		if (itemid == 5)
		{
			return "An ancient page of a druidic prayer book that appears to sparkle with alteration magic";
		}
		if (itemid == 6)
		{
			return "An ancient page of a druidic prayer book that appears to sparkle with alteration magic";
		}
		if (itemid == 7)
		{
			return "Conjuring fire was one of the first abilities taught to the magicians of New Tsiyon";
		}
		if (itemid == 8)
		{
			return "I swear to you, Lord Methabeht, smite my enemy and I shall forever give myself in service. - Darakua";
		}
		if (itemid == 9)
		{
			return "An old elemental school page detailing the art of summoning perfect circles of frost";
		}
		
		if (itemid == 10)
		{
			return "Magically bound to a location during the construction of the Trident Vault";
		}
		
		if (itemid == 11)
		{
			return "The parchment seems burned but you make out what appears to be storm runes";
		}
		if (itemid == 12)
		{
			return "The parchment seems burned but you make out what appears to be storm runes";
		}

		return "You see nothing particularly special about this object";
	}


}
