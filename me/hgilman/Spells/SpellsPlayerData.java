package me.hgilman.Spells;


import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SpellsPlayerData {
	
	private static Spells plugin;
	private Player player;
	private SpellBook spellBook;
	private LivingEntity currentTarget;
	private SpellsInfoLabel infoLabel;
	private boolean clickToCast;
	
	public SpellsPlayerData(Spells instance,Player pinstance)
	{
		plugin = instance;
		player = pinstance;
		spellBook = new SpellBook(player,plugin);
		currentTarget = null;
		infoLabel = new SpellsInfoLabel(plugin,player);
		clickToCast = false; // False by default.
	}

	public SpellBook getSpellBook() { return spellBook; }
	public LivingEntity getTarget() { return currentTarget; }
	public boolean hasTarget() { if(currentTarget==null) { return false; } else { return true; } }
	public SpellsInfoLabel getInfoLabel() { return infoLabel; }
	public boolean isClickToCast() { return clickToCast; }
	
	public void setTarget(LivingEntity currentTarget) { this.currentTarget = currentTarget; }
	public void setClickToCast(boolean clickToCast) { this.clickToCast = clickToCast; }
	
	
	
}
