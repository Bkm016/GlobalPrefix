package me.zhanshi123.globalprefix;

import me.skymc.taboolib.mysql.MysqlUtils;
import me.skymc.taboolib.mysql.protect.MySQLConnection;
import me.skymc.taboolib.plugin.PluginUtils;
import me.zhanshi123.globalprefix.cacher.PlayerData;

import java.sql.*;
import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

public class Database {
	
    private static MySQLConnection conn;

    public static void loadDatabase() {
        conn = MysqlUtils.getMySQLConnectionFromConfiguration(GlobalPrefix.getInstance().getConfig(), "MySQL", 60, GlobalPrefix.getInstance());
        if (conn.isConnection()) {
        	conn.createTable(getTable(), "name", "prefix", "suffix");
        }
    }
    
    public static MySQLConnection getConnection() {
    	return conn;
    }
    
    public static String getTable() {
    	return GlobalPrefix.getInstance().getConfig().getString("MySQL.table");
    }
    
    public static PlayerData getData(String name){
        HashMap<String, Object> data = conn.getValue(getTable(), "name", name, "prefix", "suffix");
        return new PlayerData(name, data.get("prefix") == null ? "" : data.get("prefix").toString(), data.get("suffix") == null ? "" : data.get("suffix").toString());
    }
    
    public static void updateData(PlayerData data){
    	if (isEmpty(data.getPrefix()) && isEmpty(data.getSuffix())) {
    		return;
    	}
    	new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!conn.isExists(getTable(), "name", data.getName())) {
					conn.intoValue(getTable(), data.getName(), data.getPrefix(), data.getSuffix());
				}
				else {
					conn.setValue(getTable(), "name", data.getName(), "prefix", data.getPrefix());
			    	conn.setValue(getTable(), "name", data.getName(), "suffix", data.getSuffix());
				}
			}
		}.runTaskAsynchronously(GlobalPrefix.getInstance());
    }
    
    private static boolean isEmpty(String string) {
    	return string == null || string.isEmpty();
    }
}
