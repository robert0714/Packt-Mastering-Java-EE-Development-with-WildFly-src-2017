package it.vige.realtime.asynchronousejb;

import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull; 
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.awaitility.Awaitility.await;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.awaitility.Duration;
import it.vige.realtime.asynchronousejb.scheduler.SchedulerBean;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
@RunWith(Arquillian.class)
public class SchedulerTestCase {

	private static final Logger logger = getLogger(SchedulerTestCase.class.getName());

	@Deployment
	public static Archive<?>  createEJBDeployment() {
		final EnterpriseArchive ear = create(EnterpriseArchive.class, "scheduler-ejb-test.ear");
		final JavaArchive ejbJar = create(JavaArchive.class, "scheduler-ejb-test.jar");
		ejbJar.addPackage(SchedulerBean.class.getPackage())
		      .addAsManifestResource(
				new StringAsset(
						"Dependencies: org.jboss.as.controller-client, org.jboss.dmr, org.jboss.remoting\n"),
				"MANIFEST.MF");
		ejbJar.addPackage(org.awaitility.Awaitility.class.getPackage());
		ejbJar.addPackage(org.awaitility.pollinterval.PollInterval.class.getPackage());
		ejbJar.addPackage(org.awaitility.constraint.WaitConstraint.class.getPackage());
		ejbJar.addPackage(org.awaitility.core.Predicate.class.getPackage());
		ejbJar.addPackage(org.awaitility.classpath.ClassPathResolver.class.getPackage());
        ear.addAsModule(ejbJar); 
        
		return ejbJar;
	}

	@Inject
	private SchedulerBean schedulerBean;

	 

	@Test
	public void testIgnoreResult() {
		logger.info("start test scheduler");
		Date today = new Date();
		schedulerBean.setTimer(10);
		try {
			sleep(12);
		} catch (InterruptedException e) {
			logger.log(SEVERE, "Interruption", e);
		}
		 
		await()
		    .atLeast(Duration.ONE_HUNDRED_MILLISECONDS)
		    .atMost(Duration.TEN_SECONDS)
		  .with()
		    .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
		    .until(schedulerBean::getLastProgrammaticTimeout, notNullValue() );
		Date programmaticDate = schedulerBean.getLastProgrammaticTimeout();
		
		System.out.println(programmaticDate);
		assertNotNull("the programmatic date is created", programmaticDate);
		assertTrue("the programmatic date is 10 milliseconds around instead of the today date",
				today.compareTo(programmaticDate) < 0);
		
		await()
		    .atLeast(Duration.ONE_HUNDRED_MILLISECONDS)
		    .atMost(Duration.TEN_SECONDS)
		   .with()
		    .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
		    .until(schedulerBean::getLastAutomaticTimeout , notNullValue() );		
		Date automaticDate = schedulerBean.getLastAutomaticTimeout();
		
		assertNotNull("the automatic date is created", automaticDate);
		assertTrue("the today date is 1 milliseconds around instead of the automatic date",
				today.compareTo(automaticDate) > 0);
		
		logger.info("End test scheduler.");
	}

	@Test
	public void testManualConfiguration() {
		Timer timer = schedulerBean.setTimer("10", new Date());
		ScheduleExpression scheduleExpression = timer.getSchedule();
		scheduleExpression.hour(10);
		scheduleExpression.start(new Date());
		assertEquals("The timer will start today at the 10", "10", scheduleExpression.getHour());
		assertEquals("The timer will start each Wednesday too", "Wed", scheduleExpression.getDayOfWeek());
	}

}
