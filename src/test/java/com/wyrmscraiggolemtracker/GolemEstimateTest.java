package com.wyrmscraiggolemtracker;

import net.runelite.api.Experience;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GolemEstimateTest
{
	@Test
	public void goalToggleSelectsConfiguredOrNextLevel()
	{
		int xp = Experience.getXpForLevel(60);
		assertEquals(85, GolemEstimate.targetLevel(xp, true, 85));
		assertEquals(61, GolemEstimate.targetLevel(xp, false, 85));
		assertEquals(1, GolemEstimate.targetLevel(xp, true, 0));
		assertEquals(99, GolemEstimate.targetLevel(xp, true, 100));
	}

	@Test
	public void countsAcrossMultipleLevelsAndRoundsUp()
	{
		// Level 1 to level 10 requires 1,154 XP: twelve 100-XP cycles.
		assertEquals(1154, GolemEstimate.xpRemaining(0, 10));
		assertEquals(12, GolemEstimate.golemsRemaining(0, 100, 10));
		assertEquals(11, GolemEstimate.golemsRemaining(100, 100, 10));
	}

	@Test
	public void completedOrLowerGoalsNeedNoGolems()
	{
		int xp = Experience.getXpForLevel(80);
		assertEquals(0, GolemEstimate.golemsRemaining(xp, 2440, 80));
		assertEquals(0, GolemEstimate.golemsRemaining(xp, 2440, 70));
	}

	@Test
	public void roundsUpPartialGolems()
	{
		int target = Experience.getXpForLevel(61);
		assertEquals(1, GolemEstimate.golemsRemaining(target - 1, 2440));
		assertEquals(1, GolemEstimate.golemsRemaining(target - 2440, 2440));
		assertEquals(2, GolemEstimate.golemsRemaining(target - 2441, 2440));
	}

	@Test
	public void advancesTargetAtLevelBoundary()
	{
		int xp = Experience.getXpForLevel(61);
		assertEquals(62, GolemEstimate.nextLevel(xp));
		assertEquals(Experience.getXpForLevel(62) - xp, GolemEstimate.xpRemaining(xp));
	}

	@Test
	public void stopsAtRealLevel99()
	{
		assertEquals(0, GolemEstimate.golemsRemaining(Experience.getXpForLevel(99), 2440));
		assertEquals(0, GolemEstimate.golemsRemaining(Experience.MAX_SKILL_XP, 2440));
	}

	@Test
	public void protectsAgainstInvalidConfiguredRate()
	{
		assertEquals(83, GolemEstimate.golemsRemaining(0, 0));
		assertEquals(1, GolemEstimate.golemsRemaining(0, Integer.MAX_VALUE));
	}
}
