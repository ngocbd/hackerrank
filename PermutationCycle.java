import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class G2 {
	InputStream is;
	PrintWriter out;
	String INPUT = "";
	
	void solve()
	{
		int n = ni(), K = ni(), T = ni();
		int[] f = na(n);
		for(int i = 0;i < n;i++)f[i]--;
		int[][] cys = splitIntoCycleProfs(f);
		int[] ff = new int[n+1];
		for(int c : cys[2]){
			ff[c]++;
		}
		int[] ds = new int[2000];
		int p = 0;
		for(int d = 1;d * d <= T;d++){
			if(T % d == 0){
				ds[p++] = d;
				if(d*d < T)ds[p++] = T/d;
			}
		}
		Arrays.sort(ds, 0, p);
		ds = Arrays.copyOf(ds, p);
		
		long[][] dp = new long[n+1][2]; // min swap
		int[][] mindp = new int[n+1][2];
		for(int i = 0;i < n+1;i++){
			dp[i][0] = dp[i][1] = Long.MAX_VALUE / 100;
		}
		dp[0][0] = 0;
		for(int k = 1;k <= n;k++){
			if(ff[k] == 0){
				dp[k][0] = dp[k-1][0];
				dp[k][1] = dp[k-1][1];
				mindp[k][0] = -1;
				mindp[k][1] = -1;
				continue;
			}
			for(int j = ds.length-1;j >= 0;j--){
				int y = ds[j];
				if(gcd(y, k) == 1){
					int d = T/y;
					if(ff[k] % d == 0){
						long plus = ((long)d*k-1)*(ff[k]/d);
						int pp = (int)(plus&1);
						for(int u = 0;u < 2;u++){
							if(dp[k-1][u^pp] + plus < dp[k][u]){
								dp[k][u] = dp[k-1][u^pp] + plus;
								mindp[k][u] = d;
							}
						}
					}
				}
			}
		}
		if(dp[n][K&1] > K){
			out.println("no solution");
			return;
		}
		
		int[] minds = new int[n+1];
		int[] leaps = new int[n+1];
		int lk = K&1;
		for(int k = n;k >= 1;k--){
			minds[k] = mindp[k][lk];
			leaps[k] = (T/minds[k])%k;
			long plus = ((long)minds[k]*k-1)*(ff[k]/minds[k]);
			int pp = (int)(plus&1);
			lk ^= pp;
		}
		
		int[][] cs = new int[n+1][];
		int[] indc = new int[n+1];
		boolean[] ved = new boolean[n]; // ved clus
		int[] clus = cys[0];
		int[][] check = new int[K][];
		int cp = 0;
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0;i < n;i++){
			if(!ved[clus[i]]){
				ved[clus[i]] = true;
				int len = cys[2][clus[i]];
				if(cs[len] == null){
					cs[len] = new int[minds[len]*len];
					indc[len] = 0;
				}
				cs[len][indc[len]+0*minds[len]] = i;
				int alen = cs[len].length;
				if(alen == 1)continue;
				int le = (int)((long)minds[len]*leaps[len]%alen);
				int q = indc[len] + le;
				for(int j = f[i];j != i;j = f[j], q += le){
					if(q >= alen)q -= alen;
					cs[len][q] = j;
				}
				if(++indc[len] == minds[len]){
					for(int k = 0, l = 1;l < cs[len].length;k++,l++){
						if(l == cs[len].length)l = 0;
//						out.println((cs[len][k]+1) + " " + (cs[len][l]+1));
						sb.append((cs[len][k]+1) + " " + (cs[len][l]+1) + "\n");
						K--;
						check[cp++] = new int[]{cs[len][k], cs[len][l]};
					}
					cs[len] = null;
				}
			}
		}
		assert K >= 0;
		assert K % 2 == 0;
		if(n == 1 && K > 0){
			out.println("no solution");
			return;
		}
		for(int i = 0;i < K;i++){
			sb.append(1 + " " + 2 + "\n");
//			out.println(1 + " " + 2);
			check[cp++] = new int[]{0, 1};
		}
//		check(check, f, T);
		out.print(sb);
	}
	
	void check(int[][] c, int[] f, int T)
	{
		int n = f.length;
		int[] a = new int[n];
		for(int i = 0;i < n;i++)a[i] = i;
		for(int i = 0;i < T;i++){
			for(int[] u : c){
				int d = a[u[0]]; a[u[0]] = a[u[1]]; a[u[1]] = d;
			}
		}
		if(!Arrays.equals(f, a)){
			throw new RuntimeException();
//			tr(f, a, "DEAD");
		}
	}
	
	public static int gcd(int a, int b) {
		while (b > 0) {
			int c = a;
			a = b;
			b = c % b;
		}
		return a;
	}
	
	
	public static int[][] splitIntoCycleProfs(int[] f)
	{
		int n = f.length;
		int[] ved = new int[n];
		int[] clus = new int[n];
		Arrays.fill(clus, -1);
		int[] ncyc = new int[n];
		int ncy = 0;
		int[] cind = new int[n];
		Arrays.fill(cind, -1);
		for(int i = 0;i < n;i++){
			int cur = i;
			int p = 0;
			while(ved[cur] == 0){
				ved[cur] = i+1; 
				cind[cur] = p++;
				cur = f[cur];
			}
			if(ved[cur] == i+1){
				// cycle
				int o = cur;
				cur = f[cur];
				clus[o] = ncy;
				int cy = 1;
				while(cur != o){
					clus[cur] = ncy;
					cur = f[cur];
					cy++;
				}
				ncyc[ncy] = cy;
				ncy++;
			}
		}
		return new int[][]{clus, cind, Arrays.copyOf(ncyc, ncy)};
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
	
	public static void main(String[] args) throws Exception { new G2().run(); }
	
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
