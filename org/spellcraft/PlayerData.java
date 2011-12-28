package org.spellcraft;


import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.spellcraft.gui.ScInfoLabel;

public class PlayerData {
	
	private static SpellCraft plugin;
	private Player player;
	private SpellBook spellBook;
	private LivingEntity currentTarget;
	private ScInfoLabel infoLabel;
	private boolean clickToCast;
	
	public PlayerData(SpellCraft instance,Player pinstance)
	{
		plugin = instance;
		player = pinstance;
		spellBook = new SpellBook(player,plugin);
		currentTarget = null;
		infoLabel = new ScInfoLabel(plugin,player);
		clickToCast = false; // False by default.
	}

	public SpellBook getSpellBook() { return spellBook; }
	public LivingEntity getTarget() { return currentTarget; }
	public boolean hasTarget() { if(currentTarget==null) { return false; } else { return true; } }
	public ScInfoLabel getInfoLabel() { return infoLabel; }
	public boolean isClickToCast() { return clickToCast; }
	
	public void setTarget(LivingEntity currentTarget) { this.currentTarget = currentTarget; }
	public void setClickToCast(boolean clickToCast) { this.clickToCast = clickToCast; }
	
	
	
}
