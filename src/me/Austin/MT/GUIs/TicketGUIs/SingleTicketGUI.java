package me.Austin.MT.GUIs.TicketGUIs;

import me.Austin.MT.GUIs.AdminGUIManager;
import me.Austin.MT.Managers.PMessage;
import me.Austin.MT.Managers.TicketManagers.AssignTickets;
import me.Austin.MT.Managers.TicketManagers.EscalateTicket;
import me.Austin.MT.Managers.TicketManagers.Ticket;
import me.Austin.MT.Managers.TicketManagers.TicketClosing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by mcaus_000 on 2/4/2017.
 */
public class SingleTicketGUI implements Listener {

    public static Ticket ticket;


    //Create the Ticket Inv
    public static Inventory ticketGUI = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Ticket");

    public static void loadInventory(Ticket inTicket) {

        ticket = inTicket;

        ticketGUI.setItem(0, AdminGUIManager.newItem(Material.ARROW, ChatColor.GRAY + "Go Back", "Click me to go back!"));

        ticketGUI.setItem(4, AdminGUIManager.newItem(Material.EMPTY_MAP, "Ticket Info", "Click me to view the ticket info!"));
        ticketGUI.setItem(19, AdminGUIManager.newItem(Material.EMERALD, ChatColor.GREEN + "Escalate Ticket", "Click me to escalate the ticket!"));
        ticketGUI.setItem(22, AdminGUIManager.newItem(Material.BARRIER, ChatColor.RED + "Toggle Ticket", "Click me to either open or close the ticket!"));
        ticketGUI.setItem(25, AdminGUIManager.newItem(Material.REDSTONE, ChatColor.DARK_RED + "Assign New Staff Member", "Click me to assign a new staff member to this ticket."));


    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (inv.getName().equals(ticketGUI.getName()) && e.getClickedInventory() != null) {

            if (e.getCurrentItem().getAmount() != 0) {

                ItemStack item = e.getCurrentItem();

                String preItemName = item.getItemMeta().getDisplayName();
                String itemName = ChatColor.stripColor(preItemName);
                e.setCancelled(true);

                if (itemName.equalsIgnoreCase("Ticket Info")) {
                    //Send the player all the ticket information
                    p.closeInventory();
                    PMessage.sendTicketInfo(p, ticket);
                } else if (itemName.equalsIgnoreCase("Escalate Ticket")) {
                    //Escalate Ticket
                    EscalateTicket.escalateTicket(ticket);
                    PMessage.Message(p, "Escalated ticket #" + Ticket.ticketID, "Normal");
                } else if (itemName.equalsIgnoreCase("Toggle Ticket")) {
                    //Close the ticket
                    TicketClosing.closeTicket(ticket, p);

                } else if (itemName.equalsIgnoreCase("Assign New Staff Member")) {
                    //Assign a new staff member
                    AssignTickets.assignTicket(p, true);
                } else if (itemName.equalsIgnoreCase("Go Back")) {
                    p.closeInventory();
                    TicketsGUI.loadItems(1, p);
                    p.openInventory(TicketsGUI.ticketsGUI);
                }

            }

        }


    }


    /*

                    int i = Integer.valueOf(args[1]);
                    Ticket ticket = TicketInfo.getTicket(i);
                    PMessage.Message(p, "Ticket: ID #" + ticket.ticketID + " from " + Bukkit.getPlayer(UUID.fromString(ticket.tUUID)).getName() + " on " + ticket.date + " with a priority of " + ticket.priority + " With the following message; \n" + ticket.msg, "Normal");



     */


}
