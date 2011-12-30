package org.spellcraft;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.spellcraft.castable.Spell;
/**
 * Represents the ordered collection of spells available to a player
 * @author hgilman
 *
 */
public class SpellBook {
	private static SpellCraft plugin;
	private Player player;
	/**
	 * 
	 * @param player The player to whom the SpellBook belongs
	 * @param plugin The SpellCraft plugin instance to which the SpellBook belongs
	 */
	public SpellBook(Player player, SpellCraft plugin)
	{
		SpellBook.plugin = plugin;
		this.player = player;
		
		for(Class<?> spellClass : plugin.getSpellList())
		{
			Spell newSpell;
			try {
				newSpell = (Spell) spellClass.getConstructor(SpellCraft.class,Player.class).newInstance(plugin,player);
			} catch (IllegalArgumentException e) {
				newSpell = null;
				e.printStackTrace();
			} catch (SecurityException e) {
				newSpell = null;
				e.printStackTrace();
			} catch (InstantiationException e) {
				newSpell = null;
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				newSpell = null;
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				newSpell = null;
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				newSpell = null;
				e.printStackTrace();
			}
			if(newSpell != null)
			{
			registerSpell(newSpell);
			}
			
		}
	}
	
	private ArrayList<Spell> spellRegistry = new ArrayList<Spell>();
	private int index = 0; // The current spell.
	
	/**
	 * 
	 * @return The ArrayList of spells available to the player
	 */
	public ArrayList<Spell> getRegistry() { return spellRegistry; }
	
	private void registerSpell(Spell spell)
	{
		spellRegistry.add(spell);
	}
	
	/**
	 * 
	 * @return The currently selected spell in the SpellBook
	 */
	public Spell getCurrentSpell() { return spellRegistry.get(index); }
	
	/**
	 * 
	 * @param spell The spell to select. Must be in the SpellBook.
	 */
	public void setCurrentSpell(Spell spell)
	{
		index = spellRegistry.indexOf(spell);
	}
	
	/**
	 * Scroll to the next spell available to the player
	 */
	public void nextSpell() // Scrolls to the next spell and notifies the player.
	{
		if (index!=spellRegistry.size()-1) {index++;} // If we're not on the last one, advance.
		else { index = 0; } // Or go back to start.
		if (!getCurrentSpell().usesTargeting())
		{
			plugin.getPlayerData(player).setTarget(null); // We lose targets when scrolling throuhg non-targeting spells.
		}
	}
	
	/**
	 * Searches available spells by name
	 * @param spellName The name of the spell to search for
	 * @return The spell, if any, matching the given string. If none, the function will return null.
	 */
	public Spell getSpell(String spellName)
	{
		for(Spell currentSpell : spellRegistry)
		{
			if(currentSpell.getShortName().equalsIgnoreCase(spellName))
			{
				return currentSpell;
			}
		}
		return null; // If we didn't find anything that matched.
	}
	
}
