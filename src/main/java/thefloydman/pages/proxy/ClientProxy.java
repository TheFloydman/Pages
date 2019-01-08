package thefloydman.pages.proxy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import thefloydman.pages.data.BlockInfo;
import thefloydman.pages.data.PagesSymbols;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	File configDir;

	@Override
	public void preInit(FMLPreInitializationEvent event) throws IOException, URISyntaxException {

		configDir = event.getModConfigurationDirectory();
		BlockInfo.checkForConfig(configDir);

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void itemReg(RegistryEvent.Register<Item> event) {

		PagesSymbols.initialize(this.configDir);

	}

}