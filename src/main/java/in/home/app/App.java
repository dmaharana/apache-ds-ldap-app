package in.home.app;

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

		// ldapOpsService.addUser();

//		ldapOpsService.getAllUsers();

//		ldapOpsService.addUserToGroup("Sita", "jira-users");
//		ldapOpsService.deleteUserFromGroup("Sita", "jira-users");

		ldapOpsService.searchUsers(3);
		boolean valid = ldapOpsService.authenticateUser("Sita", "password");
		if (valid) {
			System.out.println("Valid user");
		} else {
			System.out.println("Invalid credentials");
		}
	}
}
