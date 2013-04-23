package net.gamerservices.rpitems;

import com.earth2me.essentials.Essentials;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.PersistenceException;

import me.desmin88.mobdisguise.api.MobDisguiseAPI;
import net.gamerservices.rpchat.rpchat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
public class rpitems extends JavaPlugin
{
	private PluginManager pm;
	public static Permission permission;
	private Essentials essentials = null;
	public boolean isEssentials = false;
	private Listener rpitemsPlayerListener;
	private RPitemsEntityListener rpitemsEntityListener;
	static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public rpchat Rpchat = null;
	private List<Buff> buffedplayers = new ArrayList<Buff>();
	private List<Detrimental> detrimentaledentities = new ArrayList<Detrimental>();
	public List<Player> kittedplayers = new ArrayList<Player>();
	private List<Player> glidingplayers = new ArrayList<Player>();
	private boolean isMobdisguise;
	private MobDisguiseAPI mobdisguise;
	private List<Ability> abilities = new ArrayList<Ability>();
	private List<ColHeader> columnnames = new ArrayList();
	public String customore = "talonite";
	public int customeffect = 1;
	public List<Metal> metals = new ArrayList();
	public ShapedRecipe recipeEnhancedSword;
	
	static
	{
		permission = null;
	}
	
	public List<Interact> interacting = new ArrayList<Interact>();
	private RPitemsBlockListener rpitemsBlockListener;
	private RPitemsCraftListener rpitemsCraftListener;
	
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

		getCommand("queryitem").setExecutor(new QueryItem(this));
		getCommand("reloadabilities").setExecutor(new ReloadAbilities(this));
		getCommand("assignitem").setExecutor(new AssignItem(this));
		getCommand("convertspell").setExecutor(new ConvertSpell(this));
		getCommand("vaccept").setExecutor(new VAcceptCmd(this));
		getCommand("spellkit").setExecutor(new SpellKitCommand(this));

		loadAbilities();
		loadMetals();
		registerEvents();


