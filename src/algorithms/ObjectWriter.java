package algorithms;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class ObjectWriter {
	public static <T> void writeObject(T object, String fileName) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fos);
			out.writeObject(object);
			out.close();
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
