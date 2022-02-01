package linkeddata;

public class Country extends Facet {

    public Country(String label, String qid) {
        super(label, qid);
    }

    public String getNodeString(){
        return ":Country {id:'" + qid + "', label:'" + label + "'}";
    }

    public String getCypherCreate() {
        return "CREATE (" + getNodeString() + ")";
    }

}
