package in.home.app;

import javax.naming.NamingException;

import in.home.app.services.LdapOpsService;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		LdapOpsService ldapOpsService = new LdapOpsService();
		ldapOpsService.connectLdap();
		try {
			ldapOpsService.getAllUsers();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
