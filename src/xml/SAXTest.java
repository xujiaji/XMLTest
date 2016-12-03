package xml;

import xml.entity.Book;
import xml.util.GenericHelper;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by jiana on 26/11/16.
 */

public class SAXTest {
    public static void main(String[] args) {
        List<Book> bookList = parse();
        System.out.println("bookList size = " + bookList.size());
        for (Book book :
                bookList) {
            System.out.println(book);
        }
        
        
        createXML(bookList);
    }

    
    /**
     * 创建xml
     */
    public static void createXML(List<Book> bookList) {
        try {
            //***********初始化转换器************
            SAXTransformerFactory stff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler handler = stff.newTransformerHandler();
            Transformer t = handler.getTransformer();
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            Result result = new StreamResult(new FileOutputStream("NewBooks_SAX.xml"));
            handler.setResult(result);
            
          //***********开始向xml文件添加内容************
            //开始文档
            handler.startDocument();
            AttributesImpl attr = new AttributesImpl();
            //根节点配置开始
            handler.startElement("", "", "bookstore", attr);
            for (Book book : bookList) {
                attr.clear();
                attr.addAttribute("", "", "id", "", book.getId());
                handler.startElement("", "", "book", attr);
                for (String key : GenericHelper.BOOK_KEY_ARR) {
                    addElement(key, book, handler, attr);
                }
                handler.endElement("", "", "book");
            }
            handler.endElement("", "", "bookstore");
            //结束
            handler.endDocument();
//            t.transform(new StreamSource(), result);
        } catch (FileNotFoundException | TransformerException | SAXException e) {
            e.printStackTrace();
        }
    }
    
    public static void addElement(String qName, Book bookEntity, TransformerHandler handler, AttributesImpl attr) throws SAXException {
        String value = GenericHelper.getValue(qName, bookEntity);
        if (value == null || value.trim().equals("")) {
            return;
        }
        attr.clear();
        handler.startElement("", "", qName, attr);
        handler.characters(value.toCharArray(), 0, value.length());
        handler.endElement("", "", qName);
    }
    

    /**
     * 解析
     */
    public static List<Book> parse() {
        //获取一个SAXParserFactory的实力
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            //通过factory获取SAXParser对象
            SAXParser parser = factory.newSAXParser();
            SAXParserHandler handler = new SAXParserHandler();
            parser.parse("books.xml", handler);
            return handler.getBookList();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一个SAX解析处理器
     * @author jiana
     *
     */
    public static class SAXParserHandler extends DefaultHandler {
        private int bookNum = 0;
        private Book book;
        private String value;
        private List<Book> bookList = new ArrayList<>();

        /**
         * 用来标志解析开始
         */
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
//            System.out.println("SAX解析开始");
        }

        /**
         * 用来标记解析结束
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
//            System.out.println("SAX解析结束");
        }

        /**
         * 用来遍历xml文件的开始标签
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            //解析book元素的属性
            if (qName.equals("book")) {
                book = new Book();
                //已知book元素的属性名称，根据属性名称获取属性值
//                String value = attributes.getValue("id");
//                System.out.println("book的属性值是：" + value);
                for (int i = 0, num = attributes.getLength(); i < num; i++) {
                    book.setId(attributes.getValue(i));
//                    System.out.println(attributes.getQName(i) + ":" + attributes.getValue(i));
                }
                bookNum++;
            } else if (!qName.equals("bookstore")) {
//                System.out.print("节点" + qName);
            }

        }

        /**
         * 用来遍历xml文件的结束标签
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            //判断是否是一本书结束
            if (qName.equals("book")) {
                bookList.add(book);
                book = null;
//                System.out.println("---------遍历第 " + bookNum + " 本书结束------------");
            }
            GenericHelper.autoAddValue(qName, value, book);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String value = new String(ch, start, length);
            if (!"".equals(value.trim())) {
                this.value = value;
//                System.out.println(":" + value);
            }
        }

        public List<Book> getBookList() {
            return bookList;
        }
    }
}
