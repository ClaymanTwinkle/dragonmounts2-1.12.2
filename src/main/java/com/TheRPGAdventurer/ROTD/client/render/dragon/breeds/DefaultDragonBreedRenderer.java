/*
** 2016 March 07
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.client.render.dragon.breeds;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.client.model.dragon.DragonModel;
import com.TheRPGAdventurer.ROTD.client.render.dragon.DragonRenderer;
import com.TheRPGAdventurer.ROTD.client.render.dragon.layer.*;
import com.TheRPGAdventurer.ROTD.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.entity.entitytameabledragon.breeds.EnumDragonBreed;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class DefaultDragonBreedRenderer implements DragonBreedRenderer {
    
    protected final List<LayerRenderer<EntityTameableDragon>> layers = new ArrayList<>();
    
    private final DragonRenderer renderer;
    private final DragonModel model;
    
    private final ResourceLocation maleBodyTexture;
    private final ResourceLocation femaleBodyTexture;
    private final ResourceLocation maleGlowTexture;
    private final ResourceLocation femaleGlowTexture;
    private final ResourceLocation hmaleBodyTexture;
    private final ResourceLocation hfemaleBodyTexture;
    private final ResourceLocation hmaleGlowTexture;
    private final ResourceLocation hfemaleGlowTexture;
    private final ResourceLocation glowAnimTexture;
    private final ResourceLocation saddleTexture;
    private final ResourceLocation eggTexture;
    private final ResourceLocation dissolveTexture;
    private final ResourceLocation chestTexture;
//    private final ResourceLocation armorTexture;

    public DefaultDragonBreedRenderer(DragonRenderer parent, EnumDragonBreed breed) {
        renderer = parent;
        model = new DragonModel(breed);
        
        // standard layers
        layers.add(new LayerRendererDragonGlow(parent, this, model));
//        layers.add(new LayerRendererDragonGlowAnim(parent, this, model));
        layers.add(new LayerRendererDragonSaddle(parent, this, model));
        layers.add(new LayerRendererDragonArmor(parent, this, model));
        layers.add(new LayerRendererDragonChest(parent, this, model));
        layers.add(new LayerRendererDragonBanner(parent, this, model));
        
        
        // textures
        String skin = breed.getBreed().getSkin();
        maleBodyTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/bodym.png");
        femaleBodyTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/bodyfm.png");
        maleGlowTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/glowm.png");
        femaleGlowTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/glowfm.png");
        hmaleBodyTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/hbodym.png");
        hfemaleBodyTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/hbodyfm.png");
        hmaleGlowTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/hglowm.png");
        hfemaleGlowTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/hglowfm.png");
        glowAnimTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/glow_anim.png");
        saddleTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/saddle.png");
        eggTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/egg.png");
        dissolveTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + "dissolve.png");
        chestTexture = new ResourceLocation(DragonMounts.MODID, DragonRenderer.TEX_BASE + skin + "/chest.png");
    }
    
    @Override
    public List<LayerRenderer<EntityTameableDragon>> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    @Override
    public DragonRenderer getRenderer() {
        return renderer;
    }

    @Override
    public DragonModel getModel() {
        return model;
    }

    @Override
    public ResourceLocation getMaleBodyTexture() {
        return maleBodyTexture;
    }
    
	@Override
	public ResourceLocation getFemaleBodyTexture() {
		return femaleBodyTexture;
	}

    @Override
    public ResourceLocation getHMaleBodyTexture() {
        return hmaleBodyTexture;
    }

    @Override
    public ResourceLocation getHFemaleBodyTexture() {
        return hfemaleBodyTexture;
    }

    @Override
    public ResourceLocation getMaleGlowTexture() {
        return maleGlowTexture;
    }
    
    @Override
    public ResourceLocation getFemaleGlowTexture() {
        return femaleGlowTexture;
    }

    @Override
    public ResourceLocation getHMaleGlowTexture() {
        return hmaleGlowTexture;
    }

    @Override
    public ResourceLocation getHFemaleGlowTexture() {
        return hfemaleGlowTexture;
    }

    @Override
    public ResourceLocation getGlowAnimTexture() {
        return glowAnimTexture;
    }

    @Override
    public ResourceLocation getSaddleTexture() {
        return saddleTexture;
    }

    @Override
    public ResourceLocation getEggTexture() {
        return eggTexture;
    }

    @Override
    public ResourceLocation getDissolveTexture() {
        return dissolveTexture;
    }

	@Override
	public ResourceLocation getChestTexture() {
		return chestTexture;
	}

	@Override
	public ResourceLocation getArmorTexture() {
		return null;
	}

}
