package me.hgilman.Spells;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class TargetLabel extends GenericLabel {
	
	Player player;
	Spells plugin;
	
	String printLabel(LivingEntity target)
	{
		String returnValue = "";
		if (target==null)
		{
			returnValue = ChatColor.RED + "NOT SET" + ChatColor.WHITE;
		}
		else
		{
			returnValue = ChatColor.GREEN + target.toString().replaceFirst("Craft", "") + ChatColor.WHITE;
		}
		return returnValue;
	}
	
	TargetLabel(Spells iplugin, Player iplayer)
	{
		super("Current Target: ");
		plugin = iplugin;
		player = iplayer;
		this.setAlign(WidgetAnchor.TOP_RIGHT); //Align the text against the top right corner of the Label
		this.setAnchor(WidgetAnchor.TOP_RIGHT); //Align the Label against the top right corner of the screen.
		this.setDirty(true);
		((SpoutPlayer)player).getMainScreen().attachWidget(plugin,this);
	}
	
	public void updateLabel()
	{
		this.setText("Current Target: " + plugin.getTarget(player).toString()).setDirty(true);
	}
	
	public void onTick()
	{
		if(plugin.getTarget(player) != null)
		{
			if(getDistance(player.getLocation(), plugin.getTarget(player).getLocation()) > 30) // The player is too far away from their target.
			{
				plugin.setTarget(player, null);
			}
			else if(plugin.getTarget(player).isDead())
			{
				plugin.setTarget(player, null);
			}
		}
		
		this.setText("Current Target: " + printLabel(plugin.getTarget(player))).setDirty(true);
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
