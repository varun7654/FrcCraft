package com.dacubeking.frccraft;

import com.dacubeking.frccraft.commands.SetTeamNumberCommand;
import com.dacubeking.frccraft.commands.StartControlCommand;
import com.dacubeking.frccraft.commands.StopControlCommand;
import com.dacubeking.frccraft.eventHandlers.FrcEventHandler;
import com.dacubeking.frccraft.robotcom.RobotPosition;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FrcCraft extends JavaPlugin {

    static FileConfiguration config;

    public boolean isControllingRobot = false;

    public static boolean isSneaking = false;

    public static RobotPosition currentRobotPosition = new RobotPosition(0, 0, 0, 0, 0, 0, 0, "Robot Position");

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        config.addDefault("teamNumber", "3476");

        config.options().copyDefaults(true);
        saveConfig();

        NetworkTableInstance.getDefault().startClientTeam(config.getInt("teamNumber"));

        NetworkTableEntry robotPositionsEntry = NetworkTableInstance.getDefault().getEntry("autodata/robotPositions");


        robotPositionsEntry.addListener(entryNotification -> {
            @Nullable String positions = robotPositionsEntry.getString(null);
            if (positions != null) {
                String[] positionsArray = positions.split(";");
                List<RobotPosition> positionsList = new ArrayList<>(positionsArray.length);
                for (String s : positionsArray) {
                    RobotPosition robotPosition = RobotPosition.fromString(s);
                    if (robotPosition != null) {
                        positionsList.add(robotPosition);
                    }
                }
                positionsList.stream().filter(robotPosition -> robotPosition.name.equals("Robot Position")).findFirst()
                        .ifPresent(robotPosition -> currentRobotPosition = robotPosition);
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kImmediate | EntryListenerFlags.kLocal);


        getCommand("setTeamNumber").setExecutor(new SetTeamNumberCommand());
        getCommand("startControl").setExecutor(new StartControlCommand(this));
        getCommand("stopControl").setExecutor(new StopControlCommand(this));

        Bukkit.getPluginManager().registerEvents(new FrcEventHandler(), this);
    }

    public static FileConfiguration getTheConfig() {
        return config;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
