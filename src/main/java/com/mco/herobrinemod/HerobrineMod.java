package com.mco.herobrinemod;

import com.mco.herobrinemod.capabilities.AnimationInfoProvider;
import com.mco.herobrinemod.entities.herobrine.base.BaseHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.HerobrineRenderer;
import com.mco.herobrinemod.entities.herobrine.base.BaseHerobrineRenderer;
import com.mco.herobrinemod.main.HerobrineSensors;
import com.mco.herobrinemod.main.HerobrineEntities;
import com.mco.herobrinemod.main.HerobrineItems;
import com.mco.herobrinemod.main.HerobrineMemoryModules;
import com.mco.herobrinemod.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HerobrineMod.MODID)
public class HerobrineMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "herobrinemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public HerobrineMod()
    {
        GeckoLib.initialize();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        HerobrineItems.register(modEventBus);
        HerobrineEntities.register(modEventBus);
        HerobrineMemoryModules.register(modEventBus);
        HerobrineSensors.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::buildContents);
        modEventBus.addListener(this::entityAttributes);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        PacketHandler.init();
    }

    public void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(HerobrineEntities.HEROBRINE.get(), Herobrine.createAttributes().build());
        event.put(HerobrineEntities.BASE_HEROBRINE.get(), BaseHerobrine.createAttributes().build());

    }

    @SubscribeEvent
    public void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Herobrine) {
            if(!event.getObject().getCapability(AnimationInfoProvider.ANIMATION_INFO).isPresent()) {
                event.addCapability(new ResourceLocation(MODID, "animationinfo"), new AnimationInfoProvider());
            }
        }
    }

    private void buildContents(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID, "herobrine"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("item_group." + MODID + ".herobrine"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(HerobrineItems.HARD_SPAWNER.get()))
                        // Add default items to tab
                        .displayItems((enabledFlags, populator, hasPermissions) -> {
                            populator.accept(HerobrineItems.HARD_SPAWNER.get());
                            populator.accept(HerobrineItems.HARD_SWORD.get());
                            populator.accept(HerobrineItems.HARDER_SWORD.get());
                            populator.accept(HerobrineItems.HALFHARDER_SWORD.get());
                            populator.accept(HerobrineItems.HARDEST_SWORD.get());
                            populator.accept(HerobrineItems.HARD_HELMET.get());
                            populator.accept(HerobrineItems.HARD_CHESTPLATE.get());
                            populator.accept(HerobrineItems.HARD_LEGGINGS.get());
                            populator.accept(HerobrineItems.HARD_BOOTS.get());
                            populator.accept(HerobrineItems.HARDER_HELMET.get());
                            populator.accept(HerobrineItems.HARDER_CHESTPLATE.get());
                            populator.accept(HerobrineItems.HARDER_LEGGINGS.get());
                            populator.accept(HerobrineItems.HARDER_BOOTS.get());
                            populator.accept(HerobrineItems.HALFHARDER_HELMET.get());
                            populator.accept(HerobrineItems.HALFHARDER_CHESTPLATE.get());
                            populator.accept(HerobrineItems.HALFHARDER_LEGGINGS.get());
                            populator.accept(HerobrineItems.HALFHARDER_BOOTS.get());
                            populator.accept(HerobrineItems.HARDEST_HELMET.get());
                            populator.accept(HerobrineItems.HARDEST_CHESTPLATE.get());
                            populator.accept(HerobrineItems.HARDEST_LEGGINGS.get());
                            populator.accept(HerobrineItems.HARDEST_BOOTS.get());
                            populator.accept(HerobrineItems.HEROBRINE_SPAWN_EGG.get());
                        }).build()
        );
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(HerobrineEntities.HEROBRINE.get(), HerobrineRenderer::new);
            event.registerEntityRenderer(HerobrineEntities.BASE_HEROBRINE.get(), BaseHerobrineRenderer::new);

        }
    }
}
