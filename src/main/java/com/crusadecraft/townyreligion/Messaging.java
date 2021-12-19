package com.crusadecraft.townyreligion;

import com.crusadecraft.townyreligion.objects.Religion;
import com.crusadecraft.townyreligion.settings.Translation;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messaging {

	final static String prefix = Translation.of("plugin_prefix");
	
	public static void sendErrorMsg(CommandSender sender, String message) {
		//Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
	        sender.sendMessage(prefix + Colors.Red + message);
	}

	public static void sendMsg(CommandSender sender, String message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
    		sender.sendMessage(prefix + Colors.White + message);
	}
	
	public static void sendGlobalMessage(String message) {
        TownyReligion.info(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null && TownyAPI.getInstance().isTownyWorld(player.getWorld()))
            	sendMsg(player, message);
        }
	}

	public static void sendReligionMessage(Religion religion, String message) {
		//TODO Console Broadcast
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != null && TownyAPI.getInstance().isTownyWorld(player.getWorld()))
				religion.getTowns().forEach(town -> {
					town.getResidents().forEach(resident -> {
						if(resident != null)
							if (resident.getPlayer() != null)
								resident.getPlayer().sendMessage(Translation.of("msg_religion_msg", religion.getFormattedName()) + Colors.White + message);
					});
				});

		}
	}
}
