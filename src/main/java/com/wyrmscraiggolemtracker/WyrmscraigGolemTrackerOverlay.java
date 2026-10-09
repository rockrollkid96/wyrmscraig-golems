package com.wyrmscraiggolemtracker;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.QuantityFormatter;

public class WyrmscraigGolemTrackerOverlay extends OverlayPanel
{
	private final WyrmscraigGolemTrackerPlugin plugin;
	private final WyrmscraigGolemTrackerConfig config;

	@Inject
	WyrmscraigGolemTrackerOverlay(WyrmscraigGolemTrackerPlugin plugin,
		WyrmscraigGolemTrackerConfig config)
	{
		super(plugin);
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.TOP_LEFT);
		panelComponent.setPreferredSize(new Dimension(190, 0));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		int xp = plugin.getCraftingXp();
		if (xp < 0)
		{
			return null;
		}

		panelComponent.getChildren().clear();
		panelComponent.getChildren().add(TitleComponent.builder().text("Wyrmscraig Golems").build());
		boolean useGoal = config.useGoal();
		int targetLevel = GolemEstimate.targetLevel(xp, useGoal, config.targetLevel());
		int remaining = GolemEstimate.xpRemaining(xp, targetLevel);
		if (remaining == 0 && !useGoal)
		{
			addLine("Crafting", "Level 99 reached");
		}
		else
		{
			addLine(useGoal ? "Target Crafting level" : "Next Crafting level", Integer.toString(targetLevel));
			if (remaining == 0)
			{
				addLine("Goal", "Reached");
			}
			addLine("XP remaining", QuantityFormatter.formatNumber(remaining));
			addLine("Golems (estimate)", QuantityFormatter.formatNumber(
				GolemEstimate.golemsRemaining(xp, config.xpPerGolem(), targetLevel)));
			addLine("XP per golem", QuantityFormatter.formatNumber(Math.max(1, config.xpPerGolem())));
		}
		return super.render(graphics);
	}

	private void addLine(String left, String right)
	{
		panelComponent.getChildren().add(LineComponent.builder().left(left).right(right).build());
	}
}
