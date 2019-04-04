package thefloydman.pages.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class Reference {
	
	public static final String MOD_ID = "pages";
	public static final String NAME = "Pages";
	public static final String VERSION = "1.3.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "thefloydman.pages.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "thefloydman.pages.proxy.CommonProxy";
	public static final String DEPENDENCIES = "required-after:mystcraft";
	
	public static ResourceLocation forPages(final String path) {
        return new ResourceLocation("pages", path);
    }
	
}