package com.alexlabbane.christmasmanhunt.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public class HungerListener implements Listener {
	private ArrayList<Player> nonHunters;
	private ArrayList<Player> hunters;
	
	public HungerListener(ArrayList<Player> nonHunters, ArrayList<Player> hunters) {
		this.nonHunters = nonHunters;
		this.hunters = hunters;
	}
	
	@EventHandler
	public void onLoseHunger(FoodLevelChangeEvent event) {
		Player player = null;
		if(event.getEntity() instanceof Player) {
			player = (Player)event.getEntity();
		}
		
		if(this.hunters.contains(player)) {
			event.setCancelled(true);
		}
		
		return;
	}
}
