package com.alexlabbane.christmasmanhunt.hostilesnowman;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.WorldServer;

public class SpawnSnowmanArmyEgg {
	private ArrayList<Player> hunters;
	private ItemStack vexEgg; // uses a snowblock to trigger
	private Plugin plugin;
	
	public SpawnSnowmanArmyEgg(ArrayList<Player> hunters, Plugin plugin) {
		this.hunters = hunters;
		this.vexEgg = new ItemStack(Material.VEX_SPAWN_EGG);
		ItemMeta meta = this.vexEgg.getItemMeta();
		meta.setDisplayName("Spawn Snow Minions");
		
		this.vexEgg.setItemMeta(meta);
		Util.addNBTTagString(this.vexEgg, "ChristmasManhuntAbility", "SpawnSnowmanArmyEgg");
		Util.addNBTTagLong(this.vexEgg, "SpawnSnowmanArmyEggLastUse", 0);
		
		this.plugin = plugin;
	}
	
	// get reference to snow block
	public ItemStack getVexEgg() {
		return this.vexEgg;
	}
}
