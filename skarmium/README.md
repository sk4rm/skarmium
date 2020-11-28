# skarmium
###### Capture The Flag plugin for Minecraft 1.16.4 (Spigot)
##### What this plugin has:
* place and remove flags almost anywhere
* setup scoreboard and teams with one command
* reset scores quickly for a new game
* right click to capture flag
* walk over dropped ally flag to teleport it back to original location
* bugs

##### What this plugin does not have:
* anti-cheat
* player spawnpoints
* a responsible author
* the ability to wear a helmet when capturing a flag
* flag drops upon breaking the block below the flag (please use `/flag remove` instead)
* flag control (you won't know how many or where the flags are)
* a beacon beam showing you the flag location (cuz the beam is rendered on client side D: )

#

### ~~Quick~~ Setup Tutorial
1. Find or build a map that you think is fair.

2. Execute command `/flag tool` or `/flag wand` for placing a flag. Ensure that you have admin privileges.
3. Right-click the block where you want a flag to be. A gray flag (Neutral Flag) will spawn.
4. Stand on the gray flag and use `/flag team red` or `/flag team blue` to convert the gray flag to red or blue respectively.
5. If you made a placement mistake, stand on the flag and use `/flag remove`.
6. Repeat the above for the other color.
7. Type `/score setup` to automatically set up scoreboard and teams. Teams `skarmiumRedTeam` and `skarmiumBlueTeam` will be created.
8. Add players to the teams by using `/team join skarmiumRedTeam <player name>` for Red Team and `/team join skarmiumRedTeam <player name>` for Blue Team.

#

After this, the game can be started. Here are some notes:
* Right-click to capture flag.
* You can score a point by capturing it and walking over your flag.
* When you kill a enemy player capturing your flag, the flag will be placed on his death location.
* Walk over the dropped flag to teleport it back to initial location.
* You can only score when your flag is present at its initial location.
