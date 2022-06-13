package it.vige.businesscomponents.injection.interceptor.service;

import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;

import it.vige.businesscomponents.injection.interceptor.ExcludedInterceptor;
import it.vige.businesscomponents.injection.interceptor.IncludedInterceptor;


public class SimpleService {

	private Item item;

	//https://docs.wildfly.org/26/Developer_Guide.html#Jakarta_Enterprise_Beans_Clustered_Database_Timers
	@Interceptors({ ExcludedInterceptor.class, IncludedInterceptor.class })
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
