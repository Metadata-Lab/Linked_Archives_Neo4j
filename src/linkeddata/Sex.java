package linkeddata;

public class Sex extends Facet {

    public Sex(String label, String qid) {
        super(label, qid);
    }

    public String getNodeString(){
        return ":Facet:Sex {id:'" + qid + "', label:'" + label + "'}";
    }

    public String getCypherCreate() {
        return "CREATE (" + getNodeString() + ")";
    }

}
