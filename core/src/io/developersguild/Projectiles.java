package io.developersguild;

import java.util.List;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

//We might need to break these into classes sometime
public enum Projectiles {
	//Full-featured base
	BASE{
		public void land(Vector3 position, Space space){
			List<EntityStatic> collidingEntities=space.getWithin(position, 0.03);

			if(collidingEntities.size() == 0) {
				EntityStatic entity=new EntityStatic(new ModelInstance(
						Assets.modelBuilder.createBox(0.03f, 0.03f, 0.06f, 
								new Material(StrategyGame.blueAttr), 
								Usage.Normal | Usage.Position)),
						position);

				space.addEntity(entity);
			} else {
				for(EntityStatic e:collidingEntities){
					e.damage(2);
				}
			}
		}
	},
	//Explodes, damages everyone nearby
	BOMB{
		public void land(Vector3 position, Space space){
			List<EntityStatic> collidingEntities=space.getWithin(position, 0.03);
			
			for(EntityStatic e:collidingEntities){
				e.damage(8);
			}
		}
	},
	//Base that stops incoming shots. No firing.
	SHIELD,
	//Base that shoots down incoming shots. Requires reloads lasting one turn. Will coordinate so only the nearest shoots down a single projectile.
	ANTIAIR,
	//Stuns all bases within its radius of effect until its user finishes their next turn.
	EMP,
	//Cut-down base that cannot launch buildings, but has better max range.
	ARTILLERY,
	
	;
	
	public void land(Vector3 position, Space space){
		throw new UnsupportedOperationException();
	}
	
	public static final int VALID_PROJECTILES=2;
}
