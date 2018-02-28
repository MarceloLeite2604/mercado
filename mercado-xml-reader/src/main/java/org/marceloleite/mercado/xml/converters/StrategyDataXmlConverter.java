package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.xml.structures.XmlStrategy;

public class StrategyDataXmlConverter implements XmlConverter<XmlStrategy, StrategyData>{

	@Override
	public XmlStrategy convertToXml(StrategyData strategyData) {
		XmlStrategy xmlStrategy = new XmlStrategy();
		List<ClassData> strategyClassDatas = strategyData.getStrategyClassDatas();
		List<String> classNames = new ArrayList<>();
		for (ClassData strategyClassData : strategyClassDatas) {
			classNames.add(strategyClassData.getClassName());
		}
		xmlStrategy.setClassNames(classNames);
		xmlStrategy.setCurrency(strategyData.getCurrency());
		return xmlStrategy;
	}

	@Override
	public StrategyData convertToObject(XmlStrategy xmlStrategy) {
		StrategyData strategyData = new StrategyData();
		List<ClassData> strategyClassDatas = new ArrayList<>();
		List<String> classNames = xmlStrategy.getClassNames();
		for (String className : classNames) {
			ClassData strategyClassData = new ClassData();
			strategyClassData.setClassName(className);
			strategyClassDatas.add(strategyClassData);
		}
		strategyData.setStrategyClassDatas(strategyClassDatas);
		strategyData.setCurrency(xmlStrategy.getCurrency());
		return strategyData;
	}

}
