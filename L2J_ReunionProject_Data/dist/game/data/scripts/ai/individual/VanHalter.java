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
package ai.individual;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;

import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.ai.CtrlIntention;
import l2r.gameserver.datatables.DoorTable;
import l2r.gameserver.datatables.NpcTable;
import l2r.gameserver.datatables.SkillTable;
import l2r.gameserver.datatables.SpawnTable;
import l2r.gameserver.instancemanager.GrandBossManager;
import l2r.gameserver.model.L2CharPosition;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.StatsSet;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2DoorInstance;
import l2r.gameserver.model.actor.instance.L2GrandBossInstance;
import l2r.gameserver.model.actor.instance.L2MonsterInstance;
import l2r.gameserver.model.actor.instance.L2NpcInstance;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.model.zone.type.L2BossZone;
import l2r.gameserver.network.serverpackets.CameraMode;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.SpecialCamera;
import l2r.gameserver.util.Util;
import l2r.util.Rnd;

public class VanHalter extends AbstractNpcAI
{
	// mob ids
	int[] triols =
	{
		32058,
		32059,
		32060,
		32061,
		32062,
		32063,
		32064,
		32065,
		32066
	};
	
	// mob locations
	protected int[][] mobLocs =
	{
		// 32051 de 0 a 3
		{
			32051,
			-14670,
			-54846,
			-10629,
			16384,
			60
		},
		{
			32051,
			-15548,
			-54836,
			-10448,
			16384,
			60
		},
		{
			32051,
			-18123,
			-54846,
			-10629,
			16384,
			60
		},
		{
			32051,
			-17248,
			-54836,
			-10448,
			16384,
			60
		},
		// 22175 de 4 a 19
		{
			22175,
			-14960,
			-53437,
			-10629,
			7820,
			60
		},
		{
			22175,
			-14964,
			-53766,
			-10603,
			20066,
			60
		},
		{
			22175,
			-15225,
			-52968,
			-10603,
			55924,
			60
		},
		{
			22175,
			-15522,
			-52625,
			-10629,
			17737,
			60
		},
		{
			22175,
			-15676,
			-52576,
			-10603,
			23075,
			60
		},
		{
			22175,
			-15879,
			-52521,
			-10603,
			63322,
			60
		},
		{
			22175,
			-16420,
			-52481,
			-10603,
			4302,
			60
		},
		{
			22175,
			-16590,
			-52575,
			-10603,
			11742,
			60
		},
		{
			22175,
			-16835,
			-52485,
			-10603,
			40331,
			60
		},
		{
			22175,
			-17051,
			-52639,
			-10629,
			4607,
			60
		},
		{
			22175,
			-17461,
			-52839,
			-10603,
			13423,
			60
		},
		{
			22175,
			-17604,
			-53050,
			-10603,
			39469,
			60
		},
		{
			22175,
			-17641,
			-53350,
			-10629,
			14056,
			60
		},
		{
			22175,
			-17710,
			-53768,
			-10603,
			47067,
			60
		},
		{
			22175,
			-17753,
			-53950,
			-10629,
			14260,
			60
		},
		{
			22175,
			-17841,
			-54312,
			-10603,
			14180,
			60
		},
		// 22176 de 20 a 33
		{
			22176,
			-16156,
			-47121,
			-10821,
			16129,
			60
		},
		{
			22176,
			-16157,
			-46340,
			-10821,
			16468,
			60
		},
		{
			22176,
			-16164,
			-48534,
			-10917,
			16405,
			60
		},
		{
			22176,
			-16165,
			-49237,
			-10917,
			16091,
			60
		},
		{
			22176,
			-16166,
			-47732,
			-10821,
			16430,
			60
		},
		{
			22176,
			-16177,
			-49925,
			-10917,
			16622,
			60
		},
		{
			22176,
			-16198,
			-44753,
			-10725,
			16583,
			60
		},
		{
			22176,
			-16497,
			-48344,
			-10917,
			16215,
			60
		},
		{
			22176,
			-16513,
			-49019,
			-10917,
			15756,
			60
		},
		{
			22176,
			-16529,
			-46310,
			-10821,
			17047,
			60
		},
		{
			22176,
			-16530,
			-47027,
			-10821,
			16487,
			60
		},
		{
			22176,
			-16532,
			-47633,
			-10821,
			16242,
			60
		},
		{
			22176,
			-16552,
			-49694,
			-10917,
			15784,
			60
		},
		{
			22176,
			-16594,
			-45094,
			-10725,
			16166,
			60
		},
		// 32058-32068 de 34 a 44
		{
			32058,
			-12674,
			-52673,
			-10932,
			16384
		},
		{
			32059,
			-12728,
			-54317,
			-11108,
			16384
		},
		{
			32060,
			-14936,
			-53112,
			-11559,
			16384
		},
		{
			32061,
			-15658,
			-53668,
			-11579,
			16384
		},
		{
			32062,
			-16404,
			-53263,
			-11559,
			16384
		},
		{
			32063,
			-17146,
			-53238,
			-11559,
			16384
		},
		{
			32064,
			-17887,
			-53137,
			-11559,
			16384
		},
		{
			32065,
			-20082,
			-54314,
			-11106,
			16384
		},
		{
			32066,
			-20081,
			-52674,
			-10921,
			16384
		},
		{
			32067,
			-16413,
			-56569,
			-10849,
			16384
		},
		{
			32068,
			-16397,
			-54119,
			-10475,
			16384
		}
	};
	
	// list of intruders.
	protected Map<Integer, List<L2PcInstance>> _bleedingPlayers = new FastMap<>();
	
	// spawn data of monsters
	protected Map<Integer, L2Spawn> _monsterSpawn = new FastMap<>();
	protected List<L2Spawn> _royalGuardSpawn = new FastList<>();
	protected List<L2Spawn> _royalGuardCaptainSpawn = new FastList<>();
	protected List<L2Spawn> _royalGuardHelperSpawn = new FastList<>();
	protected List<L2Spawn> _triolRevelationSpawn = new FastList<>();
	protected List<L2Spawn> _triolRevelationAlive = new FastList<>();
	protected List<L2Spawn> _guardOfAltarSpawn = new FastList<>();
	
