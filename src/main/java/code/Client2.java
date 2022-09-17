package code;

import java.awt.Dimension;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

public class Client2 extends JFrame {

	private JPanel pn1;

	private JTextField txtMss;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Client2() {
		this.setTitle("Client 2");
		this.setSize(400, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		buildUI();
	}
	
	private void buildUI() {
		
		pn1 = new JPanel();
		pn1.setPreferredSize(new Dimension(0,600));
		pn1.setLayout(null);
		txtMss = new JTextField();
		txtMss.setBounds(50, 20, 270, 30);
		txtMss.setEditable(false);
		
		try {
			getmss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pn1.add(txtMss);
		this.add(pn1);
		
	}
	
	private void getmss() throws Exception {
		
				BasicConfigurator.configure();
	
				Properties settings = new Properties();
				settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
				settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
	
				Context ctx = new InitialContext(settings);

				Object obj = ctx.lookup("TopicConnectionFactory");
				ConnectionFactory factory = (ConnectionFactory) obj;

				Connection con = factory.createConnection("admin", "admin");

				con.start();

				Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);

				Destination destination = (Destination) ctx.lookup("dynamicTopics/thanthidet");
				MessageConsumer receiver = session.createConsumer(destination);

				receiver.setMessageListener(new MessageListener() {
//					@Override

					public void onMessage(Message msg) {
						try {
							if (msg instanceof TextMessage) {
								TextMessage tm = (TextMessage) msg;
								String txt = tm.getText();
								txtMss.setText(txt);
								msg.acknowledge();// gá»­i tĂ­n hiá»‡u ack
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	public static void main(String[] args) {
		new Client2().setVisible(true);
	}

}
