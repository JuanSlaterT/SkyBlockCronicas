package hotdoctor.pluigin.skyblockcronicas.PAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import hotdoctor.plugin.skyblockcronicas.Main;
import hotdoctor.plugin.skyblockcronicas.database.Database;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion {
	

    public String getAuthor() {
        return "HotDoctor";
    }
    
    public boolean canRegister() {
        return true;
    }
    
    public String getIdentifier() {
        return "cronicas";
    }
    
    public String getVersion() {
        return "1.0.0";
    }
    
    public String getPlugin() {
        return null;
    }
    
    @Override
    public boolean persist(){
        return true;
    }
    
    public void setPlugin(Main plugin) {
    	this.plugin = plugin;
    }
    private Main plugin;
    
    public String onPlaceholderRequest(final Player p, final String identifier) {
    	if(identifier.equalsIgnoreCase("cap1")) {
        	if(plugin.database.containsKey(p)) {
        		Database data = plugin.database.get(p);
        		return data.getInformation(identifier);
        	}else {
        		return "0";
        	}
    	}else if(identifier.equalsIgnoreCase("iscompleted_cap1")) {
    		if(plugin.database.containsKey(p)) {
    			Database data = plugin.database.get(p);
    			if((boolean) data.getCompleted("cap1")) {
    				return ChatColor.translateAlternateColorCodes('&', "&a&lCOMPLETADO");
    			}else {
    				return ChatColor.translateAlternateColorCodes('&', "&c&lNO COMPLETADO");
    			}
    		}else{
    			return ChatColor.translateAlternateColorCodes('&', "&c&lNO COMPLETADO");
    		}
    	}else if(identifier.equalsIgnoreCase("cap2")){
        	if(plugin.database.containsKey(p)) {
        		Database data = plugin.database.get(p);
        		return data.getInformation(identifier);
        	}else {
        		return "0";
        	}
    	}else if(identifier.equalsIgnoreCase("iscompleted_cap2")) {
    		if(plugin.database.containsKey(p)) {
    			Database data = plugin.database.get(p);
    			if((boolean) data.getCompleted("cap2")) {
    				return ChatColor.translateAlternateColorCodes('&', "&a&lCOMPLETADO");
    			}else {
    				return ChatColor.translateAlternateColorCodes('&', "&c&lNO COMPLETADO");
    			}
    		}else{
    			return ChatColor.translateAlternateColorCodes('&', "&c&lNO COMPLETADO");
    		}
    	}else if(identifier.equalsIgnoreCase("iscompleted_cap3")) {
    		if(plugin.database.containsKey(p)) {
    			Database data = plugin.database.get(p);
    			if((boolean) data.getCompleted("cap3")) {
    				return ChatColor.translateAlternateColorCodes('&', "&a&lCOMPLETADO");
    			}else {
    				return ChatColor.translateAlternateColorCodes('&', "&c&lNO COMPLETADO");
    			}
    		}else{
    			return ChatColor.translateAlternateColorCodes('&', "&c&lNO COMPLETADO");
    		}
    	}else if(identifier.equalsIgnoreCase("cap3")){
        	if(plugin.database.containsKey(p)) {
        		Database data = plugin.database.get(p);
        		return data.getInformation(identifier);
        	}else {
        		return "0";
        	}
    	}else if(identifier.equalsIgnoreCase("value")){
    		int points = 0;
    		if(plugin.database.containsKey(p)) {
    			Database data = plugin.database.get(p);
        		points = points + Integer.valueOf(data.getInformation("cap1"));
        		points = points + Integer.valueOf(data.getInformation("cap2"));
        		points = points + Integer.valueOf(data.getInformation("cap3"));
        		
    		}
    		return ""+points;
    		
    		
    	}else {
    		return "";
    	}
    	
    }
}
