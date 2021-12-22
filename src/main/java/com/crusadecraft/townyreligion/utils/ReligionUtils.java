package com.crusadecraft.townyreligion.utils;

import com.crusadecraft.townyreligion.FlatFile;
import com.crusadecraft.townyreligion.enums.TownyReligionPermissionNodes;
import com.crusadecraft.townyreligion.metadata.TownMetaDataController;
import com.crusadecraft.townyreligion.objects.Religion;
import com.crusadecraft.townyreligion.settings.TownyReligionSettings;
import com.crusadecraft.townyreligion.settings.Translation;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.statusscreens.StatusScreen;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.bukkit.util.NameValidation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
// import java.util.Collections;

public class ReligionUtils {

	/**
	 * Checks that a religion is named correctly.
	 * @param newReligion
	 * @return returns new name or null if name is invalid.
	 */
	public static String validateReligionName(String newReligion, Player religionOwner) throws Exception {
		if (newReligion.equalsIgnoreCase("new") ||
				newReligion.equalsIgnoreCase("delete") ||
				newReligion.equalsIgnoreCase("set") ||
				newReligion.equalsIgnoreCase("none")
		)
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		if (newReligion.startsWith("_"))
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		if (newReligion.endsWith("_"))
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		if (newReligion.contains("!") ||
				newReligion.contains("@") ||
				newReligion.contains("#") ||
				newReligion.contains("$") ||
				newReligion.contains("%") ||
				newReligion.contains("^") ||
				newReligion.contains("&") ||
				newReligion.contains("*") ||
				newReligion.contains("(") ||
				newReligion.contains(")") ||
				newReligion.contains("=") ||
				newReligion.contains("+") ||
				newReligion.contains("[") ||
				newReligion.contains("]") ||
				newReligion.contains("{") ||
				newReligion.contains("}") ||
				newReligion.contains("\\") ||
				newReligion.contains("|") ||
				newReligion.contains(";") ||
				newReligion.contains(":") ||
				newReligion.contains("'") ||
				newReligion.contains("\"") ||
				newReligion.contains(",") ||
				newReligion.contains(".") ||
				newReligion.contains("/") ||
				newReligion.contains("<") ||
				newReligion.contains(">") ||
				newReligion.contains("?") ||
				newReligion.contains("~") ||
				newReligion.contains("`"))
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		newReligion = newReligion.replace(" ", "_");

		newReligion = new String(newReligion.getBytes(StandardCharsets.UTF_8));


		if (!NameValidation.isValidString(newReligion))
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		if (!religionOwner.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_MIN_LENGTH.getNode()))
			if (TownyReligionSettings.getReligionMaxNameLength() != -1)
				if (newReligion.length() > TownyReligionSettings.getReligionMaxNameLength())
					throw new Exception(Translation.of("msg_err_religion_name_too_long", TownyReligionSettings.getReligionMaxNameLength()));

