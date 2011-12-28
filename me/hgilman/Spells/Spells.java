package me.hgilman.Spells;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.plugin.SpoutPlugin;

import me.hgilman.Spells.Executors.SpellCommandExecutor;
import me.hgilman.Spells.Executors.SpellsKeyBindingExecutor;
import me.hgilman.Spells.Items.Scepter;

public class Spells extends SpoutPlugin {
	
	public static Scepter goldenScepter;
	private final SpellCommandExecutor spellCommandExecutor = new SpellCommandExecutor(this);
	private final SpellsPlayerListener playerListener = new SpellsPlayerListener(this);
	private final SpellsKeyBindingExecutor bindingExecutor = new SpellsKeyBindingExecutor(this);
	public Logger log = Logger.getLogger("Minecraft");
	
	private static HashMap<String, SpellsPlayerData> playerData = new HashMap<String, SpellsPlayerData>();
	public SpellsPlayerData getPlayerData(Player player) { return playerData.get(player.getName()); }
	public void deletePlayerData(Player player) { playerData.remove(player.getName()); }
	public void newPlayerData(Player player) { playerData.put(player.getName(), new SpellsPlayerData(this,player)); }
	
	public void onEnable()
	{
		log.info("Spells v2.0 loading...");
		PluginManager pm = this.getServer().getPluginManager();

		getCommand("spellinfo").setExecutor(spellCommandExecutor);
		getCommand("listspells").setExecutor(spellCommandExecutor);
		getCommand("setspell").setExecutor(spellCommandExecutor);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		SpoutManager.getKeyBindingManager().registerBinding("CAST_SPELL", Keyboard.KEY_C, "Cast Spell", bindingExecutor, this);
		SpoutManager.getKeyBindingManager().registerBinding("SCROLL_SPELLS", Keyboard.KEY_X, "Scroll to Next Spell", bindingExecutor, this);
		SpoutManager.getKeyBindingManager().registerBinding("TARGET", Keyboard.KEY_R, "Set Target", bindingExecutor, this);	
		SpoutManager.getKeyBindingManager().registerBinding("SELF_TARGET", Keyboard.KEY_F, "Target Yourself", bindingExecutor, this);	
		SpoutManager.getKeyBindingManager().registerBinding("TOGGLE_CLICK_TO_CAST", Keyboard.KEY_Z, "Toggle ClickToCast", bindingExecutor, this);	
		
		for (Player player : this.getServer().getOnlinePlayers()) { newPlayerData(player); } // Set values for online players.
		
		SpoutManager.getFileManager().addToPreLoginCache(this,"http://images.7dunce.com/GoldenScepter.png");
		goldenScepter = new Scepter(this, "Golden Scepter", "http://images.7dunce.com/GoldenScepter.png");
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(new SpoutItemStack(goldenScepter,1));
		recipe.shape("SGS", "0S0", "0S0");
		recipe.setIngredient('S', MaterialData.stick);
		recipe.setIngredient('G', MaterialData.goldBlock);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		log.info("Spells v2.0 loaded."); // We've made it this far...
	}
	
	public void onDisable()
	
	{
		log.info("Spells v2.0 disabled.");
	}

}