package me.zhanshi123.globalprefix.commands.commands.subcommands;

import me.zhanshi123.globalprefix.ConfigManager;
import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueryCommand extends SubCommand{

    public QueryCommand() {
        super("query");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            String name = player.getName();
            
            PlayerData data;
            try {
            	data = Database.getData(name);
            }
            catch (Exception e) {
                sender.sendMessage(GlobalPrefix.getMessage("connection.close"));
            	return false;
			}
            
            if(data == null){
	        	player.sendMessage(GlobalPrefix.getMessage("query.notfound"));
            } 
            else {
            	for (String message : GlobalPrefix.getMessageList("command.update")) {
            		sender.sendMessage(message
            				.replace("<prefix>", GlobalPrefix.getPrefix(data).replace("&","§"))
            				.replace("<suffix>", GlobalPrefix.getSuffix(data).replace("&","§")));
            	}
            }
        }
        return true;

    }
}
