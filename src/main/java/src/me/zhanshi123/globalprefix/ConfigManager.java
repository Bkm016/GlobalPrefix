package me.zhanshi123.globalprefix;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    private static ConfigManager instance=null;
    
    public static ConfigManager getInstance() {
        return instance;
    }
    
    private GlobalPrefix plugin=GlobalPrefix.getInstance();
    private File f=null;
    private FileConfiguration config;
    public ConfigManager() {
        try {
            config=new YamlConfiguration();
            f = new File(plugin.getDataFolder(),"config.yml");
            if(!f.exists()) {
                plugin.saveResource("config.yml",false);
            }
            config.load(new BufferedReader(new InputStreamReader(new FileInputStream(f), Charsets.UTF_8)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        instance=this;
    }
    
    public List<String> getMySQL() {
        return Arrays.asList(config.getString("MySQL.address"),
                config.getString("MySQL.user"),
                config.getString("MySQL.pwd"));
    }
    
    public long getInterval(){
        return config.getLong("Interval");
    }
    
    public String getNoData(){
        return GlobalPrefix.getMessage("placeholder.empty");
    }
}
