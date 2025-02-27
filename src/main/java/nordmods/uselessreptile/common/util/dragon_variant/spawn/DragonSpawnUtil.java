package nordmods.uselessreptile.common.util.dragon_variant.spawn;

import net.minecraft.block.Block;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.init.URRegistryKeys;
import nordmods.uselessreptile.common.util.dragon_variant.DragonVariant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DragonSpawnUtil {
    //1st id - dragon id, 2nd id - biome id, pair<string, dragon spawn conditions> - variant and conditions (biome info stripped)
    private static final Map<Identifier, Map<Identifier, List<Pair<String, DragonSpawnConditions>>>> variantsPerBiomeCache = new HashMap<>();

    public static boolean isBiomeInList(List<Codecs.TagEntryId> list, WorldAccess world, BlockPos blockPos) {
        RegistryEntry<Biome> biome = world.getBiome(blockPos);

        for (Codecs.TagEntryId tagEntryId : list) {
            if (tagEntryId.tag()) {
                if (biome.isIn(TagKey.of(RegistryKeys.BIOME, tagEntryId.id()))) return true;
            } else if (biome.matchesId(tagEntryId.id())) return true;
        }

        return false;
    }

    public static boolean isBlockInList(List<Codecs.TagEntryId> list, WorldAccess world, BlockPos blockPos) {
        RegistryEntry<Block> block = world.getBlockState(blockPos.down()).getRegistryEntry();

        for (Codecs.TagEntryId tagEntryId : list) {
            if (tagEntryId.tag()) {
                if (block.isIn(TagKey.of(RegistryKeys.BLOCK, tagEntryId.id()))) return true;
            } else if (block.matchesId(tagEntryId.id())) return true;
        }

        return false;
    }

    public static void assignAvailableVariant(URDragonEntity entity, SpawnReason spawnReason) {
        BlockPos pos = entity.getBlockPos();
        WorldAccess world = entity.getWorld();
        Identifier id = entity.getDragonId();
        Stream<DragonVariant> variantStream = getAvailableVariants(world, pos, id);
        boolean canWarn = spawnReason == SpawnReason.NATURAL
                || spawnReason == SpawnReason.EVENT
                || spawnReason == SpawnReason.CHUNK_GENERATION
                || spawnReason == SpawnReason.BREEDING;

        DynamicRegistryManager registryManager = entity.getWorld().getRegistryManager();

        List<Pair<String, Integer>> variants = new ArrayList<>();
        variantStream.forEach(variant -> {
            registryManager.get(URRegistryKeys.DRAGON_SPAWN_CONDITIONS).get(variant.spawnConditions().get()).forEach(conditions -> {
                if (checkConditions(conditions, world, pos)) variants.add(new Pair<>(variant.name(), conditions.weight()));
            });
        });

        if (variants.isEmpty()) {
            if (canWarn) UselessReptile.LOGGER.warn("Failed to set name for {} at {} as none can spawn there. Setting default", entity.getName().getString(), entity.getBlockPos());
            entity.setVariant(entity.getDefaultVariant());
            return;
        }

        int totalWeight = 0;
        for (Pair<String, Integer> variant : variants) totalWeight += variant.getRight();

        int roll = entity.getRandom().nextInt(totalWeight);
        int previousBound = 0;
        for (Pair<String, Integer> variant : variants) {
            if (roll >= previousBound && roll < previousBound + variant.getRight()) {
                entity.setVariant(variant.getLeft());
                break;
            }
            previousBound += variant.getRight();
        }
    }

    public static Stream<DragonVariant> getAvailableVariants(WorldAccess world, BlockPos pos, Identifier dragonId) {
        DynamicRegistryManager registryManager = world.getRegistryManager();
        return getAllVariants(world, dragonId).filter(variant -> {
           for (DragonSpawnConditions conditions : registryManager.get(URRegistryKeys.DRAGON_SPAWN_CONDITIONS).get(variant.spawnConditions().get())) {
               if (checkConditions(conditions, world, pos)) return true;
           }
           return false;
        });
    }

    /**
     * @param world WorldAccess
     * @param dragonId Identifier of the dragon
     * @return Stream of all variants that can spawn naturally
     */
    public static Stream<DragonVariant> getAllVariants(WorldAccess world, Identifier dragonId) {
        DynamicRegistryManager registryManager = world.getRegistryManager();
        return registryManager.get(URRegistryKeys.DRAGON_VARIANT).stream()
                .filter(variant -> variant.dragonId().equals(dragonId))
                .filter(variant -> {
                    if (variant.spawnConditions().isPresent()) {
                        List<DragonSpawnConditions> conditionsList = registryManager.get(URRegistryKeys.DRAGON_SPAWN_CONDITIONS).get(variant.spawnConditions().get());
                        if (conditionsList == null) return false;
                        for (DragonSpawnConditions conditions : conditionsList) if (conditions.weight() > 0) return true;
                    }
                    return false;
                });
    }

    private static boolean checkConditions(DragonSpawnConditions conditions, WorldAccess world, BlockPos pos) {
        //altitude check
        if (conditions.altitudeRestriction().isPresent()) {
            DragonSpawnConditions.AltitudeRestriction restriction = conditions.altitudeRestriction().get();
            if (restriction.min() > pos.getY() || restriction.max() <= pos.getY()) return false;
        }
        //allowed tagEntries check (whitelist)
        if (conditions.allowedBiomes().isPresent()) {
            List <Codecs.TagEntryId> list = conditions.allowedBiomes().get();
            if (!list.isEmpty() && !isBiomeInList(list, world, pos)) return false;
        }
        //banned tagEntries check (blacklist)
        if (conditions.bannedBiomes().isPresent()) {
            List <Codecs.TagEntryId> list = conditions.bannedBiomes().get();
            if (!list.isEmpty() && isBiomeInList(list, world, pos)) return false;
        }
        //allowed blocks check (whitelist)
        if (conditions.allowedBlocks().isPresent()) {
            List <Codecs.TagEntryId> list = conditions.allowedBlocks().get();
            if (!list.isEmpty() && !isBlockInList(list, world, pos)) return false;
        }
        //banned blocks check (blacklist)
        if (conditions.bannedBlocks().isPresent()) {
            List <Codecs.TagEntryId> list = conditions.bannedBlocks().get();
            if (!list.isEmpty() && isBlockInList(list, world, pos)) return false;
        }

        return true;
    }
}
