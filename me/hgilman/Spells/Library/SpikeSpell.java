package me.hgilman.Spells.Library;

import me.hgilman.Spells.Spells;
import me.hgilman.Spells.Spell;
import me.hgilman.Spells.Runnables.SpikeRunnable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpikeSpell extends Spell {

	public enum SpellType {
		BASIC,
		WALL,
		FORT
	}

	protected int range = 30;
	private SpellType spellType;

	public SpikeSpell(Player playerinstance, Spells instance, SpellType ispellType, int nrange, String iname,String idescription,ItemStack... irequirements)
	{
		super(playerinstance,instance,true,iname,idescription,irequirements); // Call the super constructor.
		spellType = ispellType;
	}
	private Block[] cactusArray(Block center)
	{
		Block[] blocks;
		if(spellType==SpellType.BASIC)
		{
			blocks = new Block[1];
			blocks[0] = center;
			return blocks;
		}
		else if (spellType==SpellType.WALL)
		{
			blocks = new Block[8];
			blocks[0] = center.getRelative(0, 0, 2);
			blocks[1] = blocks[0].getRelative(2,0,0);
			blocks[2] = blocks[0].getRelative(-2, 0, 0);
			blocks[3] = center.getRelative(2, 0, 0);
			blocks[4] = blocks[3].getRelative(0, 0, -2);
			blocks[5] = center.getRelative(0, 0, -2);
			blocks[6] = blocks[5].getRelative(-2, 0, 0);
			blocks[7] = center.getRelative(-2, 0, 0);
			return blocks;
		}
		else if (spellType==SpellType.FORT)
		{
			blocks = new Block[20];

			blocks[0] = center.getRelative(0, 0, 2);
			blocks[1] = blocks[0].getRelative(2,0,0);
			blocks[2] = blocks[0].getRelative(-2, 0, 0);
			blocks[3] = center.getRelative(2, 0, 0);
			blocks[4] = blocks[3].getRelative(0, 0, -2);
			blocks[5] = center.getRelative(0, 0, -2);
			blocks[6] = blocks[5].getRelative(-2, 0, 0);
			blocks[7] = center.getRelative(-2, 0, 0);

			blocks[8] = center.getRelative(-1,0,3);
			blocks[9] = center.getRelative(-3,0,3);
			blocks[10] = center.getRelative(1,0,3);
			blocks[11] = center.getRelative(3,0,3);
			blocks[12] = center.getRelative(-3,0,1);
			blocks[13] = center.getRelative(-3,0,-1);
			blocks[14] = center.getRelative(-3,0,-3);
			blocks[15] = center.getRelative(-1,0,-3);
			blocks[16] = center.getRelative(1,0,-3);
			blocks[17] = center.getRelative(3,0,-3);
			blocks[18] = center.getRelative(3,0,-1);
			blocks[19] = center.getRelative(3,0,1);
			return blocks;
		}
		else
		{
			return null;
		}
	}
	protected void castSpell(LivingEntity target)
	{
		Block targetBlock;
		
		if(target==player && spellType==SpellType.BASIC)
		{
			plugin.getPlayerData(player).setTarget(null); // Unlock the player target.
			targetBlock = player.getTargetBlock(null, 101); // We don't want the player to spike himself.
		}
		else
		{
			targetBlock = target.getWorld().getBlockAt(target.getLocation()).getRelative(0, -1, 0); // The sand is below the target, so they get caught in the cactus.
		}
		
		if(getDistance(targetBlock.getLocation(), player.getLocation()) <= range || targetBlock.getType() != Material.AIR) // If it's out of range, or air, they don't need to know.
		{
			Block[] targetBlocks = cactusArray(targetBlock);
			for (ItemStack currentRequirement : getRequiredItems()) // Remove all the extra items from the player's inventory.
			{
				if(currentRequirement.getType()==Material.CACTUS)
				{
					removeItem(new ItemStack(Material.CACTUS,currentRequirement.getAmount()-(targetBlocks.length*3))); // Remove any extra cacti
				}
				else if(currentRequirement.getType()==Material.SAND)
				{
					removeItem(new ItemStack(Material.SAND,currentRequirement.getAmount()-(targetBlocks.length))); // Remove any extra sand
				}
				else if(currentRequirement.getType()==Material.SANDSTONE)
				{
					removeItem(new ItemStack(Material.SANDSTONE,currentRequirement.getAmount())); // Remove any extra sandstone.
				}
			}

			for (int iii=0;iii<targetBlocks.length;iii++)
			{
				Spell.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Spell.getPlugin(), new SpikeRunnable(this,SpikeRunnable.SpikeAction.SINGLE_CACTUS,targetBlocks[iii]),iii);
			}
		}

	}


}
