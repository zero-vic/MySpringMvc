package com.xy.springmvc.utils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.SAXParser;
import java.io.InputStream;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:解析springmvc-config.xml
 * Version:V1.0
 */
public class XmlPaser {

    public static String getBasePackage(String xml){
        //获取SAXReader对象
        SAXReader saxReader = new SAXReader();
        //获取springmvc-config.xml 得输入流
        InputStream in = XmlPaser.class.getClassLoader().getResourceAsStream(xml);
        //获取xml文档对象
        Document document = null;
        try {
            document = saxReader.read(in);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获取root节点
        Element rootElement = document.getRootElement();
        //获取元素节点
        Element componentScan = rootElement.element("component-scan");
        Attribute attribute = componentScan.attribute("base-package");
        String basePackage = attribute.getText();
        //basePackage com.xy.meeting.controller,com.xy.meeting.service
        return basePackage;

    }
}
