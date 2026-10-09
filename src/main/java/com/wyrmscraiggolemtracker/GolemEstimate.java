package com.wyrmscraiggolemtracker;

import net.runelite.api.Experience;

final class GolemEstimate
{
	private GolemEstimate()
	{
	}

	static int nextLevel(int xp)
	{
		return Math.min(Experience.MAX_REAL_LEVEL, Experience.getLevelForXp(xp) + 1);
	}

	static int xpRemaining(int xp)
	{
		return xpRemaining(xp, nextLevel(xp));
	}

	static int targetLevel(int xp, boolean useGoal, int configuredLevel)
	{
		return useGoal ? Math.max(1, Math.min(Experience.MAX_REAL_LEVEL, configuredLevel)) : nextLevel(xp);
	}

	static int xpRemaining(int xp, int targetLevel)
	{
		return Math.max(0, Experience.getXpForLevel(targetLevel) - xp);
	}

	static int golemsRemaining(int xp, int xpPerGolem)
	{
		return golemsRemaining(xp, xpPerGolem, nextLevel(xp));
	}

	static int golemsRemaining(int xp, int xpPerGolem, int targetLevel)
	{
		int remaining = xpRemaining(xp, targetLevel);
		int rate = Math.max(1, xpPerGolem);
		return (int) ((remaining + (long) rate - 1) / rate);
	}
}
