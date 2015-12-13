package com.shsz.parsexml.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.ProcessingInstruction;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Maps;

public class TestParseXml implements ElementHandler{
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		new TestParseXml();
		System.out.println("用时:"+(System.currentTimeMillis()-startTime)+"毫秒");
	}
	
	public TestParseXml(){
		try {
			SAXReader reader = new SAXReader();
			//InputStream input = new FileInputStream(new File("E:/ENB.xml"));
			reader.setDefaultHandler(this);
			reader.read(new File("E:/ENB.xml")); 
			/*Element rootElement =  doc.getRootElement(); 
			readNode(rootElement, "");  */
			
			//doc.accept(new MyVistor()); 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @SuppressWarnings("unchecked")  
    public static void readNode(Element root, String prefix) {  
        if (root == null) return;  
        System.out.println("元素名称:"+root.getName());
        System.out.println("元素值:"+root.getText());
        // 获取属性   
        List<Attribute> attrs = root.attributes();  
        if (attrs != null && attrs.size() > 0) {  
            System.out.print(prefix);  
            for (Attribute attr : attrs) {  
                System.out.print(attr.getValue() + " ");  
             }  
             System.out.println();  
         }  
         // 获取他的子节点   
         List<Element> childNodes = root.elements();  
         prefix += "\t";  
         for (Element e : childNodes) {  
             readNode(e, prefix);  
         }  
     }

	@Override
	public void onStart(ElementPath elementPath) {
		
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		Element e = elementPath.getCurrent();
		//System.out.println(e.getText());
		e.detach(); 
	}  
}

class MyVistor extends VisitorSupport {  
    public void visit(Attribute node) {  
        System.out.println("Attibute: " + node.getName() + "="  
                + node.getValue());  
    }  
  
    public void visit(Element node) {  
        if (node.isTextOnly()) {  
            System.out.println("Element: " + node.getName() + "="  
                     + node.getText());  
         } else {  
             System.out.println(node.getName());  
         }  
     }  
     @Override  
     public void visit(ProcessingInstruction node) {  
         System.out.println("PI:" + node.getTarget() + " " + node.getText());  
     }  
 }
