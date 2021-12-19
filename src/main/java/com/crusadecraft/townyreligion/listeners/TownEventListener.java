package com.crusadecraft.townyreligion.listeners;

import com.crusadecraft.townyreligion.Messaging;
import com.crusadecraft.townyreligion.settings.TownyReligionSettings;
import com.crusadecraft.townyreligion.settings.Translation;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;

public class TownEventListener implements Listener {

	@EventHandler
	public void onTownStatusScreen(TownStatusScreenEvent event) {
		if (TownyReligionSettings.getTownyReligionEnabled())
			if (ReligionUtils.townHasReligion(event.getTown()))
				event.addLines(Collections.singletonList(Translation.of("status_town_religion", ReligionUtils.getTownReligion(event.getTown()).getFormattedName())));
			else
				event.addLines(Collections.singletonList(Translation.of("status_town_religion", "None")));
	}

	@EventHandler
	public void onPreTownDelete(PreDeleteTownEvent event) {
		if (TownyReligionSettings.getTownyReligionEnabled())
			if (!event.isCancelled())
				if (ReligionUtils.townHasReligion(event.getTown()))
					if (ReligionUtils.getTownReligion(event.getTown()).getOverseeingTown() == event.getTown()) {
						Messaging.sendGlobalMessage(Translation.of("msg_global_new_religion", ReligionUtils.getTownReligion(event.getTown()).getFormattedName(), event.getTown().getFormattedName()));
						ReligionUtils.getTownReligion(event.getTown()).delete();
					}
	}

	/*@EventHandler
	public void preTownDestroy(PreDeleteTownEvent event) {
		if (TownyReligionSettings.isTownyReligionEnabled())
			if (TownMetaDataController.hasTownReligion(event.getTown())) {
				String religion = TownMetaDataController.getTownReligion(event.getTown());
				ReligionUtils.deleteTownFromLocalDatabase(event.getTownName());

				if (!ReligionUtils.getTowns().containsValue(religion))
					ReligionUtils.deleteReligionFromLocalDatabase(religion);
			}
	}

	@EventHandler
	public void onTownCreate(NewTownEvent event) {
		if (TownyReligionSettings.isTownyReligionEnabled())
			ReligionUtils.createTownInLocalDatabase(event.getTown().getName(), null);
	}*/
}
