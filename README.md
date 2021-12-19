
<h1>TownyReligion</h1>

## INTRODUCTION
- TownyReligion is an add on plugin for Towny and SiegeWar, which enables "religions" on Towny servers (SiegeWar optional)

---

## REQUIREMENTS
- With SiegeWar
    - Download this customized version of SiegeWar **(Required to interface with TownyReligion)**:  https://github.com/V1nc3ntWasTaken/SiegeWar/releases
    - Download this customized version of Towny **(Required to handle and modify the TownyAPI)**:  https://github.com/V1nc3ntWasTaken/Towny/releases
- Without SiegeWar
    - Download this customized version of Towny **(Required to handle and modify the TownyAPI)**:  https://github.com/V1nc3ntWasTaken/SiegeWar/releases
- All the above are **NOT REQUIRED** as of now.

---

## FEATURES
- Towns can create their own religions.
- Towns can join others' religions.
- More to come very soon.

---

## COMMANDS

### PLAYER
    - `/religion` - As a mayor, interface with the religion your town is in. As a player, view your religions status.

---

## INSTALLATION STEPS
1. Download the latest TownyReligion jar file [here](https://github.com/V1nc3ntWasTaken/TownyReligion/releases).
2. Drop the jar files into your normal plugins folder.
3. Stop your server.
4. Start your server.
5. Edit your townyperms.yml file in the Towny/settings folder.
   Add the following permission for mayors (& other ranks if appropriate):
   `- townyreligion.religion.*`.
   Add the following permission for all users:
   `- townyreligion.religion`.
6. Restart your server.
7. That's it.

---

## DEVELOPERS
1. Add the following to your `pom.xml`
```xml
<dependency>
  <groupId>com.crusadecraft</groupId>
  <artifactId>townyreligion</artifactId>
  <version>VERSION-OF_CHOICE</version>
</dependency>
```
2. Run `mvn install`
