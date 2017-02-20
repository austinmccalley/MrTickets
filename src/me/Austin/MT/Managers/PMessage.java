package me.Austin.MT.Managers;

import me.Austin.MT.Managers.TicketManagers.Ticket;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.sql.Date;
import java.util.UUID;

/**
 * This class is intended to be use is to format messages to
 * send to players. No messages shall be sent through console using this.
 *
 * @author MrMcaustin1
 * @since 0.0.1
 * <p>
 * TODO: SQLException stack trace handler
 * </p>
 */
public class PMessage {

    /**
     * Message Method - Send formatted message to player
     *
     * @param p
     *         Player the message is sent to
     * @param msg
     *         Message the player is sent
     * @param level
     *         Level of severity you want the message sent with(Normal,High,Extreme)
     */
    public static void Message(Player p, String msg, String level) {

        switch (level) {
            case "High":
                TextComponent hMSG = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.RED + msg);
                p.spigot().sendMessage(hMSG);
                break;
            case "Extreme":
                TextComponent eMSG = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.DARK_RED + msg);
                p.spigot().sendMessage(eMSG);
                break;
            case "Normal":
                TextComponent nMSG = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + msg);
                p.spigot().sendMessage(nMSG);
                break;
            default:
                TextComponent fMSG = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + msg);
                p.spigot().sendMessage(fMSG);
                break;
        }


    }

    public static void sendTicketInfo(Player p, Ticket ticket) {
        int ticketId = Ticket.ticketID;
        String uuid = Ticket.tUUID;

        String submitted = Bukkit.getPlayer(UUID.fromString(uuid)).getName();

        Date date = Ticket.date;
        String msg = Ticket.msg;


        Message(p, "Ticket #" + ticketId + " submitted by " + submitted + " on " + date + " " + " with the message; \n" + msg, "Normal");
    }


    public static void sendAdminMessage(String msg) {
        for (Player p1 : Bukkit.getOnlinePlayers()) {
            if (p1.hasPermission("MrTickets.Admin")) {
                PMessage.Message(p1, msg, "High");
            }

        }
    }

    /**
     * Easier Stack Trace Handling
     */
    public static void stackTrace() {
        Bukkit.getLogger().severe("There was a severe error in the plugin!");
        LogToFile.log("SEVERE", "There was a severe error in the plugin!");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(new Permission("MrTickets.Admin"))) {
                p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT Warning" + ChatColor.GRAY + "] " + ChatColor.RED + "There was a severe error in the plugin!");
            }
        }
    }

    /**
     * The admin help menu
     *
     * @param p
     *         The player who is requesting the admin help menu
     */
    public static void adminHelpMenu(Player p) {
        TextComponent line1 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "+-+-+ Admin Help Menu +-+-+");

        TextComponent line2 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket help - Shows this help menu");
        line2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket ?"));
        line2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run /ticket ? command").create()));


        TextComponent line3 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket - Opens admin ticket GUI");
        line3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket"));
        line3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the Admin Ticket GUI").create()));


        TextComponent line4 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket def - Opens default GUI");
        line4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket def"));
        line4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the default Ticket GUI").create()));

        p.spigot().sendMessage(line1);
        p.spigot().sendMessage(line2);
        p.spigot().sendMessage(line3);
        p.spigot().sendMessage(line4);
    }

    /**
     * The default player help menu
     *
     * @param p
     *         The player who is requesting the default help menu
     */
    public static void defHelpMenu(Player p) {
        TextComponent line1 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "+-+-+ Help Menu +-+-+");

        TextComponent line2 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket help - Shows this help menu");
        line2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket ?"));
        line2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run /ticket ? command").create()));


        TextComponent line3 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket - Opens ticket GUI");
        line3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket"));
        line3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the Ticket GUI").create()));


        TextComponent line4 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket signup - Creates an account to use on the website");
        line4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket signup"));
        line4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "!!BETA FEATURE!!" + ChatColor.LIGHT_PURPLE + " Create an account!").create()));

        p.spigot().sendMessage(line1);
        p.spigot().sendMessage(line2);
        p.spigot().sendMessage(line3);
        p.spigot().sendMessage(line4);
    }


}
