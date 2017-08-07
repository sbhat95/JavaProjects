package com.web.crawler;

public class CrawlerTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 CrawlerService crawler = new CrawlerService();
	        crawler.fetchEmails("http://mail-archives.apache.org/mod_mbox/maven-users/", "2015");

	        
	}

}
