package me.hgilman.Spells.Library;

import java.util.ArrayList;
import java.util.List;

import me.hgilman.Spells.Spell;
import me.hgilman.Spells.Spells;
import me.hgilman.Spells.Runnables.NetRunnable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.entity.CraftEnderman;
import org.bukkit.craftbukkit.entity.CraftFish;
import org.bukkit.craftbukkit.entity.CraftGhast;
import org.bukkit.craftbukkit.entity.CraftGiant;
import org.bukkit.craftbukkit.entity.CraftMonster;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftPigZombie;
import org.bukkit.craftbukkit.entity.CraftSheep;
import org.bukkit.craftbukkit.entity.CraftSilverfish;
import org.bukkit.craftbukkit.entity.CraftSkeleton;
import org.bukkit.craftbukkit.entity.CraftSlime;
import org.bukkit.craftbukkit.entity.CraftSnowman;
import org.bukkit.craftbukkit.entity.CraftSpider;
import org.bukkit.craftbukkit.entity.CraftSquid;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NetSpell extends Spell {
	
	protected int range = 30;
	private int radius = 10;
	
	public NetSpell(Spells instance, Player playerinstance)
	{
		super(playerinstance,instance,"Net","Freezes all life in 10-block radius for 10 seconds.",new ItemStack(Material.STRING,10));
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
	
	protected void freezeNearby(LivingEntity center, int range)
	{
		List<Entity> nearbyMobs = center.getNearbyEntities(range * 2, (range * 2), (range*2) );
		ArrayList<Block> frozenBlocks = new ArrayList<Block>();
		for (int iii=0;iii<nearbyMobs.size();iii++) // Scroll through every entity in the list.
		{
			if(isOf(nearbyMobs.get(iii), CraftCaveSpider.class,CraftChicken.class,CraftCow.class,CraftCreeper.class,CraftEnderDragon.class,CraftEnderman.class,CraftFish.class,CraftGhast.class,CraftGiant.class,CraftMonster.class,CraftPig.class,CraftPigZombie.class,CraftSheep.class,CraftSilverfish.class,CraftSkeleton.class,CraftSlime.class,CraftSnowman.class,CraftSpider.class,CraftSquid.class,CraftVillager.class,CraftWolf.class,CraftZombie.class))
			{
				Block frozenBlock = nearbyMobs.get(iii).getLocation().getWorld().getBlockAt(nearbyMobs.get(iii).getLocation());
				frozenBlocks.add(frozenBlock);
				frozenBlock.setType(Material.WEB);
			}
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new NetRunnable(frozenBlocks), (200));
	}
	
	protected void castSpell()
	{
		freezeNearby(player,radius);
	}
}