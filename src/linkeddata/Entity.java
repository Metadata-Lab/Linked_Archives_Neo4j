package linkeddata;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    protected String label = "";
    private List<String> IRI = new ArrayList<>();

    public Entity(String label, String iri) {
        this.label = label;
        addIRI(iri);
    }

    public abstract String getType();

    public abstract String getNodeString();

    public abstract String getCypherCreate();

    public String getLabel(){
        return label.replace("'", "`");
    }

    public void setLabel(String newLabel) {
        label = newLabel;
    }

    public List<String> getIRIs() {
        return IRI;
    }

    public String getIrisAsString() {
        String str = "";
        for (String s : IRI) {
            str += s.replace("'", "`") + ", ";
        }
        str = str.substring(0, str.length() - 2);
        return str;
    }

    public void setIRIs(List<String> newIRIs){
        IRI = newIRIs;
    }

    public void addIRI(String newIRI){
        IRI.add(newIRI);
    }

    public Collection relatedCollection() {return null;}

}
