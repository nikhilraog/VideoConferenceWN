import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.media.rtp.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class Transmit extends JFrame implements ActionListener, KeyListener,
                                          MouseListener, WindowListener {
    Vector targets;
    JList list;
    JButton startXmit;
    JButton rtcp;
    JButton update;
    JButton expiration;
    JButton statistics;
    JButton addTarget;
    JButton removeTarget;
    JTextField tf_remote_address;
    JTextField tf_remote_data_port;
    JTextField tf_media_file;
    JTextField tf_data_port;
    TargetListModel listModel;
    TransmitterMain avTransmitter;
    Viewer rtcpViewer;
    JCheckBox cb_loop;
    Config config;
    
    public static void main(String args[])
    {
    	new Transmit();
    }

    public Transmit() {
        setTitle( "Live Security Transmitter");
		//setResizable(false);
        setLocation(400,400);
	config= new Config();
	
	try {

		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	} catch(Exception ee) {
		System.out.println(ee);
	} 	
	
	//setIconImage((new ImageIcon(getClass().getResource("logo.jpg"))).getImage());

	
	GridBagLayout gridBagLayout= new GridBagLayout();

	GridBagConstraints gbc;

	JPanel p= new JPanel();
	p.setLayout( gridBagLayout);
	p.setBackground(Color.lightGray);

	JPanel localPanel= createLocalPanel();
	localPanel.setBackground(Color.gray);

 
	gbc= new GridBagConstraints();
	gbc.gridx= 0;
	gbc.gridy= 0;
	gbc.gridwidth= 2;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( localPanel, gbc);
	p.add( localPanel);
	
	JPanel targetPanel= createTargetPanel();
	targetPanel.setBackground(Color.gray);

	
	gbc= new GridBagConstraints();
	gbc.gridx= 1;
	gbc.gridy= 1;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( targetPanel, gbc);
        p.add( targetPanel);

	JPanel mediaPanel= createMediaPanel();
	mediaPanel.setBackground(Color.gray);

	gbc= new GridBagConstraints();
	gbc.gridx= 1;
	gbc.gridy= 2;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( mediaPanel, gbc);
        p.add( mediaPanel); 
	
        JPanel buttonPanel= new JPanel();
	buttonPanel.setBackground(Color.lightGray);

        rtcp= new JButton( "RTCP Monitor");
        update= new JButton( "Transmission Status");

	update.setEnabled( false);

	rtcp.addActionListener( this);
	update.addActionListener( this);
	
	buttonPanel.add( rtcp);
	//buttonPanel.add( update);


	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 3;
        gbc.gridwidth= 2;
	gbc.weightx = 1.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( buttonPanel, gbc);
	p.add( buttonPanel);
	
	
	JLabel jlb=new JLabel(new ImageIcon("12.jpg"));
     JPanel temp=new JPanel();
     temp.setLayout(gridBagLayout);
     gbc= new GridBagConstraints();
	gbc.gridx= 1;
	gbc.gridy= 1;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 10, 10, 10);
    ((GridBagLayout)temp.getLayout()).setConstraints( p, gbc);
	temp.add( p);
	
	
	 gbc= new GridBagConstraints();
	gbc.gridx= 6;
	gbc.gridy= 1;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 10, 10, 10);
	((GridBagLayout)temp.getLayout()).setConstraints( jlb, gbc);
	temp.add( jlb);
    temp.setBackground(Color.lightGray);
     
        getContentPane().add( temp);

	list.addMouseListener( this);

	addWindowListener( this);

        pack();

        setVisible( true);
    }

    private JPanel createMediaPanel() {
        JPanel p= new JPanel();

     	GridBagLayout gridBagLayout= new GridBagLayout();

        GridBagConstraints gbc;

       	p.setLayout( gridBagLayout);

	JLabel label= new JLabel( "Media Locator:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( label, gbc);
	p.add( label);

	tf_media_file= new JTextField( 35);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.weightx = 1.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( tf_media_file, gbc);
	p.add( tf_media_file);

	tf_media_file.setText( config.media_locator);

	cb_loop= new JCheckBox( "loop");

	startXmit= new JButton( "Start Transmission");
	startXmit.setEnabled( true);
	startXmit.addActionListener( this);

	gbc= new GridBagConstraints();
	gbc.gridx = 2;
	gbc.gridy = 0;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( cb_loop, gbc);
	p.add( cb_loop);

	cb_loop.setSelected( true);
	cb_loop.addActionListener( this);
	
	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( startXmit, gbc);
	p.add( startXmit);
	
	TitledBorder titledBorder= new TitledBorder( new EtchedBorder(), "Source");

	p.setBorder( titledBorder);
	
	return p;
    }	
	
    private JPanel createTargetPanel() {
        JPanel p= new JPanel();
	
     	GridBagLayout gridBagLayout= new GridBagLayout();

        GridBagConstraints gbc;

       	p.setLayout( gridBagLayout);

	targets= new Vector();

	for( int i= 0; i < config.targets.size(); i++) {
	    targets.addElement( config.targets.elementAt( i));
	}
	
        listModel= new TargetListModel( targets);

        list= new JList( listModel);
	
	list.addKeyListener( this);

	list.setPrototypeCellValue( "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        JScrollPane scrollPane= new JScrollPane( list,
                                                 ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	gbc= new GridBagConstraints();
	gbc.gridx= 0;
	gbc.gridy= 0;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( scrollPane, gbc);
	p.add( scrollPane);


        JPanel p1= new JPanel();

       	p1.setLayout( gridBagLayout);
	
	JLabel label= new JLabel( "IP Address:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( label, gbc);
	p1.add( label);
	p1.setBackground(Color.gray);

	tf_remote_address= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( tf_remote_address, gbc);
	p1.add( tf_remote_address);

	label= new JLabel( "Data Port:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( label, gbc);
	p1.add( label);

	tf_remote_data_port= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( tf_remote_data_port, gbc);
	p1.add( tf_remote_data_port);	


        JPanel p2= new JPanel();
	p2.setBackground(Color.gray);
        addTarget= new JButton( "Add Target");	
        removeTarget= new JButton( "Remove Target");

	p2.add( addTarget);
	p2.add( removeTarget);

	addTarget.addActionListener( this);
	removeTarget.addActionListener( this);
	
	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.weightx = 1.0;
	gbc.weighty = 0.0;
	gbc.gridwidth= 2;
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets( 20,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( p2, gbc);
	p1.add( p2);
	
	gbc= new GridBagConstraints();
	gbc.gridx= 1;
	gbc.gridy= 0;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( p1, gbc);
	p.add( p1);

	TitledBorder titledBorder= new TitledBorder( new EtchedBorder(), "Targets");

	p.setBorder( titledBorder);
	
	return p;
    }
    
    private JPanel createLocalPanel() {
        JPanel p= new JPanel();

     	GridBagLayout gridBagLayout= new GridBagLayout();

        GridBagConstraints gbc;

       	p.setLayout( gridBagLayout);

	JLabel label= new JLabel( "IP Address:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p.getLayout()).setConstraints( label, gbc);
	p.add( label);

	
	JTextField tf_local_host= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p.getLayout()).setConstraints( tf_local_host, gbc);
	p.add( tf_local_host);

	try {
            String host= InetAddress.getLocalHost().getHostAddress();	
	    tf_local_host.setText( host);
	} catch( UnknownHostException e) {
	}
	
	label= new JLabel( "Data Port:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p.getLayout()).setConstraints( label, gbc);
	p.add( label);

	tf_data_port= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( tf_data_port, gbc);
	p.add( tf_data_port);

	tf_data_port.setText( config.local_data_port);
	
	TitledBorder titledBorder= new TitledBorder( new EtchedBorder(), "Local Host");

	p.setBorder( titledBorder);
	
	return p;
    }

    public void actionPerformed( ActionEvent event) {
        Object source= event.getSource();

	if( source == addTarget) {
	    String ip= tf_remote_address.getText().trim();
	    String port= tf_remote_data_port.getText().trim();
	    String localPort= tf_data_port.getText().trim();

	    addTargetToList( localPort, ip, port);
	    
	    if( avTransmitter != null) {
	        avTransmitter.addTarget( ip, port);
	    }
	} else if( source == removeTarget) {
	    int index= list.getSelectedIndex();

	    if( index != -1) {
		Target target= (Target) targets.elementAt( index);

		if( avTransmitter != null) {
	            avTransmitter.removeTarget( target.ip, target.port);
		}
		
		targets.removeElement( target);
		listModel.setData( targets);		
	    }
	} else if( source == startXmit) {
	    if( startXmit.getLabel().equals( "Start Transmission")) {		
	        int data_port= new Integer( tf_data_port.getText()).intValue();
		    
		avTransmitter= new TransmitterMain( this, data_port);
		
	        avTransmitter.start( tf_media_file.getText().trim(), targets);		
		
		avTransmitter.setLooping( cb_loop.isSelected());
		
	        startXmit.setLabel( "Stop Transmission");
	    } else if( startXmit.getLabel().equals( "Stop Transmission")) {
		avTransmitter.stop();
		avTransmitter= null;

		removeNonBaseTargets();
		listModel.setData( targets);
		
	        startXmit.setLabel( "Start Transmission");		
	    }
	} else if( source == rtcp) {
	    if( rtcpViewer == null) {
	        rtcpViewer= new Viewer();
	    } else {
		rtcpViewer.setVisible( true);
		rtcpViewer.toFront();
	    }
	} else if( source == cb_loop) {
	    if( avTransmitter != null) {
		avTransmitter.setLooping( cb_loop.isSelected());
	    }
	}
    }

    private void removeNonBaseTargets() {
	String localPort= tf_data_port.getText().trim();

	for( int i= targets.size(); i > 0;) {
	    Target target= (Target) targets.elementAt( i - 1);
	    
	    if( !target.localPort.equals( localPort)) {
                targets.removeElement( target);
	    }

	    i--;
	}
    }
    
    public void addTargetToList( String localPort,
					      String ip, String port) {	
        ListUpdater listUpdater= new ListUpdater( localPort, ip,
						  port, listModel, targets);
     
         SwingUtilities.invokeLater( listUpdater);  		
    }

    public void rtcpReport( String report) {
	if( rtcpViewer != null) {
	    rtcpViewer.report( report);
	}
    }
    
    public void windowClosing( WindowEvent event) {
	config.local_data_port= tf_data_port.getText().trim();

	config.targets= new Vector();
	
	for( int i= 0; i < targets.size(); i++) {
	    Target target= (Target) targets.elementAt( i);

	    if( target.localPort.equals( config.local_data_port)) {
		config.addTarget( target.ip, target.port);
	    }
	}

	config.media_locator= tf_media_file.getText().trim();
	
	config.write();
	
        System.exit( 0);
    }

    public void windowClosed( WindowEvent event) {
    }

    public void windowDeiconified( WindowEvent event) {
    }

    public void windowIconified( WindowEvent event) {
    }

    public void windowActivated( WindowEvent event) {
    }

    public void windowDeactivated( WindowEvent event) {
    }

    public void windowOpened( WindowEvent event) {
    }

    public void keyPressed( KeyEvent event) {
    }
    
    public void keyReleased( KeyEvent event) {
        Object source= event.getSource();

	if( source == list) {
	    int index= list.getSelectedIndex();
	}
    }

    public void keyTyped( KeyEvent event) {
    }
	
    public void mousePressed( MouseEvent e) {
    }

    public void mouseReleased( MouseEvent e) {
    }

    public void mouseEntered( MouseEvent e) {
    }

    public void mouseExited( MouseEvent e) {
    }

    public void mouseClicked( MouseEvent e) {
        Object source= e.getSource();

	if( source == list) {
	    int index= list.getSelectedIndex();

	    if( index != -1) {
		Target target= (Target) targets.elementAt( index);

		tf_remote_address.setText( target.ip);
		tf_remote_data_port.setText( target.port);
	    }
	}
	
	int index= list.locationToIndex( e.getPoint());
    }

//    public static void main( String[] args) {
//        new Transmitter();
//    }
}

class TargetListModel extends AbstractListModel {
    private Vector options;

    public TargetListModel( Vector options) {
	this.options= options;
    }

    public int getSize() {
	int size;

	if( options == null) {
	    size= 0;
	} else {
	    size= options.size();
	}

	return size;
    }

    public Object getElementAt( int index) {
        String name;

        if( index < getSize()) {
	    Target o= (Target)options.elementAt( index);

            name= o.localPort + " ---> " + o.ip + ":" + o.port;
	} else {
	    name= null;
	}

	return name;
    }

    public void setData( Vector data) {
	options= data;

	fireContentsChanged( this, 0, data.size());
    }
}


class ListUpdater implements Runnable {
    String localPort, ip, port;
    TargetListModel listModel;
    Vector targets;
    
    public ListUpdater( String localPort, String ip, String port,
			TargetListModel listModel, Vector targets) {
	this.localPort= localPort;
	this.ip= ip;
	this.port= port;
	this.listModel= listModel;
	this.targets= targets;
    }
	
     public void run() {
         Target target= new Target( localPort, ip, port);

	 if( !targetExists( localPort, ip, port)) {
     	     targets.addElement( target);
             listModel.setData( targets);
	 }
    }

    public boolean targetExists( String localPort, String ip, String port) {
	boolean exists= false;
	
	for( int i= 0; i < targets.size(); i++) {
	    Target target= (Target) targets.elementAt( i);

	    if( target.localPort.equals( localPort)
	     && target.ip.equals( ip)
		&& target.port.equals( port)) {		
		exists= true;
	        break;
	    }
	}

	return exists;
    }
}
