package thefloydman.pages.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import thefloydman.pages.data.BlockInfo;
import thefloydman.pages.logging.LoggerUtils;

public abstract class CheckForConfig {

	public static void init() throws IOException {

		String baseDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
		//baseDir = baseDir.substring(0, baseDir.length() - 1);
		new File(baseDir + "\\config\\pages").mkdirs();
		File blocksFile = new File(baseDir + "\\config\\pages\\blocks.csv");

		if (!blocksFile.exists() || blocksFile.isDirectory()) {

			FileWriter writer = new FileWriter(blocksFile);
			List<List<String>> blockList = new BlockInfo().getDefaultList();
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
