import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.rms.RecordStore;

public final class Game extends GameCanvas implements Runnable {
	public static Game instance; // Field0
	public static PMMIDlet midlet = null; // Field1
	public static int Field2 = 0;
	public static int Field3 = 0;
	public static int Field4 = 0;
	public static int Field5 = 0;
	public static final short[][] Field6 = new short[][] {{53, -6, -5}, {48}, {-7}, {52, -3}, {54, -4}, {50, -1}, {56, -2}, {49}, {51}, {55}, {57}};
	private static byte[] Field7;
	private static int Field8;
	private static int Field9;
	private static int Field10;
	private static byte[] Field11;
	public static boolean Field12;
	public static long Field13;
	public static long Field14;
	public static String Field15 = "PMDATA";
	public static boolean Field16 = true;
	private static int Field17;
	private static int Field18;
	private static int Field19;
	private static int Field20;
	private static long Field21;
	private static int[] Field22;
	private static boolean[] Field23;
	private static boolean[] Field24;
	private static boolean[] Field25;
	private static int[] Field26;
	private static int[] Field27;
	private static String[] Field28;
	private static int Field29;
	private static Player[] Field30;
	private static Player[] Field31;
	public static DataInputStream Field32 = null;
	public static int Field33;
	public static boolean Field34 = false;
	public static int Field35 = -1;
	public static int Field36 = -1;
	public static int Field37 = -1;
	public static boolean Field38;
	public static byte[][] Field39;
	public static int Field40 = 0;
	public static short[] Field41;
	public static byte[] Field42;
	public static int[] Field43;
	public static byte[] Field44;
	public static int[] Field45;
	public static int Field46;
	private static int Field47 = 4129;
	private static int[] Field48 = new int[256];
	public static String[] Field49;
	public static byte[] Field50;
	public static int Field51 = -1;
	public static short Field52;
	private static int Field53;
	private static int Field54;
	public static int[] Field55;
	public static int[] Field56;
	public static Image[] Field57;
	public static Image[] Field58;
	public static int[] Field59;
	public static int[] Field60;
	public static int[] Field61;
	public static int[] Field62;
	public static short[][] Field63;
	public static byte[][] Field64;
	public static short[][] Field65;
	public static Graphics Field66;
	private static long Field67;
	public static boolean Field68 = true;
	public static int Field69;
	public static int Field70 = -1;
	public static int Field71;
	public static int Field72;
	private static Display Field73;
	public static boolean Field74 = true;
	public static int[] Field75;
	public static int[] Field76;
	public static int[] Field77;
	public static int[] Field78;
	public static int[] Field79;
	public static int[] Field80;
	public static int[] Field81 = new int[] {16777215, 16711680, 65280, 255, 16776960, 14276889, 15132922, 9205843};
	public static int[] Field82;
	public static int[] Field83;
	public static int[] Field84;
	public static int[] Field85;
	public static int[] Field86;
	public static int[][] Field87;
	public static int[][] Field88;
	public static int[][] Field89;
	public static int[][] Field90;
	public static int Field91;
	public static int Field92;
	public static String[] Field93;
	public static int Field94;
	public static int Field95;
	public static boolean Field96;
	public static Image[] Field97;
	public static int Field98;
	public static Image Field99 = null;
	public static Image Field100 = null;
	public static Image Field101 = null;
	public static String Field102;
	public static String Field103;
	public static int Field104;
	public static int Field105;
	public static int Field106;
	public static int Field107;
	public static int Field108;
	public static int Field109 = 0;
	public static Image Field110 = null;
	public static Image Field111 = null;
	public static Image Field112 = null;
	public static int Field113;
	public static int Field114;
	public static int Field115;
	public static long Field116;
	public static int Field117 = 0;
	public static boolean Field118;
	public static int Field119 = 0;
	public static int Field120 = 0;
	public static int Field121 = 0;
	public static boolean Field122 = false;
	public static int Field123 = 1;
	public static boolean Field124;
	public static int[] Field125;
	public static int Field126;
	public static boolean Field127;
	public static boolean Field128;
	public static int Field129;
	public static int Field130;
	public static boolean Field131;
	public static int Field132;
	public static long Field133;
	public static boolean Field134;
	public static boolean Field135;
	public static boolean Field136;
	public static boolean Field137;
	public static int Field138;
	public static int Field139;
	public static int Field140;
	public static int Field141;
	public static int Field142;
	public static boolean Field143;
	public static boolean Field144;
	public static int[] Field145 = new int[] {-1, -1, -1, -1, -1, -1, -1, -1};
	public static int Field146 = 0;
	public static Image Field147;
	public static Image Field148;
	public static boolean Field149 = false;
	public static int Field150;
	public static long Field151;
	public static int Field152;
	public static int Field153;
	public static boolean Field154 = true;
	public static int Field155;
	public static int Field156;
	public static int Field157;
	public static int Field158;
	public static int Field159;
	public static int Field160;
	public static int Field161;
	public static int Field162 = 200;
	public static int Field163 = 0;
	public static int Field164 = 0;
	public static int Field165;
	public static int Field166 = 1;
	public static int Field167 = -1000;
	public static int Field168 = -1;
	public static boolean Field169 = false;
	public static int Field170 = 0;
	public static int Field171;
	public static int Field172;
	public static int Field173 = 0;
	public static int Field174 = 0;
	public static int Field175 = -1;
	public static int Field176 = -1;
	public static int[] Field177 = new int[2];
	public static int[] Field178 = new int[2];
	public static Image[] Field179 = null;
	public static Image[] Field180 = null;
	public static Image[] Field181 = null;
	public static Image[] Field182 = null;
	public static Image[][] Field183 = null;
	public static Image[][] Field184 = null;
	public static Image[][] Field185 = null;
	public static Image Field186 = null;
	public static int[][] Field187 = new int[][] {{0, 0}, {0, 0}, {0, 0}, {6, 8}, {0, -2}, {0, 0}, {0, 0}, {0, 0}};
	public static int[][] Field188 = new int[][] {{0, -3}, {3, 0}, {3, 0}, {10, 4}, {0, -5}, {4, 4}, {4, 1}, {6, 4}};
	public static int[][] Field189 = new int[][] {{0, 0}, {2, 6}, {0, 0}, {-4, -8}, {4, 2}, {4, 4}, {0, 0}, {2, 6}};
	public static int[][] Field190 = new int[][] {{0, 0}, {5, 2}, {3, 6}, {5, 9}, {4, 9}, {2, 4}, {3, 7}, {3, 6}};
	public static int Field191;
	public static int Field192;
	public static int Field193;
	public static int Field194;
	public static int Field195;
	public static Image Field196;
	public static Image[] Field197;
	public static int Field198;
	public static int[] Field199;
	public static int[] Field200;
	public static int[] Field201;
	public static int[] Field202;
	public static int[] Field203;
	public static int[] Field204;
	public static boolean[] Field205;
	public static boolean[] Field206;
	public static int Field207;
	public static int[] Field208;
	public static int[] Field209;
	public static int[] Field210;
	public static int[] Field211;
	public static int[] Field212;
	public static int[] Field213;
	public static boolean[] Field214;
	public static boolean[] Field215;
	public static int[] Field216;
	public static int[] Field217;
	public static int[] Field218;
	public static int[] Field219;
	public static int[] Field220;
	public static int[] Field221;
	public static int Field222;
	public static int[] Field223;
	public static boolean[] Field224;
	public static boolean[] Field225;
	public static int[] Field226;
	public static int[] Field227;
	public static int[] Field228;
	public static int[] Field229;
	public static Image Field230;
	public static int Field231;
	public static int[] Field232;
	public static int[][] Field233;
	public static int Field234;
	public static int Field235;
	public static Image[] Field236;
	public static Image[] Field237;
	public static Image[] Field238;
	public static Image[] Field239;
	public static Image[] Field240;
	public static Image[] Field241;
	public static int[] Field242;
	public static int Field243 = -1;
	public static int Field244;
	public static int[] Field245;
	public static boolean[] Field246;
	public static int[] Field247;
	public static int[] Field248;
	public static boolean[] Field249;
	public static int[] Field250;
	public static int[] Field251;
	public static int[] Field252;
	public static int[] Field253;
	public static int[] Field254;
	public static int[] Field255;
	public static boolean[] Field256;
	public static boolean[] Field257;
	public static boolean[] Field258;
	public static int Field259;
	public static int[] Field260;
	public static int[] Field261;
	public static int[] Field262;
	public static int[] Field263;
	public static boolean[] Field264;
	public static int[] Field265;
	public static int[] Field266;
	public static int[] Field267;
	public static int[] Field268;
	public static int[] Field269;
	public static int[] Field270;
	public static int[] Field271;
	public static int[] Field272;
	public static int Field273;
	public static int[] Field274;
	public static int[] Field275;
	public static int[] Field276;
	public static int[] Field277;
	public static Image Field278;
	public static Image[] Field279;
	public static int[] Field280;
	public static int[] Field281;
	public static int[] Field282;
	public static Image[] Field283;
	public static boolean Field284 = false;
	public static int[] Field285;
	public static int[] Field286;
	public static int[] Field287;
	public static int[] Field288;
	public static int[] Field289;
	public static int[] Field290;
	public static int[] Field291;
	public static int[] Field292;
	public static int[] Field293;
	public static int[] Field294;
	public static int[] Field295;
	public static int[] Field296;
	public static int Field297;
	public static int[] Field298;
	public static int[] Field299;
	public static int[] Field300;
	public static int[] Field301;
	public static int[] Field302;
	public static int[] Field303;
	public static int[] Field304;
	public static byte[] Field305;
	public static boolean[] Field306;
	public static int Field307;
	public static int[] Field308;
	public static int[] Field309;
	public static int[] Field310;
	public static int[] Field311;
	public static int[] Field312;
	public static int[] Field313;
	public static int[] Field314;
	public static byte[] Field315;
	public static boolean[] Field316;
	public static int Field317 = 0;
	public static int Field318 = 0;
	public static int Field319;
	public static int Field320;
	public static int Field321;
	public static int Field322;
	public static boolean Field323;
	public static boolean Field324;
	public static boolean Field325 = false;
	public static boolean Field326;
	public static int Field327 = 0;
	public static int Field328 = 0;
	public static int Field329 = 16777215;
	public static int Field330 = 16777215;
	public static int Field331 = 16777215;
	public static int Field332 = 16777215;
	public static int Field333 = 16729344;
	public static int Field334 = 152;
	public static int Field335 = 255;
	public static int Field336 = 36;
	public static int Field337 = 182;
	public static int Field338 = 16;
	public static int Field339 = 33;
	public static int Field340 = 0;
	public static short[] Field341;
	public static short[] Field342;
	public static short[] Field343;
	public static short[] Field344;
	public static short[] Field345;
	public static short[] Field346;
	public static short[] Field347;
	public static int Field348 = -1;
	public static int Field349;
	public static int Field350;
	public static int Field351;
	public static int Field352;
	public static boolean Field353;
	public static boolean Field354;
	public static int Field355 = -1;
	public static Image[] Field356;
	public static int[][] Field357;
	public static int[][] Field358;
	public static int Field359;
	public static int Field360;
	public static int Field361;
	public static int Field362;
	public static int Field363;
	public static boolean Field364 = true;
	public static boolean[] Field365 = new boolean[] {true, true, true, true, false};
	public static int[] Field366 = new int[] {2, 1, 1, 2, 0};
	public static int[] Field367 = new int[] {0, 1, 2, 3, 4};
	public static int[][][] Field368 = new int[][][] {{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, {{0, 0}, {0, 0}, {1, 1}}, {{0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}}, {{3, 0, 1, 2}, {0, 1, 2, 0}, {1, 2, 0, 3}, {2, 0, 0, 0}}};
	public static int[][][] Field369 = new int[][][] {{{1, 2, 1, 2}, {3, 3, 3, 3}, {4, 4, 4, 4}}, {{2, 2, 2}, {3, 3, 3}, {3, 3, 3}}, {{1, 2, 1}, {3, 3, 3}, {3, 3, 3}}, {{1, 1, 1}, {2, 2, 2}, {2, 2, 2}}, {{0}}};
	public static int Field370;
	public static int Field371;
	public static int Field372;
	public static int Field373;
	public static int Field374;
	public static int Field375;
	public static int Field376;
	public static int Field377;
	public static int Field378;
	public static int Field379;
	public static int Field380;
	public static int Field381;
	public static int Field382;
	public static int Field383;
	public static long Field384;
	public static int Field385;
	public static String Field386;
	public static boolean Field387;
	public static Image[][] Field388;
	public static int Field389 = -2;
	public static int Field390 = 0;
	public static boolean Field391 = false;
	public static boolean Field392 = false;
	public static boolean Field393 = false;
	public static int Field394 = 70;
	public static int Field395 = 0;
	public static int Field396 = -1;
	public static int Field397 = 0;
	public static int Field398 = 72;
	public static int[] Field399 = new int[] {8, 16, 8, 16, 2};
	public static int[] Field400 = new int[] {64, 16, 64, 8, 64, 2};
	public static int Field401 = 0;
	public static int Field402 = 0;
	public static boolean Field403 = false;
	public static boolean Field404 = true;
	public static boolean Field405 = false;
	public static int[] Field406 = new int[12];
	public static int Field407 = 9;
	public static boolean Field408 = false;
	public static int Field409 = 100;
	public static short[] Field410;
	public static short[] Field411;
	public static Image[] Field412;
	public static Image Field413;
	public static Image Field414;
	public static Image[] Field415;
	public static Image[] Field416;
	public static Image[] Field417;
	public static Image[] Field418;
	public static Image Field419;
	public static Image Field420 = null;
	public static Image Field421 = null;
	public static Image[] Field422 = null;
	public static int Field423;
	public static int Field424 = 0;
	public static String[] Field425;
	public static String[] Field426;
	public static boolean Field427 = false;
	public static boolean Field428 = false;
	public static int Field429;
	public static int Field430;
	public static int Field431;
	public static int Field432;
	public static int Field433;
	public static int Field434;
	public static int Field435;
	public static int Field436;
	public static int Field437;
	public static boolean Field438;
	public static boolean Field439;
	public static Image Field440;
	public static Image Field441;
	public static Image Field442;
	public static int Field443;
	public static int Field444;
	public static int Field445;
	public static int Field446;
	public static String Field447;
	public static String Field448;
	public static short[] Field449;
	public static short[] Field450;
	public static Image[] Field451;
	public static int[] Field452;
	public static boolean Field453 = true;
	public static boolean Field454 = true;
	public static boolean Field455 = false;
	public static boolean Field456 = false;
	public static int Field457 = 59;
	public static int Field458 = 108;
	public static int Field459 = 100;
	public static int Field460 = 0;
	public static int Field461 = 0;
	public static String[] Field462 = new String[] {"Kachoo", "NikSak", "Rara", "Corakllquar", "Brad", "NotPing"};
	public static Image[] Field463;
	public static Image[] Field464;
	public static int Field465;
	public static long Field466;
	public static int Field467;
	public static int Field468;
	public static int Field469;
	public static int Field470;
	public static int Field471;
	public static int Field472;
	public static int Field473;
	
	public Game() throws IOException {
		super(false);
		setFullScreenMode(true);
		instance = this;
	}
	
	public final void hideNotify() {
		Field4 = 0;
		Field12 = true;
		Method29();
		Method31();
		Method261();
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
		Field66 = this.getGraphics();
		Method85(0, 0, 128, 128);
		Method96();
		midlet.exit();
	}
	
	public static final void Method1() {
		Method263();
	}
	
	public static final void Method2() {
		Method15(5L);
		Field3 = Field2;
		Field2 = Field4;
		Field4 &= ~Field5;
		Field5 = 0;
	}
	
	private static int Method3(int var0) {
		for(int var1 = 0; var1 < Field6.length; ++var1) {
			for(int var2 = 0; var2 < Field6[var1].length; ++var2) {
				if (var0 == Field6[var1][var2]) {
					return 1 << var1;
				}
			}
		}
	
		return 1048576;
	}
	
	public final void keyPressed(int var1) {
		Field4 |= Method3(var1);
		super.keyPressed(var1);
		if (var1 == -6) {
			Field5 |= 1;
			Field5 |= 2048;
			Field4 |= 2048;
		}
	
		if (var1 == -7) {
			Field5 |= 4;
			Field5 |= 4096;
			Field4 |= 4096;
		}
	
	}
	
	public final void keyReleased(int var1) {
		Field4 &= ~Method3(var1);
		super.keyReleased(var1);
	}
	
	public static final boolean Method4(int var0) {
		return (Field2 & var0) > 0;
	}
	
	public static final boolean Method5(int var0) {
		return (Field2 & var0) > 0 && (Field3 & var0) == 0;
	}
	
	public static final boolean Method6(int var0) {
		return (Field3 & var0) > 0 && (Field2 & var0) == 0;
	}
	
	public static final void Method7() {
		Field2 = 0;
		Field3 = 0;
		Field4 = 0;
		Field5 = 0;
	}
	
	public static final byte[] Method8(byte[] var0, short var1) {
		if (Field7 == null) {
			Field7 = Method38((short)-8473);
		}
	
		Method10(var1);
		if ((var0[0] & 3) != 3) {
			return null;
		} else {
			int var2 = var0.length - 18;
			byte[] var4 = new byte[33 + 12 + Field9 * 3 + 13 * (Field8 & 1) + 12 + var2 + 4 + 12];
			int var5 = 0;
			System.arraycopy(Field7, 0, var4, 0, 29);
			System.arraycopy(var0, 2, var4, 18, 2);
			System.arraycopy(var0, 4, var4, 22, 2);
			System.arraycopy(var0, 6, var4, 29, 4);
			System.arraycopy(Method9(Field9 * 3), 0, var4, 33, 4);
			System.arraycopy(Field7, 42, var4, 37, 4);
			System.arraycopy(Field11, 0, var4, 41, Field11.length);
			var5 = 41 + Field11.length;
			System.arraycopy(Method9(Field10), 0, var4, var5, 4);
			var5 += 4;
			if ((Field8 & 1) == 1) {
				System.arraycopy(Field7, 29, var4, var5, 13);
				var5 += 13;
			}
	
			System.arraycopy(Method9(var2 + 4), 0, var4, var5, 4);
			System.arraycopy(Field7, 46, var4, var5 + 4, 4);
			System.arraycopy(var0, 18, var4, var5 + 8, var2);
			var5 += var2 + 8;
			System.arraycopy(var0, 14, var4, var5, 4);
			System.arraycopy(var0, 10, var4, var5 + 4, 4);
			System.arraycopy(Field7, 52, var4, var5 + 8, 12);
			Field11 = null;
			System.gc();
			return var4;
		}
	}
	
	public static final byte[] Method9(int var0) {
		byte[] var1;
		(var1 = new byte[4])[0] = (byte)(var0 >> 24 & 255);
		var1[1] = (byte)(var0 >> 16 & 255);
		var1[2] = (byte)(var0 >> 8 & 255);
		var1[3] = (byte)(var0 & 255);
		return var1;
	}
	
	public static final void Method10(short var0) {
		Method45(var0);
		Field8 = Method51();
		Field9 = 1 + Method50();
		Field10 = Method52();
		Field11 = new byte[Field9 * 3];
		Method47(Field11, 0, Field11.length);
	}
	
	public static final Image Method11(String var0, String var1) {
		return Method12(Method37(var0), Method37(var1));
	}
	
	public static final Image Method12(short var0, short var1) {
		return Method13(var0, var1);
	}
	
	public static final Image Method13(short var0, int var1) {
		Object var2 = null;
		byte[] var3;
		if ((var3 = Method39(var0)) == null) {
			return null;
		} else {
			if (var3[0] != -119 && var3[1] != 80) {
				var3 = Method8(var3, (short)var1);
			}
	
			Image var4 = Image.createImage(var3, 0, var3.length);
			System.gc();
			Method15(10L);
			return var4;
		}
	}
	
	public static final void Method14() {
		Method31();
		Method65();
		Method2();
		Method259();
		Method90();
		Method17();
	}
	
	public static final void Method15(long var0) {
		try {
			Thread.sleep(var0);
		} catch (Exception var2) {
		}
	}
	
	public static final void Method16() {
		long var0 = Runtime.getRuntime().freeMemory();
		System.gc();
	
		for(int var2 = 0; var2 < 10; ++var2) {
			Thread.yield();
			if (Runtime.getRuntime().freeMemory() < var0) {
				return;
			}
		}
	
	}
	
	public static final void Method17() {
		Field14 = Method101();
		if (Field14 - Field13 < 45L) {
			Method15(45L - (Field14 - Field13));
		}
	
		Field13 = Method101();
	}
	
	public static final void Method18(byte[] var0) {
		Object var1 = null;
	
		try {
			RecordStore var3;
			if ((var3 = RecordStore.openRecordStore(Field15, true)) != null) {
				if (var3.getNumRecords() == 0) {
					var3.addRecord(var0, 0, var0.length);
				} else {
					var3.setRecord(1, var0, 0, var0.length);
				}
	
				var3.closeRecordStore();
			}
	
		} catch (Exception var2) {
		}
	}
	
	public static final byte[] Method19() {
		byte[] var0 = null;
		Object var1 = null;
	
		try {
			RecordStore var3;
			if ((var3 = RecordStore.openRecordStore(Field15, true)) != null) {
				var0 = var3.getRecord(1);
				var3.closeRecordStore();
			}
		} catch (Exception var2) {
		}
	
		return var0;
	}
	
	public static final void Method20() {
		boolean var0 = false;
		byte[] var1 = new byte[10];
		boolean var2 = false;
		var1[0] = (byte)(Field397 & 255);
		var1[1] = (byte)(Field397 >> 8 & 255);
		var1[2] = (byte)(Field397 >> 16 & 255);
		var1[3] = (byte)(Field397 >> 24 & 255);
		var1[4] = (byte)(Field328 & 255);
		var1[5] = (byte)(Field328 >> 8 & 255);
		var1[6] = (byte)(Field328 >> 16 & 255);
		var1[7] = (byte)(Field328 >> 24 & 255);
		var1[8] = (byte)(Field325 ? 1 : 0);
		var1[9] = (byte)(Field74 ? 1 : 0);
		Method18(var1);
	}
	
	public static final boolean Method21() {
		byte[] var0;
		if ((var0 = Method19()) == null) {
			return false;
		} else {
			boolean var1 = false;
	
			try {
				Field397 = (var0[0] & 255) + (var0[1] << 8 & '\uff00') + (var0[2] << 16 & 16711680) + (var0[3] << 24 & -16777216);
				Field328 = (var0[4] & 255) + (var0[5] << 8 & '\uff00') + (var0[6] << 16 & 16711680) + (var0[7] << 24 & -16777216);
				Field325 = var0[8] == 1;
				Field74 = var0[9] == 1;
				return true;
			} catch (Exception var2) {
				return false;
			}
		}
	}
	
	public static final void Method22(int var0) {
		boolean var1 = false;
		Field19 = var0;
		Field23 = new boolean[var0];
		Field24 = new boolean[var0];
		Field25 = new boolean[var0];
		Field26 = new int[var0];
		Field22 = new int[var0];
		Field27 = new int[var0];
		Field20 = -1;
		Field30 = new Player[var0];
		Field31 = new Player[var0];
		Field17 = 0;
		Field18 = 0;
		Field28 = new String[var0];
	
		for(int var2 = 0; var2 < Field19; ++var2) {
			Field23[var2] = false;
			Field24[var2] = false;
			Field25[var2] = false;
			Field26[var2] = 5;
			Field22[var2] = 1;
			Field27[var2] = 999;
			Field30[var2] = null;
			Field31[var2] = null;
		}
	
	}
	
	public static final int Method23(int var0, String var1, int var2, int var3) {
		if (Method26(var0)) {
			return -2;
		} else if (var0 >= Field19) {
			return -3;
		} else if (var1 == null) {
			return -1;
		} else {
			if (Field34) {
				Method1();
			}
	
			Field27[var0] = var2;
			if (!var1.equals(Field28[var0])) {
				Field28[var0] = var1;
			}
	
			Object var4 = null;
			return -4;
		}
	}
	
	public static final int Method24(int var0, String var1, int var2, int var3) {
		if (Method26(var0)) {
			return -2;
		} else if (var0 >= Field19) {
			return -3;
		} else if (var1 == null) {
			return -1;
		} else {
			if (Field34) {
				Method1();
			}
	
			Field27[var0] = var2;
			if (!var1.equals(Field28[var0])) {
				Field28[var0] = var1;
			}
	
			String var4 = "";
			Player var5 = null;
	
			try {
				Object var6 = null;
				if (var2 == 6) {
					InputStream var9 = var4.getClass().getResourceAsStream("/" + var1 + ".wav");
					Field30[var0] = Manager.createPlayer(var9, "audio/x-wav");
					var5 = Field30[var0];
				}
	
				if (var2 == 7) {
					InputStream var10 = var4.getClass().getResourceAsStream("/" + var1 + ".amr");
					Field30[var0] = Manager.createPlayer(var10, "audio/amr");
					var5 = Field30[var0];
				}
	
				if (var2 == 8) {
					InputStream var11 = var4.getClass().getResourceAsStream("/" + var1 + ".mp3");
					Field30[var0] = Manager.createPlayer(var11, "audio/mp3");
					var5 = Field30[var0];
				}
	
				if (var2 == 2) {
					InputStream var12 = var4.getClass().getResourceAsStream("/" + var1 + ".mid");
					Field31[var0] = Manager.createPlayer(var12, "audio/sp-midi");
					var5 = Field31[var0];
				}
	
				if (var2 == 1) {
					InputStream var13 = var4.getClass().getResourceAsStream("/" + var1 + ".mid");
					Field31[var0] = Manager.createPlayer(var13, "audio/midi");
					var5 = Field31[var0];
				}
	
				var5.realize();
	
				while(var5.getState() != 200) {
					Method15(30L);
				}
	
				++Field29;
				Field25[var0] = true;
				boolean var7 = false;
				if (var2 != 1 && var2 != 2) {
					if (var2 != 6 && var2 != 7 && var2 != 8) {
						return -2;
					}
	
					if (Field18 < 1) {
						var5.prefetch();
						++Field18;
						var7 = true;
					}
				} else if (Field17 < 1) {
					var5.prefetch();
					++Field17;
					var7 = true;
				}
	
				if (var7) {
					while(var5.getState() != 300) {
						Method15(30L);
					}
				}
	
				return 0;
			} catch (Exception var8) {
				if (var5 != null) {
					var5.close();
					Field30[var0] = null;
					Field31[var0] = null;
					Field27[var0] = 999;
					Field28[var0] = "";
				}
	
				return -1;
			}
		}
	}
	
	public static final void Method25(int var0) {
		Player var1 = null;
		if (Field30[var0] != null) {
			var1 = Field30[var0];
			--Field18;
		}
	
		if (Field31[var0] != null) {
			var1 = Field31[var0];
			--Field17;
		}
	
		if (var1 != null) {
			var1.close();
	
			while(var1.getState() != 0) {
				Method15(30L);
			}
	
			Field30[var0] = Field31[var0] = null;
			--Field29;
		}
	
	}
	
	public static final boolean Method26(int var0) {
		if (Field30[var0] != null) {
			return true;
		} else {
			return Field31[var0] != null;
		}
	}
	
	public static final boolean Method27(int var0, int var1) {
		if (!Field16) {
			return false;
		} else {
			boolean var2 = false;
			if ((var1 == Field27[var0] || var1 == -1) && Field23[var0]) {
				return true;
			} else {
				if ((var1 == 6 || var1 == -1) && Field30[var0] != null) {
					var2 = Field30[var0].getState() == 400;
				}
	
				if ((var1 == 1 || var1 == -1) && Field31[var0] != null) {
					var2 = Field31[var0].getState() == 400;
				}
	
				return var2;
			}
		}
	}
	
	public static final boolean Method28(int var0, int var1) {
		if (!Field16) {
			return true;
		} else if (Method101() - Field21 < 100L) {
			return false;
		} else {
			Field21 = Method101();
			if (Method27(var0, -1)) {
				return false;
			} else {
				int var4 = 0;
				if (Field27[var0] == 6 || Field27[var0] == 7 || Field27[var0] == 8) {
					for(int var5 = 0; var5 < Field19; ++var5) {
						if (Method27(var5, Field27[var5])) {
							++var4;
						}
					}
	
					if (var4 >= 2) {
						return false;
					}
				}
	
				Field23[var0] = true;
				Field22[var0] = var1;
				return true;
			}
		}
	}
	
	public static final void Method29() {
		if (Field16) {
			for(int var0 = 0; var0 < Field19; ++var0) {
				Field24[var0] = true;
			}
	
		}
	}
	
	private static void Method30(int var0, boolean var1) {
		Object var2 = null;
		Player var5;
		if (Field31[var0] != null) {
			var5 = Field31[var0];
		} else {
			var5 = Field30[var0];
		}
	
		try {
			if (var5 != null) {
				try {
					if (var1) {
						if (var5.getState() == 400) {
							var5.stop();
							var5.setMediaTime(0L);
						}
					} else {
						var5.stop();
						var5.setMediaTime(0L);
					}
				} catch (IllegalStateException var3) {
				}
			}
	
		} catch (MediaException var4) {
		}
	}
	
	public static final void Method31() {
		if (Field16 && Field23 != null) {
			for(int var0 = 0; var0 < Field19; ++var0) {
				if (Field24[var0]) {
					Field24[var0] = false;
					Method30(var0, false);
					if (Field27[var0] != 0) {
						Method25(var0);
					}
	
					if (Field20 == var0) {
						Field20 = -1;
					}
				}
	
				if (Field23[var0] && Field27[var0] == 1) {
					boolean var1 = false;
	
					for(int var5 = 0; var5 < Field19; ++var5) {
						if (var5 != var0) {
							Method30(var5, false);
							Field23[var5] = false;
						}
					}
	
					for(boolean var2 = true; var2; Method15(5L)) {
						for(int var6 = 0; var6 < Field19; ++var6) {
							if (var6 != var0 && Method27(var6, -1)) {
								var2 = true;
								break;
							}
	
							var2 = false;
						}
					}
				}
	
				if (!Method26(var0)) {
					if (Field27[var0] == 0 || !Field23[var0]) {
						continue;
					}
	
					boolean var7 = Field34;
					Field34 = false;
					Method24(var0, Field28[var0], Field27[var0], 0);
					Field34 = var7;
				}
	
				Object var8 = null;
				Player var9;
				if (Field31[var0] != null) {
					var9 = Field31[var0];
				} else {
					var9 = Field30[var0];
				}
	
				if (Field25[var0]) {
					Field25[var0] = false;
					if (var9 != null) {
						try {
							VolumeControl var10;
							if ((var10 = (VolumeControl)var9.getControl("VolumeControl")) != null) {
								var10.setLevel(Field26[var0] * 10);
							}
						} catch (IllegalStateException var4) {
						}
					}
				}
	
				if (Field23[var0]) {
					Field23[var0] = false;
	
					try {
						if (var9 != null && var9.getState() != 400) {
							var9.setLoopCount(Field22[var0]);
							var9.start();
							Field20 = var0;
						}
					} catch (MediaException var3) {
					}
				}
			}
	
		}
	}
	
	public static final int Method32(short var0) {
		for(int var1 = 0; var1 < Field46; ++var1) {
			if (Field41[var1] == var0) {
				return var1;
			}
		}
	
		return -1;
	}
	
	public static final void Method33(int var0) {
		Field39 = new byte[var0][];
		Field38 = false;
	}
	
	public static final void Method34(int var0) {
		try {
			Field37 = var0;
			Field38 = true;
			Field40 = 0;
			Field39[var0] = new byte[Field33];
			int var1 = 0;
			int var2 = 0;
	
			for(int var3 = Field33; var3 > 0; var2 += var1) {
				var1 = Field32.read(Field39[var0], var2, var3);
				var3 -= var1;
			}
	
			for(int var4 = 0; var4 < Field46; ++var4) {
				if (var4 != var0 && Field43[var4] == Field43[var0] && Field44[var4] == Field44[var0]) {
					Field39[var4] = Field39[var0];
				}
			}
	
			Field35 += Field33;
		} catch (Exception var5) {
		}
	}
	
	public static final void Method35() {
		Field36 = -1;
		Field35 = 0;
		Method36();
	
		try {
			String var0 = "/head.bfc";
			Field32 = new DataInputStream(var0.getClass().getResourceAsStream(var0));
			int var1;
			Field46 = var1 = Field32.readUnsignedShort();
			Field41 = new short[var1];
			Field42 = new byte[var1];
			Field43 = new int[var1];
			Field44 = new byte[var1];
			Field45 = new int[var1];
	
			for(int var2 = 0; var2 < var1; ++var2) {
				Field41[var2] = (short)Field32.readUnsignedShort();
				Field42[var2] = Field32.readByte();
				Field43[var2] = Field32.readUnsignedByte() << 16 | Field32.readUnsignedByte() << 8 | Field32.readUnsignedByte();
				Field44[var2] = (byte)Field32.readUnsignedByte();
				Field45[var2] = Field33 = Field32.readUnsignedByte() << 16 | Field32.readUnsignedByte() << 8 | Field32.readUnsignedByte();
			}
		} catch (Exception var3) {
		}
	
		Method33(Field46);
	}
	
	public static final void Method36() {
		for(int var0 = 0; var0 < 256; ++var0) {
			int var1 = 0;
			int var2 = var0 << 8;
	
			for(int var3 = 0; var3 < 8; ++var3) {
				if (((var1 ^ var2) & 0x8000) != 0) {
					var1 = var1 << 1 ^ Field47;
				} else {
					var1 <<= 1;
				}
	
				var2 <<= 1;
				var1 &= 65535;
			}
	
			Field48[var0] = var1;
		}
	
	}
	
	public static final short Method37(String var0) {
		int var1 = 65535;
	
		for(int var2 = 0; var2 < var0.length(); ++var2) {
			char var3;
			byte var4 = (byte)((var3 = var0.charAt(var2)) >> 8);
			var1 = (Field48[(var4 ^ var1 >> 8) & 255] ^ var1 << 8) & '\uffff';
			var4 = (byte)(var3 & 255);
			var1 = (Field48[(var4 ^ var1 >> 8) & 255] ^ var1 << 8) & '\uffff';
		}
	
		return (short)(var1 & '\uffff');
	}
	
	public static final byte[] Method38(short var0) {
		if (!Method45(var0)) {
			return null;
		} else {
			byte[] var1;
			Method47(var1 = new byte[Field33], 0, Field33);
			return var1;
		}
	}
	
	public static final byte[] Method39(short var0) {
		int var1;
		if ((var1 = Method32(var0)) < 0) {
			return null;
		} else {
			return Field39[var1] != null ? Field39[var1] : Method38(var0);
		}
	}
	
	public static final short[] Method40(String var0) {
		return Method41(Method37(var0));
	}
	
	public static final short[] Method41(short var0) {
		if (!Method45(var0)) {
			return null;
		} else {
			short[] var1 = new short[Field33 / 2];
	
			for(int var2 = 0; var2 < var1.length; ++var2) {
				var1[var2] = Method49();
			}
	
			return var1;
		}
	}
	
	public static final int[] Method42(String var0) {
		return Method43(Method37(var0));
	}
	
	public static final int[] Method43(short var0) {
		if (!Method45(var0)) {
			return null;
		} else {
			int[] var1 = new int[Field33 / 4];
	
			for(int var2 = 0; var2 < var1.length; ++var2) {
				var1[var2] = Method52();
			}
	
			return var1;
		}
	}
	
	public static final boolean Method44(String var0) {
		if (Method45(Method37(var0))) {
			return true;
		} else {
			try {
				Object var3 = null;
				InputStream var5;
				if ((var5 = var0.getClass().getResourceAsStream("/" + var0)) == null) {
					return false;
				} else {
					Field32 = new DataInputStream(var5);
					return true;
				}
			} catch (Exception var4) {
				return false;
			}
		}
	}
	
	public static final boolean Method45(short var0) {
		if (Field34) {
			Method1();
		}
	
		int var1;
		Field37 = var1 = Method32(var0);
		Field40 = 0;
		Field38 = false;
		if (var1 >= 0 && Field39[var1] != null) {
			Field33 = Field45[var1];
			Field38 = true;
			return true;
		} else {
			if (var1 != -1) {
				Field33 = Field45[var1];
				if (Field36 == Field44[var1] && Field35 <= Field43[var1]) {
					Method46(Field43[var1] - Field35);
					if (Field42[var1] >= 0 && Field39[var1] == null) {
						Method34(var1);
					}
	
					return true;
				}
	
				try {
					if (Field32 != null) {
						Method53();
					}
	
					Field32 = new DataInputStream(instance.getClass().getResourceAsStream("/" + Field44[var1] + ".bfc"));
					Field32.skip((long)Field43[var1]);
					Field36 = Field44[var1];
					Field35 = Field43[var1];
					if (Field42[var1] >= 0 && Field39[var1] == null) {
						Method34(var1);
					}
	
					return true;
				} catch (Exception var2) {
				}
			}
	
			return false;
		}
	}
	
	public static final void Method46(int var0) {
		if (Field38) {
			Field40 += var0;
		} else {
			Field35 += var0;
	
			try {
				while(var0 > 0) {
					var0 = (int)((long)var0 - Field32.skip((long)var0));
				}
	
			} catch (Exception var1) {
			}
		}
	}
	
	public static final byte[] Method47(byte[] var0, int var1, int var2) {
		if (Field38) {
			System.arraycopy(Field39[Field37], Field40, var0, var1, var2);
			Field40 += var2;
			return var0;
		} else {
			Field35 += var2;
	
			try {
				int var5;
				for(var5 = 0; var2 > 0; var1 += var5) {
					var5 = Field32.read(var0, var1, var2);
					var2 -= var5;
				}
	
				return var0;
			} catch (Exception var4) {
				return null;
			}
		}
	}
	
	public static final int Method48() {
		if (Field38) {
			int var0 = (Field39[Field37][Field40] & 255) << 8 | Field39[Field37][Field40 + 1] & 255;
			Field40 += 2;
			return var0;
		} else {
			Field35 += 2;
	
			try {
				return Field32.readUnsignedShort();
			} catch (Exception var1) {
				return -1;
			}
		}
	}
	
	public static final short Method49() {
		if (Field38) {
			short var0 = (short)(Field39[Field37][Field40] << 8 | Field39[Field37][Field40 + 1] & 255);
			Field40 += 2;
			return var0;
		} else {
			Field35 += 2;
	
			try {
				return Field32.readShort();
			} catch (Exception var1) {
				return -1;
			}
		}
	}
	
	public static final int Method50() {
		if (Field38) {
			int var0 = Field39[Field37][Field40] & 255;
			++Field40;
			return var0;
		} else {
			++Field35;
	
			try {
				return Field32.readUnsignedByte();
			} catch (Exception var1) {
				return -1;
			}
		}
	}
	
	public static final byte Method51() {
		if (Field38) {
			byte var0 = Field39[Field37][Field40];
			++Field40;
			return var0;
		} else {
			++Field35;
	
			try {
				return Field32.readByte();
			} catch (Exception var1) {
				return -1;
			}
		}
	}
	
	public static final int Method52() {
		if (Field38) {
			int var0 = (Field39[Field37][Field40] & 255) << 24 | (Field39[Field37][Field40 + 1] & 255) << 16 | (Field39[Field37][Field40 + 2] & 255) << 8 | Field39[Field37][Field40 + 3] & 255;
			Field40 += 4;
			return var0;
		} else {
			Field35 += 4;
	
			try {
				return Field32.readInt();
			} catch (Exception var1) {
				return -1;
			}
		}
	}
	
	public static final void Method53() {
		try {
			Field32.close();
			Field37 = -1;
			Field36 = -1;
		} catch (Exception var0) {
		}
	
		Field32 = null;
	}
	
	public static final void Method54(short var0) {
		Method55();
		Field52 = var0;
	}
	
	public static final void Method55() {
		Field49 = null;
		Field51 = -1;
	}
	
	public static final String[] Method56(int var0, int var1) {
		if (var1 < 0 && var0 == Field51) {
			return Field49;
		} else {
			boolean var2 = Field34;
			Field34 = false;
			if (!Method45(Field52)) {
				Field49 = null;
				Field50 = null;
			}
	
			Field34 = var2;
			int var3 = Method50();
			int var4 = Method50();
			if (var0 >= var4) {
				return Field49;
			} else {
				Method46(var0 * 2);
				Method46(Method48() + (var4 - var0 - 1) * 2);
				int var6;
				int[] var7 = new int[var6 = Method48()];
				if (var1 < 0) {
					Field49 = new String[var6];
					Field50 = new byte[var6];
					Field51 = var0;
	
					for(int var8 = 0; var8 < var6; ++var8) {
						var7[var8] = Method48();
						Method48();
					}
				} else {
					if (var1 >= var6) {
						return Field49;
					}
	
					Method46(var1 * 4);
					var7[0] = Method48();
					Method46(Method48() + (var6 - var1 - 1) * 4);
				}
	
				for(int var12 = 0; var12 < var6; ++var12) {
					int var10002 = var7[var12]--;
					int var9 = 0;
					char[] var10 = new char[var7[var12]];
					if (var3 == 1) {
						var9 = Method50();
	
						for(int var11 = 0; var11 < var7[var12]; ++var11) {
							var10[var11] = (char)Method50();
						}
					} else {
						var9 = Method48();
	
						for(int var14 = 0; var14 < var7[var12]; ++var14) {
							var10[var14] = (char)Method48();
						}
					}
	
					String var15 = String.valueOf(var10);
					if (var1 >= 0) {
						break;
					}
	
					Field50[var12] = (byte)var9;
					Field49[var12] = var15;
				}
	
				return Field49;
			}
		}
	}
	
	public static final String Method57(int var0) {
		int var1 = var0 >> 16;
		if (Field51 != var1) {
			Method56(var1, -1);
		}
	
		return Field49[var0 & '\uffff'];
	}
	
	public static final int Method58(int var0, int var1) {
		return (int)((long)var0 * (long)var1 >> 16);
	}
	
	public static final int Method59(int var0, int var1) {
		return (int)(((long)var0 << 16) / (long)var1);
	}
	
	public static final int Method60(int var0, int var1, int var2) {
		long var3 = (long)var0;
		long var5 = (long)var1;
		var3 = var3 * var3 >> 16;
		var5 = var5 * var5 >> 16;
		return (int)Method61(var3 + var5, (long)var2);
	}
	
	public static final long Method61(long var0, long var2) {
		if (var2 == 0L) {
			return Method62(var0);
		} else {
			for(int var4 = 0; var4 < 5; ++var4) {
				var2 -= ((var2 * var2 >> 16) - var0 << 16) / (var2 << 2);
			}
	
			return var2;
		}
	}
	
	public static final long Method62(long var0) {
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
	
	public static final int Method63(int var0, int var1) {
		Field53 = var0;
		Field54 = var1;
		if ((Field2 & 2048) > 0 && (Field3 & 2048) == 0) {
			return var0;
		} else {
			return (Field2 & 4096) > 0 && (Field3 & 4096) == 0 ? var1 : -1;
		}
	}
	
	public static final int Method64(int var0, int var1, boolean var2) {
		Field53 = var0;
		Field54 = var1;
		if ((Field2 & 2048) > 0 && (Field3 & 2048) == 0) {
			return var0;
		} else if ((Field2 & 4096) > 0 && (Field3 & 4096) == 0) {
			return var1;
		} else {
			return var2 && var0 >= 0 && (Field2 & 1) > 0 && (Field3 & 1) == 0 ? var0 : -1;
		}
	}
	
	public static final void Method65() {
		if (Field57 != null) {
			if (Field53 > -1) {
				Method80(Field57[Field53], 1, 127 - Field56[Field53], 0);
				Field53 = -1;
			}
	
			if (Field54 > -1) {
				Method80(Field57[Field54], 127 - Field55[Field54], 127 - Field56[Field54], 0);
				Field54 = -1;
			}
	
		}
	}
	
	public static final void Method66() {
		Method68();
		Field57 = new Image[5];
		Field55 = new int[5];
		Field56 = new int[5];
		Field53 = -1;
		Field54 = -1;
	}
	
	public static final void Method67(int var0, Image var1) {
		if (Field57 != null) {
			if (var0 >= 0 && var0 < Field57.length) {
				Field57[var0] = var1;
				Field55[var0] = var1.getWidth();
				Field56[var0] = var1.getHeight();
			}
		}
	}
	
	public static final void Method68() {
		Field57 = null;
		Field55 = null;
		Field56 = null;
	}
	
	public static final void Method69(int var0) {
		Field65 = new short[var0][];
		Field59 = new int[var0];
		Field61 = new int[var0];
		Field60 = new int[var0];
		Field58 = new Image[var0];
		Field62 = new int[var0];
		Field63 = new short[var0][];
		Field64 = new byte[var0][];
	}
	
	public static final void Method70(int var0, short var1, short var2, short var3, byte var4, short var5, int var6, int var7) {
		Field65[var0] = new short[230];
		Field64[var0] = new byte[230];
		byte[] var8 = Method38(var3);
		short[] var9;
		int var10 = (var9 = Method41(var5)).length;
		Field58[var0] = Method12(var1, var2);
		Field63[var0] = new short[var10];
		boolean var11 = false;
	
		for(short var12 = 0; var12 < 230; ++var12) {
			Field65[var0][var12] = -1;
			Field64[var0][var12] = var4;
		}
	
		short var15 = 0;
	
		for(short var13 = 0; var13 < var10; ++var13) {
			int var14 = var9[var13] - 30;
			Field65[var0][var14] = var13;
			Field64[var0][var14] = var8[var13];
			Field63[var0][var13] = var15;
			var15 = (short)(var15 + var8[var13]);
		}
	
		Field61[var0] = Field58[var0].getHeight() / 1;
		Field59[var0] = Field58[var0].getWidth() / var10;
		Field60[var0] = var6;
		Field62[var0] = var7;
	}
	
	public static final int Method71(String var0, int var1) {
		if (Field64[var1] == null) {
			return var0.length() * (Field59[var1] + Field60[var1]) - Field60[var1];
		} else {
			int var2 = 0;
			int var3 = var0.length();
	
			for(int var4 = 0; var4 < var3; ++var4) {
				int var5 = var0.charAt(var4) - 30;
				var2 += Field64[var1][var5];
			}
	
			return var2 + (var3 - 1) * Field60[var1];
		}
	}
	
	public static final int Method72(int var0, int var1) {
		return (var0 + Field60[var1]) / (Field59[var1] + Field60[var1]);
	}
	
	public static final int Method73(int var0, int var1, String var2, int var3) {
		if (Field64[var1] == null) {
			return Method72(var0, var1);
		} else {
			int var4 = var2.length();
	
			int var5;
			for(var5 = var3; var5 < var4; ++var5) {
				int var6;
				if ((var6 = var2.charAt(var5) - 30) < 0) {
					return var5 - var3 + 1;
				}
	
				if ((var0 = var0 - Field64[var1][var6]) < 0) {
					break;
				}
	
				var0 -= Field60[var1];
			}
	
			return var5 - var3;
		}
	}
	
	public static final int[] Method74(int var0, String var1, int var2) {
		if (Field58[var2] == null) {
			return null;
		} else {
			int var3 = 0;
			boolean var4 = false;
			int var5 = 99999;
			int var6 = var1.length();
			int var7 = 0;
			boolean var9 = false;
			int var11;
			if ((var11 = var6 / 8) < 10) {
				var11 = 10;
			}
	
			int[] var12 = new int[var11];
			boolean var13 = false;
			boolean var14 = true;
			int var15 = 0;
	
			while(var7 < var6) {
				switch (var1.charAt(var7)) {
					case '\n':
						if (!var14 && var15 > 0) {
							var12[var3++] = var7;
						}
	
						++var15;
						++var7;
						break;
					case ' ':
						++var7;
						break;
					default:
						var15 = 0;
						var13 = true;
				}
	
				if (var13) {
					var14 = false;
					var13 = false;
					int var10;
					if ((var10 = var7 + Method73(var0, var2, var1, var7)) > var6) {
						var10 = var6;
					}
	
					int var16 = -1;
	
					int var8;
					for(var8 = var7; var8 < var10; ++var8) {
						if (var1.charAt(var8) == '\n') {
							var16 = var8;
							break;
						}
	
						if (var1.charAt(var8) == ' ') {
							var16 = var8;
						}
					}
	
					if (var8 < var6 && var16 > 0 && var1.charAt(var8) != ' ') {
						var8 = var16;
					}
	
					if (var8 >= var5) {
						var8 = var5;
						var6 = 0;
					}
	
					if (var7 > var8) {
						var7 = var8;
					}
	
					var12[var3++] = var7;
					var7 = var8;
				}
			}
	
			int[] var18 = new int[var3];
	
			for(int var17 = 0; var17 < var3; ++var17) {
				var18[var17] = var12[var17];
			}
	
			return var18;
		}
	}
	
	public static final void Method75(int var0) {
		Field66.setColor(var0);
	}
	
	public static final void Method76(int var0, int var1, int var2) {
		Field66.setColor(var0, var1, var2);
	}
	
	public static final int Method77() {
		return Field66.getColor();
	}
	
	public static final void Method78(int var0, int var1, int var2, int var3) {
		Field66.fillRect(var0, var1, var2, var3);
	}
	
	public static final void Method79(int var0, int var1, int var2, int var3) {
		Field66.drawRect(var0, var1, var2, var3);
	}
	
	public static final void Method80(Image var0, int var1, int var2, int var3) {
		Field66.drawImage(var0, var1, var2, var3);
	}
	
	public static final void Method81(int var0, int var1, int var2, int var3) {
		Field66.drawLine(var0, var1, var2, var3);
	}
	
	public static final void Method82(int var0, int var1, int var2, int var3, int var4, int var5) {
		Field66.fillArc(var0, var1, var2, var3, var4, var5);
	}
	
	public static final void Method83(int var0, int var1, int var2, int var3, int var4, int var5) {
		Field66.drawArc(var0, var1, var2, var3, var4, var5);
	}
	
	public static final void Method84(int var0, int var1, int var2, int var3, int var4, int var5) {
		Field66.fillTriangle(var0, var1, var2, var3, var4, var5);
	}
	
	public static final void Method85(int var0, int var1, int var2, int var3) {
		Field66.setClip(var0, var1, var2, var3);
	}
	
	public static final int Method86() {
		return Field66.getClipX();
	}
	
	public static final int Method87() {
		return Field66.getClipY();
	}
	
	public static final int Method88() {
		return Field66.getClipWidth();
	}
	
	public static final int Method89() {
		return Field66.getClipHeight();
	}
	
	public static final void Method90() {
		instance.flushGraphics();
	}
	
	public static final int Method91(int var0, int var1, int var2, int var3, String var4, int var5, int var6, int var7, boolean var8) {
		if (Field58[var5] == null) {
			return -2;
		} else {
			int var9 = var4.length();
			int var10 = var6;
			int var12 = var1 + var3 - (Field61[var5] + Field62[var5] - 1);
			boolean var14 = false;
			boolean var15 = true;
			int var16 = 0;
	
			while(var10 < var9 && var1 < var12) {
				switch (var4.charAt(var10)) {
					case '\n':
						if (!var15 && var16 > 0 && (var1 += Field61[var5] + Field62[var5]) > var12) {
							return var10;
						}
	
						++var16;
						++var10;
						break;
					case ' ':
						++var10;
						break;
					default:
						var16 = 0;
						var14 = true;
				}
	
				if (var14) {
					var15 = false;
					var14 = false;
					int var13;
					if ((var13 = var10 + Method73(var2, var5, var4, var10)) > var9) {
						var13 = var9;
					}
	
					int var17 = -1;
	
					int var11;
					for(var11 = var10; var11 < var13; ++var11) {
						if (var4.charAt(var11) == '\n') {
							var17 = var11;
							break;
						}
	
						if (var4.charAt(var11) == ' ') {
							var17 = var11;
						}
					}
	
					if (var11 < var9 && var17 > 0 && var4.charAt(var11) != ' ') {
						var11 = var17;
					}
	
					if (var11 >= var7) {
						var11 = var7;
						var9 = 0;
					}
	
					if (var10 > var11) {
						var10 = var11;
					}
	
					String var18 = var4.substring(var10, var11);
					if (var8) {
						Method92(var0 + (var2 - Method71(var18, var5)) / 2, var1, var18, var5);
					} else {
						Method92(var0, var1, var18, var5);
					}
	
					var1 += Field61[var5] + Field62[var5];
					var10 = var11;
				}
			}
	
			return var10 < var9 && var4.charAt(var10) != ' ' ? var10 : var10 + 1;
		}
	}
	
	public static final void Method92(int var0, int var1, String var2, int var3) {
		if (Field58[var3] != null) {
			int var4 = Method89();
			int var5 = Method88();
			int var6 = Method86();
			int var7 = Method87();
			if (var1 + Field61[var3] >= var7 && var7 + var4 >= var1) {
				int var8 = var2.length();
				if (var0 == -1000) {
					var0 = (128 - Method71(var2, var3)) / 2;
				}
	
				int var9 = var1;
				int var10 = Field61[var3];
				if (var7 > var1) {
					var10 -= var7 - var1;
					var9 = var7;
				}
	
				if (var1 + var10 > var7 + var4) {
					var10 = var7 + var4 - var9;
				}
	
				int var13 = var0;
	
				for(int var14 = 0; var14 < var8; ++var14) {
					int var15 = var2.charAt(var14) - 30;
					if (var13 <= 128 && var15 >= 0) {
						short var16;
						if ((var16 = Field65[var3][var15]) == -1) {
							if (Field64[var3] == null) {
								var13 += Field59[var3] + Field60[var3];
							} else {
								var13 += Field64[var3][0] + Field60[var3];
							}
						} else {
							int var11 = var13;
							int var12;
							if (Field64[var3] == null) {
								var12 = Field59[var3];
							} else {
								var12 = Field64[var3][var15];
							}
	
							if (var13 + var12 < 0) {
								if (Field64[var3] != null) {
									var13 += Field64[var3][var15] + Field60[var3];
								} else {
									var13 += Field59[var3] + Field60[var3];
								}
							} else {
								if (var13 < var6 || var13 + var12 > var6 + var5) {
									if (var13 + var12 < var6) {
										continue;
									}
	
									if (var13 > var6 + var5) {
										break;
									}
	
									if (var13 < var6) {
										var12 -= var6 - var13;
										var11 = var6;
									}
	
									if (var11 + var12 > var6 + var5) {
										var12 = var6 + var5 - var11;
									}
								}
	
								Method85(var11, var9, var12, var10);
								if (Field64[var3] != null) {
									Method80(Field58[var3], var13 - Field63[var3][var16], var1, 0);
									var13 += Field64[var3][var15] + Field60[var3];
								} else {
									Method80(Field58[var3], var13 - Field59[var3] * var16, var1, 0);
									var13 += Field59[var3] + Field60[var3];
								}
							}
						}
					}
				}
	
				Method85(var6, var7, var5, var4);
			}
		}
	}
	
	public static final void Method93(int var0) {
		Field67 = (long)var0;
	}
	
	public static final int Method94() {
		Field67 = Field67 * 25214903917L + 11L & 281474976710655L;
		return (int)(Field67 >>> 40);
	}
	
	public static final int Method95() {
		Field67 = Field67 * 25214903917L + 11L & 281474976710655L;
		return (int)(Field67 >>> 32);
	}
	
	public static final void Method96() {
		Method258();
	
		do {
			if (Field12) {
				Method262();
				Method65();
				Method90();
				Method2();
				if (Method6(1)) {
					Field12 = false;
				}
			} else if (Field68) {
				Field68 = false;
				Method98(Field70, 2);
				Field69 = Field70;
				Field70 = Field71;
				Method98(Field70, 0);
			} else {
				Method98(Field70, 1);
			}
		} while(Field70 != -1);
	
		Method29();
		Method31();
	}
	
	public static final void Method97(int var0, int var1) {
		Field71 = var0;
		Field72 = var1;
		Field68 = true;
	}
	
	public static final void Method98(int var0, int var1) {
		if (var0 >= 0) {
			switch (var0) {
				case 0:
					switch (var1) {
						case 0:
							Method137(Field72);
							break;
						case 1:
							Method136();
							break;
						case 2:
							Method138();
					}
	
					return;
				case 1:
					switch (var1) {
						case 0:
							Method122(Field72);
							break;
						case 1:
							Method121();
							break;
						case 2:
							Method123();
					}
	
					return;
				case 2:
					switch (var1) {
						case 0:
							Method125(Field72);
							break;
						case 1:
							Method124();
							break;
						case 2:
							Method126();
					}
	
					return;
				case 3:
					switch (var1) {
						case 0:
							Method116(Field72);
							break;
						case 1:
							Method115();
							break;
						case 2:
							Method117();
					}
	
					return;
				case 4:
					switch (var1) {
						case 0:
							Method119(Field72);
							break;
						case 1:
							Method118();
							break;
						case 2:
							Method120();
					}
	
					return;
				case 5:
					switch (var1) {
						case 0:
							Method128(Field72);
							break;
						case 1:
							Method127();
							break;
						case 2:
							Method129();
					}
	
					return;
				case 6:
					switch (var1) {
						case 0:
							Method144(Field72);
							break;
						case 1:
							Method142();
							break;
						case 2:
							Method145();
					}
	
					return;
				case 7:
					switch (var1) {
						case 0:
							Method140(Field72);
							break;
						case 1:
							Method139();
							break;
						case 2:
							Method141();
					}
	
					return;
				case 8:
					switch (var1) {
						case 0:
							Method113(Field72);
							return;
						case 1:
							Method111();
							return;
						case 2:
							Method114();
					}
				default:
			}
		}
	}
	
	public static final void Method99(Display var0) {
		Field73 = var0;
	}
	
	public static final void Method100(int var0) {
		if (Field74) {
			try {
				Field73.vibrate(var0);
			} catch (Exception var1) {
			}
		}
	}
	
	public static final long Method101() {
		return System.currentTimeMillis();
	}
	
	public static final void Method102() {
		Field75 = new int[40];
		Field76 = new int[40];
		Field77 = new int[40];
		Field78 = new int[40];
		Field79 = new int[40];
		Field80 = new int[40];
	}
	
	public static final void Method103() {
		for(int var0 = 0; var0 < 40; ++var0) {
			if (Field75[var0] == 0 || Field79[var0] > 128) {
				Field75[var0] = Field81[Method94() % Field81.length];
				Field76[var0] = Method94() % 40;
				Field78[var0] = Method95() % 128;
				Field79[var0] = -Method95() % 128;
				Field80[var0] = 8 + Method94() % 7;
				Field77[var0] = Method94();
			}
	
			int[] var10000 = Field79;
			var10000[var0] += 128 * (1 + Method94() % 5) / 176;
			var10000 = Field78;
			var10000[var0] += 3 * Field450[Field77[var0]] / 1000;
			var10000 = Field76;
			var10000[var0] += 4 + Method94() % 5;
			if (Field76[var0] >= 40) {
				Field76[var0] = 0;
				Field75[var0] = Field81[Method94() % Field81.length];
			}
	
			var10000 = Field77;
			var10000[var0] += 3 + var0 % 2;
			if (Field77[var0] >= 360) {
				var10000 = Field77;
				var10000[var0] -= 360;
			}
	
			int var1 = Field76[var0] / 4;
			if (Field76[var0] >= 20) {
				var1 = (40 - Field76[var0]) / 4;
			}
	
			Method75(Field75[var0]);
			Method78(Field78[var0], Field79[var0], 128 * Field80[var0] / 176, 128 * var1 / 176);
		}
	
	}
	
	public static final void Method104(int var0, int var1, int var2, int var3, int var4, int var5) {
		int var11 = (var2 - var0) / 7;
		int var12 = (var3 - var1) / 7;
		boolean var13 = false;
		boolean var14 = false;
		boolean var15 = false;
		boolean var16 = false;
	
		for(int var17 = 0; var17 < 2; ++var17) {
			int var8 = var0;
			int var9 = var1;
			int var10 = 111 + Method94() % 140;
			if (var17 == 0) {
				Method76(var10, var10, var10);
			} else {
				Method76(var10, var10, 255);
			}
	
			for(int var18 = 1; var18 < var4; ++var18) {
				int var6 = var8;
				int var7 = var9;
				var8 = var0 + var18 * var11 + Method94() % var5 - var5 / 2;
				var9 = var1 + var18 * var12 + Method94() % var5 - var5 / 2;
				if (var18 == 7) {
					var8 = var2;
					var9 = var3;
				}
	
				Method81(var6, var7, var8, var9);
			}
		}
	
	}
	
	public static final void Method105() {
		if (Field82 == null) {
			Field82 = new int[8];
			Field83 = new int[8];
		}
	
		Method75(13158600);
	
		for(int var0 = 0; var0 < 8; ++var0) {
			if (Field83[var0] > 128 || Field82[var0] == 0 && Field83[var0] == 0) {
				Field82[var0] = Method95() % 149;
				Field83[var0] = Method95() % 21;
			}
	
			Method81(Field82[var0], Field83[var0], Field82[var0] + -3, Field83[var0] + 10);
			int[] var10000 = Field82;
			var10000[var0] += -9;
			var10000 = Field83;
			var10000[var0] += 30;
		}
	
	}
	
	public static final void Method106() {
		if (Field84 == null) {
			Field84 = new int[10];
			Field85 = new int[10];
			Field86 = new int[10];
		}
	
		Method75(16777215);
		if (Field318 == 0) {
			Method75(0);
		}
	
		for(int var0 = 0; var0 < 10; ++var0) {
			if (Field85[var0] > 128 || Field84[var0] == 0 && Field85[var0] == 0) {
				if (Field84[var0] == 0 && Field85[var0] == 0) {
					Field85[var0] = Method95() % 128;
				} else {
					Field85[var0] = -(Method95() % 10);
				}
	
				Field84[var0] = Method95() % 128;
				Field86[var0] = Method95() % 360;
			}
	
			Method81(Field84[var0] - 1, Field85[var0], Field84[var0] + 1, Field85[var0]);
			Method81(Field84[var0], Field85[var0] - 1, Field84[var0], Field85[var0] + 1);
			int[] var10000 = Field84;
			var10000[var0] += 3 * Field450[Field86[var0]] / 1000;
			var10000 = Field85;
			var10000[var0] += 2;
			var10000 = Field86;
			var10000[var0] += 5;
			if (Field86[var0] >= 360) {
				var10000 = Field86;
				var10000[var0] -= 360;
			}
		}
	
	}
	
	public static final void Method107(int var0, int var1, int var2, int var3) {
		if (Field91 != var2 || Field92 != var3 || Field87 == null || Field87.length != var0 || Field87[0].length != var1) {
			Field91 = var2;
			Field92 = var3;
			Field87 = new int[var0][];
			Field88 = new int[var0][];
			Field89 = new int[var0][];
			Field90 = new int[var0][];
	
			for(int var4 = 0; var4 < var0; ++var4) {
				Field87[var4] = new int[var1];
				Field88[var4] = new int[var1];
				Field89[var4] = new int[var1];
				Field90[var4] = new int[var1];
	
				for(int var5 = 0; var5 < var1; ++var5) {
					Field87[var4][var5] = Method95() % var2 << 4;
					Field88[var4][var5] = Method95() % var3 << 4;
					Field89[var4][var5] = -1 - Method94() % 32;
					Field90[var4][var5] = 0;
				}
			}
	
		}
	}
	
	public static final void Method108(int var0, int var1, int var2) {
		Method109(var0, var1, var2, true);
	}
	
	public static final void Method109(int var0, int var1, int var2, boolean var3) {
		if (var0 >= 0 && var0 < Field87.length) {
			Field90[var0][Method95() % Field90[var0].length] = 9;
	
			for(int var7 = 0; var7 < Field87[var0].length; ++var7) {
				int var5;
				int var6;
				int var4 = var5 = var6 = 30 - 7 * Field89[var0][var7];
				if (var7 % 3 == 1) {
					var5 = 0;
					var4 = 0;
				}
	
				if (var7 % 3 == 2) {
					var6 = 0;
					var5 = 0;
				}
	
				Method76(var4, var5, var6);
				if (Field90[var0][var7] > 0) {
					int var10002 = Field90[var0][var7]--;
					if (Field90[var0][var7] > 4) {
						Method75(0);
					} else {
						Method75(16777215);
					}
				}
	
				Method110(var1 + (Field87[var0][var7] >> 4), var2 + (Field88[var0][var7] >> 4));
				if (var3) {
					int[] var10000 = Field87[var0];
					var10000[var7] += Field89[var0][var7];
					if (Field87[var0][var7] < 0) {
						Field87[var0][var7] = Field91 - 1 << 4;
						Field88[var0][var7] = Method95() % Field92 << 4;
						Field89[var0][var7] = -4 - Method94() % 28;
					}
				}
			}
	
		}
	}
	
	public static final void Method110(int var0, int var1) {
		Method81(var0, var1, var0, var1);
		int var2;
		Method76(((var2 = Method77()) & 16711680) >> 17, (var2 & '\uff00') >> 9, (var2 & 255) >> 1);
		Method81(var0, var1 - 1, var0, var1 - 1);
		Method81(var0, var1 + 1, var0, var1 + 1);
		Method81(var0 - 1, var1, var0 - 1, var1);
		Method81(var0 + 1, var1, var0 + 1, var1);
	}
	
	public static final void Method111() {
		Method112();
		if (Method63(2, -1) == 2) {
			Method97(2, 0);
		}
	
		if (Method4(64)) {
			Field95 -= (400 + Field95) / 6;
			Field96 = false;
		}
	
		if (Method4(32)) {
			Field95 += (400 - Field95) / 6;
			Field96 = false;
		}
	
		Field95 = 95 * Field95 / 100;
		if (Field96) {
			Field95 = -255;
		}
	
		Field94 += Field95;
		if (Field94 > 32768) {
			Field94 = 32768;
		}
	
		if (Field94 < -(Field93.length * 14 << 8)) {
			Field94 = -(Field93.length * 14 << 8);
		}
	
		++Field98;
		Method14();
	}
	
	public static final void Method112() {
		Method75(0);
		Method78(0, 0, 128, 128);
		Method109(0, 0, 0, true);
		int var0 = Field94 >> 8;
	
		for(int var1 = 0; var1 < Field93.length; ++var1) {
			if (var0 > -14 && var0 < 128) {
				int var2 = 0;
				if (var0 < 42) {
					var2 = 90 - 90 * var0 * 3 / 128;
				} else if (var0 > 85) {
					var2 = 90 - (128 - var0) * 3 * 90 / 128;
				}
	
				int var3;
				Method92(var3 = 5 + 128 * Method295(var2) / 2600, var0, Field93[var1], 2);
			}
	
			var0 += 14;
		}
	
		Method80(Field97[0], 120, 43 - (Field98 >> 2 & 3), 0);
		Method80(Field97[1], 118, 46 - 39 * ((Field94 >> 8) - 128) / (Field93.length * 14), 0);
		Method80(Field97[2], 120, 92 + (Field98 >> 2 & 3), 0);
	}
	
	public static final void Method113(int var0) {
		if (Field97 == null) {
			Field97 = new Image[3];
	
			for(int var1 = 0; var1 < 3; ++var1) {
				Field97[var1] = Method11("scrollBar" + var1 + ".pim", "scrollBar" + var1 + ".ppl");
			}
		}
	
		Method107(1, 50, 128, 128);
		if (Field93 == null) {
			Field93 = Method249(Method57(196608), 2);
		}
	
		Field94 = 32768;
		Field95 = 0;
		Field96 = true;
		Field98 = 0;
		Method227(-1);
	}
	
	public static final void Method114() {
		Method227(-1);
		Method16();
	}
	
	public static final void Method115() {
		Method75(0);
		Method78(0, 0, 128, 128);
		Method109(0, 0, 0, false);
		if (Field108 > 65) {
			Method80(Field101, Field104, Field105, 0);
			Field104 -= 2;
		}
	
		Method80(Field99, 0, 128 - Field99.getHeight(), 0);
		if (Field106 < 128) {
			Method80(Field100, Field106, Field107, 0);
			Method80(Field422[(int)(Method101() / 100L % 2L)], Field106 + 7 - 10, Field107 + 16 - 3, 0);
			Field106 += 5;
		}
	
		if (Field108 > 50) {
			Method92(-1000, 85, Field102, 0);
		}
	
		if (Field108 > 120 && (Method101() & 512L) > 0L) {
			Method92(-1000, 114, Field103, 0);
		}
	
		if (Field108 < 120 && Method63(-1, 3) == 3 || Field108 > 120 && Method64(2, -1, true) == 2) {
			Method97(4, 0);
		}
	
		++Field108;
		Method14();
	}
	
	public static final void Method116(int var0) {
		Method227(-1);
		Method254(Field328);
		Field102 = Method56(86, -1)[Field328];
		String var2 = "planet";
		var2 = var2 + Field328 % 2 + "_" + Field318 % 5;
		Field99 = Method11(var2 + ".pim", var2 + ".ppl");
		Field100 = Method11("outside.pim", "outside.ppl");
		Field101 = Method11("shipSmall.pim", "shipSmall.ppl");
		Field104 = 128;
		Field105 = 128 - Field99.getHeight() * 4 / 5;
		if (Field105 < 32) {
			Field105 = 32;
		}
	
		Field106 = -61;
		Field107 = 70;
		if (Field422 == null) {
			Field422 = new Image[2];
			Field422[0] = Method11("flame0.pim", "flame0.ppl");
			Field422[1] = Method11("flame1.pim", "flame1.ppl");
		}
	
		if (Field103 == null) {
			Field103 = Method57(65551);
		}
	
		Method107(1, 50, 128, 128);
		Method230();
		Field108 = 0;
	}
	
	public static final void Method117() {
		Method227(-1);
		Method7();
		Field99 = null;
		Field100 = null;
		Field101 = null;
		Method16();
	}
	
	public static final void Method118() {
		switch (Field109) {
			case 0:
				Method250();
				if (Field324) {
					Method131("outro" + Field328 + ".bms", Field298[0], Field299[0]);
					Field109 = 1;
				}
				break;
			case 1:
				Method291();
				Field109 = 2;
			case 2:
				if (Method292()) {
					Field109 = 3;
				}
				break;
			case 3:
				Method97(2, 10);
		}
	
		if (Field391) {
			Method97(2, 0);
		}
	
	}
	
	public static final void Method119(int var0) {
		Field427 = false;
		if (Field69 != 5 && var0 != 1) {
			Field34 = true;
			Field109 = 0;
			Method148();
			Method158();
			Method190();
			Method205();
			Method174();
			Field391 = false;
			Field324 = false;
		}
	
		Field409 = 100;
		if (Field69 != 5 && Field69 != 6) {
			Method229();
		}
	
	}
	
	public static final void Method120() {
		if (Field324) {
			++Field328;
			Method20();
			Field324 = false;
		}
	
		if (Field71 != 5 && Field71 != 6) {
			Method227(-1);
			Method16();
		}
	
	}
	
	public static final void Method121() {
		Method75(16777215);
		Method78(0, 0, 128, 128);
		switch (Field117) {
			case 0:
				Method80(Field110, (128 - Field110.getWidth()) / 2, (128 - Field110.getHeight()) / 2, 0);
				break;
			case 1:
				Method80(Field111, (128 - Field111.getWidth()) / 2, 128 - Field111.getHeight() - 4, 0);
				break;
			case 2:
				Method80(Field110, Field113 - 27, Field114 - 1, 0);
				Method80(Field112, Field113, Field114, 0);
				break;
			case 3:
				int var0 = Field113;
				int var1 = Field114;
				if (Field115 < 20) {
					var0 = Field113 + Method94() % 5 - 2;
					var1 = Field114 + Method94() % 5 - 2;
					Method80(Field110, var0 - 27, var1 - 1, 0);
				} else {
					Field113 += (Field115 - 20) / 8;
					Field114 -= (Field115 - 20) / 8;
					Method80(Field110, 15, 19, 0);
				}
	
				Method80(Field112, var0, var1, 0);
				if (var0 > 158) {
					Field116 = 0L;
				} else {
					Field116 = Method101() + 2000L;
				}
	
				++Field115;
				break;
			case 4:
				int[] var2 = new int[] {0, 16777215, 0, 16777215, 3845831};
				int[] var3 = new int[] {54, 54, 37, 37, 18};
				int[] var4 = new int[] {0, -10, -5, 2, 0};
	
				for(int var5 = 0; var5 < 5; ++var5) {
					Method75(var2[var5]);
					int var6 = Field449[Field115 * 2] * var4[var5] / 1000;
					int var7 = Field449[Field115 * 2] * var3[var5] / 1000;
					if (var5 != 2 && var5 != 3) {
						Method82(64 + var6 - var7 / 2 - 1, 64 - var7 / 2, var7, var7, 0, 360);
					} else {
						Method82(64 + var6 - var7 / 2 - 1, 64 - var7 / 2, var7, var7, 80, 200);
					}
				}
	
				Method80(Field112, Field113, Field114, 0);
				Field113 += 4;
				Field114 -= 4;
				if (Field115 > 45) {
					Field116 = 0L;
				}
	
				++Field115;
				break;
			case 5:
				Method80(Field110, 19, 18, 0);
		}
	
		if (Method101() > Field116 || Method6(1)) {
			Method97(1, Field117 + 1);
		}
	
		Method14();
	}
	
	public static final void Method122(int var0) {
		Field117 = var0;
		switch (var0) {
			case 0:
				Field110 = Method11("eidos.pim", "eidos.ppl");
				Field116 = Method101() + 2500L;
				return;
			case 1:
				Field111 = Method11("eidos_legal_line.pim", "eidos_legal_line.ppl");
				Field116 = Method101() + 2500L;
				return;
			case 2:
			case 3:
				if (Field110 == null) {
					Field110 = Method11("pmback.pim", "pmback.ppl");
				}
	
				if (Field112 == null) {
					Field112 = Method11("pmrocket.pim", "pmrocket.ppl");
				}
	
				Field113 = 42;
				Field114 = 20;
				Field115 = 0;
				Field116 = Method101() + 2000L;
				return;
			case 4:
				Field113 = -49;
				Field114 = 128;
				Field115 = 0;
				Field116 = Method101() + 10000L;
				return;
			case 5:
				Field110 = Method11("mon.pim", "mon.ppl");
				Field116 = Method101() + 2000L;
				return;
			default:
				Method97(7, 0);
				Field34 = true;
		}
	}
	
	public static final void Method123() {
		switch (Field117) {
			case 0:
				Field110 = null;
			case 1:
				Field111 = null;
			case 2:
				break;
			case 3:
				Field110 = null;
				break;
			case 4:
				Field112 = null;
				break;
			case 5:
				Field110 = null;
				break;
			default:
				Field110 = null;
				Field111 = null;
				Field112 = null;
		}
	
		Method16();
	}
	
	public static final void Method124() {
		if (Field393) {
			Method290(Field123);
			Field123 += 4;
			if (Field123 >= 130) {
				Field393 = false;
			}
	
		} else if (Field122) {
			Field122 = false;
			Method131("intro" + Field328 + ".bms", Field298[0], Field299[0]);
		} else if (Field121 > 0) {
			switch (Field121) {
				case 1:
					Method97(3, 0);
					break;
				case 2:
					if (Field128) {
						Method97(-1, 0);
					}
					break;
				case 3:
					if (Field128) {
						Field389 = Field327;
						Method97(4, 0);
						Method225(1);
					}
					break;
				case 4:
					Method254(-1);
					Field165 = 500;
					Field390 = 120;
					Method97(4, 0);
					break;
				case 5:
					if (Field128) {
						Field328 = 0;
						Field325 = false;
						Method20();
						Field427 = false;
						Field348 = -1;
						Method97(7, 0);
						return;
					}
					break;
				case 6:
					Method97(4, 0);
					Method225(1);
			}
	
			Field121 = 0;
		} else {
			if (Field323 && Field299[0] > 9830400 && Field120 > 0) {
				Field121 = Method276(Field120, Field427);
				Field120 = 0;
			}
	
			if (!Field393) {
				Method212();
				Method251();
			}
	
			if (Field423 > 0) {
				if (Field423 == Field119) {
					Field423 = 0;
				} else {
					Field119 = Field423;
				}
			}
	
			if (Field323) {
				Field119 = 0;
			}
	
			if (Field423 > 0) {
				Field120 = Field423;
			}
	
			Field423 = 0;
			Method238(Field298[0], Field320 / 2, 0);
			Method151(Field298[6], Field299[6], 6, 1310720);
			Method277(Field427);
			Method14();
		}
	}
	
	public static final void Method125(int var0) {
		Method21();
		if (Field328 >= 25) {
			Field325 = !Field325;
			Field328 = 0;
			Method20();
			Method97(7, 0);
		} else {
			if (Field348 != 0) {
				Method275();
			}
	
			if (Field118) {
				Field118 = false;
				Method131("intro0controls.bms", Field298[0], Field299[0]);
			}
	
			Field122 = false;
			if (var0 == 10) {
				if (Field393) {
					Field122 = true;
					Field123 = 1;
					Method238(Field298[6], Field320 / 2, 0);
				} else if (Field124) {
					Field124 = false;
					Method131("continueStory.bms", Field298[0], Field299[0]);
					Field122 = true;
				} else {
					Method131("intro" + Field328 + ".bms", Field298[0], Field299[0]);
					if (Field328 == 0) {
						Field118 = true;
					}
				}
			}
	
			Field423 = 0;
			Field404 = true;
			Method107(2, 12, 92, 46);
			Method274();
			Method148();
			if (Field425 == null) {
				Field425 = Method56(6, -1);
				Field426 = Method56(7, -1);
			}
	
			if (!Field427) {
				Method227(2);
			}
	
		}
	}
	
	public static final void Method126() {
		if (Field71 != 5) {
			Method227(-1);
			Method16();
		}
	
	}
	
	public static final void Method127() {
		long var0 = Method101();
		if (!Field136) {
			Field126 = Method135(Field125, Field126, false);
		} else {
			if (!Field134 && !Field135 && Field133 == 0L) {
				if (Field138 == 3) {
					if (Method288()) {
						Field136 = false;
					}
				} else if (!Field144 || Method240(Field139, Field140, 0, 3276800)) {
					Field136 = false;
					Field144 = false;
				}
			}
	
			if (Field133 != 0L && Field133 < var0) {
				Field133 = 0L;
			}
	
			if (Field135 && Method5(1)) {
				Field135 = false;
			}
	
			if (Field134 && Field387) {
				Field134 = false;
			}
		}
	
		switch (Field138) {
			case 0:
				Method280();
				if (Field143 && !Field427) {
					Method156(6, 13107200, true);
				}
	
				Method156(0, 255, true);
				Method279(Field410);
				break;
			case 1:
				if (Field398 != 72) {
					Field132 += (72000 - Field132) / 10;
					Field398 = Field132 / 1000;
				}
	
				if (Field161 < 180) {
					Field161 = 90 * Field161 / 100;
				} else {
					Field161 -= 360;
					Field161 = 90 * Field161 / 100;
					Field161 += 360;
				}
	
				Method252(true);
			case 2:
			default:
				break;
			case 3:
				Method289();
				break;
			case 4:
				Method282();
				break;
			case 5:
				Method299();
		}
	
		if (Field137) {
			boolean var2 = false;
			Method75(0);
			Method78(0, 0, 128, 12);
			Method78(0, 116, 128, 17);
		}
	
		Method247();
		if (Field127) {
			int var3;
			if ((var3 = Method63(2, 4)) >= 0) {
				for(Field128 = var3 == 2; Field126 < Field125.length; Field126 = Method135(Field125, Field126, true)) {
				}
	
				Field136 = false;
			}
		} else if (Field135 && Method63(2, 3) == 3 || !Field135 && Method63(-1, 3) == 3) {
			while(Field126 < Field125.length) {
				Field126 = Method135(Field125, Field126, true);
			}
	
			Field136 = false;
			Method97(6, 100 + Field130);
			return;
		}
	
		Method14();
		if (Field126 >= Field125.length && !Field136) {
			Method97(Field130, 0);
		}
	
	}
	
	public static final void Method128(int var0) {
		Field132 = Field398 * 1000;
		if (Field125 == null) {
			Method97(Field130, 0);
		} else if (!Field131) {
			Field126 = 0;
			Field128 = false;
			Field130 = Field69;
			Field461 = 0;
			Field129 = Field348;
			Field143 = Field129 == 0;
			if (Field143) {
				Field138 = 0;
			} else {
				Field138 = 1;
			}
	
			Method130();
			Method149();
			Method244();
			Field136 = false;
			Field133 = 0L;
			Field144 = false;
			Field134 = false;
			Field135 = false;
		} else {
			Field131 = false;
		}
	}
	
	public static final void Method129() {
		if (!Field131 || Field126 >= Field125.length) {
			Field131 = false;
			Field125 = null;
			Method16();
			Method225(Field129);
			Method130();
		}
	
	}
	
	public static final void Method130() {
		Field137 = false;
		Field424 = 0;
		Field437 = 0;
		Field178[0] = 0;
		Field178[1] = 0;
	}
	
	public static final void Method131(String var0, int var1, int var2) {
		Method132(var0, var1, var2, false);
	}
	
	public static final void Method132(String var0, int var1, int var2, boolean var3) {
		Field34 = false;
		int[] var4 = Method42(var0);
		Field34 = true;
		if (var4 != null) {
			Method134(var4, var1, var2, var3);
		}
	
	}
	
	public static final void Method133(int var0) {
		if (Field165 > 0) {
			int var1 = Field305[var0] - 10;
			Method132("ingame" + Field328 + "-" + var1 + ".bms", Field298[var0], Field299[var0], false);
			Field303[var0] = 0;
		}
	}
	
	public static final void Method134(int[] var0, int var1, int var2, boolean var3) {
		Field141 = var1;
		Field142 = var2;
		Field127 = var3;
		Field125 = var0;
		Method245();
		Method97(5, 0);
	}
	
	public static final int Method135(int[] var0, int var1, boolean var2) {
		while(var1 < var0.length) {
			switch (var0[var1]) {
				case 0:
					return var1 + 1;
				case 1:
					Field136 = true;
					Field133 = Method101() + (long)(var0[var1 + 1] * 10);
					var1 += 2;
					break;
				case 2:
					Field136 = true;
					Field135 = true;
					++var1;
					break;
				case 3:
					if (Field138 == 3) {
						if (var0[var1 + 1] == 6) {
							Method284();
						}
	
						if (var0[var1 + 1] == 5) {
							Method285(0, 0);
						}
	
						var1 += 2;
						break;
					}
	
					switch (var0[var1 + 1]) {
						case 0:
							Field139 = Field298[0];
							Field140 = Field299[0];
							var1 += 2;
							break;
						case 1:
							if (Field143) {
								Field139 = Field298[6];
								Field140 = Field299[6];
							} else {
								Field139 = Field298[0];
								Field140 = Field299[0];
							}
	
							var1 += 2;
							break;
						case 5:
							Field139 = Field141;
							Field140 = Field142;
							var1 += 2;
							break;
						default:
							Field139 = Method222(var0[var1 + 1] << 16);
							Field140 = var0[var1 + 2] << 16;
							var1 += 3;
					}
	
					Method238(Field139, Field140, 0);
					break;
				case 4:
					Field144 = true;
					if (Field138 == 3) {
						if (var0[var1 + 1] == 6) {
							Method286();
						}
	
						if (var0[var1 + 1] == 5) {
							Method287(0, 0);
						}
	
						var1 += 2;
						Field136 = true;
						break;
					}
	
					switch (var0[var1 + 1]) {
						case 0:
							Field139 = Field298[0];
							Field140 = Field299[0];
							var1 += 2;
							break;
						case 1:
							if (Field143) {
								Field139 = Field298[6];
								Field140 = Field299[6];
							} else {
								Field139 = Field298[0];
								Field140 = Field299[0];
							}
	
							var1 += 2;
							break;
						case 5:
							Field139 = Field141;
							Field140 = Field142;
							var1 += 2;
							break;
						default:
							Field139 = Method222(var0[var1 + 1] << 16);
							Field140 = var0[var1 + 2] << 16;
							var1 += 3;
					}
	
					Field136 = true;
					break;
				case 5:
					Method246(Method57(var0[var1 + 1]), var0[var1 + 2]);
					Field134 = true;
					Field136 = true;
					var1 += 3;
					break;
				case 6:
					Field178[var0[var1 + 1]] = var0[var1 + 2];
					if (Field145[var0[var1 + 2]] != -1 && !var2) {
						Method28(Field145[var0[var1 + 2]], 1);
					}
	
					var1 += 3;
					break;
				case 7:
					if (Field138 == 5) {
						Field457 = 128 * var0[var1 + 2] / 1000;
						Field458 = 128 * var0[var1 + 3] / 1000;
					} else if (Field348 == 0) {
						if (var0[var1 + 1] == 0) {
							Method151(Method222(var0[var1 + 2] << 16), var0[var1 + 3] << 16, 0, 786432);
						} else {
							Method151(Method222(var0[var1 + 2] << 16), var0[var1 + 3] << 16, 6, 1310720);
						}
					}
	
					var1 += 4;
					break;
				case 8:
					if (!var2) {
						Field131 = true;
						Method97(6, var0[var1 + 1]);
						var1 += 2;
						return var1;
					}
	
					var1 += 2;
					break;
				case 9:
					Field138 = var0[var1 + 1];
					Method245();
					switch (Field138) {
						case 0:
							Method107(2, 12, 92, 46);
							Method225(0);
							Method151(Field298[6], Field299[6], 6, 1310720);
							Field409 = 100;
							Field398 = 72;
							break;
						case 1:
							if (!Field143) {
								Method225(1);
							}
						case 2:
						case 5:
						default:
							break;
						case 3:
							Method283(72 * var0[var1 + 2] / 100, 72 * var0[var1 + 3] / 100, var0[var1 + 4]);
							Method107(1, 150, 256, 256);
							var1 += 3;
							break;
						case 4:
							Method107(1, 50, 128, 128);
					}
	
					var1 += 2;
					break;
				case 10:
					Method245();
					Field134 = false;
					++var1;
					break;
				case 11:
					Field137 = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 12:
					if (Field138 == 3) {
						Field437 = var0[var1 + 1] == 1 ? 1 : 0;
					}
	
					if (Field138 == 0) {
						Field424 = var0[var1 + 1] == 1 ? 1 : 0;
					}
	
					var1 += 2;
					break;
				case 13:
					if (var0[var1 + 1] == 5) {
						Field438 = var0[var1 + 2] == 1;
					} else {
						Field439 = var0[var1 + 2] == 1;
					}
	
					var1 += 3;
					break;
				case 14:
					Field453 = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 15:
					Field454 = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 16:
					Field455 = var0[var1 + 1] == 1;
					var1 += 2;
					break;
				case 17:
					Field460 = var0[var1 + 1];
					var1 += 2;
				case 18:
				default:
					break;
				case 19:
					Field461 = var0[var1 + 1];
					var1 += 2;
			}
		}
	
		return var1;
	}
	
	public static final void Method136() {
		int var0;
		if ((var0 = Method303()) >= 0) {
			if (Field146 == 0) {
				Field397 = var0;
				Method20();
				Method54((new short[] {133, -24943, -9097, 3400, 20266})[Field397]);
				Method97(0, 1);
				return;
			}
	
			Field16 = var0 == 1 || var0 == 3;
			Method226();
			Method97(1, 0);
		}
	
	}
	
	public static final void Method137(int var0) {
		Field409 = 100;
		Field146 = var0;
		if (Field146 == 0) {
			Method302(new String[] {"flag_uk", "flag_france", "flag_germany", "flag_spain", "flag_italy"}, Field397);
		}
	
		if (Field146 == 1) {
			Method302(new String[] {"audio_off", "audio_on", "audio_off", "audio_on"}, 0);
		}
	
	}
	
	public static final void Method138() {
		Field409 = 100;
		Method301();
	}
	
	public static final void Method139() {
		Method75(0);
		Method78(0, 0, 128, 128);
		Method109(0, 0, 0, true);
		boolean var0 = false;
		boolean var1 = false;
		if (Field147 != null) {
			Method80(Field147, 17, 0, 0);
		}
	
		if (Field148 != null) {
			Method80(Field148, 24, 55, 0);
		}
	
		if ((Method101() & 512L) > 0L) {
			Method92(-1000, 114, Field103, 0);
		}
	
		if (Method64(2, -1, true) == 2) {
			Field149 = true;
			Method97(6, 2);
		}
	
		Method14();
	}
	
	public static final void Method140(int var0) {
		if (Field149) {
			Field149 = false;
			Method97(2, 10);
		} else {
			if (Field103 == null) {
				Field103 = Method57(65551);
			}
	
			Field147 = Method11("gamelogo.pim", "gamelogo.ppl");
			Field148 = Method11("gamelogo2.pim", "gamelogo2.ppl");
			Method107(1, 50, 128, 128);
			Method227(8);
			Field393 = false;
		}
	}
	
	public static final void Method141() {
		Field147 = null;
		Field148 = null;
		Method16();
		Method227(-1);
	}
	
	public static final void Method142() {
		Method14();
	}
	
	public static final void Method143() {
		if (!Field154) {
			if (Field151 == -1L) {
				Field151 = Method101();
			}
	
			int var0 = (int)(Method101() - Field151);
			int var1 = Field155 + 1000 * var0 / Field152;
			if (Field155 == 0 && var1 > 1000) {
				var1 = 1000;
				Method97(Field150, 1);
			}
	
			if (var1 > 2000) {
				Field154 = true;
			} else {
				switch (Field153) {
					case 0:
						Method147(var1);
					default:
						return;
					case 2:
						Method146(var1);
				}
			}
		}
	}
	
	public static final void Method144(int var0) {
		Field155 = 0;
		Field154 = false;
		Field151 = Method101();
		int var1 = Field150;
		Field150 = Field69;
		if (var0 >= 100) {
			Field150 = var0 - 100;
			var0 = 2;
		}
	
		Field153 = var0;
		Field152 = 250;
		if (var0 == 2) {
			Field152 = 150;
		}
	
		if (Field150 == 6) {
			Field150 = var1;
		}
	
	}
	
	public static final void Method145() {
		Field155 = 1000;
		Field151 = -1L;
	}
	
	public static final void Method146(int var0) {
		int var1 = Method77();
		Method75(0);
		Method85(0, 0, 128, 128);
		Method78(0, 0, 128, 128);
		Method75(var1);
	}
	
	public static final void Method147(int var0) {
		int var1 = var0 * 128 / 1000;
		int var2 = Method77();
		Method75(0);
		Method85(0, 0, 128, 128);
		Method78(var1 - 128, 0, 128, 128);
		int var3 = var1 + 1;
		if (var0 > 1000) {
			var3 = 129;
	
			for(int var4 = 6; var4 > 0; --var4) {
				Method78(var1 - var3 - var4, 0, var4, 128);
				var3 += var4 + (8 - var4);
			}
		} else {
			for(int var6 = 6; var6 > 0; --var6) {
				Method78(var3, 0, var6, 128);
				var3 += var6 + (8 - var6);
			}
		}
	
		Method75(var2);
	}
	
	public static final void Method148() {
		if (Field179 == null) {
			Field179 = new Image[2];
			Field179[0] = Method11("eyeLWiz.pim", "eyeLWiz.ppl");
			Field179[1] = Method11("eyeLWaz.pim", "eyeLWaz.ppl");
		}
	
		if (Field181 == null) {
			Field181 = new Image[2];
			Field181[0] = Method11("eyeRWiz.pim", "eyeRWiz.ppl");
			Field181[1] = Method11("eyeRWaz.pim", "eyeRWaz.ppl");
		}
	
		if (Field180 == null) {
			Field180 = new Image[2];
			Field180[0] = Method11("eyeMWiz.pim", "eyeMWiz.ppl");
			Field180[1] = Method11("eyeMWaz.pim", "eyeMWaz.ppl");
		}
	
		if (Field182 == null) {
			Field182 = new Image[2];
			Field182[0] = Method11("eyeCWiz.pim", "eyeCWiz.ppl");
			Field182[1] = Method11("eyeCWaz.pim", "eyeCWaz.ppl");
		}
	
		if (Field186 == null) {
			Field186 = Method11("mouth.pim", "mouth.ppl");
		}
	
		if (Field183 == null) {
			Field183 = new Image[8][];
	
			for(int var0 = 0; var0 < Field183.length; ++var0) {
				Field183[var0] = new Image[2];
				Field183[var0][0] = Method11("mouthWiz_" + var0 + ".pim", "mouthWiz_" + var0 + ".ppl");
				Field183[var0][1] = Method11("mouthWaz_" + var0 + ".pim", "mouthWaz_" + var0 + ".ppl");
			}
		}
	
	}
	
	public static final void Method149() {
		if (Field184 == null) {
			Field184 = new Image[8][];
	
			for(int var0 = 1; var0 < Field184.length; ++var0) {
				Field184[var0] = new Image[2];
				Field184[var0][0] = Method11("eyesWiz_" + var0 + ".pim", "eyesWiz_" + var0 + ".ppl");
				Field184[var0][1] = Method11("eyesWaz_" + var0 + ".pim", "eyesWaz_" + var0 + ".ppl");
			}
	
			Field185 = new Image[8][];
	
			for(int var1 = 1; var1 < Field185.length; ++var1) {
				Field185[var1] = new Image[2];
				Field185[var1][0] = Method11("eyesWiz_C" + var1 + ".pim", "eyesWiz_C" + var1 + ".ppl");
				Field185[var1][1] = Method11("eyesWaz_C" + var1 + ".pim", "eyesWaz_C" + var1 + ".ppl");
			}
		}
	
	}
	
	public static final void Method150(boolean var0) {
		Field165 = 500;
		if (var0) {
			Field162 = 200;
			Field163 = 0;
			Field164 = 0;
			Field172 = 13;
		} else {
			Field162 = 0;
			Field163 = 0;
			Field164 = 200;
			Field172 = 18;
		}
	
		Field160 = 0;
		Field175 = -1;
		Field176 = -1;
	}
	
	public static final void Method151(int var0, int var1, int var2, int var3) {
		Field298[var2 + 0] = var0;
		Field299[var2 + 0] = var1;
		Field298[var2 + 1] = var0;
		Field299[var2 + 1] = var1 - var3;
		Field298[var2 + 2] = var0;
		Field299[var2 + 2] = var1 + var3;
		Field298[var2 + 3] = var0 - var3;
		Field299[var2 + 3] = var1;
		Field298[var2 + 4] = var0 + var3;
		Field299[var2 + 4] = var1;
	
		for(int var4 = var2; var4 < var2 + 5; ++var4) {
			Field301[var4] = Field298[var4];
			Field302[var4] = Field299[var4];
		}
	
	}
	
	public static final void Method152() {
		Field206[16] = false;
		Field205[16] = false;
		Field303[Field200[16]] = Field175;
		Field175 = -1;
		Field176 = Field200[16];
		Field200[16] = -1;
	}
	
	public static final void Method153() {
		if (Field176 != -1 && (Field298[Field176] - Field300[Field176] > Field298[0] + 1638400 || Field298[Field176] + Field300[Field176] < Field298[0] - 1638400 || Field299[Field176] - Field300[Field176] > Field299[0] + 1638400 || Field299[Field176] + Field300[Field176] < Field299[0] - 1638400)) {
			Field176 = -1;
		}
	
		if (Field173 > 0) {
			int[] var10000 = Field299;
			var10000[0] -= 550000;
			--Field173;
		}
	
		if (Field160 > 0) {
			--Field160;
			if (Field160 > 25) {
				Field160 = 25;
			}
		}
	
		if (Field160 < 0) {
			++Field160;
			if (Field160 < -25) {
				Field160 = -25;
			}
		}
	
		Field161 += Field160;
		if (Field161 >= 360) {
			Field161 -= 360;
		}
	
		if (Field161 < 0) {
			Field161 += 360;
		}
	
		if (Field160 == 0) {
			byte var0 = 95;
			if (!Field323) {
				var0 = 90;
			}
	
			if (Field161 < 180) {
				Field161 = var0 * Field161 / 100;
			} else {
				Field161 -= 360;
				Field161 = var0 * Field161 / 100;
				Field161 += 360;
			}
	
			if (Field161 >= 360) {
				Field161 -= 360;
			}
	
			if (Field161 < 0) {
				Field161 += 360;
			}
		}
	
	}
	
	public static final void Method154(int var0, int var1) {
		int var2 = Method241(var0, var1);
		int var3 = Method242(var0, var1);
		int var4 = Field398 * 10 / 100;
		int var5 = Field398 * 25 / 100;
		if (!Method273(var2 - var4, var3 - var5, var2 + var4, var3 + var5)) {
			Method75(0);
			Method82(var2 - var4 - 2, var3 - var5 - 2, 2 * (var4 + 2), 2 * (var5 + 2), 0, 360);
			Method75(10494192);
			Method82(var2 - var4, var3 - var5, 2 * var4, 2 * var5, 0, 360);
			Method80(Field186, var2 - Field186.getWidth() / 2, var3 - Field186.getHeight() / 2, 0);
			var2 -= Field180[1].getWidth() / 2;
			Method80(Field180[1], var2 - var4, var3 - 2 * var5 / 3, 0);
			Method80(Field180[1], var2, var3 - 2 * var5 / 3, 0);
			Method80(Field180[1], var2 + var4, var3 - 2 * var5 / 3, 0);
		}
	}
	
	public static final void Method155() {
		if (Field165 > 0) {
			Field162 = 100 + 100 * Field165 / 500;
		}
	
		if ((Field174 & 1) > 0) {
			Method156(0, 0, false);
		} else {
			Method156(0, (Field162 << 16) + (Field163 << 8) + Field164, false);
		}
	}
	
	public static final void Method156(int var0, int var1, boolean var2) {
		byte var3 = 0;
		if (var0 > 0 || var0 == 0 && Field348 == 1) {
			var3 = 1;
		}
	
		if (var3 == 0) {
			Field172 = 18;
			var1 = 255;
			if (Field327 == -1) {
				var1 = 100 + 155 * Field165 / 500 & 255;
			}
		} else {
			Field172 = 13;
		}
	
		int var4 = Method241(Field298[var0], Field299[var0]);
		int var5 = Method242(Field298[var0], Field299[var0]);
		int[] var6 = new int[4];
		if (Field374 > 45 && Field374 < 315) {
			if (Field374 >= 45 && Field374 <= 135) {
				var6[0] = Field298[var0 + 0] - Field298[var0 + 3] >> 16;
				var6[1] = Field299[var0 + 0] - Field299[var0 + 1] >> 16;
				var6[2] = Field298[var0 + 4] - Field298[var0 + 0] >> 16;
				var6[3] = Field299[var0 + 2] - Field299[var0 + 0] >> 16;
			} else if (Field374 >= 135 && Field374 <= 225) {
				var6[0] = Field299[var0 + 2] - Field299[var0 + 0] >> 16;
				var6[1] = Field298[var0 + 0] - Field298[var0 + 3] >> 16;
				var6[2] = Field299[var0 + 0] - Field299[var0 + 1] >> 16;
				var6[3] = Field298[var0 + 4] - Field298[var0 + 0] >> 16;
			} else {
				var6[0] = Field298[var0 + 4] - Field298[var0 + 0] >> 16;
				var6[1] = Field299[var0 + 2] - Field299[var0 + 0] >> 16;
				var6[2] = Field298[var0 + 0] - Field298[var0 + 3] >> 16;
				var6[3] = Field299[var0 + 0] - Field299[var0 + 1] >> 16;
			}
		} else {
			var6[0] = Field299[var0 + 0] - Field299[var0 + 1] >> 16;
			var6[1] = Field298[var0 + 4] - Field298[var0 + 0] >> 16;
			var6[2] = Field299[var0 + 2] - Field299[var0 + 0] >> 16;
			var6[3] = Field298[var0 + 0] - Field298[var0 + 3] >> 16;
		}
	
		for(int var7 = 0; var7 < 4; ++var7) {
			var6[var7] = Field398 * var6[var7] / 100;
		}
	
		int[] var13 = new int[4];
	
		for(int var8 = 0; var8 < 4; ++var8) {
			var13[var8] = var6[var8] << 1;
		}
	
		Method265(0);
		Method85(var4, 0, 200, var5);
		Method82(var4 - var6[1] - 2, var5 - var6[0] - 2, var13[1] + 4, var13[0] + 4, 0, 360);
		Method85(0, 0, var4, var5);
		Method82(var4 - var6[3] - 2, var5 - var6[0] - 2, var13[3] + 4, var13[0] + 4, 90, 360);
		Method85(0, var5, var4, 200);
		Method82(var4 - var6[3] - 2, var5 - var6[2] - 2, var13[3] + 4, var13[2] + 4, 180, 360);
		Method85(var4, var5, 200, 200);
		Method82(var4 - var6[1] - 2, var5 - var6[2] - 2, var13[1] + 4, var13[2] + 4, 270, 360);
		Method265(var1);
		Method85(var4, 0, 200, var5);
		Method82(var4 - var6[1], var5 - var6[0], var6[1] * 2, var6[0] * 2, 0, 360);
		Method85(0, 0, var4, var5);
		Method82(var4 - var6[3], var5 - var6[0], var6[3] * 2, var6[0] * 2, 90, 360);
		Method85(0, var5, var4, 200);
		Method82(var4 - var6[3], var5 - var6[2], var6[3] * 2, var6[2] * 2, 180, 360);
		Method85(var4, var5, 200, 200);
		Method82(var4 - var6[1], var5 - var6[2], var6[1] * 2, var6[2] * 2, 270, 360);
		Method85(0, 0, 128, 128);
		if (Field409 > 50 && Field165 > 0) {
			int var14 = Field161;
			if (var2) {
				var14 = 0;
			}
	
			if (Field175 >= 0) {
				Image var9;
				Method80(var9 = Field186, var4 - var9.getWidth() / 2, var5 - var9.getHeight() / 2, 0);
			} else {
				Image var15 = Field183[Field178[var3]][var3];
				if (Field178[var3] > 0) {
					Method80(var15, var4 - var15.getWidth() / 2 + Field189[Field178[var3]][var3], var5 - var15.getHeight() / 2 + Field190[Field178[var3]][var3], 0);
				} else {
					int var10 = 6 * Field398 / 100;
					int var11 = var4 + var10 * Method296(var14 + 90) / 1000;
					int var12 = var5 + var10 * Method295(var14 + 90) / 1000;
					Method80(var15, var11 - var15.getWidth() / 2, var12 - var15.getHeight() / 2, 0);
				}
			}
	
			if (Field178[var3] == 0) {
				Image var16 = Field180[var3];
				if (var0 > 0) {
					if (Field298[0] + 1441792 < Field298[var0]) {
						var16 = Field179[var3];
					}
	
					if (Field298[0] - 1441792 > Field298[var0]) {
						var16 = Field181[var3];
					}
				} else {
					if (Field298[var0] + 65536 < Field301[var0]) {
						var16 = Field179[var3];
					}
	
					if (Field298[var0] - 65536 > Field301[var0]) {
						var16 = Field181[var3];
					}
				}
	
				if (Field177[var3] > 0) {
					var16 = Field182[var3];
				}
	
				int var18 = Field172 * Field398 / 100;
				int var20 = var4 + var18 * Method296(var14 - 115) / 1000;
				int var22 = var5 + var18 * Method295(var14 - 115) / 1000;
				Method80(var16, var20 - var16.getWidth() / 2, var22 - var16.getHeight() / 2, 0);
				var20 = var4 + var18 * Method296(var14 - 65) / 1000;
				var22 = var5 + var18 * Method295(var14 - 65) / 1000;
				Method80(var16, var20 - var16.getWidth() / 2, var22 - var16.getHeight() / 2, 0);
			} else {
				int var17 = Field172 * Field398 / 100;
				Image var19 = Field184[Field178[var3]][var3];
				if (Field177[var3] > 0) {
					var19 = Field185[Field178[var3]][var3];
				}
	
				Method80(var19, var4 - var19.getWidth() / 2 + Field187[Field178[var3]][var3], var5 - var17 - var19.getHeight() / 2 + Field188[Field178[var3]][var3], 0);
			}
	
			if (Field177[var3] > 0 || Method94() % 50 == var3) {
				if (Field177[var3] > 0) {
					int var10002 = Field177[var3]--;
					return;
				}
	
				Field177[var3] = 3;
			}
		}
	
	}
	
	public static final void Method157() {
		int var0 = Field158;
		Field158 = Field191;
		Field191 = var0;
		var0 = Field175;
		Field175 = Field193;
		Field193 = var0;
		var0 = Field159;
		Field159 = Field192;
		Field192 = var0;
		var0 = Field160;
		Field160 = Field194;
		Field194 = var0;
		var0 = Field161;
		Field161 = Field195;
		Field195 = var0;
	}
	
	public static final void Method158() {
		if (Field196 == null) {
			Field196 = Method11("fuse.pim", "fuse.ppl");
		}
	
		if (Field197 == null) {
			Field197 = new Image[2];
			Field197[0] = Method11("bombbase.pim", "bombbase.ppl");
			Field197[1] = Method11("bombbutton.pim", "bombbutton.ppl");
		}
	
	}
	
	public static final void Method159(int var0, int var1) {
		int var2 = Method241(var0, var1);
		int var3 = Method242(var0, var1);
		if (Field167 + 200 > Field395) {
			Method80(Field197[1], var2 - 9, var3 - 11 - 2, 0);
		} else {
			Method80(Field197[1], var2 - 9, var3 - 11 - 5, 0);
		}
	
		Method80(Field197[0], var2 - 14, var3 - 11, 0);
	}
	
	public static final void Method160(int var0) {
		Field198 = var0;
		Field199 = new int[var0];
		Field200 = new int[var0];
		Field201 = new int[var0];
		Field202 = new int[var0];
		Field203 = new int[var0];
		Field204 = new int[var0];
		Field206 = new boolean[var0];
		Field205 = new boolean[var0];
	}
	
	public static final void Method161(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7) {
		Field199[var0] = var1;
		Field200[var0] = var2;
		Field201[var0] = var3;
		Field202[var0] = var4;
		Field203[var0] = Method58(var4, var4);
		Field204[var0] = var5;
		Field206[var0] = var7;
		Field205[var0] = var6;
	}
	
	public static final void Method162(int var0) {
		for(int var1 = 0; var1 < Field198; ++var1) {
			if (Field206[var1] && (Field199[var1] == var0 || Field200[var1] == var0)) {
				Field206[var1] = false;
				if (Field200[16] == Field199[var1] || Field200[16] == Field200[var1]) {
					Method152();
				}
			}
		}
	
	}
	
	public static final void Method163() {
		for(int var0 = 0; var0 < Field198; ++var0) {
			if (Field206[var0]) {
				if (Field201[var0] == 0) {
					Field298[Field200[var0]] = Field298[Field199[var0]];
				} else if (Field201[var0] == 1) {
					Field299[Field200[var0]] = Field299[Field199[var0]];
				} else if (Field201[var0] == 6) {
					if (Field299[Field199[var0]] > Field299[Field200[var0]] - Field202[var0]) {
						Field299[Field199[var0]] = Field299[Field200[var0]] - Field202[var0];
					}
				} else if (Field201[var0] == 5) {
					if (Field298[Field199[var0]] > Field298[Field200[var0]] - Field202[var0]) {
						Field298[Field199[var0]] = Field298[Field200[var0]] - Field202[var0];
					}
				} else {
					Method164(Field199[var0], Field200[var0], Field202[var0], Field204[var0], Field201[var0], false);
				}
			}
		}
	
	}
	
	public static final void Method164(int var0, int var1, int var2, int var3, int var4, boolean var5) {
		int var6 = Field298[var1] - Field298[var0];
		int var7 = Field299[var1] - Field299[var0];
		int var8 = Method60(var6, var7, var2);
		if (var4 != 4 || var8 >= var2) {
			if (var4 != 3 || var8 <= var2) {
				if (var8 >= 70000) {
					int var9 = Field304[var0];
					int var10 = Field304[var1];
					int var11 = Method59(var8 - var2, Method58(var8, var9 + var10));
					int var12 = Method58(var3, Method58(var6, var11));
					int var13 = Method58(var3, Method58(var7, var11));
					if (Field306[var0]) {
						int[] var10000 = Field298;
						var10000[var0] += Method58(var12, var9);
						var10000 = Field299;
						var10000[var0] += Method58(var13, var9);
					}
	
					if (Field306[var1]) {
						int[] var15 = Field298;
						var15[var1] -= Method58(var12, var10);
						var15 = Field299;
						var15[var1] -= Method58(var13, var10);
					}
	
					if (var5) {
						Method257(var0, var1);
					}
	
				}
			}
		}
	}
	
	public static final void Method165() {
		int var0 = Field198;
		Field198 = Field207;
		Field207 = var0;
		int[] var1 = Field199;
		Field199 = Field208;
		Field208 = var1;
		var1 = Field200;
		Field200 = Field209;
		Field209 = var1;
		var1 = Field201;
		Field201 = Field210;
		Field210 = var1;
		var1 = Field202;
		Field202 = Field211;
		Field211 = var1;
		var1 = Field203;
		Field203 = Field212;
		Field212 = var1;
		var1 = Field204;
		Field204 = Field213;
		Field213 = var1;
		boolean[] var2 = Field205;
		Field205 = Field214;
		Field214 = var2;
		var2 = Field206;
		Field206 = Field215;
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
		Field217[var0] = Field298[var1] - var3;
		Field218[var0] = Field298[var1] + var3;
		if (var4 == 0) {
			var4 = 1;
		}
	
		Field219[var0] = var4;
		Field220[var0] = var4;
		Field221[var0] = var2;
	}
	
	public static final void Method168(int var0) {
		for(int var1 = 0; var1 < Field216.length; ++var1) {
			if (Field221[var1] == var0) {
				Field219[var1] = 400000;
				Field304[Field216[var1]] = 52000;
	
				for(int var2 = 0; var2 < Field198; ++var2) {
					if ((Field199[var2] == Field216[var1] || Field200[var2] == Field216[var1]) && !Field205[var2]) {
						Field206[var2] = false;
					}
				}
			}
		}
	
	}
	
	public static final void Method169() {
		if (Field216.length > 0) {
			for(int var0 = 0; var0 < Field216.length; ++var0) {
				int var1 = Field216[var0];
				if (Field219[var0] > 0) {
					int var9 = Field219[var0]--;
					if (Field219[var0] == 0) {
						int var4 = Method94() % 17;
						int[] var7 = Field298;
						var7[var1] += var4 - 10 << 14;
						var7 = Field299;
						var7[var1] += var4 - 8 << 14;
						if (Field298[var1] < Field217[var0]) {
							Field219[var0] = -Field220[var0];
	
							for(int var5 = 0; var5 < Field216.length; ++var5) {
								if (Field221[var0] == Field221[var5]) {
									Field219[var5] = Field219[var0];
								}
							}
						} else {
							Field219[var0] = Field220[var0];
						}
					}
				} else {
					int var10002 = Field219[var0]++;
					if (Field219[var0] >= 0) {
						int var2 = Method94() % 17;
						int[] var10000 = Field298;
						var10000[var1] += var2 - 6 << 14;
						var10000 = Field299;
						var10000[var1] += var2 - 8 << 14;
						if (Field298[var1] > Field218[var0]) {
							Field219[var0] = Field220[var0];
	
							for(int var3 = 0; var3 < Field216.length; ++var3) {
								if (Field221[var0] == Field221[var3]) {
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
	
	public static final void Method170(int var0) {
		Field223 = new int[var0];
		Field224 = new boolean[var0];
		Field225 = new boolean[var0];
		Field226 = new int[var0];
		Field227 = new int[var0];
		Field228 = new int[var0];
		Field222 = 0;
	}
	
	public static final void Method171(int var0, int var1, boolean var2, int var3, int var4) {
		Field223[var0] = var1;
		Field224[var0] = var2;
		Field225[var0] = false;
		Field226[var0] = var3;
		Field227[var0] = var4;
		Field228[var0] = Field300[var1];
		int[] var10000 = Field303;
		var10000[var1] |= 16384;
	}
	
	public static final void Method172(int var0) {
		for(int var1 = 0; var1 < Field223.length; ++var1) {
			if (Field223[var1] == var0 && Field224[var1]) {
				Field225[var1] = true;
			}
		}
	
	}
	
	public static final void Method173() {
		++Field222;
	
		for(int var0 = 0; var0 < Field223.length; ++var0) {
			if (Field300[Field223[var0]] > Field228[var0]) {
				int[] var10000 = Field300;
				int var10001 = Field223[var0];
				var10000[var10001] -= (Field300[Field223[var0]] - Field228[var0]) / 20;
			}
	
			if (Field225[var0] || Field226[var0] > 0 && Field222 % Field226[var0] == 0) {
				Field300[Field223[var0]] = Field228[var0] + Field227[var0];
				Field225[var0] = false;
			}
		}
	
	}
	
	public static final void Method174() {
		if (Field230 == null && Field229 != null && Field229.length > 0) {
			Field230 = Method11("grabber.pim", "grabber.ppl");
		}
	
	}
	
	public static final void Method175(int var0) {
		Field229 = new int[var0];
	}
	
	public static final void Method176(int var0, int var1) {
		Field229[var0] = var1;
	}
	
	public static final void Method177() {
		for(int var0 = 0; var0 < Field229.length; ++var0) {
			int var1;
			if ((var1 = Field229[var0]) != Field200[16] && Field303[var1] != 0) {
				Method268(Field230, Field298[var1], Field299[var1]);
			}
		}
	
	}
	
	public static final void Method178() {
		int var0 = 0;
		Field232 = new int[3];
		Field234 = 3;
		int[] var1 = new int[3];
		Field233 = new int[3][];
		Field233[0] = new int[] {-1, -1, -1, -1, -1, -1};
		Field233[1] = new int[] {-1, -1, -1, -1, -1, -1};
		Field233[2] = new int[] {-1, -1, -1, -1, -1, -1};
	
		for(int var2 = 0; var2 < Field297; ++var2) {
			if (Field305[var2] == 13) {
				Field231 = var2;
			}
	
			if (Field305[var2] == 15) {
				Field232[var0++] = var2;
			}
	
			if ((Field303[var2] & 128) > 0 && Field305[var2] == 7) {
				int var3 = (Field299[var2] >> 16) / 100 - 1;
				Field233[var3][var1[var3]++] = var2;
				int[] var10000 = Field303;
				var10000[var2] &= -2;
	
				for(int var4 = 0; var4 < Field245.length; ++var4) {
					if (Field245[var4] == var2) {
						Field257[var4] = false;
					}
				}
			}
		}
	
		if (var0 == 1) {
			int[] var5;
			(var5 = new int[1])[0] = Field232[0];
			Field232 = var5;
		}
	
	}
	
	public static final void Method179() {
		for(int var0 = 0; var0 < Field232.length; ++var0) {
			if (Field300[Field232[var0]] > 0 && Field298[Field232[var0]] > Field298[Field231] - Field300[Field231] && Field298[Field232[var0]] < Field298[Field231] + Field300[Field231]) {
				int[] var10000 = Field300;
				int var10001 = Field232[var0];
				var10000[var10001] -= 16384;
				if (Field300[Field232[var0]] <= 0) {
					Field303[Field232[var0]] = 0;
					Method162(Field232[var0]);
				}
			}
		}
	
		if (Field235 > 0) {
			--Field235;
			if (Field235 == 85) {
				Method180();
				Method131("ping_hit.bms", Field298[Field231], Field299[Field231] - 2 * Field300[Field231] / 3);
				return;
			}
		} else if (Field234 < 0) {
			Field324 = true;
		}
	
	}
	
	public static final void Method180() {
		if (Field235 == 0) {
			Method131("ping_inplace.bms", Field298[Field231], Field299[Field231] - 2 * Field300[Field231] / 3);
		}
	
		--Field234;
		if (Field234 >= 0) {
			for(int var0 = 0; var0 < Field233[Field234].length; ++var0) {
				if (Field233[Field234][var0] != -1) {
					int var1 = Field233[Field234][var0];
					int[] var10000 = Field303;
					var10000[var1] |= 1;
	
					for(int var2 = 0; var2 < Field245.length; ++var2) {
						if (Field245[var2] == var1) {
							Field257[var2] = true;
						}
					}
				}
			}
		}
	
	}
	
	public static final void Method181() {
		Method266(160, 32, 240);
	
		for(int var0 = 0; var0 < Field232.length; ++var0) {
			Method270(Field298[Field232[var0]], Field299[Field232[var0]], Field300[Field232[var0]]);
		}
	
		Method270(Field298[Field231], Field299[Field231], Field300[Field231]);
		int var7 = 2 * Field300[Field231] / 5;
		int var1 = Field298[Field231];
		int var2 = Field299[Field231] - 7 * Field300[Field231] / 10;
		int var3 = Field300[Field231] / 5;
	
		for(int var4 = -1; var4 < 2; ++var4) {
			Method265(0);
			Method270(var1 + var4 * var7, var2, var3);
			if (var4 < Field234) {
				Method265(16777215);
				Method270(var1 + var4 * var7, var2, var3);
				Method265(0);
				int var5 = Method182(var1 + var4 * var7, Field372, 9830400, 3 * var3 / 4);
				int var6 = Method182(var2, Field373, 6553600, 3 * var3 / 4);
				Method270(var1 + var4 * var7 + var5, var2 + var6, var3 / 3);
			} else {
				Method266(160, 32, 240);
				Method270(var1 + var4 * var7, var2, var3);
			}
		}
	
	}
	
	public static final int Method182(int var0, int var1, int var2, int var3) {
		int var4;
		if ((var4 = 90 * (var0 - (var1 - var2)) / var2) < 0) {
			var4 = 0;
		}
	
		if (var4 >= 180) {
			var4 = 179;
		}
	
		return var3 / 1500 * Field450[var4];
	}
	
	public static final void Method183() {
		if (Field235 <= 0) {
			Field235 = 100;
			if (Field234 < 1) {
				Field234 = -1;
				Field235 = 80;
			}
	
		}
	}
	
	public static final void Method184(int var0) {
		Field245 = new int[var0];
		Field246 = new boolean[var0];
		Field247 = new int[var0];
		Field248 = new int[var0];
		Field249 = new boolean[var0];
		Field250 = new int[var0];
		Field251 = new int[var0];
		Field252 = new int[var0];
		Field256 = new boolean[var0];
		Field257 = new boolean[var0];
		Field258 = new boolean[var0];
		Field255 = new int[var0];
		Field242 = new int[var0];
		Field253 = new int[var0];
		Field254 = new int[var0];
		Field244 = 0;
	}
	
	public static final void Method185(int var0, int var1, boolean var2, int var3, int var4, boolean var5, int var6, int var7, int var8, boolean var9, int var10) {
		Field245[var0] = var1;
		Field246[var0] = var2;
		Field247[var0] = var3;
		Field248[var0] = var4;
		Field249[var0] = var5;
		Field250[var0] = var6;
		Field251[var0] = var7;
		Field252[var0] = var8;
		Field256[var0] = var9;
		Field257[var0] = true;
		Field253[var0] = 1;
		Field242[var0] = 0;
		Field255[var0] = var10;
		Field254[var0] = Field298[var1];
		Field258[var0] = (Field303[var1] & 1) == 0;
	}
	
	public static final void Method186() {
		++Field244;
	
		for(int var0 = 0; var0 < Field245.length; ++var0) {
			if (Field257[var0]) {
				int var1 = Field244 + Field255[var0];
				if (Field246[var0] && var1 % Field247[var0] == 0) {
					int[] var10000 = Field299;
					int var10001 = Field245[var0];
					var10000[var10001] -= Field248[var0];
				}
	
				if (Field249[var0]) {
					if (var1 % Field250[var0] == 0) {
						int[] var2 = Field298;
						int var3 = Field245[var0];
						var2[var3] += Field253[var0] * Field251[var0];
						if (Field256[var0] && Method94() % 50 == 49) {
							Field253[var0] = -Field253[var0];
						}
					}
	
					if (Field298[Field245[var0]] > Field254[var0] + Field252[var0]) {
						Field253[var0] = -1;
					}
	
					if (Field298[Field245[var0]] < Field254[var0] - Field252[var0]) {
						Field253[var0] = 1;
					}
				}
			}
		}
	
	}
	
	public static final boolean Method187(int var0) {
		for(int var1 = 0; var1 < Field245.length; ++var1) {
			if (Field245[var1] == var0) {
				if (!Field246[var1] && !Field249[var1]) {
					return true;
				}
	
				return false;
			}
		}
	
		return false;
	}
	
	public static final boolean Method188(int var0) {
		for(int var1 = 0; var1 < Field245.length; ++var1) {
			if (Field245[var1] == var0) {
				return Field257[var1];
			}
		}
	
		return false;
	}
	
	public static final void Method189(int var0) {
		for(int var1 = 0; var1 < Field245.length; ++var1) {
			if (Field245[var1] == var0 && Field257[var1]) {
				Field257[var1] = false;
				int[] var10000 = Field303;
				var10000[var0] |= 1;
				if (!Field246[var1] && !Field249[var1]) {
					Method168(Field255[var1]);
				}
	
				return;
			}
		}
	
	}
	
	public static final void Method190() {
		if (Field245 != null && Field245.length != 0) {
			if (Field241 == null) {
				Field241 = new Image[2];
				Field241[0] = Method11("spiderC.pim", "spiderC.ppl");
				Field241[1] = Method11("spiderD.pim", "spiderD.ppl");
			}
	
			if (Field239 == null) {
				Field239 = new Image[6];
				Field240 = new Image[6];
	
				for(int var0 = 0; var0 <= 5; ++var0) {
					Field239[var0] = Method11("bird_left" + var0 + ".pim", "bird_left" + var0 + ".ppl");
					Field240[var0] = Method11("bird_right" + var0 + ".pim", "bird_right" + var0 + ".ppl");
				}
			}
	
			if (Field236 == null) {
				Field236 = new Image[3];
	
				for(int var1 = 0; var1 < 3; ++var1) {
					Field236[var1] = Method11("pointy_roll_" + var1 + ".pim", "pointy_roll_" + var1 + ".ppl");
				}
			}
	
			if (Field237 == null) {
				Field237 = new Image[2];
	
				for(int var2 = 0; var2 < 2; ++var2) {
					Field237[var2] = Method11("pointy_eyes_" + var2 + ".pim", "pointy_eyes_" + var2 + ".ppl");
				}
			}
	
			if (Field238 == null) {
				Field238 = new Image[2];
	
				for(int var3 = 0; var3 < 2; ++var3) {
					Field238[var3] = Method11("pointy_mouth_" + var3 + ".pim", "pointy_mouth_" + var3 + ".ppl");
				}
			}
	
		}
	}
	
	public static final void Method191() {
		for(int var0 = 0; var0 < Field245.length; ++var0) {
			Method192(Field245[var0], var0);
		}
	
	}
	
	public static final void Method192(int var0, int var1) {
		if (Field300[var0] >= 30000) {
			int var2 = Method241(Field298[var0], Field299[var0]);
			int var3 = Method242(Field298[var0], Field299[var0]);
			if (Field258[var1]) {
				int[] var11 = Field242;
	
				for(var11[var1] += (Field298[var0] - Field301[var0]) / 7000; Field242[var1] >= 500; var11[var1] -= 500) {
					var11 = Field242;
				}
	
				while(Field242[var1] < 0) {
					var11 = Field242;
					var11[var1] += 500;
				}
	
				if (!Field257[var1]) {
					Field242[var1] = 510;
				}
	
				Image var8;
				if (Field301[var0] > Field298[var0]) {
					var8 = Field239[Field242[var1] / 100];
				} else {
					var8 = Field240[Field242[var1] / 100];
				}
	
				Method80(var8, var2 - var8.getWidth() / 2, var3 - var8.getHeight() / 2, 0);
			} else if (!Field246[var1] && !Field249[var1]) {
				Image var7 = Field241[0];
				if (!Field257[var1]) {
					var7 = Field241[1];
				}
	
				Method80(var7, var2 - var7.getWidth() / 2, var3 - var7.getHeight() / 2, 0);
			} else {
				int[] var10000 = Field242;
	
				for(var10000[var1] += (Field298[var0] - Field301[var0]) / 3000; Field242[var1] >= 300; var10000[var1] -= 300) {
					var10000 = Field242;
				}
	
				while(Field242[var1] < 0) {
					var10000 = Field242;
					var10000[var1] += 300;
				}
	
				Image var4;
				if ((var4 = Field236[Field242[var1] / 100]) == null) {
					var4 = Field236[0];
				}
	
				Image var5;
				if (Field299[var0] < Field299[0]) {
					var5 = Field237[0];
				} else {
					var5 = Field237[1];
				}
	
				Image var6 = Field238[0];
				if (var0 == Field243 && Field174 > 0) {
					var6 = Field238[1];
					Method271(Field298[1 + Field174 % 4], Field299[1 + Field174 % 4], Field298[var0], Field299[var0]);
				}
	
				Method80(var4, var2 - var4.getWidth() / 2, var3 - var4.getHeight() / 2, 0);
				if (Field257[var1]) {
					Method80(var5, var2 + 0 - var5.getWidth() / 2, var3 + -6 - var5.getHeight() / 2, 0);
					Method80(var6, var2 + 0 - var6.getWidth() / 2, var3 + 3 - var6.getHeight() / 2, 0);
				}
	
			}
		}
	}
	
	public static final void Method193(int var0) {
		Field259 = var0;
		Field260 = new int[var0];
		Field261 = new int[var0];
		Field262 = new int[var0];
		Field263 = new int[var0];
		Field264 = new boolean[var0];
	}
	
	public static final void Method194(int var0, int var1, int var2, int var3, int var4) {
		Field260[var0] = var1;
		Field261[var0] = var2;
		Field262[var0] = var3;
		Field263[var0] = var4;
		Field264[var0] = false;
	}
	
	public static final void Method195(int var0) {
		Field265 = new int[var0];
		Field266 = new int[var0];
		Field267 = new int[var0];
		Field268 = new int[var0];
		Field269 = new int[var0];
		Field270 = new int[var0];
		Field271 = new int[var0];
		Field272 = new int[var0];
	}
	
	public static final void Method196(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
		Field264[var1] = true;
		Field265[var0] = var1;
		Field266[var0] = Field262[var1];
		Field267[var0] = var2;
		Field268[var0] = Field263[var1];
		Field269[var0] = var3;
		Field270[var0] = var4;
		Field271[var0] = var5;
		Field272[var0] = var6;
	}
	
	public static final void Method197() {
		for(int var0 = 0; var0 < Field265.length; ++var0) {
			int var1 = Field265[var0];
			if (Field272[var0] > 0) {
				int var10002 = Field272[var0]--;
			} else if (Field272[var0] < -1) {
				int var5 = Field272[var0]++;
			} else if (Field272[var0] == 0) {
				int[] var10000 = Field262;
				var10000[var1] += Field270[var0];
				var10000 = Field263;
				var10000[var1] += Field270[var0];
				if (Field262[var1] > Field267[var0]) {
					Field262[var1] = Field267[var0];
				}
	
				if (Field263[var1] > Field269[var0]) {
					Field263[var1] = Field269[var0];
				}
	
				if (Field262[var1] == Field267[var0] && Field263[var1] == Field269[var0]) {
					Field272[var0] = -Field271[var0] - 1;
				}
			} else if (Field272[var0] == -1) {
				int[] var3 = Field262;
				var3[var1] -= Field270[var0];
				var3 = Field263;
				var3[var1] -= Field270[var0];
				if (Field262[var1] < Field266[var0]) {
					Field262[var1] = Field266[var0];
				}
	
				if (Field263[var1] < Field268[var0]) {
					Field263[var1] = Field268[var0];
				}
	
				if (Field262[var1] == Field266[var0] && Field263[var1] == Field268[var0]) {
					Field272[var0] = Field271[var0];
				}
			}
		}
	
	}
	
	public static final void Method198() {
		for(int var0 = 0; var0 < Field297; ++var0) {
			if (Field306[var0]) {
				for(int var1 = 0; var1 < Field259; ++var1) {
					if (Field299[var0] + Field300[var0] > Field261[var1] - Field263[var1] && Field299[var0] - Field300[var0] < Field261[var1] + Field263[var1] && Field298[var0] + Field300[var0] > Field260[var1] - Field262[var1] && Field298[var0] - Field300[var0] < Field260[var1] + Field262[var1]) {
						Method199(var0, var1);
					}
				}
			}
		}
	
	}
	
	public static final void Method199(int var0, int var1) {
		if (var0 == 2) {
			Field323 = true;
		}
	
		int var2 = Field260[var1] + Field262[var1] + Field300[var0];
		if (Field298[var0] < Field260[var1]) {
			var2 = Field260[var1] - Field262[var1] - Field300[var0];
		}
	
		int var3 = Field261[var1] + Field263[var1] + Field300[var0];
		if (Field299[var0] < Field261[var1]) {
			var3 = Field261[var1] - Field263[var1] - Field300[var0];
		}
	
		int var4;
		if ((var4 = Field298[var0] - var2) < 0) {
			var4 = -var4;
		}
	
		int var5;
		if ((var5 = Field299[var0] - var3) < 0) {
			var5 = -var5;
		}
	
		if (var4 < var5) {
			int[] var6 = Field298;
			var6[var0] += (var2 - Field298[var0]) / 2;
		} else {
			int[] var10000 = Field299;
			var10000[var0] += (var3 - Field299[var0]) / 2;
		}
	}
	
	public static final void Method200() {
		Method266(0, 0, 0);
	
		for(int var0 = 0; var0 < Field259; ++var0) {
			Method267(Field260[var0], Field261[var0], Field262[var0], Field263[var0]);
		}
	
		Method265(Field329);
	
		for(int var1 = 0; var1 < Field259; ++var1) {
			Method267(Field260[var1], Field261[var1], Field262[var1], Field263[var1]);
		}
	
	}
	
	public static final void Method201() {
		Method193(Method48());
	
		for(int var0 = 0; var0 < Field259; ++var0) {
			Method194(var0, Method222(Method48() << 16), Method48() << 16, Method48() << 16, Method48() << 16);
		}
	
	}
	
	public static final void Method202() {
		int var0 = Field259;
		Field259 = Field273;
		Field273 = var0;
		int[] var1 = Field260;
		Field260 = Field274;
		Field274 = var1;
		var1 = Field261;
		Field261 = Field275;
		Field275 = var1;
		var1 = Field262;
		Field262 = Field276;
		Field276 = var1;
		var1 = Field263;
		Field263 = Field277;
		Field277 = var1;
	}
	
	public static final void Method203() {
		if (Field278 == null) {
			Field278 = Method11("rocket.pim", "rocket.ppl");
		}
	
		if (Field279 == null) {
			Field279 = new Image[2];
			Field279[0] = Method11("vertFlame0.pim", "vertFlame0.ppl");
			Field279[1] = Method11("vertFlame1.pim", "vertFlame1.ppl");
		}
	
	}
	
	public static final void Method204(int var0, int var1, int var2) {
		Method203();
		Method80(Field278, var0, var1, 0);
		if (var2 >= 0) {
			Method80(Field279[var2 & 1], var0 + 36, var1 + 33, 0);
		}
	
	}
	
	public static final void Method205() {
		if (Field284 && Field283 == null) {
			Field283 = new Image[7];
	
			for(int var0 = 0; var0 < Field283.length; ++var0) {
				Field283[var0] = Method11("sign" + var0 + ".pim", "sign" + var0 + ".ppl");
			}
		}
	
	}
	
	public static final void Method206(int var0) {
		Field280 = new int[var0];
		Field281 = new int[var0];
		Field282 = new int[var0];
		if (var0 > 0) {
			Field284 = true;
		}
	
	}
	
	public static final void Method207(int var0, int var1, int var2, int var3) {
		if (Field325 && var3 == 2) {
			var3 = 3;
		}
	
		if (Field325 && var3 == 3) {
			var3 = 2;
		}
	
		Field280[var0] = var1;
		Field281[var0] = var2;
		Field282[var0] = var3;
	}
	
	public static final void Method208() {
		if (Field283 == null) {
			Method205();
		}
	
		for(int var0 = 0; var0 < Field280.length; ++var0) {
			Image var1;
			if ((var1 = Field283[Field282[var0]]) != null) {
				Method268(var1, Field280[var0], Field281[var0]);
			}
		}
	
	}
	
	public static final void Method209(int var0) {
		Field285 = new int[var0];
		Field286 = new int[var0];
		Field287 = new int[var0];
		Field288 = new int[var0];
		Field289 = new int[var0];
		Field290 = new int[var0];
		Field291 = new int[var0];
		Field292 = new int[var0];
		Field293 = new int[var0];
		Field294 = new int[var0];
		Field295 = new int[var0];
		Field296 = new int[var0];
	}
	
	public static final void Method210(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
		if (Field325) {
			int var10 = var7 - var6;
			short var11 = 180;
			if (var6 > 180) {
				var11 = 540;
			}
	
			var7 = (var6 = var11 - var6 - var10) + var10;
		}
	
		Field285[var0] = var1;
		Field286[var0] = var2;
		Field287[var0] = var3;
		Field289[var0] = var3 + var4;
		Field290[var0] = var5;
		Field291[var0] = 0;
		Field288[var0] = var3;
		Field292[var0] = Field300[var3];
		Field293[var0] = var6;
		Field294[var0] = var7 - var6;
		Field295[var0] = 2 * var8;
		Field296[var0] = 2 * (var9 - var8);
	}
	
	public static final void Method211() {
		for(int var0 = 0; var0 < Field287.length; ++var0) {
			int var10002 = Field291[var0]++;
			if (Field291[var0] > Field290[var0]) {
				int var1 = Field288[var0];
				int[] var10000 = Field300;
				var10000[var1] -= Field300[var1] / 4;
				if (Field300[var1] < 65536) {
					Field291[var0] = 0;
					Field306[var1] = true;
					var10000 = Field303;
					var10000[var1] |= 4;
					Field300[var1] = Field292[var0];
					Field298[var1] = Field285[var0];
					Field299[var1] = Field286[var0];
					int var2 = Field295[var0] + Method95() % Field296[var0];
					int var3 = Field293[var0] + Method95() % Field294[var0];
					Field301[var1] = Field285[var0] - var2 * Method296(var3);
					Field302[var1] = Field286[var0] - var2 * Method295(var3);
					var10002 = Field288[var0]++;
					if (Field288[var0] >= Field289[var0]) {
						Field288[var0] = Field287[var0];
					}
				}
			}
		}
	
	}
	
	public static final void Method212() {
		Method215();
		Method163();
		Method216();
		Method198();
	}
	
	public static final void Method213(int var0) {
		Field297 = var0;
		Field298 = new int[var0];
		Field299 = new int[var0];
		Field300 = new int[var0];
		Field301 = new int[var0];
		Field302 = new int[var0];
		Field303 = new int[var0];
		Field304 = new int[var0];
		Field305 = new byte[var0];
		Field306 = new boolean[var0];
	}
	
	public static final void Method214(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
		if (var6 == 3 && Field327 != -2) {
			var5 &= -5;
		}
	
		Field298[var0] = var1;
		Field299[var0] = var2;
		Field301[var0] = var1;
		Field302[var0] = var2;
		Field300[var0] = var3;
		Field303[var0] = var5;
		Field306[var0] = var7;
		Field305[var0] = (byte)var6;
		Field304[var0] = Method59(65536, var4 << 14);
	}
	
	public static final void Method215() {
		Method153();
		if (Field298[0] < 0 || Field298[0] > Field319 || Field299[0] > Field320) {
			Field165 = 0;
		}
	
		for(int var0 = 0; var0 < 5; ++var0) {
			if (Field298[var0] - Field301[var0] < 8192 && Field298[var0] - Field301[var0] > -8192) {
				Field298[var0] = Field301[var0];
			}
		}
	
		for(int var4 = 0; var4 < 5; ++var4) {
			if (Field298[var4] - Field301[var4] > 983040) {
				Field298[var4] = Field301[var4] + 983040;
			}
	
			if (Field298[var4] - Field301[var4] < -983040) {
				Field298[var4] = Field301[var4] - 983040;
			}
	
			if (Field299[var4] - Field302[var4] > 983040) {
				Field299[var4] = Field302[var4] + 983040;
			}
	
			if (Field299[var4] - Field302[var4] < -983040) {
				Field299[var4] = Field302[var4] - 983040;
			}
		}
	
		for(int var5 = 0; var5 < Field297; ++var5) {
			if (Field306[var5]) {
				int var1 = Field298[var5];
				int var2 = Field299[var5];
				int var3 = Field322;
				if (var5 < 5 && Field175 >= 0) {
					var3 = 63000;
				}
	
				int[] var10000 = Field298;
				var10000[var5] += Method58(var3, Field298[var5] - Field301[var5]);
				var10000 = Field299;
				var10000[var5] += Method58(var3, Field299[var5] - Field302[var5]);
				if ((Field303[var5] & 1) > 0) {
					var10000 = Field299;
					var10000[var5] += Field321;
				}
	
				Field301[var5] = var1;
				Field302[var5] = var2;
			}
		}
	
	}
	
	public static final void Method216() {
		Field323 = false;
	
		for(int var0 = 0; var0 < 5; ++var0) {
			for(int var1 = var0 + 1; var1 < Field297; ++var1) {
				if ((Field303[var1] & 2) > 0 && Field298[var0] - Field300[var0] < Field298[var1] + Field300[var1] && Field298[var0] + Field300[var0] > Field298[var1] - Field300[var1] && Field299[var0] - Field300[var0] < Field299[var1] + Field300[var1] && Field299[var0] + Field300[var0] > Field299[var1] - Field300[var1]) {
					if ((Field303[var1] & 32) > 0) {
						Field396 = var1;
					} else {
						Method164(var0, var1, Field300[var0] + Field300[var1], 55000, 3, true);
					}
				}
			}
		}
	
		for(int var2 = 5; var2 < Field297; ++var2) {
			if (Field306[var2]) {
				for(int var3 = var2 + 1; var3 < Field297; ++var3) {
					if ((Field303[var3] & 2) > 0 && Field298[var2] - Field300[var2] < Field298[var3] + Field300[var3] && Field298[var2] + Field300[var2] > Field298[var3] - Field300[var3] && Field299[var2] - Field300[var2] < Field299[var3] + Field300[var3] && Field299[var2] + Field300[var2] > Field299[var3] - Field300[var3] && (Field303[var3] & 32) == 0) {
						if ((Field303[var3] & 16) > 0) {
							Method257(var2, var3);
						} else {
							Method164(var2, var3, Field300[var2] + Field300[var3], 55000, 3, true);
						}
					}
				}
			}
		}
	
	}
	
	public static final void Method217() {
		int var0 = Field297;
		Field297 = Field307;
		Field307 = var0;
		int[] var1 = Field298;
		Field298 = Field308;
		Field308 = var1;
		var1 = Field299;
		Field299 = Field309;
		Field309 = var1;
		var1 = Field300;
		Field300 = Field310;
		Field310 = var1;
		var1 = Field301;
		Field301 = Field311;
		Field311 = var1;
		var1 = Field302;
		Field302 = Field312;
		Field312 = var1;
		var1 = Field303;
		Field303 = Field313;
		Field313 = var1;
		var1 = Field304;
		Field304 = Field314;
		Field314 = var1;
		byte[] var2 = Field305;
		Field305 = Field315;
		Field315 = var2;
		boolean[] var3 = Field306;
		Field306 = Field316;
		Field316 = var3;
	}
	
	public static final void Method218() {
		Field341 = Method219(16384);
		Field343 = Method219(256);
		Field342 = Method219(8192);
		Field344 = Method219(16);
		int var0 = 0;
	
		for(int var1 = 0; var1 < Field297; ++var1) {
			if ((Field303[var1] & 128) > 0 && Field305[var1] != 7) {
				++var0;
			}
		}
	
		Field346 = new short[var0];
		var0 = 0;
	
		for(short var5 = 0; var5 < Field297; ++var5) {
			if ((Field303[var5] & 128) > 0 && Field305[var5] != 7) {
				Field346[var0++] = var5;
			}
		}
	
		var0 = 0;
	
		for(int var6 = 0; var6 < Field297; ++var6) {
			if ((Field303[var6] & 4) > 0 && !Field306[var6] && (Field303[var6] & 8) == 0 && (Field303[var6] & 8192) == 0 && (Field303[var6] & 256) == 0) {
				++var0;
			}
		}
	
		Field347 = new short[var0];
		var0 = 0;
	
		for(short var7 = 0; var7 < Field297; ++var7) {
			if ((Field303[var7] & 4) > 0 && !Field306[var7] && (Field303[var7] & 8) == 0 && (Field303[var7] & 8192) == 0 && (Field303[var7] & 256) == 0) {
				Field347[var0++] = var7;
			}
		}
	
		Field345 = Method219(8);
	}
	
	public static final short[] Method219(int var0) {
		int var1 = 0;
	
		for(short var2 = 0; var2 < Field297; ++var2) {
			if ((Field303[var2] & var0) == var0) {
				++var1;
			}
		}
	
		short[] var5 = new short[var1];
		var1 = 0;
	
		for(short var3 = 0; var3 < Field297; ++var3) {
			if ((Field303[var3] & var0) == var0) {
				var5[var1++] = var3;
			}
		}
	
		return var5;
	}
	
	public static final void Method220(int var0) {
		boolean var1 = true;
		Field324 = false;
		if (var0 >= 25) {
			var0 = 24;
		}
	
		if (var0 == -2) {
			Method225(0);
			Method44("ship.bin");
			var1 = false;
		} else if (var0 == -1) {
			Method225(-1);
			Method44("playground" + Field328 * 4 / 25 + ".bin");
			var1 = false;
		} else if (var0 < 10) {
			Method225(1);
			Method44("level.00" + var0 + ".bin");
		} else {
			Method225(1);
			Method44("level.0" + var0 + ".bin");
		}
	
		Field326 = false;
		if (var0 >= 0) {
			Field326 = var0 == 24;
		}
	
		Field327 = var0;
		Field319 = Method48() << 16;
		Field320 = Method48() << 16;
		Method232(Method51(), Method51());
		Method51();
		Method52();
		Field329 = Method52();
		Field330 = Method52();
		Method52();
		Field331 = Method52();
		Field332 = Method52();
		int var2 = Method222(Method48() << 16);
		int var3 = Method48() << 16;
		Field156 = Method222(Method48() << 16);
		Field157 = Method48() << 16;
		int var4 = Method48();
		Field340 = 0;
		int var5 = 0;
		int var6 = 6;
		if (!var1 && var0 != -1) {
			var6 += 5;
		}
	
		Method213(var6 + var4 + 1);
		if (var1) {
			var5 = Method223(0, var2, var3, 1310720);
		} else {
			var5 = Method223(0, var2, var3, 851968);
		}
	
		Method214(var5++, 0, 0, 655360, 30, 0, 0, false);
		if (!var1 && var0 != -1) {
			var5 = Method223(var5, var2 + 3604480, var3, 1310720);
		}
	
		var6 = var5;
	
		for(int var7 = 0; var7 < var4; ++var7) {
			Method214(var5++, Method222(Method48() << 16), Method48() << 16, Method48() << 16, Method48(), Method48(), Method51(), Method51() == 1);
		}
	
		if (var0 == -2) {
			Method175(0);
		} else {
			int var16 = 0;
	
			for(int var8 = 0; var8 < Field297; ++var8) {
				if (Field305[var8] == 3) {
					++var16;
				}
			}
	
			Method175(var16);
			int var18 = 0;
	
			for(int var9 = 0; var9 < Field297; ++var9) {
				if (Field305[var9] == 3) {
					Method176(var18++, var9);
				}
			}
		}
	
		Field171 = 5;
		Method201();
		int var17 = Method48();
		int var19 = 1310720;
		if (!var1) {
			var19 = 786432;
		}
	
		Method160(17 + var17);
		int var35 = var5 = Method224(0, 0, var19);
		++var5;
		Method161(var35, 0, 0, 4, 65536, 65500, false, false);
	
		for(int var20 = 0; var20 < var17; ++var20) {
			Method161(var5++, var6 + Method48(), var6 + Method48(), Method51(), Method48() << 16, Method48(), Method51() == 1, true);
		}
	
		byte var21;
		Method209(var21 = Method51());
	
		for(int var10 = 0; var10 < var21; ++var10) {
			Method210(var10, Method222(Method48() << 16), Method48() << 16, var6 + Method51(), Method51(), Method48(), Method48(), Method48(), Method48(), Method48());
		}
	
		Method184(var21 = Method51());
	
		for(int var27 = 0; var27 < var21; ++var27) {
			Method185(var27, var6 + Method50(), Method51() == 1, Method48(), Method48() << 14, Method51() == 1, Method48(), Method48() << 14, Method48() << 16, Method51() == 1, Method48());
		}
	
		Method166(var21 = Method51());
	
		for(int var28 = 0; var28 < var21; ++var28) {
			Method167(var28, var6 + Method50(), Method51(), Method48() << 16, Method48());
		}
	
		Method170(var21 = Method51());
	
		for(int var29 = 0; var29 < var21; ++var29) {
			Method171(var29, var6 + Method50(), Method51() == 1, Method48(), Method48() << 16);
		}
	
		Method195(var21 = Method51());
	
		for(int var30 = 0; var30 < var21; ++var30) {
			Method196(var30, var30, Method48() << 16, Method48() << 16, Method48() << 8, Method48(), Method48());
		}
	
		Method206(var21 = Method51());
	
		for(int var31 = 0; var31 < var21; ++var31) {
			Method207(var31, Method222(Method48() << 16), Method48() << 16, Method51());
		}
	
		Method150(var1);
		Field394 = 70;
		Field321 = 40000;
		Field170 = 0;
		Field167 = -10000;
		Field174 = 0;
		Field398 = 72;
		Field322 = 62000;
		Method238(Field298[0], Field299[0], 0);
		if (var0 >= 0) {
			for(int var32 = 0; var32 < 5; ++var32) {
				int[] var37 = Field299;
				var37[var32] -= 7667712;
				var37 = Field302;
				var37[var32] -= 7667712;
			}
		}
	
		Field390 = 0;
		if (var0 == -2) {
			Method221();
		} else {
			Method218();
		}
	
		if (Field326) {
			Method178();
		}
	
	}
	
	public static final void Method221() {
		for(int var0 = 0; var0 < Field297; ++var0) {
			if ((Field303[var0] & 4) > 0 && Field306[var0]) {
				switch (Field305[var0]) {
					case 7:
						Field303[var0] = 0;
						Field306[var0] = false;
				}
			}
		}
	
	}
	
	public static final int Method222(int var0) {
		if (Field325) {
			return var0 > 0 && var0 < 10000 ? (Field319 >> 16) - var0 : Field319 - var0;
		} else {
			return var0;
		}
	}
	
	public static final int Method223(int var0, int var1, int var2, int var3) {
		byte var4 = 3;
		if (var0 > 0) {
			var4 = 0;
		}
	
		Method214(var0++, var1, var2, 655360, 60, var4, 0, true);
		Method214(var0++, var1, var2 - var3, 196608, 60, var4, 0, true);
		Method214(var0++, var1, var2 + var3, 196608, 60, var4, 0, true);
		Method214(var0++, var1 - var3, var2, 196608, 60, var4, 0, true);
		Method214(var0++, var1 + var3, var2, 196608, 60, var4, 0, true);
		return var0;
	}
	
	public static final int Method224(int var0, int var1, int var2) {
		Method161(var0++, var1 + 0, var1 + 1, 0, 0, 0, false, true);
		Method161(var0++, var1 + 0, var1 + 2, 0, 0, 0, false, true);
		Method161(var0++, var1 + 0, var1 + 3, 1, 0, 0, false, true);
		Method161(var0++, var1 + 0, var1 + 4, 1, 0, 0, false, true);
		Method161(var0++, var1 + 1, var1 + 0, 6, 196608, 0, false, true);
		Method161(var0++, var1 + 0, var1 + 2, 6, 196608, 0, false, true);
		Method161(var0++, var1 + 3, var1 + 0, 5, 196608, 0, false, true);
		Method161(var0++, var1 + 0, var1 + 4, 5, 196608, 0, false, true);
		Method161(var0++, var1 + 0, var1 + 1, 4, var2, 28000, false, true);
		Method161(var0++, var1 + 0, var1 + 2, 4, var2, 28000, false, true);
		Method161(var0++, var1 + 0, var1 + 3, 4, var2, 28000, false, true);
		Method161(var0++, var1 + 0, var1 + 4, 4, var2, 28000, false, true);
		Method161(var0++, var1 + 0, var1 + 1, 3, var2, 15000, false, true);
		Method161(var0++, var1 + 0, var1 + 2, 3, var2, 12000, false, true);
		Method161(var0++, var1 + 0, var1 + 3, 3, var2, 15000, false, true);
		Method161(var0++, var1 + 0, var1 + 4, 3, var2, 15000, false, true);
		return var0;
	}
	
	public static final void Method225(int var0) {
		if (var0 != Field348) {
			Field348 = var0;
			int var1 = Field319;
			Field319 = Field349;
			Field349 = var1;
			var1 = Field320;
			Field320 = Field350;
			Field350 = var1;
			var1 = Field321;
			Field321 = Field351;
			Field351 = var1;
			var1 = Field322;
			Field322 = Field352;
			Field352 = var1;
			boolean var2 = Field323;
			Field323 = Field353;
			Field353 = var2;
			var2 = Field324;
			Field324 = Field354;
			Field354 = var2;
			Method217();
			Method202();
			Method165();
			Method243();
			Method157();
		}
	}
	
	public static final void Method226() {
		Method22(9);
		Method24(8, "title", 1, 15804);
		Method23(2, "ship", 1, 13962);
		Method23(7, "ingame0", 1, 10987);
		Method23(3, "ingame1", 1, 21882);
		Method23(4, "ingame2", 1, 13885);
		Method23(5, "ingame3", 1, 21561);
		Method23(6, "ingame4", 1, 14583);
		Method23(0, "jingle_flyIn", 1, 980);
		Method23(1, "jingle_complete", 1, 815);
	}
	
	public static final void Method227(int var0) {
		if (var0 == Field355 && var0 != -1) {
			if (var0 >= 0) {
				if (!Method27(Field355, -1)) {
					Method28(Field355, -1);
				}
	
			}
		} else {
			Method29();
			Method31();
			Field355 = var0;
			if (var0 >= 0) {
				Method28(Field355, -1);
			}
	
		}
	}
	
	public static final void Method228() {
		if (Field355 >= 0 && !Field12 && !Method27(Field355, -1)) {
			Method28(Field355, -1);
		}
	
	}
	
	public static final void Method229() {
		switch (Field318) {
			case 0:
				Method227(7);
				return;
			case 1:
				Method227(3);
				return;
			case 2:
				Method227(4);
				return;
			case 3:
				Method227(5);
				return;
			case 4:
				Method227(6);
			default:
		}
	}
	
	public static final void Method230() {
		Method227(-1);
		Method28(0, 1);
	}
	
	public static final void Method231() {
		Method227(-1);
		Method28(1, 1);
	}
	
	public static final void Method232(int var0, int var1) {
		boolean var2 = false;
	
		for(int var3 = 0; var3 < Field367.length; ++var3) {
			if (Field367[var3] == var1) {
				var2 = true;
			}
		}
	
		if (!var2) {
			var1 = Field367[Field328 % Field367.length];
			if (var0 > 0) {
				var0 = Field366[var1];
			}
		}
	
		Field317 = var0;
		Field318 = var1;
	}
	
	public static final void Method233(int var0) {
		Field356 = new Image[5];
	
		for(int var1 = 0; var1 < 5; ++var1) {
			Field356[var1] = Method11("bg_" + var0 + "_" + var1 + ".pim", "bg_" + var0 + "_" + var1 + ".ppl");
		}
	
		Field364 = Field365[var0];
		Field357 = Field368[var0];
		Field358 = Field369[var0];
		int var3 = Method237(Field357);
		int var2 = Method237(Field358) - Field356[Field358[0][0]].getHeight();
		Field360 = 0;
		Field361 = -(var3 - (128 - var2));
		Field362 = var3;
		Field363 = 128 - var2;
		Field359 = var0;
	}
	
	public static final void Method234() {
		if (Field327 >= 0) {
			if (Field356 == null || Field318 != Field359) {
				Method233(Field318);
			}
	
			if (!Field364) {
				int var5 = Field360 + (Field361 - Field360) * (Field373 >> 16) / (Field320 >> 16);
				int var7 = -((Method236(Field357) - 128) * (Field372 >> 16)) / (Field319 >> 16);
				Method235(Field357, var7, var5, false);
			} else {
				int var3 = Field360 + (Field361 - Field360) * (Field373 >> 16) / (Field320 >> 16);
				int var1 = -((Method236(Field357) - 128) * (Field372 >> 16)) / (Field319 >> 16);
				Method235(Field357, var1, var3, false);
				var3 = Field362 + (Field363 - Field362) * (Field373 >> 16) / (Field320 >> 16);
				var1 = -((Method236(Field358) - 128) * (Field372 >> 16)) / (Field319 >> 16);
				Method235(Field358, var1, var3, true);
			}
		} else {
			Method265(14473325);
			Method78(0, 0, 128, 128);
			Method265(0);
	
			for(int var0 = 0; var0 < 128; var0 += 40) {
				Method81(0, var0, 128, var0);
			}
	
			for(int var2 = 0; var2 < 128; var2 += 40) {
				Method81(var2, 0, var2, 128);
			}
	
		}
	}
	
	public static final void Method235(int[][] var0, int var1, int var2, boolean var3) {
		int var4 = 0;
		if (var3) {
			int var5 = var1;
	
			for(int var6 = 0; var6 < var0[0].length; ++var6) {
				Image var7 = Field356[var0[0][var6]];
				if (var5 < 128 && var5 > -var7.getWidth() && var2 > 0 && var2 < 128 + var7.getHeight()) {
					Method80(var7, var5, var2 - var7.getHeight(), 0);
				}
	
				var5 += var7.getWidth();
			}
	
			++var4;
		}
	
		while(var4 < var0.length) {
			int var8 = var1;
			Image var9 = null;
	
			for(int var10 = 0; var10 < var0[var4].length; ++var10) {
				var9 = Field356[var0[var4][var10]];
				if (var8 < 128 && var8 > -var9.getWidth() && var2 < 128 && var2 > -var9.getHeight()) {
					Method80(var9, var8, var2, 0);
				}
	
				var8 += var9.getWidth();
			}
	
			var2 += var9.getHeight();
			++var4;
		}
	
	}
	
	public static final int Method236(int[][] var0) {
		int var1 = 0;
	
		for(int var2 = 0; var2 < var0[0].length; ++var2) {
			var1 += Field356[var0[0][var2]].getWidth();
		}
	
		return var1;
	}
	
	public static final int Method237(int[][] var0) {
		int var1 = 0;
	
		for(int var2 = 0; var2 < var0.length; ++var2) {
			var1 += Field356[var0[var2][0]].getHeight();
		}
	
		return var1;
	}
	
	public static final void Method238(int var0, int var1, int var2) {
		if (Field348 == 0) {
			var1 = Field320 / 2;
			if (var0 < 5734400) {
				var0 = 5734400;
			}
	
			if (var0 > Field319 - 5734400) {
				var0 = Field319 - 5734400;
			}
		}
	
		Field372 = var0;
		Field373 = var1;
		Field370 = var0 >> 16;
		Field371 = var1 >> 16;
	
		for(Field374 = var2; Field374 > 359; Field374 -= 360) {
		}
	
		while(Field374 < 0) {
			Field374 += 360;
		}
	
		Field398 = 72;
	}
	
	public static final boolean Method239(int var0, int var1, int var2) {
		return Method240(var0, var1, var2, 1966080);
	}
	
	public static final boolean Method240(int var0, int var1, int var2, int var3) {
		if (Field348 == 0) {
			var1 = Field320 / 2;
		} else if (var1 > Field320 - 5734400) {
			var1 = Field320 - 5734400;
		}
	
		if (var0 < 5734400) {
			var0 = 5734400;
		}
	
		if (var0 > Field319 - 5734400) {
			var0 = Field319 - 5734400;
		}
	
		int var4 = var0 - Field372;
		int var5 = var1 - Field373;
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
	
		int var6;
		for(var6 = var4 >> 17; var6 > 359; var6 -= 360) {
		}
	
		while(var6 < 0) {
			var6 += 360;
		}
	
		if ((var6 >= 180 || Field374 >= 180) && (var6 <= 180 || Field374 <= 180)) {
			if (var6 > 180) {
				var6 -= 360;
			}
	
			if (Field374 > 180) {
				Field374 -= 360;
			}
	
			Field374 += (var6 - Field374) / 5;
		} else {
			Field374 += (var6 - Field374) / 5;
		}
	
		while(Field374 > 359) {
			Field374 -= 360;
		}
	
		while(Field374 < 0) {
			Field374 += 360;
		}
	
		if (Field348 != 0) {
			int var7;
			if ((var7 = Field298[0] - Field301[0]) < 0) {
				var7 = -var7;
			}
	
			var7 >>= 8;
			int var8;
			if ((var8 = 82 - var7 * 45 * 72 / 204800) < 36) {
				var8 = 36;
			}
	
			int var9;
			if ((var9 = (var8 - Field398) / 10) <= 10 && var9 >= 10) {
				if (var9 > 0) {
					++Field398;
				} else if (var9 < 0) {
					--Field398;
				}
			} else {
				Field398 += (var8 - Field398) / 10;
			}
		} else {
			Field398 = 72;
			Field374 = 0;
		}
	
		if (var4 < 655360 && var4 > -655360 && var5 < 655360 && var5 > -655360) {
			return true;
		} else {
			Field372 += var4 / 5;
			Field373 += var5 / 5;
			Field370 = Field372 >> 16;
			Field371 = Field373 >> 16;
			return false;
		}
	}
	
	public static final int Method241(int var0, int var1) {
		return Method293(var0 - Field372, var1 - Field373, Field374) + 4194304 >> 16;
	}
	
	public static final int Method242(int var0, int var1) {
		return Method294(var0 - Field372, var1 - Field373, Field374) + 4194304 >> 16;
	}
	
	public static final void Method243() {
		int var0 = Field370;
		Field370 = Field375;
		Field375 = var0;
		var0 = Field371;
		Field371 = Field376;
		Field376 = var0;
		var0 = Field372;
		Field372 = Field377;
		Field377 = var0;
		var0 = Field373;
		Field373 = Field378;
		Field378 = var0;
		var0 = Field374;
		Field374 = Field379;
		Field379 = var0;
	}
	
	public static final void Method244() {
		if (Field388 == null) {
			Field388 = new Image[5][];
			Field388[0] = new Image[2];
	
			for(int var0 = 0; var0 < 2; ++var0) {
				Field388[0][var0] = Method11("wiz" + var0 + ".pim", "wiz" + var0 + ".ppl");
			}
	
			Field388[1] = new Image[2];
	
			for(int var1 = 0; var1 < 2; ++var1) {
				Field388[1][var1] = Method11("waz" + var1 + ".pim", "waz" + var1 + ".ppl");
			}
	
			Field388[2] = new Image[2];
	
			for(int var2 = 0; var2 < 2; ++var2) {
				Field388[2][var2] = Method11("purple" + var2 + ".pim", "purple" + var2 + ".ppl");
			}
	
			Field388[3] = new Image[2];
	
			for(int var3 = 0; var3 < 2; ++var3) {
				Field388[3][var3] = Method11("hal" + var3 + ".pim", "hal" + var3 + ".ppl");
			}
	
			Method245();
		}
	
	}
	
	public static final void Method245() {
		Field380 = 0;
	}
	
	public static final void Method246(String var0, int var1) {
		int var2 = 12 + Field382 * 13;
		Field381 = var1;
		Field382 = Method248(var0);
		Field386 = var0;
		Field384 = Method101();
		if (Field380 == 1) {
			int var3 = 12 + Field382 * 13;
			Field384 -= (long)(400 * var2 / var3);
		}
	
		Field380 = 1;
		Field383 = 0;
		Field385 = 0;
		Field387 = false;
	}
	
	public static final void Method247() {
		if (Field380 == 1) {
			long var0;
			int var2 = (int)((var0 = Method101()) - Field384);
			int var3 = 12 + Field382 * 13;
			if (Field385 == 0 && var2 < 400) {
				var3 = var3 * var2 / 400;
			}
	
			Method75(16777215);
			Method78(0, 0, 128, var3);
			Method75(15526360);
			Method81(0, var3, 128, var3);
			Method75(11315353);
			Method81(0, var3 + 1, 128, var3 + 1);
			Method75(0);
			Method81(0, var3 + 2, 128, var3 + 2);
			if (Field385 != 0 || var2 >= 400) {
				byte var4 = 0;
				boolean var5 = false;
				if (Field388[Field381] != null) {
					Image var6 = Field388[Field381][Field383 % Field388[Field381].length];
					int var7 = (21 - var6.getWidth()) / 2;
					if (Field381 == 0) {
						Method80(var6, 0 + var7, 6 + (Field382 * 13 - var6.getHeight()) / 2, 0);
						var4 = 21;
					} else {
						Method80(var6, 107 + var7, 6 + (Field382 * 15 - var6.getHeight()) / 2, 0);
						var4 = 8;
					}
	
					Method91(var4, 6, 99, Field382 * 13, Field386, 1, 0, Field385, false);
				} else {
					Method91(14, 6, 99, Field382 * 13, Field386, 1, 0, Field385, false);
				}
	
				if (var0 > Field384) {
					Field384 = var0 + 150L;
					++Field385;
	
					char var9;
					while(Field385 < Field386.length() && (var9 = Field386.charAt(Field385)) != ' ' && var9 != '.') {
						++Field385;
					}
	
					if (Field385 >= Field386.length()) {
						Field387 = true;
						return;
					}
	
					++Field383;
				}
	
			}
		}
	}
	
	public static final int Method248(String var0) {
		return Method74(99, var0, 1).length;
	}
	
	public static final String[] Method249(String var0, int var1) {
		int[] var2;
		String[] var3 = new String[(var2 = Method74(99, var0, var1)).length];
	
		for(int var4 = 0; var4 < var2.length; ++var4) {
			if (var4 == var2.length - 1) {
				var3[var4] = var0.substring(var2[var4]);
			} else {
				var3[var4] = var0.substring(var2[var4], var2[var4 + 1]);
			}
		}
	
		return var3;
	}
	
	public static final void Method250() {
		if (Field396 > -1) {
			Method133(Field396);
			Field396 = -1;
		} else {
			if (Field389 >= -1) {
				Method220(Field389);
				Field389 = -2;
				if (Field327 < 0) {
					Field390 = 120;
				}
	
				if (!Field392) {
					if (Field327 >= 0) {
						Method131("respawn.bms", Field298[0], Field299[0]);
					}
	
					Field392 = true;
					Field404 = true;
					return;
				}
	
				Field404 = true;
			}
	
			if (Field390 >= 50) {
				Method255();
				++Field395;
			}
	
			if (Field165 < 1) {
				if (Field394 == 70) {
					Method100(640);
					Field404 = false;
					Field300[0] = 131072;
					Field300[2] = 65536;
					Field300[3] = 65536;
					Field300[4] = 65536;
					Field303[2] = 1;
					Field298[2] = Field298[0];
					Field299[2] = Field299[0] + 655360;
	
					for(int var0 = 0; var0 < 2; ++var0) {
						for(int var1 = 0; var1 < 2; ++var1) {
							Field201[8 + 2 * var0 + var1] = 3;
							Field204[8 + 2 * var0 + var1] = 500;
							Field202[8 + 2 * var0 + var1] = 2621440;
						}
					}
	
					if (Field175 >= 0) {
						Field206[16] = false;
						Field303[Field200[16]] = Field175;
						Field175 = -1;
						Field176 = Field200[16];
					}
				}
	
				Method240(Field298[0] + 10 * (Field298[0] - Field301[0]), Field299[0] + 10 * (Field299[0] - Field302[0]), 0, 1310720);
				Field162 -= 3;
				if (Field162 < 0) {
					Field162 = 0;
				}
	
				--Field394;
				if (Field394 <= 0) {
					Field389 = Field327;
				}
			}
	
			int var2 = -1;
			if (Field390 < 100) {
				var2 = Method63(-1, 3);
			} else if (Field327 == -1) {
				var2 = Method63(-1, 4);
			} else if (Field165 > 0) {
				var2 = Method63(-1, 0);
			}
	
			if (var2 == 4) {
				Field391 = true;
			}
	
			if (var2 == 3) {
				Field390 = 120;
			}
	
			if (var2 == 0) {
				Method63(-1, -1);
				Field427 = true;
				Field409 = 100;
				Method225(0);
				Method97(2, 0);
			} else {
				if (Field404 && Field390 >= 100) {
					Method251();
					Method240(Field298[0] + 10 * (Field298[0] - Field301[0]), Field299[0] + 10 * (Field299[0] - Field302[0]), 0, 3276800);
				}
	
				if (Field306[Field171]) {
					if (Field395 > Field167 + 100) {
						Method238(Field298[Field171], Field299[Field171], 0);
					}
	
					if (Field170 == 0 && Field395 > Field167 + 130) {
						Field170 = 5;
						Method100(40);
					}
				}
	
				if (Field170 > 0) {
					Method238(Field298[Field171], Field299[Field171], 0);
					--Field170;
					Field304[Field171] = 0;
					if (Field170 > 0) {
						int[] var10000 = Field300;
						int var10001 = Field171;
						var10000[var10001] += 983040;
					} else {
						Field306[Field171] = false;
						Field298[Field171] = -13107200;
					}
				}
	
				Method252(true);
				if (Field390 < 100) {
					int var3;
					if (Field390 < 80) {
						var3 = -51 + Field390 * 51 / 50;
					} else {
						var3 = -51 + (100 - Field390) * 51 / 20;
					}
	
					if (var3 > 0) {
						var3 = 0;
					}
	
					if (Field390 == 80) {
						Method100(140);
					}
	
					if (Field390 < 80) {
						Method204(14, var3, -1);
					} else {
						Method204(Method94() % 5 - 2 + 64 - 50, var3, Field390);
					}
	
					Method92(4, 114, Field102, 0);
					++Field390;
				}
	
				Method14();
			}
		}
	}
	
	public static final void Method251() {
		if ((Method5(416) || Method5(1) && !Method5(2048)) && (Field323 || Field175 >= 0)) {
			if (Field175 >= 0) {
				Method152();
			}
	
			if (Field323) {
				Field173 = 3;
			}
		}
	
		if (Method4(272)) {
			Field166 = 1;
			if (Field175 >= 0 && !Field323) {
				if (Field298[0] >= Field301[0]) {
					if (Field298[0] - Field301[0] < 65536) {
						int[] var0 = Field298;
						var0[0] += 163840;
					} else {
						int[] var1 = Field298;
						var1[0] += 81920;
					}
				}
			} else {
				int[] var10000 = Field298;
				var10000[0] += 81920;
				if (Field323) {
					Field160 += 3;
				}
			}
		}
	
		if (Method4(136)) {
			Field166 = -1;
			if (Field175 >= 0 && !Field323) {
				if (Field298[0] <= Field301[0]) {
					if (Field301[0] - Field298[0] < 65536) {
						int[] var3 = Field298;
						var3[0] -= 163840;
					} else {
						int[] var4 = Field298;
						var4[0] -= 81920;
					}
				}
			} else {
				int[] var2 = Field298;
				var2[0] -= 81920;
				if (Field323) {
					Field160 -= 3;
				}
			}
		}
	
		if (Method6(-1)) {
			if (Method6(Field399[Field401])) {
				++Field401;
				if (Field401 >= Field399.length) {
					Field324 = true;
					Field401 = 0;
				}
			} else {
				Field401 = 0;
			}
	
			if (Method6(Field400[Field402])) {
				++Field402;
				if (Field402 >= Field400.length) {
					Field403 = !Field403;
					int[] var5 = Field299;
					var5[0] -= 524288;
					var5 = Field298;
					var5[3] -= 524288;
					var5 = Field298;
					var5[4] += 524288;
					Field402 = 0;
					return;
				}
			} else {
				Field402 = 0;
			}
		}
	
	}
	
	public static final void Method252(boolean var0) {
		long var1 = Method101();
		if (Field317 == 1 && Field348 == 1) {
			Field409 = 100;
			if (Method95() % 100 == 50) {
				Field409 = 0;
			}
		}
	
		Method234();
	
		for(int var3 = 0; var3 < Field344.length; ++var3) {
			short var4 = Field344[var3];
			Method265(Field331);
			Method270(Field298[var4], Field299[var4], Field300[var4]);
			Method266(255, 255, 255);
			if (Field300[var4] > Field340) {
				Method272(Field298[var4], Field299[var4], Field340);
			} else {
				int var5;
				int var6 = (int)((long)(var5 = Field300[var4] / 4) * (var1 % 700L)) / 700;
	
				for(int var7 = 0; var7 < 4; ++var7) {
					Method272(Field298[var4], Field299[var4], Field300[var4] - var6 - var7 * var5);
				}
			}
		}
	
		Method208();
		if (Field157 > 0) {
			Method154(Field156, Field157);
		}
	
		if (var0) {
			Method155();
		}
	
		Method191();
	
		for(int var8 = 0; var8 < Field297; ++var8) {
			if ((Field303[var8] & 4) > 0 && Field306[var8] && (Field303[var8] & 8192) == 0 && (Field303[var8] & 8) == 0 && var8 != Field171) {
				Method266(0, 0, 0);
				Method270(Field298[var8], Field299[var8], Field300[var8]);
				Method265(Field330);
				Method270(Field298[var8], Field299[var8], Field300[var8]);
			}
		}
	
		if (Field306[Field171]) {
			Method266(0, 0, 0);
			Method270(Field298[Field171], Field299[Field171], Field300[Field171]);
			if (Field167 != -1 && Field395 > Field168) {
				if (Field169) {
					Method266(255, 255, 255);
					Field169 = false;
					Field168 = Field395 + (Field167 + 100 - Field395) / 4;
				} else {
					Method266(200, 0, 0);
					Field169 = true;
				}
			} else {
				Method266(200, 0, 0);
			}
	
			Method270(Field298[Field171], Field299[Field171], Field300[Field171]);
			Method268(Field196, Field298[Field171], Field299[Field171] - Field300[Field171]);
		}
	
		Method253(Field347, 0);
		Method200();
		Method253(Field347, Field329);
		Method253(Field342, 0);
		Method253(Field342, 16777215);
		Method253(Field343, 0);
		Method253(Field343, Field332);
		int var9;
		if ((var9 = (int)(var1 % 1600L)) > 800) {
			var9 = 1600 - var9;
		}
	
		Method253(Field345, 0);
		Method253(Field345, (Field334 + var9 * (Field335 - Field334) / 800 << 16) + (Field336 + var9 * (Field337 - Field336) / 800 << 8) + Field338 + var9 * (Field339 - Field338) / 800);
		Method253(Field341, 0);
		Method253(Field341, Field333);
		Method266(0, 0, 0);
	
		for(int var10 = 16; var10 < Field198; ++var10) {
			if (Field206[var10] && Field205[var10]) {
				Method264(Field298[Field199[var10]], Field299[Field199[var10]], Field298[Field200[var10]], Field299[Field200[var10]]);
			}
		}
	
		if (Field326) {
			Method181();
		}
	
		Method177();
	
		for(int var11 = 0; var11 < Field346.length; ++var11) {
			Method159(Field298[Field346[var11]], Field299[Field346[var11]]);
		}
	
		if (Field317 != 0 && Field348 == 1) {
			if (Field317 == 1) {
				Method105();
			}
	
			if (Field317 == 2) {
				Method106();
			}
		}
	
	}
	
	public static final void Method253(short[] var0, int var1) {
		Method265(var1);
	
		for(int var2 = 0; var2 < var0.length; ++var2) {
			Method270(Field298[var0[var2]], Field299[var0[var2]], Field300[var0[var2]]);
		}
	
	}
	
	public static final void Method254(int var0) {
		Method220(var0);
	}
	
	public static final void Method255() {
		if (Field206[16]) {
			Field205[16] = true;
		}
	
		Method169();
		Method186();
		Method173();
		Method197();
		if (Field174 > 0) {
			--Field174;
		}
	
		Method211();
		Method212();
		if (Field326) {
			Method179();
		}
	
	}
	
	public static final void Method256(int var0, int var1) {
		if (var0 == 2) {
			Field323 = true;
		}
	
		if ((Field303[var1] & 8192) > 0) {
			int[] var10000 = Field300;
			var10000[var1] -= 65536;
			if (Field300[var1] < 262144) {
				Field300[var1] = 0;
				Field303[var1] = 0;
			}
		}
	
		if ((Field303[var1] & 8) > 0 && !Field403) {
			Field165 -= 4000;
		}
	
		byte var2;
		if ((var2 = Field305[var1]) != 0) {
			if (Field327 != -2 && Field348 != 0) {
				if (var2 == 3 && Field175 == -1 && var1 != Field176) {
					Field206[16] = true;
					Field200[16] = var1;
					Field175 = Field303[var1];
					Field303[var1] = 0;
				}
	
				if (var2 == 6) {
					Method172(var1);
				}
	
				if (var2 == 7 && Field174 == 0 && Method188(var1)) {
					if (Method187(var1) && var0 == 2) {
						if (Field299[2] - Field302[2] > 120000) {
							Method189(var1);
						}
					} else {
						if (!Field403) {
							Field165 -= 170;
						}
	
						Field243 = var1;
						if (Field165 > 0) {
							Field174 = 50;
							Method100(240);
						}
					}
				}
	
				if (var2 == 8) {
					Field324 = true;
				} else {
					if (var2 >= 9 && var2 <= 11 && var0 == 2 && Field167 + 200 < Field395) {
						Field167 = Field395;
						Field168 = Field395 + 33;
						Field169 = false;
						int var3 = Field166;
						if (var2 == 10) {
							var3 = -1;
						}
	
						if (var2 == 11) {
							var3 = 1;
						}
	
						if (Field325 && var2 > 9) {
							var3 *= -1;
						}
	
						Method214(Field171, Field298[var1] + var3 * 1310720, Field299[var1], 655360, 15, 71, 0, true);
						int[] var4 = Field298;
						int var10001 = Field171;
						var4[var10001] += var3 * 196608;
					}
	
				}
			} else {
				Field423 = var2;
			}
		}
	}
	
	public static final void Method257(int var0, int var1) {
		if (Field165 > 0) {
			if (var0 >= 5 && var1 >= 5) {
				if ((Field303[var0] & 16) > 0 || (Field303[var1] & 16) > 0) {
					int var2 = var0;
					int var3 = var1;
					if ((Field303[var1] & 16) > 0) {
						var3 = var0;
						var2 = var1;
					}
	
					if (Field303[var3] != 0) {
						Field298[var3] = Field298[var2];
						Field299[var3] = Field299[var2];
						Field301[var3] = Field301[var2];
						Field302[var3] = Field302[var2];
						Method162(var3);
						int[] var10000 = Field300;
						var10000[var3] -= 32768;
						Field340 += 32768;
						if (Field340 >= Field300[var2]) {
							Field340 = Field300[var2];
							Field324 = true;
						}
	
						if (Field300[var3] <= 0) {
							Field303[var3] = 0;
							Field306[var3] = false;
						}
					}
				}
	
				if (Field170 > 0 && (var0 == Field171 && (Field303[var1] & 256) > 0 || var1 == Field171 && (Field303[var0] & 256) > 0)) {
					int var4 = var0;
					if (var0 == Field171) {
						var4 = var1;
					}
	
					Field300[var4] = Field300[var4] * 7 / 10;
					if (Field300[var4] < 655360) {
						Field300[var4] = 0;
						Field303[var4] = 0;
					}
				}
	
				byte var5 = Field305[var0];
				byte var6 = Field305[var1];
				if (var5 != 0 || var6 != 0) {
					if (var5 == 6 || var6 == 6) {
						if (var5 == 6) {
							Method172(var0);
						} else {
							Method172(var1);
						}
					}
	
					if ((var5 == 7 || var6 == 7) && ((Field303[var0] & 8) > 0 || (Field303[var1] & 8) > 0 || Field170 > 0 && (var0 == Field171 || var1 == Field171))) {
						if (var5 == 7) {
							Method189(var0);
						} else {
							Method189(var1);
						}
					}
	
					if ((var5 == 1 || var6 == 1) && (var5 == 2 || var6 == 2)) {
						Field324 = true;
					}
	
					if ((var5 == 1 || var6 == 1) && (var5 == 12 || var6 == 12)) {
						Field165 -= 4000;
					}
	
					if (Field326) {
						if ((var5 == 13 || var6 == 13) && (var5 == 14 || var6 == 14)) {
							if (var5 == 14) {
								Field303[var0] = 0;
							} else {
								Field303[var1] = 0;
							}
	
							Method180();
						}
	
						if (Field170 > 0 && (var0 == Field171 && var6 == 13 || var1 == Field171 && var5 == 13)) {
							Method183();
						}
					}
	
				}
			} else {
				if (var0 >= 5) {
					Method256(var1, var0);
				}
	
				if (var1 >= 5) {
					Method256(var0, var1);
				}
	
			}
		}
	}
	
	public static final void Method258() {
		Method76(255, 255, 255);
		Method78(0, 0, 128, 128);
		Method90();
		Method260();
		Method297();
		Method21();
		if (Field328 > 0) {
			Field124 = true;
		}
	
		Method97(0, 0);
	}
	
	public static final void Method259() {
		Method228();
		Method143();
	}
	
	public static final void Method260() {
		Method93(0);
		Method35();
		Method21();
		Method69(4);
		Method70(3, (short)-18738, (short)31517, (short)-14732, (byte)4, (short)-30552, 1, -2);
		Method70(1, (short)-6024, (short)9643, (short)-26430, (byte)3, (short)-10722, 1, -1);
		Method70(2, (short)-24128, (short)27667, (short)-11910, (byte)3, (short)-24666, 1, -1);
		Method70(0, (short)-30996, (short)19263, (short)-2474, (byte)3, (short)-18294, -1, -1);
		Method66();
		Method67(2, Method12((short)-6926, (short)10529));
		Method67(1, Method12((short)12759, (short)-1020));
		Method67(0, Method12((short)-28153, (short)24532));
		Method67(3, Method12((short)1349, (short)-14186));
		Method67(4, Method12((short)23585, (short)-28174));
	}
	
	public static final void Method261() {
		Field405 = true;
	}
	
	public static final void Method262() {
		if (Field405) {
			Field405 = false;
			Method76(0, 0, 0);
	
			for(int var0 = 0; var0 < 128; ++var0) {
				for(int var1 = var0 & 1; var1 < 128; var1 += 2) {
					Method81(var1, var0, var1, var0);
				}
			}
	
			Method78(0, 109, 128, 128);
			Method63(2, -1);
		}
	
	}
	
	public static final void Method263() {
		Method76(0, 0, 0);
		Method78(0, 0, 128, 128);
		--Field407;
		if (Field407 < 0) {
			Field407 = Field406.length - 1;
		}
	
		Field406[Field407] = 200;
		boolean var0 = false;
	
		for(int var1 = 0; var1 < 12; ++var1) {
			Method76(Field406[var1], 0, 0);
			Field406[var1] = Field406[var1] * 80 / 100;
			Method82(32, 32, 64, 64, var1 * 30, 20);
		}
	
		Method76(0, 0, 0);
		Method82(43, 43, 42, 42, 0, 360);
		Method90();
	}
	
	public static final void Method264(int var0, int var1, int var2, int var3) {
		int var4 = Method241(var0, var1);
		int var5 = Method242(var0, var1);
		int var6 = Method241(var2, var3);
		int var7 = Method242(var2, var3);
		Method81(var4, var5, var6, var7);
	}
	
	public static final void Method265(int var0) {
		Method266(var0 >> 16 & 255, var0 >> 8 & 255, var0 & 255);
	}
	
	public static final void Method266(int var0, int var1, int var2) {
		Field408 = false;
		if (var0 + var1 + var2 == 0) {
			Field408 = true;
		}
	
		if (Field409 == 100) {
			Method76(var0, var1, var2);
		} else {
			int var3;
			if ((var3 = Field409) > 100) {
				Method76(var0 - var0 * (200 - var3) / 100, var1 - var1 * (200 - var3) / 100, var2 - var2 * (200 - var3) / 100);
			} else {
				Method76(var0 + (255 - var0) * (100 - var3) / 100, var1 + (255 - var1) * (100 - var3) / 100, var2 + (255 - var2) * (100 - var3) / 100);
			}
		}
	}
	
	public static final void Method267(int var0, int var1, int var2, int var3) {
		if (Field408) {
			int var4 = 131072;
			var4 = 100 * var4 / Field398;
			var2 += var4;
			var3 += var4;
		}
	
		int var13 = Method241(var0 - var2, var1 - var3);
		int var5 = Method242(var0 - var2, var1 - var3);
		int var6 = Method241(var0 + var2, var1 - var3);
		int var7 = Method242(var0 + var2, var1 - var3);
		int var8 = Method241(var0 - var2, var1 + var3);
		int var9 = Method242(var0 - var2, var1 + var3);
		int var10 = Method241(var0 + var2, var1 + var3);
		int var11 = Method242(var0 + var2, var1 + var3);
		if (var13 <= 128 || var8 <= 128) {
			if (var5 <= 128 || var7 <= 128) {
				if (var10 >= 0 || var6 >= 0) {
					if (var11 >= 0 || var9 >= 0) {
						Method84(var8, var9, var13, var5, var6, var7);
						Method84(var6, var7, var10, var11, var8, var9);
					}
				}
			}
		}
	}
	
	public static final void Method268(Image var0, int var1, int var2) {
		int var3 = Method241(var1, var2);
		int var4 = Method242(var1, var2);
		Method80(var0, var3 - var0.getWidth() / 2, var4 - var0.getHeight() / 2, 0);
	}
	
	public static final void Method269(int var0, int var1, int var2, int var3) {
		Method265(0);
		Method270(var0, var1, var2);
		Method265(var3);
		Method270(var0, var1, var2);
	}
	
	public static final void Method270(int var0, int var1, int var2) {
		if (var2 >= 35000) {
			int var3 = Method241(var0, var1);
			int var4 = Method242(var0, var1);
			var2 >>= 16;
			var2 = Field398 * var2 / 100;
			if (Field408) {
				var2 += 2;
			}
	
			if (var3 > -var2 && var3 < 128 + var2 && var4 > -var2 && var4 < 128 + var2) {
				Method82(var3 - var2, var4 - var2, var2 + var2, var2 + var2, 0, 360);
			}
	
		}
	}
	
	public static final void Method271(int var0, int var1, int var2, int var3) {
		int var4 = Method241(var0, var1);
		int var5 = Method242(var0, var1);
		int var6 = Method241(var2, var3);
		int var7 = Method242(var2, var3);
		Method104(var4, var5, var6, var7, 8, 6);
	}
	
	public static final void Method272(int var0, int var1, int var2) {
		int var3 = Method241(var0, var1);
		int var4 = Method242(var0, var1);
		var2 >>= 16;
		var2 = Field398 * var2 / 100;
		if (Field408) {
			var2 += 2;
		}
	
		if (var3 > -var2 && var3 < 128 + var2 && var4 > -var2 && var4 < 128 + var2) {
			Method83(var3 - var2, var4 - var2, var2 + var2, var2 + var2, 0, 360);
		}
	
	}
	
	public static final boolean Method273(int var0, int var1, int var2, int var3) {
		return var0 > 128 || var2 < 0 || var1 > 128 || var3 < 0;
	}
	
	public static final void Method274() {
		if (Field420 == null) {
			Field420 = Method11("window.pim", "window.ppl");
		}
	
		if (Field413 == null) {
			Field413 = Method11("arrow_left.pim", "arrow_left.ppl");
		}
	
		if (Field414 == null) {
			Field414 = Method11("arrow_right.pim", "arrow_right.ppl");
		}
	
		if (Field418 == null) {
			Field418 = new Image[3];
	
			for(int var0 = 0; var0 < 3; ++var0) {
				Field418[var0] = Method11("inside" + var0 + ".pim", "inside" + var0 + ".ppl");
			}
		}
	
		if (Field419 == null) {
			Field419 = Method11("inside_lamp.pim", "inside_lamp.ppl");
		}
	
		if (Field417 == null) {
			Field417 = new Image[2];
			Field417[0] = Method11("piston_top.pim", "piston_top.ppl");
			Field417[1] = Method11("piston_bottom.pim", "piston_bottom.ppl");
		}
	
		if (Field412 == null) {
			Field412 = new Image[10];
	
			for(int var1 = 0; var1 < Field412.length; ++var1) {
				Field412[var1] = Method11("shipDecor" + var1 + ".pim", "shipDecor" + var1 + ".ppl");
			}
	
			Field411 = Method40("decor_background.bin");
			Field410 = Method40("decor_foreground.bin");
		}
	
		if (Field415 == null) {
			Field415 = new Image[9];
	
			for(int var2 = 0; var2 < 9; ++var2) {
				Field415[var2] = Method11("shipicon" + var2 + ".pim", "shipicon" + var2 + ".ppl");
			}
		}
	
		if (Field416 == null) {
			Field416 = new Image[9];
	
			for(int var3 = 0; var3 < 9; ++var3) {
				Field416[var3] = Method11("shipicon" + var3 + "b.pim", "shipicon" + var3 + "b.ppl");
			}
		}
	
	}
	
	public static final void Method275() {
		Method107(2, 12, 92, 46);
		Field409 = 100;
		Method220(-2);
		Method274();
		Field321 = 40000;
		Method238(Field298[0], Field320 / 2, 0);
	}
	
	public static final int Method276(int var0, boolean var1) {
		byte var2 = 0;
		switch (var0) {
			case 1:
				if (!var1) {
					Method131("start" + Field328 + ".bms", Field298[0], Field299[0]);
					var2 = 1;
				} else {
					var2 = 6;
				}
				break;
			case 2:
				if (var1) {
					Method132("menu_sure_restart.bms", Field298[0], Field299[0], true);
					var2 = 3;
				} else {
					Method131("menu_no_restart.bms", Field298[0], Field299[0]);
				}
				break;
			case 3:
				if (var1) {
					Method131("menu_no_holodeck.bms", Field298[0], Field299[0]);
				} else {
					Method131("menu_enter_holodeck.bms", Field298[0], Field299[0]);
					var2 = 4;
				}
				break;
			case 4:
				Method97(8, 0);
				break;
			case 5:
				if (Field16) {
					Method131("menu_audio_off.bms", Field298[0], Field299[0]);
					Method29();
					Method31();
				} else {
					Method131("menu_audio_on.bms", Field298[0], Field299[0]);
				}
	
				Field16 = !Field16;
				break;
			case 6:
				if (Field74) {
					Method131("menu_vibra_off.bms", Field298[0], Field299[0]);
				} else {
					Method131("menu_vibra_on.bms", Field298[0], Field299[0]);
				}
	
				Field74 = !Field74;
				Method20();
				Method100(640);
			case 7:
			default:
				break;
			case 8:
				Method132("menu_sure_exit.bms", Field298[0], Field299[0], true);
				var2 = 2;
				break;
			case 9:
				Method132("menu_sure_reset.bms", Field298[0], Field299[0], true);
				var2 = 5;
		}
	
		return var2;
	}
	
	public static final void Method277(boolean var0) {
		Field428 = true;
		Method280();
		Field428 = false;
		if (!var0) {
			Method156(6, 13107200, true);
		}
	
		Method155();
		Method279(Field410);
		Method278();
	}
	
	public static final void Method278() {
		boolean var0 = false;
		if (Field298[0] > 7208960) {
			Method80(Field413, 4, 88, 0);
		}
	
		if (Field298[0] < 68812800) {
			Method80(Field414, 107, 88, 0);
		}
	
	}
	
	public static final void Method279(short[] var0) {
		if (var0 != null) {
			boolean var1 = false;
			int var2 = 64 - Field398 * Field370 / 100;
	
			for(int var3 = 0; var3 < var0.length; var3 += 3) {
				Image var4;
				if ((var4 = Field412[var0[var3]]) != null) {
					int var5 = var2 + Method222(var0[var3 + 1]) - var4.getWidth() / 2;
					int var6 = var4.getWidth();
					if (var5 > -var6 && var5 < 128 + var6) {
						Method80(var4, var5, -16 + var0[var3 + 2] - var4.getHeight() / 2, 0);
					}
				}
			}
	
		}
	}
	
	public static final void Method280() {
		boolean var0 = false;
		int var1;
		int var2;
		if ((var2 = var1 = 64 - Field398 * Field370 / 100) > 0) {
			var2 = 0;
		}
	
		Method80(Field418[0], var2, -16, 0);
		var2 += 28;
	
		for(int var3 = 0; var3 < 17; ++var3) {
			if (var2 + 46 > 0 && var2 <= 128) {
				Method80(Field418[1], var2, -16, 0);
			}
	
			var2 += 46;
		}
	
		if (var2 <= 128) {
			Method80(Field418[2], var2, -16, 0);
		}
	
		int var4 = var1 + 72 * Method222(193) / 100;
		Method80(Field419, var4 - Field419.getWidth() / 2, 2, 0);
		Method281(var1 + 299, 30, 0);
		Method281(var1 + 553, 30, 1);
		Method279(Field411);
		if (Field424 > 0) {
			int var12 = 2 + Field419.getHeight() / 2;
			Method75(16711680);
			int var5 = Field424;
			if (var4 > -var5 && var4 < 128 + var5 && var12 > -var5 && var12 < 128 + var5) {
				for(int var6 = 0; var6 < 2; ++var6) {
					for(int var7 = 20; var7 < 360; var7 += 90) {
						Method83(var4 - var5, var12 - var5, var5 + var5, var5 + var5, var7, 50);
					}
	
					--var5;
				}
			}
	
			Field424 += 2;
			if (Field424 > 30) {
				Field424 = 5;
			}
		}
	
		for(int var13 = 0; var13 < Field297; ++var13) {
			if ((Field303[var13] & 4) > 0 && Field306[var13] && Field305[var13] > 0) {
				int var14 = 72 * (Field298[var13] / 100) >> 16;
				int var15 = 72 * (Field299[var13] / 100) >> 16;
				Image var8;
				Method80(var8 = Field417[1], var1 + var14 - var8.getWidth() / 2, -16 + var15 - var8.getHeight(), 0);
				Method80(var8 = Field417[0], var1 + var14 - var8.getWidth() / 2, -16, 0);
				int var9 = Field305[var13] - 1;
				var8 = Field415[var9];
				switch (var9) {
					case 4:
						if (!Field16) {
							var8 = Field416[var9];
						}
						break;
					case 5:
						if (!Field74) {
							var8 = Field416[var9];
						}
				}
	
				Method80(var8, var1 + var14 - var8.getWidth() / 2, -16 + var15 - var8.getHeight() / 2, 0);
				if (Field428 && var1 + var14 > -100 && var1 + var14 < 228) {
					String var10 = Field425[var9];
					if (Field427) {
						var10 = Field426[var9];
					}
	
					Method92(var1 + var14 - Method71(var10, 3) / 2, 89, var10, 3);
				}
			}
		}
	
	}
	
	public static final void Method281(int var0, int var1, int var2) {
		int var3 = Field420.getWidth();
		int var4 = Field420.getHeight();
		var0 -= var3 / 2;
		Method75(0);
		Method85(var0, var1, var3, var4);
		Method78(var0, var1, var3, var4);
		Method75(16777215);
		Method108(var2, var0 - var3 + var3 * var0 / 128, var1);
		if (var0 >= -var3 && var0 <= 128) {
			Method80(Field420, var0, var1, 0);
		}
	
		Method85(0, 0, 128, 128);
	}
	
	public static final void Method282() {
		if (Field421 == null) {
			Field421 = Method11("outside.pim", "outside.ppl");
		}
	
		if (Field422 == null) {
			Field422 = new Image[2];
			Field422[0] = Method11("flame0.pim", "flame0.ppl");
			Field422[1] = Method11("flame1.pim", "flame1.ppl");
		}
	
		Method75(0);
		Method78(0, 0, 128, 128);
		Method109(0, 0, 0, true);
		Method80(Field421, 33, 49, 0);
		Method80(Field422[(int)(Method101() / 100L % 2L)], 30, 62, 0);
	}
	
	public static final void Method283(int var0, int var1, int var2) {
		Field429 = 0;
		Field430 = 0;
		Field431 = 0;
		Field432 = 0;
		Field433 = 0;
		Field434 = 0;
		Field438 = true;
		Field435 = var0;
		Field436 = var1;
		Field437 = 1;
		if (var2 >= 0) {
			if (Field442 == null) {
				Field442 = Method11("planet.pim", "planet.ppl");
			}
	
			Field439 = true;
		} else {
			Field439 = false;
		}
	
		if (Field441 == null) {
			Field441 = Method11("ship.pim", "ship.ppl");
		}
	
		if (Field440 == null) {
			Field440 = Method11("pointer.pim", "pointer.ppl");
		}
	
	}
	
	public static final void Method284() {
		Method285(Field435, Field436);
	}
	
	public static final void Method285(int var0, int var1) {
		Field429 = var0 << 8;
		Field430 = var1 << 8;
		Field431 = Field429;
		Field432 = Field430;
	}
	
	public static final void Method286() {
		Method287(Field435, Field436);
	}
	
	public static final void Method287(int var0, int var1) {
		Field431 = (var0 << 8) + 128;
		Field432 = (var1 << 8) + 128;
	}
	
	public static final boolean Method288() {
		Field429 += (Field431 - Field429) / 10;
		Field430 += (Field432 - Field430) / 10;
		return (Field429 & 16776960) == (Field431 & 16776960) && (Field430 & 16776960) == (Field432 & 16776960);
	}
	
	public static final void Method289() {
		Method75(0);
		Method78(0, 0, 128, 128);
		int var0 = -(Field429 >> 8);
		int var1 = -(Field430 >> 8);
		Method109(0, var0 - 64, var1 - 64, false);
		int var2 = Field433 + var0 + 64;
		int var3 = Field434 + var1 + 64;
		Method75(16777215);
		Method110(var2, var3);
		if (Field438) {
			Method80(Field440, var2 - 16, var3 - 58 - 2, 0);
			Method80(Field441, var2 - Field441.getWidth() / 2, var3 - 58 + (33 - Field441.getHeight()) / 2 - 2, 0);
		}
	
		var2 = Field435 + var0 + 64;
		var3 = Field436 + var1 + 64;
		Method110(var2, var3);
		if (Field437 > 0) {
			Method75(16711680);
			int var4 = Field437;
			Method83(var2 - var4, var3 - var4, var4 * 2, var4 * 2, 20, 50);
			Method83(var2 - var4, var3 - var4, var4 * 2, var4 * 2, 110, 50);
			Method83(var2 - var4, var3 - var4, var4 * 2, var4 * 2, 200, 50);
			Method83(var2 - var4, var3 - var4, var4 * 2, var4 * 2, 290, 50);
			++Field437;
			if (Field437 > 20) {
				Field437 = 1;
			}
		}
	
		if (Field439) {
			Method80(Field440, var2 - 16, var3 - 58 - 2, 0);
			Method80(Field442, var2 - Field442.getWidth() / 2, var3 - 58 + (33 - Field442.getHeight()) / 2 - 2, 0);
		}
	
	}
	
	public static final void Method290(int var0) {
		Method255();
		Method280();
		if (var0 < 100) {
			Field409 = var0;
			Field398 = var0 * 72 / 100;
			if (Field398 == 0) {
				Field398 = 1;
			}
	
			Method156(6, 13107200, true);
			Field409 = 100;
			Field398 = 72;
		} else {
			Method156(6, 13107200, true);
		}
	
		Method155();
		Method279(Field410);
		Method14();
	}
	
	public static final void Method291() {
		Field444 = Field298[0];
		Field445 = Field299[0];
	
		for(int var0 = 0; var0 < 5; ++var0) {
			Field302[var0] = Field299[var0];
		}
	
		if (Field175 >= 0) {
			Method152();
		}
	
		Method231();
		Field446 = Field398;
		Field443 = 150;
		Method102();
		if (Field447 == null) {
			Field447 = Method57(327680);
			Field448 = Method57(327681);
		}
	
	}
	
	public static final boolean Method292() {
		Method239(Field444, Field445, 0);
		Field409 = 100;
		Field398 = Field446;
		Method252(false);
		if (Field443 < 100) {
			if (Field443 % 4 == 0) {
				++Field446;
			}
	
			Field409 = Field443;
		}
	
		if (Field443 > 0) {
			if (Field443 < 50) {
				Field398 = Field443 * 2 * 72 / 100;
	
				for(int var0 = 0; var0 < 5; ++var0) {
					int var1 = (100 - Field443 * 2) * (100 - Field443 * 2) * (100 - Field443 * 2) * (100 - Field443 * 2);
					int[] var10000 = Field299;
					var10000[var0] -= var1 >> 2;
					var10000 = Field298;
					var10000[var0] += var1 / 2000 * Method296((50 - Field443) * 45);
				}
	
				++Field443;
			}
	
			Method155();
		}
	
		Method103();
		int var2 = 90;
		if (Field443 > 105) {
			var2 = (150 - Field443) * 2;
		}
	
		Method92((128 - Method71(Field447, 0)) / 2 - 128 * Field450[var2] / 1000, 32, Field447, 0);
		Method92((128 - Method71(Field448, 0)) / 2 + 128 * Field450[var2] / 1000, 43, Field448, 0);
		Method14();
		Field443 -= 2;
		if (Method63(2, -1) == 2) {
			Field443 = -38;
		}
	
		if (Field443 < -40) {
			Method97(6, 0);
			Field393 = true;
			return true;
		} else {
			return false;
		}
	}
	
	public static final int Method293(int var0, int var1, int var2) {
		var0 /= 100000 / Field398;
		var1 /= 100000 / Field398;
		return Field450[var2] * var0 - Field449[var2] * var1;
	}
	
	public static final int Method294(int var0, int var1, int var2) {
		var0 /= 100000 / Field398;
		var1 /= 100000 / Field398;
		return Field449[var2] * var0 + Field450[var2] * var1;
	}
	
	public static final int Method295(int var0) {
		while(var0 < 0) {
			var0 += 360;
		}
	
		while(var0 >= 360) {
			var0 -= 360;
		}
	
		return Field449[var0];
	}
	
	public static final int Method296(int var0) {
		while(var0 < 0) {
			var0 += 360;
		}
	
		while(var0 >= 360) {
			var0 -= 360;
		}
	
		return Field450[var0];
	}
	
	public static final void Method297() {
		if (Field449 == null) {
			Field449 = Method40("sin1000.bin");
		}
	
		if (Field450 == null) {
			Field450 = Method40("cos1000.bin");
		}
	
	}
	
	public static final void Method298() {
		if (Field451 == null) {
			Field451 = new Image[4];
	
			for(int var0 = 0; var0 < 4; ++var0) {
				Field451[var0] = Method11("static" + var0 + ".pim", "static" + var0 + ".ppl");
			}
		}
	
		if (Field452 == null) {
			Field452 = new int[42];
		}
	
	}
	
	public static final void Method299() {
		Method298();
		Method75(Field460);
		Method78(0, 0, 128, 128);
		if (Field454) {
			Method300(Field457 + 12 * Field449[(int)(Method101() / 5L % 360L)] / 1000, Field458 + 12 * Field449[(int)(Method101() / 3L % 360L)] / 1000, Field459 + 40 + 20 * Field449[(int)(Method101() / 14L % 360L)] / 1000);
		} else {
			Method300(Field457, Field458, Field459);
		}
	
		if (Field453) {
			int var0 = 0;
	
			for(int var1 = 0; var1 <= 128; var1 += 23) {
				for(int var2 = -Method94() % 30; var2 <= 128; var2 += 23) {
					int var3;
					for(var3 = Method94() % 4; var3 == Field452[var0]; var3 = Method94() % 4) {
					}
	
					Field452[var0] = var3;
					Method80(Field451[var3], var2, var1, 0);
					++var0;
				}
			}
		}
	
		if (Field455) {
			Method75(16777215);
			Method78(0, 0, 128, 10);
			Method78(0, 0, 10, 128);
			Method78(118, 0, 10, 128);
			Method78(0, 98, 128, 30);
			Method75(0);
			Method79(10, 10, 107, 87);
			if (Field461 >= 0) {
				Method92(-1000, 106, Field462[Field461], 1);
			}
		}
	
	}
	
	public static final void Method300(int var0, int var1, int var2) {
		int var3 = 40 * var2 / 100;
		int var4 = 93 * var2 / 100;
		Method75(0);
		Method82(var0 - var3 / 2 - 2, var1 - var4 / 2 - 2, var3 + 2 + 2, var4 + 2 + 2, 0, 360);
		if (Field453) {
			Method75(6908265);
		} else {
			Method75(10494192);
		}
	
		Method82(var0 - var3 / 2, var1 - var4 / 2, var3, var4, 0, 360);
		Method75(0);
		int var5 = 23 * var2 / 100;
		int var6 = 5 * var2 / 100;
		if (Field134 && !Field455 && (Method101() / 150L & 1L) > 0L) {
			Method82(var0 - var5 / 2, var1 - var6 * 2, var5, var6 * 2, 0, 360);
		} else {
			Method82(var0 - var5 / 2, var1 - var6, var5, var6, 0, 360);
		}
	
		Method75(16777215);
		int var7 = 14 * var2 / 100;
		int var8 = 6 * var2 / 100;
	
		for(int var9 = 0; var9 < 3; ++var9) {
			Method82(var0 - 3 * var7 / 2 + var9 * var7, var1 - 45 * var4 / 100, var7, var7, 0, 360);
		}
	
		Method75(0);
	
		for(int var10 = 0; var10 < 3; ++var10) {
			Method82(var0 - 3 * var7 / 2 + var10 * var7 + var7 / 3, var1 - 45 * var4 / 100 + var7 / 3, var8, var8, 0, 360);
		}
	
		if (Field456 || !Field455 && Method94() > 240) {
			if (Field453) {
				Method75(6908265);
			} else {
				Method75(10494192);
			}
	
			for(int var11 = 0; var11 < 3; ++var11) {
				Method82(var0 - 3 * var7 / 2 + var11 * var7, var1 - 45 * var4 / 100, var7, var7, 0, 360);
			}
	
			Field456 = !Field456;
		}
	
	}
	
	public static final void Method301() {
		Field463 = null;
		Field464 = null;
	}
	
	public static final void Method302(String[] var0, int var1) {
		Field470 = var0.length;
		Field468 = 360 / Field470;
		Field469 = Field468 / 2;
		Field463 = new Image[Field470];
	
		for(int var2 = 0; var2 < Field470; ++var2) {
			Field463[var2] = Method11(var0[(var2 + var1) % Field470] + ".pim", var0[(var2 + var1) % Field470] + ".ppl");
		}
	
		Field464 = new Image[2];
		Field464[0] = Method11("arrow_left.pim", "arrow_left.ppl");
		Field464[1] = Method11("arrow_right.pim", "arrow_right.ppl");
		Method213(10 + Field470 * 2 + 1);
		Method160(Field470);
		Field348 = -10;
		Field321 = 30000;
		Field322 = 64000;
		Field467 = 0;
		Field471 = 10 + Field470 * 2;
		Field472 = var1;
	
		for(int var5 = 0; var5 < Field470; ++var5) {
			int var3 = 5242 * Method296(90 + var5 * Field468);
			int var4 = 5242 * Method295(90 + var5 * Field468);
			Method214(var5 + 10, var3, var4, 327680, 200, 0, 0, false);
			var3 = 6881 * Method296(90 + var5 * Field468);
			var4 = 6881 * Method295(90 + var5 * Field468);
			Method214(var5 + 10 + Field470, var3, var4, 1638400, 100, 3, 0, true);
		}
	
		Method214(Field471, 0, 0, 5242880, 200, 2, 0, false);
	
		for(int var6 = 0; var6 < Field470; ++var6) {
			Method161(var6, var6 + 10, var6 + 10 + Field470, 4, 3932160, 65000, true, true);
		}
	
		Method238(Field298[Field471], Field299[Field471] + 6553600, 0);
		Field465 = 0;
		Field466 = Method101();
	}
	
	public static final int Method303() {
		if (Field465 == 1 && Method64(2, -1, true) == 2) {
			++Field465;
			Field466 = Method101() + 500L;
		}
	
		if (Method4(8)) {
			Field467 += 4;
		} else if (Method4(16)) {
			Field467 -= 4;
		} else {
			int var0;
			if ((var0 = Field467 % Field468) > Field469) {
				Field467 += 2;
			} else if (var0 > 0) {
				Field467 -= 2;
			}
		}
	
		if (Field467 >= 360) {
			Field467 -= 360;
		}
	
		if (Field467 < 0) {
			Field467 += 360;
		}
	
		for(int var3 = 0; var3 < Field470; ++var3) {
			int var1 = 5242 * Method296(Field467 + 90 + var3 * Field468);
			int var2 = 5242 * Method295(Field467 + 90 + var3 * Field468);
			Field298[var3 + 10] = var1;
			Field299[var3 + 10] = var2;
		}
	
		Method212();
		if (Field465 == 0) {
			Field409 = (int)((Method101() - Field466) / 5L);
		}
	
		if (Field465 == 2) {
			Field409 = (int)((Field466 - Method101()) / 20L);
		}
	
		if (Field409 < 0) {
			Field409 = 0;
			++Field465;
		}
	
		if (Field409 > 100) {
			Field409 = 100;
			++Field465;
		}
	
		Method265(14540253);
		Method78(0, 0, 128, 128);
		Method269(Field298[Field471], Field299[Field471], Field300[Field471], 11184810);
		Method269(Field298[Field471], Field299[Field471], Field300[Field471] / 2, 14540253);
		Method265(0);
	
		for(int var4 = 10; var4 < 10 + Field470; ++var4) {
			Method264(Field298[var4], Field299[var4], Field298[var4 + Field470], Field299[var4 + Field470]);
		}
	
		for(int var5 = 0; var5 < Field470; ++var5) {
			Method270(Field298[var5 + 10 + Field470], Field299[var5 + 10 + Field470], Field300[var5 + 10 + Field470]);
			if (Field409 > 20) {
				Method268(Field463[var5], Field298[var5 + 10 + Field470], Field299[var5 + 10 + Field470]);
			}
		}
	
		Method265(8947848);
	
		for(int var6 = 0; var6 < 5; ++var6) {
			int var9 = 3932 * Method296(Field467 + 90 - 36 + var6 * 72);
			int var10 = 3932 * Method295(Field467 + 90 - 36 + var6 * 72);
			Method270(var9, var10, 327680);
		}
	
		if (Field409 > 50) {
			Field473 += 12;
			if (Field473 > 179) {
				Field473 -= 180;
			}
	
			int var7 = 1 + 6 * Field449[Field473] / 1000;
			Method80(Field464[0], var7, 85 - Field464[0].getHeight() / 2, 0);
			Method80(Field464[1], 128 - var7 - Field464[1].getWidth(), 85 - Field464[1].getHeight() / 2, 0);
		}
	
		Method14();
		if (Field465 == 3) {
			int var8 = (360 - Field467) / Field468 + Field472;
			if (Field467 % Field468 > Field469) {
				++var8;
			}
	
			if (var8 >= Field470) {
				var8 -= Field470;
			}
	
			if (var8 < 0) {
				var8 += Field470;
			}
	
			return var8;
		} else {
			return -1;
		}
	}
}
