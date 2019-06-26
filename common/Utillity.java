package common;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Utillity {

	//Listが返却
	public static List<String> readFileList(String path) {

		List <String> lines = new ArrayList<String>();
		try {
			File file = new File(path);
			if(!file.exists()) {
				System.out.println("ファイルが存在しません。");
				//return "";
			}
			//BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する。
			FileReader filereader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String data;


			while((data = bufferedReader.readLine()) != null) {
				lines.add(data);
			}

			bufferedReader.close();

		}catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static String readFileStr(String path) {
		String line = "";
		try {
			File file = new File(path);
			if(!file.exists()) {
				System.out.println("ファイルが存在しません。");
				//return "";
			}
			//BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する。
			FileReader filereader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String data;
			while((data = bufferedReader.readLine()) != null) {
				line += data;
			}

			bufferedReader.close();

		}catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public static void main(String[] args) {
		//List <String> html = new ArrayList<String>();

		//html = readFileList("/Users/teppei/desktop/test.html");
		String htmlOneLine = readFileStr("/Users/teppei/desktop/test.html");
		//for(int i = 0 ;i < html.size();i++) {
		//	System.out.println(html.get(i));
		//}
		System.out.println(htmlOneLine);
	}

}
