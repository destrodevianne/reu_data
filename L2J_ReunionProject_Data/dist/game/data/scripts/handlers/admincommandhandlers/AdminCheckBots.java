package handlers.admincommandhandlers;

import javolution.util.FastList;

import l2r.gameserver.datatables.ClassListData;
import l2r.gameserver.handler.IAdminCommandHandler;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.util.StringUtil;

public class AdminCheckBots implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_check_bots",
		"admin_real_onlines",
		"admin_check_farm_bots",
		"admin_check_enchant_bots"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		int farmBotsCount = 0;
		int enchantBotsCount = 0;
		for (L2PcInstance bots : L2World.getInstance().getAllPlayersArray())
		{
			if (bots.isFarmBot())
			{
				farmBotsCount++;
			}
			if (bots.isEnchantBot())
			{
				enchantBotsCount++;
			}
		}
		
		if (command.startsWith("admin_check_bots"))
		{
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/botsystem.htm");
			
			adminReply.replace("%farmbots%", String.valueOf(farmBotsCount));
			adminReply.replace("%enchantbots%", String.valueOf(enchantBotsCount));
			activeChar.sendPacket(adminReply);
		}
		if (command.startsWith("admin_check_farm_bots"))
		{
			if (farmBotsCount == 0)
			{
				activeChar.sendMessage("There are no currently farm bots. Try again later.");
				return false;
			}
			showBots(activeChar, 0, "farm");
		}
		if (command.startsWith("admin_check_enchant_bots"))
		{
			if (enchantBotsCount == 0)
			{
				activeChar.sendMessage("There are no currently enchant bots. Try again later.");
				return false;
			}
			showBots(activeChar, 0, "enchant");
		}
		
		if (command.startsWith("admin_real_onlines"))
		{
			int counter = 0;
			
			for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayersArray())
			{
				boolean addToList = true;
				if ((activeChar != onlinePlayer) && onlinePlayer.isOnline() && ((onlinePlayer.getClient() != null) && !onlinePlayer.getClient().isDetached()))
				{
					String[] player_Ip = new String[2];
					player_Ip[0] = onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress();
					int[][] trace1 = onlinePlayer.getClient().getTrace();
					for (int o = 0; o < trace1[0].length; o++)
					{
						player_Ip[1] = player_Ip[1] + trace1[0][o];
						if (o != (trace1[0].length - 1))
						{
							player_Ip[1] = player_Ip[1] + ".";
						}
					}
					
					if (getAddedIps() != null)
					{
						for (String[] listIps : getAddedIps())
						{
							if (player_Ip[0].equals(listIps[0]) && player_Ip[1].equals(listIps[1]))
							{
								addToList = false;
							}
						}
					}
					
					if (addToList)
					{
						addToList(onlinePlayer);
						counter++;
					}
				}
			}
			
			if (getAddedIps() != null)
			{
				_dualboxCheck.clear();
			}
			
			activeChar.sendMessage("There are " + counter + " real players.");
		}
		return false;
	}
	
	private void showBots(L2PcInstance activeChar, int page, String type)
	{
		L2PcInstance[] players = L2World.getInstance().getAllPlayersArray();
		
		int maxCharactersPerPage = 20;
		int maxPages = players.length / maxCharactersPerPage;
		
		if (players.length > (maxCharactersPerPage * maxPages))
		{
			maxPages++;
		}
		
		// Check if number of users changed
		if (page > maxPages)
		{
			page = maxPages;
		}
		
		int charactersStart = maxCharactersPerPage * page;
		int charactersEnd = players.length;
		if ((charactersEnd - charactersStart) > maxCharactersPerPage)
		{
			charactersEnd = charactersStart + maxCharactersPerPage;
		}
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		if (type.equals("farm"))
		{
			adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/farmbotlist.htm");
		}
		else if (type.equals("enchant"))
		{
			adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/enchantbotlist.htm");
		}
		
		final StringBuilder replyMSG = new StringBuilder(1000);
		
		for (int x = 0; x < maxPages; x++)
		{
			int pagenr = x + 1;
			if (type.equals("farm"))
			{
				StringUtil.append(replyMSG, "<center><a action=\"bypass -h admin_check_farm_bots ", String.valueOf(x), "\">Page ", String.valueOf(pagenr), "</a></center>");
			}
			else if (type.equals("enchant"))
			{
				StringUtil.append(replyMSG, "<center><a action=\"bypass -h admin_check_enchant_bots ", String.valueOf(x), "\">Page ", String.valueOf(pagenr), "</a></center>");
			}
		}
		
		adminReply.replace("%pages%", replyMSG.toString());
		replyMSG.setLength(0);
		
		for (int i = charactersStart; i < charactersEnd; i++)
		{
			if (type.equals("farm"))
			{
				if (!players[i].isFarmBot())
				{
					continue;
				}
			}
			else if (type.equals("enchant"))
			{
				if (!players[i].isEnchantBot())
				{
					continue;
				}
			}
			
			// What to send, read below to understand
			String typeToSend = null;
			if (type.equals("farm"))
			{
				typeToSend = ClassListData.getInstance().getClass(players[i].getClassId()).getClientCode();
			}
			else if (type.equals("enchant"))
			{
				typeToSend = String.valueOf(players[i].getEnchantChance());
			}
			
			// Add player info into new Table row
			StringUtil.append(replyMSG, "<tr><td width=80><a action=\"bypass -h admin_teleportto ", players[i].getName(), "\">", players[i].getName(), "</a></td><td width=110>", typeToSend, "</td><td width=40>", String.valueOf(players[i].getLevel()), "</td></tr>");
		}
		
		adminReply.replace("%players%", replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	protected static final FastList<String[]> _dualboxCheck = new FastList<>();
	
	// Dual Box Check pcIp based
	protected static boolean addToList(L2PcInstance player)
	{
		String[] player_Ip = new String[2];
		player_Ip[0] = player.getClient().getConnection().getInetAddress().getHostAddress();
		int[][] trace1 = player.getClient().getTrace();
		for (int o = 0; o < trace1[0].length; o++)
		{
			player_Ip[1] = player_Ip[1] + trace1[0][o];
			if (o != (trace1[0].length - 1))
			{
				player_Ip[1] = player_Ip[1] + ".";
			}
		}
		
		_dualboxCheck.add(player_Ip);
		return true;
	}
	
	public FastList<String[]> getAddedIps()
	{
		return _dualboxCheck;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}