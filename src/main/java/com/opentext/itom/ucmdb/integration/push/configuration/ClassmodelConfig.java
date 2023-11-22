package com.opentext.itom.ucmdb.integration.push.configuration;

import java.util.ArrayList;
import java.util.List;

public class ClassmodelConfig{
	// String : CIType name, no relation
	private List<SimpleTopology> simpleTopoList;

	public List<SimpleTopology> getSimpleTopoList() {
		if(simpleTopoList == null){
			simpleTopoList = new ArrayList<>();
		}
		return simpleTopoList;
	}

	public void setSimpleTopoList(List<SimpleTopology> simpleTopoList) {
		this.simpleTopoList = simpleTopoList;
	}

	// do not cover relation
	public ClassTypeConfiguration getAggregatedClassConfigurationByName(String classname) {
		ClassTypeConfiguration rlt = new ClassTypeConfiguration(classname);
		for(SimpleTopology simpleTopology : getSimpleTopoList()){
			simpleTopology.loopGetAttributesByName(classname, rlt);
		}
		return rlt;
	}
}