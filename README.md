# DulkirMod-Fabric
[![discord badge](https://img.shields.io/discord/819011720001224735?label=discord&color=9089DA&logo=discord&style=for-the-badge)](https://discord.gg/WnJwrNZQSn)
[![twitch](https://img.shields.io/twitch/status/dulkir?style=for-the-badge)](https://www.twitch.tv/dulkir)

Future home of dulkirmod for modern versions of minecraft.

## Beta 1.0 is available now! See the release panel on the right side for compiled source

DulkirMod 1.8.9 Can be found [here](https://github.com/inglettronald/DulkirMod).

<details>
    <summary>
        <b>Currently Implemented Features</b>
    </summary>

<ul>
  <li>Toggle Reverse third person</li>
  <li>Expandable Chat Macros with Keybindings</li>
  <li>Dynamic Key</li>
  <li>Config Menu Backend and Front End</li>
  <li>Scrollable Toolips with Zoom Option, compatible with inventory scaling</li>
  <li>Inventory Scaling, supports any float</li>
  <li>Command aliases</li>
  <li>WireFrame and World Text Rendering (needs a recoding)</li>
  <li>Abiphone DND</li>
  <li>Custom Selected Block outline</li>
  <li>Inactive Effigy Waypoints (rift)</li>
  <li>Custom Held Item Placement/Animations</li>
  <li>Commands for Preset Export/Import to/from Clipboard (see /animations)</li>
  <li>Glow Utility (no ESP toggle, currently not used for anything)</li>
  <li>Cooldown Tracking through Durability Display (Working for some sound cooldowns)</li>
  <li>NO DOWNTIME alarm. Plays Iphone alarm if you stop moving after a certain amount of time.</li>
  <li>Large Explosion Particle render toggle</li>
  <li>Hide Scoreboard Numbers</li>
  <li>Arachne Spawn Timer and Keeper Waypoints</li>
  <li>Hide Hunger Display option</li>
  <li>AOTV Etherwarp display</li>
  <li>Action Bar HUD replacements (HEALTH, MANA, DEF, SPEED)</li>
  <li>Hide Lightning in Skyblock</li>
  <li>Hide Fire Overlay</li>
  <li>Slayer Miniboss Alerts + Boxes</li>
  <li>Boss Kill time Feedback for slayers</li>
  <li>Clean Blaze Slayer Mode! (Removes particles and kills fireballs)</li>
  <li>Damage Splash Truncate/Hide</li>
  <li>Blaze Attunement Display</li>
  <li>Broken Hyperion Notification</li>
  <li>Max Visitors Notification</li>
  <li>Garden HUD for Composter/Visitors</li>
  <li>Some vampire slayer features (steak display and ichor highlight)</li>
</ul>
</details>

<details>
    <summary>
        <b>Planned</b>
    </summary>
    <ul>
      <li>More Enderman Slayer Features</li>
      <li>Farming Controls Scheme Features</li>
    </ul>
</details>

<details>
    <summary>
        <b>Suggestions</b>
    </summary>
    Please feel free to drop any suggestions for stuff to add to this mod over in my discord, linked above! I think I
    usually have a good understanding of what mod features need coding, but I'm always open to ideas.
</details>

<details>
    <summary>
        <b>Contributing</b>
    </summary>
    Contributions are welcome! The best way to do this is to create a fork of this repository and suggest changes through
    a pull request on GitHub. Read more about contributions and pull requests <a href="https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork">here</a>.
    My discord dms are also publicly open if you have further questions.
</details>

## For Users
Opening the settings menu can be achieved through the escape menu or through the `/dulkir` command. Almost all features
are off by default, so you **will** want to look into this upon first launch.

Editing HUD positioning makes use of the [JARVIS API](https://github.com/romangraef/jarvis). This library is still in development, but is a fantastic library for a common HUD element editor.
This allows multiple mods to handle the rendering on their own, but have the positioning logic be handled in one place.

Use the command `/jarvis gui` to access the main editor. There is an addressable keybinding for this as well (default `RSHIFT`)

Use the command `/animations` to share animation profiles.

### Dependencies
This mod has 2 dependencies to run:

[Fabric API](https://github.com/FabricMC/fabric/releases/)

[Fabric Kotlin Language Support](https://github.com/FabricMC/fabric-language-kotlin/releases/)

Theoretically, I could throw these in the JAR, but almost all current Fabric mods already have these dependencies anyway.

### Recommendations
(Disclaimer: None of these mods are personally checked by myself for malicious behavior. However, they are well known mods
in the Fabric community, so I thought I would include something about them here.)

There are a few mods here that I would recommend installing as a baseline:

[Sodium](https://modrinth.com/mod/sodium/version/mc1.20-0.4.10?hl=en-US) and [Lithium](https://modrinth.com/mod/lithium?hl=en-US) are basically the Optifine/Patcher of 1.20. They are aimed at general performance improvements.

[Zoomify](https://modrinth.com/mod/zoomify/versions) is the zoom mod I choose to use to replace Optifine's functionality.

[Iris](https://modrinth.com/mod/iris/version/1.6.4+1.20?hl=en-US) allows you to use shaders.

[Borderless Mining](https://www.curseforge.com/minecraft/mc-mods/borderless-mining) is a nice utility for windowed fullscreen, if you're partial to that.

[Farsight](https://www.curseforge.com/minecraft/mc-mods/farsight-fabric/files) is a novel mod that caches chunks outside of server render distance, allowing you to get some WICKED screenshots.

Skyblock Specific: [Firmament](https://github.com/romangraef/Firmament) is a mod that aims to accomplish some of the stuff you would see in NEU 1.8.9. It is also in
a beta state right now and in development, but brings some utility like item list, not resetting cursor between inventories,
image preview (chat), slot locking, and more.

## For Developers
This is intended to be run with Jetbrains Runtime to enable hot-swap to work properly.
To get hot swapping with DCEVM working in this version, I found the most convenient way of achieving this is manually installing
the hotswap jar here and changing your VM args in `build.gradle` to link your java agent properly. I'm not an insane wizard with
this stuff, so my terminology might be kinda poor here. If any developer is interested in helping me refine instructions to
be better to understand, feel free to hit me up.

In the meantime, if you're struggling, I would suggest not worrying about DCEVM. You can (probably?) achieve this by just removing the
last 2 VMargs inside your `build.gradle` and regenerating your run configuration. My DMs are always open to try to offer support
on this stuff, as it will help me learn a thing or two as well.
