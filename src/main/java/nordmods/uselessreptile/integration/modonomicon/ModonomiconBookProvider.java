package nordmods.uselessreptile.integration.modonomicon;

import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;

import java.util.function.BiConsumer;

public class ModonomiconBookProvider extends SingleBookSubProvider {
    public ModonomiconBookProvider(BiConsumer<String, String> defaultLang) {
        super("dragonarium", UselessReptile.MODID, defaultLang);
    }

    @Override
    protected void registerDefaultMacros() {

    }

    @Override
    protected void generateCategories() {

    }

    @Override
    protected BookModel additionalSetup(BookModel book) {
        add(lang("ru_ru"), this.context().bookName(), "Драконариум");

        return book.withModel(Identifier.of("modonomicon:modonomicon_green"))
                .withBookTextOffsetX(5)
                .withBookTextOffsetY(0) //no top offset
                .withBookTextOffsetWidth(-5)
                .withAllowOpenBooksWithInvalidLinks(true);
    }

    @Override
    protected String bookName() {
        return "Draconarium";
    }

    @Override
    protected String bookTooltip() {
        return "";
    }
}
