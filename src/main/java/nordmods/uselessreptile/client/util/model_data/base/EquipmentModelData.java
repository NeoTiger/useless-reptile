package nordmods.uselessreptile.client.util.model_data.base;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.client.util.ResourceUtil;

import java.util.*;

public record EquipmentModelData(Identifier item, ModelData modelData) {
    //dragon id, list<equipment model data>
    private static final Map<Identifier, List<EquipmentModelData>> equipmentModelDataHolder = new HashMap<>();

    public static final Codec<EquipmentModelData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(EquipmentModelData::item),
            ModelData.CODEC.fieldOf("model_data").forGetter(EquipmentModelData::modelData))
            .apply(instance, EquipmentModelData::new));

    public static Json deserialize(JsonElement input) {
        DataResult<Json> result = Json.CODEC.parse(JsonOps.INSTANCE, input);
        return result.getOrThrow();
    }

    public static void add(Identifier dragon, EquipmentModelData equipmentModelData) {
        List<EquipmentModelData> content = equipmentModelDataHolder.get(dragon);
        if (content != null) {
            if (content.stream().noneMatch(c -> c.item().equals(equipmentModelData.item()))) content.add(equipmentModelData);
        } else {
            content = new ArrayList<>();
            content.add(equipmentModelData);
            equipmentModelDataHolder.put(dragon, content);
        }
    }

    public static void debugPrint() {
        if (!URClientConfig.getConfig().logEquipmentModelData) return;
        for (Map.Entry<Identifier, List<EquipmentModelData>> entry : equipmentModelDataHolder.entrySet()) {
            Identifier dragonId = entry.getKey();
            StringBuilder builder = new StringBuilder().append(dragonId.toString()).append(" - found following equipment model data :");
            entry.getValue().forEach(equipmentModelData -> {
                String data = EquipmentModelData.getInfoForPrint(equipmentModelData).toString().replaceAll(ResourceUtil.TAB_NEWLINE, ResourceUtil.TAB_NEWLINE + ResourceUtil.TAB);
                builder.append(data).append(ResourceUtil.TAB_NEWLINE);
            });
            UselessReptile.LOGGER.info(builder.toString());
        }
    }
    
    public static StringBuilder getInfoForPrint(EquipmentModelData equipmentModelData) {
        StringBuilder builder = new StringBuilder();
        builder.append(ResourceUtil.TAB_NEWLINE).append("Item: ").append(equipmentModelData.item().toString());
        String modelData = ModelData.getInfoForPrint(equipmentModelData.modelData()).toString().replaceAll(ResourceUtil.TAB_NEWLINE, ResourceUtil.TAB_NEWLINE + ResourceUtil.TAB);
        builder.append(ResourceUtil.TAB_NEWLINE).append("Model Data: ").append(modelData);
        return builder;
    }

    public static void reset() {
        equipmentModelDataHolder.clear();
    }

    public static Set<Map.Entry<Identifier, List<EquipmentModelData>>> getEntries() {
        return equipmentModelDataHolder.entrySet();
    }

    public static List<EquipmentModelData> getModelData(Identifier dragon) {
        return equipmentModelDataHolder.get(dragon);
    }

    public record Json(Identifier dragonId, List<EquipmentModelData> equipmentModelData) {
        public static final Codec<Json> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Identifier.CODEC.fieldOf("dragon_id").forGetter(Json::dragonId),
                        EquipmentModelData.CODEC.listOf().fieldOf("equipment_model_data").forGetter(Json::equipmentModelData))
                .apply(instance, Json::new));

        public List<EquipmentModelData> getData() {
            return equipmentModelData();
        }
        
        public void add() {
            equipmentModelData().forEach(equipmentModelData -> EquipmentModelData.add(dragonId(), equipmentModelData));
        }
    }
}
