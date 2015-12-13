package com.shsz.parsexml.main;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class StreamXmlParser {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		try(FileReader reader = new FileReader("E:/ENB.xml")){
	        XMLInputFactory factory = XMLInputFactory.newFactory();
	        factory.setProperty("javax.xml.stream.isCoalescing",Boolean.TRUE);
	        XMLEventReader xereader = factory.createXMLEventReader(reader);
	        while(xereader.hasNext()){
	            XMLEvent e = xereader.nextEvent();
	          /*  if(e.isCharacters()){
	                Characters cdata = e.asCharacters();
	                if(cdata.isWhiteSpace()) continue;
	                System.out.println(cdata.getData());
	            }*/
	        }
	        xereader.close();
	    } catch (XMLStreamException|IOException e){
	    	System.err.println(e.getMessage());
	    }
		
		System.out.println("用时:"+(System.currentTimeMillis()-startTime)+"毫秒");
		
	}
}
