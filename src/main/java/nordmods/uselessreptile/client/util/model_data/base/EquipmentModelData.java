package nordmods.uselessreptile.client.util.model_data.base;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.UselessReptile;

import java.util.*;

public record EquipmentModelData(Identifier item, ModelData modelData) {
    //dragon id, list<equipment model data>
    private static final Map<Identifier, List<EquipmentModelData>> equipmentModelDataHolder = new HashMap<>();

    public static final Codec<EquipmentModelData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(EquipmentModelData::item),
            ModelData.CODEC.fieldOf("model_data").forGetter(EquipmentModelData::modelData))
            .apply(instance, EquipmentModelData::new));

    public static EquipmentModelDataJson deserialize(JsonElement input) {
        DataResult<EquipmentModelDataJson> result = EquipmentModelDataJson.CODEC.parse(JsonOps.INSTANCE, input);
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

    //TODO: make it more readable
    public static void debugPrint() {
        for (Map.Entry<Identifier, List<EquipmentModelData>> entry : equipmentModelDataHolder.entrySet()) {
            for (EquipmentModelData data : entry.getValue()) {
                UselessReptile.LOGGER.debug("{}: {}", entry.getKey(), data);
            }
        }
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

    public record EquipmentModelDataJson(Identifier dragonId, List<EquipmentModelData> equipmentModelData) {
        public static final Codec<EquipmentModelDataJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Identifier.CODEC.fieldOf("dragon_id").forGetter(EquipmentModelDataJson::dragonId),
                        EquipmentModelData.CODEC.listOf().fieldOf("equipment_model_data").forGetter(EquipmentModelDataJson::equipmentModelData))
                .apply(instance, EquipmentModelDataJson::new));

        public void add() {
            equipmentModelData().forEach(equipmentModelData -> EquipmentModelData.add(dragonId(), equipmentModelData));
        }
    }
}
