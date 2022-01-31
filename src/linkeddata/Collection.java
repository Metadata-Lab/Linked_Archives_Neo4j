package linkeddata;

import java.util.List;

public class Collection extends Entity {

    private List<Subject> subjects;
    private String date;
    private String description;
    private String identifier;
    private String type;
    private String title;


    public Collection(String label, String iri, List<Subject> subjects, String date, String desc, String id,
                      String type, String title) {
        super(label, iri);
        this.subjects = subjects;
        this.date = date;
        this.description = desc;
        this.identifier = id;
        this.type = type;
        this.title = title;
    }

    public String getType() {return "Collection";}

    public String getNodeString() {
        String typeString = "";
        if (typeString.length() != 0) typeString.substring(0, typeString.length()-2);
        String cypherNode = ":Collection {label: '"+getLabel()+"', iri: '"+getIrisAsString()+
                "', date: '"+this.date.replace("'", "`")+"', description: '"+
                this.description.replace("'", "`")+"', identifier: '"+
                this.identifier.replace("'", "`")+"', title: '"+
                this.title.replace("'", "`")+"', type: '"+type+"'}";
        return cypherNode;
    }

    public String getCypherCreate() {
        String cypher = "CREATE ("+getNodeString()+")\n";
        int countSub = 1;
        for (Subject subject : subjects) {
            cypher += "MATCH (s" + countSub + subject.getNodeString() + "), (c"+getNodeString()+ ")";
            cypher += " CREATE (c)-[:HAS_SUBJECT]->(s" + countSub + ")\n";
            countSub++;
        }
        return cypher;
    }

}

