package me.zhanshi123.globalprefix.commands.commands.subcommands;

import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateCommand extends SubCommand{

    public UpdateCommand() {
        super("update");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()){
            if (args.length < 4){
                sender.sendMessage(GlobalPrefix.getMessage("update.empty"));
                return false;
            } 
            else {
                String name = args[1];
                String type = args[2];
                StringBuilder sb = new StringBuilder();
                for (int i = 3 ; i < args.length ; i++) {
                	sb.append(args[i] + " ");
                }
                String value = sb.substring(0, sb.length() - 1);
                
                PlayerData data;
                try {
                	data = Database.getData(name);
                }
                catch (Exception e) {
                    sender.sendMessage(GlobalPrefix.getMessage("connection.close"));
                	return false;
				}
                
                if (data == null){
                    if (type.equalsIgnoreCase("p")){
                        data = new PlayerData(name, value, null);
                    }
                    else{
                        data = new PlayerData(name, null, value);
                    }
                } 
                else {
                    if (type.equalsIgnoreCase("p")){
                        data.setPrefix(value);
                    }
                    else {
                        data.setSuffix(value);
                    }
                }
                Database.updateData(data);
                
                if (sender instanceof Player || GlobalPrefix.getInstance().getConfig().getBoolean("Settings.ConsoleUpdateFeedback")) {
                	for (String message : GlobalPrefix.getMessageList("update.success")) {
                		sender.sendMessage(message
                				.replace("<prefix>", GlobalPrefix.getPrefix(data).replace("&","§"))
                				.replace("<suffix>", GlobalPrefix.getSuffix(data).replace("&","§")));
                	}
                }
                return true;
            }
        }
        return false;
    }
}