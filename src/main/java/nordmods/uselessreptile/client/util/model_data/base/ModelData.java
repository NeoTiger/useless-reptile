package nordmods.uselessreptile.client.util.model_data.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.client.util.ResourceUtil;

import java.util.Optional;

public record ModelData(Identifier texture, Optional<Identifier> model, Optional<Identifier> animation, boolean cull, boolean translucent) {
    public static final Codec<ModelData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(ModelData::texture),
            Identifier.CODEC.optionalFieldOf("model").forGetter(ModelData::model),
            Identifier.CODEC.optionalFieldOf("animation").forGetter(ModelData::animation),
            Codec.BOOL.optionalFieldOf("cull", true).forGetter(ModelData::cull),
            Codec.BOOL.optionalFieldOf("translucent", false).forGetter(ModelData::translucent))
            .apply(instance, ModelData::new));

    public static StringBuilder getInfoForPrint(ModelData modelData) {
        StringBuilder builder = new StringBuilder();
        builder.append(ResourceUtil.TAB_NEWLINE).append("Texture: ").append(modelData.texture());
        modelData.model().ifPresent(model -> builder.append(ResourceUtil.TAB_NEWLINE).append("Model: ").append(model));
        modelData.animation().ifPresent(animation -> builder.append(ResourceUtil.TAB_NEWLINE).append("Animation: ").append(animation));
        builder.append(ResourceUtil.TAB_NEWLINE).append("Culling: ").append(modelData.cull());
        builder.append(ResourceUtil.TAB_NEWLINE).append("Translucent: ").append(modelData.translucent());
        return builder;
    }
}
