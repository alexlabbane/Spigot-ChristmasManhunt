package com.alexlabbane.christmasmanhunt.listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.alexlabbane.christmasmanhunt.hostilesnowman.EntityHostileSnowman;
import com.alexlabbane.christmasmanhunt.hostilesnowman.SpawnSnowmanArmyEgg;
import com.alexlabbane.christmasmanhunt.util.Util;

import net.minecraft.server.v1_16_R2.WorldServer;

public class SpawnSnowmanArmyListener implements Listener {
	private Plugin plugin;
	private ArrayList<Player> hunters;
	private ArrayList<Player> nonHunters;
	private final int SPAWN_RADIUS = 8; // 8 blocks
	
	public SpawnSnowmanArmyListener(Plugin plugin, ArrayList<Player> hunters, ArrayList<Player> nonHunters) {
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
			if(!this.hunters.contains(p) || e.getItem() == null || !Util.getNBTTagString(item, "ChristmasManhuntAbility").equals("SpawnSnowmanArmyEgg"))
				return;
		} catch(NullPointerException npe) {
			return;
		}
		
		// make sure not in 5 minute cooldown period
		if(System.currentTimeMillis() - Util.getNBTTagLong(item, "SpawnSnowmanArmyEggLastUse") < 1000 * 150) {
			p.sendMessage("Minion army in cooldown for " + (150 - (System.currentTimeMillis() - Util.getNBTTagLong(item, "SpawnSnowmanArmyEggLastUse")) / 1000) + " seconds!");
			return;
		}
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// start a minion army
			Util.addNBTTagLong(item, "SpawnSnowmanArmyEggLastUse", System.currentTimeMillis());
			Entity projectile = p.launchProjectile(Egg.class);
			
			Util.addNBTTagString(projectile, "SpawnSnowmanArmy", "SpawnSnowmanArmy");
		}
	}
	
	@EventHandler
	public void eggLands(ProjectileHitEvent e) {
		try {
			if(!Util.getNBTTagString(e.getEntity(), "SpawnSnowmanArmy").equals("SpawnSnowmanArmy"))
				return;
		} catch(NullPointerException npe) {
			// Null pointer, tag can't have existed, return
			return;
		}
		
		Location loc = null;
		
		// First spawn lightning
		if(e.getHitEntity() != null)
			loc = e.getHitEntity().getLocation();
		else if(e.getHitBlock() != null)
			loc = e.getHitBlock().getLocation();
		
		if(loc != null)
			loc.getWorld().strikeLightning(loc);
		
		// Now spawn minions in a circle
		final ArrayList<EntityHostileSnowman> snowmen = new ArrayList<EntityHostileSnowman>();
		final WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		for(double i = 0; i < Math.PI * 2; i += 2 * Math.PI / 6.0) {
			Location spawnLoc = new Location(loc.getWorld(), loc.getX() + SPAWN_RADIUS * Math.sin(i), loc.getY() + 2, loc.getZ() + SPAWN_RADIUS * Math.cos(i));			
    		EntityHostileSnowman snowman = new EntityHostileSnowman(spawnLoc);
    		world.addEntity(snowman);
    		snowmen.add(snowman);
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for(EntityHostileSnowman s : snowmen) {
					world.removeEntity(s);
				}
			}
		}.runTaskLater(this.plugin, 20 * 60);
	}
	
	@EventHandler
	public void handlerHunterDeath(PlayerDeathEvent event) {
		ArrayList<ItemStack> toRemove = new ArrayList<ItemStack>();
		for(ItemStack i : event.getDrops()) {
			try {
				if(Util.getNBTTagString(i, "ChristmasManhuntAbility").equals("SpawnSnowmanArmyEgg")) {
					toRemove.add(i);
				}
			} catch(NullPointerException e) {
				// Array altered by other thread
				continue;
			}
		}
		
		for(ItemStack i : toRemove)
			event.getDrops().remove(i);
	}
	
	
	@EventHandler
	public void handleHunterRespawn(PlayerRespawnEvent event) {
		if(this.hunters.contains(event.getPlayer())) {
			event.getPlayer().getInventory().addItem(new SpawnSnowmanArmyEgg(this.hunters, this.plugin).getVexEgg());
		}
	}
	
	// make sure hunter cannot drop the egg on accident
	@EventHandler
	public void handleEggDrop(PlayerDropItemEvent event) {
		if(event.getItemDrop() == null || event.getItemDrop().getItemStack() == null || event.getPlayer() == null)
			return;
		try {
			if(this.hunters.contains(event.getPlayer()) && Util.getNBTTagString(event.getItemDrop().getItemStack(), "ChristmasManhuntAbility").equals("SpawnSnowmanArmyEgg")) {
				event.setCancelled(true);
			}
		} catch(NullPointerException e) {
			
		}
	}
}
