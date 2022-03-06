package linkeddata;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    protected String label;
    private List<String> IRI = new ArrayList<>();

    Entity(String label, String iri) {
        this.label = label;
        addIRI(iri);
    }

    public abstract String getNodeString();

    public abstract String getCypherCreate();

    public String getLabel(){
        return label.replace("'", "`");
    }

    public void setLabel(String newLabel) {
        label = newLabel;
    }


    String getIrisAsString() {
        StringBuilder str = new StringBuilder();
        for (String s : IRI) {
            str.append(s.replace("'", "`")).append(", ");
        }
        return str.toString().substring(0, str.length() - 2);
    }

    void addIRI(String newIRI){
        IRI.add(newIRI);
    }

    public Collection relatedCollection() {return null;}

}
