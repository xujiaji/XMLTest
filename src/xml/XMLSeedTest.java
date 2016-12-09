package xml;

import xml.entity.Book;

import java.util.List;

public class XMLSeedTest {
    public static final String XML_FILE = "books.xml";

    public static void main(String[] args) {
        System.out.println("*****************读取xml文件速度测试*****************");
        long start = System.currentTimeMillis();
        long end = 0;
        DOMTest.parse();
        end = System.currentTimeMillis();
        xPrint("DOM  解析时间", start, end);
        
        start = System.currentTimeMillis();
        SAXTest.parse();
        end = System.currentTimeMillis();
        xPrint("SAX  解析时间", start, end);
       
        start = System.currentTimeMillis();
        JDOMTest.parse();
        end = System.currentTimeMillis();
        xPrint("JDOM 解析时间", start, end);
        
        start = System.currentTimeMillis();
        DOM4JTest.parse();
        end = System.currentTimeMillis();
        xPrint("DOM4J解析时间", start, end);
        
        
        System.out.println("*****************创建xml文件速度测试*****************");
        List<Book> books = JDOMTest.parse();
        start = System.currentTimeMillis();
        DOMTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("DOM  创建时间", start, end);
        
        start = System.currentTimeMillis();
        SAXTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("SAX  创建时间", start, end);
       
        start = System.currentTimeMillis();
        JDOMTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("JDOM 创建时间", start, end);
        
        start = System.currentTimeMillis();
        DOM4JTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("DOM4J创建时间", start, end);
    }
    
    public static void xPrint(String who, long start, long end) {
        System.out.println(who + "：" + (end - start) + " ms");
    }
}
