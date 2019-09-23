import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String mainUrl = "https://coursehunter.net/archive";
    private static final String pageUrl = "https://coursehunter.net/archive?page=";

    private static Document getPage(String url) throws IOException {
        return Jsoup.parse(new URL(url), 15000);
    }

    private static int getPagesCounter(String url) throws IOException {
        Document page = getPage(url);
        Elements paginators = page.select("li[class=pagination__li]");
        Element lastPaginator = paginators.get(paginators.size() - 2);

        return Integer.parseInt(lastPaginator.select("span").text());
    }

    public static void main(String[] args) throws IOException {
        HashMap<String,String> result = new HashMap<String, String>();

        int i = getPagesCounter(mainUrl);
        System.out.println("Количество страниц в архиве - " + i);
        System.out.println("Начинаю поиск...");

        for (int y = 1; y <= i; y++) {
            System.out.println("Страница " + y + " из " + i);
            Document page = getPage(pageUrl + y);
            Elements courses = page.select("article[class=course]");
            for (Element e: courses) {
                Element free = e.selectFirst("div[class=course-status course-status-free]");
                if (free != null) {
                    String courseName = e.selectFirst("h3[class=course-primary-name]").text();
                    String courseUrl = e.selectFirst("a[class=course-btn btn]").attr("href");
                    result.put(courseUrl,courseName);
                }
            }


        }

        for (Map.Entry<String,String> entry : result.entrySet()) {
            System.out.println(entry.getValue() + " => " + entry.getKey());
        }
    }
}
