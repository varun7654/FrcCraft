package com.dacubeking.frccraft.commands;

import com.dacubeking.frccraft.FrcCraft;
import com.dacubeking.frccraft.robotcom.RobotPosition;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartControlCommand implements CommandExecutor {

    public final FrcCraft frcCraft;

    public StartControlCommand(FrcCraft frcCraft) {
        this.frcCraft = frcCraft;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (!frcCraft.isControllingRobot) {
                frcCraft.isControllingRobot = true;
                sender.sendMessage("Started controlling robot");
            } else {
                sender.sendMessage("Control of the robot is already active");
            }
            NetworkTableInstance.getDefault().getEntry("frccraft/control").setBoolean(true);

            RobotPosition robotPosition = FrcCraft.currentRobotPosition;
            player.teleport(
                    new Location(player.getWorld(), robotPosition.x * 3, player.getLocation().getY(), robotPosition.y * 3,
                            (float) robotPosition.theta, player.getLocation().getPitch()));
        } else {
            sender.sendMessage("This command can only be used by a player");
        }

        return true;
    }
}
