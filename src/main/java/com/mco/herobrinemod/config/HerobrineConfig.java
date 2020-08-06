package com.mco.herobrinemod.config;

import com.mco.herobrinemod.HerobrineMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = HerobrineMod.MODID)
public class HerobrineConfig
{
    @Config.Comment("Damage dealt by direct contact with laser")
    public static float laserDamage = 10.0F;

    @Config.Comment("Should the laser set fire to the terrain")
    public static boolean laserFire = true;

    @Config.Comment("Whether or not to enable the second phase of the Herobrine fight")
    public static boolean enablePhase2 = true;

    @Config.Comment("Whether or not to enable the third phase of the Herobrine fight")
    public static boolean enablePhase3 = true;

    @Config.Comment("If true, disables a part of the fight that may trigger epilepsy")
    public static boolean disableEpilepsy = false;

    @Mod.EventBusSubscriber(modid = HerobrineMod.MODID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(HerobrineMod.MODID)) {
                ConfigManager.sync(HerobrineMod.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
