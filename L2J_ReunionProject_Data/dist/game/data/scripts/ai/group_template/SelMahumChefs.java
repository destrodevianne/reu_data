package ai.group_template;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastMap;
import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.ai.CtrlIntention;
import l2r.gameserver.datatables.SkillTable;
import l2r.gameserver.datatables.SpawnTable;
import l2r.gameserver.model.L2CharPosition;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Attackable;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2MonsterInstance;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.serverpackets.MoveToLocation;
import l2r.gameserver.network.serverpackets.NpcSay;
import l2r.gameserver.util.Util;
import l2r.util.Rnd;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ai.npc.AbstractNpcAI;

public class SelMahumChefs extends AbstractNpcAI
{
	private static final int SELMAHUM_CHEF = 18908;
	private static final int SELMAHUM_ESCORT_GUARD = 22779;
	protected static final int[] SELMAHUM_SQUAD_LEADERS =
	{
		22786,
		22787,
		22788
	};
	
	private static final NpcStringId[] CHEF_FSTRINGS =
	{
		NpcStringId.I_BROUGHT_THE_FOOD,
		NpcStringId.COME_AND_EAT
	};
	private static final int CAMP_FIRE = 18927;
	private static final int FIRE_FEED = 18933;
	private static final int SKILL_TIRED = 6331;
	private static final int SKILL_FULL = 6332;
	protected static final FastMap<Integer, ChefGroup> chefGroups = new FastMap<>();
	protected static final Map<Integer, Location[]> escortSpawns = new FastMap<>();
	protected static final ConcurrentHashMap<L2Npc, Integer> fireplaces = new ConcurrentHashMap<>();
	protected static final ConcurrentHashMap<L2Npc, L2Npc> fireplacesFeed = new ConcurrentHashMap<>();
	
	public SelMahumChefs(int questId, String name, String descr)
	{
		super(name, descr);
		
		int[] mobs =
		{
			SELMAHUM_CHEF,
			SELMAHUM_ESCORT_GUARD
		};
		registerMobs(mobs, new Quest.QuestEventType[]
		{
			Quest.QuestEventType.ON_ATTACK,
			Quest.QuestEventType.ON_KILL
		});
		addSpawnId(new int[]
		{
			SELMAHUM_CHEF
		});
		addFirstTalkId(CAMP_FIRE);
		addFirstTalkId(FIRE_FEED);
		
		init();
	}
	
	public static void main(String[] args)
	{
		new SelMahumChefs(-1, SelMahumChefs.class.getSimpleName(), "ai");
	}
	
