package com.crusadecraft.townyreligion.events;

import com.crusadecraft.townyreligion.objects.Religion;
import com.crusadecraft.townyreligion.settings.Translation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PreReligionDeleteEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Religion religion;
	private final String townName;
	private final Town town;

	private boolean isCancelled = false;
	private String cancelMessage = Translation.of("msg_err_plugin_deny");

	public PreReligionDeleteEvent(Religion religion, Town town) {
		this.religion = religion;
		this.town = town;
		this.townName = town.getName();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Religion getReligionName() {
		return religion;
	}

	public String getTownName() {
		return townName;
	}

	public Town getTown() {
		return town;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public String getCancelMessage() {
		return cancelMessage;
	}

	public void setCancelMessage(String cancelMessage) {
		this.cancelMessage = cancelMessage;
	}
}
