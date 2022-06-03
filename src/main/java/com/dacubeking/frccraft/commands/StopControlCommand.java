package com.dacubeking.frccraft.commands;

import com.dacubeking.frccraft.FrcCraft;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopControlCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (FrcCraft.isControllingRobot) {
            FrcCraft.isControllingRobot = false;
            sender.sendMessage("Stopped controlling robot");
        } else {
            sender.sendMessage("Control of the robot is already inactive");
        }
        NetworkTableInstance.getDefault().getEntry("frccraft/control").setBoolean(false);
        return true;
    }
}
