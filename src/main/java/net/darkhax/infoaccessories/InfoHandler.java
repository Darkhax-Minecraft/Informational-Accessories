package net.darkhax.infoaccessories;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfoHandler {
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void onDebugScreen (RenderGameOverlayEvent.Text event) {
    
        final Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayer player = mc.player;
        final List<String> left = event.getLeft();
        final List<String> right = event.getRight();
        
        // Exit if not debug mode
        if (!mc.gameSettings.showDebugInfo) {
            
            return;
        }
        
        // Removes existing info
        // TODO add config to keep entries. 
        left.clear();
        right.clear();
        
        // Adds info from the onfo types
        for (InfoType type : InfoType.values()) {
            
            if (type.canPlayerSee(player)) {
                
                type.getInfo(player.world, player, left, right);
            }
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTooltipEvent (ItemTooltipEvent event) {
    
        if (event.getEntityPlayer() == null) {
            
            return;
        }
        
        // Adds info from the info types
        for (InfoType type : InfoType.values()) {
            
            if (type.isValidItem(event.getItemStack())) {
                
                type.getInfo(event.getEntityPlayer().world, event.getEntityPlayer(), event.getToolTip(), event.getFlags().isAdvanced() ? event.getToolTip() : new ArrayList<>());
            }
        }
    }
    
    public static double getStackAttribute (ItemStack stack, EntityEquipmentSlot slot, IAttribute attribute) {
    
        final IAttributeInstance instance = new AttributeMap().registerAttribute(attribute);
        
        EntityPlayer p = PlayerUtils.getClientPlayer();
        instance.setBaseValue(p.getEntityAttribute(attribute).getBaseValue());
        for (AttributeModifier modifier : stack.getAttributeModifiers(slot).get(attribute.getName())) {
            
            instance.applyModifier(modifier);
        }
        
        return instance.getAttributeValue();
    }
}