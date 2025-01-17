package it.vige.businesscomponents.transactions;

import static java.util.logging.Logger.getLogger;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;
import static javax.transaction.Status.STATUS_NO_TRANSACTION;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.wildfly.transaction.client.RemoteTransactionContext.getInstance;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.transactions.remote.XMLRemote;

/**
 * You can compare it.vige.businesscomponents.businesslogic.RemotingNamingTestCase
 * 
 * **/
@RunWith(Arquillian.class)
public class XMLTestCase {

	private static final Logger logger = getLogger(XMLTestCase.class.getName());

	private final static String REMOTING_PROTOCOL = "http-remoting";
	private final static int REMOTING_PORT = 8080;
	private Context context = null;

	@Deployment
	public static EnterpriseArchive createEJBDeployment() {
		final EnterpriseArchive ear = create(EnterpriseArchive.class, "xml-test.ear");
		final JavaArchive jar = create(JavaArchive.class, "xml-test.jar");
		jar.addPackage(XMLRemote.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/ejb-jar.xml")), "ejb-jar.xml");
		ear.addAsModule(jar);
		
		//Because current Test will automately add other classes in the same package. 
//		jar.deleteClass(DistributedTestCase.class);
		
		return ear;
	}

	@Test
	@RunAsClient
	public void testRemoteXML() throws Exception {
		logger.info("starting remoting ejb client test");

		try {
			createInitialContext();
			final UserTransaction userTransaction = getInstance().getUserTransaction();
			XMLRemote bean = lookupV1(XMLRemote.class, "bank");
			assertEquals(STATUS_NO_TRANSACTION, bean.transactionStatus());

			try {
				userTransaction.begin();
				bean.transactionStatus();
				fail("the transaction is not supported");
			} catch (EJBException | IllegalStateException e) {
				logger.info("the transaction is not supported");
			}
		} finally {
			closeContext();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T lookupV1(Class<T> bank, String beanName) throws NamingException {

		final String appName = "xml-test";
		final String moduleName = "remoting";
		final String distinctName = "";
		final String viewClassName = bank.getName();
		final String lookUpName = "" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName;
		System.out.println("lookUpName: " + lookUpName);
		return (T) context
				.lookup(lookUpName);
	}
	@SuppressWarnings("unchecked")
	private <T> T lookupV2(Class<T> bank, String beanName) throws NamingException {

		final String appName = "xml-test";
		final String moduleName = "remoting";
		final String distinctName = "";
		final String viewClassName = bank.getName();
       
		final String lookUpName = "ejb:" + appName + "/" + moduleName + "/" + (  (distinctName.length()>0)? (distinctName+"/"):"" ) +    beanName + "!" + viewClassName;
		System.out.println("lookUpName: " + lookUpName);
		return (T) context.lookup(lookUpName);
	}

	private void createInitialContext() throws NamingException {
		Properties jndiProperties = new Properties();

		jndiProperties.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(PROVIDER_URL, REMOTING_PROTOCOL + "://127.0.0.1:" + REMOTING_PORT);
//		jndiProperties.put(SECURITY_PRINCIPAL, "admin");
//		jndiProperties.put(SECURITY_CREDENTIALS, "secret123!");
		jndiProperties.put("jboss.naming.client.ejb.context", true);

		context = new InitialContext(jndiProperties);
	}

	private void closeContext() throws NamingException {
		if (context != null) {
			context.close();
		}
	}
}
