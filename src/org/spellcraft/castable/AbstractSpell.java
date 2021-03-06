package org.spellcraft.castable;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.spellcraft.SpellCraft;

public abstract class AbstractSpell implements Spell {

	private int range; // TODO: Add range-enhancers.
	private static SpellCraft plugin;
	private Player player; // Whoever owns the spellbook this is in.
	
	protected Player getPlayer() { return player; }
	public SpellCraft getSpellCraft() { return plugin; }
	protected int getRange() { return range; }

	public AbstractSpell(Player playerinstance, SpellCraft instance, String name, String desc, ItemStack... items)
	{
		plugin = instance;
		player = playerinstance;

		setRequiredItems(items); // Set from the array.

		spellName = name;
		spellDescription = desc + " Needs" + printRequiredItems();

		shortName = "";

		for(int iii=0;iii<name.length();iii++) // Make the short name the Name, but without spaces.
		{
			if(name.charAt(iii)!=' ') // If it's not a space.
			{
				shortName = shortName + String.valueOf(name.charAt(iii));
			}
		}

	}
	private ArrayList<ItemStack> requiredItems = new ArrayList<ItemStack>(); // The required items.
	
	protected ArrayList<ItemStack> getRequiredItems() { return requiredItems; }

	// The spell name, description, and shortname:
	private String spellName;
	public String getName() { return spellName; }
	private String spellDescription;	
	public String getDescription() { return spellDescription; }
	private String shortName;	
	public String getShortName() { return shortName; }
	private String formatMaterialName(Material material) // Makes material names look nice.
	{
		String name = material.toString();
		name = name.replace('_', ' ');
		name = name.toLowerCase();
		return name;

	}	
	public boolean usesTargeting() 
	{ 
		if(this instanceof TargetingSpell)
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
	public String printRequiredItems()
	{
		if (requiredItems.size() > 0) // Assuming there will be something to write.
		{
			String returnValue = "";
			Iterator<ItemStack> itemsIterator = requiredItems.iterator();
			while(itemsIterator.hasNext())
			{
				ItemStack currentRequirement = itemsIterator.next();
				returnValue = returnValue + " " + String.valueOf(currentRequirement.getAmount());
				returnValue = returnValue + " " +  formatMaterialName(Material.getMaterial(currentRequirement.getTypeId()));
				if (itemsIterator.hasNext()) // We're not on the last one
				{
					returnValue = returnValue + ",";
				}
			}

			return returnValue;
		}
		return ""; // If there are not requirements (probably won't happen?)
	}

	// Inventory functions:
	public String abilityFormat()
	{
		return abilityFormat(false);
	}

	
	public String abilityFormat(boolean withParentheses)
	{
		return abilityFormat(withParentheses,ChatColor.DARK_RED,ChatColor.DARK_GREEN,false);
	}

	
	public String abilityFormat(ChatColor colorFalse,ChatColor colorTrue)
	{
		return abilityFormat(false,colorFalse,colorTrue,false);
	}

	
	public String abilityFormat(boolean withParentheses,boolean targetMarking)
	{
		return abilityFormat(withParentheses,ChatColor.DARK_RED,ChatColor.DARK_GREEN,targetMarking);
	}

	
	public String abilityFormat(boolean withParentheses,ChatColor colorFalse,ChatColor colorTrue,boolean targetMarking)
	{
		String formattedName = "";
		if(hasRequiredItems())
		{
			formattedName = formattedName + colorTrue;
			if(withParentheses) { formattedName = formattedName + "("; }
			formattedName = formattedName + spellName;
			if(targetMarking && this.usesTargeting()) { formattedName = formattedName + ChatColor.GOLD + " (T)"; }
			if(withParentheses) { formattedName = formattedName + ")"; }
			formattedName = formattedName + ChatColor.WHITE;
			return formattedName;
		}
		else
		{
			formattedName = formattedName + colorFalse;
			if(withParentheses) { formattedName = formattedName + "("; }
			formattedName = formattedName + spellName;
			if(targetMarking && this.usesTargeting()) { formattedName = formattedName + ChatColor.GOLD + " (T)"; }
			if(withParentheses) { formattedName = formattedName + ")"; }
			formattedName = formattedName + ChatColor.WHITE;
			return formattedName;
		}
	}
	
	private void setRequiredItems(ItemStack... items) // If we have a conventional array instead...
	{
		for (ItemStack item : items)
		{
			requiredItems.add(item); // Scroll through and add every item.
		}
	}

	protected boolean hasRequiredItems()
	{
		PlayerInventory inventory = player.getInventory();

		for (ItemStack currentRequirement : requiredItems) // Loop through requiredItems.
		{
			if(!inventory.contains(currentRequirement.getType(),currentRequirement.getAmount()))
			{
				return false; // If any of them are missing
			}
		}
		return true; // We've made it this far.
	}

	
	protected boolean removeRequiredItem(int index) // Index is which item in the requiredItems arraylist we must remove.
	{
		if (requiredItems.size()>index) // Not OOB.
		{
			return removeItem(requiredItems.get(index))==requiredItems.get(index).getAmount(); // If it removed all of the required item, return true. Else, false.
		}
		return false; // If it's out of bounds, return false.
	}


	protected boolean removeRequiredItems()
	{
		if(hasRequiredItems()) // They must have all the items or we'll run into some messy errors.
		{
			for (int iii=0;iii<requiredItems.size();iii++) // We are actually using iii here, so don't correct this loop.
			{
				if(!removeRequiredItem(iii))
				{
					return false; // If the remove ever fails.
				}
			}
			return true; // It made it through all of them.
		}
		return false; // Something didn't work out.
	}


	
	public int removeItem(ItemStack item) { // Removes an itemstack from the inventory. Use this for quantities of items.
		PlayerInventory inventory = player.getInventory();
		int amountLeft=item.getAmount();
		while(amountLeft>0) // We still have more to remove...
		{
			int firstFound=inventory.first(item.getType()); // The next stack with this item in it.
			if(inventory.getItem(firstFound).getAmount()>amountLeft) // One stack has more than enough...
			{
				inventory.getItem(firstFound).setAmount(inventory.getItem(firstFound).getAmount()-amountLeft); // Remove the item from it.
				amountLeft=0; // We have no more to remove.
				return item.getAmount(); // Return how many we removed (all of them.)
			}
			else if (inventory.getItem(firstFound).getAmount()==amountLeft) // If there's exactly enough...
			{
				inventory.clear(firstFound); // Clear the spot.
				amountLeft=0; // We have no more to remove.
				return item.getAmount(); // Return how many we removed (all of them).
			}
			else if (inventory.getItem(firstFound).getAmount()<amountLeft) // If there's not enough to take out everything.
			{
				amountLeft-=inventory.getItem(firstFound).getAmount(); // Let's remove however many we can from a stack.
				inventory.clear(firstFound); // Clear the spot as much as we can.
			}
			else // There's no more to remove...
			{
				return item.getAmount()-amountLeft; // Return how many we managed to remove.
			}
		}
		return -1; // Some weird value.

	}

	
	protected void damageItem(int index, int amount)
	{
		PlayerInventory inventory = player.getInventory();
		ItemStack item = inventory.getItem(index);
		item.setDurability((short)(item.getDurability()+amount)); // Set the durability + amount...
		if (item.getDurability() >= item.getType().getMaxDurability()) { inventory.removeItem(item); } // It's all used up.
	}

	
	protected void damageItem(Material material, int amount) // Same, but using material instead of the index.
	{
		PlayerInventory inventory = player.getInventory();
		ItemStack item = inventory.getItem(inventory.first(material));
		item.setDurability((short)(item.getDurability()+amount)); // Set the durability + amount...
		if (item.getDurability() >= item.getType().getMaxDurability()) { inventory.removeItem(item); } // It's all used up.
	}


	
	protected int replaceItem(ItemStack original, ItemStack replacer)
	{
		PlayerInventory inventory = player.getInventory();
		int slot = inventory.first(original);
		if(slot != -1)
		{
			inventory.setItem(slot,replacer); 
			return slot; // Return the slot where it was replaced.
		}
		else
		{
			return -1;
		}
	}


	
	protected boolean addItem(ItemStack item)
	{
		PlayerInventory inventory = player.getInventory();
		if(inventory.firstEmpty()!=-1) // There is space
		{
			inventory.setItem(inventory.firstEmpty(), item);
			return true;
		}
		else
		{
			return false; // It didn't work.
		}
	}

	// Spell casting functions:

	
	public abstract void callSpell(); // The spellbook calls this.

	protected abstract void castSpell(LivingEntity target); // This is what actually casts the spell.

	protected abstract void castSpell(); // This is what actually casts the spell.

	protected double getDistance(Location locA, Location locB)
	{
		return getDistance(locA,locB,false); // By default it is false
	}

	protected double getDistance(Location locA, Location locB,boolean threeDimensional) // Our lovely distance formula.
	{
		if (!threeDimensional)
		{
			double xdiff = locA.getX() - locB.getX();
			double ydiff = locA.getZ() - locB.getZ();
			double xdiffsq = xdiff * xdiff;
			double ydiffsq = ydiff * ydiff;
			double xyadd = xdiffsq + ydiffsq;
			return Math.sqrt(xyadd);
		}
		else
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

	protected boolean isOf(Entity entity, Class<?>... classes)
	{
		for(Class<?> currentClass : classes)
		{
			if(entity.getClass() == currentClass)
			{
				return true;
			}
		}
		return false;
	}


}
