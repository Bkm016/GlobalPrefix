package me.zhanshi123.globalprefix.commands.commands.subcommands;

import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.SubCommand;
import me.zhanshi123.globalprefix.manager.PlayerDataManager.VariableType;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DelCommand extends SubCommand{

    public DelCommand() {
        super("del");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()){
            if (args.length < 4){
                sender.sendMessage(GlobalPrefix.getMessage("del.empty"));
                return false;
            } 
            else {
                new BukkitRunnable() {
					
					@Override
					public void run() {
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
		                	return;
						}
		                
		                if (type.equalsIgnoreCase("p")){
		                	GlobalPrefix.getPlayerDataManager().del(name, VariableType.PREFIX, value);
		                	if (data.getPrefix().equals(value)) {
		                		data.setPrefix("");
		                	}
		                }
		                else{
		                	GlobalPrefix.getPlayerDataManager().del(name, VariableType.SUFFIX, value);
		                	if (data.getSuffix().equals(value)) {
		                		data.setSuffix("");
		                	}
		                }
		                
		                if (sender instanceof Player || GlobalPrefix.getInstance().getConfig().getBoolean("Settings.ConsoleUpdateFeedback")) {
		                	sender.sendMessage(GlobalPrefix.getMessage("del.success")
		            				.replace("<player>", name)
		            				.replace("<variable>", value.replace("&","§")));
		                }
		                
		                Database.updateData(data);
					}
				}.runTaskAsynchronously(GlobalPrefix.getInstance());
                return true;
            }
        }
        return false;
    }
}