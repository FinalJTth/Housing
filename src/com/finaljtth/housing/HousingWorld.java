package com.finaljtth.housing;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.utils.WorldManager;

public class HousingWorld {
	private String worldName;
	private String ownerName;
	private World world;
	
	public HousingWorld() {
		worldName = null;
		ownerName = null;
		world = null;
	}
	public HousingWorld(Player player) {
		ownerName = player.getName();
		worldName = ownerName + "_housing";
		world = Bukkit.getWorld(worldName);
	}
	public HousingWorld(String ON) {
		ownerName = ON;
		worldName = ownerName + "_housing";
		world = Bukkit.getWorld(worldName);
	}
	public HousingWorld(String WN, String ON) {
		worldName = WN;
		ownerName = ON;
		world = Bukkit.getWorld(worldName);
	}
	public HousingWorld(String WN, String ON, World W) {
		worldName = WN;
		ownerName = ON;
		world = W;
	}
	
	public String getName() {
		return worldName;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public World getWorld() {
		return world;
	}
	
	public void createWorld() {
		WorldManager wm = (WorldManager) Main.getMultiverseCore().getMVWorldManager();
		if(!wm.isMVWorld(worldName)){
		    wm.cloneWorld("template", worldName);
		    wm.loadWorld(worldName);
		    wm.getMVWorld(worldName).setAlias("Housing");
		}
	}
	
	public void deleteWorld() {
		WorldManager wm = (WorldManager) Main.getMultiverseCore().getMVWorldManager();
		if(wm.isMVWorld(worldName)){
			File folder = new File(Bukkit.getServer().getWorld(worldName).getWorldFolder().getPath());
		    wm.unloadWorld(worldName);
		    wm.deleteWorld(worldName);
		    deleteDirectory(folder);
		}
	}
	
	public boolean deleteDirectory(File folder) {
		if( folder.exists() ) {
			File[] files = folder.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( folder.delete() );
	}
}
