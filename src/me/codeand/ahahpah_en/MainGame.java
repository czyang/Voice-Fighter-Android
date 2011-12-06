package me.codeand.ahahpah_en;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
public class MainGame extends LayoutGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	
	private static final int STATE_MENU = 0;
	private static final int STATE_GAME = STATE_MENU + 1;
	private static final int STATE_INFO = STATE_GAME + 1;
	
	private static final int ROCK_NUM = 4;
	private static final int BULLET_NUM = 3;
	private static final int SMOOK_NUM = 4;
	
	private static final int FONT_SIZE = 30;
	
	private static final String HISCORE_PREFS = "hiscore";
	// ===========================================================
	// Fields
	// ===========================================================
	private static Camera mCamera;
	
	public static boolean ahh = false;
	public static boolean pahh = false;
	
	private Texture mMenuTX;
	private TextureRegion mBackGroundTR;
	private Sprite mBackGround;
	private TextureRegion mTitleTR;
	private Sprite mTitleSP;
	private TextureRegion mScoresTR;
	private Sprite mScoresSP;
	private TextureRegion mHiScoresTR;
	private Sprite mHiScoresSP;
	private TextureRegion mStartTR;
	private Sprite mStartSP;
	
	private Texture mGameTX;
	private TextureRegion mAirPlaneTR;
	private Aircraft mAirPlane;
	private TextureRegion mRock1TR;
	private TextureRegion mRock2TR;
	private TextureRegion mRock3TR;

	private TextureRegion mBulletTR;
	
	private Texture mFoldTX;
	private TextureRegion mFoldTR;
	private Sprite mFold;
	
	private Rock[] mRock;
	
	private Bullet[] mBullet;
	private int mBulletIndex = 0;
	private long mLastLaunch = 0;
	
	private Texture mSmookTX;
	private TextureRegion mSmookTR;
	private Smoke[] mSmoke;
	private int mSmokeIndex = 0;
	//Update Handler
	private IUpdateHandler mGameUpdate;
	
	//private static final Handler ScoreLoopHandler = new Handler();
	//private static final Handler AdsHandler = new Handler();
	
	//Recoder
	private Recorder mRecorder;
	
	private boolean mLimitRecorder = true;
	
	private int mGameState = STATE_MENU;
	private boolean readyOver = false;
	private float overTime = 0.0f; 
	
	private float mSpeed = 0.0f;
	private float mAcceleration = 0.0f;
	
	public static float mVoicePoint = 0.0f;
	public static float mMaxUp = 0.0f;
	public static float mLastState = 0;
	private float mTargetPoint = 0.0f;
	private float mTargetArray[];
	private int mTargetNum = 0;
	private int mTargetIndex = 0;
	
	private boolean mFirstMenu = true;
	private boolean mFirstGame = true;
	
	private float mAirPlaneSpeedX = 0.0f;
	
	private float mLastRock = -1.0f;
	
	//Button
	private Texture mMenuButtonTX;
	private TextureRegion mInfoButtonTR;
	private TextureRegion mBoardButtonTR;
	private TextureRegion mSettingsButtonTR;
	private TextureRegion mCloseButtonTR;
	private Sprite mInfoButton;
	private Sprite mBoardButton;
	private Sprite mSettingsButton;
	private Sprite mCloseButton;
	
	//Infomation
	private Texture mInformationTX;
	private TextureRegion mInformationTR;
	private Sprite mInformation;
	
	//Fonts
	private Font mScoreFont;
	private Texture mFontTX;
	private Typeface mScoreTF;
    private ChangeableText mScoreText;
    private ChangeableText mHiScoreText;
    
    private static int mScore = 0;
    
	//Random
	final Random mRandom = new Random (SystemClock.uptimeMillis());
	
	final static Scene MainScene = new Scene(1);
	
	//Perference.
	static public SharedPreferences mPrefs;
	
	//Ads
	/*
	private static AdView mAdView;
	*/
	
	//Ah Pah sensitivity
	public static int mAh = 500;
	public static int mPah = 7000;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected int getLayoutID() {
		return R.layout.gamelayout;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.game_rendersurfaceview;
	}

	@Override
	public void onLoadComplete() {
	}

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera( 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT );
		//return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), mCamera));
	}

	@Override
	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("gfx/");
		mMenuTX = new Texture ( 512, 512,  TextureOptions.DEFAULT );
		mBackGroundTR = TextureRegionFactory.createFromAsset(mMenuTX, this, "background.png", 0, 0);
		mTitleTR = TextureRegionFactory.createFromAsset(mMenuTX, this, "title.png", 0, 321 );
		mStartTR = TextureRegionFactory.createFromAsset(mMenuTX, this, "start.png", 0, 400 );
		mScoresTR = TextureRegionFactory.createFromAsset(mMenuTX, this, "scores.png", 0, 480 );
		mHiScoresTR = TextureRegionFactory.createFromAsset(mMenuTX, this, "hiscores.png", 81, 480 );
		
		mBulletTR = TextureRegionFactory.createFromAsset(mMenuTX, this, "bullet.png", 200, 480 );
		
		mGameTX = new Texture ( 256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA );
		mAirPlaneTR = TextureRegionFactory.createFromAsset(mGameTX, this, "airplane.png", 0, 0);
		mRock1TR = TextureRegionFactory.createFromAsset(mGameTX, this, "rock1.png", 90, 0);
		mRock2TR = TextureRegionFactory.createFromAsset(mGameTX, this, "rock2.png", 157, 0);
		mRock3TR = TextureRegionFactory.createFromAsset(mGameTX, this, "rock3.png", 0, 50);
		
		mSmookTX = new Texture ( 64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA );
		mSmookTR = TextureRegionFactory.createFromAsset(mSmookTX, this, "smoke.png", 0, 0);
		
		mFoldTX= new Texture ( 256, 512, TextureOptions.DEFAULT );
		mFoldTR = TextureRegionFactory.createFromAsset(mFoldTX, this, "fold.png", 0, 0);
		
		mMenuButtonTX = new Texture ( 256, 128, TextureOptions.DEFAULT );
		mInfoButtonTR = TextureRegionFactory.createFromAsset(mMenuButtonTX, this, "info.png", 0, 0);
		mBoardButtonTR = TextureRegionFactory.createFromAsset(mMenuButtonTX, this, "board.png", 70, 0);
		//mSubmitButtonTR = TextureRegionFactory.createFromAsset(mMenuButtonTX, this, "submit.png", 0, 40);
		mCloseButtonTR = TextureRegionFactory.createFromAsset(mMenuButtonTX, this, "close.png", 150, 50);
		mSettingsButtonTR = TextureRegionFactory.createFromAsset(mMenuButtonTX, this, "settings.png", 0, 50);
		
		mInformationTX = new Texture ( 256, 256, TextureOptions.DEFAULT );
		mInformationTR = TextureRegionFactory.createFromAsset(mInformationTX, this, "infomation.png", 0, 0);
		
		//Load Font
		FontFactory.setAssetBasePath("font/");
		this.mScoreTF = Typeface.createFromAsset(this.getAssets(), "font/mia.ttf");
		this.mFontTX = new Texture(256, 256, TextureOptions.DEFAULT);
		this.mScoreFont = new Font(mFontTX, mScoreTF, FONT_SIZE, true, Color.BLACK);
		this.mEngine.getFontManager().loadFonts(this.mScoreFont);
		
		this.mEngine.getTextureManager().loadTextures( mMenuTX, mGameTX, mSmookTX, mFoldTX, 
				mFontTX, mMenuButtonTX, mInformationTX );
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		MainScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this); 
		
		int sen = mPrefs.getInt(Prefs.SENSITIVITY, 0);
		
		MainGame.mAh = 500 + sen * Recorder.SEN_BASE;
		if ( MainGame.mAh < 2000 ) {
			MainGame.mPah = 7000;
		} else if ( MainGame.mAh < 4000 ) {
			MainGame.mPah = 9000;
		} else if ( MainGame.mAh < 6000 ) {
			MainGame.mPah = 11000;
		} else if ( MainGame.mAh < 8000 ) {
			MainGame.mPah = 13000;
		} else if ( MainGame.mAh < 10000 ) {
			MainGame.mPah = 15000;
		} else if ( MainGame.mAh < 12000 ) {
			MainGame.mPah = 17000;
		} else if ( MainGame.mAh < 14000 ) {
			MainGame.mPah = 19000;
		} else if ( MainGame.mAh < 16000 ) {
			MainGame.mPah = 21000;
		} else if ( MainGame.mAh < 18000 ) {
			MainGame.mPah = 25000;
		} else if ( MainGame.mAh < 20000 ) {
			MainGame.mPah = 30000;
		} else {
			MainGame.mPah = 32000;
		}
		
		//Init Ads.
		/*
		mAdView = new AdView(this, AdSize.BANNER, "xxxx");
		FrameLayout layout = (FrameLayout)findViewById(R.id.game_layout);

		FrameLayout.LayoutParams adsParams =new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, 
				android.view.Gravity.BOTTOM|android.view.Gravity.CENTER_HORIZONTAL);
		layout.addView(mAdView, adsParams );
		mAdView.loadAd(new AdRequest());
		*/
		
		/*
	    mAdView = (AdView)this.findViewById(R.id.adView);
	    mAdView.loadAd(new AdRequest());
		*/
		
		//Attach MainMenu Sprite.
		mBackGround = new Sprite ( 0, 0, mBackGroundTR );
		MainScene.getLastChild().attachChild(mBackGround);
		mTitleSP = new Sprite ( 110, 8, mTitleTR);
		MainScene.getLastChild().attachChild(mTitleSP);
		mStartSP = new Sprite ( 60, 130, mStartTR );
		MainScene.getLastChild().attachChild(mStartSP);
		mScoresSP = new Sprite ( 90, 80, mScoresTR );
		MainScene.getLastChild().attachChild(mScoresSP);
		mHiScoresSP = new Sprite ( 250, 80, mHiScoresTR );
		MainScene.getLastChild().attachChild(mHiScoresSP);
		
		//Text
        mScoreText = new ChangeableText(170, 80, this.mScoreFont, "0", "123456789".length());
        mHiScoreText = new ChangeableText(350, 80, this.mScoreFont, "0", "123456789".length());
		MainScene.getLastChild().attachChild(mScoreText);
		MainScene.getLastChild().attachChild(mHiScoreText);
		
		mFold = new Sprite ( -60, 0, mFoldTR );
		MainScene.getLastChild().attachChild(mFold);
		
		mAirPlane = new Aircraft ( -100, 0, mAirPlaneTR.getWidth()/2 + 10, mAirPlaneTR.getHeight()/2, mAirPlaneTR.getHeight()/2, mAirPlaneTR );
		MainScene.getLastChild().attachChild(mAirPlane);
		
		mRock = new Rock[ROCK_NUM];
		mRock[0] = new Rock ( -100, 0, mRock1TR.getWidth()/2, mRock1TR.getHeight()/2, mRock1TR.getWidth()/2, mRock1TR);
		MainScene.getLastChild().attachChild(mRock[0]);
		mRock[1] = new Rock ( -100, 0, mRock2TR.getWidth()/2, mRock2TR.getHeight()/2, mRock2TR.getWidth()/2, mRock2TR);
		MainScene.getLastChild().attachChild(mRock[1]);
		mRock[2] = new Rock ( -100, 0, mRock3TR.getWidth()/2, mRock3TR.getHeight()/2, mRock3TR.getWidth()/2, mRock3TR);
		MainScene.getLastChild().attachChild(mRock[2]);
		mRock[3] = new Rock ( -100, 0, mRock2TR.getWidth()/2, mRock2TR.getHeight()/2, mRock2TR.getWidth()/2, mRock2TR.clone());
		MainScene.getLastChild().attachChild(mRock[3]);
		
		mBullet = new Bullet[BULLET_NUM];
		for ( int i = 0; i < BULLET_NUM; i++ ) {
			mBullet[i] = new Bullet ( -100, 0, mBulletTR.getWidth()/2, mBulletTR.getHeight()/2, mBulletTR.getHeight()/2, mBulletTR.clone());
			MainScene.getLastChild().attachChild(mBullet[i]);
		}
		
		mSmoke = new Smoke[SMOOK_NUM];
		for ( int i = 0; i < SMOOK_NUM; i++ ) {
			mSmoke[i] = new Smoke ( -100, 0, mSmookTR.clone() );
			mSmoke[i].setColor(0.0f, 0.0f, 0.0f);
			mSmoke[i].setIgnoreUpdate(true);
			mSmoke[i].setVisible(false);
			MainScene.getLastChild().attachChild(mSmoke[i]);
		}
		
		mInformation = new Sprite ( 100, 20, mInformationTR );
		mInformation.setVisible(false);
		mInformation.setIgnoreUpdate(true);
		MainScene.getLastChild().attachChild(mInformation);
		
		mBoardButton = new Sprite ( 50, 230, mBoardButtonTR ) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					//ScoreLoopHandler.post(openDashBoard);
					break;
				}
				return true;
			}
		};
		MainScene.getLastChild().attachChild(mBoardButton);
		MainScene.registerTouchArea(mBoardButton);
		
		mSettingsButton = new Sprite ( 210, 230, mSettingsButtonTR ) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					//ScoreLoopHandler.post(openSettings);
					break;
				}
				return true;
			}
		};
		MainScene.getLastChild().attachChild(mSettingsButton);
		MainScene.registerTouchArea(mSettingsButton);
		
		mInfoButton = new Sprite ( 370, 230, mInfoButtonTR ) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					hideMenuItems ();
					mScoreText.setVisible(false);
					mScoreText.setIgnoreUpdate(true);
					
					mCloseButton.setVisible(true);
					mCloseButton.setIgnoreUpdate(false);
					
					mInformation.setVisible(true);
					mInformation.setIgnoreUpdate(false);
					
					mGameState = STATE_INFO;
					break;
				}
				return true;
			}
		};
		MainScene.getLastChild().attachChild(mInfoButton);
		MainScene.registerTouchArea(mInfoButton);
		
		mCloseButton = new Sprite ( 400, 5, mCloseButtonTR ) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					showMenuItems ();
					mScoreText.setVisible(true);
					mScoreText.setIgnoreUpdate(false);
					
					mCloseButton.setVisible(false);
					mCloseButton.setIgnoreUpdate(true);
					
					mInformation.setVisible(false);
					mInformation.setIgnoreUpdate(true);
					
					mGameState = STATE_MENU;
					
					break;
				}
				return true;
			}
		};
		mCloseButton.setVisible(false);
		mCloseButton.setIgnoreUpdate(true);
		MainScene.getLastChild().attachChild(mCloseButton);
		MainScene.registerTouchArea(mCloseButton);


		
		mAcceleration = -5.0f;
		
		//Initialize the Voice Recorder.
		mRecorder = new Recorder ();
		
		mTargetArray = new float[5];
		
		//Update loop, run in every frame.
		mGameUpdate = new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				updateGame ();
				/*
				if ( ahh == true ) {
					Log.d ("Up", ""+MainGame.ahh);
				}
				if ( pahh == true ) {
					Log.d ("bullet", ""+MainGame.pahh);
				}
				
				ahh = false;
				pahh = false;
				*/
			}

			@Override
			public void reset() {
			}
		};
		
		//Game Update
		MainScene.registerUpdateHandler(mGameUpdate);
		
		MainScene.registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if ( mRecorder != null)
					mRecorder.update();
			}
		}));
		
		//Show Ads.
		//AdsHandler.post(showAdsRunnable);
		
		return MainScene;
	}
	
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_BACK && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if ( mGameState == STATE_MENU ) {
				mRecorder.release();
				mRecorder = null;
				finish();
			} else if ( mGameState == STATE_INFO ) {
				//Back to Menu
				showMenuItems ();
				mScoreText.setVisible(true);
				mScoreText.setIgnoreUpdate(false);
				
				mCloseButton.setVisible(false);
				mCloseButton.setIgnoreUpdate(true);
				
				mInformation.setVisible(false);
				mInformation.setIgnoreUpdate(true);
				
				mGameState = STATE_MENU;
			} else if ( mGameState == STATE_GAME ) {
				//Back to Menu
				showMenuItems ();
				//AdsHandler.post(showAdsRunnable);
				mCamera.setCenter(240, 160);
				mBackGround.setPosition(0, 0);
				mAirPlane.setPosition(200, 100 );
				mFirstMenu = true;
				mGameState = STATE_MENU;
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
    }
	
	/*ScoreLoop
	 * */
	 /*
    @Override
    public void onScoreSubmit(final int status, final Exception error) {
       startActivity(new Intent(this, ShowResultOverlayActivity.class));
    }
    */
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void updateGame () {
		if ( mGameState == STATE_MENU ) {
			//Menu Loop
			if ( mFirstMenu == true ) {
				mAirPlane.setPosition(200, 150);
				mFirstMenu = false;
				mFirstGame = true;
				mScoreText.setPosition(170, 80);
				mAirPlaneSpeedX = 0.0f;

				mHiScoreText.setText(""+mPrefs.getInt(HISCORE_PREFS, 0));
			}
			
			if ( pahh == true ) {
				//Log.d("Start", "Game Start");
				mGameState = STATE_GAME;
				
				//AdsHandler.post(unshowAdsRunnable);
			}
			update ();
			

			
		} else if ( mGameState == STATE_GAME ) {
			//Game Loop.
			/*
			 * Initialize the game.
			 * Disable menu items until the menu show again.
			**/
			if ( mFirstGame == true ) {
				hideMenuItems ();
				
				//mAirPlane.setPosition( 80, mAirPlane.getY());
				mFirstGame = false;
				
				mAirPlaneSpeedX = 0.8f;
				mAirPlane.setPosition(60, 150);
				
				mScoreText.setPosition(20, 10);
				mScore = 0;
				mScoreText.setText(""+mScore);
				
				//Set the Rocks.
				mLastRock = 300;
				
				for ( int i = 0; i < ROCK_NUM; i++ ) {
					mRock[i].setPosition(mLastRock + mRandom.nextFloat() * 200, mRandom.nextFloat() * 265.0f);
					
					mRock[i].setVisible(true);
					mRock[i].setIgnoreUpdate(false);
					mLastRock = mRock[i].getX() + 100;
				}
			}
			
			update ();
			
			for ( int i = 0; i < ROCK_NUM; i++ ) {
				if ( mRock[i].getX() < mCamera.getMinX() - 100 ) {
					if ( mLastRock < mCamera.getMaxX() ) {
						mLastRock = mCamera.getMaxX();
					}
					mRock[i].setPosition(mLastRock + mRandom.nextFloat() * 200, mRandom.nextFloat() * 265.0f);
					//mRock[i].setVisible(true);
					//mRock[i].setIgnoreUpdate(false);
					mLastRock = mRock[i].getX() + 80;
					mRock[i].setRotation(mRandom.nextFloat() * 360);
				}
			}
			
			//Shoot.
			if ( pahh == true ) {
				long curTime = SystemClock.uptimeMillis();
				if ( curTime - mLastLaunch > 200 ) {
					Log.d("SHE", "She");
					mBullet[mBulletIndex].launch();
					mBullet[mBulletIndex].setPosition(mAirPlane.getX() + mAirPlane.getWidth(), mAirPlane.getY() + 10);
					mBulletIndex ++;
					if ( mBulletIndex >= BULLET_NUM ) {
						mBulletIndex = 0;
					}
					mLastLaunch = curTime;
				}
			}
			
			for ( int i = 0; i < BULLET_NUM; i++ ) {
				mBullet[i].updateBullet();
			}
			
			//Smoke
			for ( int i = 0; i < SMOOK_NUM; i++ ) {
				mSmoke[i].update();
			}
			
			//Collide detect.
			for ( int i = 0; i < BULLET_NUM; i++ ) {
				if ( mBullet[i].getState() == Bullet.BULLET_DEAD ) {
					continue;
				}
				if ( mBullet[i].getX() > mCamera.getMaxX() ) {
					mBullet[i].dead();
					mBullet[i].setPosition(-100, 0);
				}
				for ( int j = 0; j < ROCK_NUM; j++ ) {
					if ( mBullet[i].collideWidth(mRock[j], mRock[j].mCircleCollideObject) == true ) {
						mSmoke[mSmokeIndex].setAction(2, true);
						mSmoke[mSmokeIndex].setPosition(mRock[j].getX(), mRock[j].getY());
						mSmokeIndex++;
						if ( mSmokeIndex == SMOOK_NUM ) {
							mSmokeIndex = 0;
						}
						
						mBullet[i].dead();
						mBullet[i].setPosition(-100, 0);
						mRock[j].setPosition(-100, 0);
						
						mScore ++;
						mScoreText.setText(""+mScore);
						break;
					}
				}
			}
			//Check Airplaen
			for ( int i = 0; i < ROCK_NUM; i++ ) {
				if ( mAirPlane.collideWidth(mRock[i], mRock[i].mCircleCollideObject) == true) {
					//Game Over.
					//mGameState = STATE_MENU;
					readyOver = true;
					overTime = SystemClock.uptimeMillis();
					
					mSmoke[mSmokeIndex].setAction(2, true);
					mSmoke[mSmokeIndex].setPosition(mAirPlane.getX(), mAirPlane.getY());
					mSmokeIndex++;
					if ( mSmokeIndex == SMOOK_NUM ) {
						mSmokeIndex = 0;
					}
					
					mAirPlane.setPosition(-100, 100 );
					
					mAirPlaneSpeedX = 0.0f;
				}
			}
			
			if ( readyOver == true ) {
				//Show Game Over Text.
				if ( SystemClock.uptimeMillis() - overTime >= 3000 ) {
					for ( int i = 0; i < ROCK_NUM; i++ ) {
						mRock[i].setVisible(false);
						mRock[i].setIgnoreUpdate(true);
					}
					for ( int i = 0; i < SMOOK_NUM; i++ ) {
						mSmoke[i].setVisible(false);
						mSmoke[i].setIgnoreUpdate(true);
					}
					
					//Show menu
					showMenuItems ();
					
					//AdsHandler.post(showAdsRunnable);
					
					mCamera.setCenter(240, 160);
					mBackGround.setPosition(0, 0);
					mAirPlane.setPosition(200, 100 );
					mGameState = STATE_MENU;
					mFirstMenu = true;
					
					readyOver = false;
					
					// Submits the score. Pass null here for the game "mode"
					//ScoreLoopHandler.post(submitScore);
					
					//update local high score
					if ( mScore > mPrefs.getInt(HISCORE_PREFS, 0) )
						mPrefs.edit().putInt(HISCORE_PREFS, mScore ).commit();
				}
			}
			
			mScoreText.setPosition(mScoreText.getX() + mAirPlaneSpeedX, 10);
			mBackGround.setPosition(mBackGround.getX() + mAirPlaneSpeedX, 0);
			mCamera.setCenter(mCamera.getCenterX() + mAirPlaneSpeedX, mCamera.getCenterY());
			
			//Update game difficulty
			mAirPlaneSpeedX = 0.8f + ( (float)mScore / 80 );
			//Log.d("",""+mAirPlaneSpeedX);
		} else {
			update ();
		}
	
		pahh = false;
		ahh = false;
	}
	
	public void update () {		
		if ( pahh == false ) { 
			if ( mVoicePoint != 0 ) {
				mTargetPoint = 280 - 320 * ( ( mVoicePoint - 500) / 500 );
			} else if ( mVoicePoint == 0 ) {
				mTargetPoint = 280;
			}
		}
		mTargetArray[mTargetIndex] = mTargetPoint;
		mTargetIndex ++;
		mTargetNum ++;
		if ( mTargetNum >= 5 ) {
			mTargetNum = 5;
		}
		if ( mTargetIndex >= 5 ) {
			mTargetIndex = 0;
		}
		
		float TargetPointSum = 0;
		float TargetAverage = 0;
		for ( int i = 0; i < mTargetNum; i++  ) {
			TargetPointSum += mTargetArray[i];
		}
		TargetAverage = TargetPointSum / mTargetNum;

		//Log.d("up", "" + TargetAverage);
		if (mAirPlane.getY() > TargetAverage) {
			if (mAirPlane.getY() > 0) {
				mAirPlane.setPosition(mAirPlane.getX(), mAirPlane.getY() - 4.0f);
			}
		} else if (mAirPlane.getY() < TargetAverage - 5) {
			mAirPlane.setPosition(mAirPlane.getX(), mAirPlane.getY() + 4.0f);
		}
		mAirPlane.setPosition(mAirPlane.getX() + mAirPlaneSpeedX, mAirPlane.getY());
	}
    
	/*
    final static Runnable unshowAdsRunnable = new Runnable() {
        public void run() {
        	mAdView.setVisibility(android.view.View.INVISIBLE);
        	mAdView.setEnabled(false);
        }
    };
    
    final static Runnable showAdsRunnable = new Runnable() {
    	public void run() {
        	mAdView.setVisibility(android.view.View.VISIBLE);
        	mAdView.setEnabled(true);
        	mAdView.loadAd(new AdRequest());
    	}
    };
    */
	
    final Runnable openSettings = new Runnable() {
    	public void run() {
    		startActivity(new Intent(MainGame.this, Prefs.class));
    	}
    };
    
    public void hideMenuItems () {
		mTitleSP.setVisible(false);
		mTitleSP.setIgnoreUpdate(true);
		mStartSP.setVisible(false);
		mStartSP.setIgnoreUpdate(true);
		mScoresSP.setVisible(false);
		mScoresSP.setIgnoreUpdate(true);
		mHiScoresSP.setVisible(false);
		mHiScoresSP.setIgnoreUpdate(true);
		mHiScoreText.setVisible(false);
		mHiScoreText.setIgnoreUpdate(true);
		
		mInfoButton.setVisible(false);
		mInfoButton.setIgnoreUpdate(true);
		MainScene.unregisterTouchArea(mInfoButton);
		
		mBoardButton.setVisible(false);
		mBoardButton.setIgnoreUpdate(true);
		MainScene.unregisterTouchArea(mBoardButton);
		
		mSettingsButton.setVisible(false);
		mSettingsButton.setIgnoreUpdate(true);
		MainScene.unregisterTouchArea(mSettingsButton);
    }
    
    public void showMenuItems () {
		mTitleSP.setVisible(true);
		mTitleSP.setIgnoreUpdate(false);
		mStartSP.setVisible(true);
		mStartSP.setIgnoreUpdate(false);
		mScoresSP.setVisible(true);
		mScoresSP.setIgnoreUpdate(false);
		mHiScoresSP.setVisible(true);
		mHiScoresSP.setIgnoreUpdate(false);
		mHiScoreText.setVisible(true);
		mHiScoreText.setIgnoreUpdate(false);
		
		mInfoButton.setVisible(true);
		mInfoButton.setIgnoreUpdate(false);
		MainScene.registerTouchArea(mInfoButton);
		
		mBoardButton.setVisible(true);
		mBoardButton.setIgnoreUpdate(false);
		MainScene.registerTouchArea(mBoardButton);
		
		mSettingsButton.setVisible(true);
		mSettingsButton.setIgnoreUpdate(false);
		MainScene.registerTouchArea(mSettingsButton);
    }
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}