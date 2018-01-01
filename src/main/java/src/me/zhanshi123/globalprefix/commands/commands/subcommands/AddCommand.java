package me.zhanshi123.globalprefix.commands.commands.subcommands;

import me.zhanshi123.globalprefix.Database;
import me.zhanshi123.globalprefix.GlobalPrefix;
import me.zhanshi123.globalprefix.cacher.PlayerData;
import me.zhanshi123.globalprefix.commands.commands.SubCommand;
import me.zhanshi123.globalprefix.manager.PlayerDataManager.VariableType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AddCommand extends SubCommand{

    public AddCommand() {
        super("add");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()){
            if (args.length < 4){
                sender.sendMessage(GlobalPrefix.getMessage("add.empty"));
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
		                
						if (type.equalsIgnoreCase("p")){
		                	GlobalPrefix.getPlayerDataManager().add(name, VariableType.PREFIX, value);
		                }
		                else{
		                	GlobalPrefix.getPlayerDataManager().add(name, VariableType.SUFFIX, value);
		                }
		                
		                if (sender instanceof Player || GlobalPrefix.getInstance().getConfig().getBoolean("Settings.ConsoleUpdateFeedback")) {
		                	sender.sendMessage(GlobalPrefix.getMessage("add.success")
		            				.replace("<player>", name)
		            				.replace("<variable>", value.replace("&","§")));
		                }
					}
				}.runTaskAsynchronously(GlobalPrefix.getInstance());
                return true;
            }
        }
        return false;
    }
}