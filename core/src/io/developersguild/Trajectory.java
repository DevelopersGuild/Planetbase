package io.developersguild;

import com.badlogic.gdx.math.Vector3;

//TODO do this properly?
// http://space.stackexchange.com/questions/1904/how-to-programmatically-calculate-orbital-elements-using-position-velocity-vecto
public class Trajectory {

	private Vector3 
			v0, 
			pos0,
			gravity
			;
	
	public Trajectory(Vector3 source, Vector3 aimDirection, float power) {
		pos0=source;
		v0=new Vector3(aimDirection).add(source).nor().scl(power);
		gravity=new Vector3(source).scl(-1f);
	}
	
	public strictfp float[] getEndpoint(float radius){
		float t=solve(radius);
		return new float[]{(float)x(t),(float)y(t),(float)z(t)};
	}
	
	private strictfp float solve(float radius){
		double radiusSq=radius*radius;
		
		double epsilon=Float.MIN_VALUE / 2.0;
		double t=0.01;
		double deltaT=0.5;
		
		double error;
		do{
			error=rSquared(t)-radiusSq;
			Debug.dbg(error);
			//focus in
			if(StrictMath.abs(error) < deltaT)
				deltaT /= 2;
			if(error > 0)
				t += deltaT;
			else
				t -= deltaT;
		}while(error>epsilon);
		
		return (float) t;
	}
	
	private strictfp double x(double t){
		return t * t * gravity.x + t * v0.x + pos0.x;
	}
	
	private strictfp double y(double t){
		return t * t * gravity.y + t * v0.y + pos0.y;
	}
	
	private strictfp double z(double t){
		return t * t * gravity.z + t * v0.z + pos0.z;
	}
	
	private strictfp double rSquared(double t){
		return x(t)*x(t)+y(t)*y(t)+z(t)*z(t);
	}
	
}
