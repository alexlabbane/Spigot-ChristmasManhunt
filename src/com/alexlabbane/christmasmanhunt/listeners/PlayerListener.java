package com.alexlabbane.christmasmanhunt.listeners;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {
	private ArrayList<Player> nonHunters;
	private ArrayList<Player> hunters;
	private Plugin plugin;
	
	public PlayerListener(Plugin plugin, ArrayList<Player> nonHunters, ArrayList<Player> hunters) {
		this.nonHunters = nonHunters;
		this.hunters = hunters;
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	//Bukkit.getLogger().log(Level.WARNING, event.getPlayer().getName() + " joined!!");
    	nonHunters.add(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	//Bukkit.getLogger().log(Level.WARNING, event.getPlayer().getName() + " left!!");
    	nonHunters.remove(event.getPlayer());
    	hunters.remove(event.getPlayer());
    }
}
