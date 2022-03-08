package linkeddata;

import java.util.List;
public class Person extends Entity {

    private List<String> roles;
    private List<Entity> related;
    private String wikidata_name;
    private String wikidata_id;
    private String birthdate;
    private String deathdate;
    private Facet sex;
    private List<Facet> countries;
    private List<Facet> occupations;

    Person(String label, String iri, List<String> role, List<Entity> related, String wikidata_name,
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

    void addRoles(List<String> roles) {
        for (String role : roles) {
            if (!roles.contains(role)) this.roles.add(role);
        }
    }

    void addRelated(List<Entity> items) {
        this.related.addAll(items);
    }

    public Collection relatedCollection() {
        //get collection related to items related to person
        for (Entity rel : related) {
            Collection coll = rel.relatedCollection();
            if (coll != null) return coll;
        }
        return null;
    }

    public String getNodeString() {
        StringBuilder roleString = new StringBuilder();
        for (String r : roles) { roleString.append(r).append(", "); }
        if (roleString.length() != 0) roleString.append(roleString.substring(0, roleString.length()-2));
        String cypherNode;
        //make person node based on whether or not wikidata information exists
        if (wikidata_id != null)
            cypherNode = ":Person {name: '" + getLabel() + "', iri: '" + getIrisAsString() + "', roles: '" + roleString.toString() +
                    "', wikidata_id:'" + wikidata_id + "', wikidata_name:'" + wikidata_name +
                    "', date_of_birth:'" + birthdate + "', date_of_death:'" +
                    deathdate + "'}";
        else
            cypherNode = ":Person {name: '"+getLabel()+"', iri: '"+getIrisAsString()+ "', roles: '"+roleString.toString()+ "'}";

        return cypherNode;
    }


    public String getCypherCreate() {
        StringBuilder cypher = new StringBuilder();
        cypher.append("CREATE (").append(getNodeString()).append(")\n");
        int countRel = 1;
        //match all related entities
        for (Entity rel : related) {
            if (rel == null) continue;
            cypher.append("MATCH (e").append(countRel).append(rel.getNodeString()).append("), (p").append(getNodeString())
                    .append(")").append(" CREATE (p)-[:IS_RELATED_TO]->(e").append(countRel).append(")\n");
            countRel++;
        }
        //check if person has facets, create relationships if they do
        if (countries != null) {
            int countc = 1;
            for (Facet c : countries) {
                cypher.append("MATCH (c").append(countc).append(c.getNodeString()).append("), (p").append(getNodeString()).append(")")
                        .append(" CREATE (p)-[:IS_CITITZEN_OF]->(c").append(countc).append(")\n");
                countc++;
            }
        }
        if (occupations != null) {
            int countOcc = 1;
            for (Facet occ : occupations) {
                cypher.append("MATCH (o").append(countOcc).append(occ.getNodeString()).append("), (p").append(getNodeString()).append(")")
                        .append(" CREATE (p)-[:HAS_OCCUPATION]->(o").append(countOcc).append(")\n");
                countOcc++;
            }
        }
        if (sex != null) {
            cypher.append("MATCH (s").append(sex.getNodeString()).append("), (p").append(getNodeString()).append(")")
                    .append(" CREATE (p)-[:IS_OF_SEX]->(s").append(")\n");
        }
        return cypher.toString();
    }
}