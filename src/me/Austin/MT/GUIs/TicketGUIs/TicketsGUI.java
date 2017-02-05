package me.Austin.MT.GUIs.TicketGUIs;

import me.Austin.MT.GUIs.AdminGUIManager;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.PMessage;
import me.Austin.MT.TicketManagers.Ticket;
import me.Austin.MT.TicketManagers.TicketInfo;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mcaus_000 on 2/4/2017.
 */
public class TicketsGUI implements Listener {


    //Create the Ticket Inv
    public static Inventory ticketsGUI = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Tickets");
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
    public static ItemStack newItem(Material material, String name, String lore, int color) {
        //The item and the item meta
        ItemStack item = new ItemStack(material, 1, (short) color);
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
        Date before = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:ss");


        try {

            ticketsGUI.clear();

            for (int q = 0; q < 54; q++) {
                ticketsGUI.clear(q);
            }

            int tempPage = page;

            int pageLimit = (tempPage * 54) - 9;

            page = tempPage * 54;


            String sql;
            Statement statement = MySQL.getConnection().createStatement();

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

                    if (Ticket.priority.equalsIgnoreCase("Normal") && Ticket.completed.equalsIgnoreCase("Open")) {
                        ticketsGUI.setItem(j, newItem(Material.STAINED_CLAY, ChatColor.GREEN + "Ticket #" + Ticket.ticketID, "Click me to view the ticket!", 5));
                    } else if (Ticket.priority.equalsIgnoreCase("High") && Ticket.completed.equalsIgnoreCase("Open")) {
                        ticketsGUI.setItem(j, newItem(Material.STAINED_CLAY, ChatColor.LIGHT_PURPLE + "Ticket #" + Ticket.ticketID, "Click me to view the ticket!", 2));

                    } else if (Ticket.priority.equalsIgnoreCase("Extreme") && Ticket.completed.equalsIgnoreCase("Open")) {
                        ticketsGUI.setItem(j, newItem(Material.STAINED_CLAY, ChatColor.DARK_RED + "Ticket #" + Ticket.ticketID, "Click me to view the ticket!", 14));

                    } else if (!Ticket.completed.equalsIgnoreCase("Open")) {
                        ticketsGUI.setItem(j, newItem(Material.STAINED_CLAY, ChatColor.BLACK + "Ticket #" + Ticket.ticketID, "Click me to view the ticket!", 15));

                    }
                }
                i++;
                j++;
            }

            Date now = new Date();

            long milliBefore = before.getTime();
            long milliNow = now.getTime();

            long diff = milliNow - milliBefore;
            double seconds = diff / 1000.0;

            double avg = seconds / pageLimit;
//            System.out.println(diff);
//            System.out.println(seconds);

            PMessage.Message(p, "That query took " + seconds + " to complete! Averaging " + avg + " seconds per ticket!", "Normal");

        } catch (SQLException e) {
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
                    loadItems(2, p);
                    p.closeInventory();
                    p.openInventory(ticketsGUI);
                } else if (noColorName.equalsIgnoreCase("Previous Page")) {
                    loadItems(1, p);
                    p.closeInventory();
                    p.openInventory(ticketsGUI);
                }


            }

        }


    }
}

