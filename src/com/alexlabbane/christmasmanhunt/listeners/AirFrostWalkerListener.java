package com.alexlabbane.christmasmanhunt.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.alexlabbane.christmasmanhunt.items.AirFrostWalker;
import com.alexlabbane.christmasmanhunt.items.AirFrostWalkerMode;
import com.alexlabbane.christmasmanhunt.util.Util;

public class AirFrostWalkerListener implements Listener {
	private Plugin plugin;
	private ArrayList<Player> hunters;
	private ArrayList<Player> nonHunters;
	
	public AirFrostWalkerListener(Plugin plugin, ArrayList<Player> hunters, ArrayList<Player> nonHunters) {
		this.plugin = plugin;
		this.hunters = hunters;
		this.nonHunters = nonHunters;
	}
	
	// when player walks
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
    	Player p = event.getPlayer();
		if(!this.hunters.contains(p))
			return;
		
		ItemStack[] inventory = p.getInventory().getStorageContents();
		ItemStack item = null;
		for(ItemStack i : inventory) {
			try {
				if(Util.getNBTTagString(i, "ChristmasManhuntAbility").equals("AirFrostWalker")) {
					item = i;
					break;
				}
			} catch(NullPointerException e) {
				
			}
		}
		
		if(item == null || Util.getNBTTagBoolean(item, "AirFrostWalkerActive") == 0)
			return;
    	
    	ArrayList<Block> iceBlocks = new ArrayList<Block>();
    	
    	if(this.hunters.contains(p)) {
    		int height = 1; // directly below player
    		if(Util.getNBTTagString(item, "AirFrostWalkerMode").equals("DOWN"))
    			height = 2; // one block underneath player

    		for(int i = -2; i <= 2; i++) {
    			for(int j = -2; j <= 2; j++) {
    				if(Math.abs(i) == Math.abs(j) && Math.abs(i) == 2)
    					continue;
    				
    				Block b = p.getLocation().subtract(i, height, j).getBlock();
    				
    				if(b.getType() == Material.AIR) {
    	    			b.setType(Material.ICE);
    	    			iceBlocks.add(b);
    				}	
    			}
    		}	
    		
    		new BukkitRunnable() {
				@Override
				public void run() {
					for(Block b : iceBlocks)
						b.setType(Material.AIR); 
		    	}
				
			}.runTaskLater(this.plugin, 100);
    	}
    }
	
	// handle activate/deactivate and mode changes
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		try {
			if(event.getItem() == null || !Util.getNBTTagString(event.getItem(), "ChristmasManhuntAbility").equals("AirFrostWalker"))
				return;
		} catch (NullPointerException e) {
			return;
		}
		
		// Make sure player is a hunter
		if(!hunters.contains(event.getPlayer()))
			return;
		
		ItemStack item = event.getItem();
		int active = Util.getNBTTagBoolean(event.getItem(), "AirFrostWalkerActive");	
		// Turn effect on/off on right click
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
			active = (1 + active) % 2;
			Util.addNBTTagBoolean(event.getItem(), "AirFrostWalkerActive", active);
			
			if(active == 1)
				event.getPlayer().sendMessage("Air Frost Walker is ACTIVE");
			else
				event.getPlayer().sendMessage("Air Frost Walker is INACTIVE");
			
		// Toggle effect mode (up/down) on left click (unless punching block)
		} else if(event.getAction() == Action.LEFT_CLICK_AIR) {
			if(Util.getNBTTagString(event.getItem(), "AirFrostWalkerMode").equals("UP")) {
				event.getPlayer().sendMessage("Air Frost Walker going DOWN");
				Util.addNBTTagString(event.getItem(), "AirFrostWalkerMode", "DOWN");
			} else if(Util.getNBTTagString(event.getItem(), "AirFrostWalkerMode").equals("DOWN")) {
				Util.addNBTTagString(event.getItem(), "AirFrostWalkerMode", "UP");
				event.getPlayer().sendMessage("Air Frost Walker going UP");
			}
		}
		
		// Update item name
		ItemMeta m = item.getItemMeta();
		String name = "";
		
		if(active == 1)
			name += "ACTIVE ";
		else
			name += "INACTIVE ";
		
		if(Util.getNBTTagString(event.getItem(), "AirFrostWalkerMode").equals("UP"))
			name += "UP";
		else if(Util.getNBTTagString(event.getItem(), "AirFrostWalkerMode").equals("DOWN"))
			name += "DOWN";
		
		m.setDisplayName(name);
		item.setItemMeta(m);
	}
	
	@EventHandler
	public void handlerHunterDeath(PlayerDeathEvent event) {
		ArrayList<ItemStack> toRemove = new ArrayList<ItemStack>();
		for(ItemStack i : event.getDrops()) {
			try {
				if(Util.getNBTTagString(i, "ChristmasManhuntAbility").equals("AirFrostWalker")) {
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
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		// Make sure player is a hunter
		if(!this.hunters.contains(p))
			return;
		
		if(Util.getNBTTagString(e.getItemInHand(), "ChristmasManhuntAbility").equals("AirFrostWalker")) {
			e.setCancelled(true);
		}
	}
	
	// make sure hunter cannot drop the ice on accident
	@EventHandler
	public void handleIceDrop(PlayerDropItemEvent event) {
		if(event.getItemDrop() == null || event.getItemDrop().getItemStack() == null || event.getPlayer() == null)
			return;
		
		try {
			if(this.hunters.contains(event.getPlayer()) && Util.getNBTTagString(event.getItemDrop().getItemStack(), "ChristmasManhuntAbility").equals("AirFrostWalker")) {
				event.setCancelled(true);
			}
		} catch(NullPointerException e) {
			
		}
	}
	
	@EventHandler
	public void handleHunterRespawn(PlayerRespawnEvent event) {
		if(this.hunters.contains(event.getPlayer())) {
			event.getPlayer().getInventory().addItem(new AirFrostWalker(hunters, plugin).getIceBlock());
		}
	}
}
