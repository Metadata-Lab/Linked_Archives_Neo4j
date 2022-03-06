package linkeddata;

public abstract class Facet {

    protected String label;
    String qid;

    Facet(String label, String qid) {
        this.label = label;
        this.qid = qid;
    }

    public abstract String getNodeString();

    public abstract String getCypherCreate();
}
