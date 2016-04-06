import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Solution {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";
	
	static void solve()
	{
		ni();
		int L = ni(), W = ni(), H = ni();
		int a = ni(), b = ni(), c = ni();
		int d = ni(), e = ni(), f = ni();
		int p = ni(), q = ni(), r = ni();
		int n = ni();
		
		int[][] ax1 = go(p, a, L, n);
		int[][] ax2 = go(p, 2*L-a, L, n);
		int[][] ay1 = go(q, b, W, n);
		int[][] ay2 = go(q, 2*W-b, W, n);
		int[][] az1 = go(r, c, H, n);
		int[][] az2 = go(r, 2*H-c, H, n);
		int[][] bx1 = go(p, d, L, n);
		int[][] bx2 = go(p, 2*L-d, L, n);
		int[][] by1 = go(q, e, W, n);
		int[][] by2 = go(q, 2*W-e, W, n);
		int[][] bz1 = go(r, f, H, n);
		int[][] bz2 = go(r, 2*H-f, H, n);
		
		int cta = 0, ctb = 0;
		for(int x = -n;x <= n;x++){
			for(int y = -n;y <= n;y++){
				for(int z = -n;z <= n;z++){
					if(x == 0 && y == 0 && z == 0)continue;
					int vx = x, vy = y, vz = z;
					int g = gcd(gcd(Math.abs(vx), Math.abs(vy)), Math.abs(vz));
					vx /= g; vy /= g; vz /= g;
//					tr(vx, vy, vz);
					
					long opta = Long.MAX_VALUE;
					for(int[][] ax : new int[][][]{ax1, ax2}){
						for(int[][] ay : new int[][][]{ay1, ay2}){
							for(int[][] az : new int[][][]{az1, az2}){
								if(ax[vx+n] == null || ay[vy+n] == null || az[vz+n] == null)continue;
								long crt = crt(
										new long[]{ax[vx+n][1], ay[vy+n][1], az[vz+n][1]},
										new long[]{ax[vx+n][0], ay[vy+n][0], az[vz+n][0]}
										);
								if(crt == -1)continue;
								opta = Math.min(opta, crt);
							}
						}
					}
					long optb = Long.MAX_VALUE;
					for(int[][] bx : new int[][][]{bx1, bx2}){
						for(int[][] by : new int[][][]{by1, by2}){
							for(int[][] bz : new int[][][]{bz1, bz2}){
								if(bx[vx+n] == null || by[vy+n] == null || bz[vz+n] == null)continue;
								long crt = crt(
										new long[]{bx[vx+n][1], by[vy+n][1], bz[vz+n][1]},
										new long[]{bx[vx+n][0], by[vy+n][0], bz[vz+n][0]}
										);
								if(crt == -1)continue;
								optb = Math.min(optb, crt);
							}
						}
					}
//					tr("go", opta, optb);
					if(opta == Long.MAX_VALUE && optb == Long.MAX_VALUE)continue;
					if(opta < optb){
						cta++;
					}else{
						ctb++;
					}
				}
			}
		}
		out.println(cta + " " + ctb);
	}
	
	public static long crt(final long[] divs, final long[] mods)
	{
		long div = divs[0];
		long mod = mods[0];
		for(int i = 1;i < divs.length;i++){
			long[] apr = exGCD(div, divs[i]);
			if((mods[i] - mod) % apr[0] != 0)return -1;
			long ndiv = div / apr[0] * divs[i];
			long da = div / apr[0];
			long nmod = (apr[1]*(mods[i]-mod)%ndiv*da%ndiv+mod)%ndiv;
			if(nmod < 0)nmod += ndiv;
			div = ndiv;
			mod = nmod;
		}
		return mod;
	}
	
	public static long[] exGCD(long a, long b)
	{
		if(a == 0 || b == 0)return null;
		int as = Long.signum(a);
		int bs = Long.signum(b);
		a = Math.abs(a); b = Math.abs(b);
		long p = 1, q = 0, r = 0, s = 1;
		while(b > 0){
			long c = a / b;
			long d;
			d = a; a = b; b = d % b;
			d = p; p = q; q = d - c * q;
			d = r; r = s; s = d - c * s;
		}
		return new long[]{a, p * as, r * bs};
	}
	
	public static BigInteger b(long x){ return BigInteger.valueOf(x); }
	
	public static BigInteger[] exGCD(BigInteger a, BigInteger b)
	{
		BigInteger p = BigInteger.ONE, q = BigInteger.ZERO, r = BigInteger.ZERO, s = BigInteger.ONE;
		while(b.signum() > 0){
			BigInteger c = a.divide(b);
			BigInteger d;
			d = a; a = b; b = d.mod(b);
			d = p; p = q; q = d.subtract(c.multiply(q));
			d = r; r = s; s = d.subtract(c.multiply(s));
		}
		return new BigInteger[]{a, p, r};
	}
	
	public static BigInteger crtx(long[] divs, long[] mods)
	{
		BigInteger div = b(divs[0]), mod = b(mods[0]);
		for(int i = 1;i < divs.length;i++){
			BigInteger[] apr = exGCD(div, b(divs[i]));
			BigInteger mm = b(mods[i]).subtract(mod);
			if(mm.mod(apr[0]).signum() != 0)return b(-1);
			BigInteger da = div.divide(apr[0]);
			BigInteger ndiv = b(divs[i]).multiply(da);
			BigInteger nmod = apr[1].multiply(mm).multiply(da).add(mod).mod(ndiv);
			if(nmod.signum() < 0)nmod = nmod.add(ndiv);
			div = ndiv;
			mod = nmod;
		}
		return mod;
	}
	
	public static int gcd(int a, int b) {
		if(a == 0)return b;
		if(b == 0)return a;
		while (b > 0){
			int c = a;
			a = b;
			b = c % b;
		}
		return a;
	}
	
	
	static int[][] go(int p, int a, int L, int n)
	{
		int[][] pet = new int[2*n+1][];
		for(int x = -n;x <= n;x++){
			int[] hit = new int[2];
			int hp = 0;
			int y = p-a;
			if(y < 0)y += 2*L;
			for(int t = 0;t <= 400;t++){
				if(y == 0){
					hit[hp++] = t;
					if(hp == 2)break;
				}
				y += x;
				while(y >= 2*L)y -= 2*L;
				while(y < 0)y += 2*L;
			}
			if(hp == 2){
				hit[1] -= hit[0];
				pet[x+n] = hit;
			}else if(hp == 1){
				throw new AssertionError(p + " " + a + " " + x + " " + L);
			}else{
				pet[x+n] = null;
			}
		}
		return pet;
	}
	
	public static void main(String[] args) throws Exception
	{
		long S = System.currentTimeMillis();
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G-S+"ms");
	}
	
	private static boolean eof()
	{
		if(lenbuf == -1)return true;
		int lptr = ptrbuf;
		while(lptr < lenbuf)if(!isSpaceChar(inbuf[lptr++]))return false;
		
		try {
			is.mark(1000);
			while(true){
				int b = is.read();
				if(b == -1){
					is.reset();
					return true;
				}else if(!isSpaceChar(b)){
					is.reset();
					return false;
				}
			}
		} catch (IOException e) {
			return true;
		}
	}
	
	private static byte[] inbuf = new byte[1024];
	static int lenbuf = 0, ptrbuf = 0;
	
	private static int readByte()
	{
		if(lenbuf == -1)throw new InputMismatchException();
		if(ptrbuf >= lenbuf){
			ptrbuf = 0;
			try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
			if(lenbuf <= 0)return -1;
		}
		return inbuf[ptrbuf++];
	}
	
	private static boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
	private static int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
	
	private static double nd() { return Double.parseDouble(ns()); }
	private static char nc() { return (char)skip(); }
	
	private static String ns()
	{
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while(!(isSpaceChar(b))){ // when nextLine, (isSpaceChar(b) && b != ' ')
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	
	private static char[] ns(int n)
	{
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while(p < n && !(isSpaceChar(b))){
			buf[p++] = (char)b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}
	
	private static char[][] nm(int n, int m)
	{
		char[][] map = new char[n][];
		for(int i = 0;i < n;i++)map[i] = ns(m);
		return map;
	}
	
	private static int[] na(int n)
	{
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = ni();
		return a;
	}
	
	private static int ni()
	{
		int num = 0, b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static long nl()
	{
		long num = 0;
		int b;
		boolean minus = false;
		while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
		if(b == '-'){
			minus = true;
			b = readByte();
		}
		
		while(true){
			if(b >= '0' && b <= '9'){
				num = num * 10 + (b - '0');
			}else{
				return minus ? -num : num;
			}
			b = readByte();
		}
	}
	
	private static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}
