package com.crusadecraft.townyreligion;

import com.crusadecraft.townyreligion.command.ReligionAdminCommand;
import com.crusadecraft.townyreligion.command.ReligionChatCommand;
import com.crusadecraft.townyreligion.command.ReligionCommand;
import com.crusadecraft.townyreligion.listeners.ResidentEventListener;
import com.crusadecraft.townyreligion.listeners.TownEventListener;
import com.crusadecraft.townyreligion.settings.Settings;
import com.crusadecraft.townyreligion.settings.TownyReligionSettings;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class TownyReligion extends JavaPlugin {

    private static TownyReligion plugin;
    public static String prefix = "[TownyReligion] ";
    private static final Version requiredTownyVersion = Version.fromString("0.97.5.3.3");
    public static boolean safeModeEnabled = false;

    public static TownyReligion getTownyReligion() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;

        printSickASCIIArt();

        if (!townyVersionCheck(getTownyVersion())) {
            severe("Your Towny version does not meet required minimum version: " + requiredTownyVersion.toString() + ". Instead, found version: " + getTownyVersion().toString() + ".");
            severe("Please update Towny and restart your server.");
            severe("https://github.com/V1nc3ntWasTaken/Towny/releases/tag/0.97.5.3.3");
            setSafeModeEnabled(true);
        } else {
            try {
                TownyAPI.isCustomVersion();
            } catch (NoSuchMethodError ignored) {
                severe("Your Towny version is not available for this plugin.");
                severe("Please update Towny to Custom Towny 0.97.5.3.3 and restart your server.");
                severe("https://github.com/V1nc3ntWasTaken/Towny/releases/tag/0.97.5.3.3");
                setSafeModeEnabled(true);
            }

            if (!getSafeModeEnabled()) {
                info("Towny version " + getTownyVersion() + " found.");
            }
        }


        if (!Settings.loadSettingsAndLang()) {
            if (!getSafeModeEnabled()) {
                severe("An error occurred while loading the settings and language files!");
                setSafeModeEnabled(true);
            }
        }

        // InviteHandler.initialize(this);

        if (TownyReligionSettings.getTownyReligionEnabled()) {

            registerListeners();

            registerCommands();

            registerDatabase();

            if (!getSafeModeEnabled()) {
                info("TownyReligion was successfully loaded!");
            }
        } else {
            if (!getSafeModeEnabled()) {
                info("TownyReligion was successfully loaded, but is disabled in the config!");
            }
        }
    }

    @Override
    public void onDisable() {
        severe("Shutting down TownyReligion....");
    }

    public static void setSafeModeEnabled(boolean enabled) {
        if (enabled) {
            if (!safeModeEnabled) {
                safeModeEnabled = true;
                severe("TownyReligion is now in safe mode! See console for more details.");
            }
        } else {
            if (safeModeEnabled) {
                safeModeEnabled = false;
                info("TownyReligion is no longer in safe mode.");
            }
        }
    }

    public static boolean getSafeModeEnabled() {
        return safeModeEnabled;
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new TownEventListener(), this);
        pm.registerEvents(new ResidentEventListener(), this);
    }

    private void registerCommands() {
        getCommand("religion").setExecutor(new ReligionCommand());
        getCommand("religionadmin").setExecutor(new ReligionAdminCommand());
        getCommand("rc").setExecutor(new ReligionChatCommand());
    }

    public static void registerDatabase() {
        FlatFile.createFolders();
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public static boolean townyVersionCheck(String version) {
        return Version.fromString(version).compareTo(requiredTownyVersion) >= 0;
    }

    public static String getTownyVersion() {
        return Bukkit.getPluginManager().getPlugin("Towny").getDescription().getVersion();
    }

    public static Version getRequiredTownyVersion() {
        return requiredTownyVersion;
    }

    private void printSickASCIIArt() {
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("  _____                            ");
        Bukkit.getLogger().info(" |_   _|                           ");
        Bukkit.getLogger().info("   | | _____      ___ __  _   _    ");
        Bukkit.getLogger().info("   | |/ _ \\ \\ /\\ / / '_ \\| | | |   ");
        Bukkit.getLogger().info("   | | (_) \\ V  V /| | | | |_| |   ");
        Bukkit.getLogger().info("   \\_/\\___/ \\_/\\_/ |_| |_|\\__, |   ");
        Bukkit.getLogger().info("                           __/ |   ");
        Bukkit.getLogger().info("      By V1nc3ntWasTaken  |___/    ");
        Bukkit.getLogger().info("______     _ _       _             ");
        Bukkit.getLogger().info("| ___ \\   | (_)     (_)            ");
        Bukkit.getLogger().info("| |_/ /___| |_  __ _ _  ___  _ __  ");
        Bukkit.getLogger().info("|    // _ \\ | |/ _` | |/ _ \\| '_ \\ ");
        Bukkit.getLogger().info("| |\\ \\  __/ | | (_| | | (_) | | | |");
        Bukkit.getLogger().info("\\_| \\_\\___|_|_|\\__, |_|\\___/|_| |_|");
        Bukkit.getLogger().info("                __/ |              ");
        Bukkit.getLogger().info("               |___/               ");
        Bukkit.getLogger().info("");
    }

    public static void info(String message) {
        // Messaging.sendMsg(Bukkit.getConsoleSender(), message);
        plugin.getLogger().log(Level.INFO, message);
    }

    public static void severe(String message) {
        // Messaging.sendErrorMsg(Bukkit.getConsoleSender(), message);
        plugin.getLogger().log(Level.SEVERE, ChatColor.RED + message);
    }
}
