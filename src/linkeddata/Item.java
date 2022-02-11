package linkeddata;

public class Item extends Entity {

    Collection isPartOf;
    String date;
    String description;
    String identifier;
    Subject subject;
    String title;
    String type;
    String mediaType;

    public Item(String label, String iri, Collection isPartOf, String date, String desc, String id,
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

    public String getType() {return "Item";}

    public Collection relatedCollection() {
        return isPartOf;
    }

    public String getNodeString() {
        String cypherNode = ":Item {label: '"+getLabel()+"', iri: '"+getIrisAsString()+
               "', date: '"+ this.date+
                "', description: '"+this.description+
                "', identifier: '"+this.identifier+
                "', title: '"+ this.title+
                "', type: '"+this.type+"', mediaType: '"+
                this.mediaType+"'}";
        return cypherNode;
    }

    public String getCypherCreate() {
        return "CREATE ("+getNodeString()+")\nMATCH (c"+isPartOf.getNodeString()+"), (s"+subject.getNodeString()+")" +
                ", (i" + getNodeString() + ") CREATE (i)-[:IS_PART_OF]->(c) CREATE (i)-[:HAS_SUBJECT]->(s)";
    }

}
