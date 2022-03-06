package linkeddata;

import java.util.List;

public class Collection extends Entity {

    private List<Subject> subjects;
    private String date;
    private String description;
    private String identifier;
    private String type;
    private String title;


    Collection(String label, String iri, List<Subject> subjects, String date, String desc, String id,
                      String type, String title) {
        super(label, iri);
        this.subjects = subjects;
        this.date = date;
        this.description = desc;
        this.identifier = id;
        this.type = type;
        this.title = title;
    }

    public String getNodeString() {
        return ":Collection {label: '"+getLabel()+"', iri: '"+getIrisAsString()+
                "', date: '"+this.date.replace("'", "`")+"', description: '"+
                this.description.replace("'", "`")+"', identifier: '"+
                this.identifier.replace("'", "`")+"', title: '"+
                this.title.replace("'", "`")+"', type: '"+type+"'}";
    }

    public String getCypherCreate() {
        StringBuilder cypher = new StringBuilder();
        cypher.append("CREATE (").append(getNodeString()).append(")\n");
        int countSub = 1;
        for (Subject subject : subjects) {
            cypher.append("MATCH (s").append(countSub).append(subject.getNodeString()).append("), (c")
                    .append(getNodeString()).append(")").append(" CREATE (c)-[:HAS_SUBJECT]->(s").append(countSub).append(")\n");
            countSub++;
        }
        return cypher.toString();
    }

}

