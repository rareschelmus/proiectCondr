package model;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;



@ServerEndpoint("/Controller/scan")
public class Scanner {
	private byte[] buffer = new byte[1000000];
	private int index = 0;
	//ScanController scanController = new ScanController();
	@OnOpen
	public void onOpen(Session session)
	{
		System.out.println("hello"+session.getId());
	}
	
	@OnClose
	public void onClose(Session session)
	{
		System.out.println("it's done");
	}
	
	@OnError
    public void onError(Throwable t) {
       t.printStackTrace();
    }

	@OnMessage
	public void onMessage(Session session, String message) throws IOException
	{
		System.out.println(message);
		if (message.equals("is pc"))
		{
			System.out.println("aici");
			index = 0;
		}
		if (message.equals("image sent"))
		{
			System.out.println("picture is saved");
			drawImage("B:\\barcode.jpg");
		}
	}

	@OnMessage
	public void  onMessage(byte[] data, boolean last, Session session) throws IOException
	{
		for (int i=0; i<data.length; ++i)
		{
			buffer[index++] = data[i];
		}
				
	}
	
	private void drawImage(String path) throws IOException 
	{
		byte[] aux = new byte[buffer.length];
		for (int i=0; i<buffer.length; ++i)
		{
			aux[i] = buffer[i];
		}
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(aux));
		//BufferedImage scaledImg = Scalr.resize(img, Method.QUALITY, 640, 480);
		ImageIO.write(img, "jpg", new File(path));
		index = 0;
		BarcodeScanner barcodeScanner = new BarcodeScanner(path);
	}
	

}