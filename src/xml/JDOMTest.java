package xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import xml.entity.Book;
import xml.util.GenericHelper;

/**
 * Created by jiana on 27/11/16.
 */

public class JDOMTest {
    public static void main(String[] args) {
        List<Book> books = parse();
        for (Book book : books) {
            System.out.println(book);
        }
        
        createXML(books);
    }

    /**
     * JDOM创建xml文件
     */
    public static void createXML(List<Book> books) {
     // *****************设置xml内容********************
        Element root = new Element("bookStore");
        Element book = new Element("book");
        book.setAttribute("id", "1");
        
        for(Book bookEntity : books) {
            for(String key : GenericHelper.BOOK_KEY_ARR) {
                Element ele = new Element(key);
                ele.setText(GenericHelper.getValue(key, bookEntity));
                book.addContent(ele);
            }
        }
        
//        Element html = new Element("html");
//        // html.setText("<!-- html注释 -->");
//        html.addContent(new CDATA("<!-- html注释 -->"));
//        root.addContent(html);
        root.addContent(book);
        Document document = new Document();
        document.setRootElement(root);
        
        // *****************输出xml文件********************
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        try {
            outputter.output(document, new FileOutputStream("NewBooks_JDOM.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析
     */
    public static List<Book> parse() {
        List<Book> books = new ArrayList<Book>();
        try {
            //***********JDOM的初始步骤**********
            SAXBuilder saxBuilder = new SAXBuilder();
            InputStream in = new FileInputStream("books.xml");
            Document document = saxBuilder.build(in);
            
            //*****************开始解析******************
            Element rootElement = document.getRootElement();
            List<Element> bookList = rootElement.getChildren();
            for (Element book : bookList) {
                Book bookEntity = new Book();
//                System.out.println("-----开始解析第" + (bookList.indexOf(book) + 1) + "书");

                // System.out.println(book.getAttributeValue("id"));知道属性名的时候可以这样获取值
                List<Attribute> attributeList = book.getAttributes();
                for (Attribute attribute : attributeList) {
                    bookEntity.setId(attribute.getValue());
//                    // 获取属性名
//                    System.out.print("属性" + attribute.getName());
//                    // 获取属性值
//                    System.out.println(":" + attribute.getValue());
                }
                // 对book节点的子节点的节点名和节点值遍历
                List<Element> bookChilds = book.getChildren();
                for (Element e : bookChilds) {
                    GenericHelper.autoAddValue(e.getName(), e.getValue(), bookEntity);
//                    System.out.println("节点" + e.getName() + ":" + e.getValue());
                }
                books.add(bookEntity);
//                System.out.println("-----结束解析第" + (bookList.indexOf(book) + 1) + "书");
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        return books;
    }
}
