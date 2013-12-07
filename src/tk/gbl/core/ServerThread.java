package tk.gbl.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * 
 * @author Don'T PH 7.0 2013-8-30
 */
public class ServerThread extends Thread {
	String rootPath;
	Socket s;
	String method;

	public ServerThread(Socket socket, String rootPath) {
		this.s = socket;
		this.rootPath = rootPath;
	}

	@Override
	public void run() {
		OutputStream os = null;
		try {
			os = s.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(os);

		InputStream is = null;
		try {
			is = s.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String getPath = null;

		String line;
		try {
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String[] param = line.split(" ");
				
				if (param[0].equals("GET")) {
					method = param[0];
					getPath = param[1];
				}
				if (line.length() == 0)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("接受完毕");
		System.out.println(getPath);
		// 处理getPath

		ResponseManager responseManager = new ResponseManager(rootPath
				+ getPath);
		responseManager.setRootPath(rootPath);
		responseManager.setReqCurrPath(getPath);
		responseManager.setPrintWriter(pw);
		responseManager.setMethod(method);
		responseManager.output();

		pw.close();
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
