package study.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {

	/**
	 * ÓÃ»§Ãû£¨µÇÂ¼ÓÊÏä£©
	 */
	private String username;
	/**
	 * ÃÜÂë
	 */
	private String password;

	/**
	 * ³õÊ¼»¯ÓÊÏäºÍÃÜÂë
	 * 
	 * @param username
	 *            ÓÊÏä
	 * @param password
	 *            ÃÜÂë
	 */
	public MailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	String getPassword() {
		return password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
