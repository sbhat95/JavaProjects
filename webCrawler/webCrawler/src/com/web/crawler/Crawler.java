package com.web.crawler;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	
	public Crawler() {
	}
	/**
	 * @param url
	 * @param pageVisited
	 * @param year
	 * @param ignoreDuplicate
	 * @return
	 * Crawl the url and prepare the links that exist in the url
	 */
	public LinkedHashSet<String> crawl(String url, HashSet<String> pageVisited, String year, String ignoreDuplicate) {
		
		PrintStream out = null;
		LinkedHashSet<String> toCrawlList = null;
		try {
			
			toCrawlList = new LinkedHashSet<String>();
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			
			Elements linksOnPage = htmlDocument.select("a[href]");
			for (Element link : linksOnPage) {
				String verfiedLink = link.absUrl("href");
				
				// ignore link which is already visited
				  if (pageVisited.contains(verfiedLink)) {
	                  continue;
	            }
				if (verfiedLink == null || !verfiedLink.contains(year))
					continue;
				
				if (ignoreDuplicate != null && !verfiedLink.contains(ignoreDuplicate))
					continue;
				
				toCrawlList.add(verfiedLink);
			}
		} catch (IOException e) {
			System.out.println("Caught IO Exception " +e);
		} finally {
			if(out != null)
				out.close();
		}
		
		return toCrawlList;
	}
}
