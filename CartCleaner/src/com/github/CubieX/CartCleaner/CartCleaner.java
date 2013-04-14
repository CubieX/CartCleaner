package com.github.CubieX.CartCleaner;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CartCleaner extends JavaPlugin
{
   Logger log;

   private CartCleanerConfigHandler cHandler = null;
   private CartCleanerEntityListener eListener = null;

   @Override
   public void onEnable()
   {      
      log = this.getLogger();
      log.info("CartCleaner version " + getDescription().getVersion() + " is enabled!");

      cHandler = new CartCleanerConfigHandler(this);
      eListener = new CartCleanerEntityListener(this, cHandler, log);
   }

   @Override
   public void onDisable()
   {      
      cHandler = null;
      eListener = null;
      log.info("CartCleaner version " + getDescription().getVersion() + " is disabled!");
   }
   
   //=============================================================================================================
   // Command Handler -- ToDo: SHOULD BE IN SEPARATE CLASS!!! ---------------------------------------------
   // player typed a command

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

      if(cHandler.getConfig().getBoolean("debug")){log.info("onCommand");}
      if (cmd.getName().equalsIgnoreCase("cc"))
      { // If the player typed /cc then do the following... (can be run from console also)
         if (args.length == 0)
         { //no arguments, so help will be displayed
            return false;
         }
         if (args.length==1)
         {
            if (args[0].equalsIgnoreCase("version")) // argument 0 is given and correct
            {            
               sender.sendMessage(ChatColor.YELLOW + "This server is running CartCleaner version " + this.getDescription().getVersion());
               return true;
            }                   
         }
         else
         {
            sender.sendMessage(ChatColor.YELLOW + "Ungueltige Anzahl Argumente.");
         }                

      }         
      return false; // if false is returned, the help for the command stated in the plugin.yml will be displayed to the player
   }

}


