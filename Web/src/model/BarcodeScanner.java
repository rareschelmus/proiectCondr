package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class BarcodeScanner {
	private String path;
	private String result; 
	public BarcodeScanner(String path2)  {
		this.path = path2;
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		result = readQRCode(this.path,charset,hintMap);
		System.out.println(result);
	}

public  String readQRCode(String filePath, String charset, Map hintMap)
   {
	BinaryBitmap binaryBitmap = null;
	try {
		binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						ImageIO.read(new FileInputStream(filePath)))));
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("File not found");
	}
	Result qrCodeResult = null;
	try {
		qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				hintMap);
	} catch (NotFoundException e) {
		// TODO Auto-generated catch block
		System.out.println("Barcode not found");
	}
	return qrCodeResult.getText();
}

public static Map<EncodeHintType, ErrorCorrectionLevel> getMap()
{
	Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
	hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	return hintMap;
}

public void setPath(String path)
{
	this.path = path;
}
public String getResult()
{
	return this.result;
}

}