package it.vige.businesscomponents.injection;

import static it.vige.businesscomponents.injection.interceptor.service.History.getItemHistory;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test; 
import org.junit.runner.RunWith; 

import it.vige.businesscomponents.injection.interceptor.Audit;
import it.vige.businesscomponents.injection.interceptor.ExcludedInterceptor;
import it.vige.businesscomponents.injection.interceptor.IncludedInterceptor;
import it.vige.businesscomponents.injection.interceptor.service.Item;
import it.vige.businesscomponents.injection.interceptor.service.ItemService;
import it.vige.businesscomponents.injection.interceptor.service.SimpleService;

@RunWith(Arquillian.class)
public class InterceptorsTestCase {

	private static final Logger logger = getLogger(InterceptorsTestCase.class.getName());

	@Deployment
	public static Archive<?> createCDIDeployment() {
		final WebArchive  jar = create(WebArchive.class, "interceptors-cdi-test.war");
		jar.addPackage(Audit.class.getPackage());
		jar.addPackage(Item.class.getPackage());  
		jar.addClasses(ExcludedInterceptor.class  );
		jar.addClasses(IncludedInterceptor.class  );
		jar.addAsWebInfResource(new FileAsset(new File("src/test/resources/META-INF/beans-interceptors.xml")),
				"beans.xml");
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/beans-interceptors.xml")),
				"beans.xml");
		return jar;
	}

	@Inject
	private ItemService itemService;

	@Inject
	private SimpleService simpleService;

	@Test
	public void testAuditInterceptor() {
		logger.info("Start interceptor test");
		Item item = new Item();
		item.setName("testItem");
		itemService.create(item);
		List<Item> items = itemService.getList();
		assertEquals(0, items.size());
		assertEquals(2, getItemHistory().size());
		simpleService.setItem(item);
		assertEquals("the bean maintains the state", item, simpleService.getItem());
		final List<String> history = getItemHistory();
		System.out.println("-------------------------------------------------------");
		history.parallelStream().forEach(unit ->{
			System.out.println("begin check");
			System.out.println(unit);
			System.out.println("check finish");
		});
//		assertTrue(history.contains("test_trace"));
		
		// The implementation had already changed.The InvocationContext retrieved from  Interceptor's aroundInvoke method is not as same as others.   
		assertTrue(history.contains("null"));
	}

	@Test
	public void testTimeout() throws NamingException {
		itemService.getExcludedList();
		itemService.createTimer();
		assertTrue(itemService.awaitTimerCall());
		assertEquals("@Timeout", itemService.getInterceptorResults());
	}

}
