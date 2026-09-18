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
	// keys
	public static int heldKeys = 0;
	public static int heldPrevKeys = 0;
	public static int _heldKeys = 0;
	public static int Field5 = 0;
	public static final short[][] KEYMAP = new short[][] {{'5', -6, -5},  {'0'},  {-7},  {'4', -3},  {'6', -4},  {'2', -1},  {'8', -2},  {'1'},  {'3'},  {'7'},  {'9'}};
	// image decoding
	private static byte[] pngTemplate; // for getting png from pim/ppl
	private static int pplOptions;
	private static int pplColorCount;
	private static int pplCRC;
	private static byte[] pplData;
	public static boolean paused;
	public static long prevTime;
	public static long currTime;
	public static String recordStoreName = "PMDATA";
	public static boolean audio = true;
	private static int numActiveSoundsMIDI;
	private static int numActiveSounds;
	private static int numSounds;
	private static int Field20;
	private static long Field21;
	private static int[] soundLoopCounts;
	private static boolean[] soundActiveStates;
	private static boolean[] soundQueueCleanup;
	private static boolean[] soundUsedStates;
	private static int[] Field26;
	private static int[] soundTypes;
	private static String[] soundFilenames;
	private static int numActiveSoundsTotal;
	private static Player[] soundPlayers;
	private static Player[] soundPlayersMIDI;
	public static DataInputStream currentData;
	public static int currentSize;
	public static boolean throbberToggle = false;
	public static int currentOffset = -1;
	public static int currentLocation = -1;
	public static int currentIndex = -1;
	public static boolean currentReserved;
	public static byte[][] bfcReservedData; // reserve data to RAM for future use (prevent reloading from bfc)
	public static int currentOffsetReserved = 0;
	public static short[] bfcHeadCrcs; // filenames' checksums
	public static byte[] bfcHeadMemStates; // preserve or not
	public static int[] bfcHeadOffsets; // offset from start of N.bfc
	public static byte[] bfcHeadLocations; // N.bfc
	public static int[] bfcHeadSizes; // size of file contents
	public static int bfcHeadNumEntries;
	private static int bfcCrcPoly = 0x1021; // used for filename crc table generating
	private static int[] bfcCrcTable = new int[256]; // filename crc table (we'll turn strings into 16-bit value)
	public static String[] Field49;
	public static byte[] Field50;
	public static int Field51 = -1;
	public static short Field52;
	// softkeys
	private static int leftSoftkey; // current pressed left softkey
	private static int rightSoftkey; // current pressed right softkey
	public static int[] softkeyWidth;
	public static int[] softkeyHeight;
	public static Image[] imgsSoftkey;
	// fonts
	public static Image[] fontImages; // sprite
	public static int[] fontAverageWidths;
	public static int[] fontSpacesPerChars;
	public static int[] fontHeights;
	public static int[] fontLineGaps;
	public static short[][] fontChrOffsets;
	public static byte[][] fontChrWidths;
	public static short[][] fontIndexes;
	public static Graphics gfx;
	private static long randSeed;
	public static boolean isNewState = true;
	public static int oldScreenIndex;
	public static int curScreenIndex = -1;
	public static int newStateIndex;
	public static int stateArg;
	private static Display dpy;
	public static boolean vibration = true;
	public static int[] confettiColors;
	public static int[] confettiHeights;
	public static int[] confettiAngles;
	public static int[] confettiX;
	public static int[] confettiY;
	public static int[] confettiWidths;
	public static int[] confettiColorsAll = new int[] {0xffffff, 0xff0000, 0xff00, 0xff, 0xffff00, 0xd9d919, 0xe6e8fa, 0x8c7853};
	public static int[] rainX;
	public static int[] rainY;
	public static int[] Field84;
	public static int[] Field85;
	public static int[] Field86;
	public static int[][] spaceStarX;
	public static int[][] spaceStarY;
	public static int[][] spaceStarXVel;
	public static int[][] Field90;
	public static int currentSpaceWidth;
	public static int currentSpaceHeight;
	public static String[] sceneHelpText;
	public static int sceneHelpScrollDistance;
	public static int sceneHelpScrollAccel;
	public static boolean sceneHelpAutoscrollActive;
	public static Image[] imgSceneHelpScrollbar;
	public static int sceneHelpScroll;
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
	public static Image imgCenterLogo;
	public static Image imgEidosLegalLine;
	public static Image imgPmRocket;
	public static int ttlScrLogoX;
	public static int ttlScrLogoY;
	public static int ttlScrTick;
	public static long ttlScrDuration;
	public static int ttlScr = 0;
	public static boolean showControlsGuide;
	public static int Field119 = 0;
	public static int Field120 = 0;
	public static int shipNextOp = 0;
	public static boolean dialogueIsAwaitingLevelStart = false;
	public static int Field123 = 1;
	public static boolean skipPrologue;
	public static int[] currentDialogue;
	public static int currentDialogueIndex;
	public static boolean Field127;
	public static boolean mustResetData;
	public static int dialogueLastSwapKey;
	public static int stateBeforeDialogue;
	public static boolean Field131;
	public static int Field132;
	public static long Field133;
	public static boolean Field134;
	public static boolean Field135;
	public static boolean Field136;
	public static boolean Field137;
	public static int dialogueEnvironment;
	public static int levelCameraTargetX;
	public static int levelCameraTargetY;
	public static int dialogueOriginX;
	public static int dialogueOriginY;
	public static boolean Field143;
	public static boolean dialogueIsCameraMoving;
	public static int[] dialogueSfx = new int[] {-1, -1, -1, -1, -1, -1, -1, -1};
	public static int sceneStartupState = 0;
	public static Image imgGamelogoTop;
	public static Image imgGamelogoBottom;
	public static boolean isPastSplash = false;
	public static int sceneTransitionNextState;
	public static long sceneTransitionStartMs;
	public static int Field152;
	public static int sceneTransitionType;
	public static boolean Field154 = true;
	public static int Field155;
	public static int levelFinishX;
	public static int levelFinishY;
	public static int Field158;
	public static int Field159;
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
	public static int levelBombObjectID;
	public static int Field172;
	public static int Field173 = 0;
	public static int levelPlayerHitTicks = 0;
	public static int levelCurrentGrabberFlags = -1;
	public static int Field176 = -1;
	public static int[] blobEyeState = new int[2];
	public static int[] currentMouthState = new int[2];
	public static Image[] imgsEyeLeft;
	public static Image[] imgsEyeM;
	public static Image[] imgsEyeRight;
	public static Image[] imgsEyeC;
	public static Image[][] imgsMouths;
	public static Image[][] imgsEyes;
	public static Image[][] imgsEyesC;
	public static Image imgMouth;
	public static int[][] Field187 = new int[][] {{0, 0}, {0, 0}, {0, 0}, {6, 8}, {0, -2}, {0, 0}, {0, 0}, {0, 0}};
	public static int[][] Field188 = new int[][] {{0, -3}, {3, 0}, {3, 0}, {10, 4}, {0, -5}, {4, 4}, {4, 1}, {6, 4}};
	public static int[][] mouthX = new int[][] {{0, 0}, {2, 6}, {0, 0}, {-4, -8}, {4, 2}, {4, 4}, {0, 0}, {2, 6}};
	public static int[][] mouthY = new int[][] {{0, 0}, {5, 2}, {3, 6}, {5, 9}, {4, 9}, {2, 4}, {3, 7}, {3, 6}};
	public static int Field191;
	public static int Field192;
	public static int Field193;
	public static int Field194;
	public static int Field195;
	public static Image imgFuse;
	public static Image[] imgsBomb;
	public static int levelNumHooks;
	public static int[] levelHookID1;
	public static int[] levelHookID2;
	public static int[] levelHookType;
	public static int[] levelHookExtent;
	public static int[] levelHookExtentSquare;
	public static int[] levelHookStretchConstant;
	public static boolean[] Field205;
	public static boolean[] levelHookIsActive;
	public static int swappedLevelNumHooks;
	public static int[] swappedLevelHookID1;
	public static int[] swappedLevelHookID2;
	public static int[] Field210;
	public static int[] Field211;
	public static int[] swappedLevelHookExtentSquare;
	public static int[] Field213;
	public static boolean[] Field214;
	public static boolean[] Field215;
	public static int[] Field216;
	public static int[] Field217;
	public static int[] Field218;
	public static int[] Field219;
	public static int[] Field220;
	public static int[] Field221;
	public static int levelExpanderTicks;
	public static int[] levelExpanderIndex;
	public static boolean[] levelExpanderIsTouchActivated;
	public static boolean[] levelExpanderIsVacant;
	public static int[] levelExpanderDelay;
	public static int[] levelExpanderExtent;
	public static int[] levelExpanderMinRadius;
	public static int[] levelGrabberIDs;
	public static Image imgGrabber;
	public static int levelPingCircleID;
	public static int[] levelPingShardIDs;
	public static int[][] levelPingEnemyIDs;
	public static int levelPingEnemyCurrentIndex;
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
	public static boolean[] Field258;
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
	public static int Field273;
	public static int[] Field274;
	public static int[] Field275;
	public static int[] Field276;
	public static int[] Field277;
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
	public static int levelWeatherID = 0;
	public static int levelBackgroundID = 0;
	public static int levelWidth;
	public static int levelHeight;
	public static int Field321;
	public static int xLossRate;
	public static boolean levelPlayerOnCircle;
	public static boolean isLevelComplete;
	public static boolean mirrored = false;
	public static boolean isFinalLevel;
	public static int currentLevelLoaded = 0;
	public static int level = 0;
	public static int levelColor = 0xffffff;
	public static int levelColorMovable = 0xffffff;
	public static int levelColorGoal = 0xffffff;
	public static int levelColorDestructible = 0xffffff;
	public static int levelColorBouncer = 0xff4500;
	public static int levelRAMinRed = 152;
	public static int levelRAMaxRed = 255;
	public static int levelRAMinGreen = 36;
	public static int levelRAMaxGreen = 182;
	public static int levelRAMinBlue = 16;
	public static int levelRAMaxBlue = 33;
	public static int levelPortalFilledRadius = 0;
	public static short[] levelIDExpanders;
	public static short[] levelIDFoam;
	public static short[] levelIDDestructibles;
	public static short[] levelIDGoals;
	public static short[] levelIDRadioactive;
	public static short[] levelIDBombDispenser;
	public static short[] levelIDPlatforms;
	public static int activeSwapKey = -1;
	public static int swappedLevelWidth;
	public static int swappedLevelHeight;
	public static int Field351;
	public static int Field352;
	public static boolean Field353;
	public static boolean Field354;
	public static int currentSoundID = -1;
	public static Image[] imgsBackground;
	public static int[][] activeBackgroundPattern;
	public static int[][] Field358;
	public static int currentLoadedBackgroundID;
	public static int valOfZero;
	public static int Field361;
	public static int Field362;
	public static int Field363;
	public static boolean currentBackgroundHasObjects = true;
	public static boolean[] backgroundHasObjects = new boolean[] {true, true, true, true, false};
	public static int[] fallbackWeathers = new int[] {2, 1, 1, 2, 0};
	public static int[] knownBackgrounds = new int[] {0, 1, 2, 3, 4};
	public static int[][][] backgroundPatterns = new int[][][] {{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, {{0, 0}, {0, 0}, {1, 1}}, {{0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}}, {{3, 0, 1, 2}, {0, 1, 2, 0}, {1, 2, 0, 3}, {2, 0, 0, 0}}};
	public static int[][][] Field369 = new int[][][] {{{1, 2, 1, 2}, {3, 3, 3, 3}, {4, 4, 4, 4}}, {{2, 2, 2}, {3, 3, 3}, {3, 3, 3}}, {{1, 2, 1}, {3, 3, 3}, {3, 3, 3}}, {{1, 1, 1}, {2, 2, 2}, {2, 2, 2}}, {{0}}};
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
	public static boolean Field387;
	public static Image[][] imgsFaces;
	public static int Field389 = -2;
	public static int levelIntroTicks = 0;
	public static boolean Field391 = false;
	public static boolean isDejaVuMessageShown = false;
	public static boolean Field393 = false;
	public static int levelDeathTicks = 70;
	public static int levelTicks = 0;
	public static int levelDialogueToDisplay = -1;
	public static int language = 0;
	public static int levelCameraZoom = 72;
	public static int[] cheatCodeLevelComplete = new int[] {8, 16, 8, 16, 2};
	public static int[] cheatCodeInvincibility = new int[] {64, 16, 64, 8, 64, 2};
	public static int iterCheatLevelComplete = 0;
	public static int iterCheatInvincibility = 0;
	public static boolean isPlayerInvincible = false;
	public static boolean Field404 = true;
	public static boolean pauseScreenDraw = false;
	public static int[] throbberColors = new int[12];
	public static int throbberColorIndex = 9;
	public static boolean gammaBlackSet = false;
	public static int gamma = 100;
	// decors inside ship: socks, flowers, cubes, etc
	public static short[] decorForeground;
	public static short[] decorBackground;
	public static Image[] imgDecors;
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
	public static int levelShipTouchedIconIndex;
	public static int Field424 = 0;
	public static String[] textTableShip;
	public static String[] textTableShipPause;
	public static boolean Field427 = false;
	public static boolean Field428 = false;
	public static int spaceMapCameraX;
	public static int spaceMapCameraY;
	public static int spaceMapTargetCameraX;
	public static int spaceMapTargetCameraY;
	public static int spaceMapShipX;
	public static int spaceMapShipY;
	public static int spaceMapBeaconX;
	public static int spaceMapBeaconY;
	public static int spaceMapBeaconRadius;
	public static boolean spaceMapShowIcons;
	public static boolean Field439;
	public static Image imgPointer;
	public static Image imgShip;
	public static Image imgPlanet;
	public static int levelCompleteTicks;
	public static int Field444;
	public static int Field445;
	public static int Field446;
	public static String textMission;
	public static String textCompleted;
	// trigonometric
	public static short[] sin1000;
	public static short[] cos1000;
	public static Image[] imgsStatic;
	public static int[] staticTiles;
	public static boolean isTransmodigrafierMissing = true;
	public static boolean isPurpleTransmissionShaky = true;
	public static boolean isShowingShardPicture = false;
	public static boolean Field456 = false;
	public static int currentPurpleX = 59;
	public static int currentPurpleY = 108;
	public static int currentPurpleSize = 100;
	public static int pingBackgroundColor = 0;
	public static int activePurpleShardNameID = 0;
	public static String[] purpleShardNames = new String[] {"Kachoo", "NikSak", "Rara", "Corakllquar", "Brad", "NotPing"};
	public static Image[] imgsSelectionMenu;
	public static Image[] imgsSelectionMenuArrows;
	public static int Field465;
	public static long Field466;
	public static int selectionAngle;
	public static int selectionAngleBetweenOthers;
	public static int selectionAngleUntilAdjust;
	public static int numSelections;
	public static int Field471;
	public static int Field472;
	public static int Field473;
	
	public Game() throws IOException {
		super(false);
		setFullScreenMode(true);
		instance = this;
	}
	
	public final void hideNotify() {
		_heldKeys = 0;
		paused = true;
		queueAllSoundsForCleanup();
		queueSoundCleanup();
		setPauseScreenDraw();
	}
	
	public final void showNotify() {
	}
	
	public final void init(PMMIDlet midlet) {
		Game.midlet = midlet;
		Thread th = new Thread(this);
		th.setPriority(5);
		th.start();
	}
	
	public final void run() {
		gfx = this.getGraphics();
		gSetClip(0, 0, 128, 128);
		start();
		midlet.exit();
	}
	
	// funny circle
	public static final void renderThrobber() {
		renderThrobberNextFrame();
	}
	
	public static final void updateKeys() {
		sleep(5L);
		heldPrevKeys = heldKeys;
		heldKeys = _heldKeys;
		_heldKeys &= ~Field5;
		Field5 = 0;
	}
	
	private static int pressedKeyValue(int keyCode) {
		for(int k = 0; k < KEYMAP.length; k++) {
			for(int i = 0; i < KEYMAP[k].length; i++) {
				if(keyCode == KEYMAP[k][i]) {
					return 1 << k;
				}
			}
		}
	
		return 0x100000;
	}
	
	public final void keyPressed(int keyCode) {
		_heldKeys |= pressedKeyValue(keyCode);
		super.keyPressed(keyCode);
		if(keyCode == -6) {
			Field5 |= 1;
			Field5 |= 0x0800;
			_heldKeys |= 0x0800;
		}
	
		if(keyCode == -7) {
			Field5 |= 4;
			Field5 |= 0x1000;
			_heldKeys |= 0x1000;
		}
	}
	
	public final void keyReleased(int keyCode) {
		_heldKeys &= ~pressedKeyValue(keyCode);
		super.keyReleased(keyCode);
	}
	
	public static final boolean isKeyHeld(int var0) {
		return (heldKeys & var0) > 0;
	}
	
	public static final boolean isKeyNewlyHeld(int var0) {
		return (heldKeys & var0) > 0 && (heldPrevKeys & var0) == 0;
	}
	
	public static final boolean isKeyReleased(int var0) {
		return (heldPrevKeys & var0) > 0 && (heldKeys & var0) == 0;
	}
	
	public static final void clearKeys() {
		heldKeys = 0;
		heldPrevKeys = 0;
		_heldKeys = 0;
		Field5 = 0;
	}
	
	public static final byte[] makePNG(byte[] pimData, short pplCrc) {
		if(pngTemplate == null) {
			pngTemplate = loadFile8((short)0xdee7);
		}
	
		loadPalette(pplCrc);

		if((pimData[0] & 3) != 3) {
			return null;
		} else {
			int idatLen = pimData.length - 18;
			byte[] pngData = new byte[0x21 + 0x0c + (pplColorCount * 3) + (0x0d * (pplOptions & 1)) + 0x0c + idatLen + 0x04 + 0x0c];
			int off = 0;

			// PNG sig with IHDR
			System.arraycopy(pngTemplate, 0x00, pngData, 0x00, 0x1d);

			// width height
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
			if((pplOptions & 1) == 1) {
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
		if(pimData == null) {
			return null;
		} else {
			if(pimData[0] != 0x89 && pimData[1] != 'P') {
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
	
		for(int i = 0; i < 10; i++) {
			Thread.yield();
			if(Runtime.getRuntime().freeMemory() < initFreeMem) {
				return;
			}
		}
	}
	
	public static final void lockFPS() {
		currTime = millis();
		if(currTime - prevTime < 45L) {
			sleep(45L - (currTime - prevTime));
		}
	
		prevTime = millis();
	}
	
	public static final void writeRecordBytes(byte[] data) {
		try {
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			if(rs != null) {
				if(rs.getNumRecords() == 0) {
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
			if(rs != null) {
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
		data[8] = (byte)(mirrored ? 1 : 0);
		data[9] = (byte)(vibration ? 1 : 0);
		writeRecordBytes(data);
	}
	
	public static final boolean loadRecordData() {
		byte[] data = readRecordBytes();

		if(data == null) {
			return false;
		} else {
			try {
				language = (data[0] & 0x000000ff) + (data[1] << 8 & 0x0000ff00) + (data[2] << 16 & 0x00ff0000) + (data[3] << 24 & 0xff000000);
				level = (data[4] & 0x000000ff) + (data[5] << 8 & 0x0000ff00) + (data[6] << 16 & 0x00ff0000) + (data[7] << 24 & 0xff000000);
				mirrored = data[8] == 1;
				vibration = data[9] == 1;
				return true;
			} catch(Exception e) {
				return false;
			}
		}
	}
	
	public static final void initSoundSystem(int var0) {
		numSounds = var0;
		soundActiveStates = new boolean[var0];
		soundQueueCleanup = new boolean[var0];
		soundUsedStates = new boolean[var0];
		Field26 = new int[var0];
		soundLoopCounts = new int[var0];
		soundTypes = new int[var0];
		Field20 = -1;
		soundPlayers = new Player[var0];
		soundPlayersMIDI = new Player[var0];
		numActiveSoundsMIDI = 0;
		numActiveSounds = 0;
		soundFilenames = new String[var0];

		for(int var2 = 0; var2 < numSounds; var2++) {
			soundActiveStates[var2] = false;
			soundQueueCleanup[var2] = false;
			soundUsedStates[var2] = false;
			Field26[var2] = 5;
			soundLoopCounts[var2] = 1;
			soundTypes[var2] = 999;
			soundPlayers[var2] = null;
			soundPlayersMIDI[var2] = null;
		}
	}
	
	public static final int loadSoundToQueue(int var0, String var1, int var2, int var3) {
		if(isSoundIndexUsed(var0)) {
			return -2;
		} else if(var0 >= numSounds) {
			return -3;
		} else if(var1 == null) {
			return -1;
		} else {
			if(throbberToggle) {
				renderThrobber();
			}
	
			soundTypes[var0] = var2;
			if(!var1.equals(soundFilenames[var0])) {
				soundFilenames[var0] = var1;
			}
	
			return -4;
		}
	}
	
	public static final int loadSoundToIndex(int var0, String var1, int var2, int var3) {
		if(isSoundIndexUsed(var0)) {
			return -2;
		} else if(var0 >= numSounds) {
			return -3;
		} else if(var1 == null) {
			return -1;
		} else {
			if(throbberToggle) {
				renderThrobber();
			}
	
			soundTypes[var0] = var2;
			if(!var1.equals(soundFilenames[var0])) {
				soundFilenames[var0] = var1;
			}
	
			String var4 = "";
			Player var5 = null;
	
			try {
				if(var2 == 6) {
					InputStream var9 = var4.getClass().getResourceAsStream("/" + var1 + ".wav");
					soundPlayers[var0] = Manager.createPlayer(var9, "audio/x-wav");
					var5 = soundPlayers[var0];
				}
	
				if(var2 == 7) {
					InputStream var10 = var4.getClass().getResourceAsStream("/" + var1 + ".amr");
					soundPlayers[var0] = Manager.createPlayer(var10, "audio/amr");
					var5 = soundPlayers[var0];
				}
	
				if(var2 == 8) {
					InputStream var11 = var4.getClass().getResourceAsStream("/" + var1 + ".mp3");
					soundPlayers[var0] = Manager.createPlayer(var11, "audio/mp3");
					var5 = soundPlayers[var0];
				}
	
				if(var2 == 2) {
					InputStream var12 = var4.getClass().getResourceAsStream("/" + var1 + ".mid");
					soundPlayersMIDI[var0] = Manager.createPlayer(var12, "audio/sp-midi");
					var5 = soundPlayersMIDI[var0];
				}
	
				if(var2 == 1) {
					InputStream var13 = var4.getClass().getResourceAsStream("/" + var1 + ".mid");
					soundPlayersMIDI[var0] = Manager.createPlayer(var13, "audio/midi");
					var5 = soundPlayersMIDI[var0];
				}
	
				var5.realize();
	
				while(var5.getState() != 200) {
					sleep(30L);
				}
	
				numActiveSoundsTotal++;
				soundUsedStates[var0] = true;
				boolean var7 = false;
				if(var2 != 1 && var2 != 2) {
					if(var2 != 6 && var2 != 7 && var2 != 8) {
						return -2;
					}
	
					if(numActiveSounds < 1) {
						var5.prefetch();
						numActiveSounds++;
						var7 = true;
					}
				} else if(numActiveSoundsMIDI < 1) {
					var5.prefetch();
					numActiveSoundsMIDI++;
					var7 = true;
				}
	
				if(var7) {
					while(var5.getState() != 300) {
						sleep(30L);
					}
				}
	
				return 0;
			} catch(Exception e) {
				if(var5 != null) {
					var5.close();
					soundPlayers[var0] = null;
					soundPlayersMIDI[var0] = null;
					soundTypes[var0] = 999;
					soundFilenames[var0] = "";
				}
	
				return -1;
			}
		}
	}
	
	public static final void closeSoundAtIndex(int var0) {
		Player var1 = null;
		if(soundPlayers[var0] != null) {
			var1 = soundPlayers[var0];
			numActiveSounds++;
		}
	
		if(soundPlayersMIDI[var0] != null) {
			var1 = soundPlayersMIDI[var0];
			numActiveSoundsMIDI++;
		}
	
		if(var1 != null) {
			var1.close();
	
			while(var1.getState() != 0) {
				sleep(30L);
			}
	
			soundPlayers[var0] = soundPlayersMIDI[var0] = null;
			numActiveSoundsTotal++;
		}
	}
	
	public static final boolean isSoundIndexUsed(int var0) {
		if(soundPlayers[var0] != null) {
			return true;
		} else {
			return soundPlayersMIDI[var0] != null;
		}
	}
	
	public static final boolean isSoundActive(int var0, int var1) {
		if(!audio) {
			return false;
		} else {
			boolean var2 = false;
			if((var1 == soundTypes[var0] || var1 == -1) && soundActiveStates[var0]) {
				return true;
			} else {
				if((var1 == 6 || var1 == -1) && soundPlayers[var0] != null) {
					var2 = soundPlayers[var0].getState() == 400;
				}
	
				if((var1 == 1 || var1 == -1) && soundPlayersMIDI[var0] != null) {
					var2 = soundPlayersMIDI[var0].getState() == 400;
				}
	
				return var2;
			}
		}
	}
	
	public static final boolean playSound(int var0, int var1) {
		if(!audio) {
			return true;
		} else if(millis() - Field21 < 100L) {
			return false;
		} else {
			Field21 = millis();
			if(isSoundActive(var0, -1)) {
				return false;
			} else {
				int var4 = 0;
				if(soundTypes[var0] == 6 || soundTypes[var0] == 7 || soundTypes[var0] == 8) {
					for(int var5 = 0; var5 < numSounds; var5++) {
						if(isSoundActive(var5, soundTypes[var5])) {
							var4++;
						}
					}
	
					if(var4 >= 2) {
						return false;
					}
				}
	
				soundActiveStates[var0] = true;
				soundLoopCounts[var0] = var1;
				return true;
			}
		}
	}
	
	public static final void queueAllSoundsForCleanup() {
		if(audio) {
			for(int var0 = 0; var0 < numSounds; var0++) {
				soundQueueCleanup[var0] = true;
			}
		}
	}
	
	private static void Method30(int var0, boolean var1) {
		Player var5;
		if(soundPlayersMIDI[var0] != null) {
			var5 = soundPlayersMIDI[var0];
		} else {
			var5 = soundPlayers[var0];
		}
	
		try {
			if(var5 != null) {
				try {
					if(var1) {
						if(var5.getState() == 400) {
							var5.stop();
							var5.setMediaTime(0L);
						}
					} else {
						var5.stop();
						var5.setMediaTime(0L);
					}
				} catch(IllegalStateException e) {
				}
			}
		} catch(MediaException e) {
		}
	}
	
	public static final void queueSoundCleanup() {
		if(audio && soundActiveStates != null) {
			for(int var0 = 0; var0 < numSounds; var0++) {
				if(soundQueueCleanup[var0]) {
					soundQueueCleanup[var0] = false;
					Method30(var0, false);
					if(soundTypes[var0] != 0) {
						closeSoundAtIndex(var0);
					}
	
					if(Field20 == var0) {
						Field20 = -1;
					}
				}
	
				if(soundActiveStates[var0] && soundTypes[var0] == 1) {
					boolean var1 = false;
	
					for(int var5 = 0; var5 < numSounds; var5++) {
						if(var5 != var0) {
							Method30(var5, false);
							soundActiveStates[var5] = false;
						}
					}
	
					for(boolean var2 = true; var2; sleep(5L)) {
						for(int var6 = 0; var6 < numSounds; var6++) {
							if(var6 != var0 && isSoundActive(var6, -1)) {
								var2 = true;
								break;
							}
	
							var2 = false;
						}
					}
				}
	
				if(!isSoundIndexUsed(var0)) {
					if(soundTypes[var0] == 0 || !soundActiveStates[var0]) {
						continue;
					}
	
					boolean var7 = throbberToggle;
					throbberToggle = false;
					loadSoundToIndex(var0, soundFilenames[var0], soundTypes[var0], 0);
					throbberToggle = var7;
				}
	
				Player var9;
				if(soundPlayersMIDI[var0] != null) {
					var9 = soundPlayersMIDI[var0];
				} else {
					var9 = soundPlayers[var0];
				}
	
				if(soundUsedStates[var0]) {
					soundUsedStates[var0] = false;
					if(var9 != null) {
						try {
							VolumeControl var10;
							if((var10 = (VolumeControl)var9.getControl("VolumeControl")) != null) {
								var10.setLevel(Field26[var0] * 10);
							}
						} catch(IllegalStateException e) {
						}
					}
				}
	
				if(soundActiveStates[var0]) {
					soundActiveStates[var0] = false;
	
					try {
						if(var9 != null && var9.getState() != 400) {
							var9.setLoopCount(soundLoopCounts[var0]);
							var9.start();
							Field20 = var0;
						}
					} catch(MediaException e) {
					}
				}
			}
		}
	}
	
	public static final int getFileIndex(short fnCrc) {
		for(int i = 0; i < bfcHeadNumEntries; i++) {
			if(bfcHeadCrcs[i] == fnCrc) {
				return i;
			}
		}
	
		return -1;
	}
	
	public static final void bfcInitReservedData(int count) {
		bfcReservedData = new byte[count][];
		currentReserved = false;
	}
	
	public static final void bfcReserve(int index) {
		try {
			currentIndex = index;
			currentReserved = true;
			currentOffsetReserved = 0;
			bfcReservedData[index] = new byte[currentSize];

			int read = 0;
			int total = 0;	
			for(int rem = currentSize; rem > 0; total += read) {
				read = currentData.read(bfcReservedData[index], total, rem);
				rem -= read;
			}
	
			for(int i = 0; i < bfcHeadNumEntries; i++) {
				if(i != index && bfcHeadOffsets[i] == bfcHeadOffsets[index] && bfcHeadLocations[i] == bfcHeadLocations[index]) {
					bfcReservedData[i] = bfcReservedData[index];
				}
			}
	
			currentOffset += currentSize;
		} catch(Exception e) {
		}
	}
	
	public static final void bfcLoadHead() {
		currentLocation = -1;
		currentOffset = 0;
		bfcGenCrcTable();
	
		try {
			String file = "/head.bfc";
			currentData = new DataInputStream(file.getClass().getResourceAsStream(file));

			int numEntries = currentData.readUnsignedShort();
			bfcHeadNumEntries = numEntries;
			bfcHeadCrcs = new short[numEntries];
			bfcHeadMemStates = new byte[numEntries];
			bfcHeadOffsets = new int[numEntries];
			bfcHeadLocations = new byte[numEntries];
			bfcHeadSizes = new int[numEntries];
	
			for(int i = 0; i < numEntries; i++) {
				bfcHeadCrcs[i] = (short)currentData.readUnsignedShort();
				bfcHeadMemStates[i] = currentData.readByte();
				bfcHeadOffsets[i] = currentData.readUnsignedByte() << 16 | currentData.readUnsignedByte() << 8 | currentData.readUnsignedByte();
				bfcHeadLocations[i] = (byte)currentData.readUnsignedByte();
				bfcHeadSizes[i] = currentSize = currentData.readUnsignedByte() << 16 | currentData.readUnsignedByte() << 8 | currentData.readUnsignedByte();
			}
		} catch(Exception e) {
		}
	
		bfcInitReservedData(bfcHeadNumEntries);
	}
	
	public static final void bfcGenCrcTable() {
		for(int i = 0; i < 256; ++i) {
			int val = 0;
			int x = i << 8;
	
			for(int k = 0; k < 8; k++) {
				if(((val ^ x) & 0x8000) != 0) {
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
	
		for(int i = 0; i < file.length(); i++) {
			char c = file.charAt(i);
			byte b = (byte)(c >> 8);
			val = (bfcCrcTable[(b ^ val >> 8) & 0xff] ^ val << 8) & 0xffff;
			b = (byte)(c & 0xff);
			val = (bfcCrcTable[(b ^ val >> 8) & 0xff] ^ val << 8) & 0xffff;
		}
	
		return (short)(val & 0xffff);
	}
	
	public static final byte[] loadFile8(short fnCrc) {
		if(!sOpenFile(fnCrc)) {
			return null;
		} else {
			byte[] var1 = new byte[currentSize];
			sReadBytes(var1, 0, currentSize);
			return var1;
		}
	}
	
	public static final byte[] getFile8(short fnCrc) {
		int index = getFileIndex(fnCrc);
		if(index < 0) {
			return null;
		} else {
			return bfcReservedData[index] != null ? bfcReservedData[index] : loadFile8(fnCrc);
		}
	}
	
	public static final short[] loadFile16(String file) {
		return loadFile16(bfcCrcFilename(file));
	}
	
	public static final short[] loadFile16(short fnCrc) {
		if(!sOpenFile(fnCrc)) {
			return null;
		} else {
			short[] arr = new short[currentSize / 2];
	
			for(int i = 0; i < arr.length; i++) {
				arr[i] = sRead16();
			}
	
			return arr;
		}
	}
	
	public static final int[] loadFile32(String file) {
		return loadFile32(bfcCrcFilename(file));
	}
	
	public static final int[] loadFile32(short fnCrc) {
		if(!sOpenFile(fnCrc)) {
			return null;
		} else {
			int[] arr = new int[currentSize / 4];
	
			for(int i = 0; i < arr.length; i++) {
				arr[i] = sRead32();
			}
	
			return arr;
		}
	}
	
	public static final boolean sOpenFile(String file) {
		if(sOpenFile(bfcCrcFilename(file))) {
			return true;
		} else {
			try {
				InputStream is = file.getClass().getResourceAsStream("/" + file);
				if(is == null) {
					return false;
				} else {
					currentData = new DataInputStream(is);
					return true;
				}
			} catch(Exception e) {
				return false;
			}
		}
	}
	
	public static final boolean sOpenFile(short fnCrc) {
		if(throbberToggle) {
			renderThrobber();
		}
	
		int index = getFileIndex(fnCrc);
		currentIndex = index;
		currentOffsetReserved = 0;
		currentReserved = false;
		if(index >= 0 && bfcReservedData[index] != null) {
			currentSize = bfcHeadSizes[index];
			currentReserved = true;
			return true;
		} else {
			if(index != -1) {
				currentSize = bfcHeadSizes[index];
				if(currentLocation == bfcHeadLocations[index] && currentOffset <= bfcHeadOffsets[index]) {
					sSkipBytes(bfcHeadOffsets[index] - currentOffset);
					if(bfcHeadMemStates[index] >= 0 && bfcReservedData[index] == null) {
						bfcReserve(index);
					}
	
					return true;
				}
	
				try {
					if(currentData != null) {
						closeStream();
					}
	
					currentData = new DataInputStream(instance.getClass().getResourceAsStream("/" + bfcHeadLocations[index] + ".bfc"));
					currentData.skip((long)bfcHeadOffsets[index]);
					currentLocation = bfcHeadLocations[index];
					currentOffset = bfcHeadOffsets[index];
					if(bfcHeadMemStates[index] >= 0 && bfcReservedData[index] == null) {
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
		if(currentReserved) {
			currentOffsetReserved += n;
		} else {
			currentOffset += n;
	
			try {
				while(n > 0) {
					n = (int)((long)n - currentData.skip((long)n));
				}
			} catch(Exception e) {
			}
		}
	}
	
	public static final byte[] sReadBytes(byte[] b, int off, int len) {
		if(currentReserved) {
			System.arraycopy(bfcReservedData[currentIndex], currentOffsetReserved, b, off, len);
			currentOffsetReserved += len;
			return b;
		} else {
			currentOffset += len;
	
			try {
				for(int i = 0; len > 0; off += i) {
					i = currentData.read(b, off, len);
					len -= i;
				}
	
				return b;
			} catch(Exception e) {
				return null;
			}
		}
	}
	
	public static final int sReadU16() {
		if(currentReserved) {
			int var0 = (bfcReservedData[currentIndex][currentOffsetReserved] & 0xff) << 8 | bfcReservedData[currentIndex][currentOffsetReserved + 1] & 0xff;
			currentOffsetReserved += 2;
			return var0;
		} else {
			currentOffset += 2;
	
			try {
				return currentData.readUnsignedShort();
			} catch(Exception e) {
				return -1;
			}
		}
	}
	
	public static final short sRead16() {
		if(currentReserved) {
			short var0 = (short)(bfcReservedData[currentIndex][currentOffsetReserved] << 8 | bfcReservedData[currentIndex][currentOffsetReserved + 1] & 0xff);
			currentOffsetReserved += 2;
			return var0;
		} else {
			currentOffset += 2;
	
			try {
				return currentData.readShort();
			} catch(Exception e) {
				return -1;
			}
		}
	}
	
	public static final int sReadU8() {
		if(currentReserved) {
			int var0 = bfcReservedData[currentIndex][currentOffsetReserved] & 0xff;
			currentOffsetReserved++;
			return var0;
		} else {
			currentOffset++;
	
			try {
				return currentData.readUnsignedByte();
			} catch(Exception e) {
				return -1;
			}
		}
	}
	
	public static final byte sRead8() {
		if(currentReserved) {
			byte var0 = bfcReservedData[currentIndex][currentOffsetReserved];
			currentOffsetReserved++;
			return var0;
		} else {
			currentOffset++;
	
			try {
				return currentData.readByte();
			} catch(Exception e) {
				return -1;
			}
		}
	}
	
	public static final int sRead32() {
		if(currentReserved) {
			int var0 = (bfcReservedData[currentIndex][currentOffsetReserved] & 0xff) << 24 | (bfcReservedData[currentIndex][currentOffsetReserved + 1] & 0xff) << 16 | (bfcReservedData[currentIndex][currentOffsetReserved + 2] & 0xff) << 8 | bfcReservedData[currentIndex][currentOffsetReserved + 3] & 0xff;
			currentOffsetReserved += 4;
			return var0;
		} else {
			currentOffset += 4;
	
			try {
				return currentData.readInt();
			} catch(Exception e) {
				return -1;
			}
		}
	}
	
	public static final void closeStream() {
		try {
			currentData.close();
			currentIndex = -1;
			currentLocation = -1;
		} catch(Exception e) {
		}
	
		currentData = null;
	}
	
	public static final void setTextTableCrc(short var0) {
		clearTextTable();
		Field52 = var0;
	}
	
	public static final void clearTextTable() {
		Field49 = null;
		Field51 = -1;
	}
	
	public static final String[] loadTextTableFromIndex(int var0, int var1) {
		if(var1 < 0 && var0 == Field51) {
			return Field49;
		} else {
			boolean var2 = throbberToggle;
			throbberToggle = false;
			if(!sOpenFile(Field52)) {
				Field49 = null;
				Field50 = null;
			}
	
			throbberToggle = var2;
			int var3 = sReadU8();
			int var4 = sReadU8();
			if(var0 >= var4) {
				return Field49;
			} else {
				sSkipBytes(var0 * 2);
				sSkipBytes(sReadU16() + (var4 - var0 - 1) * 2);
				int var6 = sReadU16();
				int[] var7 = new int[var6];
				if(var1 < 0) {
					Field49 = new String[var6];
					Field50 = new byte[var6];
					Field51 = var0;
	
					for(int var8 = 0; var8 < var6; var8++) {
						var7[var8] = sReadU16();
						sReadU16();
					}
				} else {
					if(var1 >= var6) {
						return Field49;
					}
	
					sSkipBytes(var1 * 4);
					var7[0] = sReadU16();
					sSkipBytes(sReadU16() + (var6 - var1 - 1) * 4);
				}
	
				for(int var12 = 0; var12 < var6; var12++) {
					int var10002 = var7[var12]--;
					int var9 = 0;
					char[] var10 = new char[var7[var12]];
					if(var3 == 1) {
						var9 = sReadU8();
	
						for(int var11 = 0; var11 < var7[var12]; var11++) {
							var10[var11] = (char)sReadU8();
						}
					} else {
						var9 = sReadU16();
	
						for(int var14 = 0; var14 < var7[var12]; var14++) {
							var10[var14] = (char)sReadU16();
						}
					}
	
					String var15 = String.valueOf(var10);
					if(var1 >= 0) {
						break;
					}
	
					Field50[var12] = (byte)var9;
					Field49[var12] = var15;
				}
	
				return Field49;
			}
		}
	}
	
	public static final String getText(int var0) {
		int var1 = var0 >> 16;
		if(Field51 != var1) {
			loadTextTableFromIndex(var1, -1);
		}
	
		return Field49[var0 & 0xffff];
	}
	
	public static final int lp32Mul(int var0, int var1) {
		return (int)((long)var0 * (long)var1 >> 16);
	}
	
	public static final int lp32Div(int var0, int var1) {
		return (int)(((long)var0 << 16) / (long)var1);
	}
	
	public static final int vectorMagnitude(int var0, int var1, int var2) {
		long var3 = (long)var0;
		long var5 = (long)var1;
		var3 = var3 * var3 >> 16;
		var5 = var5 * var5 >> 16;
		return (int)sqrtGuess(var3 + var5, (long)var2);
	}
	
	public static final long sqrtGuess(long var0, long var2) {
		if(var2 == 0L) {
			return sqrt(var0);
		} else {
			for(int var4 = 0; var4 < 5; var4++) {
				var2 -= ((var2 * var2 >> 16) - var0 << 16) / (var2 << 2);
			}
	
			return var2;
		}
	}
	
	public static final long sqrt(long var0) {
		long var2 = var0;
		long var4 = 0L;
		long var6 = var0;
	
		for(long var8 = 1L; var6 > 0L; var6 = var2 / var8) {
			var4 = var8;
			var8 *= 100L;
		}
	
		long var10 = 0L;
	
		long var15;
		for(var15 = 0L; var4 > 0L; var4 /= 100L) {
			var6 = var2 / var4;
	
			for(long var12 = var10 = 10L * var15; var12 < var10 + 10L && var12 * var12 <= var6; var15 = var12++) {
			}
		}
	
		return var15 << 8;
	}
	
	public static final int softkeyPressed(int l, int r) {
		leftSoftkey = l;
		rightSoftkey = r;

		if((heldKeys & 0x800) > 0 && (heldPrevKeys & 0x800) == 0) {
			return l;
		} else {
			return (heldKeys & 0x1000) > 0 && (heldPrevKeys & 0x1000) == 0 ? r : -1;
		}
	}
	
	public static final int softkeyPressed(int var0, int var1, boolean var2) {
		leftSoftkey = var0;
		rightSoftkey = var1;

		if((heldKeys & 0x800) > 0 && (heldPrevKeys & 0x800) == 0) {
			return var0;
		} else if((heldKeys & 0x1000) > 0 && (heldPrevKeys & 0x1000) == 0) {
			return var1;
		} else {
			return var2 && var0 >= 0 && (heldKeys & 1) > 0 && (heldPrevKeys & 1) == 0 ? var0 : -1;
		}
	}
	
	public static final void renderSoftkeyIcons() {
		if(imgsSoftkey != null) {
			if(leftSoftkey > -1) {
				gDrawImage(imgsSoftkey[leftSoftkey], 1, 127 - softkeyHeight[leftSoftkey], 0);
				leftSoftkey = -1;
			}
	
			if(rightSoftkey > -1) {
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
	
	public static final void setSoftkeyIcon(int var0, Image var1) {
		if(imgsSoftkey != null) {
			if(var0 >= 0 && var0 < imgsSoftkey.length) {
				imgsSoftkey[var0] = var1;
				softkeyWidth[var0] = var1.getWidth();
				softkeyHeight[var0] = var1.getHeight();
			}
		}
	}
	
	public static final void freeSoftkeyIcons() {
		imgsSoftkey = null;
		softkeyWidth = null;
		softkeyHeight = null;
	}
	
	public static final void initFonts(int var0) {
		fontIndexes = new short[var0][];
		fontAverageWidths = new int[var0];
		fontHeights = new int[var0];
		fontSpacesPerChars = new int[var0];
		fontImages = new Image[var0];
		fontLineGaps = new int[var0];
		fontChrOffsets = new short[var0][];
		fontChrWidths = new byte[var0][];
	}
	
	public static final void loadFont(int index, short pimCrc, short pplCrc, short cwtCrc, byte spaceWidth, short chrCrc, int spacePerChar, int lineGap) {
		fontIndexes[index] = new short[230];
		fontChrWidths[index] = new byte[230];
		byte[] charWidth = loadFile8(cwtCrc);
		short[] charMap = loadFile16(chrCrc);
		int charNum = charMap.length;
		fontImages[index] = loadImage(pimCrc, pplCrc);
		fontChrOffsets[index] = new short[charNum];
	
		for(short i = 0; i < 230; i++) {
			fontIndexes[index][i] = -1;
			fontChrWidths[index][i] = spaceWidth;
		}
	
		short curOff = 0;
	
		for(short i = 0; i < charNum; i++) {
			int printable = charMap[i] - 30;
			fontIndexes[index][printable] = i;
			fontChrWidths[index][printable] = charWidth[i];
			fontChrOffsets[index][i] = curOff;
			curOff = (short)(curOff + charWidth[i]);
		}
	
		fontHeights[index] = fontImages[index].getHeight() / 1;
		fontAverageWidths[index] = fontImages[index].getWidth() / charNum;
		fontSpacesPerChars[index] = spacePerChar;
		fontLineGaps[index] = lineGap;
	}
	
	public static final int calcTextWidth(String var0, int var1) {
		if(fontChrWidths[var1] == null) {
			return var0.length() * (fontAverageWidths[var1] + fontSpacesPerChars[var1]) - fontSpacesPerChars[var1];
		} else {
			int var2 = 0;
			int var3 = var0.length();
	
			for(int var4 = 0; var4 < var3; var4++) {
				int var5 = var0.charAt(var4) - 30;
				var2 += fontChrWidths[var1][var5];
			}
	
			return var2 + (var3 - 1) * fontSpacesPerChars[var1];
		}
	}
	
	public static final int calcAverageLengthUntilTerminator(int var0, int var1) {
		return (var0 + fontSpacesPerChars[var1]) / (fontAverageWidths[var1] + fontSpacesPerChars[var1]);
	}
	
	public static final int calcLengthUntilTerminator(int var0, int var1, String var2, int var3) {
		if(fontChrWidths[var1] == null) {
			return calcAverageLengthUntilTerminator(var0, var1);
		} else {
			int var4 = var2.length();
	
			int var5;
			for(var5 = var3; var5 < var4; var5++) {
				int var6;
				if((var6 = var2.charAt(var5) - 30) < 0) {
					return var5 - var3 + 1;
				}
	
				if((var0 = var0 - fontChrWidths[var1][var6]) < 0) {
					break;
				}
	
				var0 -= fontSpacesPerChars[var1];
			}
	
			return var5 - var3;
		}
	}
	
	public static final int[] getNewLineIndexes(int var0, String var1, int var2) {
		if(fontImages[var2] == null) {
			return null;
		} else {
			int var3 = 0;
			boolean var4 = false;
			int var5 = 99999;
			int var6 = var1.length();
			int var7 = 0;
			boolean var9 = false;
			int var11;
			if((var11 = var6 / 8) < 10) {
				var11 = 10;
			}
	
			int[] var12 = new int[var11];
			boolean var13 = false;
			boolean var14 = true;
			int var15 = 0;
	
			while(var7 < var6) {
				switch(var1.charAt(var7)) {
					case '\n':
						if(!var14 && var15 > 0) {
							var12[var3++] = var7;
						}
	
						var15++;
						var7++;
						break;
					case ' ':
						var7++;
						break;
					default:
						var15 = 0;
						var13 = true;
				}
	
				if(var13) {
					var14 = false;
					var13 = false;
					int var10;
					if((var10 = var7 + calcLengthUntilTerminator(var0, var2, var1, var7)) > var6) {
						var10 = var6;
					}
	
					int var16 = -1;
	
					int var8;
					for(var8 = var7; var8 < var10; var8++) {
						if(var1.charAt(var8) == '\n') {
							var16 = var8;
							break;
						}
	
						if(var1.charAt(var8) == ' ') {
							var16 = var8;
						}
					}
	
					if(var8 < var6 && var16 > 0 && var1.charAt(var8) != ' ') {
						var8 = var16;
					}
	
					if(var8 >= var5) {
						var8 = var5;
						var6 = 0;
					}
	
					if(var7 > var8) {
						var7 = var8;
					}
	
					var12[var3++] = var7;
					var7 = var8;
				}
			}
	
			int[] var18 = new int[var3];
	
			for(int var17 = 0; var17 < var3; var17++) {
				var18[var17] = var12[var17];
			}
	
			return var18;
		}
	}
	
	public static final void gSetColor(int var0) {
		gfx.setColor(var0);
	}
	
	public static final void gSetColor(int var0, int var1, int var2) {
		gfx.setColor(var0, var1, var2);
	}
	
	public static final int gGetColor() {
		return gfx.getColor();
	}
	
	public static final void gFillRect(int var0, int var1, int var2, int var3) {
		gfx.fillRect(var0, var1, var2, var3);
	}
	
	public static final void gDrawRect(int var0, int var1, int var2, int var3) {
		gfx.drawRect(var0, var1, var2, var3);
	}
	
	public static final void gDrawImage(Image var0, int var1, int var2, int var3) {
		gfx.drawImage(var0, var1, var2, var3);
	}
	
	public static final void gDrawLine(int var0, int var1, int var2, int var3) {
		gfx.drawLine(var0, var1, var2, var3);
	}
	
	public static final void gFillArc(int var0, int var1, int var2, int var3, int var4, int var5) {
		gfx.fillArc(var0, var1, var2, var3, var4, var5);
	}
	
	public static final void gDrawArc(int var0, int var1, int var2, int var3, int var4, int var5) {
		gfx.drawArc(var0, var1, var2, var3, var4, var5);
	}
	
	public static final void gFillTriangle(int var0, int var1, int var2, int var3, int var4, int var5) {
		gfx.fillTriangle(var0, var1, var2, var3, var4, var5);
	}
	
	public static final void gSetClip(int var0, int var1, int var2, int var3) {
		gfx.setClip(var0, var1, var2, var3);
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
	
	public static final int renderTextEx(int var0, int var1, int var2, int var3, String var4, int var5, int var6, int var7, boolean var8) {
		if(fontImages[var5] == null) {
			return -2;
		} else {
			int var9 = var4.length();
			int var10 = var6;
			int var12 = var1 + var3 - (fontHeights[var5] + fontLineGaps[var5] - 1);
			boolean var14 = false;
			boolean var15 = true;
			int var16 = 0;
	
			while(var10 < var9 && var1 < var12) {
				switch(var4.charAt(var10)) {
					case '\n':
						if(!var15 && var16 > 0 && (var1 += fontHeights[var5] + fontLineGaps[var5]) > var12) {
							return var10;
						}
	
						var16++;
						var10++;
						break;
					case ' ':
						var10++;
						break;
					default:
						var16 = 0;
						var14 = true;
				}
	
				if(var14) {
					var15 = false;
					var14 = false;
					int var13;
					if((var13 = var10 + calcLengthUntilTerminator(var2, var5, var4, var10)) > var9) {
						var13 = var9;
					}
	
					int var17 = -1;
	
					int var11;
					for(var11 = var10; var11 < var13; var11++) {
						if(var4.charAt(var11) == '\n') {
							var17 = var11;
							break;
						}
	
						if(var4.charAt(var11) == ' ') {
							var17 = var11;
						}
					}
	
					if(var11 < var9 && var17 > 0 && var4.charAt(var11) != ' ') {
						var11 = var17;
					}
	
					if(var11 >= var7) {
						var11 = var7;
						var9 = 0;
					}
	
					if(var10 > var11) {
						var10 = var11;
					}
	
					String var18 = var4.substring(var10, var11);
					if(var8) {
						renderText(var0 + (var2 - calcTextWidth(var18, var5)) / 2, var1, var18, var5);
					} else {
						renderText(var0, var1, var18, var5);
					}
	
					var1 += fontHeights[var5] + fontLineGaps[var5];
					var10 = var11;
				}
			}
	
			return var10 < var9 && var4.charAt(var10) != ' ' ? var10 : var10 + 1;
		}
	}
	
	public static final void renderText(int var0, int var1, String var2, int var3) {
		if(fontImages[var3] != null) {
			int var4 = gGetClipHeight();
			int var5 = gGetClipWidth();
			int var6 = gGetClipX();
			int var7 = gGetClipY();
			if(var1 + fontHeights[var3] >= var7 && var7 + var4 >= var1) {
				int var8 = var2.length();
				if(var0 == -1000) {
					var0 = (128 - calcTextWidth(var2, var3)) / 2;
				}
	
				int var9 = var1;
				int var10 = fontHeights[var3];
				if(var7 > var1) {
					var10 -= var7 - var1;
					var9 = var7;
				}
	
				if(var1 + var10 > var7 + var4) {
					var10 = var7 + var4 - var9;
				}
	
				int var13 = var0;
	
				for(int var14 = 0; var14 < var8; var14++) {
					int var15 = var2.charAt(var14) - 30;
					if(var13 <= 128 && var15 >= 0) {
						short var16;
						if((var16 = fontIndexes[var3][var15]) == -1) {
							if(fontChrWidths[var3] == null) {
								var13 += fontAverageWidths[var3] + fontSpacesPerChars[var3];
							} else {
								var13 += fontChrWidths[var3][0] + fontSpacesPerChars[var3];
							}
						} else {
							int var11 = var13;
							int var12;
							if(fontChrWidths[var3] == null) {
								var12 = fontAverageWidths[var3];
							} else {
								var12 = fontChrWidths[var3][var15];
							}
	
							if(var13 + var12 < 0) {
								if(fontChrWidths[var3] != null) {
									var13 += fontChrWidths[var3][var15] + fontSpacesPerChars[var3];
								} else {
									var13 += fontAverageWidths[var3] + fontSpacesPerChars[var3];
								}
							} else {
								if(var13 < var6 || var13 + var12 > var6 + var5) {
									if(var13 + var12 < var6) {
										continue;
									}
	
									if(var13 > var6 + var5) {
										break;
									}
	
									if(var13 < var6) {
										var12 -= var6 - var13;
										var11 = var6;
									}
	
									if(var11 + var12 > var6 + var5) {
										var12 = var6 + var5 - var11;
									}
								}
	
								gSetClip(var11, var9, var12, var10);
								if(fontChrWidths[var3] != null) {
									gDrawImage(fontImages[var3], var13 - fontChrOffsets[var3][var16], var1, 0);
									var13 += fontChrWidths[var3][var15] + fontSpacesPerChars[var3];
								} else {
									gDrawImage(fontImages[var3], var13 - fontAverageWidths[var3] * var16, var1, 0);
									var13 += fontAverageWidths[var3] + fontSpacesPerChars[var3];
								}
							}
						}
					}
				}
	
				gSetClip(var6, var7, var5, var4);
			}
		}
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
			if(paused) {
				pauseScreenOnce();
				renderSoftkeyIcons();
				gfxFlush();
				updateKeys();
				if(isKeyReleased(1)) {
					paused = false;
				}
			} else if(isNewState) {
				isNewState = false;
				execState(curScreenIndex, 2);
				oldScreenIndex = curScreenIndex;
				curScreenIndex = newStateIndex;
				execState(curScreenIndex, 0);
			} else {
				execState(curScreenIndex, 1);
			}
		} while(curScreenIndex != -1);
	
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
		if(state >= 0) {
			switch(state) {
				// selection menu
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

				// game developers
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
		if(vibration) {
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
		for(int var0 = 0; var0 < 40; var0++) {
			if(confettiColors[var0] == 0 || confettiY[var0] > 128) {
				confettiColors[var0] = confettiColorsAll[rand8() % confettiColorsAll.length];
				confettiHeights[var0] = rand8() % 40;
				confettiX[var0] = rand16() % 128;
				confettiY[var0] = -rand16() % 128;
				confettiWidths[var0] = 8 + rand8() % 7;
				confettiAngles[var0] = rand8();
			}
	
			int[] var10000 = confettiY;
			var10000[var0] += 128 * (1 + rand8() % 5) / 176;
			var10000 = confettiX;
			var10000[var0] += 3 * cos1000[confettiAngles[var0]] / 1000;
			var10000 = confettiHeights;
			var10000[var0] += 4 + rand8() % 5;
			if(confettiHeights[var0] >= 40) {
				confettiHeights[var0] = 0;
				confettiColors[var0] = confettiColorsAll[rand8() % confettiColorsAll.length];
			}
	
			var10000 = confettiAngles;
			var10000[var0] += 3 + var0 % 2;
			if(confettiAngles[var0] >= 360) {
				var10000 = confettiAngles;
				var10000[var0] -= 360;
			}
	
			int var1 = confettiHeights[var0] / 4;
			if(confettiHeights[var0] >= 20) {
				var1 = (40 - confettiHeights[var0]) / 4;
			}
	
			gSetColor(confettiColors[var0]);
			gFillRect(confettiX[var0], confettiY[var0], 128 * confettiWidths[var0] / 176, 128 * var1 / 176);
		}
	}
	
	public static final void renderCritterShock(int var0, int var1, int var2, int var3, int var4, int var5) {
		int var11 = (var2 - var0) / 7;
		int var12 = (var3 - var1) / 7;
		boolean var13 = false;
		boolean var14 = false;
		boolean var15 = false;
		boolean var16 = false;
	
		for(int var17 = 0; var17 < 2; var17++) {
			int var8 = var0;
			int var9 = var1;
			int var10 = 111 + rand8() % 140;
			if(var17 == 0) {
				gSetColor(var10, var10, var10);
			} else {
				gSetColor(var10, var10, 255);
			}
	
			for(int var18 = 1; var18 < var4; var18++) {
				int var6 = var8;
				int var7 = var9;
				var8 = var0 + var18 * var11 + rand8() % var5 - var5 / 2;
				var9 = var1 + var18 * var12 + rand8() % var5 - var5 / 2;
				if(var18 == 7) {
					var8 = var2;
					var9 = var3;
				}
	
				gDrawLine(var6, var7, var8, var9);
			}
		}
	}
	
	public static final void renderRain() {
		if(rainX == null) {
			rainX = new int[8];
			rainY = new int[8];
		}
	
		gSetColor(0xc8c8c8);
	
		for(int var0 = 0; var0 < 8; var0++) {
			if(rainY[var0] > 128 || rainX[var0] == 0 && rainY[var0] == 0) {
				rainX[var0] = rand16() % 149;
				rainY[var0] = rand16() % 21;
			}
	
			gDrawLine(rainX[var0], rainY[var0], rainX[var0] + -3, rainY[var0] + 10);
			int[] var10000 = rainX;
			var10000[var0] += -9;
			var10000 = rainY;
			var10000[var0] += 30;
		}
	}
	
	public static final void levelRenderVolcanicDust() {
		if(Field84 == null) {
			Field84 = new int[10];
			Field85 = new int[10];
			Field86 = new int[10];
		}
	
		gSetColor(0xffffff);
		if(levelBackgroundID == 0) {
			gSetColor(0);
		}
	
		for(int var0 = 0; var0 < 10; var0++) {
			if(Field85[var0] > 128 || Field84[var0] == 0 && Field85[var0] == 0) {
				if(Field84[var0] == 0 && Field85[var0] == 0) {
					Field85[var0] = rand16() % 128;
				} else {
					Field85[var0] = -(rand16() % 10);
				}
	
				Field84[var0] = rand16() % 128;
				Field86[var0] = rand16() % 360;
			}
	
			gDrawLine(Field84[var0] - 1, Field85[var0], Field84[var0] + 1, Field85[var0]);
			gDrawLine(Field84[var0], Field85[var0] - 1, Field84[var0], Field85[var0] + 1);
			int[] var10000 = Field84;
			var10000[var0] += 3 * cos1000[Field86[var0]] / 1000;
			var10000 = Field85;
			var10000[var0] += 2;
			var10000 = Field86;
			var10000[var0] += 5;
			if(Field86[var0] >= 360) {
				var10000 = Field86;
				var10000[var0] -= 360;
			}
		}
	}
	
	public static final void initSpace(int var0, int var1, int var2, int var3) {
		if(currentSpaceWidth != var2 || currentSpaceHeight != var3 || spaceStarX == null || spaceStarX.length != var0 || spaceStarX[0].length != var1) {
			currentSpaceWidth = var2;
			currentSpaceHeight = var3;
			spaceStarX = new int[var0][];
			spaceStarY = new int[var0][];
			spaceStarXVel = new int[var0][];
			Field90 = new int[var0][];
	
			for(int var4 = 0; var4 < var0; var4++) {
				spaceStarX[var4] = new int[var1];
				spaceStarY[var4] = new int[var1];
				spaceStarXVel[var4] = new int[var1];
				Field90[var4] = new int[var1];
	
				for(int var5 = 0; var5 < var1; var5++) {
					spaceStarX[var4][var5] = rand16() % var2 << 4;
					spaceStarY[var4][var5] = rand16() % var3 << 4;
					spaceStarXVel[var4][var5] = -1 - rand8() % 32;
					Field90[var4][var5] = 0;
				}
			}
		}
	}
	
	public static final void renderSpace(int var0, int var1, int var2) {
		renderSpace(var0, var1, var2, true);
	}
	
	public static final void renderSpace(int var0, int var1, int var2, boolean var3) {
		if(var0 >= 0 && var0 < spaceStarX.length) {
			Field90[var0][rand16() % Field90[var0].length] = 9;
	
			for(int var7 = 0; var7 < spaceStarX[var0].length; var7++) {
				int var5;
				int var6;
				int var4 = var5 = var6 = 30 - 7 * spaceStarXVel[var0][var7];
				if(var7 % 3 == 1) {
					var5 = 0;
					var4 = 0;
				}
	
				if(var7 % 3 == 2) {
					var6 = 0;
					var5 = 0;
				}
	
				gSetColor(var4, var5, var6);
				if(Field90[var0][var7] > 0) {
					int var10002 = Field90[var0][var7]--;
					if(Field90[var0][var7] > 4) {
						gSetColor(0);
					} else {
						gSetColor(0xffffff);
					}
				}
	
				renderStar(var1 + (spaceStarX[var0][var7] >> 4), var2 + (spaceStarY[var0][var7] >> 4));
				if(var3) {
					int[] var10000 = spaceStarX[var0];
					var10000[var7] += spaceStarXVel[var0][var7];
					if(spaceStarX[var0][var7] < 0) {
						spaceStarX[var0][var7] = currentSpaceWidth - 1 << 4;
						spaceStarY[var0][var7] = rand16() % currentSpaceHeight << 4;
						spaceStarXVel[var0][var7] = -4 - rand8() % 28;
					}
				}
			}
		}
	}
	
	public static final void renderStar(int x, int y) {
		gDrawLine(x, y, x, y);
		int col = gGetColor();
		gSetColor((col & 0xff0000) >> 17, (col & 0x00ff00) >> 9, (col & 0x0000ff) >> 1); // divide each RGB channel by 2
		gDrawLine(x, y - 1, x, y - 1);
		gDrawLine(x, y + 1, x, y + 1);
		gDrawLine(x - 1, y, x - 1, y);
		gDrawLine(x + 1, y, x + 1, y);
	}
	
	public static final void sceneHelpRun() {
		renderHelp();
		if(softkeyPressed(2, -1) == 2) {
			setNewState(2, 0);
		}
	
		if(isKeyHeld(0x40)) {
			sceneHelpScrollAccel -= (400 + sceneHelpScrollAccel) / 6;
			sceneHelpAutoscrollActive = false;
		}
	
		if(isKeyHeld(0x20)) {
			sceneHelpScrollAccel += (400 - sceneHelpScrollAccel) / 6;
			sceneHelpAutoscrollActive = false;
		}
	
		sceneHelpScrollAccel = 95 * sceneHelpScrollAccel / 100;
		if(sceneHelpAutoscrollActive) {
			sceneHelpScrollAccel = -0xff;
		}
	
		sceneHelpScrollDistance += sceneHelpScrollAccel;
		if(sceneHelpScrollDistance > 0x8000) {
			sceneHelpScrollDistance = 0x8000;
		}
	
		if(sceneHelpScrollDistance < -(sceneHelpText.length * 14 << 8)) {
			sceneHelpScrollDistance = -(sceneHelpText.length * 14 << 8);
		}
	
		sceneHelpScroll++;
		refreshGame();
	}
	
	public static final void renderHelp() {
		gSetColor(0);
		gFillRect(0, 0, 128, 128);
		renderSpace(0, 0, 0, true);
		int var0 = sceneHelpScrollDistance >> 8;
	
		for(int var1 = 0; var1 < sceneHelpText.length; var1++) {
			if(var0 > -14 && var0 < 128) {
				int var2 = 0;
				if(var0 < 42) {
					var2 = 90 - 90 * var0 * 3 / 128;
				} else if(var0 > 85) {
					var2 = 90 - (128 - var0) * 3 * 90 / 128;
				}
	
				int var3 = 5 + 128 * sin(var2) / 2600;
				renderText(var3, var0, sceneHelpText[var1], 2);
			}
	
			var0 += 14;
		}
	
		gDrawImage(imgSceneHelpScrollbar[0], 120, 43 - (sceneHelpScroll >> 2 & 3), 0);
		gDrawImage(imgSceneHelpScrollbar[1], 118, 46 - 39 * ((sceneHelpScrollDistance >> 8) - 128) / (sceneHelpText.length * 14), 0);
		gDrawImage(imgSceneHelpScrollbar[2], 120, 92 + (sceneHelpScroll >> 2 & 3), 0);
	}
	
	public static final void sceneHelpInit(int var0) {
		if(imgSceneHelpScrollbar == null) {
			imgSceneHelpScrollbar = new Image[3];
	
			for(int var1 = 0; var1 < 3; var1++) {
				imgSceneHelpScrollbar[var1] = loadImage("scrollBar" + var1 + ".pim", "scrollBar" + var1 + ".ppl");
			}
		}
	
		initSpace(1, 50, 128, 128);
		if(sceneHelpText == null) {
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
		if(scenePreLevelCurTick > 65) {
			gDrawImage(imgShipSmall, scenePreLevelSmallShipX, scenePreLevelSmallShipY, 0);
			scenePreLevelSmallShipX -= 2;
		}
	
		gDrawImage(imgCurrentPlanet, 0, 128 - imgCurrentPlanet.getHeight(), 0);
		if(scenePreLevelShipX < 128) {
			gDrawImage(imgOutside, scenePreLevelShipX, scenePreLevelShipY, 0);
			gDrawImage(imgsFlame[(int)(millis() / 100L % 2L)], scenePreLevelShipX + 7 - 10, scenePreLevelShipY + 16 - 3, 0);
			scenePreLevelShipX += 5;
		}
	
		if(scenePreLevelCurTick > 50) {
			renderText(-1000, 85, currentPlanetText, 0);
		}
	
		if(scenePreLevelCurTick > 120 && (millis() & 512L) > 0L) {
			renderText(-1000, 114, titleText, 0);
		}
	
		if(scenePreLevelCurTick < 120 && softkeyPressed(-1, 3) == 3 || scenePreLevelCurTick > 120 && softkeyPressed(2, -1, true) == 2) {
			setNewState(4, 0);
		}
	
		scenePreLevelCurTick++;
		refreshGame();
	}
	
	public static final void scenePreLevelInit(int var0) {
		playSound(-1);
		activateLevel(level);
		currentPlanetText = loadTextTableFromIndex(86, -1)[level];
		String var2 = "planet";
		var2 = var2 + level % 2 + "_" + levelBackgroundID % 5;
		imgCurrentPlanet = loadImage(var2 + ".pim", var2 + ".ppl");
		imgOutside = loadImage("outside.pim", "outside.ppl");
		imgShipSmall = loadImage("shipSmall.pim", "shipSmall.ppl");
		scenePreLevelSmallShipX = 128;
		scenePreLevelSmallShipY = 128 - imgCurrentPlanet.getHeight() * 4 / 5;
		if(scenePreLevelSmallShipY < 32) {
			scenePreLevelSmallShipY = 32;
		}
	
		scenePreLevelShipX = -61;
		scenePreLevelShipY = 70;
		if(imgsFlame == null) {
			imgsFlame = new Image[2];
			imgsFlame[0] = loadImage("flame0.pim", "flame0.ppl");
			imgsFlame[1] = loadImage("flame1.pim", "flame1.ppl");
		}
	
		if(titleText == null) {
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
			case 0:
				_sceneLevelRun();
				if(isLevelComplete) {
					startDialogue("outro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					levelCompletionState = 1;
				}
				break;
			case 1:
				initVictory();
				levelCompletionState = 2;
			case 2:
				if(renderMissionComplete()) {
					levelCompletionState = 3;
				}
				break;
			case 3:
				setNewState(2, 10);
		}
	
		if(Field391) {
			setNewState(2, 0);
		}
	}
	
	public static final void sceneLevelInit(int var0) {
		Field427 = false;
		if(oldScreenIndex != 5 && var0 != 1) {
			throbberToggle = true;
			levelCompletionState = 0;
			loadFaces();
			initExplosives();
			initEnemies();
			initSigns();
			initGrabber();
			Field391 = false;
			isLevelComplete = false;
		}
	
		gamma = 100;
		if(oldScreenIndex != 5 && oldScreenIndex != 6) {
			playCurrentBackgroundMusic();
		}
	}
	
	public static final void sceneLevelFree() {
		if(isLevelComplete) {
			level++;
			saveRecordData();
			isLevelComplete = false;
		}
	
		if(newStateIndex != 5 && newStateIndex != 6) {
			playSound(-1);
			garbageCollector();
		}
	}
	
	public static final void sceneSplashRun() {
		gSetColor(0xffffff);
		gFillRect(0, 0, 128, 128);
		switch(ttlScr) {

			// Eidos image
			case 0:
				gDrawImage(imgCenterLogo, (128 - imgCenterLogo.getWidth()) / 2, (128 - imgCenterLogo.getHeight()) / 2, 0);
				break;

			// Eidos legal line
			case 1:
				gDrawImage(imgEidosLegalLine, (128 - imgEidosLegalLine.getWidth()) / 2, 128 - imgEidosLegalLine.getHeight() - 4, 0);
				break;

			// PM RocketFuel image
			case 2:
				gDrawImage(imgCenterLogo, ttlScrLogoX - 27, ttlScrLogoY - 1, 0);
				gDrawImage(imgPmRocket, ttlScrLogoX, ttlScrLogoY, 0);
				break;

			// PM RocketFuel anim
			case 3:
				int x = ttlScrLogoX;
				int y = ttlScrLogoY;
				if(ttlScrTick < 20) {
					x = ttlScrLogoX + rand8() % 5 - 2;
					y = ttlScrLogoY + rand8() % 5 - 2;
					gDrawImage(imgCenterLogo, x - 27, y - 1, 0);
				} else {
					ttlScrLogoX += (ttlScrTick - 20) / 8;
					ttlScrLogoY -= (ttlScrTick - 20) / 8;
					gDrawImage(imgCenterLogo, 15, 19, 0);
				}
	
				gDrawImage(imgPmRocket, x, y, 0);
				if(x > 158) {
					ttlScrDuration = 0L;
				} else {
					ttlScrDuration = millis() + 2000L;
				}
	
				ttlScrTick++;
				break;

			// Maniacs of Noice scaling up
			case 4:
				int[] var2 = new int[] {0, 0xffffff, 0, 0xffffff, 0x3aaec7};
				int[] var3 = new int[] {54, 54, 37, 37, 18};
				int[] var4 = new int[] {0, -10, -5, 2, 0};
	
				for(int var5 = 0; var5 < 5; var5++) {
					gSetColor(var2[var5]);
					int var6 = sin1000[ttlScrTick * 2] * var4[var5] / 1000;
					int var7 = sin1000[ttlScrTick * 2] * var3[var5] / 1000;
					if(var5 != 2 && var5 != 3) {
						gFillArc(64 + var6 - var7 / 2 - 1, 64 - var7 / 2, var7, var7, 0, 360);
					} else {
						gFillArc(64 + var6 - var7 / 2 - 1, 64 - var7 / 2, var7, var7, 80, 200);
					}
				}
	
				gDrawImage(imgPmRocket, ttlScrLogoX, ttlScrLogoY, 0);
				ttlScrLogoX += 4;
				ttlScrLogoY -= 4;
				if(ttlScrTick > 45) {
					ttlScrDuration = 0L;
				}
	
				ttlScrTick++;
				break;

			// Maniacs of Noise image
			case 5:
				gDrawImage(imgCenterLogo, 19, 18, 0);
		}
	
		if(millis() > ttlScrDuration || isKeyReleased(1)) {
			setNewState(1, ttlScr + 1);
		}
	
		refreshGame();
	}
	
	public static final void sceneSplashInit(int screen) {
		ttlScr = screen;
		switch(screen) {

			// Eidos image
			case 0:
				imgCenterLogo = loadImage("eidos.pim", "eidos.ppl");
				ttlScrDuration = millis() + 2500L;
				return;

			// Eidos legal line
			case 1:
				imgEidosLegalLine = loadImage("eidos_legal_line.pim", "eidos_legal_line.ppl");
				ttlScrDuration = millis() + 2500L;
				return;

			// PM RocketFuel image
			case 2:

			// PM RocketFuel anim
			case 3:
				if(imgCenterLogo == null) {
					imgCenterLogo = loadImage("pmback.pim", "pmback.ppl");
				}
	
				if(imgPmRocket == null) {
					imgPmRocket = loadImage("pmrocket.pim", "pmrocket.ppl");
				}
	
				ttlScrLogoX = 42;
				ttlScrLogoY = 20;
				ttlScrTick = 0;
				ttlScrDuration = millis() + 2000L;
				return;

			// Maniacs of Noice scaling up
			case 4:
				ttlScrLogoX = -49;
				ttlScrLogoY = 128;
				ttlScrTick = 0;
				ttlScrDuration = millis() + 10000L;
				return;

			// Maniacs of Noise image
			case 5:
				imgCenterLogo = loadImage("mon.pim", "mon.ppl");
				ttlScrDuration = millis() + 2000L;
				return;

			default:
				setNewState(7, 0);
				throbberToggle = true;
		}
	}
	
	public static final void sceneSplashFree() {
		switch(ttlScr) {
			case 0:
				imgCenterLogo = null;
			case 1:
				imgEidosLegalLine = null;
			case 2:
				break;
			case 3:
				imgCenterLogo = null;
				break;
			case 4:
				imgPmRocket = null;
				break;
			case 5:
				imgCenterLogo = null;
				break;
			default:
				imgCenterLogo = null;
				imgEidosLegalLine = null;
				imgPmRocket = null;
		}
	
		garbageCollector();
	}
	
	public static final void sceneShipRun() {
		if(Field393) {
			renderBeamAnimation(Field123);
			Field123 += 4;
			if(Field123 >= 130) {
				Field393 = false;
			}
		} else if(dialogueIsAwaitingLevelStart) {
			dialogueIsAwaitingLevelStart = false;
			startDialogue("intro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
		} else if(shipNextOp > 0) {
			switch(shipNextOp) {
				case 1:
					setNewState(3, 0);
					break;
				case 2:
					if(mustResetData) {
						setNewState(-1, 0);
					}
					break;
				case 3:
					if(mustResetData) {
						Field389 = currentLevelLoaded;
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
					if(mustResetData) {
						level = 0;
						mirrored = false;
						saveRecordData();
						Field427 = false;
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
			if(levelPlayerOnCircle && levelCircleY[0] > 0x960000 && Field120 > 0) {
				shipNextOp = processMainMenuDialogue(Field120, Field427);
				Field120 = 0;
			}
	
			if(!Field393) {
				levelUpdateAll();
				updatePlayerControls();
			}
	
			if(levelShipTouchedIconIndex > 0) {
				if(levelShipTouchedIconIndex == Field119) {
					levelShipTouchedIconIndex = 0;
				} else {
					Field119 = levelShipTouchedIconIndex;
				}
			}
	
			if(levelPlayerOnCircle) {
				Field119 = 0;
			}
	
			if(levelShipTouchedIconIndex > 0) {
				Field120 = levelShipTouchedIconIndex;
			}
	
			levelShipTouchedIconIndex = 0;
			levelSetCamera(levelCircleX[0], levelHeight / 2, 0);
			levelSetPlayerPos(levelCircleX[6], levelCircleY[6], 6, 0x140000);
			renderShipBlobs(Field427);
			refreshGame();
		}
	}
	
	public static final void sceneShipInit(int var0) {
		loadRecordData();
		if(level >= 25) {
			mirrored = !mirrored;
			level = 0;
			saveRecordData();
			setNewState(7, 0);
		} else {
			if(activeSwapKey != 0) {
				initShip();
			}
	
			if(showControlsGuide) {
				showControlsGuide = false;
				startDialogue("intro0controls.bms", levelCircleX[0], levelCircleY[0]);
			}
	
			dialogueIsAwaitingLevelStart = false;
			if(var0 == 10) {
				if(Field393) {
					dialogueIsAwaitingLevelStart = true;
					Field123 = 1;
					levelSetCamera(levelCircleX[6], levelHeight / 2, 0);
				} else if(skipPrologue) {
					skipPrologue = false;
					startDialogue("continueStory.bms", levelCircleX[0], levelCircleY[0]);
					dialogueIsAwaitingLevelStart = true;
				} else {
					startDialogue("intro" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					if(level == 0) {
						showControlsGuide = true;
					}
				}
			}
	
			levelShipTouchedIconIndex = 0;
			Field404 = true;
			initSpace(2, 12, 92, 46);
			initInsideShip();
			loadFaces();
			if(textTableShip == null) {
				textTableShip = loadTextTableFromIndex(6, -1);
				textTableShipPause = loadTextTableFromIndex(7, -1);
			}
	
			if(!Field427) {
				playSound(2);
			}
		}
	}
	
	public static final void sceneShipFree() {
		if(newStateIndex != 5) {
			playSound(-1);
			garbageCollector();
		}
	}
	
	public static final void sceneDialogueRun() {
		long var0 = millis();
		if(!Field136) {
			currentDialogueIndex = executeDialogScript(currentDialogue, currentDialogueIndex, false);
		} else {
			if(!Field134 && !Field135 && Field133 == 0L) {
				if(dialogueEnvironment == 3) {
					if(renderSpaceMapNextFrame()) {
						Field136 = false;
					}
				} else if(!dialogueIsCameraMoving || levelCameraApproach(levelCameraTargetX, levelCameraTargetY, 0, 0x320000)) {
					Field136 = false;
					dialogueIsCameraMoving = false;
				}
			}
	
			if(Field133 != 0L && Field133 < var0) {
				Field133 = 0L;
			}
	
			if(Field135 && isKeyNewlyHeld(1)) {
				Field135 = false;
			}
	
			if(Field134 && Field387) {
				Field134 = false;
			}
		}
	
		switch(dialogueEnvironment) {
			case 0:
				renderShipInside();
				if(Field143 && !Field427) {
					renderPlayable(6, 0xc80000, true);
				}
	
				renderPlayable(0, 0xff, true);
				renderShipDecor(decorForeground);
				break;
			case 1:
				if(levelCameraZoom != 72) {
					Field132 += (72000 - Field132) / 10;
					levelCameraZoom = Field132 / 1000;
				}
	
				if(levelPlayerAngle < 180) {
					levelPlayerAngle = 90 * levelPlayerAngle / 100;
				} else {
					levelPlayerAngle -= 360;
					levelPlayerAngle = 90 * levelPlayerAngle / 100;
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
				renderPingTransmission();
		}
	
		if(Field137) {
			boolean var2 = false;
			gSetColor(0);
			gFillRect(0, 0, 128, 12);
			gFillRect(0, 116, 128, 17);
		}
	
		renderDialogueBox();
		if(Field127) {
			int var3 = softkeyPressed(2, 4);
			if(var3 >= 0) {
				for(mustResetData = var3 == 2; currentDialogueIndex < currentDialogue.length; currentDialogueIndex = executeDialogScript(currentDialogue, currentDialogueIndex, true)) {
				}
	
				Field136 = false;
			}
		} else if(Field135 && softkeyPressed(2, 3) == 3 || !Field135 && softkeyPressed(-1, 3) == 3) {
			while(currentDialogueIndex < currentDialogue.length) {
				currentDialogueIndex = executeDialogScript(currentDialogue, currentDialogueIndex, true);
			}
	
			Field136 = false;
			setNewState(6, 100 + stateBeforeDialogue);
			return;
		}
	
		refreshGame();
		if(currentDialogueIndex >= currentDialogue.length && !Field136) {
			setNewState(stateBeforeDialogue, 0);
		}
	}
	
	public static final void sceneDialogueInit(int var0) {
		Field132 = levelCameraZoom * 1000;
		if(currentDialogue == null) {
			setNewState(stateBeforeDialogue, 0);
		} else if(!Field131) {
			currentDialogueIndex = 0;
			mustResetData = false;
			stateBeforeDialogue = oldScreenIndex;
			activePurpleShardNameID = 0;
			dialogueLastSwapKey = activeSwapKey;
			Field143 = dialogueLastSwapKey == 0;
			if(Field143) {
				dialogueEnvironment = 0;
			} else {
				dialogueEnvironment = 1;
			}
	
			Method130();
			loadEyes();
			loadFacesIcons();
			Field136 = false;
			Field133 = 0L;
			dialogueIsCameraMoving = false;
			Field134 = false;
			Field135 = false;
		} else {
			Field131 = false;
		}
	}
	
	public static final void sceneDialogueFree() {
		if(!Field131 || currentDialogueIndex >= currentDialogue.length) {
			Field131 = false;
			currentDialogue = null;
			garbageCollector();
			swapLevelData(dialogueLastSwapKey);
			Method130();
		}
	}
	
	public static final void Method130() {
		Field137 = false;
		Field424 = 0;
		spaceMapBeaconRadius = 0;
		currentMouthState[0] = 0;
		currentMouthState[1] = 0;
	}
	
	public static final void startDialogue(String var0, int var1, int var2) {
		startDialogue(var0, var1, var2, false);
	}
	
	public static final void startDialogue(String var0, int var1, int var2, boolean var3) {
		throbberToggle = false;
		int[] var4 = loadFile32(var0);
		throbberToggle = true;
		if(var4 != null) {
			startDialogue(var4, var1, var2, var3);
		}
	}
	
	public static final void levelDisplayDialogue(int var0) {
		if(levelPlayerHealth > 0) {
			int var1 = levelCircleType[var0] - 10;
			startDialogue("ingame" + level + "-" + var1 + ".bms", levelCircleX[var0], levelCircleY[var0], false);
			levelCircleFlags[var0] = 0;
		}
	}
	
	public static final void startDialogue(int[] var0, int var1, int var2, boolean var3) {
		dialogueOriginX = var1;
		dialogueOriginY = var2;
		Field127 = var3;
		currentDialogue = var0;
		hideDialogueBox();
		setNewState(5, 0);
	}
	
	public static final int executeDialogScript(int[] var0, int var1, boolean var2) {
		while(var1 < var0.length) {
			switch(var0[var1]) {
				case 0:
					return var1 + 1;
				case 1:
					Field136 = true;
					Field133 = millis() + (long)(var0[var1 + 1] * 10);
					var1 += 2;
					break;
				case 2:
					Field136 = true;
					Field135 = true;
					var1++;
					break;
				case 3:
					if(dialogueEnvironment == 3) {
						if(var0[var1 + 1] == 6) {
							spaceMapSetCameraToBeacon();
						}
	
						if(var0[var1 + 1] == 5) {
							spaceMapSetCameraInstant(0, 0);
						}
	
						var1 += 2;
						break;
					}
	
					switch(var0[var1 + 1]) {
						case 0:
							levelCameraTargetX = levelCircleX[0];
							levelCameraTargetY = levelCircleY[0];
							var1 += 2;
							break;
						case 1:
							if(Field143) {
								levelCameraTargetX = levelCircleX[6];
								levelCameraTargetY = levelCircleY[6];
							} else {
								levelCameraTargetX = levelCircleX[0];
								levelCameraTargetY = levelCircleY[0];
							}
	
							var1 += 2;
							break;
						case 5:
							levelCameraTargetX = dialogueOriginX;
							levelCameraTargetY = dialogueOriginY;
							var1 += 2;
							break;
						default:
							levelCameraTargetX = levelAlignToGameMirror(var0[var1 + 1] << 16);
							levelCameraTargetY = var0[var1 + 2] << 16;
							var1 += 3;
					}
	
					levelSetCamera(levelCameraTargetX, levelCameraTargetY, 0);
					break;
				case 4:
					dialogueIsCameraMoving = true;
					if(dialogueEnvironment == 3) {
						if(var0[var1 + 1] == 6) {
							spaceMapSetTargetCameraToBeacon();
						}
	
						if(var0[var1 + 1] == 5) {
							spaceMapSetTargetCamera(0, 0);
						}
	
						var1 += 2;
						Field136 = true;
						break;
					}
	
					switch(var0[var1 + 1]) {
						case 0:
							levelCameraTargetX = levelCircleX[0];
							levelCameraTargetY = levelCircleY[0];
							var1 += 2;
							break;
						case 1:
							if(Field143) {
								levelCameraTargetX = levelCircleX[6];
								levelCameraTargetY = levelCircleY[6];
							} else {
								levelCameraTargetX = levelCircleX[0];
								levelCameraTargetY = levelCircleY[0];
							}
	
							var1 += 2;
							break;
						case 5:
							levelCameraTargetX = dialogueOriginX;
							levelCameraTargetY = dialogueOriginY;
							var1 += 2;
							break;
						default:
							levelCameraTargetX = levelAlignToGameMirror(var0[var1 + 1] << 16);
							levelCameraTargetY = var0[var1 + 2] << 16;
							var1 += 3;
					}
	
					Field136 = true;
					break;
				case 5:
					setCurrentSpeech(getText(var0[var1 + 1]), var0[var1 + 2]);
					Field134 = true;
					Field136 = true;
					var1 += 3;
					break;
				case 6:
					currentMouthState[var0[var1 + 1]] = var0[var1 + 2];
					if(dialogueSfx[var0[var1 + 2]] != -1 && !var2) {
						playSound(dialogueSfx[var0[var1 + 2]], 1);
					}
	
					var1 += 3;
					break;
				case 7:
					if(dialogueEnvironment == 5) {
						currentPurpleX = 128 * var0[var1 + 2] / 1000;
						currentPurpleY = 128 * var0[var1 + 3] / 1000;
					} else if(activeSwapKey == 0) {
						if(var0[var1 + 1] == 0) {
							levelSetPlayerPos(levelAlignToGameMirror(var0[var1 + 2] << 16), var0[var1 + 3] << 16, 0, 0x0c0000);
						} else {
							levelSetPlayerPos(levelAlignToGameMirror(var0[var1 + 2] << 16), var0[var1 + 3] << 16, 6, 0x140000);
						}
					}
	
					var1 += 4;
					break;
				case 8:
					if(!var2) {
						Field131 = true;
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
							if(!Field143) {
								swapLevelData(1);
							}
						case 2:
						case 5:
						default:
							break;
						case 3:
							initSpaceMap(72 * var0[var1 + 2] / 100, 72 * var0[var1 + 3] / 100, var0[var1 + 4]);
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
					Field134 = false;
					var1++;
					break;
				case 11:
					Field137 = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 12:
					if(dialogueEnvironment == 3) {
						spaceMapBeaconRadius = var0[var1 + 1] == 1 ? 1 : 0;
					}
	
					if(dialogueEnvironment == 0) {
						Field424 = var0[var1 + 1] == 1 ? 1 : 0;
					}
	
					var1 += 2;
					break;
				case 13:
					if(var0[var1 + 1] == 5) {
						spaceMapShowIcons = var0[var1 + 2] == 1;
					} else {
						Field439 = var0[var1 + 2] == 1;
					}
	
					var1 += 3;
					break;
				case 14:
					isTransmodigrafierMissing = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 15:
					isPurpleTransmissionShaky = var0[var1 + 1] == 1;
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
					activePurpleShardNameID = var0[var1 + 1];
					var1 += 2;
			}
		}
	
		return var1;
	}
	
	public static final void sceneSelectionRun() {
		int select = getSelectedIndex();
		if(select >= 0) {
			if(sceneStartupState == 0) {
				language = select;
				saveRecordData();
				setTextTableCrc((new short[] {(short)0x0085, (short)0x9e91, (short)0xdc77, (short)0x0d48, (short)0x4f2a})[language]);
				setNewState(0, 1);
				return;
			}
	
			audio = select == 1 || select == 3;
			loadAllSounds();
			setNewState(1, 0);
		}
	}
	
	public static final void sceneSelectionInit(int var0) {
		gamma = 100;
		sceneStartupState = var0;
		if(sceneStartupState == 0) {
			loadSelectionOptions(new String[] {"flag_uk", "flag_france", "flag_germany", "flag_spain", "flag_italy"}, language);
		}
	
		if(sceneStartupState == 1) {
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
		if(imgGamelogoTop != null) {
			gDrawImage(imgGamelogoTop, 17, 0, 0);
		}
	
		if(imgGamelogoBottom != null) {
			gDrawImage(imgGamelogoBottom, 24, 55, 0);
		}
	
		if((millis() & 512L) > 0L) {
			renderText(-1000, 114, titleText, 0);
		}
	
		if(softkeyPressed(2, -1, true) == 2) {
			isPastSplash = true;
			setNewState(6, 2);
		}
	
		refreshGame();
	}
	
	public static final void sceneMainMenuInit(int var0) {
		if(isPastSplash) {
			isPastSplash = false;
			setNewState(2, 10);
		} else {
			if(titleText == null) {
				titleText = getText(0x1000f);
			}
	
			imgGamelogoTop = loadImage("gamelogo.pim", "gamelogo.ppl");
			imgGamelogoBottom = loadImage("gamelogo2.pim", "gamelogo2.ppl");
			initSpace(1, 50, 128, 128);
			playSound(8);
			Field393 = false;
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
		if(!Field154) {
			if(sceneTransitionStartMs == -1L) {
				sceneTransitionStartMs = millis();
			}
	
			int var0 = (int)(millis() - sceneTransitionStartMs);
			int var1 = Field155 + 1000 * var0 / Field152;
			if(Field155 == 0 && var1 > 1000) {
				var1 = 1000;
				setNewState(sceneTransitionNextState, 1);
			}
	
			if(var1 > 2000) {
				Field154 = true;
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
		Field155 = 0;
		Field154 = false;
		sceneTransitionStartMs = millis();
		int var1 = sceneTransitionNextState;
		sceneTransitionNextState = oldScreenIndex;
		if(var0 >= 100) {
			sceneTransitionNextState = var0 - 100;
			var0 = 2;
		}
	
		sceneTransitionType = var0;
		Field152 = 250;
		if(var0 == 2) {
			Field152 = 150;
		}
	
		if(sceneTransitionNextState == 6) {
			sceneTransitionNextState = var1;
		}
	}
	
	public static final void sceneTransitionFree() {
		Field155 = 1000;
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
		if(var0 > 1000) {
			var3 = 129;
	
			for(int var4 = 6; var4 > 0; var4--) {
				gFillRect(var1 - var3 - var4, 0, var4, 128);
				var3 += var4 + (8 - var4);
			}
		} else {
			for(int var6 = 6; var6 > 0; var6--) {
				gFillRect(var3, 0, var6, 128);
				var3 += var6 + (8 - var6);
			}
		}
	
		gSetColor(var2);
	}
	
	public static final void loadFaces() {
		if(imgsEyeLeft == null) {
			imgsEyeLeft = new Image[2];
			imgsEyeLeft[0] = loadImage("eyeLWiz.pim", "eyeLWiz.ppl");
			imgsEyeLeft[1] = loadImage("eyeLWaz.pim", "eyeLWaz.ppl");
		}
	
		if(imgsEyeRight == null) {
			imgsEyeRight = new Image[2];
			imgsEyeRight[0] = loadImage("eyeRWiz.pim", "eyeRWiz.ppl");
			imgsEyeRight[1] = loadImage("eyeRWaz.pim", "eyeRWaz.ppl");
		}
	
		if(imgsEyeM == null) {
			imgsEyeM = new Image[2];
			imgsEyeM[0] = loadImage("eyeMWiz.pim", "eyeMWiz.ppl");
			imgsEyeM[1] = loadImage("eyeMWaz.pim", "eyeMWaz.ppl");
		}
	
		if(imgsEyeC == null) {
			imgsEyeC = new Image[2];
			imgsEyeC[0] = loadImage("eyeCWiz.pim", "eyeCWiz.ppl");
			imgsEyeC[1] = loadImage("eyeCWaz.pim", "eyeCWaz.ppl");
		}
	
		if(imgMouth == null) {
			imgMouth = loadImage("mouth.pim", "mouth.ppl");
		}
	
		if(imgsMouths == null) {
			imgsMouths = new Image[8][];
	
			for(int var0 = 0; var0 < imgsMouths.length; var0++) {
				imgsMouths[var0] = new Image[2];
				imgsMouths[var0][0] = loadImage("mouthWiz_" + var0 + ".pim", "mouthWiz_" + var0 + ".ppl");
				imgsMouths[var0][1] = loadImage("mouthWaz_" + var0 + ".pim", "mouthWaz_" + var0 + ".ppl");
			}
		}
	}
	
	public static final void loadEyes() {
		if(imgsEyes == null) {
			imgsEyes = new Image[8][];
	
			for(int var0 = 1; var0 < imgsEyes.length; var0++) {
				imgsEyes[var0] = new Image[2];
				imgsEyes[var0][0] = loadImage("eyesWiz_" + var0 + ".pim", "eyesWiz_" + var0 + ".ppl");
				imgsEyes[var0][1] = loadImage("eyesWaz_" + var0 + ".pim", "eyesWaz_" + var0 + ".ppl");
			}
	
			imgsEyesC = new Image[8][];
	
			for(int var1 = 1; var1 < imgsEyesC.length; var1++) {
				imgsEyesC[var1] = new Image[2];
				imgsEyesC[var1][0] = loadImage("eyesWiz_C" + var1 + ".pim", "eyesWiz_C" + var1 + ".ppl");
				imgsEyesC[var1][1] = loadImage("eyesWaz_C" + var1 + ".pim", "eyesWaz_C" + var1 + ".ppl");
			}
		}
	}
	
	public static final void initPlayer(boolean var0) {
		levelPlayerHealth = 500;
		if(var0) {
			levelPlayerRed = 200;
			levelPlayerGreen = 0;
			levelPlayerBlue = 0;
			Field172 = 13;
		} else {
			levelPlayerRed = 0;
			levelPlayerGreen = 0;
			levelPlayerBlue = 200;
			Field172 = 18;
		}
	
		levelPlayerRotationRate = 0;
		levelCurrentGrabberFlags = -1;
		Field176 = -1;
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
	
		for(int var4 = var2; var4 < var2 + 5; var4++) {
			levelCirclePrevX[var4] = levelCircleX[var4];
			levelCirclePrevY[var4] = levelCircleY[var4];
		}
	}
	
	public static final void Method152() {
		levelHookIsActive[16] = false;
		Field205[16] = false;
		levelCircleFlags[levelHookID2[16]] = levelCurrentGrabberFlags;
		levelCurrentGrabberFlags = -1;
		Field176 = levelHookID2[16];
		levelHookID2[16] = -1;
	}
	
	public static final void updatePlayerRotation() {
		if(Field176 != -1 && (levelCircleX[Field176] - levelCircleRadius[Field176] > levelCircleX[0] + 0x190000 || levelCircleX[Field176] + levelCircleRadius[Field176] < levelCircleX[0] - 0x190000 || levelCircleY[Field176] - levelCircleRadius[Field176] > levelCircleY[0] + 0x190000 || levelCircleY[Field176] + levelCircleRadius[Field176] < levelCircleY[0] - 0x190000)) {
			Field176 = -1;
		}
	
		if(Field173 > 0) {
			int[] var10000 = levelCircleY;
			var10000[0] -= 550000;
			Field173--;
		}
	
		if(levelPlayerRotationRate > 0) {
			levelPlayerRotationRate--;
			if(levelPlayerRotationRate > 25) {
				levelPlayerRotationRate = 25;
			}
		}
	
		if(levelPlayerRotationRate < 0) {
			levelPlayerRotationRate++;
			if(levelPlayerRotationRate < -25) {
				levelPlayerRotationRate = -25;
			}
		}
	
		levelPlayerAngle += levelPlayerRotationRate;
		if(levelPlayerAngle >= 360) {
			levelPlayerAngle -= 360;
		}
	
		if(levelPlayerAngle < 0) {
			levelPlayerAngle += 360;
		}
	
		if(levelPlayerRotationRate == 0) {
			byte var0 = 95;
			if(!levelPlayerOnCircle) {
				var0 = 90;
			}
	
			if(levelPlayerAngle < 180) {
				levelPlayerAngle = var0 * levelPlayerAngle / 100;
			} else {
				levelPlayerAngle -= 360;
				levelPlayerAngle = var0 * levelPlayerAngle / 100;
				levelPlayerAngle += 360;
			}
	
			if(levelPlayerAngle >= 360) {
				levelPlayerAngle -= 360;
			}
	
			if(levelPlayerAngle < 0) {
				levelPlayerAngle += 360;
			}
		}
	}
	
	public static final void renderFacePurple(int var0, int var1) {
		int var2 = levelAlignX(var0, var1);
		int var3 = levelAlignY(var0, var1);
		int var4 = levelCameraZoom * 10 / 100;
		int var5 = levelCameraZoom * 25 / 100;
		if(!isOutOfScreenBounds(var2 - var4, var3 - var5, var2 + var4, var3 + var5)) {
			gSetColor(0);
			gFillArc(var2 - var4 - 2, var3 - var5 - 2, 2 * (var4 + 2), 2 * (var5 + 2), 0, 360);
			gSetColor(0xa020f0);
			gFillArc(var2 - var4, var3 - var5, 2 * var4, 2 * var5, 0, 360);
			gDrawImage(imgMouth, var2 - imgMouth.getWidth() / 2, var3 - imgMouth.getHeight() / 2, 0);
			var2 -= imgsEyeM[1].getWidth() / 2;
			gDrawImage(imgsEyeM[1], var2 - var4, var3 - 2 * var5 / 3, 0);
			gDrawImage(imgsEyeM[1], var2, var3 - 2 * var5 / 3, 0);
			gDrawImage(imgsEyeM[1], var2 + var4, var3 - 2 * var5 / 3, 0);
		}
	}
	
	public static final void levelRenderPlayer() {
		if(levelPlayerHealth > 0) {
			levelPlayerRed = 100 + 100 * levelPlayerHealth / 500;
		}
	
		if((levelPlayerHitTicks & 1) > 0) {
			renderPlayable(0, 0, false);
		} else {
			renderPlayable(0, (levelPlayerRed << 16) + (levelPlayerGreen << 8) + levelPlayerBlue, false);
		}
	}
	
	public static final void renderPlayable(int var0, int var1, boolean var2) {
		byte var3 = 0;
		if(var0 > 0 || var0 == 0 && activeSwapKey == 1) {
			var3 = 1;
		}
	
		if(var3 == 0) {
			Field172 = 18;
			var1 = 0xff;
			if(currentLevelLoaded == -1) {
				var1 = 100 + 155 * levelPlayerHealth / 500 & 255;
			}
		} else {
			Field172 = 13;
		}
	
		int var4 = levelAlignX(levelCircleX[var0], levelCircleY[var0]);
		int var5 = levelAlignY(levelCircleX[var0], levelCircleY[var0]);
		int[] var6 = new int[4];
		if(levelCameraAngle > 45 && levelCameraAngle < 315) {
			if(levelCameraAngle >= 45 && levelCameraAngle <= 135) {
				var6[0] = levelCircleX[var0 + 0] - levelCircleX[var0 + 3] >> 16;
				var6[1] = levelCircleY[var0 + 0] - levelCircleY[var0 + 1] >> 16;
				var6[2] = levelCircleX[var0 + 4] - levelCircleX[var0 + 0] >> 16;
				var6[3] = levelCircleY[var0 + 2] - levelCircleY[var0 + 0] >> 16;
			} else if(levelCameraAngle >= 135 && levelCameraAngle <= 225) {
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
	
		for(int var7 = 0; var7 < 4; var7++) {
			var6[var7] = levelCameraZoom * var6[var7] / 100;
		}
	
		int[] var13 = new int[4];
	
		for(int var8 = 0; var8 < 4; var8++) {
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
		if(gamma > 50 && levelPlayerHealth > 0) {
			int var14 = levelPlayerAngle;
			if(var2) {
				var14 = 0;
			}
	
			if(levelCurrentGrabberFlags >= 0) {
				Image var9 = imgMouth;
				gDrawImage(var9, var4 - var9.getWidth() / 2, var5 - var9.getHeight() / 2, 0);
			} else {
				Image var15 = imgsMouths[currentMouthState[var3]][var3];
				if(currentMouthState[var3] > 0) {
					gDrawImage(var15, var4 - var15.getWidth() / 2 + mouthX[currentMouthState[var3]][var3], var5 - var15.getHeight() / 2 + mouthY[currentMouthState[var3]][var3], 0);
				} else {
					int var10 = 6 * levelCameraZoom / 100;
					int var11 = var4 + var10 * cos(var14 + 90) / 1000;
					int var12 = var5 + var10 * sin(var14 + 90) / 1000;
					gDrawImage(var15, var11 - var15.getWidth() / 2, var12 - var15.getHeight() / 2, 0);
				}
			}
	
			if(currentMouthState[var3] == 0) {
				Image var16 = imgsEyeM[var3];
				if(var0 > 0) {
					if(levelCircleX[0] + 0x160000 < levelCircleX[var0]) {
						var16 = imgsEyeLeft[var3];
					}
	
					if(levelCircleX[0] - 0x160000 > levelCircleX[var0]) {
						var16 = imgsEyeRight[var3];
					}
				} else {
					if(levelCircleX[var0] + 0x010000 < levelCirclePrevX[var0]) {
						var16 = imgsEyeLeft[var3];
					}
	
					if(levelCircleX[var0] - 0x010000 > levelCirclePrevX[var0]) {
						var16 = imgsEyeRight[var3];
					}
				}
	
				if(blobEyeState[var3] > 0) {
					var16 = imgsEyeC[var3];
				}
	
				int var18 = Field172 * levelCameraZoom / 100;
				int var20 = var4 + var18 * cos(var14 - 115) / 1000;
				int var22 = var5 + var18 * sin(var14 - 115) / 1000;
				gDrawImage(var16, var20 - var16.getWidth() / 2, var22 - var16.getHeight() / 2, 0);
				var20 = var4 + var18 * cos(var14 - 65) / 1000;
				var22 = var5 + var18 * sin(var14 - 65) / 1000;
				gDrawImage(var16, var20 - var16.getWidth() / 2, var22 - var16.getHeight() / 2, 0);
			} else {
				int var17 = Field172 * levelCameraZoom / 100;
				Image var19 = imgsEyes[currentMouthState[var3]][var3];
				if(blobEyeState[var3] > 0) {
					var19 = imgsEyesC[currentMouthState[var3]][var3];
				}
	
				gDrawImage(var19, var4 - var19.getWidth() / 2 + Field187[currentMouthState[var3]][var3], var5 - var17 - var19.getHeight() / 2 + Field188[currentMouthState[var3]][var3], 0);
			}

			if(blobEyeState[var3] > 0 || rand8() % 50 == var3) {
				if(blobEyeState[var3] > 0) {
					int var10002 = blobEyeState[var3]--;
					return;
				}
	
				blobEyeState[var3] = 3;
			}
		}
	}
	
	public static final void Method157() {
		int var0 = Field158;
		Field158 = Field191;
		Field191 = var0;
		var0 = levelCurrentGrabberFlags;
		levelCurrentGrabberFlags = Field193;
		Field193 = var0;
		var0 = Field159;
		Field159 = Field192;
		Field192 = var0;
		var0 = levelPlayerRotationRate;
		levelPlayerRotationRate = Field194;
		Field194 = var0;
		var0 = levelPlayerAngle;
		levelPlayerAngle = Field195;
		Field195 = var0;
	}
	
	public static final void initExplosives() {
		if(imgFuse == null) {
			imgFuse = loadImage("fuse.pim", "fuse.ppl");
		}
	
		if(imgsBomb == null) {
			imgsBomb = new Image[2];
			imgsBomb[0] = loadImage("bombbase.pim", "bombbase.ppl");
			imgsBomb[1] = loadImage("bombbutton.pim", "bombbutton.ppl");
		}
	}
	
	public static final void renderBombDispenser(int var0, int var1) {
		int var2 = levelAlignX(var0, var1);
		int var3 = levelAlignY(var0, var1);
		if(levelBombStartTicks + 200 > levelTicks) {
			gDrawImage(imgsBomb[1], var2 - 9, var3 - 11 - 2, 0);
		} else {
			gDrawImage(imgsBomb[1], var2 - 9, var3 - 11 - 5, 0);
		}
	
		gDrawImage(imgsBomb[0], var2 - 14, var3 - 11, 0);
	}
	
	public static final void levelInitHooks(int var0) {
		levelNumHooks = var0;
		levelHookID1 = new int[var0];
		levelHookID2 = new int[var0];
		levelHookType = new int[var0];
		levelHookExtent = new int[var0];
		levelHookExtentSquare = new int[var0];
		levelHookStretchConstant = new int[var0];
		levelHookIsActive = new boolean[var0];
		Field205 = new boolean[var0];
	}
	
	public static final void levelSetHook(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7) {
		levelHookID1[var0] = var1;
		levelHookID2[var0] = var2;
		levelHookType[var0] = var3;
		levelHookExtent[var0] = var4;
		levelHookExtentSquare[var0] = lp32Mul(var4, var4);
		levelHookStretchConstant[var0] = var5;
		levelHookIsActive[var0] = var7;
		Field205[var0] = var6;
	}
	
	public static final void levelDetachHooks(int var0) {
		for(int var1 = 0; var1 < levelNumHooks; var1++) {
			if(levelHookIsActive[var1] && (levelHookID1[var1] == var0 || levelHookID2[var1] == var0)) {
				levelHookIsActive[var1] = false;
				if(levelHookID2[16] == levelHookID1[var1] || levelHookID2[16] == levelHookID2[var1]) {
					Method152();
				}
			}
		}
	}
	
	public static final void levelUpdateHooks() {
		for(int var0 = 0; var0 < levelNumHooks; var0++) {
			if(levelHookIsActive[var0]) {
				if(levelHookType[var0] == 0) {
					levelCircleX[levelHookID2[var0]] = levelCircleX[levelHookID1[var0]];
				} else if(levelHookType[var0] == 1) {
					levelCircleY[levelHookID2[var0]] = levelCircleY[levelHookID1[var0]];
				} else if(levelHookType[var0] == 6) {
					if(levelCircleY[levelHookID1[var0]] > levelCircleY[levelHookID2[var0]] - levelHookExtent[var0]) {
						levelCircleY[levelHookID1[var0]] = levelCircleY[levelHookID2[var0]] - levelHookExtent[var0];
					}
				} else if(levelHookType[var0] == 5) {
					if(levelCircleX[levelHookID1[var0]] > levelCircleX[levelHookID2[var0]] - levelHookExtent[var0]) {
						levelCircleX[levelHookID1[var0]] = levelCircleX[levelHookID2[var0]] - levelHookExtent[var0];
					}
				} else {
					onCircleTouchCircle(levelHookID1[var0], levelHookID2[var0], levelHookExtent[var0], levelHookStretchConstant[var0], levelHookType[var0], false);
				}
			}
		}
	}
	
	public static final void onCircleTouchCircle(int var0, int var1, int var2, int var3, int var4, boolean var5) {
		int var6 = levelCircleX[var1] - levelCircleX[var0];
		int var7 = levelCircleY[var1] - levelCircleY[var0];
		int var8 = vectorMagnitude(var6, var7, var2);
		if(var4 != 4 || var8 >= var2) {
			if(var4 != 3 || var8 <= var2) {
				if(var8 >= 70000) {
					int var9 = levelCircle4ByWeight[var0];
					int var10 = levelCircle4ByWeight[var1];
					int var11 = lp32Div(var8 - var2, lp32Mul(var8, var9 + var10));
					int var12 = lp32Mul(var3, lp32Mul(var6, var11));
					int var13 = lp32Mul(var3, lp32Mul(var7, var11));
					if(levelCircleHasPhysics[var0]) {
						int[] var10000 = levelCircleX;
						var10000[var0] += lp32Mul(var12, var9);
						var10000 = levelCircleY;
						var10000[var0] += lp32Mul(var13, var9);
					}
	
					if(levelCircleHasPhysics[var1]) {
						int[] var15 = levelCircleX;
						var15[var1] -= lp32Mul(var12, var10);
						var15 = levelCircleY;
						var15[var1] -= lp32Mul(var13, var10);
					}
	
					if(var5) {
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
		int[] var1 = levelHookID1;
		levelHookID1 = swappedLevelHookID1;
		swappedLevelHookID1 = var1;
		var1 = levelHookID2;
		levelHookID2 = swappedLevelHookID2;
		swappedLevelHookID2 = var1;
		var1 = levelHookType;
		levelHookType = Field210;
		Field210 = var1;
		var1 = levelHookExtent;
		levelHookExtent = Field211;
		Field211 = var1;
		var1 = levelHookExtentSquare;
		levelHookExtentSquare = swappedLevelHookExtentSquare;
		swappedLevelHookExtentSquare = var1;
		var1 = levelHookStretchConstant;
		levelHookStretchConstant = Field213;
		Field213 = var1;
		boolean[] var2 = Field205;
		Field205 = Field214;
		Field214 = var2;
		var2 = levelHookIsActive;
		levelHookIsActive = Field215;
		Field215 = var2;
	}
	
	public static final void Method166(int var0) {
		Field216 = new int[var0];
		Field217 = new int[var0];
		Field218 = new int[var0];
		Field219 = new int[var0];
		Field220 = new int[var0];
		Field221 = new int[var0];
	}
	
	public static final void Method167(int var0, int var1, int var2, int var3, int var4) {
		Field216[var0] = var1;
		Field217[var0] = levelCircleX[var1] - var3;
		Field218[var0] = levelCircleX[var1] + var3;
		if(var4 == 0) {
			var4 = 1;
		}
	
		Field219[var0] = var4;
		Field220[var0] = var4;
		Field221[var0] = var2;
	}
	
	public static final void Method168(int var0) {
		for(int var1 = 0; var1 < Field216.length; var1++) {
			if(Field221[var1] == var0) {
				Field219[var1] = 400000;
				levelCircle4ByWeight[Field216[var1]] = 52000;
	
				for(int var2 = 0; var2 < levelNumHooks; var2++) {
					if((levelHookID1[var2] == Field216[var1] || levelHookID2[var2] == Field216[var1]) && !Field205[var2]) {
						levelHookIsActive[var2] = false;
					}
				}
			}
		}
	}
	
	public static final void Method169() {
		if(Field216.length > 0) {
			for(int var0 = 0; var0 < Field216.length; var0++) {
				int var1 = Field216[var0];
				if(Field219[var0] > 0) {
					int var9 = Field219[var0]--;
					if(Field219[var0] == 0) {
						int var4 = rand8() % 17;
						int[] var7 = levelCircleX;
						var7[var1] += var4 - 10 << 14;
						var7 = levelCircleY;
						var7[var1] += var4 - 8 << 14;
						if(levelCircleX[var1] < Field217[var0]) {
							Field219[var0] = -Field220[var0];
	
							for(int var5 = 0; var5 < Field216.length; var5++) {
								if(Field221[var0] == Field221[var5]) {
									Field219[var5] = Field219[var0];
								}
							}
						} else {
							Field219[var0] = Field220[var0];
						}
					}
				} else {
					int var10002 = Field219[var0]++;
					if(Field219[var0] >= 0) {
						int var2 = rand8() % 17;
						int[] var10000 = levelCircleX;
						var10000[var1] += var2 - 6 << 14;
						var10000 = levelCircleY;
						var10000[var1] += var2 - 8 << 14;
						if(levelCircleX[var1] > Field218[var0]) {
							Field219[var0] = Field220[var0];
	
							for(int var3 = 0; var3 < Field216.length; var3++) {
								if(Field221[var0] == Field221[var3]) {
									Field219[var3] = Field219[var0];
								}
							}
						} else {
							Field219[var0] = -Field220[var0];
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
		for(int var1 = 0; var1 < levelExpanderIndex.length; var1++) {
			if(levelExpanderIndex[var1] == var0 && levelExpanderIsTouchActivated[var1]) {
				levelExpanderIsVacant[var1] = true;
			}
		}
	}
	
	public static final void updateExpanders() {
		levelExpanderTicks++;
	
		for(int var0 = 0; var0 < levelExpanderIndex.length; var0++) {
			if(levelCircleRadius[levelExpanderIndex[var0]] > levelExpanderMinRadius[var0]) {
				int[] var10000 = levelCircleRadius;
				int var10001 = levelExpanderIndex[var0];
				var10000[var10001] -= (levelCircleRadius[levelExpanderIndex[var0]] - levelExpanderMinRadius[var0]) / 20;
			}
	
			if(levelExpanderIsVacant[var0] || levelExpanderDelay[var0] > 0 && levelExpanderTicks % levelExpanderDelay[var0] == 0) {
				levelCircleRadius[levelExpanderIndex[var0]] = levelExpanderMinRadius[var0] + levelExpanderExtent[var0];
				levelExpanderIsVacant[var0] = false;
			}
		}
	}
	
	public static final void initGrabber() {
		if(imgGrabber == null && levelGrabberIDs != null && levelGrabberIDs.length > 0) {
			imgGrabber = loadImage("grabber.pim", "grabber.ppl");
		}
	}
	
	public static final void initGrabberIDs(int var0) {
		levelGrabberIDs = new int[var0];
	}
	
	public static final void setGrabberID(int var0, int var1) {
		levelGrabberIDs[var0] = var1;
	}
	
	public static final void Method177() {
		for(int var0 = 0; var0 < levelGrabberIDs.length; var0++) {
			int var1 = levelGrabberIDs[var0];
			if(var1 != levelHookID2[16] && levelCircleFlags[var1] != 0) {
				levelRenderImage(imgGrabber, levelCircleX[var1], levelCircleY[var1]);
			}
		}
	}
	
	public static final void Method178() {
		int var0 = 0;
		levelPingShardIDs = new int[3];
		levelPingEnemyCurrentIndex = 3;
		int[] var1 = new int[3];
		levelPingEnemyIDs = new int[3][];
		levelPingEnemyIDs[0] = new int[] {-1, -1, -1, -1, -1, -1};
		levelPingEnemyIDs[1] = new int[] {-1, -1, -1, -1, -1, -1};
		levelPingEnemyIDs[2] = new int[] {-1, -1, -1, -1, -1, -1};
	
		for(int var2 = 0; var2 < levelNumCircles; var2++) {
			if(levelCircleType[var2] == 13) {
				levelPingCircleID = var2;
			}
	
			if(levelCircleType[var2] == 15) {
				levelPingShardIDs[var0++] = var2;
			}
	
			if((levelCircleFlags[var2] & 0x80) > 0 && levelCircleType[var2] == 7) {
				int var3 = (levelCircleY[var2] >> 16) / 100 - 1;
				levelPingEnemyIDs[var3][var1[var3]++] = var2;
				int[] var10000 = levelCircleFlags;
				var10000[var2] &= -2;
	
				for(int var4 = 0; var4 < levelEnemyIndex.length; var4++) {
					if(levelEnemyIndex[var4] == var2) {
						levelEnemyIsAlive[var4] = false;
					}
				}
			}
		}
	
		if(var0 == 1) {
			int[] var5 = new int[1];
			var5[0] = levelPingShardIDs[0];
			levelPingShardIDs = var5;
		}
	}
	
	public static final void updatePing() {
		/* process every Ping's shard, and if the X axis of the shard is within
		 * Ping's circle radius, we'll reduce the shard radius. */
		for(int i = 0; i < levelPingShardIDs.length; i++) {
			if(levelCircleRadius[levelPingShardIDs[i]] > 0 &&
					levelCircleX[levelPingShardIDs[i]] > levelCircleX[levelPingCircleID] - levelCircleRadius[levelPingCircleID] &&
					levelCircleX[levelPingShardIDs[i]] < levelCircleX[levelPingCircleID] + levelCircleRadius[levelPingCircleID]) {

				levelCircleRadius[levelPingShardIDs[i]] -= 0x4000;
				if(levelCircleRadius[levelPingShardIDs[i]] <= 0) {
					levelCircleFlags[levelPingShardIDs[i]] = 0;
					levelDetachHooks(levelPingShardIDs[i]);
				}
			}
		}
	
		/* levelPingHitTimeout = 100 if Ping is hit (levelPingHit()). */
		if(levelPingHitTimeout > 0) {
			levelPingHitTimeout--;
			if(levelPingHitTimeout == 85) {
				levelPingInplace();
				startDialogue("ping_hit.bms", levelCircleX[levelPingCircleID], levelCircleY[levelPingCircleID] - 2 * levelCircleRadius[levelPingCircleID] / 3);
				return;
			}
		} else if(levelPingEnemyCurrentIndex < 0) {
			isLevelComplete = true;
		}
	}
	
	public static final void levelPingInplace() {
		if(levelPingHitTimeout == 0) {
			startDialogue("ping_inplace.bms", levelCircleX[levelPingCircleID], levelCircleY[levelPingCircleID] - 2 * levelCircleRadius[levelPingCircleID] / 3);
		}
	
		/* Ping spawns enemies. */
		levelPingEnemyCurrentIndex--;
		if(levelPingEnemyCurrentIndex >= 0) {
			for(int i = 0; i < levelPingEnemyIDs[levelPingEnemyCurrentIndex].length; i++) {
				int enemyID = levelPingEnemyIDs[levelPingEnemyCurrentIndex][i];

				if(enemyID != -1) {
					levelCircleFlags[enemyID] |= 1;
	
					for(int k = 0; k < levelEnemyIndex.length; k++) {
						if(levelEnemyIndex[k] == enemyID) {
							levelEnemyIsAlive[k] = true;
						}
					}
				}
			}
		}
	}
	
	public static final void Method181() {
		setGammaColor(160, 32, 240);
	
		for(int var0 = 0; var0 < levelPingShardIDs.length; var0++) {
			levelRenderCircle(levelCircleX[levelPingShardIDs[var0]], levelCircleY[levelPingShardIDs[var0]], levelCircleRadius[levelPingShardIDs[var0]]);
		}
	
		levelRenderCircle(levelCircleX[levelPingCircleID], levelCircleY[levelPingCircleID], levelCircleRadius[levelPingCircleID]);
		int var7 = 2 * levelCircleRadius[levelPingCircleID] / 5;
		int var1 = levelCircleX[levelPingCircleID];
		int var2 = levelCircleY[levelPingCircleID] - 7 * levelCircleRadius[levelPingCircleID] / 10;
		int var3 = levelCircleRadius[levelPingCircleID] / 5;
	
		for(int var4 = -1; var4 < 2; var4++) {
			setGammaColor(0);
			levelRenderCircle(var1 + var4 * var7, var2, var3);
			if(var4 < levelPingEnemyCurrentIndex) {
				setGammaColor(0xffffff);
				levelRenderCircle(var1 + var4 * var7, var2, var3);
				setGammaColor(0);
				int var5 = Method182(var1 + var4 * var7, levelCameraX, 0x960000, 3 * var3 / 4);
				int var6 = Method182(var2, levelCameraY, 0x640000, 3 * var3 / 4);
				levelRenderCircle(var1 + var4 * var7 + var5, var2 + var6, var3 / 3);
			} else {
				setGammaColor(160, 32, 240);
				levelRenderCircle(var1 + var4 * var7, var2, var3);
			}
		}
	}
	
	public static final int Method182(int var0, int var1, int var2, int var3) {
		int var4 = 90 * (var0 - (var1 - var2)) / var2;
		if(var4 < 0) {
			var4 = 0;
		}
	
		if(var4 >= 180) {
			var4 = 179;
		}
	
		return var3 / 1500 * cos1000[var4];
	}
	
	public static final void levelPingHit() {
		if(levelPingHitTimeout <= 0) {
			levelPingHitTimeout = 100;
			if(levelPingEnemyCurrentIndex < 1) {
				levelPingEnemyCurrentIndex = -1;
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
		Field258 = new boolean[var0];
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
		Field258[var0] = (levelCircleFlags[var1] & 1) == 0;
	}
	
	public static final void updateEnemies() {
		levelEnemyTicks++;
	
		for(int var0 = 0; var0 < levelEnemyIndex.length; var0++) {
			if(levelEnemyIsAlive[var0]) {
				int var1 = levelEnemyTicks + levelEnemyMovementStartTicks[var0];
				if(levelEnemyCanJump[var0] && var1 % levelEnemyFramesUntilJump[var0] == 0) {
					int[] var10000 = levelCircleY;
					int var10001 = levelEnemyIndex[var0];
					var10000[var10001] -= levelEnemyJumpHeight[var0];
				}
	
				if(levelEnemyIsMovingInX[var0]) {
					if(var1 % levelEnemyFramesUntilXMovement[var0] == 0) {
						int[] var2 = levelCircleX;
						int var3 = levelEnemyIndex[var0];
						var2[var3] += levelEnemyXMovementDirection[var0] * levelEnemyXMovementSpeed[var0];
						if(levelEnemyIsJitteringX[var0] && rand8() % 50 == 49) {
							levelEnemyXMovementDirection[var0] = -levelEnemyXMovementDirection[var0];
						}
					}
	
					if(levelCircleX[levelEnemyIndex[var0]] > levelEnemyCenterX[var0] + levelEnemyXMovementDistance[var0]) {
						levelEnemyXMovementDirection[var0] = -1;
					}
	
					if(levelCircleX[levelEnemyIndex[var0]] < levelEnemyCenterX[var0] - levelEnemyXMovementDistance[var0]) {
						levelEnemyXMovementDirection[var0] = 1;
					}
				}
			}
		}
	}
	
	public static final boolean isEnemyStatic(int var0) {
		for(int var1 = 0; var1 < levelEnemyIndex.length; var1++) {
			if(levelEnemyIndex[var1] == var0) {
				if(!levelEnemyCanJump[var1] && !levelEnemyIsMovingInX[var1]) {
					return true;
				}
	
				return false;
			}
		}
	
		return false;
	}
	
	public static final boolean isEnemyAlive(int var0) {
		for(int var1 = 0; var1 < levelEnemyIndex.length; var1++) {
			if(levelEnemyIndex[var1] == var0) {
				return levelEnemyIsAlive[var1];
			}
		}
	
		return false;
	}
	
	public static final void killEnemy(int var0) {
		for(int var1 = 0; var1 < levelEnemyIndex.length; var1++) {
			if(levelEnemyIndex[var1] == var0 && levelEnemyIsAlive[var1]) {
				levelEnemyIsAlive[var1] = false;
				int[] var10000 = levelCircleFlags;
				var10000[var0] |= 1;
				if(!levelEnemyCanJump[var1] && !levelEnemyIsMovingInX[var1]) {
					Method168(levelEnemyMovementStartTicks[var1]);
				}
	
				return;
			}
		}
	}
	
	public static final void initEnemies() {
		if(levelEnemyIndex != null && levelEnemyIndex.length != 0) {
			if(imgsSpider == null) {
				imgsSpider = new Image[2];
				imgsSpider[0] = loadImage("spiderC.pim", "spiderC.ppl");
				imgsSpider[1] = loadImage("spiderD.pim", "spiderD.ppl");
			}
	
			if(imgsBirdLeft == null) {
				imgsBirdLeft = new Image[6];
				imgsBirdRight = new Image[6];
	
				for(int var0 = 0; var0 <= 5; var0++) {
					imgsBirdLeft[var0] = loadImage("bird_left" + var0 + ".pim", "bird_left" + var0 + ".ppl");
					imgsBirdRight[var0] = loadImage("bird_right" + var0 + ".pim", "bird_right" + var0 + ".ppl");
				}
			}
	
			if(imgsPointyRoll == null) {
				imgsPointyRoll = new Image[3];
	
				for(int var1 = 0; var1 < 3; var1++) {
					imgsPointyRoll[var1] = loadImage("pointy_roll_" + var1 + ".pim", "pointy_roll_" + var1 + ".ppl");
				}
			}
	
			if(imgsPointyEyes == null) {
				imgsPointyEyes = new Image[2];
	
				for(int var2 = 0; var2 < 2; var2++) {
					imgsPointyEyes[var2] = loadImage("pointy_eyes_" + var2 + ".pim", "pointy_eyes_" + var2 + ".ppl");
				}
			}
	
			if(imgsPointyMouth == null) {
				imgsPointyMouth = new Image[2];
	
				for(int var3 = 0; var3 < 2; var3++) {
					imgsPointyMouth[var3] = loadImage("pointy_mouth_" + var3 + ".pim", "pointy_mouth_" + var3 + ".ppl");
				}
			}
		}
	}
	
	public static final void Method191() {
		for(int var0 = 0; var0 < levelEnemyIndex.length; var0++) {
			renderEnemies(levelEnemyIndex[var0], var0);
		}
	}
	
	public static final void renderEnemies(int var0, int var1) {
		if(levelCircleRadius[var0] >= 30000) {
			int var2 = levelAlignX(levelCircleX[var0], levelCircleY[var0]);
			int var3 = levelAlignY(levelCircleX[var0], levelCircleY[var0]);
			if(Field258[var1]) {
				int[] var11 = levelEnemyFrame;
	
				for(var11[var1] += (levelCircleX[var0] - levelCirclePrevX[var0]) / 7000; levelEnemyFrame[var1] >= 500; var11[var1] -= 500) {
					var11 = levelEnemyFrame;
				}
	
				while(levelEnemyFrame[var1] < 0) {
					var11 = levelEnemyFrame;
					var11[var1] += 500;
				}
	
				if(!levelEnemyIsAlive[var1]) {
					levelEnemyFrame[var1] = 510;
				}
	
				Image var8;
				if(levelCirclePrevX[var0] > levelCircleX[var0]) {
					var8 = imgsBirdLeft[levelEnemyFrame[var1] / 100];
				} else {
					var8 = imgsBirdRight[levelEnemyFrame[var1] / 100];
				}
	
				gDrawImage(var8, var2 - var8.getWidth() / 2, var3 - var8.getHeight() / 2, 0);
			} else if(!levelEnemyCanJump[var1] && !levelEnemyIsMovingInX[var1]) {
				Image var7 = imgsSpider[0];
				if(!levelEnemyIsAlive[var1]) {
					var7 = imgsSpider[1];
				}
	
				gDrawImage(var7, var2 - var7.getWidth() / 2, var3 - var7.getHeight() / 2, 0);
			} else {
				int[] var10000 = levelEnemyFrame;
	
				for(var10000[var1] += (levelCircleX[var0] - levelCirclePrevX[var0]) / 3000; levelEnemyFrame[var1] >= 300; var10000[var1] -= 300) {
					var10000 = levelEnemyFrame;
				}
	
				while(levelEnemyFrame[var1] < 0) {
					var10000 = levelEnemyFrame;
					var10000[var1] += 300;
				}
	
				Image var4 = imgsPointyRoll[levelEnemyFrame[var1] / 100];
				if(var4 == null) {
					var4 = imgsPointyRoll[0];
				}
	
				Image var5;
				if(levelCircleY[var0] < levelCircleY[0]) {
					var5 = imgsPointyEyes[0];
				} else {
					var5 = imgsPointyEyes[1];
				}
	
				Image var6 = imgsPointyMouth[0];
				if(var0 == lastAttackingEnemy && levelPlayerHitTicks > 0) {
					var6 = imgsPointyMouth[1];
					levelRenderCritterShock(levelCircleX[1 + levelPlayerHitTicks % 4], levelCircleY[1 + levelPlayerHitTicks % 4], levelCircleX[var0], levelCircleY[var0]);
				}
	
				gDrawImage(var4, var2 - var4.getWidth() / 2, var3 - var4.getHeight() / 2, 0);
				if(levelEnemyIsAlive[var1]) {
					gDrawImage(var5, var2 + 0 - var5.getWidth() / 2, var3 + -6 - var5.getHeight() / 2, 0);
					gDrawImage(var6, var2 + 0 - var6.getWidth() / 2, var3 + 3 - var6.getHeight() / 2, 0);
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
		for(int var0 = 0; var0 < levelAnimRectIndex.length; var0++) {
			int var1 = levelAnimRectIndex[var0];
			if(levelAnimRectCurrentIteration[var0] > 0) {
				int var10002 = levelAnimRectCurrentIteration[var0]--;
			} else if(levelAnimRectCurrentIteration[var0] < -1) {
				int var5 = levelAnimRectCurrentIteration[var0]++;
			} else if(levelAnimRectCurrentIteration[var0] == 0) {
				int[] var10000 = levelRectHWidth;
				var10000[var1] += levelAnimRectSpeed[var0];
				var10000 = levelRectHHeight;
				var10000[var1] += levelAnimRectSpeed[var0];
				if(levelRectHWidth[var1] > levelAnimRectMaxHWidth[var0]) {
					levelRectHWidth[var1] = levelAnimRectMaxHWidth[var0];
				}
	
				if(levelRectHHeight[var1] > levelAnimRectMaxHHeight[var0]) {
					levelRectHHeight[var1] = levelAnimRectMaxHHeight[var0];
				}
	
				if(levelRectHWidth[var1] == levelAnimRectMaxHWidth[var0] && levelRectHHeight[var1] == levelAnimRectMaxHHeight[var0]) {
					levelAnimRectCurrentIteration[var0] = -levelAnimRectIterations[var0] - 1;
				}
			} else if(levelAnimRectCurrentIteration[var0] == -1) {
				int[] var3 = levelRectHWidth;
				var3[var1] -= levelAnimRectSpeed[var0];
				var3 = levelRectHHeight;
				var3[var1] -= levelAnimRectSpeed[var0];
				if(levelRectHWidth[var1] < levelAnimRectMinHWidth[var0]) {
					levelRectHWidth[var1] = levelAnimRectMinHWidth[var0];
				}
	
				if(levelRectHHeight[var1] < levelAnimRectMinHHeight[var0]) {
					levelRectHHeight[var1] = levelAnimRectMinHHeight[var0];
				}
	
				if(levelRectHWidth[var1] == levelAnimRectMinHWidth[var0] && levelRectHHeight[var1] == levelAnimRectMinHHeight[var0]) {
					levelAnimRectCurrentIteration[var0] = levelAnimRectIterations[var0];
				}
			}
		}
	}
	
	public static final void updateCollisionCircleRect() {
		for(int var0 = 0; var0 < levelNumCircles; var0++) {
			if(levelCircleHasPhysics[var0]) {
				for(int var1 = 0; var1 < levelNumRects; var1++) {
					if(levelCircleY[var0] + levelCircleRadius[var0] > levelRectY[var1] - levelRectHHeight[var1] && levelCircleY[var0] - levelCircleRadius[var0] < levelRectY[var1] + levelRectHHeight[var1] && levelCircleX[var0] + levelCircleRadius[var0] > levelRectX[var1] - levelRectHWidth[var1] && levelCircleX[var0] - levelCircleRadius[var0] < levelRectX[var1] + levelRectHWidth[var1]) {
						onCircleTouchRect(var0, var1);
					}
				}
			}
		}
	}
	
	public static final void onCircleTouchRect(int var0, int var1) {
		if(var0 == 2) {
			levelPlayerOnCircle = true;
		}
	
		int var2 = levelRectX[var1] + levelRectHWidth[var1] + levelCircleRadius[var0];
		if(levelCircleX[var0] < levelRectX[var1]) {
			var2 = levelRectX[var1] - levelRectHWidth[var1] - levelCircleRadius[var0];
		}
	
		int var3 = levelRectY[var1] + levelRectHHeight[var1] + levelCircleRadius[var0];
		if(levelCircleY[var0] < levelRectY[var1]) {
			var3 = levelRectY[var1] - levelRectHHeight[var1] - levelCircleRadius[var0];
		}
	
		int var4 = levelCircleX[var0] - var2;
		if(var4 < 0) {
			var4 = -var4;
		}
	
		int var5 = levelCircleY[var0] - var3;
		if(var5 < 0) {
			var5 = -var5;
		}
	
		if(var4 < var5) {
			int[] var6 = levelCircleX;
			var6[var0] += (var2 - levelCircleX[var0]) / 2;
		} else {
			int[] var10000 = levelCircleY;
			var10000[var0] += (var3 - levelCircleY[var0]) / 2;
		}
	}
	
	public static final void levelRenderRects() {
		setGammaColor(0, 0, 0);
	
		for(int var0 = 0; var0 < levelNumRects; var0++) {
			levelDrawRect(levelRectX[var0], levelRectY[var0], levelRectHWidth[var0], levelRectHHeight[var0]);
		}
	
		setGammaColor(levelColor);
	
		for(int var1 = 0; var1 < levelNumRects; var1++) {
			levelDrawRect(levelRectX[var1], levelRectY[var1], levelRectHWidth[var1], levelRectHHeight[var1]);
		}
	}
	
	public static final void levelLoadRects() {
		levelInitRects(sReadU16());
	
		for(int var0 = 0; var0 < levelNumRects; var0++) {
			levelAddRect(var0, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, sReadU16() << 16, sReadU16() << 16);
		}
	}
	
	public static final void swapLevelRectData() {
		int var0 = levelNumRects;
		levelNumRects = Field273;
		Field273 = var0;
		int[] var1 = levelRectX;
		levelRectX = Field274;
		Field274 = var1;
		var1 = levelRectY;
		levelRectY = Field275;
		Field275 = var1;
		var1 = levelRectHWidth;
		levelRectHWidth = Field276;
		Field276 = var1;
		var1 = levelRectHHeight;
		levelRectHHeight = Field277;
		Field277 = var1;
	}
	
	public static final void initRocket() {
		if(imgRocket == null) {
			imgRocket = loadImage("rocket.pim", "rocket.ppl");
		}
	
		if(imgsVertFlame == null) {
			imgsVertFlame = new Image[2];
			imgsVertFlame[0] = loadImage("vertFlame0.pim", "vertFlame0.ppl");
			imgsVertFlame[1] = loadImage("vertFlame1.pim", "vertFlame1.ppl");
		}
	}
	
	public static final void renderRocket(int var0, int var1, int var2) {
		initRocket();
		gDrawImage(imgRocket, var0, var1, 0);
		if(var2 >= 0) {
			gDrawImage(imgsVertFlame[var2 & 1], var0 + 36, var1 + 33, 0);
		}
	}
	
	public static final void initSigns() {
		if(mustLoadSigns && imgsSign == null) {
			imgsSign = new Image[7];
	
			for(int var0 = 0; var0 < imgsSign.length; var0++) {
				imgsSign[var0] = loadImage("sign" + var0 + ".pim", "sign" + var0 + ".ppl");
			}
		}
	}
	
	public static final void initLevelSigns(int var0) {
		levelSignX = new int[var0];
		levelSignY = new int[var0];
		levelSignTypes = new int[var0];
		if(var0 > 0) {
			mustLoadSigns = true;
		}
	}
	
	public static final void levelSetSign(int var0, int var1, int var2, int var3) {
		if(mirrored && var3 == 2) {
			var3 = 3;
		}
	
		if(mirrored && var3 == 3) {
			var3 = 2;
		}
	
		levelSignX[var0] = var1;
		levelSignY[var0] = var2;
		levelSignTypes[var0] = var3;
	}
	
	public static final void renderSigns() {
		if(imgsSign == null) {
			initSigns();
		}
	
		for(int var0 = 0; var0 < levelSignX.length; var0++) {
			Image var1 = imgsSign[levelSignTypes[var0]];
			if(var1 != null) {
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
		if(mirrored) {
			int var10 = var7 - var6;
			short var11 = 180;
			if(var6 > 180) {
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
		for(int var0 = 0; var0 < levelSpawnStartIndex.length; var0++) {
			int var10002 = levelSpawnCurrentTick[var0]++;
			if(levelSpawnCurrentTick[var0] > levelSpawnEndTick[var0]) {
				int var1 = levelSpawnCurrentIndex[var0];
				int[] var10000 = levelCircleRadius;
				var10000[var1] -= levelCircleRadius[var1] / 4;
				if(levelCircleRadius[var1] < 0x010000) {
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
					if(levelSpawnCurrentIndex[var0] >= levelSpawnEndIndex[var0]) {
						levelSpawnCurrentIndex[var0] = levelSpawnStartIndex[var0];
					}
				}
			}
		}
	}
	
	public static final void levelUpdateAll() {
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
		if(var6 == 3 && currentLevelLoaded != -2) {
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
		if(levelCircleX[0] < 0 || levelCircleX[0] > levelWidth || levelCircleY[0] > levelHeight) {
			levelPlayerHealth = 0;
		}
	
		for(int var0 = 0; var0 < 5; var0++) {
			if(levelCircleX[var0] - levelCirclePrevX[var0] < 0x2000 && levelCircleX[var0] - levelCirclePrevX[var0] > -0x2000) {
				levelCircleX[var0] = levelCirclePrevX[var0];
			}
		}
	
		for(int var4 = 0; var4 < 5; var4++) {
			if(levelCircleX[var4] - levelCirclePrevX[var4] > 0x0f0000) {
				levelCircleX[var4] = levelCirclePrevX[var4] + 0x0f0000;
			}
	
			if(levelCircleX[var4] - levelCirclePrevX[var4] < -0x0f0000) {
				levelCircleX[var4] = levelCirclePrevX[var4] - 0x0f0000;
			}
	
			if(levelCircleY[var4] - levelCirclePrevY[var4] > 0x0f0000) {
				levelCircleY[var4] = levelCirclePrevY[var4] + 0x0f0000;
			}
	
			if(levelCircleY[var4] - levelCirclePrevY[var4] < -0x0f0000) {
				levelCircleY[var4] = levelCirclePrevY[var4] - 0x0f0000;
			}
		}
	
		for(int var5 = 0; var5 < levelNumCircles; var5++) {
			if(levelCircleHasPhysics[var5]) {
				int var1 = levelCircleX[var5];
				int var2 = levelCircleY[var5];
				int var3 = xLossRate;
				if(var5 < 5 && levelCurrentGrabberFlags >= 0) {
					var3 = 63000;
				}
	
				int[] var10000 = levelCircleX;
				var10000[var5] += lp32Mul(var3, levelCircleX[var5] - levelCirclePrevX[var5]);
				var10000 = levelCircleY;
				var10000[var5] += lp32Mul(var3, levelCircleY[var5] - levelCirclePrevY[var5]);
				if((levelCircleFlags[var5] & 1) > 0) {
					var10000 = levelCircleY;
					var10000[var5] += Field321;
				}
	
				levelCirclePrevX[var5] = var1;
				levelCirclePrevY[var5] = var2;
			}
		}
	}
	
	public static final void updatePlayerCollision() {
		levelPlayerOnCircle = false;
	
		for(int var0 = 0; var0 < 5; var0++) {
			for(int var1 = var0 + 1; var1 < levelNumCircles; var1++) {
				if((levelCircleFlags[var1] & 2) > 0 && levelCircleX[var0] - levelCircleRadius[var0] < levelCircleX[var1] + levelCircleRadius[var1] && levelCircleX[var0] + levelCircleRadius[var0] > levelCircleX[var1] - levelCircleRadius[var1] && levelCircleY[var0] - levelCircleRadius[var0] < levelCircleY[var1] + levelCircleRadius[var1] && levelCircleY[var0] + levelCircleRadius[var0] > levelCircleY[var1] - levelCircleRadius[var1]) {
					if((levelCircleFlags[var1] & 0x20) > 0) {
						levelDialogueToDisplay = var1;
					} else {
						onCircleTouchCircle(var0, var1, levelCircleRadius[var0] + levelCircleRadius[var1], 55000, 3, true);
					}
				}
			}
		}
	
		for(int var2 = 5; var2 < levelNumCircles; var2++) {
			if(levelCircleHasPhysics[var2]) {
				for(int var3 = var2 + 1; var3 < levelNumCircles; var3++) {
					if((levelCircleFlags[var3] & 2) > 0 && levelCircleX[var2] - levelCircleRadius[var2] < levelCircleX[var3] + levelCircleRadius[var3] && levelCircleX[var2] + levelCircleRadius[var2] > levelCircleX[var3] - levelCircleRadius[var3] && levelCircleY[var2] - levelCircleRadius[var2] < levelCircleY[var3] + levelCircleRadius[var3] && levelCircleY[var2] + levelCircleRadius[var2] > levelCircleY[var3] - levelCircleRadius[var3] && (levelCircleFlags[var3] & 0x20) == 0) {
						if((levelCircleFlags[var3] & 0x10) > 0) {
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
	
	public static final void levelObtainIDs() {
		levelIDExpanders = levelGetCircleIDs(0x4000);
		levelIDDestructibles = levelGetCircleIDs(0x0100);
		levelIDFoam = levelGetCircleIDs(0x2000);
		levelIDGoals = levelGetCircleIDs(0x0010);
		int var0 = 0;
	
		for(int var1 = 0; var1 < levelNumCircles; var1++) {
			if((levelCircleFlags[var1] & 0x80) > 0 && levelCircleType[var1] != 7) {
				var0++;
			}
		}
	
		levelIDBombDispenser = new short[var0];
		var0 = 0;
	
		for(short var5 = 0; var5 < levelNumCircles; var5++) {
			if((levelCircleFlags[var5] & 0x80) > 0 && levelCircleType[var5] != 7) {
				levelIDBombDispenser[var0++] = var5;
			}
		}
	
		var0 = 0;
	
		for(int var6 = 0; var6 < levelNumCircles; var6++) {
			if((levelCircleFlags[var6] & 0x0004) > 0 && !levelCircleHasPhysics[var6] && (levelCircleFlags[var6] & 0x0008) == 0 && (levelCircleFlags[var6] & 0x2000) == 0 && (levelCircleFlags[var6] & 0x0100) == 0) {
				var0++;
			}
		}
	
		levelIDPlatforms = new short[var0];
		var0 = 0;
	
		for(short var7 = 0; var7 < levelNumCircles; var7++) {
			if((levelCircleFlags[var7] & 0x0004) > 0 && !levelCircleHasPhysics[var7] && (levelCircleFlags[var7] & 0x0008) == 0 && (levelCircleFlags[var7] & 0x2000) == 0 && (levelCircleFlags[var7] & 0x0100) == 0) {
				levelIDPlatforms[var0++] = var7;
			}
		}
	
		levelIDRadioactive = levelGetCircleIDs(8);
	}
	
	public static final short[] levelGetCircleIDs(int var0) {
		int var1 = 0;
	
		for(short var2 = 0; var2 < levelNumCircles; var2++) {
			if((levelCircleFlags[var2] & var0) == var0) {
				var1++;
			}
		}
	
		short[] var5 = new short[var1];
		var1 = 0;
	
		for(short var3 = 0; var3 < levelNumCircles; var3++) {
			if((levelCircleFlags[var3] & var0) == var0) {
				var5[var1++] = var3;
			}
		}
	
		return var5;
	}
	
	public static final void loadLevel(int var0) {
		boolean var1 = true;
		isLevelComplete = false;
		if(var0 >= 25) {
			var0 = 24;
		}
	
		if(var0 == -2) {
			swapLevelData(0);
			sOpenFile("ship.bin");
			var1 = false;
		} else if(var0 == -1) {
			swapLevelData(-1);
			sOpenFile("playground" + level * 4 / 25 + ".bin");
			var1 = false;
		} else if(var0 < 10) {
			swapLevelData(1);
			sOpenFile("level.00" + var0 + ".bin");
		} else {
			swapLevelData(1);
			sOpenFile("level.0" + var0 + ".bin");
		}
	
		isFinalLevel = false;
		if(var0 >= 0) {
			isFinalLevel = var0 == 24;
		}
	
		currentLevelLoaded = var0;
		levelWidth = sReadU16() << 16;
		levelHeight = sReadU16() << 16;
		setWeatherAndBackgroundIDs(sRead8(), sRead8());
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
		if(!var1 && var0 != -1) {
			var6 += 5;
		}
	
		levelInitCircles(var6 + var4 + 1);
		if(var1) {
			var5 = levelAddHitbox(0, var2, var3, 0x140000);
		} else {
			var5 = levelAddHitbox(0, var2, var3, 0x0d0000);
		}
	
		levelSetCircle(var5++, 0, 0, 0xa0000, 30, 0, 0, false);
		if(!var1 && var0 != -1) {
			var5 = levelAddHitbox(var5, var2 + 0x370000, var3, 0x140000);
		}
	
		var6 = var5;
	
		for(int var7 = 0; var7 < var4; var7++) {
			levelSetCircle(var5++, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, sReadU16() << 16, sReadU16(), sReadU16(), sRead8(), sRead8() == 1);
		}
	
		if(var0 == -2) {
			initGrabberIDs(0);
		} else {
			int var16 = 0;
	
			for(int var8 = 0; var8 < levelNumCircles; var8++) {
				if(levelCircleType[var8] == 3) {
					var16++;
				}
			}
	
			initGrabberIDs(var16);
			int var18 = 0;
	
			for(int var9 = 0; var9 < levelNumCircles; var9++) {
				if(levelCircleType[var9] == 3) {
					setGrabberID(var18++, var9);
				}
			}
		}
	
		levelBombObjectID = 5;
		levelLoadRects();
		int var17 = sReadU16();
		int var19 = 0x140000;
		if(!var1) {
			var19 = 0x0c0000;
		}
	
		levelInitHooks(17 + var17);
		int var35 = var5 = levelSetPlayerHook(0, 0, var19);
		var5++;
		levelSetHook(var35, 0, 0, 4, 65536, 65500, false, false);
	
		for(int var20 = 0; var20 < var17; var20++) {
			levelSetHook(var5++, var6 + sReadU16(), var6 + sReadU16(), sRead8(), sReadU16() << 16, sReadU16(), sRead8() == 1, true);
		}
	
		byte var21;
		levelInitSpawners(var21 = sRead8());
	
		for(int var10 = 0; var10 < var21; var10++) {
			levelSetSpawner(var10, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, var6 + sRead8(), sRead8(), sReadU16(), sReadU16(), sReadU16(), sReadU16(), sReadU16());
		}
	
		initEnemies(var21 = sRead8());
	
		for(int var27 = 0; var27 < var21; var27++) {
			levelSetEnemy(var27, var6 + sReadU8(), sRead8() == 1, sReadU16(), sReadU16() << 14, sRead8() == 1, sReadU16(), sReadU16() << 14, sReadU16() << 16, sRead8() == 1, sReadU16());
		}
	
		Method166(var21 = sRead8());
	
		for(int var28 = 0; var28 < var21; var28++) {
			Method167(var28, var6 + sReadU8(), sRead8(), sReadU16() << 16, sReadU16());
		}
	
		levelInitExpanders(var21 = sRead8());
	
		for(int var29 = 0; var29 < var21; var29++) {
			levelSetExpander(var29, var6 + sReadU8(), sRead8() == 1, sReadU16(), sReadU16() << 16);
		}
	
		levelInitAnimatedRects(var21 = sRead8());
	
		for(int var30 = 0; var30 < var21; var30++) {
			levelSetAnimRect(var30, var30, sReadU16() << 16, sReadU16() << 16, sReadU16() << 8, sReadU16(), sReadU16());
		}
	
		initLevelSigns(var21 = sRead8());
	
		for(int var31 = 0; var31 < var21; var31++) {
			levelSetSign(var31, levelAlignToGameMirror(sReadU16() << 16), sReadU16() << 16, sRead8());
		}
	
		initPlayer(var1);
		levelDeathTicks = 70;
		Field321 = 40000;
		levelBombExplodeTicks = 0;
		levelBombStartTicks = -10000;
		levelPlayerHitTicks = 0;
		levelCameraZoom = 72;
		xLossRate = 62000;
		levelSetCamera(levelCircleX[0], levelCircleY[0], 0);
		if(var0 >= 0) {
			for(int var32 = 0; var32 < 5; var32++) {
				int[] var37 = levelCircleY;
				var37[var32] -= 0x750000;
				var37 = levelCirclePrevY;
				var37[var32] -= 0x750000;
			}
		}
	
		levelIntroTicks = 0;
		if(var0 == -2) {
			Method221();
		} else {
			levelObtainIDs();
		}
	
		if(isFinalLevel) {
			Method178();
		}
	}
	
	public static final void Method221() {
		for(int var0 = 0; var0 < levelNumCircles; var0++) {
			if((levelCircleFlags[var0] & 4) > 0 && levelCircleHasPhysics[var0]) {
				switch(levelCircleType[var0]) {
					case 7:
						levelCircleFlags[var0] = 0;
						levelCircleHasPhysics[var0] = false;
				}
			}
		}
	}
	
	public static final int levelAlignToGameMirror(int var0) {
		if(mirrored) {
			return var0 > 0 && var0 < 10000 ? (levelWidth >> 16) - var0 : levelWidth - var0;
		} else {
			return var0;
		}
	}
	
	public static final int levelAddHitbox(int var0, int var1, int var2, int var3) {
		byte var4 = 3;
		if(var0 > 0) {
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
		if(var0 != activeSwapKey) {
			activeSwapKey = var0;
			int var1 = levelWidth;
			levelWidth = swappedLevelWidth;
			swappedLevelWidth = var1;
			var1 = levelHeight;
			levelHeight = swappedLevelHeight;
			swappedLevelHeight = var1;
			var1 = Field321;
			Field321 = Field351;
			Field351 = var1;
			var1 = xLossRate;
			xLossRate = Field352;
			Field352 = var1;
			boolean var2 = levelPlayerOnCircle;
			levelPlayerOnCircle = Field353;
			Field353 = var2;
			var2 = isLevelComplete;
			isLevelComplete = Field354;
			Field354 = var2;
			swapLevelCircleData();
			swapLevelRectData();
			swapLevelHookData();
			swapCameraData();
			Method157();
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
		if(var0 == currentSoundID && var0 != -1) {
			if(var0 >= 0) {
				if(!isSoundActive(currentSoundID, -1)) {
					playSound(currentSoundID, -1);
				}
			}
		} else {
			queueAllSoundsForCleanup();
			queueSoundCleanup();
			currentSoundID = var0;
			if(var0 >= 0) {
				playSound(currentSoundID, -1);
			}
		}
	}
	
	public static final void playCurrentSound() {
		if(currentSoundID >= 0 && !paused && !isSoundActive(currentSoundID, -1)) {
			playSound(currentSoundID, -1);
		}
	}
	
	public static final void playCurrentBackgroundMusic() {
		switch(levelBackgroundID) {
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
	
	public static final void setWeatherAndBackgroundIDs(int var0, int var1) {
		boolean var2 = false;
	
		for(int var3 = 0; var3 < knownBackgrounds.length; var3++) {
			if(knownBackgrounds[var3] == var1) {
				var2 = true;
			}
		}
	
		if(!var2) {
			var1 = knownBackgrounds[level % knownBackgrounds.length];
			if(var0 > 0) {
				var0 = fallbackWeathers[var1];
			}
		}
	
		levelWeatherID = var0;
		levelBackgroundID = var1;
	}
	
	public static final void loadBackground(int var0) {
		imgsBackground = new Image[5];
	
		for(int var1 = 0; var1 < 5; var1++) {
			imgsBackground[var1] = loadImage("bg_" + var0 + "_" + var1 + ".pim", "bg_" + var0 + "_" + var1 + ".ppl");
		}
	
		currentBackgroundHasObjects = backgroundHasObjects[var0];
		activeBackgroundPattern = backgroundPatterns[var0];
		Field358 = Field369[var0];
		int var3 = getBackgroundTotalHeight(activeBackgroundPattern);
		int var2 = getBackgroundTotalHeight(Field358) - imgsBackground[Field358[0][0]].getHeight();
		valOfZero = 0;
		Field361 = -(var3 - (128 - var2));
		Field362 = var3;
		Field363 = 128 - var2;
		currentLoadedBackgroundID = var0;
	}
	
	public static final void renderBackground() {
		if(currentLevelLoaded >= 0) {
			if(imgsBackground == null || levelBackgroundID != currentLoadedBackgroundID) {
				loadBackground(levelBackgroundID);
			}
	
			if(!currentBackgroundHasObjects) {
				int var5 = valOfZero + (Field361 - valOfZero) * (levelCameraY >> 16) / (levelHeight >> 16);
				int var7 = -((getBackgroundTotalWidth(activeBackgroundPattern) - 128) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackground(activeBackgroundPattern, var7, var5, false);
			} else {
				int var3 = valOfZero + (Field361 - valOfZero) * (levelCameraY >> 16) / (levelHeight >> 16);
				int var1 = -((getBackgroundTotalWidth(activeBackgroundPattern) - 128) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackground(activeBackgroundPattern, var1, var3, false);
				var3 = Field362 + (Field363 - Field362) * (levelCameraY >> 16) / (levelHeight >> 16);
				var1 = -((getBackgroundTotalWidth(Field358) - 128) * (levelCameraX >> 16)) / (levelWidth >> 16);
				renderBackground(Field358, var1, var3, true);
			}
		} else {
			setGammaColor(0xdcc86d);
			gFillRect(0, 0, 128, 128);
			setGammaColor(0);
	
			for(int var0 = 0; var0 < 128; var0 += 40) {
				gDrawLine(0, var0, 128, var0);
			}
	
			for(int var2 = 0; var2 < 128; var2 += 40) {
				gDrawLine(var2, 0, var2, 128);
			}
		}
	}
	
	public static final void renderBackground(int[][] var0, int var1, int var2, boolean var3) {
		int var4 = 0;
		if(var3) {
			int var5 = var1;
	
			for(int var6 = 0; var6 < var0[0].length; var6++) {
				Image var7 = imgsBackground[var0[0][var6]];
				if(var5 < 128 && var5 > -var7.getWidth() && var2 > 0 && var2 < 128 + var7.getHeight()) {
					gDrawImage(var7, var5, var2 - var7.getHeight(), 0);
				}
	
				var5 += var7.getWidth();
			}
	
			var4++;
		}
	
		while(var4 < var0.length) {
			int var8 = var1;
			Image var9 = null;
	
			for(int var10 = 0; var10 < var0[var4].length; var10++) {
				var9 = imgsBackground[var0[var4][var10]];
				if(var8 < 128 && var8 > -var9.getWidth() && var2 < 128 && var2 > -var9.getHeight()) {
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
	
		for(int var2 = 0; var2 < var0[0].length; var2++) {
			var1 += imgsBackground[var0[0][var2]].getWidth();
		}
	
		return var1;
	}
	
	public static final int getBackgroundTotalHeight(int[][] var0) {
		int var1 = 0;
	
		for(int var2 = 0; var2 < var0.length; var2++) {
			var1 += imgsBackground[var0[var2][0]].getHeight();
		}
	
		return var1;
	}
	
	public static final void levelSetCamera(int var0, int var1, int var2) {
		if(activeSwapKey == 0) {
			var1 = levelHeight / 2;
			if(var0 < 0x578000) {
				var0 = 0x578000;
			}
	
			if(var0 > levelWidth - 0x578000) {
				var0 = levelWidth - 0x578000;
			}
		}
	
		levelCameraX = var0;
		levelCameraY = var1;
		levelCameraIntX = var0 >> 16;
		levelCameraIntY = var1 >> 16;
	
		for(levelCameraAngle = var2; levelCameraAngle > 359; levelCameraAngle -= 360) {
		}
	
		while(levelCameraAngle < 0) {
			levelCameraAngle += 360;
		}
	
		levelCameraZoom = 72;
	}
	
	public static final boolean levelCameraApproach(int var0, int var1, int var2) {
		return levelCameraApproach(var0, var1, var2, 0x1e0000);
	}
	
	public static final boolean levelCameraApproach(int var0, int var1, int var2, int var3) {
		if(activeSwapKey == 0) {
			var1 = levelHeight / 2;
		} else if(var1 > levelHeight - 0x578000) {
			var1 = levelHeight - 0x578000;
		}
	
		if(var0 < 0x578000) {
			var0 = 0x578000;
		}
	
		if(var0 > levelWidth - 0x578000) {
			var0 = levelWidth - 0x578000;
		}
	
		int var4 = var0 - levelCameraX;
		int var5 = var1 - levelCameraY;
		if(var4 > var3) {
			var4 = var3;
		}
	
		if(var4 < -var3) {
			var4 = -var3;
		}
	
		if(var5 > var3) {
			var5 = var3;
		}
	
		if(var5 < -var3) {
			var5 = -var3;
		}
	
		int var6 = var4 >> 17;
		while(var6 > 359) {
			var6 -= 360;
		}
	
		while(var6 < 0) {
			var6 += 360;
		}
	
		if((var6 >= 180 || levelCameraAngle >= 180) && (var6 <= 180 || levelCameraAngle <= 180)) {
			if(var6 > 180) {
				var6 -= 360;
			}
	
			if(levelCameraAngle > 180) {
				levelCameraAngle -= 360;
			}
	
			levelCameraAngle += (var6 - levelCameraAngle) / 5;
		} else {
			levelCameraAngle += (var6 - levelCameraAngle) / 5;
		}
	
		while(levelCameraAngle > 359) {
			levelCameraAngle -= 360;
		}
	
		while(levelCameraAngle < 0) {
			levelCameraAngle += 360;
		}
	
		if(activeSwapKey != 0) {
			int var7 = levelCircleX[0] - levelCirclePrevX[0];
			if(var7 < 0) {
				var7 = -var7;
			}
	
			var7 >>= 8;
			int var8 = 82 - var7 * 45 * 72 / 0x32000;
			if(var8 < 36) {
				var8 = 36;
			}
	
			int var9 = (var8 - levelCameraZoom) / 10;
			if(var9 <= 10 && var9 >= 10) {
				if(var9 > 0) {
					levelCameraZoom++;
				} else if(var9 < 0) {
					levelCameraZoom--;
				}
			} else {
				levelCameraZoom += (var8 - levelCameraZoom) / 10;
			}
		} else {
			levelCameraZoom = 72;
			levelCameraAngle = 0;
		}
	
		if(var4 < 0xa0000 && var4 > -0xa0000 && var5 < 0xa0000 && var5 > -0xa0000) {
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
		if(imgsFaces == null) {
			imgsFaces = new Image[5][];
			imgsFaces[0] = new Image[2];
	
			for(int i = 0; i < 2; i++) {
				imgsFaces[0][i] = loadImage("wiz" + i + ".pim", "wiz" + i + ".ppl");
			}
	
			imgsFaces[1] = new Image[2];
	
			for(int i = 0; i < 2; i++) {
				imgsFaces[1][i] = loadImage("waz" + i + ".pim", "waz" + i + ".ppl");
			}
	
			imgsFaces[2] = new Image[2];
	
			for(int i = 0; i < 2; i++) {
				imgsFaces[2][i] = loadImage("purple" + i + ".pim", "purple" + i + ".ppl");
			}
	
			imgsFaces[3] = new Image[2];
	
			for(int i = 0; i < 2; i++) {
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
		if(showDialogue == 1) {
			int var3 = 12 + currentSpeechNumLines * 13;
			currentSpeechStartMs -= (long)(400 * var2 / var3);
		}
	
		showDialogue = 1;
		currentSpeakerIteration = 0;
		currentSpeechIteration = 0;
		Field387 = false;
	}
	
	public static final void renderDialogueBox() {
		if(showDialogue == 1) {
			long currTime = millis();
			int var2 = (int)(currTime - currentSpeechStartMs);
			int boxHeight = 12 + currentSpeechNumLines * 13;
			if(currentSpeechIteration == 0 && var2 < 400) {
				boxHeight = boxHeight * var2 / 400;
			}
	
			gSetColor(0xffffff); // white
			gFillRect(0, 0, 128, boxHeight);
			gSetColor(0xece9d8); // light gray
			gDrawLine(0, boxHeight, 128, boxHeight);
			gSetColor(0xaca899); // darker gray
			gDrawLine(0, boxHeight + 1, 128, boxHeight + 1);
			gSetColor(0);
			gDrawLine(0, boxHeight + 2, 128, boxHeight + 2);

			if(currentSpeechIteration != 0 || var2 >= 400) {
				byte var4 = 0;
				boolean var5 = false;
				if(imgsFaces[currentSpeaker] != null) {
					Image var6 = imgsFaces[currentSpeaker][currentSpeakerIteration % imgsFaces[currentSpeaker].length];
					int var7 = (21 - var6.getWidth()) / 2;
					if(currentSpeaker == 0) {
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
	
				if(currTime > currentSpeechStartMs) {
					currentSpeechStartMs = currTime + 150L;
					currentSpeechIteration++;
	
					char var9;
					while(currentSpeechIteration < currentSpeech.length() && (var9 = currentSpeech.charAt(currentSpeechIteration)) != ' ' && var9 != '.') {
						currentSpeechIteration++;
					}
	
					if(currentSpeechIteration >= currentSpeech.length()) {
						Field387 = true;
						return;
					}
	
					currentSpeakerIteration++;
				}
			}
		}
	}
	
	public static final int numNewLines(String var0) {
		return getNewLineIndexes(99, var0, 1).length;
	}
	
	public static final String[] strSplitLines(String var0, int var1) {
		int[] var2 = getNewLineIndexes(99, var0, var1);
		String[] var3 = new String[var2.length];
	
		for(int var4 = 0; var4 < var2.length; var4++) {
			if(var4 == var2.length - 1) {
				var3[var4] = var0.substring(var2[var4]);
			} else {
				var3[var4] = var0.substring(var2[var4], var2[var4 + 1]);
			}
		}
	
		return var3;
	}
	
	public static final void _sceneLevelRun() {
		if(levelDialogueToDisplay > -1) {
			levelDisplayDialogue(levelDialogueToDisplay);
			levelDialogueToDisplay = -1;
		} else {
			if(Field389 >= -1) {
				loadLevel(Field389);
				Field389 = -2;
				if(currentLevelLoaded < 0) {
					levelIntroTicks = 120;
				}
	
				if(!isDejaVuMessageShown) {
					if(currentLevelLoaded >= 0) {
						startDialogue("respawn.bms", levelCircleX[0], levelCircleY[0]);
					}
	
					isDejaVuMessageShown = true;
					Field404 = true;
					return;
				}
	
				Field404 = true;
			}
	
			if(levelIntroTicks >= 50) {
				updateAll();
				levelTicks++;
			}
	
			if(levelPlayerHealth < 1) {
				if(levelDeathTicks == 70) {
					vibrate(640);
					Field404 = false;
					levelCircleRadius[0] = 0x20000;
					levelCircleRadius[2] = 0x10000;
					levelCircleRadius[3] = 0x10000;
					levelCircleRadius[4] = 0x10000;
					levelCircleFlags[2] = 1;
					levelCircleX[2] = levelCircleX[0];
					levelCircleY[2] = levelCircleY[0] + 0xa0000;
	
					for(int var0 = 0; var0 < 2; var0++) {
						for(int var1 = 0; var1 < 2; var1++) {
							levelHookType[8 + 2 * var0 + var1] = 3;
							levelHookStretchConstant[8 + 2 * var0 + var1] = 500;
							levelHookExtent[8 + 2 * var0 + var1] = 0x280000;
						}
					}
	
					if(levelCurrentGrabberFlags >= 0) {
						levelHookIsActive[16] = false;
						levelCircleFlags[levelHookID2[16]] = levelCurrentGrabberFlags;
						levelCurrentGrabberFlags = -1;
						Field176 = levelHookID2[16];
					}
				}
	
				levelCameraApproach(levelCircleX[0] + 10 * (levelCircleX[0] - levelCirclePrevX[0]), levelCircleY[0] + 10 * (levelCircleY[0] - levelCirclePrevY[0]), 0, 0x140000);
				levelPlayerRed -= 3;
				if(levelPlayerRed < 0) {
					levelPlayerRed = 0;
				}
	
				levelDeathTicks--;
				if(levelDeathTicks <= 0) {
					Field389 = currentLevelLoaded;
				}
			}
	
			int var2 = -1;
			if(levelIntroTicks < 100) {
				var2 = softkeyPressed(-1, 3);
			} else if(currentLevelLoaded == -1) {
				var2 = softkeyPressed(-1, 4);
			} else if(levelPlayerHealth > 0) {
				var2 = softkeyPressed(-1, 0);
			}
	
			if(var2 == 4) {
				Field391 = true;
			}
	
			if(var2 == 3) {
				levelIntroTicks = 120;
			}
	
			if(var2 == 0) {
				softkeyPressed(-1, -1);
				Field427 = true;
				gamma = 100;
				swapLevelData(0);
				setNewState(2, 0);
			} else {
				if(Field404 && levelIntroTicks >= 100) {
					updatePlayerControls();
					levelCameraApproach(levelCircleX[0] + 10 * (levelCircleX[0] - levelCirclePrevX[0]), levelCircleY[0] + 10 * (levelCircleY[0] - levelCirclePrevY[0]), 0, 3276800);
				}
	
				if(levelCircleHasPhysics[levelBombObjectID]) {
					if(levelTicks > levelBombStartTicks + 100) {
						levelSetCamera(levelCircleX[levelBombObjectID], levelCircleY[levelBombObjectID], 0);
					}
	
					if(levelBombExplodeTicks == 0 && levelTicks > levelBombStartTicks + 130) {
						levelBombExplodeTicks = 5;
						vibrate(40);
					}
				}
	
				if(levelBombExplodeTicks > 0) {
					levelSetCamera(levelCircleX[levelBombObjectID], levelCircleY[levelBombObjectID], 0);
					levelBombExplodeTicks--;
					levelCircle4ByWeight[levelBombObjectID] = 0;
					if(levelBombExplodeTicks > 0) {
						int[] var10000 = levelCircleRadius;
						int var10001 = levelBombObjectID;
						var10000[var10001] += 0x0f0000;
					} else {
						levelCircleHasPhysics[levelBombObjectID] = false;
						levelCircleX[levelBombObjectID] = -0xc80000;
					}
				}
	
				levelRender(true);
				if(levelIntroTicks < 100) {
					int var3;
					if(levelIntroTicks < 80) {
						var3 = -51 + levelIntroTicks * 51 / 50;
					} else {
						var3 = -51 + (100 - levelIntroTicks) * 51 / 20;
					}
	
					if(var3 > 0) {
						var3 = 0;
					}
	
					if(levelIntroTicks == 80) {
						vibrate(140);
					}
	
					if(levelIntroTicks < 80) {
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
		if((isKeyNewlyHeld(0x1a0) || isKeyNewlyHeld(1) && !isKeyNewlyHeld(0x800)) && (levelPlayerOnCircle || levelCurrentGrabberFlags >= 0)) {
			if(levelCurrentGrabberFlags >= 0) {
				Method152();
			}
	
			if(levelPlayerOnCircle) {
				Field173 = 3;
			}
		}
	
		if(isKeyHeld(0x110)) {
			levelPlayerDirection = 1;
			if(levelCurrentGrabberFlags >= 0 && !levelPlayerOnCircle) {
				if(levelCircleX[0] >= levelCirclePrevX[0]) {
					if(levelCircleX[0] - levelCirclePrevX[0] < 0x10000) {
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
				if(levelPlayerOnCircle) {
					levelPlayerRotationRate += 3;
				}
			}
		}
	
		if(isKeyHeld(0x88)) {
			levelPlayerDirection = -1;
			if(levelCurrentGrabberFlags >= 0 && !levelPlayerOnCircle) {
				if(levelCircleX[0] <= levelCirclePrevX[0]) {
					if(levelCirclePrevX[0] - levelCircleX[0] < 0x10000) {
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
				if(levelPlayerOnCircle) {
					levelPlayerRotationRate -= 3;
				}
			}
		}
	
		if(isKeyReleased(-1)) {
			if(isKeyReleased(cheatCodeLevelComplete[iterCheatLevelComplete])) {
				iterCheatLevelComplete++;
				if(iterCheatLevelComplete >= cheatCodeLevelComplete.length) {
					isLevelComplete = true;
					iterCheatLevelComplete = 0;
				}
			} else {
				iterCheatLevelComplete = 0;
			}
	
			if(isKeyReleased(cheatCodeInvincibility[iterCheatInvincibility])) {
				iterCheatInvincibility++;
				if(iterCheatInvincibility >= cheatCodeInvincibility.length) {
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
		if(levelWeatherID == 1 && activeSwapKey == 1) {
			gamma = 100;
			if(rand16() % 100 == 50) {
				gamma = 0;
			}
		}
	
		renderBackground();
	
		for(int var3 = 0; var3 < levelIDGoals.length; var3++) {
			short var4 = levelIDGoals[var3];
			setGammaColor(levelColorGoal);
			levelRenderCircle(levelCircleX[var4], levelCircleY[var4], levelCircleRadius[var4]);
			setGammaColor(255, 255, 255);
			if(levelCircleRadius[var4] > levelPortalFilledRadius) {
				levelRenderCircleOutline(levelCircleX[var4], levelCircleY[var4], levelPortalFilledRadius);
			} else {
				int var5 = levelCircleRadius[var4] / 4;
				int var6 = (int)((long)var5 * (var1 % 700L)) / 700;
	
				for(int var7 = 0; var7 < 4; var7++) {
					levelRenderCircleOutline(levelCircleX[var4], levelCircleY[var4], levelCircleRadius[var4] - var6 - var7 * var5);
				}
			}
		}
	
		renderSigns();
		if(levelFinishY > 0) {
			renderFacePurple(levelFinishX, levelFinishY);
		}
	
		if(var0) {
			levelRenderPlayer();
		}
	
		Method191();
	
		for(int var8 = 0; var8 < levelNumCircles; var8++) {
			if((levelCircleFlags[var8] & 4) > 0 && levelCircleHasPhysics[var8] && (levelCircleFlags[var8] & 0x2000) == 0 && (levelCircleFlags[var8] & 8) == 0 && var8 != levelBombObjectID) {
				setGammaColor(0, 0, 0);
				levelRenderCircle(levelCircleX[var8], levelCircleY[var8], levelCircleRadius[var8]);
				setGammaColor(levelColorMovable);
				levelRenderCircle(levelCircleX[var8], levelCircleY[var8], levelCircleRadius[var8]);
			}
		}
	
		if(levelCircleHasPhysics[levelBombObjectID]) {
			setGammaColor(0, 0, 0);
			levelRenderCircle(levelCircleX[levelBombObjectID], levelCircleY[levelBombObjectID], levelCircleRadius[levelBombObjectID]);
			if(levelBombStartTicks != -1 && levelTicks > levelBombNextFlashTick) {
				if(levelBombFlashed) {
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
	
			levelRenderCircle(levelCircleX[levelBombObjectID], levelCircleY[levelBombObjectID], levelCircleRadius[levelBombObjectID]);
			levelRenderImage(imgFuse, levelCircleX[levelBombObjectID], levelCircleY[levelBombObjectID] - levelCircleRadius[levelBombObjectID]);
		}
	
		levelRenderCircleIDs(levelIDPlatforms, 0);
		levelRenderRects();
		levelRenderCircleIDs(levelIDPlatforms, levelColor);
		levelRenderCircleIDs(levelIDFoam, 0);
		levelRenderCircleIDs(levelIDFoam, 0xffffff);
		levelRenderCircleIDs(levelIDDestructibles, 0);
		levelRenderCircleIDs(levelIDDestructibles, levelColorDestructible);
		int var9 = (int)(var1 % 1600L);
		if(var9 > 800) {
			var9 = 1600 - var9;
		}
	
		levelRenderCircleIDs(levelIDRadioactive, 0);
		levelRenderCircleIDs(levelIDRadioactive, (levelRAMinRed + var9 * (levelRAMaxRed - levelRAMinRed) / 800 << 16) + (levelRAMinGreen + var9 * (levelRAMaxGreen - levelRAMinGreen) / 800 << 8) + levelRAMinBlue + var9 * (levelRAMaxBlue - levelRAMinBlue) / 800);
		levelRenderCircleIDs(levelIDExpanders, 0);
		levelRenderCircleIDs(levelIDExpanders, levelColorBouncer);
		setGammaColor(0, 0, 0);
	
		for(int var10 = 16; var10 < levelNumHooks; var10++) {
			if(levelHookIsActive[var10] && Field205[var10]) {
				levelRenderLine(levelCircleX[levelHookID1[var10]], levelCircleY[levelHookID1[var10]], levelCircleX[levelHookID2[var10]], levelCircleY[levelHookID2[var10]]);
			}
		}
	
		if(isFinalLevel) {
			Method181();
		}
	
		Method177();
	
		for(int var11 = 0; var11 < levelIDBombDispenser.length; var11++) {
			renderBombDispenser(levelCircleX[levelIDBombDispenser[var11]], levelCircleY[levelIDBombDispenser[var11]]);
		}
	
		if(levelWeatherID != 0 && activeSwapKey == 1) {
			if(levelWeatherID == 1) {
				renderRain();
			}
	
			if(levelWeatherID == 2) {
				levelRenderVolcanicDust();
			}
		}
	}
	
	public static final void levelRenderCircleIDs(short[] var0, int var1) {
		setGammaColor(var1);
	
		for(int var2 = 0; var2 < var0.length; var2++) {
			levelRenderCircle(levelCircleX[var0[var2]], levelCircleY[var0[var2]], levelCircleRadius[var0[var2]]);
		}
	}
	
	public static final void activateLevel(int var0) {
		loadLevel(var0);
	}
	
	public static final void updateAll() {
		if(levelHookIsActive[16]) {
			Field205[16] = true;
		}
	
		Method169();
		updateEnemies();
		updateExpanders();
		updateAnimRects();
		if(levelPlayerHitTicks > 0) {
			levelPlayerHitTicks--;
		}
	
		updateSpawners();
		levelUpdateAll();
		if(isFinalLevel) {
			updatePing();
		}
	}
	
	public static final void processPlayerCircles(int id1, int id2) {
		if(id1 == 2)
			levelPlayerOnCircle = true;

		/* destructive objects. */
		if((levelCircleFlags[id2] & 0x2000) > 0) {
			levelCircleRadius[id2] -= 0x10000;
			if(levelCircleRadius[id2] < 0x40000) {
				levelCircleRadius[id2] = 0;
				levelCircleFlags[id2] = 0;
			}
		}
	
		/* radioactive goo. */
		if((levelCircleFlags[id2] & 8) > 0 &&
				!isPlayerInvincible) {

			levelPlayerHealth -= 4000;
		}

		byte circleType = levelCircleType[id2];

		if(circleType != 0) {
			if(currentLevelLoaded != -2 &&
					activeSwapKey != 0) {

				/* grabber. */
				if(circleType == 3 &&
						levelCurrentGrabberFlags == -1 &&
						id2 != Field176) {

					levelHookIsActive[16] = true;
					levelHookID2[16] = id2;
					levelCurrentGrabberFlags = levelCircleFlags[id2];
					levelCircleFlags[id2] = 0;
				}
	
				/* vacant an expander. */
				if(circleType == 6) {
					setExpanderVacant(id2);
				}
	
				/* enemy hurts a player. */
				if(circleType == 7 &&
						levelPlayerHitTicks == 0 &&
						isEnemyAlive(id2)) {

					if(isEnemyStatic(id2) && id1 == 2) {
						if(levelCircleY[2] - levelCirclePrevY[2] > 120000) {
							killEnemy(id2);
						}
					} else {
						if(!isPlayerInvincible) {
							levelPlayerHealth -= 170;
						}
	
						lastAttackingEnemy = id2;
						if(levelPlayerHealth > 0) {
							levelPlayerHitTicks = 50;
							vibrate(240);
						}
					}
				}
	
				/* finish the level. */
				if(circleType == 8) {

					isLevelComplete = true;

				} else if(circleType >= 9 &&
						circleType <= 11 &&
						id1 == 2 &&
						levelBombStartTicks + 200 < levelTicks) {

					levelBombStartTicks = levelTicks;
					levelBombNextFlashTick = levelTicks + 33;

					levelBombFlashed = false;

					int dir = levelPlayerDirection;
					if(circleType == 10) {
						dir = -1;
					}
	
					if(circleType == 11) {
						dir = 1;
					}
	
					if(mirrored && circleType > 9) {
						dir *= -1;
					}
	
					levelSetCircle(levelBombObjectID, levelCircleX[id2] + dir * 0x140000, levelCircleY[id2], 0xa0000, 15, 71, 0, true);
					levelCircleX[levelBombObjectID] += dir * 0x30000;
				}
			} else {
				levelShipTouchedIconIndex = circleType;
			}
		}
	}
	
	public static final void processCircles(int id1, int id2) {
		if(levelPlayerHealth > 0) {

			if(id1 >= 5 && id2 >= 5) { // level circles.

				if((levelCircleFlags[id1] & 16) > 0 || (levelCircleFlags[id2] & 16) > 0) {

					/* check if a ball touches portal's circle hitbox, if so,
					 * fill the portal's radius with the ball's radius. */
					int swpID1 = id1;
					int swpID2 = id2;

					if((levelCircleFlags[id2] & 16) > 0) {
						swpID1 = id2;
						swpID2 = id1;
					}
	
					if(levelCircleFlags[swpID2] != 0) {
						levelCircleX[swpID2] = levelCircleX[swpID1];
						levelCircleY[swpID2] = levelCircleY[swpID1];
						levelCirclePrevX[swpID2] = levelCirclePrevX[swpID1];
						levelCirclePrevY[swpID2] = levelCirclePrevY[swpID1];
						levelDetachHooks(swpID2);

						levelCircleRadius[swpID2] -= 0x8000;
						levelPortalFilledRadius += 0x8000;

						if(levelPortalFilledRadius >= levelCircleRadius[swpID1]) {
							levelPortalFilledRadius = levelCircleRadius[swpID1];
							isLevelComplete = true;
						}
	
						if(levelCircleRadius[swpID2] <= 0) {
							levelCircleFlags[swpID2] = 0;
							levelCircleHasPhysics[swpID2] = false;
						}
					}
				}

				/* bomb destroys destructible objects. */
				if(levelBombExplodeTicks > 0 &&
						(id1 == levelBombObjectID &&
						 (levelCircleFlags[id2] & 0x100) > 0 ||
						 id2 == levelBombObjectID && (levelCircleFlags[id1] & 0x100) > 0)) {

					int circleID = id1;
					if(id1 == levelBombObjectID) {
						circleID = id2;
					}

					/* reduce 30% of the first circle radius when the explosion
					 * wave (the second circle) reaches the first circle. */
					levelCircleRadius[circleID] = levelCircleRadius[circleID] * 7 / 10;

					if(levelCircleRadius[circleID] < 0xa0000) {
						levelCircleRadius[circleID] = 0;
						levelCircleFlags[circleID] = 0;
					}
				}

				byte circleType1 = levelCircleType[id1];
				byte circleType2 = levelCircleType[id2];

				if(circleType1 != 0 || circleType2 != 0) {

					/* vacant an expander. */
					if(circleType1 == 6 || circleType2 == 6) {

						if(circleType1 == 6)
							setExpanderVacant(id1);
						else
							setExpanderVacant(id2);

					}

					/* check if enemy's circle hitbox touches radioactive goo or
					 * an explosion wave. */
					if((circleType1 == 7 || circleType2 == 7) &&
							((levelCircleFlags[id1] & 8) > 0 ||
							 (levelCircleFlags[id2] & 8) > 0 ||
							 levelBombExplodeTicks > 0 &&
							 (id1 == levelBombObjectID || id2 == levelBombObjectID))) {

						if(circleType1 == 7) {
							killEnemy(id1);
						} else {
							killEnemy(id2);
						}

					}
	
					/* a circle touches a finish circle. */
					if((circleType1 == 1 || circleType2 == 1) &&
							(circleType1 == 2 || circleType2 == 2)) {

						isLevelComplete = true;
					}
	
					/* a circle touches a radioactive circle. */
					if((circleType1 == 1 || circleType2 == 1) &&
							(circleType1 == 12 || circleType2 == 12)) {

						levelPlayerHealth -= 4000;
					}
	
					/* an explosion wave reaches Ping. */
					if(isFinalLevel) {

						if((circleType1 == 13 || circleType2 == 13) &&
								(circleType1 == 14 || circleType2 == 14)) {

							if(circleType1 == 14) {
								levelCircleFlags[id1] = 0;
							} else {
								levelCircleFlags[id2] = 0;
							}
	
							levelPingInplace();
						}
	
						if(levelBombExplodeTicks > 0 &&
								(id1 == levelBombObjectID && circleType2 == 13 ||
								 id2 == levelBombObjectID && circleType1 == 13)) {

							levelPingHit();
						}
					}
				}
			} else { // player circles.

				if(id1 >= 5) {
					processPlayerCircles(id2, id1);
				}
	
				if(id2 >= 5) {
					processPlayerCircles(id1, id2);
				}
			}
		}
	}
	
	public static final void loadGeneral() {
		gSetColor(255, 255, 255);
		gFillRect(0, 0, 128, 128);
		gfxFlush(); // white screen at the start
		loadGeneralUI();
		loadTrigonometric();
		loadRecordData();
		if(level > 0) {
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
		if(pauseScreenDraw) {
			pauseScreenDraw = false;
			gSetColor(0, 0, 0);
	
			for(int y = 0; y < 128; y++) {
				for(int x = y & 1; x < 128; x += 2) {
					gDrawLine(x, y, x, y);
				}
			}
	
			gFillRect(0, 109, 128, 128);
			softkeyPressed(2, -1);
		}
	}

	// funny circle again	
	public static final void renderThrobberNextFrame() {
		gSetColor(0, 0, 0);
		gFillRect(0, 0, 128, 128);
		throbberColorIndex--;
		if(throbberColorIndex < 0) {
			throbberColorIndex = throbberColors.length - 1;
		}
	
		throbberColors[throbberColorIndex] = 200;
	
		for(int i = 0; i < 12; i++) {
			gSetColor(throbberColors[i], 0, 0);
			throbberColors[i] = throbberColors[i] * 80 / 100;
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
		gammaBlackSet = false;
		if(r + g + b == 0) {
			gammaBlackSet = true;
		}
	
		if(gamma == 100) {
			gSetColor(r, g, b);
		} else {
			int gm = gamma;
			if(gm > 100) {
				gSetColor(r - r * (200 - gm) / 100, g - g * (200 - gm) / 100, b - b * (200 - gm) / 100);
			} else {
				gSetColor(r + (255 - r) * (100 - gm) / 100, g + (255 - g) * (100 - gm) / 100, b + (255 - b) * (100 - gm) / 100);
			}
		}
	}
	
	public static final void levelDrawRect(int var0, int var1, int var2, int var3) {
		if(gammaBlackSet) {
			int var4 = 0x20000;
			var4 = 100 * var4 / levelCameraZoom;
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
		if(var13 <= 128 || var8 <= 128) {
			if(var5 <= 128 || var7 <= 128) {
				if(var10 >= 0 || var6 >= 0) {
					if(var11 >= 0 || var9 >= 0) {
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
		if(var2 >= 35000) {
			int var3 = levelAlignX(var0, var1);
			int var4 = levelAlignY(var0, var1);
			var2 >>= 16;
			var2 = levelCameraZoom * var2 / 100;
			if(gammaBlackSet) {
				var2 += 2;
			}
	
			if(var3 > -var2 && var3 < 128 + var2 && var4 > -var2 && var4 < 128 + var2) {
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
		if(gammaBlackSet) {
			var2 += 2;
		}
	
		if(var3 > -var2 && var3 < 128 + var2 && var4 > -var2 && var4 < 128 + var2) {
			gDrawArc(var3 - var2, var4 - var2, var2 + var2, var2 + var2, 0, 360);
		}
	}
	
	public static final boolean isOutOfScreenBounds(int var0, int var1, int var2, int var3) {
		return var0 > 128 || var2 < 0 || var1 > 128 || var3 < 0;
	}
	
	public static final void initInsideShip() {
		if(imgWindow == null) {
			imgWindow = loadImage("window.pim", "window.ppl");
		}
	
		if(imgArrowLeft == null) {
			imgArrowLeft = loadImage("arrow_left.pim", "arrow_left.ppl");
		}
	
		if(imgArrowRight == null) {
			imgArrowRight = loadImage("arrow_right.pim", "arrow_right.ppl");
		}
	
		if(imgsInside == null) {
			imgsInside = new Image[3];
	
			for(int i = 0; i < 3; i++) {
				imgsInside[i] = loadImage("inside" + i + ".pim", "inside" + i + ".ppl");
			}
		}
	
		if(imgInsideLamp == null) {
			imgInsideLamp = loadImage("inside_lamp.pim", "inside_lamp.ppl");
		}
	
		if(imgsPiston == null) {
			imgsPiston = new Image[2];
			imgsPiston[0] = loadImage("piston_top.pim", "piston_top.ppl");
			imgsPiston[1] = loadImage("piston_bottom.pim", "piston_bottom.ppl");
		}
	
		if(imgDecors == null) {
			imgDecors = new Image[10];
	
			for(int i = 0; i < imgDecors.length; i++) {
				imgDecors[i] = loadImage("shipDecor" + i + ".pim", "shipDecor" + i + ".ppl");
			}
	
			decorBackground = loadFile16("decor_background.bin");
			decorForeground = loadFile16("decor_foreground.bin");
		}
	
		if(imgsShipIcons == null) {
			imgsShipIcons = new Image[9];
	
			for(int i = 0; i < 9; i++) {
				imgsShipIcons[i] = loadImage("shipicon" + i + ".pim", "shipicon" + i + ".ppl");
			}
		}
	
		if(imgsShipIconsOff == null) {
			imgsShipIconsOff = new Image[9];
	
			for(int i = 0; i < 9; i++) {
				imgsShipIconsOff[i] = loadImage("shipicon" + i + "b.pim", "shipicon" + i + "b.ppl");
			}
		}
	}
	
	public static final void initShip() {
		initSpace(2, 12, 92, 46);
		gamma = 100;
		loadLevel(-2);
		initInsideShip();
		Field321 = 40000;
		levelSetCamera(levelCircleX[0], levelHeight / 2, 0);
	}
	
	public static final int processMainMenuDialogue(int var0, boolean var1) {
		byte var2 = 0;
		switch(var0) {
			case 1:
				if(!var1) {
					startDialogue("start" + level + ".bms", levelCircleX[0], levelCircleY[0]);
					var2 = 1;
				} else {
					var2 = 6;
				}
				break;
			case 2:
				if(var1) {
					startDialogue("menu_sure_restart.bms", levelCircleX[0], levelCircleY[0], true);
					var2 = 3;
				} else {
					startDialogue("menu_no_restart.bms", levelCircleX[0], levelCircleY[0]);
				}
				break;
			case 3:
				if(var1) {
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
				if(audio) {
					startDialogue("menu_audio_off.bms", levelCircleX[0], levelCircleY[0]);
					queueAllSoundsForCleanup();
					queueSoundCleanup();
				} else {
					startDialogue("menu_audio_on.bms", levelCircleX[0], levelCircleY[0]);
				}
	
				audio = !audio;
				break;
			case 6:
				if(vibration) {
					startDialogue("menu_vibra_off.bms", levelCircleX[0], levelCircleY[0]);
				} else {
					startDialogue("menu_vibra_on.bms", levelCircleX[0], levelCircleY[0]);
				}
	
				vibration = !vibration;
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
		Field428 = true;
		renderShipInside();
		Field428 = false;
		if(!var0) {
			renderPlayable(6, 0xc80000, true);
		}
	
		levelRenderPlayer();
		renderShipDecor(decorForeground);
		renderShipArrows();
	}
	
	public static final void renderShipArrows() {
		if(levelCircleX[0] > 0x6e0000) {
			gDrawImage(imgArrowLeft, 4, 88, 0);
		}
	
		if(levelCircleX[0] < 0x41a0000) {
			gDrawImage(imgArrowRight, 107, 88, 0);
		}
	}
	
	public static final void renderShipDecor(short[] decorData) {
		if(decorData != null) {
			boolean b = false;
			int shiftX = 64 - levelCameraZoom * levelCameraIntX / 100;
	
			for(int i = 0; i < decorData.length; i += 3) {
				Image img = imgDecors[decorData[i]];
				if(img != null) {
					int x = shiftX + levelAlignToGameMirror(decorData[i + 1]) - img.getWidth() / 2;
					int y = img.getWidth();
					if(x > -y && x < 128 + y) {
						gDrawImage(img, x, -16 + decorData[i + 2] - img.getHeight() / 2, 0);
					}
				}
			}
		}
	}
	
	public static final void renderShipInside() {
		int var1;
		int var2;
		if((var2 = var1 = 64 - levelCameraZoom * levelCameraIntX / 100) > 0) {
			var2 = 0;
		}
	
		gDrawImage(imgsInside[0], var2, -16, 0);
		var2 += 28;
	
		for(int var3 = 0; var3 < 17; var3++) {
			if(var2 + 46 > 0 && var2 <= 128) {
				gDrawImage(imgsInside[1], var2, -16, 0);
			}
	
			var2 += 46;
		}
	
		if(var2 <= 128) {
			gDrawImage(imgsInside[2], var2, -16, 0);
		}
	
		int var4 = var1 + 72 * levelAlignToGameMirror(193) / 100;
		gDrawImage(imgInsideLamp, var4 - imgInsideLamp.getWidth() / 2, 2, 0);
		renderWindow(var1 + 299, 30, 0);
		renderWindow(var1 + 553, 30, 1);
		renderShipDecor(decorBackground);
		if(Field424 > 0) {
			int var12 = 2 + imgInsideLamp.getHeight() / 2;
			gSetColor(0xff0000);
			int var5 = Field424;
			if(var4 > -var5 && var4 < 128 + var5 && var12 > -var5 && var12 < 128 + var5) {
				for(int var6 = 0; var6 < 2; var6++) {
					for(int var7 = 20; var7 < 360; var7 += 90) {
						gDrawArc(var4 - var5, var12 - var5, var5 + var5, var5 + var5, var7, 50);
					}
	
					var5--;
				}
			}
	
			Field424 += 2;
			if(Field424 > 30) {
				Field424 = 5;
			}
		}
	
		for(int var13 = 0; var13 < levelNumCircles; var13++) {
			if((levelCircleFlags[var13] & 4) > 0 && levelCircleHasPhysics[var13] && levelCircleType[var13] > 0) {
				int var14 = 72 * (levelCircleX[var13] / 100) >> 16;
				int var15 = 72 * (levelCircleY[var13] / 100) >> 16;
				Image var8;
				gDrawImage(var8 = imgsPiston[1], var1 + var14 - var8.getWidth() / 2, -16 + var15 - var8.getHeight(), 0);
				gDrawImage(var8 = imgsPiston[0], var1 + var14 - var8.getWidth() / 2, -16, 0);
				int var9 = levelCircleType[var13] - 1;
				var8 = imgsShipIcons[var9];
				switch(var9) {
					case 4:
						if(!audio) {
							var8 = imgsShipIconsOff[var9];
						}
						break;
					case 5:
						if(!vibration) {
							var8 = imgsShipIconsOff[var9];
						}
				}
	
				gDrawImage(var8, var1 + var14 - var8.getWidth() / 2, -16 + var15 - var8.getHeight() / 2, 0);
				if(Field428 && var1 + var14 > -100 && var1 + var14 < 228) {
					String var10 = textTableShip[var9];
					if(Field427) {
						var10 = textTableShipPause[var9];
					}
	
					renderText(var1 + var14 - calcTextWidth(var10, 3) / 2, 89, var10, 3);
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
		if(var0 >= -var3 && var0 <= 128) {
			gDrawImage(imgWindow, var0, var1, 0);
		}
	
		gSetClip(0, 0, 128, 128);
	}
	
	public static final void renderSplash() {
		if(imgSplashShip == null) {
			imgSplashShip = loadImage("outside.pim", "outside.ppl");
		}
	
		if(imgsFlame == null) {
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
		spaceMapShowIcons = true;
		spaceMapBeaconX = var0;
		spaceMapBeaconY = var1;
		spaceMapBeaconRadius = 1;
		if(var2 >= 0) {
			if(imgPlanet == null) {
				imgPlanet = loadImage("planet.pim", "planet.ppl");
			}
	
			Field439 = true;
		} else {
			Field439 = false;
		}
	
		if(imgShip == null) {
			imgShip = loadImage("ship.pim", "ship.ppl");
		}
	
		if(imgPointer == null) {
			imgPointer = loadImage("pointer.pim", "pointer.ppl");
		}
	}
	
	public static final void spaceMapSetCameraToBeacon() {
		spaceMapSetCameraInstant(spaceMapBeaconX, spaceMapBeaconY);
	}
	
	public static final void spaceMapSetCameraInstant(int x, int y) {
		spaceMapCameraX = x << 8;
		spaceMapCameraY = y << 8;
		spaceMapTargetCameraX = spaceMapCameraX;
		spaceMapTargetCameraY = spaceMapCameraY;
	}
	
	public static final void spaceMapSetTargetCameraToBeacon() {
		spaceMapSetTargetCamera(spaceMapBeaconX, spaceMapBeaconY);
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

		if(spaceMapShowIcons) {
			gDrawImage(imgPointer, posX - 16, posY - 58 - 2, 0);
			gDrawImage(imgShip, posX - imgShip.getWidth() / 2, posY - 58 + (33 - imgShip.getHeight()) / 2 - 2, 0);
		}
	
		posX = spaceMapBeaconX + camX + 64;
		posY = spaceMapBeaconY + camY + 64;
		renderStar(posX, posY);

		if(spaceMapBeaconRadius > 0) {
			gSetColor(0xff0000);
			int rad = spaceMapBeaconRadius;

			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 20, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 110, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 200, 50);
			gDrawArc(posX - rad, posY - rad, rad * 2, rad * 2, 290, 50);

			if(spaceMapBeaconRadius++ > 20) {
				spaceMapBeaconRadius = 1;
			}
		}
	
		if(Field439) {
			gDrawImage(imgPointer, posX - 16, posY - 58 - 2, 0);
			gDrawImage(imgPlanet, posX - imgPlanet.getWidth() / 2, posY - 58 + (33 - imgPlanet.getHeight()) / 2 - 2, 0);
		}
	}
	
	public static final void renderBeamAnimation(int step) {
		updateAll();
		renderShipInside();

		if(step < 100) {
			gamma = step;
			levelCameraZoom = step * 72 / 100;

			if(levelCameraZoom == 0) {
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
		Field444 = levelCircleX[0];
		Field445 = levelCircleY[0];
	
		for(int var0 = 0; var0 < 5; var0++) {
			levelCirclePrevY[var0] = levelCircleY[var0];
		}
	
		if(levelCurrentGrabberFlags >= 0) {
			Method152();
		}
	
		playJingleComplete();
		Field446 = levelCameraZoom;
		levelCompleteTicks = 150;
		initConfetti();
		if(textMission == null) {
			textMission = getText(0x50000);
			textCompleted = getText(0x50001);
		}
	}
	
	public static final boolean renderMissionComplete() {
		levelCameraApproach(Field444, Field445, 0);
		gamma = 100;
		levelCameraZoom = Field446;
		levelRender(false);
		if(levelCompleteTicks < 100) {
			if(levelCompleteTicks % 4 == 0) {
				Field446++;
			}
	
			gamma = levelCompleteTicks;
		}
	
		if(levelCompleteTicks > 0) {
			if(levelCompleteTicks < 50) {
				levelCameraZoom = levelCompleteTicks * 2 * 72 / 100;
	
				for(int var0 = 0; var0 < 5; var0++) {
					int var1 = (100 - levelCompleteTicks * 2) * (100 - levelCompleteTicks * 2) * (100 - levelCompleteTicks * 2) * (100 - levelCompleteTicks * 2);
					int[] var10000 = levelCircleY;
					var10000[var0] -= var1 >> 2;
					var10000 = levelCircleX;
					var10000[var0] += var1 / 2000 * cos((50 - levelCompleteTicks) * 45);
				}
	
				levelCompleteTicks++;
			}
	
			levelRenderPlayer();
		}
	
		renderConfetti();
		int var2 = 90;
		if(levelCompleteTicks > 105) {
			var2 = (150 - levelCompleteTicks) * 2;
		}
	
		renderText((128 - calcTextWidth(textMission, 0)) / 2 - 128 * cos1000[var2] / 1000, 32, textMission, 0);
		renderText((128 - calcTextWidth(textCompleted, 0)) / 2 + 128 * cos1000[var2] / 1000, 43, textCompleted, 0);
		refreshGame();
		levelCompleteTicks -= 2;
		if(softkeyPressed(2, -1) == 2) {
			levelCompleteTicks = -38;
		}
	
		if(levelCompleteTicks < -40) {
			setNewState(6, 0);
			Field393 = true;
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
		while(deg < 0) {
			deg += 360;
		}
	
		while(deg >= 360) {
			deg -= 360;
		}
	
		return sin1000[deg];
	}
	
	public static final int cos(int deg) {
		while(deg < 0) {
			deg += 360;
		}
	
		while(deg >= 360) {
			deg -= 360;
		}
	
		return cos1000[deg];
	}
	
	public static final void loadTrigonometric() {
		if(sin1000 == null) {
			sin1000 = loadFile16("sin1000.bin");
		}
	
		if(cos1000 == null) {
			cos1000 = loadFile16("cos1000.bin");
		}
	}
	
	public static final void loadStatic() {
		if(imgsStatic == null) {
			imgsStatic = new Image[4];
	
			for(int var0 = 0; var0 < 4; var0++) {
				imgsStatic[var0] = loadImage("static" + var0 + ".pim", "static" + var0 + ".ppl");
			}
		}
	
		if(staticTiles == null) {
			staticTiles = new int[42];
		}
	}
	
	public static final void renderPingTransmission() {
		loadStatic();
		gSetColor(pingBackgroundColor);
		gFillRect(0, 0, 128, 128);
		if(isPurpleTransmissionShaky) {
			renderPurple(currentPurpleX + 12 * sin1000[(int)(millis() / 5L % 360L)] / 1000, currentPurpleY + 12 * sin1000[(int)(millis() / 3L % 360L)] / 1000, currentPurpleSize + 40 + 20 * sin1000[(int)(millis() / 14L % 360L)] / 1000);
		} else {
			renderPurple(currentPurpleX, currentPurpleY, currentPurpleSize);
		}
	
		if(isTransmodigrafierMissing) {
			int var0 = 0;
	
			for(int var1 = 0; var1 <= 128; var1 += 23) {
				for(int var2 = -rand8() % 30; var2 <= 128; var2 += 23) {
					int var3;
					for(var3 = rand8() % 4; var3 == staticTiles[var0]; var3 = rand8() % 4) {
					}
	
					staticTiles[var0] = var3;
					gDrawImage(imgsStatic[var3], var2, var1, 0);
					var0++;
				}
			}
		}
	
		if(isShowingShardPicture) {
			gSetColor(0xffffff);
			gFillRect(0, 0, 128, 10);
			gFillRect(0, 0, 10, 128);
			gFillRect(118, 0, 10, 128);
			gFillRect(0, 98, 128, 30);
			gSetColor(0);
			gDrawRect(10, 10, 107, 87);
			if(activePurpleShardNameID >= 0) {
				renderText(-1000, 106, purpleShardNames[activePurpleShardNameID], 1);
			}
		}
	}
	
	public static final void renderPurple(int var0, int var1, int var2) {
		int var3 = 40 * var2 / 100;
		int var4 = 93 * var2 / 100;
		gSetColor(0);
		gFillArc(var0 - var3 / 2 - 2, var1 - var4 / 2 - 2, var3 + 2 + 2, var4 + 2 + 2, 0, 360);
		if(isTransmodigrafierMissing) {
			gSetColor(0x696969);
		} else {
			gSetColor(0xa020f0);
		}
	
		gFillArc(var0 - var3 / 2, var1 - var4 / 2, var3, var4, 0, 360);
		gSetColor(0);
		int var5 = 23 * var2 / 100;
		int var6 = 5 * var2 / 100;
		if(Field134 && !isShowingShardPicture && (millis() / 150L & 1L) > 0L) {
			gFillArc(var0 - var5 / 2, var1 - var6 * 2, var5, var6 * 2, 0, 360);
		} else {
			gFillArc(var0 - var5 / 2, var1 - var6, var5, var6, 0, 360);
		}
	
		gSetColor(0xffffff);
		int var7 = 14 * var2 / 100;
		int var8 = 6 * var2 / 100;
	
		for(int var9 = 0; var9 < 3; var9++) {
			gFillArc(var0 - 3 * var7 / 2 + var9 * var7, var1 - 45 * var4 / 100, var7, var7, 0, 360);
		}
	
		gSetColor(0);
	
		for(int var10 = 0; var10 < 3; var10++) {
			gFillArc(var0 - 3 * var7 / 2 + var10 * var7 + var7 / 3, var1 - 45 * var4 / 100 + var7 / 3, var8, var8, 0, 360);
		}
	
		if(Field456 || !isShowingShardPicture && rand8() > 240) {
			if(isTransmodigrafierMissing) {
				gSetColor(0x696969);
			} else {
				gSetColor(0xa020f0);
			}
	
			for(int var11 = 0; var11 < 3; var11++) {
				gFillArc(var0 - 3 * var7 / 2 + var11 * var7, var1 - 45 * var4 / 100, var7, var7, 0, 360);
			}
	
			Field456 = !Field456;
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
	
		for(int var2 = 0; var2 < numSelections; var2++) {
			imgsSelectionMenu[var2] = loadImage(imageFiles[(var2 + var1) % numSelections] + ".pim", imageFiles[(var2 + var1) % numSelections] + ".ppl");
		}
	
		imgsSelectionMenuArrows = new Image[2];
		imgsSelectionMenuArrows[0] = loadImage("arrow_left.pim", "arrow_left.ppl");
		imgsSelectionMenuArrows[1] = loadImage("arrow_right.pim", "arrow_right.ppl");
		levelInitCircles(10 + numSelections * 2 + 1);
		levelInitHooks(numSelections);
		activeSwapKey = -10;
		Field321 = 30000;
		xLossRate = 64000;
		selectionAngle = 0;
		Field471 = 10 + numSelections * 2;
		Field472 = var1;
	
		for(int var5 = 0; var5 < numSelections; var5++) {
			int var3 = 5242 * cos(90 + var5 * selectionAngleBetweenOthers);
			int var4 = 5242 * sin(90 + var5 * selectionAngleBetweenOthers);
			levelSetCircle(var5 + 10, var3, var4, 0x50000, 200, 0, 0, false);
			var3 = 6881 * cos(90 + var5 * selectionAngleBetweenOthers);
			var4 = 6881 * sin(90 + var5 * selectionAngleBetweenOthers);
			levelSetCircle(var5 + 10 + numSelections, var3, var4, 0x190000, 100, 3, 0, true);
		}
	
		levelSetCircle(Field471, 0, 0, 0x500000, 200, 2, 0, false);
	
		for(int var6 = 0; var6 < numSelections; var6++) {
			levelSetHook(var6, var6 + 10, var6 + 10 + numSelections, 4, 0x3c0000, 65000, true, true);
		}
	
		levelSetCamera(levelCircleX[Field471], levelCircleY[Field471] + 0x640000, 0);
		Field465 = 0;
		Field466 = millis();
	}
	
	public static final int getSelectedIndex() {
		if(Field465 == 1 && softkeyPressed(2, -1, true) == 2) {
			Field465++;
			Field466 = millis() + 500L;
		}
	
		if(isKeyHeld(8)) {
			selectionAngle += 4;
		} else if(isKeyHeld(16)) {
			selectionAngle -= 4;
		} else {
			int var0 = selectionAngle % selectionAngleBetweenOthers;
			if(var0 > selectionAngleUntilAdjust) {
				selectionAngle += 2;
			} else if(var0 > 0) {
				selectionAngle -= 2;
			}
		}
	
		if(selectionAngle >= 360) {
			selectionAngle -= 360;
		}
	
		if(selectionAngle < 0) {
			selectionAngle += 360;
		}
	
		for(int var3 = 0; var3 < numSelections; var3++) {
			int var1 = 5242 * cos(selectionAngle + 90 + var3 * selectionAngleBetweenOthers);
			int var2 = 5242 * sin(selectionAngle + 90 + var3 * selectionAngleBetweenOthers);
			levelCircleX[var3 + 10] = var1;
			levelCircleY[var3 + 10] = var2;
		}
	
		levelUpdateAll();
		if(Field465 == 0) {
			gamma = (int)((millis() - Field466) / 5L);
		}
	
		if(Field465 == 2) {
			gamma = (int)((Field466 - millis()) / 20L);
		}
	
		if(gamma < 0) {
			gamma = 0;
			Field465++;
		}
	
		if(gamma > 100) {
			gamma = 100;
			Field465++;
		}
	
		setGammaColor(0xdddddd);
		gFillRect(0, 0, 128, 128);
		sceneSelectionRenderCircle(levelCircleX[Field471], levelCircleY[Field471], levelCircleRadius[Field471], 0xaaaaaa);
		sceneSelectionRenderCircle(levelCircleX[Field471], levelCircleY[Field471], levelCircleRadius[Field471] / 2, 0xdddddd);
		setGammaColor(0);
	
		for(int var4 = 10; var4 < 10 + numSelections; var4++) {
			levelRenderLine(levelCircleX[var4], levelCircleY[var4], levelCircleX[var4 + numSelections], levelCircleY[var4 + numSelections]);
		}
	
		for(int var5 = 0; var5 < numSelections; var5++) {
			levelRenderCircle(levelCircleX[var5 + 10 + numSelections], levelCircleY[var5 + 10 + numSelections], levelCircleRadius[var5 + 10 + numSelections]);
			if(gamma > 20) {
				levelRenderImage(imgsSelectionMenu[var5], levelCircleX[var5 + 10 + numSelections], levelCircleY[var5 + 10 + numSelections]);
			}
		}
	
		setGammaColor(0x888888);
	
		for(int var6 = 0; var6 < 5; var6++) {
			int var9 = 3932 * cos(selectionAngle + 90 - 36 + var6 * 72);
			int var10 = 3932 * sin(selectionAngle + 90 - 36 + var6 * 72);
			levelRenderCircle(var9, var10, 0x50000);
		}
	
		if(gamma > 50) {
			Field473 += 12;
			if(Field473 > 179) {
				Field473 -= 180;
			}
	
			int var7 = 1 + 6 * sin1000[Field473] / 1000;
			gDrawImage(imgsSelectionMenuArrows[0], var7, 85 - imgsSelectionMenuArrows[0].getHeight() / 2, 0);
			gDrawImage(imgsSelectionMenuArrows[1], 128 - var7 - imgsSelectionMenuArrows[1].getWidth(), 85 - imgsSelectionMenuArrows[1].getHeight() / 2, 0);
		}
	
		refreshGame();
		if(Field465 == 3) {
			int var8 = (360 - selectionAngle) / selectionAngleBetweenOthers + Field472;
			if(selectionAngle % selectionAngleBetweenOthers > selectionAngleUntilAdjust) {
				var8++;
			}
	
			if(var8 >= numSelections) {
				var8 -= numSelections;
			}
	
			if(var8 < 0) {
				var8 += numSelections;
			}
	
			return var8;
		} else {
			return -1;
		}
	}
}
