package nordmods.uselessreptile.integration;

import net.fabricmc.loader.api.FabricLoader;

public class ModonomiconIntegration {
    public static void init() {
        if (!FabricLoader.getInstance().isModLoaded("modonomicon")) return;
        System.out.println("AAA Modonomicon loaded! AAA");
    }
}
