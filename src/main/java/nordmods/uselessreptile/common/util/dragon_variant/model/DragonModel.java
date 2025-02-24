package nordmods.uselessreptile.common.util.dragon_variant.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nordmods.uselessreptile.client.config.URClientConfig;

import java.util.Optional;

public record DragonModel(ModelData modelData, Optional<String> displayNameKey) {
    public static final Codec<DragonModel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ModelData.CODEC.fieldOf("model_data").forGetter(DragonModel::modelData),
                    Codec.STRING.optionalFieldOf("display_name_key").forGetter(DragonModel::displayNameKey))
            .apply(instance, DragonModel::new));

    public static void debugPrint() {
        if (!URClientConfig.getConfig().logDragonModelData) return;
        //TODO
    }
}