	protected List<L2Spawn> _cameraMarkerSpawn = new FastList<>();
	
	protected L2Spawn _ritualOfferingSpawn = null;
	protected L2Spawn _ritualSacrificeSpawn = null;
	protected L2Spawn _vanHalterSpawn = null;
	
	// instance of monsters
	protected List<L2MonsterInstance> _monsters = new FastList<>();
	protected List<L2MonsterInstance> _royalGuard = new FastList<>();
	protected List<L2MonsterInstance> _royalGuardCaptain = new FastList<>();
	protected List<L2MonsterInstance> _royalGuardHepler = new FastList<>();
	protected List<L2MonsterInstance> _triolRevelation = new FastList<>();
	protected List<L2NpcInstance> _guardOfAltar = new FastList<>();
	
	protected List<L2NpcInstance> _cameraMarker = new FastList<>();
	
	protected List<L2DoorInstance> _doorOfAltar = new FastList<>();
	protected List<L2DoorInstance> _doorOfSacrifice = new FastList<>();
	protected L2Npc _ritualOffering = null;
	protected L2Npc _ritualSacrifice = null;
	protected L2GrandBossInstance _vanHalter = null;
	
	// Task
	protected ScheduledFuture<?> _movieTask = null;
	protected ScheduledFuture<?> _closeDoorOfAltarTask = null;
	protected ScheduledFuture<?> _openDoorOfAltarTask = null;
	protected ScheduledFuture<?> _callRoyalGuardHelperTask = null;
	protected ScheduledFuture<?> _wakeUpTask = null;
	protected ScheduledFuture<?> _timeUpTask = null;
	protected ScheduledFuture<?> _intervalTask = null;
	protected ScheduledFuture<?> _halterEscapeTask = null;
	protected ScheduledFuture<?> _setBleedTask = null;
	
	// state of High Priestess van Halter
	boolean _isLocked = false;
	boolean _isHalterSpawned = false;
	boolean _isSacrificeSpawned = false;
	boolean _isCaptainSpawned = false;
	boolean _isHelperCalled = false;
	
	private static final byte WAITING = 0;
	private static final byte FIGHT = 1;
	private static final byte DEAD = 2;
	
	protected L2BossZone _Zone = GrandBossManager.getInstance().getZone(-16373, -53562, -10300);
	
	// initialize
	public VanHalter(int id, String name, String descr)
	{
		super(name, descr);
		int[] mobs =
		{
			29062,
			22188,
			32058,
			32059,
			32060,
			32061,
			32062,
			32063,
			32064,
			32065,
			32066
		};
		this.registerMobs(mobs);
		
		// init doors
		_doorOfAltar.add(DoorTable.getInstance().getDoor(19160014));
		_doorOfAltar.add(DoorTable.getInstance().getDoor(19160015));
		_doorOfSacrifice.add(DoorTable.getInstance().getDoor(19160016));
		_doorOfSacrifice.add(DoorTable.getInstance().getDoor(19160017));
		
		// load spawn data of monsters.
		loadRoyalGuard();
		loadTriolRevelation();
		loadRoyalGuardCaptain();
		loadRoyalGuardHelper();
		loadGuardOfAltar();
		loadVanHalter();
		loadRitualOffering();
		loadRitualSacrifice();
		
		StatsSet info = GrandBossManager.getInstance().getStatsSet(29062);
		int _state = GrandBossManager.getInstance().getBossStatus(29062);
		
		if (_state == WAITING)
		{
			// spawn mobs
			// cerrar puerta sacrifice
			// schedule puerta altar
			setupAltar(false);
		}
		else if (_state == FIGHT)
		{
			// repone hp/mp
			// schedule 2 horas de timeup
			setupAltar(true);
		}
		else if (_state == DEAD)
		{
			// load the respawn date and time for Van Halter from DB
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			if (temp > 0)
			{
				// el respawn no ha pasado, schedule setupAltar para temp
				_intervalTask = ThreadPoolManager.getInstance().scheduleGeneral(new Init(false), temp);
			}
			else
			{
				// el tiempo de respawn ha pasado
				setupAltar(false);
			}
		}
		
		// setting spawn data of Dummy camera marker.
		_cameraMarkerSpawn.clear();
		try
		{
			// Dummy npc
			L2NpcTemplate template1 = NpcTable.getInstance().getTemplate(13014);
			L2Spawn tempSpawn;
			
			// Dummy camera marker.
			tempSpawn = new L2Spawn(template1);
			tempSpawn.setLocx(-16397);
			tempSpawn.setLocy(-55200);
			tempSpawn.setLocz(-10449);
			tempSpawn.setHeading(16384);
			tempSpawn.setAmount(1);
			tempSpawn.setRespawnDelay(60000);
			SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
			
			_cameraMarkerSpawn.add(tempSpawn);
			
			template1 = NpcTable.getInstance().getTemplate(13014);
			tempSpawn = new L2Spawn(template1);
			tempSpawn.setLocx(-16397);
			tempSpawn.setLocy(-55200);
			tempSpawn.setLocz(-10051);
			tempSpawn.setHeading(16384);
			tempSpawn.setAmount(1);
			tempSpawn.setRespawnDelay(60000);
			SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
			
			_cameraMarkerSpawn.add(tempSpawn);
			
			template1 = NpcTable.getInstance().getTemplate(13014);
			tempSpawn = new L2Spawn(template1);
			tempSpawn.setLocx(-16397);
			tempSpawn.setLocy(-55200);
			tempSpawn.setLocz(-9741);
			tempSpawn.setHeading(16384);
			tempSpawn.setAmount(1);
			tempSpawn.setRespawnDelay(60000);
			SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
			
			_cameraMarkerSpawn.add(tempSpawn);
			
			template1 = NpcTable.getInstance().getTemplate(13014);
			tempSpawn = new L2Spawn(template1);
			tempSpawn.setLocx(-16397);
			tempSpawn.setLocy(-55200);
			tempSpawn.setLocz(-9394);
			tempSpawn.setHeading(16384);
			tempSpawn.setAmount(1);
			tempSpawn.setRespawnDelay(60000);
			SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
			
			_cameraMarkerSpawn.add(tempSpawn);
			
			template1 = NpcTable.getInstance().getTemplate(13014);
			tempSpawn = new L2Spawn(template1);
			tempSpawn.setLocx(-16397);
			tempSpawn.setLocy(-55197);
			tempSpawn.setLocz(-8739);
			tempSpawn.setHeading(16384);
			tempSpawn.setAmount(1);
			tempSpawn.setRespawnDelay(60000);
			SpawnTable.getInstance().addNewSpawn(tempSpawn, false);
			
			_cameraMarkerSpawn.add(tempSpawn);
			
		}
		catch (Exception e)
		{
			_log.warning("VanHalterManager: Problem with loading camera: " + e.getMessage());
		}
		
		// check state of High Priestess van Halter.
		_log.info("VanHalterManager: State of High Priestess van Halter is " + _state + ".");
	}
	
