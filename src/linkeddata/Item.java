package linkeddata;

public class Item extends Entity {

    private Collection isPartOf;
    private String date;
    private String description;
    private String identifier;
    private Subject subject;
    private String title;
    private String type;
    private String mediaType;

    Item(String label, String iri, Collection isPartOf, String date, String desc, String id,
                Subject subject, String title, String type, String mediaType) {
        super(label, iri);
        this.isPartOf = isPartOf;
        this.date = date;
        this.description = desc;
        this.identifier = id;
        this.subject = subject;
        this.title = title;
        this.type = type;
        this.mediaType = mediaType;
    }

    public Collection relatedCollection() {
        return isPartOf;
    }

    public String getNodeString() {
        return ":Item {label: '"+getLabel()+"', iri: '"+getIrisAsString()+
                "', date: '"+ this.date+
                "', description: '"+this.description+
                "', identifier: '"+this.identifier+
                "', title: '"+ this.title+
                "', type: '"+this.type+"', mediaType: '"+
                this.mediaType+"'}";
    }

    public String getCypherCreate() {
        return "CREATE ("+getNodeString()+")\nMATCH (c"+isPartOf.getNodeString()+"), (s"+subject.getNodeString()+")" +
                ", (i" + getNodeString() + ") CREATE (i)-[:IS_PART_OF]->(c) CREATE (i)-[:HAS_SUBJECT]->(s)";
    }

}
