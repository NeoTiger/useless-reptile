package nordmods.uselessreptile.client.util.model_data.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.client.util.ResourceUtil;

import java.util.*;

public record DragonModelData(ModelData modelData, Optional<List<EquipmentModelData>> equipmentModelDataOverrides) {
    //dragon id, map<variant or name, dragon model data>
    private static final Map<Identifier, Map<String, DragonModelData>> variantModelDataHolder = new HashMap<>();
    private static final Map<Identifier, Map<String, DragonModelData>> customNameModelDataHolder = new HashMap<>();

    public static final Codec<DragonModelData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ModelData.CODEC.fieldOf("model_data").forGetter(DragonModelData::modelData),
                    EquipmentModelData.CODEC.listOf().optionalFieldOf("equipment_model_overrides").forGetter(DragonModelData::equipmentModelDataOverrides))
            .apply(instance, DragonModelData::new));

    public static Json deserializeJson(JsonElement element) throws JsonParseException {
        JsonObject input = element.getAsJsonObject();
        DataResult<Json> result = Json.CODEC.parse(JsonOps.INSTANCE, input);
        return result.getOrThrow();
    }

    public static void addVariant(Identifier dragon, String variant, DragonModelData modelData) {
        Map<String, DragonModelData> content = variantModelDataHolder.get(dragon);
        if (content != null) {
            if (!content.containsKey(variant)) content.put(variant, modelData);
        } else {
            content = new HashMap<>();
            content.put(variant, modelData);
            variantModelDataHolder.put(dragon, content);
        }
    }

    public static void addCustomName(Identifier dragon, String customName, DragonModelData modelData) {
        Map<String, DragonModelData> content = customNameModelDataHolder.get(dragon);
        if (content != null) {
            if (!content.containsKey(customName)) content.put(customName, modelData);
        } else {
            content = new HashMap<>();
            content.put(customName, modelData);
            customNameModelDataHolder.put(dragon, content);
        }
    }

    public static void debugPrint() {
        for (Map.Entry<Identifier, Map<String, DragonModelData>> entry : variantModelDataHolder.entrySet()) {
            Identifier dragonId = entry.getKey();
            for (Map.Entry<String, DragonModelData> data : entry.getValue().entrySet()) {
                StringBuilder builder = new StringBuilder().append(dragonId.toString()).append(" - found model data for variant \"").append(data.getKey()).append("\":");
                builder.append(getInfoForPrint(data.getValue()).append(ResourceUtil.TAB_NEWLINE));
                UselessReptile.LOGGER.info(builder.toString());
            }
        }

        for (Map.Entry<Identifier, Map<String, DragonModelData>> entry : customNameModelDataHolder.entrySet()) {
            Identifier dragonId = entry.getKey();
            for (Map.Entry<String, DragonModelData> data : entry.getValue().entrySet()) {
                StringBuilder builder = new StringBuilder().append(dragonId.toString()).append(" - found model data for custom name \"").append(data.getKey()).append("\":");
                builder.append(getInfoForPrint(data.getValue()).append(ResourceUtil.TAB_NEWLINE));
                UselessReptile.LOGGER.info(builder.toString());
            }
        }
    }

    public static StringBuilder getInfoForPrint(DragonModelData dragonModelData) {
        StringBuilder builder = new StringBuilder();
        String modelData = ModelData.getInfoForPrint(dragonModelData.modelData()).toString().replaceAll(ResourceUtil.TAB_NEWLINE, ResourceUtil.TAB_NEWLINE + ResourceUtil.TAB);
        builder.append(ResourceUtil.TAB_NEWLINE).append("Model Data: ").append(modelData);
        dragonModelData.equipmentModelDataOverrides().ifPresent(equipmentModelDataList -> {
            builder.append(ResourceUtil.TAB_NEWLINE).append("Equipment Model Data Overrides: ");
            equipmentModelDataList.forEach(equipmentModelData -> {
                String data = EquipmentModelData.getInfoForPrint(equipmentModelData).toString().replaceAll(ResourceUtil.TAB_NEWLINE, ResourceUtil.TAB_NEWLINE + ResourceUtil.TAB);
                builder.append(data).append(ResourceUtil.TAB_NEWLINE);
            });
        });
        return builder;
    }

    public static void reset() {
        variantModelDataHolder.clear();
        customNameModelDataHolder.clear();
    }

    public static Set<Map.Entry<Identifier, Map<String, DragonModelData>>> getVariantEntries() {
        return variantModelDataHolder.entrySet();
    }

    public static Map<String, DragonModelData> getVariantModelData(Identifier dragon) {
        return variantModelDataHolder.get(dragon);
    }

    public static Set<Map.Entry<Identifier, Map<String, DragonModelData>>> getCustomNameEntries() {
        return customNameModelDataHolder.entrySet();
    }

    public static Map<String, DragonModelData> getCustomNameModelData(Identifier dragon) {
        return customNameModelDataHolder.get(dragon);
    }

    public record Json(Identifier dragonId, Optional<String> variant, Optional<String> customName, ModelData modelData, Optional<List<EquipmentModelData>> equipmentModelDataOverrides) {
        public static final Codec<Json> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Identifier.CODEC.fieldOf("dragon_id").forGetter(Json::dragonId),
                        Codec.STRING.optionalFieldOf("variant").forGetter(Json::variant),
                        Codec.STRING.optionalFieldOf("custom_name").forGetter(Json::customName),
                        ModelData.CODEC.fieldOf("model_data").forGetter(Json::modelData),
                        EquipmentModelData.CODEC.listOf().optionalFieldOf("equipment_model_overrides").forGetter(Json::equipmentModelDataOverrides))
                .apply(instance, Json::new));

        public DragonModelData getData() {
            return new DragonModelData(modelData(), equipmentModelDataOverrides());
        }

        public void add() {
            variant().ifPresent(variant -> addVariant(dragonId(), variant, getData()));
            customName().ifPresent(customName -> addCustomName(dragonId(), customName, getData()));
        }
    }
}
