package it.vige.businesscomponents.injection.interceptor;

import static java.util.logging.Logger.getLogger;

import java.util.Map;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import it.vige.businesscomponents.injection.interceptor.service.Item;

@Interceptor
public class ExcludedInterceptor {

	private static final Logger logger = getLogger(ExcludedInterceptor.class.getName());

	@AroundInvoke
	public Object aroundInvoke(final InvocationContext ic) throws Exception {
		String methodName = ic.getMethod().getName();
		logger.info("Executing " + ic.getTarget().getClass().getSimpleName() + "." + methodName + " method");
		Object[] parameters = (Object[]) ic.getParameters();
		logger.info("parameters are: " + parameters.length);
		if (parameters.length == 1) {
			Item item = (Item) parameters[0];
			logger.info("item: " + item.getName());
		}
		final Map<String, Object> contextData = ic.getContextData();
		System.out.println("-------robert---------ExcludedInterceptor(1)");
		System.out.println(ic.getClass().getName());
		System.out.println(ic);
		System.out.println("-------robert---------ExcludedInterceptor(1)");
		if (contextData.isEmpty()) {
			contextData.put("test_trace", "test_trace");
			
			System.out.println("-------robert detect is empty---------so add test_trace(1)");
		}
			
		return ic.proceed();
	}
}
