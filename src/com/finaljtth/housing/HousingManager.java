package com.finaljtth.housing;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.onarandombox.MultiverseCore.utils.WorldManager;

public class HousingManager {

	public static void cloningHousingWorld(Player player) {
		WorldManager wm = new WorldManager(Main.getMultiverseCore());
		String hsworld = player.getName() + "_housing";
		if(!wm.isMVWorld(hsworld)){
		    wm.cloneWorld("template", hsworld);
		    wm.loadWorld(hsworld);
		    wm.getMVWorld(hsworld).setAlias("Housing");
		}
		World w = Bukkit.getServer().getWorld(hsworld);
		player.teleport(new Location(w, -29, 32, 94));
	}
	
	public static void deleteHousingWorld(Player player) {
		WorldManager wm = new WorldManager(Main.getMultiverseCore());
		String hsworld = player.getName() + "_housing";
		if(wm.isMVWorld(hsworld)){
		    wm.unloadWorld(hsworld);
		    wm.deleteWorld(hsworld);
		}
	}
}