		if (!religionOwner.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_MAX_LENGTH.getNode()))
			if (TownyReligionSettings.getReligionMinNameLength() != -1)
				if (newReligion.length() < TownyReligionSettings.getReligionMinNameLength())
					throw new Exception(Translation.of("msg_err_religion_name_too_short", TownyReligionSettings.getReligionMinNameLength()));


		return newReligion;
	}

	/**
	 * Checks that a board is named correctly.
	 * @param boardMsg
	 * @return returns new name or null if name is invalid.
	 */
	public static String validateReligionBoard(String boardMsg, Player religionOwner) throws Exception {
		String board;

		if (
				boardMsg.equalsIgnoreCase("name=") ||
						boardMsg.equalsIgnoreCase("timeEstablished=") ||
						boardMsg.equalsIgnoreCase("towns=") ||
						//TODO Change this to 'overseeingTown'
						boardMsg.equalsIgnoreCase("ownerTown=") ||
						boardMsg.equalsIgnoreCase("inviteOnly=") ||
						boardMsg.equalsIgnoreCase("boardMsg=") ||
						boardMsg.equalsIgnoreCase("allyList=") ||
						boardMsg.equalsIgnoreCase("enemyList=")
		)
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		boardMsg = new String(boardMsg.getBytes(StandardCharsets.UTF_8));

		if (!NameValidation.isValidString(boardMsg))
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		if (!religionOwner.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_BOARD_BYPASS_MAX_LENGTH.getNode()))
			if (TownyReligionSettings.getReligionMaxBoardLength() != -1)
				if (boardMsg.length() > TownyReligionSettings.getReligionMaxNameLength())
					throw new Exception(Translation.of("msg_err_board_too_long"));

		if (!religionOwner.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_BOARD_BYPASS_MIN_LENGTH.getNode()))
			if (TownyReligionSettings.getReligionMinBoardLength() != -1)
				if (boardMsg.length() < TownyReligionSettings.getReligionMinBoardLength())
					throw new Exception(Translation.of("msg_err_board_too_short"));

		board = boardMsg;

		return board;
	}

	/**
	 * Checks that a religion is named correctly.
	 * @param newReligion
	 * @return returns new name or null if name is invalid.
	 *//*
	public static String validateReligionName(String newReligion) throws Exception {
		String religion = "";

		if (
				newReligion.equalsIgnoreCase("new") ||
						newReligion.equalsIgnoreCase("delete") ||
						newReligion.equalsIgnoreCase("set")
		)
			throw new Exception(Translation.of("msg_err_invalid_characters"));

		if (!newReligion.equalsIgnoreCase("none")) {
			if (!NameValidation.isValidString(newReligion))
				throw new Exception(Translation.of("msg_err_invalid_characters"));

			if (newReligion.length() > TownyReligionSettings.getReligionMaxNameLength())
				throw new Exception(Translation.of("msg_err_religion_name_too_long", TownyReligionSettings.getReligionMaxNameLength()));

			religion = newReligion;
		}
		return religion;
	}*/

	/**
	 * Given a resident, does this resident have the given religion?
	 * @param res Resident to check.
	 * @param religion Religion to check for.
	 * @return True if the resident has a town with the given culture.
	 */
	public static boolean isSameReligion(Resident res, String religion) {
		return res.hasTown() 
				&& TownMetaDataController.hasTownReligion(TownyAPI.getInstance().getResidentTownOrNull(res))
				&& TownMetaDataController.getTownReligion(TownyAPI.getInstance().getResidentTownOrNull(res)).equalsIgnoreCase(religion);
	}

	public static boolean townHasReligion(Town town) {
		boolean[] hasReligion = {false};

		if (town == null)
			return false;

		new Religion().getReligions().forEach(religion -> {
			if (religion.containsTown(town))
				hasReligion[0] = true;
		});

		return hasReligion[0];
	}

	public static void removeTownFromReligion(Town town) {
		if (!townHasReligion(town)) {
			return;
		}

		Religion townReligion = getTownReligion(town);

		if (townReligion != null)
			FlatFile.removeTownFromReligion(town, townReligion);
	}

	public static Religion getTownReligion(Town town) {
		if (town == null)
			return null;

		for (Religion religion : new Religion().getReligions()) {
			if (religion.containsTown(town))
				return religion;
		}

		return null;
	}

	public static ArrayList<String> getReligionNames() {
		ArrayList<String> names = new ArrayList<>();

		if (new Religion().getReligions() != null) {
			new Religion().getReligions().forEach(religion -> {
				names.add(religion.getName());
			});
		}

		return names;
	}

	public static Religion getReligionByName(String religion) {
		return FlatFile.getReligionByName(religion);
	}

	public static void addTownToReligion(Religion religion, Town town) {
		FlatFile.addTownToReligion(religion, town);
	}

	public static StatusScreen getStatusScreen(Religion religion, CommandSender sender) {
		if (religion == null)
			return null;

		StatusScreen screen = new StatusScreen(sender);

		// ___[ Atheism ]___
		screen.addComponentOf("title", ChatTools.formatTitle(religion.getFormattedName()));

		// (Open)
		if (religion.isPublic())
			screen.addComponentOf("subtitle", ChatTools.formatSubTitle(com.palmergames.bukkit.towny.object.Translation.of("status_title_open")));

		// Board: Don't kill the chickens
		if (religion.getBoardMsg() != null)
			screen.addComponentOf("board", colourKeyValue(com.palmergames.bukkit.towny.object.Translation.of("status_town_board"), religion.getBoardMsg()));

		// Created Date
		screen.addComponentOf("registered", colourKeyValue(com.palmergames.bukkit.towny.object.Translation.of("status_founded"), new SimpleDateFormat("MMM d yyyy").format(religion.getTimeEstablished())));

		// Head Town: Big City
		/*String townLine = colourKeyValue(Translation.of("status_head"), religion.getOwnerTown().getFormattedName() + String.format(" %s[%s]", com.palmergames.bukkit.towny.object.Translation.of("status_format_list_2"), religion.getOwnerTown().getResidents().size()));
		String[] residents = TownyFormatter.getFormattedNames(religion.getOwnerTown().getResidents().toArray(new Resident[0]));
		if (residents.length > 34)
			residents = shortenOverlengthArray(residents, 35);
		screen.addComponentOf("head", townLine,
				HoverEvent.showText(Component.text(Colors.translateColorCodes(TownySettings.getPAPIFormattingTown(), religion.getFormattedName()))
						.append(Component.newline())
						.append(Component.text(TownyFormatter.getResi)))

		);*/
		screen.addComponentOf("overseer", colourKeyValue(Translation.of("status_overseer"), religion.getOverseeingTown().getFormattedName()));

		return screen;
	}

	private static String colourKeyValue(String key, String value) {
		return String.format("%s%s %s%s", com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key"), key, com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value"), value);
	}

	/*private static String[] shortenOverlengthArray(String[] array, int i) {
		String[] entire = array;
		array = new String[i + 1];
		System.arraycopy(entire, 0, array, 0, i);
		array[i] = com.palmergames.bukkit.towny.object.Translation.of("status_town_reslist_overlength");
		return array;
	}*/

	public static List<Player> getOnlinePlayers(Religion religion) {
		List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
		List<Player> finalList = new ArrayList<>();

		list.forEach(player -> {
			if (TownyAPI.getInstance().getResident(player.getName()) != null &&
					TownyAPI.getInstance().getResident(player.getName()).hasTown()) {
				religion.getTownNames().forEach(townName -> {
					if (TownyAPI.getInstance().getTown(townName).hasResident(player.getName()))
						finalList.add(player);
				});
			}
		});

		return finalList;
	}
}
