package com.textradar.plugin;

import java.lang.Thread;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RadarThread extends Thread{

    public boolean running = true;

    public int timeout = 120;

    public void run(){
        while (running){
            try{
                this.sleep(timeout * 1000);
            }
            catch (InterruptedException i){
                running = false;
            }
            Collection<? extends Player> online = Bukkit.getOnlinePlayers();
            if (online.size() > 0){
                Bukkit.broadcastMessage("Player locations...");
                for (Player p: online){
                    World w = p.getWorld();
                    if (w == null){
                        continue;
                    }
                    Location l = p.getLocation();
                    int x, y, z;
                    x = l.getBlockX();
                    y = l.getBlockY();
                    z = l.getBlockZ();
                    Bukkit.broadcastMessage(p.getName() + " - " + w.getName() + " - " + x + " " + y + " " + z);
                }
            }
        }
    }

}
