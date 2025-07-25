# SiteParser

Простой Java-парсер новостей с [Hacker News](https://news.ycombinator.com/) с использованием Jsoup.

## Запуск

1. Добавь Jsoup (Maven):

```xml
<dependency>
  <groupId>org.jsoup</groupId>
  <artifactId>jsoup</artifactId>
  <version>1.17.2</version>
</dependency>
Запусти:

bash
Копировать
Редактировать
javac -cp jsoup-1.17.2.jar SiteParser.java
java -cp .:jsoup-1.17.2.jar SiteParser
Что делает
Выводит заголовки и ссылки новостей.