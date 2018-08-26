package com.finaljtth.housing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldTemplate {
	
	public static void defineCopyWorld(Player player) {
		// The world to copy
		World source = (World) Bukkit.getWorld("template");
		File sourceFolder = source.getWorldFolder();
		 
		// The world to overwrite when copying
		World target = (World) Bukkit.getWorld(player.getWorld().getName() + "_housing");
		File targetFolder = target.getWorldFolder();
		
		copyWorld(sourceFolder, targetFolder);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load " + player.getWorld().getName() + "_housing");
	}
	
	public static void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	    		
	    }
	}
}
