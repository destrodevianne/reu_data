package ai.npc.BloodAltars;

import javolution.util.FastList;
import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.quest.Quest;
import l2r.util.Rnd;

public class ElvenBloodAltar extends Quest
{
	private static final long delay = Config.CHANGE_STATUS * 60 * 1000;
	
	private final FastList<L2Npc> deadnpcs = new FastList<>();
	private final FastList<L2Npc> alivenpcs = new FastList<>();
	
	private static final int[][] BLOODALTARS_DEAD_NPC =
	{
		{
			4328,
			40280,
			53784,
			-3328,
			0
		},
		{
			4328,
			40056,
			53944,
			-3329,
			40961
		},
		{
			4327,
			40120,
			53784,
			-3336,
			32768
		}
	};
	
	private static final int[][] BLOODALTARS_ALIVE_NPC =
	{
		{
			4325,
			40280,
			53784,
			-3328,
			0
		},
		{
			4325,
			40056,
			53944,
			-3329,
			40961
		},
		{
			4324,
			40120,
			53784,
			-3336,
			32768
		}
	};
	
	public ElvenBloodAltar(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		manageNpcs(true);
		
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				ElvenBloodAltar.this.changestatus();
			}
		}, delay);
	}
	
	public static void main(String[] args)
	{
		new ElvenBloodAltar(-1, ElvenBloodAltar.class.getSimpleName(), "ai");
	}
	
	protected void manageNpcs(boolean spawnAlive)
	{
		if (spawnAlive)
		{
			for (int[] spawn : BLOODALTARS_ALIVE_NPC)
			{
				L2Npc npc = addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0L, false);
				if (npc != null)
				{
					alivenpcs.add(npc);
				}
			}
			
			if (!deadnpcs.isEmpty())
			{
				for (L2Npc npc : deadnpcs)
				{
					if (npc != null)
					{
						npc.deleteMe();
					}
				}
			}
			deadnpcs.clear();
		}
		else
		{
			for (int[] spawn : BLOODALTARS_DEAD_NPC)
			{
				L2Npc npc = addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0L, false);
				if (npc != null)
				{
					deadnpcs.add(npc);
				}
			}
			
			if (!alivenpcs.isEmpty())
			{
				for (L2Npc npc : alivenpcs)
				{
					if (npc != null)
					{
						npc.deleteMe();
					}
				}
			}
			alivenpcs.clear();
		}
	}
	
	protected void changestatus()
	{
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				if (Rnd.chance(Config.CHANCE_SPAWN))
				{
					ElvenBloodAltar.this.manageNpcs(false);
					ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
					{
						@Override
						public void run()
						{
							ElvenBloodAltar.this.changestatus();
						}
					}, Config.RESPAWN_TIME * 60 * 1000);
				}
				else
				{
					ElvenBloodAltar.this.manageNpcs(true);
					ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
					{
						@Override
						public void run()
						{
							ElvenBloodAltar.this.changestatus();
						}
					}, Config.RESPAWN_TIME * 60 * 1000);
				}
			}
		}, 10000L);
	}
}