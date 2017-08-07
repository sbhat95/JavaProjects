package com.web.crawler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CrawlerService {
	
	private static final int MAX_PAGES_TO_SEARCH = 10000;
	private HashSet<String> pagesVisited = new HashSet<String>();
	private ArrayList<String> pagesToVisit = new ArrayList<String>();
	private LinkedHashSet<String> finalMailLinks = new LinkedHashSet<String>();
	Crawler crawler = new Crawler();
	String filePath = "D://Test//Emailoutput.txt";

	/**
	 * @param url
	 * @param year
	 * Fetch the mails for given year
	 */
	public void fetchEmails(String url, String year) {

		try {
			PrintStream out = null;
			out = new PrintStream(new FileOutputStream(filePath));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			System.out.println("Error occued :: " + e);
		}

		preparePageToVisit(url, year);
		prepareMailLinks(year);

		try {
			downloadMails();
		} catch (IOException e) {
			System.out.println("Error during downloading mails :: " + e);
		}

		System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");

	}

	/**
	 * @throws IOException
	 * Downloads all mails into a file
	 */
	private void downloadMails() throws IOException {
		Iterator<String> it = finalMailLinks.iterator();
		while (it.hasNext()) {
			String url = it.next();
			Document htmlDocument = Jsoup.connect(url).get();
			Element results = htmlDocument.body();
			System.out.println(results.text());
		}

	}

	/**
	 * @param year
	 * Fetch the links of the emails to be downloaded 
	 */
	private void prepareMailLinks(String year) {
		
		LinkedHashSet<String> mailLinks = new LinkedHashSet<String>();

		for (int i = 0; i < pagesToVisit.size(); i++) {

			if (pagesVisited.size() == MAX_PAGES_TO_SEARCH)
				break;

			String verifiedUrl = (String) pagesToVisit.get(i);

			pagesVisited.add(verifiedUrl);

			LinkedHashSet<String> list = crawler.crawl(verifiedUrl, pagesVisited, year, null);

			mailLinks.addAll(list);
		}

		Iterator<String> it = mailLinks.iterator();
		while (it.hasNext()) {
			String finalMailUrls = it.next();
			if (!finalMailUrls.contains("mbox/%"))
				continue;

			finalMailLinks.add(finalMailUrls);
		}
	}

	/**
	 * @param url
	 * @param year
	 * Fetch the links for given year
	 */
	private void preparePageToVisit(String url, String year) {
		pagesToVisit.add(url);
		String verifiedUrl1 = (String) pagesToVisit.iterator().next();

		pagesToVisit.remove(verifiedUrl1);

		pagesVisited.add(verifiedUrl1);
		LinkedHashSet<String> list1 = crawler.crawl(verifiedUrl1, pagesVisited, year, "thread");
		this.pagesToVisit.addAll(list1);
	}

}
