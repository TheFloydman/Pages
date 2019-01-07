package thefloydman.pages.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.pages.data.BlockInfo;
import thefloydman.pages.logging.LoggerUtils;
import thefloydman.pages.util.Reference;

public abstract class CheckForConfig {

	public static void init(File configDir) throws IOException {

		new File(configDir.getAbsolutePath() + "\\pages").mkdirs();
		File blocksFile = new File(configDir.getAbsolutePath() + "\\pages\\blocks" + Reference.VERSION + ".csv");

		if (!blocksFile.exists() || blocksFile.isDirectory()) {

			FileWriter writer = new FileWriter(blocksFile);
			List<List<String>> blockList = new BlockInfo(configDir).getDefaultList();
			boolean firstTime = true;
			for (int i = 0; i < blockList.size(); i++) {
				String line = blockList.get(i).stream().collect(Collectors.joining(","));
				if (firstTime == false) {
					writer.write("\n");
				}
				writer.write(line);
				firstTime = false;
			}
			writer.close();

		}

	}
}
