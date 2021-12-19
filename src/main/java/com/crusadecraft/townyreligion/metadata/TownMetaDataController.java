package com.crusadecraft.townyreligion.metadata;

import com.crusadecraft.townyreligion.TownyReligion;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;

public class TownMetaDataController {

	@SuppressWarnings("unused")
	private TownyReligion plugin;
	private static StringDataField townReligion = new StringDataField("townyreligion_religion", "");

	public TownMetaDataController(TownyReligion plugin) {
		this.plugin = plugin;
	}

	public static String getTownReligion(Town town) {
		StringDataField sdf = (StringDataField) townReligion.clone();
		if (town.hasMeta(sdf.getKey()))
			return MetaDataUtil.getString(town, sdf);
		else
			return null;
	}

	public static void setTownReligion(Town town, String religion) {
		StringDataField sdf = (StringDataField) townReligion.clone();
		if (town.hasMeta(sdf.getKey()))
			if (religion.isEmpty())
				town.removeMetaData(sdf);
			else 
				MetaDataUtil.setString(town, sdf, religion);
		else
			if (religion.isEmpty())
				return;
			town.addMetaData(new StringDataField("townyreligion_religion", religion));
	}

	public static boolean hasTownReligion(Town town) {
		StringDataField sdf = (StringDataField) townReligion.clone();
		return town.hasMeta(sdf.getKey());
	}

	public static void removeTownReligion(Town town) {
		StringDataField sdf = (StringDataField) townReligion.clone();
		town.removeMetaData(sdf);
	}
}
