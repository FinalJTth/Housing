/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.finaljtth.housing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

/**
 *
 * @author NEXT Speed
 */
public class Main extends JavaPlugin implements Listener {
	
	public static org.bukkit.plugin.Plugin plugin = null;
	
    FileConfiguration config = getConfig();
    
    @Override
    public void onEnable() {
    	getLogger().info("Initializing plugin ...");
    	plugin = this;
        getLogger().info("Creating Configuration file ...");
        DataJSON.setupJSON(this);
        //Creating Configuration File on onEnable
        config.addDefault("MotD", true);     //Generate a config.yml with the option 'MotD'. It will be set to true by default.
        config.addDefault("World.world_maximum", 1);
        config.options().copyDefaults(true);        //
        saveConfig();                               //This will save the configuration file. Make sure to put it on all plugin with configuration file.
        getLogger().info("Player Housing 0.1 Alpha by FinalJTth.");
        //Enable this class to check for the new player using onPlayerJoin()
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    	getLogger().info("Plugin successfully disable");
    }
    
    public String timeToString(Player player){
        long time = player.getWorld().getTime();
        int hours = (int) Math.floor(time / 1000 + 8);
        int minutes = (int) ((time % 1000) / 1000.0 * 60);
        return String.format("%02d:%02d", hours, minutes);
    }
    
    public void motd(Player player) {
        PluginDescriptionFile pluginInfo = getDescription();
        String pd = pluginInfo.getDescription(); 
        String sn = Bukkit.getServer().getName(), pn = player.getName();
        int playeronline = Bukkit.getOnlinePlayers().size();
        if (config.getBoolean("MotD")) {             //Get Boolean from configuration file name "ShowAwesome"
            player.sendMessage(ChatColor.GREEN + "Welcome " + pn + " to " + sn + " !");
            player.sendMessage(ChatColor.GREEN + "Input " + ChatColor.AQUA + "/help" + ChatColor.GREEN + " for list of available commands.");
            player.sendMessage(ChatColor.GREEN + "Input " + ChatColor.AQUA + "/list" + ChatColor.GREEN + " for names of player currently online");
            player.sendMessage(ChatColor.GREEN + "Player Online : " + ChatColor.AQUA + playeronline + ChatColor.GREEN + " World Time : " + ChatColor.AQUA + timeToString(player));
            if (player.isOp()){
                player.sendMessage("" + ChatColor.RED + pd);
                player.sendMessage(ChatColor.RED + "Simplest Example of MotD, Unconfigurable in config");
            }
        }
    }
    
    public static MultiverseCore getMultiverseCore() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
 
        if (plugin instanceof MultiverseCore) {
            return (MultiverseCore) plugin;
        }
 
