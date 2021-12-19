package com.crusadecraft.townyreligion.settings;

public enum ConfigNodes {

	VERSION_HEADER("version", "", ""),
	VERSION(
			"version.version",
			"",
			"# This is the current version.  Please do not edit."),
	LAST_RUN_VERSION(
			"version.last_run_version",
			"",
			"# This is for showing the changelog on updates.  Please do not edit."),
	LANGUAGE(
			"language",
			"english.yml",
			"# The language file you wish to use"),
	TOWNY_RELIGION_HEADER("townyreligion",
			"",
			"",
   			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                Towny Religion Settings               | #",
			"# |                                                      | #",
			"# +------------------------------------------------------+ #",
			"############################################################"),
	TOWNY_RELIGION_ENABLED(
			"townyreligion.enabled",
			"true",
			"",
			"# If this value is true, then TownyReligion is enabled."),
	TOWNY_RELIGION_RELIGION_HEADER("townyreligion.religion", "", ""),
	TOWNY_RELIGION_RELIGION_NAME_HEADER("townyreligion.religion.name", "", ""),
	TOWNY_RELIGION_RELIGION_NAME_MAX_LENGTH(
			"townyreligion.religion.name.max_length",
			"20",
			"",
			"# The maximum length a religion name can be.",
			"# Set to -1 to disable."),
	TOWNY_RELIGION_RELIGION_NAME_MIN_LENGTH(
			"townyreligion.religion.name.min_length",
			"3",
			"",
			"# The minimum length a religion name can be.",
			"# Set to -1 to disable."),
	TOWNY_RELIGION_RELIGION_BOARD_HEADER("townyreligion.religion.board", "", ""),
	TOWNY_RELIGION_RELIGION_BOARD_MAX_LENGTH(
			"townyreligion.religion.board.max_length",
			"200",
			"",
			"# The maximum length a religion board message can be.",
			"# Set to -1 to disable."),
	TOWNY_RELIGION_RELIGION_BOARD_MIN_LENGTH(
			"townyreligion.religion.board.min_length",
			"0",
			"",
			"# The minimum length a religion board message can be.",
			"# Set to -1 to disable."),
	TOWNY_RELIGION_RELIGION_BOARD_DEFAULT_MSG(
			"townyreligion.religion.board.default_msg",
			"",
			"",
			"# The board message a religion is set to upon creation."),
	TOWNY_RELIGION_RELIGION_PRICES_HEADER("townyreligion.religion.prices", "", ""),
	TOWNY_RELIGION_RELIGION_PRICES_CREATION_PRICE(
			"townyreligion.religion.prices.creation_price",
			"20",
			"",
			"# How much it should cost to create a religion.",
			"# Set to 0 to disable."),
	TOWNY_RELIGION_RELIGION_PRICES_RENAME_PRICE(
			"townyreligion.religion.prices.rename_price",
			"10",
			"",
			"# How much it should cost to rename a religion.",
			"# Set to 0 to disable.")










	;

	private final String Root;
	private final String Default;
	private String[] comments;

	ConfigNodes(String root, String def, String... comments) {

		this.Root = root;
		this.Default = def;
		this.comments = comments;
	}

	/**
	 * Retrieves the root for a config option
	 *
	 * @return The root for a config option
	 */
	public String getRoot() {

		return Root;
	}

	/**
	 * Retrieves the default value for a config path
	 *
	 * @return The default value for a config path
	 */
	public String getDefault() {

		return Default;
	}

	/**
	 * Retrieves the comment for a config path
	 *
	 * @return The comments for a config path
	 */
	public String[] getComments() {

		if (comments != null) {
			return comments;
		}

		String[] comments = new String[1];
		comments[0] = "";
		return comments;
	}

}
