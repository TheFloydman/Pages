package thefloydman.pages.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thefloydman.pages.logging.LoggerUtils;
import thefloydman.pages.util.Reference;

public class BlockInfo {

	private List<List<String>> list;
	private List<List<String>> defaultList;

	public BlockInfo(File configDir) {

		try {
			getBlockInfoFromFile(configDir);
			getDefaultBlockInfoFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getBlockInfoFromFile(File configDir) throws IOException {

		File blocksFile = new File(configDir.getAbsolutePath() + "\\pages\\blocks" + Reference.VERSION + ".csv");

		List<List<String>> records = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(blocksFile));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			records.add(Arrays.asList(values));
		}
		this.list = records;

	}

	public List<List<String>> getList() {
		return this.list;
	}
	
	private void getDefaultBlockInfoFromFile() throws IOException {
		List<List<String>> records = new ArrayList<>();
		ResourceLocation loc = Reference.forPages("blocks.csv");
		InputStream in = getClass().getClassLoader().getResourceAsStream("assets/pages/blocks.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			records.add(Arrays.asList(values));
		}
		this.defaultList = records;

	}
	
	public List<List<String>> getDefaultList() {
		return this.defaultList;
	}

}
