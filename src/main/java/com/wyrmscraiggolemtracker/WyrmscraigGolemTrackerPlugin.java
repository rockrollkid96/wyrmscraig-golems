package com.wyrmscraiggolemtracker;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Wyrmscraig Golem Tracker",
	description = "Shows the estimated golems needed for your next Crafting level",
	tags = {"crafting", "golems", "wyrmscraig"}
)
public class WyrmscraigGolemTrackerPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private WyrmscraigGolemTrackerOverlay overlay;

	private volatile int craftingXp = -1;
	private volatile boolean running;

	@Override
	protected void startUp()
	{
		running = true;
		overlayManager.add(overlay);
		// Enabling the plugin while logged in must also initialise the tracker.
		clientThread.invoke(() ->
		{
			if (running && client.getGameState() == GameState.LOGGED_IN)
			{
				craftingXp = client.getSkillExperience(Skill.CRAFTING);
			}
		});
	}

	@Override
	protected void shutDown()
	{
		running = false;
		overlayManager.remove(overlay);
		craftingXp = -1;
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		if (running && event.getSkill() == Skill.CRAFTING
			&& client.getGameState() == GameState.LOGGED_IN)
		{
			craftingXp = event.getXp();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN && running)
		{
			craftingXp = client.getSkillExperience(Skill.CRAFTING);
		}
		else
		{
			// Hide during logout/loading so another account's XP is never displayed.
			craftingXp = -1;
		}
	}

	int getCraftingXp()
	{
		return craftingXp;
	}

	@Provides
	WyrmscraigGolemTrackerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WyrmscraigGolemTrackerConfig.class);
	}
}
