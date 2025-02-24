package nordmods.uselessreptile.common.util.dragon_variant;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.util.ResourceUtil;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.URRegistryKeys;
import nordmods.uselessreptile.common.util.dragon_variant.model.DragonEquipment;
import nordmods.uselessreptile.common.util.dragon_variant.model.DragonModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonVariantUtil {
    @Nullable
    public static DragonModel getDragonModelData(URDragonEntity dragon) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        DragonVariant variant = DragonVariant.getDragonVariant(dragon);
        if (variant == null) return null;

        return dragon.getWorld().getRegistryManager().get(URRegistryKeys.DRAGON_MODEL).get(variant.dragonModelData());
    }

    @Nullable
    public static DragonEquipment getEquipmentModelData(URDragonEntity dragon, Item item) {
        if (!ResourceUtil.isResourceReloadFinished) return null;

        DragonVariant variant = DragonVariant.getDragonVariant(dragon);
        if (variant == null) return null;

        Identifier id = Registries.ITEM.getId(item);
        List<DragonEquipment> equipments = dragon.getWorld().getRegistryManager().get(URRegistryKeys.DRAGON_EQUIPMENT).get(variant.dragonEquipment());
        if (equipments == null) return null;

        for (DragonEquipment equipment : equipments) if (equipment.item().equals(id)) return equipment;
        return null;
    }
}
