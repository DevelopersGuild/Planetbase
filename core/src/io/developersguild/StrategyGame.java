package io.developersguild;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


public class StrategyGame extends Game {

	public static StrategyGame INSTANCE;

	public PerspectiveCamera cam;
	public ModelBatch staticEntityBatch;
	public ModelInstance planetSurface;

	public Space space;
	public Environment environment;
	private CameraController camController;

	private ModelBuilder modelBuilder;

	private List<Model> models=new ArrayList<Model>();

	private float selectGroup=0;
	private EntityStatic selectedEntity;
	private boolean overheldSelectEntity=false;

	public static ColorAttribute blackAttr=ColorAttribute.createDiffuse(0f,0f,0f, 1f);
	public static ColorAttribute blueAttr =ColorAttribute.createDiffuse(0f, 0f, 1f, 1f);

	@Override
	public void create() {
		INSTANCE=this;

		Debug.dbg("Path: "+System.getProperty("user.dir"));

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		staticEntityBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		float cameraDist=0.8f;
		cam.position.set(cameraDist,cameraDist,cameraDist);
		cam.lookAt(0,0,0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();

		modelBuilder = new ModelBuilder();
		Model world = modelBuilder.createSphere(2f,2f,2f, 
				80, 80, /* 80x80 seems to be enough polys to look smooth */
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		models.add(world);

		planetSurface = new ModelInstance(world);

		camController = new CameraController(cam);
		Gdx.input.setInputProcessor(camController);

		space=new Space();
	}

	public EntityStatic createEntity(float x, float y, float z){
		List<EntityStatic> collidingEntities=space.getWithin(new Vector3(x,y,z), 0.03);

		if(collidingEntities.size() == 0) {
			EntityStatic entity=new EntityStatic(new ModelInstance(
					modelBuilder.createBox(0.03f, 0.03f, 0.06f, 
							new Material(blueAttr), 
							Usage.Normal | Usage.Position)),
					x,y,z);

			space.addEntity(entity);

			return entity; 
		} else {
			return null;
		}
	}

	@Override
	public void render() {

		float deltaTime=Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyJustPressed(Keys.C)) {
			createEntity(-cam.direction.x, -cam.direction.y, -cam.direction.z);
		}

		handleSelectionSphere();

		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


		//draw opaque object ...
		staticEntityBatch.begin(cam);
		for(EntityStatic es:space.allEntities()){
			es.renderEntity(staticEntityBatch, environment);
		}
		staticEntityBatch.render(planetSurface, environment);
		staticEntityBatch.end();

		GuiRenderer.renderGui(cam, selectedEntity, environment, deltaTime);
	}

	private void handleSelectionSphere() {
		if(Gdx.input.isKeyPressed(Keys.R)){
			if(!overheldSelectEntity)
				selectGroup+=0.01;
			if(selectGroup>0.3f) {
				overheldSelectEntity=true;
				selectGroup=0;
			} else if(!overheldSelectEntity){
				renderSelectTooltip();
			}

		} else {
			if(overheldSelectEntity){
				overheldSelectEntity=false;
			} else if(selectGroup != 0) {
				if(selectedEntity != null) {
					selectedEntity.deselect();
				}
				List<EntityStatic> candidates=space.getWithin(cam.direction.cpy().scl(-1), selectGroup);

				//Trim the selected entity and any that have no GUIs
				Iterator<EntityStatic> c=candidates.iterator();
				while(c.hasNext()){
					EntityStatic entity=c.next();
					if(entity==selectedEntity
							|| !entity.canSelect())
						c.remove();
				}

				if(candidates.size() == 1){
					selectedEntity=candidates.get(0);
					selectedEntity.select();
				} else {
					selectedEntity=null;
				}
				selectGroup=0;
			}
		}
	}

	@Override
	public void dispose() {
		staticEntityBatch.dispose();
		for(Model m:models)
			m.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public void renderSelectTooltip(){
		float radius=selectGroup+0.03f;
		ModelInstance sphere=new ModelInstance(modelBuilder.createSphere(radius, radius, radius
				,(int)(100*radius),(int)(100*radius), //This can be lower-resolution
				new Material(ColorAttribute.createDiffuse(1,0,0,0.5f)),
				Usage.Position | Usage.Normal));
		sphere.transform.setToTranslation(-cam.direction.x, -cam.direction.y, -cam.direction.z);
		staticEntityBatch.render(sphere);
	}

	public boolean hasGui(){
		return true;
	}

}