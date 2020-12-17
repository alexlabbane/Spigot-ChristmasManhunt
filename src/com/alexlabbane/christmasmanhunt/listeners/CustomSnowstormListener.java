package com.alexlabbane.christmasmanhunt.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.alexlabbane.christmasmanhunt.items.CustomSnowstorm;
import com.alexlabbane.christmasmanhunt.util.Util;

public class CustomSnowstormListener implements Listener {
	private Plugin plugin;
	private ArrayList<Player> hunters;
	private ArrayList<Player> nonHunters;
	private final int NUM_SNOWBALLS = 100;
	
	public CustomSnowstormListener(Plugin plugin, ArrayList<Player> hunters, ArrayList<Player> nonHunters) {
		this.plugin = plugin;
		this.hunters = hunters;
		this.nonHunters = nonHunters;
	}
	
	// when player right clicks with snow block
		@EventHandler
		public void onPlayerClick(PlayerInteractEvent e) {
			final Player p = e.getPlayer();
			ItemStack item = e.getItem();
			
			// must be hunter, holding correct block
			try {
				if(!this.hunters.contains(p) || e.getItem() == null || !Util.getNBTTagString(item, "ChristmasManhuntAbility").equals("CustomSnowstorm"))
					return;
			} catch (NullPointerException npe) {
				return;
			}
			// make sure not in 5 minute cooldown period
			if(System.currentTimeMillis() - Util.getNBTTagLong(item, "CustomSnowstormLastUse") < 1000 * 150) {
				p.sendMessage("Blizzard in cooldown for " + (150 - (System.currentTimeMillis() - Util.getNBTTagLong(item, "CustomSnowstormLastUse")) / 1000) + " seconds!");
				return;
			}
			
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				// start a snowstorm
				Util.addNBTTagLong(item, "CustomSnowstormLastUse", System.currentTimeMillis());
				p.getWorld().setStorm(true);
				
				// runnable to spawn snowballs for 30s
				Random r = new Random();
				
				final ArrayList<Player> nH = this.nonHunters;
				final BukkitTask b = new BukkitRunnable() {
					@Override
					public void run() {
						for(Player p2 : nH) {
							Location center = p2.getLocation();
							// spawn snowballs around player that started snowstorm (12 block radius)
							for(int i = 0; i < NUM_SNOWBALLS; i++) {
								double xLoc = center.getX() + (r.nextDouble() * 25) - 12.5;
								double yLoc = center.getY() + 20 + r.nextInt(20);
								double zLoc = center.getZ() + (r.nextDouble() * 25) - 12.5;
								
								p.getWorld().spawnEntity(new Location(p.getWorld(), xLoc, yLoc, zLoc), EntityType.SNOWBALL).setVelocity(new Vector(0, -2, 0));
							}
						}
					}
				}.runTaskTimer(this.plugin, 20, 5);			
				
				// cancel snowstorm after 30 seconds
				new BukkitRunnable() {
					@Override
					public void run() {
						b.cancel();
						p.getWorld().setStorm(false);
					}
				}.runTaskLater(this.plugin, 20 * 30);
				
			}
		}
		
		@EventHandler
		public void handlerHunterDeath(PlayerDeathEvent event) {
			ArrayList<ItemStack> toRemove = new ArrayList<ItemStack>();
			for(ItemStack i : event.getDrops()) {
				try {
					if(Util.getNBTTagString(i, "ChristmasManhuntAbility").equals("CustomSnowstorm")) {
						toRemove.add(i);
					}
				} catch(NullPointerException e) {
					continue;
				}
			}
			
			for(ItemStack i : toRemove)
				event.getDrops().remove(i);
		}
		
		
		@EventHandler
		public void handleHunterRespawn(PlayerRespawnEvent event) {
			if(this.hunters.contains(event.getPlayer())) {
				event.getPlayer().getInventory().addItem(new CustomSnowstorm(this.hunters, this.nonHunters, this.plugin).getSnowBlock());
			}
		}
		
		// when a player tries to throw snow or bedrock
		@EventHandler
		public void onBlockPlace(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			
			// Make sure player is a hunter
			if(!this.hunters.contains(p))
				return;
			
			if(Util.getNBTTagString(e.getItemInHand(), "ChristmasManhuntAbility").equals("CustomSnowstorm")) {
				e.setCancelled(true);
			}
		}
		
		// make sure hunter cannot drop the snow on accident
		@EventHandler
		public void handleSnowDrop(PlayerDropItemEvent event) {
			if(event.getItemDrop() == null || event.getItemDrop().getItemStack() == null || event.getPlayer() == null)
				return;
			
			try {
				if(this.hunters.contains(event.getPlayer()) && Util.getNBTTagString(event.getItemDrop().getItemStack(), "ChristmasManhuntAbility").equals("CustomSnowstorm")) {
					event.setCancelled(true);
				}
			} catch(NullPointerException e) {
				
			}
		}
}
