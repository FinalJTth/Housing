package com.finaljtth.housing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class DataJSON {
	
	static String pluginFolder = null;
	private static File file;
	private static File filePath;
	private static JSONParser parser = new JSONParser();
	private static JSONObject json = new JSONObject();
	
	@SuppressWarnings("unchecked")
	public static void setupJSON(Plugin plugin){
		pluginFolder = plugin.getDataFolder().getAbsolutePath();
		try {
			file = new File(pluginFolder + File.separator + "Data.json");
	        filePath = new File(pluginFolder + File.separator);
	        filePath.mkdirs();
	        if (!file.exists()) {
            	file.createNewFile();
            	FileWriter fileWriter = new FileWriter(file); 
            	JSONObject data = new JSONObject();
            	data.put("type", "1");
            	fileWriter.write(rearrange(data));
            	fileWriter.flush(); 
    	        fileWriter.close();
            }
	        file = new File(pluginFolder + File.separator + "Group.json");
	        filePath.mkdirs();
	        if (!file.exists()) {
            	file.createNewFile();
            	FileWriter fileWriter = new FileWriter(file); 
            	JSONObject data = new JSONObject();
            	data.put("type", "2");
            	fileWriter.write(rearrange(data));
            	JSONObject group = new JSONObject();
            	JSONArray grouplist = new JSONArray();
            	JSONArray player = new JSONArray();
            	List<World> worldList = Bukkit.getWorlds();
            	for (int i=0; i < worldList.size(); i++) {
            		if (worldList.get(i).getName().contains("_housing")) {
            			String worldname = worldList.get(i).getName();
            			group.put("Visitor", player);
            			grouplist.add(group);
            			group.put("Resident", player);
            			grouplist.add(group);
            			group.put("Co_owner", player);
            			grouplist.add(group);
            			group.put("Owner", player);
            			grouplist.add(group);
            			data.put(worldname, grouplist);
            		}
            	}
            	fileWriter.write(rearrange(data));
            	fileWriter.flush(); 
    	        fileWriter.close();
            }
	        json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		}
		catch (Exception ex) {
		       ex.printStackTrace();
		}
	};
	
	@SuppressWarnings("unchecked")
	public static String rearrange(JSONObject data) {
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        treeMap.putAll(data);
        Gson g = new GsonBuilder().setPrettyPrinting().create();
		return g.toJson(treeMap);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean writeJSON(String fileName, String subPath, String playername, String object, String value) {
		try {	
			file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
	        filePath = new File(pluginFolder + File.separator + subPath);
	        FileWriter fileWriter = new FileWriter(file); 
	        filePath.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            	JSONObject data = new JSONObject();
            	data.put("type", "1");
            	fileWriter.write(rearrange(data));
            	fileWriter.flush(); 
    	        fileWriter.close();
            }
            
            JSONObject playerdata = json;
            JSONObject data = new JSONObject();
            JSONArray dataArray = new JSONArray();
            if(playerdata.get(playername) != null) {
            	dataArray = (JSONArray) playerdata.get(playername);
            }
            else {
            	playerdata.put("Type", "1");
            	return false;
            }
            System.out.println("JSON : " + playerdata.toJSONString());
            //JSONObject player = new JSONObject();
		
	        data.put(object, value);
	        
	        // Classified if the JSONArray of the player is empty or not
	        if(type == 1) {
	        	if(!dataArray.isEmpty()) {
		        	boolean exist = false;
		        	int index = 0;
		        	JSONArray tempArray = (JSONArray) playerdata.get(playername);	
			        System.out.println("Array : " + tempArray.toJSONString());
			        // Loop to check if the JSONArray of the JSONObject is exist or not
			        for (int i = 0 ; i < tempArray.size(); i++) {
			        	JSONObject tempJSONObject = (JSONObject) tempArray.get(i);
			        	if(tempJSONObject.containsKey(object)){
			        		exist = true;
			        		index = i;
			        		dataArray.set(i, data);
			        		i = tempArray.size();
			        	}
			        }
			        if(exist == true) {
			        	dataArray.set(index, data);
			        }
			        else {
			        	dataArray.add(data);
			        }
		        }
		        else {
		        	dataArray.add(data);
		        }
	        }
	        else if (type == 2) {
	        	if(!dataArray.isEmpty()) {
		        	boolean exist = false;
		        	int index = 0;
		        	JSONArray tempArray = (JSONArray) playerdata.get(playername);	
			        System.out.println("Array : " + tempArray.toJSONString());
			        // Loop to check if the JSONArray of the JSONObject is exist or not
			        for (int i = 0 ; i < tempArray.size(); i++) {
			        	JSONObject tempJSONObject = (JSONObject) tempArray.get(i);
			        	if(tempJSONObject.isEmpty()) {
			        		JSONArray dataArray2 = new JSONArray();
			        		dataArray2.add(value);
				        	tempJSONObject.put(object, dataArray2);
				        	dataArray.set(i, tempJSONObject);
			        	}
			        	else if(tempJSONObject.containsKey(object)){
			        		JSONArray tempArray2 = (JSONArray) tempJSONObject.get(object);
			        		for (int j = 0 ; j < tempArray2.size(); j++) {
			        			String player = (String) tempArray2.get(i);
			        			if(player == value){
			        				exist = true;
			        				j = tempArray2.size();
			        			}
			        		}
			        		if(exist == true) {
			        			return false;
					        }
			        		else {
					        	tempArray2.add(value);
					        	tempJSONObject.put(object, tempArray2);
					        	dataArray.set(i, tempJSONObject);
					        }
			        	}
			        }
		        }
		        else {
		        	JSONObject group = new JSONObject();
	            	JSONArray player = new JSONArray();
	            	player.add(value);
	            	group.put(object, player);
		        	dataArray.add(group);
		        }
	        }
	        
	        
	        System.out.println(data.toJSONString());
	        playerdata.put(playername, dataArray);
	        
	        System.out.println(playerdata.toJSONString());
	        
	        String rearrangeJSON = rearrange(playerdata);

	        if (!data.isEmpty()) {
	        	fileWriter.write(rearrangeJSON);
	        }
	        fileWriter.flush(); 
	        fileWriter.close(); 
	        return true;
	    }
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
	
	public static String readJSON(String fileName, String subPath, String playername, String object) {
		String var = null;
		file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
		JSONObject data = new JSONObject();
		JSONArray dataArray = new JSONArray();
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject playerdata = (JSONObject) obj;
            if(playerdata.get(playername) != null) {
            	dataArray = (JSONArray) playerdata.get(playername);
            }
            boolean exist = false;
        	int index = 0;
            if(!dataArray.isEmpty()) {
            	for (int i = 0 ; i < dataArray.size(); i++) {
    	        	JSONObject tempJSONObject = (JSONObject) dataArray.get(i);
    	        	if(tempJSONObject.containsKey(object)){
    	        		exist = true;
    	        		index = i;
    	        		i = dataArray.size();
    	        	}
    	        }
            	if(exist == true) {
    	        	data = (JSONObject) dataArray.get(index);
    	        	var = (String) data.get(object);
    	        	return var;
    	        }
    	        else {
    	        	return null;
    	        }
            }
            else {
            	return null;
            }
        } 
        catch (Exception ex) {
        	ex.printStackTrace();
        }
		return var;
    }
	
	public static boolean checkExist(String fileName, String subPath, String key) {
		file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject playerdata = (JSONObject) obj;
            if(playerdata.containsKey(key)) {
            	return true;
            }
            else {
            	return false;
            }
        } 
        catch (Exception ex) {
        	ex.printStackTrace();
        }
        return false;
    }
	public static boolean checkExist(String fileName, String subPath, String key1, String key2) {
		file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject data = (JSONObject) obj;
            if(data.containsKey(key1)) {
            	JSONArray dataArray = new JSONArray();
            	if(data.get(key1) != null) {
                	dataArray = (JSONArray) data.get(key1);
                }
                if(!dataArray.isEmpty()) {
                	for (int i = 0 ; i < dataArray.size(); i++) {
        	        	JSONObject tempJSONObject = (JSONObject) dataArray.get(i);
        	        	if(tempJSONObject.containsKey(key2)){
        	        		return true;
        	        	}
        	        }
                	return false;
                }
                else {
                	return false;
                }
            }
            else {
            	return false;
            }
        } 
        catch (Exception ex) {
        	ex.printStackTrace();
        }
        return false;
    }
	
	public static JSONObject readRawJSON(String fileName, String subPath) {
		file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
		JSONObject jsonObject = null;
		try {
			Object obj = parser.parse(new FileReader(file));
			jsonObject = (JSONObject) obj;
        } 
        catch (Exception ex) {
        	ex.printStackTrace();
        }
		return jsonObject;
	}
}