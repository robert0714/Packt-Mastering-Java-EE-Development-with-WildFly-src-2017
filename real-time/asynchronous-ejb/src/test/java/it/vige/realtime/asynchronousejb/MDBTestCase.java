package it.vige.realtime.asynchronousejb;

import it.vige.realtime.asynchronousejb.messagebean.OldSpecsWorkingBean; 
import static java.util.logging.Logger.getLogger;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.jboss.as.test.shared.integration.ejb.security.PermissionUtils.createPermissionsXmlAsset;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.jboss.as.controller.client.helpers.ClientConstants.NAME;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP_ADDR;
import static org.jboss.as.controller.client.helpers.ClientConstants.OUTCOME;
import static org.jboss.as.controller.client.helpers.ClientConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.client.helpers.ClientConstants.RESULT;
import static org.jboss.as.controller.client.helpers.ClientConstants.SUCCESS;
import static org.jboss.as.test.shared.integration.ejb.security.PermissionUtils.createPermissionsXmlAsset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.util.PropertyPermission;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.common.jms.JMSOperations; 
import org.jboss.as.test.integration.common.jms.JMSOperationsProvider;
import org.jboss.as.test.shared.TimeoutUtil;
import org.jboss.remoting3.security.RemotingPermission;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.dmr.ModelNode;
import it.vige.realtime.asynchronousejb.messagebean.WorkingBean;
import it.vige.realtime.asynchronousejb.timer.OldSpecsBean;


import java.io.FilePermission;
import java.io.IOException;
import java.util.PropertyPermission;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.common.jms.JMSOperations;
import org.jboss.as.test.integration.common.jms.JMSOperationsProvider;
import org.jboss.as.test.shared.TimeoutUtil;
import org.jboss.dmr.ModelNode;
import org.jboss.remoting3.security.RemotingPermission;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Ignore;

 
//FIXME
@Ignore
//refer: https://github.com/wildfly/wildfly/blob/main/testsuite/integration/basic/src/test/java/org/jboss/as/test/integration/ejb/mdb/deliveryactive/MDBTestCase.java
@RunWith(Arquillian.class)
//@ServerSetup({MDBTestCase.JmsQueueSetup.class})
public class MDBTestCase {

	private static final Logger logger = getLogger(MDBTestCase.class.getName());

	@Deployment
	public static Archive<?> createEJBDeployment() {
		final EnterpriseArchive ear = create(EnterpriseArchive.class, "mdb-ejb-test.ear");
		final JavaArchive ejbJar = create(JavaArchive.class, "mdb-ejb-test.jar")  
//				.addPackage(WorkingBean.class.getPackage())
				.addClass(WorkingBean.class )
				.addPackage(JMSOperations.class.getPackage())
//				.addPackage(OldSpecsBean.class.getPackage())
				.addClass(TimeoutUtil.class)
				.addClass(MDBTestCase.class)
				.addClass(ManagementClient.class)
				.deleteClass(AsyncBeanTestCase.class)
				.deleteClass(BusyOperatorTestCase.class)
				.deleteClass(SchedulerTestCase.class)
				.deleteClass(TimerTestCase.class)
				// .addAsManifestResource(WorkingBean.class.getPackage(), "jboss-ejb3.xml", "jboss-ejb3.xml")
				// .addAsManifestResource(MDBTestCase.class.getPackage(), "jboss-ejb3.xml", "jboss-ejb3.xml")
                // .addAsManifestResource(new FileAsset(new File("src/main/resources/META-INF/jboss-ejb3.xml")), "jboss-ejb3.xml")
//				.addAsResource (new FileAsset(new File("src/main/resources/META-INF/jboss-ejb3.xml")), "META-INF/jboss-ejb3.xml") 
				.addAsManifestResource(
						new StringAsset(
								"Dependencies: org.jboss.as.controller-client, org.jboss.dmr, org.jboss.remoting\n"),
						"MANIFEST.MF");
		
		// grant necessary permissions
        ejbJar.addAsManifestResource(createPermissionsXmlAsset(
                new PropertyPermission("ts.timeout.factor", "read"),
                new RemotingPermission("createEndpoint"),
                new RemotingPermission("connect"),
                new FilePermission(System.getProperty("jboss.inst") + "/standalone/tmp/auth/*", "read")
        ), "permissions.xml");
        ear.addAsModule(ejbJar); 
		return ear;
	}

//	private final static String QUEUE_WORKING_LOOKUP = "java:jboss/deliveryactive/MDBWithAnnotationQueue";
//	private final static String QUEUE_OLDSPECS_LOOKUP = "java:jboss/deliveryactive/MDBWithDeploymentDescriptorQueue";
	private final static String QUEUE_WORKING_LOOKUP = "java:/jms/queue/ExpiryQueue";
	private final static String QUEUE_OLDSPECS_LOOKUP = "java:/jms/queue/DLQ";

//	@ArquillianResource
//    private ManagementClient managementClient;

