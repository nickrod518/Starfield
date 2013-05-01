/*
 * Nick Rodriguez
 * April 30, 2013
 * Star class
 * Emulates a field of stars in space
 * Ability to alter speed and star count
 */

package edu.unca.nrodrigu.starfield;

public class Star {
	public static int speed = 11;
	float x, y, z;
	float dz;

	Star(float bx, float by, float bz, float speedz) {  
		x = bx;
		y = by;
		z = bz;
		dz = speedz;
	}
	
	void move() {
		if (z >= 0) {
			z = -100;
		}
		z += (dz * speed);
	}
}