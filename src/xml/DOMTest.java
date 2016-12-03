package xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xml.entity.Book;
import xml.util.GenericHelper;

public class DOMTest {
    public static void main(String[] args) {
        List<Book> books = parse();
        for (Book book : books) {
            System.out.println(book);
        }

        createXML(books);
    }

    /**
     * 创建xml文件
     * 
     * @param books
     *            book集合
     */
    public static void createXML(List<Book> books) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // *********创建一个文档********
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            document.setXmlStandalone(true);// 去掉xml声明：standalone="no"

            // *********添加向内存中的xml树添加内容********
            Element bookstore = document.createElement("bookstore");
            for (Book bookEntity : books) {
                Element book = document.createElement("book");
                book.setAttribute("id", bookEntity.getId());
                for (String key : GenericHelper.BOOK_KEY_ARR) {
                    book.appendChild(createEle(key, bookEntity, document));
                }
                bookstore.appendChild(book);
            }
            document.appendChild(bookstore);

            // *********输出内存中的xml树输出到文件********
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(new File("NewBooks_DOM.xml")));
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建book的子节点
     * 
     * @param qName
     * @param bookEntity
     * @param document
     * @return
     */
    public static Element createEle(String qName, Book bookEntity, Document document) {
        Element ele = document.createElement(qName);
        ele.setTextContent(GenericHelper.getValue(qName, bookEntity));
        return ele;
    }

    /**
     * 解析xml
     */
    public static List<Book> parse() {
        List<Book> books = new ArrayList<Book>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse("books.xml");
            // 获取所有book节点的集合
            NodeList bookList = document.getElementsByTagName("book");
            // 遍历所有的book
            for (int i = 0, len = bookList.getLength(); i < len; i++) {
                Book bookEntity = new Book();
                Node book = bookList.item(i);
                bookEntity.setId(((Element) book).getAttribute("id"));
                // Node book = notKnowAttr(bookList, i);
                // knowAttr(bookList, i);
                NodeList childNodes = book.getChildNodes();
                for (int k = 0; k < childNodes.getLength(); k++) {
                    // 区分出text类型的node以及element类型的node
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        GenericHelper.autoAddValue(childNodes.item(k).getNodeName(),
                                childNodes.item(k).getFirstChild().getNodeValue(), bookEntity);
                        // 获取出element类型节点的节点名
                        // System.out.print(childNodes.item(k).getNodeName());
                        // System.out.println(":" +
                        // childNodes.item(k).getFirstChild().getNodeValue());
                        // 把更下一级的子节点当做文本输出
                        // System.out.println(":" +
                        // childNodes.item(k).getTextContent());
                    }
                }
                books.add(bookEntity);
            }
            return books;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
