package com.github.CubieX.CartCleaner;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class CartCleanerEntityListener implements Listener
{
   Logger eLog;
   ArrayList<String> playersInSM = new ArrayList<String>();
   private CartCleaner plugin = null;
   private CartCleanerConfigHandler cHandler = null;
   private Logger log = null;

   public CartCleanerEntityListener(CartCleaner plugin, CartCleanerConfigHandler cHandler, Logger log)
   {        
      this.plugin = plugin;
      this.cHandler = cHandler;
      this.log = log;

      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   //-----------------------------------------------------------------------------------------------------
   @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
   public void onVehicleExit(VehicleExitEvent event)
   {    
      boolean isPowered = false;

      if(event.getVehicle() instanceof Minecart)
      {
         if(cHandler.getConfig().getBoolean("debug")){log.info("Aus Minecart ausgestiegen");}

         Location posBlockUcart = event.getVehicle().getLocation(); // set location to block where the cart is IN. = Block of the rail.)

         World w = posBlockUcart.getWorld();
         Block blockUnderCart = w.getBlockAt(posBlockUcart);

         boolean doRemoveCart = false;

         if(blockUnderCart.getType() != Material.POWERED_RAIL)
         {
            doRemoveCart = true;  
            if(cHandler.getConfig().getBoolean("debug")){log.info("Block ist keine PowerRail. Entferne Minecart.");}
         }   
         else //it's a Powered Rail
         { 
            isPowered = (blockUnderCart.getData() & 8) != 0; // gets the isPowered()-State out of metadata. (8 only fits for PoweredRail!)

            if(isPowered) //is it active?
            {
               doRemoveCart = true;
               if(cHandler.getConfig().getBoolean("debug")){log.info("Block ist gepowerte PowerRail. Entferne Minecart.");}
            }
         }

         if(doRemoveCart)
         {
            Player rider = (Player) event.getVehicle().getPassenger();                
            event.getVehicle().remove();
            rider.sendMessage(ChatColor.GREEN + "Dein Minecart wurde entfernt.");
            if(cHandler.getConfig().getBoolean("debug")){log.info("Minecart entfernt. Block drunter ist: " + blockUnderCart.getType().toString() + " bei x: " + blockUnderCart.getX() + " y: " + blockUnderCart.getY() + " z: " + blockUnderCart.getX() + " isPowered: " + isPowered);}                
         }
      }
   }
}
