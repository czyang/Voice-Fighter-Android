package me.codeand.ahahpah_en;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
public class Rock extends Sprite {
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public CircleCollideObject mCircleCollideObject;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public Rock(float pX, float pY, float pCollideCenterX, float pCollideCenterY, float pCollideRadius, TextureRegion pTextureRegion) {
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
	public boolean collideWidth ( Sprite pSprite, CircleCollideObject pCircleCollideObject ) {
		return mCircleCollideObject.collideWidth( mX, mY, pSprite.getX(), pSprite.getY(), pCircleCollideObject);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
