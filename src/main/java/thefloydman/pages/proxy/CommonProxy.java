package thefloydman.pages.proxy;

import java.io.File;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import thefloydman.pages.config.CheckForConfig;
import thefloydman.pages.data.PagesSymbols;
import thefloydman.pages.util.Reference;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonProxy {
	
	File configDir;

	public void preInit(FMLPreInitializationEvent event) throws IOException {

		configDir = event.getModConfigurationDirectory();
		CheckForConfig.init(configDir);

	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public void itemReg(RegistryEvent.Register<Item> event) {
		
		PagesSymbols.initialize(configDir);
		
	}

}
