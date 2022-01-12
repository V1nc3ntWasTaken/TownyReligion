package com.crusadecraft.townyreligion.objects;

import com.crusadecraft.townyreligion.FlatFile;
import com.palmergames.bukkit.towny.object.Town;

import java.util.ArrayList;

public class Religion {
    private String name;
    private long timeEstablished;
    private Town overseeingTown;
    private ArrayList<Town> towns;
    private boolean isPublic;
    private String boardMsg;
    private ArrayList<Religion> allyList;
    private ArrayList<Religion> enemyList;

    public Religion(
            String name,
            long timeEstablished,
            ArrayList<Town> towns,
            Town overseeingTown,
            boolean isPublic,
            String boardMsg,
            ArrayList<Religion> allyList,
            ArrayList<Religion> enemyList
            ) {
        this.name = name;
        this.timeEstablished = timeEstablished;
        this.towns = towns;
        this.overseeingTown = overseeingTown;
        this.isPublic = isPublic;
        this.boardMsg = boardMsg;
        this.allyList = allyList;
        this.enemyList = enemyList;
    }

    public Religion(
            String name,
            Town overseeingTown
            ) {
        this.name = name;
        this.overseeingTown = overseeingTown;
    }

    public Religion() {}

    public String getName() {
        return this.name;
    }

    public boolean setName(String name) {
        boolean bool = FlatFile.setReligionName(this, name);

        if (bool)
            this.name = name;

        return bool;
    }

    public Town getOverseeingTown() {
        return this.overseeingTown;
    }

    public boolean setOverseeingTown(Town town) {
        boolean bool = FlatFile.setReligionOverseer(this, town);

        if (bool)
            this.overseeingTown = town;

        return bool;
    }

    public boolean create() {
        return FlatFile.createReligionFile(new Religion(name, overseeingTown));
    }

    public boolean delete() {
        return FlatFile.deleteReligionFile(this);
    }

    public String getBoardMsg() {
        return this.boardMsg;
    }

    public boolean setBoardMsg(String msg) {
        boolean bool = FlatFile.setReligionBoard(this, msg);

        if (bool)
            this.boardMsg = msg;

        return bool;
    }

    public long getTimeEstablished() {
        return this.timeEstablished;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean setPublic(boolean isPublic) {
        boolean bool = FlatFile.setReligionPublic(this, isPublic);

        if (bool)
            this.isPublic = isPublic;

        return bool;
    }

    public ArrayList<Religion> getReligions() {
        return FlatFile.getReligions();
    }

    public boolean containsTown(Town town) {
        return this.towns.contains(town) || this.overseeingTown == town;
    }

    public String getFormattedName() {
        return name.replace("_", " ");
    }

    public ArrayList<String> getTownNames() {
        ArrayList<String> townNameList = new ArrayList<>();

        for (Town town : getTowns()) {
            townNameList.add(town.getName());
        }

        return townNameList;
    }

    public ArrayList<Town> getTowns() {
        return FlatFile.getReligionTowns(this);
    }

    public boolean kickTown(Town town) {
        if (towns.contains(town) && FlatFile.removeTownFromReligion(town, this)) {
            towns.remove(town);
            return true;
        }

        return false;
    }

    public ArrayList<Religion> getEnemyList() {
        return enemyList;
    }

    public boolean addEnemy(Religion religion) {
        if (FlatFile.addEnemyToReligion(this, religion)) {
            enemyList.add(religion);
            return true;
        }
        return false;
    }

    public ArrayList<String> getEnemyNames() {
        ArrayList<String> temp = new ArrayList<>();

        for (Religion religion : getEnemyList()) {
            temp.add(religion.getName());
        }

        return temp;
    }

    public boolean removeEnemy(Religion religion) {
        if (FlatFile.removeEnemyFromReligion(this, religion)) {
            enemyList.remove(religion);
            return true;
        }

        return false;
    }

    public ArrayList<Religion> getAllyList() {
        return allyList;
    }

    public boolean addAlly(Religion religion) {
        if (FlatFile.addAllyToReligion(this, religion)) {
            allyList.add(religion);
            return true;
        }
        return false;
    }

    public ArrayList<String> getAllyNames() {
        ArrayList<String> temp = new ArrayList<>();

        for (Religion religion : getAllyList()) {
            temp.add(religion.getName());
        }

        return temp;
    }

    public boolean removeAlly(Religion religion) {
        if (FlatFile.removeAllyFromReligion(this, religion)) {
            allyList.remove(religion);
            return true;
        }

        return false;
    }
}
