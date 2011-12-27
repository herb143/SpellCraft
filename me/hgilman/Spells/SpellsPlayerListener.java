package me.hgilman.Spells;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.entity.CraftEnderman;
import org.bukkit.craftbukkit.entity.CraftFish;
import org.bukkit.craftbukkit.entity.CraftGhast;
import org.bukkit.craftbukkit.entity.CraftGiant;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftMonster;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftPigZombie;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftSheep;
import org.bukkit.craftbukkit.entity.CraftSilverfish;
import org.bukkit.craftbukkit.entity.CraftSkeleton;
import org.bukkit.craftbukkit.entity.CraftSlime;
import org.bukkit.craftbukkit.entity.CraftSnowman;
import org.bukkit.craftbukkit.entity.CraftSpider;
import org.bukkit.craftbukkit.entity.CraftSquid;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SpellsPlayerListener extends PlayerListener
{
	public static Spells plugin; // Get the plugin instance.
	public SpellsPlayerListener(Spells instance)
	{
		plugin = instance; // Get it.
	}
	
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Spells.playerBooks.put(event.getPlayer().getName(), new SpellBook(event.getPlayer(), plugin)); // Add a new spellbook for the player to the hashmap.
		plugin.setTarget(event.getPlayer(), null); // Set the player's target to null.
		plugin.setTargetLabel(event.getPlayer(), new TargetLabel(plugin,event.getPlayer())); // Make a new label for the target.
		((SpoutPlayer)event.getPlayer()).getMainScreen().attachWidget(plugin, plugin.getTargetLabel(event.getPlayer())); // Attach it to their HUD.
	}
	
	private boolean isOf(Entity entity, Class... classes)
	{
		for(int iii=0;iii<classes.length;iii++)
		{
			if(entity.getClass() == classes[iii])
			{
				return true;
			}
		}
		return false;
	}
	
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Spells.playerBooks.remove(event.getPlayer().getName());
	}
	
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		

		
		// Left clicking with golden scepter...
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && (new SpoutItemStack(player.getItemInHand()).isCustomItem()))
		{
			Spells.playerBooks.get(player.getName()).getCurrentSpell().callSpell();
		}
		
		
		// Right clicking with golden scepter...
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (new SpoutItemStack(player.getItemInHand()).isCustomItem()))
		{
			plugin.setTarget(player, null);
		}
		
		
		
		
		
	}
}