        throw new RuntimeException("MultiVerse not found!");
    }
        
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {       //On the argument, creating object "event" from class PlayerJoinEvent
        Player player = event.getPlayer();                  //Create object player, get Player entity that joined the server
        Inventory inv = player.getInventory();
        if (DataJSON.readJSON("Data", File.separator, player.getName(), "") == null){
        	DataJSON.writeJSON("Data", File.separator, player.getName(), "buildmode", "false");
        	DataJSON.writeJSON("Data", File.separator, player.getName(), "worldcount", "0");
        }
        ItemStack item = Gui.createItem(ChatColor.GREEN + "House GUI", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Right click to open House GUI")), Material.NETHER_STAR);
        if (!player.getInventory().contains(item)) {
        	inv.setItem(34, Gui.createItem(ChatColor.GREEN + "House GUI", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Right click to open House GUI")), Material.NETHER_STAR));
        }
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
    	event.getPlayer().sendMessage(ChatColor.RED + "[DEBUG] BlockPlaceEvent was executed");
    	Player player = event.getPlayer();
    	boolean buildmode = Boolean.valueOf(DataJSON.readJSON("Data", File.separator, player.getName(), "buildmode"));
    	String currentworld = player.getWorld().getName();
    	String housingworld = player.getName() + "_housing";
    	player.sendMessage("[DEBUG] Value of buildmode is " + String.valueOf(buildmode));
    	player.sendMessage("[DEBUG] Current world : " + currentworld);
    	player.sendMessage("[DEBUG] Player Housing world : " + housingworld);
    	player.sendMessage("[DEBUG] Value of buildmode is " + String.valueOf(buildmode));
    	if (player.getWorld().getName() == (player.getName() + "_housing")){
    		if (buildmode == false || player.hasPermission("housing.buildmode.bypass") == false) {
    			event.setCancelled(true);
    			player.sendMessage(ChatColor.RED + "You need to activate the build mode to do that.");
    		}
    	}
    	else {
    		if (player.isOp() == false || player.hasPermission("housing.buildrestricted.bypass") == false) {
    			event.setCancelled(true);
    			player.sendMessage(ChatColor.RED + "You can't place blocks in other player's world.");
    		}
    	}
    	return;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	Player player = event.getPlayer();
    	boolean buildmode = Boolean.valueOf(DataJSON.readJSON("Data", File.separator, player.getName(), "buildmode"));
    	if (player.getWorld().getName() == (player.getName() + "_housing")){
    		if (buildmode == false || player.hasPermission("housing.buildmode.bypass") == false) {
    			event.setCancelled(true);
    			player.sendMessage(ChatColor.RED + "You need to activate the build mode to do that.");
    		}
    	}
    	else {
    		if (player.isOp() == false || player.hasPermission("housing.buildrestricted.bypass") == false) {
    			event.setCancelled(true);
    			player.sendMessage(ChatColor.RED + "You can't break blocks in other player's world.");
    		}
    	}
    	return;
    }
    
    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event){
    	event.getPlayer().sendMessage(ChatColor.RED + "[DEBUG] PlayerInteractEvent was executed");
        if((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && event.getPlayer().getInventory().getItemInMainHand() != null) {
        	event.getPlayer().sendMessage(ChatColor.RED + "[DEBUG] PlayerInteractEvent was executed when passing through conditions");
        	ItemStack debugItem = event.getPlayer().getInventory().getItemInMainHand();
        	ItemMeta debugMeta = debugItem.getItemMeta();
        	event.getPlayer().sendMessage(ChatColor.RED + "[DEBUG] Right clicked with item " + ChatColor.WHITE + debugItem.getData().toString() + ChatColor.RED + " with name " + ChatColor.WHITE + debugMeta.getDisplayName());
        	Player player = event.getPlayer();
            ItemStack usedItem = player.getInventory().getItemInMainHand();
            ItemMeta meta = usedItem.getItemMeta();
        	if (meta.getDisplayName().contains("House GUI")) {
            	Gui.mainPage(player);
                Gui.openGui(player, Gui.main_gui);
            }
        	else {
            	return;
        	}
        }
        else {
        	return;
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {   
    	Inventory inv = Gui.main_gui;
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
  
        String invName = event.getInventory().getName();
        if (!invName.equals(inv.getName())) {
            return;
        }
  
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
      
        // What if the clicked item is null? Nullpointer, so return.
        if (clickedItem == null) {
            return;
        }

        // What if the clicked item doesn't have itemmeta? Null pointer, so return.
        if (!clickedItem.hasItemMeta()) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();

        // What if the clicked item has no display name? Null pointer, so return.
        if (!meta.hasDisplayName()) {
            return;
        }

        if (meta.getDisplayName().equals(ChatColor.GREEN + "Clear Inventory")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
            player.getInventory().clear();
            player.closeInventory();
            player.getInventory().setItem(34, Gui.createItem(ChatColor.GREEN + "House GUI", new ArrayList<String>(Arrays.asList(ChatColor.DARK_GRAY + "Right click to open House GUI")), Material.NETHER_STAR));
            return;
        }

        if (meta.getDisplayName().contains("Select game to play")) {
        	player.sendMessage("Test");
        	player.chat("/menu");
            return;
        }
        if (meta.getDisplayName().contains("Player List")) {
        	Gui.playerListPage("");
        	Gui.openGui(player, Gui.list_gui);
        	return;
        }
        if (meta.getDisplayName().contains("Visit someone else's house")) {
        	Gui.playerListPage("'s house");
        	Gui.openGui(player, Gui.list_gui);
        	return;
        }
        if (meta.getDisplayName().contains("Go to your own house")) {
        	World world = Bukkit.getServer().getWorld(player.getName() + "_housing");
    		player.teleport(new Location(world, -29, 32, 94));
        	return;
        }
        if (meta.getDisplayName().contains("Current Mode : Default Mode")) {
        	player.closeInventory();
        	Gui.buildMode(player, 0);
        	player.closeInventory();
        	return;
        }
        if (meta.getDisplayName().contains("Current Mode : Build Mode")) {
        	player.closeInventory();
        	Gui.buildMode(player, 1);
        	player.closeInventory();
        	return;
        }
        //Clicking on player head inside Visit someone else's house list
        if (meta.getDisplayName().contains("'s house")) {
        	SkullMeta sMeta = (SkullMeta) clickedItem.getItemMeta();
        	World world = Bukkit.getServer().getWorld(sMeta.getOwningPlayer().getName() + "_housing");
    		player.teleport(new Location(world, -29, 32, 94));
        	return;
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (cmd.getName().equalsIgnoreCase("motd")) { // If the player typed /motd then do the following...
        if (sender instanceof Player) {
        	Player player = (Player) sender;
        	motd(player);
            } 
        else {
        	sender.sendMessage("This command can only be run by a player.");
		}
        return true;
	}
    if(cmd.getName().equalsIgnoreCase("housing")){
    	Player player = (Player) sender;
    	//Housing - create
    	if (args[0].equalsIgnoreCase("create")) {
    		int worldcount = Integer.parseInt(DataJSON.readJSON("Data", File.separator,  player.getName(), "worldcount"));
    		int worldmax = plugin.getConfig().getInt("World.world_maximum");
    		if (player.hasPermission("housing.world.create") == false) {
   				player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
   			}
   			else if (args.length != 2) {
   				player.sendMessage(ChatColor.RED + "Missing player name argument.");
    		}
    		else if (DataJSON.readJSON("Data", File.separator, player.getName(), "worldcount") == null) {
				player.sendMessage(ChatColor.RED + "The player is not a valid player in the database");
			}
    		else if (worldcount >= worldmax) {
    			player.sendMessage(ChatColor.RED + "The player has reached the maximum number of world.");
    		}
    		else {
    			DataJSON.writeJSON("Data", File.separator, player.getName(), "worldcount", String.valueOf(worldcount+1));
            	HousingManager.cloningHousingWorld(player);
    		}
    	}
    	else if (args[0].equalsIgnoreCase("delete")) {
    		int worldcount = Integer.parseInt(DataJSON.readJSON("Data", File.separator,  player.getName(), "worldcount"));
    		World world = Bukkit.getServer().getWorld(player.getName() + "_housing");
    		if (player.hasPermission("housing.world.delete") == false) {
    			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
    		}
    		else if (args.length != 2) {
    			player.sendMessage(ChatColor.RED + "Missing player name argument.");
    		}
    		else if (DataJSON.readJSON("Data", File.separator, player.getName(), "worldcount") == null) {
				player.sendMessage(ChatColor.RED + "The player is not a valid player in the database");
			}
    		else if (worldcount == 0 || world == null) {
    			player.sendMessage(ChatColor.RED + "The player has no housing world to delete");
    		}
    		else {
    			DataJSON.writeJSON("Data", File.separator, player.getName(), "worldcount", String.valueOf(worldcount-1));
    			HousingManager.deleteHousingWorld(player);
    		}
    	}
    	else if (sender instanceof Player) {
    		if (args[0].equalsIgnoreCase("gui")) {
    			Gui.mainPage(player);
            	Gui.openGui(player, Gui.main_gui);
    		}
    		else if (args[0].equalsIgnoreCase("housing")) {
        		World world = Bukkit.getServer().getWorld(player.getName() + "_housing");
        		player.teleport(new Location(world, -29, 32, 94));
    	}
    	else {
    	  	sender.sendMessage("This command can only be run by a player.");
    	}
    }
    return true;
    }
    if(cmd.getName().equalsIgnoreCase("housebuildmode")){
    	if (sender instanceof Player) {
    		Player player = (Player) sender;
    		boolean buildmode = Boolean.valueOf(DataJSON.readJSON("Data", File.separator, player.getName(), "buildmode"));
    		if (buildmode == false) {
    			Gui.buildMode(player, 0);
    		}
    		else {
    			Gui.buildMode(player, 1);
    		}
    	}
    	else {
    		sender.sendMessage("This command can only be run by a player.");
    	}
    	return true;
    }
    if(cmd.getName().equalsIgnoreCase("writejson")){
    	if (args.length == 4 && sender.hasPermission("housing.json.write")){
    		DataJSON.writeJSON(args[0], File.separator, args[1], args[2], args[3]);
        	sender.sendMessage(ChatColor.GREEN + "Writing file " + ChatColor.AQUA + args[0] + "json" + ChatColor.GREEN + " for player " + ChatColor.AQUA + args[1] + ChatColor.GREEN + " on variable " + ChatColor.AQUA + args[2] + ChatColor.GREEN + " with value " + ChatColor.AQUA + args[3]);
        	return true;
    	}
    	else if(sender.hasPermission("housing.json.write") == false){
    		sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
    	}
    	else {
    		sender.sendMessage(ChatColor.RED + "Cannot write JSON due to missing or too many arguments");
    	}
    }
    if(cmd.getName().equalsIgnoreCase("readjson")){
    	if (args.length == 3 && sender.hasPermission("housing.json.read")) {
    		String data = DataJSON.readJSON(args[0], File.separator, args[1], args[2]);
        	sender.sendMessage(ChatColor.GREEN + "Reading file " + ChatColor.AQUA + args[0] + "json" + ChatColor.GREEN + " for player " + ChatColor.AQUA + args[1] + ChatColor.GREEN + " on variable " + ChatColor.AQUA + args[2] + ChatColor.GREEN + " with value " + ChatColor.AQUA + data);
        	return true;
    	}
    	else if(sender.hasPermission("housing.json.read") == false){
    		sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
    	}
    	else {
    		sender.sendMessage(ChatColor.RED + "Cannot read JSON due to missing or too many arguments");
    	}
    }
    if(cmd.getName().equalsIgnoreCase("readrawjson")){
    	if (args.length == 1 && sender.hasPermission("housing.json.readraw")) {
    		String data = DataJSON.readRawJSON(args[0], File.separator).toJSONString();
        	sender.sendMessage(data);
        	return true;
    	}
    	else if(sender.hasPermission("housing.json.readraw") == false){
    		sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
    	}
    	else {
    		sender.sendMessage(ChatColor.RED + "Cannot read JSON due to missing or too many arguments");
    	}
    }
	return false;
    }
}
