package nordmods.uselessreptile.common.init;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.util.dragon_variant.DragonVariant;
import nordmods.uselessreptile.common.util.dragon_variant.model.DragonEquipment;
import nordmods.uselessreptile.common.util.dragon_variant.model.DragonModel;
import nordmods.uselessreptile.common.util.dragon_variant.spawn.DragonSpawnConditions;

import java.util.List;

public class URRegistryKeys {
    public static final RegistryKey<Registry<DragonVariant>> DRAGON_VARIANT = RegistryKey.ofRegistry(UselessReptile.id("dragon_variant/variant"));
    public static final RegistryKey<Registry<DragonVariant>> DRAGON_VARIANT_CUSTOM_NAME = RegistryKey.ofRegistry(UselessReptile.id("dragon_variant/custom_name"));
    public static final RegistryKey<Registry<DragonModel>> DRAGON_MODEL = RegistryKey.ofRegistry(UselessReptile.id("dragon_variant/dragon_model"));
    public static final RegistryKey<Registry<List<DragonEquipment>>> DRAGON_EQUIPMENT = RegistryKey.ofRegistry(UselessReptile.id("dragon_variant/equipment"));
    public static final RegistryKey<Registry<List<DragonSpawnConditions>>> DRAGON_SPAWN_CONDITIONS = RegistryKey.ofRegistry(UselessReptile.id("dragon_variant/spawn_conditions"));

    public static void init() {
        DynamicRegistries.registerSynced(DRAGON_VARIANT, DragonVariant.CODEC, DragonVariant.CODEC_NO_SPAWN_INFO, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
        DynamicRegistries.registerSynced(DRAGON_VARIANT_CUSTOM_NAME, DragonVariant.CODEC_NO_SPAWN_INFO, DragonVariant.CODEC_NO_SPAWN_INFO, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
        DynamicRegistries.registerSynced(DRAGON_MODEL, DragonModel.CODEC, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
        DynamicRegistries.registerSynced(DRAGON_EQUIPMENT, DragonEquipment.CODEC.listOf(), DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
        DynamicRegistries.register(DRAGON_SPAWN_CONDITIONS, DragonSpawnConditions.CODEC.listOf());
    }
}
