package me.zhanshi123.globalprefix.inventory;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.zhanshi123.globalprefix.manager.PlayerDataManager.VariableType;

public class WareHouseInventory implements InventoryHolder {
	
	public final int PAGE;
	public final VariableType TYPE;
	public final HashMap<Integer, String> VARIABLE = new HashMap<>();
	
	public WareHouseInventory(int page, VariableType type) {
		this.PAGE = page;
		this.TYPE = type;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}
}
