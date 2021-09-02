import static java.lang.System.arraycopy;

import org.mule.extension.socket.api.socket.tcp.TcpProtocol;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ProtocoloCustomizado implements TcpProtocol {

	private static int FIXED_SIZE = 1920;
	
  public ProtocoloCustomizado() {
  }

  @Override
  public InputStream read(InputStream socketIs) throws IOException {
	  System.out.println("read-start");

	  DataInputStream dis = new DataInputStream(socketIs);
	  
	  int length = dis.readShort();
	  
	  System.out.println(String.format("read-length = %d", length));

	  
	  if(length <=0 || length > 30000) {
		  throw new IOException("length invalid: " + String.valueOf(length) );
	  }
		  
	  byte[] buffer = new byte[length];

	  dis.read(buffer);	  

	  System.out.println("read-end");

	  return new ByteArrayInputStream(buffer);
  }

  @Override
  public void write(OutputStream os, InputStream data) throws IOException {
	  System.out.println("write-start");
	  
	  byte[] buffer = new byte[FIXED_SIZE];
	  
	  int bytesRead = data.read(buffer);
	  
	  int length = FIXED_SIZE; 

	  System.out.println("write-start2-read: " + String.valueOf(bytesRead));
	  System.out.println("write-start2-total: " + String.valueOf(length));
	  	  
	  if( length > 0) {			  
		  DataOutputStream dataOutputStream = new DataOutputStream(os);
		  dataOutputStream.writeShort(length);
		  dataOutputStream.write(buffer, 0, length);
		  dataOutputStream.flush();
	  }
	  
	  System.out.println("write-end");
  
  }
}
