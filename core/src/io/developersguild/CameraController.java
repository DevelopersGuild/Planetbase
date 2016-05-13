package io.developersguild;
/*
 * In large part stolen from LibGDX, but with only rotation, not translation or scale.
 * 
 * TODO this can probably be cut down some more
 * 
 *  ~planetguy
 */
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraController extends GestureDetector {
	/** The angle to rotate when moved the full width or height of the screen. */
	private float rotateAngle = 360f;
	/** The key which must be pressed to activate rotate, translate and forward or 0 to always activate. */
	private int activateKey = 0;
	/** Indicates if the activateKey is currently being pressed. */
	protected boolean activatePressed;
	/** Whether to update the camera after it has been changed. */
	private boolean autoUpdate = true;
	/** The target to rotate around. */
	private Vector3 target = new Vector3();
	
	private int forwardKey = Keys.W;
	protected boolean forwardPressed;
	private int backwardKey = Keys.S;
	protected boolean backwardPressed;
	private int rotateRightKey = Keys.A;
	protected boolean rotateRightPressed;
	private int rotateLeftKey = Keys.D;
	protected boolean rotateLeftPressed;
	private int rotateCWKey = Keys.Q;
	protected boolean rotateCWPressed;
	private int rotateCCWKey = Keys.E;
	protected boolean rotateCCWPressed;
	/** The camera. */
	private Camera camera;
	/** The current (first) button being pressed. */
	protected int button = -1;

	private float startX, startY;
	private final Vector3 tmpV1 = new Vector3();

	protected static class CameraGestureListener extends GestureAdapter {
		public CameraController controller;
		private float previousZoom;

		@Override
		public boolean touchDown (float x, float y, int pointer, int button) {
			previousZoom = 0;
			return false;
		}

		@Override
		public boolean tap (float x, float y, int count, int button) {
			return false;
		}

		@Override
		public boolean longPress (float x, float y) {
			return false;
		}

		@Override
		public boolean fling (float velocityX, float velocityY, int button) {
			return false;
		}

		@Override
		public boolean pan (float x, float y, float deltaX, float deltaY) {
			return false;
		}

		@Override
		public boolean zoom (float initialDistance, float distance) {
			return false;
		}

		@Override
		public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			return false;
		}
	};

	protected final CameraGestureListener gestureListener;

	protected CameraController (final CameraGestureListener gestureListener, final Camera camera) {
		super(gestureListener);
		this.gestureListener = gestureListener;
		this.gestureListener.controller = this;
		this.camera = camera;
	}

	public CameraController (final Camera camera) {
		this(new CameraGestureListener(), camera);
	}

	public void update () {
		final float delta = Gdx.graphics.getDeltaTime() / 5.0f;
		if (rotateRightPressed)
			rotate(delta,0,0);
		if (rotateLeftPressed)
			rotate(-delta,0,0);
		if (forwardPressed)
			rotate(0,-delta,0);
		if (backwardPressed)
			rotate(0,delta,0);
		if(rotateCWPressed)
			rotate(0,0,delta);
		if(rotateCCWPressed)
			rotate(0,0,-delta);
		if (autoUpdate) camera.update();
	}

	private int touched;
	private boolean multiTouch;

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		touched |= (1 << pointer);
		multiTouch = !MathUtils.isPowerOfTwo(touched);
		if (multiTouch)
			this.button = -1;
		else if (this.button < 0 && (activateKey == 0 || activatePressed)) {
			startX = screenX;
			startY = screenY;
			this.button = button;
		}
		return super.touchDown(screenX, screenY, pointer, button) || (activateKey == 0 || activatePressed);
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		touched &= -1 ^ (1 << pointer);
		multiTouch = !MathUtils.isPowerOfTwo(touched);
		if (button == this.button) this.button = -1;
		return super.touchUp(screenX, screenY, pointer, button) || activatePressed;
	}

	protected boolean rotate (float deltaX, float deltaY, float deltaAngle) {
		tmpV1.set(camera.direction).crs(camera.up);
		camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
		camera.rotateAround(target, camera.up, deltaX * -rotateAngle);
		camera.rotateAround(target, camera.direction, deltaAngle * rotateAngle);
		if (autoUpdate) camera.update();
		return true;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		boolean result = super.touchDragged(screenX, screenY, pointer);
		if (result || this.button < 0) return result;
		final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
		final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
		startX = screenX;
		startY = screenY;
		return rotate(deltaX, deltaY,0);
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}

	@Override
	public boolean keyDown (int keycode) {
		setKey(keycode, true);
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		setKey(keycode, false);
		return false;
	}

	public void setKey(int keycode, boolean newVal){
		if (keycode == activateKey) {
			activatePressed = newVal;
			if(!newVal)
				button = -1;
		}
		if (keycode == forwardKey)
			forwardPressed = newVal;
		else if (keycode == backwardKey)
			backwardPressed = newVal;
		else if (keycode == rotateRightKey)
			rotateRightPressed = newVal;
		else if (keycode == rotateLeftKey) 
			rotateLeftPressed = newVal;
		else if (keycode == rotateCWKey)
			rotateCWPressed = newVal;
		else if (keycode == rotateCCWKey)
			rotateCCWPressed = newVal;
	}
}
