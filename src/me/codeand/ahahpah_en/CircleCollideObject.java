package me.codeand.ahahpah_en;

import org.anddev.andengine.entity.shape.RectangularShape;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
public class  CircleCollideObject {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	public float mCollideCenterX;
	public float mCollideCenterY;
	
	public float mCollideRadius;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public CircleCollideObject ( float pCollideCenterX, float pCollideCenterY, float pCollideRadius ) {
		mCollideCenterX = pCollideCenterX;
		mCollideCenterY = pCollideCenterY;
		mCollideRadius = pCollideRadius;
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

	public boolean collideWidth ( float pX1, float pY1, float pX2, float pY2, CircleCollideObject pCircleCollideObject ) {
		
		double tDistance = Math.sqrt( Math.pow( ( ( pX2 + pCircleCollideObject.mCollideCenterX ) - ( pX1 + mCollideCenterX ) ), 2 ) + 
				Math.pow( ( ( pY2 + pCircleCollideObject.mCollideCenterY ) - ( pY1 + mCollideCenterY ) ), 2) );
		
		if ( tDistance <= mCollideRadius + pCircleCollideObject.mCollideRadius )
			return true;
		else
			return false;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
