package me.zhanshi123.globalprefix.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.skymc.taboolib.inventory.InventoryUtil;
import me.skymc.taboolib.sound.SoundPack;
import me.zhanshi123.globalprefix.ConfigManager;
import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.inventory.WareHouseInventory;

public class PlayerDataManager implements Listener {
	
	public enum VariableType {
		PREFIX, SUFFIX;
	}
	
	public PlayerDataManager() {
		Database.getConnection().createTable(getTable(), "name", "prefix", "suffix");
		Bukkit.getPluginManager().registerEvents(this, GlobalPrefix.getInstance());
	}
	
	public String getTable() {
		return Database.getTable() + "_warehouse";
	}
	
	public String toVariableString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String value : list) {
			sb.append(value + "/##/");
		}
		return sb.toString();
	}
	
	public List<String> getVariables(String player, VariableType type) {
		List<String> list = new ArrayList<>();
		if (Database.getConnection().isExists(getTable(), "name", player)) {
			Object object = Database.getConnection().getValue(getTable(), "name", player, type.name().toLowerCase());
			if (object != null) {
				for (String value : object.toString().split("/##/")) {
					if (!value.isEmpty()) {
						list.add(value);
					}
				}
			}
		}
		return list;
	}
	
	public void updateInventory(Player player) {
		if (player.getOpenInventory().getTopInventory().getHolder() instanceof WareHouseInventory) {
			WareHouseInventory holder = (WareHouseInventory) player.getOpenInventory().getTopInventory().getHolder();
    		open(player, holder.TYPE, holder.PAGE);
    	}
	}
	
	public void add(String player, VariableType type, String value) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				List<String> list = getVariables(player, type);
				list.add(value.replace("/##/", "?"));
				
				if (Database.getConnection().isExists(getTable(), "name", player)) {
					Database.getConnection().setValue(getTable(), "name", player, type.name().toLowerCase(), toVariableString(list));
				}
				else if (type == VariableType.PREFIX) {
					Database.getConnection().intoValue(getTable(), player, toVariableString(list), "");
				}
				else if (type == VariableType.SUFFIX) {
					Database.getConnection().intoValue(getTable(), player, "", toVariableString(list));
				}
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (Bukkit.getPlayerExact(player) != null) {
							updateInventory(Bukkit.getPlayerExact(player));
						}
					}
				}.runTask(GlobalPrefix.getInstance());
			}
		}.runTaskAsynchronously(GlobalPrefix.getInstance());
	}
	
	public void del(String player, VariableType type, String value) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!Database.getConnection().isExists(getTable(), "name", player)) {
					return;
				}
				List<String> list = getVariables(player, type);
				list.remove(value.replace("/##/", "?"));
				
				Database.getConnection().setValue(getTable(), "name", player, type.name().toLowerCase(), toVariableString(list));
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (Bukkit.getPlayerExact(player) != null) {
							updateInventory(Bukkit.getPlayerExact(player));
						}
					}
				}.runTask(GlobalPrefix.getInstance());
			}
		}.runTaskAsynchronously(GlobalPrefix.getInstance());
	}
	
	public void open(Player player, VariableType type, int page) {
		WareHouseInventory holder = new WareHouseInventory(page, type);
		Inventory inv = Bukkit.createInventory(holder, 54, GlobalPrefix.getMessage("warehouse.title-" + type.name().toLowerCase()));
		
		if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof WareHouseInventory)) {
			player.sendMessage(GlobalPrefix.getMessage("warehouse.messages.loading"));
		}
		
		new BukkitRunnable() {
			
			private boolean isEmptyTag(String name, PlayerData data) {
				return name.equals(ConfigManager.getInstance().getWareHouseEmptyTag()) 
						&& ((type == VariableType.PREFIX ? data.getPrefix() : data.getSuffix()) == null || (type == VariableType.PREFIX ? data.getPrefix() : data.getSuffix()).isEmpty());
			}
			
			@Override
			public void run() {
				ConfigurationSection conf = GlobalPrefix.getInstance().getConfig().getConfigurationSection("Settings.WareHouse");
				
				List<String> list = getVariables(player.getName(), type);
				list.add(0, ConfigManager.getInstance().getWareHouseEmptyTag());
				
				PlayerData data;
				try {
					data = Database.getData(player.getName());
				}
				catch (Exception e) {
					player.sendMessage(GlobalPrefix.getMessage("connection.close"));
					return;
				}
				
				Iterator<String> i = list.iterator();
				int slot = 0;
				int loop = 0;
				while (i.hasNext() && slot < 28) {
					String name = i.next();
					if (loop >= (page - 1) * 28) {
						if (loop < page * 28) {
							String append = "";
							if (data != null) {
								if (type == VariableType.PREFIX && (name.equals(data.getPrefix()) || isEmptyTag(name, data))) {
									append = conf.getString("useing");
								}
								else if (type == VariableType.SUFFIX && (name.equals(data.getSuffix()) || isEmptyTag(name, data))) {
									append = conf.getString("useing");
								}
							}
							
							ItemStack item = new ItemStack(Material.valueOf(conf.getString("item.type"))); {
								item.setDurability((short) conf.getInt("item.data"));
								ItemMeta meta = item.getItemMeta();
								meta.setDisplayName((conf.getString("item.name") + append)
										.replace("<name>", name)
										.replace("&", "¡ì"));
								meta.setLore(GlobalPrefix.coloredList(conf.getStringList("item.lore")));
								if (!append.isEmpty()) {
									meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
									meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
								}
								item.setItemMeta(meta);
							}
							inv.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(slot), item);
							holder.VARIABLE.put(InventoryUtil.SLOT_OF_CENTENTS.get(slot), name);
							slot++;
						}
						else {
							break;
						}
					}
					loop++;
				}
				
				if (page > 1) {
					ItemStack back = new ItemStack(Material.ARROW); {
						ItemMeta meta = back.getItemMeta();
						meta.setDisplayName(conf.getString("back").replace("&", "¡ì"));
						back.setItemMeta(meta);
					}
					inv.setItem(47, back);
				}
				
				if (list.size() % 28 != 0 && slot == 28) {
					ItemStack next = new ItemStack(Material.ARROW); {
						ItemMeta meta = next.getItemMeta();
						meta.setDisplayName(conf.getString("next").replace("&", "¡ì"));
						next.setItemMeta(meta);
					}
					inv.setItem(51, next);
				}
				
				ItemStack toggle = new ItemStack(Material.SIGN); {
					ItemMeta meta = toggle.getItemMeta();
					meta.setDisplayName(conf.getString("toggle-" + type.name().toLowerCase()).replace("&", "¡ì"));
					toggle.setItemMeta(meta);
				}
				inv.setItem(49, toggle);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof WareHouseInventory)) {
							new SoundPack("BLOCK_CHEST_OPEN-1-1").play(player);
						}
						if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof WareHouseInventory)) {
							player.sendMessage(GlobalPrefix.getMessage("warehouse.messages.loaded"));
						}
						player.openInventory(inv);
					}
				}.runTask(GlobalPrefix.getInstance());
			}
		}.runTaskAsynchronously(GlobalPrefix.getInstance());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!(e.getInventory().getHolder() instanceof WareHouseInventory) || e.getCurrentItem() == null) {
			return;
		}
		e.setCancelled(true);
		
		if (e.getRawSlot() > e.getInventory().getSize() || e.getRawSlot() < 0) {
			return;
		}
		
		WareHouseInventory holder = (WareHouseInventory) e.getInventory().getHolder();
		Player player = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() == 47) {
			open(player, holder.TYPE, holder.PAGE - 1);
		}
		else if (e.getRawSlot() == 51) {
			open(player, holder.TYPE, holder.PAGE + 1);
		}
		else if (e.getRawSlot() == 49) {
			if (holder.TYPE == VariableType.PREFIX) {
				open(player, VariableType.SUFFIX, 1);
			}
			else {
				open(player, VariableType.PREFIX, 1);
			}
		}
		else if (InventoryUtil.SLOT_OF_CENTENTS.contains(e.getRawSlot()) && holder.VARIABLE.containsKey(e.getRawSlot())) {
			PlayerData data;
			try {
				data = Database.getData(player.getName());
			}
			catch (Exception err) {
				player.sendMessage(GlobalPrefix.getMessage("connection.close"));
				player.closeInventory();
				return;
			}
			
			if (e.getCurrentItem().getEnchantments().size() > 0) {
				player.sendMessage(GlobalPrefix.getMessage("warehouse.messages.already"));
				return;
			}
			
			String value = holder.VARIABLE.get(e.getRawSlot());
			if (holder.TYPE == VariableType.PREFIX) {
				data.setPrefix(value.equals(ConfigManager.getInstance().getWareHouseEmptyTag()) ? null : value);
			}
			else {
				data.setSuffix(value.equals(ConfigManager.getInstance().getWareHouseEmptyTag()) ? null : value);
			}
			
			player.sendMessage(GlobalPrefix.getMessage("warehouse.messages.success").replace("<variable>", holder.VARIABLE.get(e.getRawSlot()).replace("&", "¡ì")));
			Database.updateData(data);
			
			updateInventory(player);
		}
	}
}
