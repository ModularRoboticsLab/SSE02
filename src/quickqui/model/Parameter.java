package quickqui.model;

import java.util.List;

/**
 * A parameter provided to describe a GUI element
 */
public class Parameter {
    /**
     * Description of the kind of a parameter
     */
    public enum Kind { TEXT, NAME; }
    /**
     * The kind of the parameter
     */
    private Parameter.Kind kind;
    /**
     * The value of the parameter 
     */
    private String value;
    /**
     * Create a new parameter
     * @param kind the kind of the parameter
     * @param value the value of the parameter
     */
    public Parameter(Parameter.Kind kind, String value) {
        this.kind = kind;
        this.value = value;
    }
    /**
     * Get the kind of the parameter
     * @return the parameter kind
     */
    public Parameter.Kind getKind() { return kind; }
    /**
     * Get the value of the parameter
     * @return the parameter value
     */
    public String getValue() { return value; }
    /**
     * From a list of parameters, get the value of the first parameter of the given kind, null otherwise
     * @param ps the list of parameters to search
     * @param k the kind of parameter wanted
     * @return the value of the parameter matching the given kind, null otherwise
     */
    public static String get(List<Parameter> ps, Parameter.Kind k) {
        for(Parameter p: ps) if(p.getKind()==k) return p.getValue();
        return null;
    }
}