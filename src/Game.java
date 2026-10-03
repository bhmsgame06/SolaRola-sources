import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.rms.*;

public final class Game extends GameCanvas implements Runnable {
	public static Game instance;
	public static PMMIDlet midlet;

	// Keys.
	public static int heldKeys = 0;
	public static int heldPrevKeys = 0;
	public static int tmpHeldKeys = 0;
	public static int pressOnlyKeys = 0;
	public static final short[][] KEYMAP = new short[][] {
		{'5', -6, -5},
		{'0'},
		{-7},
		{'4', -3},
		{'6', -4},
		{'2', -1},
		{'8', -2},
		{'1'},
		{'3'},
		{'7'},
		{'9'}
	};

	// Image decoding.
	private static byte[] pngTemplate;
	private static int pplOptions;
	private static int pplColorCount;
	private static int pplCRC;
	private static byte[] pplData;

	public static boolean isPaused;
	public static long prevTime;
	public static long currTime;
	public static String recordStoreName = "PMDATA";

	// Audio engine.
	public static boolean isAudioEnabled = true;
	private static int numActiveSoundsMIDI;
	private static int numActiveSounds;
	private static int numSounds;
	private static int activeSound;
	private static long soundStartMillis;
	private static int[] soundLoopCounts;
	private static boolean[] soundActiveStates;
	private static boolean[] soundQueueCleanup;
	private static boolean[] soundUsedStates;
	private static int[] soundVolume;
	private static int[] soundTypes;
	private static String[] soundFilenames;
	private static int numActiveSoundsTotal;
	private static Player[] soundPlayers;
	private static Player[] soundPlayersMIDI;
	
	// BFC streams.
	public static DataInputStream sCurrentData;
	public static int sCurrentSize;
	public static boolean isLoadingBarShown = false;
	public static int sCurrentOffset = -1;
	public static int sCurrentLocation = -1;
	public static int sCurrentIndex = -1;
	public static boolean sCurrentReserved;
	
	// BFC head.
	// Reserve data to RAM for future use (prevent reloading from bfc)
	public static byte[][] bfcReservedData;
	public static int sCurrentOffsetReserved = 0;
	// Filenames' checksums.
	public static short[] bfcHeadCrcs;
	// If >= 0, copy file data to bfcReservedData after sOpenFile().
	public static byte[] bfcHeadMemStates;
	// Offset to the contained file from the start of N.bfc.
	public static int[] bfcHeadOffsets;
	// N.bfc
	public static byte[] bfcHeadLocations;
	// Size of file contents.
	public static int[] bfcHeadSizes;
	public static int bfcHeadNumEntries;
	// Used for filename crc table generating.
	private static int bfcCrcPoly = 0x1021;
	// Filename crc table (we'll turn strings into 16-bit values).
	private static int[] bfcCrcTable = new int[256];
	
	// Text tables.
	public static String[] textTableStrings;
	public static byte[] textTableStringTypes;
	public static int textTableIndex = -1;
	public static short textTableCrc;

	// Softkeys.
	// Current pressed left softkey.
	private static int leftSoftkey;
	// Current pressed right softkey.
	private static int rightSoftkey;
	public static int[] softkeyWidth;
	public static int[] softkeyHeight;
	public static Image[] imgsSoftkey;

	// Fonts.
	public static final int FONT_MAX_CHARS = 230;
	public static Image[] fontImages;
	public static int[] fontAverageWidths;
	public static int[] fontSpacesPerChars;
	public static int[] fontHeights;
	public static int[] fontLineGaps;
	public static short[][] fontCharOffsets;
	public static byte[][] fontCharWidths;
	public static short[][] fontCharMap;

	public static Graphics gfx;
	private static long randSeed;

	// States.
	public static boolean isNewState = true;
	public static int oldState;
	public static int curState = -1;
	public static int newStateIndex;
	public static int stateArg;
	private static Display dpy;
	public static boolean isVibraEnabled = true;

	// Confetti.
	public static int[] confettiColors;
	public static int[] confettiHeights;
	public static int[] confettiAngles;
	public static int[] confettiX;
	public static int[] confettiY;
	public static int[] confettiWidths;
	public static int[] confettiColorsAll = new int[] {
		0xffffff,
		0xff0000,
		0x00ff00,
		0x0000ff,
		0xffff00,
		0xd9d919,
		0xe6e8fa,
		0x8c7853
	};

	// Precipitations.
	public static int[] rainX;
	public static int[] rainY;
	public static int[] precipitationX;
	public static int[] precipitationY;
	public static int[] precipitationDirectionCos;

	// Space.
	public static int[][] spaceStarX;
	public static int[][] spaceStarY;
	public static int[][] spaceStarXVel;
	public static int[][] spaceStarFlashTicks;
	public static int currentSpaceWidth;
	public static int currentSpaceHeight;
	
	// Help scene.
	public static String[] sceneHelpText;
	public static int sceneHelpScrollDistance;
	public static int sceneHelpScrollAccel;
	public static boolean sceneHelpAutoscrollActive;
	public static Image[] imgsScrollbar;
	public static int sceneHelpScroll;

	// Pre level scene.
	public static Image imgCurrentPlanet;
	public static Image imgOutside;
	public static Image imgShipSmall;
	public static String currentPlanetText;
	public static String titleText;
	public static int scenePreLevelSmallShipX;
	public static int scenePreLevelSmallShipY;
	public static int scenePreLevelShipX;
	public static int scenePreLevelShipY;
	public static int scenePreLevelCurTick;

	public static int levelCompletionState = 0;

	// Splash scene.
	public static Image imgCenterLogo;
	public static Image imgEidosLegalLine;
	public static Image imgPmRocket;
	public static int splashLogoX;
	public static int splashLogoY;
	public static int splashTick;
	public static long splashDuration;
	public static int splash = 0;

	public static boolean showControlsGuide;

	// Ship scene.
	// Temporary field to check if the player is staying on the pod hitbox.
	public static int levelShipCheckPodIndex = 0;
	public static int levelShipSelectedPodIndex = 0;
	public static int shipNextOp = 0;
	
	// Dialogue scene.
	public static boolean dialogueIsAwaitingLevelStart = false;
	public static int beamAnimationFrame = 1;
	public static boolean skipPrologue;
	public static int[] currentDialogue;
	public static int currentDialogueIndex;
	public static boolean dialogueSelectionEnabled;
	public static boolean dialogueIsActionConfirmed;
	public static int dialogueLastSwapKey;
	public static int stateBeforeDialogue;
	public static boolean dialogueIsContinued;
	public static int dialogueCameraZoomApproach;
	public static long dialogueWaitEndMs;
	public static boolean isSpeakingAnimationPlaying;
	public static boolean dialogueIsOkKeyEnabled;
	public static boolean dialogueIsWaiting;
	public static boolean showBlackBars;
	public static int dialogueEnvironment;
	public static int dialogueCameraTargetX;
	public static int dialogueCameraTargetY;
	public static int dialogueOriginX;
	public static int dialogueOriginY;
	public static boolean dialogueIsWazActive;
	public static boolean dialogueIsCameraMoving;
	public static int[] dialogueSfx = new int[] {-1, -1, -1, -1, -1, -1, -1, -1};

	// Selection scene.
	public static int sceneSelectionState = 0;

	// Main menu scene.
	public static Image imgGamelogoTop;
	public static Image imgGamelogoBottom;
	public static boolean isPastSplash = false;

	// Transition scene.
	public static int sceneTransitionNextState;
	public static long sceneTransitionStartMs;
	public static int sceneTransitionInvCoeff;
	public static int sceneTransitionType;
	public static boolean sceneTransitionDisabled = true;
	public static int sceneTransitionOffset;

	// Level scene.
	public static int levelFinishX;
	public static int levelFinishY;
	public static int levelReservedField0;
	public static int levelReservedField1;
	public static int levelPlayerRotationRate;
	public static int levelPlayerAngle;
	public static int levelPlayerRed = 200;
	public static int levelPlayerGreen = 0;
	public static int levelPlayerBlue = 0;
	public static int levelPlayerHealth;
	public static int levelPlayerDirection = 1;
	public static int levelBombStartTicks = -1000;
	public static int levelBombNextFlashTick = -1;
	public static boolean levelBombFlashed = false;
	public static int levelBombExplodeTicks = 0;
	public static int levelBombObjectId;
	public static int levelPlayerEyeVectorMagnitude;
	public static int levelPlayerJumpTimes = 0;
	public static int levelPlayerHitTicks = 0;
	public static int levelPlayerCurrentGrabberFlags = -1;
	public static int levelPlayerBlockGrabberId = -1;

	// Facial proportions.
	public static int[] currentEyeBlinkTicks = new int[2];
	public static int[] currentEmotion = new int[2];
	public static Image[] imgsEyeLeft;
	public static Image[] imgsEyeM;
	public static Image[] imgsEyeRight;
	public static Image[] imgsEyeC;
	public static Image[][] imgsMouths;
	public static Image[][] imgsEyes;
	public static Image[][] imgsEyesC;
	public static Image imgMouth;
	public static int[][] eyesX = new int[][] {
		{0, 0},
		{0, 0},
		{0, 0},
		{6, 8},
		{0, -2},
		{0, 0},
		{0, 0},
		{0, 0}
	};
	public static int[][] eyesY = new int[][] {
		{0, -3},
		{3, 0},
		{3, 0},
		{10, 4},
		{0, -5},
		{4, 4},
		{4, 1},
		{6, 4}
	};
	public static int[][] mouthX = new int[][] {
		{0, 0},
		{2, 6},
		{0, 0},
		{-4, -8},
		{4, 2},
		{4, 4},
		{0, 0},
		{2, 6}
	};
	public static int[][] mouthY = new int[][] {
		{0, 0},
		{5, 2},
		{3, 6},
		{5, 9},
		{4, 9},
		{2, 4},
		{3, 7},
		{3, 6}
	};

	public static int swappedLevelReservedField0;
	public static int swappedLevelReservedField1;
	public static int swappedLevelCurrentGrabberFlags;
	public static int swappedLevelPlayerRotationRate;
	public static int swappedLevelPlayerAngle;

	public static Image imgFuse;
	public static Image[] imgsBomb;

	public static int levelNumHooks;
	public static int[] levelHookId1;
	public static int[] levelHookId2;
	public static int[] levelHookType;
	public static int[] levelHookExtent;
	public static int[] levelHookExtentSquare;
	public static int[] levelHookStretchConstant;
	public static boolean[] levelHookIsVisible;
	public static boolean[] levelHookIsActive;

	public static int swappedLevelNumHooks;
	public static int[] swappedLevelHookId1;
	public static int[] swappedLevelHookId2;
	public static int[] swappedLevelHookType;
	public static int[] swappedLevelHookExtent;
	public static int[] swappedLevelHookExtentSquare;
	public static int[] swappedLevelHookStretchConstant;
	public static boolean[] swappedLevelHookIsVisible;
	public static boolean[] swappedLevelHookIsActive;

	public static int[] levelSpiderLegIds;
	public static int[] levelSpiderLegMovementStartX;
	public static int[] levelSpiderLegMovementEndX;
	public static int[] levelSpiderLegTicks;
	public static int[] levelSpiderLegTicksUntilMovement;
	public static int[] levelSpiderLegParent;

	public static int levelExpanderTicks;
	public static int[] levelExpanderIndex;
	public static boolean[] levelExpanderIsTouchActivated;
	public static boolean[] levelExpanderIsVacant;
	public static int[] levelExpanderDelay;
	public static int[] levelExpanderExtent;
	public static int[] levelExpanderMinRadius;

	public static int[] levelGrabberIds;

	public static Image imgGrabber;
	public static int levelPingCircleId;
	public static int[] levelPingShardIds;
	public static int[][] levelPingEnemyIds;
	public static int levelPingHealth;
	public static int levelPingHitTimeout;

	public static Image[] imgsPointyRoll;
	public static Image[] imgsPointyEyes;
	public static Image[] imgsPointyMouth;
	public static Image[] imgsBirdLeft;
	public static Image[] imgsBirdRight;
	public static Image[] imgsSpider;

	public static int[] levelEnemyFrame;
	public static int lastAttackingEnemy = -1;
	public static int levelEnemyTicks;
	public static int[] levelEnemyIndex;
	public static boolean[] levelEnemyCanJump;
	public static int[] levelEnemyFramesUntilJump;
	public static int[] levelEnemyJumpHeight;
	public static boolean[] levelEnemyIsMovingInX;
	public static int[] levelEnemyFramesUntilXMovement;
	public static int[] levelEnemyXMovementSpeed;
	public static int[] levelEnemyXMovementDistance;
	public static int[] levelEnemyXMovementDirection;
	public static int[] levelEnemyCenterX;
	public static int[] levelEnemyMovementStartTicks;
	public static boolean[] levelEnemyIsJitteringX;
	public static boolean[] levelEnemyIsAlive;
	public static boolean[] levelEnemyIsBird;

	public static int levelNumRects;
	public static int[] levelRectX;
	public static int[] levelRectY;
	public static int[] levelRectHWidth;
	public static int[] levelRectHHeight;
	public static boolean[] levelRectIsAnimated;
	public static int[] levelAnimRectIndex;
	public static int[] levelAnimRectMinHWidth;
	public static int[] levelAnimRectMaxHWidth;
	public static int[] levelAnimRectMinHHeight;
	public static int[] levelAnimRectMaxHHeight;
	public static int[] levelAnimRectSpeed;
	public static int[] levelAnimRectIterations;
	public static int[] levelAnimRectCurrentIteration;

	public static int swappedLevelNumRects;
	public static int[] swappedLevelRectX;
	public static int[] swappedLevelRectY;
	public static int[] swappedLevelRectHWidth;
	public static int[] swappedLevelRectHHeight;

	public static Image imgRocket;
	public static Image[] imgsVertFlame;

	public static int[] levelSignX;
	public static int[] levelSignY;
	public static int[] levelSignTypes;
	public static Image[] imgsSign;
	public static boolean mustLoadSigns = false;

	public static int[] levelSpawnStartX;
	public static int[] levelSpawnStartY;
	public static int[] levelSpawnStartIndex;
	public static int[] levelSpawnCurrentIndex;
	public static int[] levelSpawnEndIndex;
	public static int[] levelSpawnEndTick;
	public static int[] levelSpawnCurrentTick;
	public static int[] levelSpawnRadius;
	public static int[] levelSpawnAngle;
	public static int[] levelSpawnAngleVar;
	public static int[] levelSpawnSpeed;
	public static int[] levelSpawnSpeedVar;

	public static int levelNumCircles;
	public static int[] levelCircleX;
	public static int[] levelCircleY;
	public static int[] levelCircleRadius;
	public static int[] levelCirclePrevX;
	public static int[] levelCirclePrevY;
	public static int[] levelCircleFlags;
	public static int[] levelCircle4ByWeight;
	public static byte[] levelCircleType;
	public static boolean[] levelCircleHasPhysics;

	public static int swappedLevelNumCircles;
	public static int[] swappedLevelCircleX;
	public static int[] swappedLevelCircleY;
	public static int[] swappedLevelCircleRadius;
	public static int[] swappedLevelCirclePrevX;
	public static int[] swappedLevelCirclePrevY;
	public static int[] swappedLevelCircleFlags;
	public static int[] swappedLevelCircle4ByWeight;
	public static byte[] swappedLevelCircleType;
	public static boolean[] swappedLevelCircleHasPhysics;

	public static int levelWeatherId = 0;
	public static int levelBackgroundId = 0;
	public static int levelWidth;
	public static int levelHeight;
	public static int levelMovableGravity;
	public static int levelXLossRate;
	public static boolean levelIsPlayerOnSurface;
	public static boolean isLevelComplete;
	public static boolean isMirrored = false;
	public static boolean isFinalLevel;

	public static int currentLevelLoaded = 0;
	public static int level = 0;
	public static int levelColor = 0xffffff;
	public static int levelColorMovable = 0xffffff;
	public static int levelColorGoal = 0xffffff;
	public static int levelColorDestructible = 0xffffff;
	public static int levelColorBouncer = 0xff4500;
	public static int levelRadioactiveMinRed = 0x98;
	public static int levelRadioactiveMaxRed = 0xff;
	public static int levelRadioactiveMinGreen = 0x24;
	public static int levelRadioactiveMaxGreen = 0xb6;
	public static int levelRadioactiveMinBlue = 0x10;
	public static int levelRadioactiveMaxBlue = 0x21;

	public static int levelPortalFilledRadius = 0;

	public static short[] levelIdExpanders;
	public static short[] levelIdFoam;
	public static short[] levelIdDestructibles;
	public static short[] levelIdGoals;
	public static short[] levelIdRadioactive;
	public static short[] levelIdBombDispenser;
	public static short[] levelIdPlatforms;

	public static int activeSwapKey = -1;
	public static int swappedLevelWidth;
	public static int swappedLevelHeight;
	public static int swappedLevelMovableGravity;
	public static int swappedLevelXLossRate;
	public static boolean swappedLevelIsPlayerOnSurface;
	public static boolean swappedIsLevelComplete;
	public static int currentSoundId = -1;
	public static Image[] imgsBackground;
	public static int[][] activeBackgroundPattern;
	public static int[][] activeBackgroundObjectsPattern;
	public static int currentLoadedBackgroundId;

	// NOTE: this field is always zero.
	public static int backgroundOffset;
	public static int backgroundPosY;
	public static int currentBackgroundTotalHeight;
	public static int backgroundObjectsPosY;
	public static boolean currentBackgroundHasObjects = true;
	public static boolean[] backgroundHasObjects = new boolean[] {
		true,
		true,
		true,
		true,
		false
	};
	// Fallback weather IDs.
	public static int[] fallbackWeathers = new int[] {
		2,
		1,
		1,
		2,
		0
	};
	// Background IDs.
	public static int[] knownBackgrounds = new int[] {
		0,
		1,
		2,
		3,
		4
	};

	/* 2D matrices consisting of complete images.
	 *
	 * Assume M is a current level background ID (see knownBackgrounds), and N
	 * is each value in this matrix, then the image filename for a single tile
	 * on the pattern will be bg_M_N.pim. */
	public static int[][][] backgroundPatterns = new int[][][] {
		{
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		},
		{
			{0, 0},
			{0, 0},
			{1, 1}
		},
		{
			{0, 0},
			{0, 0},
			{0, 0}
		},
		{
			{0, 0},
			{0, 0},
			{0, 0}
		},
		{
			{3, 0, 1, 2},
			{0, 1, 2, 0},
			{1, 2, 0, 3},
			{2, 0, 0, 0}
		}
	};

	// The same as backgroundPatterns, but a bit closer to the camera.
	public static int[][][] backgroundObjectsPattern = new int[][][] {
		{
			{1, 2, 1, 2},
			{3, 3, 3, 3},
			{4, 4, 4, 4}
		},
		{
			{2, 2, 2},
			{3, 3, 3},
			{3, 3, 3}
		},
		{
			{1, 2, 1},
			{3, 3, 3},
			{3, 3, 3}
		},
		{
			{1, 1, 1},
			{2, 2, 2},
			{2, 2, 2}
		},
		{
			{0}
		}
	};

	public static int levelCameraIntX;
	public static int levelCameraIntY;
	public static int levelCameraX;
	public static int levelCameraY;
	public static int levelCameraAngle;

	public static int swappedLevelCameraIntX;
	public static int swappedLevelCameraIntY;
	public static int swappedLevelCameraX;
	public static int swappedLevelCameraY;
	public static int swappedLevelCameraAngle;

	public static int showDialogue;
	public static int currentSpeaker;
	public static int currentSpeechNumLines;
	public static int currentSpeakerIteration;
	public static long currentSpeechStartMs;
	public static int currentSpeechIteration;
	public static String currentSpeech;
	public static boolean currentSpeechDone;

	public static Image[][] imgsFaces;
	public static int nextLevelToLoad = -2;
	public static int levelIntroTicks = 0;
	public static boolean mustExitPlayground = false;
	public static boolean isDejaVuMessageShown = false;
	public static boolean isBeamAnimated = false;
	public static int levelDeathTicks = 70;
	public static int levelTicks = 0;
	public static int levelDialogueToDisplay = -1;
	public static int language = 0;
	public static int levelCameraZoom = 72;
	
	// Cheat codes.
	public static int[] cheatCodeLevelComplete = new int[] {8, 16, 8, 16, 2};
	public static int[] cheatCodeInvincibility = new int[] {64, 16, 64, 8, 64, 2};

	public static int iterCheatLevelComplete = 0;
	public static int iterCheatInvincibility = 0;

	public static boolean isPlayerInvincible = false;
	public static boolean isPlayerAlive = true;
	public static boolean pauseScreenDraw = false;
	public static int[] loadingBarColors = new int[12];
	public static int loadingBarColorIndex = 9;
	public static boolean isGammaColorBlack = false;
	public static int gamma = 100;

	// Decors inside ship: socks, flowers, cubes, etc.
	public static short[] decorForeground;
	public static short[] decorBackground;
	public static Image[] imgsDecor;

	public static Image imgArrowLeft;
	public static Image imgArrowRight;
	public static Image[] imgsShipIcons;
	public static Image[] imgsShipIconsOff;
	public static Image[] imgsPiston;
	public static Image[] imgsInside;
	public static Image imgInsideLamp;
	public static Image imgWindow;
	public static Image imgSplashShip;
	public static Image[] imgsFlame;

	public static int levelShipTouchedCircleType;
	public static int shipAlarmRadius = 0;
	public static String[] textTableShip;
	public static String[] textTableShipPause;
	public static boolean isShipPaused = false;
	public static boolean isTextRendered = false;
	public static int spaceMapCameraX;
	public static int spaceMapCameraY;
	public static int spaceMapTargetCameraX;
	public static int spaceMapTargetCameraY;
	public static int spaceMapShipX;
	public static int spaceMapShipY;
	public static int spaceMapPlanetX;
	public static int spaceMapPlanetY;
	public static int spaceMapPlanetBeaconRadius;
	public static boolean spaceMapShowShip;
	public static boolean spaceMapShowPlanet;
	public static Image imgPointer;
	public static Image imgShip;
	public static Image imgPlanet;
	public static int levelCompleteTicks;
	public static int victoryCameraX;
	public static int victoryCameraY;
	public static int victoryCameraZoom;
	public static String textMission;
	public static String textCompleted;

	// Trigonometric.
	public static short[] sin1000;
	public static short[] cos1000;

	public static Image[] imgsStatic;
	public static int[] staticTiles;

	public static boolean isTransmodigrafierMissing = true;
	public static boolean isTransmissionShaky = true;
	public static boolean isShowingShardPicture = false;
	public static boolean isTransmissionBlinked = false;

	public static int currentPurpleX = 59;
	public static int currentPurpleY = 108;
	public static int currentPurpleSize = 100;
	public static int pingBackgroundColor = 0;
	public static int activePurpleShardNameId = 0;

	public static String[] purpleShardNames = new String[] {
		"Kachoo",
		"NikSak",
		"Rara",
		"Corakllquar",
		"Brad",
		"NotPing"
	};

	// Selection menu.
	public static Image[] imgsSelectionMenu;
	public static Image[] imgsSelectionMenuArrows;
	public static int selectionState;
	public static long selectionStateStartMs;
	public static int selectionAngle;
	public static int selectionAngleBetweenOthers;
	public static int selectionAngleUntilAdjust;
	public static int numSelections;
	public static int selectionMainCircleId;
	public static int selectionDefaultIndex;
	public static int selectionArrowsAnimTicks;

	public Game() throws IOException {
		super(false);
		setFullScreenMode(true);
		instance = this;
	}

	public final void hideNotify() {
		tmpHeldKeys = 0;
		isPaused = true;
		queueAllSoundsForCleanup();
		queueSoundCleanup();
		setPauseScreenDraw();
	}

	public final void showNotify() {
	}

	public final void init(PMMIDlet midlet) {
		Game.midlet = midlet;
		Thread th = new Thread(this);
		th.setPriority(Thread.NORM_PRIORITY);
		th.start();
	}

	public final void run() {
		gfx = this.getGraphics();
		gSetClip(0, 0, 128, 128);
		start();
		midlet.exit();
	}

	// Funny circle.
	public static final void renderLoadingBar() {
		renderLoadingBarNextFrame();
	}

	public static final void updateKeys() {
		sleep(5L);
		heldPrevKeys = heldKeys;
		heldKeys = tmpHeldKeys;
		tmpHeldKeys &= ~pressOnlyKeys;
		pressOnlyKeys = 0;
	}

	private static int pressedKeyValue(int keyCode) {
		for (int k = 0; k < KEYMAP.length; k++) {
			for (int i = 0; i < KEYMAP[k].length; i++) {
				if (keyCode == KEYMAP[k][i]) {
					return 1 << k;
				}
			}
		}

		return 0x100000;
	}

	public final void keyPressed(int keyCode) {
		tmpHeldKeys |= pressedKeyValue(keyCode);
		super.keyPressed(keyCode);

		if (keyCode == -6) {
			pressOnlyKeys |= 1;
			pressOnlyKeys |= 0x0800;
			tmpHeldKeys |= 0x0800;
		}

		if (keyCode == -7) {
			pressOnlyKeys |= 4;
			pressOnlyKeys |= 0x1000;
			tmpHeldKeys |= 0x1000;
		}
	}

	public final void keyReleased(int keyCode) {
		tmpHeldKeys &= ~pressedKeyValue(keyCode);
		super.keyReleased(keyCode);
	}

	public static final boolean isKeyHeld(int mask) {
		return (heldKeys & mask) > 0;
	}

	public static final boolean isKeyPressed(int mask) {
		return (heldKeys & mask) > 0 && (heldPrevKeys & mask) == 0;
	}

	public static final boolean isKeyReleased(int mask) {
		return (heldPrevKeys & mask) > 0 && (heldKeys & mask) == 0;
	}

	public static final void clearKeys() {
		heldKeys = 0;
		heldPrevKeys = 0;
		tmpHeldKeys = 0;
		pressOnlyKeys = 0;
	}

	public static final byte[] makePNG(byte[] pimData, short pplCrc) {
		if (pngTemplate == null) {
			pngTemplate = loadFile8((short)0xdee7);
		}

		loadPalette(pplCrc);

		if ((pimData[0] & 3) != 3) {
			return null;
		} else {
			int idatLen = pimData.length - 18;
			byte[] pngData = new byte[0x21 + 0x0c + (pplColorCount * 3) + (0x0d * (pplOptions & 1)) + 0x0c + idatLen + 0x04 + 0x0c];
			int off = 0;

			// PNG sig with IHDR
			System.arraycopy(pngTemplate, 0x00, pngData, 0x00, 0x1d);

			// Width and height.
			System.arraycopy(pimData, 0x02, pngData, 0x12, 0x02);
			System.arraycopy(pimData, 0x04, pngData, 0x16, 0x02);

			// IHDR CRC
			System.arraycopy(pimData, 0x06, pngData, 0x1d, 0x04);

			// PLTE
			System.arraycopy(intToBytes(pplColorCount * 3), 0x00, pngData, 0x21, 0x04);
			System.arraycopy(pngTemplate, 0x2a, pngData, 0x25, 0x04);
			System.arraycopy(pplData, 0x00, pngData, 0x29, pplData.length);
			off = 41 + pplData.length;
			System.arraycopy(intToBytes(pplCRC), 0x00, pngData, off, 0x04);
			off += 4;

			// tRNS
			if ((pplOptions & 1) == 1) {
				System.arraycopy(pngTemplate, 0x1d, pngData, off, 0x0d);
				off += 13;
			}

			// IDAT
			System.arraycopy(intToBytes(idatLen + 4), 0x00, pngData, off, 0x04);
			System.arraycopy(pngTemplate, 0x2e, pngData, off + 4, 0x04);
			System.arraycopy(pimData, 0x12, pngData, off + 8, idatLen);
			off += idatLen + 8;

			// IEND
			System.arraycopy(pimData, 0x0e, pngData, off, 0x04);
			System.arraycopy(pimData, 0x0a, pngData, off + 4, 0x04);
			System.arraycopy(pngTemplate, 0x34, pngData, off + 8, 0x0c);

			pplData = null;
			System.gc();
			return pngData;
		}
	}

	public static final byte[] intToBytes(int i) {
		byte[] b = new byte[4];
		b[0] = (byte)(i >> 24 & 0xff);
		b[1] = (byte)(i >> 16 & 0xff);
		b[2] = (byte)(i >> 8 & 0xff);
		b[3] = (byte)(i & 0xff);
		return b;
	}

	public static final void loadPalette(short pplCrc) {
		sOpenFile(pplCrc);
		pplOptions = sRead8();
		pplColorCount = 1 + sReadU8();
		pplCRC = sRead32();
		pplData = new byte[pplColorCount * 3];
		sReadBytes(pplData, 0, pplData.length);
	}

	public static final Image loadImage(String pimCrc, String pplCrc) {
		return loadImage(bfcCrcFilename(pimCrc), bfcCrcFilename(pplCrc));
	}

	public static final Image loadImage(short pimCrc, short pplCrc) {
		return _loadImage(pimCrc, pplCrc);
	}

	public static final Image _loadImage(short pimCrc, short pplCrc) {
		byte[] pimData = getFile8(pimCrc);
		if (pimData == null) {
			return null;
		} else {
			if (pimData[0] != 0x89 && pimData[1] != 'P') {
				pimData = makePNG(pimData, pplCrc);
			}

			Image img = Image.createImage(pimData, 0, pimData.length);
			System.gc();
			sleep(10L);
			return img;
		}
	}

	public static final void refreshGame() {
		queueSoundCleanup();
		renderSoftkeyIcons();
		updateKeys();
		updateTransition();
		gfxFlush();
		lockFPS();
	}

