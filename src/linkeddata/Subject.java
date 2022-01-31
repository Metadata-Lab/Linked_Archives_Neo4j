package linkeddata;

public class Subject extends Entity {

    public Subject(String label, String iri) {
        super(label, iri);
    }

    public String getType() {return "Subject";}

    public String getNodeString() {
        String cypherNode = ":Subject {label: '"+getLabel()+"', iri: '"+getIrisAsString()+"'}";
        return cypherNode;
    }

    public String getCypherCreate() {
        return "CREATE (" + getNodeString() + ")";
    }

}
