package org.marceloleite.mercado.xml.converters;

import java.util.List;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.xml.structures.XmlStrategy;

public class StrategyDataXmlConverter implements XmlConverter<XmlStrategy, StrategyData>{

	@Override
	public XmlStrategy convertToXml(StrategyData strategyData) {
		XmlStrategy xmlStrategy = new XmlStrategy();
		List<ClassData> classDatas = strategyData.getClassDatas();
		xmlStrategy.setXmlClasses(new ListClassXmlConverter().convertToXml(classDatas));
		xmlStrategy.setCurrency(strategyData.getCurrency());
		return xmlStrategy;
	}

	@Override
	public StrategyData convertToObject(XmlStrategy xmlStrategy) {
		StrategyData strategyData = new StrategyData();
		
		strategyData.setCurrency(xmlStrategy.getCurrency());
		List<ClassData> classDatas = new ListClassXmlConverter().convertToObject(xmlStrategy.getXmlClasses());
		classDatas.forEach(classData -> classData.setStrategyData(strategyData));
		strategyData.setClassDatas(classDatas);
		return strategyData;
	}

}
