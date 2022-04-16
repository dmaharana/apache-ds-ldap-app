package in.home.app.services;

import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
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
		}

		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAllUsers() throws NamingException {
		String searchFilterString = "(objectClass=inetOrgPerson)";
		String[] reqAttrs = { "cn" };
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAttrs);

		NamingEnumeration<SearchResult> users = connection.search("ou=users,ou=system", searchFilterString, controls);

		SearchResult result = null;
		while (users.hasMore()) {
			result = (SearchResult) users.next();
			Attributes attributes = result.getAttributes();
			System.out.println(attributes.get("cn"));
		}
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
