/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testexample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author hoka
 */
public class MyClass {
    private static Connection conn = null;
    private int n;
    private Statement statement;
    private DocumentBuilder builder;
    
    public MyClass() {
        try {
            this.conn = DriverManager.getConnection("jdbc:h2:./test", "sa", "");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void setN(int n) {
        this.n = n;
        System.out.println("N = " + n);
    }
    
    public void addNumbs() {
        if (conn != null) {
            try {
                statement = conn.createStatement();
                statement.getConnection().setAutoCommit(false);
                statement.execute("create table if not exists TEST (field int);");
                statement.execute("TRUNCATE TABLE TEST");
                String query;
                int batchSize = Integer.parseInt("10000");
                for (int i = 1; i <= n; i++) {
                    query = "insert into TEST values (" + Integer.toString(i) + ")";
                    statement.addBatch(query);
                    if (i % batchSize == 0) {
                        statement.executeBatch();
                    }
                }
                statement.executeBatch();
                statement.execute("commit");
            } catch (Exception ex) {
                System.out.println("error for insert " + ex.getMessage());
            }
        } else {
            System.out.println("no connecton to data base");
        } 
    }
    
    public void writeXML() throws TransformerException, IOException {
        ResultSet resultSet = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            System.out.println("newDocumentBuilder error " + ex.getMessage());
        }

        Document doc = builder.newDocument();
        Element RootElement = doc.createElement("entries");
        Element entry, field;

        resultSet = getValues();
        try {
            if (resultSet != null) {
                while (resultSet.next()) {
                    entry = doc.createElement("entry");
                    RootElement.appendChild(entry);
                    field = doc.createElement("field");
                    field.appendChild(doc.createTextNode(resultSet.getString("field")));
                    entry.appendChild(field);
                }
                statement.execute("shutdown defrag");
                statement.close();
                conn.close();
                
            } else {
                System.out.println("table TEST is empty");
            }
        } catch (SQLException | DOMException ex) {
            System.out.println("error for result set " + ex.getMessage());
            ex.printStackTrace();
        }
        doc.appendChild(RootElement);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("1.xml")));

    }

    private ResultSet getValues() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("Select * FROM TEST");
            return preparedStatement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("error for select " + ex.getMessage());
        }
        return null;
    }

    public void createXSLT() throws TransformerConfigurationException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xsltSource = new StreamSource("XSLTCreater.xsl");
        Transformer transformer = factory.newTransformer(xsltSource);
        Source textSource = new StreamSource("1.xml");
        transformer.transform(textSource, new StreamResult("2.xml"));
    }
    
    public double XMLParse() throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLParser xmlParser = new XMLParser();
        parser.parse("2.xml", xmlParser);
        return xmlParser.getSumm();
    }
    
}
