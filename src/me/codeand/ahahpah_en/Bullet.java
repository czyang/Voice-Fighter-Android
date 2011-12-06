package me.codeand.ahahpah_en;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
public class Bullet extends Sprite {

	// ===========================================================
	// Constants
	// ===========================================================
	public static int BULLET_DEAD = 0;
	public static int BULLET_LAUNCHED = BULLET_DEAD + 1;
	
	private static float BULLET_SPEED = 6.0f;
	
	public CircleCollideObject mCircleCollideObject;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private int mState = BULLET_DEAD;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public Bullet(float pX, float pY, float pCollideCenterX, float pCollideCenterY, float pCollideRadius, TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);

		mCircleCollideObject = new CircleCollideObject(pCollideCenterX, pCollideCenterY, pCollideRadius);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void updateBullet () {
		if ( mState == BULLET_LAUNCHED )
			this.mX += BULLET_SPEED;
	}
	
	public void launch () {
		mState = BULLET_LAUNCHED;
	}
	
	public void dead () {
		mState = BULLET_DEAD;
	}
	
	public int getState () {
		return mState;
	}
	
	public boolean collideWidth ( Sprite pSprite, CircleCollideObject pCircleCollideObject ) {
		return mCircleCollideObject.collideWidth( mX, mY, pSprite.getX(), pSprite.getY(), pCircleCollideObject);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
