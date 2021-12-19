package com.crusadecraft.townyreligion.command;

/*import com.crusadecraft.townyreligion.Database;*/
import com.crusadecraft.townyreligion.Messaging;
import com.crusadecraft.townyreligion.TownyReligion;
import com.crusadecraft.townyreligion.enums.TownyReligionPermissionNodes;
import com.crusadecraft.townyreligion.metadata.TownMetaDataController;
import com.crusadecraft.townyreligion.settings.Settings;
import com.crusadecraft.townyreligion.settings.Translation;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReligionAdminCommand implements CommandExecutor, TabCompleter {

	private static final List<String> townyReligionAdminTabCompletes = Arrays.asList("reload", "alltowns", "town");

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		switch (args[0].toLowerCase()) {
			case "alltowns":
				if (args.length == 2)
					return NameUtil.filterByStart(Arrays.asList("set"), args[1]);
				else if (args.length == 3)
					return NameUtil.filterByStart(Arrays.asList("religion"), args[2]);
				else
					return Collections.emptyList();
			case "town":
				if (args.length == 2)
					return getTownsStartingWith(args[1]);
				else if (args.length == 3)
					return NameUtil.filterByStart(Arrays.asList("set"), args[2]);
				else if (args.length == 4)
					return NameUtil.filterByStart(Arrays.asList("religion"), args[3]);
				/*else if (args.length == 5) {
					return getReligionsStartingWith(args[1]);
				}*/
				return Collections.emptyList();
			default:
				if (args.length == 1)
					return NameUtil.filterByStart(townyReligionAdminTabCompletes, args[0]);
				else
					return Collections.emptyList();
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		parseReligionAdminCommand(sender, args);
		return true;
	}

	private void parseReligionAdminCommand(CommandSender sender, String[] args) {
		/*
		 * Parse Command.
		 */
		if (args.length > 0) {

			if (sender instanceof Player && !(sender.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_COMMAND_ADMIN.getNode(args[0])))) {
				Messaging.sendErrorMsg(sender, Translation.of("msg_err_command_disable"));
				return;
			}

			switch (args[0]) {
			case "reload":
				parseReligionAdminReloadCommand(sender);
				break;
			case "alltowns":
				// parseReligionAdminAllTownsCommand(sender, StringMgmt.remFirstArg(args));
				break;
			case "town":
				// parseReligionAdminTownCommand(sender, StringMgmt.remFirstArg(args));
				break;
			default:
				showHelp(sender);
			}
		} else {
			showHelp(sender);
		}
	}
	
	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/religionadmin"));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ra", "reload", Translation.of("admin_help_1")));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ra", "alltowns set religion [religion]", ""));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ra", "town [town_name] set religion [religion]", ""));
	}

	private void showReligionAdminTownHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ra", "town [town_name] set religion [religion]", ""));
	}

	private void showReligionAdminAllTownsHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ra", "alltowns set religion [religion]", ""));
	}

	private void parseReligionAdminReloadCommand(CommandSender sender){
		if (Settings.loadSettingsAndLang())
			Messaging.sendMsg(sender, Translation.of("config_and_lang_file_reloaded_successfully"));
		else {
			Messaging.sendErrorMsg(sender, Translation.of("config_and_lang_file_could_not_be_loaded"));
			TownyReligion.setSafeModeEnabled(true);
			return;
		}

		/*if (Database.loadAll())
			Messaging.sendMsg(sender, Translation.of("flatfile_database_reloaded_successfully"));
		else {
			Messaging.sendErrorMsg(sender, Translation.of("flatfile_database_could_not_be_loaded"));
			TownyReligion.setSafeModeEnabled(true);
			return;
		}

		if (Database.loadAll() && Settings.loadSettingsAndLang())
			TownyReligion.setSafeModeEnabled(false);*/
	}

	/*private void parseReligionAdminTownCommand(CommandSender sender, String[] args) {
		if (args.length > 3
			&& args[1].equalsIgnoreCase("set")
			&& args[2].equalsIgnoreCase("religion")) {

			Town town = TownyUniverse.getInstance().getTown(args[0]);
			if (town == null) {
				Messaging.sendErrorMsg(sender, Translation.of("msg_err_town_not_registered", args[0]));
				return;
			}

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[3]);
			for(int i = 4; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}

			String newReligion;
			try {
				newReligion = ReligionUtils.validateReligionName(stringBuilder.toString(), sender);
			} catch (Exception e) {
				if(sender instanceof Player)
					Messaging.sendErrorMsg(sender, e.getMessage());
				else
					TownyReligion.severe(e.getMessage());
				return;
			}

			//Set religion
			TownMetaDataController.setTownReligion(town, newReligion);
			
			//Prepare feedback message.
			String message= Translation.of("msg_religion_removed");
			if (!newReligion.isEmpty())
				message = Translation.of("msg_specific_town_religion_set", town.getName(), StringMgmt.capitalize(newReligion));
			
			TownyMessaging.sendPrefixedTownMessage(town, message);

			if(sender instanceof Player)
				Messaging.sendMsg(sender, message);
			else
				TownyReligion.severe(message); // Send message to console.

		} else {
			showReligionAdminTownHelp(sender);
		}
	}

	private void parseReligionAdminAllTownsCommand(CommandSender sender, String[] args) {
		if (args.length > 2
				&& args[0].equalsIgnoreCase("set")
				&& args[1].equalsIgnoreCase("religion")) {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[2]);
			for(int i = 3; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}

			String newReligion;
			try {
				newReligion = ReligionUtils.validateReligionName(stringBuilder.toString());
			} catch (Exception e) {
				if(sender instanceof Player)
					Messaging.sendErrorMsg(sender, e.getMessage());
				else
					TownyReligion.severe(e.getMessage());
				return;
			}

			//Set religion in all towns
			for(Town town: TownyUniverse.getInstance().getTowns()) {
				TownMetaDataController.setTownReligion(town, newReligion);
			}

			//Prepare feedback message.
			String message = Translation.of("msg_religion_removed_all_towns");
			if (!newReligion.isEmpty())
				message = Translation.of("msg_all_town_religion_set", StringMgmt.capitalize(newReligion));
			
			Messaging.sendGlobalMessage(message);
			
			if(!(sender instanceof Player)) // Send message to console.
				TownyReligion.info(message);

		} else {
			showReligionAdminAllTownsHelp(sender);
		}
	}*/

	/**
	 * Returns a List<String> containing strings of town names that match with arg.
	 *
	 * @return Matches
	 */
	static List<String> getTownsStartingWith(String arg) {
		List<String> matches = new ArrayList<>();
		TownyUniverse townyUniverse = TownyUniverse.getInstance();
		matches.addAll(townyUniverse.getTownsTrie().getStringsFromKey(arg));
		return matches;
	}

	/**
	 * Returns a List<String> containing strings of religion names that match with arg.
	 *
	 * @return Matches
	 */
	/*static List<String> getReligionsStartingWith(String arg) {
		return NameUtil.filterByStart(ReligionUtils.getReligions(), arg);
	}*/
}

