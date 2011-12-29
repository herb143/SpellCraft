package org.spellcraft.executor;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Material;
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
import org.spellcraft.PlayerData;
import org.spellcraft.SpellCraft;

public class SpellsKeyBindingExecutor implements BindingExecutionDelegate {

	private static SpellCraft plugin;

	public SpellsKeyBindingExecutor(SpellCraft instance)
	{
		plugin = instance;
	}


	@Override
	public void keyPressed(KeyBindingEvent event) {
		SpoutPlayer player = event.getPlayer();
		PlayerData playerData = plugin.getPlayerData(player);

		if (player.getActiveScreen() == ScreenType.GAME_SCREEN && new SpoutItemStack(player.getItemInHand()).isCustomItem())
		{

			if (event.getBinding().getId() == "CAST_SPELL")
			{

				playerData.getSpellBook().getCurrentSpell().callSpell();
			}

			if (event.getBinding().getId() == "TOGGLE_CLICK_TO_CAST")
			{
				if (playerData.isClickToCast())
				{
					playerData.setClickToCast(false); // Toggle
				}
				else
				{
					playerData.setClickToCast(true);
				}
			}

			else if (event.getBinding().getId() == "SCROLL_SPELLS")
			{

				playerData.getSpellBook().nextSpell();
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
				LivingEntity targetCandidate = null;
				BlockIterator blockIterator = new BlockIterator(player, targetRange); // Loop through Player's line of sight.
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
							targetCandidate = entity; // It's close enough. Thanks to Dirtystarfish for the approximation code above.
						}
					}
					if(targetCandidate != null) { break; }
				}
				if(targetCandidate == null) // We didn't find anything
				{
					playerData.setTarget(null);
				}
				else
				{
					Block targetBlock = player.getTargetBlock(null,101);
					if(targetBlock.getType() != Material.AIR && getDistance(targetBlock.getLocation(),player.getLocation()) < getDistance(targetCandidate.getLocation(),player.getLocation()))
					{
						playerData.setTarget(null); // There is a non-air block in between the player and their target.
					}
					else
					{
						playerData.setTarget(targetCandidate);
					}
				}

			}
			else if(event.getBinding().getId() == "SELF_TARGET")
			{
				playerData.setTarget((LivingEntity)player);
			}
			else if(event.getBinding().getId() == "UNLOCK_TARGET")
			{
				playerData.setTarget(null);
			}
		}
	}


	@Override
	public void keyReleased(KeyBindingEvent event)
	{
		// Do nothing
	}

	private double getDistance(Location locA, Location locB) // Our lovely distance formula.
	{
		double xdiff = locA.getX() - locB.getX();
		double ydiff = locA.getZ() - locB.getZ();
		double zdiff = locA.getY() - locB.getY();
		double xdiffsq = xdiff * xdiff;
		double ydiffsq = ydiff * ydiff;
		double zdiffsq = zdiff * zdiff;
		double xyzadd = xdiffsq + ydiffsq + zdiffsq;
		return Math.sqrt(xyzadd);
	}

}
