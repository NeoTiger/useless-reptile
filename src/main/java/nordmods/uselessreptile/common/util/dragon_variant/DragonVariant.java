package nordmods.uselessreptile.common.util.dragon_variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.URRegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record DragonVariant(Identifier dragonId, String name, Identifier dragonModelData, Identifier dragonEquipment, Optional<Identifier> spawnConditions) {
    public static final Codec<DragonVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(DragonVariant::dragonId),
                    Codec.STRING.fieldOf("name").forGetter(DragonVariant::name),
                    Identifier.CODEC.fieldOf("dragon_model_data").forGetter(DragonVariant::dragonModelData),
                    Identifier.CODEC.fieldOf("equipment").forGetter(DragonVariant::dragonEquipment),
                    Identifier.CODEC.optionalFieldOf("spawn_conditions").forGetter(DragonVariant::spawnConditions))
            .apply(instance, DragonVariant::new));

    public static final Codec<DragonVariant> CODEC_NO_SPAWN_INFO = RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(DragonVariant::dragonId),
                    Codec.STRING.fieldOf("name").forGetter(DragonVariant::name),
                    Identifier.CODEC.fieldOf("dragon_model_data").forGetter(DragonVariant::dragonModelData),
                    Identifier.CODEC.fieldOf("equipment").forGetter(DragonVariant::dragonEquipment))
            .apply(instance, (id, variant, dragonModelData, dragonEquipment) -> new DragonVariant(id, variant, dragonModelData, dragonEquipment, Optional.empty())));

    @Nullable
    public static DragonVariant getByVariant(URDragonEntity dragon) {
        Identifier id = dragon.getDragonId();
        String name = dragon.getVariant();
        Registry<DragonVariant> registry = dragon.getWorld().getRegistryManager().get(URRegistryKeys.DRAGON_VARIANT);
        return registry.stream()
                .filter(dragonVariant -> dragonVariant.dragonId().equals(id) && dragonVariant.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static DragonVariant getByCustomName(URDragonEntity dragon) {
        Identifier id = dragon.getDragonId();
        String name = dragon.getName().getString();
        Registry<DragonVariant> registry = dragon.getWorld().getRegistryManager().get(URRegistryKeys.DRAGON_VARIANT_CUSTOM_NAME);
        return registry.stream()
                .filter(dragonVariant -> dragonVariant.dragonId().equals(id) && dragonVariant.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static DragonVariant getDragonVariant(URDragonEntity dragon) {
        DragonVariant variant = null;
        if (dragon.hasCustomName()) variant = getByCustomName(dragon);
        if (variant != null) return variant;
        return getByVariant(dragon);
    }
}
