package nordmods.uselessreptile.client.util.model_data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.client.util.model_data.base.DragonModelData;

import java.util.Map;

public class DragonModelDataReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public DragonModelDataReloadListener() {
        super(new GsonBuilder().create(), "dragon_model_data");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        DragonModelData.reset();
        prepared.forEach((key, value) -> DragonModelData.deserializeJson(value).add());
        DragonModelData.debugPrint();
    }

    @Override
    public Identifier getFabricId() {
        return UselessReptile.id("dragon_model_data");
    }

    public static void init () {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new DragonModelDataReloadListener());
    }
}
