package com.crusadecraft.townyreligion.command;

import com.crusadecraft.townyreligion.Messaging;
import com.crusadecraft.townyreligion.TownyReligion;
import com.crusadecraft.townyreligion.enums.TownyReligionPermissionNodes;
import com.crusadecraft.townyreligion.events.*;
import com.crusadecraft.townyreligion.objects.Religion;
import com.crusadecraft.townyreligion.settings.TownyReligionSettings;
import com.crusadecraft.townyreligion.settings.Translation;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.*;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.economy.Account;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.util.StringMgmt;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.security.MessageDigest;
import java.util.*;

public class ReligionCommand implements CommandExecutor, TabCompleter {

	private static final List<String> religionTabCompletes = Arrays.asList(
			"new",
			"leave",
			"delete",
			"join",
            "invite",
            "set",
            "toggle",
			"townlist",
			"kick",
            "say",
			"here",
			"ally",
            "enemy"/*,
			"list",
            "ranklist",
            "rank"*/
	);

	private static final List<String> religionSideTabCompletes = Arrays.asList(
			"add",
			"remove",
			"list"
	);

	private static final List<String> religionListTabCompletes = Arrays.asList(
			"by"
	);

	private static final List<String> religionListByTabCompletes = Arrays.asList(
			"towns"
	);

	private static final List<String> religionReligionTabCompletes = Arrays.asList(
			"townlist",
			"enemy",
			"ally"
	);

	private static final List<String> religionReligionEnemyAllyTabCompletes = Arrays.asList(
			"list"
	);

	private static final List<String> religionSetTabCompletes = Arrays.asList(
			"overseer",
			"board",
			"name"
	);

	private static final List<String> religionToggleTabCompletes = Arrays.asList(
			"public"
	);

	// Ignored warnings to make things easier.
	@SuppressWarnings({"ConstantConditions", "SwitchStatementWithTooFewBranches"})
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (TownyReligion.getSafeModeEnabled()) {
			return Collections.emptyList();
		}

		// Make sure the plugin is enabled in the config.
		if (!TownyReligionSettings.getTownyReligionEnabled())
			return Collections.emptyList();

