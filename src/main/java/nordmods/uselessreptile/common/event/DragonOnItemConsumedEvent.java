package nordmods.uselessreptile.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Fired whenever {@link nordmods.uselessreptile.common.entity.base.URDragonEntity} attempts to consume an item.
 * user - usually either {@link net.minecraft.entity.player.PlayerEntity} (when interacting with mob) or {@link nordmods.uselessreptile.common.entity.base.URDragonEntity}
 * itemStack - {@link ItemStack} that is attempted to be consumed
 */
public interface DragonOnItemConsumedEvent {
    Event<DragonOnItemConsumedEvent> EVENT = EventFactory.createArrayBacked(
            DragonOnItemConsumedEvent.class,
            callbacks -> ((user, itemStack) -> {
                for (DragonOnItemConsumedEvent event : callbacks) event.onItemConsumed(user, itemStack);
            })
    );
    void onItemConsumed(@Nullable LivingEntity user, ItemStack itemStack);
}
