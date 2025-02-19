package nordmods.uselessreptile.client.util.model_data;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.config.URClientConfig;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.client.util.model_data.base.DragonModelData;
import nordmods.uselessreptile.client.util.model_data.base.EquipmentModelData;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import org.jetbrains.annotations.Nullable;

public class ModelDataUtil {
    @Nullable
    public static DragonModelData getDragonModelData(URDragonEntity dragon) {
        DragonModelData dragonModelData = null;
        if (!URClientConfig.getConfig().disableNamedEntityModels && dragon.getCustomName() != null) {
            if (DragonModelData.getCustomNameModelData(dragon.getDragonId()) != null)
                dragonModelData = DragonModelData.getCustomNameModelData(dragon.getDragonId()).get(dragon.getCustomName().getString());
            if (dragonModelData != null) return dragonModelData;
        }
        return DragonModelData.getVariantModelData(dragon.getDragonId()).get(dragon.getVariant());
    }

    @Nullable
    public static EquipmentModelData getEquipmentModelData(URDragonEntity dragon, Item item) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        Identifier id = Registries.ITEM.getId(item);
        DragonModelData dragonModelData = getDragonModelData(dragon);
        if (dragonModelData != null && dragonModelData.equipmentModelDataOverrides().isPresent())
            for (EquipmentModelData data : dragonModelData.equipmentModelDataOverrides().get()) {
                if (data.item().equals(id)) return data;
            }
        return getDefaultEquipmentModelData(dragon, id);
    }

    @Nullable
    public static EquipmentModelData getDefaultEquipmentModelData(URDragonEntity dragon, Identifier id) {
        for (EquipmentModelData data : EquipmentModelData.getModelData(dragon.getDragonId())) {
            if (data.item().equals(id)) return data;
        }
        return null;
    }
}
