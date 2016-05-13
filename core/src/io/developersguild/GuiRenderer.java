package io.developersguild;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class GuiRenderer {

	public static ModelBatch guiBatch;
	private static SpriteBatch spriteBatch;

	static{
		spriteBatch=new SpriteBatch();
		spriteBatch.enableBlending();
	}

	public static void renderGui(Camera cam, EntityStatic entity, Environment environment, float deltaTime){
		spriteBatch.begin();
		if(entity!=null) {
			spriteBatch.draw(new TextureRegion(StrategyGame.guiTex), 0, 0);
			entity.handleGuiInteraction(deltaTime, cam);
		}
		spriteBatch.end();
	}

}
