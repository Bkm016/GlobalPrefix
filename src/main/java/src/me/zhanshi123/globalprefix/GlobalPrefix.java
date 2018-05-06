package me.zhanshi123.globalprefix;

import me.skymc.taboolib.Main;
import me.skymc.taboolib.plugin.PluginUtils;
import me.zhanshi123.globalprefix.cacher.Cacher;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.Commands;
import me.zhanshi123.globalprefix.hooks.PrefixPlaceholder;
import me.zhanshi123.globalprefix.inventory.WareHouseInventory;
import me.zhanshi123.globalprefix.manager.PlayerDataManager;
import me.zhanshi123.globalprefix.metrics.Metrics;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalPrefix extends JavaPlugin {

    /**
     * IDEA Git 更新测试
     * 2018-5-6 23:32:44
     */

    private static GlobalPrefix plugin;
    private static PlayerDataManager playerdatamanager;

    public static GlobalPrefix getInstance() {
        return plugin;
    }
    
    public static PlayerDataManager getPlayerDataManager() {
    	return playerdatamanager;
    }
    
    public static ConfigurationSection getSettings(String key) {
    	return plugin.getConfig().getConfigurationSection("Settings." + key);
    }
    
	public static String getMessage(String key) {
		return plugin.getConfig().getString("Messages." + key).replace("&", "§");
	}
	
	public static List<String> getMessageList(String key) {
		return coloredList(plugin.getConfig().getStringList("Messages." + key));
	}
	
	public static List<String> coloredList(List<String> list) {
		for (int i = 0; i< list.size() ; i++) {
			list.set(i, list.get(i).replace("&", "§"));
		}
		return list;
	}
	
	public static String getPrefix(PlayerData data) {
        if (data == null){
            return ConfigManager.getInstance().getNoData();
        }
        else {
            if (data.getPrefix() == null || data.getPrefix().isEmpty()){
                return ConfigManager.getInstance().getNoData();
            }
            else {
                return data.getPrefix();
            }
        }
    }
    
    public static String getSuffix(PlayerData data) {
        if (data == null){
            return ConfigManager.getInstance().getNoData();
        }
        else {
            if (data.getSuffix()==null || data.getSuffix().isEmpty()){
                return ConfigManager.getInstance().getNoData();
            }
            else {
                return data.getSuffix();
            }
        }
    }

    @Override
    public void onEnable() {
    	plugin = this;
    	saveDefaultConfig();
        
        if (Bukkit.getPluginManager().getPlugin("TabooLib") == null) {
			Bukkit.getConsoleSender().sendMessage("§4[TabooLib - Version] §c缺少禁忌书库, 插件已关闭");
			return;
		}
		else if (Double.valueOf(Bukkit.getPluginManager().getPlugin("TabooLib").getDescription().getVersion()) < 3.15) {
			Bukkit.getConsoleSender().sendMessage("§4[TabooLib - Version] §c禁忌书库版本过低, 需要最低 3.15 版本");
			return;
		}
        
        Database.loadDatabase();
        if (Database.getConnection().isConnection()) {
        	playerdatamanager = new PlayerDataManager();
        }
        else {
            Bukkit.getConsoleSender().sendMessage(GlobalPrefix.getMessage("connection.close"));
        	setEnabled(false);
        	return;
        }
        
        new ConfigManager();
        new Cacher();
        new Commands();
        new Metrics(this);
        
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (papi == null) {
            Bukkit.getConsoleSender().sendMessage(getMessage("placeholder.notfound"));
        } 
        else{
        	if (!new PrefixPlaceholder().hook()) {
        		Bukkit.getConsoleSender().sendMessage(getMessage("placeholder.hook"));
        	}
        }
        Bukkit.getConsoleSender().sendMessage(getMessage("loaded"));
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
        	if (player.getOpenInventory().getTopInventory().getHolder() instanceof WareHouseInventory) {
        		player.closeInventory();
        	}
        }
    }
}
