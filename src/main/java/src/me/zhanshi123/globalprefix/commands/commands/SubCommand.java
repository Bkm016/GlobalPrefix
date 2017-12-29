package me.zhanshi123.globalprefix.commands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand
{
    String name=null;
    public SubCommand(String name)
    {
        this.name=name;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    public String getName()
    {
        return name;
    }
}
