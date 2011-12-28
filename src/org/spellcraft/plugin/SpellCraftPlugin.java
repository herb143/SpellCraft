package org.spellcraft.plugin;

import org.getspout.spoutapi.plugin.SpoutPlugin;
import org.spellcraft.SpellCraft;

public abstract class SpellCraftPlugin extends SpoutPlugin
{
	
	public SpellCraft getSpellCraftPlugin()
	{
		return (SpellCraft) this.getSpoutServer().getPluginManager().getPlugin("SpellCraft");
	}
}
