/*
 * Copyright (C) 2004-2014 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import l2r.gameserver.enums.TrapAction;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2TrapInstance;
import l2r.gameserver.model.effects.EffectTemplate;
import l2r.gameserver.model.effects.L2Effect;
import l2r.gameserver.model.events.EventDispatcher;
import l2r.gameserver.model.events.impl.character.trap.OnTrapAction;
import l2r.gameserver.model.stats.Env;
import l2r.gameserver.network.SystemMessageId;

/**
 * Trap Remove effect implementation.
 * @author UnAfraid
 */
public final class TrapRemove extends L2Effect
{
	private final int _power;
	
	public TrapRemove(Env env, EffectTemplate template)
	{
		super(env, template);
		
		if (template.hasParameters())
		{
			_power = template.getParameters().getInteger("power");
		}
		else
		{
			throw new IllegalArgumentException(getClass().getSimpleName() + ": effect without power!");
		}
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public boolean onStart()
	{
		final L2Character target = getEffected();
		if (!target.isTrap())
		{
			return false;
		}
		
		if (target.isAlikeDead())
		{
			return false;
		}
		
		final L2TrapInstance trap = (L2TrapInstance) target;
		if (!trap.canBeSeen(getEffector()))
		{
			if (getEffector().isPlayer())
			{
				getEffector().sendPacket(SystemMessageId.INCORRECT_TARGET);
			}
			return false;
		}
		
		if (trap.getLevel() > _power)
		{
			return false;
		}
		
		// Notify to scripts
		EventDispatcher.getInstance().notifyEventAsync(new OnTrapAction(trap, getEffector(), TrapAction.TRAP_DISARMED), trap);
		
		trap.unSummon();
		if (getEffector().isPlayer())
		{
			getEffector().sendPacket(SystemMessageId.A_TRAP_DEVICE_HAS_BEEN_STOPPED);
		}
		
		return true;
	}
}
