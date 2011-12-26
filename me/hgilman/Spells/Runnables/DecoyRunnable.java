package me.hgilman.Spells.Runnables;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;


public class DecoyRunnable implements Runnable {
	
	private LivingEntity decoy;
	private int action;
	
	
	public DecoyRunnable(LivingEntity instanceDecoy, int naction)
	{
		decoy = instanceDecoy;
		action = naction;
	}
	
	public void run()
	{
		World world = decoy.getWorld();
		if(action==1) // Kill
		{
			world.playEffect(decoy.getLocation(), Effect.SMOKE, 0);
			world.playEffect(decoy.getLocation(), Effect.SMOKE, 1);
			world.playEffect(decoy.getLocation(), Effect.SMOKE, 2);
			world.playEffect(decoy.getLocation(), Effect.SMOKE, 3);
			world.playEffect(decoy.getLocation(), Effect.BLAZE_SHOOT, 0);
			decoy.remove();
		}
		else if(action==0) // Heal
		{
			world.playEffect(decoy.getLocation(), Effect.SMOKE, 0);
			decoy.setHealth(decoy.getMaxHealth());
		}
	}

}
