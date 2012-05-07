package net.gamerservices.rpitems;

import com.earth2me.essentials.Essentials;
import com.palmergames.bukkit.towny.Towny;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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
  static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

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
     setupDatabase();

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

  private void setupDatabase()
  {
    try
    {
       getDatabase().find(sqlAbility.class).findRowCount();
       getDatabase().find(sqlItem.class).findRowCount();
    }
    catch (PersistenceException ex) {
       System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
       removeDDL();
       installDDL();
    }
  }


  public List<Class<?>> getDatabaseClasses()
  {
     List list = new ArrayList();
     list.add(sqlAbility.class);
     list.add(sqlItem.class);
     
     return list;
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
	sqlItem sItem = (sqlItem)this.getDatabase().find(sqlItem.class).where().ieq("id", Integer.toString(itemid)).findUnique();
	if (sItem == null) {
		return "an item";
	} else {
		return sItem.getName();
	}
  }

	public void useItem(int itemid, Player player) {
		// TODO Auto-generated method stub
		sqlItem sItem = (sqlItem)this.getDatabase().find(sqlItem.class).where().ieq("id", Integer.toString(itemid)).findUnique();
		if (sItem == null) {
			player.sendMessage("There is no use for this");
		} else {
			player.sendMessage(sItem.getUsemessage());
		}
	}
}
