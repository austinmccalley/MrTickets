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
        //Title
        TextComponent line1 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "+-+-+ Admin Help Menu +-+-+");

        //Help
        TextComponent line2 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket help - Shows this help menu");
        line2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket ?"));
        line2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run /ticket ? command").create()));

        //Admin GUI
        TextComponent line3 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket - Opens admin ticket GUI");
        line3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket"));
        line3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the Admin Ticket GUI").create()));

        //Default GUI
        TextComponent line4 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket def - Opens default GUI");
        line4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket def"));
        line4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the default Ticket GUI").create()));

        //Register Server
        TextComponent line5 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket registerserver - Shows the Server's UUID and random word to register the server");
        line5.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket registerserver"));
        line5.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket registerserver").create()));

        //Signup Server
        TextComponent line6 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket signup <password> - Sign up command");
        line6.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket signup"));
        line6.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket signup").create()));

        //Login Server
        TextComponent line7 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket login <password> - Login command");
        line7.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket login"));
        line7.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket login").create()));

        //Ticket ID
        TextComponent line8 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket id <ticket id> - Opens a GUI which contains information regarding the ticket");
        line8.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket id"));
        line8.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket id").create()));

        //Tickets
        TextComponent line9 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket tickets - Opens tickets GUI containg all the tickets");
        line9.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket tickets"));
        line9.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the tickets GUI").create()));

        //Open
        TextComponent line10 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket open <random number> <Message> - Open a ticket with a supplied random number");
        line10.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket open"));
        line10.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket open").create()));

        p.spigot().sendMessage(line1);
        p.spigot().sendMessage(line2);
        p.spigot().sendMessage(line3);
        p.spigot().sendMessage(line4);
        p.spigot().sendMessage(line5);
        p.spigot().sendMessage(line6);
        p.spigot().sendMessage(line7);
        p.spigot().sendMessage(line8);
        p.spigot().sendMessage(line9);
        p.spigot().sendMessage(line10);
    }

    /**
     * The default player help menu
     *
     * @param p
     *         The player who is requesting the default help menu
     */
    public static void defHelpMenu(Player p) {
        //Title
        TextComponent line1 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "+-+-+ Help Menu +-+-+");

        //Help
        TextComponent line2 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket help - Shows this help menu");
        line2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket ?"));
        line2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run /ticket ? command").create()));

        //Def GUI
        TextComponent line3 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket - Opens ticket GUI");
        line3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket"));
        line3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Open the Ticket GUI").create()));

        //Signup
        TextComponent line4 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket signup - Creates an account to use on the website");
        line4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket signup"));
        line4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + " Create an account!").create()));

        //Login Server
        TextComponent line7 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket login <password> - Login command");
        line7.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket login"));
        line7.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket login").create()));

        //Open
        TextComponent line5 = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "MrT" + ChatColor.GRAY + "] " + ChatColor.WHITE + "/ticket open <random number> <Message> - Open a ticket with a supplied random number");
        line5.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ticket open"));
        line5.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Run the command /ticket open").create()));


        p.spigot().sendMessage(line1);
        p.spigot().sendMessage(line2);
        p.spigot().sendMessage(line3);
        p.spigot().sendMessage(line4);
        p.spigot().sendMessage(line7);
        p.spigot().sendMessage(line5);
    }


}
