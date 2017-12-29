package me.zhanshi123.globalprefix.cacher;

import me.zhanshi123.globalprefix.GlobalPrefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CacheReleaseTask extends BukkitRunnable {
    @Override
    public void run() {
        HashMap<String,PlayerData> data=Cacher.getInstance().getData();
        Collection<Player> players=getOnlinePlayers();
        HashMap<String,PlayerData> temp= (HashMap<String, PlayerData>) data.clone();
        new BukkitRunnable(){
			@Override
            public void run() {
                for(Map.Entry<String,PlayerData> entry:data.entrySet()){
                    if(!players.contains(entry.getKey())){
                        temp.remove(entry.getKey());
                    }
                }
                Cacher.getInstance().setData(temp);
            }
        }.runTaskAsynchronously(GlobalPrefix.getInstance());
    }
    
	private Collection<Player> getOnlinePlayers(){
        Collection<Player> players = null;
        try {
            Class<?> clazz = Class.forName("org.bukkit.Bukkit");
            Method method = clazz.getMethod("getOnlinePlayers");
            if (method.getReturnType().equals(Collection.class)) {
                players = (Collection<Player>) method.invoke(Bukkit.getServer());
            } else {
                players = Arrays.asList((Player[]) method.invoke(Bukkit.getServer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }
}
