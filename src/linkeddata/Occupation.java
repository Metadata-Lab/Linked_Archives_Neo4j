package linkeddata;

public class Occupation extends Facet {

    public Occupation(String label, String qid) {
        super(label, qid);
    }

    public String getNodeString(){
        return ":Facet:Occupation {id:'" + qid + "', label:'" + label + "'}";
    }

    public String getCypherCreate() {
        return "CREATE (" + getNodeString() + ")";
    }

}
