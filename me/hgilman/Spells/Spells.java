package me.hgilman.Spells;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.plugin.SpoutPlugin;

import me.hgilman.Spells.Executors.SpellCommandExecutor;
import me.hgilman.Spells.Items.Scepter;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.ShapedRecipes;

public class Spells extends SpoutPlugin {
	
	public static HashMap<String, SpellBook> playerBooks = new HashMap<String, SpellBook>();
	private static HashMap<String, LivingEntity> playerTargets = new HashMap<String, LivingEntity>();
	private static HashMap<String, TargetLabel> playerTargetLabels = new HashMap<String, TargetLabel>();
	
	public LivingEntity getTarget(Player player)
	{
		return playerTargets.get(player.getName());
	}
	public void setTarget(Player player, LivingEntity target)
	{
		playerTargets.put(player.getName(), target);
	}
	public void updateTargetLabel(Player player)
	{
		playerTargetLabels.get(player.getName()).updateLabel();
	}
	public void setTargetLabel(Player player,TargetLabel label)
	{
		playerTargetLabels.put(player.getName(),label);
	}
	public TargetLabel getTargetLabel(Player player)
	{
		return playerTargetLabels.get(player.getName());
	}
	
	public static Scepter goldenScepter;
	
	private SpellCommandExecutor spellCommandExecutor;
	
	private final SpellsPlayerListener playerListener = new SpellsPlayerListener(this);
	private final SpellsInputListener inputListener = new SpellsInputListener(this);
	
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
		pm.registerEvent(Event.Type.CUSTOM_EVENT, inputListener,Event.Priority.Normal,this);
		
		extractFile("GoldenScepter.png", true);
		goldenScepter = new Scepter(this, "Golden Scepter", "/plugins/Spells/GoldenScepter.png");
		
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(new SpoutItemStack(goldenScepter,1));
		recipe.shape("SGS", "0S0", "0S0");
		recipe.setIngredient('S', MaterialData.stick);
		recipe.setIngredient('G', MaterialData.goldBlock);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		// Add spellbooks for online players, if any.
		Player[] onlinePlayers = this.getServer().getOnlinePlayers();
		for (int iii=0;iii<onlinePlayers.length;iii++)
		{
			playerBooks.put(onlinePlayers[iii].getName(), new SpellBook(onlinePlayers[iii], this));
		}
		// Add targets for online players, if any.
		for (int iii=0;iii<onlinePlayers.length;iii++)
		{
			playerTargets.put(onlinePlayers[iii].getName(), null);
		}
		// Add targetlabels for online players, if any.
		for (int iii=0;iii<onlinePlayers.length;iii++)
		{
			playerTargetLabels.put(onlinePlayers[iii].getName(), new TargetLabel(this,onlinePlayers[iii]));
			((SpoutPlayer)onlinePlayers[iii]).getMainScreen().attachWidget(this, this.getTargetLabel(onlinePlayers[iii]));
		}
		
		
		log.info("Spells plugin loaded."); // We've made it this far...
	}
	
	public void onDisable()
	{
		log.info("Spells plugin disabled.");
	}
	
	//Code taken from Rycochet TODO: Figure this out
	public boolean extractFile(String regex, boolean cache) {
		boolean found = false;
		try {
			JarFile jar = new JarFile(getFile());
			for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
				JarEntry entry = (JarEntry) entries.nextElement();
				String name = entry.getName();
				if (name.matches(regex)) {
					if (!getDataFolder().exists()) {
						getDataFolder().mkdir();
					}
					try {
						File file = new File(getDataFolder(), name);
						if (!file.exists()) {
							InputStream is = jar.getInputStream(entry);
							FileOutputStream fos = new FileOutputStream(file);
							while (is.available() > 0) {
								fos.write(is.read());
							}
							fos.close();
							is.close();
							found = true;
						}
						if (cache && name.matches(".*\\.(txt|yml|xml|png|jpg|ogg|midi|wav|zip)$")) {
							SpoutManager.getFileManager().addToPreLoginCache(this, file);
						}
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		return found;
	}
	//End code taken from Ryochet

}