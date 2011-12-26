package me.hgilman.Spells;

import java.util.ArrayList;

import me.hgilman.Spells.Library.*;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpellBook {
	private Spells plugin;
	private Player player;
	
	public SpellBook(Player playerinstance, Spells instance)
	{
		plugin = instance;
		player = playerinstance;

		registerSpell(new DecoySpell(30,5,CreatureType.CHICKEN,10,player,plugin,"Basic Decoy","All mobs within 5 blocks will attack this weak decoy.",new ItemStack(Material.RAW_CHICKEN,2),new ItemStack(Material.EGG,4)));
		registerSpell(new DecoySpell(30,10,CreatureType.SHEEP,20,playerinstance,instance,"Enhanced Decoy","All mobs within 10 blocks will attack this enhanced decoy.",new ItemStack(Material.WOOL,5),new ItemStack(Material.BONE,1)));
		registerSpell(new DecoySpell(30,15,CreatureType.COW,30,playerinstance,instance,"Super Decoy","All mobs within 15 blocks will attack this very strong decoy.",new ItemStack(Material.LEATHER,4),new ItemStack(Material.RAW_BEEF,2),new ItemStack(Material.BONE,3)));
		registerSpell(new RapidfireSpell(player,plugin));
		registerSpell(new SpikeSpell(player, plugin, SpikeSpell.SpellType.BASIC, 30, "Spike","Builds a cactus instantaeneously.",new ItemStack(Material.CACTUS, 4),new ItemStack(Material.SAND,1)));
		registerSpell(new SpikeSpell(player, plugin, SpikeSpell.SpellType.WALL, 30, "Spike Wall","Builds a wall of cacti instantaeneously.",new ItemStack(Material.CACTUS, 35),new ItemStack(Material.SAND,9),new ItemStack(Material.SANDSTONE,8)));
		registerSpell(new SpikeSpell(player, plugin, SpikeSpell.SpellType.FORT, 30, "Spike Fort","Builds a fortification of cacti instantaeneously.",new ItemStack(Material.CACTUS, 64),new ItemStack(Material.SAND,25),new ItemStack(Material.SANDSTONE,20)));
		registerSpell(new NetSpell(plugin,player));
	}
	
	ArrayList<Spell> spellRegistry = new ArrayList<Spell>();
	private int index = 0; // The current spell.
	
	private void registerSpell(Spell spell)
	{
		spellRegistry.add(spell);
	}
	
	public Spell getCurrentSpell() { return spellRegistry.get(index); }
	
	public void nextSpell() // Scrolls to the next spell and notifies the player.
	{
		if (index!=spellRegistry.size()-1) {index++;} // If we're not on the last one, advance.
		else { index = 0; } // Or go back to start.

		player.sendMessage("Selected spell: " + getCurrentSpell().abilityFormat()); // tell us what our current spell is now.
	}
	
	public Spell getSpell(String spellName)
	{
		for(int iii=0;iii<spellRegistry.size();iii++)
		{
			if(spellRegistry.get(iii).getShortName().equalsIgnoreCase(spellName))
			{
				return spellRegistry.get(iii);
			}
		}
		return null; // If we didn't find anything that matched.
	}
	
}
