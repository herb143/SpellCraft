package me.hgilman.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.BlockIterator;
import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SpellsInputListener extends InputListener {
	private Spells plugin;
	public SpellsInputListener(Spells instance)
	{
		plugin = instance;
	}

	public void onKeyPressedEvent(KeyPressedEvent event)
	{
		SpoutPlayer player = event.getPlayer();

		if (player.getActiveScreen() == ScreenType.GAME_SCREEN && new SpoutItemStack(player.getItemInHand()).isCustomItem())
		{

			if (event.getKey() == Keyboard.KEY_C)
			{

				plugin.getBook(player).getCurrentSpell().callSpell();
			}

			if (event.getKey() == Keyboard.KEY_Z)
			{
				if (plugin.isClickToCast(player))
				{
					plugin.setClickToCast(player, false); // Toggle
					player.sendMessage(ChatColor.RED + "ClickToCast disabled.");
				}
				else
				{
					plugin.setClickToCast(player, true);
					player.sendMessage(ChatColor.GREEN + "ClickToCast enabled.");
				}
			}

			else if (event.getKey() == Keyboard.KEY_X)
			{

				plugin.getBook(player).nextSpell();
			}
			else if (event.getKey() == Keyboard.KEY_R)
			{

				// CODE BY DIRTYSTARFISH
				List<Entity> nearbyE = player.getNearbyEntities(30,30,30);
				ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

				for (Entity e : nearbyE) {
					if (e instanceof LivingEntity) {
						livingE.add((LivingEntity) e);
					}
				}
				BlockIterator bItr = new BlockIterator(player, 15);
				Block block;
				Location loc;
				int bx, by, bz;
				double ex, ey, ez;
				boolean broken = false;
				// loop through player's line of sight
				while (bItr.hasNext()) {
					block = bItr.next();
					bx = block.getX();
					by = block.getY();
					bz = block.getZ();
					// check for entities near this block in the line of sight
					for (LivingEntity e : livingE) {
						loc = e.getLocation();
						ex = loc.getX();
						ey = loc.getY();
						ez = loc.getZ();
						if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
							// entity is close enough, set target and stop
							plugin.setTarget(player,e);
							broken = true;
							break;
						}
					}
					if(broken) { break; }
				}
				if(!broken) // We didn't find anything
				{
					plugin.setTarget(player,null);
				}

				// END CODE BY DIRTYSTARFISH

			}

		}
	}


}
