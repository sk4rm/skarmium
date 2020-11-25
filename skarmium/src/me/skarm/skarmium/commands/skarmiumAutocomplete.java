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
                possible_arguments.add("team");
                possible_arguments.add("remove");
                return possible_arguments;

            }

            if (cmd.getName().equalsIgnoreCase("score")) {
                // score ...
                List<String> possible_arguments = new ArrayList<>();
                possible_arguments.add("help");
                possible_arguments.add("setup");
                possible_arguments.add("reset");
                possible_arguments.add("limit");
                return possible_arguments;
            }

        } else if (args.length == 2) {
            if (cmd.getName().equalsIgnoreCase("flag")) {
                // flag ... ...
                if (args[0].equalsIgnoreCase("team")) {
                    List<String> possible_arguments = new ArrayList<>();
                    possible_arguments.add("red");
                    possible_arguments.add("blue");
                    possible_arguments.add("gray");

                    return possible_arguments;
                }
            }
        }

        return null;
    }
}
