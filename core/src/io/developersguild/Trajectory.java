package io.developersguild;

import com.badlogic.gdx.math.Vector3;

//TODO do this properly?
//HAHA no, just use delta angle ~ power
// http://space.stackexchange.com/questions/1904/how-to-programmatically-calculate-orbital-elements-using-position-velocity-vecto
public class Trajectory {

	private Vector3 
			v0, 
			pos0
			;
	
	private float power;
	
	public Trajectory(Vector3 source, Vector3 aimDirection, float power) {
		pos0=source;
		v0=new Vector3(aimDirection).add(source).nor().scl(power);
		this.power=power;
	}
	
	public strictfp float[] getEndpoint(float radius){
		Vector3 axis=new Vector3(pos0).crs(v0);
		Vector3 angle=new Vector3(pos0);
		Vector3 result=angle.rotate(axis, power*36).scl(radius);
		return new float[]{result.x, result.y, result.z};
	}
	
}
