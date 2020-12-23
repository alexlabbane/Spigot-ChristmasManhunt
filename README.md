# Speedrunner vs Krampus

This is a Spigot plugin developed for [Jiro's Speedrunner vs Krampus video](https://www.youtube.com/channel/UCihm5tjx-wqJgHTpyjWGYkQ) on Youtube. It was inspired by Dream's Minecraft Manhunt series on Youtube but is designed to introduce new, interesting gameplay elements. In this version, Krampus is the "hunter" trying to kill the Speedrunner before the Speedrunner can deliver gifts to seven designated houses on the map.

You can find downloads for the Spigot Plugin and world file in the releases on GitHub or by following [this link](https://github.com/alexlabbane/Spigot-ChristmasManhunt/releases/tag/1.0).

# Description

The only change to gameplay that affects both players is that snowballs now do a base damage of 1.5 hearts when hitting a player, making them useful as projectiles throughout the game.

Below are instructions for how to play each of the two roles: the Speedrunner and Krampus.

## Speedrunner

As the Speedrunner, to start the game, simply press the button at spawn to receive a map and 7 presents to deliver. No command is required.

The primary goal of the Speedrunner is to use the map (given at the beginning of the game) to deliver presents (also given at the beginning) to 7 designated locations. If the Speedrunner dies once, the game is over and Krampus wins. To deliver a present, drop it in the hopper at each location and a beacon should light up, indicating the present has been delivered.

The Speedrunner is given no special abilities and will have to be creative to beat Krampus!

## Krampus

To start the game as Krampus, simply type the command `/hunter add <player name>`. Then, running `/kill` will cause Krampus to respawn at their designated starting point. To become a Speedrunner again, use the command `/hunter remove <player name>`.

For Krampus, the goal of the game is to kill the Speedrunner before the presents can be delivered. To help with this, Krampus has some special items/abilities to be aware of. For all of these abilities, I recommend trying them out a couple of times in game to see how the work first, but descriptions are provided below.

### Tracking Compass

Krampus is given a compass that, when right clicked, will update to point to the location of the nearest Speedrunner. This works in a similar manner to the compass seen in Dream's Minecraft Manhunt.

### Air Frost Walker

This is the preferred way of travel throughout the map for Krampus. Essentially, it allows Krampus to walk on a trail of ice while in the air (think regular frost walker excep on air instead of water). This is the most complex ability to use (but also the most fun in my opinion).

The ability is controlled through the use of the ice block given the Krampus when running the `/hunter add <player name>` command. There are two kinds of settings for this ability:

1) Active/Inactive: This simply indicates whether or not the ability is active or not and can be changed by right clicking with the ice block in hand.
2) Up/Down: This alters the behavior of the ability to allow the hunter to move up or down while in the air. It can be changed by left clicking with the ice block in hand.
While in "Up" mode, ice blocks will form directly below the player. This allows the player to move up by sprint-jumping or remain at the same height while not jumping. While in "Down" mode, ice blocks form one block below the player. This allows the player to remain at the same height by sprint-jumping or go down while not jumping.

A faster way to move down with this ability active is to have it in "Down" mode and deactivate the ability, fall a few blocks, and reactivate it to catch yourself. Likely, some experimentation will have to take place to get used to using this.

### Cause a Blizzard

This ability can be activated every 150 seconds and will cause the weather to change to stormy. In addition, snowballs will begin falling from the sky on any Speedrunners, causing damage. This effect lasts for 30 seconds before the blizzard subsides. As a warning, using this effect in water will cause the game to be extremely laggy due to the large number of snowball entities that do not despawn while in water.

The ability can be activated by right clicking with the snow block in hand.

### Spawn Snowman Minions

This ability allows Krampus to throw an egg that, when it lands, will cause a lightning strike at the landing spot and spawn several hostile snowmen in a circle around it. These snowmen will attempt to throw snowballs at any player (including Krampus), but they will only deal damage to the Speedrunner.

To throw the egg, right click while holding the Vex Spawn Egg in hand.

# Winning Conditions

For the Speedrunner to win, all 7 presents must be delivered without dying.

For Krampus to win, the Speedrunner must die before all 7 presents are delivered. Krampus can die and respawn as many times as needed.

# Installation

To play this map with the plugin, head over to the download [here](https://github.com/alexlabbane/Spigot-ChristmasManhunt/releases/tag/1.0) and download both the world file and plugin jar file. The plugin is verified to work on 1.16.3 but may also work on other 1.16 versions.

Drop the plugin into the 'plugins' folder on a Spigot server, and replace the 'world' folder with the world you just downloaded. From there, you should be ready to go!

# Videos

If you play this map on Youtube, Twitch, etc. please provide a link to Jiro's channel: https://www.youtube.com/channel/UCihm5tjx-wqJgHTpyjWGYkQ

In addition, provide a link to the map/plugin download so that people can try it themselves: https://github.com/alexlabbane/Spigot-ChristmasManhunt
