package it.vige.realtime.asynchronousejb.messagebean;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.jboss.ejb3.annotation.DeliveryActive;

@MessageDriven
// (name  = "OldSpecsWorkingBean", activationConfig = {
// 		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/deliveryactive/MDBWithDeploymentDescriptorQueue"),
// 		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
// 		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@DeliveryActive(false)
public class OldSpecsWorkingBean implements MessageListener, MessageDrivenBean {

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
	}

}
