import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

import javax.media.rtp.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class RTCPViewer extends JFrame implements ActionListener, KeyListener,
                                            MouseListener, WindowListener {

    private JList list;
    private Vector reports;
    private RtcpListModel listModel;
    private JButton clear;
    private JButton start;
    private boolean recording;
    
    public RTCPViewer() {
        setTitle( "JMF/RTCP Monitor");

	recording= true;
	
	reports= new Vector();
	
        GridBagLayout gridBagLayout= new GridBagLayout();

	GridBagConstraints gbc;

	JPanel p= new JPanel();
	p.setLayout( gridBagLayout);

	JPanel localPanel= createLocalPanel();
 
	gbc= new GridBagConstraints();
	gbc.gridx= 0;
	gbc.gridy= 0;
	gbc.weightx= 1.0;
	gbc.weighty= 1.0;
	gbc.anchor= GridBagConstraints.CENTER;
	gbc.fill= GridBagConstraints.BOTH;
	gbc.insets= new Insets( 10, 5, 0, 0);
	((GridBagLayout)p.getLayout()).setConstraints( localPanel, gbc);
	p.add( localPanel);

        JPanel buttonPanel= new JPanel();
    	
        clear= new JButton( "Clear");
        start= new JButton( "Stop Recording");

	clear.addActionListener( this);
	start.addActionListener( this);
	
	buttonPanel.add( clear);
	buttonPanel.add( start);

	gbc= new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 1;
        gbc.gridwidth= 2;
	gbc.weightx = 1.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets( 5,5,10,5);
	((GridBagLayout)p.getLayout()).setConstraints( buttonPanel, gbc);
	p.add( buttonPanel);

	
        getContentPane().add( p);

	list.addMouseListener( this);

	addWindowListener( this);

        pack();

        setVisible( true);		
    }

    private JPanel createLocalPanel() {
        JPanel p= new JPanel();

     	GridBagLayout gridBagLayout= new GridBagLayout();

        GridBagConstraints gbc;

       	p.setLayout( gridBagLayout);

	listModel= new RtcpListModel( reports);
	
        list= new JList( listModel);
	
	list.addKeyListener( this);

	list.setPrototypeCellValue( "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

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
	gbc.insets= new Insets( 10, 10, 10, 10);
	((GridBagLayout)p.getLayout()).setConstraints( scrollPane, gbc);
	p.add( scrollPane);

	TitledBorder titledBorder= new TitledBorder( new EtchedBorder(), "RTCP Reports");

	p.setBorder( titledBorder);
	
        return p;
    }

    public void windowClosing( WindowEvent event) {
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
	// int index= list.locationToIndex( e.getPoint());
    }

    public void actionPerformed( ActionEvent event) {
        Object source= event.getSource();

	if( source == clear) {
	    reports= new Vector();
	    listModel.setData( reports);	    
	} else if( source == start) {
	    if( start.getLabel().equals( "Stop Recording")) {
		recording= false;
		start.setLabel( "Start Recording");
	    } else {
		recording= true;
		start.setLabel( "Stop Recording");		
	    }
	}
    }

    public void report( String text) {
	if( recording) {
	    reports.addElement( text);
	    
	    listModel.setData( reports);

	    list.ensureIndexIsVisible( reports.size() - 1);
	}
    }
}

class RtcpListModel extends AbstractListModel {
    private Vector options;

    public RtcpListModel( Vector options) {
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
	    name= (String)options.elementAt( index);
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

