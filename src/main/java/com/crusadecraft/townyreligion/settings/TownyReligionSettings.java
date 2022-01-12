package com.crusadecraft.townyreligion.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TownyReligionSettings {

	public static String getVersion() {
		return Settings.getString(ConfigNodes.VERSION);
	}

	public static String getLastRunVersion() {
		return Settings.getString(ConfigNodes.LAST_RUN_VERSION);
	}

	public static boolean getTownyReligionEnabled() {
		return Settings.getBoolean(ConfigNodes.TOWNY_RELIGION_ENABLED);
	}

	public static List<String> getReligionBlacklistedNames() {
		List<String> temp = new ArrayList<>();

		if (Settings.getString(ConfigNodes.TOWNY_RELIGION_RELIGION_NAME_BLACKLISTED).contains(":"))
			Collections.addAll(temp, Settings.getString(ConfigNodes.TOWNY_RELIGION_RELIGION_NAME_BLACKLISTED).toLowerCase(Locale.ROOT).split(":"));
		else
			temp.add(Settings.getString(ConfigNodes.TOWNY_RELIGION_RELIGION_NAME_BLACKLISTED));

		return temp;
	}

	public static int getReligionMaxNameLength() {
		return Settings.getInt(ConfigNodes.TOWNY_RELIGION_RELIGION_NAME_MAX_LENGTH);
	}

	public static int getReligionMinNameLength() {
		return Settings.getInt(ConfigNodes.TOWNY_RELIGION_RELIGION_NAME_MIN_LENGTH);
	}

	public static int getReligionMaxBoardLength() {
		return Settings.getInt(ConfigNodes.TOWNY_RELIGION_RELIGION_BOARD_MAX_LENGTH);
	}

	public static int getReligionMinBoardLength() {
		return Settings.getInt(ConfigNodes.TOWNY_RELIGION_RELIGION_BOARD_MIN_LENGTH);
	}

	public static String getReligionDefaultBoard() {
		return Settings.getString(ConfigNodes.TOWNY_RELIGION_RELIGION_BOARD_DEFAULT_MSG);
	}

	public static int getReligionCreationPrice() {
		return Settings.getInt(ConfigNodes.TOWNY_RELIGION_RELIGION_PRICES_CREATION_PRICE);
	}

	public static int getReligionRenamePrice() {
		return Settings.getInt(ConfigNodes.TOWNY_RELIGION_RELIGION_PRICES_RENAME_PRICE);
	}

}
