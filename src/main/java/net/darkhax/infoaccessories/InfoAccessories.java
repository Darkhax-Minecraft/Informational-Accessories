package net.darkhax.infoaccessories;

import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.infoaccessories.items.ItemInfoAccessory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "infoaccessories", name = "Info Accessories", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.1.449,);", certificateFingerprint = "@FINGERPRINT@")
public class InfoAccessories {
    
    public static final LoggingHelper LOG = new LoggingHelper("Info Accessories");
    public static final RegistryHelper REGISTRY = new RegistryHelper("infoaccessories").setTab(CreativeTabs.MISC);
    public static Item infoItem;
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
    
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new InfoHandler());
        infoItem = REGISTRY.registerItem(new ItemInfoAccessory(), "info_accessory");
    }
    
    @SubscribeEvent
    public void registerItems (RegistryEvent.Register<Item> event) {
    
        for (Item item : REGISTRY.getItems()) {
            
            event.getRegistry().register(item);
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels (ModelRegistryEvent event) {
    
        for (Item item : REGISTRY.getItems()) {
            
            REGISTRY.registerInventoryModel(item);
        }
    }
    
    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {
    
        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}