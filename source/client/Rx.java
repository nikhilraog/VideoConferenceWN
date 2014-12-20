
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

//import javax.media.rtp.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class Rx extends JFrame implements ActionListener, KeyListener,
                                          MouseListener, WindowListener {
    Vector targets;
    JList list;
    JButton rtcp;
    JButton startRx;
    JButton expiration;
    JButton statistics;
    JButton addTarget;
    JButton removeTarget;
    JTextField tf_remote_address;
    JTextField tf_remote_data_port;
    JTextField tf_local_data_port;
    JTextField tf_media_file;
    TargetListModel listModel;
    RTCPViewer rtcpViewer;
    JCheckBox cb_loop;
    Config config;
    AVReceiver avReceiver;
    JPanel localPanel;

    public Rx() {
        setTitle( "Security Eye");
        setResizable(false);
        setLocation(200,200);

	try {

		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	} catch(Exception ee) {
		System.out.println(ee);
	}	
	
	//setIconImage((new ImageIcon(getClass().getResource("logo.jpg"))).getImage());

	config= new Config();
	

	GridBagLayout gridBagLayout= new GridBagLayout();

	GridBagConstraints gbc;

	JPanel p= new JPanel();
	p.setLayout( gridBagLayout);
	p.setBackground(new Color(255,255,255));


	localPanel= createLocalPanel();
	localPanel.setBackground(new Color(61,100,101));
 
	gbc= new GridBagConstraints();
	gbc.gridx= 0;
	gbc.gridy= 0;
	gbc.gridwidth= 2;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( localPanel, gbc);
	//p.add( localPanel);
	
	JPanel targetPanel= createTargetPanel();
	targetPanel.setBackground(Color.lightGray);
	
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
	
        JPanel buttonPanel= new JPanel();
	buttonPanel.setBackground(Color.white);
    	
        rtcp= new JButton( "RTCP Monitor");
        startRx= new JButton( "Start Receiver");

	rtcp.addActionListener( this);
	startRx.addActionListener( this);
	
	buttonPanel.add( rtcp);
	buttonPanel.add( startRx);

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
	buttonPanel.setBackground(Color.white);
	
	
	
	JPanel main=new JPanel();
	
	//GridBagLayout gridBagLayout= new GridBagLayout();

	//GridBagConstraints gbc;
	main.setLayout(gridBagLayout);
	JLabel jlb=new JLabel(new ImageIcon("12.jpg"));
	gbc= new GridBagConstraints();
	gbc.gridx= 0;
	gbc.gridy= 0;
	gbc.gridwidth= 2;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 5, 5, 5, 5);
	((GridBagLayout)p.getLayout()).setConstraints( jlb, gbc);
	main.add(jlb);
	
	
	gbc= new GridBagConstraints();
	gbc.gridx= 6;
	gbc.gridy= 0;
	gbc.gridwidth= 2;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 5, 5, 5, 5);
	((GridBagLayout)p.getLayout()).setConstraints(p, gbc);
	main.add(p);
	//jlb.setBounds(5,5,450,450);
	//p.setBounds(451,5,550,450);
        
    getContentPane().add(main);
    list.addMouseListener( this);

	addWindowListener( this);

        pack();

        setVisible( true);
    }
	
    private JPanel createTargetPanel() {
        JPanel p= new JPanel();
	p.setBackground(new Color(61,100,101));

     	GridBagLayout gridBagLayout= new GridBagLayout();

        GridBagConstraints gbc;

       	p.setLayout( gridBagLayout);

	targets= config.targets;
	
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
	p1.setBackground(Color.gray);

       	p1.setLayout( gridBagLayout);
	
	JLabel label1= new JLabel( "Source IP Address:");
	label1.setForeground(Color.red);

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,5,5);
	((GridBagLayout)p.getLayout()).setConstraints( label1, gbc);
	p.add( label1);
	
	JTextField tf_local_host= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( tf_local_host, gbc);
	p.add( tf_local_host);

	try {
            String host= InetAddress.getLocalHost().getHostAddress();	
	    tf_local_host.setText( host);
	} catch( UnknownHostException e) {
	}
	
	
	JLabel label= new JLabel( "Sender IP:");	
	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( label, gbc);
	p1.add( label);

	tf_remote_address= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 2;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( tf_remote_address, gbc);
	p1.add( tf_remote_address);

	label= new JLabel( "Sender Port:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 3;
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
	gbc.gridy = 3;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( tf_remote_data_port, gbc);
	p1.add( tf_remote_data_port);	

	label= new JLabel( "Local Port:");

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 4;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.EAST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( label, gbc);
	p1.add( label);

	tf_local_data_port= new JTextField( 15);

	gbc= new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 4;
	gbc.weightx = 0.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets( 5,5,0,5);
	((GridBagLayout)p1.getLayout()).setConstraints( tf_local_data_port, gbc);
	p1.add( tf_local_data_port);
	

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
	gbc.gridy = 5;
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

	if( targets.size() > 0) {
	    removeTarget.setEnabled( true);
	} else {
	    removeTarget.setEnabled( false);
	}
	    
	return p;
    }
    
    private JPanel createLocalPanel() {
        JPanel p= new JPanel();
	p.setBackground(new Color(61,100,101));

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
	gbc.insets = new Insets( 5,5,5,5);
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
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( tf_local_host, gbc);
	p.add( tf_local_host);

	try {
            String host= InetAddress.getLocalHost().getHostAddress();	
	    tf_local_host.setText( host);
	} catch( UnknownHostException e) {
	}
	
	//TitledBorder titledBorder= new TitledBorder( new EtchedBorder(), "Local Host");

	//p.setBorder( titledBorder);
	
	return p;
    }

    public void actionPerformed( ActionEvent event) {
        Object source= event.getSource();

	if( source == addTarget) {
	    String ip= tf_remote_address.getText().trim();
	    String port= tf_remote_data_port.getText().trim();
	    String localPort= tf_local_data_port.getText().trim();

	    if( avReceiver != null) {
		avReceiver.addTarget( ip, port, localPort);
	    }
	    
	    addTargetToList( localPort, ip, port);
	} else if( source == removeTarget) {
	    String ip= tf_remote_address.getText().trim();
	    String port= tf_remote_data_port.getText().trim();
	    
	    int index= list.getSelectedIndex();

	    if( index != -1) {
		Target target= (Target) targets.elementAt( index);

		if( avReceiver != null) {
		    avReceiver.removeTarget( ip, port);
		}

		targets.removeElement( target);
		listModel.setData( targets);

		if( targets.size() == 0) {
		    removeTarget.setEnabled( false);
		}

		if( targets.size() > 0) {
		    if( index > 0) {
			index--;
		    } else {
			index= 0;
		    }

		    list.setSelectedIndex( index);

		    setTargetFields();		    
		} else {
		    list.setSelectedIndex( -1);		    
		}		    
	    }
	} else if( source == rtcp) {
	    if( rtcpViewer == null) {
	        rtcpViewer= new RTCPViewer();
	    } else {
		rtcpViewer.setVisible( true);
		rtcpViewer.toFront();
	    }
	} else if( source == startRx) {
	    if( startRx.getLabel().equals( "Start Receiver")) {
	        avReceiver= new AVReceiver( this, targets);
		startRx.setLabel( "Stop Receiver");
	    } else {
		avReceiver.close();
		avReceiver= null;
		
		startRx.setLabel( "Start Receiver");		
	    }
	}	
    }

    synchronized public void addTargetToList( String localPort,
					      String ip, String port) {	
        ListUpdater listUpdater= new ListUpdater( localPort, ip,
						   port, listModel, targets,
						  removeTarget);
     
        SwingUtilities.invokeLater( listUpdater);  		
    }

    public void rtcpReport( String report) {
	if( rtcpViewer != null) {
	    rtcpViewer.report( report);
	}
    }
    
    public void windowClosing( WindowEvent event) {
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
	    setTargetFields();
	}
    }

    public void setTargetFields() {
	int index= list.getSelectedIndex();

	if( index != -1) {
	    Target target= (Target) targets.elementAt( index);

	    tf_remote_address.setText( target.ip);
	    tf_remote_data_port.setText( target.port);
	    tf_local_data_port.setText( target.localPort);		
	}
    }
    
    public static void main( String[] args) {
        new Rx();
    }
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

            name= o.localPort + " <--- " + o.ip + ":" + o.port;
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
    JButton removeTarget;
    
    public ListUpdater( String localPort, String ip, String port,
			TargetListModel listModel, Vector targets,
			JButton removeTarget) {
	this.localPort= localPort;
	this.ip= ip;
	this.port= port;
	this.listModel= listModel;
	this.targets= targets;
	this.removeTarget= removeTarget;
    }
	
     public void run() {
         Target target= new Target( localPort, ip, port);

	 if( !targetExists( localPort, ip, port)) {
     	     targets.addElement( target);
             listModel.setData( targets);
	     removeTarget.setEnabled( true);	     
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