	// Load Royal Guard
	protected void loadRoyalGuard()
	{
		_royalGuardSpawn.clear();
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(22175);
			if (template1 != null)
			{
				for (int i = 4; i < 20; i++)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(1);
					spawnDat.setLocx(mobLocs[i][0]);
					spawnDat.setLocy(mobLocs[i][1]);
					spawnDat.setLocz(mobLocs[i][2]);
					spawnDat.setHeading(mobLocs[i][3]);
					spawnDat.setRespawnDelay(mobLocs[i][4]);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_royalGuardSpawn.add(spawnDat);
				}
			}
			else
			{
				_log.warning("VanHalterManager.loadRoyalGuard: Data missing in NPC table for ID: " + 22175 + ".");
			}
			_log.info("VanHalterManager: Loaded " + _royalGuardSpawn.size() + " Royal Guard spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(22176);
			if (template1 != null)
			{
				for (int i = 20; i < 34; i++)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(1);
					spawnDat.setLocx(mobLocs[i][0]);
					spawnDat.setLocy(mobLocs[i][1]);
					spawnDat.setLocz(mobLocs[i][2]);
					spawnDat.setHeading(mobLocs[i][3]);
					spawnDat.setRespawnDelay(mobLocs[i][4]);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_royalGuardSpawn.add(spawnDat);
				}
			}
			else
			{
				_log.warning("VanHalterManager.loadRoyalGuard: Data missing in NPC table for ID: " + 22176 + ".");
			}
			_log.info("VanHalterManager: Loaded " + _royalGuardSpawn.size() + " Royal Guard spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnRoyalGuard()
	{
		if (!_royalGuard.isEmpty())
			deleteRoyalGuard();
		
		for (L2Spawn rgs : _royalGuardSpawn)
		{
			rgs.startRespawn();
			_royalGuard.add((L2MonsterInstance) rgs.doSpawn());
		}
	}
	
	protected void deleteRoyalGuard()
	{
		for (L2MonsterInstance rg : _royalGuard)
		{
			rg.getSpawn().stopRespawn();
			rg.deleteMe();
		}
		
		_royalGuard.clear();
	}
	
	// load Triol's Revelation.
	protected void loadTriolRevelation()
	{
		_triolRevelationSpawn.clear();
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			for (int i = 32058; i <= 32068; i++)
			{
				template1 = NpcTable.getInstance().getTemplate(i);
				if (template1 != null)
				{
					for (int j = 34; j < 45; j++)
					{
						spawnDat = new L2Spawn(template1);
						spawnDat.setAmount(1);
						spawnDat.setLocx(mobLocs[j][0]);
						spawnDat.setLocy(mobLocs[j][1]);
						spawnDat.setLocz(mobLocs[j][2]);
						spawnDat.setHeading(mobLocs[j][3]);
						SpawnTable.getInstance().addNewSpawn(spawnDat, false);
						_triolRevelationSpawn.add(spawnDat);
					}
				}
				else
				{
					_log.warning("VanHalterManager: Data missing in NPC table for ID: " + i + ".");
				}
			}
			
			_log.info("VanHalterManager: Loaded " + _triolRevelationSpawn.size() + " Triol's Revelation spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnTriolRevelation()
	{
		if (!_triolRevelation.isEmpty())
			deleteTriolRevelation();
		
		for (L2Spawn trs : _triolRevelationSpawn)
		{
			trs.startRespawn();
			_triolRevelation.add((L2MonsterInstance) trs.doSpawn());
			if (trs.getNpcid() != 32067 && trs.getNpcid() != 32068)
				_triolRevelationAlive.add(trs);
		}
	}
	
	protected void deleteTriolRevelation()
	{
		for (L2MonsterInstance tr : _triolRevelation)
		{
			tr.getSpawn().stopRespawn();
			tr.deleteMe();
		}
		_triolRevelation.clear();
	}
	
	// load Royal Guard Captain.
	protected void loadRoyalGuardCaptain()
	{
		_royalGuardCaptainSpawn.clear();
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(22188);
			if (template1 != null)
			{
				spawnDat = new L2Spawn(template1);
				spawnDat.setAmount(1);
				spawnDat.setLocx(-16385);
				spawnDat.setLocy(-52461);
				spawnDat.setLocz(-10629);
				spawnDat.setHeading(16384);
				spawnDat.setRespawnDelay(60);
				SpawnTable.getInstance().addNewSpawn(spawnDat, false);
				_royalGuardCaptainSpawn.add(spawnDat);
			}
			else
			{
				_log.warning("VanHalterManager: Data missing in NPC table for ID: " + 22188 + ".");
			}
			_log.info("VanHalterManager: Loaded " + _royalGuardCaptainSpawn.size() + " Royal Guard Captain spawn locations.");
		}
		catch (Exception e)
		{
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnRoyalGuardCaptain()
	{
		if (!_royalGuardCaptain.isEmpty())
			deleteRoyalGuardCaptain();
		
		for (L2Spawn trs : _royalGuardCaptainSpawn)
		{
			trs.startRespawn();
			_royalGuardCaptain.add((L2MonsterInstance) trs.doSpawn());
		}
		_isCaptainSpawned = true;
	}
	
	protected void deleteRoyalGuardCaptain()
	{
		for (L2MonsterInstance tr : _royalGuardCaptain)
		{
			tr.getSpawn().stopRespawn();
			tr.deleteMe();
		}
		
		_royalGuardCaptain.clear();
	}
	
	// load Royal Guard Helper.
	protected void loadRoyalGuardHelper()
	{
		_royalGuardHelperSpawn.clear();
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(22191);
			if (template1 != null)
			{
				spawnDat = new L2Spawn(template1);
				spawnDat.setAmount(1);
				spawnDat.setLocx(-16397);
				spawnDat.setLocy(-53199);
				spawnDat.setLocz(-10448);
				spawnDat.setHeading(16384);
				spawnDat.setRespawnDelay(60);
				SpawnTable.getInstance().addNewSpawn(spawnDat, false);
				_royalGuardHelperSpawn.add(spawnDat);
			}
			else
			{
				_log.warning("VanHalterManager: Data missing in NPC table for ID: " + 22191 + ".");
			}
			_log.info("VanHalterManager: Loaded " + _royalGuardHelperSpawn.size() + " Royal Guard Helper spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnRoyalGuardHepler()
	{
		for (L2Spawn trs : _royalGuardHelperSpawn)
		{
			trs.startRespawn();
			_royalGuardHepler.add((L2MonsterInstance) trs.doSpawn());
		}
	}
	
	protected void deleteRoyalGuardHepler()
	{
		for (L2MonsterInstance tr : _royalGuardHepler)
		{
			tr.getSpawn().stopRespawn();
			tr.deleteMe();
		}
		_royalGuardHepler.clear();
	}
	
	// load Guards Of Altar
	protected void loadGuardOfAltar()
	{
		_guardOfAltarSpawn.clear();
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(32051);
			if (template1 != null)
			{
				for (int i = 0; i < 4; i++)
				{
					spawnDat = new L2Spawn(template1);
					spawnDat.setAmount(1);
					spawnDat.setLocx(mobLocs[i][0]);
					spawnDat.setLocy(mobLocs[i][1]);
					spawnDat.setLocz(mobLocs[i][2]);
					spawnDat.setHeading(mobLocs[i][3]);
					SpawnTable.getInstance().addNewSpawn(spawnDat, false);
					_guardOfAltarSpawn.add(spawnDat);
				}
			}
			else
			{
				_log.warning("VanHalterManager: Data missing in NPC table for ID: " + 32051 + ".");
			}
			_log.info("VanHalterManager: Loaded " + _guardOfAltarSpawn.size() + " Guard Of Altar spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnGuardOfAltar()
	{
		if (!_guardOfAltar.isEmpty())
			deleteGuardOfAltar();
		
		for (L2Spawn trs : _guardOfAltarSpawn)
		{
			trs.startRespawn();
			_guardOfAltar.add((L2NpcInstance) trs.doSpawn());
		}
	}
	
	protected void deleteGuardOfAltar()
	{
		for (L2NpcInstance tr : _guardOfAltar)
		{
			tr.getSpawn().stopRespawn();
			tr.deleteMe();
		}
		
		_guardOfAltar.clear();
	}
	
	// load High Priestess van Halter.
	protected void loadVanHalter()
	{
		_vanHalterSpawn = null;
		
		L2Spawn spawnDat;
		L2NpcTemplate template1;
		try
		{
			template1 = NpcTable.getInstance().getTemplate(29062);
			if (template1 != null)
			{
				spawnDat = new L2Spawn(template1);
				spawnDat.setAmount(1);
				spawnDat.setLocx(-16397);
				spawnDat.setLocy(-53308);
				spawnDat.setLocz(-10448);
				spawnDat.setHeading(16384);
				spawnDat.setRespawnDelay(172800);
				SpawnTable.getInstance().addNewSpawn(spawnDat, false);
				_vanHalterSpawn = spawnDat;
			}
			else
			{
				_log.warning("VanHalterManager: Data missing in NPC table for ID: " + 29062 + ".");
			}
			_log.info("VanHalterManager: Loaded High Priestess van Halter spawn.");
		}
		catch (Exception e)
		{
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnVanHalter()
	{
		_vanHalter = (L2GrandBossInstance) _vanHalterSpawn.doSpawn();
		_vanHalter.setIsImmobilized(true);
		_vanHalter.setIsInvul(true);
		_isHalterSpawned = true;
		GrandBossManager.getInstance().addBoss(_vanHalter);
	}
	
	protected void deleteVanHalter()
	{
		_vanHalter.setIsImmobilized(false);
		_vanHalter.setIsInvul(false);
		_vanHalter.getSpawn().stopRespawn();
		_vanHalter.deleteMe();
		_isHalterSpawned = false;
	}
	
	// load Ritual Offering.
	protected void loadRitualOffering()
	{
		_ritualOfferingSpawn = null;
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(32038);
			if (template1 != null)
			{
				spawnDat = new L2Spawn(template1);
				spawnDat.setAmount(1);
				spawnDat.setLocx(-16397);
				spawnDat.setLocy(-53199);
				spawnDat.setLocz(-10448);
				spawnDat.setHeading(16384);
				spawnDat.setRespawnDelay(172800);
				SpawnTable.getInstance().addNewSpawn(spawnDat, false);
				_ritualOfferingSpawn = spawnDat;
			}
			else
			{
				_log.warning("VanHalterManager: Data missing in NPC table for ID: " + 32038 + ".");
			}
			
			_log.info("VanHalterManager: Loaded Ritual Offering spawn locations.");
		}
		catch (Exception e)
		{
			_log.warning("VanHalterManager.loadRitualOffering: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnRitualOffering()
	{
		_ritualOffering = _ritualOfferingSpawn.doSpawn();
		_ritualOffering.setIsImmobilized(true);
		_ritualOffering.setIsInvul(true);
		_ritualOffering.setIsParalyzed(true);
	}
	
	protected void deleteRitualOffering()
	{
		_ritualOffering.setIsImmobilized(false);
		_ritualOffering.setIsInvul(false);
		_ritualOffering.setIsParalyzed(false);
		_ritualOffering.getSpawn().stopRespawn();
		_ritualOffering.deleteMe();
	}
	
	// Load Ritual Sacrifice.
	protected void loadRitualSacrifice()
	{
		_ritualSacrificeSpawn = null;
		
		try
		{
			L2Spawn spawnDat;
			L2NpcTemplate template1;
			
			template1 = NpcTable.getInstance().getTemplate(22195);
			if (template1 != null)
			{
				spawnDat = new L2Spawn(template1);
				spawnDat.setAmount(1);
				spawnDat.setLocx(-16397);
				spawnDat.setLocy(-53199);
				spawnDat.setLocz(-10448);
				spawnDat.setHeading(16384);
				spawnDat.setRespawnDelay(172800);
				SpawnTable.getInstance().addNewSpawn(spawnDat, false);
				_ritualSacrificeSpawn = spawnDat;
			}
			else
			{
				_log.warning("VanHalterManager: Data missing in NPC table for ID: " + 22195 + ".");
			}
			_log.info("VanHalterManager: Loaded Ritual Sacrifice spawn locations.");
		}
		catch (Exception e)
		{
			// problem with initializing spawn, go to next one
			_log.warning("VanHalterManager: Spawn could not be initialized: " + e);
		}
	}
	
	protected void spawnRitualSacrifice()
	{
		_ritualSacrifice = _ritualSacrificeSpawn.doSpawn();
		_ritualSacrifice.setIsImmobilized(true);
		_ritualSacrifice.setIsInvul(true);
		_isSacrificeSpawned = true;
	}
	
	protected void deleteRitualSacrifice()
	{
		if (!_isSacrificeSpawned)
			return;
		
		_ritualSacrifice.getSpawn().stopRespawn();
		_ritualSacrifice.deleteMe();
		_isSacrificeSpawned = false;
	}
	
	protected void spawnCameraMarker()
	{
		
		if (!_cameraMarker.isEmpty())
			deleteCameraMarker();
		
		for (int i = 0; i < _cameraMarkerSpawn.size(); i++)
		{
			_cameraMarker.add((L2NpcInstance) _cameraMarkerSpawn.get(i).doSpawn());
			
			_cameraMarker.get(i).getSpawn().stopRespawn();
			_cameraMarker.get(i).setIsImmobilized(true);
		}
	}
	
	protected void deleteCameraMarker()
	{
		if (_cameraMarker.isEmpty())
			return;
		
		for (int i = 0; i < _cameraMarker.size(); i++)
		
		{
			_cameraMarker.get(i).deleteMe();
		}
		_cameraMarker.clear();
	}
	
	// door control.
	
	/*
	 * Puerta de entrada a la zona del altar si loop==true inicia schedule para cerrar la puerta pasados 3 min (180000 millis, no?)
	 */
	protected void openDoorOfAltar(boolean loop)
	{
		for (L2DoorInstance door : _doorOfAltar)
		{
			try
			{
				door.openMe();
			}
			catch (Exception e)
			{
				_log.severe(e.getMessage());
			}
		}
		
		if (loop)
		{
			_isLocked = false;
			
			if (_closeDoorOfAltarTask != null)
				_closeDoorOfAltarTask.cancel(false);
			_closeDoorOfAltarTask = null;
			_closeDoorOfAltarTask = ThreadPoolManager.getInstance().scheduleGeneral(new CloseDoorOfAltar(), 180000);
		}
		else
		{
			if (_closeDoorOfAltarTask != null)
				_closeDoorOfAltarTask.cancel(false);
			_closeDoorOfAltarTask = null;
		}
	}
	
	public class OpenDoorOfAltar implements Runnable
	{
		@Override
		public void run()
		{
			openDoorOfAltar(true);
		}
	}
	
	// Lo mismo que el openDoorOfAltar solo que el schedule de 3 min es para abrir la puerta
	protected void closeDoorOfAltar(boolean loop)
	{
		for (L2DoorInstance door : _doorOfAltar)
		{
			door.closeMe();
		}
		
		if (loop)
		{
			if (_openDoorOfAltarTask != null)
				_openDoorOfAltarTask.cancel(false);
			_openDoorOfAltarTask = null;
			_openDoorOfAltarTask = ThreadPoolManager.getInstance().scheduleGeneral(new OpenDoorOfAltar(), 180000);
		}
		else
		{
			if (_openDoorOfAltarTask != null)
				_openDoorOfAltarTask.cancel(false);
			_openDoorOfAltarTask = null;
		}
	}
	
	public class CloseDoorOfAltar implements Runnable
	{
		@Override
		public void run()
		{
			closeDoorOfAltar(true);
		}
	}
	
	protected void openDoorOfSacrifice()
	{
		for (L2DoorInstance door : _doorOfSacrifice)
		{
			try
			{
				door.openMe();
			}
			catch (Exception e)
			{
				_log.severe(e.getMessage());
			}
		}
	}
	
	protected void closeDoorOfSacrifice()
	{
		for (L2DoorInstance door : _doorOfSacrifice)
		{
			try
			{
				door.closeMe();
			}
			catch (Exception e)
			{
				_log.severe(e.getMessage());
			}
		}
	}
	
	// event
	public void checkTriolRevelationDestroy()
	{
		if (_isCaptainSpawned)
			return;
		
		boolean isTriolRevelationDestroyed = true;
		for (L2Spawn tra : _triolRevelationAlive)
		{
			if (!tra.getLastSpawn().isDead())
				isTriolRevelationDestroyed = false;
		}
		
		if (isTriolRevelationDestroyed)
		{
			spawnRoyalGuardCaptain();
		}
	}
	
	public void checkRoyalGuardCaptainDestroy()
	{
		if (!_isHalterSpawned)
			return;
		
		deleteRoyalGuard();
		deleteRoyalGuardCaptain();
		spawnGuardOfAltar();
		openDoorOfSacrifice();
		
		CreatureSay cs = new CreatureSay(0, 1, "Altar's Gatekeeper", "The door of the 3rd floor in the altar has opened.");
		for (L2PcInstance pc : getPlayersInside())
		{
			pc.sendPacket(cs);
		}
		
		_vanHalter.setIsImmobilized(true);
		_vanHalter.setIsInvul(true);
		spawnCameraMarker();
		
		if (_timeUpTask != null)
			_timeUpTask.cancel(false);
		_timeUpTask = null;
		
		_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(1), 20000);
	}
	
	/**
	 * start fight against High Priestess van Halter
	 */
	protected void combatBeginning()
	{
		if (_timeUpTask != null)
			_timeUpTask.cancel(false);
		_timeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new TimeUp(), 7200000);
	}
	
	// call Royal Guard Helper and escape from player.
	public void callRoyalGuardHelper()
	{
		if (!_isHelperCalled)
		{
			_isHelperCalled = true;
			_halterEscapeTask = ThreadPoolManager.getInstance().scheduleGeneral(new HalterEscape(), 500);
			_callRoyalGuardHelperTask = ThreadPoolManager.getInstance().scheduleGeneral(new CallRoyalGuardHelper(), 1000);
		}
	}
	
	public class CallRoyalGuardHelper implements Runnable
	{
		@Override
		public void run()
		{
			spawnRoyalGuardHepler();
			
			if (_royalGuardHepler.size() <= 6 && !_vanHalter.isDead())
			{
				if (_callRoyalGuardHelperTask != null)
					_callRoyalGuardHelperTask.cancel(false);
				_callRoyalGuardHelperTask = ThreadPoolManager.getInstance().scheduleGeneral(new CallRoyalGuardHelper(), 10000);
			}
			else
			{
				if (_callRoyalGuardHelperTask != null)
					_callRoyalGuardHelperTask.cancel(false);
				_callRoyalGuardHelperTask = null;
			}
		}
	}
	
	public class HalterEscape implements Runnable
	{
		@Override
		public void run()
		{
			if (_royalGuardHepler.size() <= 6 && !_vanHalter.isDead())
			{
				if (_vanHalter.isAfraid())
				{
					_vanHalter.stopFear(true);
				}
				else
				{
					_vanHalter.startFear();
					if (_vanHalter.getZ() >= -10476)
					{
						L2CharPosition pos = new L2CharPosition(-16397, -53308, -10448, 0);
						if (_vanHalter.getX() == pos.x && _vanHalter.getY() == pos.y)
						{
							_vanHalter.stopFear(true);
						}
						else
						{
							_vanHalter.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
						}
					}
					else if (_vanHalter.getX() >= -16397)
					{
						L2CharPosition pos = new L2CharPosition(-15548, -54830, -10475, 0);
						_vanHalter.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
					}
					else
					{
						L2CharPosition pos = new L2CharPosition(-17248, -54830, -10475, 0);
						_vanHalter.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, pos);
					}
				}
				if (_halterEscapeTask != null)
					_halterEscapeTask.cancel(false);
				_halterEscapeTask = ThreadPoolManager.getInstance().scheduleGeneral(new HalterEscape(), 5000);
			}
			else
			{
				_vanHalter.stopFear(true);
				if (_halterEscapeTask != null)
					_halterEscapeTask.cancel(false);
				_halterEscapeTask = null;
			}
		}
	}
	
	// High Priestess van Halter dead or time up.
	public void enterInterval()
	{
		// cancel all task
		if (_callRoyalGuardHelperTask != null)
			_callRoyalGuardHelperTask.cancel(false);
		_callRoyalGuardHelperTask = null;
		
		if (_closeDoorOfAltarTask != null)
			_closeDoorOfAltarTask.cancel(false);
		_closeDoorOfAltarTask = null;
		
		if (_halterEscapeTask != null)
			_halterEscapeTask.cancel(false);
		_halterEscapeTask = null;
		
		if (_intervalTask != null)
			_intervalTask.cancel(false);
		_intervalTask = null;
		
		if (_movieTask != null)
			_movieTask.cancel(false);
		_movieTask = null;
		
		if (_openDoorOfAltarTask != null)
			_openDoorOfAltarTask.cancel(false);
		_openDoorOfAltarTask = null;
		
		if (_timeUpTask != null)
			_timeUpTask.cancel(false);
		_timeUpTask = null;
		
		// delete monsters
		if (_vanHalter.isDead())
		{
			_vanHalter.getSpawn().stopRespawn();
		}
		else
		{
			deleteVanHalter();
		}
		deleteRoyalGuardHepler();
		deleteRoyalGuardCaptain();
		deleteRoyalGuard();
		deleteRitualOffering();
		deleteRitualSacrifice();
		deleteGuardOfAltar();
		
		// set interval end.
		if (_intervalTask != null)
			_intervalTask.cancel(false);
		
		if (GrandBossManager.getInstance().getBossStatus(29062) == DEAD)
		{
			// si vanhalter ha muerto calcular respawn y abrir puertas
			int respawnTime = (Rnd.get(172800000, 172800000 + 8640000));
			StatsSet info = GrandBossManager.getInstance().getStatsSet(29062);
			info.set("respawn_time", (System.currentTimeMillis() + respawnTime));
			GrandBossManager.getInstance().setStatsSet(29062, info);
			openDoorOfAltar(false);
			openDoorOfSacrifice();
		}
		else if (GrandBossManager.getInstance().getBossStatus(29062) == FIGHT)
		{
			// si se ha pasado el tiempo y sigue vivo expulsar a los players y programar reinicio del altar
			// cerrar todas las puertas
			_intervalTask = ThreadPoolManager.getInstance().scheduleGeneral(new Init(false), 3600000);
			_Zone.oustAllPlayers();
			closeDoorOfAltar(false);
			closeDoorOfSacrifice();
		}
	}
	
	/**
	 * Init class. Llama al setupAltar para inizializar la zona del boss
	 */
	private class Init implements Runnable
	{
		public boolean isFighting;
		
		public Init(boolean fight)
		{
			isFighting = fight;
		}
		
		@Override
		public void run()
		{
			setupAltar(isFighting);
		}
	}
	
	/**
	 * Carga los spawns y programa el timeup si el boss
	 * estΞ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€
	 * �Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ
	 * �οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β
	 * ²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β
	 * ²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½
	 * Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�
	 * Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ
	 * ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ
	 * ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ
	 * �Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�
	 * Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ
	 * ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’
	 * Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ
	 * ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�
	 * οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�
	 * Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ
	 * ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½
	 * ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ
	 * ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ
	 * �Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�
	 * Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�
	 * οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ
	 * ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†
	 * Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½
	 * Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ
	 * ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²
	 * β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β
	 * ¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ
	 * ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β
	 * „ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ
	 * ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�
	 * Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β
	 * ½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ
	 * ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�
	 * οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ
	 * ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ
	 * ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�
	 * Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ
	 * �οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ
	 * �οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�
	 * Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ
	 * �οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬
	 * β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½
	 * Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�
	 * Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�
	 * οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ
	 * ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½
	 * ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ
	 * �οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ
	 * �Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�
	 * Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�
	 * οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²
	 * Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�
	 * οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ
	 * �Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ
	 * ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²
	 * β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ
	 * ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�
	 * Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ
	 * ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½
	 * Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ
	 * �β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β
	 * ½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½
	 * Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ
	 * ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�
	 * Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½
	 * Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ
	 * ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ
	 * �οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ
	 * �Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�
	 * Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�
	 * οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²
	 * Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†
	 * Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ
	 * ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ
	 * ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ
	 * �οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β
	 * ¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ
	 * ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β
	 * „ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�
	 * Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ
	 * ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�
	 * Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�
	 * Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ
	 * ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ
	 * �οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�
	 * οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β
	 * ½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ
	 * ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬
	 * ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�
	 * Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�
	 * οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ
	 * �οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ
	 * �β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β
	 * ¬Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ
	 * ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�β€™Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β
	 * ½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β²Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¬Ξ�οΏ½ΞΏΞ
	 * �Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½
	 * Ξ�Β²Ξ²β€�Β¬Ξ’Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ�οΏ½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β½Ξ�οΏ½ΞΏΞ�Β½Ξ�β€™Ξ’Β²Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�β€™Ξ’Β¬Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β€�Β¬ΞΏΞ�Β½Ξ�οΏ½Ξ²β‚¬Β Ξ�οΏ½ΞΏΞ�Β½Ξ�Ξ�Ξ�οΏ½Ξ’Β½Ξ�οΏ½Ξ’Β²Ξ�Β²Ξ²β‚¬οΏ½Ξ’Β¬Ξ�Β²Ξ²β‚¬οΏ½Ξ�β€ Ξ�οΏ½ΞΏΞ�Β½Ξ�Β²Ξ²β€�Β¬Ξ²β€�Ξ†Ξ�οΏ½Ξ²β‚¬β„ΆΞ�β€™Ξ’Β¦ siendo
	 * atacado
	 * @param isFighting
	 */
	public void setupAltar(boolean isFighting)
	{
		// cancel all task
		if (_callRoyalGuardHelperTask != null)
			_callRoyalGuardHelperTask.cancel(false);
		_callRoyalGuardHelperTask = null;
		
		if (_closeDoorOfAltarTask != null)
			_closeDoorOfAltarTask.cancel(false);
		_closeDoorOfAltarTask = null;
		
		if (_halterEscapeTask != null)
			_halterEscapeTask.cancel(false);
		_halterEscapeTask = null;
		
		if (_intervalTask != null)
			_intervalTask.cancel(false);
		_intervalTask = null;
		
		if (_movieTask != null)
			_movieTask.cancel(false);
		_movieTask = null;
		
		if (_openDoorOfAltarTask != null)
			_openDoorOfAltarTask.cancel(false);
		_openDoorOfAltarTask = null;
		
		if (_timeUpTask != null)
			_timeUpTask.cancel(false);
		_timeUpTask = null;
		
		// delete all monsters
		deleteVanHalter();
		deleteTriolRevelation();
		deleteRoyalGuardHepler();
		deleteRoyalGuardCaptain();
		deleteRoyalGuard();
		deleteRitualSacrifice();
		deleteRitualOffering();
		deleteGuardOfAltar();
		deleteCameraMarker();
		
		// clear flag.
		_isLocked = false;
		_isCaptainSpawned = false;
		_isHelperCalled = false;
		_isHalterSpawned = false;
		
		// set door state
		closeDoorOfSacrifice();
		openDoorOfAltar(true);
		
		// respawn monsters.
		spawnTriolRevelation();
		spawnRoyalGuard();
		spawnRitualOffering();
		spawnVanHalter();
		
		if (isFighting)
		{
			StatsSet info = GrandBossManager.getInstance().getStatsSet(29062);
			final int hp = info.getInteger("currentHP");
			final int mp = info.getInteger("currentMP");
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						_vanHalter.setCurrentHpMp(hp, mp);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}, 100L);
			
			if (_timeUpTask != null)
				_timeUpTask.cancel(false);
			// 6 hours
			_timeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new TimeUp(), 7200000);
		}
	}
	
	// time up.
	public class TimeUp implements Runnable
	{
		@Override
		public void run()
		{
			enterInterval();
		}
	}
	
	// appearance movie.
	private class Movie implements Runnable
	{
		private int _distance = 6502500;
		private int _taskId;
		private List<L2PcInstance> _players = getPlayersInside();
		
		public Movie(int taskId)
		{
			_taskId = taskId;
		}
		
		@Override
		public void run()
		{
			_vanHalter.setHeading(16384);
			_vanHalter.setTarget(_ritualOffering);
			
			switch (_taskId)
			{
				case 1:
					GrandBossManager.getInstance().setBossStatus(29062, WAITING);
					
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_vanHalter) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _vanHalter, 50, 90, 0, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(2), 16);
					
					break;
				
				case 2:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(4)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(4), 1842, 100, -3, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(3), 1);
					
					break;
				
				case 3:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(4)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(4), 1861, 97, -10, 1500, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(4), 1500);
					
					break;
				
				case 4:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(3)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(3), 1876, 97, 12, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(5), 1);
					
					break;
				
				case 5:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(3)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(3), 1839, 94, 0, 1500, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(6), 1500);
					
					break;
				
				case 6:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(2)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(2), 1872, 94, 15, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(7), 1);
					
					break;
				
				case 7:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(2)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(2), 1839, 92, 0, 1500, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(8), 1500);
					
					break;
				
				case 8:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(1)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(1), 1872, 92, 15, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(9), 1);
					
