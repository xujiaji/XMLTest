package xml;

import java.util.List;

import xml.entity.Book;

public class XMLSeedTest {

    public static void main(String[] args) {
        System.out.println("*****************读取xml文件速度测试*****************");
        long start = System.currentTimeMillis();
        long end = 0;
        DOMTest.parse();
        end = System.currentTimeMillis();
        xPrint("DOM解析时间", start, end);
        
        start = System.currentTimeMillis();
        SAXTest.parse();
        end = System.currentTimeMillis();
        xPrint("SAX解析时间", start, end);
       
        start = System.currentTimeMillis();
        JDOMTest.parse();
        end = System.currentTimeMillis();
        xPrint("JDOM解析时间", start, end);
        
        start = System.currentTimeMillis();
        DOM4JTest.parse();
        end = System.currentTimeMillis();
        xPrint("DOM4J解析时间", start, end);
        
        
        System.out.println("*****************创建xml文件速度测试*****************");
        List<Book> books = JDOMTest.parse();
        start = System.currentTimeMillis();
        DOMTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("DOM创建时间", start, end);
        
        start = System.currentTimeMillis();
        SAXTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("SAX创建时间", start, end);
       
        start = System.currentTimeMillis();
        JDOMTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("JDOM创建时间", start, end);
        
        start = System.currentTimeMillis();
        DOM4JTest.createXML(books);
        end = System.currentTimeMillis();
        xPrint("DOM4J创建时间", start, end);
    }
    
    public static void xPrint(String who, long start, long end) {
        System.out.println(who + "：" + (end - start));
    }
}
