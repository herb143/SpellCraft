package org.spellcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.spellcraft.SpellCraft;

public class ScPlayerListener extends PlayerListener
{
	public static SpellCraft plugin; // Get the plugin instance.
	public ScPlayerListener(SpellCraft instance)
	{
		plugin = instance; // Get it.
	}
	
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		plugin.newPlayerData(event.getPlayer());
	}

	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.deletePlayerData(event.getPlayer());
	}
	
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		// Left clicking with golden scepter...
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && new SpoutItemStack(player.getItemInHand()).isCustomItem() && plugin.getPlayerData(player).isClickToCast())
		{
			plugin.getPlayerData(player).getSpellBook().getCurrentSpell().callSpell();
		}
		
		
		
		
		
	}
}
