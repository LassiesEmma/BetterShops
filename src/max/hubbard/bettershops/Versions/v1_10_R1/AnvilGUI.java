package max.hubbard.bettershops.Versions.v1_10_R1;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import max.hubbard.bettershops.Core;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.ContainerAnvil;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenWindow;

public class AnvilGUI implements max.hubbard.bettershops.Versions.AnvilGUI {
    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    private class EightAnvilContainer extends ContainerAnvil {
        public EightAnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    public enum AnvilSlot {
        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private int slot;

        private AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }

            return null;
        }
    }

    public class AnvilClickEvent extends max.hubbard.bettershops.Versions.AnvilGUI.AnvilClickEvent {
        private AnvilSlot slot;

        private String name;

        private boolean close = true;
        private boolean destroy = true;

        private ItemStack item;
        private HumanEntity entity;

        public AnvilClickEvent(AnvilSlot slot, String name, ItemStack it, HumanEntity ent) {
            super();
            this.slot = slot;
            this.name = name;
            this.item = it;
            this.entity = ent;
        }


        public int getSlot() {
            if (slot != null) {
                return slot.getSlot();
            } else {
                return 0;
            }
        }

        public ItemStack getCurrentItem() {
            return item;
        }

        public HumanEntity getWhoClicked() {
            return entity;
        }

        public String getName() {
            return name;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    public interface AnvilClickEventHandler extends max.hubbard.bettershops.Versions.AnvilGUI.AnvilClickEventHandler {
        public void onAnvilClick(AnvilClickEvent event);

        @Override
        void onAnvilClick(max.hubbard.bettershops.Versions.AnvilGUI.AnvilClickEvent ev);
    }

    public AnvilGUI() {

    }

    private Player player;

    private max.hubbard.bettershops.Versions.AnvilGUI.AnvilClickEventHandler handler;

    private HashMap<max.hubbard.bettershops.Versions.AnvilGUI.AnvilSlot, ItemStack> items = new HashMap<max.hubbard.bettershops.Versions.AnvilGUI.AnvilSlot, ItemStack>();

    private Inventory inv;

    private Listener listener;


    public AnvilGUI(Player player, final AnvilClickEventHandler handler) {
        this.player = player;
        this.handler = handler;

        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    Player clicker = (Player) event.getWhoClicked();

                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name, event.getCurrentItem(), event.getWhoClicked());

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose()) {
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    Inventory inv = event.getInventory();

                    if (inv.equals(AnvilGUI.this.inv)) {
                        inv.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    destroy();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, Core.getCore()); //Replace with instance of main class
    }

    @Override
    public void doGUIThing(final Player player, final max.hubbard.bettershops.Versions.AnvilGUI.AnvilClickEventHandler handler) {

        this.player = player;
        this.handler = handler;
        final float exp = player.getExp();
        final int level = player.getLevel();
        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    Player clicker = (Player) event.getWhoClicked();

                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name, event.getCurrentItem(), event.getWhoClicked());

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose()) {
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy()) {
                            destroy();
                        }
                        player.setExp(exp);
                        player.setLevel(level);
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    Inventory inv = event.getInventory();

                    if (inv.equals(AnvilGUI.this.inv)) {
                        inv.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    destroy();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, Core.getCore()); //Replace with instance of main class
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void setSlot(max.hubbard.bettershops.Versions.AnvilGUI.AnvilSlot slot, ItemStack item) {
        if (items == null) {
            items = new HashMap<max.hubbard.bettershops.Versions.AnvilGUI.AnvilSlot, ItemStack>();
        }
        if (slot != null && item != null)
            items.put(slot, item);
    }

    public void open() {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        //Set the items to the items from the inventory given
        inv = container.getBukkitView().getTopInventory();

        if (items == null) {
            items = new HashMap<max.hubbard.bettershops.Versions.AnvilGUI.AnvilSlot, ItemStack>();
        }
        for (max.hubbard.bettershops.Versions.AnvilGUI.AnvilSlot slot : items.keySet()) {
            inv.setItem(slot.getSlot(), items.get(slot));
        }


        //Counter stuff that the game uses to keep track of inventories
        int c = p.nextContainerCounter();

        //Send the packet

        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatComponentText("Repairing")));


        //Set their active container to the container
        p.activeContainer = container;

        //Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        //Add the slot listener
        p.activeContainer.addSlotListener(p);

    }

    public void destroy() {
        player = null;
        handler = null;
        items = null;

        if (listener != null) {

            HandlerList.unregisterAll(listener);
        }

        listener = null;
    }
}
