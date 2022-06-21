package it.vige.businesscomponents.injection.inject.impl;

import static it.vige.businesscomponents.injection.inject.model.StateBook.DRAFT;
import static java.util.Arrays.asList;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import it.vige.businesscomponents.injection.inject.Draft;
import it.vige.businesscomponents.injection.inject.Published;
import it.vige.businesscomponents.injection.inject.Service;
import it.vige.businesscomponents.injection.inject.model.Book;

@it.vige.businesscomponents.injection.inject.profile.Book
public class BookService implements Service {

	@Draft
	@Produces
	public List<Book> getDraftBooks() {
		Book[] books = new Book[] { new Book("Glassfish", "Luca Stancapiano", DRAFT),
				new Book("Maven working", "Luca Stancapiano", DRAFT) };
		return asList(books);
	}
	
	//https://docs.jboss.org/cdi/api/2.0/javax/enterprise/inject/Disposes.html
	public void close(@Disposes @Draft List<Book> books) {
		books.clear();
	}
}
