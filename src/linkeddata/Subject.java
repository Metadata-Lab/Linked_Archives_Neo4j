package linkeddata;

public class Subject extends Entity {

    Subject(String label, String iri) {
        super(label, iri);
    }

    public String getNodeString() {
        return ":Subject {label: '"+getLabel()+"', iri: '"+getIrisAsString()+"'}";
    }

    public String getCypherCreate() {
        return "CREATE (" + getNodeString() + ")";
    }

}
