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
import com.palmergames.bukkit.util.Colors;
import com.palmergames.bukkit.util.NameValidation;
import org.bukkit.Bukkit;
import org.bukkit.Warning;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReligionUtils {

	/**
	 * Checks that a religion is named correctly.
	 * @param newReligion new religion name
	 * @return returns new name or null if name is invalid.
	 */
	public static String validateReligionName(String newReligion, Player religionOwner) throws Exception {
		if (newReligion.equalsIgnoreCase("new") ||
				newReligion.equalsIgnoreCase("leave") ||
				newReligion.equalsIgnoreCase("delete") ||
				newReligion.equalsIgnoreCase("join") ||
				newReligion.equalsIgnoreCase("invite") ||
				newReligion.equalsIgnoreCase("set") ||
				newReligion.equalsIgnoreCase("toggle") ||
				newReligion.equalsIgnoreCase("townlist")
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
	 * @param boardMsg the board message
	 * @return returns new name or null if name is invalid.
	 */
	public static String validateReligionBoard(String boardMsg, Player religionOwner) throws Exception {
		String board;

		if (
				boardMsg.equalsIgnoreCase("name=") ||
						boardMsg.equalsIgnoreCase("timeEstablished=") ||
						boardMsg.equalsIgnoreCase("towns=") ||
						boardMsg.equalsIgnoreCase("overseeingTown=") ||
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
	 * Given a resident, does this resident have the given religion?
	 * @param res Resident to check.
	 * @param religion Religion to check for.
	 * @return True if the resident has a town with the given culture.
	 */
	public static boolean isSameReligion(Resident res, String religion) {
		if (res == null)
			return false;

		if (TownyAPI.getInstance().getResidentTownOrNull(res) == null)
			return false;

		return res.hasTown()
				&& TownMetaDataController.hasTownReligion(Objects.requireNonNull(TownyAPI.getInstance().getResidentTownOrNull(res)))
				&& Objects.requireNonNull(TownMetaDataController.getTownReligion(Objects.requireNonNull(TownyAPI.getInstance().getResidentTownOrNull(res)))).equalsIgnoreCase(religion);
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
			new Religion().getReligions().forEach(religion -> names.add(religion.getName()));
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

		// ___[ Allahism ]___
		screen.addComponentOf("title", ChatTools.formatTitle(religion.getFormattedName()));

		// (Open)
		if (religion.isPublic())
			screen.addComponentOf("subtitle", ChatTools.formatSubTitle(com.palmergames.bukkit.towny.object.Translation.of("status_title_open")));

		// Board: Don't kill the chickens, and please restock the furnaces
		if (religion.getBoardMsg() != null)
			screen.addComponentOf("board", colourKeyValue(com.palmergames.bukkit.towny.object.Translation.of("status_town_board"), religion.getBoardMsg()));

		// Overseer: Amphire | Cork (City)
		screen.addComponentOf("overseer", colourKeyValue(Translation.of("status_overseer"), religion.getOverseeingTown().getMayor().getName() + Colors.translateColorCodes(Translation.of("status_mayor_town_separator", com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value"))) + religion.getOverseeingTown().getFormattedName()));

		// Created Date
		screen.addComponentOf("registered", colourKeyValue(com.palmergames.bukkit.towny.object.Translation.of("status_founded"), new SimpleDateFormat("MMM d yyyy").format(religion.getTimeEstablished())));

		int limit = 10;

		// Towns [5]: Abraham, Texas, Tokyo, and 2 more...
		StringBuilder religionNames = new StringBuilder();
		if (religion.getTowns().size() < limit)
			for (Town town : religion.getTowns()) {
				if (religion.getTowns().indexOf(town) == 0)
					religionNames.append(town.getFormattedName());
				else
					religionNames.append(", ").append(town.getFormattedName());
			}
		else {
			int ext = religion.getTowns().size() - limit;
			for (Town town : religion.getTowns()) {
				if (religion.getTowns().indexOf(town) < limit) {
					if (religion.getTowns().indexOf(town) == 0) {
						religionNames.append(town.getFormattedName());
					} else {
						religionNames.append(", ").append(town.getFormattedName());
					}
				} else {
					religionNames.append(", ").append(Translation.of("status_town_ext", ext));
					break;
				}
			}
		}
		screen.addComponentOf("towns", colourKeyValue(Translation.of("status_towns") + " " + com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") + "[" + religion.getTowns().size() + "]" + com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") + ":", Colors.White + religionNames));

		// Allies [2]: Pepeism, Allahism
		religionNames = new StringBuilder();
		StringBuilder allyNames = new StringBuilder();
		if (religion.getAllyList().size() < limit)
			for (Religion ally : religion.getAllyList()) {
				if (religion.getAllyList().indexOf(ally) == 0)
					religionNames.append(ally.getFormattedName());
				else
					religionNames.append(", ").append(ally.getFormattedName());
			}
		else {
			int ext = religion.getAllyList().size() - limit;
			for (Religion ally : religion.getAllyList()) {
				if (religion.getAllyList().indexOf(ally) < limit) {
					if (religion.getAllyList().indexOf(ally) == 0) {
						religionNames.append(ally.getFormattedName());
					} else {
						religionNames.append(", ").append(ally.getFormattedName());
					}
				} else {
					religionNames.append(", ").append(Translation.of("status_town_ext", ext));
					break;
				}
			}
		}
		screen.addComponentOf("allies", colourKeyValue(Translation.of("status_allies") + " " + com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") + "[" + religion.getAllyList().size() + "]" + com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") + ":", Colors.White + religionNames));

		// Enemies [1]: Budahism
		religionNames = new StringBuilder();
		if (religion.getEnemyList().size() < limit)
			for (Religion enemy : religion.getEnemyList()) {
				if (religion.getEnemyList().indexOf(enemy) == 0)
					religionNames.append(enemy.getFormattedName());
				else
					religionNames.append(", ").append(enemy.getFormattedName());
			}
		else {
			int ext = religion.getEnemyList().size() - limit;
			for (Religion enemy : religion.getEnemyList()) {
				if (religion.getEnemyList().indexOf(enemy) < limit) {
					if (religion.getEnemyList().indexOf(enemy) == 0) {
						religionNames.append(enemy.getFormattedName());
					} else {
						religionNames.append(", ").append(enemy.getFormattedName());
					}
				} else {
					religionNames.append(", ").append(Translation.of("status_town_ext", ext));
					break;
				}
			}
		}
		screen.addComponentOf("enemies", colourKeyValue(Translation.of("status_enemies") + " " + com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") + "[" + religion.getEnemyList().size() + "]" + com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") + ":", Colors.White + religionNames));

		return screen;
	}

	private static String colourKeyValue(String key, String value) {
		return String.format("%s%s %s%s", com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key"), key, com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value"), value);
	}

	public static List<Player> getOnlinePlayers(Religion religion) {
		List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
		List<Player> finalList = new ArrayList<>();

		list.forEach(player -> {
			if (TownyAPI.getInstance().getResident(player.getName()) != null) {
				if (Objects.requireNonNull(TownyAPI.getInstance().getResident(player.getName())).hasTown()) {
					religion.getTownNames().forEach(townName -> {
						if (TownyAPI.getInstance().getTown(townName) != null)
							if (Objects.requireNonNull(TownyAPI.getInstance().getTown(townName)).hasResident(player.getName()))
								finalList.add(player);
					});
				}
			}
		});

		return finalList;
	}

	@Warning(true)
	public static Set<Religion> getReligionsSorted(String sortingMethod) {
		Set<Religion> set = new HashSet<>();
		HashMap<Religion, Object> map = new HashMap<>();
		HashMap<Religion, Object> sortedMap = new HashMap<>();
		ArrayList<Religion> religionList = new Religion().getReligions();

		if (sortingMethod.equalsIgnoreCase("residents")) {
			if (!religionList.isEmpty()) {
				for (Religion religion : religionList) {
					long residentNum = 0;
					for (Town town : religion.getTowns()) {
						residentNum += town.getNumResidents();
					}

					map.put(religion, residentNum);
				}

				
			}
		} else if (sortingMethod.equalsIgnoreCase("towns")) {

		}  else {

		}

		return set;
	}
}
