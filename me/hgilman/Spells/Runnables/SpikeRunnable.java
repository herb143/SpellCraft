package me.hgilman.Spells.Runnables;

import me.hgilman.Spells.Spell;
import me.hgilman.Spells.Library.SpikeSpell;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class SpikeRunnable implements Runnable {

	private SpikeSpell caller;
	private Block targetBlock;
	private SpikeAction action;
	private Material replacerMaterial;
	private boolean sandstoneSupport;
	
	public static enum SpikeAction {
		CREATE_CACTUS,
		DESTROY_CACTUS,
		SINGLE_CACTUS
	}

	
	public SpikeRunnable(Block ntargetBlock)
	{
		action = SpikeAction.CREATE_CACTUS;
		targetBlock = ntargetBlock;
	}
	
	public SpikeRunnable(Block ntargetBlock, Material nreplacerMaterial, boolean ssandstoneSupport)
	{
		action = SpikeAction.DESTROY_CACTUS;
		targetBlock = ntargetBlock;
		replacerMaterial = nreplacerMaterial;
		sandstoneSupport = ssandstoneSupport;
	}
	
	
	public SpikeRunnable(SpikeSpell ncaller, SpikeAction naction,Block ntargetBlock)
	{
		caller = ncaller;
		action = naction;
		targetBlock = ntargetBlock;
	}
	
	
	private boolean canPlaceCactus(Block targetBlock)
	{
		if (targetBlock.getType() == Material.CACTUS || targetBlock.getType() == Material.BEDROCK) 
		{
			return false; // Cannot spawn cactus on cactus, or bedrock.
		}
		
		for (int i = 1; i <= 3; i++) // For each space above the block...
		{
			if (targetBlock.getRelative(0, i, 0).getType() == Material.AIR) { } // If it's air do nothing
			else { return false; } // Otherwise you can't place a cactus.
		}
		
		for (int i = 1; i <= 3; i++) // For each space left of the block...
		{
			if (targetBlock.getRelative(1, i, 0).getType() == Material.AIR) { } // If it's air do nothing
			else { return false; } // Otherwise you can't place a cactus.
		}
		
		for (int i = 1; i <= 3; i++) // For each space right of the block...
		{
			if (targetBlock.getRelative(-1, i, 0).getType() == Material.AIR) { } // If it's air do nothing
			else { return false; } // Otherwise you can't place a cactus.
		}
		
		for (int i = 1; i <= 3; i++) // For each space in front of the block...
		{
			if (targetBlock.getRelative(0, i, 1).getType() == Material.AIR) { } // If it's air do nothing
			else { return false; } // Otherwise you can't place a cactus.
		}
		
		for (int i = 1; i <= 3; i++) // For each space behind the block...
		{
			if (targetBlock.getRelative(0, i, -1).getType() == Material.AIR) { } // If it's air do nothing
			else { return false; } // Otherwise you can't place a cactus.
		}
		
		return true; // If nothing turned up.
	}
	
	private void singleCactus(Block targetBlock)
	{
		if(canPlaceCactus(targetBlock))
		{
			caller.removeItem(new ItemStack(Material.CACTUS, 3));
			caller.removeItem(new ItemStack(Material.SAND,1));
			
			Material originalTargetMaterial = targetBlock.getType(); // So we can restore later.
			boolean support = false; // By default

			if (targetBlock.getRelative(0,-1,0).getType() == Material.AIR || targetBlock.getRelative(0,-1,0).getType() == Material.WATER)
			{
				targetBlock.getRelative(0,-1,0).setType(Material.SANDSTONE); // It would fall down otherwise.
				support = true;
			}

			targetBlock.setType(Material.SAND);

			int b = 0; // The first event happens immediately.
			for (int iii=1;iii<=3;iii++) // For the 3 cacti.
			{
				Spell.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Spell.getPlugin(), new SpikeRunnable(targetBlock.getRelative(0, iii, 0)),b);
				b++; // Frequency set.
			}

			Spell.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Spell.getPlugin(), new SpikeRunnable(targetBlock,originalTargetMaterial,support),300); // Destroy cactus in 15 seconds.
		}
	}
	
	public void run()
	{
		if (action==SpikeAction.CREATE_CACTUS) // If we're building a cactus...
		{
			targetBlock.setType(Material.CACTUS);
		}
		else if (action==SpikeAction.DESTROY_CACTUS)
		{
			
			for(int iii=3;iii>=1;iii--) // For each space above it, starting at the top...
			{
				if (targetBlock.getRelative(0,iii,0).getType() == Material.CACTUS) // They haven't built over it.
				{
					targetBlock.getRelative(0,iii,0).setType(Material.AIR); // Destroy cactus.
					targetBlock.getWorld().dropItemNaturally(targetBlock.getRelative(0,iii,0).getLocation(),new ItemStack(Material.CACTUS,1)); // 3 are recoverable.
				}
			}
			if (targetBlock.getType()==Material.SAND) // If they haven't built over the target block...
			{
				targetBlock.setType(replacerMaterial); // Revert
			}
			if (sandstoneSupport && targetBlock.getRelative(0, -1, 0).getType() == Material.SANDSTONE) // Sandstone support hasn't been built over.
			{
				targetBlock.getRelative(0,-1,0).setType(Material.AIR);
			}
		}
		else if (action==SpikeAction.SINGLE_CACTUS)
		{
			singleCactus(targetBlock);
		}
	}
	
	
}
