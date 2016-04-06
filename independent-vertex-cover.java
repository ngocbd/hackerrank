import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;

public class F {
	InputStream is;
	PrintWriter out;
	String INPUT = "";
	
	void solve()
	{
		int n = ni();
		int mod = 1000000007;
		int[] a = na(n);
		long[] b = new long[n];
		for(int i = 0;i < n;i++){
			b[i] = (long)a[n-1-i] * 1000000007 + i;
		}
		int lis = lis(b);
		if(lis >= 3){
			out.println(0);
			return;
		}
		
		boolean[] first = new boolean[n];
		int pre = -1;
		for(int i = 0;i < n;i++){
			if(pre < a[i]){
				first[i] = true;
				pre = a[i];
			}
		}
		
		int head = 0, tail = 0;
		long ret = 1;
		int reach = -1;
		for(int i = 0;i < n;i++){
			if(!first[i])continue;
			while(head < n && (head <= i || first[head] || a[i] >= a[head]))head++;
			while(tail < n && (tail <= i || first[tail]))tail++;
			if(head == tail){
				ret = ret * 2;
				ret %= mod;
			}else{
				if(tail < reach){
				}else{
					ret = ret * 2;
					ret %= mod;
				}
				reach = head;
			}
		}
		out.println(ret);
	}
	
	public static int lis(long[] in)
	{
		int n = in.length;
		int ret = 0;
		long[] h = new long[n + 1];
		Arrays.fill(h, Long.MIN_VALUE / 2);
		for(int i = 0;i < n;i++){
			int ind = Arrays.binarySearch(h, 0, ret + 1, in[i]);
			if(ind < 0){
				ind = -ind-2;
				h[ind+1] = in[i];
				if(ind >= ret)ret++;
			}
		}
		return ret;
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
	
	public static void main(String[] args) throws Exception { new F().run(); }
	
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
