package nordmods.uselessreptile.integration.modonomicon;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import net.minecraft.data.DataOutput;
import nordmods.uselessreptile.UselessReptile;

public class ModonomiconLanguageProvider extends AbstractModonomiconLanguageProvider {
    private final String locale;
    public ModonomiconLanguageProvider(DataOutput output, String locale, com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider cachedProvider) {
        super(output, UselessReptile.MODID, locale, cachedProvider);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if (locale.equals("en_us")) addEnglishTranslations();
        if (locale.equals("ru_ru")) addRussianTranslations();
    }

    private void addEnglishTranslations() {

    }

    private void addRussianTranslations() {

    }
}
