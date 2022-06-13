package it.vige.realtime.asynchronousejb.messagebean;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.DeliveryActive;

@MessageDriven
 (name  = "OldSpecsWorkingBean", activationConfig = {
 		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jms/queue/DLQ"),
 		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
 		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") }) 
@DeliveryActive(true)
public class OldSpecsWorkingBean implements MessageListener , MessageDrivenBean {

	private static final long serialVersionUID = -2127857668359011883L;
	private static final Logger logger = getLogger(OldSpecsWorkingBean.class.getName());

	private MessageDrivenContext ctx;
	public static boolean received;

	@Override
	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
		this.ctx = ctx;
		received = true;
	}

	@Override
	public void ejbRemove() throws EJBException {
		logger.info("contextData: " + ctx.getContextData());
		received = false;
	}

	@Override
	public void onMessage(Message message) {
		TextMessage msg = null;
		try { 
			
			if (message instanceof TextMessage) {
				msg = (TextMessage) message;
				logger.info("MESSAGE BEAN: Message received: " + msg.getText());
				received = true;
			} else {
				logger.warning("Message of wrong type: " + message.getClass().getName());
			}
		} catch (JMSException e) {
			logger.log(SEVERE, "jms error", e);
			ctx.setRollbackOnly();
		} catch (Throwable te) {
			logger.log(SEVERE, "generic error", te);
		}
	}

}
