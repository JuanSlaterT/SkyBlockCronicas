package hotdoctor.plugin.skyblockcronicas.listeners;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import hotdoctor.plugin.skyblockcronicas.Main;
import hotdoctor.plugin.skyblockcronicas.database.Database;

public class Listeners implements Listener{
	
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	private Main plugin;
	private HashMap<Player, Player> currentPlayer = new HashMap<>();
	private HashMap<Player, Integer> currentStreak = new HashMap<>();
	
	@EventHandler
	public void morir(PlayerDeathEvent e ) {
		if(e.getEntity() instanceof Player) {
			Player p = e.getEntity();
			if(Bukkit.getOnlinePlayers().contains(p)) {
				double dinero = Main.econ.getBalance(p);
				double resultado = dinero * 0.98;
				if(dinero > 1000) {
					plugin.SendMessageToPlayer(p, "");
					plugin.SendMessageToPlayer(p, "");
					plugin.SendMessageToPlayer(p, "");
					plugin.SendMessageToPlayer(p, "");
					int formated = (int) resultado;
					plugin.SendMessageToPlayer(p, "&c&l(!) &f¡Acabas de perder &c"+formated+" &fpor haber muerto!");
					plugin.SendMessageToPlayer(p, "&c&l(!) &fGuarda tu dinero en el &bBanco &fla próxima vez.");
					Main.econ.withdrawPlayer(p, resultado);
					plugin.SendMessageToPlayer(p, "");
					plugin.SendMessageToPlayer(p, "");
					plugin.SendMessageToPlayer(p, "");
					plugin.SendMessageToPlayer(p, "");
					e.setDeathMessage("");
				}else {
					if(dinero > 100) {
						int formated = (int) resultado;
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						resultado = dinero * 0.5;
						plugin.SendMessageToPlayer(p, "&c&l(!) &f¡Acabas de perder &c"+formated+" &fpor haber muerto!");
						plugin.SendMessageToPlayer(p, "&c&l(!) &fGuarda tu dinero en el &bBanco &fla próxima vez.");
						Main.econ.withdrawPlayer(p, resultado);
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						plugin.SendMessageToPlayer(p, "");
						e.setDeathMessage("");
					}
				}
			}
		}
		
	}
	@EventHandler
	public void picar(BlockBreakEvent e) throws IOException {
		Player p = e.getPlayer();
		Material type = e.getBlock().getType();
		if(plugin.configuration.getBoolean("capitulo.cap3.enabled")) {
			if(p.getInventory().getItemInHand().getType().toString().contains("PICKAXE")) {
				if(type.toString().contains("STONE") || type.toString().contains("COBBLESTONE") || type.toString().contains("ORE")) {
					
					if(plugin.database.containsKey(p)) {
						Database data = plugin.database.get(p);
						Integer value = Integer.valueOf(data.getInformation("cap3"));
						Integer result = value + 1;
						data.saveInformation("cap3", ""+result);
						if(value == 900) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ctc setvoted "+p.getName()+" day3");
							data.setCompleted("cap3");
						}else if(value > 900){
							if(!data.getCompleted("cap3")) {
								data.setCompleted("cap3");

							}
						}
					}else {
						Database data = new Database(p, plugin);
						Integer result = 1;
						data.saveInformation("cap3", ""+result);
						plugin.database.put(p, data);
					}
				}
			}
		}
	}
	@EventHandler
	public void matar(EntityDeathEvent e) throws IOException {
		if(e.getEntity().getKiller() instanceof Player) {
			Player asesino = (Player) e.getEntity().getKiller();
			if(plugin.configuration.getBoolean("capitulo.cap1.enabled")) {
				if(e.getEntity().getType().equals(EntityType.ZOMBIE) || e.getEntity().getType().equals(EntityType.SKELETON) || e.getEntity().getType().equals(EntityType.CREEPER) || e.getEntity().getType().equals(EntityType.SPIDER) || e.getEntity().getType().equals(EntityType.ENDERMAN) || e.getEntity().getType().equals(EntityType.PIG_ZOMBIE)) {
					if(plugin.database.containsKey(asesino)) {
						Database data = plugin.database.get(asesino);
						Integer value = Integer.valueOf(data.getInformation("cap1"));
						Integer result = value + 1;
						data.saveInformation("cap1", ""+result);
						if(value == 50) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ctc setvoted "+asesino.getName()+" day1");
							data.setCompleted("cap1");
						}
						if(value >= 50) {
							if(!data.getCompleted("cap1")) {
								data.setCompleted("cap1");
							}
						}
						
					}else {
						Database data = new Database(asesino, plugin);
						Integer result = 0;
						data.saveInformation("cap1", ""+result);
						plugin.database.put(asesino, data);
					}
				}
			}else if(plugin.configuration.getBoolean("capitulo.cap2.enabled")) {
				if(e.getEntity().getWorld().getName().equalsIgnoreCase("pvp")) {
					if(e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						if(plugin.database.containsKey(asesino)) {
							if(!currentStreak.containsKey(asesino)) {
								currentStreak.put(asesino, 1);
							}
							if(!currentPlayer.containsKey(asesino)) {
								currentPlayer.put(asesino, p);
							}else {
								if(p.equals(currentPlayer.get(asesino))) {
									if(currentStreak.get(asesino) < 3) {
										int i = currentStreak.get(asesino);
										currentStreak.remove(asesino);
										currentStreak.put(asesino, i + 1);
									}else {
										return;
									}
								}else {
									currentPlayer.remove(asesino);
									currentPlayer.put(asesino, p);
									currentStreak.remove(asesino);
									currentStreak.put(asesino, 1);
								}
							}
							Database data = plugin.database.get(asesino);
							Integer value = Integer.valueOf(data.getInformation("cap2"));
							Integer result = value + 1;
							data.saveInformation("cap2", ""+result);
							if(value == 35) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ctc setvoted "+asesino.getName()+" day2");
								data.setCompleted("cap2");
							}
							if(value >= 35) {
								if(!data.getCompleted("cap2")) {
									data.setCompleted("cap2");
								}
								
							}
							
						}else {
							if(!currentStreak.containsKey(asesino)) {
								currentStreak.put(asesino, 1);
							}
							if(!currentPlayer.containsKey(asesino)) {
								currentPlayer.put(asesino, p);
							}
							Database data = new Database(p, plugin);
							Integer result = 1;
							data.saveInformation("cap2", ""+result);
							plugin.database.put(asesino, data);
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler 
	public void off(PlayerQuitEvent e) {
		Player p = e.getPlayer();
			if(plugin.database.containsKey(p)) {
				Database data = plugin.database.get(p);
				try {
					data.saveInformation();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
	}
	
	
	@EventHandler
	public void on(PlayerJoinEvent e) throws IOException {
		Player p = e.getPlayer();
			if(!plugin.database.containsKey(p)) {
				Database data = new Database(p, plugin);
				plugin.database.put(p, data);
				data.getInformation();
			}else {
				Database data = plugin.database.get(p);
				data.getInformation();
			}
		
	}

}
