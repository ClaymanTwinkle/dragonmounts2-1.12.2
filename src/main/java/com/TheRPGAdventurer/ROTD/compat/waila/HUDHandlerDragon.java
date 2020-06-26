package com.TheRPGAdventurer.ROTD.compat.waila;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper.DragonLifeStage;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper.DragonLifeStageHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;
import java.util.List;

public class HUDHandlerDragon implements IWailaEntityProvider {
    public static IWailaEntityProvider INSTANCE = new HUDHandlerDragon();

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        EntityTameableDragon dragon = (EntityTameableDragon) entity;
        if (dragon.isMale()) {
            currenttip.add(I18n.translateToLocal("dragon.gender.male"));
        } else {
            currenttip.add(I18n.translateToLocal("dragon.gender.female"));
        }

        if(dragon.isTamed()) {
            currenttip.add(I18n.translateToLocal("dragon.Tamed.name"));
        } else {
            currenttip.add(I18n.translateToLocal("dragon.nontamed.name"));
        }

        // life stage
        DragonLifeStageHelper lifeStage = dragon.getLifeStageHelper();
        String lifeStageName = lifeStage.getLifeStage().name().toLowerCase();
        currenttip.add(String.format("%s: %s", I18n.translateToLocal("dragon.currentstage.name"), I18n.translateToLocal("dragon.lifestage."+lifeStageName)));

        int ticksSinceCreation = dragon.getLifeStageHelper().getTicksSinceCreation();
        currenttip.add(String.format("%s: %.2f%%", I18n.translateToLocal("dragon.nextstage.name"), DragonLifeStage.getStageProgressFromTickCount(ticksSinceCreation) * 100));
        return currenttip;
    }
}
