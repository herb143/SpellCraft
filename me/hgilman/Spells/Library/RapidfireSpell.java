package me.hgilman.Spells.Library;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.hgilman.Spells.Spell;
import me.hgilman.Spells.Spells;
import me.hgilman.Spells.Runnables.RapidfireRunnable;

public class RapidfireSpell extends Spell {
	
	public RapidfireSpell(Player instancePlayer,Spells instance)
	{
		super(instancePlayer,instance,"Rapidfire","Fires off a fast volley of arrows.",new ItemStack(Material.ARROW, 8), new ItemStack(Material.REDSTONE, 4)); // Call the super constructor.
	}
	
	protected void castSpell()
	{
		removeRequiredItems();
		for (int iii=2;iii<15;iii=iii+2) // Do this 7 times.
		{
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new RapidfireRunnable(player),iii); // Destroy cactus in 15 seconds.
		}
	}
}