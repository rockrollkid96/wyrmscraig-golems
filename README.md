# Wyrmscraig Golem Tracker

A small RuneLite overlay estimating the full golems needed for the next real Crafting level, up to 99.

Set **Crafting XP per golem** to the total XP of a full cycle, including shaping and core creation. The provisional default is 2,440 for Wyrmscraig goat fur; verify it against your own full cycle. Fur choice and XP modifiers can change this value. The formula is `ceil(XP remaining / XP per golem)`.

The overlay appears whenever logged in, regardless of location. All Crafting XP updates the estimate. Partial golems are not tracked separately: the number estimates full future cycles from current XP. At level 99 it shows "Level 99 reached". The plugin reads skill data and draws an overlay; it performs no player actions.

Enable **Use level goal** to track **Target Crafting level** (1–99, default 99) instead of the next level. Goal mode is off by default. Changes update the overlay immediately. The estimate uses all XP remaining to the selected level; goals at or below your current level show **Goal: Reached**, zero XP remaining, and zero golems. Disable goal mode to return to the next-level estimate. Existing XP-per-golem settings are preserved.

These settings use the existing RuneLite `@ConfigItem` API; `@Range` limits the target to 1–99. The overlay reads the settings while rendering, so no new events or subscriptions are needed.

## Run locally

Use Java 11 and run `./gradlew run` from the root (Windows PowerShell: `./gradlew.bat run`). Enable **Wyrmscraig Golem Tracker** in RuneLite's plugin settings. The development launcher is `src/test/java/com/wyrmscraiggolemtracker/WyrmscraigGolemTrackerPluginTest.java`.

For Jagex Account login, follow https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts.

Run calculation tests with `./gradlew test` (Windows: `./gradlew.bat test`). Only manual in-game testing can confirm client behavior.

## RuneLite APIs and events

- `Plugin` and `@PluginDescriptor` define the plugin and its listing. `startUp()` registers the overlay; `shutDown()` removes it and clears state.
- `@Inject` supplies RuneLite-managed dependencies. `@Provides` and `ConfigManager.getConfig()` supply the configuration interface. `@ConfigGroup`, `@ConfigItem`, and `@Range` define the settings and positive XP range.
- `Client.getSkillExperience(Skill.CRAFTING)` reads current Crafting XP. `Client.getGameState()` checks login state. Boosted levels are not used.
- `ClientThread.invoke()` initialises XP on the client thread when enabling the plugin while already logged in, without blocking startup.
- `@Subscribe` registers event handlers. `StatChanged` updates cached XP only for Crafting. `GameStateChanged` refreshes XP on login and hides the overlay during other states, including logout/loading. There are no scene scans or per-tick polls.
- `Experience.getLevelForXp()` derives the level from XP; `getXpForLevel()` supplies the next level's threshold. `MAX_REAL_LEVEL` caps the target at 99.
- `OverlayManager` adds/removes the overlay. `OverlayPanel` draws the panel; `OverlayPosition.TOP_LEFT` supplies its initial position. `TitleComponent` and `LineComponent` draw text, and `QuantityFormatter.formatNumber()` formats numbers. Rendering performs a few small calculations on cached XP.
- The development launcher's `ExternalPluginManager.loadBuiltin()` loads the local plugin; `RuneLite.main()` starts the development client.

The example greeting feature was removed. Its saved `example/greeting` setting is left untouched; the tracker uses a new feature-specific group with no equivalent greeting setting to migrate.

## Manual checks

1. Enable while logged in: confirm the overlay appears immediately and XP remaining matches the next Crafting threshold.
2. Craft a full golem: verify the XP per cycle for your fur and adjust the setting if needed. Confirm the estimate changes as Crafting XP is gained.
3. Change XP per golem: verify the estimate updates. Check a level-up and one XP short of a level when practical.
4. Log out/in, hop worlds, and disable/re-enable: check for stale account data or duplicate overlays.
5. At 99 Crafting, confirm the level-99 message replaces the estimate.
6. Enable goal mode and select a higher level: confirm the target and total XP/golems update. Change the target again and toggle goal mode off to confirm next-level mode returns.
7. Select your current level or a lower level: confirm **Goal: Reached** and zero golems. This lets you check the reached-goal display without needing to level up.
