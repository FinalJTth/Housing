package com.finaljtth.housing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Gui {
	
	public static Inventory main_gui;
	public static Inventory list_gui;
	
	public static ItemStack createItem(String name, ArrayList<String> desc, Material mat) {
        ItemStack i = new ItemStack(mat, 1);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(name);
        iMeta.setLore(desc);
        i.setItemMeta(iMeta);
        return i;
    }
	
	public static ItemStack createSkullItem(Player player, String suffix) {
		String playerName = player.getName();
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
        SkullMeta sMeta = (SkullMeta) skull.getItemMeta();
        skull.setDurability((short)3);
        sMeta.setOwningPlayer(player);
        sMeta.setDisplayName(playerName + suffix);
        skull.setItemMeta(sMeta);
        return skull;
    }
	
	public static void playerListPage(String suffix) {
		list_gui = Bukkit.createInventory(null, 54, "Player List");
		int slot = 10;
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			list_gui.setItem(slot, createSkullItem(player, suffix));
			slot++;
			if (slot == 17){
				slot = 19;
			}
			if (slot == 26){
				slot = 28;
			}
			if (slot == 35){
				slot = 37;
			}
		}
    }
    
    public static void mainPage(Player player) {
    	boolean buildmode = Boolean.valueOf(DataJSON.readJSON("Data", File.separator, player.getName(), "buildmode"));
    	main_gui = Bukkit.createInventory(null, 45, "Housing");
    	main_gui.setItem(18, createItem(ChatColor.GREEN + "Go to your own house", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Travel via teleportation to go to||" + "your own house.")), Material.BIRCH_DOOR));
    	main_gui.setItem(27, createItem(ChatColor.GREEN + "Visit someone else's house", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Travel via teleportation to visit||" + "other's people house.")), Material.SPRUCE_DOOR));
    	main_gui.setItem(36, createItem(ChatColor.GREEN + "Select game to play", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Choose a game to play or," + "go back to main lobby.")), Material.COMPASS));
        main_gui.setItem(38, createItem(ChatColor.GREEN + "House Privacy Settings", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Allow who to visit your house.")), Material.IRON_DOOR));
        main_gui.setItem(40, createItem(ChatColor.GREEN + "House Settings", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Edit various of the house setting.")), Material.DIODE));
        main_gui.setItem(41, createItem(ChatColor.GREEN + "Group", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Edit group and their permission.")), Material.PAPER));
        main_gui.setItem(42, createItem(ChatColor.GREEN + "Player List", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "List all players inside your house.")), Material.BOOK_AND_QUILL));
        main_gui.setItem(44, createItem(ChatColor.GREEN + "Clear Inventory", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Clicking this will clear your inventory.")), Material.HOPPER));
    	if (buildmode == true) {
    		main_gui.setItem(17, createItem(ChatColor.GREEN + "Current Mode : Build Mode", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "You are in " + ChatColor.AQUA +"&bBuild Mode||" + ChatColor.DARK_GRAY +"The world cannot be modified.")), Material.STONE_PICKAXE));
    	}
    	else {
    		main_gui.setItem(17, createItem(ChatColor.GREEN + "Current Mode : Default Mode", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "You are in " + ChatColor.AQUA +"&bDefault Mode||" + ChatColor.DARK_GRAY +"The world can be modified.")), Material.STONE_PICKAXE));
    	}
    }
    
    public static InventoryView openGui(Player player, Inventory inv) {
        return player.openInventory(inv);
    }
    
    public static void buildMode(Player player, int num) {
    	if (num == 0) {
        	player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
        	DataJSON.writeJSON("Data", File.separator, player.getName(), "buildmode", "true");
        	player.sendMessage(ChatColor.GREEN + "You are changing to " + ChatColor.AQUA + "Build Mode.");
        	player.sendMessage(ChatColor.GRAY + "Use \"/housebuildmode\" to deactivate it");
        	player.sendMessage(ChatColor.GRAY + "The world can now be modified");
        }
        if (num == 1) {
        	player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
        	DataJSON.writeJSON("Data", File.separator, player.getName(), "buildmode", "false");
        	player.sendMessage(ChatColor.GREEN + "You are changing to " + ChatColor.AQUA + "Default Mode.");
        	player.sendMessage(ChatColor.GRAY + "Use \"/housebuildmode\" to reactivate it.");
        	player.sendMessage(ChatColor.GRAY + "The world cannot be modified");
        }
    }
    
}
