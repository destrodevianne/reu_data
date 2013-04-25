package ai.group_template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastMap;
import l2r.Config;
import l2r.gameserver.ai.CtrlEvent;
import l2r.gameserver.ai.CtrlIntention;
import l2r.gameserver.model.L2CharPosition;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.SocialAction;
import l2r.gameserver.util.Util;
import l2r.util.Rnd;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ai.npc.AbstractNpcAI;

public class SelMahumTrainingGrounds extends AbstractNpcAI
{
	private static final Map<Integer, Camp> camps = new FastMap<>();
	private static final Map<L2Npc, Integer> campIdByNpc = new FastMap<>();
	
	private static final int[] SELMAHUM_RECRUIT =
	{
		22780,
		22782,
		22784
	};
	
	private static final int[] SELMAHUM_SOLDIER =
	{
		22781,
		22783,
		22785
	};
	
	private static final int[] SELMAHUM_DRILL_SERGEANT =
	{
		22775,
		22777,
		22778
	};
	private static final int SELMAHUM_TRAINING_OFFICER = 22776;
	private static final int[] CHIEF_SOCIAL_ACTIONS =
	{
		1,
		4,
		5,
		7
	};
	
	private static final int[] SOLDIER_SOCIAL_ACTIONS =
	{
		1,
		5,
		6,
		7
	};
	
	public SelMahumTrainingGrounds(int questId, String name, String descr)
	{
		super(name, descr);
		
		for (int i : SELMAHUM_DRILL_SERGEANT)
		{
			addAttackId(i);
			addKillId(i);
			addSpawnId(new int[]
			{
				i
			});
		}
		
		for (int i : SELMAHUM_RECRUIT)
		{
			addAttackId(i);
			addKillId(i);
			addSpawnId(new int[]
			{
				i
			});
		}
		
		for (int i : SELMAHUM_SOLDIER)
		{
			addAttackId(i);
			addKillId(i);
			addSpawnId(new int[]
			{
				i
			});
		}
		
		addAttackId(SELMAHUM_TRAINING_OFFICER);
		addKillId(SELMAHUM_TRAINING_OFFICER);
		addSpawnId(new int[]
		{
			SELMAHUM_TRAINING_OFFICER
		});
		
		init();
	}
	
	public static void main(String[] args)
	{
		new SelMahumTrainingGrounds(-1, SelMahumTrainingGrounds.class.getSimpleName(), "ai");
	}
	
