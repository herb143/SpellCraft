package me.hgilman.Spells;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class SpellsInfoLabel extends GenericLabel {

	private Player player;
	private static Spells plugin;

	String printLabel(LivingEntity target)
	{
		String returnValue = "";

		if (target==null)
		{
			returnValue = ChatColor.RED + "NOT SET" + ChatColor.WHITE;
		}
		else
		{
			if(target instanceof CraftPlayer)
			{
				returnValue = ChatColor.GREEN + target.toString().replaceFirst("CraftPlayer\\{name=", "").replaceFirst("\\}", "") + ChatColor.WHITE;
			}
			else
			{
				returnValue = ChatColor.GREEN + target.toString().replaceFirst("Craft", "") + ChatColor.WHITE;
			}
		}
		return returnValue;
	}

	private String sClickToCast()
	{
		if(plugin.getPlayerData(player).isClickToCast())
		{
			return ChatColor.GREEN + "enabled" + ChatColor.WHITE;
		}
		else
		{
			return ChatColor.RED + "disabled" + ChatColor.WHITE;
		}
	}


	SpellsInfoLabel(Spells iplugin, Player iplayer)
	{
		super("Loading info from Spells v2.0 database...");
		plugin = iplugin;
		player = iplayer;
		this.setAlign(WidgetAnchor.BOTTOM_RIGHT); //Align the text against the top right corner of the Label
		this.setAnchor(WidgetAnchor.BOTTOM_RIGHT); //Align the Label against the top right corner of the screen.

		this.setDirty(true);
		((SpoutPlayer)player).getMainScreen().attachWidget(plugin,this);
	}

	public void onTick()
	{
		SpellsPlayerData playerData = plugin.getPlayerData(player);

		LivingEntity target = playerData.getTarget();
		if(playerData.getTarget() != null)
		{
			if(getDistance(player.getLocation(), target.getLocation()) > 30) // The player is too far away from their target.
			{
				playerData.setTarget(null);
			}
			else if(target.isDead())
			{
				playerData.setTarget(null);
			}
		}

		if (playerData.getSpellBook().getCurrentSpell().usesTargeting())
		{
			this.setText("Target: " + printLabel(target) + "\nSpell: " + playerData.getSpellBook().getCurrentSpell().abilityFormat(ChatColor.RED,ChatColor.GREEN) + "\nClickToCast " + sClickToCast()).setDirty(true);
		}
		else
		{
			this.setText("Spell: " + playerData.getSpellBook().getCurrentSpell().abilityFormat(ChatColor.RED,ChatColor.GREEN) + "\nClickToCast " + sClickToCast()).setDirty(true);
		}
		if(new SpoutItemStack(player.getItemInHand()).isCustomItem())
		{
			this.setVisible(true).setDirty(true);
		}
		else
		{
			this.setVisible(false).setDirty(true);
		}
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
