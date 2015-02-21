package fr.esir.nsoc.tsen.core;

public class Universe {
	
	private ADE_Scope scope;
	



	public Universe(ADE_Scope scope) {
		super();
		this.scope = scope;
	}

	public Universe() {
		super();
		this.scope = null;
	}


	public ADE_Scope getScope() {
		return scope;
	}


	public void setScope(ADE_Scope scope) {
		this.scope = scope;
	}
	
	
	
	
	

}
