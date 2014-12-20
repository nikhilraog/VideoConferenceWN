import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.DriverManager.*;

class Login extends JFrame implements ActionListener
{
	JTextField name;
	JPasswordField pass;
	JButton login;
	Connection conn;
	Statement stat;
	ResultSet res;
	String str;
	Login()
	{
		super("Login");
		JPanel jp=new JPanel(new GridLayout(2,2,10,20));
		JLabel l1=new JLabel("UserName");
		jp.add(l1);
		name=new JTextField(10);
		jp.add(name);
		JLabel l2=new JLabel("Password");
		jp.add(l2);
		pass=new JPasswordField(10);
		jp.add(pass);
		Container c=getContentPane();
		c.setLayout(new BorderLayout(10,10));
		c.add(new JLabel("LOGIN",JLabel.CENTER),BorderLayout.NORTH);
		c.add(jp,BorderLayout.CENTER);
		login=new JButton("LOGIN");
		login.addActionListener(this);
		c.add(login,BorderLayout.SOUTH);
		setSize(300,160);
		setLocation(100,100);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{

		if(name.getText().trim()!="" && pass.getText().trim()!="")
		{
		
		try
		{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				conn=DriverManager.getConnection("jdbc:odbc:Login");
				stat=conn.createStatement();
				str="Select * from Users where LoginID='"+name.getText();
				str=str+"' and Password='"+pass.getText()+"'";
				res=stat.executeQuery(str);
		        if(res.next())
		        {
		        	JOptionPane.showConfirmDialog(this,"Successfully Login","Information",-1,0);
		        	this.setVisible(false);
		        	dispose();
		        	System.out.println("WELCOME");
		        	Transmit s=new Transmit();
		        }
		        else
		        	JOptionPane.showConfirmDialog(this,"Invalid Password","Error",-1,0);
		        res.close();
		        stat.close();
		        conn.close();
		        }
		        catch(Exception ex)
		        {
		        	JOptionPane.showConfirmDialog(this,ex.toString(),"Error",-1,0);
		        }
		        }
		        else
		        {
		        	JOptionPane.showConfirmDialog(this,"Please fill the textbox","Information",-1,0);	
		        }
	        }
	public static void main(String arg[])
	{
		Login l=new Login();
	}
}