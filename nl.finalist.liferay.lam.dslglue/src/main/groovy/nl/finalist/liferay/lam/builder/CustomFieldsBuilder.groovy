package nl.finalist.liferay.lam.builder
import nl.finalist.liferay.lam.api.CustomField
class CustomFieldsBuilder {

	CustomField customField
	
	CustomField createCustomField( Closure definition ){
        customField = new CustomField()
 
 		//System.out.println(definition)
        runClosure definition
 
        customField
    }
    
    private runClosure(Closure runClosure) {
        // Create clone of closure for threading access.
        Closure runClone = runClosure.clone()
 
        // Set delegate of closure to this builder.
        runClone.delegate = this
 
        // And only use this builder as the closure delegate.
        runClone.resolveStrategy = Closure.DELEGATE_ONLY
 
        // Run closure code.
        runClone()
    }
    
    void name( String name) {
    	customField.name = name;
    }
    
    void defaultValue( Object defaultValue) {
    	customField.defaultValue = defaultValue;
    }
    
    void value( Object value) {
    	customField.value = value;
    }
    
    void type( int type) {
    	customField.type = type;
    }
    
    void roles( String[] roles) {
    	customField.roles = roles;
    }
    
    def methodMissing(String name, arguments) {
        System.out.println("METHOD " + name + "  IS MISSING ")
    }
    

}