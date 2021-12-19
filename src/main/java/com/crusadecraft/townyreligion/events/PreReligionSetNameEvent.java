package com.crusadecraft.townyreligion.events;

import com.crusadecraft.townyreligion.objects.Religion;
import com.crusadecraft.townyreligion.settings.Translation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PreReligionSetNameEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Religion religion;
	private final String newName;

	private boolean isCancelled = false;
	private String cancelMessage = Translation.of("msg_err_plugin_deny");

	public PreReligionSetNameEvent(Religion religion, String newName) {
		this.religion = religion;
		this.newName = newName;
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

	public String getNewName() {
		return newName;
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
