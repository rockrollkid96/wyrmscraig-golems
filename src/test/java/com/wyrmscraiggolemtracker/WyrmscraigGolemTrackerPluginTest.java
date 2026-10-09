package com.wyrmscraiggolemtracker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WyrmscraigGolemTrackerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(WyrmscraigGolemTrackerPlugin.class);
		RuneLite.main(args);
	}
}
