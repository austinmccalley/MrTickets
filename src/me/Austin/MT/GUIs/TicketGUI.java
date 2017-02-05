package me.Austin.MT.GUIs;

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
public class TicketGUI implements Listener {

    //Create the Ticket Inv
    public static Inventory ticketGUI = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Ticket");

    static {

        ticketGUI.setItem(4, AdminGUIManager.newItem(Material.EMPTY_MAP, "Ticket Info", "Click me to view the ticket info!"));
        ticketGUI.setItem(19, AdminGUIManager.newItem(Material.EMERALD, ChatColor.GREEN + "Escalate Ticket", "Click me to escalate the ticket!"));
        ticketGUI.setItem(22, AdminGUIManager.newItem(Material.BARRIER, ChatColor.RED + "Close Ticket", "Click me to close the ticket!"));
        ticketGUI.setItem(25, AdminGUIManager.newItem(Material.REDSTONE, ChatColor.DARK_RED + "Assign New Staff Member ", "Click me to assign a new staff member to this ticket."));

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
                    //Return ticket info

                } else if (itemName.equalsIgnoreCase("Escalate Ticket")) {
                    //Escalate Ticket

                } else if (itemName.equalsIgnoreCase("Close Ticket")) {
                    //Close the ticket

                } else if (itemName.equalsIgnoreCase("Assign New Staff Member")) {
                    //Assign a new staff member

                }

            }

        }


    }


}
