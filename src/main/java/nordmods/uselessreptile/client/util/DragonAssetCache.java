package nordmods.uselessreptile.client.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import nordmods.uselessreptile.client.util.model_data.base.DragonModelData;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DragonAssetCache extends AssetCache {
    private HashMap<EquipmentSlot, DragonEquipmentAnimatable> equipmentAnimatablesMap = createEmptyEquipmentMap();
    private Text defaultDisplayName;

    @Override
    public void cleanCache() {
        super.cleanCache();
        equipmentAnimatablesMap.forEach((slot, animatable) -> {
            if (animatable != null) {
                animatable.getAssetCache().cleanCache();
                animatable.equipmentBones.clear();
            }
        });
        equipmentAnimatablesMap = createEmptyEquipmentMap();
        defaultDisplayName = null;
    }

    public void setEquipmentAnimatable(EquipmentSlot slot, DragonEquipmentAnimatable equipmentAnimatable) {
        equipmentAnimatablesMap.put(slot, equipmentAnimatable);}

    @Nullable
    public DragonEquipmentAnimatable getEquipmentAnimatable(EquipmentSlot slot) {
        return equipmentAnimatablesMap.get(slot);
    }

    @Nullable
    public Text getDefaultDisplayName(URDragonEntity dragon) {
        if (!ResourceUtil.isResourceReloadFinished) return null;
        if (defaultDisplayName == null) {
            final Map<String, DragonModelData> map = DragonModelData.getVariantModelData(dragon.getDragonId());
            if (map != null) {
                final DragonModelData modelData = map.get(dragon.getVariant());
                if (modelData != null && modelData.displayNameKey().isPresent()) defaultDisplayName = Text.translatable(modelData.displayNameKey().get());
            }
            if (defaultDisplayName == null) defaultDisplayName = dragon.getType().getName();
        }
        return defaultDisplayName;
    }

    private static HashMap<EquipmentSlot, DragonEquipmentAnimatable> createEmptyEquipmentMap() {
        HashMap<EquipmentSlot, DragonEquipmentAnimatable> map = new HashMap<>(EquipmentSlot.values().length);
        for (EquipmentSlot slot : EquipmentSlot.values()) map.put(slot, null);
        return map;
    }
}