					break;
				
				case 9:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(1)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(1), 1839, 90, 5, 1500, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(10), 1500);
					
					break;
				
				case 10:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(0)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(0), 1872, 90, 5, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(11), 1);
					
					break;
				
				case 11:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_cameraMarker.get(0)) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _cameraMarker.get(0), 2002, 90, 2, 1500, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(12), 2000);
					
					break;
				
				case 12:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_vanHalter) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _vanHalter, 50, 90, 10, 0, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(13), 1000);
					
					break;
				
				case 13:
					// High Priestess van Halter uses the skill to kill Ritual Offering.
					L2Skill skill = SkillTable.getInstance().getInfo(1168, 7);
					_ritualOffering.setIsInvul(false);
					_vanHalter.setTarget(_ritualOffering);
					_vanHalter.setIsImmobilized(false);
					_vanHalter.doCast(skill);
					_vanHalter.setIsImmobilized(true);
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(14), 4700);
					
					break;
				
				case 14:
					_ritualOffering.setIsInvul(false);
					_ritualOffering.reduceCurrentHp(_ritualOffering.getMaxHp() + 1, _vanHalter, null);
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(15), 4300);
					
					break;
				
				case 15:
					spawnRitualSacrifice();
					deleteRitualOffering();
					
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_vanHalter) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _vanHalter, 100, 90, 15, 1500, 15000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(16), 2000);
					
					break;
				
				case 16:
					// set camera.
					for (L2PcInstance pc : _players)
					{
						if (pc.getPlanDistanceSq(_vanHalter) <= _distance)
						{
							enterMovieMode(pc);
							specialCamera(pc, _vanHalter, 5200, 90, -10, 9500, 6000);
						}
						else
						{
							leaveMovieMode(pc);
						}
					}
					
					// set next task.
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(17), 6000);
					
					break;
				
				case 17:
					// reset camera.
					for (L2PcInstance pc : _players)
					{
						leaveMovieMode(pc);
					}
					deleteRitualSacrifice();
					deleteCameraMarker();
					_vanHalter.setIsImmobilized(false);
					_vanHalter.setIsInvul(false);
					
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Movie(18), 1000);
					
					break;
				
				case 18:
					combatBeginning();
					if (_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
			}
		}
	}
	
	public void enterMovieMode(L2PcInstance player)
	{
		if (player == null)
			return;
		player.setTarget(null);
		player.stopMove(null);
		player.setIsInvul(true);
		player.setIsImmobilized(true);
		player.sendPacket(new CameraMode(1));
	}
	
	public void leaveMovieMode(L2PcInstance player)
	{
		if (player == null)
			return;
		if (!player.isGM())
			player.setIsInvul(false);
		player.setIsImmobilized(false);
		player.sendPacket(new CameraMode(0));
	}
	
	/**
	 * yaw:North=90, south=270, east=0, west=180<BR>
	 * pitch > 0:looks up,pitch < 0:looks down<BR>
	 * time:faster that small value is.<BR>
	 * @param player
	 * @param l2NpcInstance
	 * @param dist
	 * @param yaw
	 * @param pitch
	 * @param time
	 * @param duration
	 */
	public void specialCamera(L2PcInstance player, L2NpcInstance l2NpcInstance, int dist, int yaw, int pitch, int time, int duration)
	{
		player.sendPacket(new SpecialCamera(l2NpcInstance.getObjectId(), dist, yaw, pitch, time, duration));
	}
	
	public void specialCamera(L2PcInstance player, L2GrandBossInstance l2NpcInstance, int dist, int yaw, int pitch, int time, int duration)
	{
		player.sendPacket(new SpecialCamera(l2NpcInstance.getObjectId(), dist, yaw, pitch, time, duration));
	}
	
	protected List<L2PcInstance> getPlayersInside()
	{
		List<L2PcInstance> lst = new FastList<>();
		if (_Zone != null)
		{
			for (L2Character cha : _Zone.getCharactersInside())
			{
				if (cha instanceof L2PcInstance)
					lst.add((L2PcInstance) cha);
			}
			return lst;
		}
		_log.warning("VanHalterManager: Zone for Van Halter is missing.");
		return null;
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == 29062)
		{
			if ((npc.getCurrentHp() / npc.getMaxHp()) * 100 <= 20)
				callRoyalGuardHelper();
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == 29062)
		{
			GrandBossManager.getInstance().setBossStatus(29062, DEAD);
			enterInterval();
		}
		else if (npcId == 22188)
		{
			checkRoyalGuardCaptainDestroy();
		}
		else
		{
			checkTriolRevelationDestroy();
		}
		return super.onKill(npc, killer, isPet);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (npc.getObjectId() >= 32058 && npc.getObjectId() <= 32066)
		{
			npc.disableCoreAI(true);
			npc.setIsNoRndWalk(true);
			npc.setIsImmobilized(true);
		}
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		L2Skill bleed = SkillTable.getInstance().getInfo(4615, 12);
		
		if (Util.contains(triols, npc.getNpcId()))
		{
			npc.setTarget(player);
			npc.doCast(bleed);
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	public static void main(String[] args)
	{
		new VanHalter(-1, VanHalter.class.getSimpleName(), "ai");
	}
}