package com.mco.herobrinemod.main;

import com.mco.herobrinemod.entities.herobrine.phase1.EntityHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.ghast.EntityCorruptedGhast;

public class MainEntities {

    public static void registerEntity(){

        LibEntityRegistry.registerEntity("herobrine", EntityHerobrine.class, 0xFFF000, 0x000000);
        LibEntityRegistry.registerEntity("hard_herobrine", EntityHardHerobrine.class, 0xFFFFFF, 0xFFFFFF);
        LibEntityRegistry.registerEntity("corrupted_ghast", EntityCorruptedGhast.class, 0x00000, 0xffed00);
    }

}
