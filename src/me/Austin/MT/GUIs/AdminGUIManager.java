package me.Austin.MT.GUIs;

import me.Austin.MT.GUIs.TicketGUIs.TicketsGUI;
import me.Austin.MT.Managers.PMessage;
import me.Austin.MT.TicketManagers.AssignTickets;
import me.Austin.MT.TicketManagers.ClosedTicketHandler;
import me.Austin.MT.TicketManagers.RecentTickets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Created by MrMcaustin1 on 12/29/2016.
 * <p>
 * This class manages the Admin Ticket GUI
 *
 * @author MrMcaustin1
 * @since 1.0
 */
public class AdminGUIManager implements Listener {

    //Create the Admin GUI Inventory
    public static Inventory defGUI = Bukkit.createInventory(null, 9, ChatColor.GOLD + "MrTickets Admin");

    //Set the items inside the Admin GUI
    static {
        defGUI.setItem(2, newItem(Material.SAPLING, ChatColor.BLUE + "Recent Tickets",
                "Click me to view tickets recently submitted"));
        defGUI.setItem(3, newItem(Material.WOOL, ChatColor.GREEN + "My Assigned Tickets",
                "Click me to view your assigned tickets"));
        defGUI.setItem(4, newItem(Material.THIN_GLASS, ChatColor.AQUA + "Welcome!",
                "Currently there has been " + AssignTickets.totalTickets() + " total tickets submitted!"));
        defGUI.setItem(5, newItem(Material.REDSTONE_BLOCK, ChatColor.AQUA + "Important Tickets",
                "Click me to view important tickets"));
        defGUI.setItem(6, newItem(Material.BARRIER, ChatColor.RED + "Closed Tickets", "Tickets that you have closed!"));
    }


    /**
     * @param material
     *         The material you want
     * @param name
     *         Item name
     * @param lore
     *         Item lore
     *
     * @return The item all fancy like
     */
    public static ItemStack newItem(Material material, String name, String lore) {
        //The item and the item meta
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        //Set the item name and lore
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);

        //Return the fancy item
        return item;
    }


    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();// The player who clicked
        Inventory inv = e.getInventory(); // The inventory opened

        //Checks if the inv is the Admin GUI
        if (inv.getName().equals(defGUI.getName()) && e.getClickedInventory() != null) {

            //Make sure they are clicking an item
            if (e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getType() != null && e.getCurrentItem().getAmount() != 0) {

                ItemStack item = e.getCurrentItem(); // The item that is clicked

                String preitemName = item.getItemMeta().getDisplayName(); // Get item name
                String itemName = ChatColor.stripColor(preitemName);//Remove all the fancy colors
                e.setCancelled(true); // Prevent collection of items

                if (itemName.equalsIgnoreCase("Important Tickets")) {//Important Tickets
                    PMessage.Message(p, ChatColor.RED + "This feature will be implemented in version 0.1.0", "Normal");
                    // PMessage.Message(p, ImportantTickets.getImportantTickets().toString().replace("[", "").replace("]", "").replace(",", "\n"), "Normal");//Get a list of all the important tickets and remove the brackets and commas

                } else if (itemName.equalsIgnoreCase("My Assigned Tickets")) {//Assigned Tickets


                    Map<Integer, String> map = AssignTickets.assignedTickets(p);//Set a local map of the assigned tickets of the player


                    for (Map.Entry<Integer, String> mapEntry : map.entrySet()) {//for each entry in the map

                        PMessage.Message(p, "Ticket ID #" + mapEntry.getKey() + " submitted by "
                                + Bukkit.getPlayer(UUID.fromString(mapEntry.getValue())).getName(), "Normal"); //Message the player its key(ticketID) and its value(Player Name)

                    }

                    AssignTickets.aTickets.clear();//Clear aTickets to remove duplicates or interference
                    map.clear(); //Clear local map


                } else if (itemName.equalsIgnoreCase("Recent Tickets")) {//Recent Tickets
                    //TODO: RecentTickets.recentTickets(p) add try and catch statement in there

                    try {

                        Map<Integer, String> map = RecentTickets.recentTickets(p);//Set a local map of the recent tickets

                        for (Map.Entry<Integer, String> mapEntry : map.entrySet()) {//For each entry in the map

                            PMessage.Message(p, "Ticket ID #" + mapEntry.getKey() + " submitted by "
                                    + Bukkit.getPlayer(UUID.fromString(mapEntry.getValue())).getName(), "Normal");//Message the player the key(ticketID) and its value(Player Name)

                        }

                        RecentTickets.rTickets.clear();//Clear rTickets of all entries
                        map.clear();//Clear local map

                    } catch (SQLException e2) {
                        e2.printStackTrace();
                        PMessage.stackTrace();
                    }
                } else if (itemName.equalsIgnoreCase("Welcome!")) {//If the welcome sign is clicked
                    TicketsGUI.loadItems(1, p);
                    p.closeInventory();
                    p.openInventory(TicketsGUI.ticketsGUI);
                } else if (itemName.equalsIgnoreCase("Closed Tickets")) { //Closed Tickets
                    Map<Integer, Date> map = ClosedTicketHandler.adminClosedTickets(p);//Set a local map of tickets closed by that user
                    for (Map.Entry<Integer, Date> mapEntry : map.entrySet()) {//For each entry in the map
                        PMessage.Message(p, "Ticket ID #" + mapEntry.getKey() + " submitted on " + mapEntry.getValue(), "Normal");//Message the player the key(ticketID) and its value(Date)
                    }
                    map.clear();//clear local map
                } else {
                    PMessage.Message(p, "This feature is coming soon!", "High");//Just in case an item gets into the inventory and we don't know what it is.
                }

            }
        }

    }

}
