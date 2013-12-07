package tk.gbl.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class HttpServer implements Runnable{
	String port;
	String path;
	
	boolean isStart = true;
	ServerSocket ss = null;
	
	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public HttpServer(String port,String path){
		this.port = port;
		this.path = path;
	}
	
	public HttpServer(){
		this("80","C:/Users/Administrator/Desktop");
	}
	
	public void start(){
		init();
		
		try {
			ss = new ServerSocket(Integer.parseInt(port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
		
	}

	private void init() {
		
	}

	@Override
	public void run() {
		while (isStart) {
			Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				
			}
			new ServerThread(s,path).start();
		}
	}
}
