package me.hgilman.Spells.Library;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.craftbukkit.entity.*;

import me.hgilman.Spells.Spell;
import me.hgilman.Spells.Spells;
import me.hgilman.Spells.Runnables.DecoyRunnable;

public class DecoySpell extends Spell {

	private int range;
	private int targetRadius;
	private CreatureType creatureType;
	private int lastsForSeconds;
	
	
	public DecoySpell(int nrange,int ntargetRadius,CreatureType nCreatureType,int nlastsforseconds, Player playerinstance, Spells instance, String Name,String Description,ItemStack... items)
	{
		super(playerinstance,instance,Name,Description,items); // Call the super constructor.
		range = nrange;
		targetRadius = ntargetRadius;
		creatureType = nCreatureType;
		lastsForSeconds = nlastsforseconds;
	}
	
	protected boolean isOf(Entity entity, Class... classes)
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
	
	protected void targetNearby(LivingEntity decoy, int range)
	{
		List<Entity> nearbyMobs = decoy.getNearbyEntities(range * 2, (range * 2), (range*2) );
		for (int iii=0;iii<nearbyMobs.size();iii++) // Scroll through every entity in the list.
		{
			if(isOf(nearbyMobs.get(iii), CraftCaveSpider.class,CraftChicken.class,CraftCow.class,CraftCreeper.class,CraftEnderDragon.class,CraftEnderman.class,CraftFish.class,CraftGhast.class,CraftGiant.class,CraftMonster.class,CraftPig.class,CraftPigZombie.class,CraftSheep.class,CraftSilverfish.class,CraftSkeleton.class,CraftSlime.class,CraftSnowman.class,CraftSpider.class,CraftSquid.class,CraftVillager.class,CraftWolf.class,CraftZombie.class))
			{
				// We need it to be a thing that can target stuff.
				((Creature) nearbyMobs.get(iii)).setTarget(decoy);
			}
		}	
	}
	
	protected void castSpell()
	{
		Block targetBlock = player.getTargetBlock(null, 101);
		if (getDistance(targetBlock.getLocation(),player.getLocation())<=range && targetBlock.getType()!=Material.AIR && targetBlock.getRelative(0, 1, 0).getType()==Material.AIR) // We need room for it. And it must be in range.
		{
			removeRequiredItems();
			targetBlock = targetBlock.getRelative(0, 1, 0); // So we spawn above the target block.
			LivingEntity decoy = player.getWorld().spawnCreature(targetBlock.getLocation(), creatureType);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DecoyRunnable(decoy,1), (lastsForSeconds*20));
			for (int iii=1;iii<lastsForSeconds;iii++)
			{
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DecoyRunnable(decoy,0), (iii*20));
			}
			targetNearby(decoy,targetRadius);
		}
		
	}
	
}
