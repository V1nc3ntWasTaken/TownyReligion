package com.crusadecraft.townyreligion;

import com.crusadecraft.townyreligion.objects.Religion;
import com.crusadecraft.townyreligion.settings.TownyReligionSettings;
import com.crusadecraft.townyreligion.utils.FileMgmt;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FlatFile {
    static File religionsFolder = new File(Towny.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "religions");
    static File religionFile;
    static FileWriter fileWriter;
    static BufferedWriter bufferedWriter;
    static StringBuffer stringBuffer;
    static Scanner scanner;

    /**
     * Creates the folders required for TownyReligions to function.
     */
    public static void createFolders() {
        FileMgmt.checkOrCreateFolder(religionsFolder.getAbsolutePath());
    }

    public static boolean createReligionFile(Religion religion) {
        String name = religion.getName();
        Town leadTown = religion.getOverseeingTown();

        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + name + ".txt");

        if (religionFile.exists())
            return false;

        if (!FileMgmt.checkOrCreateFile(religionsFolder.getAbsolutePath() + File.separator + name + ".txt"))
            return false;

        if (!religionFile.exists())
            return false;

        try {
            fileWriter = new FileWriter(religionFile, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("name=" + name.replace(' ', '_'));
            bufferedWriter.newLine();

            bufferedWriter.write("timeEstablished=" + System.currentTimeMillis());
            bufferedWriter.newLine();

            bufferedWriter.write("towns=" + leadTown.getName());
            bufferedWriter.newLine();

            bufferedWriter.write("overseeingTown=" + leadTown.getName());
            bufferedWriter.newLine();

            bufferedWriter.write("isPublic=" + "false");
            bufferedWriter.newLine();

            bufferedWriter.write("board=" + TownyReligionSettings.getReligionDefaultBoard());
            bufferedWriter.newLine();

            bufferedWriter.write("allyList=" + "");
            bufferedWriter.newLine();

            bufferedWriter.write("enemyList=" + "");
            bufferedWriter.newLine();

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteReligionFile(Religion religion) {
        if (religion == null)
            return false;

        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");

        if (!religionFile.exists())
            return false;

        return religionFile.delete();
    }

    public static ArrayList<Religion> getReligions() {
        ArrayList<Religion> religionList = new ArrayList<>();

        if (new File(religionsFolder.getAbsolutePath()).listFiles() == null)
            return null;

        for (File religionFlatFile : new File(religionsFolder.getAbsolutePath()).listFiles()) {
            religionList.add(getReligionFromFile(religionFlatFile));
        }

        return religionList;
    }

    public static Religion getReligionFromFile(File flatfile) {
        if (!flatfile.exists())
            return null;

        String name = null;
        Long timeEstablished = null;
        String towns = null;
        String overseeingTown = null;
        boolean isPublic = false;
        String board = null;
        String allyList = null;
        String enemyList = null;


        try {
            scanner = new Scanner(flatfile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                if (line.startsWith("name="))
                    name = line.replaceFirst("name=", "");
                else if (line.startsWith("timeEstablished="))
                    timeEstablished = Long.parseLong(line.replaceFirst("timeEstablished=", ""));
                else if (line.startsWith("towns="))
                    towns = line.replaceFirst("towns=", "");
                else if (line.startsWith("overseeingTown="))
                    overseeingTown = line.replaceFirst("overseeingTown=", "");
                else if (line.startsWith("isPublic="))
                    isPublic = line.replaceFirst("isPublic=", "").equals("true");
                else if (line.startsWith("board="))
                    board = line.replaceFirst("board=", "");
                else if (line.startsWith("allyList="))
                    allyList = line.replaceFirst("allyList=", "");
                else if (line.startsWith("enemyList="))
                    enemyList = line.replaceFirst("enemyList=", "");
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (board != null && board.equals(""))
            board = null;

        if (name == null)
            return null;

        if (timeEstablished == null)
            return null;

        if (towns == null)
            return null;

        ArrayList<Town> townsObject = new ArrayList<>();
        String[] townsStringList = towns.split(",");
        for (String townName : townsStringList) {
            if (TownyAPI.getInstance().getTown(townName) != null)
                townsObject.add(TownyAPI.getInstance().getTown(townName));
        }

        if (townsObject.isEmpty())
            return null;

        if (overseeingTown == null)
            return null;

        Town overseeingTownObject = TownyAPI.getInstance().getTown(overseeingTown);
        if (overseeingTownObject == null)
            return null;

        ArrayList<Town> allyListObject = new ArrayList<>();
        if (allyList != null) {
            String[] allyListStringList = allyList.split(",");
            for (String townName : allyListStringList) {
                if (TownyAPI.getInstance().getTown(townName) != null)
                    allyListObject.add(TownyAPI.getInstance().getTown(townName));
            }
        }

        ArrayList<Town> enemyListObject = new ArrayList<>();
        if (enemyList != null) {
            String[] enemyListStringList = enemyList.split(",");
            for (String townName : enemyListStringList) {
                if (TownyAPI.getInstance().getTown(townName) != null)
                    enemyListObject.add(TownyAPI.getInstance().getTown(townName));
            }
        }

        return new Religion(
                name,
                timeEstablished,
                townsObject,
                overseeingTownObject,
                isPublic,
                board,
                allyListObject,
                enemyListObject
        );
    }

    public static boolean removeTownFromReligion(Town town, Religion religion) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");
        stringBuffer = new StringBuffer();

        if (!religionFile.exists())
            return false;

        String townsString = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                stringBuffer.append(line).append(System.lineSeparator());

                if (line.startsWith("towns=")) {
                    townsString = line.replaceFirst("towns=", "");
                }
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (townsString == null)
            return false;

        ArrayList<Town> townsObjectList = new ArrayList<>();
        for (String string : townsString.split(",")) {
            if (TownyAPI.getInstance().getTown(string) != null) {
                townsObjectList.add(TownyAPI.getInstance().getTown(string));
            }
        }

        if (!townsObjectList.contains(town))
            return false;

        townsObjectList.remove(town);

        final String fileContents = stringBuffer.toString();
        String[] temp = {null, null, null};
        String newTownsString;
        boolean[] tests = {false, false, false};

        try {
            // Test 1
            temp[0] = fileContents;

            temp[0] = temp[0].replaceFirst("," + town.getName(), "");
            if (!temp[0].equals(fileContents))
                tests[0] = true;
        } catch (Exception ignored) {}

        try {
            // Test 2
            temp[1] = fileContents;

            temp[1] = temp[1].replaceFirst(town.getName() + ",", "");
            if (!temp[1].equals(fileContents))
                tests[1] = true;
        } catch (Exception ignored) {}

        try {
            // Test 3
            temp[2] = fileContents;

            temp[2] = temp[2].replaceFirst(town.getName(), "");
            if (!temp[2].equals(fileContents))
                tests[2] = true;
        } catch (Exception ignored) {}

        if (tests[0]) {
            newTownsString = temp[0];
        } else if (tests[1]) {
            newTownsString = temp[1];
        } else if (tests[2]) {
            newTownsString = temp[2];
        } else {
            return false;
        }

        try {
            fileWriter = new FileWriter(religionFile);

            fileWriter.write(newTownsString);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList<Town> getReligionTowns(Religion religion) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");

        String towns = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                if (line.startsWith("towns="))
                    towns = line.replaceFirst("towns=", "");
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (towns == null)
            return null;

        ArrayList<Town> townObjectList = new ArrayList<>();
        for (String townName : towns.split(",")) {
            if (TownyAPI.getInstance().getTown(townName) != null)
                townObjectList.add(TownyAPI.getInstance().getTown(townName));
        }

        if (townObjectList == null)
            return null;

        return townObjectList;
    }

    public static boolean setReligionOverseer(Religion religion, Town town) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");
        stringBuffer = new StringBuffer();

        if (!religionFile.exists())
            return false;

        String overseeingTownString = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                stringBuffer.append(line).append(System.lineSeparator());

                if (line.startsWith("overseeingTown="))
                    overseeingTownString = line.replaceFirst("overseeingTown=", "");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (overseeingTownString == null)
            return false;

        Town overseeingTownObject = null;
        if (TownyAPI.getInstance().getTown(overseeingTownString) != null)
            overseeingTownObject = TownyAPI.getInstance().getTown(overseeingTownString);

        if (overseeingTownObject == null)
            return false;

        final String fileContents = stringBuffer.toString();
        String newTownsString;
        String[] temp = {null};
        boolean[] tests = {false};

        try {
            // Test 1
            temp[0] = fileContents;

            temp[0] = temp[0].replaceFirst("overseeingTown=" + overseeingTownObject.getName(), "ownerTown=" + town.getName());
            if (!temp[0].equals(fileContents))
                tests[0] = true;
        } catch (Exception ignored) {}


        if (tests[0]) {
            newTownsString = temp[0];
        } else {
            return false;
        }

        try {
            fileWriter = new FileWriter(religionFile);

            fileWriter.write(newTownsString);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean setReligionBoard(Religion religion, String board) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");
        stringBuffer = new StringBuffer();

        if (!religionFile.exists())
            return false;

        String boardString = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                stringBuffer.append(line).append(System.lineSeparator());

                if (line.startsWith("board="))
                    boardString = line.replaceFirst("board=", "");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        final String fileContents = stringBuffer.toString();
        String newBoardString;
        String[] temp = {null};
        boolean[] tests = {false};

        try {
            // Test 1
            temp[0] = fileContents;

            temp[0] = temp[0].replaceFirst("board=" + boardString, "board=" + board);
            if (!temp[0].equals(fileContents))
                tests[0] = true;
        } catch (Exception ignored) {}

        newBoardString = temp[0];

        try {
            fileWriter = new FileWriter(religionFile);

            fileWriter.write(newBoardString);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean setReligionName(Religion religion, String newName) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");
        File newReligionFile = new File(religionsFolder.getAbsolutePath() + File.separator + newName + ".txt");
        stringBuffer = new StringBuffer();

        if (!religionFile.exists())
            return false;

        String nameString = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                stringBuffer.append(line).append(System.lineSeparator());

                if (line.startsWith("name="))
                    nameString = line.replaceFirst("name=", "");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        final String fileContents = stringBuffer.toString();
        String newNameString;
        String[] temp = {null};
        boolean[] tests = {false};

        try {
            // Test 1
            temp[0] = fileContents;

            temp[0] = temp[0].replaceFirst("name=" + nameString, "name=" + newName);
            if (!temp[0].equals(fileContents))
                tests[0] = true;
        } catch (Exception ignored) {}

        newNameString = temp[0];

        try {
            // fileWriter = new FileWriter(religionFile, false);
            fileWriter = new FileWriter(religionFile);

            fileWriter.write(newNameString);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!religionFile.renameTo(newReligionFile))
            return false;

        return true;
    }

    public static boolean setReligionPublic(Religion religion, boolean isPublic) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");
        stringBuffer = new StringBuffer();

        if (!religionFile.exists())
            return false;

        String isPublicString = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                stringBuffer.append(line).append(System.lineSeparator());

                if (line.startsWith("isPublic="))
                    isPublicString = line.replaceFirst("isPublic=", "");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        final String fileContents = stringBuffer.toString();
        String newIsPublicString;
        String[] temp = {null};
        boolean[] tests = {false};

        try {
            // Test 1
            temp[0] = fileContents;

            temp[0] = temp[0].replaceFirst("isPublic=" + isPublicString, "isPublic=" + isPublic);
            if (!temp[0].equals(fileContents))
                tests[0] = true;
        } catch (Exception ignored) {}

        newIsPublicString = temp[0];

        try {
            fileWriter = new FileWriter(religionFile);

            fileWriter.write(newIsPublicString);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean addTownToReligion(Religion religion, Town town) {
        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + religion.getName() + ".txt");
        stringBuffer = new StringBuffer();

        if (!religionFile.exists())
            return false;

        String townsString = null;

        try {
            scanner = new Scanner(religionFile);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                stringBuffer.append(line).append(System.lineSeparator());

                if (line.startsWith("towns="))
                    townsString = line.replaceFirst("towns=", "");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        final String fileContents = stringBuffer.toString();
        String newTownsString;
        String[] temp = {null};
        boolean[] tests = {false};

        try {
            // Test 1
            temp[0] = fileContents;

            temp[0] = temp[0].replaceFirst("isPublic=", "isPublic=" + town.getName() + ",");
            if (!temp[0].equals(fileContents))
                tests[0] = true;
        } catch (Exception ignored) {}

        newTownsString = temp[0];

        try {
            fileWriter = new FileWriter(religionFile);

            fileWriter.write(newTownsString);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Religion getReligionByName(String name) {
        if (name == null)
            return null;

        religionFile = new File(religionsFolder.getAbsolutePath() + File.separator + name + ".txt");

        if (!religionFile.exists())
            return null;

        Religion religion = getReligionFromFile(religionFile);

        if (religion == null)
            return null;

        return religion;
    }
}
