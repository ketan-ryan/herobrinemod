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
    @Config.Name("Is Phase 3 Enabled")
    @Config.Comment("Whether or not to enable the third phase of the Herobrine fight")
    @Config.RequiresWorldRestart
    public static boolean enableFight = true;

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