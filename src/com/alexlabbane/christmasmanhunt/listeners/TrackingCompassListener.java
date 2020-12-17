package com.alexlabbane.christmasmanhunt.listeners;

import java.util.ArrayList;

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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.alexlabbane.christmasmanhunt.items.TrackingCompass;
import com.alexlabbane.christmasmanhunt.util.Util;

public class TrackingCompassListener implements Listener {
	private Plugin plugin;
	private ArrayList<Player> hunters;
	private ArrayList<Player> nonHunters;
	
	public TrackingCompassListener(Plugin plugin, ArrayList<Player> hunters, ArrayList<Player> nonHunters) {
		this.plugin = plugin;
		this.hunters = hunters;
		this.nonHunters = nonHunters;
	}
	
	// handle updating the compass
		@EventHandler
		public void onPlayerClick(PlayerInteractEvent event) {
			if(event.getItem() == null)
				return;
			
			// Make sure player is a hunter
			try {
				if(!hunters.contains(event.getPlayer()) || !Util.getNBTTagString(event.getItem(), "ChristmasManhuntAbility").equals("TrackingCompass"))
					return;
			} catch(NullPointerException e) {
				return;
			}
			
			ItemStack item = event.getItem();
					
			if(event.getAction() == Action.RIGHT_CLICK_AIR) {
				Player nearestNonHunter = getNearestNonHunter(event.getPlayer());
				if(nearestNonHunter == null) {
					event.getPlayer().sendMessage(ChatColor.RED + "Nobody to track!");
					return;
				}
				
				event.getPlayer().setCompassTarget(nearestNonHunter.getLocation());
				event.getPlayer().sendMessage(ChatColor.GREEN + "Tracking " + nearestNonHunter.getName() + " !");
			}
		}
		
		// make sure hunter cannot drop the compass on accident
		@EventHandler
		public void handleCompassDrop(PlayerDropItemEvent event) {
			if(event.getItemDrop() == null || event.getItemDrop().getItemStack() == null || event.getPlayer() == null)
				return;
			
			try {
				if(this.hunters.contains(event.getPlayer()) && Util.getNBTTagString(event.getItemDrop().getItemStack(), "ChristmasManhuntAbility").equals("TrackingCompass")) {
					event.setCancelled(true);
				}
			} catch(NullPointerException e) {
				
			}
		}
		
		@EventHandler
		public void handleHunterRespawn(PlayerRespawnEvent event) {
			if(this.hunters.contains(event.getPlayer())) {
				event.getPlayer().getInventory().addItem(new TrackingCompass(this.hunters, this.nonHunters, this.plugin).getCompass());
				new BukkitRunnable() {
					@Override
					public void run() {
						// Code to run on hunter respawn
						event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 577, 91, -1500));
						return;
					}
					
				}.runTaskLater(this.plugin, 1);
			}
		}
		
		@EventHandler
		public void handlerHunterDeath(PlayerDeathEvent event) {
			ArrayList<ItemStack> toRemove = new ArrayList<ItemStack>();
			for(ItemStack i : event.getDrops()) {
				try {
					if(Util.getNBTTagString(i, "ChristmasManhuntAbility").equals("TrackingCompass")) {
						toRemove.add(i);
					}
				} catch(NullPointerException e) {
					continue;
				}
			}
			
			for(ItemStack i : toRemove)
				event.getDrops().remove(i);
		}
		
		// get the nearest player to p that is not a hunter
		private Player getNearestNonHunter(Player p) {
			Player nearest = null;
			double minDist = Double.MAX_VALUE;
			
			for(Player player : this.nonHunters) {
				Location l1 = player.getLocation();
				Location l2 = p.getLocation();
				
				double distance = Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2) + Math.pow(l1.getY() - l2.getY(), 2) + Math.pow(l1.getZ() - l2.getZ(), 2));
				
				if(distance < minDist) {
					minDist = distance;
					nearest = player;
				}
			}
			
			return nearest;
		}
}
