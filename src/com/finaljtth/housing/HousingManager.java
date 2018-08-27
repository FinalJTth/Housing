package com.finaljtth.housing;

import java.io.File;

import org.bukkit.Bukkit;

import com.onarandombox.MultiverseCore.utils.WorldManager;

public class HousingManager {

	public static void cloningHousingWorld(String player) {
		WorldManager wm = (WorldManager) Main.getMultiverseCore().getMVWorldManager();
		String hsworld = player + "_housing";
		if(!wm.isMVWorld(hsworld)){
		    wm.cloneWorld("template", hsworld);
		    wm.loadWorld(hsworld);
		    wm.getMVWorld(hsworld).setAlias("Housing");
		}
	}
	
	public static void deleteHousingWorld(String player) {
		WorldManager wm = (WorldManager) Main.getMultiverseCore().getMVWorldManager();
		String hsworld = player + "_housing";
		if(wm.isMVWorld(hsworld)){
			File folder = new File(Bukkit.getServer().getWorld(hsworld).getWorldFolder().getPath());
		    wm.unloadWorld(hsworld);
		    wm.deleteWorld(hsworld);
		    deleteDirectory(folder);
		}
	}
	
	public static boolean deleteDirectory(File path) {
		 if( path.exists() ) {
			 File[] files = path.listFiles();
			 for(int i=0; i<files.length; i++) {
				 if(files[i].isDirectory()) {
					 deleteDirectory(files[i]);
				 }
				 else {
					 files[i].delete();
				 }
			 }
		 }
		 return( path.delete() );
	}
}
