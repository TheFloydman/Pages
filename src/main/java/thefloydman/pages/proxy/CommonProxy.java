package thefloydman.pages.proxy;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thefloydman.pages.config.CheckForConfig;
import thefloydman.pages.data.PagesSymbols;
import thefloydman.pages.util.Reference;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) throws IOException {

		CheckForConfig.init();

	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public void itemReg(RegistryEvent.Register<Item> event) {
		
		PagesSymbols.initialize();
		
	}

}
