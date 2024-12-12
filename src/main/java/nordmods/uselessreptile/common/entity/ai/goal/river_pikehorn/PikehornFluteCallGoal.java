package nordmods.uselessreptile.common.entity.ai.goal.river_pikehorn;

import net.minecraft.entity.player.PlayerEntity;
import nordmods.uselessreptile.common.entity.RiverPikehornEntity;
import nordmods.uselessreptile.common.entity.ai.goal.common.FlyingDragonCallBackGoal;

public class PikehornFluteCallGoal extends FlyingDragonCallBackGoal<RiverPikehornEntity> {

    public PikehornFluteCallGoal(RiverPikehornEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        if (!entity.isTamed()) return false;
        if (entity.isLeashed() || entity.hasVehicle() || entity.isSitting()) return false;
        if (!entity.shouldFollow) return false;
        PlayerEntity player = (PlayerEntity) entity.getOwner();
        if (player == null) return false;
        double distance = entity.squaredDistanceTo(player);
        if (distance < player.getWidth() * player.getWidth() * 4) return false;
        if (isFollowing) return true;
        return distance < maxCallDistance;
    }

    @Override
    public void start() {
        super.start();
        entity.stopHunt();
    }

    @Override
    public void stop() {
        super.stop();
        entity.shouldFollow = false;
    }
}
