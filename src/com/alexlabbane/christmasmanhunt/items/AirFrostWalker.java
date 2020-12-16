package com.alexlabbane.christmasmanhunt.items;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

public class AirFrostWalker implements Listener {
	private ArrayList<Player> hunters;
	private ItemStack iceBlock;
	private Plugin plugin;
	
	public AirFrostWalker(ArrayList<Player> hunters, Plugin plugin) {
		this.hunters = hunters;
		this.plugin = plugin;
		
		this.iceBlock = new ItemStack(Material.ICE);
		ItemMeta meta = this.iceBlock.getItemMeta();
		meta.setDisplayName("INACTIVE UP");
		
		this.iceBlock.setItemMeta(meta);
		Util.addNBTTagString(this.iceBlock, "ChristmasManhuntAbility", "AirFrostWalker");
		Util.addNBTTagBoolean(this.iceBlock, "AirFrostWalkerActive", 0);
		Util.addNBTTagString(this.iceBlock, "AirFrostWalkerMode", "UP");
	}
	
	public void setActive(boolean active) {
		if(active)
			Util.addNBTTagBoolean(this.iceBlock, "AirFrostWalkerActive", 1);
		else
			Util.addNBTTagBoolean(this.iceBlock, "AirFrostWalkerActive", 0);
	}
	
	public boolean getActive() {
		if(Util.getNBTTagBoolean(this.iceBlock, "AirFrostWalkerActive") == 1)
			return true;
		else
			return false;
	}
	
	public void setMode(AirFrostWalkerMode mode) {
		if(mode == AirFrostWalkerMode.UP)
			Util.addNBTTagString(this.iceBlock, "AirFrostWalkerMode", "UP");
		else if(mode == AirFrostWalkerMode.DOWN)
			Util.addNBTTagString(this.iceBlock, "AirFrostWalkerMode", "DOWN");
	}
	
	public AirFrostWalkerMode getMode() {
		if(Util.getNBTTagString(this.iceBlock, "AirFrostWalkerMode").equals("UP"))
			return AirFrostWalkerMode.UP;
		else
			return AirFrostWalkerMode.DOWN;
	}
	
	// get a compass
	public ItemStack getIceBlock() {
		return this.iceBlock;
	}
}
