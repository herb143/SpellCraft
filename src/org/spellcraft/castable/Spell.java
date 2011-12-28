package org.spellcraft.castable;

import org.bukkit.ChatColor;

public interface Spell {

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getShortName();

	public abstract boolean usesTargeting();

	public abstract String printRequiredItems();

	public abstract String abilityFormat();

	public abstract String abilityFormat(boolean withParentheses);

	public abstract String abilityFormat(ChatColor colorFalse, ChatColor colorTrue);

	public abstract String abilityFormat(boolean withParentheses, boolean targetMarking);

	public abstract String abilityFormat(boolean withParentheses, ChatColor colorFalse, ChatColor colorTrue, boolean targetMarking);
	
	public abstract void callSpell();

}