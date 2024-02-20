package edu.usfca.cs272.templates.functional.lambdas;

public class SimpleClass implements SimpleInterface {
	@Override
	public void simpleMethod() {
		System.out.println(this.getClass().getTypeName());
	}
}
