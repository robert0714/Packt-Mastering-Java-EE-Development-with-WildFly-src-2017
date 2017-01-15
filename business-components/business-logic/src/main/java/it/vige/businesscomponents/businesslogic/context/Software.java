package it.vige.businesscomponents.businesslogic.context;

public interface Software {

	int go(int speed);

	int retry(int speed);
	
	int getSpeed();

	void log();
	
	void add(Object data);

}
