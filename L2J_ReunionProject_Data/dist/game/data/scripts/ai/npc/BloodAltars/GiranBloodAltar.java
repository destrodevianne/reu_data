package ai.npc.BloodAltars;

import javolution.util.FastList;
import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.quest.Quest;
import l2r.util.Rnd;

public class GiranBloodAltar extends Quest
{
	private static final long delay = Config.CHANGE_STATUS * 60 * 1000;
	
	private final FastList<L2Npc> deadnpcs = new FastList<>();
	private final FastList<L2Npc> alivenpcs = new FastList<>();
	private final FastList<L2Npc> bosses = new FastList<>();
	
	protected boolean progress1 = false;
	protected boolean progress2 = false;
	protected boolean progress3 = false;
	
	private static final int[][] bossGroups =
	{
		{
			25760,
			80888,
			142872,
			-3552,
			6133
		},
		{
			25763,
			80632,
			142200,
			-3559,
			8191
		},
		{
			25766,
			80312,
			142568,
			-3573,
			4836
		}
	};
	
	private static final int[][] BLOODALTARS_DEAD_NPC =
	{
		{
			4328,
			80856,
			142680,
			-3553,
			0
		},
		{
			4327,
			80952,
			142536,
			-3557,
			0
		},
		{
			4328,
			80824,
			142392,
			-3554,
			49152
		}
	};
	
	private static final int[][] BLOODALTARS_ALIVE_NPC =
	{
		{
			4325,
			80856,
			142680,
			-3553,
			0
		},
		{
			4324,
			80952,
			142536,
			-3557,
			0
		},
		{
			4325,
			80824,
			142392,
			-3554,
			49152
		}
	};
	
	public GiranBloodAltar(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		manageNpcs(true);
		
		addKillId(25760);
		addKillId(25763);
		addKillId(25766);
		
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				GiranBloodAltar.this.changestatus();
			}
		}, delay);
	}
	
	public static void main(String[] args)
	{
		new GiranBloodAltar(-1, GiranBloodAltar.class.getSimpleName(), "ai");
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
	
	protected void manageBosses(boolean spawn)
	{
		if (spawn)
		{
			for (int[] bossspawn : bossGroups)
			{
				L2Npc boss = addSpawn(bossspawn[0], bossspawn[1], bossspawn[2], bossspawn[3], bossspawn[4], false, 0L, false);
				if (boss != null)
				{
					bosses.add(boss);
				}
				
			}
			
		}
		else if (!bosses.isEmpty())
		{
			for (L2Npc boss : bosses)
			{
				if (boss != null)
				{
					boss.deleteMe();
				}
			}
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
					GiranBloodAltar.this.manageNpcs(false);
					GiranBloodAltar.this.manageBosses(true);
				}
				else
				{
					GiranBloodAltar.this.manageBosses(false);
					GiranBloodAltar.this.manageNpcs(true);
					ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
					{
						@Override
						public void run()
						{
							GiranBloodAltar.this.changestatus();
						}
					}, Config.RESPAWN_TIME * 60 * 1000);
				}
			}
		}, 10000L);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		int npcId = npc.getNpcId();
		
		if (npcId == 25760)
		{
			progress1 = true;
		}
		
		if (npcId == 25763)
		{
			progress2 = true;
		}
		
		if (npcId == 25766)
		{
			progress3 = true;
		}
		
		if ((progress1) && (progress2) && (progress3))
		{
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
			{
				@Override
				public void run()
				{
					progress1 = false;
					progress2 = false;
					progress3 = false;
					
					GiranBloodAltar.this.manageBosses(false);
					GiranBloodAltar.this.manageNpcs(true);
					ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
					{
						@Override
						public void run()
						{
							GiranBloodAltar.this.changestatus();
						}
					}, Config.RESPAWN_TIME * 60 * 1000);
				}
			}, 30000L);
		}
		
		return super.onKill(npc, player, isSummon);
	}
}