package com.mco.herobrinemod;

import com.mco.herobrinemod.main.MainEntities;
import com.mco.herobrinemod.main.MainItems;
import com.mco.herobrinemod.proxies.CommonProxy;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(modid = HerobrineMod.MODID, name = HerobrineMod.NAME, version = HerobrineMod.VERSION, dependencies = "required-after:llibrary@[1.7.14,)")
public class HerobrineMod
{
    public static final String MODID = "herobrinemod";
    public static final String NAME = "The Hard Herobrine Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @SidedProxy(clientSide = "com.mco.herobrinemod.proxies.ClientProxy", serverSide = "com.mco.herobrinemod.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static HerobrineMod instance;

    /**
     * Sets the value of a private final field.
     * @param classToAccess
     * @param instance
     * @param value
     * @param fieldNames
     */
    public static <T, E> void setPrivateFinalValue(Class <? super T > classToAccess, T instance, E value, String... fieldNames) {
        Field field = ReflectionHelper.findField(classToAccess, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldNames));

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(instance, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);

        setPrivateFinalValue(RangedAttribute.class, (RangedAttribute) SharedMonsterAttributes.MAX_HEALTH, Integer.MAX_VALUE, "maximumValue", "field_111118_b");
        MainEntities.registerEntity();
        MainItems.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }

}
