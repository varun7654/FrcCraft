package com.dacubeking.frccraft.commands;

import com.dacubeking.frccraft.FrcCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetTeamNumberCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        try {
            int teamNumber = Integer.parseInt(args[0]);
            if (teamNumber < 0 || teamNumber > 255) {
                sender.sendMessage("Team number must be between 0 and 255");
                return true;
            }

            sender.sendMessage("Team number set to " + teamNumber);
            sender.sendMessage("Restart the server to apply the change");
            FrcCraft.getTheConfig().set("teamNumber", teamNumber);
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Team number must be an integer");
            return true;
        }
    }
}
