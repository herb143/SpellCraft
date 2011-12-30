package org.spellcraft;


import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.spellcraft.gui.ScInfoLabel;
/**
 * Represents the data held by SpellCraft for a player
 * @author hgilman
 *
 */
public class PlayerData {
	private SpellCraft plugin;
	private Player player;
	private SpellBook spellBook;
	private LivingEntity currentTarget;
	private ScInfoLabel infoLabel;
	private boolean clickToCast;
	/**
	 * 
	 * @param plugin The SpellCraft plugin instance to which this object belongs
	 * @param player The player to whom this object belongs
	 */
	public PlayerData(SpellCraft plugin,Player player)
	{
		this.plugin = plugin;
		this.player = player;
		spellBook = new SpellBook(this.player,this.plugin);
		currentTarget = null;
		infoLabel = new ScInfoLabel(this.plugin,this.player);
		clickToCast = false; // False by default.
	}
	
	/**
	 * 
	 * @return The collection of spells currently available to the player
	 */
	public SpellBook getSpellBook() { return spellBook; }
	/**
	 * 
	 * @return The player's current target
	 */
	public LivingEntity getTarget() { return currentTarget; }
	public boolean hasTarget() { if(currentTarget==null) { return false; } else { return true; } }
	/**
	 * 
	 * @return The info label in the corner of the player's screen
	 */
	public ScInfoLabel getInfoLabel() { return infoLabel; }
	/**
	 * 
	 * @return True if ClickToCast is enabled, false if ClickToCast is disabled
	 */
	public boolean isClickToCast() { return clickToCast; }
	
	/**
	 * Sets the player's current target.
	 * @param currentTarget The entity for the player to target
	 */
	public void setTarget(LivingEntity currentTarget) { this.currentTarget = currentTarget; }
	/**
	 * Enables or disables ClickToCast
	 * @param clickToCast
	 */
	public void setClickToCast(boolean clickToCast) { this.clickToCast = clickToCast; }
	
	
	
}
