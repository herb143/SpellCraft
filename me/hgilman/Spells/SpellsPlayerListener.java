package me.hgilman.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;


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
	}
	
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Spells.playerBooks.remove(event.getPlayer().getName());
	}
	
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		// Gold hoe behavior override.
		if (player.getItemInHand().getType() == Material.GOLD_HOE){
			event.setCancelled(true);	// This only overrides the default gold hoe behavior, not calls to plugins.
		}
		
		
		// Left clicking with gold hoe...
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.GOLD_HOE)
		{
			Spells.playerBooks.get(player.getName()).getCurrentSpell().callSpell();
		}
		
		
		// Right clicking with gold hoe...
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.GOLD_HOE)
		{
			Spells.playerBooks.get(player.getName()).nextSpell();
		}
		
		
		
		
		
	}
}
