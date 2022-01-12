package com.crusadecraft.townyreligion.enums;

public enum TownyReligionPermissionNodes {

	// Command Nodes:
	TOWNYRELIGION_RELIGION("townyreligion.religion"),
	TOWNYRELIGION_RELIGION_NEW("townyreligion.religion.new"),
	TOWNYRELIGION_RELIGION_NEW_BYPASS_NO_COST("townyreligion.religion.new.bypass.no_cost"),
	TOWNYRELIGION_RELIGION_DELETE("townyreligion.religion.delete"),
	TOWNYRELIGION_RELIGION_LEAVE("townyreligion.religion.leave"),
	TOWNYRELIGION_RELIGION_INVITE("townyreligion.religion.invite"),
	TOWNYRELIGION_RELIGION_SET_NAME("townyreligion.religion.set.name"),
	TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_MAX_LENGTH("townyreligion.religion.set.name.bypass.max_length"),
	TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_MIN_LENGTH("townyreligion.religion.set.name.bypass.min_length"),
	TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_COST("townyreligion.religion.set.name.bypass.cost"),
	TOWNYRELIGION_RELIGION_SET_BOARD("townyreligion.religion.set.board"),
	TOWNYRELIGION_RELIGION_SET_BOARD_BYPASS_MAX_LENGTH("townyreligion.religion.set.board.bypass.max_length"),
	TOWNYRELIGION_RELIGION_SET_BOARD_BYPASS_MIN_LENGTH("townyreligion.religion.set.board.bypass.min_length"),
	TOWNYRELIGION_RELIGION_SET_OVERSEER("townyreligion.religion.set.overseer"),
	TOWNYRELIGION_RELIGION_TOGGLE_PUBLIC("townyreligion.religion.toggle.public"),
	TOWNYRELIGION_RELIGION_JOIN("townyreligion.religion.join"),
	TOWNYRELIGION_RELIGION_JOIN_BYPASS_PUBLIC_CHECK("townyreligion.religion.join.bypass.public_check"),
	TOWNYRELIGION_RELIGION_TOWNLIST("townyreligion.religion.townlist"),
	TOWNYRELIGION_RELIGION_KICK("townyreligion.religion.kick"),
	TOWNYRELIGION_RELIGION_SAY("townyreligion.religion.say"),
	TOWNYRELIGION_RELIGION_LIST("townyreligion.religion.list"),
	TOWNYRELIGION_RELIGION_HERE("townyreligion.religion.here"),

	TOWNYRELIGION_RELIGION_ENEMY_ADD("townyreligion.religion.enemy.add"),
	TOWNYRELIGION_RELIGION_ENEMY_REMOVE("townyreligion.religion.enemy.remove"),
	TOWNYRELIGION_RELIGION_ENEMY_LIST("townyreligion.religion.enemy.list"),
	TOWNYRELIGION_RELIGION_ALLY_ADD("townyreligion.religion.ally.add"),
	TOWNYRELIGION_RELIGION_ALLY_REMOVE("townyreligion.religion.ally.remove"),
	TOWNYRELIGION_RELIGION_ALLY_LIST("townyreligion.religion.ally.list"),














	TOWNYRELIGION_COMMAND_ADMIN("townyreligion.admin.*");

	/* Mayor Default:
	 *   TOWNYRELIGION_RELIGION_NEW
	 *   TOWNYRELIGION_RELIGION_DELETE
	 *   TOWNYRELIGION_RELIGION_LEAVE
	 *
	 * Admin Default:
	 *   TOWNYRELIGION_RELIGION_NEW_NO_COST
	 *
	 */



	private String value;

	/**
	 * Constructor
	 * 
	 * @param permission - Permission.
	 */
	TownyReligionPermissionNodes(String permission) {

		this.value = permission;
	}

	/**
	 * Retrieves the permission node
	 * 
	 * @return The permission node
	 */
	public String getNode() {

		return value;
	}

	/**
	 * Retrieves the permission node
	 * replacing the character *
	 * 
	 * @param replace - String
	 * @return The permission node
	 */
	public String getNode(String replace) {

		return value.replace("*", replace);
	}
}
