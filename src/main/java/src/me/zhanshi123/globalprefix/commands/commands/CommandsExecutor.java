package me.zhanshi123.globalprefix.commands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.zhanshi123.globalprefix.GlobalPrefix;

public class CommandsExecutor implements CommandExecutor
{
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sendHelp(commandSender);
            return false;
        }
        else {
            String arg = args[0];
            SubCommand subCommand=Commands.getInstance().getSubCommand(arg);
            if(subCommand == null)  {
                subCommand = Commands.getInstance().getSubCommand("*");
            }
            return subCommand.onCommand(commandSender, command, label, args);
        }
    }
    
    private void sendHelp(CommandSender sender) {
    	for (String message : GlobalPrefix.getMessageList("command.query")) {
    		sender.sendMessage(message);
    	}
        if(sender.hasPermission("gp.update")){
        	for (String message : GlobalPrefix.getMessageList("command.update")) {
        		sender.sendMessage(message);
        	}
        	for (String message : GlobalPrefix.getMessageList("command.add")) {
        		sender.sendMessage(message);
        	}
        	for (String message : GlobalPrefix.getMessageList("command.del")) {
        		sender.sendMessage(message);
        	}
        }
    }
}
