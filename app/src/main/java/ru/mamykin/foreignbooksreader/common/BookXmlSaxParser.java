package ru.mamykin.foreignbooksreader.common;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ru.mamykin.foreignbooksreader.models.FictionBook;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public class BookXmlSaxParser extends DefaultHandler2 {
    private StringBuilder titleSb = new StringBuilder();
    private StringBuilder textSb = new StringBuilder();
    private TextHashMap transMap = new TextHashMap();
    private SaxParserListener listener;
    private String lastSentence;
    private boolean titleInfo;
    private boolean inSection;
    private FictionBook book;
    private boolean inTitle;
    private String element;

    public BookXmlSaxParser(SaxParserListener listener, FictionBook book) {
        this.listener = listener;
        this.book = book;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        element = qName;
        switch (element) {
            case "title-info":
                titleInfo = true;
                break;
            case "title":
                inTitle = true;
                break;
            case "section":
                inSection = true;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if (inTitle) {
            // Собираем заголовок
            titleSb.append("<").append(element).append(">")
                    .append(ch, start, length)
                    .append("</").append(element).append(">");
        } else if (inSection) {
            if (element.equals("p")) {
                // Собираем текст
                lastSentence = new String(ch, start, length);
                textSb.append("<p>").append(lastSentence).append("</p>");
            } else if (element.equals("t")) {
                // Собираем перевод
                transMap.put(lastSentence.trim(), new String(ch, start, length).trim());
            }
        } else switch (element) {
            case "genre":
                book.setBookGenre(new String(ch, start, length));
                break;
            case "first-name":
                if (titleInfo)
                    book.setBookAuthor(new String(ch, start, length) + " ");
                else
                    book.setDocAuthor(new String(ch, start, length) + " ");
                break;
            case "last-name":
                if (titleInfo)
                    book.setBookAuthor(book.getBookAuthor() + new String(ch, start, length) + " ");
                else
                    book.setDocAuthor(book.getDocAuthor() + new String(ch, start, length) + " ");
                break;
            case "middle-name":
                if (titleInfo)
                    book.setBookAuthor(book.getBookAuthor() + new String(ch, start, length));
                else
                    book.setDocAuthor(book.getDocAuthor() + new String(ch, start, length));
                break;
            case "book-title":
                book.setBookTitle(new String(ch, start, length));
                break;
            case "lang":
                book.setBookLang(new String(ch, start, length));
                break;
            case "src-lang":
                book.setBookSrcLang(new String(ch, start, length));
                break;
            case "library":
                book.setDocLibrary(new String(ch, start, length));
                break;
            case "url":
                book.setDocUrl(new String(ch, start, length));
                break;
            case "date":
                book.setDocDate(Utils.getDateFromString(new String(ch, start, length)));
                break;
            case "version":
                book.setDocVersion(Double.parseDouble(new String(ch, start, length)));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        element = "";
        switch (qName) {
            case "title-info":
                titleInfo = false;
                break;
            case "title":
                inTitle = false;
                break;
            case "section":
                inSection = false;
                break;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        book.setBookText(textSb.toString());
        book.setTransMap(transMap);
        book.setSectionTitle(titleSb.toString());
        listener.onParseCompleted();
    }

    /**
     * Парсинг книги по пути к файлу, вызывается при открытии файла с устройства
     * @param book     объект с книгой, в котором содержится путь к файлу
     * @param listener слушатель, в котором вызывается {@link SaxParserListener#onParseCompleted()} по окончании парсинга
     */
    public static void parseBook(FictionBook book, SaxParserListener listener) {
        try {
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final SAXParser parser = factory.newSAXParser();
            final BookXmlSaxParser parseHandler = new BookXmlSaxParser(listener, book);
            parser.parse(new File(book.getFilePath()), parseHandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public interface SaxParserListener {
        void onParseCompleted();
    }
}