	public static final void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch(Exception e) {
		}
	}

	public static final void garbageCollector() {
		long initFreeMem = Runtime.getRuntime().freeMemory();
		System.gc();

		for (int i = 0; i < 10; i++) {
			Thread.yield();
			if (Runtime.getRuntime().freeMemory() < initFreeMem) {
				return;
			}
		}
	}

	public static final void lockFPS() {
		currTime = millis();
		if (currTime - prevTime < 45L) {
			sleep(45L - (currTime - prevTime));
		}

		prevTime = millis();
	}

	public static final void writeRecordBytes(byte[] data) {
		try {
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			if (rs != null) {
				if (rs.getNumRecords() == 0) {
					rs.addRecord(data, 0, data.length);
				} else {
					rs.setRecord(1, data, 0, data.length);
				}

				rs.closeRecordStore();
			}
		} catch(Exception e) {
		}
	}

	public static final byte[] readRecordBytes() {
		byte[] data = null;

		try {
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			if (rs != null) {
				data = rs.getRecord(1);
				rs.closeRecordStore();
			}
		} catch(Exception e) {
		}

		return data;
	}

	public static final void saveRecordData() {
		byte[] data = new byte[10];
		data[0] = (byte)(language & 0xff);
		data[1] = (byte)(language >> 8 & 0xff);
		data[2] = (byte)(language >> 16 & 0xff);
		data[3] = (byte)(language >> 24 & 0xff);
		data[4] = (byte)(level & 0xff);
		data[5] = (byte)(level >> 8 & 0xff);
		data[6] = (byte)(level >> 16 & 0xff);
		data[7] = (byte)(level >> 24 & 0xff);
		data[8] = (byte)(isMirrored ? 1 : 0);
		data[9] = (byte)(isVibraEnabled ? 1 : 0);
		writeRecordBytes(data);
	}

	public static final boolean loadRecordData() {
		byte[] data = readRecordBytes();

		if (data == null) {
			return false;
		} else {
			try {
				language = (data[0] & 0x000000ff) + (data[1] << 8 & 0x0000ff00) + (data[2] << 16 & 0x00ff0000) + (data[3] << 24 & 0xff000000);
				level = (data[4] & 0x000000ff) + (data[5] << 8 & 0x0000ff00) + (data[6] << 16 & 0x00ff0000) + (data[7] << 24 & 0xff000000);
				isMirrored = data[8] == 1;
				isVibraEnabled = data[9] == 1;
				return true;
			} catch(Exception e) {
				return false;
			}
		}
	}

	public static final void initSoundSystem(int num) {
		numSounds = num;

		soundActiveStates = new boolean[num];
		soundQueueCleanup = new boolean[num];
		soundUsedStates = new boolean[num];
		soundVolume = new int[num];
		soundLoopCounts = new int[num];
		soundTypes = new int[num];
		activeSound = -1;
		soundPlayers = new Player[num];
		soundPlayersMIDI = new Player[num];
		numActiveSoundsMIDI = 0;
		numActiveSounds = 0;
		soundFilenames = new String[num];

		for (int i = 0; i < numSounds; i++) {
			soundActiveStates[i] = false;
			soundQueueCleanup[i] = false;
			soundUsedStates[i] = false;
			soundVolume[i] = 5;
			soundLoopCounts[i] = 1;
			soundTypes[i] = 999;
			soundPlayers[i] = null;
			soundPlayersMIDI[i] = null;
		}
	}

	public static final int loadSoundToQueue(int index, String filename, int soundType, int reserved) {
		if (isSoundIndexUsed(index)) {
			return -2;
		} else if (index >= numSounds) {
			return -3;
		} else if (filename == null) {
			return -1;
		} else {
			if (isLoadingBarShown) {
				renderLoadingBar();
			}

			soundTypes[index] = soundType;
			if (!filename.equals(soundFilenames[index])) {
				soundFilenames[index] = filename;
			}

			return -4;
		}
	}

	public static final int loadSoundToIndex(int index, String filename, int soundType, int reserved) {
		if (isSoundIndexUsed(index)) {
			return -2;
		} else if (index >= numSounds) {
			return -3;
		} else if (filename == null) {
			return -1;
		} else {
			if (isLoadingBarShown) {
				renderLoadingBar();
			}

			soundTypes[index] = soundType;
			if (!filename.equals(soundFilenames[index])) {
				soundFilenames[index] = filename;
			}

			String obj = "";
			Player pl = null;

			try {
				if (soundType == 6) {
					InputStream is = obj.getClass().getResourceAsStream("/" + filename + ".wav");
					soundPlayers[index] = Manager.createPlayer(is, "audio/x-wav");
					pl = soundPlayers[index];
				}

				if (soundType == 7) {
					InputStream is = obj.getClass().getResourceAsStream("/" + filename + ".amr");
					soundPlayers[index] = Manager.createPlayer(is, "audio/amr");
					pl = soundPlayers[index];
				}

				if (soundType == 8) {
					InputStream is = obj.getClass().getResourceAsStream("/" + filename + ".mp3");
					soundPlayers[index] = Manager.createPlayer(is, "audio/mp3");
					pl = soundPlayers[index];
				}

				if (soundType == 2) {
					InputStream is = obj.getClass().getResourceAsStream("/" + filename + ".mid");
					soundPlayersMIDI[index] = Manager.createPlayer(is, "audio/sp-midi");
					pl = soundPlayersMIDI[index];
				}

				if (soundType == 1) {
					InputStream is = obj.getClass().getResourceAsStream("/" + filename + ".mid");
					soundPlayersMIDI[index] = Manager.createPlayer(is, "audio/midi");
					pl = soundPlayersMIDI[index];
				}

				pl.realize();

				while (pl.getState() != Player.REALIZED) {
					sleep(30L);
				}

				numActiveSoundsTotal++;
				soundUsedStates[index] = true;

				boolean prefetched = false;
				if (soundType != 1 && soundType != 2) {
					if (soundType != 6 && soundType != 7 && soundType != 8) {
						return -2;
					}

					if (numActiveSounds < 1) {
						pl.prefetch();
						numActiveSounds++;
						prefetched = true;
					}
				} else if (numActiveSoundsMIDI < 1) {
					pl.prefetch();
					numActiveSoundsMIDI++;
					prefetched = true;
				}

				if (prefetched) {
					while (pl.getState() != Player.PREFETCHED) {
						sleep(30L);
					}
				}

				return 0;
			} catch(Exception e) {
				if (pl != null) {
					pl.close();
					soundPlayers[index] = null;
					soundPlayersMIDI[index] = null;
					soundTypes[index] = 999;
					soundFilenames[index] = "";
				}

				return -1;
			}
		}
	}

	public static final void closeSoundAtIndex(int index) {
		Player pl = null;

		if (soundPlayers[index] != null) {
			pl = soundPlayers[index];
			numActiveSounds--;
		}

		if (soundPlayersMIDI[index] != null) {
			pl = soundPlayersMIDI[index];
			numActiveSoundsMIDI--;
		}

		if (pl != null) {
			pl.close();

			while (pl.getState() != Player.CLOSED) {
				sleep(30L);
			}

			soundPlayers[index] = soundPlayersMIDI[index] = null;
			numActiveSoundsTotal--;
		}
	}

	public static final boolean isSoundIndexUsed(int index) {
		if (soundPlayers[index] != null) {
			return true;
		} else {
			return soundPlayersMIDI[index] != null;
		}
	}

	public static final boolean isSoundActive(int index, int soundType) {
		if (!isAudioEnabled) {
			return false;
		} else {
			boolean started = false;

			if ((soundType == soundTypes[index] || soundType == -1) && soundActiveStates[index]) {
				return true;
			} else {
				if ((soundType == 6 || soundType == -1) && soundPlayers[index] != null) {
					started = soundPlayers[index].getState() == Player.STARTED;
				}

				if ((soundType == 1 || soundType == -1) && soundPlayersMIDI[index] != null) {
					started = soundPlayersMIDI[index].getState() == Player.STARTED;
				}

				return started;
			}
		}
	}

	public static final boolean playSound(int index, int loopCount) {
		if (!isAudioEnabled) {
			return true;
		} else if (millis() - soundStartMillis < 100L) {
			return false;
		} else {
			soundStartMillis = millis();

			if (isSoundActive(index, -1)) {
				return false;
			} else {
				int activeSounds = 0;

				if (soundTypes[index] == 6 || soundTypes[index] == 7 || soundTypes[index] == 8) {
					for (int i = 0; i < numSounds; i++) {
						if (isSoundActive(i, soundTypes[i])) {
							activeSounds++;
						}
					}

					if (activeSounds >= 2) {
						return false;
					}
				}

				soundActiveStates[index] = true;
				soundLoopCounts[index] = loopCount;

				return true;
			}
		}
	}

	public static final void queueAllSoundsForCleanup() {
		if (isAudioEnabled) {
			for (int i = 0; i < numSounds; i++) {
				soundQueueCleanup[i] = true;
			}
		}
	}

	private static void stopSoundAtIndex(int index, boolean checkStarted) {
		Player pl;
		if (soundPlayersMIDI[index] != null) {
			pl = soundPlayersMIDI[index];
		} else {
			pl = soundPlayers[index];
		}

		try {
			if (pl != null) {
				try {
					if (checkStarted) {
						if (pl.getState() == Player.STARTED) {
							pl.stop();
							pl.setMediaTime(0L);
						}
					} else {
						pl.stop();
						pl.setMediaTime(0L);
					}
				} catch(IllegalStateException e) {
				}
			}
		} catch(MediaException e) {
		}
	}

	public static final void queueSoundCleanup() {
		if (isAudioEnabled && soundActiveStates != null) {
			for (int i = 0; i < numSounds; i++) {
				if (soundQueueCleanup[i]) {
					soundQueueCleanup[i] = false;
					stopSoundAtIndex(i, false);

					if (soundTypes[i] != 0) {
						closeSoundAtIndex(i);
					}

					if (activeSound == i) {
						activeSound = -1;
					}
				}

				if (soundActiveStates[i] && soundTypes[i] == 1) {
					for (int j = 0; j < numSounds; j++) {
						if (j != i) {
							stopSoundAtIndex(j, false);
							soundActiveStates[j] = false;
						}
					}

					for (boolean b = true; b; sleep(5L)) {
						for (int j = 0; j < numSounds; j++) {
							if (j != i && isSoundActive(j, -1)) {
								b = true;
								break;
							}

							b = false;
						}
					}
				}

				if (!isSoundIndexUsed(i)) {
					if (soundTypes[i] == 0 || !soundActiveStates[i]) {
						continue;
					}

					boolean tmp = isLoadingBarShown;
					isLoadingBarShown = false;
					loadSoundToIndex(i, soundFilenames[i], soundTypes[i], 0);
					isLoadingBarShown = tmp;
				}

				Player pl;
				if (soundPlayersMIDI[i] != null) {
					pl = soundPlayersMIDI[i];
				} else {
					pl = soundPlayers[i];
				}

				if (soundUsedStates[i]) {
					soundUsedStates[i] = false;
					if (pl != null) {
						try {
							VolumeControl control;
							if ((control = (VolumeControl)pl.getControl("VolumeControl")) != null) {
								control.setLevel(soundVolume[i] * 10);
							}
						} catch(IllegalStateException e) {
						}
					}
				}

				if (soundActiveStates[i]) {
					soundActiveStates[i] = false;

					try {
						if (pl != null && pl.getState() != Player.STARTED) {
							pl.setLoopCount(soundLoopCounts[i]);
							pl.start();
							activeSound = i;
						}
					} catch(MediaException e) {
					}
				}
			}
		}
	}

	public static final int getFileIndex(short fnCrc) {
		for (int i = 0; i < bfcHeadNumEntries; i++) {
			if (bfcHeadCrcs[i] == fnCrc) {
				return i;
			}
		}

		return -1;
	}

	public static final void bfcInitReservedData(int count) {
		bfcReservedData = new byte[count][];
		sCurrentReserved = false;
	}

	public static final void bfcReserve(int index) {
		try {
			sCurrentIndex = index;
			sCurrentReserved = true;
			sCurrentOffsetReserved = 0;
			bfcReservedData[index] = new byte[sCurrentSize];

			int read = 0;
			int total = 0;	
			for (int rem = sCurrentSize; rem > 0; total += read) {
				read = sCurrentData.read(bfcReservedData[index], total, rem);
				rem -= read;
			}

			for (int i = 0; i < bfcHeadNumEntries; i++) {
				if (i != index && bfcHeadOffsets[i] == bfcHeadOffsets[index] && bfcHeadLocations[i] == bfcHeadLocations[index]) {
					bfcReservedData[i] = bfcReservedData[index];
				}
			}

			sCurrentOffset += sCurrentSize;
		} catch(Exception e) {
		}
	}

	public static final void bfcLoadHead() {
		sCurrentLocation = -1;
		sCurrentOffset = 0;
		bfcGenCrcTable();

		try {
			String file = "/head.bfc";
			sCurrentData = new DataInputStream(file.getClass().getResourceAsStream(file));

			int numEntries = sCurrentData.readUnsignedShort();
			bfcHeadNumEntries = numEntries;
			bfcHeadCrcs = new short[numEntries];
			bfcHeadMemStates = new byte[numEntries];
			bfcHeadOffsets = new int[numEntries];
			bfcHeadLocations = new byte[numEntries];
			bfcHeadSizes = new int[numEntries];

			for (int i = 0; i < numEntries; i++) {
				bfcHeadCrcs[i] = (short)sCurrentData.readUnsignedShort();
				bfcHeadMemStates[i] = sCurrentData.readByte();
				bfcHeadOffsets[i] = sCurrentData.readUnsignedByte() << 16 | sCurrentData.readUnsignedByte() << 8 | sCurrentData.readUnsignedByte();
				bfcHeadLocations[i] = (byte)sCurrentData.readUnsignedByte();
				bfcHeadSizes[i] = sCurrentSize = sCurrentData.readUnsignedByte() << 16 | sCurrentData.readUnsignedByte() << 8 | sCurrentData.readUnsignedByte();
			}
		} catch(Exception e) {
		}

		bfcInitReservedData(bfcHeadNumEntries);
	}

	public static final void bfcGenCrcTable() {
		for (int i = 0; i < 256; ++i) {
			int val = 0;
			int x = i << 8;

			for (int k = 0; k < 8; k++) {
				if (((val ^ x) & 0x8000) != 0) {
					val = val << 1 ^ bfcCrcPoly;
				} else {
					val <<= 1;
				}

				x <<= 1;
				val &= 0xffff;
			}

			bfcCrcTable[i] = val;
		}
	}

	public static final short bfcCrcFilename(String file) {
		int val = 0xffff;

		for (int i = 0; i < file.length(); i++) {
			char c = file.charAt(i);
			byte b = (byte)(c >> 8);
			val = (bfcCrcTable[(b ^ val >> 8) & 0xff] ^ val << 8) & 0xffff;
			b = (byte)(c & 0xff);
			val = (bfcCrcTable[(b ^ val >> 8) & 0xff] ^ val << 8) & 0xffff;
		}

		return (short)(val & 0xffff);
	}

	public static final byte[] loadFile8(short fnCrc) {
		if (!sOpenFile(fnCrc)) {
			return null;
		} else {
			byte[] arr = new byte[sCurrentSize];
			sReadBytes(arr, 0, sCurrentSize);
			return arr;
		}
	}

	public static final byte[] getFile8(short fnCrc) {
		int index = getFileIndex(fnCrc);
		if (index < 0) {
			return null;
		} else {
			return bfcReservedData[index] != null ? bfcReservedData[index] : loadFile8(fnCrc);
		}
	}

	public static final short[] loadFile16(String file) {
		return loadFile16(bfcCrcFilename(file));
	}

	public static final short[] loadFile16(short fnCrc) {
		if (!sOpenFile(fnCrc)) {
			return null;
		} else {
			short[] arr = new short[sCurrentSize / 2];

			for (int i = 0; i < arr.length; i++) {
				arr[i] = sRead16();
			}

			return arr;
		}
	}

	public static final int[] loadFile32(String file) {
		return loadFile32(bfcCrcFilename(file));
	}

	public static final int[] loadFile32(short fnCrc) {
		if (!sOpenFile(fnCrc)) {
			return null;
		} else {
			int[] arr = new int[sCurrentSize / 4];

			for (int i = 0; i < arr.length; i++) {
				arr[i] = sRead32();
			}

			return arr;
		}
	}

	public static final boolean sOpenFile(String file) {
		if (sOpenFile(bfcCrcFilename(file))) {
			return true;
		} else {
			try {
				InputStream is = file.getClass().getResourceAsStream("/" + file);
				if (is == null) {
					return false;
				} else {
					sCurrentData = new DataInputStream(is);
					return true;
				}
			} catch(Exception e) {
				return false;
			}
		}
	}

	public static final boolean sOpenFile(short fnCrc) {
		if (isLoadingBarShown) {
			renderLoadingBar();
		}

		int index = getFileIndex(fnCrc);
		sCurrentIndex = index;
		sCurrentOffsetReserved = 0;
		sCurrentReserved = false;
		if (index >= 0 && bfcReservedData[index] != null) {
			sCurrentSize = bfcHeadSizes[index];
			sCurrentReserved = true;
			return true;
		} else {
			if (index != -1) {
				sCurrentSize = bfcHeadSizes[index];
				if (sCurrentLocation == bfcHeadLocations[index] && sCurrentOffset <= bfcHeadOffsets[index]) {
					sSkipBytes(bfcHeadOffsets[index] - sCurrentOffset);
					if (bfcHeadMemStates[index] >= 0 && bfcReservedData[index] == null) {
						bfcReserve(index);
					}

					return true;
				}

				try {
					if (sCurrentData != null) {
						sCloseFile();
					}

					sCurrentData = new DataInputStream(instance.getClass().getResourceAsStream("/" + bfcHeadLocations[index] + ".bfc"));
					sCurrentData.skip((long)bfcHeadOffsets[index]);
					sCurrentLocation = bfcHeadLocations[index];
					sCurrentOffset = bfcHeadOffsets[index];
					if (bfcHeadMemStates[index] >= 0 && bfcReservedData[index] == null) {
						bfcReserve(index);
					}

					return true;
				} catch(Exception e) {
				}
			}

			return false;
		}
	}

	public static final void sSkipBytes(int n) {
		if (sCurrentReserved) {
			sCurrentOffsetReserved += n;
		} else {
			sCurrentOffset += n;

			try {
				while (n > 0) {
					n = (int)((long)n - sCurrentData.skip((long)n));
				}
			} catch(Exception e) {
			}
		}
	}

	public static final byte[] sReadBytes(byte[] b, int off, int len) {
		if (sCurrentReserved) {
			System.arraycopy(bfcReservedData[sCurrentIndex], sCurrentOffsetReserved, b, off, len);
			sCurrentOffsetReserved += len;
			return b;
		} else {
			sCurrentOffset += len;

			try {
				for (int i = 0; len > 0; off += i) {
					i = sCurrentData.read(b, off, len);
					len -= i;
				}

				return b;
			} catch(Exception e) {
				return null;
			}
		}
	}

	public static final int sReadU16() {
		if (sCurrentReserved) {
			int val = (bfcReservedData[sCurrentIndex][sCurrentOffsetReserved] & 0xff) << 8 | bfcReservedData[sCurrentIndex][sCurrentOffsetReserved + 1] & 0xff;
			sCurrentOffsetReserved += 2;

			return val;
		} else {
			sCurrentOffset += 2;

			try {
				return sCurrentData.readUnsignedShort();
			} catch(Exception e) {
				return -1;
			}
		}
	}

	public static final short sRead16() {
		if (sCurrentReserved) {
			short val = (short)(bfcReservedData[sCurrentIndex][sCurrentOffsetReserved] << 8 | bfcReservedData[sCurrentIndex][sCurrentOffsetReserved + 1] & 0xff);
			sCurrentOffsetReserved += 2;

			return val;
		} else {
			sCurrentOffset += 2;

			try {
				return sCurrentData.readShort();
			} catch(Exception e) {
				return -1;
			}
		}
	}

	public static final int sReadU8() {
		if (sCurrentReserved) {
			int val = bfcReservedData[sCurrentIndex][sCurrentOffsetReserved] & 0xff;
			sCurrentOffsetReserved++;

			return val;
		} else {
			sCurrentOffset++;

			try {
				return sCurrentData.readUnsignedByte();
			} catch(Exception e) {
				return -1;
			}
		}
	}

	public static final byte sRead8() {
		if (sCurrentReserved) {
			byte val = bfcReservedData[sCurrentIndex][sCurrentOffsetReserved];
			sCurrentOffsetReserved++;

			return val;
		} else {
			sCurrentOffset++;

			try {
				return sCurrentData.readByte();
			} catch(Exception e) {
				return -1;
			}
		}
	}

	public static final int sRead32() {
		if (sCurrentReserved) {
			int val = (bfcReservedData[sCurrentIndex][sCurrentOffsetReserved] & 0xff) << 24 | (bfcReservedData[sCurrentIndex][sCurrentOffsetReserved + 1] & 0xff) << 16 | (bfcReservedData[sCurrentIndex][sCurrentOffsetReserved + 2] & 0xff) << 8 | bfcReservedData[sCurrentIndex][sCurrentOffsetReserved + 3] & 0xff;
			sCurrentOffsetReserved += 4;

			return val;
		} else {
			sCurrentOffset += 4;

			try {
				return sCurrentData.readInt();
			} catch(Exception e) {
				return -1;
			}
		}
	}

	public static final void sCloseFile() {
		try {
			sCurrentData.close();
			sCurrentIndex = -1;
			sCurrentLocation = -1;
		} catch(Exception e) {
		}

		sCurrentData = null;
	}

	public static final void setTextTableCrc(short crc) {
		clearTextTable();
		textTableCrc = crc;
	}

	public static final void clearTextTable() {
		textTableStrings = null;
		textTableIndex = -1;
	}

	public static final String[] loadTextTableFromIndex(int index, int stringIndex) {
		if (stringIndex < 0 && index == textTableIndex) {
			return textTableStrings;
		} else {
			boolean tmp = isLoadingBarShown;
			isLoadingBarShown = false;
			if (!sOpenFile(textTableCrc)) {
				textTableStrings = null;
				textTableStringTypes = null;
			}
			isLoadingBarShown = tmp;

			// Text table file header.
			int bitness = sReadU8(); // 0 = 16-bit for each char, 1 = 8-bit for each char.
			int numTables = sReadU8();

			if (index >= numTables) {
				return textTableStrings;
			} else {
				sSkipBytes(index * 2);
				sSkipBytes(sReadU16() + (numTables - index - 1) * 2);

				int numStrings = sReadU16();
				int[] stringLens = new int[numStrings];

				if (stringIndex < 0) {
					textTableStrings = new String[numStrings];
					textTableStringTypes = new byte[numStrings];
					textTableIndex = index;

					for (int i = 0; i < numStrings; i++) {
						stringLens[i] = sReadU16();
						sReadU16();
					}
				} else {
					if (stringIndex >= numStrings) {
						return textTableStrings;
					}

					sSkipBytes(stringIndex * 4);
					stringLens[0] = sReadU16();
					sSkipBytes(sReadU16() + (numStrings - stringIndex - 1) * 4);
				}

				for (int i = 0; i < numStrings; i++) {
					stringLens[i]--;

					int type = 0;
					char[] charArr = new char[stringLens[i]];

					if (bitness == 1) {
						type = sReadU8();

						for (int j = 0; j < stringLens[i]; j++) {
							charArr[j] = (char)sReadU8();
						}
					} else {
						type = sReadU16();

						for (int j = 0; j < stringLens[i]; j++) {
							charArr[j] = (char)sReadU16();
						}
					}

					String string = String.valueOf(charArr);
					if (stringIndex >= 0) {
						break;
					}

					textTableStringTypes[i] = (byte)type;
					textTableStrings[i] = string;
				}

				return textTableStrings;
			}
		}
	}

	public static final String getText(int stringAddr) {
		int index = stringAddr >> 16;
		if (textTableIndex != index) {
			loadTextTableFromIndex(index, -1);
		}

		return textTableStrings[stringAddr & 0xffff];
	}

	// Fixed-point multiplication operation.
	public static final int lp32Mul(int x, int y) {
		return (int)((long)x * (long)y >> 16);
	}

	// Fixed-point division operation.
	public static final int lp32Div(int x, int y) {
		return (int)(((long)x << 16) / (long)y);
	}

	// Find the length (magnitude) of the vector using the Pythagorean theorem.
	public static final int vectorMagnitude(int x, int y, int assume) {
		long sqrX = (long)x;
		long sqrY = (long)y;
		sqrX = sqrX * sqrX >> 16;
		sqrY = sqrY * sqrY >> 16;
		return (int)sqrtGuess(sqrX + sqrY, (long)assume);
	}

	// Newton's method for calculating the square root.
	public static final long sqrtGuess(long arg, long assume) {
		if (assume == 0L) {
			return sqrt(arg);
		} else {
			for (int i = 0; i < 5; i++) {
				assume -= ((assume * assume >> 16) - arg << 16) / (assume << 2);
			}

			return assume;
		}
	}

	// Long-division method for calculating the square root.
	public static final long sqrt(long arg) {
		long val = arg;

		long place = 0L;
		long numberInCurPlace = arg;

		for (long tmpPlace = 1L; numberInCurPlace > 0L; numberInCurPlace = val / tmpPlace) {
			place = tmpPlace;
			tmpPlace *= 100L;
		}

		long result;
		for (result = 0L; place > 0L; place /= 100L) {
			numberInCurPlace = val / place;

			long tryResultBase = 10L * result;
			long tryResult = tryResultBase;

			while (tryResult < tryResultBase + 10L && tryResult * tryResult <= numberInCurPlace) {
				result = tryResult++;
			}
		}

		return result << 8;
	}

	// Check softkeys if pressed.
	public static final int softkeyPressed(int left, int right) {
		leftSoftkey = left;
		rightSoftkey = right;

		if ((heldKeys & 0x800) > 0 && (heldPrevKeys & 0x800) == 0) {
			return left;
		} else {
			return (heldKeys & 0x1000) > 0 && (heldPrevKeys & 0x1000) == 0 ? right : -1;
		}
	}

	// Check softkeys and the OK key as the left softkey (if okKeyEnabled = true).
	public static final int softkeyPressed(int left, int right, boolean okKeyEnabled) {
		leftSoftkey = left;
		rightSoftkey = right;

		if ((heldKeys & 0x800) > 0 && (heldPrevKeys & 0x800) == 0) {
			return left;
		} else if ((heldKeys & 0x1000) > 0 && (heldPrevKeys & 0x1000) == 0) {
			return right;
		} else {
			return okKeyEnabled && left >= 0 && (heldKeys & 1) > 0 && (heldPrevKeys & 1) == 0 ? left : -1;
		}
	}

	public static final void renderSoftkeyIcons() {
		if (imgsSoftkey != null) {
			if (leftSoftkey > -1) {
				gDrawImage(imgsSoftkey[leftSoftkey], 1, 127 - softkeyHeight[leftSoftkey], 0);
				leftSoftkey = -1;
			}

			if (rightSoftkey > -1) {
				gDrawImage(imgsSoftkey[rightSoftkey], 127 - softkeyWidth[rightSoftkey], 127 - softkeyHeight[rightSoftkey], 0);
				rightSoftkey = -1;
			}
		}
	}

	public static final void initSoftkeyIcons() {
		freeSoftkeyIcons();
		imgsSoftkey = new Image[5];
		softkeyWidth = new int[5];
		softkeyHeight = new int[5];
		leftSoftkey = -1;
		rightSoftkey = -1;
	}

	public static final void setSoftkeyIcon(int index, Image img) {
		if (imgsSoftkey != null) {
			if (index >= 0 && index < imgsSoftkey.length) {
				imgsSoftkey[index] = img;
				softkeyWidth[index] = img.getWidth();
				softkeyHeight[index] = img.getHeight();
			}
		}
	}

	public static final void freeSoftkeyIcons() {
		imgsSoftkey = null;
		softkeyWidth = null;
		softkeyHeight = null;
	}

	public static final void initFonts(int num) {
		fontCharMap = new short[num][];
		fontAverageWidths = new int[num];
		fontHeights = new int[num];
		fontSpacesPerChars = new int[num];
		fontImages = new Image[num];
		fontLineGaps = new int[num];
		fontCharOffsets = new short[num][];
		fontCharWidths = new byte[num][];
	}

	public static final void loadFont(int index, short pimCrc, short pplCrc, short cwtCrc, byte spaceWidth, short chrCrc, int spacePerChar, int lineGap) {
		fontCharMap[index] = new short[FONT_MAX_CHARS];
		fontCharWidths[index] = new byte[FONT_MAX_CHARS];

		byte[] charWidth = loadFile8(cwtCrc);
		short[] charMap = loadFile16(chrCrc);

		int numChars = charMap.length;

		fontImages[index] = loadImage(pimCrc, pplCrc);
		fontCharOffsets[index] = new short[numChars];

		for (short i = 0; i < FONT_MAX_CHARS; i++) {
			fontCharMap[index][i] = -1;
			fontCharWidths[index][i] = spaceWidth;
		}

		short curOff = 0;

		for (short i = 0; i < numChars; i++) {
			int ch = charMap[i] - 30;

			fontCharMap[index][ch] = i;
			fontCharWidths[index][ch] = charWidth[i];
			fontCharOffsets[index][i] = curOff;

			curOff = (short)(curOff + charWidth[i]);
		}

		fontHeights[index] = fontImages[index].getHeight() / 1;
		fontAverageWidths[index] = fontImages[index].getWidth() / numChars;
		fontSpacesPerChars[index] = spacePerChar;
		fontLineGaps[index] = lineGap;
	}

	public static final int getStringWidth(String string, int fontId) {
		if (fontCharWidths[fontId] == null) {
			return string.length() * (fontAverageWidths[fontId] + fontSpacesPerChars[fontId]) - fontSpacesPerChars[fontId];
		}

		int width = 0;
		int strLen = string.length();

		for (int i = 0; i < strLen; i++) {
			int ch = string.charAt(i) - 30;
			width += fontCharWidths[fontId][ch];
		}

		return width + (strLen - 1) * fontSpacesPerChars[fontId];
	}

	public static final int getAverageLengthUntilTerminator(int maxStringWidth, int fontId) {
		return (maxStringWidth + fontSpacesPerChars[fontId]) / (fontAverageWidths[fontId] + fontSpacesPerChars[fontId]);
	}

	public static final int getLengthUntilTerminator(int maxStringWidth, int fontId, String string, int startIndex) {
		if (fontCharWidths[fontId] == null) {
			return getAverageLengthUntilTerminator(maxStringWidth, fontId);
		}

		int strLen = string.length();

		int i;
		for (i = startIndex; i < strLen; i++) {
			int ch;
			if ((ch = string.charAt(i) - 30) < 0) {
				return i - startIndex + 1;
			}

			if ((maxStringWidth = maxStringWidth - fontCharWidths[fontId][ch]) < 0) {
				break;
			}

			maxStringWidth -= fontSpacesPerChars[fontId];
		}

		return i - startIndex;
	}

	// Split a string into lines.
	public static final int[] getNewLineIndices(int maxStringLength, String string, int fontId) {
		if (fontImages[fontId] == null) {
			return null;
		}

		int numNewLines = 0;
		int maxLineLength = 99999;
		int strLen = string.length();
		int curIndex = 0;

		int approxNewLines = strLen / 8;
		if (approxNewLines < 10) {
			approxNewLines = 10;
		}

		int[] approxLineIndices = new int[approxNewLines];
		boolean isNotWhitespace = false;
		boolean ignoreFirstNewLines = true;
		int newLineStreakCount = 0;

		while (curIndex < strLen) {
			switch(string.charAt(curIndex)) {
				case '\n':
					if (!ignoreFirstNewLines && newLineStreakCount > 0)
						approxLineIndices[numNewLines++] = curIndex;

					newLineStreakCount++;
					curIndex++;
					break;

				case ' ':
					curIndex++;
					break;

				default:
					newLineStreakCount = 0;
					isNotWhitespace = true;
			}

			if (!isNotWhitespace) continue;

			ignoreFirstNewLines = false;
			isNotWhitespace = false;

			int curLineLen;
			if ((curLineLen = curIndex + getLengthUntilTerminator(maxStringLength, fontId, string, curIndex)) > strLen) {
				curLineLen = strLen;
			}

			int nextSpace = -1;

			int eolIndex;
			for (eolIndex = curIndex; eolIndex < curLineLen; eolIndex++) {
				if (string.charAt(eolIndex) == '\n') {
					nextSpace = eolIndex;
					break;
				}

				if (string.charAt(eolIndex) == ' ') {
					nextSpace = eolIndex;
				}
			}

			if (eolIndex < strLen && nextSpace > 0 && string.charAt(eolIndex) != ' ') {
				eolIndex = nextSpace;
			}

			if (eolIndex >= maxLineLength) {
				eolIndex = maxLineLength;
				strLen = 0;
			}

			if (curIndex > eolIndex) {
				curIndex = eolIndex;
			}

			approxLineIndices[numNewLines++] = curIndex;
			curIndex = eolIndex;
		}

		int[] result = new int[numNewLines];

		for (int i = 0; i < numNewLines; i++) {
			result[i] = approxLineIndices[i];
		}

		return result;
	}

	public static final void gSetColor(int rgb) {
		gfx.setColor(rgb);
	}

	public static final void gSetColor(int red, int green, int blue) {
		gfx.setColor(red, green, blue);
	}

	public static final int gGetColor() {
		return gfx.getColor();
	}

	public static final void gFillRect(int x, int y, int width, int height) {
		gfx.fillRect(x, y, width, height);
	}

	public static final void gDrawRect(int x, int y, int width, int height) {
		gfx.drawRect(x, y, width, height);
	}

	public static final void gDrawImage(Image img, int x, int y, int anchor) {
		gfx.drawImage(img, x, y, anchor);
	}

	public static final void gDrawLine(int x1, int y1, int x2, int y2) {
		gfx.drawLine(x1, y1, x2, y2);
	}

	public static final void gFillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		gfx.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	public static final void gDrawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		gfx.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	public static final void gFillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		gfx.fillTriangle(x1, y1, x2, y2, x3, y3);
	}

	public static final void gSetClip(int x, int y, int width, int height) {
		gfx.setClip(x, y, width, height);
	}

	public static final int gGetClipX() {
		return gfx.getClipX();
	}

	public static final int gGetClipY() {
		return gfx.getClipY();
	}

	public static final int gGetClipWidth() {
		return gfx.getClipWidth();
	}

	public static final int gGetClipHeight() {
		return gfx.getClipHeight();
	}

	public static final void gfxFlush() {
		instance.flushGraphics();
	}

	public static final int renderTextEx(int x, int y, int width, int height, String string, int fontId, int startIndex, int endIndex, boolean centered) {
		if (fontImages[fontId] == null)
			return -2;

		int strLen = string.length();
		int index = startIndex;
		int bottom = y + height - (fontHeights[fontId] + fontLineGaps[fontId] - 1);
		boolean isNotWhitespace = false;
		boolean ignoreFirstNewLines = true;
		int newLineStreakCount = 0;

		while (index < strLen && y < bottom) {
			switch(string.charAt(index)) {
				case '\n':
					if (!ignoreFirstNewLines && newLineStreakCount > 0 && (y += fontHeights[fontId] + fontLineGaps[fontId]) > bottom) {
						return index;
					}

					newLineStreakCount++;
					index++;
					break;

				case ' ':
					index++;
					break;

				default:
					newLineStreakCount = 0;
					isNotWhitespace = true;
			}

			if (!isNotWhitespace) continue;

			ignoreFirstNewLines = false;
			isNotWhitespace = false;

			int curLineLen;
			if ((curLineLen = index + getLengthUntilTerminator(width, fontId, string, index)) > strLen) {
				curLineLen = strLen;
			}

			int nextSpace = -1;

			int eolIndex;
			for (eolIndex = index; eolIndex < curLineLen; eolIndex++) {
				if (string.charAt(eolIndex) == '\n') {
					nextSpace = eolIndex;
					break;
				}

				if (string.charAt(eolIndex) == ' ') {
					nextSpace = eolIndex;
				}
			}

			if (eolIndex < strLen && nextSpace > 0 && string.charAt(eolIndex) != ' ') {
				eolIndex = nextSpace;
			}

			if (eolIndex >= endIndex) {
				eolIndex = endIndex;
				strLen = 0;
			}

			if (index > eolIndex) {
				index = eolIndex;
			}

			String currentLine = string.substring(index, eolIndex);
			if (centered) {
				renderText(x + (width - getStringWidth(currentLine, fontId)) / 2, y, currentLine, fontId);
			} else {
				renderText(x, y, currentLine, fontId);
			}

			y += fontHeights[fontId] + fontLineGaps[fontId];
			index = eolIndex;
		}

		return index < strLen && string.charAt(index) != ' ' ? index : index + 1;
	}

	public static final void renderText(int x, int y, String string, int fontId) {
		if (fontImages[fontId] == null)
			return;

		int clipHeight = gGetClipHeight();
		int clipWidth = gGetClipWidth();
		int clipX = gGetClipX();
		int clipY = gGetClipY();

		if (y + fontHeights[fontId] < clipY ||
				clipY + clipHeight < y)
			return;

		int strLen = string.length();

		// Center of the screen
		if (x == -1000) {
			x = (128 - getStringWidth(string, fontId)) / 2;
		}

		int textY = y;
		int fontHeight = fontHeights[fontId];

		if (clipY > y) {
			fontHeight -= clipY - y;
			textY = clipY;
		}

		if (y + fontHeight > clipY + clipHeight) {
			fontHeight = clipY + clipHeight - textY;
		}

		int curX = x;

		for (int i = 0; i < strLen; i++) {

			int ch = string.charAt(i) - 30;
			if (curX > 128 || ch < 0)
				continue;

			short charIndex;
			if ((charIndex = fontCharMap[fontId][ch]) == -1) {

				curX += fontCharWidths[fontId] != null ?
					fontCharWidths[fontId][0] + fontSpacesPerChars[fontId] :
					fontAverageWidths[fontId] + fontSpacesPerChars[fontId];

			} else {

				int charX = curX;
				int charWidth = fontCharWidths[fontId] != null ?
					fontCharWidths[fontId][ch] :
					fontAverageWidths[fontId];

				if (curX + charWidth < 0) {

					curX += fontCharWidths[fontId] != null ?
						fontCharWidths[fontId][ch] + fontSpacesPerChars[fontId] :
						fontAverageWidths[fontId] + fontSpacesPerChars[fontId];

				} else {

					if (curX < clipX || curX + charWidth > clipX + clipWidth) {
						if (curX + charWidth < clipX) continue;
						if (curX > clipX + clipWidth) break;

						if (curX < clipX) {
							charWidth -= clipX - curX;
							charX = clipX;
						}

						if (charX + charWidth > clipX + clipWidth) {
							charWidth = clipX + clipWidth - charX;
						}
					}

					gSetClip(charX, textY, charWidth, fontHeight);

					if (fontCharWidths[fontId] != null) {
						gDrawImage(fontImages[fontId], curX - fontCharOffsets[fontId][charIndex], y, 0);
						curX += fontCharWidths[fontId][ch] + fontSpacesPerChars[fontId];
					} else {
						gDrawImage(fontImages[fontId], curX - fontAverageWidths[fontId] * charIndex, y, 0);
						curX += fontAverageWidths[fontId] + fontSpacesPerChars[fontId];
					}
				}
			}
		}

		gSetClip(clipX, clipY, clipWidth, clipHeight);
	}

	public static final void setRandSeed(int seed) {
		randSeed = (long)seed;
	}

	public static final int rand8() {
		randSeed = randSeed * 0x5deece66dL + 11L & 0xffffffffffffL;
		return (int)(randSeed >>> 40);
	}

	public static final int rand16() {
		randSeed = randSeed * 0x5deece66dL + 11L & 0xffffffffffffL;
		return (int)(randSeed >>> 32);
	}

	public static final void start() {
		loadGeneral();

		do {
			if (isPaused) {
				pauseScreenOnce();
				renderSoftkeyIcons();
				gfxFlush();
				updateKeys();
				if (isKeyReleased(1)) {
					isPaused = false;
				}
			} else if (isNewState) {
				isNewState = false;
				execState(curState, 2);
				oldState = curState;
				curState = newStateIndex;
				execState(curState, 0);
			} else {
				execState(curState, 1);
			}
		} while (curState != -1);

		queueAllSoundsForCleanup();
		queueSoundCleanup();
	}

	public static final void setNewState(int state, int arg) {
		newStateIndex = state;
		stateArg = arg;
		isNewState = true;
	}

	// res: 0 - init, 1 - run, 2 - free
	public static final void execState(int state, int res) {
		if (state >= 0) {
			switch(state) {

				case 0:
					switch(res) {
						case 0:
							sceneSelectionInit(stateArg);
							break;
						case 1:
							sceneSelectionRun();
							break;
						case 2:
							sceneSelectionFree();
					}
					return;

				case 1:
					switch(res) {
						case 0:
							sceneSplashInit(stateArg);
							break;
						case 1:
							sceneSplashRun();
							break;
						case 2:
							sceneSplashFree();
					}
					return;

				case 2:
					switch(res) {
						case 0:
							sceneShipInit(stateArg);
							break;
						case 1:
							sceneShipRun();
							break;
						case 2:
							sceneShipFree();
					}
					return;

				case 3:
					switch(res) {
						case 0:
							scenePreLevelInit(stateArg);
							break;
						case 1:
							scenePreLevelRun();
							break;
						case 2:
							scenePreLevelFree();
					}
					return;

				case 4:
					switch(res) {
						case 0:
							sceneLevelInit(stateArg);
							break;
						case 1:
							sceneLevelRun();
							break;
						case 2:
							sceneLevelFree();
					}
					return;

				case 5:
					switch(res) {
						case 0:
							sceneDialogueInit(stateArg);
							break;
						case 1:
							sceneDialogueRun();
							break;
						case 2:
							sceneDialogueFree();
					}
					return;

				case 6:
					switch(res) {
						case 0:
							sceneTransitionInit(stateArg);
							break;
						case 1:
							sceneTransitionRun();
							break;
						case 2:
							sceneTransitionFree();
					}
					return;

				case 7:
					switch(res) {
						case 0:
							sceneMainMenuInit(stateArg);
							break;
						case 1:
							sceneMainMenuRun();
							break;
						case 2:
							sceneMainMenuFree();
					}
					return;

				case 8:
					switch(res) {
						case 0:
							sceneHelpInit(stateArg);
							return;
						case 1:
							sceneHelpRun();
							return;
						case 2:
							sceneHelpFree();
					}

				default:
			}
		}
	}

	public static final void setDisplay(Display dpy) {
		Game.dpy = dpy;
	}

	public static final void vibrate(int duration) {
		if (isVibraEnabled) {
			dpy.vibrate(duration);
		}
	}

	public static final long millis() {
		return System.currentTimeMillis();
	}

	public static final void initConfetti() {
		confettiColors = new int[40];
		confettiHeights = new int[40];
		confettiAngles = new int[40];
		confettiX = new int[40];
		confettiY = new int[40];
		confettiWidths = new int[40];
	}

	public static final void renderConfetti() {
		for (int i = 0; i < 40; i++) {
			if (confettiColors[i] == 0 || confettiY[i] > 128) {
				confettiColors[i] = confettiColorsAll[rand8() % confettiColorsAll.length];
				confettiHeights[i] = rand8() % 40;
				confettiX[i] = rand16() % 128;
				confettiY[i] = -rand16() % 128;
				confettiWidths[i] = 8 + rand8() % 7;
				confettiAngles[i] = rand8();
			}

			confettiY[i] += (1 + rand8() % 5) * 128 / 176;
			confettiX[i] += cos1000[confettiAngles[i]] * 3 / 1000;
			confettiHeights[i] += 4 + rand8() % 5;
			if (confettiHeights[i] >= 40) {
				confettiHeights[i] = 0;
				confettiColors[i] = confettiColorsAll[rand8() % confettiColorsAll.length];
			}

			confettiAngles[i] += 3 + i % 2;
			if (confettiAngles[i] >= 360) {
				confettiAngles[i] -= 360;
			}

			int confettiHeight = confettiHeights[i] / 4;
			if (confettiHeights[i] >= 20) {
				confettiHeight = (40 - confettiHeights[i]) / 4;
			}

			gSetColor(confettiColors[i]);
			gFillRect(confettiX[i], confettiY[i], confettiWidths[i] * 128 / 176, confettiHeight * 128 / 176);
		}
	}

	public static final void renderCritterShock(int x1, int y1, int x2, int y2, int numPoints, int shockDisplacement) {
		int rateX = (x2 - x1) / 7;
		int rateY = (y2 - y1) / 7;

		for (int i = 0; i < 2; i++) {
			int shX2 = x1;
			int shY2 = y1;
			int shockColor = 111 + rand8() % 140;

			gSetColor(shockColor, shockColor, i == 0 ? shockColor : 255);

			for (int j = 1; j < numPoints; j++) {
				int shX1 = shX2;
				int shY1 = shY2;
				shX2 = x1 + j * rateX + rand8() % shockDisplacement - shockDisplacement / 2;
				shY2 = y1 + j * rateY + rand8() % shockDisplacement - shockDisplacement / 2;

				if (j == 7) {
					shX2 = x2;
					shY2 = y2;
				}

				gDrawLine(shX1, shY1, shX2, shY2);
			}
		}
	}

	public static final void renderRain() {
		if (rainX == null) {
			rainX = new int[8];
			rainY = new int[8];
		}

		gSetColor(0xc8c8c8);

		for (int i = 0; i < 8; i++) {
			if (rainY[i] > 128 || rainX[i] == 0 && rainY[i] == 0) {
				rainX[i] = rand16() % 149;
				rainY[i] = rand16() % 21;
			}

			gDrawLine(rainX[i], rainY[i], rainX[i] + -3, rainY[i] + 10);
			rainX[i] += -9;
			rainY[i] += 30;
		}
	}

	public static final void levelRenderPrecipitation() {
		if (precipitationX == null) {
			precipitationX = new int[10];
			precipitationY = new int[10];
			precipitationDirectionCos = new int[10];
		}

		gSetColor(0xffffff);
		if (levelBackgroundId == 0) {
			gSetColor(0);
		}

		for (int i = 0; i < 10; i++) {
			if (precipitationY[i] > 128 || precipitationX[i] == 0 && precipitationY[i] == 0) {
				if (precipitationX[i] == 0 && precipitationY[i] == 0) {
					precipitationY[i] = rand16() % 128;
				} else {
					precipitationY[i] = -(rand16() % 10);
				}

				precipitationX[i] = rand16() % 128;
				precipitationDirectionCos[i] = rand16() % 360;
			}

			gDrawLine(precipitationX[i] - 1, precipitationY[i], precipitationX[i] + 1, precipitationY[i]);
			gDrawLine(precipitationX[i], precipitationY[i] - 1, precipitationX[i], precipitationY[i] + 1);

			precipitationX[i] += cos1000[precipitationDirectionCos[i]] * 3 / 1000;
			precipitationY[i] += 2;
			precipitationDirectionCos[i] += 5;
			if (precipitationDirectionCos[i] >= 360) {
				precipitationDirectionCos[i] -= 360;
			}
		}
	}

	public static final void initSpace(int numSpaces, int maxStars, int width, int height) {
		if (currentSpaceWidth != width ||
				currentSpaceHeight != height ||
				spaceStarX == null ||
				spaceStarX.length != numSpaces ||
				spaceStarX[0].length != maxStars) {

			currentSpaceWidth = width;
			currentSpaceHeight = height;
			spaceStarX = new int[numSpaces][];
			spaceStarY = new int[numSpaces][];
			spaceStarXVel = new int[numSpaces][];
			spaceStarFlashTicks = new int[numSpaces][];

			for (int i = 0; i < numSpaces; i++) {
				spaceStarX[i] = new int[maxStars];
				spaceStarY[i] = new int[maxStars];
				spaceStarXVel[i] = new int[maxStars];
				spaceStarFlashTicks[i] = new int[maxStars];

				for (int j = 0; j < maxStars; j++) {
					spaceStarX[i][j] = rand16() % width << 4;
					spaceStarY[i][j] = rand16() % height << 4;
					spaceStarXVel[i][j] = -1 - rand8() % 32;
					spaceStarFlashTicks[i][j] = 0;
				}
			}
		}
	}

	public static final void renderSpace(int spaceId, int x, int y) {
		renderSpace(spaceId, x, y, true);
	}

	public static final void renderSpace(int spaceId, int x, int y, boolean update) {
		if (spaceId >= 0 && spaceId < spaceStarX.length) {
			spaceStarFlashTicks[spaceId][rand16() % spaceStarFlashTicks[spaceId].length] = 9;

			for (int i = 0; i < spaceStarX[spaceId].length; i++) {

				/* The brightness of the star depends on its velocity (or how I
				 * call it, "Z axis"). */
				int starR = 30 - 7 * spaceStarXVel[spaceId][i];
				int starG = starR;
				int starB = starR;

				if (i % 3 == 1) {
					starG = 0;
					starR = 0;
				}

				if (i % 3 == 2) {
					starB = 0;
					starG = 0;
				}

				gSetColor(starR, starG, starB);

				if (spaceStarFlashTicks[spaceId][i] > 0) {
					if (--spaceStarFlashTicks[spaceId][i] > 4) {
						gSetColor(0);
					} else {
						gSetColor(0xffffff);
					}
				}

				renderStar(x + (spaceStarX[spaceId][i] >> 4), y + (spaceStarY[spaceId][i] >> 4));

				if (update) {
					spaceStarX[spaceId][i] += spaceStarXVel[spaceId][i];
					if (spaceStarX[spaceId][i] < 0) {
						spaceStarX[spaceId][i] = currentSpaceWidth - 1 << 4;
						spaceStarY[spaceId][i] = rand16() % currentSpaceHeight << 4;
						spaceStarXVel[spaceId][i] = -4 - rand8() % 28;
					}
				}
			}
		}
	}

	public static final void renderStar(int x, int y) {
		gDrawLine(x, y, x, y);
		int col = gGetColor();
		// Divide each RGB channel by 2.
		gSetColor((col & 0xff0000) >> 17, (col & 0x00ff00) >> 9, (col & 0x0000ff) >> 1);
		gDrawLine(x, y - 1, x, y - 1);
		gDrawLine(x, y + 1, x, y + 1);
		gDrawLine(x - 1, y, x - 1, y);
		gDrawLine(x + 1, y, x + 1, y);
	}

	public static final void sceneHelpRun() {
		renderHelp();

		// OK softkey.
		if (softkeyPressed(2, -1) == 2) {
			setNewState(2, 0);
		}

		// Down key.
		if (isKeyHeld(0x40)) {
			sceneHelpScrollAccel -= (400 + sceneHelpScrollAccel) / 6;
			sceneHelpAutoscrollActive = false;
		}

		// Up key.
		if (isKeyHeld(0x20)) {
			sceneHelpScrollAccel += (400 - sceneHelpScrollAccel) / 6;
			sceneHelpAutoscrollActive = false;
		}

		sceneHelpScrollAccel = sceneHelpScrollAccel * 95 / 100;
		if (sceneHelpAutoscrollActive) {
			sceneHelpScrollAccel = -0xff;
		}

		sceneHelpScrollDistance += sceneHelpScrollAccel;
		if (sceneHelpScrollDistance > 0x8000) {
			sceneHelpScrollDistance = 0x8000;
		}

		if (sceneHelpScrollDistance < -(sceneHelpText.length * 14 << 8)) {
			sceneHelpScrollDistance = -(sceneHelpText.length * 14 << 8);
		}

		sceneHelpScroll++;

		refreshGame();
	}

	public static final void renderHelp() {
		gSetColor(0);
		gFillRect(0, 0, 128, 128);

		renderSpace(0, 0, 0, true);

		int scrollDistance = sceneHelpScrollDistance >> 8;

		for (int i = 0; i < sceneHelpText.length; i++) {
			if (scrollDistance > -14 && scrollDistance < 128) {
				int textOffsetX = 0;
				if (scrollDistance < 42) {
					textOffsetX = 90 - scrollDistance * 90 * 3 / 128;
				} else if (scrollDistance > 85) {
					textOffsetX = 90 - (128 - scrollDistance) * 90 * 3 / 128;
				}

				int textX = 5 + sin(textOffsetX) * 128 / 2600;
				renderText(textX, scrollDistance, sceneHelpText[i], 2);
			}

			scrollDistance += 14;
		}

		gDrawImage(imgsScrollbar[0], 120, 43 - (sceneHelpScroll >> 2 & 3), 0);
		gDrawImage(imgsScrollbar[1], 118, 46 - ((sceneHelpScrollDistance >> 8) - 128) * 39 / (sceneHelpText.length * 14), 0);
		gDrawImage(imgsScrollbar[2], 120, 92 + (sceneHelpScroll >> 2 & 3), 0);
	}

	public static final void sceneHelpInit(int arg) {
		if (imgsScrollbar == null) {
			imgsScrollbar = new Image[3];

			for (int i = 0; i < 3; i++) {
				imgsScrollbar[i] = loadImage("scrollBar" + i + ".pim", "scrollBar" + i + ".ppl");
			}
		}

		initSpace(1, 50, 128, 128);

		if (sceneHelpText == null) {
			sceneHelpText = strSplitLines(getText(0x30000), 2);
		}

		sceneHelpScrollDistance = 0x8000;
		sceneHelpScrollAccel = 0;
		sceneHelpAutoscrollActive = true;
		sceneHelpScroll = 0;

		playSound(-1);
	}

	public static final void sceneHelpFree() {
		playSound(-1);
		garbageCollector();
	}

	public static final void scenePreLevelRun() {
		gSetColor(0);
		gFillRect(0, 0, 128, 128);

		renderSpace(0, 0, 0, false);

		// A distant ship.
		if (scenePreLevelCurTick > 65) {
			gDrawImage(imgShipSmall, scenePreLevelSmallShipX, scenePreLevelSmallShipY, 0);
			scenePreLevelSmallShipX -= 2;
		}

		// A planet.
		gDrawImage(imgCurrentPlanet, 0, 128 - imgCurrentPlanet.getHeight(), 0);

		// A closer ship.
		if (scenePreLevelShipX < 128) {
			gDrawImage(imgOutside, scenePreLevelShipX, scenePreLevelShipY, 0);
			gDrawImage(imgsFlame[(int)(millis() / 100L % 2L)], scenePreLevelShipX + 7 - 10, scenePreLevelShipY + 16 - 3, 0);
			scenePreLevelShipX += 5;
		}

		// Render a planet name.
		if (scenePreLevelCurTick > 50) {
			renderText(-1000, 85, currentPlanetText, 0);
		}

		// Render "press 5" string.
		if (scenePreLevelCurTick > 120 && (millis() & 512L) > 0L) {
			renderText(-1000, 114, titleText, 0);
		}

		// Enter the level if skipped or pressed OK.
		if (scenePreLevelCurTick < 120 && softkeyPressed(-1, 3) == 3 ||
				scenePreLevelCurTick > 120 && softkeyPressed(2, -1, true) == 2) {

			setNewState(4, 0);
		}

		scenePreLevelCurTick++;

		refreshGame();
	}

	public static final void scenePreLevelInit(int arg) {
		playSound(-1);

		// Preinitialize a level.
		activateLevel(level);

		currentPlanetText = loadTextTableFromIndex(86, -1)[level];

		String filename = "planet" + level % 2 + "_" + levelBackgroundId % 5;
		imgCurrentPlanet = loadImage(filename + ".pim", filename + ".ppl");
		imgOutside = loadImage("outside.pim", "outside.ppl");
		imgShipSmall = loadImage("shipSmall.pim", "shipSmall.ppl");

		// Ship positions.
		scenePreLevelSmallShipX = 128;
		scenePreLevelSmallShipY = 128 - imgCurrentPlanet.getHeight() * 4 / 5;
		if (scenePreLevelSmallShipY < 32) {
			scenePreLevelSmallShipY = 32;
		}
		scenePreLevelShipX = -61;
		scenePreLevelShipY = 70;

		// Flame sprites.
		if (imgsFlame == null) {
			imgsFlame = new Image[2];
			imgsFlame[0] = loadImage("flame0.pim", "flame0.ppl");
			imgsFlame[1] = loadImage("flame1.pim", "flame1.ppl");
		}

		// "press 5" string.
		if (titleText == null) {
			titleText = getText(0x1000f);
		}

		initSpace(1, 50, 128, 128);

		playJingleFlyIn();

		scenePreLevelCurTick = 0;
	}

	public static final void scenePreLevelFree() {
		playSound(-1);

		clearKeys();

		imgCurrentPlanet = null;
		imgOutside = null;
		imgShipSmall = null;

		garbageCollector();
	}

	public static final void sceneLevelRun() {
		switch(levelCompletionState) {
			// Update the level constantly.
			case 0:
				sceneLevelUpdate();
				if (isLevelComplete) {
					startDialogue("outro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					levelCompletionState = 1;
				}
				break;

			// Level completed.
			case 1:
				initVictory();
				levelCompletionState = 2;
			case 2:
				if (renderMissionComplete()) {
					levelCompletionState = 3;
				}
				break;

			// Return to the ship.
			case 3:
				setNewState(2, 10);
		}

		if (mustExitPlayground) {
			setNewState(2, 0);
		}
	}

	public static final void sceneLevelInit(int arg) {
		isShipPaused = false;

		/* 'arg' represents the choice of whether to load resources (possibly
		 * reloading them) or to skip that. */
		if (oldState != 5 && arg != 1) {
			isLoadingBarShown = true;
			levelCompletionState = 0;

			loadFaces();
			initExplosives();
			initEnemies();
			initSigns();
			initGrabber();

			mustExitPlayground = false;
			isLevelComplete = false;
		}

		gamma = 100;

		/* Do not bother audio engine if old scene was a dialogue or a
		 * transition. */
		if (oldState != 5 && oldState != 6) {
			playCurrentBackgroundMusic();
		}
	}

	public static final void sceneLevelFree() {
		if (isLevelComplete) {
			level++;
			saveRecordData();
			isLevelComplete = false;
		}

		/* Do not bother audio engine if old scene was a dialogue or a
		 * transition. */
		if (newStateIndex != 5 && newStateIndex != 6) {
			playSound(-1);
			garbageCollector();
		}
	}

	public static final void sceneSplashRun() {
		gSetColor(0xffffff);
		gFillRect(0, 0, 128, 128);
		switch(splash) {

			// Eidos image.
			case 0:
				gDrawImage(imgCenterLogo, (128 - imgCenterLogo.getWidth()) / 2, (128 - imgCenterLogo.getHeight()) / 2, 0);
				break;

			// Eidos legal line.
			case 1:
				gDrawImage(imgEidosLegalLine, (128 - imgEidosLegalLine.getWidth()) / 2, 128 - imgEidosLegalLine.getHeight() - 4, 0);
				break;

			// PM RocketFuel image.
			case 2:
				gDrawImage(imgCenterLogo, splashLogoX - 27, splashLogoY - 1, 0);
				gDrawImage(imgPmRocket, splashLogoX, splashLogoY, 0);
				break;

			// PM RocketFuel animation.
			case 3:
				int x = splashLogoX;
				int y = splashLogoY;
				if (splashTick < 20) {
					x = splashLogoX + rand8() % 5 - 2;
					y = splashLogoY + rand8() % 5 - 2;
					gDrawImage(imgCenterLogo, x - 27, y - 1, 0);
				} else {
					splashLogoX += (splashTick - 20) / 8;
					splashLogoY -= (splashTick - 20) / 8;
					gDrawImage(imgCenterLogo, 15, 19, 0);
				}

				gDrawImage(imgPmRocket, x, y, 0);
				if (x > 158) {
					splashDuration = 0L;
				} else {
					splashDuration = millis() + 2000L;
				}

				splashTick++;
				break;

			// Maniacs of Noice scaling up.
			case 4:
				int[] elemColors = new int[] {0, 0xffffff, 0, 0xffffff, 0x3aaec7};
				int[] elemRadius = new int[] {54, 54, 37, 37, 18};
				int[] elemX = new int[] {0, -10, -5, 2, 0};

				for (int i = 0; i < 5; i++) {
					gSetColor(elemColors[i]);

					int scaleX = sin1000[splashTick * 2] * elemX[i] / 1000;
					int scaleRadius = sin1000[splashTick * 2] * elemRadius[i] / 1000;

					if (i != 2 && i != 3)
						gFillArc(64 + scaleX - scaleRadius / 2 - 1, 64 - scaleRadius / 2, scaleRadius, scaleRadius, 0, 360);
					else
						gFillArc(64 + scaleX - scaleRadius / 2 - 1, 64 - scaleRadius / 2, scaleRadius, scaleRadius, 80, 200);
				}

				gDrawImage(imgPmRocket, splashLogoX, splashLogoY, 0);

				splashLogoX += 4;
				splashLogoY -= 4;

				if (splashTick > 45)
					splashDuration = 0L;

				splashTick++;
				break;

			// Maniacs of Noise image.
			case 5:
				gDrawImage(imgCenterLogo, 19, 18, 0);
		}

		// Show a next splash.
		if (millis() > splashDuration || isKeyReleased(1)) {
			setNewState(1, splash + 1);
		}

		refreshGame();
	}

	public static final void sceneSplashInit(int arg) {
		splash = arg;

		switch(arg) {

			// Eidos image.
			case 0:
				imgCenterLogo = loadImage("eidos.pim", "eidos.ppl");
				splashDuration = millis() + 2500L;
				return;

			// Eidos legal line.
			case 1:
				imgEidosLegalLine = loadImage("eidos_legal_line.pim", "eidos_legal_line.ppl");
				splashDuration = millis() + 2500L;
				return;

			// PM RocketFuel image.
			case 2:

			// PM RocketFuel animation.
			case 3:
				if (imgCenterLogo == null)
					imgCenterLogo = loadImage("pmback.pim", "pmback.ppl");

				if (imgPmRocket == null)
					imgPmRocket = loadImage("pmrocket.pim", "pmrocket.ppl");

				splashLogoX = 42;
				splashLogoY = 20;
				splashTick = 0;
				splashDuration = millis() + 2000L;
				return;

			// Maniacs of Noice scaling up.
			case 4:
				splashLogoX = -49;
				splashLogoY = 128;
				splashTick = 0;
				splashDuration = millis() + 10000L;
				return;

			// Maniacs of Noise image.
			case 5:
				imgCenterLogo = loadImage("mon.pim", "mon.ppl");
				splashDuration = millis() + 2000L;
				return;

			// Enter the main menu.
			default:
				setNewState(7, 0);
				isLoadingBarShown = true;
		}
	}

	public static final void sceneSplashFree() {
		switch(splash) {

			// Eidos image.
			case 0:
				imgCenterLogo = null;

			// Eidos legal line.
			case 1:
				imgEidosLegalLine = null;

			// PM RocketFuel image.
			case 2:
				break;

			// PM RocketFuel animation.
			case 3:
				imgCenterLogo = null;
				break;

			// Maniacs of Noice scaling up.
			case 4:
				imgPmRocket = null;
				break;

			// Maniacs of Noise image.
			case 5:
				imgCenterLogo = null;
				break;

			// Enter the main menu.
			default:
				imgCenterLogo = null;
				imgEidosLegalLine = null;
				imgPmRocket = null;
		}

		garbageCollector();
	}

	public static final void sceneShipRun() {
		if (isBeamAnimated) {
			renderBeamAnimation(beamAnimationFrame);
			beamAnimationFrame += 4;
			if (beamAnimationFrame >= 130) {
				isBeamAnimated = false;
			}
		} else if (dialogueIsAwaitingLevelStart) {
			dialogueIsAwaitingLevelStart = false;
			startDialogue("intro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
		} else if (shipNextOp > 0) {
			switch(shipNextOp) {
				case 1:
					setNewState(3, 0);
					break;
				case 2:
					if (dialogueIsActionConfirmed) {
						setNewState(-1, 0);
					}
					break;
				case 3:
					if (dialogueIsActionConfirmed) {
						nextLevelToLoad = currentLevelLoaded;
						setNewState(4, 0);
						swapLevelData(1);
					}
					break;
				case 4:
					activateLevel(-1);
					levelPlayerHealth = 500;
					levelIntroTicks = 120;
					setNewState(4, 0);
					break;
				case 5:
					if (dialogueIsActionConfirmed) {
						level = 0;
						isMirrored = false;
						saveRecordData();
						isShipPaused = false;
						activeSwapKey = -1;
						setNewState(7, 0);
						return;
					}
					break;
				case 6:
					setNewState(4, 0);
					swapLevelData(1);
			}

			shipNextOp = 0;
		} else {
			if (levelIsPlayerOnSurface && levelCircleY[0] > 0x960000 && levelShipSelectedPodIndex > 0) {
				shipNextOp = processMainMenuDialogue(levelShipSelectedPodIndex, isShipPaused);
				levelShipSelectedPodIndex = 0;
			}

			if (!isBeamAnimated) {
				updateLevel();
				updatePlayerControls();
			}

			if (levelShipTouchedCircleType > 0) {
				if (levelShipTouchedCircleType == levelShipCheckPodIndex) {
					levelShipTouchedCircleType = 0;
				} else {
					levelShipCheckPodIndex = levelShipTouchedCircleType;
				}
			}

			if (levelIsPlayerOnSurface) {
				levelShipCheckPodIndex = 0;
			}

			if (levelShipTouchedCircleType > 0) {
				levelShipSelectedPodIndex = levelShipTouchedCircleType;
			}

			levelShipTouchedCircleType = 0;
			levelSetCamera(levelCircleX[0], levelHeight / 2, 0);
			levelSetPlayerPos(levelCircleX[6], levelCircleY[6], 6, 0x140000);
			renderShipBlobs(isShipPaused);
			refreshGame();
		}
	}

	public static final void sceneShipInit(int var0) {
		loadRecordData();
		if (level >= 25) {
			isMirrored = !isMirrored;
			level = 0;
			saveRecordData();
			setNewState(7, 0);
		} else {
			if (activeSwapKey != 0) {
				initShip();
			}

			if (showControlsGuide) {
				showControlsGuide = false;
				startDialogue("intro0controls.bms", levelCircleX[0], levelCircleY[0]);
			}

			dialogueIsAwaitingLevelStart = false;
			if (var0 == 10) {
				if (isBeamAnimated) {
					dialogueIsAwaitingLevelStart = true;
					beamAnimationFrame = 1;
					levelSetCamera(levelCircleX[6], levelHeight / 2, 0);
				} else if (skipPrologue) {
					skipPrologue = false;
					startDialogue("continueStory.bms", levelCircleX[0], levelCircleY[0]);
					dialogueIsAwaitingLevelStart = true;
				} else {
					startDialogue("intro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					if (level == 0) {
						showControlsGuide = true;
					}
				}
			}

			levelShipTouchedCircleType = 0;
			isPlayerAlive = true;
			initSpace(2, 12, 92, 46);
			initInsideShip();
			loadFaces();
			if (textTableShip == null) {
				textTableShip = loadTextTableFromIndex(6, -1);
				textTableShipPause = loadTextTableFromIndex(7, -1);
			}

			if (!isShipPaused) {
				playSound(2);
			}
		}
	}

	public static final void sceneShipFree() {
		if (newStateIndex != 5) {
			playSound(-1);
			garbageCollector();
		}
	}

	public static final void sceneDialogueRun() {
		long var0 = millis();
		if (!dialogueIsWaiting) {
			currentDialogueIndex = executeDialogueScript(currentDialogue, currentDialogueIndex, false);
		} else {
			if (!isSpeakingAnimationPlaying && !dialogueIsOkKeyEnabled && dialogueWaitEndMs == 0L) {
				if (dialogueEnvironment == 3) {
					if (renderSpaceMapNextFrame()) {
						dialogueIsWaiting = false;
					}
				} else if (!dialogueIsCameraMoving || levelCameraApproach(dialogueCameraTargetX, dialogueCameraTargetY, 0, 0x320000)) {
					dialogueIsWaiting = false;
					dialogueIsCameraMoving = false;
				}
			}

			if (dialogueWaitEndMs != 0L && dialogueWaitEndMs < var0) {
				dialogueWaitEndMs = 0L;
			}

			if (dialogueIsOkKeyEnabled && isKeyPressed(1)) {
				dialogueIsOkKeyEnabled = false;
			}

			if (isSpeakingAnimationPlaying && currentSpeechDone) {
				isSpeakingAnimationPlaying = false;
			}
		}

		switch(dialogueEnvironment) {
			case 0:
				renderShipInside();
				if (dialogueIsWazActive && !isShipPaused) {
					renderPlayable(6, 0xc80000, true);
				}

				renderPlayable(0, 0xff, true);
				renderShipDecor(decorForeground);
				break;
			case 1:
				if (levelCameraZoom != 72) {
					dialogueCameraZoomApproach += (72000 - dialogueCameraZoomApproach) / 10;
					levelCameraZoom = dialogueCameraZoomApproach / 1000;
				}

				if (levelPlayerAngle < 180) {
					levelPlayerAngle = levelPlayerAngle * 90 / 100;
				} else {
					levelPlayerAngle -= 360;
					levelPlayerAngle = levelPlayerAngle * 90 / 100;
					levelPlayerAngle += 360;
				}

				levelRender(true);
			case 2:
			default:
				break;
			case 3:
				renderSpaceMap();
				break;
			case 4:
				renderSplash();
				break;
			case 5:
				renderTransmission();
		}

		if (showBlackBars) {
			boolean var2 = false;
			gSetColor(0);
			gFillRect(0, 0, 128, 12);
			gFillRect(0, 116, 128, 17);
		}

		renderDialogueBox();
		if (dialogueSelectionEnabled) {
			int var3 = softkeyPressed(2, 4);
			if (var3 >= 0) {
				for (dialogueIsActionConfirmed = var3 == 2; currentDialogueIndex < currentDialogue.length; currentDialogueIndex = executeDialogueScript(currentDialogue, currentDialogueIndex, true)) {
				}

				dialogueIsWaiting = false;
			}
		} else if (dialogueIsOkKeyEnabled && softkeyPressed(2, 3) == 3 || !dialogueIsOkKeyEnabled && softkeyPressed(-1, 3) == 3) {
			while (currentDialogueIndex < currentDialogue.length) {
				currentDialogueIndex = executeDialogueScript(currentDialogue, currentDialogueIndex, true);
			}

			dialogueIsWaiting = false;
			setNewState(6, 100 + stateBeforeDialogue);
			return;
		}

		refreshGame();
		if (currentDialogueIndex >= currentDialogue.length && !dialogueIsWaiting) {
			setNewState(stateBeforeDialogue, 0);
		}
	}

	public static final void sceneDialogueInit(int var0) {
		dialogueCameraZoomApproach = levelCameraZoom * 1000;
		if (currentDialogue == null) {
			setNewState(stateBeforeDialogue, 0);
		} else if (!dialogueIsContinued) {
			currentDialogueIndex = 0;
			dialogueIsActionConfirmed = false;
			stateBeforeDialogue = oldState;
			activePurpleShardNameId = 0;
			dialogueLastSwapKey = activeSwapKey;
			dialogueIsWazActive = dialogueLastSwapKey == 0;
			if (dialogueIsWazActive) {
				dialogueEnvironment = 0;
			} else {
				dialogueEnvironment = 1;
			}

			resetDialogue();
			loadEyes();
			loadFacesIcons();
			dialogueIsWaiting = false;
			dialogueWaitEndMs = 0L;
			dialogueIsCameraMoving = false;
			isSpeakingAnimationPlaying = false;
			dialogueIsOkKeyEnabled = false;
		} else {
			dialogueIsContinued = false;
		}
	}

	public static final void sceneDialogueFree() {
		if (!dialogueIsContinued || currentDialogueIndex >= currentDialogue.length) {
			dialogueIsContinued = false;
			currentDialogue = null;
			garbageCollector();
			swapLevelData(dialogueLastSwapKey);
			resetDialogue();
		}
	}

	public static final void resetDialogue() {
		showBlackBars = false;
		shipAlarmRadius = 0;
		spaceMapPlanetBeaconRadius = 0;
		currentEmotion[0] = 0;
		currentEmotion[1] = 0;
	}

	public static final void startDialogue(String var0, int var1, int var2) {
		startDialogue(var0, var1, var2, false);
	}

	public static final void startDialogue(String var0, int var1, int var2, boolean var3) {
		isLoadingBarShown = false;
		int[] var4 = loadFile32(var0);
		isLoadingBarShown = true;
		if (var4 != null) {
			startDialogue(var4, var1, var2, var3);
		}
	}

	public static final void levelDisplayDialogue(int var0) {
		if (levelPlayerHealth > 0) {
			int var1 = levelCircleType[var0] - 10;
			startDialogue("ingame" + level + "-" + var1 + ".bms", levelCircleX[var0], levelCircleY[var0], false);
			levelCircleFlags[var0] = 0;
		}
	}

	public static final void startDialogue(int[] var0, int var1, int var2, boolean var3) {
		dialogueOriginX = var1;
		dialogueOriginY = var2;
		dialogueSelectionEnabled = var3;
		currentDialogue = var0;
		hideDialogueBox();
		setNewState(5, 0);
	}

	public static final int executeDialogueScript(int[] var0, int var1, boolean var2) {
		while (var1 < var0.length) {
			switch(var0[var1]) {
				case 0:
					return var1 + 1;
				case 1:
					dialogueIsWaiting = true;
					dialogueWaitEndMs = millis() + (long)(var0[var1 + 1] * 10);
					var1 += 2;
					break;
				case 2:
					dialogueIsWaiting = true;
					dialogueIsOkKeyEnabled = true;
					var1++;
					break;
				case 3:
					if (dialogueEnvironment == 3) {
						if (var0[var1 + 1] == 6) {
							spaceMapSetCameraToBeacon();
						}

						if (var0[var1 + 1] == 5) {
							spaceMapSetCameraInstant(0, 0);
						}

						var1 += 2;
						break;
					}

					switch(var0[var1 + 1]) {
						case 0:
							dialogueCameraTargetX = levelCircleX[0];
							dialogueCameraTargetY = levelCircleY[0];
							var1 += 2;
							break;
						case 1:
							if (dialogueIsWazActive) {
								dialogueCameraTargetX = levelCircleX[6];
								dialogueCameraTargetY = levelCircleY[6];
							} else {
								dialogueCameraTargetX = levelCircleX[0];
								dialogueCameraTargetY = levelCircleY[0];
							}

							var1 += 2;
							break;
						case 5:
							dialogueCameraTargetX = dialogueOriginX;
							dialogueCameraTargetY = dialogueOriginY;
							var1 += 2;
							break;
						default:
							dialogueCameraTargetX = levelAlignToGameMirror(var0[var1 + 1] << 16);
							dialogueCameraTargetY = var0[var1 + 2] << 16;
							var1 += 3;
					}

					levelSetCamera(dialogueCameraTargetX, dialogueCameraTargetY, 0);
					break;
				case 4:
					dialogueIsCameraMoving = true;
					if (dialogueEnvironment == 3) {
						if (var0[var1 + 1] == 6) {
							spaceMapSetTargetCameraToBeacon();
						}

						if (var0[var1 + 1] == 5) {
							spaceMapSetTargetCamera(0, 0);
						}

						var1 += 2;
						dialogueIsWaiting = true;
						break;
					}

					switch(var0[var1 + 1]) {
						case 0:
							dialogueCameraTargetX = levelCircleX[0];
							dialogueCameraTargetY = levelCircleY[0];
							var1 += 2;
							break;
						case 1:
							if (dialogueIsWazActive) {
								dialogueCameraTargetX = levelCircleX[6];
								dialogueCameraTargetY = levelCircleY[6];
							} else {
								dialogueCameraTargetX = levelCircleX[0];
								dialogueCameraTargetY = levelCircleY[0];
							}

							var1 += 2;
							break;
						case 5:
							dialogueCameraTargetX = dialogueOriginX;
							dialogueCameraTargetY = dialogueOriginY;
							var1 += 2;
							break;
						default:
							dialogueCameraTargetX = levelAlignToGameMirror(var0[var1 + 1] << 16);
							dialogueCameraTargetY = var0[var1 + 2] << 16;
							var1 += 3;
					}

					dialogueIsWaiting = true;
					break;
				case 5:
					setCurrentSpeech(getText(var0[var1 + 1]), var0[var1 + 2]);
					isSpeakingAnimationPlaying = true;
					dialogueIsWaiting = true;
					var1 += 3;
					break;
				case 6:
					currentEmotion[var0[var1 + 1]] = var0[var1 + 2];
					if (dialogueSfx[var0[var1 + 2]] != -1 && !var2) {
						playSound(dialogueSfx[var0[var1 + 2]], 1);
					}

					var1 += 3;
					break;
				case 7:
					if (dialogueEnvironment == 5) {
						currentPurpleX = var0[var1 + 2] * 128 / 1000;
						currentPurpleY = var0[var1 + 3] * 128 / 1000;
					} else if (activeSwapKey == 0) {
						if (var0[var1 + 1] == 0) {
							levelSetPlayerPos(levelAlignToGameMirror(var0[var1 + 2] << 16), var0[var1 + 3] << 16, 0, 0x0c0000);
						} else {
							levelSetPlayerPos(levelAlignToGameMirror(var0[var1 + 2] << 16), var0[var1 + 3] << 16, 6, 0x140000);
						}
					}

					var1 += 4;
					break;
				case 8:
					if (!var2) {
						dialogueIsContinued = true;
						setNewState(6, var0[var1 + 1]);
						var1 += 2;
						return var1;
					}

					var1 += 2;
					break;
				case 9:
					dialogueEnvironment = var0[var1 + 1];
					hideDialogueBox();
					switch(dialogueEnvironment) {
						case 0:
							initSpace(2, 12, 92, 46);
							swapLevelData(0);
							levelSetPlayerPos(levelCircleX[6], levelCircleY[6], 6, 0x140000);
							gamma = 100;
							levelCameraZoom = 72;
							break;
						case 1:
							if (!dialogueIsWazActive) {
								swapLevelData(1);
							}
						case 2:
						case 5:
						default:
							break;
						case 3:
							initSpaceMap(var0[var1 + 2] * 72 / 100, var0[var1 + 3] * 72 / 100, var0[var1 + 4]);
							initSpace(1, 150, 256, 256);
							var1 += 3;
							break;
						case 4:
							initSpace(1, 50, 128, 128);
					}

					var1 += 2;
					break;
				case 10:
					hideDialogueBox();
					isSpeakingAnimationPlaying = false;
					var1++;
					break;
				case 11:
					showBlackBars = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 12:
					if (dialogueEnvironment == 3) {
						spaceMapPlanetBeaconRadius = var0[var1 + 1] == 1 ? 1 : 0;
					}

					if (dialogueEnvironment == 0) {
						shipAlarmRadius = var0[var1 + 1] == 1 ? 1 : 0;
					}

					var1 += 2;
					break;
				case 13:
					if (var0[var1 + 1] == 5) {
						spaceMapShowShip = var0[var1 + 2] == 1;
					} else {
						spaceMapShowPlanet = var0[var1 + 2] == 1;
					}

					var1 += 3;
					break;
				case 14:
					isTransmodigrafierMissing = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 15:
					isTransmissionShaky = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 16:
					isShowingShardPicture = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 17:
					pingBackgroundColor = var0[var1 + 1];
					var1 += 2;
				case 18:
				default:
					break;
				case 19:
					activePurpleShardNameId = var0[var1 + 1];
					var1 += 2;
			}
		}

		return var1;
	}

	public static final void sceneSelectionRun() {
		int select = getSelectedIndex();
		if (select >= 0) {
			if (sceneSelectionState == 0) {
				language = select;
				saveRecordData();
				setTextTableCrc((new short[] {(short)0x0085, (short)0x9e91, (short)0xdc77, (short)0x0d48, (short)0x4f2a})[language]);
				setNewState(0, 1);
				return;
			}

			isAudioEnabled = select == 1 || select == 3;
			loadAllSounds();
			setNewState(1, 0);
		}
	}

	public static final void sceneSelectionInit(int var0) {
		gamma = 100;
		sceneSelectionState = var0;
		if (sceneSelectionState == 0) {
			loadSelectionOptions(new String[] {"flag_uk", "flag_france", "flag_germany", "flag_spain", "flag_italy"}, language);
		}

		if (sceneSelectionState == 1) {
			loadSelectionOptions(new String[] {"audio_off", "audio_on", "audio_off", "audio_on"}, 0);
		}
	}

	public static final void sceneSelectionFree() {
		gamma = 100;
		sceneSelectionCleanup();
	}

	public static final void sceneMainMenuRun() {
		gSetColor(0);
		gFillRect(0, 0, 128, 128);
		renderSpace(0, 0, 0, true);
		if (imgGamelogoTop != null) {
			gDrawImage(imgGamelogoTop, 17, 0, 0);
		}

		if (imgGamelogoBottom != null) {
			gDrawImage(imgGamelogoBottom, 24, 55, 0);
		}

		if ((millis() & 512L) > 0L) {
			renderText(-1000, 114, titleText, 0);
		}

		if (softkeyPressed(2, -1, true) == 2) {
			isPastSplash = true;
			setNewState(6, 2);
		}

		refreshGame();
	}

	public static final void sceneMainMenuInit(int var0) {
		if (isPastSplash) {
			isPastSplash = false;
			setNewState(2, 10);
		} else {
			if (titleText == null) {
				titleText = getText(0x1000f);
			}

			imgGamelogoTop = loadImage("gamelogo.pim", "gamelogo.ppl");
			imgGamelogoBottom = loadImage("gamelogo2.pim", "gamelogo2.ppl");
			initSpace(1, 50, 128, 128);
			playSound(8);
			isBeamAnimated = false;
		}
	}

	public static final void sceneMainMenuFree() {
		imgGamelogoTop = null;
		imgGamelogoBottom = null;
		garbageCollector();
		playSound(-1);
	}

	public static final void sceneTransitionRun() {
		refreshGame();
	}

	public static final void _updateTransition() {
		if (!sceneTransitionDisabled) {
			if (sceneTransitionStartMs == -1L) {
				sceneTransitionStartMs = millis();
			}

			int var0 = (int)(millis() - sceneTransitionStartMs);
			int var1 = sceneTransitionOffset + var0 * 1000 / sceneTransitionInvCoeff;
			if (sceneTransitionOffset == 0 && var1 > 1000) {
				var1 = 1000;
				setNewState(sceneTransitionNextState, 1);
			}

			if (var1 > 2000) {
				sceneTransitionDisabled = true;
			} else {
				switch(sceneTransitionType) {
					case 0:
						renderTransition(var1);
					default:
						return;
					case 2:
						fillScreenBlack(var1);
				}
			}
		}
	}

	public static final void sceneTransitionInit(int var0) {
		sceneTransitionOffset = 0;
		sceneTransitionDisabled = false;
		sceneTransitionStartMs = millis();
		int var1 = sceneTransitionNextState;
		sceneTransitionNextState = oldState;
		if (var0 >= 100) {
			sceneTransitionNextState = var0 - 100;
			var0 = 2;
		}

		sceneTransitionType = var0;
		sceneTransitionInvCoeff = 250;
		if (var0 == 2) {
			sceneTransitionInvCoeff = 150;
		}

		if (sceneTransitionNextState == 6) {
			sceneTransitionNextState = var1;
		}
	}

	public static final void sceneTransitionFree() {
		sceneTransitionOffset = 1000;
		sceneTransitionStartMs = -1L;
	}

	public static final void fillScreenBlack(int var0) {
		int var1 = gGetColor();
		gSetColor(0);
		gSetClip(0, 0, 128, 128);
		gFillRect(0, 0, 128, 128);
		gSetColor(var1);
	}

	public static final void renderTransition(int var0) {
		int var1 = var0 * 128 / 1000;
		int var2 = gGetColor();
		gSetColor(0);
		gSetClip(0, 0, 128, 128);
		gFillRect(var1 - 128, 0, 128, 128);
		int var3 = var1 + 1;
		if (var0 > 1000) {
			var3 = 129;

			for (int var4 = 6; var4 > 0; var4--) {
				gFillRect(var1 - var3 - var4, 0, var4, 128);
				var3 += var4 + (8 - var4);
			}
		} else {
			for (int var6 = 6; var6 > 0; var6--) {
				gFillRect(var3, 0, var6, 128);
				var3 += var6 + (8 - var6);
			}
		}

		gSetColor(var2);
	}

	public static final void loadFaces() {
		if (imgsEyeLeft == null) {
			imgsEyeLeft = new Image[2];
			imgsEyeLeft[0] = loadImage("eyeLWiz.pim", "eyeLWiz.ppl");
			imgsEyeLeft[1] = loadImage("eyeLWaz.pim", "eyeLWaz.ppl");
		}

		if (imgsEyeRight == null) {
			imgsEyeRight = new Image[2];
			imgsEyeRight[0] = loadImage("eyeRWiz.pim", "eyeRWiz.ppl");
			imgsEyeRight[1] = loadImage("eyeRWaz.pim", "eyeRWaz.ppl");
		}

		if (imgsEyeM == null) {
			imgsEyeM = new Image[2];
			imgsEyeM[0] = loadImage("eyeMWiz.pim", "eyeMWiz.ppl");
			imgsEyeM[1] = loadImage("eyeMWaz.pim", "eyeMWaz.ppl");
		}

		if (imgsEyeC == null) {
			imgsEyeC = new Image[2];
			imgsEyeC[0] = loadImage("eyeCWiz.pim", "eyeCWiz.ppl");
			imgsEyeC[1] = loadImage("eyeCWaz.pim", "eyeCWaz.ppl");
		}

		if (imgMouth == null) {
			imgMouth = loadImage("mouth.pim", "mouth.ppl");
		}

		if (imgsMouths == null) {
			imgsMouths = new Image[8][];

			for (int var0 = 0; var0 < imgsMouths.length; var0++) {
				imgsMouths[var0] = new Image[2];
				imgsMouths[var0][0] = loadImage("mouthWiz_" + var0 + ".pim", "mouthWiz_" + var0 + ".ppl");
				imgsMouths[var0][1] = loadImage("mouthWaz_" + var0 + ".pim", "mouthWaz_" + var0 + ".ppl");
			}
		}
	}

	public static final void loadEyes() {
		if (imgsEyes == null) {
			imgsEyes = new Image[8][];

			for (int var0 = 1; var0 < imgsEyes.length; var0++) {
				imgsEyes[var0] = new Image[2];
				imgsEyes[var0][0] = loadImage("eyesWiz_" + var0 + ".pim", "eyesWiz_" + var0 + ".ppl");
				imgsEyes[var0][1] = loadImage("eyesWaz_" + var0 + ".pim", "eyesWaz_" + var0 + ".ppl");
			}

			imgsEyesC = new Image[8][];

			for (int var1 = 1; var1 < imgsEyesC.length; var1++) {
				imgsEyesC[var1] = new Image[2];
				imgsEyesC[var1][0] = loadImage("eyesWiz_C" + var1 + ".pim", "eyesWiz_C" + var1 + ".ppl");
				imgsEyesC[var1][1] = loadImage("eyesWaz_C" + var1 + ".pim", "eyesWaz_C" + var1 + ".ppl");
			}
		}
	}

	public static final void initPlayer(boolean var0) {
		levelPlayerHealth = 500;
		if (var0) {
			levelPlayerRed = 200;
			levelPlayerGreen = 0;
			levelPlayerBlue = 0;
			levelPlayerEyeVectorMagnitude = 13;
		} else {
			levelPlayerRed = 0;
			levelPlayerGreen = 0;
			levelPlayerBlue = 200;
			levelPlayerEyeVectorMagnitude = 18;
		}

		levelPlayerRotationRate = 0;
		levelPlayerCurrentGrabberFlags = -1;
		levelPlayerBlockGrabberId = -1;
	}

	public static final void levelSetPlayerPos(int var0, int var1, int var2, int var3) {
		levelCircleX[var2 + 0] = var0;
		levelCircleY[var2 + 0] = var1;
		levelCircleX[var2 + 1] = var0;
		levelCircleY[var2 + 1] = var1 - var3;
		levelCircleX[var2 + 2] = var0;
		levelCircleY[var2 + 2] = var1 + var3;
		levelCircleX[var2 + 3] = var0 - var3;
		levelCircleY[var2 + 3] = var1;
		levelCircleX[var2 + 4] = var0 + var3;
		levelCircleY[var2 + 4] = var1;

		for (int var4 = var2; var4 < var2 + 5; var4++) {
			levelCirclePrevX[var4] = levelCircleX[var4];
			levelCirclePrevY[var4] = levelCircleY[var4];
		}
	}

	public static final void levelPlayerReleaseGrabber() {
		levelHookIsActive[16] = false;
		levelHookIsVisible[16] = false;
		levelCircleFlags[levelHookId2[16]] = levelPlayerCurrentGrabberFlags;
		levelPlayerCurrentGrabberFlags = -1;
		levelPlayerBlockGrabberId = levelHookId2[16];
		levelHookId2[16] = -1;
	}

	public static final void updatePlayerRotation() {
		if (levelPlayerBlockGrabberId != -1 && (levelCircleX[levelPlayerBlockGrabberId] - levelCircleRadius[levelPlayerBlockGrabberId] > levelCircleX[0] + 0x190000 || levelCircleX[levelPlayerBlockGrabberId] + levelCircleRadius[levelPlayerBlockGrabberId] < levelCircleX[0] - 0x190000 || levelCircleY[levelPlayerBlockGrabberId] - levelCircleRadius[levelPlayerBlockGrabberId] > levelCircleY[0] + 0x190000 || levelCircleY[levelPlayerBlockGrabberId] + levelCircleRadius[levelPlayerBlockGrabberId] < levelCircleY[0] - 0x190000)) {
			levelPlayerBlockGrabberId = -1;
		}

		if (levelPlayerJumpTimes > 0) {
			int[] var10000 = levelCircleY;
			var10000[0] -= 550000;
			levelPlayerJumpTimes--;
		}

		if (levelPlayerRotationRate > 0) {
			levelPlayerRotationRate--;
			if (levelPlayerRotationRate > 25) {
				levelPlayerRotationRate = 25;
			}
		}

		if (levelPlayerRotationRate < 0) {
			levelPlayerRotationRate++;
			if (levelPlayerRotationRate < -25) {
				levelPlayerRotationRate = -25;
			}
		}

		levelPlayerAngle += levelPlayerRotationRate;
		if (levelPlayerAngle >= 360) {
			levelPlayerAngle -= 360;
		}

		if (levelPlayerAngle < 0) {
			levelPlayerAngle += 360;
		}

		if (levelPlayerRotationRate == 0) {
			byte var0 = 95;
			if (!levelIsPlayerOnSurface) {
				var0 = 90;
			}

			if (levelPlayerAngle < 180) {
				levelPlayerAngle = levelPlayerAngle * var0 / 100;
			} else {
				levelPlayerAngle -= 360;
				levelPlayerAngle = levelPlayerAngle * var0 / 100;
				levelPlayerAngle += 360;
			}

			if (levelPlayerAngle >= 360) {
				levelPlayerAngle -= 360;
			}

			if (levelPlayerAngle < 0) {
				levelPlayerAngle += 360;
			}
		}
	}

	public static final void renderFacePurple(int var0, int var1) {
		int var2 = levelAlignX(var0, var1);
		int var3 = levelAlignY(var0, var1);
		int var4 = levelCameraZoom * 10 / 100;
		int var5 = levelCameraZoom * 25 / 100;
		if (!isOutOfScreenBounds(var2 - var4, var3 - var5, var2 + var4, var3 + var5)) {
			gSetColor(0);
			gFillArc(var2 - var4 - 2, var3 - var5 - 2, 2 * (var4 + 2), 2 * (var5 + 2), 0, 360);
			gSetColor(0xa020f0);
			gFillArc(var2 - var4, var3 - var5, 2 * var4, 2 * var5, 0, 360);
			gDrawImage(imgMouth, var2 - imgMouth.getWidth() / 2, var3 - imgMouth.getHeight() / 2, 0);
			var2 -= imgsEyeM[1].getWidth() / 2;
			gDrawImage(imgsEyeM[1], var2 - var4, var3 - var5 * 2 / 3, 0);
			gDrawImage(imgsEyeM[1], var2, var3 - var5 * 2 / 3, 0);
			gDrawImage(imgsEyeM[1], var2 + var4, var3 - var5 * 2 / 3, 0);
		}
	}

	public static final void levelRenderPlayer() {
		if (levelPlayerHealth > 0) {
			levelPlayerRed = 100 + levelPlayerHealth * 100 / 500;
		}

		if ((levelPlayerHitTicks & 1) > 0) {
			renderPlayable(0, 0, false);
		} else {
			renderPlayable(0, (levelPlayerRed << 16) + (levelPlayerGreen << 8) + levelPlayerBlue, false);
		}
	}

	public static final void renderPlayable(int var0, int var1, boolean var2) {
		byte var3 = 0;
		if (var0 > 0 || var0 == 0 && activeSwapKey == 1) {
			var3 = 1;
		}

		if (var3 == 0) {
			levelPlayerEyeVectorMagnitude = 18;
			var1 = 0xff;
			if (currentLevelLoaded == -1) {
				var1 = 100 + levelPlayerHealth * 155 / 500 & 255;
			}
		} else {
			levelPlayerEyeVectorMagnitude = 13;
		}

		int var4 = levelAlignX(levelCircleX[var0], levelCircleY[var0]);
		int var5 = levelAlignY(levelCircleX[var0], levelCircleY[var0]);
		int[] var6 = new int[4];
		if (levelCameraAngle > 45 && levelCameraAngle < 315) {
			if (levelCameraAngle >= 45 && levelCameraAngle <= 135) {
				var6[0] = levelCircleX[var0 + 0] - levelCircleX[var0 + 3] >> 16;
				var6[1] = levelCircleY[var0 + 0] - levelCircleY[var0 + 1] >> 16;
				var6[2] = levelCircleX[var0 + 4] - levelCircleX[var0 + 0] >> 16;
				var6[3] = levelCircleY[var0 + 2] - levelCircleY[var0 + 0] >> 16;
			} else if (levelCameraAngle >= 135 && levelCameraAngle <= 225) {
				var6[0] = levelCircleY[var0 + 2] - levelCircleY[var0 + 0] >> 16;
				var6[1] = levelCircleX[var0 + 0] - levelCircleX[var0 + 3] >> 16;
				var6[2] = levelCircleY[var0 + 0] - levelCircleY[var0 + 1] >> 16;
				var6[3] = levelCircleX[var0 + 4] - levelCircleX[var0 + 0] >> 16;
			} else {
				var6[0] = levelCircleX[var0 + 4] - levelCircleX[var0 + 0] >> 16;
				var6[1] = levelCircleY[var0 + 2] - levelCircleY[var0 + 0] >> 16;
				var6[2] = levelCircleX[var0 + 0] - levelCircleX[var0 + 3] >> 16;
				var6[3] = levelCircleY[var0 + 0] - levelCircleY[var0 + 1] >> 16;
			}
		} else {
			var6[0] = levelCircleY[var0 + 0] - levelCircleY[var0 + 1] >> 16;
			var6[1] = levelCircleX[var0 + 4] - levelCircleX[var0 + 0] >> 16;
			var6[2] = levelCircleY[var0 + 2] - levelCircleY[var0 + 0] >> 16;
			var6[3] = levelCircleX[var0 + 0] - levelCircleX[var0 + 3] >> 16;
		}

		for (int var7 = 0; var7 < 4; var7++) {
			var6[var7] = levelCameraZoom * var6[var7] / 100;
		}

		int[] var13 = new int[4];

		for (int var8 = 0; var8 < 4; var8++) {
			var13[var8] = var6[var8] << 1;
		}

		setGammaColor(0);
		gSetClip(var4, 0, 200, var5);
		gFillArc(var4 - var6[1] - 2, var5 - var6[0] - 2, var13[1] + 4, var13[0] + 4, 0, 360);
		gSetClip(0, 0, var4, var5);
		gFillArc(var4 - var6[3] - 2, var5 - var6[0] - 2, var13[3] + 4, var13[0] + 4, 90, 360);
		gSetClip(0, var5, var4, 200);
		gFillArc(var4 - var6[3] - 2, var5 - var6[2] - 2, var13[3] + 4, var13[2] + 4, 180, 360);
		gSetClip(var4, var5, 200, 200);
		gFillArc(var4 - var6[1] - 2, var5 - var6[2] - 2, var13[1] + 4, var13[2] + 4, 270, 360);
		setGammaColor(var1);
		gSetClip(var4, 0, 200, var5);
		gFillArc(var4 - var6[1], var5 - var6[0], var6[1] * 2, var6[0] * 2, 0, 360);
		gSetClip(0, 0, var4, var5);
		gFillArc(var4 - var6[3], var5 - var6[0], var6[3] * 2, var6[0] * 2, 90, 360);
		gSetClip(0, var5, var4, 200);
		gFillArc(var4 - var6[3], var5 - var6[2], var6[3] * 2, var6[2] * 2, 180, 360);
		gSetClip(var4, var5, 200, 200);
		gFillArc(var4 - var6[1], var5 - var6[2], var6[1] * 2, var6[2] * 2, 270, 360);
		gSetClip(0, 0, 128, 128);
		if (gamma > 50 && levelPlayerHealth > 0) {
			int var14 = levelPlayerAngle;
			if (var2) {
				var14 = 0;
			}

			if (levelPlayerCurrentGrabberFlags >= 0) {
				Image var9 = imgMouth;
				gDrawImage(var9, var4 - var9.getWidth() / 2, var5 - var9.getHeight() / 2, 0);
			} else {
				Image var15 = imgsMouths[currentEmotion[var3]][var3];
				if (currentEmotion[var3] > 0) {
					gDrawImage(var15, var4 - var15.getWidth() / 2 + mouthX[currentEmotion[var3]][var3], var5 - var15.getHeight() / 2 + mouthY[currentEmotion[var3]][var3], 0);
				} else {
					int var10 = levelCameraZoom * 6 / 100;
					int var11 = var4 + cos(var14 + 90) * var10 / 1000;
					int var12 = var5 + sin(var14 + 90) * var10 / 1000;
					gDrawImage(var15, var11 - var15.getWidth() / 2, var12 - var15.getHeight() / 2, 0);
				}
			}

			if (currentEmotion[var3] == 0) {
				Image var16 = imgsEyeM[var3];
				if (var0 > 0) {
					if (levelCircleX[0] + 0x160000 < levelCircleX[var0]) {
						var16 = imgsEyeLeft[var3];
					}

					if (levelCircleX[0] - 0x160000 > levelCircleX[var0]) {
						var16 = imgsEyeRight[var3];
					}
				} else {
					if (levelCircleX[var0] + 0x010000 < levelCirclePrevX[var0]) {
						var16 = imgsEyeLeft[var3];
					}

					if (levelCircleX[var0] - 0x010000 > levelCirclePrevX[var0]) {
						var16 = imgsEyeRight[var3];
					}
				}

				if (currentEyeBlinkTicks[var3] > 0) {
					var16 = imgsEyeC[var3];
				}

				int var18 = levelCameraZoom * levelPlayerEyeVectorMagnitude / 100;
				int var20 = var4 + cos(var14 - 115) * var18 / 1000;
				int var22 = var5 + sin(var14 - 115) * var18 / 1000;
				gDrawImage(var16, var20 - var16.getWidth() / 2, var22 - var16.getHeight() / 2, 0);
				var20 = var4 + cos(var14 - 65) * var18 / 1000;
				var22 = var5 + sin(var14 - 65) * var18 / 1000;
				gDrawImage(var16, var20 - var16.getWidth() / 2, var22 - var16.getHeight() / 2, 0);
			} else {
				int var17 = levelCameraZoom * levelPlayerEyeVectorMagnitude / 100;
				Image var19 = imgsEyes[currentEmotion[var3]][var3];
				if (currentEyeBlinkTicks[var3] > 0) {
					var19 = imgsEyesC[currentEmotion[var3]][var3];
				}

				gDrawImage(var19, var4 - var19.getWidth() / 2 + eyesX[currentEmotion[var3]][var3], var5 - var17 - var19.getHeight() / 2 + eyesY[currentEmotion[var3]][var3], 0);
			}

			if (currentEyeBlinkTicks[var3] > 0 || rand8() % 50 == var3) {
				if (currentEyeBlinkTicks[var3] > 0) {
					int var10002 = currentEyeBlinkTicks[var3]--;
					return;
				}

				currentEyeBlinkTicks[var3] = 3;
			}
		}
	}

	public static final void swapLevelPlayerData() {
		int tmp;

		tmp = levelReservedField0;
		levelReservedField0 = swappedLevelReservedField0;
		swappedLevelReservedField0 = tmp;

		tmp = levelPlayerCurrentGrabberFlags;
		levelPlayerCurrentGrabberFlags = swappedLevelCurrentGrabberFlags;
		swappedLevelCurrentGrabberFlags = tmp;

		tmp = levelReservedField1;
		levelReservedField1 = swappedLevelReservedField1;
		swappedLevelReservedField1 = tmp;

		tmp = levelPlayerRotationRate;
		levelPlayerRotationRate = swappedLevelPlayerRotationRate;
		swappedLevelPlayerRotationRate = tmp;

		tmp = levelPlayerAngle;
		levelPlayerAngle = swappedLevelPlayerAngle;
		swappedLevelPlayerAngle = tmp;
	}

	public static final void initExplosives() {
		if (imgFuse == null) {
			imgFuse = loadImage("fuse.pim", "fuse.ppl");
		}

		if (imgsBomb == null) {
			imgsBomb = new Image[2];
			imgsBomb[0] = loadImage("bombbase.pim", "bombbase.ppl");
			imgsBomb[1] = loadImage("bombbutton.pim", "bombbutton.ppl");
		}
	}

	public static final void renderBombDispenser(int var0, int var1) {
		int var2 = levelAlignX(var0, var1);
		int var3 = levelAlignY(var0, var1);
		if (levelBombStartTicks + 200 > levelTicks) {
			gDrawImage(imgsBomb[1], var2 - 9, var3 - 11 - 2, 0);
		} else {
			gDrawImage(imgsBomb[1], var2 - 9, var3 - 11 - 5, 0);
		}

		gDrawImage(imgsBomb[0], var2 - 14, var3 - 11, 0);
	}

	public static final void levelInitHooks(int var0) {
		levelNumHooks = var0;
		levelHookId1 = new int[var0];
		levelHookId2 = new int[var0];
		levelHookType = new int[var0];
		levelHookExtent = new int[var0];
		levelHookExtentSquare = new int[var0];
		levelHookStretchConstant = new int[var0];
		levelHookIsActive = new boolean[var0];
		levelHookIsVisible = new boolean[var0];
	}

	public static final void levelSetHook(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7) {
		levelHookId1[var0] = var1;
		levelHookId2[var0] = var2;
		levelHookType[var0] = var3;
		levelHookExtent[var0] = var4;
		levelHookExtentSquare[var0] = lp32Mul(var4, var4);
		levelHookStretchConstant[var0] = var5;
		levelHookIsActive[var0] = var7;
		levelHookIsVisible[var0] = var6;
	}

	public static final void levelDetachHooks(int var0) {
		for (int var1 = 0; var1 < levelNumHooks; var1++) {
			if (levelHookIsActive[var1] && (levelHookId1[var1] == var0 || levelHookId2[var1] == var0)) {
				levelHookIsActive[var1] = false;
				if (levelHookId2[16] == levelHookId1[var1] || levelHookId2[16] == levelHookId2[var1]) {
					levelPlayerReleaseGrabber();
				}
			}
		}
	}

	public static final void levelUpdateHooks() {
		for (int var0 = 0; var0 < levelNumHooks; var0++) {
			if (levelHookIsActive[var0]) {
				if (levelHookType[var0] == 0) {
					levelCircleX[levelHookId2[var0]] = levelCircleX[levelHookId1[var0]];
				} else if (levelHookType[var0] == 1) {
					levelCircleY[levelHookId2[var0]] = levelCircleY[levelHookId1[var0]];
				} else if (levelHookType[var0] == 6) {
					if (levelCircleY[levelHookId1[var0]] > levelCircleY[levelHookId2[var0]] - levelHookExtent[var0]) {
						levelCircleY[levelHookId1[var0]] = levelCircleY[levelHookId2[var0]] - levelHookExtent[var0];
					}
				} else if (levelHookType[var0] == 5) {
					if (levelCircleX[levelHookId1[var0]] > levelCircleX[levelHookId2[var0]] - levelHookExtent[var0]) {
						levelCircleX[levelHookId1[var0]] = levelCircleX[levelHookId2[var0]] - levelHookExtent[var0];
					}
				} else {
					onCircleTouchCircle(levelHookId1[var0], levelHookId2[var0], levelHookExtent[var0], levelHookStretchConstant[var0], levelHookType[var0], false);
				}
			}
		}
	}

	public static final void onCircleTouchCircle(int var0, int var1, int var2, int var3, int var4, boolean var5) {
		int var6 = levelCircleX[var1] - levelCircleX[var0];
		int var7 = levelCircleY[var1] - levelCircleY[var0];
		int var8 = vectorMagnitude(var6, var7, var2);
		if (var4 != 4 || var8 >= var2) {
			if (var4 != 3 || var8 <= var2) {
				if (var8 >= 70000) {
					int var9 = levelCircle4ByWeight[var0];
					int var10 = levelCircle4ByWeight[var1];
					int var11 = lp32Div(var8 - var2, lp32Mul(var8, var9 + var10));
					int var12 = lp32Mul(var3, lp32Mul(var6, var11));
					int var13 = lp32Mul(var3, lp32Mul(var7, var11));
					if (levelCircleHasPhysics[var0]) {
						int[] var10000 = levelCircleX;
						var10000[var0] += lp32Mul(var12, var9);
						var10000 = levelCircleY;
						var10000[var0] += lp32Mul(var13, var9);
					}

					if (levelCircleHasPhysics[var1]) {
						int[] var15 = levelCircleX;
						var15[var1] -= lp32Mul(var12, var10);
						var15 = levelCircleY;
						var15[var1] -= lp32Mul(var13, var10);
					}

					if (var5) {
						processCircles(var0, var1);
					}
				}
			}
		}
	}

	public static final void swapLevelHookData() {
		int var0 = levelNumHooks;
		levelNumHooks = swappedLevelNumHooks;
		swappedLevelNumHooks = var0;
		int[] var1 = levelHookId1;
		levelHookId1 = swappedLevelHookId1;
		swappedLevelHookId1 = var1;
		var1 = levelHookId2;
		levelHookId2 = swappedLevelHookId2;
		swappedLevelHookId2 = var1;
		var1 = levelHookType;
		levelHookType = swappedLevelHookType;
		swappedLevelHookType = var1;
		var1 = levelHookExtent;
		levelHookExtent = swappedLevelHookExtent;
		swappedLevelHookExtent = var1;
		var1 = levelHookExtentSquare;
		levelHookExtentSquare = swappedLevelHookExtentSquare;
		swappedLevelHookExtentSquare = var1;
		var1 = levelHookStretchConstant;
		levelHookStretchConstant = swappedLevelHookStretchConstant;
		swappedLevelHookStretchConstant = var1;
		boolean[] var2 = levelHookIsVisible;
		levelHookIsVisible = swappedLevelHookIsVisible;
		swappedLevelHookIsVisible = var2;
		var2 = levelHookIsActive;
		levelHookIsActive = swappedLevelHookIsActive;
		swappedLevelHookIsActive = var2;
	}

	public static final void initSpiderLegs(int num) {
		levelSpiderLegIds = new int[num];
		levelSpiderLegMovementStartX = new int[num];
		levelSpiderLegMovementEndX = new int[num];
		levelSpiderLegTicks = new int[num];
		levelSpiderLegTicksUntilMovement = new int[num];
		levelSpiderLegParent = new int[num];
	}

	public static final void levelSetSpiderLeg(int index, int legId, int parent, int movementAmplitude, int startTicks) {
		levelSpiderLegIds[index] = legId;
		levelSpiderLegMovementStartX[index] = levelCircleX[legId] - movementAmplitude;
		levelSpiderLegMovementEndX[index] = levelCircleX[legId] + movementAmplitude;

		if (startTicks == 0) {
			startTicks = 1;
		}

		levelSpiderLegTicks[index] = startTicks;
		levelSpiderLegTicksUntilMovement[index] = startTicks;
		levelSpiderLegParent[index] = parent;
	}

	public static final void immobilizeSpider(int parent) {
		for (int i = 0; i < levelSpiderLegIds.length; i++) {
			if (levelSpiderLegParent[i] == parent) {
				levelSpiderLegTicks[i] = 400000;
				levelCircle4ByWeight[levelSpiderLegIds[i]] = 52000;

				for (int k = 0; k < levelNumHooks; k++) {
					if ((levelHookId1[k] == levelSpiderLegIds[i] ||
								levelHookId2[k] == levelSpiderLegIds[i]) && !levelHookIsVisible[k]) {

						levelHookIsActive[k] = false;
					}
				}
			}
		}
	}

	public static final void updateSpiders() {
		if (levelSpiderLegIds.length > 0) {
			for (int i = 0; i < levelSpiderLegIds.length; i++) {

				int legId = levelSpiderLegIds[i];

				/*
				 * levelSpiderLegTicks[n] sign is a direction to where the
				 * spider will move.
				 *
				 * levelSpiderLegTicks[n] sign is set to its opposite value
				 * every time when the spider reaches an invisible boundary
				 * (levelSpiderLegMovement[Start,End]X).
				 */
				if (levelSpiderLegTicks[i] > 0) {
					levelSpiderLegTicks[i]--;

					if (levelSpiderLegTicks[i] == 0) {
						int inc = rand8() % 17;
						levelCircleX[legId] += inc - 10 << 14;
						levelCircleY[legId] += inc - 8 << 14;

						if (levelCircleX[legId] < levelSpiderLegMovementStartX[i]) {
							levelSpiderLegTicks[i] = -levelSpiderLegTicksUntilMovement[i];

							/* apply a new direction to all legs owned by a
							 * specific spider. */
							for (int k = 0; k < levelSpiderLegIds.length; k++) {
								if (levelSpiderLegParent[i] == levelSpiderLegParent[k]) {
									levelSpiderLegTicks[k] = levelSpiderLegTicks[i];
								}
							}
						} else {
							levelSpiderLegTicks[i] = levelSpiderLegTicksUntilMovement[i];
						}
					}
				} else {
					levelSpiderLegTicks[i]++;

					if (levelSpiderLegTicks[i] >= 0) {
						int inc = rand8() % 17;
						levelCircleX[legId] += inc - 6 << 14;
						levelCircleY[legId] += inc - 8 << 14;

						if (levelCircleX[legId] > levelSpiderLegMovementEndX[i]) {
							levelSpiderLegTicks[i] = levelSpiderLegTicksUntilMovement[i];

							for (int k = 0; k < levelSpiderLegIds.length; k++) {
								if (levelSpiderLegParent[i] == levelSpiderLegParent[k]) {
									levelSpiderLegTicks[k] = levelSpiderLegTicks[i];
								}
							}
						} else {
							levelSpiderLegTicks[i] = -levelSpiderLegTicksUntilMovement[i];
						}
					}
				}
			}
		}
	}

	public static final void levelInitExpanders(int var0) {
		levelExpanderIndex = new int[var0];
		levelExpanderIsTouchActivated = new boolean[var0];
		levelExpanderIsVacant = new boolean[var0];
		levelExpanderDelay = new int[var0];
		levelExpanderExtent = new int[var0];
		levelExpanderMinRadius = new int[var0];
		levelExpanderTicks = 0;
	}

	public static final void levelSetExpander(int var0, int var1, boolean var2, int var3, int var4) {
		levelExpanderIndex[var0] = var1;
		levelExpanderIsTouchActivated[var0] = var2;
		levelExpanderIsVacant[var0] = false;
		levelExpanderDelay[var0] = var3;
		levelExpanderExtent[var0] = var4;
		levelExpanderMinRadius[var0] = levelCircleRadius[var1];
		int[] var10000 = levelCircleFlags;
		var10000[var1] |= 0x4000;
	}

	public static final void setExpanderVacant(int var0) {
		for (int var1 = 0; var1 < levelExpanderIndex.length; var1++) {
			if (levelExpanderIndex[var1] == var0 && levelExpanderIsTouchActivated[var1]) {
				levelExpanderIsVacant[var1] = true;
			}
		}
	}

	public static final void updateExpanders() {
		levelExpanderTicks++;

		for (int var0 = 0; var0 < levelExpanderIndex.length; var0++) {
			if (levelCircleRadius[levelExpanderIndex[var0]] > levelExpanderMinRadius[var0]) {
				int[] var10000 = levelCircleRadius;
				int var10001 = levelExpanderIndex[var0];
				var10000[var10001] -= (levelCircleRadius[levelExpanderIndex[var0]] - levelExpanderMinRadius[var0]) / 20;
			}

			if (levelExpanderIsVacant[var0] || levelExpanderDelay[var0] > 0 && levelExpanderTicks % levelExpanderDelay[var0] == 0) {
				levelCircleRadius[levelExpanderIndex[var0]] = levelExpanderMinRadius[var0] + levelExpanderExtent[var0];
				levelExpanderIsVacant[var0] = false;
			}
		}
	}

	public static final void initGrabber() {
		if (imgGrabber == null && levelGrabberIds != null && levelGrabberIds.length > 0) {
			imgGrabber = loadImage("grabber.pim", "grabber.ppl");
		}
	}

	public static final void initGrabberIds(int var0) {
		levelGrabberIds = new int[var0];
	}

	public static final void setGrabberId(int var0, int var1) {
		levelGrabberIds[var0] = var1;
	}

	public static final void levelRenderGrabbers() {
		for (int i = 0; i < levelGrabberIds.length; i++) {
			int grabberId = levelGrabberIds[i];

			if (grabberId != levelHookId2[16] &&
					levelCircleFlags[grabberId] != 0) {

				levelRenderImage(imgGrabber, levelCircleX[grabberId], levelCircleY[grabberId]);
			}
		}
	}

	public static final void initPing() {
		int shardIndex = 0;

		levelPingShardIds = new int[3];
		levelPingHealth = 3;

		int[] partIndices = new int[3];

		levelPingEnemyIds = new int[3][];
		levelPingEnemyIds[0] = new int[] {-1, -1, -1, -1, -1, -1};
		levelPingEnemyIds[1] = new int[] {-1, -1, -1, -1, -1, -1};
		levelPingEnemyIds[2] = new int[] {-1, -1, -1, -1, -1, -1};

		for (int i = 0; i < levelNumCircles; i++) {
			/* Ping itself. */
			if (levelCircleType[i] == 13) {
				levelPingCircleId = i;
			}

			/* Ping's shards. */
			if (levelCircleType[i] == 15) {
				levelPingShardIds[shardIndex++] = i;
			}

			/* enemies. */
			if ((levelCircleFlags[i] & 0x80) > 0 &&
					levelCircleType[i] == 7) {

				/* Ping summons enemies in groups, and every group depends on Y
				 * value of the enemies.
				 * At least it's not hardcoded in the code...
				 *
				 * Generally all the enemies are at the top of the 25th level. */
				int enemyGroup = (levelCircleY[i] >> 16) / 100 - 1;
				levelPingEnemyIds[enemyGroup][partIndices[enemyGroup]++] = i;
				levelCircleFlags[i] &= -2;

				for (int k = 0; k < levelEnemyIndex.length; k++) {
					if (levelEnemyIndex[k] == i) {
						levelEnemyIsAlive[k] = false;
					}
				}
			}
		}

		/* Wha?... */
		if (shardIndex == 1) {
			int[] shardIds = new int[1];
			shardIds[0] = levelPingShardIds[0];
			levelPingShardIds = shardIds;
		}
	}

	public static final void updatePing() {
		/* process every Ping's shard, and if the X axis of the shard is within
		 * Ping's circle radius, we'll reduce the shard radius. */
		for (int i = 0; i < levelPingShardIds.length; i++) {
			if (levelCircleRadius[levelPingShardIds[i]] > 0 &&
					levelCircleX[levelPingShardIds[i]] > levelCircleX[levelPingCircleId] - levelCircleRadius[levelPingCircleId] &&
					levelCircleX[levelPingShardIds[i]] < levelCircleX[levelPingCircleId] + levelCircleRadius[levelPingCircleId]) {

				levelCircleRadius[levelPingShardIds[i]] -= 0x4000;
				if (levelCircleRadius[levelPingShardIds[i]] <= 0) {
					levelCircleFlags[levelPingShardIds[i]] = 0;
					levelDetachHooks(levelPingShardIds[i]);
				}
			}
		}

		/* levelPingHitTimeout = 100 if Ping is hit (levelPingHit()). */
		if (levelPingHitTimeout > 0) {
			levelPingHitTimeout--;
			if (levelPingHitTimeout == 85) {
				levelPingSpawnEnemies();
				startDialogue("ping_hit.bms", levelCircleX[levelPingCircleId], levelCircleY[levelPingCircleId] - levelCircleRadius[levelPingCircleId] * 2 / 3);
				return;
			}
		} else if (levelPingHealth < 0) {
			isLevelComplete = true;
		}
	}

	public static final void levelPingSpawnEnemies() {
		if (levelPingHitTimeout == 0) {
			startDialogue("ping_inplace.bms", levelCircleX[levelPingCircleId], levelCircleY[levelPingCircleId] - levelCircleRadius[levelPingCircleId] * 2 / 3);
		}

		/* Ping spawns enemies. */
		levelPingHealth--;
		if (levelPingHealth >= 0) {
			for (int i = 0; i < levelPingEnemyIds[levelPingHealth].length; i++) {
				int enemyId = levelPingEnemyIds[levelPingHealth][i];

				if (enemyId != -1) {
					levelCircleFlags[enemyId] |= 1;

					for (int k = 0; k < levelEnemyIndex.length; k++) {
						if (levelEnemyIndex[k] == enemyId) {
							levelEnemyIsAlive[k] = true;
						}
					}
				}
			}
		}
	}

	public static final void renderPing() {
		setGammaColor(160, 32, 240);

		for (int i = 0; i < levelPingShardIds.length; i++) {
			levelRenderCircle(levelCircleX[levelPingShardIds[i]], levelCircleY[levelPingShardIds[i]], levelCircleRadius[levelPingShardIds[i]]);
		}

		levelRenderCircle(levelCircleX[levelPingCircleId], levelCircleY[levelPingCircleId], levelCircleRadius[levelPingCircleId]);

		int eyeDiameter = levelCircleRadius[levelPingCircleId] * 2 / 5;
		int centerEyeX = levelCircleX[levelPingCircleId];
		int eyesY = levelCircleY[levelPingCircleId] - levelCircleRadius[levelPingCircleId] * 7 / 10;
		int eyeRadius = levelCircleRadius[levelPingCircleId] / 5;

		/* For each eye. */
		for (int i = -1; i < 2; i++) {
			setGammaColor(0);
			levelRenderCircle(centerEyeX + i * eyeDiameter, eyesY, eyeRadius);

			if (i < levelPingHealth) { // Opened eye.

				setGammaColor(0xffffff);
				levelRenderCircle(centerEyeX + i * eyeDiameter, eyesY, eyeRadius);

				setGammaColor(0);
				int pupilX = calcCoefficientDelta(centerEyeX + i * eyeDiameter, levelCameraX, 0x960000, eyeRadius * 3 / 4);
				int pupilY = calcCoefficientDelta(eyesY, levelCameraY, 0x640000, eyeRadius * 3 / 4);
				levelRenderCircle(centerEyeX + i * eyeDiameter + pupilX, eyesY + pupilY, eyeRadius / 3);

			} else { // Closed eye.

				setGammaColor(160, 32, 240);
				levelRenderCircle(centerEyeX + i * eyeDiameter, eyesY, eyeRadius);

			}
		}
	}

	public static final int calcCoefficientDelta(int base, int pos, int range, int coefficient) {
		int angle = (base - (pos - range)) * 90 / range;

		if (angle < 0) {
			angle = 0;
		}

		if (angle >= 180) {
			angle = 179;
		}

		return coefficient * cos1000[angle] / 1500;
	}

	public static final void levelPingHit() {
		if (levelPingHitTimeout <= 0) {
			levelPingHitTimeout = 100;
			if (levelPingHealth < 1) {
				levelPingHealth = -1;
				levelPingHitTimeout = 80;
			}
		}
	}

	public static final void initEnemies(int var0) {
		levelEnemyIndex = new int[var0];
		levelEnemyCanJump = new boolean[var0];
		levelEnemyFramesUntilJump = new int[var0];
		levelEnemyJumpHeight = new int[var0];
		levelEnemyIsMovingInX = new boolean[var0];
		levelEnemyFramesUntilXMovement = new int[var0];
		levelEnemyXMovementSpeed = new int[var0];
		levelEnemyXMovementDistance = new int[var0];
		levelEnemyIsJitteringX = new boolean[var0];
		levelEnemyIsAlive = new boolean[var0];
		levelEnemyIsBird = new boolean[var0];
		levelEnemyMovementStartTicks = new int[var0];
		levelEnemyFrame = new int[var0];
		levelEnemyXMovementDirection = new int[var0];
		levelEnemyCenterX = new int[var0];
		levelEnemyTicks = 0;
	}

	public static final void levelSetEnemy(int var0, int var1, boolean var2, int var3, int var4, boolean var5, int var6, int var7, int var8, boolean var9, int var10) {
		levelEnemyIndex[var0] = var1;
		levelEnemyCanJump[var0] = var2;
		levelEnemyFramesUntilJump[var0] = var3;
		levelEnemyJumpHeight[var0] = var4;
		levelEnemyIsMovingInX[var0] = var5;
		levelEnemyFramesUntilXMovement[var0] = var6;
		levelEnemyXMovementSpeed[var0] = var7;
		levelEnemyXMovementDistance[var0] = var8;
		levelEnemyIsJitteringX[var0] = var9;
		levelEnemyIsAlive[var0] = true;
		levelEnemyXMovementDirection[var0] = 1;
		levelEnemyFrame[var0] = 0;
		levelEnemyMovementStartTicks[var0] = var10;
		levelEnemyCenterX[var0] = levelCircleX[var1];
		levelEnemyIsBird[var0] = (levelCircleFlags[var1] & 1) == 0;
	}

	public static final void updateEnemies() {
		levelEnemyTicks++;

		for (int var0 = 0; var0 < levelEnemyIndex.length; var0++) {
			if (levelEnemyIsAlive[var0]) {
				int var1 = levelEnemyTicks + levelEnemyMovementStartTicks[var0];
				if (levelEnemyCanJump[var0] && var1 % levelEnemyFramesUntilJump[var0] == 0) {
					int[] var10000 = levelCircleY;
					int var10001 = levelEnemyIndex[var0];
					var10000[var10001] -= levelEnemyJumpHeight[var0];
				}

				if (levelEnemyIsMovingInX[var0]) {
					if (var1 % levelEnemyFramesUntilXMovement[var0] == 0) {
						int[] var2 = levelCircleX;
						int var3 = levelEnemyIndex[var0];
						var2[var3] += levelEnemyXMovementDirection[var0] * levelEnemyXMovementSpeed[var0];
						if (levelEnemyIsJitteringX[var0] && rand8() % 50 == 49) {
							levelEnemyXMovementDirection[var0] = -levelEnemyXMovementDirection[var0];
						}
					}

					if (levelCircleX[levelEnemyIndex[var0]] > levelEnemyCenterX[var0] + levelEnemyXMovementDistance[var0]) {
						levelEnemyXMovementDirection[var0] = -1;
					}

					if (levelCircleX[levelEnemyIndex[var0]] < levelEnemyCenterX[var0] - levelEnemyXMovementDistance[var0]) {
						levelEnemyXMovementDirection[var0] = 1;
					}
				}
			}
		}
	}

	public static final boolean isEnemyStatic(int var0) {
		for (int var1 = 0; var1 < levelEnemyIndex.length; var1++) {
			if (levelEnemyIndex[var1] == var0) {
				if (!levelEnemyCanJump[var1] && !levelEnemyIsMovingInX[var1]) {
					return true;
				}

				return false;
			}
		}

		return false;
	}

	public static final boolean isEnemyAlive(int var0) {
		for (int var1 = 0; var1 < levelEnemyIndex.length; var1++) {
			if (levelEnemyIndex[var1] == var0) {
				return levelEnemyIsAlive[var1];
			}
		}

		return false;
	}

	public static final void killEnemy(int var0) {
		for (int var1 = 0; var1 < levelEnemyIndex.length; var1++) {
			if (levelEnemyIndex[var1] == var0 && levelEnemyIsAlive[var1]) {
				levelEnemyIsAlive[var1] = false;
				int[] var10000 = levelCircleFlags;
				var10000[var0] |= 1;
				if (!levelEnemyCanJump[var1] && !levelEnemyIsMovingInX[var1]) {
					immobilizeSpider(levelEnemyMovementStartTicks[var1]);
				}

				return;
			}
		}
	}

	public static final void initEnemies() {
		if (levelEnemyIndex != null && levelEnemyIndex.length != 0) {
			if (imgsSpider == null) {
				imgsSpider = new Image[2];
				imgsSpider[0] = loadImage("spiderC.pim", "spiderC.ppl");
				imgsSpider[1] = loadImage("spiderD.pim", "spiderD.ppl");
			}

			if (imgsBirdLeft == null) {
				imgsBirdLeft = new Image[6];
				imgsBirdRight = new Image[6];

				for (int var0 = 0; var0 <= 5; var0++) {
					imgsBirdLeft[var0] = loadImage("bird_left" + var0 + ".pim", "bird_left" + var0 + ".ppl");
					imgsBirdRight[var0] = loadImage("bird_right" + var0 + ".pim", "bird_right" + var0 + ".ppl");
				}
			}

			if (imgsPointyRoll == null) {
				imgsPointyRoll = new Image[3];

				for (int var1 = 0; var1 < 3; var1++) {
					imgsPointyRoll[var1] = loadImage("pointy_roll_" + var1 + ".pim", "pointy_roll_" + var1 + ".ppl");
				}
			}

			if (imgsPointyEyes == null) {
				imgsPointyEyes = new Image[2];

				for (int var2 = 0; var2 < 2; var2++) {
					imgsPointyEyes[var2] = loadImage("pointy_eyes_" + var2 + ".pim", "pointy_eyes_" + var2 + ".ppl");
				}
			}

			if (imgsPointyMouth == null) {
				imgsPointyMouth = new Image[2];

				for (int var3 = 0; var3 < 2; var3++) {
					imgsPointyMouth[var3] = loadImage("pointy_mouth_" + var3 + ".pim", "pointy_mouth_" + var3 + ".ppl");
				}
			}
		}
	}

	public static final void renderEnemies() {
		for (int i = 0; i < levelEnemyIndex.length; i++) {
			renderEnemies(levelEnemyIndex[i], i);
		}
	}

	public static final void renderEnemies(int circleId, int enemyIndex) {
		if (levelCircleRadius[circleId] >= 30000) {
			int alignX = levelAlignX(levelCircleX[circleId], levelCircleY[circleId]);
			int alignY = levelAlignY(levelCircleX[circleId], levelCircleY[circleId]);

			if (levelEnemyIsBird[enemyIndex]) {

				levelEnemyFrame[enemyIndex] += (levelCircleX[circleId] - levelCirclePrevX[circleId]) / 7000;

				while (levelEnemyFrame[enemyIndex] >= 500) {
					levelEnemyFrame[enemyIndex] -= 500;
				}

				while (levelEnemyFrame[enemyIndex] < 0) {
					levelEnemyFrame[enemyIndex] += 500;
				}

				if (!levelEnemyIsAlive[enemyIndex]) {
					levelEnemyFrame[enemyIndex] = 510;
				}

				Image bird;
				if (levelCirclePrevX[circleId] > levelCircleX[circleId]) {
					bird = imgsBirdLeft[levelEnemyFrame[enemyIndex] / 100];
				} else {
					bird = imgsBirdRight[levelEnemyFrame[enemyIndex] / 100];
				}

				gDrawImage(bird, alignX - bird.getWidth() / 2, alignY - bird.getHeight() / 2, 0);

			} else if (!levelEnemyCanJump[enemyIndex] && !levelEnemyIsMovingInX[enemyIndex]) {

				Image spider = imgsSpider[0];
				if (!levelEnemyIsAlive[enemyIndex]) {
					spider = imgsSpider[1];
				}

				gDrawImage(spider, alignX - spider.getWidth() / 2, alignY - spider.getHeight() / 2, 0);

			} else {

				levelEnemyFrame[enemyIndex] += (levelCircleX[circleId] - levelCirclePrevX[circleId]) / 3000;

				while (levelEnemyFrame[enemyIndex] >= 300) {
					levelEnemyFrame[enemyIndex] -= 300;
				}

				while (levelEnemyFrame[enemyIndex] < 0) {
					levelEnemyFrame = levelEnemyFrame;
					levelEnemyFrame[enemyIndex] += 300;
				}

				Image pointyRoll = imgsPointyRoll[levelEnemyFrame[enemyIndex] / 100];
				if (pointyRoll == null) {
					pointyRoll = imgsPointyRoll[0];
				}

				Image pointyEyes;
				if (levelCircleY[circleId] < levelCircleY[0]) {
					pointyEyes = imgsPointyEyes[0];
				} else {
					pointyEyes = imgsPointyEyes[1];
				}

				Image pointyMouth = imgsPointyMouth[0];
				if (circleId == lastAttackingEnemy && levelPlayerHitTicks > 0) {
					pointyMouth = imgsPointyMouth[1];
					levelRenderCritterShock(levelCircleX[1 + levelPlayerHitTicks % 4], levelCircleY[1 + levelPlayerHitTicks % 4], levelCircleX[circleId], levelCircleY[circleId]);
				}

				gDrawImage(pointyRoll, alignX - pointyRoll.getWidth() / 2, alignY - pointyRoll.getHeight() / 2, 0);
				if (levelEnemyIsAlive[enemyIndex]) {
					gDrawImage(pointyEyes, alignX + 0 - pointyEyes.getWidth() / 2, alignY + -6 - pointyEyes.getHeight() / 2, 0);
					gDrawImage(pointyMouth, alignX + 0 - pointyMouth.getWidth() / 2, alignY + 3 - pointyMouth.getHeight() / 2, 0);
				}

			}
		}
	}

	public static final void levelInitRects(int var0) {
		levelNumRects = var0;
		levelRectX = new int[var0];
		levelRectY = new int[var0];
		levelRectHWidth = new int[var0];
		levelRectHHeight = new int[var0];
		levelRectIsAnimated = new boolean[var0];
	}

	public static final void levelAddRect(int var0, int var1, int var2, int var3, int var4) {
		levelRectX[var0] = var1;
		levelRectY[var0] = var2;
		levelRectHWidth[var0] = var3;
		levelRectHHeight[var0] = var4;
		levelRectIsAnimated[var0] = false;
	}

	public static final void levelInitAnimatedRects(int var0) {
		levelAnimRectIndex = new int[var0];
		levelAnimRectMinHWidth = new int[var0];
		levelAnimRectMaxHWidth = new int[var0];
		levelAnimRectMinHHeight = new int[var0];
		levelAnimRectMaxHHeight = new int[var0];
		levelAnimRectSpeed = new int[var0];
		levelAnimRectIterations = new int[var0];
		levelAnimRectCurrentIteration = new int[var0];
	}

	public static final void levelSetAnimRect(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
		levelRectIsAnimated[var1] = true;
		levelAnimRectIndex[var0] = var1;
		levelAnimRectMinHWidth[var0] = levelRectHWidth[var1];
		levelAnimRectMaxHWidth[var0] = var2;
		levelAnimRectMinHHeight[var0] = levelRectHHeight[var1];
		levelAnimRectMaxHHeight[var0] = var3;
		levelAnimRectSpeed[var0] = var4;
		levelAnimRectIterations[var0] = var5;
		levelAnimRectCurrentIteration[var0] = var6;
	}

	public static final void updateAnimRects() {
		for (int var0 = 0; var0 < levelAnimRectIndex.length; var0++) {
			int var1 = levelAnimRectIndex[var0];
			if (levelAnimRectCurrentIteration[var0] > 0) {
				int var10002 = levelAnimRectCurrentIteration[var0]--;
			} else if (levelAnimRectCurrentIteration[var0] < -1) {
				int var5 = levelAnimRectCurrentIteration[var0]++;
			} else if (levelAnimRectCurrentIteration[var0] == 0) {
				int[] var10000 = levelRectHWidth;
				var10000[var1] += levelAnimRectSpeed[var0];
				var10000 = levelRectHHeight;
				var10000[var1] += levelAnimRectSpeed[var0];
				if (levelRectHWidth[var1] > levelAnimRectMaxHWidth[var0]) {
					levelRectHWidth[var1] = levelAnimRectMaxHWidth[var0];
				}

				if (levelRectHHeight[var1] > levelAnimRectMaxHHeight[var0]) {
					levelRectHHeight[var1] = levelAnimRectMaxHHeight[var0];
				}

				if (levelRectHWidth[var1] == levelAnimRectMaxHWidth[var0] && levelRectHHeight[var1] == levelAnimRectMaxHHeight[var0]) {
					levelAnimRectCurrentIteration[var0] = -levelAnimRectIterations[var0] - 1;
				}
			} else if (levelAnimRectCurrentIteration[var0] == -1) {
				int[] var3 = levelRectHWidth;
				var3[var1] -= levelAnimRectSpeed[var0];
				var3 = levelRectHHeight;
				var3[var1] -= levelAnimRectSpeed[var0];
				if (levelRectHWidth[var1] < levelAnimRectMinHWidth[var0]) {
					levelRectHWidth[var1] = levelAnimRectMinHWidth[var0];
				}

				if (levelRectHHeight[var1] < levelAnimRectMinHHeight[var0]) {
					levelRectHHeight[var1] = levelAnimRectMinHHeight[var0];
				}

				if (levelRectHWidth[var1] == levelAnimRectMinHWidth[var0] && levelRectHHeight[var1] == levelAnimRectMinHHeight[var0]) {
					levelAnimRectCurrentIteration[var0] = levelAnimRectIterations[var0];
				}
			}
		}
	}

	public static final void updateCollisionCircleRect() {
		for (int var0 = 0; var0 < levelNumCircles; var0++) {
			if (levelCircleHasPhysics[var0]) {
				for (int var1 = 0; var1 < levelNumRects; var1++) {
					if (levelCircleY[var0] + levelCircleRadius[var0] > levelRectY[var1] - levelRectHHeight[var1] && levelCircleY[var0] - levelCircleRadius[var0] < levelRectY[var1] + levelRectHHeight[var1] && levelCircleX[var0] + levelCircleRadius[var0] > levelRectX[var1] - levelRectHWidth[var1] && levelCircleX[var0] - levelCircleRadius[var0] < levelRectX[var1] + levelRectHWidth[var1]) {
						onCircleTouchRect(var0, var1);
					}
				}
			}
		}
	}

	public static final void onCircleTouchRect(int var0, int var1) {
		if (var0 == 2) {
			levelIsPlayerOnSurface = true;
		}

		int var2 = levelRectX[var1] + levelRectHWidth[var1] + levelCircleRadius[var0];
		if (levelCircleX[var0] < levelRectX[var1]) {
			var2 = levelRectX[var1] - levelRectHWidth[var1] - levelCircleRadius[var0];
		}

		int var3 = levelRectY[var1] + levelRectHHeight[var1] + levelCircleRadius[var0];
		if (levelCircleY[var0] < levelRectY[var1]) {
			var3 = levelRectY[var1] - levelRectHHeight[var1] - levelCircleRadius[var0];
		}

		int var4 = levelCircleX[var0] - var2;
		if (var4 < 0) {
			var4 = -var4;
		}

		int var5 = levelCircleY[var0] - var3;
		if (var5 < 0) {
			var5 = -var5;
		}

		if (var4 < var5) {
			int[] var6 = levelCircleX;
			var6[var0] += (var2 - levelCircleX[var0]) / 2;
		} else {
			int[] var10000 = levelCircleY;
			var10000[var0] += (var3 - levelCircleY[var0]) / 2;
		}
	}

	public static final void levelRenderRects() {
		setGammaColor(0, 0, 0);

		for (int var0 = 0; var0 < levelNumRects; var0++) {
			levelDrawRect(levelRectX[var0], levelRectY[var0], levelRectHWidth[var0], levelRectHHeight[var0]);
		}

		setGammaColor(levelColor);

		for (int var1 = 0; var1 < levelNumRects; var1++) {
			levelDrawRect(levelRectX[var1], levelRectY[var1], levelRectHWidth[var1], levelRectHHeight[var1]);
		}
	}

	public static final void levelLoadRects() {
		levelInitRects(sReadU16());

		for (int var0 = 0; var0 < levelNumRects; var0++) {
			levelAddRect(var0, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, sReadU16() << 16, sReadU16() << 16);
		}
	}

	public static final void swapLevelRectData() {
		int var0 = levelNumRects;
		levelNumRects = swappedLevelNumRects;
		swappedLevelNumRects = var0;
		int[] var1 = levelRectX;
		levelRectX = swappedLevelRectX;
		swappedLevelRectX = var1;
		var1 = levelRectY;
		levelRectY = swappedLevelRectY;
		swappedLevelRectY = var1;
		var1 = levelRectHWidth;
		levelRectHWidth = swappedLevelRectHWidth;
		swappedLevelRectHWidth = var1;
		var1 = levelRectHHeight;
		levelRectHHeight = swappedLevelRectHHeight;
		swappedLevelRectHHeight = var1;
	}

	public static final void initRocket() {
		if (imgRocket == null) {
			imgRocket = loadImage("rocket.pim", "rocket.ppl");
		}

		if (imgsVertFlame == null) {
			imgsVertFlame = new Image[2];
			imgsVertFlame[0] = loadImage("vertFlame0.pim", "vertFlame0.ppl");
			imgsVertFlame[1] = loadImage("vertFlame1.pim", "vertFlame1.ppl");
		}
	}

	public static final void renderRocket(int var0, int var1, int var2) {
		initRocket();
		gDrawImage(imgRocket, var0, var1, 0);
		if (var2 >= 0) {
			gDrawImage(imgsVertFlame[var2 & 1], var0 + 36, var1 + 33, 0);
		}
	}

	public static final void initSigns() {
		if (mustLoadSigns && imgsSign == null) {
			imgsSign = new Image[7];

			for (int var0 = 0; var0 < imgsSign.length; var0++) {
				imgsSign[var0] = loadImage("sign" + var0 + ".pim", "sign" + var0 + ".ppl");
			}
		}
	}

	public static final void initLevelSigns(int var0) {
		levelSignX = new int[var0];
		levelSignY = new int[var0];
		levelSignTypes = new int[var0];
		if (var0 > 0) {
			mustLoadSigns = true;
		}
	}

	public static final void levelSetSign(int var0, int var1, int var2, int var3) {
		if (isMirrored && var3 == 2) {
			var3 = 3;
		}

		if (isMirrored && var3 == 3) {
			var3 = 2;
		}

		levelSignX[var0] = var1;
		levelSignY[var0] = var2;
		levelSignTypes[var0] = var3;
	}

	public static final void renderSigns() {
		if (imgsSign == null) {
			initSigns();
		}

		for (int var0 = 0; var0 < levelSignX.length; var0++) {
			Image var1 = imgsSign[levelSignTypes[var0]];
			if (var1 != null) {
				levelRenderImage(var1, levelSignX[var0], levelSignY[var0]);
			}
		}
	}

	public static final void levelInitSpawners(int var0) {
		levelSpawnStartX = new int[var0];
		levelSpawnStartY = new int[var0];
		levelSpawnStartIndex = new int[var0];
		levelSpawnCurrentIndex = new int[var0];
		levelSpawnEndIndex = new int[var0];
		levelSpawnEndTick = new int[var0];
		levelSpawnCurrentTick = new int[var0];
		levelSpawnRadius = new int[var0];
		levelSpawnAngle = new int[var0];
		levelSpawnAngleVar = new int[var0];
		levelSpawnSpeed = new int[var0];
		levelSpawnSpeedVar = new int[var0];
	}

	public static final void levelSetSpawner(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
		if (isMirrored) {
			int var10 = var7 - var6;
			short var11 = 180;
			if (var6 > 180) {
				var11 = 540;
			}

			var7 = (var6 = var11 - var6 - var10) + var10;
		}

		levelSpawnStartX[var0] = var1;
		levelSpawnStartY[var0] = var2;
		levelSpawnStartIndex[var0] = var3;
		levelSpawnEndIndex[var0] = var3 + var4;
		levelSpawnEndTick[var0] = var5;
		levelSpawnCurrentTick[var0] = 0;
		levelSpawnCurrentIndex[var0] = var3;
		levelSpawnRadius[var0] = levelCircleRadius[var3];
		levelSpawnAngle[var0] = var6;
		levelSpawnAngleVar[var0] = var7 - var6;
		levelSpawnSpeed[var0] = 2 * var8;
		levelSpawnSpeedVar[var0] = 2 * (var9 - var8);
	}

	public static final void updateSpawners() {
		for (int var0 = 0; var0 < levelSpawnStartIndex.length; var0++) {
			int var10002 = levelSpawnCurrentTick[var0]++;
			if (levelSpawnCurrentTick[var0] > levelSpawnEndTick[var0]) {
				int var1 = levelSpawnCurrentIndex[var0];
				int[] var10000 = levelCircleRadius;
				var10000[var1] -= levelCircleRadius[var1] / 4;
				if (levelCircleRadius[var1] < 0x010000) {
					levelSpawnCurrentTick[var0] = 0;
					levelCircleHasPhysics[var1] = true;
					var10000 = levelCircleFlags;
					var10000[var1] |= 4;
					levelCircleRadius[var1] = levelSpawnRadius[var0];
					levelCircleX[var1] = levelSpawnStartX[var0];
					levelCircleY[var1] = levelSpawnStartY[var0];
					int var2 = levelSpawnSpeed[var0] + rand16() % levelSpawnSpeedVar[var0];
					int var3 = levelSpawnAngle[var0] + rand16() % levelSpawnAngleVar[var0];
					levelCirclePrevX[var1] = levelSpawnStartX[var0] - var2 * cos(var3);
					levelCirclePrevY[var1] = levelSpawnStartY[var0] - var2 * sin(var3);
					var10002 = levelSpawnCurrentIndex[var0]++;
					if (levelSpawnCurrentIndex[var0] >= levelSpawnEndIndex[var0]) {
						levelSpawnCurrentIndex[var0] = levelSpawnStartIndex[var0];
					}
				}
			}
		}
	}

	public static final void updateLevel() {
		updatePlayerMovement();
		levelUpdateHooks();
		updatePlayerCollision();
		updateCollisionCircleRect();
	}

	public static final void levelInitCircles(int var0) {
		levelNumCircles = var0;
		levelCircleX = new int[var0];
		levelCircleY = new int[var0];
		levelCircleRadius = new int[var0];
		levelCirclePrevX = new int[var0];
		levelCirclePrevY = new int[var0];
		levelCircleFlags = new int[var0];
		levelCircle4ByWeight = new int[var0];
		levelCircleType = new byte[var0];
		levelCircleHasPhysics = new boolean[var0];
	}

	public static final void levelSetCircle(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
		if (var6 == 3 && currentLevelLoaded != -2) {
			var5 &= -5;
		}

		levelCircleX[var0] = var1;
		levelCircleY[var0] = var2;
		levelCirclePrevX[var0] = var1;
		levelCirclePrevY[var0] = var2;
		levelCircleRadius[var0] = var3;
		levelCircleFlags[var0] = var5;
		levelCircleHasPhysics[var0] = var7;
		levelCircleType[var0] = (byte)var6;
		levelCircle4ByWeight[var0] = lp32Div(0x10000, var4 << 14);
	}

	public static final void updatePlayerMovement() {
		updatePlayerRotation();
		if (levelCircleX[0] < 0 || levelCircleX[0] > levelWidth || levelCircleY[0] > levelHeight) {
			levelPlayerHealth = 0;
		}

		for (int var0 = 0; var0 < 5; var0++) {
			if (levelCircleX[var0] - levelCirclePrevX[var0] < 0x2000 && levelCircleX[var0] - levelCirclePrevX[var0] > -0x2000) {
				levelCircleX[var0] = levelCirclePrevX[var0];
			}
		}

		for (int var4 = 0; var4 < 5; var4++) {
			if (levelCircleX[var4] - levelCirclePrevX[var4] > 0x0f0000) {
				levelCircleX[var4] = levelCirclePrevX[var4] + 0x0f0000;
			}

			if (levelCircleX[var4] - levelCirclePrevX[var4] < -0x0f0000) {
				levelCircleX[var4] = levelCirclePrevX[var4] - 0x0f0000;
			}

			if (levelCircleY[var4] - levelCirclePrevY[var4] > 0x0f0000) {
				levelCircleY[var4] = levelCirclePrevY[var4] + 0x0f0000;
			}

			if (levelCircleY[var4] - levelCirclePrevY[var4] < -0x0f0000) {
				levelCircleY[var4] = levelCirclePrevY[var4] - 0x0f0000;
			}
		}

		for (int var5 = 0; var5 < levelNumCircles; var5++) {
			if (levelCircleHasPhysics[var5]) {
				int var1 = levelCircleX[var5];
				int var2 = levelCircleY[var5];
				int var3 = levelXLossRate;
				if (var5 < 5 && levelPlayerCurrentGrabberFlags >= 0) {
					var3 = 63000;
				}

				int[] var10000 = levelCircleX;
				var10000[var5] += lp32Mul(var3, levelCircleX[var5] - levelCirclePrevX[var5]);
				var10000 = levelCircleY;
				var10000[var5] += lp32Mul(var3, levelCircleY[var5] - levelCirclePrevY[var5]);
				if ((levelCircleFlags[var5] & 1) > 0) {
					var10000 = levelCircleY;
					var10000[var5] += levelMovableGravity;
				}

				levelCirclePrevX[var5] = var1;
				levelCirclePrevY[var5] = var2;
			}
		}
	}

	public static final void updatePlayerCollision() {
		levelIsPlayerOnSurface = false;

		for (int var0 = 0; var0 < 5; var0++) {
			for (int var1 = var0 + 1; var1 < levelNumCircles; var1++) {
				if ((levelCircleFlags[var1] & 2) > 0 && levelCircleX[var0] - levelCircleRadius[var0] < levelCircleX[var1] + levelCircleRadius[var1] && levelCircleX[var0] + levelCircleRadius[var0] > levelCircleX[var1] - levelCircleRadius[var1] && levelCircleY[var0] - levelCircleRadius[var0] < levelCircleY[var1] + levelCircleRadius[var1] && levelCircleY[var0] + levelCircleRadius[var0] > levelCircleY[var1] - levelCircleRadius[var1]) {
					if ((levelCircleFlags[var1] & 0x20) > 0) {
						levelDialogueToDisplay = var1;
					} else {
						onCircleTouchCircle(var0, var1, levelCircleRadius[var0] + levelCircleRadius[var1], 55000, 3, true);
					}
				}
			}
		}

		for (int var2 = 5; var2 < levelNumCircles; var2++) {
			if (levelCircleHasPhysics[var2]) {
				for (int var3 = var2 + 1; var3 < levelNumCircles; var3++) {
					if ((levelCircleFlags[var3] & 2) > 0 && levelCircleX[var2] - levelCircleRadius[var2] < levelCircleX[var3] + levelCircleRadius[var3] && levelCircleX[var2] + levelCircleRadius[var2] > levelCircleX[var3] - levelCircleRadius[var3] && levelCircleY[var2] - levelCircleRadius[var2] < levelCircleY[var3] + levelCircleRadius[var3] && levelCircleY[var2] + levelCircleRadius[var2] > levelCircleY[var3] - levelCircleRadius[var3] && (levelCircleFlags[var3] & 0x20) == 0) {
						if ((levelCircleFlags[var3] & 0x10) > 0) {
							processCircles(var2, var3);
						} else {
							onCircleTouchCircle(var2, var3, levelCircleRadius[var2] + levelCircleRadius[var3], 55000, 3, true);
						}
					}
				}
			}
		}
	}

	public static final void swapLevelCircleData() {
		int var0 = levelNumCircles;
		levelNumCircles = swappedLevelNumCircles;
		swappedLevelNumCircles = var0;
		int[] var1 = levelCircleX;
		levelCircleX = swappedLevelCircleX;
		swappedLevelCircleX = var1;
		var1 = levelCircleY;
		levelCircleY = swappedLevelCircleY;
		swappedLevelCircleY = var1;
		var1 = levelCircleRadius;
		levelCircleRadius = swappedLevelCircleRadius;
		swappedLevelCircleRadius = var1;
		var1 = levelCirclePrevX;
		levelCirclePrevX = swappedLevelCirclePrevX;
		swappedLevelCirclePrevX = var1;
		var1 = levelCirclePrevY;
		levelCirclePrevY = swappedLevelCirclePrevY;
		swappedLevelCirclePrevY = var1;
		var1 = levelCircleFlags;
		levelCircleFlags = swappedLevelCircleFlags;
		swappedLevelCircleFlags = var1;
		var1 = levelCircle4ByWeight;
		levelCircle4ByWeight = swappedLevelCircle4ByWeight;
		swappedLevelCircle4ByWeight = var1;
		byte[] var2 = levelCircleType;
		levelCircleType = swappedLevelCircleType;
		swappedLevelCircleType = var2;
		boolean[] var3 = levelCircleHasPhysics;
		levelCircleHasPhysics = swappedLevelCircleHasPhysics;
		swappedLevelCircleHasPhysics = var3;
	}

	public static final void levelObtainIds() {
		levelIdExpanders = levelGetCircleIds(0x4000);
		levelIdDestructibles = levelGetCircleIds(0x0100);
		levelIdFoam = levelGetCircleIds(0x2000);
		levelIdGoals = levelGetCircleIds(0x0010);
		int var0 = 0;

		for (int var1 = 0; var1 < levelNumCircles; var1++) {
			if ((levelCircleFlags[var1] & 0x80) > 0 && levelCircleType[var1] != 7) {
				var0++;
			}
		}

		levelIdBombDispenser = new short[var0];
		var0 = 0;

		for (short var5 = 0; var5 < levelNumCircles; var5++) {
			if ((levelCircleFlags[var5] & 0x80) > 0 && levelCircleType[var5] != 7) {
				levelIdBombDispenser[var0++] = var5;
			}
		}

		var0 = 0;

		for (int var6 = 0; var6 < levelNumCircles; var6++) {
			if ((levelCircleFlags[var6] & 0x0004) > 0 && !levelCircleHasPhysics[var6] && (levelCircleFlags[var6] & 0x0008) == 0 && (levelCircleFlags[var6] & 0x2000) == 0 && (levelCircleFlags[var6] & 0x0100) == 0) {
				var0++;
			}
		}

		levelIdPlatforms = new short[var0];
		var0 = 0;

		for (short var7 = 0; var7 < levelNumCircles; var7++) {
			if ((levelCircleFlags[var7] & 0x0004) > 0 && !levelCircleHasPhysics[var7] && (levelCircleFlags[var7] & 0x0008) == 0 && (levelCircleFlags[var7] & 0x2000) == 0 && (levelCircleFlags[var7] & 0x0100) == 0) {
				levelIdPlatforms[var0++] = var7;
			}
		}

		levelIdRadioactive = levelGetCircleIds(8);
	}

	public static final short[] levelGetCircleIds(int var0) {
		int var1 = 0;

		for (short var2 = 0; var2 < levelNumCircles; var2++) {
			if ((levelCircleFlags[var2] & var0) == var0) {
				var1++;
			}
		}

		short[] var5 = new short[var1];
		var1 = 0;

		for (short var3 = 0; var3 < levelNumCircles; var3++) {
			if ((levelCircleFlags[var3] & var0) == var0) {
				var5[var1++] = var3;
			}
		}

		return var5;
	}

	public static final void loadLevel(int var0) {
		boolean var1 = true;
		isLevelComplete = false;
		if (var0 >= 25) {
			var0 = 24;
		}

		if (var0 == -2) {
			swapLevelData(0);
			sOpenFile("ship.bin");
			var1 = false;
		} else if (var0 == -1) {
			swapLevelData(-1);
			sOpenFile("playground" + level * 4 / 25 + ".bin");
			var1 = false;
		} else if (var0 < 10) {
			swapLevelData(1);
			sOpenFile("level.00" + var0 + ".bin");
		} else {
			swapLevelData(1);
			sOpenFile("level.0" + var0 + ".bin");
		}

		isFinalLevel = false;
		if (var0 >= 0) {
			isFinalLevel = var0 == 24;
		}

		currentLevelLoaded = var0;
		levelWidth = sReadU16() << 16;
		levelHeight = sReadU16() << 16;
		setWeatherAndBackgroundIds(sRead8(), sRead8());
		sRead8();
		sRead32();
		levelColor = sRead32();
		levelColorMovable = sRead32();
		sRead32();
		levelColorGoal = sRead32();
		levelColorDestructible = sRead32();
		int var2 = levelAlignToGameMirror(sReadU16() << 16);
		int var3 = sReadU16() << 16;
		levelFinishX = levelAlignToGameMirror(sReadU16() << 16);
		levelFinishY = sReadU16() << 16;
		int var4 = sReadU16();
		levelPortalFilledRadius = 0;
		int var5 = 0;
		int var6 = 6;
		if (!var1 && var0 != -1) {
			var6 += 5;
		}

		levelInitCircles(var6 + var4 + 1);
		if (var1) {
			var5 = levelAddHitbox(0, var2, var3, 0x140000);
		} else {
			var5 = levelAddHitbox(0, var2, var3, 0x0d0000);
		}

		levelSetCircle(var5++, 0, 0, 0xa0000, 30, 0, 0, false);
		if (!var1 && var0 != -1) {
			var5 = levelAddHitbox(var5, var2 + 0x370000, var3, 0x140000);
		}

		var6 = var5;

		for (int var7 = 0; var7 < var4; var7++) {
			levelSetCircle(var5++, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, sReadU16() << 16, sReadU16(), sReadU16(), sRead8(), sRead8() == 1);
		}

		if (var0 == -2) {
			initGrabberIds(0);
		} else {
			int var16 = 0;

			for (int var8 = 0; var8 < levelNumCircles; var8++) {
				if (levelCircleType[var8] == 3) {
					var16++;
				}
			}

			initGrabberIds(var16);
			int var18 = 0;

			for (int var9 = 0; var9 < levelNumCircles; var9++) {
				if (levelCircleType[var9] == 3) {
					setGrabberId(var18++, var9);
				}
			}
		}

		levelBombObjectId = 5;
		levelLoadRects();
		int var17 = sReadU16();
		int var19 = 0x140000;
		if (!var1) {
			var19 = 0x0c0000;
		}

		levelInitHooks(17 + var17);
		int var35 = var5 = levelSetPlayerHook(0, 0, var19);
		var5++;
		levelSetHook(var35, 0, 0, 4, 65536, 65500, false, false);

		for (int var20 = 0; var20 < var17; var20++) {
			levelSetHook(var5++, var6 + sReadU16(), var6 + sReadU16(), sRead8(), sReadU16() << 16, sReadU16(), sRead8() == 1, true);
		}

		byte var21;
		levelInitSpawners(var21 = sRead8());

		for (int var10 = 0; var10 < var21; var10++) {
			levelSetSpawner(var10, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, var6 + sRead8(), sRead8(), sReadU16(), sReadU16(), sReadU16(), sReadU16(), sReadU16());
		}

		initEnemies(var21 = sRead8());

		for (int var27 = 0; var27 < var21; var27++) {
			levelSetEnemy(var27, var6 + sReadU8(), sRead8() == 1, sReadU16(), sReadU16() << 14, sRead8() == 1, sReadU16(), sReadU16() << 14, sReadU16() << 16, sRead8() == 1, sReadU16());
		}

		initSpiderLegs(var21 = sRead8());

		for (int var28 = 0; var28 < var21; var28++) {
			levelSetSpiderLeg(var28, var6 + sReadU8(), sRead8(), sReadU16() << 16, sReadU16());
		}

		levelInitExpanders(var21 = sRead8());

		for (int var29 = 0; var29 < var21; var29++) {
			levelSetExpander(var29, var6 + sReadU8(), sRead8() == 1, sReadU16(), sReadU16() << 16);
		}

		levelInitAnimatedRects(var21 = sRead8());

		for (int var30 = 0; var30 < var21; var30++) {
			levelSetAnimRect(var30, var30, sReadU16() << 16, sReadU16() << 16, sReadU16() << 8, sReadU16(), sReadU16());
		}

		initLevelSigns(var21 = sRead8());

		for (int var31 = 0; var31 < var21; var31++) {
			levelSetSign(var31, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, sRead8());
		}

		initPlayer(var1);
		levelDeathTicks = 70;
		levelMovableGravity = 40000;
		levelBombExplodeTicks = 0;
		levelBombStartTicks = -10000;
		levelPlayerHitTicks = 0;
		levelCameraZoom = 72;
		levelXLossRate = 62000;
		levelSetCamera(levelCircleX[0], levelCircleY[0], 0);
		if (var0 >= 0) {
			for (int var32 = 0; var32 < 5; var32++) {
				int[] var37 = levelCircleY;
				var37[var32] -= 0x750000;
				var37 = levelCirclePrevY;
				var37[var32] -= 0x750000;
			}
		}

		levelIntroTicks = 0;
		if (var0 == -2) {
			levelShipDisablePods();
		} else {
			levelObtainIds();
		}

		if (isFinalLevel) {
			initPing();
		}
	}

	/* hardcoded ship pods disabling. */
	public static final void levelShipDisablePods() {
		for (int var0 = 0; var0 < levelNumCircles; var0++) {
			if ((levelCircleFlags[var0] & 4) > 0 &&
					levelCircleHasPhysics[var0]) {

				switch(levelCircleType[var0]) {

					/* disable "more games" pod physics. */
					case 7:
						levelCircleFlags[var0] = 0;
						levelCircleHasPhysics[var0] = false;
				}
			}
		}
	}

	public static final int levelAlignToGameMirror(int var0) {
		if (isMirrored) {
			return var0 > 0 && var0 < 10000 ? (levelWidth >> 16) - var0 : levelWidth - var0;
		} else {
			return var0;
		}
	}

	public static final int levelAddHitbox(int var0, int var1, int var2, int var3) {
		byte var4 = 3;
		if (var0 > 0) {
			var4 = 0;
		}

		levelSetCircle(var0++, var1, var2, 0xa0000, 60, var4, 0, true);
		levelSetCircle(var0++, var1, var2 - var3, 0x030000, 60, var4, 0, true);
		levelSetCircle(var0++, var1, var2 + var3, 0x030000, 60, var4, 0, true);
		levelSetCircle(var0++, var1 - var3, var2, 0x030000, 60, var4, 0, true);
		levelSetCircle(var0++, var1 + var3, var2, 0x030000, 60, var4, 0, true);
		return var0;
	}

	public static final int levelSetPlayerHook(int var0, int var1, int var2) {
		levelSetHook(var0++, var1 + 0, var1 + 1, 0, 0, 0, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 2, 0, 0, 0, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 3, 1, 0, 0, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 4, 1, 0, 0, false, true);
		levelSetHook(var0++, var1 + 1, var1 + 0, 6, 0x030000, 0, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 2, 6, 0x030000, 0, false, true);
		levelSetHook(var0++, var1 + 3, var1 + 0, 5, 0x030000, 0, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 4, 5, 0x030000, 0, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 1, 4, var2, 28000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 2, 4, var2, 28000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 3, 4, var2, 28000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 4, 4, var2, 28000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 1, 3, var2, 15000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 2, 3, var2, 12000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 3, 3, var2, 15000, false, true);
		levelSetHook(var0++, var1 + 0, var1 + 4, 3, var2, 15000, false, true);
		return var0;
	}

	public static final void swapLevelData(int var0) {
		if (var0 != activeSwapKey) {
			activeSwapKey = var0;
			int var1 = levelWidth;
			levelWidth = swappedLevelWidth;
			swappedLevelWidth = var1;
			var1 = levelHeight;
			levelHeight = swappedLevelHeight;
			swappedLevelHeight = var1;
			var1 = levelMovableGravity;
			levelMovableGravity = swappedLevelMovableGravity;
			swappedLevelMovableGravity = var1;
			var1 = levelXLossRate;
			levelXLossRate = swappedLevelXLossRate;
			swappedLevelXLossRate = var1;
			boolean var2 = levelIsPlayerOnSurface;
			levelIsPlayerOnSurface = swappedLevelIsPlayerOnSurface;
			swappedLevelIsPlayerOnSurface = var2;
			var2 = isLevelComplete;
			isLevelComplete = swappedIsLevelComplete;
			swappedIsLevelComplete = var2;
			swapLevelCircleData();
			swapLevelRectData();
			swapLevelHookData();
			swapCameraData();
			swapLevelPlayerData();
		}
	}

	public static final void loadAllSounds() {
		initSoundSystem(9);
		loadSoundToIndex(8, "title", 1, 15804);
		loadSoundToQueue(2, "ship", 1, 13962);
		loadSoundToQueue(7, "ingame0", 1, 10987);
		loadSoundToQueue(3, "ingame1", 1, 21882);
		loadSoundToQueue(4, "ingame2", 1, 13885);
		loadSoundToQueue(5, "ingame3", 1, 21561);
		loadSoundToQueue(6, "ingame4", 1, 14583);
		loadSoundToQueue(0, "jingle_flyIn", 1, 980);
		loadSoundToQueue(1, "jingle_complete", 1, 815);
	}

	public static final void playSound(int var0) {
		if (var0 == currentSoundId && var0 != -1) {
			if (var0 >= 0) {
				if (!isSoundActive(currentSoundId, -1)) {
					playSound(currentSoundId, -1);
				}
			}
		} else {
			queueAllSoundsForCleanup();
			queueSoundCleanup();
			currentSoundId = var0;
			if (var0 >= 0) {
				playSound(currentSoundId, -1);
			}
		}
	}

	public static final void playCurrentSound() {
		if (currentSoundId >= 0 && !isPaused && !isSoundActive(currentSoundId, -1)) {
			playSound(currentSoundId, -1);
		}
	}

	public static final void playCurrentBackgroundMusic() {
		switch(levelBackgroundId) {
			case 0:
				playSound(7);
				return;
			case 1:
				playSound(3);
				return;
			case 2:
				playSound(4);
				return;
			case 3:
				playSound(5);
				return;
			case 4:
				playSound(6);
			default:
		}
	}

	public static final void playJingleFlyIn() {
		playSound(-1);
		playSound(0, 1);
	}

	public static final void playJingleComplete() {
		playSound(-1);
		playSound(1, 1);
	}

	public static final void setWeatherAndBackgroundIds(int var0, int var1) {
		boolean var2 = false;

		for (int var3 = 0; var3 < knownBackgrounds.length; var3++) {
			if (knownBackgrounds[var3] == var1) {
				var2 = true;
			}
		}

		if (!var2) {
			var1 = knownBackgrounds[level % knownBackgrounds.length];
			if (var0 > 0) {
				var0 = fallbackWeathers[var1];
			}
		}

		levelWeatherId = var0;
		levelBackgroundId = var1;
	}

	public static final void loadBackground(int var0) {
		imgsBackground = new Image[5];

		for (int var1 = 0; var1 < 5; var1++) {
			imgsBackground[var1] = loadImage("bg_" + var0 + "_" + var1 + ".pim", "bg_" + var0 + "_" + var1 + ".ppl");
		}

		currentBackgroundHasObjects = backgroundHasObjects[var0];
		activeBackgroundPattern = backgroundPatterns[var0];
		activeBackgroundObjectsPattern = backgroundObjectsPattern[var0];
		int var3 = getBackgroundTotalHeight(activeBackgroundPattern);
		int var2 = getBackgroundTotalHeight(activeBackgroundObjectsPattern) - imgsBackground[activeBackgroundObjectsPattern[0][0]].getHeight();
		backgroundOffset = 0;
		backgroundPosY = -(var3 - (128 - var2));
		currentBackgroundTotalHeight = var3;
		backgroundObjectsPosY = 128 - var2;
		currentLoadedBackgroundId = var0;
	}

	public static final void renderBackground() {
		if (currentLevelLoaded >= 0) {
			if (imgsBackground == null || levelBackgroundId != currentLoadedBackgroundId) {
				loadBackground(levelBackgroundId);
			}

			if (!currentBackgroundHasObjects) {
				int var5 = backgroundOffset + (backgroundPosY - backgroundOffset) * (levelCameraY >> 16) / (levelHeight >> 16);
				int var7 = -((getBackgroundTotalWidth(activeBackgroundPattern) - 128) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackground(activeBackgroundPattern, var7, var5, false);
			} else {
				int var3 = backgroundOffset + (backgroundPosY - backgroundOffset) * (levelCameraY >> 16) / (levelHeight >> 16);
				int var1 = -((getBackgroundTotalWidth(activeBackgroundPattern) - 128) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackground(activeBackgroundPattern, var1, var3, false);
				var3 = currentBackgroundTotalHeight + (backgroundObjectsPosY - currentBackgroundTotalHeight) * (levelCameraY >> 16) / (levelHeight >> 16);
				var1 = -((getBackgroundTotalWidth(activeBackgroundObjectsPattern) - 128) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackground(activeBackgroundObjectsPattern, var1, var3, true);
			}
		} else {
			setGammaColor(0xdcc86d);
			gFillRect(0, 0, 128, 128);
			setGammaColor(0);

			for (int var0 = 0; var0 < 128; var0 += 40) {
				gDrawLine(0, var0, 128, var0);
			}

			for (int var2 = 0; var2 < 128; var2 += 40) {
				gDrawLine(var2, 0, var2, 128);
			}
		}
	}

	public static final void renderBackground(int[][] var0, int var1, int var2, boolean var3) {
		int var4 = 0;
		if (var3) {
			int var5 = var1;

			for (int var6 = 0; var6 < var0[0].length; var6++) {
				Image var7 = imgsBackground[var0[0][var6]];
				if (var5 < 128 && var5 > -var7.getWidth() && var2 > 0 && var2 < 128 + var7.getHeight()) {
					gDrawImage(var7, var5, var2 - var7.getHeight(), 0);
				}

				var5 += var7.getWidth();
			}

			var4++;
		}

		while (var4 < var0.length) {
			int var8 = var1;
			Image var9 = null;

			for (int var10 = 0; var10 < var0[var4].length; var10++) {
				var9 = imgsBackground[var0[var4][var10]];
				if (var8 < 128 && var8 > -var9.getWidth() && var2 < 128 && var2 > -var9.getHeight()) {
					gDrawImage(var9, var8, var2, 0);
				}

				var8 += var9.getWidth();
			}

			var2 += var9.getHeight();
			var4++;
		}
	}

	public static final int getBackgroundTotalWidth(int[][] var0) {
		int var1 = 0;

		for (int var2 = 0; var2 < var0[0].length; var2++) {
			var1 += imgsBackground[var0[0][var2]].getWidth();
		}

		return var1;
	}

	public static final int getBackgroundTotalHeight(int[][] var0) {
		int var1 = 0;

		for (int var2 = 0; var2 < var0.length; var2++) {
			var1 += imgsBackground[var0[var2][0]].getHeight();
		}

		return var1;
	}

	public static final void levelSetCamera(int var0, int var1, int var2) {
		if (activeSwapKey == 0) {
			var1 = levelHeight / 2;
			if (var0 < 0x578000) {
				var0 = 0x578000;
			}

			if (var0 > levelWidth - 0x578000) {
				var0 = levelWidth - 0x578000;
			}
		}

		levelCameraX = var0;
		levelCameraY = var1;
		levelCameraIntX = var0 >> 16;
		levelCameraIntY = var1 >> 16;

		for (levelCameraAngle = var2; levelCameraAngle > 359; levelCameraAngle -= 360) {
		}

		while (levelCameraAngle < 0) {
			levelCameraAngle += 360;
		}

		levelCameraZoom = 72;
	}

	public static final boolean levelCameraApproach(int var0, int var1, int var2) {
		return levelCameraApproach(var0, var1, var2, 0x1e0000);
	}

	public static final boolean levelCameraApproach(int var0, int var1, int var2, int var3) {
		if (activeSwapKey == 0) {
			var1 = levelHeight / 2;
		} else if (var1 > levelHeight - 0x578000) {
			var1 = levelHeight - 0x578000;
		}

		if (var0 < 0x578000) {
			var0 = 0x578000;
		}

		if (var0 > levelWidth - 0x578000) {
			var0 = levelWidth - 0x578000;
		}

		int var4 = var0 - levelCameraX;
		int var5 = var1 - levelCameraY;
		if (var4 > var3) {
			var4 = var3;
		}

		if (var4 < -var3) {
			var4 = -var3;
		}

		if (var5 > var3) {
			var5 = var3;
		}

		if (var5 < -var3) {
			var5 = -var3;
		}

		int var6 = var4 >> 17;
		while (var6 > 359) {
			var6 -= 360;
		}

		while (var6 < 0) {
			var6 += 360;
		}

		if ((var6 >= 180 || levelCameraAngle >= 180) && (var6 <= 180 || levelCameraAngle <= 180)) {
			if (var6 > 180) {
				var6 -= 360;
			}

			if (levelCameraAngle > 180) {
				levelCameraAngle -= 360;
			}

			levelCameraAngle += (var6 - levelCameraAngle) / 5;
		} else {
			levelCameraAngle += (var6 - levelCameraAngle) / 5;
		}

		while (levelCameraAngle > 359) {
			levelCameraAngle -= 360;
		}

		while (levelCameraAngle < 0) {
			levelCameraAngle += 360;
		}

		if (activeSwapKey != 0) {
			int var7 = levelCircleX[0] - levelCirclePrevX[0];
			if (var7 < 0) {
				var7 = -var7;
			}

			var7 >>= 8;
			int var8 = 82 - var7 * 72 * 45 / 0x32000;
			if (var8 < 36) {
				var8 = 36;
			}

			int var9 = (var8 - levelCameraZoom) / 10;
			if (var9 <= 10 && var9 >= 10) {
				if (var9 > 0) {
					levelCameraZoom++;
				} else if (var9 < 0) {
					levelCameraZoom--;
				}
			} else {
				levelCameraZoom += (var8 - levelCameraZoom) / 10;
			}
		} else {
			levelCameraZoom = 72;
			levelCameraAngle = 0;
		}

		if (var4 < 0xa0000 && var4 > -0xa0000 && var5 < 0xa0000 && var5 > -0xa0000) {
			return true;
		} else {
			levelCameraX += var4 / 5;
			levelCameraY += var5 / 5;
			levelCameraIntX = levelCameraX >> 16;
			levelCameraIntY = levelCameraY >> 16;
			return false;
		}
	}

	public static final int levelAlignX(int var0, int var1) {
		return levelAlignX(var0 - levelCameraX, var1 - levelCameraY, levelCameraAngle) + 0x400000 >> 16;
	}

	public static final int levelAlignY(int var0, int var1) {
		return levelAlignY(var0 - levelCameraX, var1 - levelCameraY, levelCameraAngle) + 0x400000 >> 16;
	}

	public static final void swapCameraData() {
		int var0 = levelCameraIntX;
		levelCameraIntX = swappedLevelCameraIntX;
		swappedLevelCameraIntX = var0;
		var0 = levelCameraIntY;
		levelCameraIntY = swappedLevelCameraIntY;
		swappedLevelCameraIntY = var0;
		var0 = levelCameraX;
		levelCameraX = swappedLevelCameraX;
		swappedLevelCameraX = var0;
		var0 = levelCameraY;
		levelCameraY = swappedLevelCameraY;
		swappedLevelCameraY = var0;
		var0 = levelCameraAngle;
		levelCameraAngle = swappedLevelCameraAngle;
		swappedLevelCameraAngle = var0;
	}

	public static final void loadFacesIcons() {
		if (imgsFaces == null) {
			imgsFaces = new Image[5][];
			imgsFaces[0] = new Image[2];

			for (int i = 0; i < 2; i++) {
				imgsFaces[0][i] = loadImage("wiz" + i + ".pim", "wiz" + i + ".ppl");
			}

			imgsFaces[1] = new Image[2];

			for (int i = 0; i < 2; i++) {
				imgsFaces[1][i] = loadImage("waz" + i + ".pim", "waz" + i + ".ppl");
			}

			imgsFaces[2] = new Image[2];

			for (int i = 0; i < 2; i++) {
				imgsFaces[2][i] = loadImage("purple" + i + ".pim", "purple" + i + ".ppl");
			}

			imgsFaces[3] = new Image[2];

			for (int i = 0; i < 2; i++) {
				imgsFaces[3][i] = loadImage("hal" + i + ".pim", "hal" + i + ".ppl");
			}

			hideDialogueBox();
		}
	}

	public static final void hideDialogueBox() {
		showDialogue = 0;
	}

	public static final void setCurrentSpeech(String var0, int var1) {
		int var2 = 12 + currentSpeechNumLines * 13;
		currentSpeaker = var1;
		currentSpeechNumLines = numNewLines(var0);
		currentSpeech = var0;
		currentSpeechStartMs = millis();
		if (showDialogue == 1) {
			int var3 = 12 + currentSpeechNumLines * 13;
			currentSpeechStartMs -= (long)(var2 * 400 / var3);
		}

		showDialogue = 1;
		currentSpeakerIteration = 0;
		currentSpeechIteration = 0;
		currentSpeechDone = false;
	}

	public static final void renderDialogueBox() {
		if (showDialogue == 1) {
			long currTime = millis();
			int var2 = (int)(currTime - currentSpeechStartMs);
			int boxHeight = 12 + currentSpeechNumLines * 13;
			if (currentSpeechIteration == 0 && var2 < 400) {
				boxHeight = boxHeight * var2 / 400;
			}

			gSetColor(0xffffff); // White.
			gFillRect(0, 0, 128, boxHeight);
			gSetColor(0xece9d8); // Light gray.
			gDrawLine(0, boxHeight, 128, boxHeight);
			gSetColor(0xaca899); // Darker gray.
			gDrawLine(0, boxHeight + 1, 128, boxHeight + 1);
			gSetColor(0); // Black.
			gDrawLine(0, boxHeight + 2, 128, boxHeight + 2);

			if (currentSpeechIteration != 0 || var2 >= 400) {
				byte var4 = 0;
				boolean var5 = false;
				if (imgsFaces[currentSpeaker] != null) {
					Image var6 = imgsFaces[currentSpeaker][currentSpeakerIteration % imgsFaces[currentSpeaker].length];
					int var7 = (21 - var6.getWidth()) / 2;
					if (currentSpeaker == 0) {
						gDrawImage(var6, 0 + var7, 6 + (currentSpeechNumLines * 13 - var6.getHeight()) / 2, 0);
						var4 = 21;
					} else {
						gDrawImage(var6, 107 + var7, 6 + (currentSpeechNumLines * 15 - var6.getHeight()) / 2, 0);
						var4 = 8;
					}

					renderTextEx(var4, 6, 99, currentSpeechNumLines * 13, currentSpeech, 1, 0, currentSpeechIteration, false);
				} else {
					renderTextEx(14, 6, 99, currentSpeechNumLines * 13, currentSpeech, 1, 0, currentSpeechIteration, false);
				}

				if (currTime > currentSpeechStartMs) {
					currentSpeechStartMs = currTime + 150L;
					currentSpeechIteration++;

					char var9;
					while (currentSpeechIteration < currentSpeech.length() && (var9 = currentSpeech.charAt(currentSpeechIteration)) != ' ' && var9 != '.') {
						currentSpeechIteration++;
					}

					if (currentSpeechIteration >= currentSpeech.length()) {
						currentSpeechDone = true;
						return;
					}

					currentSpeakerIteration++;
				}
			}
		}
	}

	public static final int numNewLines(String var0) {
		return getNewLineIndices(99, var0, 1).length;
	}

	public static final String[] strSplitLines(String var0, int var1) {
		int[] var2 = getNewLineIndices(99, var0, var1);
		String[] var3 = new String[var2.length];

		for (int var4 = 0; var4 < var2.length; var4++) {
			if (var4 == var2.length - 1) {
				var3[var4] = var0.substring(var2[var4]);
			} else {
				var3[var4] = var0.substring(var2[var4], var2[var4 + 1]);
			}
		}

		return var3;
	}

	public static final void sceneLevelUpdate() {
		if (levelDialogueToDisplay > -1) {
			levelDisplayDialogue(levelDialogueToDisplay);
			levelDialogueToDisplay = -1;
		} else {
			if (nextLevelToLoad >= -1) {
				loadLevel(nextLevelToLoad);
				nextLevelToLoad = -2;
				if (currentLevelLoaded < 0) {
					levelIntroTicks = 120;
				}

				if (!isDejaVuMessageShown) {
					if (currentLevelLoaded >= 0) {
						startDialogue("respawn.bms", levelCircleX[0], levelCircleY[0]);
					}

					isDejaVuMessageShown = true;
					isPlayerAlive = true;
					return;
				}

				isPlayerAlive = true;
			}

			if (levelIntroTicks >= 50) {
				updateAll();
				levelTicks++;
			}

			if (levelPlayerHealth < 1) {
				if (levelDeathTicks == 70) {
					vibrate(640);
					isPlayerAlive = false;
					levelCircleRadius[0] = 0x20000;
					levelCircleRadius[2] = 0x10000;
					levelCircleRadius[3] = 0x10000;
					levelCircleRadius[4] = 0x10000;
					levelCircleFlags[2] = 1;
					levelCircleX[2] = levelCircleX[0];
					levelCircleY[2] = levelCircleY[0] + 0xa0000;

					for (int var0 = 0; var0 < 2; var0++) {
						for (int var1 = 0; var1 < 2; var1++) {
							levelHookType[8 + 2 * var0 + var1] = 3;
							levelHookStretchConstant[8 + 2 * var0 + var1] = 500;
							levelHookExtent[8 + 2 * var0 + var1] = 0x280000;
						}
					}

					if (levelPlayerCurrentGrabberFlags >= 0) {
						levelHookIsActive[16] = false;
						levelCircleFlags[levelHookId2[16]] = levelPlayerCurrentGrabberFlags;
						levelPlayerCurrentGrabberFlags = -1;
						levelPlayerBlockGrabberId = levelHookId2[16];
					}
				}

				levelCameraApproach(levelCircleX[0] + 10 * (levelCircleX[0] - levelCirclePrevX[0]), levelCircleY[0] + 10 * (levelCircleY[0] - levelCirclePrevY[0]), 0, 0x140000);
				levelPlayerRed -= 3;
				if (levelPlayerRed < 0) {
					levelPlayerRed = 0;
				}

				levelDeathTicks--;
				if (levelDeathTicks <= 0) {
					nextLevelToLoad = currentLevelLoaded;
				}
			}

			int var2 = -1;
			if (levelIntroTicks < 100) {
				var2 = softkeyPressed(-1, 3);
			} else if (currentLevelLoaded == -1) {
				var2 = softkeyPressed(-1, 4);
			} else if (levelPlayerHealth > 0) {
				var2 = softkeyPressed(-1, 0);
			}

			if (var2 == 4) {
				mustExitPlayground = true;
			}

			if (var2 == 3) {
				levelIntroTicks = 120;
			}

			if (var2 == 0) {
				softkeyPressed(-1, -1);
				isShipPaused = true;
				gamma = 100;
				swapLevelData(0);
				setNewState(2, 0);
			} else {
				if (isPlayerAlive && levelIntroTicks >= 100) {
					updatePlayerControls();
					levelCameraApproach(levelCircleX[0] + 10 * (levelCircleX[0] - levelCirclePrevX[0]), levelCircleY[0] + 10 * (levelCircleY[0] - levelCirclePrevY[0]), 0, 3276800);
				}

				if (levelCircleHasPhysics[levelBombObjectId]) {
					if (levelTicks > levelBombStartTicks + 100) {
						levelSetCamera(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], 0);
					}

					if (levelBombExplodeTicks == 0 && levelTicks > levelBombStartTicks + 130) {
						levelBombExplodeTicks = 5;
						vibrate(40);
					}
				}

				if (levelBombExplodeTicks > 0) {
					levelSetCamera(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], 0);
					levelBombExplodeTicks--;
					levelCircle4ByWeight[levelBombObjectId] = 0;
					if (levelBombExplodeTicks > 0) {
						int[] var10000 = levelCircleRadius;
						int var10001 = levelBombObjectId;
						var10000[var10001] += 0x0f0000;
					} else {
						levelCircleHasPhysics[levelBombObjectId] = false;
						levelCircleX[levelBombObjectId] = -0xc80000;
					}
				}

				levelRender(true);
				if (levelIntroTicks < 100) {
					int var3;
					if (levelIntroTicks < 80) {
						var3 = -51 + levelIntroTicks * 51 / 50;
					} else {
						var3 = -51 + (100 - levelIntroTicks) * 51 / 20;
					}

					if (var3 > 0) {
						var3 = 0;
					}

					if (levelIntroTicks == 80) {
						vibrate(140);
					}

					if (levelIntroTicks < 80) {
						renderRocket(14, var3, -1);
					} else {
						renderRocket(rand8() % 5 - 2 + 64 - 50, var3, levelIntroTicks);
					}

					renderText(4, 114, currentPlanetText, 0);
					levelIntroTicks++;
				}

				refreshGame();
			}
		}
	}

	public static final void updatePlayerControls() {
		if ((isKeyPressed(0x1a0) || isKeyPressed(1) && !isKeyPressed(0x800)) && (levelIsPlayerOnSurface || levelPlayerCurrentGrabberFlags >= 0)) {
			if (levelPlayerCurrentGrabberFlags >= 0) {
				levelPlayerReleaseGrabber();
			}

			if (levelIsPlayerOnSurface) {
				levelPlayerJumpTimes = 3;
			}
		}

		if (isKeyHeld(0x110)) {
			levelPlayerDirection = 1;
			if (levelPlayerCurrentGrabberFlags >= 0 && !levelIsPlayerOnSurface) {
				if (levelCircleX[0] >= levelCirclePrevX[0]) {
					if (levelCircleX[0] - levelCirclePrevX[0] < 0x10000) {
						int[] var0 = levelCircleX;
						var0[0] += 0x28000;
					} else {
						int[] var1 = levelCircleX;
						var1[0] += 0x14000;
					}
				}
			} else {
				int[] var10000 = levelCircleX;
				var10000[0] += 0x14000;
				if (levelIsPlayerOnSurface) {
					levelPlayerRotationRate += 3;
				}
			}
		}

		if (isKeyHeld(0x88)) {
			levelPlayerDirection = -1;
			if (levelPlayerCurrentGrabberFlags >= 0 && !levelIsPlayerOnSurface) {
				if (levelCircleX[0] <= levelCirclePrevX[0]) {
					if (levelCirclePrevX[0] - levelCircleX[0] < 0x10000) {
						int[] var3 = levelCircleX;
						var3[0] -= 0x28000;
					} else {
						int[] var4 = levelCircleX;
						var4[0] -= 0x14000;
					}
				}
			} else {
				int[] var2 = levelCircleX;
				var2[0] -= 0x14000;
				if (levelIsPlayerOnSurface) {
					levelPlayerRotationRate -= 3;
				}
			}
		}

		if (isKeyReleased(-1)) {
			if (isKeyReleased(cheatCodeLevelComplete[iterCheatLevelComplete])) {
				iterCheatLevelComplete++;
				if (iterCheatLevelComplete >= cheatCodeLevelComplete.length) {
					isLevelComplete = true;
					iterCheatLevelComplete = 0;
				}
			} else {
				iterCheatLevelComplete = 0;
			}

			if (isKeyReleased(cheatCodeInvincibility[iterCheatInvincibility])) {
				iterCheatInvincibility++;
				if (iterCheatInvincibility >= cheatCodeInvincibility.length) {
					isPlayerInvincible = !isPlayerInvincible;
					int[] var5 = levelCircleY;
					var5[0] -= 0x80000;
					var5 = levelCircleX;
					var5[3] -= 0x80000;
					var5 = levelCircleX;
					var5[4] += 0x80000;
					iterCheatInvincibility = 0;
					return;
				}
			} else {
				iterCheatInvincibility = 0;
			}
		}
	}

	public static final void levelRender(boolean var0) {
		long var1 = millis();
		if (levelWeatherId == 1 && activeSwapKey == 1) {
			gamma = 100;
			if (rand16() % 100 == 50) {
				gamma = 0;
			}
		}

		renderBackground();

		for (int var3 = 0; var3 < levelIdGoals.length; var3++) {
			short var4 = levelIdGoals[var3];
			setGammaColor(levelColorGoal);
			levelRenderCircle(levelCircleX[var4], levelCircleY[var4], levelCircleRadius[var4]);
			setGammaColor(255, 255, 255);
			if (levelCircleRadius[var4] > levelPortalFilledRadius) {
				levelRenderCircleOutline(levelCircleX[var4], levelCircleY[var4], levelPortalFilledRadius);
			} else {
				int var5 = levelCircleRadius[var4] / 4;
				int var6 = (int)((long)var5 * (var1 % 700L)) / 700;

				for (int var7 = 0; var7 < 4; var7++) {
					levelRenderCircleOutline(levelCircleX[var4], levelCircleY[var4], levelCircleRadius[var4] - var6 - var7 * var5);
				}
			}
		}

		renderSigns();
		if (levelFinishY > 0) {
			renderFacePurple(levelFinishX, levelFinishY);
		}

		if (var0) {
			levelRenderPlayer();
		}

		renderEnemies();

		for (int var8 = 0; var8 < levelNumCircles; var8++) {
			if ((levelCircleFlags[var8] & 4) > 0 && levelCircleHasPhysics[var8] && (levelCircleFlags[var8] & 0x2000) == 0 && (levelCircleFlags[var8] & 8) == 0 && var8 != levelBombObjectId) {
				setGammaColor(0, 0, 0);
				levelRenderCircle(levelCircleX[var8], levelCircleY[var8], levelCircleRadius[var8]);
				setGammaColor(levelColorMovable);
				levelRenderCircle(levelCircleX[var8], levelCircleY[var8], levelCircleRadius[var8]);
			}
		}

		if (levelCircleHasPhysics[levelBombObjectId]) {
			setGammaColor(0, 0, 0);
			levelRenderCircle(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], levelCircleRadius[levelBombObjectId]);
			if (levelBombStartTicks != -1 && levelTicks > levelBombNextFlashTick) {
				if (levelBombFlashed) {
					setGammaColor(255, 255, 255);
					levelBombFlashed = false;
					levelBombNextFlashTick = levelTicks + (levelBombStartTicks + 100 - levelTicks) / 4;
				} else {
					setGammaColor(200, 0, 0);
					levelBombFlashed = true;
				}
			} else {
				setGammaColor(200, 0, 0);
			}

			levelRenderCircle(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], levelCircleRadius[levelBombObjectId]);
			levelRenderImage(imgFuse, levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId] - levelCircleRadius[levelBombObjectId]);
		}

		levelRenderCircleIds(levelIdPlatforms, 0);
		levelRenderRects();
		levelRenderCircleIds(levelIdPlatforms, levelColor);
		levelRenderCircleIds(levelIdFoam, 0);
		levelRenderCircleIds(levelIdFoam, 0xffffff);
		levelRenderCircleIds(levelIdDestructibles, 0);
		levelRenderCircleIds(levelIdDestructibles, levelColorDestructible);
		int var9 = (int)(var1 % 1600L);
		if (var9 > 800) {
			var9 = 1600 - var9;
		}

		levelRenderCircleIds(levelIdRadioactive, 0);
		levelRenderCircleIds(levelIdRadioactive, (levelRadioactiveMinRed + (levelRadioactiveMaxRed - levelRadioactiveMinRed) * var9 / 800 << 16) + (levelRadioactiveMinGreen + (levelRadioactiveMaxGreen - levelRadioactiveMinGreen) * var9 / 800 << 8) + levelRadioactiveMinBlue + (levelRadioactiveMaxBlue - levelRadioactiveMinBlue) * var9 / 800);
		levelRenderCircleIds(levelIdExpanders, 0);
		levelRenderCircleIds(levelIdExpanders, levelColorBouncer);
		setGammaColor(0, 0, 0);

		for (int var10 = 16; var10 < levelNumHooks; var10++) {
			if (levelHookIsActive[var10] && levelHookIsVisible[var10]) {
				levelRenderLine(levelCircleX[levelHookId1[var10]], levelCircleY[levelHookId1[var10]], levelCircleX[levelHookId2[var10]], levelCircleY[levelHookId2[var10]]);
			}
		}

		if (isFinalLevel) {
			renderPing();
		}

		levelRenderGrabbers();

		for (int var11 = 0; var11 < levelIdBombDispenser.length; var11++) {
			renderBombDispenser(levelCircleX[levelIdBombDispenser[var11]], levelCircleY[levelIdBombDispenser[var11]]);
		}

		if (levelWeatherId != 0 && activeSwapKey == 1) {
			if (levelWeatherId == 1) {
				renderRain();
			}

			if (levelWeatherId == 2) {
				levelRenderPrecipitation();
			}
		}
	}

	public static final void levelRenderCircleIds(short[] var0, int var1) {
		setGammaColor(var1);

		for (int var2 = 0; var2 < var0.length; var2++) {
			levelRenderCircle(levelCircleX[var0[var2]], levelCircleY[var0[var2]], levelCircleRadius[var0[var2]]);
		}
	}

	public static final void activateLevel(int var0) {
		loadLevel(var0);
	}

	public static final void updateAll() {
		if (levelHookIsActive[16]) {
			levelHookIsVisible[16] = true;
		}

		updateSpiders();
		updateEnemies();
		updateExpanders();
		updateAnimRects();
		if (levelPlayerHitTicks > 0) {
			levelPlayerHitTicks--;
		}

		updateSpawners();
		updateLevel();
		if (isFinalLevel) {
			updatePing();
		}
	}

	public static final void processPlayerCircles(int id1, int id2) {
		if (id1 == 2)
			levelIsPlayerOnSurface = true;

		/* destructive objects. */
		if ((levelCircleFlags[id2] & 0x2000) > 0) {
			levelCircleRadius[id2] -= 0x10000;
			if (levelCircleRadius[id2] < 0x40000) {
				levelCircleRadius[id2] = 0;
				levelCircleFlags[id2] = 0;
			}
		}

		/* radioactive goo. */
		if ((levelCircleFlags[id2] & 8) > 0 &&
				!isPlayerInvincible) {

			levelPlayerHealth -= 4000;
		}

		byte circleType = levelCircleType[id2];

		if (circleType != 0) {
			if (currentLevelLoaded != -2 &&
					activeSwapKey != 0) {

				/* grabber. */
				if (circleType == 3 &&
						levelPlayerCurrentGrabberFlags == -1 &&
						id2 != levelPlayerBlockGrabberId) {

					levelHookIsActive[16] = true;
					levelHookId2[16] = id2;
					levelPlayerCurrentGrabberFlags = levelCircleFlags[id2];
					levelCircleFlags[id2] = 0;
				}

				/* vacant an expander. */
				if (circleType == 6) {
					setExpanderVacant(id2);
				}

				/* enemy hurts a player. */
				if (circleType == 7 &&
						levelPlayerHitTicks == 0 &&
						isEnemyAlive(id2)) {

					if (isEnemyStatic(id2) && id1 == 2) {
						if (levelCircleY[2] - levelCirclePrevY[2] > 120000) {
							killEnemy(id2);
						}
					} else {
						if (!isPlayerInvincible) {
							levelPlayerHealth -= 170;
						}

						lastAttackingEnemy = id2;
						if (levelPlayerHealth > 0) {
							levelPlayerHitTicks = 50;
							vibrate(240);
						}
					}
				}

				/* finish the level. */
				if (circleType == 8) {

					isLevelComplete = true;

				} else if (circleType >= 9 &&
						circleType <= 11 &&
						id1 == 2 &&
						levelBombStartTicks + 200 < levelTicks) {

					levelBombStartTicks = levelTicks;
					levelBombNextFlashTick = levelTicks + 33;

					levelBombFlashed = false;

					int dir = levelPlayerDirection;
					if (circleType == 10) {
						dir = -1;
					}

					if (circleType == 11) {
						dir = 1;
					}

					if (isMirrored && circleType > 9) {
						dir *= -1;
					}

					levelSetCircle(levelBombObjectId, levelCircleX[id2] + dir * 0x140000, levelCircleY[id2], 0xa0000, 15, 71, 0, true);
					levelCircleX[levelBombObjectId] += dir * 0x30000;
				}
			} else {
				levelShipTouchedCircleType = circleType;
			}
		}
	}

	public static final void processCircles(int id1, int id2) {
		if (levelPlayerHealth > 0) {

			if (id1 >= 5 && id2 >= 5) { // Level circles.

				if ((levelCircleFlags[id1] & 16) > 0 || (levelCircleFlags[id2] & 16) > 0) {

					/* Check if a ball touches portal's circle hitbox, if so,
					 * fill the portal's radius with the ball's radius. */
					int swpId1 = id1;
					int swpId2 = id2;

					if ((levelCircleFlags[id2] & 16) > 0) {
						swpId1 = id2;
						swpId2 = id1;
					}

					if (levelCircleFlags[swpId2] != 0) {
						levelCircleX[swpId2] = levelCircleX[swpId1];
						levelCircleY[swpId2] = levelCircleY[swpId1];
						levelCirclePrevX[swpId2] = levelCirclePrevX[swpId1];
						levelCirclePrevY[swpId2] = levelCirclePrevY[swpId1];
						levelDetachHooks(swpId2);

						levelCircleRadius[swpId2] -= 0x8000;
						levelPortalFilledRadius += 0x8000;

						if (levelPortalFilledRadius >= levelCircleRadius[swpId1]) {
							levelPortalFilledRadius = levelCircleRadius[swpId1];
							isLevelComplete = true;
						}

						if (levelCircleRadius[swpId2] <= 0) {
							levelCircleFlags[swpId2] = 0;
							levelCircleHasPhysics[swpId2] = false;
						}
					}
				}

				/* bomb destroys destructible objects. */
				if (levelBombExplodeTicks > 0 &&
						(id1 == levelBombObjectId &&
						 (levelCircleFlags[id2] & 0x100) > 0 ||
						 id2 == levelBombObjectId && (levelCircleFlags[id1] & 0x100) > 0)) {

					int circleId = id1;
					if (id1 == levelBombObjectId) {
						circleId = id2;
					}

					/* reduce 30% of the first circle radius when the explosion
					 * wave (the second circle) reaches the first circle. */
					levelCircleRadius[circleId] = levelCircleRadius[circleId] * 7 / 10;

					if (levelCircleRadius[circleId] < 0xa0000) {
						levelCircleRadius[circleId] = 0;
						levelCircleFlags[circleId] = 0;
					}
				}

				byte circleType1 = levelCircleType[id1];
				byte circleType2 = levelCircleType[id2];

				if (circleType1 != 0 || circleType2 != 0) {

					/* vacant an expander. */
					if (circleType1 == 6 || circleType2 == 6) {

						if (circleType1 == 6)
							setExpanderVacant(id1);
						else
							setExpanderVacant(id2);

					}

					/* check if enemy's circle hitbox touches radioactive goo or
					 * an explosion wave. */
					if ((circleType1 == 7 || circleType2 == 7) &&
							((levelCircleFlags[id1] & 8) > 0 ||
							 (levelCircleFlags[id2] & 8) > 0 ||
							 levelBombExplodeTicks > 0 &&
							 (id1 == levelBombObjectId || id2 == levelBombObjectId))) {

						if (circleType1 == 7) {
							killEnemy(id1);
						} else {
							killEnemy(id2);
						}

					}

					/* a circle touches a finish circle. */
					if ((circleType1 == 1 || circleType2 == 1) &&
							(circleType1 == 2 || circleType2 == 2)) {

						isLevelComplete = true;
					}

					/* a circle touches a radioactive circle. */
					if ((circleType1 == 1 || circleType2 == 1) &&
							(circleType1 == 12 || circleType2 == 12)) {

						levelPlayerHealth -= 4000;
					}

					/* an explosion wave reaches Ping. */
					if (isFinalLevel) {

						if ((circleType1 == 13 || circleType2 == 13) &&
								(circleType1 == 14 || circleType2 == 14)) {

							if (circleType1 == 14) {
								levelCircleFlags[id1] = 0;
							} else {
								levelCircleFlags[id2] = 0;
							}

							levelPingSpawnEnemies();
						}

						if (levelBombExplodeTicks > 0 &&
								(id1 == levelBombObjectId && circleType2 == 13 ||
								 id2 == levelBombObjectId && circleType1 == 13)) {

							levelPingHit();
						}
					}
				}
			} else { // Player circles.

				if (id1 >= 5) {
					processPlayerCircles(id2, id1);
				}

				if (id2 >= 5) {
					processPlayerCircles(id1, id2);
				}
			}
		}
	}

	public static final void loadGeneral() {
		gSetColor(255, 255, 255);
		gFillRect(0, 0, 128, 128);
		gfxFlush(); // White screen at the start.
		loadGeneralUI();
		loadTrigonometric();
		loadRecordData();
		if (level > 0) {
			skipPrologue = true;
		}

		setNewState(0, 0);
	}

	public static final void updateTransition() {
		playCurrentSound();
		_updateTransition();
	}

	public static final void loadGeneralUI() {
		setRandSeed(0);
		bfcLoadHead();
		loadRecordData();

		initFonts(4);
		loadFont(3, (short)0xb6ce, (short)0x7b1d, (short)0xc674, (byte)4, (short)0x88a8, 1, -2);
		loadFont(1, (short)0xe878, (short)0x25ab, (short)0x98c2, (byte)3, (short)0xd61e, 1, -1);
		loadFont(2, (short)0xa1c0, (short)0x6c13, (short)0xd17a, (byte)3, (short)0x9fa6, 1, -1);
		loadFont(0, (short)0x86ec, (short)0x4b3f, (short)0xf656, (byte)3, (short)0xb88a, -1, -1);

		initSoftkeyIcons();
		setSoftkeyIcon(2, loadImage((short)0xe4f2, (short)0x2921));
		setSoftkeyIcon(1, loadImage((short)0x31d7, (short)0xfc04));
		setSoftkeyIcon(0, loadImage((short)0x9207, (short)0x5fd4));
		setSoftkeyIcon(3, loadImage((short)0x0545, (short)0xc896));
		setSoftkeyIcon(4, loadImage((short)0x5c21, (short)0x91f2));
	}

	public static final void setPauseScreenDraw() {
		pauseScreenDraw = true;
	}

	public static final void pauseScreenOnce() {
		if (pauseScreenDraw) {
			pauseScreenDraw = false;
			gSetColor(0, 0, 0);

			for (int y = 0; y < 128; y++) {
				for (int x = y & 1; x < 128; x += 2) {
					gDrawLine(x, y, x, y);
				}
			}

			gFillRect(0, 109, 128, 128);
			softkeyPressed(2, -1);
		}
	}

	// Funny circle again...
	public static final void renderLoadingBarNextFrame() {
		gSetColor(0, 0, 0);
		gFillRect(0, 0, 128, 128);
		loadingBarColorIndex--;
		if (loadingBarColorIndex < 0) {
			loadingBarColorIndex = loadingBarColors.length - 1;
		}

		loadingBarColors[loadingBarColorIndex] = 200;

		for (int i = 0; i < 12; i++) {
			gSetColor(loadingBarColors[i], 0, 0);
			loadingBarColors[i] = loadingBarColors[i] * 80 / 100;
			gFillArc(32, 32, 64, 64, i * 30, 20);
		}

		gSetColor(0, 0, 0);
		gFillArc(43, 43, 42, 42, 0, 360);
		
		gfxFlush();
	}

	public static final void levelRenderLine(int var0, int var1, int var2, int var3) {
		int var4 = levelAlignX(var0, var1);
		int var5 = levelAlignY(var0, var1);
		int var6 = levelAlignX(var2, var3);
		int var7 = levelAlignY(var2, var3);
		gDrawLine(var4, var5, var6, var7);
	}

	public static final void setGammaColor(int c) {
		setGammaColor(c >> 16 & 255, c >> 8 & 255, c & 255);
	}

	public static final void setGammaColor(int r, int g, int b) {
		isGammaColorBlack = false;
		if (r + g + b == 0) {
			isGammaColorBlack = true;
		}

		if (gamma == 100) {
			gSetColor(r, g, b);
		} else {
			int gm = gamma;
			if (gm > 100) {
				gSetColor(r - r * (200 - gm) / 100,
						g - g * (200 - gm) / 100,
						b - b * (200 - gm) / 100);
			} else {
				gSetColor(r + (255 - r) * (100 - gm) / 100,
						g + (255 - g) * (100 - gm) / 100,
						b + (255 - b) * (100 - gm) / 100);
			}
		}
	}

	public static final void levelDrawRect(int var0, int var1, int var2, int var3) {
		if (isGammaColorBlack) {
			int var4 = 0x20000;
			var4 = var4 * 100 / levelCameraZoom;
			var2 += var4;
			var3 += var4;
		}

		int var13 = levelAlignX(var0 - var2, var1 - var3);
		int var5 = levelAlignY(var0 - var2, var1 - var3);
		int var6 = levelAlignX(var0 + var2, var1 - var3);
		int var7 = levelAlignY(var0 + var2, var1 - var3);
		int var8 = levelAlignX(var0 - var2, var1 + var3);
		int var9 = levelAlignY(var0 - var2, var1 + var3);
		int var10 = levelAlignX(var0 + var2, var1 + var3);
		int var11 = levelAlignY(var0 + var2, var1 + var3);
		if (var13 <= 128 || var8 <= 128) {
			if (var5 <= 128 || var7 <= 128) {
				if (var10 >= 0 || var6 >= 0) {
					if (var11 >= 0 || var9 >= 0) {
						gFillTriangle(var8, var9, var13, var5, var6, var7);
						gFillTriangle(var6, var7, var10, var11, var8, var9);
					}
				}
			}
		}
	}

	public static final void levelRenderImage(Image var0, int var1, int var2) {
		int var3 = levelAlignX(var1, var2);
		int var4 = levelAlignY(var1, var2);
		gDrawImage(var0, var3 - var0.getWidth() / 2, var4 - var0.getHeight() / 2, 0);
	}

	public static final void sceneSelectionRenderCircle(int var0, int var1, int var2, int var3) {
		setGammaColor(0);
		levelRenderCircle(var0, var1, var2);
		setGammaColor(var3);
		levelRenderCircle(var0, var1, var2);
	}

	public static final void levelRenderCircle(int var0, int var1, int var2) {
		if (var2 >= 35000) {
			int var3 = levelAlignX(var0, var1);
			int var4 = levelAlignY(var0, var1);
			var2 >>= 16;
			var2 = levelCameraZoom * var2 / 100;
			if (isGammaColorBlack) {
				var2 += 2;
			}

			if (var3 > -var2 && var3 < 128 + var2 && var4 > -var2 && var4 < 128 + var2) {
				gFillArc(var3 - var2, var4 - var2, var2 + var2, var2 + var2, 0, 360);
			}
		}
	}

	public static final void levelRenderCritterShock(int var0, int var1, int var2, int var3) {
		int var4 = levelAlignX(var0, var1);
		int var5 = levelAlignY(var0, var1);
		int var6 = levelAlignX(var2, var3);
		int var7 = levelAlignY(var2, var3);
		renderCritterShock(var4, var5, var6, var7, 8, 6);
	}

	public static final void levelRenderCircleOutline(int var0, int var1, int var2) {
		int var3 = levelAlignX(var0, var1);
		int var4 = levelAlignY(var0, var1);
		var2 >>= 16;
		var2 = levelCameraZoom * var2 / 100;
		if (isGammaColorBlack) {
			var2 += 2;
		}

		if (var3 > -var2 && var3 < 128 + var2 && var4 > -var2 && var4 < 128 + var2) {
			gDrawArc(var3 - var2, var4 - var2, var2 + var2, var2 + var2, 0, 360);
		}
	}

	public static final boolean isOutOfScreenBounds(int var0, int var1, int var2, int var3) {
		return var0 > 128 || var2 < 0 || var1 > 128 || var3 < 0;
	}

	public static final void initInsideShip() {
		if (imgWindow == null) {
			imgWindow = loadImage("window.pim", "window.ppl");
		}

		if (imgArrowLeft == null) {
			imgArrowLeft = loadImage("arrow_left.pim", "arrow_left.ppl");
		}

		if (imgArrowRight == null) {
			imgArrowRight = loadImage("arrow_right.pim", "arrow_right.ppl");
		}

		if (imgsInside == null) {
			imgsInside = new Image[3];

			for (int i = 0; i < 3; i++) {
				imgsInside[i] = loadImage("inside" + i + ".pim", "inside" + i + ".ppl");
			}
		}

		if (imgInsideLamp == null) {
			imgInsideLamp = loadImage("inside_lamp.pim", "inside_lamp.ppl");
		}

		if (imgsPiston == null) {
			imgsPiston = new Image[2];
			imgsPiston[0] = loadImage("piston_top.pim", "piston_top.ppl");
			imgsPiston[1] = loadImage("piston_bottom.pim", "piston_bottom.ppl");
		}

		if (imgsDecor == null) {
			imgsDecor = new Image[10];

			for (int i = 0; i < imgsDecor.length; i++) {
				imgsDecor[i] = loadImage("shipDecor" + i + ".pim", "shipDecor" + i + ".ppl");
			}

			decorBackground = loadFile16("decor_background.bin");
			decorForeground = loadFile16("decor_foreground.bin");
		}

		if (imgsShipIcons == null) {
			imgsShipIcons = new Image[9];

			for (int i = 0; i < 9; i++) {
				imgsShipIcons[i] = loadImage("shipicon" + i + ".pim", "shipicon" + i + ".ppl");
			}
		}

		if (imgsShipIconsOff == null) {
			imgsShipIconsOff = new Image[9];

			for (int i = 0; i < 9; i++) {
				imgsShipIconsOff[i] = loadImage("shipicon" + i + "b.pim", "shipicon" + i + "b.ppl");
			}
		}
	}

	public static final void initShip() {
		initSpace(2, 12, 92, 46);
		gamma = 100;
		loadLevel(-2);
		initInsideShip();
		levelMovableGravity = 40000;
		levelSetCamera(levelCircleX[0], levelHeight / 2, 0);
	}

	public static final int processMainMenuDialogue(int var0, boolean var1) {
		byte var2 = 0;
		switch(var0) {
			case 1:
				if (!var1) {
					startDialogue("start" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					var2 = 1;
				} else {
					var2 = 6;
				}
				break;
			case 2:
				if (var1) {
					startDialogue("menu_sure_restart.bms", levelCircleX[0], levelCircleY[0], true);
					var2 = 3;
				} else {
					startDialogue("menu_no_restart.bms", levelCircleX[0], levelCircleY[0]);
				}
				break;
			case 3:
				if (var1) {
					startDialogue("menu_no_holodeck.bms", levelCircleX[0], levelCircleY[0]);
				} else {
					startDialogue("menu_enter_holodeck.bms", levelCircleX[0], levelCircleY[0]);
					var2 = 4;
				}
				break;
			case 4:
				setNewState(8, 0);
				break;
			case 5:
				if (isAudioEnabled) {
					startDialogue("menu_audio_off.bms", levelCircleX[0], levelCircleY[0]);
					queueAllSoundsForCleanup();
					queueSoundCleanup();
				} else {
					startDialogue("menu_audio_on.bms", levelCircleX[0], levelCircleY[0]);
				}

				isAudioEnabled = !isAudioEnabled;
				break;
			case 6:
				if (isVibraEnabled) {
					startDialogue("menu_vibra_off.bms", levelCircleX[0], levelCircleY[0]);
				} else {
					startDialogue("menu_vibra_on.bms", levelCircleX[0], levelCircleY[0]);
				}

				isVibraEnabled = !isVibraEnabled;
				saveRecordData();
				vibrate(640);
			case 7:
			default:
				break;
			case 8:
				startDialogue("menu_sure_exit.bms", levelCircleX[0], levelCircleY[0], true);
				var2 = 2;
				break;
			case 9:
				startDialogue("menu_sure_reset.bms", levelCircleX[0], levelCircleY[0], true);
				var2 = 5;
		}

		return var2;
	}

	public static final void renderShipBlobs(boolean var0) {
		isTextRendered = true;
		renderShipInside();
		isTextRendered = false;
		if (!var0) {
			renderPlayable(6, 0xc80000, true);
		}

		levelRenderPlayer();
		renderShipDecor(decorForeground);
		renderShipArrows();
	}

	public static final void renderShipArrows() {
		if (levelCircleX[0] > 0x6e0000) {
			gDrawImage(imgArrowLeft, 4, 88, 0);
		}

		if (levelCircleX[0] < 0x41a0000) {
			gDrawImage(imgArrowRight, 107, 88, 0);
		}
	}

	public static final void renderShipDecor(short[] decorData) {
		if (decorData != null) {
			boolean b = false;
			int shiftX = 64 - levelCameraZoom * levelCameraIntX / 100;

			for (int i = 0; i < decorData.length; i += 3) {
				Image img = imgsDecor[decorData[i]];
				if (img != null) {
					int x = shiftX + levelAlignToGameMirror(decorData[i + 1]) - img.getWidth() / 2;
					int y = img.getWidth();
					if (x > -y && x < 128 + y) {
						gDrawImage(img, x, -16 + decorData[i + 2] - img.getHeight() / 2, 0);
					}
				}
			}
		}
	}

	public static final void renderShipInside() {
		int var1;
		int var2;
		if ((var2 = var1 = 64 - levelCameraZoom * levelCameraIntX / 100) > 0) {
			var2 = 0;
		}

		gDrawImage(imgsInside[0], var2, -16, 0);
		var2 += 28;

		for (int var3 = 0; var3 < 17; var3++) {
			if (var2 + 46 > 0 && var2 <= 128) {
				gDrawImage(imgsInside[1], var2, -16, 0);
			}

			var2 += 46;
		}

		if (var2 <= 128) {
			gDrawImage(imgsInside[2], var2, -16, 0);
		}

		int var4 = var1 + levelAlignToGameMirror(193) * 72 / 100;
		gDrawImage(imgInsideLamp, var4 - imgInsideLamp.getWidth() / 2, 2, 0);
		renderWindow(var1 + 299, 30, 0);
		renderWindow(var1 + 553, 30, 1);
		renderShipDecor(decorBackground);
		if (shipAlarmRadius > 0) {
			int var12 = 2 + imgInsideLamp.getHeight() / 2;
			gSetColor(0xff0000);
			int var5 = shipAlarmRadius;
			if (var4 > -var5 && var4 < 128 + var5 && var12 > -var5 && var12 < 128 + var5) {
				for (int var6 = 0; var6 < 2; var6++) {
					for (int var7 = 20; var7 < 360; var7 += 90) {
						gDrawArc(var4 - var5, var12 - var5, var5 + var5, var5 + var5, var7, 50);
					}

					var5--;
				}
			}

			shipAlarmRadius += 2;
			if (shipAlarmRadius > 30) {
				shipAlarmRadius = 5;
			}
		}

		for (int var13 = 0; var13 < levelNumCircles; var13++) {
			if ((levelCircleFlags[var13] & 4) > 0 && levelCircleHasPhysics[var13] && levelCircleType[var13] > 0) {
				int var14 = 72 * (levelCircleX[var13] / 100) >> 16;
				int var15 = 72 * (levelCircleY[var13] / 100) >> 16;
				Image var8;
				gDrawImage(var8 = imgsPiston[1], var1 + var14 - var8.getWidth() / 2, -16 + var15 - var8.getHeight(), 0);
				gDrawImage(var8 = imgsPiston[0], var1 + var14 - var8.getWidth() / 2, -16, 0);
				int var9 = levelCircleType[var13] - 1;
				var8 = imgsShipIcons[var9];
				switch(var9) {
					case 4:
						if (!isAudioEnabled) {
							var8 = imgsShipIconsOff[var9];
						}
						break;
					case 5:
						if (!isVibraEnabled) {
							var8 = imgsShipIconsOff[var9];
						}
				}

				gDrawImage(var8, var1 + var14 - var8.getWidth() / 2, -16 + var15 - var8.getHeight() / 2, 0);
				if (isTextRendered && var1 + var14 > -100 && var1 + var14 < 228) {
					String var10 = textTableShip[var9];
					if (isShipPaused) {
						var10 = textTableShipPause[var9];
					}

					renderText(var1 + var14 - getStringWidth(var10, 3) / 2, 89, var10, 3);
				}
			}
		}
	}

	public static final void renderWindow(int var0, int var1, int var2) {
		int var3 = imgWindow.getWidth();
		int var4 = imgWindow.getHeight();
		var0 -= var3 / 2;
		gSetColor(0);
		gSetClip(var0, var1, var3, var4);
		gFillRect(var0, var1, var3, var4);
		gSetColor(0xffffff);
		renderSpace(var2, var0 - var3 + var3 * var0 / 128, var1);
		if (var0 >= -var3 && var0 <= 128) {
			gDrawImage(imgWindow, var0, var1, 0);
		}

		gSetClip(0, 0, 128, 128);
	}

	public static final void renderSplash() {
		if (imgSplashShip == null) {
			imgSplashShip = loadImage("outside.pim", "outside.ppl");
		}

		if (imgsFlame == null) {
			imgsFlame = new Image[2];
			imgsFlame[0] = loadImage("flame0.pim", "flame0.ppl");
			imgsFlame[1] = loadImage("flame1.pim", "flame1.ppl");
		}

		gSetColor(0);
		gFillRect(0, 0, 128, 128);
		renderSpace(0, 0, 0, true);
		gDrawImage(imgSplashShip, 33, 49, 0);
		gDrawImage(imgsFlame[(int)(millis() / 100L % 2L)], 30, 62, 0);
	}

	public static final void initSpaceMap(int var0, int var1, int var2) {
		spaceMapCameraX = 0;
		spaceMapCameraY = 0;
		spaceMapTargetCameraX = 0;
		spaceMapTargetCameraY = 0;
		spaceMapShipX = 0;
		spaceMapShipY = 0;
		spaceMapShowShip = true;
		spaceMapPlanetX = var0;
		spaceMapPlanetY = var1;
		spaceMapPlanetBeaconRadius = 1;
		if (var2 >= 0) {
			if (imgPlanet == null) {
				imgPlanet = loadImage("planet.pim", "planet.ppl");
			}

			spaceMapShowPlanet = true;
		} else {
			spaceMapShowPlanet = false;
		}

		if (imgShip == null) {
			imgShip = loadImage("ship.pim", "ship.ppl");
		}

		if (imgPointer == null) {
			imgPointer = loadImage("pointer.pim", "pointer.ppl");
		}
	}

	public static final void spaceMapSetCameraToBeacon() {
		spaceMapSetCameraInstant(spaceMapPlanetX, spaceMapPlanetY);
	}

	public static final void spaceMapSetCameraInstant(int x, int y) {
		spaceMapCameraX = x << 8;
		spaceMapCameraY = y << 8;
		spaceMapTargetCameraX = spaceMapCameraX;
		spaceMapTargetCameraY = spaceMapCameraY;
	}

	public static final void spaceMapSetTargetCameraToBeacon() {
		spaceMapSetTargetCamera(spaceMapPlanetX, spaceMapPlanetY);
	}

	public static final void spaceMapSetTargetCamera(int x, int y) {
		spaceMapTargetCameraX = (x << 8) + 128;
		spaceMapTargetCameraY = (y << 8) + 128;
	}

	public static final boolean renderSpaceMapNextFrame() {
		spaceMapCameraX += (spaceMapTargetCameraX - spaceMapCameraX) / 10;
		spaceMapCameraY += (spaceMapTargetCameraY - spaceMapCameraY) / 10;
		return (spaceMapCameraX & 0xffff00) == (spaceMapTargetCameraX & 0xffff00) &&
			(spaceMapCameraY & 0xffff00) == (spaceMapTargetCameraY & 0xffff00);
	}

	public static final void renderSpaceMap() {
		gSetColor(0);
		gFillRect(0, 0, 128, 128);

		int camX = -(spaceMapCameraX >> 8);
		int camY = -(spaceMapCameraY >> 8);
		renderSpace(0, camX - 64, camY - 64, false);

		int posX = spaceMapShipX + camX + 64;
		int posY = spaceMapShipY + camY + 64;

		gSetColor(0xffffff);
		renderStar(posX, posY);

		if (spaceMapShowShip) {
			gDrawImage(imgPointer, posX - 16, posY - 58 - 2, 0);
			gDrawImage(imgShip, posX - imgShip.getWidth() / 2, posY - 58 + (33 - imgShip.getHeight()) / 2 - 2, 0);
		}

		posX = spaceMapPlanetX + camX + 64;
		posY = spaceMapPlanetY + camY + 64;
		renderStar(posX, posY);

		if (spaceMapPlanetBeaconRadius > 0) {
			gSetColor(0xff0000);
			int rad = spaceMapPlanetBeaconRadius;

			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 20, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 110, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 200, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 290, 50);

			if (spaceMapPlanetBeaconRadius++ > 20) {
				spaceMapPlanetBeaconRadius = 1;
			}
		}

		if (spaceMapShowPlanet) {
			gDrawImage(imgPointer, posX - 16, posY - 58 - 2, 0);
			gDrawImage(imgPlanet, posX - imgPlanet.getWidth() / 2, posY - 58 + (33 - imgPlanet.getHeight()) / 2 - 2, 0);
		}
	}

	public static final void renderBeamAnimation(int frame) {
		updateAll();
		renderShipInside();

		if (frame < 100) {
			gamma = frame;
			levelCameraZoom = frame * 72 / 100;

			if (levelCameraZoom == 0) {
				levelCameraZoom = 1;
			}

			renderPlayable(6, 0xc80000, true);
			gamma = 100;
			levelCameraZoom = 72;
		} else {
			renderPlayable(6, 0xc80000, true);
		}

		levelRenderPlayer();
		renderShipDecor(decorForeground);
		refreshGame();
	}

	public static final void initVictory() {
		victoryCameraX = levelCircleX[0];
		victoryCameraY = levelCircleY[0];

		for (int var0 = 0; var0 < 5; var0++) {
			levelCirclePrevY[var0] = levelCircleY[var0];
		}

		if (levelPlayerCurrentGrabberFlags >= 0) {
			levelPlayerReleaseGrabber();
		}

		playJingleComplete();
		victoryCameraZoom = levelCameraZoom;
		levelCompleteTicks = 150;
		initConfetti();
		if (textMission == null) {
			textMission = getText(0x50000);
			textCompleted = getText(0x50001);
		}
	}

	public static final boolean renderMissionComplete() {
		levelCameraApproach(victoryCameraX, victoryCameraY, 0);
		gamma = 100;
		levelCameraZoom = victoryCameraZoom;
		levelRender(false);
		if (levelCompleteTicks < 100) {
			if (levelCompleteTicks % 4 == 0) {
				victoryCameraZoom++;
			}

			gamma = levelCompleteTicks;
		}

		if (levelCompleteTicks > 0) {
			if (levelCompleteTicks < 50) {
				levelCameraZoom = levelCompleteTicks * 72 * 2 / 100;

				for (int var0 = 0; var0 < 5; var0++) {
					int var1 = (100 - levelCompleteTicks * 2) * (100 - levelCompleteTicks * 2) * (100 - levelCompleteTicks * 2) * (100 - levelCompleteTicks * 2);
					int[] var10000 = levelCircleY;
					var10000[var0] -= var1 >> 2;
					var10000 = levelCircleX;
					var10000[var0] += var1 * cos((50 - levelCompleteTicks) * 45) / 2000;
				}

				levelCompleteTicks++;
			}

			levelRenderPlayer();
		}

		renderConfetti();
		int var2 = 90;
		if (levelCompleteTicks > 105) {
			var2 = (150 - levelCompleteTicks) * 2;
		}

		renderText((128 - getStringWidth(textMission, 0)) / 2 - cos1000[var2] * 128 / 1000, 32, textMission, 0);
		renderText((128 - getStringWidth(textCompleted, 0)) / 2 + cos1000[var2] * 128 / 1000, 43, textCompleted, 0);
		refreshGame();
		levelCompleteTicks -= 2;
		if (softkeyPressed(2, -1) == 2) {
			levelCompleteTicks = -38;
		}

		if (levelCompleteTicks < -40) {
			setNewState(6, 0);
			isBeamAnimated = true;
			return true;
		} else {
			return false;
		}
	}

	public static final int levelAlignX(int var0, int var1, int var2) {
		var0 /= 100000 / levelCameraZoom;
		var1 /= 100000 / levelCameraZoom;
		return cos1000[var2] * var0 - sin1000[var2] * var1;
	}

	public static final int levelAlignY(int var0, int var1, int var2) {
		var0 /= 100000 / levelCameraZoom;
		var1 /= 100000 / levelCameraZoom;
		return sin1000[var2] * var0 + cos1000[var2] * var1;
	}

	public static final int sin(int deg) {
		while (deg < 0) {
			deg += 360;
		}

		while (deg >= 360) {
			deg -= 360;
		}

		return sin1000[deg];
	}

	public static final int cos(int deg) {
		while (deg < 0) {
			deg += 360;
		}

		while (deg >= 360) {
			deg -= 360;
		}

		return cos1000[deg];
	}

	public static final void loadTrigonometric() {
		if (sin1000 == null) {
			sin1000 = loadFile16("sin1000.bin");
		}

		if (cos1000 == null) {
			cos1000 = loadFile16("cos1000.bin");
		}
	}

	public static final void loadStatic() {
		if (imgsStatic == null) {
			imgsStatic = new Image[4];

			for (int var0 = 0; var0 < 4; var0++) {
				imgsStatic[var0] = loadImage("static" + var0 + ".pim", "static" + var0 + ".ppl");
			}
		}

		if (staticTiles == null) {
			staticTiles = new int[42];
		}
	}

	public static final void renderTransmission() {
		loadStatic();
		gSetColor(pingBackgroundColor);
		gFillRect(0, 0, 128, 128);
		if (isTransmissionShaky) {
			renderPurple(currentPurpleX + sin1000[(int)(millis() / 5L % 360L)] * 12 / 1000, currentPurpleY + sin1000[(int)(millis() / 3L % 360L)] * 12 / 1000, currentPurpleSize + 40 + sin1000[(int)(millis() / 14L % 360L)] * 20 / 1000);
		} else {
			renderPurple(currentPurpleX, currentPurpleY, currentPurpleSize);
		}

		if (isTransmodigrafierMissing) {
			int var0 = 0;

			for (int var1 = 0; var1 <= 128; var1 += 23) {
				for (int var2 = -rand8() % 30; var2 <= 128; var2 += 23) {
					int var3;
					for (var3 = rand8() % 4; var3 == staticTiles[var0]; var3 = rand8() % 4) {
					}

					staticTiles[var0] = var3;
					gDrawImage(imgsStatic[var3], var2, var1, 0);
					var0++;
				}
			}
		}

		if (isShowingShardPicture) {
			gSetColor(0xffffff);
			gFillRect(0, 0, 128, 10);
			gFillRect(0, 0, 10, 128);
			gFillRect(118, 0, 10, 128);
			gFillRect(0, 98, 128, 30);
			gSetColor(0);
			gDrawRect(10, 10, 107, 87);
			if (activePurpleShardNameId >= 0) {
				renderText(-1000, 106, purpleShardNames[activePurpleShardNameId], 1);
			}
		}
	}

	public static final void renderPurple(int var0, int var1, int var2) {
		int var3 = var2 * 40 / 100;
		int var4 = var2 * 93 / 100;
		gSetColor(0);
		gFillArc(var0 - var3 / 2 - 2, var1 - var4 / 2 - 2, var3 + 2 + 2, var4 + 2 + 2, 0, 360);
		if (isTransmodigrafierMissing) {
			gSetColor(0x696969);
		} else {
			gSetColor(0xa020f0);
		}

		gFillArc(var0 - var3 / 2, var1 - var4 / 2, var3, var4, 0, 360);
		gSetColor(0);
		int var5 = var2 * 23 / 100;
		int var6 = var2 * 5 / 100;
		if (isSpeakingAnimationPlaying && !isShowingShardPicture && (millis() / 150L & 1L) > 0L) {
			gFillArc(var0 - var5 / 2, var1 - var6 * 2, var5, var6 * 2, 0, 360);
		} else {
			gFillArc(var0 - var5 / 2, var1 - var6, var5, var6, 0, 360);
		}

		gSetColor(0xffffff);
		int var7 = var2 * 14 / 100;
		int var8 = var2 * 6 / 100;

		for (int var9 = 0; var9 < 3; var9++) {
			gFillArc(var0 - var7 * 3 / 2 + var9 * var7, var1 - var4 * 45 / 100, var7, var7, 0, 360);
		}

		gSetColor(0);

		for (int var10 = 0; var10 < 3; var10++) {
			gFillArc(var0 - var7 * 3 / 2 + var10 * var7 + var7 / 3, var1 - var4 * 45 / 100 + var7 / 3, var8, var8, 0, 360);
		}

		if (isTransmissionBlinked || !isShowingShardPicture && rand8() > 240) {
			if (isTransmodigrafierMissing) {
				gSetColor(0x696969);
			} else {
				gSetColor(0xa020f0);
			}

			for (int var11 = 0; var11 < 3; var11++) {
				gFillArc(var0 - var7 * 3 / 2 + var11 * var7, var1 - var4 * 45 / 100, var7, var7, 0, 360);
			}

			isTransmissionBlinked = !isTransmissionBlinked;
		}
	}

	public static final void sceneSelectionCleanup() {
		imgsSelectionMenu = null;
		imgsSelectionMenuArrows = null;
	}

	public static final void loadSelectionOptions(String[] imageFiles, int var1) {
		numSelections = imageFiles.length;
		selectionAngleBetweenOthers = 360 / numSelections;
		selectionAngleUntilAdjust = selectionAngleBetweenOthers / 2;
		imgsSelectionMenu = new Image[numSelections];

		for (int var2 = 0; var2 < numSelections; var2++) {
			imgsSelectionMenu[var2] = loadImage(imageFiles[(var2 + var1) % numSelections] + ".pim", imageFiles[(var2 + var1) % numSelections] + ".ppl");
		}

		imgsSelectionMenuArrows = new Image[2];
		imgsSelectionMenuArrows[0] = loadImage("arrow_left.pim", "arrow_left.ppl");
		imgsSelectionMenuArrows[1] = loadImage("arrow_right.pim", "arrow_right.ppl");
		levelInitCircles(10 + numSelections * 2 + 1);
		levelInitHooks(numSelections);
		activeSwapKey = -10;
		levelMovableGravity = 30000;
		levelXLossRate = 64000;
		selectionAngle = 0;
		selectionMainCircleId = 10 + numSelections * 2;
		selectionDefaultIndex = var1;

		for (int var5 = 0; var5 < numSelections; var5++) {
			int var3 = 5242 * cos(90 + var5 * selectionAngleBetweenOthers);
			int var4 = 5242 * sin(90 + var5 * selectionAngleBetweenOthers);
			levelSetCircle(var5 + 10, var3, var4, 0x50000, 200, 0, 0, false);
			var3 = 6881 * cos(90 + var5 * selectionAngleBetweenOthers);
			var4 = 6881 * sin(90 + var5 * selectionAngleBetweenOthers);
			levelSetCircle(var5 + 10 + numSelections, var3, var4, 0x190000, 100, 3, 0, true);
		}

		levelSetCircle(selectionMainCircleId, 0, 0, 0x500000, 200, 2, 0, false);

		for (int var6 = 0; var6 < numSelections; var6++) {
			levelSetHook(var6, var6 + 10, var6 + 10 + numSelections, 4, 0x3c0000, 65000, true, true);
		}

		levelSetCamera(levelCircleX[selectionMainCircleId], levelCircleY[selectionMainCircleId] + 0x640000, 0);
		selectionState = 0;
		selectionStateStartMs = millis();
	}

	public static final int getSelectedIndex() {
		if (selectionState == 1 && softkeyPressed(2, -1, true) == 2) {
			selectionState++;
			selectionStateStartMs = millis() + 500L;
		}

		if (isKeyHeld(8)) {
			selectionAngle += 4;
		} else if (isKeyHeld(16)) {
			selectionAngle -= 4;
		} else {
			int var0 = selectionAngle % selectionAngleBetweenOthers;
			if (var0 > selectionAngleUntilAdjust) {
				selectionAngle += 2;
			} else if (var0 > 0) {
				selectionAngle -= 2;
			}
		}

		if (selectionAngle >= 360) {
			selectionAngle -= 360;
		}

		if (selectionAngle < 0) {
			selectionAngle += 360;
		}

		for (int var3 = 0; var3 < numSelections; var3++) {
			int var1 = 5242 * cos(selectionAngle + 90 + var3 * selectionAngleBetweenOthers);
			int var2 = 5242 * sin(selectionAngle + 90 + var3 * selectionAngleBetweenOthers);
			levelCircleX[var3 + 10] = var1;
			levelCircleY[var3 + 10] = var2;
		}

		updateLevel();
		if (selectionState == 0) {
			gamma = (int)((millis() - selectionStateStartMs) / 5L);
		}

		if (selectionState == 2) {
			gamma = (int)((selectionStateStartMs - millis()) / 20L);
		}

		if (gamma < 0) {
			gamma = 0;
			selectionState++;
		}

		if (gamma > 100) {
			gamma = 100;
			selectionState++;
		}

		setGammaColor(0xdddddd);
		gFillRect(0, 0, 128, 128);
		sceneSelectionRenderCircle(levelCircleX[selectionMainCircleId], levelCircleY[selectionMainCircleId], levelCircleRadius[selectionMainCircleId], 0xaaaaaa);
		sceneSelectionRenderCircle(levelCircleX[selectionMainCircleId], levelCircleY[selectionMainCircleId], levelCircleRadius[selectionMainCircleId] / 2, 0xdddddd);
		setGammaColor(0);

		for (int var4 = 10; var4 < 10 + numSelections; var4++) {
			levelRenderLine(levelCircleX[var4], levelCircleY[var4], levelCircleX[var4 + numSelections], levelCircleY[var4 + numSelections]);
		}

		for (int var5 = 0; var5 < numSelections; var5++) {
			levelRenderCircle(levelCircleX[var5 + 10 + numSelections], levelCircleY[var5 + 10 + numSelections], levelCircleRadius[var5 + 10 + numSelections]);
			if (gamma > 20) {
				levelRenderImage(imgsSelectionMenu[var5], levelCircleX[var5 + 10 + numSelections], levelCircleY[var5 + 10 + numSelections]);
			}
		}

		setGammaColor(0x888888);

		for (int var6 = 0; var6 < 5; var6++) {
			int var9 = 3932 * cos(selectionAngle + 90 - 36 + var6 * 72);
			int var10 = 3932 * sin(selectionAngle + 90 - 36 + var6 * 72);
			levelRenderCircle(var9, var10, 0x50000);
		}

		if (gamma > 50) {
			selectionArrowsAnimTicks += 12;
			if (selectionArrowsAnimTicks > 179) {
				selectionArrowsAnimTicks -= 180;
			}

			int var7 = 1 + sin1000[selectionArrowsAnimTicks] * 6 / 1000;
			gDrawImage(imgsSelectionMenuArrows[0], var7, 85 - imgsSelectionMenuArrows[0].getHeight() / 2, 0);
			gDrawImage(imgsSelectionMenuArrows[1], 128 - var7 - imgsSelectionMenuArrows[1].getWidth(), 85 - imgsSelectionMenuArrows[1].getHeight() / 2, 0);
		}

		refreshGame();
		if (selectionState == 3) {
			int var8 = (360 - selectionAngle) / selectionAngleBetweenOthers + selectionDefaultIndex;
			if (selectionAngle % selectionAngleBetweenOthers > selectionAngleUntilAdjust) {
				var8++;
			}

			if (var8 >= numSelections) {
				var8 -= numSelections;
			}

			if (var8 < 0) {
				var8 += numSelections;
			}

			return var8;
		} else {
			return -1;
		}
	}
}
