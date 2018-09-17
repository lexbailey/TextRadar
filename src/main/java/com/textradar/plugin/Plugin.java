package com.textradar.plugin;

import java.util.HashMap;
import java.util.Collection;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private final File propFile = new File("textradar.properties");

    Properties props;
    RadarThread radar = null;

    private void stopRadar(){
        if (radar != null){
            radar.running = false;
            radar.interrupt();
            try{
                radar.join();
            }
            catch (InterruptedException i){}
            radar = null;
        }
    }

    @Override
    public void onDisable() {
        // NOTE: All registered events are automatically unregistered when a plugin is disabled
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        getLogger().info("Disabling Text Radar");
        stopRadar();
    }

    @Override
    public void onEnable() {
        props = new Properties();
        if (!propFile.exists()){
            try{
                FileOutputStream defaultProps = new FileOutputStream(propFile);
                defaultProps.write("freq=120\n".getBytes("UTF-8"));
                defaultProps.close();
            }
            catch (Exception e){
                getLogger().severe("Unable to write default textradar settings. Message:" + e.toString());
            }
        }
        try{
            props.load(new FileInputStream(propFile));
        }
        catch (Exception e){
            getLogger().severe("Unable to load textradar settings. Message:" + e.toString());
        }

        // Register our events
        PluginManager pm = getServer().getPluginManager();

        PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );

        stopRadar();
        radar = new RadarThread();
        radar.timeout = Integer.parseInt(props.getProperty("freq", "120"));
        radar.start();
    }

}
