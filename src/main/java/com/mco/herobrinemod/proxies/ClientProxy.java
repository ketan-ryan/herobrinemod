package com.mco.herobrinemod.proxies;

import com.mco.herobrinemod.entities.herobrine.phase1.EntityHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.RenderHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.RenderHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.ghast.EntityCorruptedGhast;
import com.mco.herobrinemod.entities.herobrine.phase2.ghast.RenderCorruptedGhast;
import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase3.RenderHardestHerobrine;
import com.mco.herobrinemod.main.MainItems;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event){
        RenderingRegistry.registerEntityRenderingHandler(EntityHerobrine.class, RenderHerobrine.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityHardHerobrine.class, RenderHardHerobrine.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityHardestHerobrine.class, RenderHardestHerobrine.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedGhast.class, RenderCorruptedGhast.FACTORY);
    }

    @Override
    public void init(FMLInitializationEvent event){
        MainItems.registerRenders();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event){
    }

}
