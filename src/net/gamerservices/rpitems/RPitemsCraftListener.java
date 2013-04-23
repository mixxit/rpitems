package net.gamerservices.rpitems;

import java.awt.List;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RPitemsCraftListener implements Listener {

	rpitems plugin;
	public RPitemsCraftListener(rpitems rpitems) {
		// TODO Auto-generated constructor stub
		this.plugin = rpitems;
	}
	
	private boolean recipeMatches(ShapedRecipe recipe1, ShapedRecipe recipe2) {
        if (!recipe1.getResult().equals(recipe2.getResult())) return false;
        if (recipe1.getShape().length != recipe2.getShape().length) return false;
        for (int i=0; i<recipe1.getShape().length; i++) {
            if (recipe1.getShape()[i].length() != recipe2.getShape()[i].length()) return false;
        }
        return true;
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCraftPrepare(PrepareItemCraftEvent event)
	{
		CraftingInventory craftinv = (CraftingInventory) event.getInventory();
		Recipe recipe = event.getRecipe();
		
		if (recipe instanceof ShapedRecipe) {
			ShapedRecipe sRecipe = (ShapedRecipe)recipe;
			if (recipeMatches(sRecipe, plugin.recipeEnhancedSword)) {
				
				ItemStack slot1 = (ItemStack) craftinv.getMatrix()[0];
				ItemStack slot2 = (ItemStack) craftinv.getMatrix()[1];
				ItemStack slot3 = (ItemStack) craftinv.getMatrix()[2];
				
				ItemStack slot4 = (ItemStack) craftinv.getMatrix()[3];
				ItemStack slot5 = (ItemStack) craftinv.getMatrix()[4]; // sword
				ItemStack slot6 = (ItemStack) craftinv.getMatrix()[5];
				
				ItemStack slot7 = (ItemStack) craftinv.getMatrix()[6];
				ItemStack slot8 = (ItemStack) craftinv.getMatrix()[7];
				ItemStack slot9 = (ItemStack) craftinv.getMatrix()[8];
				
				
				try
				{
					if (
							slot1.getItemMeta().getLore().get(0).contains("infused ore") && 
							slot2.getItemMeta().getLore().get(0).contains("infused ore") &&
							slot3.getItemMeta().getLore().get(0).contains("infused ore") &&
							slot4.getItemMeta().getLore().get(0).contains("infused ore") &&
							slot6.getItemMeta().getLore().get(0).contains("infused ore") &&
							slot7.getItemMeta().getLore().get(0).contains("infused ore") && 
							slot8.getItemMeta().getLore().get(0).contains("infused ore") &&
							slot9.getItemMeta().getLore().get(0).contains("infused ore") &&
							
							slot2.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0)) &&
							slot3.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0)) &&
							slot4.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0)) &&
							slot6.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0)) &&
							slot7.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0)) &&
							slot8.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0)) &&
							slot9.getItemMeta().getLore().get(0).equals(slot1.getItemMeta().getLore().get(0))
							
						)
					{
						// get first word
						String parts[] = slot1.getItemMeta().getLore().get(0).split(" ");
						String type = parts[0];
						ItemStack result = new ItemStack(slot5);
		                ItemMeta meta = result.getItemMeta();
		                meta.setDisplayName(parts[0] + " Encrusted Sword");
		                //Lore in the plugin file. This is for the public boolean.
		                ArrayList<String> lore = new ArrayList();
		                lore.add(type+" infused");
		                meta.setLore(lore);
		                result.setItemMeta(meta);
		           
		                craftinv.setResult(result);
					} else {
						craftinv.setResult(null);
					}
				} catch (Exception e)
				{
					craftinv.setResult(null);
				}
				
			}
		}
	}

}
