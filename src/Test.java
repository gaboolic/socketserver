import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 用来测试的。。。
 * 这个是可以单独使用的web服务器 只实现了静态文件
 * @author Don'T PH 7.0
 * 2013-12-7
 */
public class Test {

	String getPath;

	public void test() throws IOException {
		ServerSocket ss = new ServerSocket(9999);

		while (true) {
			Socket s = ss.accept();
			InputStream is = s.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String[] param = line.split(" ");
				if (param[0].equals("GET")) {
					getPath = param[1];
					// break;
				}
				if (line.length() == 0)
					break;
			}

			System.out.println("接受完毕");
			System.out.println(getPath);

			BufferedReader fileReader = new BufferedReader(new FileReader(
					"C:/Users/Administrator/Desktop" + getPath));
			String outStr = "";
			while ((line = fileReader.readLine()) != null) {
				outStr += line;
			}
			fileReader.close();
			System.out.println(outStr);
			
			OutputStream os = s.getOutputStream();
			PrintWriter pw = new PrintWriter(os);

			String str = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n";
			pw.println(str);
			pw.println(outStr);

			pw.close();
			os.close();
			s.close();

		}

	}

	public static void main(String[] args) throws IOException {
		new Test().test();
	}

}
