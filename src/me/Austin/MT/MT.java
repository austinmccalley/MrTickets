package me.Austin.MT;

import me.Austin.MT.GUIs.AdminGUIManager;
import me.Austin.MT.GUIs.GUIManager;
import me.Austin.MT.GUIs.TicketGUIs.SingleTicketGUI;
import me.Austin.MT.GUIs.TicketGUIs.TicketsGUI;
import me.Austin.MT.Managers.ErrorNumGen;
import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.LoginHandlers.LoginHandler;
import me.Austin.MT.Managers.LoginHandlers.SignupHandler;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.Objects.TicketInfo;
import me.Austin.MT.Managers.PMessage;
import me.Austin.MT.Managers.TicketManagers.Ticket;
import me.Austin.MT.Managers.TicketManagers.TicketOpen;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This is the main class for the plugin.
 * This file will <b>NOT</b> be documented until Alpha release!
 *
 * @author MrMcaustin1
 * @since 0.0.1
 */


public class MT extends JavaPlugin {
    public static HashMap<String, String> tPlayers = new HashMap<>();
    public static MT plugin;
    private Logger logger = this.getLogger();

    public void onEnable() {

        if (!getServer().getOnlineMode()) {
            logger.info("We currently DO NOT support offline servers.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        plugin = this;

        logger.info("Setting up MrTickets");

        MySQL.connect();

        Server.createTable();

        Bukkit.getServer().getPluginManager().registerEvents(new Join(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new GUIManager(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AdminGUIManager(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SingleTicketGUI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TicketsGUI(), this);

        Server.setInfo();

        ConsoleLogger.setLogger(getLogger());
        ConsoleLogger.setupConsoleFilter(getLogger());
    }

    public void onDisable() {

        logger.info("Disabling MrTickets");

        closeInv();

        MySQL.disconnect();

        plugin = null;
    }


    private void closeInv() {
        Bukkit.getOnlinePlayers().forEach((p) -> p.closeInventory());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("ticket")) {

            if (!(sender instanceof Player)) {

                int rNumb = ErrorNumGen.newNum();
                sender.sendMessage(
                        "Error #" + rNumb + " has been logged. Please use the /ticket command from in game only.");
                LogToFile.log("warning",
                        "A user has tried using the /ticket command from inside console this feature is not enabled! Error #"
                                + rNumb);
                return false;
            }

            Player p = (Player) sender;

            //Below is the process of sorting through the sub commands.
            //It first checks if there is any arguments and if there isn't then it checks if the player executing the command has the permission "MrTickets.Admin".
            //If the player does it opens the Admin GUI and if they don't it opens the default GUI.
            if (!LoginHandler.isLoggedIn(p)) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("signup")) {
                        if (args.length == 2) {
                            SignupHandler.signUp(p, args[1]);
                        } else {
                            PMessage.Message(p, "Please specify a password to sign up with!", "High");
                        }


                    } else if (args[0].equalsIgnoreCase("login")) {
                        if (args.length == 2) {
                            LoginHandler.login(p, args[1]);
                        } else {
                            PMessage.Message(p, "Please specify a password to login with!", "High");
                        }
                    }

                } else {
                    if (SignupHandler.checkSignUp(p)) {
                        PMessage.Message(p, "Please login before using any MrTickets command! The command is /ticket login <password>", "High");
                    } else {
                        PMessage.Message(p, "Please sign up and then login before using any MrTickets command! The command is /ticket signup <password>", "High");
                    }
                }
            } else {
                if (args.length == 0) {
                    if (p.hasPermission(new Permission("MrTickets.Admin"))) {
                        p.openInventory(AdminGUIManager.defGUI);

                    } else {
                        p.openInventory(GUIManager.defGUI);
                    }
                } else {

                    //If the first argument is help or a question mark it checks if the person has admin privileges.
                    //If so it shows the admin help screen and if they don't it shows the default help screen

                    if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
                        if (p.hasPermission(new Permission("MrTicket.Admin"))) {
                            PMessage.adminHelpMenu(p);
                            return true;
                        } else {
                            PMessage.defHelpMenu(p);
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("def")) {
                        p.openInventory(GUIManager.defGUI);
                    } else if (args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("o")) {
                        boolean contains = tPlayers.containsKey(p.getUniqueId().toString());
                        if (!contains) {
                            int rNumb = ErrorNumGen.newNum();

                            PMessage.Message(p, "You have to click 'Open a Ticket' in the GUI first. Error #" + rNumb, "High");
                            LogToFile.log("warn",
                                    "Tried command /ticket op without going through the GUI. Error #" + rNumb);

                        } else {

                            String numE = tPlayers.get(p.getUniqueId().toString());
                            if (args[1].equals(numE)) {
                                StringBuilder buffer = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    buffer.append(' ').append(args[i]);
                                }
                                String msg = buffer.toString();
                                int num = Integer.valueOf(args[1]);
                                try {
                                    TicketOpen.openTicket(p, num, msg);
                                    tPlayers.remove(p.getUniqueId().toString());
                                    PMessage.Message(p, "Ticket was successfully submitted. Please reference it with #" + num
                                            + " on our website!", "Normal");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    PMessage.stackTrace();
                                }
                            } else {

                                PMessage.Message(p, "That ticket number was wrong. Please try again!", "High");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("id")) {
                        //TODO: Normal and Staff Version
                        if (args[1] != null || (args.length == 2)) {
                            Ticket ticket = TicketInfo.getTicket(Integer.valueOf(args[1]));
                            PMessage.sendTicketInfo(p, ticket);
                        } else {
                            PMessage.Message(p, "Please specify a ticket id!", "High");
                        }
                    } else if (args[0].equalsIgnoreCase("tickets")) {
                        if (p.hasPermission(new Permission("MrTickets.Admin"))) {
                            PMessage.Message(p, "Loading tickets...", "Normal");
                            TicketsGUI.loadItems(1, p);
                            p.openInventory(TicketsGUI.ticketsGUI);
                        } else {
                            PMessage.Message(p, "This is a staff only command!", "High");
                        }
                    } else if (args[0].equalsIgnoreCase("registerserver")) {
                        if (p.isOp()) {
                            PMessage.Message(p, "Server's UUID: " + Server.getSUUID(), "Normal");
                            PMessage.Message(p, "Server's RndWrd: " + Server.getRndWrd(), "Normal");
                        } else {
                            PMessage.Message(p, "You HAVE to be OP to run this command!", "Extreme");
                        }
                    } else if (!(args.length > 1)) {
                        PMessage.Message(p, "That was not a proper /ticket command format! Please do /ticket ? to view all possible formats!", "High");
                    }
                }

            }
        }
        return false;
    }


}