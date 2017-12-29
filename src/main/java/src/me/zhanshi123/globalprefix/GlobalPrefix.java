package me.zhanshi123.globalprefix;

import me.zhanshi123.globalprefix.cacher.Cacher;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.Commands;
import me.zhanshi123.globalprefix.hooks.PrefixPlaceholder;
import me.zhanshi123.globalprefix.metrics.Metrics;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalPrefix extends JavaPlugin {
    private static GlobalPrefix plugin;

    public static GlobalPrefix getInstance() {
        return plugin;
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

    @Override
    public void onEnable() {
    	plugin = this;
        
        if (Bukkit.getPluginManager().getPlugin("TabooLib") == null) {
			Bukkit.getConsoleSender().sendMessage("§4[TabooLib - Version] §c缺少禁忌书库, 插件已关闭");
			return;
		}
		else if (Double.valueOf(Bukkit.getPluginManager().getPlugin("TabooLib").getDescription().getVersion()) < 3.1) {
			Bukkit.getConsoleSender().sendMessage("§4[TabooLib - Version] §c禁忌书库版本过低, 需要最低 3.1 版本");
			return;
		}
        
        new ConfigManager();
        new Database();
        new Cacher();
        new Commands();
        
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        
        if(papi == null){
            Bukkit.getConsoleSender().sendMessage(getMessage("placeholder.notfound"));
        } 
        else{
          if(!new PrefixPlaceholder().hook()){
              Bukkit.getConsoleSender().sendMessage(getMessage("placeholder.hook"));
          }
        }
        
        new Metrics(this);
        Bukkit.getConsoleSender().sendMessage(getMessage("loaded"));
    }

    @Override
    public void onDisable() {
        Database.getInstance().close();
    }
    
    public static String getPrefix(PlayerData data){
        if(data == null){
            return ConfigManager.getInstance().getNoData();
        }
        else{
            if(data.getPrefix() == null || data.getPrefix().isEmpty()){
                return ConfigManager.getInstance().getNoData();
            }
            else{
                return data.getPrefix();
            }
        }
    }
    
    public static String getSuffix(PlayerData data){
        if(data == null){
            return ConfigManager.getInstance().getNoData();
        }
        else{
            if(data.getSuffix()==null || data.getSuffix().isEmpty()){
                return ConfigManager.getInstance().getNoData();
            }
            else{
                return data.getSuffix();
            }
        }
    }
}
