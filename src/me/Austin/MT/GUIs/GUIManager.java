package me.Austin.MT.GUIs;

import me.Austin.MT.*;
import me.Austin.MT.Managers.ErrorNumGen;
import me.Austin.MT.Managers.PMessage;
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Default ticket GUI
 *
 * @author MrMcaustin1
 * @since 1.0
 */
public class GUIManager implements Listener {

    public static Inventory defGUI = Bukkit.createInventory(null, 9, ChatColor.GOLD + "MrTickets");//Create the inventory MrTickets for default users


    //Set all items in here
    static {

        defGUI.setItem(2, newItem(Material.BARRIER, defGUI, 2, ChatColor.RED + "Closed Tickets", "Click me to view your tickets that have been closed!"));
        defGUI.setItem(3, newItem(Material.ANVIL, defGUI, 3, ChatColor.GREEN + "My Active Tickets",
                "Click me to view your active tickets!"));
        defGUI.setItem(4, newItem(Material.STAINED_GLASS_PANE, defGUI, 4, ChatColor.GOLD + "Welcome!",
                "This is your ticket support GUI!"));
        defGUI.setItem(5,
                newItem(Material.EMERALD, defGUI, 5, ChatColor.AQUA + "Open a Ticket", "Click me to open a ticket!"));

    }

    /**
     * Makes the item fancy
     *
     * @param material The material
     * @param inv      The inventory <i>Deprecated</i>
     * @param Slot     The slot <i>Deprecated</i>
     * @param name     The name
     * @param lore     The lore
     * @return The item all fancy like.
     */
    public static ItemStack newItem(Material material, Inventory inv, int Slot, String name, String lore) {
        //The item and the item meta
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        //Set the item name and lore
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);

        //Return the fancy item
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();// The player who clicked
        Inventory inv = e.getInventory(); // The inventory opened

        //Check to see if they are in the default GUI
        if (inv.getName().equals(defGUI.getName())) {

            //Make sure they are clicking an item
            //TODO: Fix this thing that's creating an error when outside of the inv is clicked
            if (!(e.getCurrentItem().getType() == Material.AIR) || !(e.getCurrentItem().getType() == null)) {

                ItemStack item = e.getCurrentItem(); // The item that was clicked
                String itemName = item.getItemMeta().getDisplayName(); // Get item name

                e.setCancelled(true);//Make sure they cant collect any items


                if (itemName.equalsIgnoreCase(ChatColor.AQUA + "Open a Ticket")) {//Open a ticket

                    if (!MT.tPlayers.containsKey(p.getUniqueId().toString())) {//Check to make sure they don't already have a number

                        int rNumb = ErrorNumGen.newNum();//Generate an error number for the user to look at the forums for

                        PMessage.Message(p, "Please type the command /ticket open " + rNumb
                                + " (Insert ticket response here) to submit the ticket.", "Normal");//Message the player how to submit the ticket with the ticket num
                        MT.tPlayers.put(p.getUniqueId().toString(), Integer.toString(rNumb));//Add the player and their ticket number to a list for refrence

                        p.closeInventory();//Close the inventory

                    } else {//Player already has ticket num
                        String tNum = MT.tPlayers.get(p.getUniqueId().toString());//Get ticket num from list
                        PMessage.Message(p, "You were already assigned a ticket number it is " + tNum + ".", "Normal");//Send player the ticket num
                    }

                } else if (itemName.equalsIgnoreCase(ChatColor.GREEN + "My Active Tickets")) {//Active Tickets

                    @SuppressWarnings("unchecked")
                    Map<Integer, Long> map = ActiveTickets.myActiveTickets(p);//Set a local map with the active tickets

                    for (Entry<Integer, Long> mapEntry : map.entrySet()) {//For each entry

                        Long dateSub = mapEntry.getValue();//Get the date submitted
                        long cTime = System.currentTimeMillis();//Get the current date
                        long tSince = cTime - dateSub;//Get the time difference
                        PMessage.Message(p, "Ticket ID #" + mapEntry.getKey() + " submitted  "
                                + ActiveTickets.getDurationBreakdown(tSince) + " minutes ago", "Normal");//Send the player the ticket id(key) and the time difference
                    }

                    map.clear();//Clear local map
                    ActiveTickets.mAT.clear();//Clear mAT

                    p.closeInventory();//Close inv


                } else if (itemName.equalsIgnoreCase(ChatColor.GOLD + "Welcome!")) {//Welcome!
                    //Just make sure it doesn't do anything stupid
                    //Maybe: Do something on welcome sign! Website or some sort of configurable option

                } else if (itemName.equalsIgnoreCase(ChatColor.RED + "Closed Tickets")) {//Closed tickets

                    PMessage.Message(p, "Ticket #'s " + ClosedTicketHandler.defClosedTickets(p).toString().replace("[", "").replace("]", ""), "Normal");//Send the player all the tickets that have been closed that they submitted

                } else {

                    PMessage.Message(p, "This has not yet been implemented.", "High");//Just incase something endsup in the inventory that shouldn't be there.
                }
            }
        }
    }
}
