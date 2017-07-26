import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * 
 * @author komal
 *
 */
public class PexChallenge {

	public static String DELIMETER = ",";
	public static String URL_FILE_PATH = "urls.txt";
	public static String RESULT_FILE_PATH = "result.csv";

	public static void main(String[] args) {

		File file = new File(URL_FILE_PATH);
		File resultFile = new File(RESULT_FILE_PATH);
		try {
			if (!resultFile.exists()) {
				resultFile.createNewFile();
			}
			BufferedReader b = new BufferedReader(new FileReader(file));

			PrintWriter pw = new PrintWriter(resultFile.getAbsoluteFile());
			String readLine = "";

			while ((readLine = b.readLine()) != null) {
				String url = readLine;
				String top3colors = getImageFromUrl(url);
				System.out.println(top3colors);
				pw.write(url + DELIMETER + top3colors + "\n");
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getImageFromUrl(String urlStr) throws MalformedURLException, IOException {
		URL url = new URL(urlStr);
		BufferedImage image = ImageIO.read(url);

		int height = image.getHeight();
		int width = image.getWidth();
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				Integer counter = (Integer) map.get(rgb);
				if (counter == null)
					counter = 0;
				counter++;
				map.put(rgb, counter);
			}
		}
		return getTopThreeCommonColour(map);
	}

	public static int[] getRGBArr(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return new int[] { red, green, blue };

	}

	public static String getTopThreeCommonColour(Map<Integer, Integer> map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		Map.Entry me1 = (Map.Entry) list.get(list.size() - 1);
		Map.Entry me2 = (Map.Entry) list.get(list.size() - 2);
		Map.Entry me3 = (Map.Entry) list.get(list.size() - 3);
		int[] rgb1 = getRGBArr((Integer) me1.getKey());
		int[] rgb2 = getRGBArr((Integer) me2.getKey());
		int[] rgb3 = getRGBArr((Integer) me3.getKey());

		return rgb1[0] + rgb1[1] + rgb1[2] + DELIMETER //
				+ rgb2[0] + rgb2[1] + rgb2[2] + DELIMETER //
				+ rgb3[0] + rgb3[1] + rgb3[2];
	}
}
