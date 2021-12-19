package com.crusadecraft.townyreligion.command;

import com.crusadecraft.townyreligion.Messaging;
import com.crusadecraft.townyreligion.metadata.TownMetaDataController;
import com.crusadecraft.townyreligion.settings.Translation;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.util.BukkitTools;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReligionChatCommand implements CommandExecutor, TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

	private void showReligionCommunicationHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/rc", "[msg]", ""));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player && args.length > 0)
			parseReligionCommunicationCommand((Player) sender, args);
		else
			showReligionCommunicationHelp(sender);

		return true;
	}

	/**
	 * Send message to any online players in religion
	 */
	private void parseReligionCommunicationCommand(Player player, String[] args) {
		//Ensure the sender is in a town
		String townReligion = "";
		Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
		if (resident != null && resident.hasTown() && TownMetaDataController.hasTownReligion(TownyAPI.getInstance().getResidentTownOrNull(resident))) {
			townReligion = TownMetaDataController.getTownReligion(TownyAPI.getInstance().getResidentTownOrNull(resident));
		} else {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_command_disable"));
			return;
		}

		String formattedMessage = Translation.of("religion_chat_message", StringMgmt.capitalize(townReligion), resident.getName(), StringMgmt.join(args, " "));

		Resident otherResident;
		for(Player otherPlayer: BukkitTools.getOnlinePlayers()) {
			otherResident = TownyUniverse.getInstance().getResident(otherPlayer.getUniqueId());
			if (otherResident != null && ReligionUtils.isSameReligion(otherResident, townReligion)) {

				//Send message
				otherPlayer.sendMessage(formattedMessage);
				//TownyMessaging........
			}
		}
	}
}