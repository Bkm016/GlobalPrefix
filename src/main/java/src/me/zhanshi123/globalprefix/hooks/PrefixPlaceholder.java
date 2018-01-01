package me.zhanshi123.globalprefix.hooks;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.zhanshi123.globalprefix.ConfigManager;
import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.Cacher;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PrefixPlaceholder extends EZPlaceholderHook{
    
    public PrefixPlaceholder(){
        super(GlobalPrefix.getInstance(), "gp");
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        String name = player.getName();
        try {
	        if(s.equalsIgnoreCase("cached_prefix")){
	            return GlobalPrefix.getPrefix(Cacher.getInstance().get(name)).replace("&","¡ì");
	        }
	        else if(s.equalsIgnoreCase("cached_suffix")){
	            return GlobalPrefix.getSuffix(Cacher.getInstance().get(name)).replace("&","¡ì");
	        }
	        else if(s.equalsIgnoreCase("prefix")){
	            return GlobalPrefix.getPrefix(Database.getData(name)).replace("&","¡ì");
	        }
	        else if(s.equalsIgnoreCase("suffix")){
	            return GlobalPrefix.getSuffix(Database.getData(name)).replace("&","¡ì");
	        }
	        else {
	            return null;
	        }
        }
        catch (Exception e) {
        	return "-";
		}
    }
}
