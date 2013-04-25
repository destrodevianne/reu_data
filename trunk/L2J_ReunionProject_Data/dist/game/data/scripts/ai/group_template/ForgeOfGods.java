package ai.group_template;

import ai.npc.AbstractNpcAI;

import l2r.gameserver.ai.CtrlIntention;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.actor.L2Attackable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.util.Rnd;

public class ForgeOfGods extends AbstractNpcAI
{
	private static final int MOB_ID_INDEX = 0;
	private static final int SPAWN_MOB_ID_INDEX = 1;
	private static final int CHANCE_TO_SPAWN_INDEX = 2;
	private static final int MAX_MOB_ON_SPAWN_INDEX = 3;
	
	private static final int[][] MOBS_SPAWNS =
	{
		// mob id, spawn mob id, spawn chance, max mob spawn
		{
			21379,
			21379,
			20,
			3
		},
		{
			21376,
			21394,
			20,
			1
		},
		{
			21653,
			21394,
			20,
			1
		},
		{
			21381,
			21381,
			20,
			3
		},
		{
			21383,
			21394,
			20,
			1
		},
		{
			21665,
			21394,
			20,
			1
		},
		{
			22646,
			22646,
			20,
			3
		},
		{
			21394,
			21395,
			20,
			1
		},
		{
			21398,
			21398,
			20,
			2
		},
		{
			21393,
			21393,
			50,
			5
		},
		{
			21386,
			21386,
			30,
			1
		}
	};
	
	public ForgeOfGods(int questId, String name, String descr)
	{
		super(name, descr);
		
		for (int[] currentSpawn : MOBS_SPAWNS)
			addKillId(currentSpawn[MOB_ID_INDEX]);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		L2Attackable killedMob = (L2Attackable) npc;
		if (killedMob == null)
			return null;
		
		L2Spawn killedMobSpawn = killedMob.getSpawn();
		if (killedMobSpawn != null && !killedMobSpawn.isRespawnEnabled())
			return null;
		
		int killedMobID = killedMob.getNpcId();
		L2Attackable newSpawnMob = null;
		
		for (int[] currentSpawn : MOBS_SPAWNS)
		{
			int currentMobID = currentSpawn[MOB_ID_INDEX];
			if (currentMobID == killedMobID)
			{
				if (Rnd.get(100) < currentSpawn[CHANCE_TO_SPAWN_INDEX])
				{
					for (int currentMobSpawnIndex = 0; currentMobSpawnIndex < currentSpawn[MAX_MOB_ON_SPAWN_INDEX]; currentMobSpawnIndex++)
					{
						newSpawnMob = (L2Attackable) addSpawn(currentSpawn[SPAWN_MOB_ID_INDEX], npc);
						if (newSpawnMob == null)
							break;
						newSpawnMob.setRunning();
						newSpawnMob.addDamageHate(killer, 0, 999);
						newSpawnMob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, killer);
					}
				}
			}
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public static void main(String[] args)
	{
		new ForgeOfGods(-1, ForgeOfGods.class.getSimpleName(), "ai");
	}
}