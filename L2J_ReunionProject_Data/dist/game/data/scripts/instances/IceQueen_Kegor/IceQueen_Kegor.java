package instances.IceQueen_Kegor;

import java.util.List;

import javolution.util.FastList;
import quests.Q10284_AcquisitionOfDivineSword.Q10284_AcquisitionOfDivineSword;

import l2r.gameserver.ai.CtrlIntention;
import l2r.gameserver.datatables.SkillTable;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2CharPosition;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Attackable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.model.quest.State;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.NpcSay;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.gameserver.util.Util;

public class IceQueen_Kegor extends Quest
{
	private class KegorWorld extends InstanceWorld
	{
		public long[] storeTime =
		{
			0,
			0
		};
		public boolean underAttack = false;
		public L2Npc KEGOR = null;
		public List<L2Attackable> liveMobs;
		
		public KegorWorld()
		{
			
		}
	}
	
	private static final String qn = "IceQueen_Kegor";
	private static final int INSTANCEID = 138;
	
	private static final int KROON = 32653;
	private static final int TAROON = 32654;
	private static final int KEGOR_IN_CAVE = 18846;
	private static final int MONSTER = 22766;
	
	private static final int ANTIDOTE = 15514;
	
	private static final int BUFF = 6286;
	
	private static final int[][] MOB_SPAWNS =
	{
		{
			185216,
			-184112,
			-3308,
			-15396
		},
		{
			185456,
			-184240,
			-3308,
			-19668
		},
		{
			185712,
			-184384,
			-3308,
			-26696
		},
		{
			185920,
			-184544,
			-3308,
			-32544
		},
		{
			185664,
			-184720,
			-3308,
			27892
		},
	};
	
