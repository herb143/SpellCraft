package me.hgilman.Spells.Executors;

import java.util.ArrayList;
import java.util.List;

import me.hgilman.Spells.Spells;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SpellsKeyBindingExecutor implements BindingExecutionDelegate {
	
	private static Spells plugin;
	
	public SpellsKeyBindingExecutor(Spells instance)
	{
		plugin = instance;
	}
	
	
	@Override
	public void keyPressed(KeyBindingEvent event) {
		SpoutPlayer player = event.getPlayer();

		if (player.getActiveScreen() == ScreenType.GAME_SCREEN && new SpoutItemStack(player.getItemInHand()).isCustomItem())
		{

			if (event.getBinding().getId() == "CAST_SPELL")
			{

				plugin.getPlayerData(player).getSpellBook().getCurrentSpell().callSpell();
			}

			if (event.getBinding().getId() == "TOGGLE_CLICK_TO_CAST")
			{
				if (plugin.getPlayerData(player).isClickToCast())
				{
					plugin.getPlayerData(player).setClickToCast(false); // Toggle
				}
				else
				{
					plugin.getPlayerData(player).setClickToCast(true);
				}
			}

			else if (event.getBinding().getId() == "SCROLL_SPELLS")
			{

				plugin.getPlayerData(player).getSpellBook().nextSpell();
			}
			else if (event.getBinding().getId() == "TARGET")
			{
				int targetRange = 15;
				
				List<Entity> nearbyEntities = player.getNearbyEntities(targetRange*2,targetRange*2,targetRange*2);
				ArrayList<LivingEntity> livingEntities = new ArrayList<LivingEntity>();
				
				for (Entity entity : nearbyEntities)
				{
					if (entity instanceof LivingEntity)
					{
						livingEntities.add((LivingEntity) entity);
					}
				}
				BlockIterator blockIterator = new BlockIterator(player, targetRange); // Loop through Player's line of sight.
				boolean broken = false;
				while (blockIterator.hasNext())
				{
					Block block = blockIterator.next();
					int blockX = block.getX();
					int blockY = block.getY();
					int blockZ = block.getZ();

					for (LivingEntity entity : livingEntities)
					{
						Location entityLocation = entity.getLocation();
						Double entityX = entityLocation.getX();
						Double entityY = entityLocation.getY();
						Double entityZ = entityLocation.getZ();
						if ((blockX-.75 <= entityX && entityX <= blockX+1.75) && (blockZ-.75 <= entityZ && entityZ <= blockZ+1.75) && (blockY-1 <= entityY && entityY <= blockY+2.5))
						{
							plugin.getPlayerData(player).setTarget(entity); // It's close enough. Thanks to Dirtystarfish for the approximation code above.
							broken = true;
							break;
						}
					}
					if(broken) { break; }
				}
				if(!broken) // We didn't find anything
				{
					plugin.getPlayerData(player).setTarget(null);
				}

			}
			else if(event.getBinding().getId() == "SELF_TARGET")
			{
				plugin.getPlayerData(player).setTarget((LivingEntity)player);
			}
			else if(event.getBinding().getId() == "UNLOCK_TARGET")
			{
				plugin.getPlayerData(player).setTarget(null);
			}

		}
	}


	@Override
	public void keyReleased(KeyBindingEvent event)
	{
		// Do nothing
	}
	
}
