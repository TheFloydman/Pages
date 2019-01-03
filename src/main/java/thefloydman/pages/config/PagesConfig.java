package thefloydman.pages.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import thefloydman.pages.util.Reference;

@Config(modid = Reference.MOD_ID)
public class PagesConfig {

	@Name("vanilla")
	public static CategoryVanilla vanillaBlocks = new CategoryVanilla();
	
	@Name("rf_tools")
	public static CategoryRFTools rfBlocks = new CategoryRFTools();

	public static class CategoryVanilla {
		

		@RequiresMcRestart
		@Name("Block of Diamond CardRank")
		public int cardRankBlockOfDiamond = 6;

		@RequiresMcRestart
		@Name("Block of Emerald CardRank")
		public int cardRankBlockOfEmerald = 6;
		
		@RequiresMcRestart
		@Name("Air Block CardRank")
		public int cardRankAirBlock = 6;

	}
	
	public static class CategoryRFTools {

		@RequiresMcRestart
		@Name("Dimensional Shard Ore CardRank")
		public int cardRankDimensionalShardOre = 4;

	}

	@Mod.EventBusSubscriber
	public static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}