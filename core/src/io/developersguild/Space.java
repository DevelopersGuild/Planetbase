package io.developersguild;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.math.Vector3;

public class Space {
	
	private List<EntityStatic> entities=new ArrayList<EntityStatic>();
	
	public void addEntity(EntityStatic e){
		entities.add(e);
		e.setParent(this);
	}
	
	public void removeEntity(EntityStatic e){
		entities.remove(e);
	}
	
	private double sq(double d){
		return d*d;
	}
	
	private double distSq(Vector3 e1, EntityStatic e2){
		return sq(e1.x -e2.x()) + sq(e1.y-e2.y()) + sq(e1.z-e2.z());
	}
	
	public List<EntityStatic> getWithin(final Vector3 e, double distance) {
		final double distSq=distance*distance;
		List<EntityStatic> matches=new ArrayList<EntityStatic>();
		for(EntityStatic other:entities){
			if(distSq(e, other) < distSq) {
				matches.add(other);
			}
		}
		return matches;
	}
	
	public List<EntityStatic> allEntities(){
		return entities;
	}
}
