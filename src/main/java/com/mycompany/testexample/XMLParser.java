/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testexample;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author hoka
 */
public class XMLParser extends DefaultHandler{
    double summ = 0;

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals("entry")) {
            summ += Double.valueOf(atts.getValue(0));
        }
    }

    public double getSumm() {
        return summ;
    }
}
