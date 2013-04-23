package net.gamerservices.rpitems;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Detrimental {

	String name = "NULL";
	int timeleft = 1;
	LivingEntity le;
	Player cause;
	int effectvalue = 0;
	
	public Detrimental(LivingEntity entity,Player cause,String effectname, int timer, int effectvalue)
	{
		this.name = effectname;
		this.cause = cause;
		this.timeleft = timer;
		this.le = entity;
		this.effectvalue = effectvalue;
	}
}
