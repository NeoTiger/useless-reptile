package nordmods.uselessreptile.common.util.dragon_variant.spawn;

import net.minecraft.block.Block;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registry;
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

import java.util.ArrayList;
import java.util.List;

public class DragonSpawnUtil {
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

    public static void assignVariantFromList(URDragonEntity entity, List<Pair<String, DragonSpawnConditions>> variants, SpawnReason spawnReason) {
        int totalWeight = 0;
        for (Pair<String, DragonSpawnConditions> variant : variants) totalWeight += variant.getRight().weight();
        boolean canWarn = spawnReason == SpawnReason.NATURAL
                || spawnReason == SpawnReason.EVENT
                || spawnReason == SpawnReason.CHUNK_GENERATION
                || spawnReason == SpawnReason.BREEDING;
        if (totalWeight <= 0) {
            if (canWarn) UselessReptile.LOGGER.warn("Failed to set name for {} at {} as none can spawn there. Setting default", entity.getName().getString(), entity.getBlockPos());
            entity.setVariant(entity.getDefaultVariant());
            return;
        }

        int roll = entity.getRandom().nextInt(totalWeight);
        int previousBound = 0;

        for (Pair<String, DragonSpawnConditions> variant : variants) {
            if (roll >= previousBound && roll < previousBound + variant.getRight().weight()) {
                entity.setVariant(variant.getLeft());
                break;
            }
            previousBound += variant.getRight().weight();
        }
    }

    public static List<Pair<String, DragonSpawnConditions>> getAvailableVariants(WorldAccess world, URDragonEntity entity) {
        return getAvailableVariants(world, entity.getBlockPos(), entity.getDragonId());
    }

    public static List<Pair<String, DragonSpawnConditions>> getAvailableVariants(WorldAccess world, BlockPos pos, Identifier dragonId) {
        List<Pair<String, List<DragonSpawnConditions>>> variants = new ArrayList<>();
        Registry<List<DragonSpawnConditions>> spawnConditionsRegistry = world.getRegistryManager().get(URRegistryKeys.DRAGON_SPAWN_CONDITIONS);
        world.getRegistryManager().get(URRegistryKeys.DRAGON_VARIANT).stream()
                .filter(dragonVariant -> dragonVariant.dragonId().equals(dragonId))
                .forEach(dragonVariant ->
                        dragonVariant.spawnConditions().ifPresent(conditions -> variants.add(new Pair<>(dragonVariant.name(), spawnConditionsRegistry.get(conditions)))));

        List<Pair<String, DragonSpawnConditions>> allowedVariants = new ArrayList<>(variants.size());
        variants.forEach(variant -> {
            variant.getRight().forEach(conditions -> {
                //altitude check
                if (conditions.altitudeRestriction().isPresent()) {
                    DragonSpawnConditions.AltitudeRestriction restriction = conditions.altitudeRestriction().get();
                    if (restriction.min() > pos.getY() || restriction.max() <= pos.getY()) return;
                }
                //banned tagEntries check (blacklist)
                if (conditions.bannedBiomes().isPresent()) {
                    List <Codecs.TagEntryId> list = conditions.bannedBiomes().get();
                    if (!list.isEmpty() && isBiomeInList(list, world, pos)) return;
                }
                //allowed tagEntries check (whitelist)
                if (conditions.allowedBiomes().isPresent()) {
                    List <Codecs.TagEntryId> list = conditions.allowedBiomes().get();
                    if (!list.isEmpty() && !isBiomeInList(list, world, pos)) return;
                }
                //banned blocks check (blacklist)
                if (conditions.bannedBlocks().isPresent()) {
                    List <Codecs.TagEntryId> list = conditions.bannedBlocks().get();
                    if (!list.isEmpty() && isBlockInList(list, world, pos)) return;
                }
                //allowed blocks check (whitelist)
                if (conditions.allowedBlocks().isPresent()) {
                    List <Codecs.TagEntryId> list = conditions.allowedBlocks().get();
                    if (!list.isEmpty() && !isBlockInList(list, world, pos)) return;
                }

                allowedVariants.add(new Pair<>(variant.getLeft(), conditions));
            });
        });
        return allowedVariants;
    }
}
