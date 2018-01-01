package me.zhanshi123.globalprefix.commands.commands.subcommands;

import me.zhanshi123.globalprefix.ConfigManager;
import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.SubCommand;
import me.zhanshi123.globalprefix.manager.PlayerDataManager.VariableType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WareHouseCommand extends SubCommand{

    public WareHouseCommand() {
        super("warehouse");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
        	GlobalPrefix.getPlayerDataManager().open((Player) sender, VariableType.PREFIX, 1);
        }
        return false;
    }
}
