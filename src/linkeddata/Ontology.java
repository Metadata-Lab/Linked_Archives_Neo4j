package linkeddata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;


class Ontology {

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

    void addObject(String label, Collection obj) { collections.put(label, obj); }
    void addObject(String label, Item obj) { items.put(label, obj); }
    void addObject(String label, Person obj) { people.put(label, obj); }
    void addObject(String label, Subject obj) { subjects.put(label, obj); }

    void addFacet(String id, Facet obj) { facets.put(id, obj); }
    Facet getFacet(String id) { return facets.get(id); }

    Collection getCollection(String label) { return collections.get(label); }
    Person getPerson(String label) { return people.get(label); }

    boolean personExists(String label) { return (people.keySet().contains(label)); }
    boolean subjectExists(String label) { return (subjects.keySet().contains(label)); }

    Item getItem(String label) {
        if (items.get(label) != null) return items.get(label);
        else {
            for (String s : items.keySet()) {
                if (s.contains(label)) return items.get(s);
                else if (s.contains(label.replace("\"", "\'"))) return items.get(s);
            }
            return null;
        }
    }

    Subject getSubject(String label) {
        return subjects.get(label);
    }

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
