package com.dacubeking.frccraft.eventHandlers;

import com.dacubeking.frccraft.FrcCraft;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.NotNull;

public class FrcEventHandler implements Listener {


    @EventHandler
    public void PlayerMoveEvent(@NotNull PlayerMoveEvent playerMoveEvent) {
        double x = playerMoveEvent.getPlayer().getLocation().getX();
        double y = playerMoveEvent.getPlayer().getLocation().getZ();

        double theta = playerMoveEvent.getPlayer().getLocation().getYaw();

        NetworkTableInstance.getDefault().getEntry("frccraft/x").setDouble(x / 3);
        NetworkTableInstance.getDefault().getEntry("frccraft/y").setDouble(y / 3);
        NetworkTableInstance.getDefault().getEntry("frccraft/theta").setDouble(theta / 3);
    }

    @EventHandler
    public void playerToggleSneakEvent(@NotNull PlayerToggleSneakEvent playerToggleSneakEvent) {
        FrcCraft.isSneaking = playerToggleSneakEvent.getPlayer().isSneaking();
        NetworkTableInstance.getDefault().getEntry("frccraft/sneak").setBoolean(FrcCraft.isSneaking);
    }

    @EventHandler
    public void onTick(@NotNull ServerTickStartEvent serverTickStartEvent) {
        if (FrcCraft.isSneaking) {
            Bukkit.getOnlinePlayers().stream().findFirst().ifPresent(player -> player.setRotation(
                    (float) FrcCraft.currentRobotPosition.theta, player.getLocation().getPitch()
            ));
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent playerJoinEvent) {
        if (Bukkit.getOnlinePlayers().size() > 1 && !FrcCraft.isControllingRobot) {
            playerJoinEvent.getPlayer().kick(Component.text("Only one player is allowed to play at a time"));
        }
    }

    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent playerQuitEvent) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            NetworkTableInstance.getDefault().getEntry("frccraft/control").setBoolean(false);
        }
    }
}
