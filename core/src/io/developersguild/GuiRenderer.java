package io.developersguild;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class GuiRenderer {
	
	public static ModelBatch guiBatch;
	
	public static void renderAttached(Camera cam, EntityStatic entity, Environment environment, float deltaTime){
		guiBatch.begin(cam);
		
		//Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
		//Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA,Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
		
		entity.renderGui(guiBatch, environment, cam, deltaTime);

		guiBatch.end();
	}

}
