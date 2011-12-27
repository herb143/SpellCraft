package me.hgilman.Spells.Runnables;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class NetRunnable implements Runnable {
	
	private ArrayList<Block> frozenBlocks;
	
	public NetRunnable(ArrayList<Block> iFrozenBlocks)
	{
		frozenBlocks = iFrozenBlocks;
	}
	
	public void run()
	{
		for(Block toReset : frozenBlocks)
		{
			if(toReset.getType()==Material.WEB) // As long as nothing has been built over it.
			{
				toReset.setType(Material.AIR);
				toReset.getWorld().playEffect(toReset.getLocation(), Effect.SMOKE, 0);
				toReset.getWorld().playEffect(toReset.getLocation(), Effect.SMOKE, 1);
				toReset.getWorld().playEffect(toReset.getLocation(), Effect.SMOKE, 2);
				toReset.getWorld().playEffect(toReset.getLocation(), Effect.BLAZE_SHOOT, 0);
			}
			
		}
	}
}
