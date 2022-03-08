package linkeddata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;


class Ontology {

    //map label of object to object by type
    private Map<String, Collection> collections;
    private Map<String, Item> items;
    private Map<String, Person> people;
    private Map<String, Subject> subjects;
    private Map<String, Facet> facets;

    Ontology(){
        collections = new HashMap<>();
        items = new HashMap<>();
        people = new HashMap<>();
        subjects = new HashMap<>();
        facets = new HashMap<>();
    }

    //add objects to appropriate map based on type (types that extend Entity)
    void addObject(String label, Collection obj) { collections.put(label, obj); }
    void addObject(String label, Item obj) { items.put(label, obj); }
    void addObject(String label, Person obj) { people.put(label, obj); }
    void addObject(String label, Subject obj) { subjects.put(label, obj); }

    //add Facet type objects (all in same map)
    void addFacet(String id, Facet obj) { facets.put(id, obj); }

    //get objects by wiki id or label
    Facet getFacet(String id) { return facets.get(id); }
    Collection getCollection(String label) { return collections.get(label); }
    Person getPerson(String label) { return people.get(label); }
    Subject getSubject(String label) {
        return subjects.get(label);
    }

    //check if object already exists in ontology
    boolean personExists(String label) { return (people.keySet().contains(label)); }
    boolean subjectExists(String label) { return (subjects.keySet().contains(label)); }

    /**
     * get Item based on label
     * @param label label of item
     * @return Item object if exist, null if not
     */
    Item getItem(String label) {
        if (items.get(label) != null) return items.get(label);
        else {
            //account for slight variations in label
            for (String s : items.keySet()) {
                if (s.contains(label)) return items.get(s);
                else if (s.contains(label.replace("\"", "\'"))) return items.get(s);
            }
            return null;
        }
    }

    /**
     * write all cypher statements to create ontology in neo4j to text file
     */
    void printOntologyCypher() {

        try {
            FileWriter file = new FileWriter("cypher.txt");
            BufferedWriter writer = new BufferedWriter(file);
            for (Subject x : subjects.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: subjects");
            for (Collection x : collections.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: collections");
            for (Item x : items.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: items");
            for (Facet x : facets.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: facets");
            for (Person x : people.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: people");

            writer.close();
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
