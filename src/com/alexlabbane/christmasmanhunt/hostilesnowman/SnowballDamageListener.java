package com.alexlabbane.christmasmanhunt.hostilesnowman;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_16_R2.PlayerInventory;

public class SnowballDamageListener implements Listener {
	private ArrayList<Player> hunters;
	
	public SnowballDamageListener(ArrayList<Player> hunters) {
		this.hunters = hunters;
	}
	
	// When snow minion hits player with snowball
	@EventHandler
	public void onSnowballDamage(ProjectileHitEvent e) {
		if(e.getEntity().getType() == EntityType.SNOWBALL && e.getHitEntity() instanceof Player) {
			Player p = (Player)e.getHitEntity();
			
			if(this.hunters.contains(p) && !(e.getEntity().getShooter() instanceof Player))
				return;

			// TODO: Check if shield blocked the damage
			if(p.isBlocking()) {
				Vector target = p.getLocation().getDirection().normalize();
				Vector projectile = e.getEntity().getVelocity().normalize();
				
				double dot = target.getX() * projectile.getX() + target.getZ() * projectile.getZ();

				// if angle between vectors is at least 90 degrees
				if(dot < 0)
					return;
			}
			
			double baseDamage = 4.0;
			double damage = baseDamage * (1.0 - getDamageReduced(p));
			p.damage(Math.min(3.0, damage));
		}
		
	}
	
	// gets reduced damage as a percentage
	public static double getDamageReduced(Player player) {
        org.bukkit.inventory.PlayerInventory inv = player.getInventory();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack boots = inv.getBoots();
        ItemStack pants = inv.getLeggings();
        double red = 0.0;
        //
        if(helmet != null) {
            if (helmet.getType() == Material.LEATHER_HELMET) red = red + 0.04;
            else if (helmet.getType() == Material.GOLDEN_HELMET) red = red + 0.08;
            else if (helmet.getType() == Material.CHAINMAIL_HELMET) red = red + 0.08;
            else if (helmet.getType() == Material.IRON_HELMET) red = red + 0.08;
            else if (helmet.getType() == Material.DIAMOND_HELMET) red = red + 0.12;
        }
        //
        if(chest != null) {
            if (chest.getType() == Material.LEATHER_CHESTPLATE)    red = red + 0.12;
            else if (chest.getType() == Material.GOLDEN_CHESTPLATE)red = red + 0.20;
            else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE) red = red + 0.20;
            else if (chest.getType() == Material.IRON_CHESTPLATE) red = red + 0.24;
            else if (chest.getType() == Material.DIAMOND_CHESTPLATE) red = red + 0.32;
        }
        //
        if(pants != null) {
            if (pants.getType() == Material.LEATHER_LEGGINGS) red = red + 0.08;
            else if (pants.getType() == Material.GOLDEN_LEGGINGS)    red = red + 0.12;
            else if (pants.getType() == Material.CHAINMAIL_LEGGINGS) red = red + 0.16;
            else if (pants.getType() == Material.IRON_LEGGINGS)    red = red + 0.20;
            else if (pants.getType() == Material.DIAMOND_LEGGINGS) red = red + 0.24;
        }
        //
        if(boots != null) {
            if (boots.getType() == Material.LEATHER_BOOTS) red = red + 0.04;
            else if (boots.getType() == Material.GOLDEN_BOOTS) red = red + 0.04;
            else if (boots.getType() == Material.CHAINMAIL_BOOTS) red = red + 0.04;
            else if (boots.getType() == Material.IRON_BOOTS) red = red + 0.08;
            else if (boots.getType() == Material.DIAMOND_BOOTS)    red = red + 0.12;
        }
        //
        return red;
    }
}
