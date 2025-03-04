package max.hubbard.bettershops.Versions;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import max.hubbard.bettershops.Utils.WordsCapitalizer;
import net.minecraft.server.v1_10_R1.World;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class EntityCheck {

    public static boolean isNPC(LivingEntity e) {

        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            Class clas = Class.forName("max.hubbard.bettershops.Versions." + version + ".Entities." + WordsCapitalizer.capitalizeEveryWord(e.getType().name().replaceAll("_", " ")).replaceAll(" ","") + "NPC");
            Method m = clas.getSuperclass().getMethod("getBukkitEntity");
            World mcWorld = (World) ((CraftWorld) e.getLocation().getWorld()).getHandle();
            Entity cl = (Entity) m.invoke(clas.getSuperclass().getConstructor(mcWorld.getClass()).newInstance(mcWorld));

            return clas != null && e.getClass().isAssignableFrom(cl.getClass());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