	private static final int[] ENTRY_POINT =
	{
		186852,
		-173492,
		-3763
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
		if ((player.getLevel() < 82) || (player.getLevel() > 85))
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
	
	protected int enterInstance(L2PcInstance player, String template, teleCoord teleto)
	{
		int instanceId = 0;
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		// existing instance
		if (world != null)
		{
			// this instance
			if (!(world instanceof KegorWorld))
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
		{
			return 0;
		}
		
		instanceId = InstanceManager.getInstance().createDynamicInstance(template);
		final Instance inst = InstanceManager.getInstance().getInstance(instanceId);
		inst.setSpawnLoc(new Location(player));
		world = new KegorWorld();
		world.setInstanceId(instanceId);
		world.setTemplateId(INSTANCEID);
		world.setStatus(0);
		
		((KegorWorld) world).storeTime[0] = System.currentTimeMillis();
		InstanceManager.getInstance().addWorld(world);
		_log.info("KegorWorld started " + template + " Instance: " + instanceId + " created by player: " + player.getName());
		teleto.instanceId = instanceId;
		teleportplayer(player, teleto);
		world.addAllowed(player.getObjectId());
		return instanceId;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (npc.getNpcId() == KEGOR_IN_CAVE)
		{
			InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
			if ((tmpworld != null) && (tmpworld instanceof KegorWorld))
			{
				KegorWorld world = (KegorWorld) tmpworld;
				
				if (event.equalsIgnoreCase("spawn"))
				{
					world.liveMobs = new FastList<>();
					for (int[] spawn : MOB_SPAWNS)
					{
						L2Attackable spawnedMob = (L2Attackable) addSpawn(MONSTER, spawn[0], spawn[1], spawn[2], spawn[3], false, 0, false, world.getInstanceId());
						world.liveMobs.add(spawnedMob);
					}
				}
				
				else if (event.equalsIgnoreCase("buff"))
				{
					// schedule mob attack
					if ((world.liveMobs != null) && !world.liveMobs.isEmpty())
					{
						for (L2Attackable monster : world.liveMobs)
						{
							if (monster.getKnownList().knowsObject(npc))
							{
								monster.addDamageHate(npc, 0, 999);
								monster.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, npc, null);
							}
							else
							{
								monster.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(npc.getX(), npc.getY(), npc.getZ(), 0));
							}
						}
						
						// buff player
						if (npc.getKnownList().getKnownPlayers().size() == 1)
						{
							L2Skill buff = SkillTable.getInstance().getInfo(BUFF, 1);
							if (buff != null)
							{
								for (L2PcInstance pl : npc.getKnownList().getKnownPlayers().values())
								{
									if (Util.checkIfInRange(buff.getCastRange(), npc, pl, false))
									{
										npc.setTarget(pl);
										npc.doCast(buff);
									}
								}
							}
						}
						startQuestTimer("buff", 30000, npc, player);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		int npcId = npc.getNpcId();
		String htmltext = getNoQuestMsg(player);
		
		QuestState hostQuest = player.getQuestState(Q10284_AcquisitionOfDivineSword.class.getSimpleName());
		
		if (hostQuest == null)
		{
			System.out.println("null host quest");
			return htmltext;
		}
		
		if ((npcId == KROON) || (npcId == TAROON))
		{
			teleCoord tele = new teleCoord();
			tele.x = ENTRY_POINT[0];
			tele.y = ENTRY_POINT[1];
			tele.z = ENTRY_POINT[2];
			
			htmltext = npcId == KROON ? "32653-07.htm" : "32654-07.htm";
			if (enterInstance(player, "IceQueen_Kegor.xml", tele) > 0)
			{
				htmltext = "";
				if ((hostQuest.getInt("progress") == 2) && (hostQuest.getQuestItemsCount(ANTIDOTE) == 0))
				{
					hostQuest.giveItems(ANTIDOTE, 1);
					hostQuest.playSound("ItemSound.quest_middle");
					hostQuest.set("cond", "4");
				}
			}
		}
		
		else if (npc.getNpcId() == KEGOR_IN_CAVE)
		{
			InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(player.getInstanceId());
			if ((tmpworld != null) && (tmpworld instanceof KegorWorld))
			{
				KegorWorld world = (KegorWorld) tmpworld;
				if ((hostQuest.getInt("progress") == 2) && (hostQuest.getQuestItemsCount(ANTIDOTE) > 0) && !world.underAttack)
				{
					hostQuest.takeItems(ANTIDOTE, hostQuest.getQuestItemsCount(ANTIDOTE));
					hostQuest.playSound("ItemSound.quest_middle");
					hostQuest.set("cond", "5");
					htmltext = "18846-01.htm";
					world.underAttack = true;
					npc.setIsInvul(false);
					npc.setIsMortal(true);
					startQuestTimer("spawn", 3000, npc, player);
					startQuestTimer("buff", 3500, npc, player);
				}
				
				else if (hostQuest.getState() == State.COMPLETED)
				{
					world.removeAllowed(player.getObjectId());
					final Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
					Location tele;
					tele = inst.getSpawnLoc();
					exitInstance(player, tele);
					htmltext = "";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState hostQuest = player.getQuestState(Q10284_AcquisitionOfDivineSword.class.getSimpleName());
		if (hostQuest == null)
		{
			return null;
		}
		
		if (npc.getNpcId() == KEGOR_IN_CAVE)
		{
			InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(player.getInstanceId());
			if ((tmpworld != null) && (tmpworld instanceof KegorWorld))
			{
				KegorWorld world = (KegorWorld) tmpworld;
				
				if (world.KEGOR == null)
				{
					world.KEGOR = npc;
				}
				if (hostQuest.getState() != State.STARTED)
				{
					return "18846-04.htm";
				}
				
				if (!world.underAttack && (hostQuest.getInt("progress") == 2))
				{
					return "18846-00.htm";
				}
				else if (hostQuest.getInt("progress") == 3)
				{
					hostQuest.giveItems(57, 296425);
					hostQuest.addExpAndSp(921805, 82230);
					hostQuest.playSound("ItemSound.quest_finish");
					hostQuest.exitQuest(false);
					return "18846-03.htm";
				}
				else
				{
					return "18846-02.htm";
				}
			}
		}
		return null;
	}
	
	@Override
	public final String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		QuestState hostQuest = player.getQuestState(Q10284_AcquisitionOfDivineSword.class.getSimpleName());
		if ((hostQuest == null) || (hostQuest.getState() != State.STARTED))
		{
			return null;
		}
		
		InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
		if ((tmpworld != null) && (tmpworld instanceof KegorWorld))
		{
			KegorWorld world = (KegorWorld) tmpworld;
			
			if (npc.getNpcId() == MONSTER)
			{
				if (world.liveMobs != null)
				{
					world.liveMobs.remove(npc);
					if (world.liveMobs.isEmpty() && (world.KEGOR != null) && !world.KEGOR.isDead() && (hostQuest.getInt("progress") == 2))
					{
						world.underAttack = false;
						world.liveMobs = null;
						cancelQuestTimer("buff", world.KEGOR, null);
						world.KEGOR.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, player, null);
						NpcSay cs = new NpcSay(world.KEGOR.getObjectId(), Say2.ALL, world.KEGOR.getNpcId(), NpcStringId.I_CAN_FINALLY_TAKE_A_BREATHER_BY_THE_WAY_WHO_ARE_YOU_HMM_I_THINK_I_KNOW_WHO_SENT_YOU);
						world.KEGOR.broadcastPacket(cs);
						hostQuest.set("progress", "3");
						hostQuest.set("cond", "6");
						hostQuest.playSound("ItemSound.quest_middle");
						
						// destroy instance after 3 min
						Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
						inst.setDuration(3 * 60000);
						inst.setEmptyDestroyTime(0);
					}
				}
				
				else if (npc.getNpcId() == KEGOR_IN_CAVE)
				{
					world.KEGOR = null;
					NpcSay cs = new NpcSay(npc.getObjectId(), Say2.ALL, npc.getNpcId(), NpcStringId.HOW_COULD_I_FALL_IN_A_PLACE_LIKE_THIS);
					npc.broadcastPacket(cs);
					
					// destroy instance after 1 min
					Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
					inst.setDuration(60000);
					inst.setEmptyDestroyTime(0);
				}
			}
		}
		return super.onKill(npc, player, isPet);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		// Doesn't work now. NPC doesn't wish to attack Monster
		/*
		 * else if (npcId == _mob && KEGOR != null) { if (getQuestTimer("attack_mobs", KEGOR, null) == null) startQuestTimer("attack_mobs", 10000, KEGOR, null); }
		 */
		return super.onSpawn(npc);
	}
	
	public IceQueen_Kegor(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addFirstTalkId(KEGOR_IN_CAVE);
		addStartNpc(KROON);
		addStartNpc(TAROON);
		addTalkId(KROON);
		addTalkId(TAROON);
		addTalkId(KEGOR_IN_CAVE);
		addKillId(KEGOR_IN_CAVE);
		addKillId(MONSTER);
		addSpawnId(KEGOR_IN_CAVE);
		addSpawnId(MONSTER);
	}
	
	public static void main(String[] args)
	{
		new IceQueen_Kegor(-1, qn, "instances");
	}
}