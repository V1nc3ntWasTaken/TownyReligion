# TownyReligion

INTRODUCTION
- TownyReligion is an add on plugin for Towny and SiegeWar, which enables "religions" on Towny servers (SiegeWar optional)

REQUIREMENTS
- With SiegeWar
    - Download this customized version of SiegeWar **(Required to interface with TownyReligion)**:  https://github.com/V1nc3ntWasTaken/SiegeWar/releases
    - Download this customized version of Towny **(Required to handle and modify the TownyAPI)**:  https://github.com/V1nc3ntWasTaken/Towny/releases
- Without SiegeWar
    - Download this customized version of Towny **(Required to handle and modify the TownyAPI)**:  https://github.com/V1nc3ntWasTaken/SiegeWar/releases
- All the above are kept up to date.

FEATURES
- Towns can create their own religions.
- Towns can join others' religions.
- More to come very soon.

COMMANDS

    PLAYER
    - `/religion` - As a mayor, interface with the religion your town is in. As a player, view your religions status.

INSTALLATION STEPS
1. Download the TownyCultures jar file here: https://github.com/V1nc3ntWasTaken/TownyReligion/releases
2. Drop the jar file into your normal plugins folder.
3. Stop your server.
4. Start your server.
5. Edit your townyperms.yml file in the Towny/settings folder,
   and add the following permission for mayors (& other ranks if appropriate):<br>
   `- townyreligion.religion.*`.
6. Run the command `/ta reload townyperms`.
7. That's it.