		getServer().getScheduler().scheduleSyncDelayedTask(this, new BuffUpdate(), 10L);
		getServer().getScheduler().scheduleSyncDelayedTask(this, new RewardQueue(), 10L);
		getServer().getScheduler().scheduleSyncDelayedTask(this, new DetrimentalUpdate(), 10L);
	}

	public void registerEvents()
	{
		this.rpitemsPlayerListener = new RPitemsPlayerListener(this);
		getServer().getPluginManager().registerEvents(this.rpitemsPlayerListener, this);

		this.rpitemsEntityListener = new RPitemsEntityListener(this);
		getServer().getPluginManager().registerEvents(this.rpitemsEntityListener, this);
		
		this.rpitemsBlockListener = new RPitemsBlockListener(this);
		getServer().getPluginManager().registerEvents(this.rpitemsBlockListener, this);
		
		this.rpitemsCraftListener = new RPitemsCraftListener(this);
		getServer().getPluginManager().registerEvents(this.rpitemsCraftListener, this);
		
	}
	
	public void loadMetals()
	{
		Metal metal = new Metal();
		metal.name = "Adamantine";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Amazonium";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Argonite";
		metal.value = 13;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Banite";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Austrium";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Bombastium";
		metal.value = 4;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Bathusium";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Cobalt";
		metal.value = 7;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Brightsteel";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Corbonium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Cavorite";
		metal.value = 1;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Darksteel";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Cuendillar";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Ebony";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Eitr";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Fractite";
		metal.value = 13;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Elementium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Infinium";
		metal.value = 19;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Gorgonite";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Kratonite";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Katagonium";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Melangium";
		metal.value = 12;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Latinum";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Morphite";
		metal.value = 17;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Mithril";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Obdurium";
		metal.value = 15;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Novite";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Octogen";
		metal.value = 12;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Obsidium";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Omnesium";
		metal.value = 8;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Omnium";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Pergium";
		metal.value = 1;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Oxium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Parium";
		metal.value = 2;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Phazite";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Photonium";
		metal.value = 3;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Philotium";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Polarite";
		metal.value = 4;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Pizzazium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Promethium";
		metal.value = 19;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Primium";
		metal.value = 16;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Psitanium";
		metal.value = 17;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Protonite";
		metal.value = 17;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Quantium";
		metal.value = 18;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Pyerite";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Relux";
		metal.value = 15;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Radanium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Rovolon";
		metal.value = 11;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Residuum";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Ryanium";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Runite";
		metal.value = 9;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Sheol";
		metal.value = 6;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Saronite";
		metal.value = 14;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Sivanium";
		metal.value = 8;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Sinisite";
		metal.value = 16;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Solonite";
		metal.value = 17;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Solarite";
		metal.value = 19;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Stravidium";
		metal.value = 10;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Soulsteel";
		metal.value = 11;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Synthium";
		metal.value = 11;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Stormphrax";
		metal.value = 14;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Thaesium";
		metal.value = 23;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tarydium";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Thyrium";
		metal.value = 1;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Thorium";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Timonium";
		metal.value = 14;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tibanna";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Triidium";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Titanite";
		metal.value = 17;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Tronium";
		metal.value = 2;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tritanium";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Turbidium";
		metal.value = 1;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tungite";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Valyrium";
		metal.value = 7;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Uridium";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Vionesium";
		metal.value = 7;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Veridium";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Vizorium";
		metal.value = 10;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Viridium";
		metal.value = 9;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Xenium";
		metal.value = 9;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Voltairium";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Xithricite";
		metal.value = 12;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Xirdalium";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Zanium";
		metal.value = 3;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Yuanon";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Zoridium";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Zexonite";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Solinium";
		metal.value = 20;
		this.metals.add(metal);
		
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(this.metals.size());
        Metal chosenmetal = this.metals.get(index);
        this.customore = chosenmetal.name;
        this.customeffect = chosenmetal.value;
        System.out.println("[RPItems] Randomised metal for today is: " + chosenmetal.name + " (bonus: " + chosenmetal.value + ")");
        
        
        Bukkit.addRecipe(recipeEnhancedSword = new ShapedRecipe(new ItemStack(Material.DIAMOND_SWORD, 1)).shape("DDD","DSD","DDD").setIngredient('D', Material.DIAMOND).setIngredient('S', Material.DIAMOND_SWORD));
	}
	
	
	
	public void onSentryDeath(Player victim, Player attacker)
	{
		// always drop something
		if (victim.getName().contains("Raid"))
		{
			getServer().broadcastMessage(ChatColor.RED + "The world stands silent as "+victim.getName()+" has been defeated by " + attacker.getName());
		}
		droporgiveRandomAbilityScrollBySentry(victim.getName(),victim.getLocation(),attacker);
		
	}
	
	public void onSentryChase(Player victim, Player attacker)
	{
		int index = RandomNumber(1,10);
		if (index > 9)
		{
			attacker.sendMessage(victim.getName()+(" says 'You will not evade me!'"));
			attacker.teleport(victim.getLocation());
		}
		
	}
	
	public void onSentryHurt(Player victim, Player attacker)
	{
		int index = RandomNumber(1,10);
		if (index > 9)
		{
			attacker.sendMessage(victim.getName()+(" says 'You will not evade me!'"));
			attacker.teleport(victim.getLocation());
		}
		
	}

	public String getColumnByID(int id)
	{
		for (ColHeader col : this.columnnames)
		{
			if (col.id == id)
			{
				return col.column;
			}
		}
		return null;
	}

	public void loadAbilities()
	{
		System.out.println("[RPItems] Loading abilities...");
		int loadedabilities = 0;
		try
		{
			//File file = new File("abilities.tsv");
			
			// Now pulled automatically from the site
			
			BufferedReader bufRdr;
			String filepath = this.getDataFolder().getAbsoluteFile()+"/abilities.tsv";
			System.out.println("Trying local cache: " + filepath);
			FileReader file = new FileReader(filepath);
			
			bufRdr = new BufferedReader (file);
			//BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
			String line = null;
			int row = 0;
			int col = 0;
			this.columnnames.clear();
			this.abilities.clear();

			while((line = bufRdr.readLine()) != null)
			{
				Ability ability = new Ability();
				String dataArray[] = line.split("\\t");
				for (String columndata : dataArray)
				{
					if (row == 0)
					{
						ColHeader column = new ColHeader();
						column.id = col;
						column.column = columndata;
						columnnames.add(column);
					} else {
						String thiscolheader = getColumnByID(col);

						if (thiscolheader.equals("id"))
						{
							if (!columndata.isEmpty())
								ability.id = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("name"))
						{
							ability.name = columndata;
						}
						if (thiscolheader.equals("lore"))
						{
							ability.lore = columndata;
						}
						if (thiscolheader.equals("description"))
						{
							ability.description = columndata;
						}
						if (thiscolheader.equals("effectvalue"))
						{
							ability.effectvalue = columndata;
						}
						if (thiscolheader.equals("effect"))
						{
							ability.effect = columndata;
						}
						if (thiscolheader.equals("buff"))
						{
							ability.buff = columndata;
						}
						if (thiscolheader.equals("buffduration"))
						{
							if (!columndata.isEmpty())
								ability.buffduration = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("skill"))
						{
							ability.skill = columndata;
						}
						
						if (thiscolheader.equals("skillexp"))
						{
							if (!columndata.isEmpty())
								ability.skillexp = Integer.parseInt(columndata);
						}
						
						if (thiscolheader.equals("tpx"))
						{
							if (!columndata.isEmpty())
								ability.tpx = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("tpy"))
						{
							if (!columndata.isEmpty())
								ability.tpy = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("tpz"))
						{
							if (!columndata.isEmpty())
								ability.tpz = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("powercost"))
						{
							if (!columndata.isEmpty())
								ability.powercost = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("level"))
						{
							if (!columndata.isEmpty())
								ability.level = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("races"))
						{
							ability.races = columndata;
						}
						if (!columndata.isEmpty())

							if (thiscolheader.equals("scholarlylevel"))
							{
								if (!columndata.isEmpty())
									ability.scholarlylevel = Integer.parseInt(columndata);
							}
						if (thiscolheader.equals("naturallevel"))
						{
							if (!columndata.isEmpty())
								ability.naturallevel = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("combatlevel"))
						{
							if (!columndata.isEmpty())
								ability.combatlevel = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("rangedlevel"))
						{
							if (!columndata.isEmpty())
								ability.rangedlevel = Integer.parseInt(columndata);
						}
						if (thiscolheader.equals("onCastText"))
						{
							ability.onCastText = columndata;
						}
						if (thiscolheader.equals("onCastEndText"))
						{
							ability.onCastEndText = columndata;
						}
						if (thiscolheader.equals("onCastTargetText"))
						{
							ability.onCastTargetText = columndata;
						}
						if (thiscolheader.equals("onCastTargetFail"))
						{
							ability.onCastTargetFail = columndata;
						}
						if (thiscolheader.equals("onBuffStartText"))
						{
							ability.onBuffStartText = columndata;
						}
						if (thiscolheader.equals("onCastPlayerSay"))
						{
							ability.onCastPlayerSay = columndata;
						}
						if (thiscolheader.equals("specialeffect"))
						{
							ability.specialeffect = columndata;
						}
						if (thiscolheader.equals("specialeffectvalue"))
						{
							ability.specialeffectvalue = columndata;
						}
						if (thiscolheader.equals("specialeffecttarget"))
						{
							ability.specialeffecttarget = columndata;
						}
						if (thiscolheader.equals("specialeffecttargetvalue"))
						{
							ability.specialeffecttargetvalue = columndata;
						}
						if (thiscolheader.equals("loottables"))
						{
							ability.loottables = columndata;
						}
						
						if (thiscolheader.equals("region"))
						{
							ability.region = columndata;
						}
						if (thiscolheader.equals("requirestarget"))
						{
							ability.requirestarget = columndata;
						}
					}
					col++;
				}
				col = 0;
				if (row != 0)
				{
					// Check if it's missing an ID or Name if so skip it
					if (ability.name.equals("") || ability.id == 0)
					{
						//System.out.println("Invalid Ability Skipped (Missing ID or Name");
					} else {
						//System.out.println("Loaded ability: " + ability.id + ": " + ability.name);
						this.abilities.add(ability);
						loadedabilities++;
					}
				}
				row++;
			}
			bufRdr.close();
		} catch (Exception e)
		{
			System.out.println("RPItems Exception (loadAbilities): " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("[RPItems] Loaded " + loadedabilities + " abilities");


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

	public boolean isDisguised(Player player)
	{
		return this.mobdisguise.isDisguised(player);
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

	public Ability getAbilityByID(int id)
	{
		for (Ability ability : this.abilities)
		{
			if (ability.id == id)
				return ability;
		}
		return null;
	}

	public String getItemName(int itemid) {
		// TODO Auto-generated method stub
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.name;
		} else {
			return "a faded scroll";
		}
	}
	
	public List<LivingEntity> getLivingEntitiesInCone(Player player)
    {
		List<LivingEntity> le = new ArrayList<LivingEntity>();
		for (Entity e : player.getNearbyEntities(200, 200, 200))
		{
			if (e instanceof LivingEntity)
			{
				if(isEntityInLineOfSight(player,e))
				{
					le.add((LivingEntity)e);
				}
			}
		}
		return le;
    }
   
	public boolean isEntityInLineOfSight(Player player, Entity entity)
	{
		if (entity instanceof LivingEntity)
		{
			entity = (LivingEntity) entity;
			double x = player.getLocation().toVector().distance(entity.getLocation().toVector());
			Vector direction = player.getLocation().getDirection().multiply(x);
			Vector answer = direction.add(player.getLocation().toVector());
			if(answer.distance(entity.getLocation().toVector()) < 1.37){
				if (player.hasLineOfSight(entity))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	//useItem on Self
	public void useItem(int itemid, Player player, Block block) {
		if (player.isFlying())
		{
			player.sendMessage(ChatColor.GRAY+"You cannot use abilities while flying");
			return;
		}
		
		
		if (this.Rpchat.getPlayerPower(player) < 1)
		{
			player.sendMessage(ChatColor.GRAY+"You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			return;
		}
		Ability ability = this.getAbilityByID(itemid);
		if (ability == null)
		{
			// Did not find ability
			player.sendMessage(ChatColor.GRAY+"This appears to have no use");
			return;
		}
		if (!this.getItemRaces(itemid).equals("ALL"))
		{
			if (!isPlayerOneOfRace(this.getItemRaces(itemid),player))
			{
				player.sendMessage(ChatColor.GRAY+"This ability seems to be something your race is not capable of doing");
				return;
			}
		}
		if (hasValidSkill(player,itemid))
		{
			// has the skill necessary
			if (checkPowerToCastAndRemove(player,itemid)) {
				
				// check if the ability does not require a target
				if (ability.requirestarget.equals("no"))
				{
					// doesnt need a target, lets use this on ourselves
					useAbilityOnSelf(player,itemid);
					// dispatched to useAbilityOnSelf, return
					return;
				} else {
					if (ability.requirestarget.equals("yes"))
					{
						List<LivingEntity> le = getLivingEntitiesInCone(player);
						if (le.size()> 0)
						{
							for (LivingEntity entity : le)
							{
								this.useAbilityOnEntity(player, itemid, entity);
								
								// just the first please
								return;
							}
						}
						// didnt find anyhing refund it
						refundPower(player, itemid);
						//too spammy
						//player.sendMessage(ChatColor.GRAY+"Your target is not completely visible");
						return;
					}
					
					if (ability.requirestarget.equals("both"))
					{
						List<LivingEntity> le = getLivingEntitiesInCone(player);
						if (le.size()> 0)
						{
							for (LivingEntity entity : le)
							{
								this.useAbilityOnEntity(player, itemid, entity);
								
								// just the first please
								return;
							}
						}
						// didnt find entity - use it on the player
						useAbilityOnSelf(player,itemid);
						return;
						
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY+"You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		} else {
			player.sendMessage(ChatColor.GRAY+"You are not high enough level to use this ability");
			return;
		}
	}

	public boolean isPlayerInTownCalled(Player player, String town)
	{
		return this.Rpchat.isPlayerInTownCalled(player, town);
	}
	
	
	public boolean isPlayerInOtherPlayersTown(Player player)
	{
		return this.Rpchat.isPlayerInOtherPlayersTown(player);
	}
	//useItem on Entity

	public void useItem(int itemid, Player player, Entity rightClicked) {
		if (this.Rpchat.getPlayerPower(player) < 1)
		{
			player.sendMessage(ChatColor.GRAY+"You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			return;
		}
		Ability ability = this.getAbilityByID(itemid);
		if (ability == null)
		{
			// Did not find ability
			player.sendMessage(ChatColor.GRAY+"This appears to have no use");
			return;
		}
		if (!this.getItemRaces(itemid).equals("ALL"))
		{
			if (!isPlayerOneOfRace(this.getItemRaces(itemid),player))
			{
				player.sendMessage(ChatColor.GRAY+"This ability seems to be something your race is not capable of doing");
				return;
			}
		}
		if (hasValidSkill(player,itemid))
		{
			// has the skill necessary
			if (checkPowerToCastAndRemove(player,itemid)) {
				useAbilityOnEntity(player,itemid,rightClicked);
			} else {
				player.sendMessage(ChatColor.GRAY+"You do not have enough power ("+this.Rpchat.getPlayerPower(player)+"/100) Meditate with /meditate");
			}
		} else {
			player.sendMessage(ChatColor.GRAY+"You are not high enough level to use this ability");
			return;
		}
	}
	
	private void useAbilityOnSelf(Player player, int itemid)
	{
		Ability ability = this.getAbilityByID(itemid);
		boolean hasCast = false;
		
		if (ability == null)
		{
			// Did not find ability
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"This appears to have no use");
			return;
		}
		
		if (ability.effect.equals("effect_none"))
		{
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"This appears to have no use");
			return;
		}
		
		if (ability.requirestarget.equals("yes"))
		{
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"This ability requires a target");
			return;
		}
		
		
		// do they have this buff already?
		if (ability.effect.equals("buff") && isBuffed(player,ability.buff))
		{
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"The buff has no effect as an existing buff that shares the same properties is already active");
			return;
		}
		
		// do they have this buff already?
		if (ability.effect.equals("detrimental") && isDetrimentaled(player,ability.buff))
		{
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"The detrimental has no effect as an existing detrimental that shares the same properties is already active");
			return;
		}
		
		// Send Special Effect
		sendSpecialEffect(player,ability.specialeffect,ability.specialeffectvalue);
		
		// Send onCastText
		if (!ability.onCastText.equals(""))
		{
			player.sendMessage(ChatColor.GRAY+ability.onCastText);
		}
		
		// Check Effects
		
		//SELF TELEPORT
		if (ability.effect.equals("teleport"))
		{
			Location loc = new Location(this.getServer().getWorld("world"), ability.tpx,ability.tpy,ability.tpz);
			this.Rpchat.teleport(player, loc);
			hasCast=true;
		}
		
		//SELF TELEPORT
		if (ability.effect.equals("gate"))
		{
			if (player.getBedSpawnLocation() != null)
			{
				Location loc = player.getBedSpawnLocation();
				this.Rpchat.teleport(player, loc);
				hasCast=true;
			} else {
				player.sendMessage(ChatColor.GRAY+"* Your bed is not set to send you home");
			}
		}
		
		//GROUP TELEPORT
		if (ability.effect.equals("groupteleport"))
		{
			Location loc = new Location(this.getServer().getWorld("world"), ability.tpx,ability.tpy,ability.tpz);
			List<Player> pes = this.getPlayersNearPlayer(player);
			for (Player p : pes)
			{
				
				if (this.Rpchat.isPlayerInPlayersGroup(player, p))
				{
					// Send Special Effect
					sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
					p.sendMessage(ChatColor.GRAY+"You were teleported by " + player.getName());
					//teleport target
					if (!ability.onCastTargetText.equals(""))
					{
						p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
					}
					this.Rpchat.teleport(p, loc);
				} else {
					player.sendMessage(ChatColor.GRAY+p.getName()+" could not be teleported because they are not in your group");
				}
			}
			// and player
			this.Rpchat.teleport(player, loc);
			hasCast=true;
		}
		
		if (ability.effect.equals("weather"))
		{
			player.getWorld().setStorm(false);
			hasCast=true;
		}
		
		//SELF HEALING
		if (ability.effect.equals("heal"))
		{
			// check heal amount
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				int healamount = Integer.parseInt(ability.effectvalue);
				if (player.getHealth() < (player.getMaxHealth()))
				{
					
					int amount = player.getHealth() + healamount;
					if (amount > player.getMaxHealth())
					{
						amount = player.getMaxHealth();
					}
					player.setHealth(amount);
					this.onSentryPlayerHealed(player, player);
					
					if (!ability.onCastTargetText.equals(""))
					{
						player.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
					}
					sendSpecialEffect(player,ability.specialeffecttarget,ability.specialeffecttargetvalue);
					hasCast=true;
					
				}
			} else {
				player.sendMessage(ChatColor.GRAY+"The heal is insignificant (heals for 0)");
			}
			
		}
		
		if (ability.effect.equals("shieldheal"))
		{
			// check heal amount
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				int healamount = Integer.parseInt(ability.effectvalue);
				if (this.Rpchat.getPlayerHPBonus(player) < this.Rpchat.getPlayerHPBonusMax(player))
				{
					
					int amount = this.Rpchat.getPlayerHPBonus(player) + healamount;
					if (amount > this.Rpchat.getPlayerHPBonusMax(player))
					{
						amount = this.Rpchat.getPlayerHPBonusMax(player);
					}
					this.Rpchat.setPlayerHPBonus(player,amount);
					this.onSentryPlayerHealed(player, player);
					
					if (!ability.onCastTargetText.equals(""))
					{
						player.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
					}
					sendSpecialEffect(player,ability.specialeffecttarget,ability.specialeffecttargetvalue);
					hasCast=true;
					
				}
			} else {
				player.sendMessage(ChatColor.GRAY+"The heal is insignificant (heals for 0)");
			}
			
		}
		
		
		
		
		
		if (ability.effect.equals("item"))
		{
			// check heal amount
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				int item_id = Integer.parseInt(ability.effectvalue);
				ItemStack stack = new ItemStack(item_id, 5);
				player.getWorld().dropItemNaturally(player.getLocation(),stack);
				hasCast=true;
			}
			
		}
		
		if (ability.effect.equals("blink"))
		{
			int distance = 5;
			try 
			{
				distance = Integer.parseInt(ability.effectvalue);
				
			} catch (Exception e)
			{
				System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a blink spell but has no effectvalue set!");
			}
			try
			{
				
				Block block = player.getTargetBlock(null, distance);
				// find an airblock around this
				Location newloc = block.getLocation();
				newloc.setPitch(player.getLocation().getPitch());
				newloc.setYaw(player.getLocation().getYaw());
				newloc.setY(newloc.getY() + 1);
				while (!newloc.getBlock().getType().equals(Material.AIR) && newloc.getY() < 129)
				{
					newloc.setY(newloc.getY() + 1);
				}
				
				
				if (newloc.getBlock().getType().equals(Material.AIR))
				{
					player.teleport(newloc);
					hasCast=true;
				} 
			} catch (Exception e)
			{
				// failed to find a block
			}
		}
		
		// AOE HEALING
		if (ability.effect.equals("aoeheal"))
		{
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				int healamount = Integer.parseInt(ability.effectvalue);
				for (LivingEntity entity : this.getEntitiesNearPlayer(player))
				{
					if (entity instanceof Player)
					{
						Player p = (Player)entity;
						
						if (p.getHealth() < (p.getMaxHealth()))
						{
							
							int amount = p.getHealth() + healamount;
							if (amount > p.getMaxHealth())
							{
								amount = p.getMaxHealth();
							}
							p.setHealth(amount);
							this.onSentryPlayerHealed(player, p);
							
							if (!ability.onCastTargetText.equals(""))
							{
								p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
							}
							sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
							hasCast=true;
							
						
						
						}
					}
				}
			}
			
		}
		
		// POINT BLANK AOE DD
		if (ability.effect.equals("aoedmg"))
		{
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				int damageamount = Integer.parseInt(ability.effectvalue);
				for (LivingEntity entity : this.getEntitiesNearPlayer(player))
				{
					if (entity instanceof Player)
					{
						Player p = (Player)entity;
						if (p != player)
						{
							if (isPlayerSentry(p))
							{
								if (!ability.onCastTargetText.equals(""))
								{
									p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
								}
								p.damage(damageamount,player);
								sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
								hasCast=true;
							} else {
								// no need for aoe spam	
								//player.sendMessage(ChatColor.GRAY+"Hostile Spells and Abilities only function on NPCs");
							}
						}
					} else {
						if (entity instanceof Monster)
						{
							LivingEntity m = (LivingEntity)entity;
							m.damage(damageamount,player);
							sendSpecialEffect(m,ability.specialeffecttarget,ability.specialeffecttargetvalue);
							hasCast=true;
						}
					}
				}
			}
			
		}
			
		// Check Buffs
		if (ability.effect.equals("buff") && ability.buffduration > 0 && !ability.buff.equals(""))
		{
			// self only expbonus
			
			if (ability.buff.equals("expbonus"))
			{
				this.addBuff(player, ability.buff, ability.buffduration, ability.effectvalue);
				hasCast=true;
			}
			
			// self only shield
			
			if (ability.buff.equals("shield"))
			{
				this.addBuff(player, ability.buff, ability.buffduration, ability.effectvalue);
				int hpbuff = 0;
				try
				{
					hpbuff = Integer.parseInt(ability.effectvalue);
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a shield buff but has no effectvalue set!");
				}
				
				this.Rpchat.setPlayerHPBonus(player, hpbuff);
				this.Rpchat.setPlayerHPBonusMax(player, hpbuff);
				
				hasCast=true;
			}
			
			if (ability.buff.equals("clarity"))
			{
				this.addBuff(player, ability.buff, ability.buffduration,ability.effectvalue);
				hasCast=true;
			}
			
			// self only heal
			
			if (ability.buff.equals("heal"))
			{
				this.addBuff(player, ability.buff, ability.buffduration,ability.effectvalue);
				hasCast=true;
			}
			
			// self only leap
			
			if (ability.buff.equals("leap"))
			{
				// let the glide function add the buff
				Glide(player,ability.buffduration);
				hasCast=true;
			}
			
			// self only fly
			
			if (ability.buff.equals("fly"))
			{
				// let the glide function add the buff
				Fly(player,ability.buffduration);
				hasCast=true;
			}
			
			if (ability.buff.equals("speed"))
			{
				try
				{
					// let the Speed function add the buff
					
					int runspeed = Integer.parseInt(ability.effectvalue);
					Speed(player,runspeed,ability.buffduration);
					hasCast=true;
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a speed buff but has no effectvalue set!");
				}
				
			}
			
			if (ability.buff.equals("defense"))
			{
				try
				{
					// let the Speed function add the buff
					
					int defense = Integer.parseInt(ability.effectvalue);
					Defense(player,defense,ability.buffduration);
					hasCast=true;
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a defense buff but has no effectvalue set!");
				}
				
			}
			
			// self only strength
			
			if (ability.buff.equals("strength"))
			{
				try
				{
					// let the Strength function add the buff
					
					int strength = Integer.parseInt(ability.effectvalue);
					Strength(player,strength,ability.buffduration);
					hasCast=true;
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a strength buff but has no effectvalue set!");
				}
				
			}
			
		}
		
		// Check Detrimentals casted on SELF
		if (ability.effect.equals("detrimental") && ability.buffduration > 0 && !ability.buff.equals(""))
		{
			
			
			if (ability.buff.equals("poison"))
			{
				try
				{
					int poisonamount = Integer.parseInt(ability.effectvalue);
					this.addDetrimental((LivingEntity)player, player, ability.buff, ability.buffduration, poisonamount);
					hasCast=true;
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a poison detrimental but has no effectvalue set!");
				}
			}
		}
		
		// Add skill experience
		if (!ability.skill.equals("normal") && ability.skillexp > 0)
		{
			if (hasCast == true)
			{
				this.addSkillExperience(player, ability.skill, ability.skillexp, false);
				List<Player> list = this.Rpchat.getNearbyGroupPlayers(player);
				if (list.size() > 0)
				{
					for (Player pp : list)
					{
						this.addSkillExperience(pp, ability.skill, ability.skillexp/6, true);
					}
				}
				
			}
		}
		
		if (hasCast == false)
		{
			//player.sendMessage(ChatColor.GRAY+"This ability appears to be without power at this time, perhaps you should hold on to it");
			// Cast failed for some reason, refund power
			refundPower(player,itemid);
			
		}
		
	}
	
	private void onSentryPlayerHealed(Player from, Player to) {
		// TODO Auto-generated method stub
		
	}

	public boolean isPlayerSentry(Player p) {
		// TODO Auto-generated method stub
		return this.Rpchat.isPlayerSentry(p);
	}

	private void useAbilityOnEntity(Player player, int itemid, Entity entity)
	{
		boolean hasCast = false;
		Ability ability = this.getAbilityByID(itemid);
		if (ability == null)
		{
			// Did not find ability
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"This appears to have no use");
			return;
		}
		
		if (ability.effect.equals("effect_none"))
		{
			refundPower(player,itemid);
			player.sendMessage(ChatColor.GRAY+"This appears to have no use");
			return;
		}
		
		if (!ability.requirestarget.equals("yes") && !ability.requirestarget.equals("both"))
		{
			refundPower(player,itemid);
			useAbilityOnSelf(player,itemid);
			return;
		}
		
		// do they have this buff already?
		if (ability.effect.equals("buff") && entity instanceof Player)
		{
			Player p = (Player)entity; 
			if (isBuffed(p,ability.buff))
			{
				refundPower(player,itemid);
				player.sendMessage(ChatColor.GRAY+"The buff has no effect as an existing buff that shares the same properties is already active");
				return;
			}
			
		}
		
		// do they have this detrimental already?
		if (ability.effect.equals("detrimental") && entity instanceof Player)
		{
			Player p = (Player)entity; 
			if (isDetrimentaled(p,ability.buff))
			{
				refundPower(player,itemid);
				player.sendMessage(ChatColor.GRAY+"The detrimental has no effect as an existing detrimental that shares the same properties is already active");
				return;
			}
		}
		
		// Send Special Effect
		sendSpecialEffect(player,ability.specialeffect,ability.specialeffectvalue);
		
		// Send onCastText
		if (!ability.onCastText.equals(""))
		{
			player.sendMessage(ChatColor.GRAY+ability.onCastText);
		}
		
		// Check Effects
		
		// TARGETTED DAMAGE
		if (ability.effect.equals("targetdmg"))
		{
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				if (entity instanceof Player)
				{
					Player p = (Player)entity; 
					if (isPlayerSentry(p))
					{
						int dmgamount = Integer.parseInt(ability.effectvalue);
						p.damage(dmgamount,player);
						
						if (!ability.onCastTargetText.equals(""))
						{
							p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
						}
						sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
						hasCast=true;
					} else {
						player.sendMessage(ChatColor.GRAY+"Hostile Spells and Abilities only function on NPCs");
					}
				}
				
				if (entity instanceof Monster)
				{
					int dmgamount = Integer.parseInt(ability.effectvalue);
					LivingEntity m = (LivingEntity)entity;
					m.damage(dmgamount,player);
					sendSpecialEffect(m,ability.specialeffecttarget,ability.specialeffecttargetvalue);
					hasCast=true;
				}
				
			} else {
				player.sendMessage(ChatColor.GRAY+"The damage is insignificant (damages for 0)");
			}	
			
		}
		
		// life tap
		
		if (ability.effect.equals("lifetap"))
		{
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				if (entity instanceof Player)
				{
					Player p = (Player)entity; 
					if (isPlayerSentry(p))
					{
						int dmgamount = Integer.parseInt(ability.effectvalue);
						p.damage(dmgamount,player);
						
						int playermaxhealth = player.getMaxHealth();
						int healthto = dmgamount + player.getHealth();
						if (healthto > playermaxhealth)
						{
							healthto = playermaxhealth;
						}
						player.sendMessage("You sap the life into yourself");
						player.setHealth(healthto);
						
						if (!ability.onCastTargetText.equals(""))
						{
							p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
						}
						sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
						hasCast=true;
					} else {
						player.sendMessage(ChatColor.GRAY+"Hostile Spells and Abilities only function on NPCs");
					}
				}
				
				if (entity instanceof Monster)
				{
					int dmgamount = Integer.parseInt(ability.effectvalue);
					LivingEntity m = (LivingEntity)entity;
					m.damage(dmgamount,player);
					
					int playermaxhealth = player.getMaxHealth();
					int healthto = dmgamount + player.getHealth();
					if (healthto > playermaxhealth)
					{
						healthto = playermaxhealth;
					}
					player.sendMessage("You sap the life into yourself");
					player.setHealth(healthto);

					
					sendSpecialEffect(m,ability.specialeffecttarget,ability.specialeffecttargetvalue);
					hasCast=true;
				}
				
			} else {
				player.sendMessage(ChatColor.GRAY+"The damage is insignificant (damages for 0)");
			}	
			
		}		
		
		// TARGETTED HEAL
		if (ability.effect.equals("heal"))
		{
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				if (entity instanceof Player)
				{
					Player p = (Player)entity; 
					int healamount = Integer.parseInt(ability.effectvalue);
					
					
					if (p.getHealth() < (p.getMaxHealth()))
					{
						
						int amount = p.getHealth() + healamount;
						if (amount > p.getMaxHealth())
						{
							amount = p.getMaxHealth();
						}
						p.setHealth(amount);
						this.onSentryPlayerHealed(player, p);
						
						if (!ability.onCastTargetText.equals(""))
						{
							p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
						}
						sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
						hasCast=true;
						
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY+"The heal is insignificant (heals for 0)");
			}		
			
		}
		
		if (ability.effect.equals("shieldheal"))
		{
			if (!ability.effectvalue.equals("") && !ability.effectvalue.equals(0))
			{
				if (entity instanceof Player)
				{
					Player p = (Player)entity; 
					int healamount = Integer.parseInt(ability.effectvalue);
					
					
					if (this.Rpchat.getPlayerHPBonus(p) < this.Rpchat.getPlayerHPBonusMax(p))
					{
						
						int amount = this.Rpchat.getPlayerHPBonus(p) + healamount;
						if (amount > this.Rpchat.getPlayerHPBonusMax(p))
						{
							amount = this.Rpchat.getPlayerHPBonusMax(p);
						}
						this.Rpchat.setPlayerHPBonus(p, amount);
						this.onSentryPlayerHealed(player, p);
						
						if (!ability.onCastTargetText.equals(""))
						{
							p.sendMessage(ChatColor.GRAY+ability.onCastTargetText);
						}
						sendSpecialEffect(p,ability.specialeffecttarget,ability.specialeffecttargetvalue);
						hasCast=true;
						
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY+"The heal is insignificant (heals for 0)");
			}		
			
		}
			
		// Check Buffs for target players
		if (ability.effect.equals("buff") && ability.buffduration > 0 && !ability.buff.equals("") && entity instanceof Player)
		{
			Player targetforbuff = (Player)entity;
			
			// TURNVAMPIRE
			if (ability.buff.equals("turnvampire"))
			{
				this.addBuff(targetforbuff, ability.buff, ability.buffduration, ability.effectvalue);
				if (!ability.onBuffStartText.equals(""))
				{
					targetforbuff.sendMessage(ChatColor.GRAY+ability.onBuffStartText);
				}
				targetforbuff.damage(1,player);
				hasCast=true;
			}
			
			if (ability.buff.equals("shield"))
			{
				this.addBuff(targetforbuff, ability.buff, ability.buffduration, ability.effectvalue);
				if (!ability.onBuffStartText.equals(""))
				{
					targetforbuff.sendMessage(ChatColor.GRAY+ability.onBuffStartText);
				}
				
				int hpbuff = 0;
				try
				{
					hpbuff = Integer.parseInt(ability.effectvalue);
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a shield buff but has no effectvalue set!");
				}
				
				this.Rpchat.setPlayerHPBonus(targetforbuff, hpbuff);
				this.Rpchat.setPlayerHPBonusMax(targetforbuff, hpbuff);
				hasCast=true;
			}
			
			if (ability.buff.equals("clarity"))
			{
				this.addBuff(targetforbuff, ability.buff, ability.buffduration, ability.effectvalue);
				if (!ability.onBuffStartText.equals(""))
				{
					targetforbuff.sendMessage(ChatColor.GRAY+ability.onBuffStartText);
				}
				hasCast=true;
			}
			
			if (ability.buff.equals("strength"))
			{
				this.addBuff(targetforbuff, ability.buff, ability.buffduration, ability.effectvalue);
				if (!ability.onBuffStartText.equals(""))
				{
					targetforbuff.sendMessage(ChatColor.GRAY+ability.onBuffStartText);
				}
				hasCast=true;
			}
			
			if (ability.buff.equals("heal"))
			{
				this.addBuff(targetforbuff, ability.buff, ability.buffduration, ability.effectvalue);
				if (!ability.onBuffStartText.equals(""))
				{
					targetforbuff.sendMessage(ChatColor.GRAY+ability.onBuffStartText);
				}
				hasCast=true;
			}
			
			
		}
		
		
		// Check Detrimentals
		
		// Detrimentals are like BUFFs but effect all entities (Mobs etc for poisons)
		
		// TARGETTED 
		
		if (ability.effect.equals("detrimental") && ability.buffduration > 0 && !ability.buff.equals(""))
		{
			
			if (entity instanceof LivingEntity)
			{
				try
				{
					int dmgamount = Integer.parseInt(ability.effectvalue);
					LivingEntity m = (LivingEntity)entity;
					
					if (ability.buff.equals("poison"))
					{
						if (entity instanceof Player)
						{
							Player p = (Player)entity;	
							if (this.isPlayerSentry(p))
							{
								this.addDetrimental(p, player, ability.buff, ability.buffduration, dmgamount);
								hasCast=true;
							} else {
								player.sendMessage(ChatColor.GRAY+"Hostile Spells and Abilities only function on NPCs");						
							}
						} else {
							this.addDetrimental(m, player, ability.buff, ability.buffduration, dmgamount);
							hasCast=true;
						}
					}
				} catch (Exception e)
				{
					System.out.println("[RPItems] ERROR! Spell " + ability.name + " is a poison detrimental but has no effectvalue set!");
				}
				
			}
			
			
		}
		
		// Add skill experience
		if (!ability.skill.equals("normal") && ability.skillexp > 0)
		{
			if (hasCast == true && (entity instanceof Monster))
			{
				
				
				this.addSkillExperience(player, ability.skill, ability.skillexp, false);
				List<Player> list = this.Rpchat.getNearbyGroupPlayers(player);
				if (list.size() > 0)
				{
					for (Player pp : list)
					{
						this.addSkillExperience(pp, ability.skill, ability.skillexp/6, true);
					}
				}
			}
		}
		
		if (hasCast == false)
		{
			refundPower(player,itemid);
			//player.sendMessage(ChatColor.GRAY+"This ability appears to be without power at this time, perhaps you should hold on to it");
		}
		
	}
	


	private void sendSpecialEffect(Entity target, String specialeffect,
			String specialeffectvalue) {
		if (!specialeffect.equals(""))
		{
			if (specialeffect.equals("SMOKE"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.SMOKE, 1);
			}
			
			if (specialeffect.equals("FIREBALL"))
			{
				if (target instanceof Player)
				{
					Player player = (Player)target;
					Location loc = player.getEyeLocation().toVector().add(target.getLocation().getDirection().multiply(2)).toLocation(target.getWorld(), target.getLocation().getYaw(), target.getLocation().getPitch());
					Fireball projectile = target.getWorld().spawn(loc, Fireball.class);
					projectile.setShooter(player);
					projectile.setYield(0);
					projectile.setBounce(false);
					projectile.setIsIncendiary(false);
					projectile.setVelocity(loc.getDirection().multiply(5));
				}
			}
			
			if (specialeffect.equals("BLAZE_SHOOT") && specialeffectvalue.equals("0"))
			{
				
				target.getWorld().playEffect(target.getLocation(), Effect.BLAZE_SHOOT, 0);
			}
			if (specialeffect.equals("BLAZE_SHOOT") && specialeffectvalue.equals("1"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.BLAZE_SHOOT, 1);
			}
			if (specialeffect.equals("BLAZE_SHOOT") && specialeffectvalue.equals("2"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.BLAZE_SHOOT, 2);
			}
			
			if (specialeffect.equals("BLAZE_SHOOT") && specialeffectvalue.equals("3"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.BLAZE_SHOOT, 3);
			}
			if (specialeffect.equals("BLAZE_SHOOT") && specialeffectvalue.equals("19"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.BLAZE_SHOOT, 19);
			}
			
			
			if (specialeffect.equals("BOW_FIRE"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.BOW_FIRE, 1);
			}
			
			if (specialeffect.equals("ZOMBIE_CHEW_IRON_DOOR"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.ZOMBIE_CHEW_IRON_DOOR, 1);
			}
			
			if (specialeffect.equals("LIGHTNING_STRIKE"))
			{
				target.getWorld().strikeLightningEffect(target.getLocation());
			}
			
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("1"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 1);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("2"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 2);
			}
			
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("3"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 3);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("4"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 4);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("5"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 5);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("6"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 6);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("7"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 7);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("8"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 8);
			}
			
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("9"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 9);
			}
			if (specialeffect.equals("POTION_BREAK") && specialeffectvalue.equals("10"))
			{
				target.getWorld().playEffect(target.getLocation(), Effect.POTION_BREAK, 10);
			}
		}
		
	}

	private boolean hasValidSkill(Player player,int itemid)
	{
		// check skill this item uses and whether or not they have the right one
		Ability ability = this.getAbilityByID(itemid);
		if (ability == null)
		{
			return false;
		}

		if (ability.skill.equals("normal"))
		{
			if(isMinlevel(player,itemid))
			{
				return true;
			} else {
				return false;
			}
		}

		if (ability.skill.equals("combat"))
		{
			if(isMinCombatlevel(player,itemid))
			{
				return true;
			} else {
				return false;
			}
		}

		if (ability.skill.equals("ranged"))
		{
			if(isMinRangedlevel(player,itemid))
			{
				return true;
			} else {
				return false;
			}
		}

		if (ability.skill.equals("scholarly"))
		{
			if(isMinScholarlylevel(player,itemid))
			{
				return true;
			} else {
				return false;
			}
		}

		if (ability.skill.equals("natural"))
		{
			if(isMinNaturallevel(player,itemid))
			{
				return true;
			} else {
				return false;
			}
		}

		// not a valid skill type 
		return false;


	}

	private boolean isPlayerOneOfRace(String itemRaces, Player player) {
		// TODO Auto-generated method stub
		String playerrace = this.Rpchat.getPlayerRace(player);
		for (String race : itemRaces.split(","))
		{
			if (race.equals(playerrace))
			{
				return true;
			}
		}

		return false;
	}

	private boolean checkPowerToCastAndRemove(Player player, int itemid) {
		// TODO Auto-generated method stub

		if (player.isOp())
		{
			return true;
		}

		if (this.getSpellPowerCost(itemid) <= this.Rpchat.getPlayerPower(player))
		{
			int newpower = this.Rpchat.getPlayerPower(player) - this.getSpellPowerCost(itemid);
			this.Rpchat.setPlayerPower(player, newpower);
			return true;
		}
		return false;
	}
	
	private void refundPower(Player player, int itemid) {
		// TODO Auto-generated method stub
		int powercost = this.getSpellPowerCost(itemid);
		int newpower = this.Rpchat.getPlayerPower(player) + this.getSpellPowerCost(itemid);
		if (newpower > 100)
		{
			newpower = 100;
		}
		this.Rpchat.setPlayerPower(player, newpower);
	}

	private void addSkillExperience(Player player,String type,int amount, boolean groupbonus)
	{
		int maxexperience = this.Rpchat.getMaxExperience(player);
		int maxlevel = this.Rpchat.getLevelFromExp(maxexperience);
		int ce = this.Rpchat.getPlayerCombatExp(player);
		int re = this.Rpchat.getPlayerRangedExp(player);
		int ne = this.Rpchat.getPlayerNaturalExp(player);
		int se = this.Rpchat.getPlayerScholarlyExp(player);
		
		int cl = this.Rpchat.getLevelFromExp(ce);
		int rl = this.Rpchat.getLevelFromExp(re);
		int nl = this.Rpchat.getLevelFromExp(ne);
		int sl = this.Rpchat.getLevelFromExp(se);
		
		int totallevel = cl+rl+nl+sl;
		if (this.isBuffed(player, "expbonus"))
		{
			amount = amount + 15;
		}
		if (this.hasPerm(player,"rpitems.xpbonus"))
		{
			amount = amount * 2;
		}

		if (totallevel < maxlevel)
		{
			if (type.equals("combat"))
			{
				int newce = ce + amount;
				if (newce <= maxexperience)
				{
					this.Rpchat.setPlayerCombatExp(player, newce,amount, groupbonus);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					this.Rpchat.setPlayerCombatExp(player, maxexperience, amount, groupbonus);
				}
			}

			if (type.equals("ranged"))
			{
				int newre = re + amount;
				if (newre <= maxexperience)
				{
					this.Rpchat.setPlayerRangedExp(player, newre,amount, groupbonus);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					this.Rpchat.setPlayerRangedExp(player, maxexperience, amount, groupbonus);
				}
			}

			if (type.equals("natural"))
			{
				int newne = ne + amount;
				if (newne <= maxexperience)
				{
					this.Rpchat.setPlayerNaturalExp(player, newne,amount, groupbonus);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					this.Rpchat.setPlayerNaturalExp(player, maxexperience, amount, groupbonus);
				}
			}

			if (type.equals("scholarly"))
			{
				int newse = se + amount;
				if (newse <= maxexperience)
				{
					this.Rpchat.setPlayerScholarlyExp(player, newse,amount, groupbonus);
				} else {
					// adding exp to this will put their total experience higher than allowed, shave off the extra
					this.Rpchat.setPlayerScholarlyExp(player, maxexperience, amount, groupbonus);
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
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.combatlevel;
		} else {
			return 0;
		}
	}

	public int getRangedItemLevel(int itemid)
	{
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.rangedlevel;
		} else {
			return 0;
		}
	}

	public int getScholarlyItemLevel(int itemid)
	{
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.scholarlylevel;
		} else {
			return 0;
		}
	}

	public int getNaturalItemLevel(int itemid)
	{
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.naturallevel;
		} else {
			return 0;
		}
	}

	public void setFloating(Player player, boolean bool)
	{
		if (bool == true)
		{
			this.glidingplayers.add(player);
			player.setFallDistance(0F);
			// allow cheat bypass
			this.Rpchat.setCheatBypass(player,true);
		} else {
			this.glidingplayers.remove(player);
			player.setFallDistance(0);
			// disable cheat bypass
			this.Rpchat.setCheatBypass(player,false);

		}
	}
	
	public void setFlying(Player player, boolean bool)
	{
		if (bool == true)
		{
			player.setAllowFlight(true);
			player.setFlying(true);
			this.Rpchat.setCheatBypass(player,true);
		} else {
			player.setAllowFlight(false);			// disable cheat bypass
			player.setFlying(false);
			this.Rpchat.setCheatBypass(player,false);

		}
	}
	
	private void Speed(Player player,int runspeed, int duration) {
		// TODO Auto-generated method stub
		if (!isBuffed(player,"speed"))
		{
			Buff speed = new Buff(player, "speed", duration, runspeed);
			this.buffedplayers.add(speed);
			player.sendMessage(ChatColor.GRAY+"You feel invigorated!");
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 10, runspeed));
			
			
		} else {
			player.sendMessage(ChatColor.GRAY+"You must wait before using this buff again");
		}
	}
	
	private void Strength(Player player,int strength, int duration) {
		// TODO Auto-generated method stub
		if (!isBuffed(player,"strength"))
		{
			Buff str = new Buff(player, "strength", duration, strength);
			this.buffedplayers.add(str);
			player.sendMessage(ChatColor.GRAY+"You feel sronger!");
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration * 10, strength));
			
			
		} else {
			player.sendMessage(ChatColor.GRAY+"You must wait before using this buff again");
		}
	}
	
	private void Defense(Player player,int defense, int duration) {
		// TODO Auto-generated method stub
		if (!isBuffed(player,"strength"))
		{
			Buff def = new Buff(player, "defense", duration, defense);
			this.buffedplayers.add(def);
			player.sendMessage(ChatColor.GRAY+"You feel more defensive!");
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 10, defense));
			
			
		} else {
			player.sendMessage(ChatColor.GRAY+"You must wait before using this buff again");
		}
	}

	private void Glide(Player player,int duration) {
		// TODO Auto-generated method stub
		if (!isBuffed(player,"unstoppableleap"))
		{
			Buff glide = new Buff(player, "unstoppableleap", duration, 0);
			this.buffedplayers.add(glide);
			Vector v = player.getLocation().getDirection();
			v.multiply(4);
			setFloating(player,true);
			player.setVelocity(v);
		} else {
			player.sendMessage(ChatColor.GRAY+"You must wait before using this buff again");
		}
	}
	
	private void Fly(Player player,int duration) {
		// TODO Auto-generated method stub
		if (!isBuffed(player,"fly"))
		{
			Buff fly = new Buff(player, "fly", duration, 0);
			this.buffedplayers.add(fly);
			setFlying(player,true);
		} else {
			player.sendMessage(ChatColor.GRAY+"You must wait before using this buff again");
		}
	}

	public boolean isGliding(Player player)
	{
		if (glidingplayers.contains(player))
		{
			return true;
		} else {
			return false;
		}
	}

	public boolean isBuffed(Player player, String buffname)
	{
		for (Buff buff : buffedplayers)
		{
			if (buff.player.equals(player))
			{
				if (buff.name.equals(buffname))
				{
					return true;
				}
			}

		}
		return false;
	}

	public boolean isDetrimentaled(LivingEntity le, String detrimentalname)
	{
		for (Detrimental detrimental : detrimentaledentities)
		{
			if (detrimental.le.equals(le))
			{
				if (detrimental.name.equals(detrimentalname))
				{
					return true;
				}
			}

		}
		return false;
	}
	
	private class BuffUpdate implements Runnable {
		public void run() {

			DoBuffTick();
		}
	}
	
	private class RewardQueue implements Runnable {
		public void run() {

			DoRewardQueue();
		}
	}
	
	private class DetrimentalUpdate implements Runnable {
		public void run() {

			DoDetrimentalTick();
		}
	}
	public void DoRewardQueue()
	{
		if (!this.Rpchat.rewardqueue.isEmpty())
		{
			Set<Entry<String, String>> es = new HashSet<Entry<String,String>>();
			for (Entry<String,String> e : this.Rpchat.rewardqueue.entrySet())
			{
				String playername =	e.getKey();
				String itemname = e.getValue();
				
				if (this.getServer().getPlayer(playername) != null)
				{
					this.Rpchat.isInteger(e.getValue());
					int itemid = Integer.parseInt(e.getValue());
					Player player = this.getServer().getPlayer(playername);
					es.add(e);
					droporgiveItem(player,itemid);
				}
			}
			
			this.Rpchat.rewardqueue.entrySet().removeAll(es);
		}
		
		getServer().getScheduler().scheduleSyncDelayedTask(this, new RewardQueue(), 10L);
	}

	public void DoBuffTick()
	{
		getServer().getScheduler().scheduleSyncDelayedTask(this, new BuffUpdate(), 10L);
		
		List<Buff> toRemove = new ArrayList<Buff>();
		for (Buff buff : buffedplayers)
		{
			if (buff.timeleft > 0)
			{
				ProcessBuffDuringTick(buff);
				buff.timeleft = buff.timeleft - 1;
				
				if (buff.timeleft == 5)
				{
					buff.player.sendMessage(ChatColor.GRAY+"Your buff " + buff.name + " is about to fade");
				}
				
				
				// Other buff tick effects
				if (buff.name.equals("clarity"))
				{
					Player player = buff.player;
					int curpower = this.Rpchat.getPlayerPower(player);
					int poweramount = curpower + buff.effectvalue;
					if (poweramount > 100)
					{
						poweramount = 100;
					}
					//player.sendMessage(ChatColor.GRAY + "Your mind is at ease (gained: " + buff.effectvalue + " power ");
					this.Rpchat.setPlayerPower(player, poweramount);
				}
				
				// Other buff tick effects
				if (buff.name.equals("heal"))
				{
					Player player = buff.player;
					int curhealth = player.getHealth();
					int healamount = curhealth + buff.effectvalue;
					if (healamount > 20)
					{
						healamount = 20;
					}
					if (healamount < 0)
					{
						healamount = 0;
					}
					//player.sendMessage(ChatColor.GRAY + "Your mind is at ease (gained: " + buff.effectvalue + " power ");
					player.setHealth(healamount);
					
				}
				
				
			} else {
				buff.player.sendMessage(ChatColor.GRAY+"Your buff '"+buff.name+"' has faded");	

				if (buff.name.equals("shield"))
				{
					// remove all hp buffs
					this.Rpchat.setPlayerHPBonus(buff.player, 0);
					this.Rpchat.setPlayerHPBonusMax(buff.player, 0);
				}

				if (buff.name.equals("unstoppableleap"))
				{
					// remove fall distance
					setFloating(buff.player,false);
				}
				
				if (buff.name.equals("fly"))
				{
					// remove fall distance
					setFlying(buff.player,false);
				}
				toRemove.add(buff);
			}
		}

		for (Buff buff : toRemove)
		{
			this.buffedplayers.remove(buff);
		}



		
	}
	
	
	public void DoDetrimentalTick()
	{
		List<Detrimental> toRemove = new ArrayList<Detrimental>();
		for (Detrimental detrimental : detrimentaledentities)
		{
			if (detrimental.timeleft > 0)
			{
				ProcessDetrimentalDuringTick(detrimental);
				
				detrimental.timeleft = detrimental.timeleft - 1;
			} else {
				
				if (detrimental.le instanceof Player)
				{
					Player player = (Player)detrimental.le;
					player.sendMessage(ChatColor.GRAY+"Your detrimental '"+detrimental.name+"' has faded");	
				}

				toRemove.add(detrimental);
			}
		}

		for (Detrimental detrimental : toRemove)
		{
			this.detrimentaledentities.remove(detrimental);
		}



		getServer().getScheduler().scheduleSyncDelayedTask(this, new DetrimentalUpdate(), 10L);
	}
	
	
	

	private void ProcessBuffDuringTick(Buff buff) {
		
	}
	
	private void ProcessDetrimentalDuringTick(Detrimental detrimental) {
	
		if (detrimental.name.equals("poison"))
		{
			if (detrimental.le instanceof Player)
			{
				Player player = (Player)detrimental.le;
				player.sendMessage(ChatColor.GRAY+"You were hit by " + detrimental.effectvalue + " points of " + detrimental.name + " damage");
			}
			detrimental.le.damage(detrimental.effectvalue, detrimental.cause);
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
		List<Entity> en = player.getNearbyEntities(5, 5, 5);
		
		List<LivingEntity> allle = new ArrayList<LivingEntity>();
		
		for (Entity e : en)
		{
			if (e instanceof LivingEntity)
			{
				allle.add((LivingEntity)e);
			}
		}
		allle.add((LivingEntity)player);
		
		return allle;
	}

	private boolean isPlayerInPVPArea(Player player)
	{
		return this.Rpchat.isPlayerInPVPArea(player);
	}


	public void addBuff(Player player, String buffname, int timer, String effectvalue)
	{
		// fix the effect value, stored as string in data
		
		int inteffectval = 0;
		try 
		{
			inteffectval = Integer.parseInt(effectvalue);
		} catch (NumberFormatException e)
		{
			inteffectval = 0;
		}
		
		Buff buff = new Buff(player, buffname, timer, inteffectval);
		this.buffedplayers.add(buff);
	}

	public void addDetrimental(LivingEntity entity, Player cause, String detrimentalname, int timer, int effectvalue)
	{
		// get our UUIds 
		
		Detrimental detrimental = new Detrimental(entity, cause, detrimentalname, timer, effectvalue);
		this.detrimentaledentities.add(detrimental);
	}
	
	public int RandomNumber(int start, int end)
	{
		Random rand = new Random();
		return rand.nextInt(end)+start;
	}
	
	public List<Ability> getAbilitiesByEntityTypeAndRegion(EntityType entityType, String region)
	{
		List<Ability> abilitiesbymobs = new ArrayList<Ability>();
		String mobtype = entityType.toString().toLowerCase();
		for (Ability ability : this.abilities)
		{
			if (ability.loottables.contains(mobtype) && (ability.region.equals(region) || ability.region.equals("")))
			{
				abilitiesbymobs.add(ability);
			}
		}
		
		return abilitiesbymobs;
	}
	
	public List<Ability> getAbilitiesBySentryNameAndRegion(String sentryname, String region)
	{
		List<Ability> abilitiesbymobs = new ArrayList<Ability>();
		
		
		for (Ability ability : this.abilities)
		{
			if (ability.loottables.contains(sentryname) && (ability.region.equals(region) || ability.region.equals("")))
			{
				abilitiesbymobs.add(ability);
			}
		}
		
		return abilitiesbymobs;
	}
	
	
	public void droporgiveItem(Player player, int itemid)
	{
		// convert tring to lowercase
				String standingintown = this.Rpchat.getPlayerStandingInTownName(player);
				//ItemStack is = new ItemStack(Material.BOOK_AND_QUILL);
				int id = itemid;
				
				Location location = player.getLocation();
				
				Ability ability = this.getAbilityByID(id);
				id = ability.id;
				
				// now we need to see if this is a quest item instead

				if (ability.effect.equals("questitem"))
				{
					
					ItemStack egg = new ItemStack(Material.MONSTER_EGG);
					egg.addUnsafeEnchantment(Enchantment.OXYGEN, id);
				    ItemMeta im = egg.getItemMeta();
					im.setDisplayName(ability.name);
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(ability.lore);
					im.setLore(lore);
					egg.setItemMeta(im);
					Item droppedItem = player.getLocation().getWorld().dropItemNaturally(location,egg);
					return;
				} 
				
				// tome format
				ItemStack is = new ItemStack(387,1);
				BookMeta meta =  (BookMeta)is.getItemMeta();
				meta.setAuthor("illusiak the geomancer");
				meta.setTitle(this.getItemName(id));
				ArrayList<String> pages = new ArrayList<String>();
				pages.add(this.getItemLore(id));
				meta.setPages(pages);
				is.setItemMeta(meta);
				Item droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
	}
	
	public void droporgiveRandomAbilityScrollBySentry(String sentryname, Location location, Player player) {
		// convert tring to lowercase
		String standingintown = this.Rpchat.getPlayerStandingInTownName(player);
		List<Ability> abilitiesbymobs = this.getAbilitiesBySentryNameAndRegion(sentryname, standingintown);
		//ItemStack is = new ItemStack(Material.BOOK_AND_QUILL);
		int id = 1;
		
		if (abilitiesbymobs.size() > 0)
		{
			int index = RandomNumber(0,abilitiesbymobs.size());
	        Ability ability = abilitiesbymobs.get(index);
			id = ability.id;
			
			// now we need to see if this is a quest item instead

			if (ability.effect.equals("questitem"))
			{
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ability.lore);
				String name = ability.name;
				
				ItemStack egg = new ItemStack(Material.MONSTER_EGG);
				egg.addUnsafeEnchantment(Enchantment.OXYGEN, id);
				// Give it a name
			    System.out.println("Dropping an item: " + name);
			    ItemMeta im = egg.getItemMeta();
				im.setDisplayName(name);
				im.setLore(lore);
				egg.setItemMeta(im);
				Item droppedItem = player.getLocation().getWorld().dropItemNaturally(location,egg);
				return;
			}
		} 
		
		// tome format
		
		
		
		
		ItemStack is = new ItemStack(387,1);
		BookMeta meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.getItemName(id));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(this.getItemLore(id));
		lore.add(this.queryItemText(id));
		meta.setPages(lore);
		is.setItemMeta(meta);
		Item droppedItem = player.getLocation().getWorld().dropItemNaturally(location,is);
		
		
	}
	
	public void dropResurrectScroll(Player player, Location location) {
		// TODO Auto-generated method stub
		java.util.Date date= new java.util.Date();
		
		ItemStack is = new ItemStack(387,1);
		BookMeta meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("yio'sa the time mage");
		meta.setTitle(player.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("....and there, the Eversoul " + player.getName() + " did travel unto the Maw where he seeked to find release...");
		lore.add(Long.toString(date.getTime()));
		meta.setPages(lore);
		is.setItemMeta(meta);
		is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		Item droppedItem = player.getLocation().getWorld().dropItemNaturally(player.getLocation(),is);
		
	}
	
	
	
	public void droporgiveRandomAbilityScrollByMonster(EntityType entityType, Location location, Player player) {
		// convert tring to lowercase
		String standingintown = this.Rpchat.getPlayerStandingInTownName(player);
		List<Ability> abilitiesbymobs = this.getAbilitiesByEntityTypeAndRegion(entityType, standingintown);
		//ItemStack is = new ItemStack(Material.BOOK_AND_QUILL);
		int id = 1;
		
		if (abilitiesbymobs.size() > 0)
		{
			int index = RandomNumber(0,abilitiesbymobs.size());
	        Ability ability = abilitiesbymobs.get(index);
			id = ability.id;
			
			// now we need to see if this is a quest item instead
			if (ability.effect.equals("questitem"))
			{
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ability.lore);
				String name = ability.name;
				
				ItemStack egg = new ItemStack(Material.MONSTER_EGG);
				egg.addUnsafeEnchantment(Enchantment.OXYGEN, id);
				// Give it a name
			    System.out.println("Dropping an item: " + name);
			    ItemMeta im = egg.getItemMeta();
				im.setDisplayName(name);
				im.setLore(lore);
				egg.setItemMeta(im);
				Item droppedItem = player.getLocation().getWorld().dropItemNaturally(location,egg);
				
				return;
			}
			
		} 
		
		
		// tome format
		ItemStack is = new ItemStack(387,1);
		BookMeta meta =  (BookMeta)is.getItemMeta();
		meta.setAuthor("illusiak the geomancer");
		meta.setTitle(this.getItemName(id));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(this.getItemLore(id));
		lore.add(this.queryItemText(id));
		meta.setPages(lore);
		is.setItemMeta(meta);
		Item droppedItem = player.getLocation().getWorld().dropItemNaturally(location,is);
	}
	
	public int getSpellPowerCost(int itemid)
	{
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.powercost;
		} else {
			return 0;
		}
	}

	public int getItemLevel(int itemid) {
		// TODO Auto-generated method stub

		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.level;
		} else {
			return 0;
		}
	}

	public String getItemAlliances(int itemid) {
		// TODO Auto-generated method stub
		return "ALL";
	}

	public String getItemRaces(int itemid) {
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			if (!ability.races.equals(""))
			{
				return ability.races;
			} else {
				return "ALL";
			}
		} else {
			return "ALL";
		}
	}

	public String getItemLore(int itemid) {
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.lore;
		} else {
			return "You see nothing particularly special about this object";
		}

	}
	public String getItemEffect(int itemid) {
		Ability ability = this.getAbilityByID(itemid);
		if (ability != null)
		{
			return ability.effect;
		} else {
			return "";
		}

	}

	public void turnVampire(Player player) {
		// TODO Auto-generated method stub
		this.Rpchat.setPlayerRace(player,"vampire");

	}

	public void queryItem(Player player, int itemid) 
	{
		// TODO Auto-generated method stub
		player.sendMessage(ChatColor.GRAY+"Item Name: " + this.getItemName(itemid));
		player.sendMessage(ChatColor.GRAY+"Item Lore: " + this.getItemLore(itemid));
		player.sendMessage(ChatColor.GRAY+"Races Req: " + this.getItemRaces(itemid));
		player.sendMessage(ChatColor.GRAY+"Alliance Req: " + this.getItemAlliances(itemid));
		player.sendMessage(ChatColor.GRAY+"Power Cost: " + this.getSpellPowerCost(itemid));
		player.sendMessage(ChatColor.GRAY+"Base Level Req: " + this.getItemLevel(itemid));
		player.sendMessage(ChatColor.GRAY+"Com: " + this.getCombatItemLevel(itemid) + " Ran: " + this.getRangedItemLevel(itemid) + " Scho: "+ this.getScholarlyItemLevel(itemid)+ " Nat: " + this.getNaturalItemLevel(itemid));
	}
	public String queryItemText(int itemid) 
	{
		String text = "Item Name: " + this.getItemName(itemid);
		text = text + " Races Req: " + this.getItemRaces(itemid);
		text = text + " Alliance Req: " + this.getItemAlliances(itemid);
		text = text + " Power Cost: " + this.getSpellPowerCost(itemid);
		text = text + " Base Level Req: " + this.getItemLevel(itemid);
		text = text + " Com: " + this.getCombatItemLevel(itemid) + " Ran: " + this.getRangedItemLevel(itemid) + " Scho: "+ this.getScholarlyItemLevel(itemid)+ " Nat: " + this.getNaturalItemLevel(itemid);
		return text;
	}

	public void useResurrectItem(Player player, ItemStack itemInHand) {
		// TODO Auto-generated method stub
		if (player.isFlying())
		{
			player.sendMessage(ChatColor.GRAY+"You cannot use abilities while flying");
			return;
		}
		ItemStack is = itemInHand;
		BookMeta meta =  (BookMeta)is.getItemMeta();
		
		
		String targetplayer = meta.getTitle();
		Player target = this.getServer().getPlayer(targetplayer);
		if (target != null)
		{
			int naturalexp = this.Rpchat.getPlayerNaturalExp(player);
			int naturallevel = this.Rpchat.getLevelFromExp(naturalexp);
			
			if (naturallevel > 49 || this.hasPerm(player,"rpitems.admin"))
			{
				List<String> pages = meta.getPages();
				java.util.Date date= new java.util.Date();
				Long newtime = date.getTime();
				Long oldtime = Long.parseLong(pages.get(1));
				Long diff = newtime - oldtime;
				if (diff > 900000)
				{
					player.getInventory().setItemInHand(null);
					player.updateInventory();
					player.sendMessage("The tome crumbles to dust, it seems too much time has passed to be able to bring them back");
				} else {
					player.sendMessage("You have resurrected " + target.getName());
					target.sendMessage("You have been resurrected by " + player.getName());
					this.Rpchat.teleport(target,player.getLocation());
					player.getInventory().setItemInHand(null);
					player.updateInventory();
					player.sendMessage("The tome crumbles to dust");
				}
			} else {
				player.sendMessage("Only a level 50 nature or divine mage may cast a resurrect spell");
				
			}
		} else {
			player.sendMessage("This player is offline");
		}
	}

	public int getItemIdFromItemStack(ItemStack item) {
		// TODO Auto-generated method stub
		
		if (item.getData().getItemType() == Material.WRITTEN_BOOK)
		{
			BookMeta meta = (BookMeta)item.getItemMeta();
			if (meta != null)
			{
				String itemname = meta.getTitle();
				String author = meta.getAuthor();
				if (author != null && itemname != null)
				{
					if (!author.equals("") && !itemname.equals(""))
					{
						if (author.equals("illusiak the geomancer"))
						{
							for (Ability a : this.abilities)
							{
								if (a.name.equals(itemname))
								{
									return a.id;
								}
							}
						}
					}
				}
			}

		}
		return 0;
	}

	
	
	
}
