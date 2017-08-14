package util;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class URLPageToTextUtil {
	public static String getTextFromURL(String url) throws IOException {

		URL pageURL = new URL(url);
		StringBuilder text = new StringBuilder();
		Scanner scan = new Scanner(pageURL.openStream(), "utf-8");
		
		try {
			while (scan.hasNextLine()) {
				text.append(scan.nextLine());
			}
		} finally {
			scan.close();
		}
		return text.toString();
	}
}