	private void init()
	{
		File f = new File(Config.DATAPACK_ROOT, "data/spawnZones/selmahum_chefs.xml");
		if (!f.exists())
		{
			_log.severe("[Sel Mahum Chefs]: Error! selmahum_chef.xml file is missing!");
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
				if ("chef".equalsIgnoreCase(n.getNodeName()))
				{
					int id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
					ChefGroup group = new ChefGroup(id);
					group.pathPoints = new TreeMap<>();
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("pathPoint".equalsIgnoreCase(d.getNodeName()))
						{
							int order = Integer.parseInt(d.getAttributes().getNamedItem("order").getNodeValue());
							int x = Integer.parseInt(d.getAttributes().getNamedItem("x").getNodeValue());
							int y = Integer.parseInt(d.getAttributes().getNamedItem("y").getNodeValue());
							int z = Integer.parseInt(d.getAttributes().getNamedItem("z").getNodeValue());
							Location loc = new Location(x, y, z, 0);
							group.pathPoints.put(Integer.valueOf(order), loc);
						}
					}
					chefGroups.put(Integer.valueOf(id), group);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "[Sel Mahum Chefs]: Error while loading selmahum_chef.xml file: " + e.getMessage(), e);
		}
		calculateEscortSpawns();
		loadFireplaces();
		initSpawns();
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (npc.getNpcId() == SELMAHUM_CHEF)
		{
			ChefGroup group = getChefGroup(npc);
			if (group == null)
			{
				return null;
			}
			Location[] spawns = escortSpawns.get(group.id);
			for (int i = 0; i < 2; i++)
			{
				group.escorts[i] = addSpawn(SELMAHUM_ESCORT_GUARD, spawns[i].getX(), spawns[i].getY(), spawns[i].getZ(), spawns[i].getHeading(), false, 0L);
				group.escorts[i].getSpawn().stopRespawn();
				group.escorts[i].setIsNoRndWalk(true);
				group.escorts[i].setWalking();
				group.escorts[i].getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, group.chef);
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public final String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if (npc.getNpcId() == SELMAHUM_CHEF)
		{
			ChefGroup group = getChefGroup(npc);
			if (group != null)
			{
				if ((group.lastInvincible.get() < System.currentTimeMillis()) && (((npc.getCurrentHp() / npc.getMaxHp()) * 100.0D) < 50.0D))
				{
					group.lastInvincible.set(System.currentTimeMillis() + 600000L);
					SkillTable.getInstance().getInfo(5989, 1).getEffects(npc, npc);
				}
				else if (npc.getFirstEffect(5989) != null)
				{
					if ((group.chef.getTarget() != null) && (group.chef.getTarget().equals(attacker)) && (((attacker.getCurrentHp() / attacker.getMaxHp()) * 100.0D) < 90.0D))
					{
						if (!npc.isCastingNow())
						{
							npc.doCast(SkillTable.getInstance().getInfo(6330, 1));
						}
					}
				}
				
				for (L2Npc escort : group.escorts)
				{
					if (!escort.isInCombat())
					{
						escort.setRunning();
						((L2Attackable) escort).addDamageHate(attacker, 0, 500);
						escort.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
					}
				}
			}
		}
		else if (npc.getNpcId() == SELMAHUM_ESCORT_GUARD)
		{
			ChefGroup group = getChefGroup(npc);
			if ((group != null) && (!group.chef.isDead()) && (!group.chef.isInCombat()))
			{
				group.chef.setRunning();
				((L2Attackable) group.chef).addDamageHate(attacker, 0, 500);
				group.chef.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
			}
			
			if ((group != null) && (group.escorts != null))
			{
				for (L2Npc escort : group.escorts)
				{
					if (!escort.isInCombat())
					{
						escort.setRunning();
						((L2Attackable) escort).addDamageHate(attacker, 0, 500);
						escort.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public final String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (npc.getNpcId() == SELMAHUM_CHEF)
		{
			ChefGroup group = getChefGroup(npc);
			if (group != null)
			{
				for (L2Npc escort : group.escorts)
				{
					if ((escort != null) && (!npc.isDead()))
					{
						escort.deleteMe();
					}
				}
			}
		}
		return null;
	}
	
	protected boolean doFireplace(ChefGroup group)
	{
		if (!group.atFirePlace)
		{
			for (L2Npc fire : fireplaces.keySet())
			{
				if ((Util.calculateDistance(group.chef, fire, true) < 400.0D) && (fire.getObjectId() != group.lastFirePlaceId) && (fireplaces.get(fire).intValue() == 0))
				{
					group.atFirePlace = true;
					int xDiff = (group.chef.getX() - fire.getX()) > 0 ? -Rnd.get(30, 40) : Rnd.get(30, 40);
					int yDiff = (group.chef.getY() - fire.getY()) > 0 ? -Rnd.get(30, 40) : Rnd.get(30, 40);
					group.chef.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(fire.getX() - xDiff, fire.getY() - yDiff, fire.getZ(), calculateHeading(group.chef, fire)));
					fireplaces.replace(fire, Integer.valueOf(1));
					group.chef.broadcastPacket(new NpcSay(group.chef.getObjectId(), 0, group.chef.getNpcId(), CHEF_FSTRINGS[Rnd.get(2)]));
					ThreadPoolManager.getInstance().scheduleAi(new FireplaceTask(group, fire), 1000L);
					break;
				}
			}
			if (group.atFirePlace)
			{
				return true;
			}
		}
		return false;
	}
	
	private ChefGroup getChefGroup(L2Npc npc)
	{
		if ((npc == null) || ((npc.getNpcId() != SELMAHUM_CHEF) && (npc.getNpcId() != SELMAHUM_ESCORT_GUARD)))
		{
			return null;
		}
		for (ChefGroup group : chefGroups.values())
		{
			if ((npc.getNpcId() == SELMAHUM_CHEF) && (npc.equals(group.chef)))
			{
				return group;
			}
			if (npc.getNpcId() == SELMAHUM_ESCORT_GUARD)
			{
				for (L2Npc escort : group.escorts)
				{
					if (npc.equals(escort))
					{
						return group;
					}
				}
			}
		}
		return null;
	}
	
	protected int getNextPoint(ChefGroup group, int currentPoint)
	{
		if (group.pathPoints.lastKey().intValue() == currentPoint)
		{
			group.reverseDirection = true;
		}
		else if (group.pathPoints.firstKey().intValue() == currentPoint)
		{
			group.reverseDirection = false;
		}
		
		if (group.reverseDirection)
		{
			return group.pathPoints.lowerKey(Integer.valueOf(currentPoint)).intValue();
		}
		return group.pathPoints.higherKey(Integer.valueOf(currentPoint)).intValue();
	}
	
	private void initSpawns()
	{
		for (Object element : chefGroups.keySet())
		{
			int groupId = ((Integer) element).intValue();
			ChefGroup group = chefGroups.get(Integer.valueOf(groupId));
			Location spawn = group.pathPoints.firstEntry().getValue();
			group.chef = addSpawn(SELMAHUM_CHEF, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getHeading(), false, 0L);
			group.chef.getSpawn().setAmount(1);
			group.chef.getSpawn().startRespawn();
			group.chef.getSpawn().setRespawnDelay(60);
			group.chef.setWalking();
			group.escorts = new L2Npc[2];
			Location[] spawns = escortSpawns.get(groupId);
			for (int i = 0; i < 2; i++)
			{
				group.escorts[i] = addSpawn(SELMAHUM_ESCORT_GUARD, spawns[i].getX(), spawns[i].getY(), spawns[i].getZ(), spawns[i].getHeading(), false, 0L);
				group.escorts[i].getSpawn().stopRespawn();
				group.escorts[i].setIsNoRndWalk(true);
				group.escorts[i].setWalking();
				group.escorts[i].getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, group.chef);
			}
		}
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new WalkTask(), 180000L, 2500L);
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new RangeCheckTask(), 180000L, 1000L);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return super.onFirstTalk(npc, player);
	}
	
