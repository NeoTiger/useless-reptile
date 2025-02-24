package nordmods.uselessreptile.common.util.dragon_variant.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.config.URClientConfig;

public record DragonEquipment(Identifier item, ModelData modelData) {
    public static final Codec<DragonEquipment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("item").forGetter(DragonEquipment::item),
                    ModelData.CODEC.fieldOf("model_data").forGetter(DragonEquipment::modelData))
            .apply(instance, DragonEquipment::new));

    public static void debugPrint() {
        if (!URClientConfig.getConfig().logEquipmentModelData) return;
        //TODO
    }
}
