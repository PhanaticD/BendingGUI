package com.strangeone101.bendinggui.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.command.PKCommand;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.Config;
import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import com.strangeone101.bendinggui.menus.MenuSelectElement;

public class GuiCommand extends PKCommand
{
	public GuiCommand() 
	{
		super("gui", "/bending gui [help/version/reload]", "Opens a menu that allows users to configure bending without commands", new String[] {"gui", "menu"});
	}
	
	public void execute(CommandSender arg0, List<String> arg1) 
	{
		executeCommand(arg0, arg1);		
	}
	
	public static boolean executeCommand(CommandSender sender, List<String> args)
	{
		if (!(sender instanceof Player) && args.size() == 0)
		{
			sender.sendMessage(ChatColor.YELLOW + "Command usage from console is /gui <version/reload>");
			return true;
		}
		
		if (!sender.hasPermission("bendinggui.command"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			return true;
		}

		if (args.size() == 0 && sender instanceof Player)
		{
			Player player = (Player) sender;
			if (Config.guiRequireItem)
			{
				if (player.getInventory().contains(BendingGUI.getGuiItem()))
				{
					player.getInventory().remove(BendingGUI.getGuiItem());
				}
				player.getInventory().addItem(BendingGUI.getGuiItem());
				player.sendMessage(ChatColor.GREEN + Config.getGiveMessage);
			}
			else
			{
				MenuBendingOptions menu = new MenuBendingOptions(player);
				menu.openMenu(player);
			}
			
			return true;
		}
		else if ((args.get(0).equalsIgnoreCase("choose") || args.get(0).equalsIgnoreCase("c") || args.get(0).equalsIgnoreCase("ch")) && sender instanceof Player)
		{
			Player player = (Player) sender;
			if (args.size() == 2)
			{
				if (player.hasPermission("bending.admin.choose"))
				{
					Player playero = Bukkit.getPlayer(args.get(1));
					if (playero != null)
					{
						MenuSelectElement menu = new MenuSelectElement(playero);
						menu.openMenu(player);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Error while finding player!");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "You don't have permission to choose other players element!");
				}
				return true;
			}
			if (!player.hasPermission("bending.command.choose"))
			{
				player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			}
			else if (BendingPlayer.getBendingPlayer(player).getElements().size() == 0)
			{
				MenuSelectElement menu = new MenuSelectElement(player);
				menu.openMenu(player);
			}
			else if (player.hasPermission("bending.command.rechoose"))
			{
				MenuSelectElement menu = new MenuSelectElement(player);
				menu.openMenu(player);
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You have already chosen an element!");
			}

		}
		else if (args.get(0).equalsIgnoreCase("version") || args.get(0).equalsIgnoreCase("v") || args.get(0).equalsIgnoreCase("ver"))
		{
            if (sender.hasPermission("bendinggui.version"))
            {
                sender.sendMessage(ChatColor.YELLOW + "BendingGUI is version " + BendingGUI.INSTANCE.getDescription().getVersion() + ", running on ProjectKorra " + ProjectKorra.plugin.getDescription().getVersion());
                if (!BendingGUI.versionInfo.equals("")) {
                	sender.sendMessage(ChatColor.RED + "Support for this version of ProjectKorra is not guaranteed.");
                }
                return true;
            }
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
		}
        else if (args.get(0).equalsIgnoreCase("reload") || args.get(0).equalsIgnoreCase("r") || args.get(0).equalsIgnoreCase("rel"))
        {
            if (sender.hasPermission("bendinggui.reload"))
            {
                BendingGUI.INSTANCE.reload();
                sender.sendMessage(ChatColor.YELLOW + "BendingGUI Reloaded!");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
        }
		else if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Player playero = Bukkit.getPlayer(args.get(0));
			if (playero == null && sender.hasPermission("bendinggui.admin"))
			{
				sender.sendMessage(ChatColor.RED + "Error while finding player!");
				return true;
			}
			else if (playero != null && !sender.hasPermission("bendinggui.admin"))
			{
				sender.sendMessage(ChatColor.RED + "You don't have permission to edit other players bending!");
			}
			else if (playero == null)
			{
				sender.sendMessage(ChatColor.RED + "Command usage is /gui or /gui <choose/version/reload>");
			}
			else
			{
				MenuBendingOptions menu = new MenuBendingOptions(playero);
				menu.openMenu(player);
			}
			return true;
			
		} else {
			sender.sendMessage(ChatColor.YELLOW + "Command usage is /gui or /gui <choose/version/reload> or /gui [player]");
		}
		return true;
	}
}
