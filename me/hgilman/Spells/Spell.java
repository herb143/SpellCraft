package me.hgilman.Spells;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Spell {
	
	protected int range; // TODO: Add range-enhancers.
	protected static Spells plugin;
	protected Player player; // Whoever owns the spellbook this is in.
	
	public static Spells getPlugin() { return plugin; }
	public Player getPlayer() { return player; }

	public Spell(Player playerinstance, Spells instance, String name, String desc, ItemStack... items)
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
				shortName.concat(String.valueOf(name.charAt(iii)));
			}
		}
		
	}
	private ArrayList<ItemStack> requiredItems = new ArrayList<ItemStack>(); // The required items.
	
	public ArrayList<ItemStack> getRequiredItems() { return requiredItems; }
	
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
	
	public String printRequiredItems()
	{
		if (requiredItems.size() > 0) // Assuming there will be something to write.
		{
		String returnValue = "";
		for (int i=0;i<requiredItems.size();i++) // TODO: Replace with iterator.
		{
			returnValue = returnValue.concat(" ").concat(String.valueOf(requiredItems.get(i).getAmount()));
			returnValue = returnValue.concat(" ").concat(formatMaterialName(Material.getMaterial(requiredItems.get(i).getTypeId())));
			if (requiredItems.size()-1!=i) // We're not on the last one
			{
				returnValue = returnValue.concat(",");
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
		String formattedName = "";
		if(hasRequiredItems())
		{
			formattedName = formattedName + ChatColor.DARK_GREEN;
			if(withParentheses) { formattedName = formattedName + "("; }
			formattedName = formattedName + spellName;
			if(withParentheses) { formattedName = formattedName + ")"; }
			formattedName = formattedName + ChatColor.WHITE;
			return formattedName;
		}
		else
		{
			formattedName = formattedName + ChatColor.DARK_RED;
			if(withParentheses) { formattedName = formattedName + "("; }
			formattedName = formattedName + spellName;
			if(withParentheses) { formattedName = formattedName + ")"; }
			formattedName = formattedName + ChatColor.WHITE;
			return formattedName;
		}
	}
	
	public void setRequiredItems(ArrayList<ItemStack> items) { requiredItems = items; } // If we're lucky enough to have the arraylist...
	
	public void setRequiredItems(ItemStack... items) // If we have a conventional array instead...
	{
		for (int i = 0; i < items.length; i++)
		{
			requiredItems.add(items[i]); // Scroll through and add every item.
		}
	}
	
	public void addRequiredItem(ItemStack item)
	{
		requiredItems.add(item);
	}
	
	public boolean hasRequiredItems()
	{
		PlayerInventory inventory = player.getInventory();
		for (int iii=0;iii<requiredItems.size();iii++) // Loop through requiredItems.
		{
			if(!inventory.contains(requiredItems.get(iii).getType(),requiredItems.get(iii).getAmount()))
			{
				return false; // If any of them are missing
			}
		}
		return true; // We've made it this far.
	}
	
	public boolean removeRequiredItem(int index) // Index is which item in the requiredItems arraylist we must remove.
	{
		if (requiredItems.size()>index) // Not OOB.
		{
			return removeItem(requiredItems.get(index))==requiredItems.get(index).getAmount(); // If it removed all of the required item, return true. Else, false.
		}
		return false; // If it's out of bounds, return false.
	}
	
	public boolean removeRequiredItems()
	{
		if(hasRequiredItems()) // They must have all the items or we'll run into some messy errors.
		{
			for (int ii=0;ii<requiredItems.size();ii++) // TODO: Replace with iterator.
			{
				if(!removeRequiredItem(ii))
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
	
	public void damageItem(int index, int amount)
	{
		PlayerInventory inventory = player.getInventory();
		ItemStack item = inventory.getItem(index);
		item.setDurability((short)(item.getDurability()+amount)); // Set the durability + amount...
		if (item.getDurability() >= item.getType().getMaxDurability()) { inventory.removeItem(item); } // It's all used up.
	}
	public void damageItem(Material material, int amount) // Same, but using material instead of the index.
	{
		PlayerInventory inventory = player.getInventory();
		ItemStack item = inventory.getItem(inventory.first(material));
		item.setDurability((short)(item.getDurability()+amount)); // Set the durability + amount...
		if (item.getDurability() >= item.getType().getMaxDurability()) { inventory.removeItem(item); } // It's all used up.
	}
	
	public int replaceItem(ItemStack original, ItemStack replacer)
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
	
	public boolean addItem(ItemStack item)
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
	public void callSpell() // The spellbook calls this.
	{
		if (hasRequiredItems())
		{
			castSpell();
		}
		else
		{
			player.sendMessage("Could not cast! Spell requires" + printRequiredItems() + "!");
		}
	}
	
	protected void castSpell() // This is what actually casts the spell.
	{
		player.sendMessage("Sorry. The developers messed up. You should probably tell them about this error.");
	}
	
	protected double getDistance(Location locA, Location locB) // Our lovely distance formula.
	{
		double xdiff = locA.getX() - locB.getX();
		double ydiff = locA.getZ() - locB.getZ();
		double xdiffsq = xdiff * xdiff;
		double ydiffsq = ydiff * ydiff;
		double xyadd = xdiffsq + ydiffsq;
		return Math.sqrt(xyadd);
	}


}
