package com.mco.herobrinemod.main;

import com.mco.herobrinemod.entities.herobrine.phase1.EntityHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.ghast.EntityCorruptedGhast;
import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase3.laser.EntityLaser;

public class MainEntities {

    public static void registerEntity()
    {
        LibEntityRegistry.registerEntity("herobrine", EntityHerobrine.class, 0xFFF000, 0x000000);
        LibEntityRegistry.registerEntity("hard_herobrine", EntityHardHerobrine.class, 0xFFFFFF, 0xFFFFFF);
        LibEntityRegistry.registerEntity("hardest_herobrine", EntityHardestHerobrine.class, 0x00000, 0x00000);

        LibEntityRegistry.registerEntityEggless("corrupted_ghast", EntityCorruptedGhast.class);
        LibEntityRegistry.registerEntityEggless("entity_laser", EntityLaser.class);

    }

}
