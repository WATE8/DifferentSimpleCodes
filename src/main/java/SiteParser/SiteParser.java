package SiteParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class SiteParser {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://news.ycombinator.com/").get();
            Elements titles = doc.select("span.titleline > a");

            for (Element title : titles) {
                System.out.println(title.text());
                System.out.println("Ссылка: " + title.absUrl("href"));
                System.out.println("-----");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при подключении: " + e.getMessage());
        }
    }
}
