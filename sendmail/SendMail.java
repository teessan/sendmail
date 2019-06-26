package sendmail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import common.Util;

public class SendMail {

	private String smtpServer = "";
	private String mailPort = "";
	private String authAccount = "";
	private String passWord = "";
	private String mailContents = "";
	private String encode = "UTF-8";
	private Session session = null;
	private MimeMessage message = null;

	//smtpServer
	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	//mailPort
	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	//authAccount
	public String getAuthAccount() {
		return authAccount;
	}

	public void setAuthAccount(String authAccount) {
		this.authAccount = authAccount;
	}

	//passWord
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	//contents
	public String getMailContents() {
		return mailContents;
	}

	public void setMailContents(String mailContents) {
		this.mailContents = mailContents;
	}

	//encode
	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	//session
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	//message
	public MimeMessage getMessage() {
		return message;
	}

	public void setMessage(MimeMessage message) {
		this.message = message;
	}

	public void setSmtpProperty() {
		final Properties props = new Properties();
		//SMTPサーバの設定。ここではgoogleemailのSMTPサーバーを設定
		props.setProperty("mail.smtp.host", smtpServer);
		//SSL用にポート番号を変更
		props.setProperty("mail.smtp.port", mailPort);
		//タイムアウト設定
		props.setProperty("mail.smtp.connectiontimeout", "60000");
		props.setProperty("mail.smtp.timeout", "60000");

		//認証
		props.setProperty("mail.smtp.auth", "true");
		//SSLを使用するとこはこの設定が必要
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", mailPort);

		//propsに設定した情報を使用して、sessionの作成
		final Session mailsession = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(authAccount, passWord);
			}
		});
		//session設定
		setSession(mailsession);
	}

	public void setMessageContents(String fromMail,String fromName, String toMail, String toName ,String Subject,String contentsFlag) {
		MimeMessage contentMessage = new MimeMessage(session);
		try {
			Address addFrom = new InternetAddress(fromMail, fromName, encode);
			contentMessage.setFrom(addFrom);
			Address addTo = new InternetAddress(toMail, toName, encode);
			contentMessage.addRecipient(Message.RecipientType.TO, addTo);
			contentMessage.setSubject(Subject, encode);
			if (contentsFlag == "text") {
				//メール本文(テキスト)
				contentMessage.setText(mailContents,encode);
				//その他の付加情報
				contentMessage.addHeader("X-Mailer","blancoMail 0.1");
			}else if (contentsFlag == "html") {
				contentMessage.setHeader("Content-Type", "text/html; charset=" + encode);
				contentMessage.setContent(mailContents, "text/html; charset=" + encode);
			}else {
				System.exit(1);
			}
			contentMessage.setSentDate(new Date());
			setMessage(contentMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void send() {
		//メール送信
		try {
			Transport.send(message);
		} catch (AuthenticationFailedException e) {
			//認証失敗
			e.printStackTrace();
		} catch (MessagingException e) {
			//smtpサーバへの接続失敗
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SendMail sm = new SendMail();
		//設定
		sm.setSmtpServer("smtp.gmail.com");//googleのsmtpサーバ
		sm.setMailPort("465");//smtpサーバのポート
		sm.setAuthAccount("XXXX@gmail.com");//googleアカウント
		sm.setPassWord("XXXXXX");//googleアカウントのパスワード
		sm.setSmtpProperty();
		//ここまで

		//コンテンツ元データを読み込み
		String contentsStr = "";
		String contentsFlag = "";
		if (contentsFlag == "html") {
			//html contentsの指定
			contentsStr = Util.readFileStr("test.html");
		}else if (contentsFlag == "text") {
			//text contentsの指定
			contentsStr = Util.readFileStr("test.txt");
		}else {
			System.exit(1);
		}
		//送信元メールアドレス
		String fromMailAddress = "sender mail address";
		//送信元名称
		String fromMailName = "sender name";
		//件名
		String subject = "こんにちは！";

		//ここをDBからの取得にすれば複数のメールアドレスを取ることが容易
		Map<Integer,Map<String,String>> userMap = new LinkedHashMap<Integer,Map<String,String>>();
		Map<String,String> user1 = new LinkedHashMap<String,String>();
		user1.put("mail_address", "XXXX@gmail.com");//送信したいメールアドレス１つ目
		user1.put("name","set name");//名称（コンテンツ内の差し込みに使われる）
		Map<String,String> user2 = new LinkedHashMap<String,String>();
		user2.put("mail_address","XXXX@gmail.com");//送信したいメールアドレス２つ目
		user2.put("name","nina");//名称（コンテンツ内の差し込みに使われる）
		userMap.put(1,user1);
		userMap.put(2,user2);


		for (Map.Entry<Integer,Map<String,String>> element : userMap.entrySet()) {
			 Map<String,String> scores = element.getValue();
			 String sendMailAddress = scores.get("mail_address");
			 String sendMailName = scores.get("name");
			 //コンテンツに名前を差し込むための処理
			 String replaceContents = contentsStr.replace("{@test}",sendMailName );
			 //変更されたコンテンツをセット
			 sm.setMailContents(replaceContents);
			 //送信元、送信先、件名、htmlメールかtextメールかのセット
			 sm.setMessageContents(fromMailAddress,fromMailName, sendMailAddress,sendMailName, subject,contentsFlag);
			 //送信
			 sm.send();
		}
	}

}
