package com.crusadecraft.townyreligion.listeners;

import com.crusadecraft.townyreligion.TownyReligion;
import com.crusadecraft.townyreligion.settings.TownyReligionSettings;
import com.crusadecraft.townyreligion.settings.Translation;
import com.crusadecraft.townyreligion.utils.ReligionUtils;
import com.palmergames.bukkit.towny.event.statusscreen.ResidentStatusScreenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;

public class ResidentEventListener implements Listener {

    @EventHandler
    public void onResidentStatusScreen(ResidentStatusScreenEvent event) {
        if (TownyReligion.getSafeModeEnabled()) {
            return;
        }

        if (TownyReligionSettings.getTownyReligionEnabled())
            if (ReligionUtils.townHasReligion(event.getResident().getTownOrNull()))
                event.addLines(Collections.singletonList(Translation.of("status_religion", ReligionUtils.getTownReligion(event.getResident().getTownOrNull()).getFormattedName())));
            else
                event.addLines(Collections.singletonList(Translation.of("status_religion", "None")));
    }
}

