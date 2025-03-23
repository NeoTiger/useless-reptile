package nordmods.uselessreptile.datagen.data.mod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import nordmods.uselessreptile.common.dragon_variant.model.DragonModel;
import nordmods.uselessreptile.common.dragon_variant.model.ModelData;
import nordmods.uselessreptile.common.init.UREntities;
import nordmods.uselessreptile.common.init.URSounds;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class URDragonModelProvider implements DataProvider {
    protected final FabricDataOutput output;
    private final DataOutput.PathResolver pathResolver;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;
    private final List<Pair<Identifier, DragonModel>> holder = new ArrayList<>();

    public URDragonModelProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "ur_dragon_variant/dragon_model");
        this.registryLookupFuture = registryLookupFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture.thenCompose((registryLookupFuture) -> {
            addSpawnEntries();
            List<CompletableFuture<?>> list = new ArrayList<>();
            holder.forEach(entry -> {
                Path path = pathResolver.resolveJson(entry.getLeft());
                list.add(DataProvider.writeCodecToPath(writer, registryLookupFuture, DragonModel.CODEC, entry.getRight(), path));
            });
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    protected void addSpawnEntries() {
        addWyvern("jeb_");
        addWyvern("green");
        addWyvern("brown");

        addMoleclaw("black");
        addMoleclaw("brown");
        addMoleclaw("grey");
        addMoleclaw("albino");

        addLightningChaser("blue");
        addLightningChaser("grey");
        addLightningChaser("brown");
        addLightningChaser("purple");

        addRiverPikehorn("green");
        addRiverPikehorn("dark_green");
        addRiverPikehorn("blue");
        addRiverPikehorn("dark_blue");
        addRiverPikehorn("purple");
        addRiverPikehorn("dark_purple");
        addRiverPikehorn("teal");
        addRiverPikehorn("dark_teal");
    }

    protected ModelData getModelData(Identifier id, String variant, boolean cull) {
        Identifier texture = Identifier.of(id.getNamespace(), "textures/entity/" + id.getPath() + "/" + variant +".png");
        Identifier model = Identifier.of(id.getNamespace(), "geo/entity/" + id.getPath() + "/" + id.getPath() +".geo.json");
        Identifier animation = Identifier.of(id.getNamespace(), "animations/entity/" + id.getPath() + "/" + id.getPath() +".animation.json");
        return new ModelData(texture, model, Optional.of(animation), cull, false);
    }

    protected void addWyvern(String variant) {
        List<DragonModel.Sound> sounds = new ArrayList<>();
        sounds.add(new DragonModel.Sound("step", URSounds.WYVERN_STEP.getId(), Optional.empty(), Optional.empty()));
        sounds.add(new DragonModel.Sound("woosh", URSounds.DRAGON_WOOSH.getId(), Optional.of(2f), Optional.empty()));
        sounds.add(new DragonModel.Sound("flap", SoundEvents.ENTITY_ENDER_DRAGON_FLAP.getId(), Optional.of(3f), Optional.of(0.7f)));
        sounds.add(new DragonModel.Sound("shoot", SoundEvents.ENTITY_ENDER_DRAGON_SHOOT.getId(), Optional.of(2f), Optional.empty()));
        sounds.add(new DragonModel.Sound("bite", URSounds.WYVERN_BITE.getId(), Optional.empty(), Optional.empty()));
        addEntry(UREntities.WYVERN_ENTITY, variant, Optional.of(sounds), true);
    }

    protected void addMoleclaw(String variant) {
        List<DragonModel.Sound> sounds = new ArrayList<>();
        sounds.add(new DragonModel.Sound("step", URSounds.DRAGON_STEP.getId(), Optional.empty(), Optional.of(0.7f)));
        sounds.add(new DragonModel.Sound("attack_strong", URSounds.MOLECLAW_STRONG_ATTACK.getId(), Optional.empty(), Optional.empty()));
        sounds.add(new DragonModel.Sound("attack", URSounds.MOLECLAW_ATTACK.getId(), Optional.empty(), Optional.empty()));
        addEntry(UREntities.MOLECLAW_ENTITY, variant, Optional.of(sounds), false);
    }

    protected void addRiverPikehorn(String variant) {
        List<DragonModel.Sound> sounds = new ArrayList<>();
        sounds.add(new DragonModel.Sound("step", SoundEvents.ENTITY_CHICKEN_STEP.getId(), Optional.of(0.5f), Optional.of(0.8f)));
        sounds.add(new DragonModel.Sound("woosh", URSounds.DRAGON_WOOSH.getId(), Optional.of(0.7f), Optional.of(1.2f)));
        sounds.add(new DragonModel.Sound("flap", SoundEvents.ENTITY_ENDER_DRAGON_FLAP.getId(), Optional.empty(), Optional.of(1.2f)));
        sounds.add(new DragonModel.Sound("attack", URSounds.PIKEHORN_ATTACK.getId(), Optional.empty(), Optional.empty()));
        addEntry(UREntities.RIVER_PIKEHORN_ENTITY, variant, Optional.of(sounds), true);
    }

    protected void addLightningChaser(String variant) {
        List<DragonModel.Sound> sounds = new ArrayList<>();
        sounds.add(new DragonModel.Sound("step", URSounds.DRAGON_STEP.getId(), Optional.empty(), Optional.empty()));
        sounds.add(new DragonModel.Sound("woosh", URSounds.DRAGON_WOOSH.getId(), Optional.of(2f), Optional.empty()));
        sounds.add(new DragonModel.Sound("flap", SoundEvents.ENTITY_ENDER_DRAGON_FLAP.getId(), Optional.of(3f), Optional.of(0.6f)));
        sounds.add(new DragonModel.Sound("flap_heavy", SoundEvents.ENTITY_ENDER_DRAGON_FLAP.getId(), Optional.of(3f), Optional.of(0.5f)));
        sounds.add(new DragonModel.Sound("bite", URSounds.LIGHTNING_CHASER_BITE.getId(), Optional.empty(), Optional.empty()));
        addEntry(UREntities.LIGHTNING_CHASER_ENTITY, variant, Optional.of(sounds), true);
    }

    protected void addEntry(Identifier id, DragonModel variant) {
        holder.add(new Pair<>(id, variant));
    }

    protected void addEntry(Identifier dragonId, String variant, Optional<List<DragonModel.Sound>> sounds, boolean cull) {
        addEntry(getId(dragonId, variant), new DragonModel(getModelData(dragonId, variant, cull), Optional.empty(), sounds));
    }

    protected void addEntry(EntityType<? extends Entity> entityType, String variant, Optional<List<DragonModel.Sound>> sounds, boolean cull) {
        addEntry(EntityType.getId(entityType), variant, sounds, cull);
    }

    protected Identifier getId(Identifier dragonId, String variant) {
        return Identifier.of(dragonId.getNamespace(), dragonId.getPath() + "/" + variant);
    }

    @Override
    public String getName() {
        return "Dragon Model";
    }
}
