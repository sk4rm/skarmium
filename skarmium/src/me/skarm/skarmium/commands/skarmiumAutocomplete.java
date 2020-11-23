package me.skarm.skarmium.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class skarmiumAutocomplete implements TabCompleter {

    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            if (cmd.getName().equalsIgnoreCase("flag")) {
                // flag ...
                List<String> possible_arguments = new ArrayList<>();
                possible_arguments.add("help");
                possible_arguments.add("tool");
                possible_arguments.add("set");
                possible_arguments.add("remove");

                return possible_arguments;
            }
        }

        return null;
    }
}
