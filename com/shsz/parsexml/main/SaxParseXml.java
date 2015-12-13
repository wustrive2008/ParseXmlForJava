package com.shsz.parsexml.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

/**
 * 
* @ClassName: SaxParseXml 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author wustrive
* @date 2015年12月13日 下午3:55:32 
*
 */
public class SaxParseXml {
	public static void main(String[] args) throws Exception {
		 SAXParserFactory factory = SAXParserFactory.newInstance();  
	     SAXParser saxParser = factory.newSAXParser();  
	     final long startTime = System.currentTimeMillis();
	    
	     DefaultHandler handler = new DefaultHandler() {  
	    	 String tagName=null;
	    	 String nIndex = null;
	    	 String vIndex = null;
	    	 String snKey = null;
	    	 FileWriter fw = new FileWriter("F:/ENB.txt"); 
	    	 LinkedHashMap<String,String> pmNameMap = null;
	    	 List<LinkedHashMap<String,Object>> pmDataMapList = new ArrayList<LinkedHashMap<String,Object>>();
	    	 LinkedHashMap<String,Object> pmDataMap = null;
	    	 LinkedHashMap<String,String> cvMap = null;
	    	 
	    	 @Override
	    	 public void endDocument() throws SAXException {
	    		 try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		System.out.println("用时:"+(System.currentTimeMillis()-startTime)+"毫秒");
	    	 };
	    	 
	    	 public void startElement(String uri, String localName,  
                     String qName, Attributes attributes)  
                     throws SAXException {  
            	 if("PmName".equals(qName)){
            		 pmNameMap  = Maps.newLinkedHashMap();
            	 }
            	 
            	 if("N".equals(qName)){
            		 for (int i = 0; i < attributes.getLength(); i++) {  
            			 if("i".equals(attributes.getQName(i))){
            				 nIndex = attributes.getValue(i);
            			 }
                     }
            	 }
            	 
            	 if("Pm".equals(qName)){
            		 pmDataMap  = Maps.newLinkedHashMap();
            		 for (int i = 0; i < attributes.getLength(); i++) {  
            			 if("Dn".equals(attributes.getQName(i))){
            				 pmDataMap.put("Dn", attributes.getValue(i));
            			 }
            			 if("UserLabel".equals(attributes.getQName(i))){
            				 pmDataMap.put("UserLabel", attributes.getValue(i));
            			 }
                     }
            	 }
            	 
            	 if("V".equals(qName)||"CV".equals(qName)){
            		 if("CV".equals(qName)){
                		 cvMap = new MyLinkedHashMap<String,String>();
                	 }
            		 for (int i = 0; i < attributes.getLength(); i++) {  
            			 if("i".equals(attributes.getQName(i))){
            				 vIndex = attributes.getValue(i);
            			 }
                     }
            	 }
            	 
                 this.tagName=qName;  
             }  

	    	 @Override
             public void endElement(String uri, String localName, String qName) throws SAXException {  
	        	 if("CV".equals(qName)){
	        		 pmDataMap.put(pmNameMap.get(vIndex),cvMap);
	        	 }
	        	 if("V".equals(qName)){
	        		 if(null == pmDataMap.get(pmNameMap.get(vIndex))){
	        			 pmDataMap.put(pmNameMap.get(vIndex),"");
	        		 }
	        	 }
	        	 
	        	 if("Pm".equals(qName)){
	        		 pmDataMapList.add(pmDataMap);
	        		 pmDataMap = null;
	        	 }
	        	 
	        	 if("SV".equals(qName)){
	        		 if(null == cvMap.get(snKey)){
	        			 cvMap.put(snKey, "");
	        		 }
	        		 snKey = null;
	        	 }
	        	 
	        	 if("Measurements".equals(qName)){
	        		for (int i = 0; i < pmDataMapList.size(); i++) {
	        			Joiner joiner = Joiner.on("|").skipNulls(); 
	        			//写Key
	        			if(i == 0){
	        				System.out.println(joiner.join(pmDataMapList.get(0).keySet()));
	        				try {
								fw.write(joiner.join(pmDataMapList.get(i).keySet())+"\r\n");
								fw.write("\r\n");
							} catch (IOException e) {
								e.printStackTrace();
							}
	        			}
	        			//写Value
	        			try {
							fw.write(joiner.join(pmDataMapList.get(i).values())+"\r\n");
							fw.write("\r\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
	        		 try {
						fw.write("\r\n\r\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
	        		 pmDataMap = null;
	        		 pmNameMap = null;
	        		 pmDataMapList = new ArrayList<LinkedHashMap<String,Object>>();
	        		 this.vIndex = null;
	        		 this.nIndex = null;
	        	 }
	        	 
                this.tagName=null;
               
             }
             
             @Override
            public void characters(char[] ch, int start, int length)
            		throws SAXException {
            	super.characters(ch, start, length);
            	
            	String value=new String(ch,start,length); 
            	if("N".equals(tagName)){
            		pmNameMap.put(nIndex, value);
            	}
            	
            	if("V".equals(tagName)){
            		pmDataMap.put(pmNameMap.get(vIndex), value);
            	}
            	
            	if("SN".equals(tagName)){
            		snKey = value;
            	}
            	
				if("SV".equals(tagName)){
					cvMap.put(snKey, value);       		
				}
            }
	     };
	     
	     saxParser.parse("F:/ENB.xml", handler);
	     
	}
	
}

class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V>{
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
	    Set<Map.Entry<K,V>> keyset = this.entrySet();
	    Iterator<Map.Entry<K,V>> i = keyset.iterator();
	    if(!i.hasNext())
	        return "";
	    StringBuffer buffer = new StringBuffer();
	    for(;;){
		    Map.Entry<K,V> me = i.next();
		    K key= me.getKey();
		    V value= me.getValue();
		    buffer.append(key.toString()+":");
		    buffer.append(value.toString()+",");
		    if(!i.hasNext()){
		    	buffer = buffer.deleteCharAt(buffer.length()-1);
		    	return  buffer.toString();
		    }
		}
	}
	
}
