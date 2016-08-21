package com.wannabe.graven.processor;


import com.wannabe.graven.domain.DependencyList;

public interface DependencyProcessor {

	DependencyList extractDependency(String text);

}