	protected int calculateHeading(Location fromLoc, Location toLoc)
	{
		return Util.calculateHeadingFrom(fromLoc.getX(), fromLoc.getY(), toLoc.getX(), toLoc.getY());
	}
	
	private int calculateHeading(L2Character fromLoc, L2Character toLoc)
	{
		return Util.calculateHeadingFrom(fromLoc.getX(), fromLoc.getY(), toLoc.getX(), toLoc.getY());
	}
	
	private void loadFireplaces()
	{
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(CAMP_FIRE))
		{
			if (spawn != null)
			{
				spawn.getLastSpawn().setDisplayEffect(0);
				fireplaces.put(spawn.getLastSpawn(), Integer.valueOf(0));
				spawn.getLastSpawn().isShowName();
			}
		}
	}
	
	private void calculateEscortSpawns()
	{
		for (Object element : chefGroups.keySet())
		{
			int groupId = ((Integer) element).intValue();
			ChefGroup group = chefGroups.get(Integer.valueOf(groupId));
			Location loc = group.pathPoints.firstEntry().getValue();
			double chefAngle = Util.convertHeadingToDegree(loc.getHeading());
			chefAngle += 180.0D;
			if (chefAngle > 359.0D)
			{
				chefAngle -= 360.0D;
			}
			int xDirection = (chefAngle <= 90.0D) || (chefAngle >= 270.0D) ? 1 : -1;
			int yDirection = (chefAngle >= 180.0D) && (chefAngle < 360.0D) ? -1 : 1;
			Location[] spawnLocs = new Location[2];
			spawnLocs[0] = new Location(loc.getX() + (xDirection * ((int) Math.sin(45.0D) * 100)), loc.getY() + (yDirection * ((int) Math.cos(45.0D) * 100)), loc.getZ(), loc.getHeading());
			spawnLocs[1] = new Location(loc.getX() - (xDirection * ((int) Math.sin(45.0D) * 100)), loc.getY() + (yDirection * ((int) Math.cos(45.0D) * 100)), loc.getZ(), loc.getHeading());
			/*
			 * spawnLocs[0].setX(loc.getX() + (xDirection * ((int) Math.sin(45.0D) * 100))); spawnLocs[0].setY(loc.getY() + (yDirection * ((int) Math.cos(45.0D) * 100))); spawnLocs[0].setZ(loc.getZ()); spawnLocs[0].setHeading(loc.getHeading()); spawnLocs[1] = new Location();
			 * spawnLocs[1].setX(loc.getX() - (xDirection * ((int) Math.sin(45.0D) * 100))); spawnLocs[1].setY(loc.getY() + (yDirection * ((int) Math.cos(45.0D) * 100))); spawnLocs[1].setZ(loc.getZ()); spawnLocs[1].setHeading(loc.getHeading());
			 */
			escortSpawns.put(groupId, spawnLocs);
		}
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		return "";
	}
	
	private class ReturnFromFireplace implements Runnable
	{
		private final L2MonsterInstance mob;
		
		protected ReturnFromFireplace(L2MonsterInstance mob)
		{
			this.mob = mob;
		}
		
		@Override
		public void run()
		{
			if ((this.mob != null) && (!this.mob.isInCombat()) && (!this.mob.isDead()))
			{
				if ((this.mob.getFirstEffect(SKILL_FULL) == null) && (this.mob.getFirstEffect(SKILL_TIRED) == null))
				{
					this.mob.setIsNoRndWalk(false);
					this.mob.setDisplayEffect(3);
					this.mob.returnToSpawn();
				}
				else
				{
					ThreadPoolManager.getInstance().scheduleAi(new ReturnFromFireplace(mob), 30000);
				}
			}
		}
	}
	
	private class MoveToFireplace implements Runnable
	{
		private final L2MonsterInstance mob;
		private final int type;
		
		protected MoveToFireplace(L2MonsterInstance mob, int type)
		{
			this.mob = mob;
			this.type = type;
		}
		
		@Override
		public void run()
		{
			if (this.mob.isMoving())
			{
				ThreadPoolManager.getInstance().scheduleAi(new MoveToFireplace(mob, type), 1000L);
			}
			else if ((!this.mob.isInCombat()) && (!this.mob.isDead()))
			{
				if (this.type == 0)
				{
					SkillTable.getInstance().getInfo(SKILL_TIRED, 1).getEffects(this.mob, this.mob);
					this.mob.setDisplayEffect(2);
				}
				else if (this.type == 1)
				{
					SkillTable.getInstance().getInfo(SKILL_FULL, 1).getEffects(this.mob, this.mob);
					this.mob.setDisplayEffect(1);
				}
				this.mob.getAI().setIntention(CtrlIntention.AI_INTENTION_REST);
				this.mob.setIsNoRndWalk(true);
				ThreadPoolManager.getInstance().scheduleAi(new ReturnFromFireplace(this.mob), 300000L);
			}
		}
	}
	
	private class MoveChefFromFireplace implements Runnable
	{
		private final SelMahumChefs.ChefGroup group;
		private final L2Npc fire;
		
		protected MoveChefFromFireplace(SelMahumChefs.ChefGroup group, L2Npc fire)
		{
			this.group = group;
			this.fire = fire;
		}
		
		@Override
		public void run()
		{
			this.group.atFirePlace = false;
			SelMahumChefs.fireplaces.replace(this.fire, Integer.valueOf(0));
		}
	}
	
	private class FireplaceTask implements Runnable
	{
		private final SelMahumChefs.ChefGroup group;
		private L2Npc fireplace;
		
		protected FireplaceTask(SelMahumChefs.ChefGroup group, L2Npc fireplace)
		{
			this.group = group;
			this.fireplace = fireplace;
		}
		
		@Override
		public void run()
		{
			if ((this.fireplace.getDisplayEffect() == 0) && (SelMahumChefs.fireplacesFeed.containsKey(this.fireplace)))
			{
				SelMahumChefs.fireplacesFeed.get(this.fireplace).deleteMe();
				SelMahumChefs.fireplacesFeed.remove(this.fireplace);
			}
			else if (this.fireplace.getDisplayEffect() == 0)
			{
				this.fireplace.setDisplayEffect(1);
				for (L2Character leader : this.group.chef.getKnownList().getKnownCharactersInRadius(1500L))
				{
					if ((leader instanceof L2MonsterInstance))
					{
						if (Util.contains(SelMahumChefs.SELMAHUM_SQUAD_LEADERS, ((L2MonsterInstance) leader).getNpcId()))
						{
							if ((!leader.isInCombat()) && (!leader.isDead()) && (leader.getFirstEffect(SKILL_TIRED) == null) && (Util.calculateDistance(this.fireplace, leader, true) > 300.0D))
							{
								int rndX = Rnd.get(100) < 50 ? -Rnd.get(50, 100) : Rnd.get(50, 100);
								int rndY = Rnd.get(100) < 50 ? -Rnd.get(50, 100) : Rnd.get(50, 100);
								Location fireplaceLoc = new Location(this.fireplace.getX(), this.fireplace.getY(), this.fireplace.getZ());
								Location leaderLoc = new Location(this.fireplace.getX() + rndX, this.fireplace.getY() + rndY, this.fireplace.getZ());
								L2CharPosition position = new L2CharPosition(this.fireplace.getX() + rndX, this.fireplace.getY() + rndY, this.fireplace.getZ(), calculateHeading(leaderLoc, fireplaceLoc));
								leader.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, position);
								ThreadPoolManager.getInstance().scheduleAi(new MoveToFireplace((L2MonsterInstance) leader, 1), 100);
							}
						}
					}
				}
			}
			else if ((this.fireplace.getDisplayEffect() == 1) && (!SelMahumChefs.fireplacesFeed.containsKey(this.fireplace)))
			{
				L2Npc feed = addSpawn(FIRE_FEED, this.fireplace.getX(), this.fireplace.getY(), this.fireplace.getZ(), 0, false, 0L, false);
				feed.isShowName();
				SelMahumChefs.fireplacesFeed.put(this.fireplace, feed);
				for (L2Character leader : this.group.chef.getKnownList().getKnownCharactersInRadius(1500L))
				{
					if ((leader instanceof L2MonsterInstance))
					{
						if (Util.contains(SelMahumChefs.SELMAHUM_SQUAD_LEADERS, ((L2MonsterInstance) leader).getNpcId()))
						{
							if ((!leader.isInCombat()) && (!leader.isDead()) && (leader.getFirstEffect(SKILL_FULL) == null) && (Util.calculateDistance(this.fireplace, leader, true) > 300.0D))
							{
								int rndX = Rnd.get(100) < 50 ? -Rnd.get(50, 100) : Rnd.get(50, 100);
								int rndY = Rnd.get(100) < 50 ? -Rnd.get(50, 100) : Rnd.get(50, 100);
								Location fireplaceLoc = new Location(this.fireplace.getX(), this.fireplace.getY(), this.fireplace.getZ());
								Location leaderLoc = new Location(this.fireplace.getX() + rndX, this.fireplace.getY() + rndY, this.fireplace.getZ());
								L2CharPosition position = new L2CharPosition(this.fireplace.getX() + rndX, this.fireplace.getY() + rndY, this.fireplace.getZ(), calculateHeading(leaderLoc, fireplaceLoc));
								leader.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, position);
								ThreadPoolManager.getInstance().scheduleAi(new MoveToFireplace((L2MonsterInstance) leader, 1), 100);
							}
						}
					}
				}
			}
			else if ((this.fireplace.getDisplayEffect() == 1) && (SelMahumChefs.fireplacesFeed.containsKey(this.fireplace)))
			{
				L2Npc feed = SelMahumChefs.fireplacesFeed.get(this.fireplace);
				SelMahumChefs.fireplacesFeed.remove(this.fireplace);
				SelMahumChefs.fireplaces.remove(this.fireplace);
				L2Npc fire = addSpawn(CAMP_FIRE, this.fireplace);
				fire.isShowName();
				fire.setDisplayEffect(0);
				this.fireplace.deleteMe();
				SelMahumChefs.fireplaces.put(fire, Integer.valueOf(1));
				SelMahumChefs.fireplacesFeed.put(fire, feed);
				this.fireplace = fire;
				this.fireplace.setDisplayEffect(0);
			}
			this.group.lastFirePlaceId = this.fireplace.getObjectId();
			ThreadPoolManager.getInstance().scheduleAi(new MoveChefFromFireplace(group, fireplace), 10000L);
		}
	}
	
	protected class RangeCheckTask implements Runnable
	{
		protected RangeCheckTask()
		{
		}
		
		@Override
		public void run()
		{
			for (Object element : SelMahumChefs.chefGroups.keySet())
			{
				int groupId = ((Integer) element).intValue();
				
				SelMahumChefs.ChefGroup group = SelMahumChefs.chefGroups.get(Integer.valueOf(groupId));
				if ((!group.chef.isInCombat()) && (!group.chef.isDead()))
				{
					for (L2Npc escort : group.escorts)
					{
						if ((escort != null) && (!escort.isDead()))
						{
							if (Util.checkIfInRange(150, escort, group.chef, false))
							{
								escort.setWalking();
							}
							else
							{
								escort.setRunning();
							}
							if (!escort.getAI().getIntention().equals(CtrlIntention.AI_INTENTION_FOLLOW))
							{
								escort.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, group.chef);
							}
							MoveToLocation mov1 = new MoveToLocation(escort);
							escort.broadcastPacket(mov1);
						}
					}
				}
			}
		}
	}
	
	protected class WalkTask implements Runnable
	{
		protected WalkTask()
		{
		}
		
		@Override
		public void run()
		{
			for (Object element : SelMahumChefs.chefGroups.keySet())
			{
				int groupId = ((Integer) element).intValue();
				
				SelMahumChefs.ChefGroup group = SelMahumChefs.chefGroups.get(Integer.valueOf(groupId));
				if ((group.chef.isInCombat()) || (group.chef.isDead()) || (group.chef.isMoving()) || (group.atFirePlace))
				{
					if (group.chef.isMoving())
					{
						MoveToLocation mov = new MoveToLocation(group.chef);
						group.chef.broadcastPacket(mov);
					}
					
				}
				else if (!doFireplace(group))
				{
					group.currentPoint = getNextPoint(group, group.currentPoint);
					Location loc = group.pathPoints.get(Integer.valueOf(group.currentPoint));
					int nextPathPoint = getNextPoint(group, group.currentPoint);
					loc.setHeading(calculateHeading(loc, group.pathPoints.get(Integer.valueOf(nextPathPoint))));
					group.chef.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getHeading()));
				}
			}
		}
	}
	
	private class ChefGroup
	{
		public final int id;
		public L2Npc chef;
		public L2Npc[] escorts;
		public int currentPoint = 0;
		public boolean atFirePlace = false;
		public int lastFirePlaceId = 0;
		public AtomicLong lastInvincible = new AtomicLong();
		public boolean reverseDirection = false;
		public TreeMap<Integer, Location> pathPoints;
		
		public ChefGroup(int id)
		{
			this.id = id;
			this.lastInvincible.set(0);
		}
	}
}