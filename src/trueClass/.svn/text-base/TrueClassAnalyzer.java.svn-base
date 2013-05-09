package trueClass;

import java.util.Map;
import java.util.Vector;

import algorithms.ObjectReader;

public class TrueClassAnalyzer {
	private static final int LENGTH = 4;
	private static Map<String, Vector<Boolean>> musicData;
	private static Map<String, Vector<Boolean>> speechData;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadData();
		analize();
	}
	
	private static void analize() {
		int min = Integer.MAX_VALUE;
		for(String key : musicData.keySet())
		{
			Vector<Boolean> vector = musicData.get(key);
			int startIndex = 0, index = 1;
			Boolean value = vector.get(0);
			while (index < vector.size())
			{
				if (vector.get(index) != value)
				{
					if (min > index - startIndex) min = index - startIndex;
					if(index - startIndex == LENGTH)
					{
						System.out.println("StartIndex: " + startIndex + " File: " + key);
					}
					startIndex = index;
					value = vector.get(index);
				}
				index++;
			}
		}
		for(String key : speechData.keySet())
		{
			Vector<Boolean> vector = speechData.get(key);
			int startIndex = 0, index = 1;
			Boolean value = vector.get(0);
			while (index < vector.size())
			{
				if (vector.get(index) != value)
				{
					if (min > index - startIndex) min = index - startIndex;
					if(index - startIndex == LENGTH)
					{
						System.out.println("StartIndex: " + startIndex + " File: " + key);
					}
					startIndex = index;
					value = vector.get(index);
				}
				index++;
			}
		}
		System.out.println(min);
	}

	private static void loadData() {
		musicData = ObjectReader.loadObject("MusicTrueClassificationMap.obj");
		speechData = ObjectReader.loadObject("SpeechTrueClassificationMap.obj");
	}

}
