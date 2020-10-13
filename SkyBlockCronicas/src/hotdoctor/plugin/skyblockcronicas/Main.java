package hotdoctor.plugin.skyblockcronicas;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import hotdoctor.plugin.skyblockcronicas.commands.Comandos;
import hotdoctor.plugin.skyblockcronicas.database.Database;
import hotdoctor.plugin.skyblockcronicas.listeners.Listeners;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	public String rutaConfig;
	public File configyml;
	public YamlConfiguration configuration;
	
	public HashMap<Player, Database> database = new HashMap<>();
    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
	public void onEnable() {
		configyml = new File(this.getDataFolder(), "config.yml");
		registerConfiguration();
		configuration = YamlConfiguration.loadConfiguration(configyml);
		Listeners();
		registrarComandos();
		this.SendMessageByConsole("%prefix% &6Cargado sin problemas!");
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(database.containsKey(p)) {
				Database data = database.get(p);
				try {
					data.getInformation();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Database data = null;
				try {
					data = new Database(p, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					data.getInformation();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				database.put(p, data);
			}
		}
		hotdoctor.pluigin.skyblockcronicas.PAPI.PlaceholderAPI papi = new hotdoctor.pluigin.skyblockcronicas.PAPI.PlaceholderAPI();
		papi.setPlugin(this);
		papi.register();
		saveData();
		
	}
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(database.containsKey(p)) {
				Database data = database.get(p);
				try {
					data.saveInformation();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void saveData() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(database.containsKey(p)) {
						Database data = database.get(p);
						try {
							data.saveInformation();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			
		}.runTaskTimerAsynchronously(this, 0, 300*20);
	}
    public void registrarComandos() {
        this.getCommand("admincronicas").setExecutor((CommandExecutor)new Comandos(this));
    }
    
    public void Listeners() {
    	PluginManager pm = this.getServer().getPluginManager();
    	pm.registerEvents((Listener) new Listeners(this), (Plugin) this);
    }
    
	// VOIDS
	public void registerConfiguration() {
		rutaConfig = configyml.getAbsolutePath();
		if(!(configyml.exists())) {
			setConfigurationDefaults();
			
		}
	}
	
	public void setConfigurationDefaults() {
		this.saveResource("config.yml", true);
	}
	public void saveConfiguration() {
		try {
			configuration.save(configyml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SendMessageByConsole(String text) {
		
		String replaced = text.replaceAll("%prefix%", String.valueOf("&8[&6&lVexus&bMC&8]"));
		String colored = ChatColor.translateAlternateColorCodes('&', replaced);
		Bukkit.getConsoleSender().sendMessage(colored);
	}
	public String setPlaceholders(Player p, String message) {
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			return PlaceholderAPI.setPlaceholders(p, message);
		}else {
			return ChatColor.translateAlternateColorCodes('&', message);
		}
	}
	
	
	public void SendMessageToPlayer(Player p, String text) {
		String replaced = text.replaceAll("%prefix%", String.valueOf("&8[&6&lVexus&bMC&8]"));
		String colored = ChatColor.translateAlternateColorCodes('&', replaced);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        	colored = setPlaceholders(p, colored);
        	p.sendMessage(colored);
        	return;
        }
		p.sendMessage(colored);
		return;
	}
	// VOIDS

}
