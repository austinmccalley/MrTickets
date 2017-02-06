package me.Austin.MT.GUIs.TicketGUIs;

import me.Austin.MT.GUIs.AdminGUIManager;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.Objects.TicketInfo;
import me.Austin.MT.Managers.TicketManagers.Ticket;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by mcaus_000 on 2/4/2017.
 */
public class TicketsGUI implements Listener {

    //Create the Ticket Inv
    public static Inventory ticketsGUI = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Tickets");
    static int pageT = 0;
    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";

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
    public static ItemStack newItem(Material material, String name, String lore, int color, int amt) {
        //The item and the item meta
        ItemStack item = new ItemStack(material, amt, (short) color);
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

    public static void loadItems(int page, Player p) {
        pageT = page;


        try {
            String sql;
            Statement statement = MySQL.getConnection().createStatement();
            ticketsGUI.clear();

            for (int q = 0; q < 54; q++) {
                ticketsGUI.clear(q);
            }

            int tempPage = page;

            int pageLimit = (tempPage * 54) - (9 * tempPage);

            page = tempPage * 54;


            if (pageLimit <= 45) {
                sql = "SELECT * FROM " + tickets + " WHERE ticketID<=" + pageLimit + ";";
            } else {
                int secPageLimit = pageLimit - 54;
                int firstPageLimit = pageLimit + 2;
                sql = "SELECT * FROM " + tickets + " WHERE ticketID<=" + firstPageLimit + " AND ticketID>=" + secPageLimit + ";";
            }


            ResultSet rs = statement.executeQuery(sql);


            ticketsGUI.setItem(53, AdminGUIManager.newItem(Material.ARROW, ChatColor.GRAY + "Next Page", "Click me to go the next page."));
            ticketsGUI.setItem(45, AdminGUIManager.newItem(Material.ARROW, ChatColor.GRAY + "Previous Page", "Click me to go the previous page."));

            int i;
            int j = 0;
            i = (54 - 9) * (tempPage - 1);

            while (rs.next()) {
                if (i < pageLimit) {

                    Ticket ticket = TicketInfo.getTicket(rs.getInt("TicketID"));

                    //How many responses.
                    int amt = 1;

                    //Color if player is assigned the ticket
                    int col = 0;

                    switch (Ticket.priority) {
                        case "Normal":
                            col = 5;
                            break;
                        case "High":
                            col = 2;
                            break;
                        case "Extreme":
                            col = 14;
                            break;
                    }

                    if (!Ticket.completed.equalsIgnoreCase("Open")) {
                        col = 15;
                    }

                    String uuid = p.getUniqueId().toString();
                    if (Ticket.assigned.equals(uuid)) {
                        col = 4;
                    }


                    ticketsGUI.setItem(j, newItem(Material.STAINED_CLAY, "Ticket #" + Ticket.ticketID, "Click me to view the ticket!", col, amt));


                }
                i++;
                j++;
            }

        } catch (
                SQLException e)

        {
            e.printStackTrace();
        }


    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (inv.getName().equals(ticketsGUI.getName()) && e.getClickedInventory() != null) {

            if (e.getCurrentItem().getAmount() != 0) {

                ItemStack item = e.getCurrentItem();

                String prePreItemName = item.getItemMeta().getDisplayName();
                String noColorName = ChatColor.stripColor(prePreItemName);


                String tID = noColorName.replace("Ticket #", "");

                String pattern = "-?\\d+";


                e.setCancelled(true);
                if (tID.matches(pattern)) {
                    int id = Integer.parseInt(tID);


                    Ticket ticket = TicketInfo.getTicket(id);

                    SingleTicketGUI.loadInventory(ticket);

                    p.closeInventory();

                    p.openInventory(SingleTicketGUI.ticketGUI);
                } else if (noColorName.equalsIgnoreCase("Next Page")) {
                    loadItems(pageT + 1, p);
                    p.closeInventory();
                    p.openInventory(ticketsGUI);
                } else if (noColorName.equalsIgnoreCase("Previous Page")) {
                    loadItems(pageT - 1, p);
                    p.closeInventory();
                    p.openInventory(ticketsGUI);
                }


            }

        }


    }
}

