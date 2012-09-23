package com.github.CubieX.CartCleaner;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.CubieX.CartCleaner.CartCleaner;

public class CartCleanerConfigHandler 
{
    private FileConfiguration config;
    private final CartCleaner plugin;

    public CartCleanerConfigHandler(CartCleaner plugin) 
    {
        this.plugin = plugin;
        config = plugin.getConfig();

        if (config.get("language") == null) {
            config.set("language", "de");
        }
        if (config.get("debug") == null) {
            config.set("debug", false);
        } 

        plugin.saveConfig();
    }

    public FileConfiguration getConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        return config;
    }
}
