package xml.util;

import xml.entity.Book;

public class GenericHelper {
    /**
     * book下的子节点
     */
    public static final String[] BOOK_KEY_ARR = { "name", "author", "year", "price" };
    
    /**
     * 自动设置值
     * @param qName
     * @param value
     * @param book
     */
    public static void autoAddValue(String qName, String value, Book book) {
        switch (qName) {
        case "name":
            book.setName(value);
            break;
        case "author":
            book.setAuthor(value);
            break;
        case "year":
            book.setYear(value);
            break;
        case "price":
            book.setPrice(value);
            break;
        }
    }
    
    /**
     * 获取值
     * @param qName
     * @param bookEntity
     * @return
     */
    public static String getValue(String qName, Book bookEntity) {
        String value = null;
        switch (qName) {
        case "name":
            value = bookEntity.getName();
            break;
        case "author":
            value = bookEntity.getAuthor();
            break;
        case "year":
            value = bookEntity.getYear();
            break;
        case "price":
            value = bookEntity.getPrice();
            break;
        }
        return value;
    }
}
