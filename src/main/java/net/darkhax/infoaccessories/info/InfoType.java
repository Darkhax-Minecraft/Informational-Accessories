package net.darkhax.infoaccessories.info;

import java.util.List;

import net.darkhax.bookshelf.data.MoonPhase;
import net.darkhax.bookshelf.lib.MCDate;
import net.darkhax.bookshelf.util.BlockUtils;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.bookshelf.util.StackUtils;
import net.darkhax.infoaccessories.InfoAccessories;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public enum InfoType {
    
    PLAYER_X(Items.COMPASS, (world, player, left, right) -> left.add(String.format("X: %.3f", player.posX))),
    PLAYER_Y(2, (world, player, left, right) -> left.add(String.format("Y: %.3f", player.posY))),
    PLAYER_Z(Items.COMPASS, (world, player, left, right) -> left.add(String.format("Z: %.3f", player.posZ))),
    CALENDAR(5, (world, player, left, right) -> addDateInfo(world, left)),
    MOVEMENT(0, (world, player, left, right) -> left.add(String.format("Motion X: %.3f Y: %.3f Z: %.3f", player.motionX, player.motionY, player.motionZ))),
    MOON_PHASE(1, (world, player, left, right) -> left.add("Moon Phase: " + MoonPhase.getCurrentPhase().getPhaseName())),
    BIOME(6, (world, player, left, right) -> left.add("Biome: " + world.getBiomeForCoordsBody(player.getPosition()).getBiomeName())),
    CHUNK(7, (world, player, left, right) -> addChunkInfo(world, player, left)),
    SLIME_CHUNK(4, (world, player, left, right) -> left.add("Slime Chunk: " + BlockUtils.isSlimeChunk(world, player.getPosition()))),
    DPS(8, (world, player, left, right) -> left.add("//TODO")),
    STRUCTURE(9, (world, player, left, right) -> left.add("//TODO"));
    
    private final IInfo info;
    private final Item item;
    private final int meta;
    
    InfoType (int meta, IInfo info) {
        
        this(InfoAccessories.infoItem, meta, info);
    }
    
    InfoType (Item item, IInfo info) {
        
        this(item, 0, info);
    }
    
    InfoType (Item item, int meta, IInfo info) {
    
        this.item = item;
        this.meta = meta;
        this.info = info;
    }
    
    public ItemStack getInfoStack () {
        
        return new ItemStack(item, 1, meta);
    }
    
    public boolean isValidItem(ItemStack stack) {
        
        return StackUtils.areStacksSimilar(stack, this.getInfoStack());
    }
    
    public boolean canPlayerSee(EntityPlayer player) {
        
        return PlayerUtils.playerHasItem(player, this.item, this.meta);
    }
    
    public void getInfo(World world, EntityPlayer player, List<String> info, List<String> debug) {
        
        this.info.applyInfo(world, player, info, debug);
    }
    
    private static void addDateInfo(World world, List<String> infoList) {
        
        final MCDate date = new MCDate(world);
        infoList.add(date.getLocalizedMonthName() + " " + date.getDay() + ", " + date.getYear());
    }
    
    private static void addChunkInfo (World world, EntityPlayer player, List<String> info) {
        
        final Chunk chunk = world.getChunkFromBlockCoords(player.getPosition());
        info.add(String.format("Chunk X: %d y:%d", chunk.x, chunk.z));
    }
}
