package linkeddata;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    //all entities have label and iri, individual types have additional properties
    protected String label;
    private List<String> IRI = new ArrayList<>();

    Entity(String label, String iri) {
        this.label = label;
        addIRI(iri);
    }

    /**
     * create cypher for identifying object node that can be inserted to CREATE or MATCH statements
     * @return cypher statement
     *      includes node type and properties
     *      does not include parenthesis, label, or cypher command
     */
    public abstract String getNodeString();

    /**
     * create cypher for creating object node and its edges
     * @return cypher statement
     *      includes create node statement, all match node and create relation statements for edges
     */
    public abstract String getCypherCreate();

    //get label, make sure to replace apostrophes to avoid issues with strings in cypher
    public String getLabel(){ return label.replace("'", "`"); }

    public void setLabel(String newLabel) { label = newLabel; }

    /**
     * output IRIs as singular string list, replace quotes to avoid issues with cypher syntax
     * @return
     */
    String getIrisAsString() {
        StringBuilder str = new StringBuilder();
        for (String s : IRI) {
            str.append(s.replace("'", "`")).append(", ");
        }
        return str.toString().substring(0, str.length() - 2);
    }

    void addIRI(String newIRI){ IRI.add(newIRI); }

    public Collection relatedCollection() { return null; }

}