		// Make sure the player has the required permission.
		if (!sender.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION.getNode()))
			return Collections.emptyList();

		try {
			switch (args[0].toLowerCase(Locale.ROOT)) {
				case "new":
				case "leave":
				case "delete":
				case "townlist":
				case "say":
				case "here":
					return Collections.emptyList();
				case "ally":
					if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null) {
						switch (args[1].toLowerCase(Locale.ROOT)) {
							case "add":
								ArrayList<String> religionList = ReligionUtils.getReligionNames();
								religionList.remove(ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getName());
								ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getEnemyList().forEach(religion -> religionList.remove(religion.getName()));
								ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getAllyList().forEach(religion -> religionList.remove(religion.getName()));
								return NameUtil.filterByStart(religionList, args[2]);
							case "remove":
								ArrayList<String> religionList1 = ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getAllyNames();
								return NameUtil.filterByStart(religionList1, args[2]);
							case "list":
								return Collections.emptyList();
							default:
								return NameUtil.filterByStart(religionSideTabCompletes, args[1]);
						}
					} else {
						return Collections.emptyList();
					}
				case "enemy":
					if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null) {
						switch (args[1].toLowerCase(Locale.ROOT)) {
							case "add":
								ArrayList<String> religionList = ReligionUtils.getReligionNames();
								religionList.remove(ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getName());
								ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getEnemyList().forEach(religion -> religionList.remove(religion.getName()));
								ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getAllyList().forEach(religion -> religionList.remove(religion.getName()));;
								return NameUtil.filterByStart(religionList, args[2]);
							case "remove":
								ArrayList<String> religionList1 = ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getEnemyNames();
								return NameUtil.filterByStart(religionList1, args[2]);
							case "list":
								return Collections.emptyList();
							default:
								return NameUtil.filterByStart(religionSideTabCompletes, args[1]);
						}
					} else {
						return Collections.emptyList();
					}
				case "join":
					// Make sure the player has the required permission, and that the player has a valid town.
					if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null) {
						if (args.length == 2) {
							return NameUtil.filterByStart(ReligionUtils.getReligionNames(), args[1]);
						} else {
							return Collections.emptyList();
						}
					} else {
						return Collections.emptyList();
					}
                case "invite":
                    // Make sure the player has the required permission, and that the player has a valid town.
                    if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null) {
                        if (args.length == 2) {
                            ArrayList<String> tp = new ArrayList<>();

                            Bukkit.getOnlinePlayers().forEach(pl -> tp.add(pl.getName()));
                            return tp;
                        } else {
                            return Collections.emptyList();
                        }
                    } else {
                        return Collections.emptyList();
                    }
				case "set":
					// Make sure the player has the required permission, and that the player has a valid town.
					if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null)
						switch (args[1].toLowerCase(Locale.ROOT)) {
							case "overseer":
								if (args.length == 3)
									// Return all the town names in the player's town's religion.
									return ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getTownNames();
								else
									return Collections.emptyList();
							case "board":
							case "name":
								// Return an empty list since the cases don't require any more arguments.
								return Collections.emptyList();
							default:
								if (args.length == 2)
									// Return the default tab complete list if /religion set ARGUMENT is null.
									return NameUtil.filterByStart(religionSetTabCompletes, args[1]);
								else
									return Collections.emptyList();
						}
					else
						return Collections.emptyList();
				case "toggle":
					if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null)
						switch (args[1].toLowerCase(Locale.ROOT)) {
							case "public":
								// Return an empty list since the cases don't require any more arguments.
								return Collections.emptyList();
							default:
								if (args.length == 2)
									// Return the default tab complete list if /religion toggle ARGUMENT is null.
									return NameUtil.filterByStart(religionToggleTabCompletes, args[1]);
								else
									return Collections.emptyList();
						}
					else
						return Collections.emptyList();
				case "kick":
					if ((sender instanceof Player) && ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()) != null)
						if (args.length == 2)
							// Return all the town names in the player's town's religion.
							return NameUtil.filterByStart(ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(sender.getName()).getTownOrNull()).getTownNames(), args[1]);
						else
							return Collections.emptyList();
				case "list":
					switch (args[1].toLowerCase(Locale.ROOT)) {
						case "by":
							return NameUtil.filterByStart(religionListByTabCompletes, args[2]);
						default:
							if (args.length == 2)
								return NameUtil.filterByStart(religionListTabCompletes, args[1]);
							else
								return Collections.emptyList();
					}
				default:
					// If the /religion ARGUMENT is null
					if (NameUtil.filterByStart(religionTabCompletes, args[0]).isEmpty())
						if (args.length == 1)
							// Return a list of all the religion names for use with statusscreen.
							return NameUtil.filterByStart(ReligionUtils.getReligionNames(), args[0]);
						else
							// Used with /rel ARGUMENT townlist
							if (ReligionUtils.getReligionByName(args[0]) != null)
								if (args.length == 2) {
									return NameUtil.filterByStart(religionReligionTabCompletes, args[1]);
								} else {
									if (args[1].equalsIgnoreCase("ally") || args[1].equalsIgnoreCase("enemy")) {
										if (args.length == 3) {
											return religionReligionEnemyAllyTabCompletes;
										} else {
											return Collections.emptyList();
										}
									} else {
										return Collections.emptyList();
									}
								}
							else
								return Collections.emptyList();
					else
						// Return the default tab complete list for /religion.
						return NameUtil.filterByStart(religionTabCompletes, args[0]);
			}
		} catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
			// To make things easier, ignore the thrown exceptions, and just return an empty tab complete list.
			return Collections.emptyList();
		}
	}

	private void showReligionHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/religion"));
        sender.sendMessage(ChatTools.formatCommand("/rel", "", "Your religion's status"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "[religion]", "Selected religion's status"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "new [religion]", "Create new religion"));
		// sender.sendMessage(ChatTools.formatCommand("/rel", "list", "List all religions"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "here", "Religion at your position"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "leave", "Leave your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "delete", "Delete your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "join", "Join a religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "kick", "Kick a town from your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "say", "Say something to your religion"));
        // sender.sendMessage(ChatTools.formatCommand("/rel", "invite [town]", "Invite a town to join your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "townlist", "Show all towns in your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "toggle", "Toggle options for your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "set", "Change settings for your religion"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "ally", "Control religion allies"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "enemy", "Control religion enemies"));
		sender.sendMessage(ChatTools.formatCommand("/rel", "?", "View this page"));
	}

	private void showReligionSetHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/religion set"));
		sender.sendMessage(ChatTools.formatCommand("/rel set", "overseer [town]", "Change the overseeing town"));
		sender.sendMessage(ChatTools.formatCommand("/rel set", "board [message]", "Change or remove the town board"));
		sender.sendMessage(ChatTools.formatCommand("/rel set", "name [name]", "Change the religion name"));
	}

	private void showReligionToggleHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/religion toggle"));
		sender.sendMessage(ChatTools.formatCommand("/rel toggle", "public", "Toggle public status of religion"));
	}

	private void showReligionAllyHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/religion ally"));
		sender.sendMessage(ChatTools.formatCommand("/rel ally", "add [Religion]", "Add religion as ally"));
		sender.sendMessage(ChatTools.formatCommand("/rel ally", "remove [Religion]", "Remove religion as ally"));
		sender.sendMessage(ChatTools.formatCommand("/rel ally", "list", "List religion allies"));
	}

	private void showReligionEnemyHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/religion enemy"));
		sender.sendMessage(ChatTools.formatCommand("/rel enemy", "add [Religion]", "Add religion as enemy"));
		sender.sendMessage(ChatTools.formatCommand("/rel enemy", "remove [Religion]", "Remove religion as enemy"));
		sender.sendMessage(ChatTools.formatCommand("/rel enemy", "list", "List religion enemies"));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (TownyReligion.getSafeModeEnabled()) {
			Messaging.sendErrorMsg(sender, "TownyReligion is in safe mode. Check the console for details, and try executing /ra reload");
			return false;
		}

		// Make sure the plugin is enabled in the config.
		if (!TownyReligionSettings.getTownyReligionEnabled()) {
			Messaging.sendErrorMsg(sender, Translation.of("msg_err_disabled"));
			return false;
		}

		// Filter out non player objects.
		// This will be changed later on.
		if (sender instanceof Player)
			parseReligionCommand((Player) sender, args);
		else
			showReligionHelp(sender);

		return true;
	}

	/* ---------------------------
	 * |                         |
	 * |        Command:         |
	 * |                         |
	 * |        /rel             |
	 * |             new         |
	 * |             delete      |
	 * |             list*       |
	 * |             here        |
	 * |             leave       |
	 * |             join        |
	 * |             kick        |
	 * |             say         |
	 * |             townlist    |
	 * |             invite      |
	 * |             set         |
	 * |             toggle      |
	 * |             ally        |
	 * |             enemy       |
	 * |             ?           |
	 * |                         |
	 * ---------------------------
	 */

	private void parseReligionCommand(Player player, String[] args) {

		// Handle command.
		if (args.length > 0) {
			switch (args[0]) {
				case "new":
					parseReligionNewCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "delete":
					parseReligionDeleteCommand(player);
					break;
				case "list":
					// parseReligionListCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "enemy":
					parseReligionEnemyCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "ally":
					parseReligionAllyCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "here":
					parseReligionHereCommand(player);
					break;
				case "leave":
					parseReligionLeaveCommand(player);
					break;
				case "join":
					parseReligionJoinCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "kick":
					parseReligionKickCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "say":
					parseReligionSayCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "townlist":
					parseReligionTownlistCommand(player);
					break;
                case "invite":
                    parseReligionInviteCommand(player, StringMgmt.remFirstArg(args));
                    break;
				case "set":
					parseReligionSetCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "toggle":
					parseReligionToggleCommand(player, StringMgmt.remFirstArg(args));
					break;
				case "?":
					showReligionHelp(player);
					break;
				default:
					// Attempt to fetch a religion object from /religion ARGUMENT.
					if (ReligionUtils.getReligionByName(args[0]) == null) {
						Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
						return;
					}

					// Get religion object from /religion ARGUMENT and show the status screen.
					Religion religion = ReligionUtils.getReligionByName(args[0]);
					parseReligionStatusScreenCommand(player, religion, args);
					break;
			}
		} else
			//  Endure that the player exists, and has a town with a religion.
			if (TownyAPI.getInstance().getResident(player.getName()) != null)
				//noinspection ConstantConditions
				if (TownyAPI.getInstance().getResident(player.getName()).getTownOrNull() != null)
					//noinspection ConstantConditions
					if (ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(player.getName()).getTownOrNull()) != null)
						// Handle command.
						//noinspection ConstantConditions
						parseReligionStatusScreenCommand(player, ReligionUtils.getTownReligion(TownyAPI.getInstance().getResident(player.getName()).getTownOrNull()), args);
					else
						Messaging.sendMsg(player, Translation.of("msg_err_not_in_religion"));
				else
					Messaging.sendMsg(player, Translation.of("msg_err_not_in_religion"));
			else
				Messaging.sendMsg(player, Translation.of("msg_err_not_in_religion"));
	}

	private void parseReligionStatusScreenCommand(Player player, Religion religion, String[] args) {
		// Make sure the religion provided is valid.
		if (religion == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
			return;
		}

		// Check if the player wanted to see the townlist.
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("townlist")) {
				parseReligionTownlistCommand(player, religion);
			} else if (args[1].equalsIgnoreCase("enemy")) {
				try {
					if (args[2].equalsIgnoreCase("list"))
						parseReligionEnemyListCommand(player, religion);
					else
						showReligionEnemyHelp(player);
				} catch (ArrayIndexOutOfBoundsException ignored) {
					showReligionAllyHelp(player);
				}
			} else if (args[1].equalsIgnoreCase("ally")) {
				try {
					if (args[2].equalsIgnoreCase("list"))
						parseReligionAllyListCommand(player, religion);
					else
						showReligionAllyHelp(player);
				} catch (ArrayIndexOutOfBoundsException ignored) {
					showReligionAllyHelp(player);
				}
			} else {
				TownyMessaging.sendStatusScreen(player, ReligionUtils.getStatusScreen(religion, player));
			}
		} else {
			// Send a status screen to the player provided.
			TownyMessaging.sendStatusScreen(player, ReligionUtils.getStatusScreen(religion, player));
		}
	}

	private void parseReligionNewCommand(Player player, String[] args) {
		// Ensure the player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_NEW.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		Resident resident = TownyUniverse.getInstance().getResident(player.getName());

		// Ensure the resident exists, and has a town without a religion.
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);
		Account account = Objects.requireNonNull(town).getAccount();

		if (ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_already_has_religion"));
			return;
		}

		// Ensure that the name provided is not empty.
		if (args.length == 0) {
			Messaging.sendMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		// Establish the creation price of a religion based on a permission node.
		double creationPrice = player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_NEW_BYPASS_NO_COST.getNode()) ? 0 : TownyReligionSettings.getReligionCreationPrice();

		// Ensure that the town account can afford to create a new religion.
		if (creationPrice != 0)
			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_NEW_BYPASS_NO_COST.getNode()))
				if (!account.canPayFromHoldings(creationPrice)) {
					Messaging.sendErrorMsg(player, Translation.of("msg_err_insufficient_funds", TownyEconomyHandler.getFormattedBalance(creationPrice), TownyEconomyHandler.getFormattedBalance(creationPrice - account.getCachedBalance())));
					return;
				}

		// Handle spaces in /religion ARGUMENT.
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(args[0]);
		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(" ").append(args[i]);
		}

		// Validate given name.
		String newReligion = stringBuilder.toString();
		try {
			newReligion = ReligionUtils.validateReligionName(newReligion, player);
		} catch (Exception e) {
			Messaging.sendErrorMsg(player, e.getMessage());
			return;
		}
		if (newReligion.isEmpty()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_invalid_characters"));
			return;
		}

		// Ensure that the religion does not have a name similar to one already in use.
        for (String religionName : ReligionUtils.getReligionNames()) {
			if (religionName.equalsIgnoreCase(newReligion)) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_name_in_use"));
				return;
			}
		}

		// Send a confirmation message to the religion creator.
		String finalNewReligion = newReligion;
		Confirmation.runOnAccept(() -> {
			// Fire a cancelable event.
			PreReligionCreateEvent event = new PreReligionCreateEvent(finalNewReligion, town);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
				return;
			}

			// Check to make sure the player can still afford the payment price.
			// This prevents a glitch where the player can withdraw money from the town account before hitting accept.
			if (creationPrice != 0)
				if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_NEW_BYPASS_NO_COST.getNode()))
					if (!account.canPayFromHoldings(creationPrice)) {
						Messaging.sendErrorMsg(player, Translation.of("msg_err_insufficient_funds", TownyEconomyHandler.getFormattedBalance(creationPrice), TownyEconomyHandler.getFormattedBalance(creationPrice - account.getCachedBalance())));
						return;
					}

			// Make sure the player still has the required permissions.
			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_NEW_BYPASS_NO_COST.getNode()))
				town.getAccount().withdraw(creationPrice, "Religion creation price");

			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_NEW.getNode())) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
				return;
			}

			// Actually create the religion object.
			new Religion(finalNewReligion, town).create();
			Religion religion = ReligionUtils.getTownReligion(town);

			// Send a global and town message about the new religion.
			Messaging.sendGlobalMessage(Translation.of("msg_global_new_religion", religion.getFormattedName(), town.getFormattedName()));
			TownyMessaging.sendPrefixedTownMessage(town, Translation.of("msg_religion_set", religion.getFormattedName()));
		})
				.setTitle(com.palmergames.bukkit.towny.object.Translation.of("msg_confirm_purchase", TownyEconomyHandler.getFormattedBalance(creationPrice)))
				.sendTo(player);
	}

	private void parseReligionDeleteCommand(Player player) {
		// Make sure player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_DELETE.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		// Fetch the town's religion object.
		Religion religion = ReligionUtils.getTownReligion(town);

		// Confirm if the user meant to delete the religion.
		Confirmation.runOnAccept(() -> {
			// Fire cancelable event
			PreReligionDeleteEvent event = new PreReligionDeleteEvent(religion, town);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
				return;
			}

			// Send a global message about the disbanding of the religion, and actually delete the religion object.
			Messaging.sendGlobalMessage(Translation.of("msg_global_delete_religion", religion.getFormattedName(), religion.getOverseeingTown().getFormattedName()));
			religion.delete();
		}).sendTo(player);
	}

	private void parseReligionListCommand(Player player, String[] args) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_LIST.getNode())){
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		String compileType = "Residents";
		if (args.length == 3) {
			switch (args[2].toLowerCase(Locale.ROOT)) {
				case "towns":
					compileType = "Towns";
					break;
				case "residents":
					compileType = "Residents";
					break;
				default:
					break;
			}
		}

		int page = 1;
		if (args.length == 4) {
			try {
				page = Integer.parseInt(args[3]);
				if (page < 1) {
					page = 1;
				}
			} catch (NumberFormatException ignored) {
				page = 1;
			}
		}

		Set<Religion> religionSortedSet = ReligionUtils.getReligionsSorted(compileType);

		player.sendMessage(ChatTools.formatTitle("Religions"));
		player.sendMessage(Colors.Blue + Translation.of("religion_name") + Colors.Gray + " - " + Colors.LightBlue + Translation.of("number_of", compileType));

	}

	private void parseReligionHereCommand(Player player) {
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_HERE.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		if (TownyAPI.getInstance().isWilderness(player.getLocation())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_not_claimed"));
			return;
		}

		Town town = TownyAPI.getInstance().getTown(player.getLocation());
		if (town != null) {
			Religion religion = ReligionUtils.getTownReligion(town);

			if (religion != null) {
				TownyMessaging.sendStatusScreen(player, ReligionUtils.getStatusScreen(religion, player));
			} else {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			}
		} else {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_not_claimed"));
		}
	}

    private void parseReligionLeaveCommand(Player player) {
		// Make sure the player has the required permissions.
        if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_LEAVE.getNode())) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
            return;
        }

		// Make sure the player exists, and has a town with a religion, but is not the owner of the religion.
        Resident resident = TownyUniverse.getInstance().getResident(player.getName());
        if (resident == null)
            return;

        if (!resident.hasTown()) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
            return;
        }

        Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

        if (!ReligionUtils.townHasReligion(town)) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
            return;
        }

		Religion religion = ReligionUtils.getTownReligion(town);

        if (religion.getOverseeingTown() == town) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_leave_religion_you_own"));
            return;
        }

		// Fire cancelable event
		PreReligionLeaveEvent event = new PreReligionLeaveEvent(religion, town);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
			return;
		}

		Confirmation.runOnAccept(() -> {
			// Remove the player's town from the religion.
			ReligionUtils.removeTownFromReligion(town);

			// Notify religion and town about the religion event.
			TownyMessaging.sendPrefixedTownMessage(town, Translation.of("msg_religion_remove"));
			Messaging.sendReligionMessage(religion, Translation.of("msg_religion_town_left", town.getFormattedName()));
		}).sendTo(player);
    }

	private void parseReligionKickCommand(Player player, String[] args) {
		// Make sure player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_KICK.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);
		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		// Make sure the player provided a valid town in the religion.
		if (args.length == 0) {
			Messaging.sendMsg(player, Translation.of("msg_err_town_not_provided"));
			return;
		}

		Town targetTown = TownyAPI.getInstance().getTown(args[0]);
		if (targetTown == null) {
			Messaging.sendMsg(player, Translation.of("msg_err_town_non_existent"));
			return;
		}

		if (!ReligionUtils.townHasReligion(targetTown)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_not_in_religion"));
			return;
		}

		// Make sure the target town is in the town's religion
		if (!ReligionUtils.getTownReligion(targetTown).getName().equals(ReligionUtils.getTownReligion(town).getName())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_not_in_religion"));
			return;
		}

		// Make sure the target town is not the town executing the command
		if (targetTown == town) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_self_kick"));
			return;
		}

		// Kick the town from the religion, and inform the religion and target town about the event.
		Religion religion = ReligionUtils.getTownReligion(town);
		religion.kickTown(targetTown);

		Messaging.sendReligionMessage(religion, Translation.of("msg_religion_kicked_town", targetTown.getFormattedName()));
		TownyMessaging.sendPrefixedTownMessage(targetTown, Translation.of("msg_town_kicked", religion.getFormattedName()));
	}

	private void parseReligionSayCommand(Player player, String[] args) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SAY.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);
		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);
		if (args.length == 0) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_blank_msg"));
			return;
		}

		// Handle spaces in /religion say ARGUMENT...
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(args[0]);
		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(" ").append(args[i]);
		}

		Messaging.sendReligionMessage(religion, Translation.of("religion_say_msg", stringBuilder.toString()));
	}

	private void parseReligionJoinCommand(Player player, String[] args) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_JOIN.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists, and has a town without a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = resident.getTownOrNull();

		if (ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_already_has_religion"));
			return;
		}

		// Ensure that the name provided is not empty.
		if (args.length == 0) {
			Messaging.sendMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		// Get the religion from the message and check if it's valid.
		Religion religion = ReligionUtils.getReligionByName(args[0]);

		if (religion == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
			return;
		}

		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_JOIN_BYPASS_PUBLIC_CHECK.getNode()))
			if (!religion.isPublic()) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_not_public", religion.getFormattedName()));
				return;
			}

		// Fire cancelable event.
		PreReligionJoinEvent event = new PreReligionJoinEvent(religion, town);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
			return;
		}

		Confirmation.runOnAccept(() -> {
			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_JOIN_BYPASS_PUBLIC_CHECK.getNode()))
				if (!religion.isPublic()) {
					Messaging.sendErrorMsg(player, Translation.of("msg_err_not_public", religion.getFormattedName()));
					return;
				}

			// Add the player's town to the religion.
			ReligionUtils.addTownToReligion(religion, town);

			// Notify religion about the join event.
			Messaging.sendReligionMessage(religion, Translation.of("msg_religion_town_join", town.getFormattedName()));
		}).sendTo(player);
	}

	private void parseReligionTownlistCommand(Player player) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_TOWNLIST.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = resident.getTownOrNull();
		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		// Build the message with all the towns.
		Religion religion = ReligionUtils.getTownReligion(town);
		StringBuilder religionNames = new StringBuilder("");
		for (Town townInd : religion.getTowns()) {
			if (religion.getTowns().indexOf(townInd) == 0)
				religionNames.append(townInd.getFormattedName());
			else
				religionNames.append(", ").append(townInd.getFormattedName());
		}

		// Send the messages to the player.
		player.sendMessage(ChatTools.formatTitle(religion.getFormattedName() + " " + Translation.of("status_towns")));
		player.sendMessage(Colors.translateColorCodes(
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						Translation.of("status_towns") +
						" " +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") +
						"[" +
						religion.getTowns().size() +
						"]" +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						": " +
						Colors.White +
						religionNames
		));
	}

	private void parseReligionTownlistCommand(Player player, Religion religion) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_TOWNLIST.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		// Build the message with all the towns.
		StringBuilder religionNames = new StringBuilder("");
		for (Town townInd : religion.getTowns()) {
			if (religion.getTowns().indexOf(townInd) == 0)
				religionNames.append(townInd.getFormattedName());
			else
				religionNames.append(", ").append(townInd.getFormattedName());
		}

		// Send the messages to the players.
		player.sendMessage(ChatTools.formatTitle(religion.getFormattedName() + " " + Translation.of("status_towns")));
		player.sendMessage(Colors.translateColorCodes(
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
				Translation.of("status_towns") +
				" " +
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") +
				"[" +
				religion.getTowns().size() +
				"]" +
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
				": " +
				Colors.White +
				religionNames
				));
	}

    private void parseReligionInviteCommand(Player player, String[] args) {
        // Make sure the player has the required permissions.
        if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_INVITE.getNode())) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
            return;
        }

        // Make sure the player exists, and has a town with a religion.
        Resident resident = TownyUniverse.getInstance().getResident(player.getName());
        if (resident == null)
            return;

        if (!resident.hasTown()) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
            return;
        }

        Town town = resident.getTownOrNull();

        if (!ReligionUtils.townHasReligion(town)) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
            return;
        }

        Religion religion = ReligionUtils.getTownReligion(town);

        if (args.length == 0) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town_name"));
            return;
        }

        if (TownyUniverse.getInstance().getTown(args[0]) == null) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_town_non_existent"));
            return;
        }

        Town invitee = TownyUniverse.getInstance().getTown(args[0]);

        if (ReligionUtils.townHasReligion(invitee)) {
            Messaging.sendErrorMsg(player, Translation.of("msg_err_town_belongs_to_other_religion", invitee.getFormattedName()));
            return;
        }

        // Fire cancelable event
        //TODO Change
        /*PreReligionLeaveEvent event = new PreReligionLeaveEvent(religion, town);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
            return;
        }*/

        //TODO Invite Here
        //       Invite Cancelable event
    }

	/* ---------------------------
	 * |                         |
	 * |        Command:         |
	 * |                         |
	 * |    /rel set             |
	 * |             overseer    |
	 * |             board       |
	 * |             name        |
	 * |                         |
	 * ---------------------------
	 */

	private void parseReligionSetCommand(Player player, String[] args) {
		// Ensure that an option was provided.
		if (args.length == 0) {
			showReligionSetHelp(player);
			return;
		}

		// Handle command.
		switch (args[0]) {
			case "overseer":
				parseReligionSetOverseerCommand(player, StringMgmt.remFirstArg(args));
				break;
			case "board":
				parseReligionSetBoardCommand(player, StringMgmt.remFirstArg(args));
				break;
			case "name":
				parseReligionSetNameCommand(player, StringMgmt.remFirstArg(args));
				break;
			default:
				showReligionSetHelp(player);
				break;
		}
	}

	private void parseReligionSetOverseerCommand(Player player, String[] args) {
		// Ensure that the player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_OVERSEER.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure that a possible town name was provided.
		if (args.length == 0) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_not_provided"));
			return;
		}

		// Ensure that the player exists, and is in a town that has a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);
		Town targetTown = TownyAPI.getInstance().getTown(args[0]);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		// Ensure that the potential town provided exists, is not the original owner's town, and is in the religion.
		if (targetTown == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_not_registered", args[0]));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);

		if (religion.getOverseeingTown() == targetTown) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_self_oversee_assignment", religion.getOverseeingTown()));
			return;
		}

		if (!religion.containsTown(targetTown)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_not_in_religion"));
			return;
		}

		// Fire cancelable event.
		PreReligionSetOverseerEvent event = new PreReligionSetOverseerEvent(religion, town, targetTown);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
			return;
		}

		// Actually change the religion and inform the religion about the change event.
		religion.setOverseeingTown(targetTown);
		Messaging.sendReligionMessage(religion, Translation.of("msg_religion_ownership_changed", targetTown.getFormattedName()));
	}

	private void parseReligionSetBoardCommand(Player player, String[] args) {
		// Ensure the player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_BOARD.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);

		// Check if the player wants to remove the religion, or set a new one.
		if (args.length == 0 || args[0].equalsIgnoreCase("remove")) {
			// Fire cancelable event.
			PreReligionSetBoardEvent event = new PreReligionSetBoardEvent(religion, town, "");
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
				return;
			}

			// Change the religion message board and inform the religion about it.
			religion.setBoardMsg("");
			Messaging.sendMsg(player, Translation.of("msg_religion_board_remove"));
		} else {
			// Handle spaces in /religion set board ARGUMENT
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[0]);
			for (int i = 1; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}
			String newMsg = stringBuilder.toString();

			// Ensure the new religion board message follows the default string rules.
			String newBoard;
			try {
				newBoard = ReligionUtils.validateReligionBoard(newMsg, player);
			} catch (Exception e) {
				Messaging.sendErrorMsg(player, e.getMessage());
				return;
			}

			if (newBoard.isEmpty()) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_invalid_characters"));
				return;
			}

			// Fire cancelable event.
			PreReligionSetBoardEvent event = new PreReligionSetBoardEvent(religion, town, newBoard);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
				return;
			}

			// Change the religion message board and inform the religion about it.
			religion.setBoardMsg(newBoard);
			Messaging.sendMsg(player, Translation.of("msg_religion_board_set"));
		}
	}

	private void parseReligionSetNameCommand(Player player, String[] args) {
		// Ensure player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the name of the new town is not null.
		if (args.length == 0) {
			Messaging.sendMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);
		Account account = town.getAccount();

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_already_has_religion"));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);

		// Establish the rename price of a religion based on a permission node.
		double renamePrice = player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_COST.getNode()) ? 0 : TownyReligionSettings.getReligionRenamePrice();

		// Ensure that the town account can afford to create a new religion.
		if (renamePrice != 0)
			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_COST.getNode()))
				if (!account.canPayFromHoldings(renamePrice)) {
					Messaging.sendErrorMsg(player, Translation.of("msg_err_insufficient_funds", TownyEconomyHandler.getFormattedBalance(renamePrice), TownyEconomyHandler.getFormattedBalance(renamePrice - account.getCachedBalance())));
					return;
				}

		// Handle spaces in the name.
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(args[0]);
		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(" ").append(args[i]);
		}
		String[] newName = {stringBuilder.toString()};

		// Make sure the name follows the default name rules.
		try {
			newName[0] = ReligionUtils.validateReligionName(newName[0], player);
		} catch (Exception e) {
			Messaging.sendErrorMsg(player, e.getMessage());
			return;
		}

		if (newName[0].isEmpty()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_invalid_characters"));
			return;
		}

		// Send a confirmation message to the religion creator.
		String finalNewName = newName[0];
		Confirmation.runOnAccept(() -> {
			// Fire a cancelable event.
			PreReligionSetNameEvent event = new PreReligionSetNameEvent(religion, finalNewName);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
				return;
			}

			// Check to make sure the player can still afford the payment price.
			// This prevents a glitch where the player can withdraw money from the town account before hitting accept.
			if (renamePrice != 0)
				if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_COST.getNode()))
					if (!account.canPayFromHoldings(renamePrice)) {
						Messaging.sendErrorMsg(player, Translation.of("msg_err_insufficient_funds", TownyEconomyHandler.getFormattedBalance(renamePrice), TownyEconomyHandler.getFormattedBalance(renamePrice - account.getCachedBalance())));
						return;
					}

			// Make sure the player still has the required permissions.
			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME_BYPASS_COST.getNode()))
				town.getAccount().withdraw(renamePrice, "Religion rename price");

			if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_SET_NAME.getNode())) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
				return;
			}

			// Actually create the religion object.
			// Set the new religion name, and inform the religion of the name change.
			religion.setName(newName[0]);
			Messaging.sendReligionMessage(religion, Translation.of("msg_religion_rename", newName));
		})
				.setTitle(com.palmergames.bukkit.towny.object.Translation.of("msg_confirm_purchase", TownyEconomyHandler.getFormattedBalance(renamePrice)))
				.sendTo(player);
	}

	/* --------------------------
	 * |                        |
	 * |        Command:        |
	 * |                        |
	 * |    /rel toggle         |
	 * |                public  |
	 * |                        |
	 * --------------------------
	 */

	private void parseReligionToggleCommand(Player player, String[] args) {
		// Handle /religion toggle ARGUMENT being empty.
		if (args.length == 0) {
			showReligionToggleHelp(player);
			return;
		}

		// Handle the command.
		switch (args[0]) {
			case "public":
				parseReligionTogglePublicCommand(player);
				break;
			default:
				showReligionToggleHelp(player);
				break;
		}
	}

	private void parseReligionTogglePublicCommand(Player player) {
		// Ensure player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_TOGGLE_PUBLIC.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_already_has_religion"));
			return;
		}

		// Get religion object and current public status.
		Religion religion = ReligionUtils.getTownReligion(town);
		boolean isPublic = religion.isPublic();

		//Fire cancelable event.
		PreReligionSetPublicEvent event = new PreReligionSetPublicEvent(religion, town, !isPublic);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
			return;
		}

		// If the religion is already public, disable the public status, and inform the religion of the event.
		if (isPublic) {
			religion.setPublic(false);

			Messaging.sendReligionMessage(religion, Translation.of("msg_religion_toggle_public", "disabled"));
		} else {
			religion.setPublic(true);

			Messaging.sendReligionMessage(religion, Translation.of("msg_religion_toggle_public", "enabled"));
		}
	}

	/* --------------------------
	 * |                        |
	 * |        Command:        |
	 * |                        |
	 * |    /rel enemy          |
	 * |               add      |
	 * |               remove   |
	 * |               list     |
	 * |                        |
	 * --------------------------
	 */

	private void parseReligionEnemyCommand(Player player, String[] args) {
		// Handle /religion enemy ARGUMENT being empty.
		if (args.length == 0) {
			showReligionEnemyHelp(player);
			return;
		}

		// Handle the command.
		switch (args[0]) {
			case "add":
				parseReligionEnemyAddCommand(player, StringMgmt.remFirstArg(args));
				break;
			case "remove":
				parseReligionEnemyRemoveCommand(player, StringMgmt.remFirstArg(args));
				break;
			case "list":
				parseReligionEnemyListCommand(player);
				break;
			default:
				showReligionEnemyHelp(player);
				break;
		}
	}

	private void parseReligionEnemyAddCommand(Player player, String[] args) {
		// Ensure player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ENEMY_ADD.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		if (args.length == 0) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		Religion targetReligion = ReligionUtils.getReligionByName(args[0]);
		if (targetReligion == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);
		for (Religion enemy : religion.getEnemyList()) {
			if (enemy.getName().equals(targetReligion.getName())) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_already", Translation.of("religion_enemy")));
				return;
			}
		}

		for (Religion ally : religion.getAllyList()) {
			if (ally.getName().equals(targetReligion.getName())) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_already", Translation.of("religion_ally")));
				return;
			}
		}

		ReligionUtils.getTownReligion(town).addEnemy(targetReligion);
		Messaging.sendReligionMessage(religion, Translation.of("msg_religion_enemy_add", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
		Messaging.sendReligionMessage(targetReligion, Translation.of("msg_religion_enemy_add", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
	}

	private void parseReligionEnemyRemoveCommand(Player player, String[] args) {
		// Ensure player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ENEMY_REMOVE.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		if (args.length == 0) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		Religion targetReligion = ReligionUtils.getReligionByName(args[0]);
		if (targetReligion == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);
		boolean temp = false;
		for (Religion enemy : religion.getEnemyList()) {
			if (enemy.getName().equals(targetReligion.getName())) {
				temp = true;
				break;
			}
		}

		if (!temp) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not", Translation.of("religion_enemy")));
			return;
		}

		ReligionUtils.getTownReligion(town).removeEnemy(targetReligion);
		Messaging.sendReligionMessage(religion, Translation.of("msg_religion_enemy_remove", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
		Messaging.sendReligionMessage(targetReligion, Translation.of("msg_religion_enemy_remove", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
	}

	private void parseReligionEnemyListCommand(Player player) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ENEMY_LIST.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = resident.getTownOrNull();
		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		// Build the message with all the towns.
		Religion religion = ReligionUtils.getTownReligion(town);
		StringBuilder religionNames = new StringBuilder("");
		for (Religion religion1 : religion.getEnemyList()) {
			if (religion.getEnemyList().indexOf(religion1) == 0)
				religionNames.append(religion1.getFormattedName());
			else
				religionNames.append(", ").append(religion1.getFormattedName());
		}

		// Send the messages to the player.
		player.sendMessage(ChatTools.formatTitle(religion.getFormattedName() + " " + Translation.of("status_enemies")));
		player.sendMessage(Colors.translateColorCodes(
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						Translation.of("status_enemies") +
						" " +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") +
						"[" +
						religion.getEnemyList().size() +
						"]" +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						": " +
						Colors.White +
						religionNames
		));
	}

	private void parseReligionEnemyListCommand(Player player, Religion religion) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ENEMY_LIST.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		// Build the message with all the towns.
		StringBuilder religionNames = new StringBuilder("");
		for (Religion religion1 : religion.getEnemyList()) {
			if (religion.getEnemyList().indexOf(religion1) == 0)
				religionNames.append(religion1.getFormattedName());
			else
				religionNames.append(", ").append(religion1.getFormattedName());
		}

		// Send the messages to the player.
		player.sendMessage(ChatTools.formatTitle(religion.getFormattedName() + " " + Translation.of("status_enemies")));
		player.sendMessage(Colors.translateColorCodes(
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						Translation.of("status_enemies") +
						" " +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") +
						"[" +
						religion.getEnemyList().size() +
						"]" +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						": " +
						Colors.White +
						religionNames
		));
	}

	/* -------------------------
	 * |                       |
	 * |        Command:       |
	 * |                       |
	 * |    /rel ally          |
	 * |              add      |
	 * |              remove   |
	 * |              list     |
	 * |                       |
	 * -------------------------
	 */

	private void parseReligionAllyCommand(Player player, String[] args) {
		// Handle /religion enemy ARGUMENT being empty.
		if (args.length == 0) {
			showReligionEnemyHelp(player);
			return;
		}

		// Handle the command.
		switch (args[0]) {
			case "add":
				parseReligionAllyAddCommand(player, StringMgmt.remFirstArg(args));
				break;
			case "remove":
				parseReligionAllyRemoveCommand(player, StringMgmt.remFirstArg(args));
				break;
			case "list":
				parseReligionAllyListCommand(player);
				break;
			default:
				showReligionAllyHelp(player);
				break;
		}
	}

	private void parseReligionAllyAddCommand(Player player, String[] args) {
		// Ensure player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ALLY_ADD.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		if (args.length == 0) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		Religion targetReligion = ReligionUtils.getReligionByName(args[0]);
		if (targetReligion == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);
		for (Religion ally : religion.getAllyList()) {
			if (ally.getName().equals(targetReligion.getName())) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_already", Translation.of("religion_ally")));
				return;
			}
		}

		for (Religion enemy : religion.getEnemyList()) {
			if (enemy.getName().equals(targetReligion.getName())) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_already", Translation.of("religion_enemy")));
				return;
			}
		}

		ReligionUtils.getTownReligion(town).addAlly(targetReligion);
		Messaging.sendReligionMessage(religion, Translation.of("msg_religion_ally_add", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
		Messaging.sendReligionMessage(targetReligion, Translation.of("msg_religion_ally_add", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
	}

	private void parseReligionAllyRemoveCommand(Player player, String[] args) {
		// Ensure player has the required permission.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ALLY_REMOVE.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Ensure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		if (args.length == 0) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_name"));
			return;
		}

		Religion targetReligion = ReligionUtils.getReligionByName(args[0]);
		if (targetReligion == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not_registered", args[0]));
			return;
		}

		Religion religion = ReligionUtils.getTownReligion(town);
		boolean temp = false;
		for (Religion ally : religion.getAllyList()) {
			if (ally.getName().equals(targetReligion.getName())) {
				temp = true;
				break;
			}
		}

		if (!temp) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_religion_not", Translation.of("religion_ally")));
			return;
		}

		ReligionUtils.getTownReligion(town).removeAlly(targetReligion);
		Messaging.sendReligionMessage(religion, Translation.of("msg_religion_ally_remove", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
		Messaging.sendReligionMessage(targetReligion, Translation.of("msg_religion_ally_remove", targetReligion.getFormattedName(), ReligionUtils.getTownReligion(town).getFormattedName()));
	}

	private void parseReligionAllyListCommand(Player player) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ALLY_LIST.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if (!resident.hasTown()) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_town"));
			return;
		}

		Town town = resident.getTownOrNull();
		if (!ReligionUtils.townHasReligion(town)) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_town_no_religion"));
			return;
		}

		// Build the message with all the towns.
		Religion religion = ReligionUtils.getTownReligion(town);
		StringBuilder religionNames = new StringBuilder("");
		for (Religion religion1 : religion.getAllyList()) {
			if (religion.getAllyList().indexOf(religion1) == 0)
				religionNames.append(religion1.getFormattedName());
			else
				religionNames.append(", ").append(religion1.getFormattedName());
		}

		// Send the messages to the player.
		player.sendMessage(ChatTools.formatTitle(religion.getFormattedName() + " " + Translation.of("status_allies")));
		player.sendMessage(Colors.translateColorCodes(
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						Translation.of("status_allies") +
						" " +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") +
						"[" +
						religion.getEnemyList().size() +
						"]" +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						": " +
						Colors.White +
						religionNames
		));
	}

	private void parseReligionAllyListCommand(Player player, Religion religion) {
		// Make sure the player has the required permissions.
		if (!player.hasPermission(TownyReligionPermissionNodes.TOWNYRELIGION_RELIGION_ALLY_LIST.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_no_perms"));
			return;
		}

		// Make sure the player exists, and has a town with a religion.
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		// Build the message with all the towns.
		StringBuilder religionNames = new StringBuilder("");
		for (Religion religion1 : religion.getAllyList()) {
			if (religion.getAllyList().indexOf(religion1) == 0)
				religionNames.append(religion1.getFormattedName());
			else
				religionNames.append(", ").append(religion1.getFormattedName());
		}

		// Send the messages to the player.
		player.sendMessage(ChatTools.formatTitle(religion.getFormattedName() + " " + Translation.of("status_allies")));
		player.sendMessage(Colors.translateColorCodes(
				com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						Translation.of("status_allies") +
						" " +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_value") +
						"[" +
						religion.getEnemyList().size() +
						"]" +
						com.palmergames.bukkit.towny.object.Translation.of("status_format_key_value_key") +
						": " +
						Colors.White +
						religionNames
		));
	}
}