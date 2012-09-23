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
import org.bukkit.material.PoweredRail;
import org.bukkit.plugin.java.JavaPlugin;

public class CartCleaner extends JavaPlugin implements Listener
{
    Logger log;

    private CartCleanerConfigHandler cHandler = null;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        log = this.getLogger();
        log.info("CartCleaner version " + getDescription().getVersion() + " is enabled!");

        cHandler = new CartCleanerConfigHandler(this);
    }

    public void onDisable()
    {
        log.info("CartCleaner version " + getDescription().getVersion() + " is disabled!");
        cHandler = null;
    }    

    //================================================================================================
    @EventHandler // event has Normal priority
    public void onPlayerLogin(PlayerLoginEvent event)
    {                
        // Your code here...        
    }   

    //================================================================================================
    @EventHandler // event has Normal priority
    public void onVehicleExit(VehicleExitEvent event)
    {    
        boolean isPowered = false;
        
        if(event.getVehicle() instanceof Minecart)
        {
            if(cHandler.getConfig().getBoolean("debug")){log.info("Aus Minecart ausgestiegen");}

            Location posBlockUcart = event.getVehicle().getLocation(); // set location to Block underneath the Minecart (the block it is IN actually)

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


    //=============================================================================================================
    // Command Handler -- ToDo: SHOULD BE IN SEPARATE CLASS!!! ---------------------------------------------
    // player typed a command

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) 
        {
            player = (Player) sender;
        }

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
                sender.sendMessage(ChatColor.YELLOW + "Ungültige Anzahl Argumente.");
            }                

        }         
        return false; // if false is returned, the help for the command stated in the plugin.yml will be displayed to the player
    }

}


