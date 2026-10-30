import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.rms.*;
import com.nokia.mid.sound.Sound;

public final class Game extends GameCanvas implements Runnable {
	public static Game instance;
	public static PMMIDlet midlet;

	// Keys.
	private static final int KEY_ALL =				-1;
	private static final int KEY_UNKNOWN =			(1 << 24);
	private static final int KEY_OK =				(1 << 0);
	private static final int KEY_0 =				(1 << 1);
	private static final int KEY_BACK =				(1 << 2);
	private static final int KEY_LEFT =				(1 << 3);
	private static final int KEY_RIGHT =			(1 << 4);
	private static final int KEY_UP =				(1 << 5);
	private static final int KEY_DOWN =				(1 << 6);
	private static final int KEY_TOP_LEFT =			(1 << 7);
	private static final int KEY_TOP_RIGHT =		(1 << 8);
	private static final int KEY_BOTTOM_LEFT =		(1 << 9);
	private static final int KEY_BOTTOM_RIGHT =		(1 << 10);
	private static final int KEY_SOFT_LEFT =		(1 << 11);
	private static final int KEY_SOFT_RIGHT =		(1 << 12);
	public static int heldKeys = 0;
	public static int heldPrevKeys = 0;
	public static int tmpHeldKeys = 0;
	/* Keys that are presented in this field will first be registered by the
	 * `updateKeys()`, and then unmasked from `heldKeys` in the next
	 * `updateKeys()` call even if the key is still held physically. */
	public static int pressOnlyKeys = 0;
	public static final short[][] KEYMAP = new short[][] {
		{'5', -6, -5},	// 0
		{'0'},			// 1
		{-7},			// 2
		{'4', -3},		// 3
		{'6', -4},		// 4
		{'2', -1},		// 5
		{'8', -2},		// 6
		{'1'},			// 7
		{'3'},			// 8
		{'7'},			// 9
		{'9'},			// 10
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
	private static int numActiveSoundsMidi;
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
	private static Sound[] soundPlayersOtt;
	private static Player[] soundPlayers;
	private static Player[] soundPlayersMidi;

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
	private static final int FONT_MAX_CHARS = 230;
	public static Image[] fontImages;
	public static int[] fontAverageWidths;
	public static int[] fontSpacesPerChars;
	public static int[] fontHeights;
	public static int[] fontLineGaps;
	public static short[][] fontCharOffsets;
	public static byte[][] fontCharWidths;
	public static short[][] fontCharMap;

	private static final int SCREEN_WIDTH = 240;
	private static final int SCREEN_HEIGHT = 320;
	public static Graphics gfx;
	private static long randSeed;

	// States.
	private static final int STATE_EXEC_INIT = 0;
	private static final int STATE_EXEC_RUN = 1;
	private static final int STATE_EXEC_FREE = 2;
	private static final int STATE_ID_SELECTION = 0;
	private static final int STATE_ID_SPLASH = 1;
	private static final int STATE_ID_SHIP = 2;
	private static final int STATE_ID_PRE_LEVEL = 3;
	private static final int STATE_ID_LEVEL = 4;
	private static final int STATE_ID_SCRIPT = 5;
	private static final int STATE_ID_TRANSITION = 6;
	private static final int STATE_ID_TITLE = 7;
	private static final int STATE_ID_HELP = 8;
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
		0x8c7853,
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
	public static int splashRocketX;
	public static int splashRocketY;
	public static int splashTick;
	public static long splashDuration;
	public static int splash = 0;

	public static boolean showControlsGuide;

	// Ship scene.
	private static final int SHIP_OP_NOP = 0;
	private static final int SHIP_OP_START = 1;
	private static final int SHIP_OP_EXIT = 2;
	private static final int SHIP_OP_RESTART = 3;
	private static final int SHIP_OP_HOLODECK = 4;
	private static final int SHIP_OP_RESET = 5;
	private static final int SHIP_OP_RESUME = 6;
	/* Temporary field to check if the player touched the pod and keeping a
	 * contact with it, or the player already landed on the floor. */
	public static int shipCheckPodId = 0;
	public static int shipPodChoiceId = 0;
	public static int shipNextOp = SHIP_OP_NOP;

	// Script (cutscene) scene.
	private static final int SCRIPT_DIALOGUE_BOX_SLIDE_MS = 400;
	private static final int SCRIPT_CMD_SUBMIT = 0;
	private static final int SCRIPT_CMD_WAIT = 1;
	private static final int SCRIPT_CMD_WAIT_OK = 2;
	private static final int SCRIPT_CMD_SET_CAMERA = 3;
	private static final int SCRIPT_CMD_SET_CAMERA_SMOOTH = 4;
	private static final int SCRIPT_CMD_SET_TEXT = 5;
	private static final int SCRIPT_CMD_SET_FACE = 6;
	private static final int SCRIPT_CMD_SET_ENTITY_POS = 7;
	private static final int SCRIPT_CMD_SET_STATE = 8;
	private static final int SCRIPT_CMD_SET_ENV = 9;
	private static final int SCRIPT_CMD_HIDE_BOX = 10;
	private static final int SCRIPT_CMD_SET_BARS = 11;
	private static final int SCRIPT_CMD_SET_ALARM = 12;
	private static final int SCRIPT_CMD_SPACE_MAP_SHOW_POINTER = 13;
	private static final int SCRIPT_CMD_TRANSMISSION_SET_NOISE = 14;
	private static final int SCRIPT_CMD_TRANSMISSION_SET_SHAKE = 15;
	private static final int SCRIPT_CMD_PHOTO_FRAME_SHOW = 16;
	private static final int SCRIPT_CMD_TRANSMISSION_SET_BACKGROUND_COLOR = 17;
	private static final int SCRIPT_CMD_RESERVED = 18;
	private static final int SCRIPT_CMD_PHOTO_FRAME_SET_LABEL_ID = 19;
	private static final int SCRIPT_ENV_SHIP = 0;
	private static final int SCRIPT_ENV_LEVEL = 1;
	private static final int SCRIPT_ENV_RESERVED = 2;
	private static final int SCRIPT_ENV_SPACE_MAP = 3;
	private static final int SCRIPT_ENV_OUTSIDE = 4;
	private static final int SCRIPT_ENV_TRANSMISSION = 5;
	public static boolean scriptIsAwaitingLevelStart = false;
	public static int beamAnimationTicks = 1;
	public static boolean skipPrologue;
	public static int[] currentScript;
	public static int currentScriptIndex;
	public static boolean scriptSelectionEnabled;
	public static boolean scriptIsActionConfirmed;
	public static int scriptLastSwapKey;
	public static int stateBeforeScript;
	public static boolean scriptIsContinued;
	public static int scriptCameraZoomApproach;
	public static long scriptWaitEndMs;
	public static boolean isSpeakingAnimationPlaying;
	public static boolean scriptIsOkKeyEnabled;
	public static boolean scriptIsWaiting;
	public static boolean scriptShowBlackBars;
	public static int scriptEnvironment;
	public static int scriptCameraTargetX;
	public static int scriptCameraTargetY;
	public static int scriptOriginX;
	public static int scriptOriginY;
	public static boolean scriptIsWazActive;
	public static boolean scriptIsCameraMoving;
	public static int[] scriptSfx = new int[] {
		-1,
		20,
		14,
		19,
		15,
		17,
		16,
		18,
	};

	// Selection scene.
	private static final int SELECTION_STATE_LANGUAGE = 0;
	private static final int SELECTION_STATE_AUDIO = 1;
	public static int sceneSelectionState = 0;

	// Main menu scene.
	public static Image imgGameLogoTop;
	public static Image imgGameLogoBottom;
	public static boolean isPastSplash = false;

	// Transition scene.
	private static final int TRANSITION_TYPE_WIPE = 0;
	private static final int TRANSITION_TYPE_INSTANT = 2;
	public static int sceneTransitionNextState;
	public static long sceneTransitionStartMs;
	public static int sceneTransitionInvCoeff;
	public static int sceneTransitionType;
	public static boolean sceneTransitionDisabled = true;
	public static int sceneTransitionOffset;

	// Level scene.
	private static final int LEVEL_DEFAULT_JUMP_TICKS = 3;
	public static int levelEntityX;
	public static int levelEntityY;
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
	public static int levelPlayerJumpTicks = 0;
	public static int levelPlayerHitTicks = 0;
	public static int levelPlayerCurrentGrabberFlags = -1;
	public static int levelPlayerIgnoreGrabberId = -1;

	// Facial proportions.
	public static int[] currentEyeBlinkTicks = new int[2];
	public static int[] currentFace = new int[2];
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
		{8, 11},
		{0, -3},
		{0, 0},
		{0, 0},
		{0, 0},
	};
	public static int[][] eyesY = new int[][] {
		{0, -4},
		{4, 0},
		{4, 0},
		{14, 5},
		{0, -7},
		{6, 6},
		{5, 1},
		{8, 6},
	};
	public static int[][] mouthX = new int[][] {
		{0, 0},
		{3, 8},
		{0, 0},
		{-5, -11},
		{6, 2},
		{5, 5},
		{0, 0},
		{3, 8},
	};
	public static int[][] mouthY = new int[][] {
		{0, 0},
		{7, 3},
		{4, 8},
		{7, 13},
		{6, 13},
		{3, 6},
		{6, 5},
		{4, 9},
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
	public static int[] levelExpanderIds;
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
	public static int[] levelEnemyIds;
	public static boolean[] levelEnemyCanJump;
	public static int[] levelEnemyFramesUntilJump;
	public static int[] levelEnemyJumpHeight;
	public static boolean[] levelEnemyIsMovingInX;
	public static int[] levelEnemyFramesUntilXMovement;
	public static int[] levelEnemyXMovementSpeed;
	public static int[] levelEnemyXMovementDistance;
	public static int[] levelEnemyXMovementDirection;
	public static int[] levelEnemyCenterX;
	/* Can be either movement start ticks for the critter or parent ID for the
	 * spider. */
	public static int[] levelEnemyArg;
	public static boolean[] levelEnemyIsJitteringX;
	public static boolean[] levelEnemyIsAlive;
	public static boolean[] levelEnemyIsBird;

	public static int levelNumRects;
	public static int[] levelRectX;
	public static int[] levelRectY;
	public static int[] levelRectHWidth;
	public static int[] levelRectHHeight;
	public static boolean[] levelRectIsAnimated;
	public static int[] levelAnimRectIds;
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
	public static int[] levelSpawnStartIds;
	public static int[] levelSpawnCurrentIndex;
	public static int[] levelSpawnEndIds;
	public static int[] levelSpawnEndTick;
	public static int[] levelSpawnCurrentTick;
	public static int[] levelSpawnRadius;
	public static int[] levelSpawnAngle;
	public static int[] levelSpawnAngleRange;
	public static int[] levelSpawnSpeed;
	public static int[] levelSpawnSpeedRange;

	public static int levelNumCircles;
	public static int[] levelCircleX;
	public static int[] levelCircleY;
	public static int[] levelCircleRadius;
	public static int[] levelCirclePrevX;
	public static int[] levelCirclePrevY;
	public static int[] levelCircleFlags;
	public static int[] levelCircleWeight;
	public static byte[] levelCircleType;
	public static boolean[] levelCircleHasPhysics;

	public static int swappedLevelNumCircles;
	public static int[] swappedLevelCircleX;
	public static int[] swappedLevelCircleY;
	public static int[] swappedLevelCircleRadius;
	public static int[] swappedLevelCirclePrevX;
	public static int[] swappedLevelCirclePrevY;
	public static int[] swappedLevelCircleFlags;
	public static int[] swappedLevelCircleDensity;
	public static byte[] swappedLevelCircleType;
	public static boolean[] swappedLevelCircleHasPhysics;

	public static int levelWeatherId = 0;
	public static int levelBackgroundId = 0;
	public static int levelWidth;
	public static int levelHeight;
	public static int levelMovableGravity;
	public static int levelMovableAccel;
	public static boolean levelIsPlayerOnSurface;
	public static boolean isLevelComplete;
	public static boolean isMirrored = false;
	public static boolean isFinalLevel;

	public static int currentLevelLoaded = 0;
	public static int level = 0;
	public static int levelColor = 0xffffff;
	public static int levelColorMovable = 0xffffff;
	public static int levelColorPortal = 0xffffff;
	public static int levelColorDestructible = 0xffffff;
	public static int levelColorExpander = 0xff4500;
	public static int levelRadioactiveMinRed = 0x98;
	public static int levelRadioactiveMaxRed = 0xff;
	public static int levelRadioactiveMinGreen = 0x24;
	public static int levelRadioactiveMaxGreen = 0xb6;
	public static int levelRadioactiveMinBlue = 0x10;
	public static int levelRadioactiveMaxBlue = 0x21;

	public static int levelPortalFilledRadius = 0;

	public static short[] levelIdsExpanders;
	public static short[] levelIdsFoam;
	public static short[] levelIdsDestructibles;
	public static short[] levelIdsPortals;
	public static short[] levelIdsRadioactive;
	public static short[] levelIdsBombDispenser;
	public static short[] levelIdsPlatforms;

	public static int activeSwapKey = -1;
	public static int swappedLevelWidth;
	public static int swappedLevelHeight;
	public static int swappedLevelMovableGravity;
	public static int swappedLevelMovableAccel;
	public static boolean swappedLevelIsPlayerOnSurface;
	public static boolean swappedIsLevelComplete;

	// Manage sounds.
	private static final int SOUND_ID_JINGLE_FLYIN = 0;
	private static final int SOUND_ID_JINGLE_COMPLETE = 1;
	private static final int SOUND_ID_SHIP = 2;
	private static final int SOUND_ID_INGAME1 = 3;
	private static final int SOUND_ID_INGAME2 = 4;
	private static final int SOUND_ID_INGAME3 = 5;
	private static final int SOUND_ID_INGAME4 = 6;
	private static final int SOUND_ID_INGAME0 = 7;
	private static final int SOUND_ID_TITLE = 8;
	private static final int SOUND_ID_SFX_JUMP = 9;
	private static final int SOUND_ID_SFX_HURT = 10;
	private static final int SOUND_ID_SFX_BOMB = 11;
	private static final int SOUND_ID_SFX_GO = 12;
	private static final int SOUND_ID_SFX_HURRA = 13;
	private static final int SOUND_ID_SFX_EMO_STARTLED = 14;
	private static final int SOUND_ID_SFX_EMO_SURPRISED = 15;
	private static final int SOUND_ID_SFX_EMO_HAPPY = 16;
	private static final int SOUND_ID_SFX_EMO_ANGRY = 17;
	private static final int SOUND_ID_SFX_EMO_BORED = 18;
	private static final int SOUND_ID_SFX_ROCKET = 21;
	private static final int SOUND_ID_SFX_XIFU = 22;
	private static final int SOUND_ID_SFX_DISTRESS = 23;
	private static final int SOUND_ID_SFX_JUMP2 = 24;
	private static final int SOUND_ID_SFX_HURT2 = 25;
	private static final int SOUND_ID_SFX_XIFU2 = 26;
	private static final int SOUND_NUM_SOUNDS = 27;
	public static boolean currentSoundSfxXifuState;
	public static int currentSoundId = -1;

	// Background.
	public static Image[] imgsBackground;
	public static int[][] activeBackgroundPattern;
	public static int[][] activeBackgroundObjectsPattern;
	public static int currentLoadedBackgroundId;
	// NOTE: this field is always zero.
	public static int backgroundOffsetY;
	public static int backgroundEndPosY;
	public static int currentBackgroundTotalHeight;
	public static int backgroundObjectsEndPosY;
	public static boolean currentBackgroundHasObjects = true;
	public static boolean[] backgroundHasObjects = new boolean[] {
		true,
		true,
		true,
		true,
		false,
	};
	// Fallback weather IDs.
	public static int[] fallbackWeathers = new int[] {
		2,
		1,
		1,
		2,
		0,
	};
	// Background IDs.
	public static int[] knownBackgrounds = new int[] {
		0,
		1,
		2,
		3,
		4,
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
			{0, 0, 0},
		},
		{
			{0, 0},
			{0, 0},
			{1, 1},
		},
		{
			{0, 0},
			{0, 0},
			{0, 0},
		},
		{
			{0, 0},
			{0, 0},
			{0, 0},
		},
		{
			{3, 0, 1, 2},
			{0, 1, 2, 0},
			{1, 2, 0, 3},
			{2, 0, 0, 0},
		},
	};

	// The same as backgroundPatterns, but a bit closer to the camera.
	public static int[][][] backgroundObjectsPattern = new int[][][] {
		{
			{1, 2, 1, 2},
			{3, 3, 3, 3},
			{4, 4, 4, 4},
		},
		{
			{2, 2, 2},
			{3, 3, 3},
			{3, 3, 3},
		},
		{
			{1, 2, 1},
			{3, 3, 3},
			{3, 3, 3},
		},
		{
			{1, 1, 1},
			{2, 2, 2},
			{2, 2, 2},
		},
		{
			{0},
		},
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

	public static int dialogueIsShown;
	public static int dialogueFace;
	public static int dialogueNumLines;
	public static int dialogueFaceIteration;
	public static long dialogueStartMs;
	public static int dialogueIteration;
	public static String dialogueText;
	public static boolean dialogueDone;

	public static Image[][] imgsFaces;
	public static int nextLevelToLoad = -2;
	public static int levelIntroTicks = 0;
	public static boolean mustExitHolodeck = false;
	public static boolean isDejaVuMessageShown = false;
	public static boolean isBeamAnimated = false;

	private static final int LEVEL_CAMERA_ZOOM_DEFAULT = 100;
	public static int levelDeathTicks = 70;
	public static int levelTicks = 0;
	public static int levelScriptToRun = -1;
	public static int language = 0;
	public static int levelCameraZoom = LEVEL_CAMERA_ZOOM_DEFAULT;

	// Cheat codes.
	public static int[] cheatCodeLevelComplete = new int[] {
		KEY_LEFT,
		KEY_RIGHT,
		KEY_LEFT,
		KEY_RIGHT,
		KEY_0,
	};
	public static int[] cheatCodeInvincibility = new int[] {
		KEY_DOWN,
		KEY_RIGHT,
		KEY_DOWN,
		KEY_LEFT,
		KEY_DOWN,
		KEY_0,
	};

	public static int iterCheatLevelComplete = 0;
	public static int iterCheatInvincibility = 0;

	public static boolean isPlayerInvincible = false;
	public static boolean isPlayerAlive = true;
	public static boolean pauseScreenDraw = false;
	public static boolean loadingBarSoundCleanup = true;
	public static int[] loadingBarColors = new int[12];
	public static int loadingBarColorIndex = 9;
	public static boolean isFadeBlackSet = false;
	public static int fade = 100;

	// Decors inside ship: socks, flowers, cubes, etc.
	public static short[] shipDecorForeground;
	public static short[] shipDecorBackground;
	public static Image[] imgsShipDecor;

	public static Image imgArrowLeft;
	public static Image imgArrowRight;
	public static Image[] imgsShipIcon;
	public static Image[] imgsShipIconB;
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
	public static int victoryTicks;
	public static int victoryCameraX;
	public static int victoryCameraY;
	public static int victoryCameraZoom;
	public static String textMission;
	public static String textCompleted;

	// Trigonometric.
	public static short[] sin1000;
	public static short[] cos1000;

	// Transmission.
	public static Image[] imgsStatic;
	public static int[] staticTiles;
	public static boolean isTransmissionNoisy = true;
	public static boolean isTransmissionShaky = true;
	public static boolean isPhotoFrameShown = false;
	public static boolean isTransmissionBlinked = false;
	public static int transmissionEntityX = 115;
	public static int transmissionEntityY = 300;
	public static int transmissionEntitySize = 100;
	public static int transmissionBackgroundColor = 0;
	public static int activePhotoFrameLabelId = 0;

	public static String[] photoFrameLabels = new String[] {
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
		gSetClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
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
		for (int i = 0; i < KEYMAP.length; i++)
			for (int j = 0; j < KEYMAP[i].length; j++)
				if (keyCode == KEYMAP[i][j])
					return (1 << i);

		return KEY_UNKNOWN;
	}

	public final void keyPressed(int keyCode) {
		tmpHeldKeys |= pressedKeyValue(keyCode);
		super.keyPressed(keyCode);

		if (keyCode == -6) {
			pressOnlyKeys |= KEY_OK;
			pressOnlyKeys |= KEY_SOFT_LEFT;
			tmpHeldKeys |= KEY_SOFT_LEFT;
		}

		if (keyCode == -7) {
			pressOnlyKeys |= KEY_BACK;
			pressOnlyKeys |= KEY_SOFT_RIGHT;
			tmpHeldKeys |= KEY_SOFT_RIGHT;
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

	public static final byte[] makePng(byte[] pimData, short pplCrc) {
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
				pimData = makePng(pimData, pplCrc);
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
		lockFps();
	}

	public static final void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
		}
	}

	public static final void garbageCollector() {
		long initFreeMem = Runtime.getRuntime().freeMemory();
		System.gc();

		for (int i = 0; i < 10; i++) {
			Thread.yield();
			if (Runtime.getRuntime().freeMemory() < initFreeMem)
				return;
		}
	}

	public static final void lockFps() {
		currTime = millis();
		if (currTime - prevTime < 45L)
			sleep(45L - (currTime - prevTime));

		prevTime = millis();
	}

	public static final void writeRecordBytes(byte[] data) {
		try {
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			if (rs != null) {
				if (rs.getNumRecords() == 0)
					rs.addRecord(data, 0, data.length);
				else
					rs.setRecord(1, data, 0, data.length);

				rs.closeRecordStore();
			}
		} catch (Exception e) {
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
		} catch (Exception e) {
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
				language = (data[0] & 0xff) +
					(data[1] << 8 & 0xff00) +
					(data[2] << 16 & 0xff0000) +
					(data[3] << 24 & 0xff000000);

				level = (data[4] & 0xff) +
					(data[5] << 8 & 0xff00) +
					(data[6] << 16 & 0xff0000) +
					(data[7] << 24 & 0xff000000);

				isMirrored = data[8] == 1;
				isVibraEnabled = data[9] == 1;

				return true;
			} catch (Exception e) {
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
		soundPlayersOtt = new Sound[num];
		soundPlayers = new Player[num];
		soundPlayersMidi = new Player[num];
		numActiveSoundsMidi = 0;
		numActiveSounds = 0;
		soundFilenames = new String[num];

		for (int i = 0; i < numSounds; i++) {
			soundActiveStates[i] = false;
			soundQueueCleanup[i] = false;
			soundUsedStates[i] = false;
			soundVolume[i] = 5;
			soundLoopCounts[i] = 1;
			soundTypes[i] = 999;
			soundPlayersOtt[i] = null;
			soundPlayers[i] = null;
			soundPlayersMidi[i] = null;
		}
	}

	public static final int loadSoundToQueue(int index, String filename, int soundType, int size) {
		if (isSoundIndexUsed(index))
			return -2;

		if (index >= numSounds)
			return -3;

		if (filename == null)
			return -1;

		if (isLoadingBarShown) renderLoadingBar();

		soundTypes[index] = soundType;
		if (!filename.equals(soundFilenames[index])) {
			soundFilenames[index] = filename;
		}

		String obj = "";

		if (soundType == 0) {
			InputStream is = null;
			try {
				is = obj.getClass().getResourceAsStream("/" + filename + ".ott");
				byte[] arr = new byte[size];
				is.read(arr);
				soundPlayersOtt[index] = new Sound(arr, 1);
				is.close();
			} catch (Exception e) {
				soundPlayersOtt[index] = null;
				soundTypes[index] = 999;
				soundTypes[index] = 0;
			}
			return 0;
		}

		return -4;
	}

	public static final int loadSoundToIndex(int index, String filename, int soundType, int size) {
		if (isSoundIndexUsed(index))
			return -2;

		if (index >= numSounds)
			return -3;

		if (filename == null)
			return -1;

		if (isLoadingBarShown) renderLoadingBar();

		soundTypes[index] = soundType;
		if (!filename.equals(soundFilenames[index])) {
			soundFilenames[index] = filename;
		}

		String obj = "";

		if (soundType == 0) {
			InputStream is = null;
			try {
				is = obj.getClass().getResourceAsStream("/" + filename + ".ott");
				byte[] arr = new byte[size];
				is.read(arr);
				soundPlayersOtt[index] = new Sound(arr, 1);
				is.close();
			} catch (Exception e) {
				soundPlayersOtt[index] = null;
				soundTypes[index] = 999;
				soundTypes[index] = 0;
			}
			return 0;
		}

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
				soundPlayersMidi[index] = Manager.createPlayer(is, "audio/sp-midi");
				pl = soundPlayersMidi[index];
			}

			if (soundType == 1) {
				InputStream is = obj.getClass().getResourceAsStream("/" + filename + ".mid");
				soundPlayersMidi[index] = Manager.createPlayer(is, "audio/midi");
				pl = soundPlayersMidi[index];
			}

			pl.realize();

			while (pl.getState() != Player.REALIZED)
				sleep(30L);

			numActiveSoundsTotal++;
			soundUsedStates[index] = true;

			boolean prefetched = false;
			if (soundType != 1 && soundType != 2) {
				if (soundType != 6 && soundType != 7 && soundType != 8)
					return -2;

				if (numActiveSounds < 1) {
					pl.prefetch();
					numActiveSounds++;
					prefetched = true;
				}
			} else if (numActiveSoundsMidi < 1) {
				pl.prefetch();
				numActiveSoundsMidi++;
				prefetched = true;
			}

			if (prefetched)
				while (pl.getState() != Player.PREFETCHED)
					sleep(30L);

			return 0;
		} catch (Exception e) {
			if (pl != null) {
				pl.close();
				soundPlayers[index] = null;
				soundPlayersMidi[index] = null;
				soundTypes[index] = 999;
				soundFilenames[index] = "";
			}

			return -1;
		}
	}

	public static final void closeSoundAtIndex(int index) {
		Player pl = null;

		if (soundPlayers[index] != null) {
			pl = soundPlayers[index];
			numActiveSounds--;
		}

		if (soundPlayersMidi[index] != null) {
			pl = soundPlayersMidi[index];
			numActiveSoundsMidi--;
		}

		if (pl != null) {
			pl.close();

			while (pl.getState() != Player.CLOSED)
				sleep(30L);

			soundPlayers[index] = soundPlayersMidi[index] = null;
			numActiveSoundsTotal--;
		}

		if (soundPlayersOtt[index] != null) {
			soundPlayersOtt[index].release();
			soundPlayersOtt[index] = null;
		}
	}

	public static final boolean isSoundIndexUsed(int index) {
		if (soundPlayers[index] != null)
			return true;

		if (soundPlayersMidi[index] != null)
			return true;

		if (soundPlayersOtt[index] != null)
			return true;

		return false;
	}

	public static final boolean isSoundActive(int index, int soundType) {
		if (!isAudioEnabled)
			return false;

		boolean started = false;

		if ((soundType == soundTypes[index] || soundType == -1) && soundActiveStates[index])
			return true;

		if ((soundType == 0 || soundType == -1) && soundPlayersOtt[index] != null)
			started = soundPlayersOtt[index].getState() == 0;

		if ((soundType == 6 || soundType == -1) && soundPlayers[index] != null)
			started = soundPlayers[index].getState() == Player.STARTED;

		if ((soundType == 1 || soundType == -1) && soundPlayersMidi[index] != null)
			started = soundPlayersMidi[index].getState() == Player.STARTED;

		return started;
	}

	public static final boolean playSound(int index, int loopCount) {
		if (!isAudioEnabled)
			return true;

		if (millis() - soundStartMillis < 300L)
			return false;

		soundStartMillis = millis();

		if (isSoundActive(index, -1))
			return false;

		int activeSounds = 0;

		if (soundTypes[index] == 6 || soundTypes[index] == 7 || soundTypes[index] == 8) {
			for (int i = 0; i < numSounds; i++)
				if (isSoundActive(i, soundTypes[i]))
					activeSounds++;

			if (activeSounds >= 3)
				return false;
		}

		soundActiveStates[index] = true;
		soundLoopCounts[index] = loopCount;

		return true;
	}

	public static final void queueAllSoundsForCleanup() {
		if (isAudioEnabled)
			for (int i = 0; i < numSounds; i++)
				soundQueueCleanup[i] = true;
	}

