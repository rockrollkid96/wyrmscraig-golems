package com.wyrmscraiggolemtracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(WyrmscraigGolemTrackerConfig.GROUP)
public interface WyrmscraigGolemTrackerConfig extends Config
{
	String GROUP = "wyrmscraig-golem-tracker";

	@ConfigItem(
		keyName = "useGoal",
		name = "Use level goal",
		description = "Track the chosen target level instead of the next Crafting level",
		position = 1
	)
	default boolean useGoal()
	{
		return false;
	}

	@Range(min = 1, max = 99)
	@ConfigItem(
		keyName = "targetLevel",
		name = "Target Crafting level",
		description = "Desired Crafting level (1–99), used when Use level goal is enabled",
		position = 2
	)
	default int targetLevel()
	{
		return 99;
	}

	@Range(min = 1, max = 200000000)
	@ConfigItem(
		keyName = "xpPerGolem",
		name = "Crafting XP per golem",
		description = "Total Crafting XP for a full golem, including shaping and core creation. Set this for your fur and XP modifiers."
	)
	default int xpPerGolem()
	{
		return 2440;
	}
}
