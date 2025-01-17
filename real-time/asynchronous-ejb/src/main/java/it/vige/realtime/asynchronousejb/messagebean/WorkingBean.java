package it.vige.realtime.asynchronousejb.messagebean;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.DeliveryActive;

/**
 * Message-Driven Bean implementation class for: WorkingBean <br/>
 * propertyValue = "java:jms/queue/ExpiryQueue" <br/>
 * propertyValue = "java:jboss/deliveryactive/MDBWithAnnotationQueue" <br/>
 */
@MessageDriven(name  = "WorkingBean", activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jms/queue/ExpiryQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
//Do not deliver messages to this MDB until start-delivery management operation is explicitly called on it.
@DeliveryActive(true)
public class WorkingBean implements MessageListener {

	private static final Logger logger = getLogger(WorkingBean.class.getName());

	@Resource
	private MessageDrivenContext mdc;

	public static boolean taken;

	@Override
	public void onMessage(Message message) {
		TextMessage msg = null;
		try { 
			
			if (message instanceof TextMessage) {
				msg = (TextMessage) message;
				logger.info("MESSAGE BEAN: Message received: " + msg.getText());
				taken = true;
			} else {
				logger.warning("Message of wrong type: " + message.getClass().getName());
			}
		} catch (JMSException e) {
			logger.log(SEVERE, "jms error", e);
			mdc.setRollbackOnly();
		} catch (Throwable te) {
			logger.log(SEVERE, "generic error", te);
		}
	}
}
