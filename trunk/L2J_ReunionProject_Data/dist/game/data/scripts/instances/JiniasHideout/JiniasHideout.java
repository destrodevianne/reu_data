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
package instances.JiniasHideout;

import quests.Q10284_AcquisitionOfDivineSword.Q10284_AcquisitionOfDivineSword;
import quests.Q10285_MeetingSirra.Q10285_MeetingSirra;
import quests.Q10286_ReunionWithSirra.Q10286_ReunionWithSirra;
import quests.Q10287_StoryOfThoseLeft.Q10287_StoryOfThoseLeft;

import l2r.gameserver.ai.CtrlIntention;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.NpcSay;
import l2r.gameserver.network.serverpackets.SystemMessage;

/**
 * @author GKR
 */

public class JiniasHideout extends Quest
{
	private class JiniasWorld extends InstanceWorld
	{
		public long[] storeTime =
		{
			0,
			0
		};
		public int questId;
		
		public JiniasWorld()
		{
			
		}
	}
	
	private static final String qn = "JiniasHideout";
	
	private static final int RAFFORTY = 32020;
	private static final int JINIA = 32760;
	private static final int SIRRA = 32762;
	
	private static final int[] ENTRY_POINT =
	{
		-23530,
		-8963,
		-5413
	};
	
	public class teleCoord
	{
		int instanceId;
		int x;
		int y;
		int z;
	}
	
	private void teleportplayer(L2PcInstance player, teleCoord teleto)
	{
		player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		player.setInstanceId(teleto.instanceId);
		player.teleToLocation(teleto.x, teleto.y, teleto.z);
		return;
	}
	
	private boolean checkConditions(L2PcInstance player)
	{
		if (player.getLevel() < 82 || player.getLevel() > 85)
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_LEVEL_REQUIREMENT_NOT_SUFFICIENT);
			sm.addPcName(player);
			player.sendPacket(sm);
			return false;
		}
		
		return true;
	}
	
	protected void exitInstance(L2PcInstance player, Location tele)
	{
		player.setInstanceId(0);
		player.teleToLocation(tele, false);
	}
	
	protected int enterInstance(L2PcInstance player, String template, teleCoord teleto, int questId)
	{
		int instanceId = 0;
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		// existing instance
		if (world != null)
		{
			// this instance
			if (!(world instanceof JiniasWorld))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER));
				return 0;
			}
			teleto.instanceId = world.getInstanceId();
			teleportplayer(player, teleto);
			return instanceId;
		}
		// New instance
		if (!checkConditions(player))
			return 0;
		
		instanceId = InstanceManager.getInstance().createDynamicInstance(template);
		final Instance inst = InstanceManager.getInstance().getInstance(instanceId);
		inst.setSpawnLoc(new Location(player));
		world = new JiniasWorld();
		world.setInstanceId(instanceId);
		((JiniasWorld) world).questId = questId;
		
		// Template id depends of quest
		switch (questId)
		{
			case 10284:
				world.setTemplateId(140);
				break;
			case 10285:
				world.setTemplateId(141);
				break;
			case 10286:
				world.setTemplateId(145);
				break;
			case 10287:
				world.setTemplateId(146);
				break;
		}
		
		world.setStatus(0);
		((JiniasWorld) world).storeTime[0] = System.currentTimeMillis();
		InstanceManager.getInstance().addWorld(world);
		_log.info("JiniasWorld started " + template + " Instance: " + instanceId + " created by player: " + player.getName());
		teleto.instanceId = instanceId;
		teleportplayer(player, teleto);
		world.addAllowed(player.getObjectId());
		return instanceId;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = null;
		
		if (event.startsWith("enterInstance_") && npc.getNpcId() == RAFFORTY)
		{
			int questId = -1;
			String tmpl = null;
			QuestState hostQuest = null;
			
			try
			{
				questId = Integer.parseInt(event.substring(14));
				
				teleCoord tele = new teleCoord();
				tele.x = ENTRY_POINT[0];
				tele.y = ENTRY_POINT[1];
				tele.z = ENTRY_POINT[2];
				
				switch (questId)
				{
					case 10284:
						hostQuest = player.getQuestState(Q10284_AcquisitionOfDivineSword.class.getSimpleName());
						tmpl = "JiniasHideout.xml";
						htmltext = "10284_failed.htm";
						break;
					case 10285:
						hostQuest = player.getQuestState(Q10285_MeetingSirra.class.getSimpleName());
						tmpl = "JiniasHideout.xml";
						htmltext = "10285_failed.htm";
						break;
					case 10286:
						hostQuest = player.getQuestState(Q10286_ReunionWithSirra.class.getSimpleName());
						tmpl = "JiniasHideout.xml";
						htmltext = "10286_failed.htm";
						break;
					case 10287:
						hostQuest = player.getQuestState(Q10287_StoryOfThoseLeft.class.getSimpleName());
						tmpl = "JiniasHideout.xml";
						htmltext = "10287_failed.htm";
						break;
				}
				
				if (hostQuest != null && hostQuest.isStarted() && hostQuest.getInt("progress") == 1)
				{
					hostQuest.playSound("ItemSound.quest_middle");
					hostQuest.set("cond", "2");
				}
				
				if (tmpl != null)
				{
					if (enterInstance(player, tmpl, tele, questId) > 0)
						htmltext = null;
				}
			}
			catch (Exception e)
			{
				
			}
		}
		
		else if (event.equalsIgnoreCase("leaveInstance") && npc.getNpcId() == JINIA)
		{
			QuestState hostQuest = null;
			InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
			final Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
			world.removeAllowed(player.getObjectId());
			Location tele;
			tele = inst.getSpawnLoc();
			exitInstance(player, tele);
			
			switch (((JiniasWorld) world).questId)
			{
				case 10285:
					hostQuest = player.getQuestState(Q10285_MeetingSirra.class.getSimpleName());
					break;
				case 10286:
					hostQuest = player.getQuestState(Q10286_ReunionWithSirra.class.getSimpleName());
					break;
				case 10287:
					hostQuest = player.getQuestState(Q10287_StoryOfThoseLeft.class.getSimpleName());
					break;
			}
			
			if (hostQuest != null && hostQuest.isStarted() && hostQuest.getInt("progress") == 2)
			{
				switch (((JiniasWorld) world).questId)
				{
					case 10285:
						htmltext = "10285_goodbye.htm";
						break;
					case 10286:
						htmltext = "10286_goodbye.htm";
						hostQuest.playSound("ItemSound.quest_middle");
						hostQuest.set("cond", "5");
						break;
					case 10287:
						htmltext = "10287_goodbye.htm";
						hostQuest.playSound("ItemSound.quest_middle");
						hostQuest.set("cond", "5");
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (npc.getNpcId() == SIRRA)
		{
			InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
			if (tmpworld != null && tmpworld instanceof JiniasWorld)
			{
				NpcStringId msg = null;
				switch (tmpworld.getTemplateId())
				{
					case 141:
						msg = NpcStringId.THERES_NOTHING_YOU_CANT_SAY_I_CANT_LISTEN_TO_YOU_ANYMORE;
						break;
					case 145:
						msg = NpcStringId.YOU_ADVANCED_BRAVELY_BUT_GOT_SUCH_A_TINY_RESULT_HOHOHO;
				}
				
				if (msg != null)
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.ALL, npc.getNpcId(), msg));
			}
		}
		return null;
	}
	
	public JiniasHideout(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(RAFFORTY);
		addTalkId(JINIA, RAFFORTY);
		addSpawnId(SIRRA);
	}
	
	public static void main(String[] args)
	{
		new JiniasHideout(-1, qn, "instances");
	}
}
