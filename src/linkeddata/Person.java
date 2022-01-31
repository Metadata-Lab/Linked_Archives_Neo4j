package linkeddata;

import java.util.List;
public class Person extends Entity {

    List<String> roles;
    List<Entity> related;
    String wikidata_name;
    String wikidata_id;
    String birthdate;
    String deathdate;
    Facet sex;
    List<Facet> countries;
    List<Facet> occupations;

    public Person(String label, String iri, List<String> role, List<Entity> related, String wikidata_name,
                  String wikidata_id, String birthdate, String deathdate, Facet sex, List<Facet> countries,
                  List<Facet> occupations) {
        super(label, iri);
        this.roles = role;
        this.related = related;
        this.wikidata_name = wikidata_name;
        this.wikidata_id = wikidata_id;
        this.birthdate = birthdate;
        this.deathdate = deathdate;
        this.sex = sex;
        this.countries = countries;
        this.occupations = occupations;
    }

    public String getType() {return "Person";}

    public void addRoles(List<String> roles) {
        for (String role : roles) {
            if (!roles.contains(role)) this.roles.add(role);
        }
    }

    public void addRelated(List<Entity> items) {
        for (Entity i : items){
            this.related.add(i);
        }
    }

    public Collection relatedCollection() {
        for (Entity rel : related) {
            Collection coll = rel.relatedCollection();
            if (coll != null) return coll;
        }
        return null;
    }

    public String getNodeString() {
        String roleString = "";
        for (String r : roles) { roleString += r+", "; }
        if (roleString.length() != 0) roleString = roleString.substring(0, roleString.length()-2);
        String cypherNode;
        if (wikidata_id == null) {
            cypherNode = ":Person {name: '"+getLabel()+"', iri: '"+getIrisAsString()+ "', roles: '"+roleString+ "'}";
        } else {
            cypherNode = ":Person {name: '"+getLabel()+"', iri: '"+getIrisAsString()+ "', roles: '"+roleString+
                    "', date of birth:'" + birthdate + "', date of death:'" + deathdate + "'}";
        }
        return cypherNode;
    }


    public String getCypherCreate() {
        String cypher = "CREATE ("+getNodeString()+")\n";
        int countRel = 1;
        for (Entity rel : related) {
            if (rel == null) continue;
            cypher += "MATCH (e" + countRel + rel.getNodeString() + "), (p"+getNodeString()+ ")";
            cypher += " CREATE (p)-[:IS_RELATED_TO]->(e" + countRel + ")\n";
            countRel++;
        }
        if (countries != null) {
            int countc = 1;
            for (Facet c : countries) {
                cypher += "MATCH (c" + countc + c.getNodeString() + "), (p" + getNodeString() + ")";
                cypher += " CREATE (p)-[:CITITZEN_OF]->(c" + countc + ")\n";
                countc++;
            }
        }
        if (occupations != null) {
            int countOcc = 1;
            for (Facet occ : occupations) {
                cypher += "MATCH (o" + countOcc + occ.getNodeString() + "), (p"+getNodeString()+ ")";
                cypher += " CREATE (p)-[:HAS_OCCUPATION]->(o" + countOcc + ")\n";
                countOcc++;
            }
        }
        return cypher;
    }
}