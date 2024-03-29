package com.searchweb.bot;

import java.security.GeneralSecurityException;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.searchweb.db.IRepository;
import com.searchweb.db.MySqlRepository;
import com.searchweb.entity.Article;

@Component
public abstract class Abot implements Runnable {
	@Resource
	MySqlRepository repo1;
	
	public static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	protected WebClient client = null;
	
	public IRepository repo;
	
	public static boolean isInitLock = false;
	
	public String keyword;
	
	public String url;
	
	public Abot() {
//		new Exception();
	}

	public Abot(String keyword, String url) {
		super();
		this.keyword = keyword;
		this.url = url;
		
		Abot bot = context.getBean(Abot.class);
		bot.sayA();
		if(!isInitLock) {
//			repo.add(new Article());
//			this.initialize();
//			this.loadRssMap(strategyName);
		}
	}
	public abstract void sayA();

	public void setClient() {
		if (this.client == null) {
			client = new WebClient(BrowserVersion.FIREFOX_3);
			client.setTimeout(0); // Set to zero for an infinite wait.
			client.setJavaScriptEnabled(true);
			client.setRedirectEnabled(true);
			client.setThrowExceptionOnScriptError(false);
			client.setThrowExceptionOnFailingStatusCode(false);
			
			client.getCookieManager().setCookiesEnabled(true);
			client.getCookieManager().clearCookies();
			
//			WebClient.setIgnoreOutsideContent(true);
			client.setRefreshHandler(new ThreadedRefreshHandler());
			client.setAjaxController(new NicelyResynchronizingAjaxController());
			try {
				client.setUseInsecureSSL(true);
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void closeClient(){
		this.client.closeAllWindows();		
	}
	
	
	public void initialize() {
		this.setClient();
	}
	
	public void finalize() {
		this.closeClient();
	}
	

	public abstract Article getArticle(String keyword, String url);
	
	public Article UseGateBaiduToConvert(String hrefAttribute) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void pushToDB(Article article) {
		repo.add(article);
	}
	
	public void run() {
		// TODO Auto-generated method stub
		pushToDB(this.getArticle(this.keyword, this.url));
	}

}
