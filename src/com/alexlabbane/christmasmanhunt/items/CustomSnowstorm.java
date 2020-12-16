package com.alexlabbane.christmasmanhunt.items;

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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.alexlabbane.christmasmanhunt.util.Util;

public class CustomSnowstorm implements Listener {
	private ArrayList<Player> hunters;
	private ArrayList<Player> nonHunters;
	private ItemStack snowBlock; // uses a snowblock to trigger
	private Plugin plugin;
	
	public CustomSnowstorm(ArrayList<Player> hunters, ArrayList<Player> nonHunters, Plugin plugin) {
		this.hunters = hunters;
		this.nonHunters = nonHunters;
		this.snowBlock = new ItemStack(Material.SNOW_BLOCK);
		ItemMeta meta = this.snowBlock.getItemMeta();
		meta.setDisplayName("Cause Snowstorm");
		
		this.snowBlock.setItemMeta(meta);
		Util.addNBTTagString(this.snowBlock, "ChristmasManhuntAbility", "CustomSnowstorm");
		Util.addNBTTagLong(this.snowBlock, "CustomsnowstormLastUse", 0);
		
		this.plugin = plugin;
	}
	
	// get reference to snow block
	public ItemStack getSnowBlock() {
		return this.snowBlock;
	}
}
