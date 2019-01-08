package thefloydman.pages.proxy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import net.minecraft.item.Item;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import thefloydman.pages.data.BlockInfo;
import thefloydman.pages.data.PagesSymbols;
import thefloydman.pages.util.Reference;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonProxy {
	
	File configDir;

	public void preInit(FMLPreInitializationEvent event) throws IOException, URISyntaxException {

		configDir = event.getModConfigurationDirectory();
		BlockInfo.checkForConfig(configDir);

	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public void itemReg(RegistryEvent.Register<Item> event) {
		
		PagesSymbols.initialize(configDir);
		
	}

}