	private static void stopSoundAtIndex(int index, boolean checkStarted) {
		if (soundPlayersOtt[index] != null)
			soundPlayersOtt[index].stop();

		Player pl;
		if (soundPlayersMidi[index] != null)
			pl = soundPlayersMidi[index];
		else
			pl = soundPlayers[index];

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
				} catch (IllegalStateException e) {
				}
			}
		} catch (MediaException e) {
		}
	}

	public static final void queueSoundCleanup() {
		if (!isAudioEnabled || soundActiveStates == null)
			return;

		try {
			for (int i = 0; i < numSounds; i++) {
				if (soundQueueCleanup[i]) {
					soundQueueCleanup[i] = false;
					stopSoundAtIndex(i, false);

					if (soundTypes[i] != 0)
						closeSoundAtIndex(i);

					if (activeSound == i)
						activeSound = -1;
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
					if (soundTypes[i] == 0 || !soundActiveStates[i])
						continue;

					boolean tmp = isLoadingBarShown;
					isLoadingBarShown = false;
					loadSoundToIndex(i, soundFilenames[i], soundTypes[i], 0);
					isLoadingBarShown = tmp;
				}

				Player pl;
				if (soundPlayersMidi[i] != null)
					pl = soundPlayersMidi[i];
				else
					pl = soundPlayers[i];

				if (soundUsedStates[i]) {
					soundUsedStates[i] = false;
					if (pl != null) {
						try {
							VolumeControl control;
							if ((control = (VolumeControl)pl.getControl("VolumeControl")) != null)
								control.setLevel(soundVolume[i] * 10);
						} catch (IllegalStateException e) {
						}
					}

					boolean stopped = false;

					if (activeSound != -1 && soundPlayersOtt[activeSound] != null && isSoundActive(activeSound, 0)) {
						soundPlayersOtt[activeSound].stop();
						stopped = true;
					}

					if (soundPlayersOtt[i] != null)
						soundPlayersOtt[i].setGain(soundVolume[i] * 15);

					if (activeSound != -1 && soundPlayersOtt[activeSound] != null && stopped)
						soundPlayersOtt[activeSound].play(soundLoopCounts[activeSound] == -1 ? 0 : soundLoopCounts[activeSound]);
				}

				if (soundActiveStates[i]) {
					soundActiveStates[i] = false;

					try {
						if (soundPlayersOtt[i] != null) {
							soundPlayersOtt[i].play(soundLoopCounts[i] == -1 ? 0 : soundLoopCounts[i]);
						}
					} catch (Exception e) {
					}

					try {
						if (pl != null && pl.getState() != Player.STARTED) {
							pl.setLoopCount(soundLoopCounts[i]);
							pl.start();
							activeSound = i;
						}
					} catch (MediaException e) {
					}
				}
			}
		} catch (IllegalStateException e) {
			return;
		}
	}

	public static final int getFileIndex(short fnCrc) {
		for (int i = 0; i < bfcHeadNumEntries; i++)
			if (bfcHeadCrcs[i] == fnCrc)
				return i;

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

			for (int i = 0; i < bfcHeadNumEntries; i++)
				if (i != index && bfcHeadOffsets[i] == bfcHeadOffsets[index] && bfcHeadLocations[i] == bfcHeadLocations[index])
					bfcReservedData[i] = bfcReservedData[index];

			sCurrentOffset += sCurrentSize;
		} catch (Exception e) {
		}
	}

	/* Load main index file (head.bfc) to RAM. It's used for indexing inner
	 * files in N.bfc and reading them. */
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
		} catch (Exception e) {
		}

		bfcInitReservedData(bfcHeadNumEntries);
	}

	public static final void bfcGenCrcTable() {
		for (int i = 0; i < 256; ++i) {
			int val = 0;
			int x = i << 8;

			for (int j = 0; j < 8; j++) {
				if (((val ^ x) & 0x8000) != 0)
					val = val << 1 ^ bfcCrcPoly;
				else
					val <<= 1;

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
		if (index < 0)
			return null;
		else
			return bfcReservedData[index] != null ? bfcReservedData[index] : loadFile8(fnCrc);
	}

	public static final short[] loadFile16(String file) {
		return loadFile16(bfcCrcFilename(file));
	}

	public static final short[] loadFile16(short fnCrc) {
		if (!sOpenFile(fnCrc)) {
			return null;
		} else {
			short[] arr = new short[sCurrentSize / 2];

			for (int i = 0; i < arr.length; i++)
				arr[i] = sRead16();

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

			for (int i = 0; i < arr.length; i++)
				arr[i] = sRead32();

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
			} catch (Exception e) {
				return false;
			}
		}
	}

	public static final boolean sOpenFile(short fnCrc) {
		if (isLoadingBarShown) renderLoadingBar();

		int index = getFileIndex(fnCrc);
		sCurrentIndex = index;
		sCurrentOffsetReserved = 0;
		sCurrentReserved = false;

		if (index >= 0 && bfcReservedData[index] != null) {
			// Read the required file from RAM.
			sCurrentSize = bfcHeadSizes[index];
			sCurrentReserved = true;
			return true;
		} else {
			// Read the required file from .bfc resources.
			if (index == -1)
				return false;

			sCurrentSize = bfcHeadSizes[index];

			if (sCurrentLocation == bfcHeadLocations[index] &&
					sCurrentOffset <= bfcHeadOffsets[index]) {

				sSkipBytes(bfcHeadOffsets[index] - sCurrentOffset);

				// Copy the file to RAM for future use.
				if (bfcHeadMemStates[index] >= 0 && bfcReservedData[index] == null)
					bfcReserve(index);

				return true;
			}

			try {
				if (sCurrentData != null)
					sCloseFile();

				sCurrentData = new DataInputStream(instance.getClass().getResourceAsStream("/" + bfcHeadLocations[index] + ".bfc"));
				sCurrentData.skip((long)bfcHeadOffsets[index]);
				sCurrentLocation = bfcHeadLocations[index];
				sCurrentOffset = bfcHeadOffsets[index];

				// Copy the file to RAM for future use.
				if (bfcHeadMemStates[index] >= 0 && bfcReservedData[index] == null)
					bfcReserve(index);

				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	public static final void sSkipBytes(int n) {
		if (sCurrentReserved) {
			sCurrentOffsetReserved += n;
		} else {
			sCurrentOffset += n;

			try {
				while (n > 0)
					n = (int)((long)n - sCurrentData.skip((long)n));
			} catch (Exception e) {
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
			} catch (Exception e) {
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
			} catch (Exception e) {
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
			} catch (Exception e) {
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
			} catch (Exception e) {
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
			} catch (Exception e) {
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
			} catch (Exception e) {
				return -1;
			}
		}
	}

	public static final void sCloseFile() {
		try {
			sCurrentData.close();
			sCurrentIndex = -1;
			sCurrentLocation = -1;
		} catch (Exception e) {
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

			// File header.
			int bitness = sReadU8(); // 0 = 16-bit for each char, 1 = 8-bit for each char.
			int numTables = sReadU8(); // Number of tables (arrays of the strings).

			if (index >= numTables) {
				return textTableStrings;
			} else {
				sSkipBytes(index * 2);
				sSkipBytes(sReadU16() + (numTables - index - 1) * 2);

				// Table data structure.
				int numStrings = sReadU16(); // Number of strings in the table.
				int[] stringLens = new int[numStrings];

				if (stringIndex < 0) {
					textTableStrings = new String[numStrings];
					textTableStringTypes = new byte[numStrings];
					textTableIndex = index;

					for (int i = 0; i < numStrings; i++) {
						stringLens[i] = sReadU16(); // String length.
						sReadU16(); // Offset to a string.
					}
				} else {
					if (stringIndex >= numStrings)
						return textTableStrings;

					sSkipBytes(stringIndex * 4);
					stringLens[0] = sReadU16();
					sSkipBytes(sReadU16() + (numStrings - stringIndex - 1) * 4);
				}

				// Reading the string.
				for (int i = 0; i < numStrings; i++) {
					stringLens[i]--;

					int type = 0;
					char[] charArr = new char[stringLens[i]];

					if (bitness == 1) {
						type = sReadU8();

						for (int j = 0; j < stringLens[i]; j++)
							charArr[j] = (char)sReadU8();
					} else {
						type = sReadU16();

						for (int j = 0; j < stringLens[i]; j++)
							charArr[j] = (char)sReadU16();
					}

					String string = String.valueOf(charArr);
					if (stringIndex >= 0)
						break;

					textTableStringTypes[i] = (byte)type;
					textTableStrings[i] = string;
				}

				return textTableStrings;
			}
		}
	}

	public static final String getText(int stringAddr) {
		int index = stringAddr >> 16;

		if (textTableIndex != index)
			loadTextTableFromIndex(index, -1);

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
			for (int i = 0; i < 5; i++)
				assume -= ((assume * assume >> 16) - arg << 16) / (assume << 2);

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

			while (tryResult < tryResultBase + 10L && tryResult * tryResult <= numberInCurPlace)
				result = tryResult++;
		}

		return result << 8;
	}

	// Check softkeys if pressed.
	public static final int softkeyPressed(int left, int right) {
		leftSoftkey = left;
		rightSoftkey = right;

		if ((heldKeys & KEY_SOFT_LEFT) > 0 &&
				(heldPrevKeys & KEY_SOFT_LEFT) == 0) {

			return left;

		} else {

			return (heldKeys & KEY_SOFT_RIGHT) > 0 &&
				(heldPrevKeys & KEY_SOFT_RIGHT) == 0 ? right : -1;

		}
	}

	// Check softkeys and the OK key as the left softkey (if `okKeyEnabled = true`).
	public static final int softkeyPressed(int left, int right, boolean okKeyEnabled) {
		leftSoftkey = left;
		rightSoftkey = right;

		if ((heldKeys & KEY_SOFT_LEFT) > 0 &&
				(heldPrevKeys & KEY_SOFT_LEFT) == 0) {

			return left;

		} else if ((heldKeys & KEY_SOFT_RIGHT) > 0 &&
				(heldPrevKeys & KEY_SOFT_RIGHT) == 0) {

			return right;

		} else {

			return okKeyEnabled &&
				left >= 0 &&
				(heldKeys & 1) > 0 &&
				(heldPrevKeys & KEY_OK) == 0 ? left : -1;

		}
	}

	public static final void renderSoftkeyIcons() {
		if (imgsSoftkey != null) {
			if (leftSoftkey > -1) {
				gDrawImage(imgsSoftkey[leftSoftkey], 1, SCREEN_HEIGHT - 2 - softkeyHeight[leftSoftkey], 0);
				leftSoftkey = -1;
			}

			if (rightSoftkey > -1) {
				gDrawImage(imgsSoftkey[rightSoftkey], SCREEN_WIDTH - 2 - softkeyWidth[rightSoftkey], SCREEN_HEIGHT - 1 - softkeyHeight[rightSoftkey], 0);
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
		if (fontCharWidths[fontId] == null)
			return string.length() * (fontAverageWidths[fontId] + fontSpacesPerChars[fontId]) - fontSpacesPerChars[fontId];

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
		if (fontCharWidths[fontId] == null)
			return getAverageLengthUntilTerminator(maxStringWidth, fontId);

		int strLen = string.length();

		int i;
		for (i = startIndex; i < strLen; i++) {
			int ch;
			if ((ch = string.charAt(i) - 30) < 0)
				return i - startIndex + 1;

			if ((maxStringWidth = maxStringWidth - fontCharWidths[fontId][ch]) < 0)
				break;

			maxStringWidth -= fontSpacesPerChars[fontId];
		}

		return i - startIndex;
	}

	// Split a string into lines.
	public static final int[] getNewLineIndices(int maxStringLength, String string, int fontId) {
		if (fontImages[fontId] == null)
			return null;

		int numNewLines = 0;
		int maxLineLength = 99999;
		int strLen = string.length();
		int curIndex = 0;

		int approxNewLines = strLen / 8;
		if (approxNewLines < 10)
			approxNewLines = 10;

		int[] approxLineIndices = new int[approxNewLines];
		boolean isNotWhitespace = false;
		boolean ignoreFirstNewLines = true;
		int newLineStreakCount = 0;

		while (curIndex < strLen) {
			switch (string.charAt(curIndex)) {
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
			if ((curLineLen = curIndex + getLengthUntilTerminator(maxStringLength, fontId, string, curIndex)) > strLen)
				curLineLen = strLen;

			int nextSpace = -1;

			int eolIndex;
			for (eolIndex = curIndex; eolIndex < curLineLen; eolIndex++) {
				if (string.charAt(eolIndex) == '\n') {
					nextSpace = eolIndex;
					break;
				}

				if (string.charAt(eolIndex) == ' ')
					nextSpace = eolIndex;
			}

			if (eolIndex < strLen && nextSpace > 0 && string.charAt(eolIndex) != ' ')
				eolIndex = nextSpace;

			if (eolIndex >= maxLineLength) {
				eolIndex = maxLineLength;
				strLen = 0;
			}

			if (curIndex > eolIndex)
				curIndex = eolIndex;

			approxLineIndices[numNewLines++] = curIndex;
			curIndex = eolIndex;
		}

		int[] result = new int[numNewLines];

		for (int i = 0; i < numNewLines; i++)
			result[i] = approxLineIndices[i];

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
			switch (string.charAt(index)) {
				case '\n':
					if (!ignoreFirstNewLines && newLineStreakCount > 0 && (y += fontHeights[fontId] + fontLineGaps[fontId]) > bottom)
						return index;

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
			if ((curLineLen = index + getLengthUntilTerminator(width, fontId, string, index)) > strLen)
				curLineLen = strLen;

			int nextSpace = -1;

			int eolIndex;
			for (eolIndex = index; eolIndex < curLineLen; eolIndex++) {
				if (string.charAt(eolIndex) == '\n') {
					nextSpace = eolIndex;
					break;
				}

				if (string.charAt(eolIndex) == ' ')
					nextSpace = eolIndex;
			}

			if (eolIndex < strLen && nextSpace > 0 && string.charAt(eolIndex) != ' ')
				eolIndex = nextSpace;

			if (eolIndex >= endIndex) {
				eolIndex = endIndex;
				strLen = 0;
			}

			if (index > eolIndex)
				index = eolIndex;

			String currentLine = string.substring(index, eolIndex);
			if (centered)
				renderText(x + (width - getStringWidth(currentLine, fontId)) / 2, y, currentLine, fontId);
			else
				renderText(x, y, currentLine, fontId);

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
		if (x == -1000)
			x = (SCREEN_WIDTH - getStringWidth(string, fontId)) / 2;

		int textY = y;
		int fontHeight = fontHeights[fontId];

		if (clipY > y) {
			fontHeight -= clipY - y;
			textY = clipY;
		}

		if (y + fontHeight > clipY + clipHeight)
			fontHeight = clipY + clipHeight - textY;

		int curX = x;

		for (int i = 0; i < strLen; i++) {

			int ch = string.charAt(i) - 30;
			if (curX > SCREEN_WIDTH || ch < 0)
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

						if (charX + charWidth > clipX + clipWidth)
							charWidth = clipX + clipWidth - charX;
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

	// General loop.
	public static final void start() {
		loadGeneral();

		do {
			if (isPaused) {

				pauseScreenOnce();
				renderSoftkeyIcons();
				gfxFlush();
				updateKeys();

				if (isKeyReleased(KEY_OK))
					isPaused = false;

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
			switch (state) {

				case STATE_ID_SELECTION:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneSelectionInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneSelectionRun();
							break;
						case STATE_EXEC_FREE:
							sceneSelectionFree();
					}
					return;

				case STATE_ID_SPLASH:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneSplashInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneSplashRun();
							break;
						case STATE_EXEC_FREE:
							sceneSplashFree();
					}
					return;

				case STATE_ID_SHIP:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneShipInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneShipRun();
							break;
						case STATE_EXEC_FREE:
							sceneShipFree();
					}
					return;

				case STATE_ID_PRE_LEVEL:
					switch (res) {
						case STATE_EXEC_INIT:
							scenePreLevelInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							scenePreLevelRun();
							break;
						case STATE_EXEC_FREE:
							scenePreLevelFree();
					}
					return;

				case STATE_ID_LEVEL:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneLevelInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneLevelRun();
							break;
						case STATE_EXEC_FREE:
							sceneLevelFree();
					}
					return;

				case STATE_ID_SCRIPT:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneScriptInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneScriptRun();
							break;
						case STATE_EXEC_FREE:
							sceneScriptFree();
					}
					return;

				case STATE_ID_TRANSITION:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneTransitionInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneTransitionRun();
							break;
						case STATE_EXEC_FREE:
							sceneTransitionFree();
					}
					return;

				case STATE_ID_TITLE:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneTitleInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneTitleRun();
							break;
						case STATE_EXEC_FREE:
							sceneTitleFree();
					}
					return;

				case STATE_ID_HELP:
					switch (res) {
						case STATE_EXEC_INIT:
							sceneHelpInit(stateArg);
							break;
						case STATE_EXEC_RUN:
							sceneHelpRun();
							break;
						case STATE_EXEC_FREE:
							sceneHelpFree();
					}
					return;
			}
		}
	}

	public static final void setDisplay(Display dpy) {
		Game.dpy = dpy;
	}

	public static final void vibrate(int duration) {
		if (isVibraEnabled)
			dpy.vibrate(duration);
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
			if (confettiColors[i] == 0 || confettiY[i] > SCREEN_HEIGHT) {
				confettiColors[i] = confettiColorsAll[rand8() % confettiColorsAll.length];
				confettiHeights[i] = rand8() % 40;
				confettiX[i] = rand16() % SCREEN_WIDTH;
				confettiY[i] = -rand16() % SCREEN_HEIGHT;
				confettiWidths[i] = 8 + rand8() % 7;
				confettiAngles[i] = rand8();
			}

			confettiY[i] += (1 + rand8() % 5) * SCREEN_WIDTH / 176;
			confettiX[i] += cos1000[confettiAngles[i]] * 3 / 1000;
			confettiHeights[i] += 4 + rand8() % 5;
			if (confettiHeights[i] >= 40) {
				confettiHeights[i] = 0;
				confettiColors[i] = confettiColorsAll[rand8() % confettiColorsAll.length];
			}

			confettiAngles[i] += 3 + i % 2;
			if (confettiAngles[i] >= 360)
				confettiAngles[i] -= 360;

			int confettiHeight = confettiHeights[i] / 4;
			if (confettiHeights[i] >= 20)
				confettiHeight = (40 - confettiHeights[i]) / 4;

			gSetColor(confettiColors[i]);
			gFillRect(confettiX[i], confettiY[i], confettiWidths[i] * SCREEN_WIDTH / 176, confettiHeight * SCREEN_WIDTH / 176);
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
			if (rainY[i] > SCREEN_HEIGHT || rainX[i] == 0 && rainY[i] == 0) {
				rainX[i] = rand16() % 280;
				rainY[i] = rand16() % 53;
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
		if (levelBackgroundId == 0)
			gSetColor(0);

		for (int i = 0; i < 10; i++) {
			if (precipitationY[i] > SCREEN_HEIGHT || precipitationX[i] == 0 && precipitationY[i] == 0) {
				if (precipitationX[i] == 0 && precipitationY[i] == 0)
					precipitationY[i] = rand16() % SCREEN_HEIGHT;
				else
					precipitationY[i] = -(rand16() % 10);

				precipitationX[i] = rand16() % SCREEN_WIDTH;
				precipitationDirectionCos[i] = rand16() % 360;
			}

			gDrawLine(precipitationX[i] - 1, precipitationY[i], precipitationX[i] + 1, precipitationY[i]);
			gDrawLine(precipitationX[i], precipitationY[i] - 1, precipitationX[i], precipitationY[i] + 1);

			precipitationX[i] += cos1000[precipitationDirectionCos[i]] * 3 / 1000;
			precipitationY[i] += 2;
			precipitationDirectionCos[i] += 5;
			if (precipitationDirectionCos[i] >= 360)
				precipitationDirectionCos[i] -= 360;
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
					if (--spaceStarFlashTicks[spaceId][i] > 4)
						gSetColor(0);
					else
						gSetColor(0xffffff);
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
		gSetColor((col & 0xff0000) >> 17, (col & 0xff00) >> 9, (col & 0xff) >> 1);
		gDrawLine(x, y - 1, x, y - 1);
		gDrawLine(x, y + 1, x, y + 1);
		gDrawLine(x - 1, y, x - 1, y);
		gDrawLine(x + 1, y, x + 1, y);
	}

	public static final void sceneHelpRun() {
		renderHelp();

		// OK softkey.
		if (softkeyPressed(2, -1) == 2)
			setNewState(STATE_ID_SHIP, 0);

		// Down scroll.
		if (isKeyHeld(KEY_DOWN)) {
			sceneHelpScrollAccel -= (400 + sceneHelpScrollAccel) / 6;
			sceneHelpAutoscrollActive = false;
		}

		// Up scroll.
		if (isKeyHeld(KEY_UP)) {
			sceneHelpScrollAccel += (400 - sceneHelpScrollAccel) / 6;
			sceneHelpAutoscrollActive = false;
		}

		sceneHelpScrollAccel = sceneHelpScrollAccel * 95 / 100;
		if (sceneHelpAutoscrollActive)
			sceneHelpScrollAccel = -0xff;

		sceneHelpScrollDistance += sceneHelpScrollAccel;
		if (sceneHelpScrollDistance > (SCREEN_HEIGHT << 8))
			sceneHelpScrollDistance = (SCREEN_HEIGHT << 8);

		if (sceneHelpScrollDistance < -(sceneHelpText.length * 21 << 8))
			sceneHelpScrollDistance = -(sceneHelpText.length * 21 << 8);

		sceneHelpScroll++;

		refreshGame();
	}

	public static final void renderHelp() {
		gSetColor(0);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		renderSpace(0, 0, 0, true);

		int scrollDistance = sceneHelpScrollDistance >> 8;

		for (int i = 0; i < sceneHelpText.length; i++) {
			if (scrollDistance > -21 && scrollDistance < SCREEN_HEIGHT) {
				int textOffsetX = 0;

				if (scrollDistance < (SCREEN_HEIGHT / 3))
					textOffsetX = 90 - scrollDistance * 90 * 3 / SCREEN_HEIGHT;
				else if (scrollDistance > (SCREEN_HEIGHT * 2 / 3))
					textOffsetX = 90 - (SCREEN_HEIGHT - scrollDistance) * 90 * 3 / SCREEN_HEIGHT;

				int textX = 5 + sin(textOffsetX) * SCREEN_WIDTH / 2600;
				renderText(textX, scrollDistance, sceneHelpText[i], 2);
			}

			scrollDistance += 21;
		}

		gDrawImage(imgsScrollbar[0], SCREEN_WIDTH - 11, 107 - (sceneHelpScroll >> 2 & 3), 0);
		gDrawImage(imgsScrollbar[1], SCREEN_WIDTH - 11, 112 - ((sceneHelpScrollDistance >> 8) - SCREEN_HEIGHT) * 101 / (sceneHelpText.length * 21), 0);
		gDrawImage(imgsScrollbar[2], SCREEN_WIDTH - 11, 224 + (sceneHelpScroll >> 2 & 3), 0);
	}

	public static final void sceneHelpInit(int arg) {
		if (imgsScrollbar == null) {
			imgsScrollbar = new Image[3];

			for (int i = 0; i < 3; i++)
				imgsScrollbar[i] = loadImage("scrollBar" + i + ".pim", "scrollBar" + i + ".ppl");
		}

		initSpace(1, 50, SCREEN_WIDTH, SCREEN_HEIGHT);

		if (sceneHelpText == null)
			sceneHelpText = strSplitLines(getText(0x30000), 2);

		sceneHelpScrollDistance = SCREEN_HEIGHT << 8;
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
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		renderSpace(0, 0, 0, false);

		// A distant ship.
		if (scenePreLevelCurTick > 65) {
			gDrawImage(imgShipSmall, scenePreLevelSmallShipX, scenePreLevelSmallShipY, 0);
			scenePreLevelSmallShipX -= 4;
		}

		// A planet.
		gDrawImage(imgCurrentPlanet, 0, SCREEN_HEIGHT - imgCurrentPlanet.getHeight(), 0);

		// A closer ship.
		if (scenePreLevelShipX < SCREEN_WIDTH) {
			gDrawImage(imgOutside, scenePreLevelShipX, scenePreLevelShipY, 0);
			gDrawImage(imgsFlame[(int)(millis() / 100L % 2L)], scenePreLevelShipX + 8 - 15, scenePreLevelShipY + 23 - 5, 0);
			scenePreLevelShipX += 10;
		}

		// Render a planet name.
		if (scenePreLevelCurTick > 50)
			renderText(-1000, SCREEN_HEIGHT - 107, currentPlanetText, 0);

		// Render "press 5" string.
		if (scenePreLevelCurTick > 120 && (millis() & 512L) > 0L)
			renderText(-1000, SCREEN_HEIGHT - 19, titleText, 0);

		// Enter the level if skipped or pressed OK.
		if (scenePreLevelCurTick < 120 && softkeyPressed(-1, 3) == 3 ||
				scenePreLevelCurTick > 120 && softkeyPressed(2, -1, true) == 2) {

			setNewState(STATE_ID_LEVEL, 0);
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
		scenePreLevelSmallShipX = SCREEN_WIDTH;
		scenePreLevelSmallShipY = SCREEN_HEIGHT - imgCurrentPlanet.getHeight() * 4 / 5;

		if (scenePreLevelSmallShipY < SCREEN_HEIGHT / 4)
			scenePreLevelSmallShipY = SCREEN_HEIGHT / 4;

		scenePreLevelShipX = -85;
		scenePreLevelShipY = 238;

		// Flame sprites.
		if (imgsFlame == null) {
			imgsFlame = new Image[2];
			imgsFlame[0] = loadImage("flame0.pim", "flame0.ppl");
			imgsFlame[1] = loadImage("flame1.pim", "flame1.ppl");
		}

		// "press 5" string.
		if (titleText == null)
			titleText = getText(0x1000f);

		initSpace(1, 50, SCREEN_WIDTH, SCREEN_HEIGHT);

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
		switch (levelCompletionState) {
			// Update the level constantly.
			case 0:
				sceneLevelUpdate();
				if (isLevelComplete) {
					startScript("outro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					levelCompletionState = 1;
				}
				break;

			// Level completed.
			case 1:
				initVictory();
				levelCompletionState = 2;
			case 2:
				if (renderVictory())
					levelCompletionState = 3;
				break;

			// Return to the ship.
			case 3:
				setNewState(STATE_ID_SHIP, 10);
		}

		if (mustExitHolodeck)
			setNewState(STATE_ID_SHIP, 0);
	}

	public static final void sceneLevelInit(int arg) {
		isShipPaused = false;

		/* `arg` represents the choice of whether to load resources (possibly
		 * reloading them) or to skip that. */
		if (oldState != 5 && arg != 1) {
			isLoadingBarShown = true;
			levelCompletionState = 0;

			loadFaces();
			initExplosives();
			initEnemies();
			initSigns();
			initGrabber();

			mustExitHolodeck = false;
			isLevelComplete = false;
		}

		fade = 100;

		/* Do not bother audio engine if old scene was a script or a
		 * transition. */
		if (oldState != 5 && oldState != 6)
			playCurrentBackgroundMusic();
	}

	public static final void sceneLevelFree() {
		if (isLevelComplete) {
			level++;
			saveRecordData();
			isLevelComplete = false;
		}

		/* Do not bother audio engine if old scene was a script or a
		 * transition. */
		if (newStateIndex != 5 && newStateIndex != 6) {
			playSound(-1);
			garbageCollector();
		}
	}

	public static final void sceneSplashRun() {
		gSetColor(0xffffff);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		switch (splash) {

			// Eidos logo.
			case 0:
				gDrawImage(imgCenterLogo, (SCREEN_WIDTH - imgCenterLogo.getWidth()) / 2, (SCREEN_HEIGHT - imgCenterLogo.getHeight()) / 2, 0);

			// Eidos legal line.
			case 1:
				gDrawImage(imgEidosLegalLine, (SCREEN_WIDTH - imgEidosLegalLine.getWidth()) / 2, SCREEN_HEIGHT - imgEidosLegalLine.getHeight() - 4, 0);
				break;

			// Progressive Media logo.
			case 2:
				gDrawImage(imgCenterLogo, splashRocketX - 35, splashRocketY - 5, 0);
				gDrawImage(imgPmRocket, splashRocketX, splashRocketY, 0);
				break;

			// Progressive Media animation.
			case 3:
				int x = splashRocketX;
				int y = splashRocketY;

				if (splashTick < 20) {
					x = splashRocketX + rand8() % 5 - 2;
					y = splashRocketY + rand8() % 5 - 2;
					gDrawImage(imgCenterLogo, x - 35, y - 5, 0);
				} else {
					splashRocketX += (splashTick - 20) / 8;
					splashRocketY -= (splashTick - 20) / 8;
					gDrawImage(imgCenterLogo, (SCREEN_WIDTH - 136) >> 1, (SCREEN_HEIGHT - 124) >> 1, 0);
				}

				gDrawImage(imgPmRocket, x, y, 0);
				if (x > SCREEN_WIDTH + 30)
					splashDuration = 0L;
				else
					splashDuration = millis() + 2000L;

				splashTick++;
				break;

			// Maniacs of Noice logo scaling up.
			case 4:
				int[] elemColors = new int[] {0, 0xffffff, 0, 0xffffff, 0x3aaec7};
				int[] elemRadius = new int[] {74, 74, 50, 50, 24};
				int[] elemX = new int[] {0, -14, -7, 3, 0};

				for (int i = 0; i < 5; i++) {
					gSetColor(elemColors[i]);

					int scaleX = sin1000[splashTick * 2] * elemX[i] / 1000;
					int scaleRadius = sin1000[splashTick * 2] * elemRadius[i] / 1000;

					if (i != 2 && i != 3)
						gFillArc((SCREEN_WIDTH >> 1) + scaleX - scaleRadius / 2 - 1,
								(SCREEN_HEIGHT >> 1) - scaleRadius / 2,
								scaleRadius,
								scaleRadius,
								0,
								360);
					else
						gFillArc((SCREEN_WIDTH >> 1) + scaleX - scaleRadius / 2 - 1,
								(SCREEN_HEIGHT >> 1) - scaleRadius / 2,
								scaleRadius,
								scaleRadius,
								80,
								200);
				}

				gDrawImage(imgPmRocket, splashRocketX, splashRocketY, 0);

				splashRocketX += SCREEN_WIDTH / 30;
				splashRocketY -= SCREEN_HEIGHT / 30;

				if (splashTick > 45)
					splashDuration = 0L;

				splashTick++;
				break;

			// Maniacs of Noise logo.
			case 5:
				gDrawImage(imgCenterLogo, ((SCREEN_WIDTH - 128) >> 1) + 2, ((SCREEN_HEIGHT - 120) >> 1) - 3, 0);
		}

		// Show a next splash.
		if (millis() > splashDuration || isKeyReleased(KEY_OK))
			setNewState(STATE_ID_SPLASH, splash + 1);

		refreshGame();
	}

	public static final void sceneSplashInit(int arg) {
		/* Skip the Eidos legal line because it's already merged with Eidos
		 * logo splash. */
		if (arg == 1)
			arg++;

		splash = arg;

		switch (arg) {

			// Eidos logo.
			case 0:
				imgCenterLogo = loadImage("eidos.pim", "eidos.ppl");
				splashDuration = millis() + 2500L;

			// Eidos legal line.
			case 1:
				imgEidosLegalLine = loadImage("eidos_legal_line.pim", "eidos_legal_line.ppl");
				splashDuration = millis() + 2500L;
				return;

			// Progressive Media logo.
			case 2:

			// Progressive Media animation.
			case 3:
				if (imgCenterLogo == null)
					imgCenterLogo = loadImage("pmback.pim", "pmback.ppl");

				if (imgPmRocket == null)
					imgPmRocket = loadImage("pmrocket.pim", "pmrocket.ppl");

				splashRocketX = ((SCREEN_WIDTH - 68) >> 1) + 1;
				splashRocketY = ((SCREEN_HEIGHT - 84) >> 1) - 15;
				splashTick = 0;
				splashDuration = millis() + 2000L;
				return;

			// Maniacs of Noice logo scaling up.
			case 4:
				splashRocketX = -68;
				splashRocketY = SCREEN_HEIGHT;
				splashTick = 0;
				splashDuration = millis() + 10000L;
				return;

			// Maniacs of Noise logo.
			case 5:
				imgCenterLogo = loadImage("mon.pim", "mon.ppl");
				splashDuration = millis() + 2000L;
				return;

			// Enter the title.
			default:
				setNewState(STATE_ID_TITLE, 0);
				isLoadingBarShown = true;
		}
	}

	public static final void sceneSplashFree() {
		switch (splash) {

			// Eidos logo.
			case 0:
				imgCenterLogo = null;

			// Eidos legal line.
			case 1:
				imgEidosLegalLine = null;

			// Progressive Media logo.
			case 2:
				break;

			// Progressive Media animation.
			case 3:
				imgCenterLogo = null;
				break;

			// Maniacs of Noice logo scaling up.
			case 4:
				imgPmRocket = null;
				break;

			// Maniacs of Noise logo.
			case 5:
				imgCenterLogo = null;
				break;

			// Enter the title.
			default:
				imgCenterLogo = null;
				imgEidosLegalLine = null;
				imgPmRocket = null;
		}

		garbageCollector();
	}

	public static final void sceneShipRun() {
		// Animate beam.
		if (isBeamAnimated) {
			renderBeamAnimation(beamAnimationTicks);
			beamAnimationTicks += 4;

			if (beamAnimationTicks >= 130)
				isBeamAnimated = false;

			return;
		}

		// Start intro script.
		if (scriptIsAwaitingLevelStart) {
			scriptIsAwaitingLevelStart = false;
			startScript("intro" + level + ".bms", levelCircleX[0], levelCircleY[0]);

			return;
		}

		// Process ship operation after a pod was touched.
		if (shipNextOp > SHIP_OP_NOP) {
			switch (shipNextOp) {
				case SHIP_OP_START:
					setNewState(STATE_ID_PRE_LEVEL, 0);
					break;

				case SHIP_OP_EXIT:
					if (scriptIsActionConfirmed)
						setNewState(-1, 0);
					break;

				case SHIP_OP_RESTART:
					if (scriptIsActionConfirmed) {
						nextLevelToLoad = currentLevelLoaded;
						setNewState(STATE_ID_LEVEL, 0);
						swapLevelData(1);
					}
					break;

				case SHIP_OP_HOLODECK:
					activateLevel(-1);
					levelPlayerHealth = 500;
					levelIntroTicks = 120;
					setNewState(STATE_ID_LEVEL, 0);
					break;

				case SHIP_OP_RESET:
					if (scriptIsActionConfirmed) {
						level = 0;
						isMirrored = false;
						saveRecordData();
						isShipPaused = false;
						activeSwapKey = -1;
						setNewState(STATE_ID_TITLE, 0);
						return;
					}
					break;

				case SHIP_OP_RESUME:
					setNewState(STATE_ID_LEVEL, 0);
					swapLevelData(1);
			}

			shipNextOp = SHIP_OP_NOP;

			return;
		}

		// Process all physics in the ship.

		/* If the player landed, set next ship operation that will be processed
		 * in the next frame. */
		if (levelIsPlayerOnSurface && levelCircleY[0] > 0x960000 && shipPodChoiceId > 0) {
			shipNextOp = processShipScript(shipPodChoiceId, isShipPaused);
			shipPodChoiceId = 0;
		}

		if (!isBeamAnimated) {
			updateLevel();
			updatePlayerControls();
		}

		if (levelShipTouchedCircleType > 0) {
			if (levelShipTouchedCircleType == shipCheckPodId)
				levelShipTouchedCircleType = 0;
			else
				// Remember newly touched circle...
				shipCheckPodId = levelShipTouchedCircleType;
		}

		// ...if landed, initialize it to 0, because shipNextOp will be set.
		if (levelIsPlayerOnSurface)
			shipCheckPodId = 0;

		if (levelShipTouchedCircleType > 0)
			shipPodChoiceId = levelShipTouchedCircleType;

		levelShipTouchedCircleType = 0;

		// Lock vertical camera position to the center.
		levelSetCamera(levelCircleX[0], levelHeight / 2, 0);
		// Set Waz position.
		levelSetPlayerPos(levelCircleX[6], levelCircleY[6], 6, 0x140000);
		renderShip(isShipPaused);

		refreshGame();
	}

	public static final void sceneShipInit(int arg) {
		loadRecordData();

		if (level >= 25) {
			// It's all over...
			isMirrored = !isMirrored;
			level = 0;
			saveRecordData();
			setNewState(STATE_ID_TITLE, 0);
		} else {
			if (activeSwapKey != 0)
				initShip();

			if (showControlsGuide) {
				showControlsGuide = false;
				startScript("intro0controls.bms", levelCircleX[0], levelCircleY[0]);
			}

			scriptIsAwaitingLevelStart = false;

			// Show intro script.
			if (arg == 10) {
				if (isBeamAnimated) {
					scriptIsAwaitingLevelStart = true;
					beamAnimationTicks = 1;
					levelSetCamera(levelCircleX[6], levelHeight / 2, 0);
				} else if (skipPrologue) {
					// Welcome a player, then immediatelly enter the ship.
					skipPrologue = false;
					startScript("continueStory.bms", levelCircleX[0], levelCircleY[0]);
					scriptIsAwaitingLevelStart = true;
				} else {
					// Show a prologue.
					startScript("intro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					if (level == 0)
						showControlsGuide = true;
				}
			}

			levelShipTouchedCircleType = 0;
			isPlayerAlive = true;
			initSpace(2, 12, 128, 64); // For the ship window.
			initInsideShip();
			loadFaces();

			if (textTableShip == null) {
				// Pod labels.
				textTableShip = loadTextTableFromIndex(6, -1);
				textTableShipPause = loadTextTableFromIndex(7, -1);
			}

			if (!isShipPaused)
				playSound(SOUND_ID_SHIP);
		}
	}

	public static final void sceneShipFree() {
		if (newStateIndex != 5) {
			playSound(-1);
			garbageCollector();
		}
	}

	public static final void sceneScriptRun() {
		long curTime = millis();

		if (!scriptIsWaiting) {

			/* Execute a next chunk of the script if time for previous chunk
			 * has expired. */
			currentScriptIndex = execScript(currentScript, currentScriptIndex, false);

		} else {

			/* Wait for the chunk to complete execution. */
			if (!isSpeakingAnimationPlaying && !scriptIsOkKeyEnabled && scriptWaitEndMs == 0L) {
				if (scriptEnvironment == SCRIPT_ENV_SPACE_MAP) {

					if (renderSpaceMapNextFrame())
						scriptIsWaiting = false;

				} else if (!scriptIsCameraMoving || levelCameraApproach(scriptCameraTargetX, scriptCameraTargetY, 0, 0x320000)) {

					scriptIsWaiting = false;
					scriptIsCameraMoving = false;

				}
			}

			if (scriptWaitEndMs != 0L && scriptWaitEndMs < curTime)
				scriptWaitEndMs = 0L;

			if (scriptIsOkKeyEnabled && isKeyPressed(KEY_OK))
				scriptIsOkKeyEnabled = false;

			if (isSpeakingAnimationPlaying && dialogueDone)
				isSpeakingAnimationPlaying = false;

		}

		// Process an environment in the script.
		switch (scriptEnvironment) {
			case SCRIPT_ENV_SHIP:
				renderShipInside();

				if (scriptIsWazActive && !isShipPaused)
					renderPlayable(6, 0xc80000, true);

				renderPlayable(0, 0xff, true);
				renderShipDecor(shipDecorForeground);
				break;

			case SCRIPT_ENV_LEVEL:
				if (levelCameraZoom != LEVEL_CAMERA_ZOOM_DEFAULT) {
					scriptCameraZoomApproach += ((LEVEL_CAMERA_ZOOM_DEFAULT * 1000) - scriptCameraZoomApproach) / 10;
					levelCameraZoom = scriptCameraZoomApproach / 1000;
				}

				if (levelPlayerAngle < 180) {
					levelPlayerAngle = levelPlayerAngle * 90 / 100;
				} else {
					levelPlayerAngle -= 360;
					levelPlayerAngle = levelPlayerAngle * 90 / 100;
					levelPlayerAngle += 360;
				}

				levelRender(true);
				break;

			case SCRIPT_ENV_RESERVED:
				break;

			case SCRIPT_ENV_SPACE_MAP:
				renderSpaceMap();
				break;

			case SCRIPT_ENV_OUTSIDE:
				renderOutside();
				break;

			case SCRIPT_ENV_TRANSMISSION:
				renderTransmission();
		}

		// Black bars.
		if (scriptShowBlackBars) {
			gSetColor(0);
			gFillRect(0, 0, SCREEN_WIDTH, 32);
			gFillRect(0, SCREEN_HEIGHT - 32, SCREEN_WIDTH, 37);
		}

		// Process dialogue script box.
		renderDialogueBox();
		if (scriptSelectionEnabled) {

			// YES or NO.

			int key = softkeyPressed(2, 4);
			if (key >= 0) {
				// Walk through the entire script until EOF.
				scriptIsActionConfirmed = key == 2;

				while (currentScriptIndex < currentScript.length)
					currentScriptIndex = execScript(currentScript, currentScriptIndex, true);

				scriptIsWaiting = false;
			}

		} else if (scriptIsOkKeyEnabled && softkeyPressed(2, 3) == 3 ||
				!scriptIsOkKeyEnabled && softkeyPressed(-1, 3) == 3) {

			// OK or SKIP (or only SKIP).

			// Walk through the entire script until EOF.
			while (currentScriptIndex < currentScript.length)
				currentScriptIndex = execScript(currentScript, currentScriptIndex, true);

			scriptIsWaiting = false;
			setNewState(STATE_ID_TRANSITION, 100 + stateBeforeScript);

			return;

		}

		refreshGame();

		// Return to the previous scene, and exit the script.
		if (currentScriptIndex >= currentScript.length && !scriptIsWaiting)
			setNewState(stateBeforeScript, 0);
	}

	public static final void sceneScriptInit(int arg) {
		scriptCameraZoomApproach = levelCameraZoom * 1000;

		if (currentScript == null) {

			setNewState(stateBeforeScript, 0);

		} else if (!scriptIsContinued) {

			currentScriptIndex = 0;
			scriptIsActionConfirmed = false;
			stateBeforeScript = oldState;
			activePhotoFrameLabelId = 0;
			scriptLastSwapKey = activeSwapKey;
			scriptIsWazActive = scriptLastSwapKey == 0;
			if (scriptIsWazActive)
				scriptEnvironment = SCRIPT_ENV_SHIP;
			else
				scriptEnvironment = SCRIPT_ENV_LEVEL;

			resetScript();
			loadEyes();
			loadFacesIcons();
			scriptIsWaiting = false;
			scriptWaitEndMs = 0L;
			scriptIsCameraMoving = false;
			isSpeakingAnimationPlaying = false;
			scriptIsOkKeyEnabled = false;

		} else {

			/* We don't need to load the resources again as we are continuing
			 * the script. */
			scriptIsContinued = false;

		}
	}

	public static final void sceneScriptFree() {
		if (!scriptIsContinued || currentScriptIndex >= currentScript.length) {
			scriptIsContinued = false;
			currentScript = null;
			garbageCollector();
			swapLevelData(scriptLastSwapKey);
			resetScript();
		}
	}

	public static final void resetScript() {
		scriptShowBlackBars = false;
		shipAlarmRadius = 0;
		spaceMapPlanetBeaconRadius = 0;
		currentFace[0] = 0;
		currentFace[1] = 0;
	}

	public static final void startScript(String filename, int originX, int originY) {
		startScript(filename, originX, originY, false);
	}

	public static final void startScript(String filename, int originX, int originY, boolean enableSelection) {
		isLoadingBarShown = false;
		int[] data = loadFile32(filename);
		isLoadingBarShown = true;

		if (data != null)
			startScript(data, originX, originY, enableSelection);
	}

	public static final void levelDisplayScript(int circleId) {
		if (levelPlayerHealth > 0) {
			int index = levelCircleType[circleId] - 10;
			startScript("ingame" + level + "-" + index + ".bms", levelCircleX[circleId], levelCircleY[circleId], false);
			levelCircleFlags[circleId] = 0;
		}
	}

	public static final void startScript(int[] data, int originX, int originY, boolean enableSelection) {
		scriptOriginX = originX;
		scriptOriginY = originY;
		scriptSelectionEnabled = enableSelection;

		currentScript = data;
		hideDialogueBox();

		setNewState(STATE_ID_SCRIPT, 0);
	}

	/* The BMS script is divided into several independent parts (chunks). Each
	 * part ends with `SCRIPT_CMD_SUBMIT` command. */
	public static final int execScript(int[] data, int offset, boolean skipping) {
		while (offset < data.length) {
			switch (data[offset]) {
				// Submit.
				case SCRIPT_CMD_SUBMIT:
					return offset + 1;

				// Wait n*10 milliseconds.
				case SCRIPT_CMD_WAIT:
					scriptIsWaiting = true;
					scriptWaitEndMs = millis() + (long)(data[offset + 1] * 10);
					offset += 2;
					break;

				// Wait for the user to click OK key.
				case SCRIPT_CMD_WAIT_OK:
					scriptIsWaiting = true;
					scriptIsOkKeyEnabled = true;
					offset++;
					break;

				// Set camera position.
				case SCRIPT_CMD_SET_CAMERA:
					// When in the space map.
					if (scriptEnvironment == SCRIPT_ENV_SPACE_MAP) {
						if (data[offset + 1] == 6)
							spaceMapSetCameraToBeacon();

						if (data[offset + 1] == 5)
							spaceMapSetCameraInstant(0, 0);

						offset += 2;
						break;
					}

					// When in the level.
					switch (data[offset + 1]) {

						// Set to the player.
						case 0:
							scriptCameraTargetX = levelCircleX[0];
							scriptCameraTargetY = levelCircleY[0];
							offset += 2;
							break;

						// Set to Wiz or Waz position (generally in the ship).
						case 1:
							if (scriptIsWazActive) {
								scriptCameraTargetX = levelCircleX[6];
								scriptCameraTargetY = levelCircleY[6];
							} else {
								scriptCameraTargetX = levelCircleX[0];
								scriptCameraTargetY = levelCircleY[0];
							}

							offset += 2;
							break;

						// Set to the origin.
						case 5:
							scriptCameraTargetX = scriptOriginX;
							scriptCameraTargetY = scriptOriginY;
							offset += 2;
							break;

						// Pull camera position from the script data.
						default:
							scriptCameraTargetX = levelAlignXToMirror(data[offset + 1] << 16);
							scriptCameraTargetY = data[offset + 2] << 16;
							offset += 3;
					}

					levelSetCamera(scriptCameraTargetX, scriptCameraTargetY, 0);
					break;

				// Set camera position with a smooth movement.
				case SCRIPT_CMD_SET_CAMERA_SMOOTH:
					scriptIsCameraMoving = true;

					// When in the space map.
					if (scriptEnvironment == SCRIPT_ENV_SPACE_MAP) {
						if (data[offset + 1] == 6)
							spaceMapSetTargetCameraToBeacon();

						if (data[offset + 1] == 5)
							spaceMapSetTargetCamera(0, 0);

						offset += 2;
						scriptIsWaiting = true;
						break;
					}

					// When in the level.
					switch (data[offset + 1]) {

						// Set to the player.
						case 0:
							scriptCameraTargetX = levelCircleX[0];
							scriptCameraTargetY = levelCircleY[0];
							offset += 2;
							break;

						// Set to Wiz or Waz position.
						case 1:
							if (scriptIsWazActive) {
								scriptCameraTargetX = levelCircleX[6];
								scriptCameraTargetY = levelCircleY[6];
							} else {
								scriptCameraTargetX = levelCircleX[0];
								scriptCameraTargetY = levelCircleY[0];
							}

							offset += 2;
							break;

						// Set to the origin.
						case 5:
							scriptCameraTargetX = scriptOriginX;
							scriptCameraTargetY = scriptOriginY;
							offset += 2;
							break;

						// Pull camera position from the script data.
						default:
							scriptCameraTargetX = levelAlignXToMirror(data[offset + 1] << 16);
							scriptCameraTargetY = data[offset + 2] << 16;
							offset += 3;
					}

					scriptIsWaiting = true;
					break;

				// Set current text in script (and who speaks it).
				case SCRIPT_CMD_SET_TEXT:
					setCurrentDialogue(getText(data[offset + 1]), data[offset + 2]);
					isSpeakingAnimationPlaying = true;
					scriptIsWaiting = true;
					offset += 3;
					break;

				// Set a face to Wiz or Waz.
				case SCRIPT_CMD_SET_FACE:
					currentFace[data[offset + 1]] = data[offset + 2];
					if (scriptSfx[data[offset + 2]] != -1 && !skipping)
						playSound(scriptSfx[data[offset + 2]], 1);

					offset += 3;
					break;

				/* Set entity position (can be either a player or entity in the
				 * transmission). */
				case SCRIPT_CMD_SET_ENTITY_POS:
					if (scriptEnvironment == SCRIPT_ENV_TRANSMISSION) {
						transmissionEntityX = SCREEN_WIDTH * data[offset + 2] / 1000;
						transmissionEntityY = SCREEN_HEIGHT * data[offset + 3] / 1000;
					} else if (activeSwapKey == 0) {
						if (data[offset + 1] == 0)
							levelSetPlayerPos(levelAlignXToMirror(data[offset + 2] << 16), data[offset + 3] << 16, 0, 0xc0000);
						else
							levelSetPlayerPos(levelAlignXToMirror(data[offset + 2] << 16), data[offset + 3] << 16, 6, 0x140000);
					}

					offset += 4;
					break;

				// Set a new scene with the transition.
				case SCRIPT_CMD_SET_STATE:
					if (!skipping) {
						scriptIsContinued = true;
						setNewState(STATE_ID_TRANSITION, data[offset + 1]);
						offset += 2;
						return offset;
					}

					offset += 2;
					break;

				// Set script environment.
				case SCRIPT_CMD_SET_ENV:
					scriptEnvironment = data[offset + 1];
					hideDialogueBox();

					switch (scriptEnvironment) {
						// Ship.
						case SCRIPT_ENV_SHIP:
							initSpace(2, 12, 128, 46);
							swapLevelData(0);
							levelSetPlayerPos(levelCircleX[6], levelCircleY[6], 6, 0x140000);
							fade = 100;
							levelCameraZoom = LEVEL_CAMERA_ZOOM_DEFAULT;
							break;

						// Level.
						case SCRIPT_ENV_LEVEL:
							if (!scriptIsWazActive)
								swapLevelData(1);
							break;

						case SCRIPT_ENV_RESERVED:
							break;

						// Space map.
						case SCRIPT_ENV_SPACE_MAP:
							initSpaceMap(data[offset + 2] * LEVEL_CAMERA_ZOOM_DEFAULT / 100, data[offset + 3] * LEVEL_CAMERA_ZOOM_DEFAULT / 100, data[offset + 4]);
							initSpace(1, 150, SCREEN_WIDTH << 1, SCREEN_HEIGHT << 1);
							offset += 3;
							break;

						// Space.
						case SCRIPT_ENV_OUTSIDE:
							initSpace(1, 50, SCREEN_WIDTH, SCREEN_HEIGHT);
							break;

						case SCRIPT_ENV_TRANSMISSION:
							break;
					}

					offset += 2;
					break;

				// Hide dialogue script box.
				case SCRIPT_CMD_HIDE_BOX:
					hideDialogueBox();
					isSpeakingAnimationPlaying = false;
					offset++;
					break;

				// Show black bars.
				case SCRIPT_CMD_SET_BARS:
					scriptShowBlackBars = data[offset + 1] == 1;
					offset += 2;
					break;

				// Enable one of the alarm visual signals.
				case SCRIPT_CMD_SET_ALARM:
					if (scriptEnvironment == SCRIPT_ENV_SPACE_MAP)
						spaceMapPlanetBeaconRadius = data[offset + 1] == 1 ? 1 : 0;

					if (scriptEnvironment == SCRIPT_ENV_SHIP) {
						int rad = shipAlarmRadius = data[offset + 1] == 1 ? 1 : 0;
						if (rad > 0 && !skipping)
							playSfxDistress();
					}

					offset += 2;
					break;

				// Space map: Show ship or planet pointer.
				case SCRIPT_CMD_SPACE_MAP_SHOW_POINTER:
					if (data[offset + 1] == 5)
						spaceMapShowShip = data[offset + 2] == 1;
					else
						spaceMapShowPlanet = data[offset + 2] == 1;

					offset += 3;
					break;

				// Transmission: Set interference.
				case SCRIPT_CMD_TRANSMISSION_SET_NOISE:
					isTransmissionNoisy = data[offset + 1] == 1;
					offset += 2;
					break;

				// Transmission: Set shake.
				case SCRIPT_CMD_TRANSMISSION_SET_SHAKE:
					isTransmissionShaky = data[offset + 1] == 1;
					offset += 2;
					break;

				// Photo frame: Show a photo frame.
				case SCRIPT_CMD_PHOTO_FRAME_SHOW:
					isPhotoFrameShown = data[offset + 1] == 1;
					offset += 2;
					break;

				// Transmission: Set background color.
				case SCRIPT_CMD_TRANSMISSION_SET_BACKGROUND_COLOR:
					transmissionBackgroundColor = data[offset + 1];
					offset += 2;
					break;

				// Reserved.
				case SCRIPT_CMD_RESERVED:
					break;

				// Photo frame: Set a label ID (see case 16).
				case SCRIPT_CMD_PHOTO_FRAME_SET_LABEL_ID:
					activePhotoFrameLabelId = data[offset + 1];
					offset += 2;
			}
		}

		return offset;
	}

	public static final void sceneSelectionRun() {
		int index = getSelectedIndex();
		if (index >= 0) {
			if (sceneSelectionState == SELECTION_STATE_LANGUAGE) {
				language = index;
				saveRecordData();
				setTextTableCrc((new short[] {
					(short)0x0085,
					(short)0x9e91,
					(short)0xdc77,
					(short)0x0d48,
					(short)0x4f2a
				})[language]);
				setNewState(STATE_ID_SELECTION, 1);
			} else {
				isAudioEnabled = index == 1 || index == 3;
				loadAllSounds();
				setNewState(STATE_ID_SPLASH, 0);
			}
		}
	}

	public static final void sceneSelectionInit(int arg) {
		fade = 100;
		sceneSelectionState = arg;

		if (sceneSelectionState == SELECTION_STATE_LANGUAGE) {
			loadSelectionOptions(new String[] {
				"flag_uk",
				"flag_france",
				"flag_germany",
				"flag_spain",
				"flag_italy"
			}, language);
		}

		if (sceneSelectionState == SELECTION_STATE_AUDIO) {
			loadSelectionOptions(new String[] {
				"audio_off",
				"audio_on",
				"audio_off",
				"audio_on"
			}, 0);
		}
	}

	public static final void sceneSelectionFree() {
		fade = 100;
		sceneSelectionCleanup();
	}

	public static final void sceneTitleRun() {
		renderOutside();

		// Sola
		gDrawImage(imgGameLogoTop, (SCREEN_WIDTH - 102) >> 1, ((SCREEN_HEIGHT - 64) >> 1) - 53, 0);

		// Rola
		gDrawImage(imgGameLogoBottom, (SCREEN_WIDTH - 88) >> 1, ((SCREEN_HEIGHT - 59) >> 1) + 50, 0);

		// "press 5" string.
		if ((millis() & 512L) > 0L)
			renderText(-1000, SCREEN_HEIGHT - 19, titleText, 0);

		// OK softkey.
		if (softkeyPressed(2, -1, true) == 2) {
			isPastSplash = true;
			setNewState(STATE_ID_TRANSITION, 2);
		}

		refreshGame();
	}

	public static final void sceneTitleInit(int arg) {
		if (isPastSplash) {
			isPastSplash = false;
			setNewState(STATE_ID_SHIP, 10);
		} else {
			if (titleText == null)
				titleText = getText(0x1000f);

			imgGameLogoTop = loadImage("gamelogo.pim", "gamelogo.ppl");
			imgGameLogoBottom = loadImage("gamelogo2.pim", "gamelogo2.ppl");

			initSpace(1, 50, SCREEN_WIDTH, SCREEN_HEIGHT);
			playSound(SOUND_ID_TITLE);

			isBeamAnimated = false;
		}
	}

	public static final void sceneTitleFree() {
		imgGameLogoTop = null;
		imgGameLogoBottom = null;

		garbageCollector();
		playSound(-1);
	}

	public static final void sceneTransitionRun() {
		/* refreshGame() calls updateTransition() on it's own even when
		 * curState is not STATE_ID_TRANSITION. */
		refreshGame();
	}

	public static final void renderTransition() {
		if (!sceneTransitionDisabled) {
			if (sceneTransitionStartMs == -1L)
				sceneTransitionStartMs = millis();

			int elapsed = (int)(millis() - sceneTransitionStartMs);
			int tick = sceneTransitionOffset + elapsed * 1000 / sceneTransitionInvCoeff;

			if (sceneTransitionOffset == 0 && tick > 1000) {
				// Middle of the transition.
				tick = 1000;
				setNewState(sceneTransitionNextState, 1);
			}

			if (tick > 2000) {
				// Transition complete.
				sceneTransitionDisabled = true;
			} else {
				// Render it the rest of the time.
				switch (sceneTransitionType) {
					case TRANSITION_TYPE_WIPE:
						renderWipeTransition(tick);
						break;

					case TRANSITION_TYPE_INSTANT:
						fillScreenBlack(tick);
				}
			}
		}
	}

	public static final void sceneTransitionInit(int arg) {
		sceneTransitionOffset = 0;
		sceneTransitionDisabled = false;
		sceneTransitionStartMs = millis();

		// Save an old next state.
		int savedState = sceneTransitionNextState;
		sceneTransitionNextState = oldState;

		if (arg >= 100) {
			sceneTransitionNextState = arg - 100;
			arg = TRANSITION_TYPE_INSTANT;
		}

		sceneTransitionType = arg;

		// Leave more time for a wipe-transition.
		sceneTransitionInvCoeff = 250;
		if (arg == TRANSITION_TYPE_INSTANT)
			sceneTransitionInvCoeff = 150;

		/* If `arg` says that next state will be STATE_ID_TRANSITION, set
		 * `savedState` as a new state. */
		if (sceneTransitionNextState == STATE_ID_TRANSITION)
			sceneTransitionNextState = savedState;
	}

	public static final void sceneTransitionFree() {
		sceneTransitionOffset = 1000;
		sceneTransitionStartMs = -1L;
	}

	public static final void fillScreenBlack(int arg) {
		int oldColor = gGetColor();

		gSetColor(0);
		gSetClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		gSetColor(oldColor);
	}

	public static final void renderWipeTransition(int tick) {
		// Represent black pixels in the width that cover the screen.
		int screenCover = SCREEN_WIDTH * tick / 1000;
		int oldColor = gGetColor();

		gSetColor(0);
		gSetClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gFillRect(screenCover - SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Current vertical line position.
		int curLineX = screenCover + 1;

		if (tick > 1000) {
			curLineX = SCREEN_WIDTH + 1;

			for (int i = 6; i > 0; i--) {
				gFillRect(screenCover - curLineX - i, 0, i, SCREEN_HEIGHT);
				curLineX += i + (8 - i);
			}
		} else {
			for (int i = 6; i > 0; i--) {
				gFillRect(curLineX, 0, i, SCREEN_HEIGHT);
				curLineX += i + (8 - i);
			}
		}

		gSetColor(oldColor);
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

			for (int i = 0; i < imgsMouths.length; i++) {
				imgsMouths[i] = new Image[2];
				imgsMouths[i][0] = loadImage("mouthWiz_" + i + ".pim", "mouthWiz_" + i + ".ppl");
				imgsMouths[i][1] = loadImage("mouthWaz_" + i + ".pim", "mouthWaz_" + i + ".ppl");
			}
		}
	}

	public static final void loadEyes() {
		if (imgsEyes == null) {
			imgsEyes = new Image[8][];
			for (int i = 1; i < imgsEyes.length; i++) {
				imgsEyes[i] = new Image[2];
				imgsEyes[i][0] = loadImage("eyesWiz_" + i + ".pim", "eyesWiz_" + i + ".ppl");
				imgsEyes[i][1] = loadImage("eyesWaz_" + i + ".pim", "eyesWaz_" + i + ".ppl");
			}

			imgsEyesC = new Image[8][];
			for (int i = 1; i < imgsEyesC.length; i++) {
				imgsEyesC[i] = new Image[2];
				imgsEyesC[i][0] = loadImage("eyesWiz_C" + i + ".pim", "eyesWiz_C" + i + ".ppl");
				imgsEyesC[i][1] = loadImage("eyesWaz_C" + i + ".pim", "eyesWaz_C" + i + ".ppl");
			}
		}
	}

	public static final void initPlayer(boolean isWaz) {
		levelPlayerHealth = 500;
		if (isWaz) {
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
		levelPlayerIgnoreGrabberId = -1;
	}

	public static final void levelSetPlayerPos(int x, int y, int circleId, int size) {
		levelCircleX[circleId + 0] = x;
		levelCircleY[circleId + 0] = y;
		levelCircleX[circleId + 1] = x;
		levelCircleY[circleId + 1] = y - size;
		levelCircleX[circleId + 2] = x;
		levelCircleY[circleId + 2] = y + size;
		levelCircleX[circleId + 3] = x - size;
		levelCircleY[circleId + 3] = y;
		levelCircleX[circleId + 4] = x + size;
		levelCircleY[circleId + 4] = y;

		for (int i = circleId; i < circleId + 5; i++) {
			levelCirclePrevX[i] = levelCircleX[i];
			levelCirclePrevY[i] = levelCircleY[i];
		}
	}

	public static final void levelPlayerReleaseGrabber() {
		levelHookIsActive[16] = false;
		levelHookIsVisible[16] = false;
		levelCircleFlags[levelHookId2[16]] = levelPlayerCurrentGrabberFlags;
		levelPlayerCurrentGrabberFlags = -1;
		levelPlayerIgnoreGrabberId = levelHookId2[16];
		levelHookId2[16] = -1;
	}

	public static final void updatePlayerRotation() {
		// If the player is far enough from the last holded grabber.
		if (levelPlayerIgnoreGrabberId != -1 &&
				(levelCircleX[levelPlayerIgnoreGrabberId] - levelCircleRadius[levelPlayerIgnoreGrabberId] > levelCircleX[0] + 0x190000 ||
				 levelCircleX[levelPlayerIgnoreGrabberId] + levelCircleRadius[levelPlayerIgnoreGrabberId] < levelCircleX[0] - 0x190000 ||
				 levelCircleY[levelPlayerIgnoreGrabberId] - levelCircleRadius[levelPlayerIgnoreGrabberId] > levelCircleY[0] + 0x190000 ||
				 levelCircleY[levelPlayerIgnoreGrabberId] + levelCircleRadius[levelPlayerIgnoreGrabberId] < levelCircleY[0] - 0x190000)) {

			levelPlayerIgnoreGrabberId = -1;
		}

		// Handle jump.
		if (levelPlayerJumpTicks > 0) {
			levelCircleY[0] -= 550000;
			levelPlayerJumpTicks--;
		}

		// Handle player rotational inertia.
		if (levelPlayerRotationRate > 0) {
			levelPlayerRotationRate--;
			if (levelPlayerRotationRate > 25)
				levelPlayerRotationRate = 25;
		}

		if (levelPlayerRotationRate < 0) {
			levelPlayerRotationRate++;
			if (levelPlayerRotationRate < -25)
				levelPlayerRotationRate = -25;
		}

		// Handle player rotation.
		levelPlayerAngle += levelPlayerRotationRate;
		if (levelPlayerAngle >= 360)
			levelPlayerAngle -= 360;

		if (levelPlayerAngle < 0)
			levelPlayerAngle += 360;

		// Smoothly return rotation angle to 0.
		if (levelPlayerRotationRate == 0) {
			byte coeff = 95;
			if (!levelIsPlayerOnSurface)
				coeff = 90;

			if (levelPlayerAngle < 180) {
				levelPlayerAngle = levelPlayerAngle * coeff / 100;
			} else {
				levelPlayerAngle -= 360;
				levelPlayerAngle = levelPlayerAngle * coeff / 100;
				levelPlayerAngle += 360;
			}

			if (levelPlayerAngle >= 360)
				levelPlayerAngle -= 360;

			if (levelPlayerAngle < 0)
				levelPlayerAngle += 360;
		}
	}

	public static final void levelRenderPurple(int x, int y) {
		int alignX = levelAlignX(x, y);
		int alignY = levelAlignY(x, y);
		int width = levelCameraZoom * 10 / 100;
		int height = levelCameraZoom * 25 / 100;

		if (!isOutOfScreenBounds(alignX - width, alignY - height, alignX + width, alignY + height)) {
			// Outline of the body.
			gSetColor(0);
			gFillArc(alignX - width - 3,
					alignY - height - 3,
					2 * (width + 3),
					2 * (height + 3),
					0,
					360);

			// Purple's body.
			gSetColor(0xa020f0);
			gFillArc(alignX - width,
					alignY - height,
					2 * width,
					2 * height,
					0,
					360);

			// Mouth.
			gDrawImage(imgMouth, alignX - imgMouth.getWidth() / 2, alignY - imgMouth.getHeight() / 2, 0);
			alignX -= imgsEyeM[1].getWidth() / 2;

			// Eyes.
			gDrawImage(imgsEyeM[1], alignX - width, alignY - height * 2 / 3, 0);
			gDrawImage(imgsEyeM[1], alignX, alignY - height * 2 / 3, 0);
			gDrawImage(imgsEyeM[1], alignX + width, alignY - height * 2 / 3, 0);
		}
	}

	public static final void levelRenderPlayer() {
		if (levelPlayerHealth > 0)
			levelPlayerRed = 100 + levelPlayerHealth * 100 / 500;

		// Blink the color of the blob if we got a damage.
		if ((levelPlayerHitTicks & 1) > 0)
			renderPlayable(0, 0, false);
		else
			renderPlayable(0, (levelPlayerRed << 16) + (levelPlayerGreen << 8) + levelPlayerBlue, false);
	}

	public static final void renderPlayable(int circleId, int color, boolean dontRotate) {
		byte playerId = 0;
		if (circleId > 0 || circleId == 0 && activeSwapKey == 1)
			playerId = 1;

		if (playerId == 0) {
			levelPlayerEyeVectorMagnitude = 18;
			color = 0xff;
			if (currentLevelLoaded == -1)
				color = 100 + levelPlayerHealth * 155 / 500 & 255;
		} else {
			levelPlayerEyeVectorMagnitude = 13;
		}

		int alignX = levelAlignX(levelCircleX[circleId], levelCircleY[circleId]);
		int alignY = levelAlignY(levelCircleX[circleId], levelCircleY[circleId]);
		int[] hitboxPos = new int[4];

		// Rotate hitbox points if the camera is rotated significantly.
		if (levelCameraAngle > 45 && levelCameraAngle < 315) {
			if (levelCameraAngle >= 45 && levelCameraAngle <= 135) {
				// 90 deg left.
				hitboxPos[0] = levelCircleX[circleId + 0] - levelCircleX[circleId + 3] >> 16;
				hitboxPos[1] = levelCircleY[circleId + 0] - levelCircleY[circleId + 1] >> 16;
				hitboxPos[2] = levelCircleX[circleId + 4] - levelCircleX[circleId + 0] >> 16;
				hitboxPos[3] = levelCircleY[circleId + 2] - levelCircleY[circleId + 0] >> 16;
			} else if (levelCameraAngle >= 135 && levelCameraAngle <= 225) {
				// 180 deg.
				hitboxPos[0] = levelCircleY[circleId + 2] - levelCircleY[circleId + 0] >> 16;
				hitboxPos[1] = levelCircleX[circleId + 0] - levelCircleX[circleId + 3] >> 16;
				hitboxPos[2] = levelCircleY[circleId + 0] - levelCircleY[circleId + 1] >> 16;
				hitboxPos[3] = levelCircleX[circleId + 4] - levelCircleX[circleId + 0] >> 16;
			} else {
				// 90 deg right.
				hitboxPos[0] = levelCircleX[circleId + 4] - levelCircleX[circleId + 0] >> 16;
				hitboxPos[1] = levelCircleY[circleId + 2] - levelCircleY[circleId + 0] >> 16;
				hitboxPos[2] = levelCircleX[circleId + 0] - levelCircleX[circleId + 3] >> 16;
				hitboxPos[3] = levelCircleY[circleId + 0] - levelCircleY[circleId + 1] >> 16;
			}
		} else {
			// 0 deg.
			hitboxPos[0] = levelCircleY[circleId + 0] - levelCircleY[circleId + 1] >> 16;
			hitboxPos[1] = levelCircleX[circleId + 4] - levelCircleX[circleId + 0] >> 16;
			hitboxPos[2] = levelCircleY[circleId + 2] - levelCircleY[circleId + 0] >> 16;
			hitboxPos[3] = levelCircleX[circleId + 0] - levelCircleX[circleId + 3] >> 16;
		}

		// Align them to a current camera zoom value.
		for (int i = 0; i < 4; i++)
			hitboxPos[i] = levelCameraZoom * hitboxPos[i] / 100;

		int[] hitboxPos2x = new int[4];

		for (int i = 0; i < 4; i++)
			hitboxPos2x[i] = hitboxPos[i] << 1;

		// Blob's outline.
		setFadeColor(0);
		gSetClip(alignX, 0, 200, alignY);
		gFillArc(alignX - hitboxPos[1] - 3, alignY - hitboxPos[0] - 3, hitboxPos2x[1] + 6, hitboxPos2x[0] + 6, 0, 360);
		gSetClip(0, 0, alignX, alignY);
		gFillArc(alignX - hitboxPos[3] - 3, alignY - hitboxPos[0] - 3, hitboxPos2x[3] + 6, hitboxPos2x[0] + 6, 90, 360);
		gSetClip(0, alignY, alignX, 200);
		gFillArc(alignX - hitboxPos[3] - 3, alignY - hitboxPos[2] - 3, hitboxPos2x[3] + 6, hitboxPos2x[2] + 6, 180, 360);
		gSetClip(alignX, alignY, 200, 200);
		gFillArc(alignX - hitboxPos[1] - 3, alignY - hitboxPos[2] - 3, hitboxPos2x[1] + 6, hitboxPos2x[2] + 6, 270, 360);

		// Blob's body.
		setFadeColor(color);
		gSetClip(alignX, 0, 200, alignY);
		gFillArc(alignX - hitboxPos[1], alignY - hitboxPos[0], hitboxPos[1] * 2, hitboxPos[0] * 2, 0, 360);
		gSetClip(0, 0, alignX, alignY);
		gFillArc(alignX - hitboxPos[3], alignY - hitboxPos[0], hitboxPos[3] * 2, hitboxPos[0] * 2, 90, 360);
		gSetClip(0, alignY, alignX, 200);
		gFillArc(alignX - hitboxPos[3], alignY - hitboxPos[2], hitboxPos[3] * 2, hitboxPos[2] * 2, 180, 360);
		gSetClip(alignX, alignY, 200, 200);
		gFillArc(alignX - hitboxPos[1], alignY - hitboxPos[2], hitboxPos[1] * 2, hitboxPos[2] * 2, 270, 360);

		gSetClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		if (fade > 50 && levelPlayerHealth > 0) {
			int playerAngle = levelPlayerAngle;
			if (dontRotate)
				playerAngle = 0;

			// Render a mouth.
			if (levelPlayerCurrentGrabberFlags >= 0) {
				Image img = imgMouth;
				gDrawImage(img, alignX - img.getWidth() / 2, alignY - img.getHeight() / 2, 0);
			} else {
				Image img = imgsMouths[currentFace[playerId]][playerId];

				if (currentFace[playerId] > 0) {
					gDrawImage(img, alignX - img.getWidth() / 2 + mouthX[currentFace[playerId]][playerId], alignY - img.getHeight() / 2 + mouthY[currentFace[playerId]][playerId], 0);
				} else {
					int mouthMag = levelCameraZoom * 6 / 100;
					int mouthX = alignX + cos(playerAngle + 90) * mouthMag / 1000;
					int mouthY = alignY + sin(playerAngle + 90) * mouthMag / 1000;
					gDrawImage(img, mouthX - img.getWidth() / 2, mouthY - img.getHeight() / 2, 0);
				}
			}

			// Render an eyes.
			if (currentFace[playerId] == 0) {
				Image img = imgsEyeM[playerId];

				if (circleId > 0) {
					// Waz looks at Wiz (ship).
					if (levelCircleX[0] + 0x160000 < levelCircleX[circleId])
						img = imgsEyeLeft[playerId];

					if (levelCircleX[0] - 0x160000 > levelCircleX[circleId])
						img = imgsEyeRight[playerId];
				} else {
					// Ordinary levels.
					if (levelCircleX[circleId] + 0x10000 < levelCirclePrevX[circleId])
						img = imgsEyeLeft[playerId];

					if (levelCircleX[circleId] - 0x10000 > levelCirclePrevX[circleId])
						img = imgsEyeRight[playerId];
				}

				// Closed eye.
				if (currentEyeBlinkTicks[playerId] > 0)
					img = imgsEyeC[playerId];

				// Rendering eyes.
				int eyeMag = levelCameraZoom * levelPlayerEyeVectorMagnitude / 100;
				int eyeX, eyeY;

				eyeX = alignX + cos(playerAngle - 115) * eyeMag / 1000;
				eyeY = alignY + sin(playerAngle - 115) * eyeMag / 1000;
				gDrawImage(img, eyeX - img.getWidth() / 2, eyeY - img.getHeight() / 2, 0);

				eyeX = alignX + cos(playerAngle - 65) * eyeMag / 1000;
				eyeY = alignY + sin(playerAngle - 65) * eyeMag / 1000;
				gDrawImage(img, eyeX - img.getWidth() / 2, eyeY - img.getHeight() / 2, 0);
			} else {
				int eyeMag = levelCameraZoom * levelPlayerEyeVectorMagnitude / 100;

				Image img = imgsEyes[currentFace[playerId]][playerId];
				if (currentEyeBlinkTicks[playerId] > 0)
					img = imgsEyesC[currentFace[playerId]][playerId];

				gDrawImage(img, alignX - img.getWidth() / 2 + eyesX[currentFace[playerId]][playerId], alignY - eyeMag - img.getHeight() / 2 + eyesY[currentFace[playerId]][playerId], 0);
			}

			// Handle pseudorandom eye blinking.
			if (currentEyeBlinkTicks[playerId] > 0 || rand8() % 50 == playerId) {
				if (currentEyeBlinkTicks[playerId] > 0)
					currentEyeBlinkTicks[playerId]--;
				else
					currentEyeBlinkTicks[playerId] = 3;
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

	public static final void renderBombDispenser(int x, int y) {
		int alignX = levelAlignX(x, y);
		int alignY = levelAlignY(x, y);

		if (levelBombStartTicks + 200 > levelTicks)
			gDrawImage(imgsBomb[1], alignX - 12, alignY - 16 - 3, 0);
		else
			gDrawImage(imgsBomb[1], alignX - 12, alignY - 16 - 7, 0);

		gDrawImage(imgsBomb[0], alignX - 20, alignY - 16, 0);
	}

	public static final void levelInitHooks(int num) {
		levelNumHooks = num;
		levelHookId1 = new int[num];
		levelHookId2 = new int[num];
		levelHookType = new int[num];
		levelHookExtent = new int[num];
		levelHookExtentSquare = new int[num];
		levelHookStretchConstant = new int[num];
		levelHookIsActive = new boolean[num];
		levelHookIsVisible = new boolean[num];
	}

	public static final void levelSetHook(int id, int circleId1, int circleId2, int type, int extent, int stretchConst, boolean isVisible, boolean isActive) {
		levelHookId1[id] = circleId1;
		levelHookId2[id] = circleId2;
		levelHookType[id] = type;
		levelHookExtent[id] = extent;
		levelHookExtentSquare[id] = lp32Mul(extent, extent);
		levelHookStretchConstant[id] = stretchConst;
		levelHookIsActive[id] = isActive;
		levelHookIsVisible[id] = isVisible;
	}

	public static final void levelDetachHooks(int id) {
		for (int i = 0; i < levelNumHooks; i++) {
			if (levelHookIsActive[i] && (levelHookId1[i] == id || levelHookId2[i] == id)) {

				levelHookIsActive[i] = false;

				if (levelHookId2[16] == levelHookId1[i] || levelHookId2[16] == levelHookId2[i])
					levelPlayerReleaseGrabber();

			}
		}
	}

	public static final void levelUpdateHooks() {
		for (int i = 0; i < levelNumHooks; i++) {
			if (levelHookIsActive[i]) {

				if (levelHookType[i] == 0) {

					// Lock X-axis.
					levelCircleX[levelHookId2[i]] = levelCircleX[levelHookId1[i]];

				} else if (levelHookType[i] == 1) {

					// Lock Y-axis.
					levelCircleY[levelHookId2[i]] = levelCircleY[levelHookId1[i]];

				} else if (levelHookType[i] == 6) {

					// Limit Y distance.
					if (levelCircleY[levelHookId1[i]] > levelCircleY[levelHookId2[i]] - levelHookExtent[i])
						levelCircleY[levelHookId1[i]] = levelCircleY[levelHookId2[i]] - levelHookExtent[i];

				} else if (levelHookType[i] == 5) {

					// Limit X distance.
					if (levelCircleX[levelHookId1[i]] > levelCircleX[levelHookId2[i]] - levelHookExtent[i])
						levelCircleX[levelHookId1[i]] = levelCircleX[levelHookId2[i]] - levelHookExtent[i];

				} else {

					// Elastic pull/push.
					onCircleTouchCircle(levelHookId1[i], levelHookId2[i], levelHookExtent[i], levelHookStretchConstant[i], levelHookType[i], false);

				}

			}
		}
	}

	public static final void onCircleTouchCircle(int circleId1, int circleId2, int extent, int stretchConst, int hookType, boolean process) {
		int triLeg1 = levelCircleX[circleId2] - levelCircleX[circleId1];
		int triLeg2 = levelCircleY[circleId2] - levelCircleY[circleId1];
		int mag = vectorMagnitude(triLeg1, triLeg2, extent);

		if (hookType != 4 || mag >= extent) {
			if (hookType != 3 || mag <= extent) {
				if (mag >= 70000) {
					int density1 = levelCircleWeight[circleId1];
					int density2 = levelCircleWeight[circleId2];

					int overlapRatio = lp32Div(mag - extent, lp32Mul(mag, density1 + density2));
					int pushX = lp32Mul(stretchConst, lp32Mul(triLeg1, overlapRatio));
					int pushY = lp32Mul(stretchConst, lp32Mul(triLeg2, overlapRatio));

					if (levelCircleHasPhysics[circleId1]) {
						levelCircleX[circleId1] += lp32Mul(pushX, density1);
						levelCircleY[circleId1] += lp32Mul(pushY, density1);
					}

					if (levelCircleHasPhysics[circleId2]) {
						levelCircleX[circleId2] -= lp32Mul(pushX, density2);
						levelCircleY[circleId2] -= lp32Mul(pushY, density2);
					}

					if (process)
						processCircles(circleId1, circleId2);
				}
			}
		}
	}

	public static final void swapLevelHookData() {
		int tmp1;
		int[] tmp2;
		boolean[] tmp3;

		tmp1 = levelNumHooks;
		levelNumHooks = swappedLevelNumHooks;
		swappedLevelNumHooks = tmp1;

		tmp2 = levelHookId1;
		levelHookId1 = swappedLevelHookId1;
		swappedLevelHookId1 = tmp2;

		tmp2 = levelHookId2;
		levelHookId2 = swappedLevelHookId2;
		swappedLevelHookId2 = tmp2;

		tmp2 = levelHookType;
		levelHookType = swappedLevelHookType;
		swappedLevelHookType = tmp2;

		tmp2 = levelHookExtent;
		levelHookExtent = swappedLevelHookExtent;
		swappedLevelHookExtent = tmp2;

		tmp2 = levelHookExtentSquare;
		levelHookExtentSquare = swappedLevelHookExtentSquare;
		swappedLevelHookExtentSquare = tmp2;

		tmp2 = levelHookStretchConstant;
		levelHookStretchConstant = swappedLevelHookStretchConstant;
		swappedLevelHookStretchConstant = tmp2;

		tmp3 = levelHookIsVisible;
		levelHookIsVisible = swappedLevelHookIsVisible;
		swappedLevelHookIsVisible = tmp3;

		tmp3 = levelHookIsActive;
		levelHookIsActive = swappedLevelHookIsActive;
		swappedLevelHookIsActive = tmp3;
	}

	public static final void initSpiderLegs(int num) {
		levelSpiderLegIds = new int[num];
		levelSpiderLegMovementStartX = new int[num];
		levelSpiderLegMovementEndX = new int[num];
		levelSpiderLegTicks = new int[num];
		levelSpiderLegTicksUntilMovement = new int[num];
		levelSpiderLegParent = new int[num];
	}

	public static final void levelSetSpiderLeg(int id, int legId, int parent, int movementAmplitude, int startTicks) {
		levelSpiderLegIds[id] = legId;
		levelSpiderLegMovementStartX[id] = levelCircleX[legId] - movementAmplitude;
		levelSpiderLegMovementEndX[id] = levelCircleX[legId] + movementAmplitude;

		if (startTicks == 0)
			startTicks = 1;

		levelSpiderLegTicks[id] = startTicks;
		levelSpiderLegTicksUntilMovement[id] = startTicks;
		levelSpiderLegParent[id] = parent;
	}

	public static final void immobilizeSpider(int parent) {
		for (int i = 0; i < levelSpiderLegIds.length; i++) {
			if (levelSpiderLegParent[i] == parent) {
				levelSpiderLegTicks[i] = 400000;
				levelCircleWeight[levelSpiderLegIds[i]] = 52000;

				for (int j = 0; j < levelNumHooks; j++) {
					if ((levelHookId1[j] == levelSpiderLegIds[i] ||
								levelHookId2[j] == levelSpiderLegIds[i]) &&
							!levelHookIsVisible[j]) {

						levelHookIsActive[j] = false;
					}
				}
			}
		}
	}

	public static final void updateSpiders() {
		if (levelSpiderLegIds.length > 0) {
			for (int i = 0; i < levelSpiderLegIds.length; i++) {

				int legId = levelSpiderLegIds[i];

				/* levelSpiderLegTicks[n] sign is a direction to where the
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

							/* Apply a new direction to all legs owned by a
							 * specific spider. */
							for (int j = 0; j < levelSpiderLegIds.length; j++) {
								if (levelSpiderLegParent[j] == levelSpiderLegParent[j])
									levelSpiderLegTicks[j] = levelSpiderLegTicks[i];
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

							for (int j = 0; j < levelSpiderLegIds.length; j++) {
								if (levelSpiderLegParent[i] == levelSpiderLegParent[j])
									levelSpiderLegTicks[j] = levelSpiderLegTicks[i];
							}
						} else {
							levelSpiderLegTicks[i] = -levelSpiderLegTicksUntilMovement[i];
						}
					}
				}
			}
		}
	}

	public static final void levelInitExpanders(int num) {
		levelExpanderIds = new int[num];
		levelExpanderIsTouchActivated = new boolean[num];
		levelExpanderIsVacant = new boolean[num];
		levelExpanderDelay = new int[num];
		levelExpanderExtent = new int[num];
		levelExpanderMinRadius = new int[num];
		levelExpanderTicks = 0;
	}

	public static final void levelSetExpander(int id, int circleId, boolean isTouchActivated, int delay, int extent) {
		levelExpanderIds[id] = circleId;
		levelExpanderIsTouchActivated[id] = isTouchActivated;
		levelExpanderIsVacant[id] = false;
		levelExpanderDelay[id] = delay;
		levelExpanderExtent[id] = extent;
		levelExpanderMinRadius[id] = levelCircleRadius[circleId];
		levelCircleFlags[circleId] |= 0x4000;
	}

	public static final void setExpanderVacant(int id) {
		for (int i = 0; i < levelExpanderIds.length; i++) {
			if (levelExpanderIds[i] == id && levelExpanderIsTouchActivated[i])
				levelExpanderIsVacant[i] = true;
		}
	}

	public static final void updateExpanders() {
		levelExpanderTicks++;

		for (int i = 0; i < levelExpanderIds.length; i++) {
			// Process expander radius reducing after vacant.
			if (levelCircleRadius[levelExpanderIds[i]] > levelExpanderMinRadius[i])
				levelCircleRadius[levelExpanderIds[i]] -= (levelCircleRadius[levelExpanderIds[i]] - levelExpanderMinRadius[i]) / 20;

			// Process expander vacant.
			if (levelExpanderIsVacant[i] ||
					levelExpanderDelay[i] > 0 && levelExpanderTicks % levelExpanderDelay[i] == 0) {

				levelCircleRadius[levelExpanderIds[i]] = levelExpanderMinRadius[i] + levelExpanderExtent[i];
				levelExpanderIsVacant[i] = false;
			}
		}
	}

	public static final void initGrabber() {
		if (imgGrabber == null && levelGrabberIds != null && levelGrabberIds.length > 0)
			imgGrabber = loadImage("grabber.pim", "grabber.ppl");
	}

	public static final void initGrabberIds(int num) {
		levelGrabberIds = new int[num];
	}

	public static final void setGrabberId(int id, int circleId) {
		levelGrabberIds[id] = circleId;
	}

	public static final void levelRenderGrabbers() {
		for (int i = 0; i < levelGrabberIds.length; i++) {
			int grabberId = levelGrabberIds[i];

			if (grabberId != levelHookId2[16] &&
					levelCircleFlags[grabberId] != 0) {

				levelDrawImage(imgGrabber, levelCircleX[grabberId], levelCircleY[grabberId]);
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
			// Ping itself.
			if (levelCircleType[i] == 13)
				levelPingCircleId = i;

			// Ping's shards.
			if (levelCircleType[i] == 15)
				levelPingShardIds[shardIndex++] = i;

			// Enemies.
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

				for (int j = 0; j < levelEnemyIds.length; j++)
					if (levelEnemyIds[j] == i)
						levelEnemyIsAlive[j] = false;
			}
		}

		// Wha?...
		if (shardIndex == 1) {
			int[] shardIds = new int[1];
			shardIds[0] = levelPingShardIds[0];
			levelPingShardIds = shardIds;
		}
	}

	public static final void updatePing() {
		/* Process every Ping's shard, and if the X axis of the shard is within
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

		// levelPingHitTimeout = 100 if Ping is hit (levelPingHit()).
		if (levelPingHitTimeout > 0) {
			levelPingHitTimeout--;
			if (levelPingHitTimeout == 85) {
				levelPingSpawnEnemies();
				startScript("ping_hit.bms", levelCircleX[levelPingCircleId], levelCircleY[levelPingCircleId] - levelCircleRadius[levelPingCircleId] * 2 / 3);
				return;
			}
		} else if (levelPingHealth < 0) {
			isLevelComplete = true;
		}
	}

	public static final void levelPingSpawnEnemies() {
		if (levelPingHitTimeout == 0) {
			startScript("ping_inplace.bms", levelCircleX[levelPingCircleId], levelCircleY[levelPingCircleId] - levelCircleRadius[levelPingCircleId] * 2 / 3);
		}

		/* Ping spawns enemies. */
		levelPingHealth--;
		if (levelPingHealth >= 0) {
			for (int i = 0; i < levelPingEnemyIds[levelPingHealth].length; i++) {
				int enemyId = levelPingEnemyIds[levelPingHealth][i];

				if (enemyId != -1) {
					levelCircleFlags[enemyId] |= 1;

					for (int j = 0; j < levelEnemyIds.length; j++) {
						if (levelEnemyIds[j] == enemyId)
							levelEnemyIsAlive[j] = true;
					}
				}
			}
		}
	}

	public static final void renderPing() {
		setFadeColor(160, 32, 240);

		for (int i = 0; i < levelPingShardIds.length; i++)
			levelRenderCircle(levelCircleX[levelPingShardIds[i]], levelCircleY[levelPingShardIds[i]], levelCircleRadius[levelPingShardIds[i]]);

		levelRenderCircle(levelCircleX[levelPingCircleId], levelCircleY[levelPingCircleId], levelCircleRadius[levelPingCircleId]);

		int eyeDiameter = levelCircleRadius[levelPingCircleId] * 2 / 5;
		int centerEyeX = levelCircleX[levelPingCircleId];
		int eyesY = levelCircleY[levelPingCircleId] - levelCircleRadius[levelPingCircleId] * 7 / 10;
		int eyeRadius = levelCircleRadius[levelPingCircleId] / 5;

		// For each eye.
		for (int i = -1; i < 2; i++) {
			setFadeColor(0);
			levelRenderCircle(centerEyeX + i * eyeDiameter, eyesY, eyeRadius);

			if (i < levelPingHealth) {

				// Opened eye.
				setFadeColor(0xffffff);
				levelRenderCircle(centerEyeX + i * eyeDiameter, eyesY, eyeRadius);

				setFadeColor(0);
				int pupilX = coefficientDelta(centerEyeX + i * eyeDiameter, levelCameraX, 0x960000, eyeRadius * 3 / 4);
				int pupilY = coefficientDelta(eyesY, levelCameraY, 0x640000, eyeRadius * 3 / 4);
				levelRenderCircle(centerEyeX + i * eyeDiameter + pupilX, eyesY + pupilY, eyeRadius / 3);

			} else {

				// Closed eye.
				setFadeColor(160, 32, 240);
				levelRenderCircle(centerEyeX + i * eyeDiameter, eyesY, eyeRadius);

			}
		}
	}

	public static final int coefficientDelta(int base, int pos, int range, int coefficient) {
		int angle = (base - (pos - range)) * 90 / range;

		if (angle < 0)
			angle = 0;

		if (angle >= 180)
			angle = 179;

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

	public static final void initEnemies(int num) {
		levelEnemyIds = new int[num];
		levelEnemyCanJump = new boolean[num];
		levelEnemyFramesUntilJump = new int[num];
		levelEnemyJumpHeight = new int[num];
		levelEnemyIsMovingInX = new boolean[num];
		levelEnemyFramesUntilXMovement = new int[num];
		levelEnemyXMovementSpeed = new int[num];
		levelEnemyXMovementDistance = new int[num];
		levelEnemyIsJitteringX = new boolean[num];
		levelEnemyIsAlive = new boolean[num];
		levelEnemyIsBird = new boolean[num];
		levelEnemyArg = new int[num];
		levelEnemyFrame = new int[num];
		levelEnemyXMovementDirection = new int[num];
		levelEnemyCenterX = new int[num];
		levelEnemyTicks = 0;
	}

	public static final void levelSetEnemy(int id, int circleId, boolean canJump, int framesUntilJump, int jumpHeight, boolean isMovingInX, int framesUntilXMovement, int xMovementSpeed, int xMovementDistance, boolean isJitteringX, int arg) {
		levelEnemyIds[id] = circleId;
		levelEnemyCanJump[id] = canJump;
		levelEnemyFramesUntilJump[id] = framesUntilJump;
		levelEnemyJumpHeight[id] = jumpHeight;
		levelEnemyIsMovingInX[id] = isMovingInX;
		levelEnemyFramesUntilXMovement[id] = framesUntilXMovement;
		levelEnemyXMovementSpeed[id] = xMovementSpeed;
		levelEnemyXMovementDistance[id] = xMovementDistance;
		levelEnemyIsJitteringX[id] = isJitteringX;
		levelEnemyIsAlive[id] = true;
		levelEnemyXMovementDirection[id] = 1;
		levelEnemyFrame[id] = 0;
		levelEnemyArg[id] = arg;
		levelEnemyCenterX[id] = levelCircleX[circleId];
		levelEnemyIsBird[id] = (levelCircleFlags[circleId] & 1) == 0;
	}

	public static final void updateEnemies() {
		levelEnemyTicks++;

		for (int i = 0; i < levelEnemyIds.length; i++) {
			if (levelEnemyIsAlive[i]) {
				int enemyTicks = levelEnemyTicks + levelEnemyArg[i];

				// Process jump.
				if (levelEnemyCanJump[i] && enemyTicks % levelEnemyFramesUntilJump[i] == 0)
					levelCircleY[levelEnemyIds[i]] -= levelEnemyJumpHeight[i];

				// Process movement.
				if (levelEnemyIsMovingInX[i]) {
					if (enemyTicks % levelEnemyFramesUntilXMovement[i] == 0) {
						levelCircleX[levelEnemyIds[i]] += levelEnemyXMovementDirection[i] * levelEnemyXMovementSpeed[i];
						if (levelEnemyIsJitteringX[i] && rand8() % 50 == 49)
							levelEnemyXMovementDirection[i] = -levelEnemyXMovementDirection[i];
					}

					if (levelCircleX[levelEnemyIds[i]] > levelEnemyCenterX[i] + levelEnemyXMovementDistance[i])
						levelEnemyXMovementDirection[i] = -1;

					if (levelCircleX[levelEnemyIds[i]] < levelEnemyCenterX[i] - levelEnemyXMovementDistance[i])
						levelEnemyXMovementDirection[i] = 1;
				}
			}
		}
	}

	public static final boolean isEnemyStatic(int circleId) {
		for (int i = 0; i < levelEnemyIds.length; i++) {
			if (levelEnemyIds[i] == circleId) {
				if (!levelEnemyCanJump[i] && !levelEnemyIsMovingInX[i])
					return true;

				return false;
			}
		}

		return false;
	}

	public static final boolean isEnemyAlive(int circleId) {
		for (int i = 0; i < levelEnemyIds.length; i++) {
			if (levelEnemyIds[i] == circleId)
				return levelEnemyIsAlive[i];
		}

		return false;
	}

	public static final void killEnemy(int circleId) {
		for (int i = 0; i < levelEnemyIds.length; i++) {
			if (levelEnemyIds[i] == circleId && levelEnemyIsAlive[i]) {
				levelEnemyIsAlive[i] = false;
				levelCircleFlags[circleId] |= 1;

				if (!levelEnemyCanJump[i] && !levelEnemyIsMovingInX[i])
					immobilizeSpider(levelEnemyArg[i]);

				return;
			}
		}
	}

	public static final void initEnemies() {
		if (levelEnemyIds != null && levelEnemyIds.length != 0) {
			if (imgsSpider == null) {
				imgsSpider = new Image[2];
				imgsSpider[0] = loadImage("spiderC.pim", "spiderC.ppl");
				imgsSpider[1] = loadImage("spiderD.pim", "spiderD.ppl");
			}

			if (imgsBirdLeft == null) {
				imgsBirdLeft = new Image[6];
				imgsBirdRight = new Image[6];

				for (int i = 0; i <= 5; i++) {
					imgsBirdLeft[i] = loadImage("bird_left" + i + ".pim", "bird_left" + i + ".ppl");
					imgsBirdRight[i] = loadImage("bird_right" + i + ".pim", "bird_right" + i + ".ppl");
				}
			}

			if (imgsPointyRoll == null) {
				imgsPointyRoll = new Image[3];

				for (int i = 0; i < 3; i++)
					imgsPointyRoll[i] = loadImage("pointy_roll_" + i + ".pim", "pointy_roll_" + i + ".ppl");
			}

			if (imgsPointyEyes == null) {
				imgsPointyEyes = new Image[2];

				for (int i = 0; i < 2; i++)
					imgsPointyEyes[i] = loadImage("pointy_eyes_" + i + ".pim", "pointy_eyes_" + i + ".ppl");
			}

			if (imgsPointyMouth == null) {
				imgsPointyMouth = new Image[2];

				for (int i = 0; i < 2; i++)
					imgsPointyMouth[i] = loadImage("pointy_mouth_" + i + ".pim", "pointy_mouth_" + i + ".ppl");
			}
		}
	}

	public static final void renderEnemies() {
		for (int i = 0; i < levelEnemyIds.length; i++)
			renderEnemies(levelEnemyIds[i], i);
	}

	public static final void renderEnemies(int circleId, int enemyId) {
		if (levelCircleRadius[circleId] >= 30000) {
			int alignX = levelAlignX(levelCircleX[circleId], levelCircleY[circleId]);
			int alignY = levelAlignY(levelCircleX[circleId], levelCircleY[circleId]);

			if (levelEnemyIsBird[enemyId]) {

				levelEnemyFrame[enemyId] += (levelCircleX[circleId] - levelCirclePrevX[circleId]) / 7000;

				while (levelEnemyFrame[enemyId] >= 500)
					levelEnemyFrame[enemyId] -= 500;

				while (levelEnemyFrame[enemyId] < 0)
					levelEnemyFrame[enemyId] += 500;

				if (!levelEnemyIsAlive[enemyId])
					levelEnemyFrame[enemyId] = 510;

				Image bird;
				if (levelCirclePrevX[circleId] > levelCircleX[circleId])
					bird = imgsBirdLeft[levelEnemyFrame[enemyId] / 100];
				else
					bird = imgsBirdRight[levelEnemyFrame[enemyId] / 100];

				gDrawImage(bird, alignX - bird.getWidth() / 2, alignY - bird.getHeight() / 2, 0);

			} else if (!levelEnemyCanJump[enemyId] && !levelEnemyIsMovingInX[enemyId]) {

				Image spider = imgsSpider[0];
				if (!levelEnemyIsAlive[enemyId])
					spider = imgsSpider[1];

				gDrawImage(spider, alignX - spider.getWidth() / 2, alignY - spider.getHeight() / 2, 0);

			} else {

				levelEnemyFrame[enemyId] += (levelCircleX[circleId] - levelCirclePrevX[circleId]) / 3000;

				while (levelEnemyFrame[enemyId] >= 300)
					levelEnemyFrame[enemyId] -= 300;

				while (levelEnemyFrame[enemyId] < 0) {
					levelEnemyFrame = levelEnemyFrame;
					levelEnemyFrame[enemyId] += 300;
				}

				Image pointyRoll = imgsPointyRoll[levelEnemyFrame[enemyId] / 100];
				if (pointyRoll == null)
					pointyRoll = imgsPointyRoll[0];

				Image pointyEyes;
				if (levelCircleY[circleId] < levelCircleY[0])
					pointyEyes = imgsPointyEyes[0];
				else
					pointyEyes = imgsPointyEyes[1];

				Image pointyMouth = imgsPointyMouth[0];
				if (circleId == lastAttackingEnemy && levelPlayerHitTicks > 0) {
					pointyMouth = imgsPointyMouth[1];
					levelRenderCritterShock(levelCircleX[1 + levelPlayerHitTicks % 4], levelCircleY[1 + levelPlayerHitTicks % 4], levelCircleX[circleId], levelCircleY[circleId]);
				}

				gDrawImage(pointyRoll, alignX - pointyRoll.getWidth() / 2, alignY - pointyRoll.getHeight() / 2, 0);
				if (levelEnemyIsAlive[enemyId]) {
					gDrawImage(pointyEyes, alignX + 0 - pointyEyes.getWidth() / 2, alignY + -8 - pointyEyes.getHeight() / 2, 0);
					gDrawImage(pointyMouth, alignX + 0 - pointyMouth.getWidth() / 2, alignY + 5 - pointyMouth.getHeight() / 2, 0);
				}

			}
		}
	}

	public static final void levelInitRects(int num) {
		levelNumRects = num;
		levelRectX = new int[num];
		levelRectY = new int[num];
		levelRectHWidth = new int[num];
		levelRectHHeight = new int[num];
		levelRectIsAnimated = new boolean[num];
	}

	public static final void levelAddRect(int id, int x, int y, int hWidth, int hHeight) {
		levelRectX[id] = x;
		levelRectY[id] = y;
		levelRectHWidth[id] = hWidth;
		levelRectHHeight[id] = hHeight;
		levelRectIsAnimated[id] = false;
	}

	public static final void levelInitAnimatedRects(int num) {
		levelAnimRectIds = new int[num];
		levelAnimRectMinHWidth = new int[num];
		levelAnimRectMaxHWidth = new int[num];
		levelAnimRectMinHHeight = new int[num];
		levelAnimRectMaxHHeight = new int[num];
		levelAnimRectSpeed = new int[num];
		levelAnimRectIterations = new int[num];
		levelAnimRectCurrentIteration = new int[num];
	}

	public static final void levelSetAnimRect(int id, int rectId, int maxHWidth, int maxHHeight, int speed, int iterations, int curIteration) {
		levelRectIsAnimated[rectId] = true;
		levelAnimRectIds[id] = rectId;
		levelAnimRectMinHWidth[id] = levelRectHWidth[rectId];
		levelAnimRectMaxHWidth[id] = maxHWidth;
		levelAnimRectMinHHeight[id] = levelRectHHeight[rectId];
		levelAnimRectMaxHHeight[id] = maxHHeight;
		levelAnimRectSpeed[id] = speed;
		levelAnimRectIterations[id] = iterations;
		levelAnimRectCurrentIteration[id] = curIteration;
	}

	public static final void updateAnimRects() {
		for (int i = 0; i < levelAnimRectIds.length; i++) {
			int rectId = levelAnimRectIds[i];

			if (levelAnimRectCurrentIteration[i] > 0) {

				levelAnimRectCurrentIteration[i]--;

			} else if (levelAnimRectCurrentIteration[i] < -1) {

				levelAnimRectCurrentIteration[i]++;

			} else if (levelAnimRectCurrentIteration[i] == 0) {

				levelRectHWidth[rectId] += levelAnimRectSpeed[i];
				levelRectHHeight[rectId] += levelAnimRectSpeed[i];

				if (levelRectHWidth[rectId] > levelAnimRectMaxHWidth[i]) {
					levelRectHWidth[rectId] = levelAnimRectMaxHWidth[i];
				}

				if (levelRectHHeight[rectId] > levelAnimRectMaxHHeight[i]) {
					levelRectHHeight[rectId] = levelAnimRectMaxHHeight[i];
				}

				if (levelRectHWidth[rectId] == levelAnimRectMaxHWidth[i] && levelRectHHeight[rectId] == levelAnimRectMaxHHeight[i]) {
					levelAnimRectCurrentIteration[i] = -levelAnimRectIterations[i] - 1;
				}

			} else if (levelAnimRectCurrentIteration[i] == -1) {

				levelRectHWidth[rectId] -= levelAnimRectSpeed[i];
				levelRectHHeight[rectId] -= levelAnimRectSpeed[i];

				if (levelRectHWidth[rectId] < levelAnimRectMinHWidth[i]) {
					levelRectHWidth[rectId] = levelAnimRectMinHWidth[i];
				}

				if (levelRectHHeight[rectId] < levelAnimRectMinHHeight[i]) {
					levelRectHHeight[rectId] = levelAnimRectMinHHeight[i];
				}

				if (levelRectHWidth[rectId] == levelAnimRectMinHWidth[i] && levelRectHHeight[rectId] == levelAnimRectMinHHeight[i]) {
					levelAnimRectCurrentIteration[i] = levelAnimRectIterations[i];
				}

			}
		}
	}

	public static final void updateCollisionCircleRect() {
		for (int i = 0; i < levelNumCircles; i++) {
			if (levelCircleHasPhysics[i]) {
				for (int j = 0; j < levelNumRects; j++) {
					if (levelCircleY[i] + levelCircleRadius[i] > levelRectY[j] - levelRectHHeight[j] &&
							levelCircleY[i] - levelCircleRadius[i] < levelRectY[j] + levelRectHHeight[j] &&
							levelCircleX[i] + levelCircleRadius[i] > levelRectX[j] - levelRectHWidth[j] &&
							levelCircleX[i] - levelCircleRadius[i] < levelRectX[j] + levelRectHWidth[j]) {

						onCircleTouchRect(i, j);
					}
				}
			}
		}
	}

	// A dumb function where both circle and rect assumed as "rects".
	public static final void onCircleTouchRect(int circleId, int rectId) {
		if (circleId == 2)
			levelIsPlayerOnSurface = true;

		int extentX = levelRectX[rectId] + levelRectHWidth[rectId] + levelCircleRadius[circleId];
		if (levelCircleX[circleId] < levelRectX[rectId])
			extentX = levelRectX[rectId] - levelRectHWidth[rectId] - levelCircleRadius[circleId];

		int extentY = levelRectY[rectId] + levelRectHHeight[rectId] + levelCircleRadius[circleId];
		if (levelCircleY[circleId] < levelRectY[rectId])
			extentY = levelRectY[rectId] - levelRectHHeight[rectId] - levelCircleRadius[circleId];

		int distX = levelCircleX[circleId] - extentX;
		if (distX < 0)
			distX = -distX;

		int distY = levelCircleY[circleId] - extentY;
		if (distY < 0)
			distY = -distY;

		if (distX < distY)
			levelCircleX[circleId] += (extentX - levelCircleX[circleId]) / 2;
		else
			levelCircleY[circleId] += (extentY - levelCircleY[circleId]) / 2;
	}

	public static final void levelRenderRects() {
		setFadeColor(0, 0, 0);
		for (int i = 0; i < levelNumRects; i++)
			levelDrawRect(levelRectX[i], levelRectY[i], levelRectHWidth[i], levelRectHHeight[i]);

		setFadeColor(levelColor);
		for (int i = 0; i < levelNumRects; i++)
			levelDrawRect(levelRectX[i], levelRectY[i], levelRectHWidth[i], levelRectHHeight[i]);
	}

	public static final void levelLoadRects() {
		levelInitRects(sReadU16());

		for (int i = 0; i < levelNumRects; i++)
			levelAddRect(i, levelAlignXToMirror(sReadU16() << 16), sReadU16() << 16, sReadU16() << 16, sReadU16() << 16);
	}

	public static final void swapLevelRectData() {
		int tmp1;
		int[] tmp2;

		tmp1 = levelNumRects;
		levelNumRects = swappedLevelNumRects;
		swappedLevelNumRects = tmp1;

		tmp2 = levelRectX;
		levelRectX = swappedLevelRectX;
		swappedLevelRectX = tmp2;

		tmp2 = levelRectY;
		levelRectY = swappedLevelRectY;
		swappedLevelRectY = tmp2;

		tmp2 = levelRectHWidth;
		levelRectHWidth = swappedLevelRectHWidth;
		swappedLevelRectHWidth = tmp2;

		tmp2 = levelRectHHeight;
		levelRectHHeight = swappedLevelRectHHeight;
		swappedLevelRectHHeight = tmp2;
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

	public static final void renderRocket(int x, int y, int tick) {
		initRocket();
		gDrawImage(imgRocket, x, y, 0);

		if (tick >= 0)
			gDrawImage(imgsVertFlame[tick & 1], x + 50, y + 48, 0);
	}

	public static final void initSigns() {
		if (mustLoadSigns && imgsSign == null) {
			imgsSign = new Image[7];

			for (int i = 0; i < imgsSign.length; i++)
				imgsSign[i] = loadImage("sign" + i + ".pim", "sign" + i + ".ppl");
		}
	}

	public static final void initLevelSigns(int num) {
		levelSignX = new int[num];
		levelSignY = new int[num];
		levelSignTypes = new int[num];
		if (num > 0)
			mustLoadSigns = true;
	}

	public static final void levelSetSign(int id, int x, int y, int type) {
		if (isMirrored && type == 2) type = 3;
		if (isMirrored && type == 3) type = 2;

		levelSignX[id] = x;
		levelSignY[id] = y;
		levelSignTypes[id] = type;
	}

	public static final void renderSigns() {
		if (imgsSign == null)
			initSigns();

		for (int i = 0; i < levelSignX.length; i++) {
			Image img = imgsSign[levelSignTypes[i]];
			if (img != null)
				levelDrawImage(img, levelSignX[i], levelSignY[i]);
		}
	}

	public static final void levelInitSpawners(int num) {
		levelSpawnStartX = new int[num];
		levelSpawnStartY = new int[num];
		levelSpawnStartIds = new int[num];
		levelSpawnCurrentIndex = new int[num];
		levelSpawnEndIds = new int[num];
		levelSpawnEndTick = new int[num];
		levelSpawnCurrentTick = new int[num];
		levelSpawnRadius = new int[num];
		levelSpawnAngle = new int[num];
		levelSpawnAngleRange = new int[num];
		levelSpawnSpeed = new int[num];
		levelSpawnSpeedRange = new int[num];
	}

	public static final void levelSetSpawner(int id, int x, int y, int startIndex, int endIndex, int endTick, int startAngle, int endAngle, int startSpeed, int endSpeed) {
		if (isMirrored) {
			int angleRange = endAngle - startAngle;
			short ang = 180;
			if (startAngle > 180)
				ang = 540;

			endAngle = (startAngle = ang - startAngle - angleRange) + angleRange;
		}

		levelSpawnStartX[id] = x;
		levelSpawnStartY[id] = y;
		levelSpawnStartIds[id] = startIndex;
		levelSpawnEndIds[id] = startIndex + endIndex;
		levelSpawnEndTick[id] = endTick;
		levelSpawnCurrentTick[id] = 0;
		levelSpawnCurrentIndex[id] = startIndex;
		levelSpawnRadius[id] = levelCircleRadius[startIndex];
		levelSpawnAngle[id] = startAngle;
		levelSpawnAngleRange[id] = endAngle - startAngle;
		levelSpawnSpeed[id] = 2 * startSpeed;
		levelSpawnSpeedRange[id] = 2 * (endSpeed - startSpeed);
	}

	public static final void updateSpawners() {
		for (int i = 0; i < levelSpawnStartIds.length; i++) {
			if (++levelSpawnCurrentTick[i] > levelSpawnEndTick[i]) {
				int id = levelSpawnCurrentIndex[i];

				levelCircleRadius[id] -= levelCircleRadius[id] / 4;
				// If a ball in the current ID is already disappeared, spawn a new ball.
				if (levelCircleRadius[id] < 0x10000) {
					levelSpawnCurrentTick[i] = 0;
					levelCircleHasPhysics[id] = true;
					levelCircleFlags[id] |= 4;
					levelCircleRadius[id] = levelSpawnRadius[i];
					levelCircleX[id] = levelSpawnStartX[i];
					levelCircleY[id] = levelSpawnStartY[i];

					int newSpeed = levelSpawnSpeed[i] + rand16() % levelSpawnSpeedRange[i];
					int newAngle = levelSpawnAngle[i] + rand16() % levelSpawnAngleRange[i];
					levelCirclePrevX[id] = levelSpawnStartX[i] - newSpeed * cos(newAngle);
					levelCirclePrevY[id] = levelSpawnStartY[i] - newSpeed * sin(newAngle);

					levelSpawnCurrentIndex[i]++;
					if (levelSpawnCurrentIndex[i] >= levelSpawnEndIds[i])
						levelSpawnCurrentIndex[i] = levelSpawnStartIds[i];
				}
			}
		}
	}

	public static final void updateLevel() {
		updatePlayerMovement();
		levelUpdateHooks();
		updateCircleCollision();
		updateCollisionCircleRect();
	}

	public static final void levelInitCircles(int num) {
		levelNumCircles = num;
		levelCircleX = new int[num];
		levelCircleY = new int[num];
		levelCircleRadius = new int[num];
		levelCirclePrevX = new int[num];
		levelCirclePrevY = new int[num];
		levelCircleFlags = new int[num];
		levelCircleWeight = new int[num];
		levelCircleType = new byte[num];
		levelCircleHasPhysics = new boolean[num];
	}

	public static final void levelSetCircle(int id, int x, int y, int radius, int density, int flags, int type, boolean hasPhysics) {
		if (type == 3 && currentLevelLoaded != -2)
			flags &= -5;

		levelCircleX[id] = x;
		levelCircleY[id] = y;
		levelCirclePrevX[id] = x;
		levelCirclePrevY[id] = y;
		levelCircleRadius[id] = radius;
		levelCircleFlags[id] = flags;
		levelCircleHasPhysics[id] = hasPhysics;
		levelCircleType[id] = (byte)type;
		levelCircleWeight[id] = lp32Div(0x10000, density << 14);
	}

	public static final void updatePlayerMovement() {
		updatePlayerRotation();

		// Check if player is out of level bounds.
		if (levelCircleX[0] < 0 ||
				levelCircleX[0] > levelWidth ||
				levelCircleY[0] > levelHeight) {

			levelPlayerHealth = 0;
		}

		// Stop the player entirely if the speed is too small (<0,125).
		for (int i = 0; i < 5; i++) {
			if (levelCircleX[i] - levelCirclePrevX[i] < 0x2000 &&
					levelCircleX[i] - levelCirclePrevX[i] > -0x2000) {

				levelCircleX[i] = levelCirclePrevX[i];
			}
		}

		// Limit the player speed.
		for (int i = 0; i < 5; i++) {
			if (levelCircleX[i] - levelCirclePrevX[i] > 0xf0000)
				levelCircleX[i] = levelCirclePrevX[i] + 0xf0000;

			if (levelCircleX[i] - levelCirclePrevX[i] < -0xf0000)
				levelCircleX[i] = levelCirclePrevX[i] - 0xf0000;

			if (levelCircleY[i] - levelCirclePrevY[i] > 0xf0000)
				levelCircleY[i] = levelCirclePrevY[i] + 0xf0000;

			if (levelCircleY[i] - levelCirclePrevY[i] < -0xf0000)
				levelCircleY[i] = levelCirclePrevY[i] - 0xf0000;
		}

		/* Slowly decrease the speed for all movables (cuz levelMovableAccel
		 * will be almost always < 1.0). */
		for (int i = 0; i < levelNumCircles; i++) {
			if (levelCircleHasPhysics[i]) {
				int circleX = levelCircleX[i];
				int circleY = levelCircleY[i];
				int movableAccel = levelMovableAccel;

				if (i < 5 && levelPlayerCurrentGrabberFlags >= 0)
					movableAccel = 63000;

				levelCircleX[i] += lp32Mul(movableAccel, levelCircleX[i] - levelCirclePrevX[i]);
				levelCircleY[i] += lp32Mul(movableAccel, levelCircleY[i] - levelCirclePrevY[i]);

				if ((levelCircleFlags[i] & 1) > 0)
					levelCircleY[i] += levelMovableGravity;

				levelCirclePrevX[i] = circleX;
				levelCirclePrevY[i] = circleY;
			}
		}
	}

	public static final void updateCircleCollision() {
		levelIsPlayerOnSurface = false;

		// Player circles.
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < levelNumCircles; j++) {
				if ((levelCircleFlags[j] & 2) > 0 &&
						levelCircleX[i] - levelCircleRadius[i] < levelCircleX[j] + levelCircleRadius[j] &&
						levelCircleX[i] + levelCircleRadius[i] > levelCircleX[j] - levelCircleRadius[j] &&
						levelCircleY[i] - levelCircleRadius[i] < levelCircleY[j] + levelCircleRadius[j] &&
						levelCircleY[i] + levelCircleRadius[i] > levelCircleY[j] - levelCircleRadius[j]) {

					if ((levelCircleFlags[j] & 0x20) > 0)
						levelScriptToRun = j;
					else
						onCircleTouchCircle(i, j, levelCircleRadius[i] + levelCircleRadius[j], 55000, 3, true);
				}
			}
		}

		// Level circles.
		for (int i = 5; i < levelNumCircles; i++) {
			if (levelCircleHasPhysics[i]) {
				for (int j = i + 1; j < levelNumCircles; j++) {
					if ((levelCircleFlags[j] & 2) > 0 &&
							levelCircleX[i] - levelCircleRadius[i] < levelCircleX[j] + levelCircleRadius[j] &&
							levelCircleX[i] + levelCircleRadius[i] > levelCircleX[j] - levelCircleRadius[j] &&
							levelCircleY[i] - levelCircleRadius[i] < levelCircleY[j] + levelCircleRadius[j] &&
							levelCircleY[i] + levelCircleRadius[i] > levelCircleY[j] - levelCircleRadius[j] &&
							(levelCircleFlags[j] & 0x20) == 0) {

						if ((levelCircleFlags[j] & 0x10) > 0)
							processCircles(i, j);
						else
							onCircleTouchCircle(i, j, levelCircleRadius[i] + levelCircleRadius[j], 55000, 3, true);
					}
				}
			}
		}
	}

	public static final void swapLevelCircleData() {
		int tmp1;
		int[] tmp2;
		byte[] tmp3;
		boolean[] tmp4;

		tmp1 = levelNumCircles;
		levelNumCircles = swappedLevelNumCircles;
		swappedLevelNumCircles = tmp1;

		tmp2 = levelCircleX;
		levelCircleX = swappedLevelCircleX;
		swappedLevelCircleX = tmp2;

		tmp2 = levelCircleY;
		levelCircleY = swappedLevelCircleY;
		swappedLevelCircleY = tmp2;

		tmp2 = levelCircleRadius;
		levelCircleRadius = swappedLevelCircleRadius;
		swappedLevelCircleRadius = tmp2;

		tmp2 = levelCirclePrevX;
		levelCirclePrevX = swappedLevelCirclePrevX;
		swappedLevelCirclePrevX = tmp2;

		tmp2 = levelCirclePrevY;
		levelCirclePrevY = swappedLevelCirclePrevY;
		swappedLevelCirclePrevY = tmp2;

		tmp2 = levelCircleFlags;
		levelCircleFlags = swappedLevelCircleFlags;
		swappedLevelCircleFlags = tmp2;

		tmp2 = levelCircleWeight;
		levelCircleWeight = swappedLevelCircleDensity;
		swappedLevelCircleDensity = tmp2;

		tmp3 = levelCircleType;
		levelCircleType = swappedLevelCircleType;
		swappedLevelCircleType = tmp3;

		tmp4 = levelCircleHasPhysics;
		levelCircleHasPhysics = swappedLevelCircleHasPhysics;
		swappedLevelCircleHasPhysics = tmp4;
	}

	// Handle flags of all the circles of the level.
	public static final void levelObtainIds() {
		int id;

		levelIdsExpanders = levelGetCircleIdsByFlags(0x4000);
		levelIdsDestructibles = levelGetCircleIdsByFlags(0x0100);
		levelIdsFoam = levelGetCircleIdsByFlags(0x2000);
		levelIdsPortals = levelGetCircleIdsByFlags(0x0010);

		// Bomb dispensers.
		id = 0;
		for (int i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 0x80) > 0 &&
					levelCircleType[i] != 7) {

				id++;
			}
		}
		levelIdsBombDispenser = new short[id];
		id = 0;
		for (short i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 0x80) > 0 &&
					levelCircleType[i] != 7) {

				levelIdsBombDispenser[id++] = i;
			}
		}

		// Platforms.
		id = 0;
		for (int i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 4) > 0 &&
					!levelCircleHasPhysics[i] &&
					(levelCircleFlags[i] & 8) == 0 &&
					(levelCircleFlags[i] & 0x2000) == 0 &&
					(levelCircleFlags[i] & 0x100) == 0) {

				id++;
			}
		}
		levelIdsPlatforms = new short[id];
		id = 0;
		for (short i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 4) > 0 &&
					!levelCircleHasPhysics[i] &&
					(levelCircleFlags[i] & 8) == 0 &&
					(levelCircleFlags[i] & 0x2000) == 0 &&
					(levelCircleFlags[i] & 0x100) == 0) {

				levelIdsPlatforms[id++] = i;
			}
		}

		levelIdsRadioactive = levelGetCircleIdsByFlags(8);
	}

	public static final short[] levelGetCircleIdsByFlags(int flags) {
		int id = 0;

		for (short i = 0; i < levelNumCircles; i++)
			if ((levelCircleFlags[i] & flags) == flags)
				id++;

		short[] ids = new short[id];
		id = 0;

		for (short i = 0; i < levelNumCircles; i++)
			if ((levelCircleFlags[i] & flags) == flags)
				ids[id++] = i;

		return ids;
	}

	// General function to parse and load level files.
	public static final void loadLevel(int levelId) {
		int playerX, playerY, playerSize;
		boolean isStandardLevel = true;
		isLevelComplete = false;

		if (levelId >= 25)
			levelId = 24;

		// Open level file.
		if (levelId == -2) {
			swapLevelData(0);
			sOpenFile("ship.bin");
			isStandardLevel = false;
		} else if (levelId == -1) {
			swapLevelData(-1);
			sOpenFile("playground" + level * 4 / 25 + ".bin");
			isStandardLevel = false;
		} else if (levelId < 10) {
			swapLevelData(1);
			sOpenFile("level.00" + levelId + ".bin");
		} else {
			swapLevelData(1);
			sOpenFile("level.0" + levelId + ".bin");
		}

		// Check if it's the final level.
		isFinalLevel = false;
		if (levelId >= 0)
			isFinalLevel = levelId == 24;

		currentLevelLoaded = levelId;

		// The level file header.
		levelWidth = sReadU16() << 16;
		levelHeight = sReadU16() << 16;
		setWeatherAndBackgroundIds(sRead8(), sRead8());
		sRead8();
		sRead32();
		levelColor = sRead32();
		levelColorMovable = sRead32();
		sRead32();
		levelColorPortal = sRead32();
		levelColorDestructible = sRead32();
		playerX = levelAlignXToMirror(sReadU16() << 16);
		playerY = sReadU16() << 16;
		levelEntityX = levelAlignXToMirror(sReadU16() << 16);
		levelEntityY = sReadU16() << 16;
		int numCircles = sReadU16();

		levelPortalFilledRadius = 0;

		int curCircleIndex = 0;
		int baseCircleIndex = 6;
		if (!isStandardLevel && levelId != -1)
			baseCircleIndex += 5;

		// Init player circles.
		levelInitCircles(baseCircleIndex + numCircles + 1 + 1);
		if (isStandardLevel)
			curCircleIndex = levelAddPlayerHitbox(0, playerX, playerY, 0x140000);
		else
			curCircleIndex = levelAddPlayerHitbox(0, playerX, playerY, 0xd0000);

		// Reserved for a bomb object.
		levelSetCircle(curCircleIndex++, 0, 0, 0xa0000, 30, 0, 0, false);
		/* Reserved for Waz in the ship. Yeah, Wiz and Waz hitboxes in the same
		 * level (ship), that's real. */
		if (!isStandardLevel && levelId != -1)
			curCircleIndex = levelAddPlayerHitbox(curCircleIndex, playerX + 0x370000, playerY, 0x140000);

		// Init circles.
		baseCircleIndex = curCircleIndex;

		for (int i = 0; i < numCircles; i++)
			levelSetCircle(curCircleIndex++, levelAlignXToMirror(sReadU16() << 16), sReadU16() << 16, sReadU16() << 16, sReadU16(), sReadU16(), sRead8(), sRead8() == 1);

		/* Init grabbers (thru circle type).
		 * This is ignored if the level ID is -2 (ship) to prevent the player
		 * hooking on the 3rd pod. */
		if (levelId == -2) {
			initGrabberIds(0);
		} else {
			int num = 0;
			for (int i = 0; i < levelNumCircles; i++) {
				if (levelCircleType[i] == 3)
					num++;
			}
			initGrabberIds(num);
			int id = 0;
			for (int i = 0; i < levelNumCircles; i++) {
				if (levelCircleType[i] == 3)
					setGrabberId(id++, i);
			}
		}

		// Bomb circle ID.
		levelBombObjectId = 5;

		// Init rectangles.
		levelLoadRects();

		// Init player and level hooks.
		int baseNumHooks = sReadU16();
		/* Side hitbox distance from the central hitbox of the player.
		 * Wiz has hitboxes that are closer to the center. */
		playerSize = 0x140000;
		if (!isStandardLevel)
			playerSize = 0xc0000;

		levelInitHooks(17 + baseNumHooks);
		curCircleIndex = levelSetPlayerHook(0, 0, playerSize);
		// General player hook reserved for a grabber.
		int playerHook = curCircleIndex++;
		levelSetHook(playerHook, 0, 0, 4, 65536, 65500, false, false);

		for (int i = 0; i < baseNumHooks; i++)
			levelSetHook(curCircleIndex++, baseCircleIndex + sReadU16(), baseCircleIndex + sReadU16(), sRead8(), sReadU16() << 16, sReadU16(), sRead8() == 1, true);

		byte num;

		// Init spawners (spawns movables).
		levelInitSpawners(num = sRead8());

		for (int i = 0; i < num; i++)
			levelSetSpawner(i, levelAlignXToMirror(sReadU16() << 16), sReadU16() << 16, baseCircleIndex + sRead8(), sRead8(), sReadU16(), sReadU16(), sReadU16(), sReadU16(), sReadU16());

		// Init enemies.
		initEnemies(num = sRead8());

		for (int i = 0; i < num; i++)
			levelSetEnemy(i, baseCircleIndex + sReadU8(), sRead8() == 1, sReadU16(), sReadU16() << 14, sRead8() == 1, sReadU16(), sReadU16() << 14, sReadU16() << 16, sRead8() == 1, sReadU16());

		// Init spider legs.
		initSpiderLegs(num = sRead8());

		for (int i = 0; i < num; i++)
			levelSetSpiderLeg(i, baseCircleIndex + sReadU8(), sRead8(), sReadU16() << 16, sReadU16());

		// Init expanders.
		levelInitExpanders(num = sRead8());

		for (int i = 0; i < num; i++)
			levelSetExpander(i, baseCircleIndex + sReadU8(), sRead8() == 1, sReadU16(), sReadU16() << 16);

		// Init animated (actively resizing) rectangles.
		levelInitAnimatedRects(num = sRead8());

		for (int i = 0; i < num; i++)
			levelSetAnimRect(i, i, sReadU16() << 16, sReadU16() << 16, sReadU16() << 8, sReadU16(), sReadU16());

		// Init signs.
		initLevelSigns(num = sRead8());

		for (int i = 0; i < num; i++)
			levelSetSign(i, levelAlignXToMirror(sReadU16() << 16), sReadU16() << 16, sRead8());

		// Init player.
		initPlayer(isStandardLevel);

		// Default level values.
		levelDeathTicks = 70;
		levelMovableGravity = 40000;
		levelBombExplodeTicks = 0;
		levelBombStartTicks = -10000;
		levelPlayerHitTicks = 0;
		levelCameraZoom = LEVEL_CAMERA_ZOOM_DEFAULT;
		levelMovableAccel = 62000;

		// Point the camera to the player.
		levelSetCamera(levelCircleX[0], levelCircleY[0], 0);

		// If in a standard level, Waz will fall out from the rocket.
		if (levelId >= 0) {
			for (int i = 0; i < 5; i++) {
				levelCircleY[i] -= 0xbe0000;
				levelCirclePrevY[i] -= 0xbe0000;
			}
		}

		levelIntroTicks = 0;

		if (levelId == -2)
			levelShipDisablePods();
		else
			levelObtainIds();

		// Init ping.
		if (isFinalLevel)
			initPing();
	}

	// Hardcoded ship pods disabling.
	public static final void levelShipDisablePods() {
		for (int i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 4) > 0 &&
					levelCircleHasPhysics[i]) {

				switch (levelCircleType[i]) {

					// Disable "MORE GAMES" pod physics and collision.
					case 7:
						levelCircleFlags[i] = 0;
						levelCircleHasPhysics[i] = false;
				}
			}
		}
	}

	public static final int levelAlignXToMirror(int x) {
		if (isMirrored)
			return x > 0 && x < 10000 ? (levelWidth >> 16) - x : levelWidth - x;
		else
			return x;
	}

	public static final int levelAddPlayerHitbox(int id, int x, int y, int size) {
		byte flags = 3;
		if (id > 0)
			// Disable the collision of the other players.
			flags = 0;

		levelSetCircle(id++, x, y, 0xa0000, 60, flags, 0, true);

		levelSetCircle(id++, x, y - size, 0x30000, 60, flags, 0, true);
		levelSetCircle(id++, x, y + size, 0x30000, 60, flags, 0, true);
		levelSetCircle(id++, x - size, y, 0x30000, 60, flags, 0, true);
		levelSetCircle(id++, x + size, y, 0x30000, 60, flags, 0, true);

		return id;
	}

	// Hook side hitboxes to the center hitbox of the player.
	public static final int levelSetPlayerHook(int hookId, int circleId, int hookDist) {
		// Lock hitbox axis.
		levelSetHook(hookId++, circleId + 0, circleId + 1, 0, 0, 0, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 2, 0, 0, 0, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 3, 1, 0, 0, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 4, 1, 0, 0, false, true);

		// Limit hitbox distance.
		levelSetHook(hookId++, circleId + 1, circleId + 0, 6, 0x30000, 0, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 2, 6, 0x30000, 0, false, true);
		levelSetHook(hookId++, circleId + 3, circleId + 0, 5, 0x30000, 0, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 4, 5, 0x30000, 0, false, true);

		// Elastic pull.
		levelSetHook(hookId++, circleId + 0, circleId + 1, 4, hookDist, 28000, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 2, 4, hookDist, 28000, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 3, 4, hookDist, 28000, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 4, 4, hookDist, 28000, false, true);

		// Elastic push.
		levelSetHook(hookId++, circleId + 0, circleId + 1, 3, hookDist, 15000, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 2, 3, hookDist, 12000, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 3, 3, hookDist, 15000, false, true);
		levelSetHook(hookId++, circleId + 0, circleId + 4, 3, hookDist, 15000, false, true);

		return hookId;
	}

	public static final void swapLevelData(int swapKey) {
		if (swapKey != activeSwapKey) {
			activeSwapKey = swapKey;

			int tmp1;
			boolean tmp2;

			tmp1 = levelWidth;
			levelWidth = swappedLevelWidth;
			swappedLevelWidth = tmp1;

			tmp1 = levelHeight;
			levelHeight = swappedLevelHeight;
			swappedLevelHeight = tmp1;

			tmp1 = levelMovableGravity;
			levelMovableGravity = swappedLevelMovableGravity;
			swappedLevelMovableGravity = tmp1;

			tmp1 = levelMovableAccel;
			levelMovableAccel = swappedLevelMovableAccel;
			swappedLevelMovableAccel = tmp1;

			tmp2 = levelIsPlayerOnSurface;
			levelIsPlayerOnSurface = swappedLevelIsPlayerOnSurface;
			swappedLevelIsPlayerOnSurface = tmp2;

			tmp2 = isLevelComplete;
			isLevelComplete = swappedIsLevelComplete;
			swappedIsLevelComplete = tmp2;

			swapLevelCircleData();
			swapLevelRectData();
			swapLevelHookData();
			swapCameraData();
			swapLevelPlayerData();
		}
	}

	// Load all sounds used by the game.
	public static final void loadAllSounds() {
		initSoundSystem(SOUND_NUM_SOUNDS);
		loadSoundToIndex(SOUND_ID_TITLE, "title", 1, 15804);
		loadSoundToQueue(SOUND_ID_SHIP, "ship", 1, 13962);
		loadSoundToQueue(SOUND_ID_INGAME0, "ingame0", 1, 10987);
		loadSoundToQueue(SOUND_ID_INGAME1, "ingame1", 1, 21882);
		loadSoundToQueue(SOUND_ID_INGAME2, "ingame2", 1, 13885);
		loadSoundToQueue(SOUND_ID_INGAME3, "ingame3", 1, 21561);
		loadSoundToQueue(SOUND_ID_INGAME4, "ingame4", 1, 14583);
		loadSoundToQueue(SOUND_ID_JINGLE_FLYIN, "jingle_flyIn", 1, 980);
		loadSoundToQueue(SOUND_ID_JINGLE_COMPLETE, "jingle_complete", 1, 815);
		loadSoundToQueue(SOUND_ID_SFX_JUMP, "sfx_jump", 6, 6904);
		loadSoundToQueue(SOUND_ID_SFX_JUMP2, "sfx_jump2", 6, 10800);
		loadSoundToQueue(SOUND_ID_SFX_HURT, "sfx_hurt", 6, 6942);
		loadSoundToQueue(SOUND_ID_SFX_HURT2, "sfx_hurt2", 6, 7742);
		loadSoundToQueue(SOUND_ID_SFX_XIFU, "sfx_xifu", 6, 9120);
		loadSoundToQueue(SOUND_ID_SFX_XIFU2, "sfx_xifu2", 6, 12386);
		loadSoundToQueue(SOUND_ID_SFX_EMO_STARTLED, "sfx_emo_startled", 6, 6252);
		loadSoundToQueue(SOUND_ID_SFX_EMO_SURPRISED, "sfx_emo_surprised", 6, 6170);
		loadSoundToQueue(SOUND_ID_SFX_EMO_HAPPY, "sfx_emo_happy", 6, 8682);
		loadSoundToQueue(SOUND_ID_SFX_EMO_ANGRY, "sfx_emo_angry", 6, 8036);
		loadSoundToQueue(SOUND_ID_SFX_EMO_BORED, "sfx_emo_bored", 6, 19966);
		loadSoundToQueue(SOUND_ID_SFX_DISTRESS, "sfx_distress", 6, 8228);
		loadSoundToQueue(SOUND_ID_SFX_ROCKET, "sfx_rocket", 6, 43036);
		loadSoundToQueue(SOUND_ID_SFX_BOMB, "sfx_bomb", 6, 10672);
		loadSoundToQueue(SOUND_ID_SFX_GO, "sfx_go", 6, 12500);
		loadSoundToQueue(SOUND_ID_SFX_HURRA, "sfx_hurra", 6, 10690);
	}

	public static final void playSound(int index) {
		if (index == currentSoundId && index != -1) {
			if (index >= 0) {
				if (!isSoundActive(currentSoundId, -1)) {
					playSound(currentSoundId, -1);
				}
			}
		} else {
			queueAllSoundsForCleanup();
			queueSoundCleanup();
			currentSoundId = index;
			if (index >= 0) {
				playSound(currentSoundId, -1);
			}
		}
	}

	public static final void playCurrentSound() {
		if (currentSoundId >= 0 &&
				!isPaused &&
				!isSoundActive(currentSoundId, -1)) {

			playSound(currentSoundId, -1);
		}
	}

	// Play the background soundtrack.
	public static final void playCurrentBackgroundMusic() {
		switch (levelBackgroundId) {
			case 0:
				playSound(SOUND_ID_INGAME0);
				return;

			case 1:
				playSound(SOUND_ID_INGAME1);
				return;

			case 2:
				playSound(SOUND_ID_INGAME2);
				return;

			case 3:
				playSound(SOUND_ID_INGAME3);
				return;

			case 4:
				playSound(SOUND_ID_INGAME4);
		}
	}

	public static final void playSfxJump() {
		if (rand8() % 3 == 0 && !isShipPaused) {
			if (rand8() % 2 == 0)
				playSound(SOUND_ID_SFX_JUMP2, 1);
			else
				playSound(SOUND_ID_SFX_JUMP, 1);
		}
	}

	public static final void playSfxHurt() {
		if (rand8() % 2 == 0)
			playSound(SOUND_ID_SFX_HURT2, 1);
		else
			playSound(SOUND_ID_SFX_HURT, 1);
	}

	public static final void playJingleFlyIn() {
		playSound(-1);
		playSound(SOUND_ID_JINGLE_FLYIN, 1);
	}

	public static final void playSfxRocket() {
		playSound(SOUND_ID_SFX_ROCKET, 1);
	}

	public static final void playSfxBomb() {
		playSound(SOUND_ID_SFX_BOMB, 1);
	}

	public static final void playSfxDistress() {
		playSound(SOUND_ID_SFX_DISTRESS, 1);
	}

	public static final void playJingleComplete() {
		playSound(-1);
		playSound(SOUND_ID_JINGLE_COMPLETE, 1);
	}

	public static final void playSfxHurra() {
		playSound(SOUND_ID_SFX_HURRA, 1);
	}

	public static final void playSfxGo() {
		playSound(SOUND_ID_SFX_GO, 1);
	}

	public static final void playSfxXifu() {
		currentSoundSfxXifuState = !currentSoundSfxXifuState;
		if (currentSoundSfxXifuState)
			playSound(SOUND_ID_SFX_XIFU2, 1);
		else
			playSound(SOUND_ID_SFX_XIFU, 1);
	}

	/* Function that sets weather and background of the level by their ID.
	 * If the specified background isn't known by this function, we'll fallback
	 * to known background and weather. */
	public static final void setWeatherAndBackgroundIds(int weatherId, int bgId) {
		boolean bgFound = false;

		for (int i = 0; i < knownBackgrounds.length; i++)
			if (knownBackgrounds[i] == bgId)
				bgFound = true;

		if (!bgFound) {
			bgId = knownBackgrounds[level % knownBackgrounds.length];

			if (weatherId > 0)
				weatherId = fallbackWeathers[bgId];
		}

		levelWeatherId = weatherId;
		levelBackgroundId = bgId;
	}

	// Load background by its ID.
	public static final void loadBackground(int id) {
		imgsBackground = new Image[5];
		for (int i = 0; i < 5; i++)
			imgsBackground[i] = loadImage("bg_" + id + "_" + i + ".pim", "bg_" + id + "_" + i + ".ppl");

		currentBackgroundHasObjects = backgroundHasObjects[id];
		activeBackgroundPattern = backgroundPatterns[id];
		activeBackgroundObjectsPattern = backgroundObjectsPattern[id];

		int bgHeight = getBackgroundTotalHeight(activeBackgroundPattern);
		int bgObjHeight = getBackgroundTotalHeight(activeBackgroundObjectsPattern) - imgsBackground[activeBackgroundObjectsPattern[0][0]].getHeight();

		// This field is always zero.
		backgroundOffsetY = 0;
		/* Constant value of the Y end of the background during the entire
		 * level. */
		backgroundEndPosY = -(bgHeight - (SCREEN_HEIGHT - bgObjHeight));
		currentBackgroundTotalHeight = bgHeight;
		/* Constant value of the Y end of the background objects during the
		 * entire level. */
		backgroundObjectsEndPosY = SCREEN_HEIGHT - bgObjHeight;

		currentLoadedBackgroundId = id;
	}

	public static final void renderBackground() {
		if (currentLevelLoaded >= 0) {
			// If in a standard level, render an image of the background.

			// Load background if not loaded yet.
			if (imgsBackground == null || levelBackgroundId != currentLoadedBackgroundId)
				loadBackground(levelBackgroundId);

			int x, y;

			if (!currentBackgroundHasObjects) {
				// Render background WITHOUT foreground objects.
				y = backgroundOffsetY + (backgroundEndPosY - backgroundOffsetY) * (levelCameraY >> 16) / (levelHeight >> 16);
				x = -((getBackgroundTotalWidth(activeBackgroundPattern) - SCREEN_WIDTH) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackgroundPattern(activeBackgroundPattern, x, y, false);
			} else {
				// Render background WITH foreground objects.
				y = backgroundOffsetY + (backgroundEndPosY - backgroundOffsetY) * (levelCameraY >> 16) / (levelHeight >> 16);
				x = -((getBackgroundTotalWidth(activeBackgroundPattern) - SCREEN_WIDTH) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackgroundPattern(activeBackgroundPattern, x, y, false);

				// Render foreground objects at the very bottom.
				y = currentBackgroundTotalHeight + (backgroundObjectsEndPosY - currentBackgroundTotalHeight) * (levelCameraY >> 16) / (levelHeight >> 16);
				x = -((getBackgroundTotalWidth(activeBackgroundObjectsPattern) - SCREEN_WIDTH) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackgroundPattern(activeBackgroundObjectsPattern, x, y, true);
			}
		} else {
			// If in a non-standard level (e.g. holodeck), render the background manually.

			setFadeColor(0xdcc86d);
			gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

			setFadeColor(0);
			for (int i = 0; i < SCREEN_HEIGHT; i += 40)
				gDrawLine(0, i, SCREEN_WIDTH, i);

			for (int i = 0; i < SCREEN_WIDTH; i += 40)
				gDrawLine(i, 0, i, SCREEN_HEIGHT);
		}
	}

	public static final void renderBackgroundPattern(int[][] pattern, int x, int y, boolean renderObjects) {
		int indexY = 0;

		// The same as the next `while` loop, but don't increment `y` variable.
		if (renderObjects) {
			int offX = x;

			for (int i = 0; i < pattern[0].length; i++) {
				Image img = imgsBackground[pattern[0][i]];

				if (offX < SCREEN_WIDTH &&
						offX > -img.getWidth() &&
						y > 0 &&
						y < SCREEN_HEIGHT + img.getHeight()) {

					gDrawImage(img, offX, y - img.getHeight(), 0);
				}

				offX += img.getWidth();
			}

			indexY++;
		}

		while (indexY < pattern.length) {
			int offX = x;
			Image img = null;

			for (int i = 0; i < pattern[indexY].length; i++) {
				img = imgsBackground[pattern[indexY][i]];

				if (offX < SCREEN_WIDTH &&
						offX > -img.getWidth() &&
						y < SCREEN_HEIGHT &&
						y > -img.getHeight()) {

					gDrawImage(img, offX, y, 0);
				}

				offX += img.getWidth();
			}

			y += img.getHeight();

			indexY++;
		}
	}

	public static final int getBackgroundTotalWidth(int[][] pattern) {
		int width = 0;

		for (int i = 0; i < pattern[0].length; i++)
			width += imgsBackground[pattern[0][i]].getWidth();

		return width;
	}

	public static final int getBackgroundTotalHeight(int[][] pattern) {
		int height = 0;

		for (int i = 0; i < pattern.length; i++)
			height += imgsBackground[pattern[i][0]].getHeight();

		return height;
	}

	public static final void levelSetCamera(int x, int y, int angle) {
		// Check target position.
		if (activeSwapKey == 0) {
			y = levelHeight / 2;

			if (x < 0x780000)
				x = 0x780000;

			if (x > levelWidth - 0x780000)
				x = levelWidth - 0x780000;
		}

		levelCameraX = x;
		levelCameraY = y;
		levelCameraIntX = x >> 16;
		levelCameraIntY = y >> 16;

		levelCameraAngle = angle;

		while (levelCameraAngle > 359)
			levelCameraAngle -= 360;

		while (levelCameraAngle < 0)
			levelCameraAngle += 360;

		levelCameraZoom = LEVEL_CAMERA_ZOOM_DEFAULT;
	}

	public static final boolean levelCameraApproach(int x, int y, int reserved) {
		return levelCameraApproach(x, y, reserved, 0x1e0000);
	}

	public static final boolean levelCameraApproach(int x, int y, int reserved, int diffMax) {
		// Check target position.
		if (activeSwapKey == 0)
			y = levelHeight / 2;
		else if (y > levelHeight - 0xa00000)
			y = levelHeight - 0xa00000;

		if (x < 0x780000)
			x = 0x780000;

		if (x > levelWidth - 0x780000)
			x = levelWidth - 0x780000;

		int xDiff = x - levelCameraX;
		int yDiff = y - levelCameraY;

		// Check if the axis differences are exceeding `diffMax`.
		if (xDiff > diffMax)
			xDiff = diffMax;

		if (xDiff < -diffMax)
			xDiff = -diffMax;

		if (yDiff > diffMax)
			yDiff = diffMax;

		if (yDiff < -diffMax)
			yDiff = -diffMax;

		int slopeAngle = xDiff >> 17;

		while (slopeAngle > 359)
			slopeAngle -= 360;

		while (slopeAngle < 0)
			slopeAngle += 360;

		if ((slopeAngle >= 180 || levelCameraAngle >= 180) &&
				(slopeAngle <= 180 || levelCameraAngle <= 180)) {

			// Upside down isn't allowed :)
			if (slopeAngle > 180)
				slopeAngle -= 360;

			if (levelCameraAngle > 180)
				levelCameraAngle -= 360;

			levelCameraAngle += (slopeAngle - levelCameraAngle) / 5;
		} else {
			levelCameraAngle += (slopeAngle - levelCameraAngle) / 5;
		}

		while (levelCameraAngle > 359)
			levelCameraAngle -= 360;

		while (levelCameraAngle < 0)
			levelCameraAngle += 360;

		// Handle camera zooming when player is moving.
		if (activeSwapKey != 0) {
			int playerXDiff = levelCircleX[0] - levelCirclePrevX[0];
			if (playerXDiff < 0)
				playerXDiff = -playerXDiff;

			// Divide by 256.
			playerXDiff >>= 8;

			int targetZoom = 110 - playerXDiff * LEVEL_CAMERA_ZOOM_DEFAULT * 45 / 0x32000;
			if (targetZoom < 50)
				targetZoom = 50;

			int diffZoom = (targetZoom - levelCameraZoom) / 10;
			if (diffZoom <= 10 && diffZoom >= 10) {

				/* This condition is NEVER executed as this is impossible to
				 * have >= 100 zoom difference. */
				if (diffZoom > 0)
					levelCameraZoom++;
				else if (diffZoom < 0)
					levelCameraZoom--;

			} else {

				levelCameraZoom += (targetZoom - levelCameraZoom) / 10;

			}
		} else {
			// In the ship, lock to these values.
			levelCameraZoom = LEVEL_CAMERA_ZOOM_DEFAULT;
			levelCameraAngle = 0;
		}

		if (xDiff < 0xa0000 && xDiff > -0xa0000 && yDiff < 0xa0000 && yDiff > -0xa0000) {
			// Target camera position is reached.
			return true;
		} else {
			// Target camera position is NOT reached yet, keep approaching.
			levelCameraX += xDiff / 5;
			levelCameraY += yDiff / 5;
			levelCameraIntX = levelCameraX >> 16;
			levelCameraIntY = levelCameraY >> 16;
			return false;
		}
	}

	public static final int levelAlignX(int x, int y) {
		return levelAlignX(x - levelCameraX, y - levelCameraY, levelCameraAngle) + (SCREEN_WIDTH << 15) >> 16;
	}

	public static final int levelAlignY(int x, int y) {
		return levelAlignY(x - levelCameraX, y - levelCameraY, levelCameraAngle) + (SCREEN_HEIGHT << 15) >> 16;
	}

	public static final void swapCameraData() {
		int tmp;

		tmp = levelCameraIntX;
		levelCameraIntX = swappedLevelCameraIntX;
		swappedLevelCameraIntX = tmp;

		tmp = levelCameraIntY;
		levelCameraIntY = swappedLevelCameraIntY;
		swappedLevelCameraIntY = tmp;

		tmp = levelCameraX;
		levelCameraX = swappedLevelCameraX;
		swappedLevelCameraX = tmp;

		tmp = levelCameraY;
		levelCameraY = swappedLevelCameraY;
		swappedLevelCameraY = tmp;

		tmp = levelCameraAngle;
		levelCameraAngle = swappedLevelCameraAngle;
		swappedLevelCameraAngle = tmp;
	}

	public static final void loadFacesIcons() {
		if (imgsFaces == null) {
			imgsFaces = new Image[5][];
			imgsFaces[0] = new Image[2];

			for (int i = 0; i < 2; i++)
				imgsFaces[0][i] = loadImage("wiz" + i + ".pim", "wiz" + i + ".ppl");

			imgsFaces[1] = new Image[2];

			for (int i = 0; i < 2; i++)
				imgsFaces[1][i] = loadImage("waz" + i + ".pim", "waz" + i + ".ppl");

			imgsFaces[2] = new Image[2];

			for (int i = 0; i < 2; i++)
				imgsFaces[2][i] = loadImage("purple" + i + ".pim", "purple" + i + ".ppl");

			imgsFaces[3] = new Image[2];

			for (int i = 0; i < 2; i++)
				imgsFaces[3][i] = loadImage("hal" + i + ".pim", "hal" + i + ".ppl");

			hideDialogueBox();
		}
	}

	public static final void hideDialogueBox() {
		dialogueIsShown = 0;
	}

	public static final void setCurrentDialogue(String string, int faceId) {
		int boxHeight = 12 + dialogueNumLines * 19;
		dialogueFace = faceId;
		dialogueNumLines = numNewLines(string);
		dialogueText = string;
		dialogueStartMs = millis();

		if (dialogueIsShown == 1) {
			/* If the dialogue box wasn't hidden before the next one appeared
			 * continue sliding it so that the new text fits. */
			int newBoxHeight = 12 + dialogueNumLines * 19;
			dialogueStartMs -= (long)(boxHeight * SCRIPT_DIALOGUE_BOX_SLIDE_MS / newBoxHeight);
		}

		dialogueIsShown = 1;
		dialogueFaceIteration = 0;
		dialogueIteration = 0;
		dialogueDone = false;
	}

	public static final void renderDialogueBox() {
		if (dialogueIsShown == 1) {
			long currTime = millis();
			int elapsed = (int)(currTime - dialogueStartMs);
			int boxHeight = 12 + dialogueNumLines * 19;

			// Make slide effect if we're just starting the next dialogue box.
			if (dialogueIteration == 0 && elapsed < SCRIPT_DIALOGUE_BOX_SLIDE_MS)
				boxHeight = boxHeight * elapsed / SCRIPT_DIALOGUE_BOX_SLIDE_MS;

			gSetColor(0xffffff); // White.
			gFillRect(0, 0, SCREEN_WIDTH, boxHeight);
			gSetColor(0xece9d8); // Light gray.
			gDrawLine(0, boxHeight, SCREEN_WIDTH, boxHeight);
			gSetColor(0xaca899); // Darker gray.
			gDrawLine(0, boxHeight + 1, SCREEN_WIDTH, boxHeight + 1);
			gSetColor(0); // Black.
			gDrawLine(0, boxHeight + 2, SCREEN_WIDTH, boxHeight + 2);

			// Render.
			if (dialogueIteration != 0 || elapsed >= SCRIPT_DIALOGUE_BOX_SLIDE_MS) {
				byte textX = 0;
				if (imgsFaces[dialogueFace] != null) {
					Image img = imgsFaces[dialogueFace][dialogueFaceIteration % imgsFaces[dialogueFace].length];
					int imgX = (27 - img.getWidth()) / 2;

					if (dialogueFace == 0) {
						// Wiz at the left.
						gDrawImage(img, 0 + imgX, 6 + (dialogueNumLines * 19 - img.getHeight()) / 2, 0);
						textX = 27;
					} else {
						// Other at the right.
						gDrawImage(img, SCREEN_WIDTH - 27 + imgX, 6 + (dialogueNumLines * 15 - img.getHeight()) / 2, 0);
						textX = 8;
					}

					// Render a text itself.
					renderTextEx(textX, 6, SCREEN_WIDTH - 35, dialogueNumLines * 19, dialogueText, 1, 0, dialogueIteration, false);
				} else {
					renderTextEx(17, 6, SCREEN_WIDTH - 35, dialogueNumLines * 19, dialogueText, 1, 0, dialogueIteration, false);
				}

				/* Find the next space or period character in the script
				 * string. */
				if (currTime > dialogueStartMs) {
					if (dialogueFace == 2 && dialogueIteration == 0 && scriptEnvironment == SCRIPT_ENV_TRANSMISSION)
						playSfxXifu();

					dialogueStartMs = currTime + 150L;
					dialogueIteration++;

					char ch;
					while (dialogueIteration < dialogueText.length() &&
							(ch = dialogueText.charAt(dialogueIteration)) != ' ' &&
							ch != '.') {

						dialogueIteration++;
					}

					if (dialogueIteration >= dialogueText.length()) {
						dialogueDone = true;
						return;
					}

					dialogueFaceIteration++;
				}
			}
		}
	}

	public static final int numNewLines(String string) {
		return getNewLineIndices(SCREEN_WIDTH - 35, string, 1).length;
	}

	// Split a continuous string into string array (lines).
	public static final String[] strSplitLines(String string, int fontId) {
		int[] lineIndices = getNewLineIndices(SCREEN_WIDTH - 35, string, fontId);
		String[] arr = new String[lineIndices.length];

		for (int i = 0; i < lineIndices.length; i++) {
			if (i == lineIndices.length - 1)
				arr[i] = string.substring(lineIndices[i]);
			else
				arr[i] = string.substring(lineIndices[i], lineIndices[i + 1]);
		}

		return arr;
	}

	public static final void sceneLevelUpdate() {
		if (levelScriptToRun > -1) {
			// Handle script if needed.
			levelDisplayScript(levelScriptToRun);
			levelScriptToRun = -1;
		} else {
			// Handle level contents and state.
			if (nextLevelToLoad >= -1) {
				loadLevel(nextLevelToLoad);
				nextLevelToLoad = -2;

				if (currentLevelLoaded < 0)
					levelIntroTicks = 120;

				/* If died in a standard level, show deja vu cutscene only ONE
				 * time during the entire game session. */
				if (!isDejaVuMessageShown) {
					if (currentLevelLoaded >= 0)
						startScript("respawn.bms", levelCircleX[0], levelCircleY[0]);

					isDejaVuMessageShown = true;
					isPlayerAlive = true;
					return;
				}

				isPlayerAlive = true;
			}

			// Enable the level physics.
			if (levelIntroTicks >= 50) {
				updateAll();
				levelTicks++;
			}

			// Process a player death.
			if (levelPlayerHealth < 1) {
				if (levelDeathTicks == 70) {
					vibrate(40);
					isPlayerAlive = false;
					levelCircleRadius[0] = 0x20000;
					levelCircleRadius[2] = 0x10000;
					levelCircleRadius[3] = 0x10000;
					levelCircleRadius[4] = 0x10000;
					levelCircleFlags[2] = 1;
					levelCircleX[2] = levelCircleX[0];
					levelCircleY[2] = levelCircleY[0] + 0xa0000;

					for (int i = 0; i < 2; i++) {
						for (int j = 0; j < 2; j++) {
							levelHookType[8 + 2 * i + j] = 3;
							levelHookStretchConstant[8 + 2 * i + j] = 500;
							levelHookExtent[8 + 2 * i + j] = 0x280000;
						}
					}

					if (levelPlayerCurrentGrabberFlags >= 0) {
						levelHookIsActive[16] = false;
						levelCircleFlags[levelHookId2[16]] = levelPlayerCurrentGrabberFlags;
						levelPlayerCurrentGrabberFlags = -1;
						levelPlayerIgnoreGrabberId = levelHookId2[16];
					}
				}

				// Move the camera to follow the player, but slower movement.
				levelCameraApproach(levelCircleX[0] + 10 * (levelCircleX[0] - levelCirclePrevX[0]), levelCircleY[0] + 10 * (levelCircleY[0] - levelCirclePrevY[0]), 0, 0x140000);

				if ((levelPlayerRed -= 3) < 0)
					levelPlayerRed = 0;

				if (--levelDeathTicks <= 0)
					nextLevelToLoad = currentLevelLoaded;
			}

			// Process softkeys.
			int softkeyId = -1;
			if (levelIntroTicks < 100)
				softkeyId = softkeyPressed(-1, 3);
			else if (currentLevelLoaded == -1)
				softkeyId = softkeyPressed(-1, 4);
			else if (levelPlayerHealth > 0)
				softkeyId = softkeyPressed(-1, 0);

			if (softkeyId == 4) {
				// Exit holodeck.
				mustExitHolodeck = true;
			}

			if (softkeyId == 3) {
				// Skip intro.
				levelIntroTicks = 120;
			}

			if (softkeyId == 0) {
				// Pause.
				softkeyPressed(-1, -1);
				isShipPaused = true;
				fade = 100;
				swapLevelData(0);
				setNewState(STATE_ID_SHIP, 0);
			} else {
				// Process the level.

				// Process controls and camera.
				if (isPlayerAlive && levelIntroTicks >= 100) {
					updatePlayerControls();
					/* Move the camera to follow the player, but slightly ahead (a
					 * bit more right, or left, depending on current direction of
					 * movement). */
					levelCameraApproach(levelCircleX[0] + 10 * (levelCircleX[0] - levelCirclePrevX[0]), levelCircleY[0] + 10 * (levelCircleY[0] - levelCirclePrevY[0]), 0, 0x320000);
				}

				// Process bomb object.
				if (levelCircleHasPhysics[levelBombObjectId]) {
					if (levelTicks > levelBombStartTicks + 100)
						levelSetCamera(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], 0);

					if (levelBombExplodeTicks == 0 &&
							levelTicks > levelBombStartTicks + 130) {

						// Begin explode.
						levelBombExplodeTicks = 5;
						playSfxBomb();
						vibrate(10);
					}
				}

				// Process bomb explosion.
				if (levelBombExplodeTicks > 0) {
					levelSetCamera(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], 0);
					levelBombExplodeTicks--;
					levelCircleWeight[levelBombObjectId] = 0;

					if (levelBombExplodeTicks > 0) {
						levelCircleRadius[levelBombObjectId] += 0xf0000;
					} else {
						// Reset bomb object.
						levelCircleHasPhysics[levelBombObjectId] = false;
						levelCircleX[levelBombObjectId] = -0xc80000;
					}
				}

				// Render a level.
				levelRender(true);

				// Render intro.
				if (levelIntroTicks < 100) {
					int rocketY;
					if (levelIntroTicks < 80)
						rocketY = -72 + levelIntroTicks * 72 / 50;
					else
						rocketY = -72 + (100 - levelIntroTicks) * 72 / 20;

					if (rocketY > 0)
						rocketY = 0;

					if (levelIntroTicks == 80) {
						playSfxRocket();
						vibrate(15);
					}

					if (levelIntroTicks < 80)
						renderRocket(((SCREEN_WIDTH - 123) >> 1) - 7, rocketY, -1);
					else
						renderRocket(rand8() % 5 - 2 + (SCREEN_WIDTH >> 1) - 69, rocketY, levelIntroTicks);

					renderText(4, SCREEN_HEIGHT - 19, currentPlanetText, 0);
					levelIntroTicks++;
				}

				refreshGame();
			}
		}
	}

	public static final void updatePlayerControls() {
		// Process player jump and/or grabber release.
		if ((isKeyPressed(KEY_UP | KEY_TOP_LEFT | KEY_TOP_RIGHT) ||
					isKeyPressed(KEY_OK) &&
					!isKeyPressed(KEY_SOFT_LEFT)) &&
				(levelIsPlayerOnSurface || levelPlayerCurrentGrabberFlags >= 0)) {

			if (levelPlayerCurrentGrabberFlags >= 0)
				levelPlayerReleaseGrabber();

			if (levelIsPlayerOnSurface) {
				playSfxJump();
				levelPlayerJumpTicks = LEVEL_DEFAULT_JUMP_TICKS;
			}
		}

		// Process player moving right.
		if (isKeyHeld(KEY_RIGHT | KEY_TOP_RIGHT)) {
			levelPlayerDirection = 1;

			if (levelPlayerCurrentGrabberFlags >= 0 && !levelIsPlayerOnSurface) {
				if (levelCircleX[0] >= levelCirclePrevX[0]) {

					if (levelCircleX[0] - levelCirclePrevX[0] < 0x10000)
						// A sharp shove.
						levelCircleX[0] += 0x28000;
					else
						levelCircleX[0] += 0x14000;

				}
			} else {
				levelCircleX[0] += 0x14000;
				if (levelIsPlayerOnSurface)
					levelPlayerRotationRate += 3;
			}
		}

		// Process player moving left.
		if (isKeyHeld(KEY_LEFT | KEY_TOP_LEFT)) {
			levelPlayerDirection = -1;

			if (levelPlayerCurrentGrabberFlags >= 0 && !levelIsPlayerOnSurface) {
				if (levelCircleX[0] <= levelCirclePrevX[0]) {

					if (levelCirclePrevX[0] - levelCircleX[0] < 0x10000)
						// A sharp shove.
						levelCircleX[0] -= 0x28000;
					else
						levelCircleX[0] -= 0x14000;

				}
			} else {
				levelCircleX[0] -= 0x14000;
				if (levelIsPlayerOnSurface)
					levelPlayerRotationRate -= 3;
			}
		}

		// This IS NOT related to the story. Process cheat codes.
		if (isKeyReleased(KEY_ALL)) {

			// Level complete.
			if (isKeyReleased(cheatCodeLevelComplete[iterCheatLevelComplete])) {
				iterCheatLevelComplete++;
				if (iterCheatLevelComplete >= cheatCodeLevelComplete.length) {
					isLevelComplete = true;
					iterCheatLevelComplete = 0;
				}
			} else {
				iterCheatLevelComplete = 0;
			}

			// Invincibility.
			if (isKeyReleased(cheatCodeInvincibility[iterCheatInvincibility])) {
				iterCheatInvincibility++;
				if (iterCheatInvincibility >= cheatCodeInvincibility.length) {
					isPlayerInvincible = !isPlayerInvincible;
					levelCircleY[0] -= 0x80000;
					levelCircleX[3] -= 0x80000;
					levelCircleX[4] += 0x80000;
					iterCheatInvincibility = 0;
				}
			} else {
				iterCheatInvincibility = 0;
			}
		}
	}

	public static final void levelRender(boolean renderPlayer) {
		long elapsed = millis();

		// A random clap of thunder.
		if (levelWeatherId == 1 && activeSwapKey == 1) {
			fade = 100;
			if (rand16() % 100 == 50)
				fade = 0;
		}

		renderBackground();

		// Render portals.
		for (int i = 0; i < levelIdsPortals.length; i++) {
			short id = levelIdsPortals[i];

			setFadeColor(levelColorPortal);
			levelRenderCircle(levelCircleX[id], levelCircleY[id], levelCircleRadius[id]);

			setFadeColor(255, 255, 255);
			if (levelCircleRadius[id] > levelPortalFilledRadius) {
				levelRenderCircleOutline(levelCircleX[id], levelCircleY[id], levelPortalFilledRadius);
			} else {
				// The portal is fully filled.
				int rad = levelCircleRadius[id] / 4;
				int timeRad = (int)((long)rad * (elapsed % 700L)) / 700;

				for (int j = 0; j < 4; j++)
					levelRenderCircleOutline(levelCircleX[id], levelCircleY[id], levelCircleRadius[id] - timeRad - j * rad);
			}
		}

		renderSigns();

		// Render a entity (for now only purple is used).
		if (levelEntityY > 0)
			levelRenderPurple(levelEntityX, levelEntityY);

		// Render a player.
		if (renderPlayer)
			levelRenderPlayer();

		// Render enemies.
		renderEnemies();

		// Render movable balls.
		for (int i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 4) > 0 &&
					levelCircleHasPhysics[i] &&
					(levelCircleFlags[i] & 0x2000) == 0 &&
					(levelCircleFlags[i] & 8) == 0 &&
					i != levelBombObjectId) {

				setFadeColor(0, 0, 0);
				levelRenderCircle(levelCircleX[i], levelCircleY[i], levelCircleRadius[i]);

				setFadeColor(levelColorMovable);
				levelRenderCircle(levelCircleX[i], levelCircleY[i], levelCircleRadius[i]);
			}
		}

		// Render a bomb.
		if (levelCircleHasPhysics[levelBombObjectId]) {
			setFadeColor(0, 0, 0);
			levelRenderCircle(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], levelCircleRadius[levelBombObjectId]);

			if (levelBombStartTicks != -1 && levelTicks > levelBombNextFlashTick) {
				if (levelBombFlashed) {
					setFadeColor(255, 255, 255);
					levelBombFlashed = false;
					levelBombNextFlashTick = levelTicks + (levelBombStartTicks + 100 - levelTicks) / 4;
				} else {
					setFadeColor(200, 0, 0);
					levelBombFlashed = true;
				}
			} else {
				setFadeColor(200, 0, 0);
			}

			levelRenderCircle(levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId], levelCircleRadius[levelBombObjectId]);
			levelDrawImage(imgFuse, levelCircleX[levelBombObjectId], levelCircleY[levelBombObjectId] - levelCircleRadius[levelBombObjectId]);
		}

		levelRenderCircleIds(levelIdsPlatforms, 0);
		levelRenderRects();
		levelRenderCircleIds(levelIdsPlatforms, levelColor);

		// Render foams.
		levelRenderCircleIds(levelIdsFoam, 0);
		levelRenderCircleIds(levelIdsFoam, 0xffffff);

		// Render destructibles.
		levelRenderCircleIds(levelIdsDestructibles, 0);
		levelRenderCircleIds(levelIdsDestructibles, levelColorDestructible);

		// Render radioactive goos with an animated color.
		int fadeCoeff = (int)(elapsed % 1600L);
		if (fadeCoeff > 800)
			fadeCoeff = 1600 - fadeCoeff;

		levelRenderCircleIds(levelIdsRadioactive, 0);
		levelRenderCircleIds(levelIdsRadioactive,
				(levelRadioactiveMinRed + (levelRadioactiveMaxRed - levelRadioactiveMinRed) * fadeCoeff / 800 << 16) +
				(levelRadioactiveMinGreen + (levelRadioactiveMaxGreen - levelRadioactiveMinGreen) * fadeCoeff / 800 << 8) +
				levelRadioactiveMinBlue + (levelRadioactiveMaxBlue - levelRadioactiveMinBlue) * fadeCoeff / 800);

		// Render expanders.
		levelRenderCircleIds(levelIdsExpanders, 0);
		levelRenderCircleIds(levelIdsExpanders, levelColorExpander);

		// Draw hook lines.
		setFadeColor(0, 0, 0);
		for (int i = 16; i < levelNumHooks; i++)
			if (levelHookIsActive[i] && levelHookIsVisible[i])
				levelRenderLine(levelCircleX[levelHookId1[i]], levelCircleY[levelHookId1[i]], levelCircleX[levelHookId2[i]], levelCircleY[levelHookId2[i]]);

		// Render Ping.
		if (isFinalLevel)
			renderPing();

		// Render grabbers.
		levelRenderGrabbers();

		// Render bomb dispensers.
		for (int i = 0; i < levelIdsBombDispenser.length; i++)
			renderBombDispenser(levelCircleX[levelIdsBombDispenser[i]], levelCircleY[levelIdsBombDispenser[i]]);

		// Render precipitations. Depends on the current weather.
		if (levelWeatherId != 0 && activeSwapKey == 1) {
			if (levelWeatherId == 1)
				renderRain();

			if (levelWeatherId == 2)
				levelRenderPrecipitation();
		}
	}

	public static final void levelRenderCircleIds(short[] ids, int color) {
		setFadeColor(color);
		for (int i = 0; i < ids.length; i++)
			levelRenderCircle(levelCircleX[ids[i]], levelCircleY[ids[i]], levelCircleRadius[ids[i]]);
	}

	public static final void activateLevel(int levelId) {
		loadLevel(levelId);
	}

	public static final void updateAll() {
		if (levelHookIsActive[16])
			levelHookIsVisible[16] = true;

		updateSpiders();
		updateEnemies();
		updateExpanders();
		updateAnimRects();

		if (levelPlayerHitTicks > 0)
			levelPlayerHitTicks--;

		updateSpawners();
		updateLevel();

		if (isFinalLevel)
			updatePing();
	}

	public static final void processPlayerCircles(int circleId1, int circleId2) {
		if (circleId1 == 2)
			levelIsPlayerOnSurface = true;

		// Destructible objects.
		if ((levelCircleFlags[circleId2] & 0x2000) > 0) {
			levelCircleRadius[circleId2] -= 0x10000;
			if (levelCircleRadius[circleId2] < 0x40000) {
				levelCircleRadius[circleId2] = 0;
				levelCircleFlags[circleId2] = 0;
			}
		}

		// Radioactive goo.
		if ((levelCircleFlags[circleId2] & 8) > 0 &&
				!isPlayerInvincible) {

			levelPlayerHealth -= 4000;
		}

		byte circleType = levelCircleType[circleId2];

		if (circleType != 0) {
			if (currentLevelLoaded != -2 &&
					activeSwapKey != 0) {

				// Grabber.
				if (circleType == 3 &&
						levelPlayerCurrentGrabberFlags == -1 &&
						circleId2 != levelPlayerIgnoreGrabberId) {

					levelHookIsActive[16] = true;
					levelHookId2[16] = circleId2;
					levelPlayerCurrentGrabberFlags = levelCircleFlags[circleId2];
					levelCircleFlags[circleId2] = 0;
				}

				// Vacant an expander.
				if (circleType == 6)
					setExpanderVacant(circleId2);

				// Enemy hurts a player.
				if (circleType == 7 &&
						levelPlayerHitTicks == 0 &&
						isEnemyAlive(circleId2)) {

					if (isEnemyStatic(circleId2) && circleId1 == 2) {
						if (levelCircleY[2] - levelCirclePrevY[2] > 120000)
							killEnemy(circleId2);
					} else {
						playSfxHurt();

						if (!isPlayerInvincible)
							levelPlayerHealth -= 170;

						lastAttackingEnemy = circleId2;
						if (levelPlayerHealth > 0) {
							levelPlayerHitTicks = 50;
							vibrate(20);
						}
					}
				}

				if (circleType == 8) {

					// Finish the level.
					playSfxHurra();
					isLevelComplete = true;

				} else if (circleType >= 9 &&
						circleType <= 11 &&
						circleId1 == 2 &&
						levelBombStartTicks + 200 < levelTicks) {

					levelBombStartTicks = levelTicks;
					levelBombNextFlashTick = levelTicks + 33;

					levelBombFlashed = false;

					int dir = levelPlayerDirection;

					if (circleType == 10)
						dir = -1;

					if (circleType == 11)
						dir = 1;

					if (isMirrored && circleType > 9)
						dir *= -1;

					levelSetCircle(levelBombObjectId, levelCircleX[circleId2] + dir * 0x140000, levelCircleY[circleId2], 0xa0000, 15, 71, 0, true);
					levelCircleX[levelBombObjectId] += dir * 0x30000;
				}
			} else {
				levelShipTouchedCircleType = circleType;
			}
		}
	}

	public static final void processCircles(int circleId1, int circleId2) {
		if (levelPlayerHealth > 0) {

			if (circleId1 >= 5 && circleId2 >= 5) { // Level circles.

				if ((levelCircleFlags[circleId1] & 16) > 0 || (levelCircleFlags[circleId2] & 16) > 0) {

					/* Check if a ball touches portal's circle hitbox, if so,
					 * fill the portal's radius with the ball's radius. */
					int swpId1 = circleId1;
					int swpId2 = circleId2;

					if ((levelCircleFlags[circleId2] & 16) > 0) {
						swpId1 = circleId2;
						swpId2 = circleId1;
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
							playSfxHurra();
							isLevelComplete = true;
						}

						if (levelCircleRadius[swpId2] <= 0) {
							levelCircleFlags[swpId2] = 0;
							levelCircleHasPhysics[swpId2] = false;
						}
					}
				}

				// Bomb destroys destructible objects.
				if (levelBombExplodeTicks > 0 &&
						(circleId1 == levelBombObjectId &&
						 (levelCircleFlags[circleId2] & 0x100) > 0 ||
						 circleId2 == levelBombObjectId && (levelCircleFlags[circleId1] & 0x100) > 0)) {

					int circleId = circleId1;
					if (circleId1 == levelBombObjectId)
						circleId = circleId2;

					/* Reduce 30% of the first circle radius when the explosion
					 * wave (the second circle) reaches the first circle. */
					levelCircleRadius[circleId] = levelCircleRadius[circleId] * 7 / 10;

					if (levelCircleRadius[circleId] < 0xa0000) {
						levelCircleRadius[circleId] = 0;
						levelCircleFlags[circleId] = 0;
					}
				}

				byte circleType1 = levelCircleType[circleId1];
				byte circleType2 = levelCircleType[circleId2];

				if (circleType1 != 0 || circleType2 != 0) {

					// Vacant an expander.
					if (circleType1 == 6 || circleType2 == 6) {

						if (circleType1 == 6)
							setExpanderVacant(circleId1);
						else
							setExpanderVacant(circleId2);

					}

					/* Check if enemy's circle hitbox touches radioactive goo or
					 * an explosion wave. */
					if ((circleType1 == 7 || circleType2 == 7) &&
							((levelCircleFlags[circleId1] & 8) > 0 ||
							 (levelCircleFlags[circleId2] & 8) > 0 ||
							 levelBombExplodeTicks > 0 &&
							 (circleId1 == levelBombObjectId || circleId2 == levelBombObjectId))) {

						if (circleType1 == 7)
							killEnemy(circleId1);
						else
							killEnemy(circleId2);

					}

					// A circle touches a finish circle.
					if ((circleType1 == 1 || circleType2 == 1) &&
							(circleType1 == 2 || circleType2 == 2)) {

						playSfxHurra();
						isLevelComplete = true;
					}

					// A circle touches a radioactive circle.
					if ((circleType1 == 1 || circleType2 == 1) &&
							(circleType1 == 12 || circleType2 == 12)) {

						levelPlayerHealth -= 4000;
					}

					// An explosion wave reaches Ping.
					if (isFinalLevel) {

						if ((circleType1 == 13 || circleType2 == 13) &&
								(circleType1 == 14 || circleType2 == 14)) {

							if (circleType1 == 14)
								levelCircleFlags[circleId1] = 0;
							else
								levelCircleFlags[circleId2] = 0;

							levelPingSpawnEnemies();
						}

						if (levelBombExplodeTicks > 0 &&
								(circleId1 == levelBombObjectId && circleType2 == 13 ||
								 circleId2 == levelBombObjectId && circleType1 == 13)) {

							levelPingHit();
						}
					}
				}
			} else { // Player circles.

				if (circleId1 >= 5)
					processPlayerCircles(circleId2, circleId1);

				if (circleId2 >= 5)
					processPlayerCircles(circleId1, circleId2);
			}
		}
	}

	public static final void loadGeneral() {
		gSetColor(255, 255, 255);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gfxFlush(); // White screen at the start.
		loadUi();
		loadTrigonometric();
		loadRecordData();

		if (level > 0)
			skipPrologue = true;

		setNewState(STATE_ID_SELECTION, 0);
	}

	public static final void updateTransition() {
		playCurrentSound();
		loadingBarSoundCleanup = true;
		renderTransition();
	}

	public static final void loadUi() {
		setRandSeed(0);
		bfcLoadHead();
		loadRecordData();

		initFonts(4);
		loadFont(3, (short)0xb6ce, (short)0x7b1d, (short)0xc674, (byte)4, (short)0x88a8, 1, -2);
		loadFont(1, (short)0xe878, (short)0x25ab, (short)0x98c2, (byte)4, (short)0xd61e, -1, -2);
		loadFont(2, (short)0xa1c0, (short)0x6c13, (short)0xd17a, (byte)4, (short)0x9fa6, -1, -2);
		loadFont(0, (short)0x86ec, (short)0x4b3f, (short)0xf656, (byte)5, (short)0xb88a, -1, -1);

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
			clearKeys();
			gSetColor(0, 0, 0);

			// Draw a network.
			for (int y = 0; y < SCREEN_HEIGHT; y++)
				for (int x = y & 1; x < SCREEN_WIDTH; x += 2)
					gDrawLine(x, y, x, y);

			// Softkey field at the bottom.
			gFillRect(0, SCREEN_HEIGHT - 28, SCREEN_WIDTH, SCREEN_HEIGHT);
			softkeyPressed(2, -1);
		}
	}

	// Funny circle again...
	public static final void renderLoadingBarNextFrame() {
		if (loadingBarSoundCleanup) {
			queueAllSoundsForCleanup();
			queueSoundCleanup();
			loadingBarSoundCleanup = false;
		}

		gSetColor(0, 0, 0);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		if (--loadingBarColorIndex < 0)
			loadingBarColorIndex = loadingBarColors.length - 1;

		loadingBarColors[loadingBarColorIndex] = 200;

		for (int i = 0; i < 12; i++) {
			gSetColor(loadingBarColors[i], 0, 0);
			loadingBarColors[i] = loadingBarColors[i] * 80 / 100;

			gFillArc(SCREEN_WIDTH >> 2,
					(SCREEN_WIDTH >> 2) + ((SCREEN_HEIGHT - SCREEN_WIDTH) >> 1),
					SCREEN_WIDTH >> 1,
					SCREEN_WIDTH >> 1,
					i * 30,
					20);
		}

		gSetColor(0, 0, 0);
		gFillArc((SCREEN_WIDTH - (SCREEN_WIDTH / 3)) >> 1,
				(SCREEN_HEIGHT - (SCREEN_WIDTH / 3)) >> 1,
				SCREEN_WIDTH / 3,
				SCREEN_WIDTH / 3,
				0,
				360);

		gfxFlush();

		clearKeys();
	}

	public static final void levelRenderLine(int x1, int y1, int x2, int y2) {
		int alignX1 = levelAlignX(x1, y1);
		int alignY1 = levelAlignY(x1, y1);
		int alignX2 = levelAlignX(x2, y2);
		int alignY2 = levelAlignY(x2, y2);

		gDrawLine(alignX1, alignY1, alignX2, alignY2);
	}

	public static final void setFadeColor(int c) {
		setFadeColor(c >> 16 & 255, c >> 8 & 255, c & 255);
	}

	/* `fade` field represents a value from 0 to 200, where max value (200) =
	 * max fade = #000000 = black, and min value (0) = max brightness = #ffffff
	 * = white. */
	public static final void setFadeColor(int r, int g, int b) {
		isFadeBlackSet = false;
		if (r + g + b == 0) {
			isFadeBlackSet = true;
		}

		if (fade == 100) {
			gSetColor(r, g, b);
		} else {
			int gm = fade;
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

	public static final void levelDrawRect(int x, int y, int width, int height) {
		if (isFadeBlackSet) {
			int outlineWidth = 0x30000;
			// Keep the outline pixel width the same on any camera zoom value.
			outlineWidth = outlineWidth * 100 / levelCameraZoom;

			width += outlineWidth;
			height += outlineWidth;
		}

		int x1 = levelAlignX(x - width, y - height);
		int y1 = levelAlignY(x - width, y - height);
		int x2 = levelAlignX(x + width, y - height);
		int y2 = levelAlignY(x + width, y - height);
		int x3 = levelAlignX(x - width, y + height);
		int y3 = levelAlignY(x - width, y + height);
		int x4 = levelAlignX(x + width, y + height);
		int y4 = levelAlignY(x + width, y + height);

		if (x1 > SCREEN_WIDTH && x3 > SCREEN_WIDTH)
			return;

		if (y1 > SCREEN_HEIGHT && y2 > SCREEN_HEIGHT)
			return;

		if (x4 < 0 && x2 < 0)
			return;

		if (y4 < 0 && y3 < 0)
			return;

		gFillTriangle(x3, y3, x1, y1, x2, y2);
		gFillTriangle(x2, y2, x4, y4, x3, y3);
	}

	public static final void levelDrawImage(Image img, int x, int y) {
		int alignX = levelAlignX(x, y);
		int alignY = levelAlignY(x, y);

		gDrawImage(img, alignX - img.getWidth() / 2, alignY - img.getHeight() / 2, 0);
	}

	public static final void sceneSelectionRenderCircle(int x, int y, int radius, int color) {
		setFadeColor(0);
		levelRenderCircle(x, y, radius);

		setFadeColor(color);
		levelRenderCircle(x, y, radius);
	}

	public static final void levelRenderCircle(int x, int y, int radius) {
		if (radius >= 35000) {
			int alignX = levelAlignX(x, y);
			int alignY = levelAlignY(x, y);

			radius >>= 16;
			radius = levelCameraZoom * radius / 100;

			if (isFadeBlackSet)
				radius += 3;

			if (alignX > -radius &&
					alignX < SCREEN_WIDTH + radius &&
					alignY > -radius &&
					alignY < SCREEN_HEIGHT + radius) {

				gFillArc(alignX - radius,
						alignY - radius,
						radius + radius,
						radius + radius,
						0,
						360);
			}
		}
	}

	public static final void levelRenderCritterShock(int x1, int y1, int x2, int y2) {
		int alignX1 = levelAlignX(x1, y1);
		int alignY1 = levelAlignY(x1, y1);
		int alignX2 = levelAlignX(x2, y2);
		int alignY2 = levelAlignY(x2, y2);

		renderCritterShock(alignX1, alignY1, alignX2, alignY2, 8, 6);
	}

	public static final void levelRenderCircleOutline(int x, int y, int radius) {
		int alignX = levelAlignX(x, y);
		int alignY = levelAlignY(x, y);

		radius >>= 16;
		radius = levelCameraZoom * radius / 100;

		if (isFadeBlackSet)
			radius += 3;

		if (alignX > -radius &&
				alignX < SCREEN_WIDTH + radius &&
				alignY > -radius &&
				alignY < SCREEN_HEIGHT + radius) {

			gDrawArc(alignX - radius,
					alignY - radius,
					radius + radius,
					radius + radius,
					0,
					360);
		}
	}

	// Check if any part of the rectangle extends beyond the screen bounds.
	public static final boolean isOutOfScreenBounds(int x1, int y1, int x2, int y2) {
		return x1 > SCREEN_WIDTH || x2 < 0 || y1 > SCREEN_HEIGHT || y2 < 0;
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

			for (int i = 0; i < 3; i++)
				imgsInside[i] = loadImage("inside" + i + ".pim", "inside" + i + ".ppl");
		}

		if (imgInsideLamp == null) {
			imgInsideLamp = loadImage("inside_lamp.pim", "inside_lamp.ppl");
		}

		if (imgsPiston == null) {
			imgsPiston = new Image[2];
			imgsPiston[0] = loadImage("piston_top.pim", "piston_top.ppl");
			imgsPiston[1] = loadImage("piston_bottom.pim", "piston_bottom.ppl");
		}

		if (imgsShipDecor == null) {
			imgsShipDecor = new Image[10];

			for (int i = 0; i < imgsShipDecor.length; i++)
				imgsShipDecor[i] = loadImage("shipDecor" + i + ".pim", "shipDecor" + i + ".ppl");

			shipDecorBackground = loadFile16("decor_background.bin");
			shipDecorForeground = loadFile16("decor_foreground.bin");
		}

		if (imgsShipIcon == null) {
			imgsShipIcon = new Image[9];

			for (int i = 0; i < 9; i++)
				imgsShipIcon[i] = loadImage("shipicon" + i + ".pim", "shipicon" + i + ".ppl");
		}

		if (imgsShipIconB == null) {
			imgsShipIconB = new Image[9];

			for (int i = 0; i < 9; i++)
				imgsShipIconB[i] = loadImage("shipicon" + i + "b.pim", "shipicon" + i + "b.ppl");
		}
	}

	public static final void initShip() {
		initSpace(2, 12, 128, 64);
		fade = 100;
		loadLevel(-2);
		initInsideShip();
		levelMovableGravity = 40000;
		levelSetCamera(levelCircleX[0], levelHeight / 2, 0);
	}

	/* This function processes a script (which exactly depends on `choice`) and
	 * returns corresponding ship operation value that will be executed next. */
	public static final int processShipScript(int choice, boolean isInPlanet) {
		byte shipOp = 0;

		switch (choice) {
			case 1:
				if (!isInPlanet) {
					playSfxGo();
					startScript("start" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					shipOp = SHIP_OP_START;
				} else {
					shipOp = SHIP_OP_RESUME;
				}
				break;

			case 2:
				if (isInPlanet) {
					startScript("menu_sure_restart.bms", levelCircleX[0], levelCircleY[0], true);
					shipOp = SHIP_OP_RESTART;
				} else {
					startScript("menu_no_restart.bms", levelCircleX[0], levelCircleY[0]);
				}
				break;

			case 3:
				if (isInPlanet) {
					startScript("menu_no_holodeck.bms", levelCircleX[0], levelCircleY[0]);
				} else {
					startScript("menu_enter_holodeck.bms", levelCircleX[0], levelCircleY[0]);
					shipOp = SHIP_OP_HOLODECK;
				}
				break;

			case 4:
				setNewState(STATE_ID_HELP, 0);
				break;

			case 5:
				if (isAudioEnabled) {
					startScript("menu_audio_off.bms", levelCircleX[0], levelCircleY[0]);
					queueAllSoundsForCleanup();
					queueSoundCleanup();
				} else {
					startScript("menu_audio_on.bms", levelCircleX[0], levelCircleY[0]);
				}

				isAudioEnabled = !isAudioEnabled;
				break;

			case 6:
				if (isVibraEnabled) {
					startScript("menu_vibra_off.bms", levelCircleX[0], levelCircleY[0]);
				} else {
					startScript("menu_vibra_on.bms", levelCircleX[0], levelCircleY[0]);
				}

				isVibraEnabled = !isVibraEnabled;
				saveRecordData();
				vibrate(40);

			case 7:
				// Reserved.
				break;

			case 8:
				startScript("menu_sure_exit.bms", levelCircleX[0], levelCircleY[0], true);
				shipOp = SHIP_OP_EXIT;
				break;

			case 9:
				startScript("menu_sure_reset.bms", levelCircleX[0], levelCircleY[0], true);
				shipOp = SHIP_OP_RESET;
		}

		return shipOp;
	}

	// Render everything in the ship.
	public static final void renderShip(boolean isInPlanet) {
		isTextRendered = true;
		renderShipInside();
		isTextRendered = false;

		if (!isInPlanet)
			renderPlayable(6, 0xc80000, true);

		levelRenderPlayer();
		renderShipDecor(shipDecorForeground);
		renderShipArrows();
	}

	public static final void renderShipArrows() {
		// Don't render arrows if the player is almost close to the ship bounds.
		if (levelCircleX[0] > 0x6e0000)
			gDrawImage(imgArrowLeft, 4, (SCREEN_HEIGHT >> 1) + 27, 0);

		if (levelCircleX[0] < 0x41a0000)
			gDrawImage(imgArrowRight, SCREEN_WIDTH - 4 - 17, (SCREEN_HEIGHT >> 1) + 27, 0);
	}

	public static final void renderShipDecor(short[] decorData) {
		if (decorData != null) {
			/* Starting point from where everything in the ship will be rendered
			 * left-to-right. */
			int shiftX = (SCREEN_WIDTH >> 1) - levelCameraZoom * levelCameraIntX / 100;

			for (int i = 0; i < decorData.length; i += 3) {

				Image img = imgsShipDecor[decorData[i]];
				if (img != null) {
					int x = shiftX + levelAlignXToMirror(decorData[i + 1]) - img.getWidth() / 2;
					int width = img.getWidth();
					if (x > -width && x < SCREEN_WIDTH + width)
						gDrawImage(img, x, ((SCREEN_HEIGHT - 224) >> 1) + decorData[i + 2] - img.getHeight() / 2, 0);
				}

			}
		}
	}

	public static final void renderShipInside() {
		gSetColor(0);
		gFillRect(0, 0, SCREEN_WIDTH, (SCREEN_HEIGHT - 224) >> 1);
		gFillRect(0, SCREEN_HEIGHT - ((SCREEN_HEIGHT - 224) >> 1), SCREEN_WIDTH, (SCREEN_HEIGHT - 224) >> 1);

		/* Starting point from where everything in the ship will be rendered
		 * left-to-right. */
		int shiftX = (SCREEN_WIDTH >> 1) - levelCameraIntX * levelCameraZoom / 100;

		int wallX = shiftX;
		if (wallX > 0)
			wallX = 0;

		// Render background of the ship.
		gDrawImage(imgsInside[0], wallX, (SCREEN_HEIGHT - 224) >> 1, 0);
		wallX += 40;

		for (int i = 0; i < 17; i++) {
			if (wallX + 64 > 0 && wallX <= SCREEN_WIDTH)
				gDrawImage(imgsInside[1], wallX, (SCREEN_HEIGHT - 224) >> 1, 0);

			wallX += 64;
		}

		if (wallX <= SCREEN_WIDTH)
			gDrawImage(imgsInside[2], wallX, (SCREEN_HEIGHT - 224) >> 1, 0);

		// Render a lamp.
		int lampX = shiftX + levelAlignXToMirror(193) * LEVEL_CAMERA_ZOOM_DEFAULT / 100;
		gDrawImage(imgInsideLamp, lampX - imgInsideLamp.getWidth() / 2, (SCREEN_HEIGHT >> 1) - 110, 0);

		// Render window.
		renderShipWindow(shiftX + 410, ((SCREEN_HEIGHT - 64) >> 1) - 16, 0);
		renderShipWindow(shiftX + 758, ((SCREEN_HEIGHT - 64) >> 1) - 16, 1);
		renderShipDecor(shipDecorBackground);

		// Render the lamp beacon animation.
		if (shipAlarmRadius > 0) {
			int lampY = (SCREEN_HEIGHT >> 1) - 110 + imgInsideLamp.getHeight() / 2;

			gSetColor(0xff0000);
			int rad = shipAlarmRadius;

			if (lampX > -rad &&
					lampX < SCREEN_WIDTH + rad &&
					lampY > -rad &&
					lampY < SCREEN_HEIGHT + rad) {

				for (int i = 0; i < 2; i++) {
					for (int j = 20; j < 360; j += 90) {
						gDrawArc(lampX - rad,
								lampY - rad,
								rad + rad,
								rad + rad,
								j,
								50);
					}

					rad--;
				}
			}

			shipAlarmRadius += 2;
			if (shipAlarmRadius > 30)
				shipAlarmRadius = 5;
		}

		// Render pods.
		for (int i = 0; i < levelNumCircles; i++) {
			if ((levelCircleFlags[i] & 4) > 0 &&
					levelCircleHasPhysics[i] &&
					levelCircleType[i] > 0) {

				int podX = LEVEL_CAMERA_ZOOM_DEFAULT * (levelCircleX[i] / 100) >> 16;
				int podY = LEVEL_CAMERA_ZOOM_DEFAULT * (levelCircleY[i] / 100) >> 16;

				// Render pistons.
				Image img;
				gDrawImage(img = imgsPiston[1], shiftX + podX - img.getWidth() / 2, ((SCREEN_HEIGHT - 224) >> 1) + podY - img.getHeight(), 0);
				gDrawImage(img = imgsPiston[0], shiftX + podX - img.getWidth() / 2, (SCREEN_HEIGHT - 224) >> 1, 0);

				int podId = levelCircleType[i] - 1;
				img = imgsShipIcon[podId];

				// Switch to the "off" variant of the icons if needed.
				switch (podId) {
					case 4:
						if (!isAudioEnabled)
							img = imgsShipIconB[podId];
						break;

					case 5:
						if (!isVibraEnabled)
							img = imgsShipIconB[podId];
				}

				// Draw pod icons.
				gDrawImage(img, shiftX + podX - img.getWidth() / 2, ((SCREEN_HEIGHT - 224) >> 1) + podY - img.getHeight() / 2, 0);

				// Render pod labels.
				if (isTextRendered &&
						shiftX + podX > -100 &&
						shiftX + podX < 340) {

					String label = textTableShip[podId];
					if (isShipPaused)
						label = textTableShipPause[podId];

					renderText(shiftX + podX - getStringWidth(label, 3) / 2, (SCREEN_HEIGHT >> 1) + 28, label, 3);
				}
			}
		}
	}

	// Render the window in the ship.
	public static final void renderShipWindow(int x, int y, int spaceId) {
		int windowWidth = imgWindow.getWidth();
		int windowHeight = imgWindow.getHeight();

		x -= windowWidth / 2;

		gSetColor(0);
		gSetClip(x, y, windowWidth, windowHeight);
		gFillRect(x, y, windowWidth, windowHeight);

		gSetColor(0xffffff);
		renderSpace(spaceId, x - windowWidth + windowWidth * x / SCREEN_WIDTH, y);

		if (x >= -windowWidth && x <= SCREEN_WIDTH)
			gDrawImage(imgWindow, x, y, 0);

		gSetClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	public static final void renderOutside() {
		if (imgSplashShip == null) {
			imgSplashShip = loadImage("outside.pim", "outside.ppl");
		}

		if (imgsFlame == null) {
			imgsFlame = new Image[2];
			imgsFlame[0] = loadImage("flame0.pim", "flame0.ppl");
			imgsFlame[1] = loadImage("flame1.pim", "flame1.ppl");
		}

		gSetColor(0);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		renderSpace(0, 0, 0, true);

		int shipX = (SCREEN_WIDTH - 85) >> 1;
		int shipY = (SCREEN_HEIGHT - 41) >> 1;
		gDrawImage(imgSplashShip, shipX, shipY, 0);
		gDrawImage(imgsFlame[(int)(millis() / 100L % 2L)], shipX - 7, shipY + 18, 0);
	}

	public static final void initSpaceMap(int planetX, int planetY, int planetId) {
		spaceMapCameraX = 0;
		spaceMapCameraY = 0;
		spaceMapTargetCameraX = 0;
		spaceMapTargetCameraY = 0;
		spaceMapShipX = 0;
		spaceMapShipY = 0;
		spaceMapShowShip = true;
		spaceMapPlanetX = planetX;
		spaceMapPlanetY = planetY;
		spaceMapPlanetBeaconRadius = 1;

		if (planetId >= 0) {
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

	// Immediatelly set a camera position to an emergency beacon.
	public static final void spaceMapSetCameraToBeacon() {
		spaceMapSetCameraInstant(spaceMapPlanetX, spaceMapPlanetY);
	}

	// Immediatelly set a camera position.
	public static final void spaceMapSetCameraInstant(int x, int y) {
		spaceMapCameraX = x << 8;
		spaceMapCameraY = y << 8;
		spaceMapTargetCameraX = spaceMapCameraX;
		spaceMapTargetCameraY = spaceMapCameraY;
	}

	// Smoothly set a camera position to an emergency beacon.
	public static final void spaceMapSetTargetCameraToBeacon() {
		spaceMapSetTargetCamera(spaceMapPlanetX, spaceMapPlanetY);
	}

	// Smoothly set a camera position.
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
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		int camX = -(spaceMapCameraX >> 8);
		int camY = -(spaceMapCameraY >> 8);
		renderSpace(0, camX - (SCREEN_WIDTH >> 1), camY - (SCREEN_HEIGHT >> 1), false);

		// Render a ship (star).
		int posX = spaceMapShipX + camX + (SCREEN_WIDTH >> 1);
		int posY = spaceMapShipY + camY + (SCREEN_HEIGHT >> 1);
		gSetColor(0xffffff);
		renderStar(posX, posY);

		// Render a ship pointer.
		if (spaceMapShowShip) {
			gDrawImage(imgPointer, posX - 23, posY - 81 - 2, 0);
			gDrawImage(imgShip, posX - imgShip.getWidth() / 2, posY - 81 + (47 - imgShip.getHeight()) / 2 - 2, 0);
		}

		// Render a planet (star).
		posX = spaceMapPlanetX + camX + (SCREEN_WIDTH >> 1);
		posY = spaceMapPlanetY + camY + (SCREEN_HEIGHT >> 1);
		renderStar(posX, posY);

		// Render an emergency beacon.
		if (spaceMapPlanetBeaconRadius > 0) {
			gSetColor(0xff0000);
			int rad = spaceMapPlanetBeaconRadius;

			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 20, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 110, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 200, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 290, 50);

			if (spaceMapPlanetBeaconRadius++ > 20)
				spaceMapPlanetBeaconRadius = 1;
		}

		// Render a planet pointer.
		if (spaceMapShowPlanet) {
			gDrawImage(imgPointer, posX - 23, posY - 81 - 2, 0);
			gDrawImage(imgPlanet, posX - imgPlanet.getWidth() / 2, posY - 81 + (47 - imgPlanet.getHeight()) / 2 - 2, 0);
		}
	}

	public static final void renderBeamAnimation(int tick) {
		updateAll();
		renderShipInside();

		if (tick < 100) {
			fade = tick;
			levelCameraZoom = tick * LEVEL_CAMERA_ZOOM_DEFAULT / 100;

			if (levelCameraZoom == 0)
				levelCameraZoom = 1;

			renderPlayable(6, 0xc80000, true);
			fade = 100;
			levelCameraZoom = LEVEL_CAMERA_ZOOM_DEFAULT;
		} else {
			renderPlayable(6, 0xc80000, true);
		}

		levelRenderPlayer();
		renderShipDecor(shipDecorForeground);
		refreshGame();
	}

	public static final void initVictory() {
		victoryCameraX = levelCircleX[0];
		victoryCameraY = levelCircleY[0];

		for (int i = 0; i < 5; i++)
			levelCirclePrevY[i] = levelCircleY[i];

		if (levelPlayerCurrentGrabberFlags >= 0)
			levelPlayerReleaseGrabber();

		playJingleComplete();
		victoryCameraZoom = levelCameraZoom;
		victoryTicks = 150;
		initConfetti();

		if (textMission == null) {
			textMission = getText(0x50000);
			textCompleted = getText(0x50001);
		}
	}

	public static final boolean renderVictory() {
		levelCameraApproach(victoryCameraX, victoryCameraY, 0);
		fade = 100;
		levelCameraZoom = victoryCameraZoom;
		levelRender(false);

		if (victoryTicks < 100) {
			// Slowly zoom the camera.
			if (victoryTicks % 4 == 0)
				victoryCameraZoom++;

			fade = victoryTicks;
		}

		if (victoryTicks > 0) {
			if (victoryTicks < 50) {
				// Animate the beam.
				levelCameraZoom = victoryTicks * LEVEL_CAMERA_ZOOM_DEFAULT * 2 / 100;

				for (int i = 0; i < 5; i++) {
					int speed = (100 - victoryTicks * 2) *
						(100 - victoryTicks * 2) *
						(100 - victoryTicks * 2) *
						(100 - victoryTicks * 2);

					levelCircleY[i] -= speed >> 2;
					levelCircleX[i] += speed * cos((50 - victoryTicks) * 45) / 2000;
				}

				victoryTicks++;
			}

			levelRenderPlayer();
		}

		renderConfetti();

		int angle = 90;
		if (victoryTicks > 105)
			angle = (150 - victoryTicks) * 2;

		// Render the "mission completed" text.
		renderText((SCREEN_WIDTH - getStringWidth(textMission, 0)) / 2 - SCREEN_WIDTH * cos1000[angle] / 1000, SCREEN_HEIGHT >> 2, textMission, 0);
		renderText((SCREEN_WIDTH - getStringWidth(textCompleted, 0)) / 2 + SCREEN_WIDTH * cos1000[angle] / 1000, (SCREEN_HEIGHT >> 2) + 14, textCompleted, 0);
		refreshGame();

		victoryTicks -= 2;
		if (softkeyPressed(2, -1) == 2)
			victoryTicks = -38;

		// Return to the ship.
		if (victoryTicks < -40) {
			setNewState(STATE_ID_TRANSITION, 0);
			isBeamAnimated = true;
			return true;
		} else {
			return false;
		}
	}

	public static final int levelAlignX(int x, int y, int angle) {
		x /= 100000 / levelCameraZoom;
		y /= 100000 / levelCameraZoom;
		return cos1000[angle] * x - sin1000[angle] * y;
	}

	public static final int levelAlignY(int x, int y, int angle) {
		x /= 100000 / levelCameraZoom;
		y /= 100000 / levelCameraZoom;
		return sin1000[angle] * x + cos1000[angle] * y;
	}

	public static final int sin(int angle) {
		while (angle < 0)
			angle += 360;

		while (angle >= 360)
			angle -= 360;

		return sin1000[angle];
	}

	public static final int cos(int angle) {
		while (angle < 0)
			angle += 360;

		while (angle >= 360)
			angle -= 360;

		return cos1000[angle];
	}

	/* A fixed-point precalculated trigonometric arrays in the range of
	 * -1000...1000. */
	public static final void loadTrigonometric() {
		if (sin1000 == null)
			sin1000 = loadFile16("sin1000.bin");

		if (cos1000 == null)
			cos1000 = loadFile16("cos1000.bin");
	}

	public static final void loadStatic() {
		if (imgsStatic == null) {
			imgsStatic = new Image[4];

			for (int i = 0; i < 4; i++)
				imgsStatic[i] = loadImage("static" + i + ".pim", "static" + i + ".ppl");
		}

		if (staticTiles == null)
			staticTiles = new int[((SCREEN_WIDTH + 30) / 23 + 1) * (SCREEN_HEIGHT / 23 + 1)];
	}

	public static final void renderTransmission() {
		// Do nothing if already loaded.
		loadStatic();

		// Render background.
		gSetColor(transmissionBackgroundColor);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Render an entity.
		if (isTransmissionShaky) {
			renderPurple(transmissionEntityX + sin1000[(int)(millis() / 5L % 360L)] * (SCREEN_WIDTH / 10) / 1000,
					transmissionEntityY + sin1000[(int)(millis() / 3L % 360L)] * (SCREEN_HEIGHT / 10) / 1000,
					transmissionEntitySize + 40 + sin1000[(int)(millis() / 14L % 360L)] * 20 / 1000);
		} else {
			renderPurple(transmissionEntityX,
					transmissionEntityY,
					transmissionEntitySize);
		}

		// Render a transmission interference.
		if (isTransmissionNoisy) {
			int index = 0;

			for (int y = 0; y <= SCREEN_HEIGHT; y += 32) {
				for (int x = -rand8() % 30; x <= SCREEN_WIDTH; x += 32) {
					int tileId;

					do {
						tileId = rand8() % 4;
					} while(tileId == staticTiles[index]);

					staticTiles[index] = tileId;
					gDrawImage(imgsStatic[tileId], x, y, 0);

					index++;
				}
			}
		}

		// Show a photo frame.
		if (isPhotoFrameShown) {
			gSetColor(0xffffff);
			gFillRect(0, 0, SCREEN_WIDTH, 10);
			gFillRect(0, 0, 10, SCREEN_HEIGHT);
			gFillRect(SCREEN_WIDTH - 10, 0, 10, SCREEN_HEIGHT);
			gFillRect(0, SCREEN_HEIGHT - 30, SCREEN_WIDTH, 30);

			gSetColor(0);
			gDrawRect(10, 10, SCREEN_WIDTH - 21, SCREEN_HEIGHT - 41);

			if (activePhotoFrameLabelId >= 0)
				renderText(-1000, SCREEN_HEIGHT - 22, photoFrameLabels[activePhotoFrameLabelId], 1);
		}
	}

	public static final void renderPurple(int x, int y, int size) {
		int purpleWidth = size * 102 / 100;
		int purpleHeight = size * 233 / 100;

		// Render purple's outline.
		gSetColor(0);
		gFillArc(x - purpleWidth / 2 - 2,
				y - purpleHeight / 2 - 2,
				purpleWidth + 2 + 2,
				purpleHeight + 2 + 2,
				0,
				360);

		// Render purple's body.
		if (isTransmissionNoisy)
			gSetColor(0x696969);
		else
			gSetColor(0xa020f0);

		gFillArc(x - purpleWidth / 2,
				y - purpleHeight / 2,
				purpleWidth,
				purpleHeight,
				0,
				360);

		// Render mouth.
		gSetColor(0);

		int mouthWidth = size * 57 / 100;
		int mouthHeight = size * 12 / 100;

		if (isSpeakingAnimationPlaying && !isPhotoFrameShown && (millis() / 150L & 1L) > 0L)
			gFillArc(x - mouthWidth / 2,
					y - mouthHeight * 2,
					mouthWidth,
					mouthHeight * 2,
					0,
					360);
		else
			gFillArc(x - mouthWidth / 2,
					y - mouthHeight,
					mouthWidth,
					mouthHeight,
					0,
					360);

		// Render eyes.
		gSetColor(0xffffff);

		int sizeEyes = size * 35 / 100;
		int sizePupils = size * 16 / 100;

		for (int i = 0; i < 3; i++)
			gFillArc(x - sizeEyes * 3 / 2 + i * sizeEyes,
					y - purpleHeight * 45 / 100, sizeEyes,
					sizeEyes,
					0,
					360);

		// Render pupils.
		gSetColor(0);

		for (int i = 0; i < 3; i++)
			gFillArc(x - sizeEyes * 3 / 2 + i * sizeEyes + sizeEyes / 3,
					y - purpleHeight * 45 / 100 + sizeEyes / 3,
					sizePupils,
					sizePupils,
					0,
					360);

		// Render eyelids.
		if (isTransmissionBlinked || !isPhotoFrameShown && rand8() > 240) {
			if (isTransmissionNoisy)
				gSetColor(0x696969);
			else
				gSetColor(0xa020f0);

			for (int i = 0; i < 3; i++)
				gFillArc(x - sizeEyes * 3 / 2 + i * sizeEyes,
						y - purpleHeight * 45 / 100,
						sizeEyes,
						sizeEyes,
						0,
						360);

			isTransmissionBlinked = !isTransmissionBlinked;
		}
	}

	public static final void sceneSelectionCleanup() {
		imgsSelectionMenu = null;
		imgsSelectionMenuArrows = null;
	}

	public static final void loadSelectionOptions(String[] filenames, int defaultIndex) {
		numSelections = filenames.length;
		selectionAngleBetweenOthers = 360 / numSelections;
		selectionAngleUntilAdjust = selectionAngleBetweenOthers / 2;

		imgsSelectionMenu = new Image[numSelections];
		for (int i = 0; i < numSelections; i++) {
			imgsSelectionMenu[i] = loadImage(filenames[(i + defaultIndex) % numSelections] + ".pim", filenames[(i + defaultIndex) % numSelections] + ".ppl");
		}

		imgsSelectionMenuArrows = new Image[2];
		imgsSelectionMenuArrows[0] = loadImage("arrow_left.pim", "arrow_left.ppl");
		imgsSelectionMenuArrows[1] = loadImage("arrow_right.pim", "arrow_right.ppl");

		levelInitCircles(10 + numSelections * 2 + 1);
		levelInitHooks(numSelections);

		activeSwapKey = -10;
		levelMovableGravity = 30000;
		levelMovableAccel = 64000;
		selectionAngle = 0;
		selectionMainCircleId = 10 + numSelections * 2;
		selectionDefaultIndex = defaultIndex;

		// Set initial circle positions.
		for (int i = 0; i < numSelections; i++) {
			int x, y;

			// Invisible pendant of the circle.
			x = 5242 * cos(90 + i * selectionAngleBetweenOthers);
			y = 5242 * sin(90 + i * selectionAngleBetweenOthers);
			levelSetCircle(i + 10, x, y, 0x50000, 200, 0, 0, false);

			// Actual circle with image.
			x = 6881 * cos(90 + i * selectionAngleBetweenOthers);
			y = 6881 * sin(90 + i * selectionAngleBetweenOthers);
			levelSetCircle(i + 10 + numSelections, x, y, 0x190000, 100, 3, 0, true);
		}

		levelSetCircle(selectionMainCircleId, 0, 0, 0x500000, 200, 2, 0, false);

		// Set hooks.
		for (int i = 0; i < numSelections; i++) {
			levelSetHook(i, i + 10, i + 10 + numSelections, 4, 0x3c0000, 65000, true, true);
		}

		// Initial camera position.
		levelSetCamera(levelCircleX[selectionMainCircleId], levelCircleY[selectionMainCircleId] + 0x640000, 0);

		selectionState = 0;
		selectionStateStartMs = millis();
	}

	// This function renders selection menu and returns index of the user selected entry.
	public static final int getSelectedIndex() {
		// Check if OK softkey is pressed.
		if (selectionState == 1 && softkeyPressed(2, -1, true) == 2) {
			selectionState++;
			selectionStateStartMs = millis() + 500L;
		}

		if (isKeyHeld(KEY_LEFT)) {
			selectionAngle += 4;
		} else if (isKeyHeld(KEY_RIGHT)) {
			selectionAngle -= 4;
		} else {
			// Adjust angle.
			int remAngle = selectionAngle % selectionAngleBetweenOthers;
			if (remAngle > selectionAngleUntilAdjust)
				selectionAngle += 2;
			else if (remAngle > 0)
				selectionAngle -= 2;
		}

		if (selectionAngle >= 360)
			selectionAngle -= 360;

		if (selectionAngle < 0)
			selectionAngle += 360;

		// Set circle pendant positions.
		for (int i = 0; i < numSelections; i++) {
			int x = 5242 * cos(selectionAngle + 90 + i * selectionAngleBetweenOthers);
			int y = 5242 * sin(selectionAngle + 90 + i * selectionAngleBetweenOthers);
			levelCircleX[i + 10] = x;
			levelCircleY[i + 10] = y;
		}

		updateLevel();

		// Fade animation and selection state are depending on the delta time.
		if (selectionState == 0)
			fade = (int)((millis() - selectionStateStartMs) / 5L);

		if (selectionState == 2)
			fade = (int)((selectionStateStartMs - millis()) / 20L);

		if (fade < 0) {
			fade = 0;
			selectionState++;
		}

		if (fade > 100) {
			fade = 100;
			selectionState++;
		}

		// Render background.
		setFadeColor(0xdddddd);
		gFillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		// Render a donut (main circle).
		sceneSelectionRenderCircle(levelCircleX[selectionMainCircleId], levelCircleY[selectionMainCircleId], levelCircleRadius[selectionMainCircleId], 0xaaaaaa);
		sceneSelectionRenderCircle(levelCircleX[selectionMainCircleId], levelCircleY[selectionMainCircleId], levelCircleRadius[selectionMainCircleId] / 2, 0xdddddd);
		setFadeColor(0);

		// Render hooks.
		for (int i = 10; i < 10 + numSelections; i++) {
			levelRenderLine(levelCircleX[i], levelCircleY[i], levelCircleX[i + numSelections], levelCircleY[i + numSelections]);
		}

		// Render selections.
		for (int i = 0; i < numSelections; i++) {
			levelRenderCircle(levelCircleX[i + 10 + numSelections], levelCircleY[i + 10 + numSelections], levelCircleRadius[i + 10 + numSelections]);
			if (fade > 20)
				levelDrawImage(imgsSelectionMenu[i], levelCircleX[i + 10 + numSelections], levelCircleY[i + 10 + numSelections]);
		}

		// Render donut's screws.
		setFadeColor(0x888888);
		for (int i = 0; i < 5; i++) {
			int x = 3932 * cos(selectionAngle + 90 - 36 + i * 72);
			int y = 3932 * sin(selectionAngle + 90 - 36 + i * 72);
			levelRenderCircle(x, y, 0x50000);
		}

		// Render arrows.
		if (fade > 50) {
			selectionArrowsAnimTicks += 12;
			if (selectionArrowsAnimTicks > 179)
				selectionArrowsAnimTicks -= 180;

			int arrowX = 1 + sin1000[selectionArrowsAnimTicks] * 6 / 1000;
			gDrawImage(imgsSelectionMenuArrows[0], arrowX, (SCREEN_HEIGHT * 2 / 3) - imgsSelectionMenuArrows[0].getHeight() / 2, 0);
			gDrawImage(imgsSelectionMenuArrows[1], SCREEN_WIDTH - arrowX - imgsSelectionMenuArrows[1].getWidth(), (SCREEN_HEIGHT * 2 / 3) - imgsSelectionMenuArrows[1].getHeight() / 2, 0);
		}

		refreshGame();

		// Check if selection is submitted.
		if (selectionState == 3) {
			int selectionIndex = (360 - selectionAngle) / selectionAngleBetweenOthers + selectionDefaultIndex;

			if (selectionAngle % selectionAngleBetweenOthers > selectionAngleUntilAdjust)
				selectionIndex++;

			if (selectionIndex >= numSelections)
				selectionIndex -= numSelections;

			if (selectionIndex < 0)
				selectionIndex += numSelections;

			return selectionIndex;
		} else {
			return -1;
		}
	}
}
