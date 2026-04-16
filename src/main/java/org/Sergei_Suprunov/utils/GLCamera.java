package org.Sergei_Suprunov.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GLCamera {

	private float azimuth, zenith;
	private final Vector3f pos;

	public GLCamera() {
		this.azimuth = -1.57f;
		this.zenith = 0.0f;
		this.pos = new Vector3f(3.0f, 4.0f, 10.0f);
	}

	public void addAzimuth(float ang) { azimuth += ang; }

	public void addZenith(float ang) {
		if (Math.abs(zenith + ang) <= Math.PI / 2.0f) {
			zenith += ang;
		}
	}

	public void forward(float speed) {
		Vector3f dir = new Vector3f(
				(float) (Math.cos(zenith) * Math.cos(azimuth)),
				(float) Math.sin(zenith),
				(float) (Math.cos(zenith) * Math.sin(azimuth))
		);
		pos.add(dir.mul(speed));
	}

	public void backward(float speed) { forward(-speed); }

	public void right(float speed) {
		Vector3f dir = new Vector3f(
				(float) -Math.sin(azimuth),
				0.0f,
				(float) Math.cos(azimuth)
		);
		pos.add(dir.mul(speed));
	}

	public void left(float speed) { right(-speed); }

	public void up(float speed) { pos.y += speed; }

	public void down(float speed) { pos.y -= speed; }

	public Matrix4f getViewMatrix() {
		Vector3f direction = new Vector3f(
				(float) (Math.cos(zenith) * Math.cos(azimuth)),
				(float) Math.sin(zenith),
				(float) (Math.cos(zenith) * Math.sin(azimuth))
		);

		Vector3f center = new Vector3f(pos).add(direction);
		Vector3f up = new Vector3f(0, 1, 0);

		return new Matrix4f().lookAt(pos, center, up);
	}

	public Vector3f getPosition() { return pos; }
}