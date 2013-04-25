/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import gr.reunion.configs.CustomServerConfigs;
import javolution.text.TextBuilder;

import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class CcpVCmd implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"ccp",
		"nobuff",
		"changeexp",
		"enchantanime",
		"hidestores",
		"blockshotsanime",
		"shotsonenter",
		"tradeprot"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("ccp"))
		{
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("tradeprot"))
		{
			if (activeChar.getTradeRefusal())
			{
				activeChar.setTradeRefusal(false);
				activeChar.sendMessage("Trade refusal mode disabled.");
			}
			else
			{
				activeChar.setTradeRefusal(true);
				activeChar.sendMessage("Trade refusal mode enabled.");
			}
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("changeexp"))
		{
			if (CustomServerConfigs.ALLOW_EXP_GAIN_COMMAND)
			{
				if (activeChar.getExpOn())
				{
					activeChar.setExpOn(false);
					activeChar.sendMessage("Experience gain disabled.");
				}
				else
				{
					activeChar.setExpOn(true);
					activeChar.sendMessage("Experience gain enabled.");
				}
			}
			else
			{
				activeChar.sendMessage("Experience command disabled by a gm.");
			}
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("nobuff"))
		{
			if (activeChar.isProtected())
			{
				activeChar.sendMessage("The grief-buff protection disabled.");
				activeChar.setProtectedPlayer(false);
			}
			else
			{
				activeChar.sendMessage("The grief-buff protection enabled.");
				activeChar.setProtectedPlayer(true);
			}
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("enchantanime"))
		{
			if (activeChar.isEnchantAnimation())
			{
				activeChar.setEnchantAnimation(false);
				activeChar.sendMessage("Enchant animation disabled.");
			}
			else
			{
				activeChar.setEnchantAnimation(true);
				activeChar.sendMessage("Enchant animation enabled.");
			}
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("hidestores"))
		{
			if (activeChar.getAppearance().isHideStores())
			{
				activeChar.getAppearance().setHideStores(false);
				activeChar.sendMessage("All stores are visible, please restart.");
			}
			else
			{
				activeChar.getAppearance().setHideStores(true);
				activeChar.sendMessage("All stores are invisible, please restart.");
			}
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("shotsonenter"))
		{
			if (activeChar.isOnEnterLoadSS())
			{
				activeChar.setOnEnterLoadSS(false);
				activeChar.sendMessage("On enter auto load shots disabled.");
			}
			else
			{
				activeChar.setOnEnterLoadSS(true);
				activeChar.sendMessage("On enter auto load shots enabled.");
			}
			sendHtml(activeChar);
		}
		else if (command.equalsIgnoreCase("blockshotsanime"))
		{
			if (activeChar.isSsAnimationBlocked())
			{
				activeChar.setSsAnimation(false);
				activeChar.sendMessage("Broadcast shots animation disabled.");
			}
			else
			{
				activeChar.setSsAnimation(true);
				activeChar.sendMessage("Broadcast shots animation enabled.");
			}
			sendHtml(activeChar);
		}
		
		return true;
	}
	
	public void sendHtml(L2PcInstance player)
	{
		TextBuilder builder = new TextBuilder();
		NpcHtmlMessage html = new NpcHtmlMessage(5);
		
		builder.append("<html noscrollbar><title>Character Control Panel</title><body>");
		builder.append("<center><br>");
		builder.append("<img src=\"l2ui.SquareGray\" width=290 height=1><br1>");
		builder.append("<table width=\"280\" cellpadding=\"5\" bgcolor=\"151515\">");
		builder.append("<tr>");
		builder.append("<td valign=\"top\"><center><font color=\"EBDF6C\">L2 Reunion</font> Character Control Panel<br>You this panel to set various settings for your character! Have in mind that settings are stored after restart and log out.</center></td>");
		builder.append("</tr>");
		builder.append(" </table><br1>");
		builder.append(" <img src=\"l2ui.SquareGray\" width=290 height=1><br>");
		builder.append("</center>");
		builder.append("<center>");
		builder.append("<img src=\"L2UI.SquareBlank\" width=290 height=3>");
		builder.append("<img src=\"L2UI.SquareGray\" width=290 height=2>");
		builder.append("<img src=\"L2UI.SquareBlank\" width=290 height=3>");
		builder.append("<table width=290 bgcolor=151515><tr>");
		builder.append("<td width=110><font color=9f9f9f>Configuration Option:</font></td>");
		builder.append("<td width=60 align=\"center\"><font color=9f9f9f>Action:</font></td>");
		builder.append("<td width=60 align=\"center\"><font color=9f9f9f>Status:</font></td></tr>");
		builder.append("</table>");
		builder.append("<img src=\"L2UI.SquareBlank\" width=290 height=3>");
		builder.append("<img src=\"L2UI.SquareGray\" width=290 height=2>");
		builder.append("<img src=\"L2UI.SquareBlank\" width=290 height=3>");
		builder.append("<table width=290 bgcolor=151515>");
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>Trade Refusal:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .tradeprot\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (player.getTradeRefusal())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>Block Experience:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .changeexp\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (!player.getExpOn())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>Badbuff Protection:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .nobuff\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (player.isProtected())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>Enchant Animation:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .enchantanime\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (player.isEnchantAnimation())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>Hide Stores:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .hidestores\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (player.isHideStores())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>On Enter Load Shots:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .shotsonenter\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (player.isOnEnterLoadSS())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("<tr>");
		builder.append("<td width=110><font color=898989>Block Shots Animation:</font></td>");
		builder.append("<td width=60><button value=\"Change\" action=\"bypass -h voice .blockshotsanime\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>");
		if (player.isSsAnimationBlocked())
		{
			builder.append("<td width=60 align=\"center\"><font color=849D68>Enabled</font></td></tr>");
		}
		else
		{
			builder.append("<td width=60 align=\"center\"><font color=A26D64>Disabled</font></td></tr>");
		}
		
		builder.append("</table>");
		builder.append("<img src=\"L2UI.SquareBlank\" width=290 height=3>");
		builder.append("<img src=\"L2UI.SquareGray\" width=290 height=2>");
		builder.append("<img src=\"L2UI.SquareBlank\" width=290 height=3>");
		builder.append("</center></body></html>");
		html.setHtml(builder.toString());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}