package nordmods.uselessreptile.common.init;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import nordmods.uselessreptile.common.util.dragon_variant.DragonVariant;
import nordmods.uselessreptile.common.util.dragon_variant.model.DragonEquipment;
import nordmods.uselessreptile.common.util.dragon_variant.model.DragonModel;
import nordmods.uselessreptile.common.util.dragon_variant.spawn.DragonSpawnConditions;

import java.util.List;

public class URRegistryKeys {
    public static final RegistryKey<Registry<DragonVariant>> DRAGON_VARIANT = RegistryKey.ofRegistry(Identifier.of("ur_dragon_variant","variant"));
    public static final RegistryKey<Registry<DragonVariant>> DRAGON_VARIANT_CUSTOM_NAME = RegistryKey.ofRegistry(Identifier.of("ur_dragon_variant","custom_name"));
    public static final RegistryKey<Registry<DragonModel>> DRAGON_MODEL = RegistryKey.ofRegistry(Identifier.of("ur_dragon_variant","dragon_model"));
    public static final RegistryKey<Registry<List<DragonEquipment>>> DRAGON_EQUIPMENT = RegistryKey.ofRegistry(Identifier.of("ur_dragon_variant","equipment"));
    public static final RegistryKey<Registry<List<DragonSpawnConditions>>> DRAGON_SPAWN_CONDITIONS = RegistryKey.ofRegistry(Identifier.of("ur_dragon_variant","spawn_conditions"));

    public static void init() {
        DynamicRegistries.registerSynced(DRAGON_VARIANT, DragonVariant.CODEC, DragonVariant.CODEC_NO_SPAWN_INFO);
        DynamicRegistries.registerSynced(DRAGON_VARIANT_CUSTOM_NAME, DragonVariant.CODEC_NO_SPAWN_INFO);
        DynamicRegistries.registerSynced(DRAGON_MODEL, DragonModel.CODEC);
        DynamicRegistries.registerSynced(DRAGON_EQUIPMENT, DragonEquipment.CODEC.listOf());
        DynamicRegistries.register(DRAGON_SPAWN_CONDITIONS, DragonSpawnConditions.CODEC.listOf());
    }
}
