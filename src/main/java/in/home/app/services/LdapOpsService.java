package in.home.app.services;

import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LdapOpsService {

	private Properties env;
	private DirContext connection;

	public LdapOpsService() {
		super();

	}

	public void connectLdap() {
		env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389");
		env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");
		try {
			connection = new InitialDirContext(env);
			System.out.println("Establishing connection: " + connection);
		} catch (AuthenticationException e) {
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAllUsers() {
		String searchFilterString = "(objectClass=inetOrgPerson)";
		String[] reqAttrs = { "cn", "sn" };
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAttrs);
		SearchResult result = null;

		NamingEnumeration<SearchResult> users;
		try {
			users = connection.search("ou=users,ou=system", searchFilterString, controls);

			while (users.hasMore()) {
				result = (SearchResult) users.next();
				Attributes attributes = result.getAttributes();
				System.out.println(attributes.get("cn"));
				System.out.println(attributes.get("sn"));
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void searchUsers(int uid) {
		String cnString = "Sita";
//		String searchFilterString = "(uid=" + uid + ")";
//		String searchFilterString = "(&(uid=" + uid + ")(cn=" + cnString + "))";
		String searchFilterString = "(|(uid=1)(uid=2))";
		System.out.println("searchFilterString: " + searchFilterString);

		String[] reqAttrs = { "cn", "sn" };
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAttrs);
		SearchResult result = null;

		NamingEnumeration<SearchResult> users;
		try {
			users = connection.search("ou=users,ou=system", searchFilterString, controls);
			while (users.hasMore()) {
				result = (SearchResult) users.next();
				Attributes attributes = result.getAttributes();
				System.out.println(attributes.get("cn"));
				System.out.println(attributes.get("sn"));
			}
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void addUser() {
		Attributes attributes = new BasicAttributes();
		Attribute attr = new BasicAttribute("objectClass");
		attr.add("inetOrgPerson");

		attributes.put(attr);
		attributes.put("sn", "Maia");

		try {
			connection.createSubcontext("cn=Sita,ou=users,ou=system", attributes);
			System.out.println("Success: User created!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addUserToGroup(String usernameString, String groupNameString) {
		ModificationItem[] mods = new ModificationItem[1];
		Attribute attr = new BasicAttribute("uniqueMember", "cn=" + usernameString + ",ou=users,ou=system");
		mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);

		try {
			connection.modifyAttributes("cn=" + groupNameString + ",ou=groups,ou=system", mods);
			System.out.println("Success: User added to group!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteUserFromGroup(String usernameString, String groupNameString) {
		ModificationItem[] mods = new ModificationItem[1];
		Attribute attr = new BasicAttribute("uniqueMember", "cn=" + usernameString + ",ou=users,ou=system");
		mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);

		try {
			connection.modifyAttributes("cn=" + groupNameString + ",ou=groups,ou=system", mods);
			System.out.println("Success: User deleted from group!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean authenticateUser(String usernameString, String passwordString) {
		boolean valid = false;

		env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389");
		env.put(Context.SECURITY_PRINCIPAL, "cn=" + usernameString + ",ou=users,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, passwordString);
		try {
			connection = new InitialDirContext(env);
			System.out.println("Establishing connection: " + connection);
			connection.close();
			valid = true;
		} catch (AuthenticationException e) {
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valid;
	}

	public Properties getEnv() {
		return env;
	}

	public void setEnv(Properties env) {
		this.env = env;
	}

	public DirContext getConnection() {
		return connection;
	}

	public void setConnection(DirContext connection) {
		this.connection = connection;
	}

}
