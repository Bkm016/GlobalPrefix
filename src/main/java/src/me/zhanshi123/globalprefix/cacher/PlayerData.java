package me.zhanshi123.globalprefix.cacher;

public class PlayerData {
    private String name, prefix, suffix;
    private long cachedTime;

    public PlayerData(String name, String prefix, String suffix, long cachedTime) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.cachedTime = cachedTime;
    }

    public PlayerData(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        cachedTime=System.currentTimeMillis();
    }

    public long getCachedTime() {
        return cachedTime;
    }

    public void setCachedTime(long cachedTime) {
        this.cachedTime = cachedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "{name:" + name + ",prefix:" + prefix + ",suffix:" + suffix + "}"; 
    }
}
