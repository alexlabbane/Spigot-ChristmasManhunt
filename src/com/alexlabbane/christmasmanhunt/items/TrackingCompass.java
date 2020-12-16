package com.alexlabbane.christmasmanhunt.items;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import com.alexlabbane.christmasmanhunt.util.Util;

public class TrackingCompass {
	private ArrayList<Player> nonHunters;
	private ArrayList<Player> hunters;
	private ItemStack compass;
	private Plugin plugin;
	
	public TrackingCompass(ArrayList<Player> nonHunters, ArrayList<Player> hunters, Plugin plugin) {		
		this.nonHunters = nonHunters;
		this.hunters = hunters;
		this.plugin = plugin;
		
		this.compass = new ItemStack(Material.COMPASS);
		ItemMeta meta = this.compass.getItemMeta();
		meta.setDisplayName("Tracking Compass");
		
		this.compass.setItemMeta(meta);
		Util.addNBTTagString(this.compass, "ChristmasManhuntAbility", "TrackingCompass");
	}
	
	// get a compass
	public ItemStack getCompass() {
		return this.compass;
	}
}
