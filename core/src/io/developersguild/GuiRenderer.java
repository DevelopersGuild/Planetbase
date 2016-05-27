package io.developersguild;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class GuiRenderer {

	private static SpriteBatch spriteBatch;
	private static TextureRegion[] textureRegions;
	private static Texture[] textures;

	static{
		//TODO not embed the file path into here
		int i=0;
		String[] textureLocs=new String[]{
				"/home/planetguy/dev/3dxGame/core/src/gui.png",
				"/home/planetguy/dev/3dxGame/core/src/gui_blank.png",
				"/home/planetguy/dev/3dxGame/core/src/power.png",
				"/home/planetguy/dev/3dxGame/core/src/ind_base.png",
				"/home/planetguy/dev/3dxGame/core/src/ind_bomb.png"
		};
		textureRegions=new TextureRegion[textureLocs.length];
		textures=new Texture[textureLocs.length];
		for(String path:textureLocs) {
			Texture aimingTex=new Texture(path);
			textures[i]=aimingTex;
			aimingTex.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			textureRegions[i++]=new TextureRegion(aimingTex);
		}

		spriteBatch=new SpriteBatch();
		spriteBatch.enableBlending();
	}

	public static void renderGui(Camera cam, EntityStatic entity, Environment environment, float deltaTime){
		spriteBatch.begin();
		if(entity!=null) {
			spriteBatch.draw(
				    textureRegions[2].getTexture(),
				    0,
				    0,
				    0,
				    0,
				    textureRegions[2].getRegionWidth(),
				    textureRegions[2].getRegionHeight() * entity.timeHeld,                                        /* Scale output height */
				    1,
				    1,
				    0,
				    textureRegions[2].getRegionX(),
				    textureRegions[2].getRegionY(), /* Move input data position */
				    textureRegions[2].getRegionWidth(),
				    textureRegions[2].getRegionHeight(),         /* Scale input height */
				    false,
				    false);
			spriteBatch.draw(textureRegions[0], 0, 0);
			
			entity.handleGuiInteraction(deltaTime, cam);
		} else {
			spriteBatch.draw(textureRegions[1], 0, 0);
		}
		spriteBatch.end();
	}

}
