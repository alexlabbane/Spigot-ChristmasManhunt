package com.alexlabbane.christmasmanhunt;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.alexlabbane.christmasmanhunt.hostilesnowman.EntityHostileSnowman;
import com.alexlabbane.christmasmanhunt.hostilesnowman.SnowballDamageListener;
import com.alexlabbane.christmasmanhunt.hostilesnowman.SpawnSnowmanArmyEgg;
import com.alexlabbane.christmasmanhunt.items.AirFrostWalker;
import com.alexlabbane.christmasmanhunt.items.CustomSnowstorm;
import com.alexlabbane.christmasmanhunt.items.TrackingCompass;
import com.alexlabbane.christmasmanhunt.listeners.AirFrostWalkerListener;
import com.alexlabbane.christmasmanhunt.listeners.CustomSnowstormListener;
import com.alexlabbane.christmasmanhunt.listeners.PlayerListener;
import com.alexlabbane.christmasmanhunt.listeners.SpawnSnowmanArmyListener;
import com.alexlabbane.christmasmanhunt.listeners.TrackingCompassListener;
import com.alexlabbane.christmasmanhunt.util.Util;

import net.minecraft.server.v1_16_R2.WorldServer;

public class ChristmasManhunt extends JavaPlugin implements Listener {
	static ArrayList<Player> nonHunters;
	static ArrayList<Player> hunters;
	static CustomSnowstorm snowstorm;
	static SpawnSnowmanArmyEgg snowmanArmy;

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {  
    	Util.setPlugin(this);
    	
    	nonHunters = new ArrayList<Player>();
    	hunters = new ArrayList<Player>();
    	snowmanArmy = new SpawnSnowmanArmyEgg(hunters, this);
    	
    	getServer().getPluginManager().registerEvents(new TrackingCompassListener(this, hunters, nonHunters), this);
    	getServer().getPluginManager().registerEvents(new AirFrostWalkerListener(this, hunters, nonHunters), this);
    	getServer().getPluginManager().registerEvents(new PlayerListener(this, nonHunters, hunters), this);
    	getServer().getPluginManager().registerEvents(new SnowballDamageListener(hunters), this);
    	getServer().getPluginManager().registerEvents(new CustomSnowstormListener(this, hunters, nonHunters), this);
    	getServer().getPluginManager().registerEvents(new SpawnSnowmanArmyListener(this, hunters, nonHunters), this);


    	getServer().getLogger().log(Level.WARNING, "[Christmas Manhunt] Plugin successfully initialized.");

    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player requestor = null;
    	if(sender instanceof Player) {
    		requestor = (Player)sender;
    	} else {
    		return false;
    	}
    	
    	if(label.equals("hunter")) {
    		if(args.length != 2) {
				sender.sendMessage("Usage: hunter <add/remove> <player name>");
    			return false;
    		}
    		
    		Player p = getPlayerByName(requestor.getWorld(), args[1]);
    		
    		if(p == null) {
				sender.sendMessage("No player with the name " + args[1] + " is on the server!");
				return false;
			}
				
    		if(args[0].equals("add")) {
    			nonHunters.remove(p);
    			hunters.remove(p);
    			hunters.add(p);
    			p.getInventory().addItem(new TrackingCompass(hunters, nonHunters, this).getCompass());
    			p.getInventory().addItem(new AirFrostWalker(hunters, this).getIceBlock());
    			p.getInventory().addItem(new CustomSnowstorm(hunters, nonHunters, this).getSnowBlock());
    			p.getInventory().addItem(new SpawnSnowmanArmyEgg(hunters, this).getVexEgg());
    			p.sendMessage(ChatColor.DARK_RED + "You are now a hunter!");
    		} else if(args[0].equals("remove")) {
    			nonHunters.remove(p);
    			hunters.remove(p);
    			nonHunters.add(p);
    			p.sendMessage(ChatColor.GREEN + "You are not a hunter.");
    			
    			// TODO: Change removals
    			//p.getInventory().removeItem(trackingCompass.getCompass());
    			//p.getInventory().removeItem(iceBlock.getIceBlock());
    			//p.getInventory().removeItem(snowstorm.getSnowBlock());
    			//p.getInventory().removeItem(snowmanArmy.getVexEgg());
    		} else {
    			return false;
    		}
    	} else if (label.equals("test")) {
    		EntityHostileSnowman snowman = new EntityHostileSnowman(requestor.getLocation());
    		WorldServer world = ((CraftWorld)requestor.getWorld()).getHandle();
    		
    		world.addEntity(snowman);
    	}
    	
    	return false;
    }
    
    public Player getPlayerByName(World world, String playerName) {
    	List<Player> players = world.getPlayers();
    	
    	for(Player p : players) {
    		if(p.getName().equals(playerName))
    			return p;
    	}
    	
    	return null;
    }
}
