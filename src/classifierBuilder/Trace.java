package classifierBuilder;
import java.sql.Timestamp;

public class Trace {
	public static void print(String str) {
		System.out.println("#" + new Timestamp(System.currentTimeMillis()) + " "
				+ str);
	}

	public static void printSpace(String str) {
		System.out.println("   #" + new Timestamp(System.currentTimeMillis()) + " "
				+ str);
	}
}
