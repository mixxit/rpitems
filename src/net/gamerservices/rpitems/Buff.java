package net.gamerservices.rpitems;

import org.bukkit.entity.Player;

public class Buff {

	String name = "NULL";
	int timeleft = 1;
	Player player;
	int effectvalue;
	
	public Buff(Player player,String effectname, int timer, int effectvalue)
	{
		this.name = effectname;
		this.timeleft = timer;
		this.player = player;
		this.effectvalue = effectvalue;
	}
}
