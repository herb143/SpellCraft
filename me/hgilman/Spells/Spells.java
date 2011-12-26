package me.hgilman.Spells;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import me.hgilman.Spells.Executors.SpellCommandExecutor;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.ShapedRecipes;

public class Spells extends JavaPlugin {
	
	public static HashMap<String, SpellBook> playerBooks = new HashMap<String, SpellBook>();
	
	private SpellCommandExecutor spellCommandExecutor;
	
	private final SpellsPlayerListener playerListener = new SpellsPlayerListener(this);
	
	public Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable()
	{
		log.info("Spells plugin loading...");
		PluginManager pm = this.getServer().getPluginManager();
		spellCommandExecutor = new SpellCommandExecutor(this);
		getCommand("spellinfo").setExecutor(spellCommandExecutor); // Set the executor.
		getCommand("listspells").setExecutor(spellCommandExecutor); // Set the executor.
		getCommand("setspell").setExecutor(spellCommandExecutor); // Set the executor.
		
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		
		
		// Disable old crafting recipe for a gold hoe:
		Iterator<?> itr = CraftingManager.getInstance().b().iterator();
		while (itr.hasNext()) // Scroll through every recipe, and find gold hoe, and delete it.
		{
			Object o = itr.next();
			if (o instanceof ShapedRecipes)
			{
				if (((ShapedRecipes) o).a==294)
				{
					itr.remove();
				}
			}
		}
		
		// Add the new crafting recipe for a SCEPTER:
		ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.GOLD_HOE,1));
		recipe.shape("SGS","0S0","0S0");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		this.getServer().addRecipe(recipe);
		
		// Add spellbooks for online players, if any.
		Player[] onlinePlayers = this.getServer().getOnlinePlayers();
		for (int iii=0;iii<onlinePlayers.length;iii++)
		{
			playerBooks.put(onlinePlayers[iii].getName(), new SpellBook(onlinePlayers[iii], this));
		}
		
		
		log.info("Spells plugin loaded."); // We've made it this far...
	}
	
	public void onDisable()
	{
		log.info("Spells plugin disabled.");
	}
	
	
}