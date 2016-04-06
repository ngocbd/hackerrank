import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class G {
	InputStream is;
	PrintWriter out;
	String INPUT = "";
	
	int[] best;
	int[] seco;
	int[] argbest;
	int[] argseco;
	
	
	int mod = 1000000007;
	int mul = 31;
	long[] muls;
	int[] par, dep, fh, bh, pw;
	int[][] spar;
	
	void solve()
	{
		muls = new long[20005];
		muls[0] = 1;
		for(int i = 1;i < 20005;i++){
			muls[i] = muls[i-1] * mul % mod;
		}
		
		int n = ni();
		int[] from = new int[n - 1];
		int[] to = new int[n - 1];
		int[] w = new int[n-1];
		for (int i = 0; i < n - 1; i++) {
			from[i] = ni() - 1;
			to[i] = ni() - 1;
			w[i]  =nc();
		}
		int[][][] g = packWU(n, from, to, w);
		best = new int[n];
		for(int i = 0;i < n;i++)best[i] = i;
		seco = new int[n];
		Arrays.fill(seco, -1);
		argbest = new int[n]; Arrays.fill(argbest, -3);
		argseco = new int[n]; Arrays.fill(argseco, -3);
		
		int[][] pars = parents(g, 0);
		par = pars[0]; dep = pars[2]; fh = pars[3]; bh = pars[4]; pw = pars[5];
		spar = logstepParents(par);
		
		for(int i = 0;i < n;i++){
			if(i > 0)out.print(" ");
			out.print(dfs(i, -2, g) + 1);
		}
//		for(int[][] e : g){
//			tr(e);
//		}
		out.println();
	}
	
	boolean larger(int s, int x, int y)
	{
		if(y < 0)return true;
		int lsx = lca2(s, x, spar, dep);
		int lsy = lca2(s, y, spar, dep);
		int low = 0, high = Math.min(dep[s] + dep[x] - 2*dep[lsx], dep[s] + dep[y] - 2*dep[lsy]) + 1;
		while(high - low > 1){
			int h = high + low >>> 1;
			long hx = hash(s, lsx, x, h);
			long hy = hash(s, lsy, y, h);
			if(hx == hy){
				low = h;
			}else{
				high = h;
			}
		}
		int lenx = dep[s] + dep[x] - 2 * dep[lsx];
		int leny = dep[s] + dep[y] - 2 * dep[lsy];
		if(low == lenx && low == leny){
//			tr(s, x, y, x > y);
			return x > y;
		}else if(low == lenx){
//			tr(s, x, y, false);
			return false;
		}else if(low == leny){
//			tr(s, x, y, true);
			return true;
		}else{
//			tr(s, x, y, get(s, lsx, x, low+1) > get(s, lsy, y, low+1), "X", lenx, leny, low);
			return get(s, lsx, x, low+1) > get(s, lsy, y, low+1);
		}
	}
	
	int get(int a, int b, int c, int len)
	{
		if(len <= dep[a] - dep[b]){
			int aa = ancestor(a, len-1, spar);
			return pw[aa];
		}else{
			int ac = ancestor(c, dep[a] + dep[c] - 2*dep[b] - len, spar);
			return pw[ac];
		}
	}
	
	long hash(int a, int b, int c, int len)
	{
		if(len <= dep[a] - dep[b]){
			int aa = ancestor(a, len, spar);
			long x = bh[a] - bh[aa];
			if(x < 0)x += mod;
			x = x * invl(muls[dep[aa]], mod) % mod;
			return x;
		}else{
			long x = bh[a] - bh[b];
			if(x < 0)x += mod;
			x = x * invl(muls[dep[b]], mod) % mod;
			x = x * muls[len-(dep[a]-dep[b])] % mod;
			int ac = ancestor(c, dep[a]+dep[c]-2*dep[b]-len, spar);
			x += fh[ac] - fh[b] * muls[dep[ac]-dep[b]];
			x %= mod;
			if(x < 0)x += mod;
			return x;
		}
	}
	
	public static long invl(long a, long mod) {
		long b = mod;
		long p = 1, q = 0;
		while (b > 0) {
			long c = a / b;
			long d;
			d = a;
			a = b;
			b = d % b;
			d = p;
			p = q;
			q = d - c * q;
		}
		return p < 0 ? p + mod : p;
	}
	
	int dfs(int cur, int pre, int[][][] g)
	{
		for(int[] e : g[cur]){
			if(e[0] != pre && e[2] == -1){
				e[2] = dfs(e[0], cur, g);
				int can = e[2];
				if(larger(cur, can, best[cur])){
					seco[cur] = best[cur];
					best[cur] = can;
					argseco[cur] = argbest[cur];
					argbest[cur] = e[0];
				}else if(larger(cur, can, seco[cur])){
					seco[cur] = can;
					argseco[cur] = e[0];
				}
			}
		}
		return argbest[cur] == pre ? seco[cur] : best[cur];
	}
	
	public int[][] parents(int[][][] g, int root) {
		int n = g.length;
		int[] par = new int[n];
		Arrays.fill(par, -1);
		int[] fh = new int[n];
		int[] bh = new int[n];
		int[] pw = new int[n];
		int[] dep = new int[n];

		int[] q = new int[n];
		q[0] = root;
		for (int p = 0, r = 1; p < r; p++) {
			int cur = q[p];
			for (int[] nex : g[cur]) {
				if (par[cur] != nex[0]) {
					q[r++] = nex[0];
					par[nex[0]] = cur;
					dep[nex[0]] = dep[cur] + 1;
					fh[nex[0]] = (int)(((long)fh[cur] * mul + nex[1]) % mod);
					bh[nex[0]] = (int)((bh[cur] + nex[1] * muls[dep[nex[0]]-1]) % mod);
					pw[nex[0]] = nex[1];
				}
			}
		}
		return new int[][] { par, q, dep, fh, bh, pw };
	}
	
	public static int lca2(int a, int b, int[][] spar, int[] depth) {
		if (depth[a] < depth[b]) {
			b = ancestor(b, depth[b] - depth[a], spar);
		} else if (depth[a] > depth[b]) {
			a = ancestor(a, depth[a] - depth[b], spar);
		}

		if (a == b)
			return a;
		int sa = a, sb = b;
		for (int low = 0, high = depth[a], t = Integer.highestOneBit(high), k = Integer
				.numberOfTrailingZeros(t); t > 0; t >>>= 1, k--) {
			if ((low ^ high) >= t) {
				if (spar[k][sa] != spar[k][sb]) {
					low |= t;
					sa = spar[k][sa];
					sb = spar[k][sb];
				} else {
					high = low | t - 1;
				}
			}
		}
		return spar[0][sa];
	}

	protected static int ancestor(int a, int m, int[][] spar) {
		for (int i = 0; m > 0 && a != -1; m >>>= 1, i++) {
			if ((m & 1) == 1)
				a = spar[i][a];
		}
		return a;
	}

	public static int[][] logstepParents(int[] par) {
		int n = par.length;
		int m = Integer.numberOfTrailingZeros(Integer.highestOneBit(n - 1)) + 1;
		int[][] pars = new int[m][n];
		pars[0] = par;
		for (int j = 1; j < m; j++) {
			for (int i = 0; i < n; i++) {
				pars[j][i] = pars[j - 1][i] == -1 ? -1
						: pars[j - 1][pars[j - 1][i]];
			}
		}
		return pars;
	}
	
	static int[][] packU(int n, int[] from, int[] to) {
		int[][] g = new int[n][];
		int[] p = new int[n];
		for (int f : from)
			p[f]++;
		for (int t : to)
			p[t]++;
		for (int i = 0; i < n; i++)
			g[i] = new int[p[i]];
		for (int i = 0; i < from.length; i++) {
			g[from[i]][--p[from[i]]] = to[i];
			g[to[i]][--p[to[i]]] = from[i];
		}
		return g;
	}
	
	public static int[][][] packWU(int n, int[] from, int[] to, int[] w) {
		int[][][] g = new int[n][][];
		int[] p = new int[n];
		for (int f : from)
			p[f]++;
		for (int t : to)
			p[t]++;
		for (int i = 0; i < n; i++)
			g[i] = new int[p[i]][3];
		for (int i = 0; i < from.length; i++) {
			--p[from[i]];
			g[from[i]][p[from[i]]][0] = to[i];
			g[from[i]][p[from[i]]][1] = w[i];
			g[from[i]][p[from[i]]][2] = -1;
			--p[to[i]];
			g[to[i]][p[to[i]]][0] = from[i];
			g[to[i]][p[to[i]]][1] = w[i];
			g[to[i]][p[to[i]]][2] = -1;
		}
		return g;
	}
	
	void run() throws Exception
	{
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		long s = System.currentTimeMillis();
		solve();
		out.flush();
		if(!INPUT.isEmpty())tr(System.currentTimeMillis()-s+"ms");
	}
	
	public static void main(String[] args) throws Exception { new G().run(); }
	
	private byte[] inbuf = new byte[1024];
	private int lenbuf = 0, ptrbuf = 0;
	
	private int readByte()
	{
		if(lenbuf == -1)throw new InputMismatchException();
		if(ptrbuf >= lenbuf){
			ptrbuf = 0;
			try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
			if(lenbuf <= 0)return -1;
		}
		return inbuf[ptrbuf++];
	}
	
	private boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
	private int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
	
	private double nd() { return Double.parseDouble(ns()); }
	private char nc() { return (char)skip(); }
	
	private String ns()
	{
		int b = skip();
		StringBuilder sb = new StringBuilder();
		while(!(isSpaceChar(b))){ // when nextLine, (isSpaceChar(b) && b != ' ')
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	
	private char[] ns(int n)
	{
		char[] buf = new char[n];
		int b = skip(), p = 0;
		while(p < n && !(isSpaceChar(b))){
			buf[p++] = (char)b;
			b = readByte();
		}
		return n == p ? buf : Arrays.copyOf(buf, p);
	}
	
	private char[][] nm(int n, int m)
	{
		char[][] map = new char[n][];
		for(int i = 0;i < n;i++)map[i] = ns(m);
		return map;
	}
	
	private int[] na(int n)
	{
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = ni();
		return a;
	}
	
	private int ni()
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
	
	private long nl()
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
	
	private static void tr(Object... o) { System.out.println(Arrays.deepToString(o)); }
}
