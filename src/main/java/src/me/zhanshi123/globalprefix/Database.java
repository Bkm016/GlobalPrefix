package me.zhanshi123.globalprefix;

import me.skymc.taboolib.mysql.MysqlUtils;
import me.skymc.taboolib.mysql.protect.MySQLConnection;
import me.zhanshi123.globalprefix.cacher.PlayerData;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

public class Database {
	
    private static Database instance;
    private static MySQLConnection conn;

    public static Database getInstance() {
        return instance;
    }
    
    public static String getTable() {
    	return GlobalPrefix.getInstance().getConfig().getString("MySQL.table");
    }
    
    public Database(){
        instance = this;
        
        conn = MysqlUtils.getMySQLConnectionFromConfiguration(GlobalPrefix.getInstance().getConfig(), "MySQL");
        if (conn != null) {
        	conn.createTable(getTable(), "name", "prefix", "suffix");
        }
    }
    
    public void close(){
    	conn.closeConnection();
    }
    
    public PlayerData getData(String name){
        HashMap<String, Object> data = conn.getValue(getTable(), "name", name, "prefix", "suffix");
        return new PlayerData(name, data.get("prefix") == null ? "" : data.get("prefix").toString(), data.get("suffix") == null ? "" : data.get("suffix").toString());
    }
    
    public void updateData(PlayerData data){
    	new BukkitRunnable() {
			
			@Override
			public void run() {
				conn.setValue(getTable(), "name", data.getName(), "prefix", data.getPrefix());
		    	conn.setValue(getTable(), "name", data.getName(), "suffix", data.getSuffix());
			}
		}.runTaskAsynchronously(GlobalPrefix.getInstance());
    }
}
