package com.finaljtth.housing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONData {
	private String pluginFolder = Main.plugin.getDataFolder().getAbsolutePath();
	private String fileName = "";
	private String subPath = "";
	private File file;
	private File filePath;
	private JSONParser parser;
	private JSONObject JSON;
	
	public JSONData() {
		filePath = new File(pluginFolder + File.separator + subPath);
		parser = new JSONParser();
		JSON = new JSONObject();
	}
	public JSONData(String FN) {
		fileName = FN;
        filePath = new File(pluginFolder + File.separator + subPath);
        file = new File(filePath + fileName + ".json");
        parser = new JSONParser();
        if (file.exists()) {
        	try {
				JSON = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			} 
        	catch (Exception ex) {
			       ex.printStackTrace();
			}
        }
        else {
        	JSON = new JSONObject();
        }
	}
	public JSONData(String P, String FN) {
		fileName = FN;
		subPath = P;
        filePath = new File(pluginFolder + File.separator + subPath);
        file = new File(filePath + fileName + ".json");
        parser = new JSONParser();
        if (file.exists()) {
        	try {
				JSON = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			} 
        	catch (Exception ex) {
			       ex.printStackTrace();
			}
        }
        else {
        	JSON = new JSONObject();
        }
	}
	public JSONObject getJSON() {
		return JSON;
	}
	public String getArrJSONString() {
		return rearrange();
	}
	public File getFile() {
		return file;
	}
	public String getPath() {
		return file.getAbsolutePath();
	}
	public int getType() {
		return (int) JSON.get("type");
	}
	
	@SuppressWarnings("unchecked")
	public void setup(){
		try {
			if (fileName == "Data.json") {
				filePath.mkdirs();
		        if (!file.exists()) {
	            	file.createNewFile();
	            	FileWriter fileWriter = new FileWriter(file);
	            	JSONObject data = new JSONObject();
	            	data.put("type", "1");
	            	fileWriter.write(rearrange(data));
	            	fileWriter.flush();
	    	        fileWriter.close();
	    	        JSON = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	            }
			}
			else if (fileName == "Group.json") {
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
	    	        JSON = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	            }
			}
		}
		catch (Exception ex) {
		       ex.printStackTrace();
		}
	}
	
	public static void writeJSON(String header, String object, String value) {
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
            JSONObject data = JSON;
            JSONObject dataAdd = new JSONObject();
            JSONArray dataArray = new JSONArray();
            int type = (int) data.get("type");
            if(data.get(header) != null) {
            	dataArray = (JSONArray) data.get(header);
            }
            else {
            	data.put("Type", "1");
            }
            System.out.println("JSON : " + data.toJSONString());
            //JSONObject player = new JSONObject();
		
	        dataAdd.put(object, value);
	        
	        // Classified if the JSONArray of the player is empty or not
	        if(type == 1) {
	        	if(!dataArray.isEmpty()) {
		        	boolean exist = false;
		        	int index = 0;
		        	JSONArray tempArray = (JSONArray) data.get(header);	
			        System.out.println("Array : " + tempArray.toJSONString());
			        // Loop to check if the JSONArray of the JSONObject is exist or not
			        for (int i = 0 ; i < tempArray.size(); i++) {
			        	JSONObject tempJSONObject = (JSONObject) tempArray.get(i);
			        	if(tempJSONObject.containsKey(object)){
			        		exist = true;
			        		index = i;
			        		dataArray.set(i, dataAdd);
			        		i = tempArray.size();
			        	}
			        }
			        if(exist == true) {
			        	dataArray.set(index, dataAdd);
			        }
			        else {
			        	dataArray.add(dataAdd);
			        }
		        }
		        else {
		        	dataArray.add(dataAdd);
		        }
	        }
	        else if (type == 2) {
	        	if(!dataArray.isEmpty()) {
		        	boolean exist = false;
		        	JSONArray tempArray = (JSONArray) data.get(header);	
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
			        			String player = (String) tempArray2.get(j);
			        			if(player == value){
			        				exist = true;
			        				j = tempArray2.size();
			        			}
			        		}
			        		if(!exist) {
			        			tempArray2.add(value);
					        	tempJSONObject.put(object, tempArray2);
					        	dataArray.set(i, tempJSONObject);
					        }
			        	}
			        	else if(!tempJSONObject.containsKey(object)){
			        		JSONArray tempArray2 = (JSONArray) tempJSONObject.get(object);
			        		for (int j = 0 ; j < tempArray2.size(); j++) {
			        			String player = (String) tempArray2.get(j);
			        			if(player == value){
			        				tempArray2.remove(j);
			        				tempJSONObject.put(object, tempArray2);
						        	dataArray.set(i, tempJSONObject);
			        				j = tempArray2.size();
			        			}
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
	        
	        
	        System.out.println(dataAdd.toJSONString());
	        data.put(header, dataArray);
	        
	        System.out.println(data.toJSONString());
	        
	        String rearrangeJSON = rearrange(data);

	        if (!dataAdd.isEmpty()) {
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
	
	@SuppressWarnings("unchecked")
	public static String rearrange(JSONObject data) {
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        treeMap.putAll(data);
        Gson g = new GsonBuilder().setPrettyPrinting().create();
		return g.toJson(treeMap);
	}
}