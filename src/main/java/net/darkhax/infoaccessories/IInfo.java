package net.darkhax.infoaccessories;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IInfo {
    
    void applyInfo (World world, EntityPlayer player, List<String> left, List<String> right);
}
