package hotdoctor.plugin.skyblockcronicas.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import hotdoctor.plugin.skyblockcronicas.Main;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;


public class Comandos implements CommandExecutor{
	
	private Main plugin;
	public Comandos(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	if(args[0].equalsIgnoreCase("crash")) {
    			if(args.length >= 1) {
    				if(Bukkit.getPlayer(args[1]) != null) {
    					Player p = Bukkit.getPlayer(args[1]);
    					PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(-5, null, -5);
    					((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    				}
    			}
    		}else {
    			Bukkit.getConsoleSender().sendMessage(" There arent commands avaible for console in this momment.");
    		}
            return true;
        }else {
        	Player player = (Player) sender;
        	if(args.length == 2) {
        		if(args[0].equalsIgnoreCase("start")) {
        			String cap = args[1];
        			if(!plugin.configuration.contains("capitulo."+cap)) {
        				plugin.configuration.set("capitulo."+cap+".enabled", true);
        			}else {
        				if(!plugin.configuration.getBoolean("capitulo."+cap+".enabled")) {
        					plugin.configuration.set("capitulo."+cap+".enabled", true);
        				}
        			}
        			try {
						plugin.configuration.save(plugin.configyml);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&8[&6Vexus&bMC&8] &6El Evento de Crónicas ha iniciado!"));
        			plugin.configuration = YamlConfiguration.loadConfiguration(plugin.configyml);
        		}else if(args[0].equalsIgnoreCase("finish")) {
        			String cap = args[1];
        			if(!plugin.configuration.contains("capitulo."+cap)) {
        				plugin.configuration.set("capitulo."+cap+".enabled", false);
        			}else {
        				if(!plugin.configuration.getBoolean("capitulo."+cap+".enabled")) {
        					plugin.configuration.set("capitulo."+cap+".enabled", false);
        				}
        			}
        			try {
						plugin.configuration.save(plugin.configyml);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&8[&6Vexus&bMC&8] &6El Evento de Crónicas ha finalizado!"));
        			plugin.configuration = YamlConfiguration.loadConfiguration(plugin.configyml);
        		}
        	}else {
        		plugin.SendMessageToPlayer(player, "%prefix% &cError!");
        	}
        	return false;
        }
    }
	
	
	
}