	private void init()
	{
		File f = new File(Config.DATAPACK_ROOT, "data/spawnZones/training_grounds.xml");
		if (!f.exists())
		{
			_log.severe("[Sel Mahum Training Grounds] Missing training_grounds.xml!");
			return;
		}
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setValidating(true);
			Document doc = factory.newDocumentBuilder().parse(f);
			
			for (Node n = doc.getDocumentElement().getFirstChild(); n != null; n = n.getNextSibling())
			{
				if (n.getNodeName().equals("camp"))
				{
					NamedNodeMap attrs = n.getAttributes();
					int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
					
					ArrayList<SelMahumTrainingGrounds.SpawnData> spawns = new ArrayList<>();
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if (d.getNodeName().equals("spawn"))
						{
							attrs = d.getAttributes();
							int npcId = Integer.parseInt(attrs.getNamedItem("npcId").getNodeValue());
							int xPos = Integer.parseInt(attrs.getNamedItem("x").getNodeValue());
							int yPos = Integer.parseInt(attrs.getNamedItem("y").getNodeValue());
							int zPos = Integer.parseInt(attrs.getNamedItem("z").getNodeValue());
							int heading = d.getAttributes().getNamedItem("heading").getNodeValue() != null ? Integer.parseInt(d.getAttributes().getNamedItem("heading").getNodeValue()) : 0;
							
							SpawnData spawnData = new SpawnData(npcId, new Location(xPos, yPos, zPos, heading));
							spawns.add(spawnData);
						}
					}
					Camp camp = new Camp(id, spawns);
					camps.put(id, camp);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "[Sel Mahum Training Grounds] Could not parse training_grounds.xml file: " + e.getMessage(), e);
		}
		initSpawns();
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (isOfficer(npc.getNpcId()))
		{
			startQuestTimer("Animate", 15000L, npc, null, true);
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public final String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		Camp camp = null;
		if ((isRecruit(npc.getNpcId())) || (isOfficer(npc.getNpcId())))
		{
			camp = camps.get(getCampId(npc));
			if ((camp.officer != null) && (!camp.officer.isDead()))
			{
				int chance = Rnd.get(100);
				if (chance < 10)
				{
					camp.officer.broadcastPacket(new CreatureSay(camp.officer.getObjectId(), 1, camp.officer.getName(), NpcStringId.HOW_DARE_YOU_ATTACK_MY_RECRUITS));
				}
				else if (chance < 20)
				{
					camp.officer.broadcastPacket(new CreatureSay(camp.officer.getObjectId(), 1, camp.officer.getName(), NpcStringId.WHO_IS_DISRUPTING_THE_ORDER));
				}
				camp.officer.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(1));
			}
			for (L2Npc recruit : camp.recruits)
			{
				if ((recruit != null) && (!recruit.isDead()) && (!recruit.equals(npc)))
				{
					recruit.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(1));
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public final String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		int npcId = npc.getNpcId();
		
		if (isOfficer(npcId))
		{
			Camp camp = camps.get(getCampId(npc));
			
			L2Npc mob = camp.recruits.get(Rnd.get(camp.recruits.size()));
			if (!mob.isDead())
			{
				mob.broadcastPacket(new CreatureSay(mob.getObjectId(), 1, mob.getName(), NpcStringId.THE_DRILLMASTER_IS_DEAD));
			}
			
			mob = camp.recruits.get(Rnd.get(camp.recruits.size()));
			if (!mob.isDead())
			{
				mob.broadcastPacket(new CreatureSay(mob.getObjectId(), 1, mob.getName(), NpcStringId.LINE_UP_THE_RANKS));
			}
			
			for (Object element : camp.recruits)
			{
				mob = (L2Npc) element;
				if (mob.getNpcId() != npcId)
				{
					int fearLocX = mob.getX() + (Rnd.get(800, 1200) - Rnd.get(1200));
					int fearLocY = mob.getY() + (Rnd.get(800, 1200) - Rnd.get(1200));
					int fearHeading = Util.calculateHeadingFrom(mob.getX(), mob.getY(), fearLocX, fearLocY);
					mob.startFear();
					mob.setRunning();
					mob.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(fearLocX, fearLocY, mob.getZ(), fearHeading));
					startQuestTimer("LineUpRank", 30000L, mob, null);
				}
			}
			cancelQuestTimer("Animate", npc, null);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	private void initSpawns()
	{
		for (int id : camps.keySet())
		{
			Camp camp = camps.get(id);
			
			for (SpawnData spawnData : camp.spawns)
			{
				Location loc = spawnData.spawnLoc;
				L2Npc npc = addSpawn(spawnData.npcId, loc.getX(), loc.getY(), loc.getZ(), loc.getHeading(), false, 0L, false);
				npc.getSpawn().setAmount(1);
				npc.getSpawn().setRespawnDelay(60);
				npc.getSpawn().startRespawn();
				npc.setIsNoRndWalk(true);
				// TODO: Need test
				npc.setRandomAnimationEnabled(false);
				if (isOfficer(spawnData.npcId))
				{
					camp.officer = npc;
				}
				else
				{
					camp.recruits.add(npc);
				}
				campIdByNpc.put(npc, camp.id);
			}
			startQuestTimer("Animate", 15000L, camp.officer, null, true);
		}
	}
	
	private int getCampId(L2Npc npc)
	{
		if (!campIdByNpc.containsKey(npc))
		{
			return -1;
		}
		return campIdByNpc.get(npc);
	}
	
	private boolean isRecruit(int npcId)
	{
		return (Util.contains(SELMAHUM_RECRUIT, npcId)) || (Util.contains(SELMAHUM_SOLDIER, npcId));
	}
	
	private boolean isOfficer(int npcId)
	{
		return (Util.contains(SELMAHUM_DRILL_SERGEANT, npcId)) || (SELMAHUM_TRAINING_OFFICER == npcId);
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("LineUpRank"))
		{
			if (!npc.isDead())
			{
				npc.setHeading(npc.getSpawn().getHeading());
				npc.teleToLocation(npc.getSpawn().getLocx(), npc.getSpawn().getLocy(), npc.getSpawn().getLocz());
				npc.getAttackByList().clear();
				npc.setWalking();
			}
		}
		else if (event.equalsIgnoreCase("Animate"))
		{
			if ((npc == null) || (npc.isDead()) || (npc.isInCombat()))
			{
				return null;
			}
			
			int campId = getCampId(npc);
			Camp camp = camps.get(campId);
			
			int idx = Rnd.get(6);
			if (idx <= (CHIEF_SOCIAL_ACTIONS.length - 1))
			{
				npc.broadcastPacket(new SocialAction(npc.getObjectId(), CHIEF_SOCIAL_ACTIONS[idx]));
				for (L2Npc recruit : camp.recruits)
				{
					if ((recruit != null) && (!recruit.isDead()))
					{
						recruit.broadcastPacket(new SocialAction(recruit.getObjectId(), SOLDIER_SOCIAL_ACTIONS[idx]));
					}
				}
			}
			startQuestTimer("Animate", 15000L, npc, null);
		}
		return null;
	}
	
	private class SpawnData
	{
		public final int npcId;
		public final Location spawnLoc;
		
		public SpawnData(int npcId, Location spawnLoc)
		{
			this.npcId = npcId;
			this.spawnLoc = spawnLoc;
		}
	}
	
	private class Camp
	{
		public final int id;
		public final List<SelMahumTrainingGrounds.SpawnData> spawns;
		public L2Npc officer;
		public ArrayList<L2Npc> recruits;
		
		public Camp(int id, List<SelMahumTrainingGrounds.SpawnData> spawns)
		{
			this.id = id;
			this.spawns = spawns;
			this.recruits = new ArrayList<>();
		}
		
	}
}