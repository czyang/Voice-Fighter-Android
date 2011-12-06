package me.codeand.ahahpah_en;

import java.util.Random;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.os.SystemClock;
import android.util.Log;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
public class Smoke extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int STATE_DEAD = 0;
	private static final int STATE_LIVE = STATE_DEAD + 1;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private int mState = STATE_DEAD;
	private float mDurationTime;
	//private float mPassedTime;
	private float mStartTime;
	
	private boolean mIsRotate = false;
	private float mRotateValue = 0.0f;
	
	final Random mRandom = new Random (SystemClock.uptimeMillis());
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public Smoke(float pX, float pY, TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
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
	public void setAction ( float pDurationTime, boolean pIsRotate ) {
		mState = STATE_LIVE;
		this.setVisible(true);
		this.setIgnoreUpdate(false);
		this.setAlpha(1.0f);
		mStartTime = SystemClock.uptimeMillis();
		mDurationTime = pDurationTime * 1000;
		
		if ( pIsRotate == true ) {
			mIsRotate = true;
			mRotateValue = -5 + mRandom.nextFloat() * 10;
		} else {
			mIsRotate = false;
		}
		
	}
	
	public void update () {
		if ( mState == STATE_LIVE ) {
			float PassedTime = SystemClock.uptimeMillis() - mStartTime;
			float alpha = mDurationTime - PassedTime;
			if ( alpha < 0 )
				alpha = 0;
			this.setAlpha(alpha/mDurationTime);
			
			if ( mIsRotate == true ) {
				this.setRotation(this.getRotation() + mRotateValue );
			}
			
			if ( alpha <= 0 ) {
				this.setVisible(false);
				this.setIgnoreUpdate(true);
				this.mState = STATE_DEAD;
			}
			//Log.d ("xxxxxxxxxxxx","xxxxxxxxxxx1211111111");
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
