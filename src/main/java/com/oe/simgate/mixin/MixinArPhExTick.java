package com.oe.simgate.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinArPhExTick {
    
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void simgate$throttleArPhEx(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (!(self.level() instanceof ServerLevel level)) return;
        
        String name = self.getType().getDescriptionId();
        if (!name.contains("arphex")) return;
        
        double nearest = Double.MAX_VALUE;
        for (Player player : level.players()) {
            double dist = self.distanceTo(player);
            if (dist < nearest) nearest = dist;
        }
        
        int simDist = level.getServer().getPlayerList().getViewDistance();
        if (nearest > simDist * 16) {
            ci.cancel();
        }
    }
}
