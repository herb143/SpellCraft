package me.hgilman.Spells;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.keyboard.KeyBindingManager;
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
	private static HashMap<String, SpellBook> playerBooks = new HashMap<String, SpellBook>();
	private static HashMap<String, LivingEntity> playerTargets = new HashMap<String, LivingEntity>();
	private static HashMap<String, TargetLabel> playerTargetLabels = new HashMap<String, TargetLabel>();
	private static HashMap<String, Boolean> playerClickToCasts = new HashMap<String,Boolean>();
	
	public boolean isClickToCast(Player player) { return playerClickToCasts.get(player.getName()); }
	public void setClickToCast(Player player,Boolean clickToCast) { playerClickToCasts.put(player.getName(), clickToCast); }
	public SpellBook getBook(Player player) { return playerBooks.get(player.getName()); }
	public LivingEntity getTarget(Player player) { return playerTargets.get(player.getName()); }
	public void setTarget(Player player, LivingEntity target) { playerTargets.put(player.getName(), target); }
	public void playerQuit(Player player)
	{
		String name = player.getName();
		playerBooks.remove(name);
		playerTargets.remove(name);
		playerTargetLabels.remove(name);
		playerClickToCasts.remove(name);
	}
	public void playerJoin(Player player)
	{
		String name = player.getName();
		playerBooks.put(player.getName(), new SpellBook(player,this));
		playerTargets.put(name, null);
		playerTargetLabels.put(name, new TargetLabel(this,player));
		playerClickToCasts.put(name, false); // Click to cast is false by default.
	}
	
	public void onEnable()
	{
		log.info("Spells plugin loading...");
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
		SpoutManager.getKeyBindingManager().registerBinding("TOGGLE_CLICK_TO_CAST", Keyboard.KEY_Z, "Toggle ClickToCast", bindingExecutor, this);	
		
		for (Player player : this.getServer().getOnlinePlayers()) { playerJoin(player); } // Set hashmaps for online players.
		
		extractFile("GoldenScepter.png", true);
		goldenScepter = new Scepter(this, "Golden Scepter", "/plugins/Spells/GoldenScepter.png");
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(new SpoutItemStack(goldenScepter,1));
		recipe.shape("SGS", "0S0", "0S0");
		recipe.setIngredient('S', MaterialData.stick);
		recipe.setIngredient('G', MaterialData.goldBlock);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		
		//SpoutManager.getKeyBindingManager().registerBinding(arg0, arg1, arg2, arg3, arg4)
		
		log.info("Spells v2.0 loaded."); // We've made it this far...
	}
	
	public void onDisable()
	
	{
		log.info("Spells v2.0 disabled.");
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