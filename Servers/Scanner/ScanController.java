public class ScanController {
	private String path;
	public ScanController(String path)
	{
		this.path = path;
	}
	
	public ScanController()
	{
		
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	void scan()
	{
		QRScanner qrScanner = new QRScanner(path);
		System.out.println(qrScanner.getResult());
	}
}
