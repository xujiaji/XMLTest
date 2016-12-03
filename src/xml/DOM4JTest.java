package xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import xml.entity.Book;
import xml.util.GenericHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jiana on 28/11/16.
 */

public class DOM4JTest {
    public static void main(String[] args) {
        List<Book> books = parse();
        for (Book book : books) {
            System.out.println(book);
        }
        
        createXML(books);
    }

    /**
     * 创建
     */
    public static void createXML(List<Book> books) {
        try {
            //************创建文档***************
            Document document = DocumentHelper.createDocument();
            
            //*************填充数据*********************
            Element bookStore = document.addElement("bookStore");
            for(Book b : books) {
                Element book = bookStore.addElement("book");
                book.addAttribute("id", b.getId());
                for(String key : GenericHelper.BOOK_KEY_ARR) {
                    Element name = book.addElement(key);
                    name.setText(GenericHelper.getValue(key, b));  
                }
            }
            
//            Element html = bookStore.addElement("html");
//            html.setText("<!-- html注释 -->");
            
            // ***************输出*******************
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream("NewBooks_DOM4J.xml"), format);
            //如果不希望特殊字符被转义，那么将默认的自动转义设置为false
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
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
            // *************DOM4J读取xml文件*******************
            SAXReader reader = new SAXReader();
            Document document = reader.read("books.xml");
            
            // ****************获取根节点开始解析***************************
            Element bookStore = document.getRootElement();
            //通过element对象的elementIterator方法获取迭代器
            Iterator<?> it = bookStore.elementIterator();
            while (it.hasNext()) {
                Book bookEntity = new Book();
                Element book  = (Element) it.next();
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
//                    System.out.println("属性" + attr.getName() + ":" + attr.getValue());
                    bookEntity.setId(attr.getValue());
                }
                Iterator<?> itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
//                    System.out.println("节点" + bookChild.getName() + ":" + bookChild.getStringValue());
                    GenericHelper.autoAddValue(bookChild.getName(),
                            bookChild.getStringValue(),
                            bookEntity);
                }
                books.add(bookEntity);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return books;
    }
}