    private static final int TIMEOUT = TimeoutUtil.adjust(5000);
//
//    static class JmsQueueSetup implements ServerSetupTask {
//
//        private JMSOperations jmsAdminOperations;
//
//        @Override
//        public void setup(ManagementClient managementClient, String containerId) throws Exception {
//            jmsAdminOperations = JMSOperationsProvider.getInstance(managementClient);
//            jmsAdminOperations.createJmsQueue("deliveryactive/MDBWithAnnotationQueue", "java:jboss/deliveryactive/MDBWithAnnotationQueue");
//            jmsAdminOperations.createJmsQueue("deliveryactive/MDBWithDeploymentDescriptorQueue", "java:jboss/deliveryactive/MDBWithDeploymentDescriptorQueue");
//        }
//
//        @Override
//        public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
//            if (jmsAdminOperations != null) {
//                jmsAdminOperations.removeJmsQueue("deliveryactive/MDBWithAnnotationQueue");
//                jmsAdminOperations.removeJmsQueue("deliveryactive/MDBWithDeploymentDescriptorQueue");
//                jmsAdminOperations.close();
//            }
//        }
//    }
	@EJB
	private WorkingBean workingBean;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory cf;

	@Resource(mappedName = QUEUE_WORKING_LOOKUP)
	private Queue queueWorking;

	@Resource(mappedName = QUEUE_OLDSPECS_LOOKUP)
	private Queue queueOldSpecs;

	@Test
	public void testWorkingBean() throws Exception {
		logger.info("starting message driven bean test");
//		 doDeliveryActive(queueWorking, QUEUE_WORKING_LOOKUP);
		
		JMSContext context = cf.createContext(AUTO_ACKNOWLEDGE);
		context.createProducer().send(queueWorking, "need a pause");
		Message message = context.createConsumer(queueWorking).receive(TIMEOUT);
		assertNotNull("the message is received by the mdb: ", message);
		System.out.println(WorkingBean.taken);
		assertTrue("the mdb was executed", workingBean.taken);
		context.close();
	}

	@Test
	public void testOldSpecs() throws Exception {
		logger.info("starting old specs test");
		JMSContext context = cf.createContext(AUTO_ACKNOWLEDGE);
		context.createProducer().send(queueOldSpecs, "need a pause");
		Message message = context.createConsumer(queueOldSpecs).receive(TIMEOUT);
		assertNotNull("the message is received by the mdb: ", message);
		assertTrue("created oldSpecsWorkingBean", OldSpecsWorkingBean.received);
		context.close();
	}
//    private void doDeliveryActive(Destination destination, String mdbName) throws Exception {
//        // ReplyingMDB has been deployed with deliveryActive set to false
//        assertMDBDeliveryIsActive(mdbName, false);
//
//        Connection connection = null;
//
//        try {
//            connection = cf.createConnection();
//            connection.start();
//
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            TemporaryQueue replyQueue = session.createTemporaryQueue();
//
//            // send a message to the MDB
//            MessageProducer producer = session.createProducer(destination);
//            Message message = session.createMessage();
//            message.setJMSReplyTo(replyQueue);
//            producer.send(message);
//
//            // the MDB did not reply to the message because its delivery is not active
//            MessageConsumer consumer = session.createConsumer(replyQueue);
//            Message reply = consumer.receive(TIMEOUT);
//            assertNull(reply);
//
//            executeMDBOperation(mdbName, "start-delivery");
//            assertMDBDeliveryIsActive(mdbName, true);
//            // WFLY-4470 check duplicate message when start delivery twice. Last assertNull(reply) should still be valid
//            executeMDBOperation(mdbName, "start-delivery");
//
//            // the message was delivered to the MDB which replied
//            reply = consumer.receive(TIMEOUT);
//            assertNotNull(reply);
//            assertEquals(message.getJMSMessageID(), reply.getJMSCorrelationID());
//
//            executeMDBOperation(mdbName, "stop-delivery");
//            assertMDBDeliveryIsActive(mdbName, false);
//
//            // send again a message to the MDB
//            message = session.createMessage();
//            message.setJMSReplyTo(replyQueue);
//            producer.send(message);
//
//            // the MDB did not reply to the message because its delivery is not active
//            reply = consumer.receive(TIMEOUT);
//            assertNull(reply);
//        } finally {
//            if (connection != null) {
//                connection.close();
//            }
//        }
//    }
//    private void executeMDBOperation(String mdbName, String opName) throws IOException {
//        ModelNode operation = createMDBOperation(mdbName);
//        operation.get(OP).set(opName);
//        ModelNode result = managementClient.getControllerClient().execute(operation);
//
//        assertTrue(result.toJSONString(true), result.hasDefined(OUTCOME));
//        assertEquals(result.toJSONString(true), SUCCESS, result.get(OUTCOME).asString());
//    }
//    private void assertMDBDeliveryIsActive(String mdbName, boolean expected) throws IOException {
//        ModelNode operation = createMDBOperation(mdbName);
//        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
//        operation.get(NAME).set("delivery-active");
//        ModelNode result = managementClient.getControllerClient().execute(operation);
//
//        assertTrue(result.toJSONString(true), result.hasDefined(OUTCOME));
//        assertEquals(result.toJSONString(true), expected, result.get(RESULT).asBoolean());
//    }

    private ModelNode createMDBOperation(String mdbName) {
        ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).add("deployment", "mdb.jar");
        operation.get(OP_ADDR).add("subsystem", "ejb3");
        operation.get(OP_ADDR).add("message-driven-bean", mdbName);
        return operation;
    }
}
