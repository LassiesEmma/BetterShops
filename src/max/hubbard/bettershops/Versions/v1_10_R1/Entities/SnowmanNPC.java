package max.hubbard.bettershops.Versions.v1_10_R1.Entities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSnowman;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_10_R1.DamageSource;
import net.minecraft.server.v1_10_R1.EntitySnowman;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.World;
import net.minecraft.server.v1_10_R1.WorldServer;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SnowmanNPC extends EntitySnowman{
    public SnowmanNPC(World world) {
        super(world);
    }


    @Override
    public void move(double d0, double d1, double d2) {
        return;
    }
         @Override
     public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void g(double x, double y, double z) {
            }


    public static Snowman spawn(Location location) {

        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        for (WorldServer ws : server.worlds){
            if (ws.getWorld().getName().equals(location.getWorld().getName())){
                world = ws;
                break;
            }
        }
        World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();
        final SnowmanNPC customEntity = new SnowmanNPC(
                world);
        customEntity.setLocation(location.getX(), location.getY(),
                location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) customEntity.getBukkitEntity())
                .setRemoveWhenFarAway(false);
        mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (CraftSnowman) customEntity.getBukkitEntity();
    }
}
