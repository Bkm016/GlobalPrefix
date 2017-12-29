package me.zhanshi123.globalprefix.cacher;

import me.zhanshi123.globalprefix.ConfigManager;
import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;

import java.util.HashMap;

public class Cacher {
    private static Cacher instance;

    public static Cacher getInstance() {
        return instance;
    }
    private long interval;
    public Cacher() {
        instance=this;
        interval= ConfigManager.getInstance().getInterval();
        new CacheReleaseTask().runTaskTimer(GlobalPrefix.getInstance(),20*60L,20*60L);
    }

    private HashMap<String,PlayerData> data=new HashMap<>();

    public HashMap<String, PlayerData> getData() {
        return data;
    }

    public void setData(HashMap<String, PlayerData> data) {
        this.data = data;
    }



    public PlayerData get(String player){
        PlayerData playerData=null;
        if(data.containsKey(player)){
            playerData=data.get(player);
            if(playerData.getCachedTime()+interval>=System.currentTimeMillis()){
                return playerData;
            }
            else{
                long time=System.currentTimeMillis();
                PlayerData temp=Database.getInstance().getData(player);
                playerData=temp;
                playerData.setCachedTime(time);
                add(player,playerData,time);
                return playerData;
            }
        }
        else{
            long time=System.currentTimeMillis();
            PlayerData temp=Database.getInstance().getData(player);
            playerData=temp;
            playerData.setCachedTime(time);
            add(player,playerData,time);
            return playerData;
        }
    }

    public void add(String player,PlayerData pdata,long time){
        if(data.containsKey(player)){
            PlayerData playerData=data.get(player);
            if(playerData.getCachedTime()+interval>=System.currentTimeMillis()){
                data.remove(player);
                data.put(player,new PlayerData(player,pdata.getPrefix(),pdata.getPrefix(),time));
            }
        }
        else{
            data.put(player,new PlayerData(player,pdata.getPrefix(),pdata.getPrefix(),time));
        }
    }
}
