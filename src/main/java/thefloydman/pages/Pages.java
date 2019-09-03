package thefloydman.pages;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefloydman.pages.proxy.CommonProxy;
import thefloydman.pages.util.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
@EventBusSubscriber
public class Pages {

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	private static Logger logger;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) throws IOException, URISyntaxException {

		logger = event.getModLog();
		proxy.preInit(event);

	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {

		proxy.init(event);

	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void itemReg(RegistryEvent.Register<Item> event) {

		proxy.itemReg(event);

	}

}
