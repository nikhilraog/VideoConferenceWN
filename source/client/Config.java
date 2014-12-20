
import java.io.*;
import java.util.*;

public class Config {
    private String pathPrefix;

    public Vector targets;

    public Config() {
        pathPrefix= System.getProperty( "user.home") + File.separator;
	    
	read();
    }

    public void read() {
	targets= new Vector();
	
        try {
            String path= pathPrefix + "rx.dat";

	    FileInputStream fin= new FileInputStream( path);
	    BufferedInputStream bin= new BufferedInputStream( fin);
	    DataInputStream din= new DataInputStream( bin);

	    int n_targets= din.readInt();

	    for( int i= 0; i < n_targets; i++) {
		String localPort= readString( din);
		String ip= readString( din);
		String port= readString( din);
		
	        targets.addElement( new Target( localPort, ip, port));		
	    }

            fin.close();	    
	} catch( IOException e) {
	    System.out.println( "xmit.dat file missing!");
	}
     }

    public void write() {
        try {
            String path= pathPrefix + "rx.dat";

            FileOutputStream fout= new FileOutputStream( path);
	    BufferedOutputStream bout= new BufferedOutputStream( fout);
	    DataOutputStream dout= new DataOutputStream( bout);

	    dout.writeInt( targets.size());

	    for( int i= 0; i < targets.size(); i++) {
	        Target target= (Target) targets.elementAt( i);

		writeString( dout, target.localPort);
		writeString( dout, target.ip);
		writeString( dout, target.port);
	    }
	    
	    dout.flush();
            dout.close();
	    fout.close();	    			
	} catch( IOException e) {
	    System.out.println( "Error writing xmit.dat!");
	}
    }

    public String readString( DataInputStream din) {
        String s= null;

        try {
            short length= din.readShort();

            if( length > 0) {
                byte buf[]= new byte[ length];

                din.read( buf, 0, length);

                s= new String( buf);
            }
        } catch( IOException e) {
            System.err.println( e);
        }

        return s;
    }

    public void writeString( DataOutputStream dout, String str) {
	try {
	    if( str != null) {
		dout.writeShort( str.length());
		dout.writeBytes( str);
	    } else {
		dout.writeShort( 0);
	    }
	} catch( Exception e) {
	    e.printStackTrace();
	}
    }
}
  


