
import java.io.*;
import java.util.*;

public class Config {
    private String pathPrefix;

    public String local_data_port;
    public Vector targets;
    public String media_locator;

    public Config() {
        pathPrefix= System.getProperty( "user.home") + File.separator;
	    
	read();
    }

    public void read() {
	targets= new Vector();
	
        try {
            String path= pathPrefix + "xmit.dat";

	    FileInputStream fin= new FileInputStream( path);
	    BufferedInputStream bin= new BufferedInputStream( fin);
	    DataInputStream din= new DataInputStream( bin);

            local_data_port= readString( din);

	    int n_targets= din.readInt();

	    for( int i= 0; i < n_targets; i++) {
		String ip= readString( din);
		String port= readString( din);

	        targets.addElement( new Target( local_data_port, ip, port));		
	    }

	    media_locator= readString( din);

            fin.close();	    
	} catch( IOException e) {
	    System.out.println( "xmit.dat file missing!");

	    local_data_port= "";
	    media_locator= "";
	}
     }

    public void write() {
        try {
            String path= pathPrefix + "xmit.dat";

            FileOutputStream fout= new FileOutputStream( path);
	    BufferedOutputStream bout= new BufferedOutputStream( fout);
	    DataOutputStream dout= new DataOutputStream( bout);

	    writeString( dout, local_data_port);

	    dout.writeInt( targets.size());

	    for( int i= 0; i < targets.size(); i++) {
	        Target target= (Target) targets.elementAt( i);

		writeString( dout, target.ip);
		writeString( dout, target.port);
	    }

	    writeString( dout, media_locator);
	    
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

    public void addTarget( String ip, String port) {
	targets.addElement( new Target( "", ip, port));
    }
}
  


