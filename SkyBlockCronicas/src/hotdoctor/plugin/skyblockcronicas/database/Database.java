package hotdoctor.plugin.skyblockcronicas.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import hotdoctor.plugin.skyblockcronicas.Main;

public class Database {
	private Player p;
	private Main plugin;
	public HashMap<String, String> values = new HashMap<>();
	
	public Database(Player p, Main plugin) throws IOException {
		this.plugin = plugin;
		this.p = p;
		getInformation();
	}
	
	public boolean getCompleted(String capitulo) {
		if(this.ifPlayerHasFile(p)) {
			YamlConfiguration yaml = this.getData(p);
			if(yaml.contains("capitulo."+capitulo+".task.completed")) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	public void setCompleted(String capitulo) throws IOException {
		if(this.ifPlayerHasFile(p)) {
			YamlConfiguration yaml = this.getData(p);
			if(yaml.contains("capitulo."+capitulo+".task.completed")) {
				yaml.set("capitulo."+capitulo+".task.completed", true);
			}else {
				yaml.set("capitulo."+capitulo+".task.completed", true);
			}
			yaml.save(this.getFile(p));
		}
	}
	public void saveInformation(String capitulo, String info) {
		if(values.containsKey(capitulo)) {
			values.remove(capitulo);
			values.put(capitulo, info);
		}else {
			values.put(capitulo, info);
		}
	}
	
	public String getInformation(String capitulo) {
		if(values.containsKey(capitulo)) {
			if(values.get(capitulo) != null) {
				return values.get(capitulo);
			}else {
				return "0";
			}
		}else {
			return "0";
		}
	}
	
	public void getInformation() throws IOException {
		if(this.ifPlayerHasFile(p)) {
			YamlConfiguration yaml = this.getData(p);
			if(yaml.contains("capitulo")) {
				for(String capID : yaml.getConfigurationSection("capitulo").getKeys(false)) {
					if(values.containsKey(capID)) {
						values.remove(capID);
						values.put(capID, yaml.getString("capitulo."+capID+".value"));
					}else {
						values.put(capID, yaml.getString("capitulo."+capID+".value"));
					}
				}
			}
		}else {
			createFile(p);
		}
	}
	
	public void saveInformation() throws IOException {
		if(!this.ifPlayerHasFile(p)) {
			this.createFile(p);
		}
		YamlConfiguration yaml = this.getData(p);
		if(!values.isEmpty()) {
			for(Entry<String, String> map : values.entrySet()) {
				
				yaml.set("capitulo."+map.getKey()+".value", map.getValue());
				yaml.save(this.getFile(p));
			}
		}
		

	}
	

    public File getFile(Entity p) {
		File archivo = new File(plugin.getDataFolder().getAbsolutePath(), "users-data");
		String path = archivo.getAbsolutePath();
		File data = new File(path, p.getName()+".yml");
		return data;
    }
	public YamlConfiguration getData(Entity p) {
		File archivo = new File(plugin.getDataFolder().getAbsolutePath(), "users-data");
		String path = archivo.getAbsolutePath();
		File data = new File(path, p.getName()+".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(data);
		return c;
		
	}
    public File createAndGet(String p) throws IOException {
    	File dir = new File(plugin.getDataFolder().getAbsolutePath(), "users-data");
    	if(!(dir.exists())) {
    		if(dir.mkdir()) {
    			// NOTHING
    		}else {
    			// NOTHING
    		}
    	}
		File data = new File(dir.getAbsolutePath(), p);
		data.createNewFile();
		if(data.exists()) {
			String fileData = "#This configuration is made by the plugin automatically, please dont use/modify these datas because you could break the process of the plugin.";
			FileOutputStream fos = new FileOutputStream(data);
			fos.write(fileData.getBytes());
			fos.flush();
			fos.close();
		}
		return data;
    }
    public void createFile(Entity p) throws IOException {
    	File dir = new File(plugin.getDataFolder().getAbsolutePath(), "users-data");
    	if(!(dir.exists())) {
    		if(dir.mkdir()) {
    			// NOTHING
    		}else {
    			// NOTHING
    		}
    	}
		File data = new File(dir.getAbsolutePath(), p.getName()+".yml");
		data.createNewFile();
		if(data.exists()) {
			String fileData = "#This configuration is made by the plugin automatically, please dont use/modify these datas because you could break the process of the plugin.";
			FileOutputStream fos = new FileOutputStream(data);
			fos.write(fileData.getBytes());
			fos.flush();
			fos.close();
		}
    }
    public boolean ifPlayerHasFile(Entity p) {
    	File dir = new File(plugin.getDataFolder().getAbsolutePath(), "users-data");
    	File data = new File(dir.getAbsolutePath(), p.getName()+".yml");
    	if(data.exists()) {
    		return true;
    	}else {
    		return false;
    	}
    }

}
