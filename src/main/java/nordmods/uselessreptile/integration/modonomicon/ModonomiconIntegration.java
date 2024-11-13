package nordmods.uselessreptile.integration.modonomicon;

import com.klikli_dev.modonomicon.api.datagen.FabricBookProvider;
import com.klikli_dev.modonomicon.api.datagen.LanguageProviderCache;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.loader.api.FabricLoader;

public class ModonomiconIntegration {
    public static void init() {
        if (!FabricLoader.getInstance().isModLoaded("modonomicon")) return;
        //TODO
    }

    public static void initDatagen(FabricDataGenerator fabricDataGenerator) {
        if (!FabricLoader.getInstance().isModLoaded("modonomicon")) return;
        //TODO
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        var enUsCache = new LanguageProviderCache("en_us");
        var ruRuCache = new LanguageProviderCache("ru_ru");

        pack.addProvider(
                FabricBookProvider.of(

                )
        );

        pack.addProvider((FabricDataOutput output) -> new ModonomiconLanguageProvider(output,"en_us", enUsCache));
        pack.addProvider((FabricDataOutput output) -> new ModonomiconLanguageProvider(output, "ru_ru", ruRuCache));
    }
}
