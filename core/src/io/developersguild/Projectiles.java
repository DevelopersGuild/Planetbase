package io.developersguild;

public enum Projectiles {
	//Full-featured base
	BASE,
	//Explodes, damages everyone nearby
	BOMB,
	//Base that stops incoming shots. No firing.
	SHIELD,
	//Base that shoots down incoming shots. Requires reloads lasting one turn. Will coordinate so only the nearest shoots down a single projectile.
	ANTIAIR,
	//Stuns all bases within its radius of effect until its user finishes their next turn.
	EMP,
	//Cut-down base that cannot launch buildings, but has better max range.
	ARTILLERY,
	
}
