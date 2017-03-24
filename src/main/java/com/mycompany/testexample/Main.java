/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testexample;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author hoka
 */

public class Main {
    public static void main(String[] a) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        MyClass myClass = new MyClass();
        
        myClass.setN(3);
        
        myClass.addNumbs();     //add N to TABLE
       
        myClass.writeXML();     //get N from TABLE to 1.xml
        
        myClass.createXSLT();    //transform 1.xml to 2.xml
      
       System.out.println("RESULT = " + myClass.XMLParse());
    }
}
