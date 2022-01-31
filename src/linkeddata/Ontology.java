package linkeddata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class Ontology {

    private Map<String, Collection> collections;
    private Map<String, Item> items;
    private Map<String, Person> people;
    private Map<String, Subject> subjects;
    public Map<String, Facet> facets;

    public Ontology(){
        collections = new HashMap<>();
        items = new HashMap<>();
        people = new HashMap<>();
        subjects = new HashMap<>();
        facets = new HashMap<>();
    }

    public void addObject(String label, Collection obj) { collections.put(label, obj); }
    public void addObject(String label, Item obj) { items.put(label, obj); }
    public void addObject(String label, Person obj) { people.put(label, obj); }
    public void addObject(String label, Subject obj) { subjects.put(label, obj); }

    public void addFacet(String id, Facet obj) { facets.put(id, obj); }
    public Facet getFacet(String id) { return facets.get(id); }

    public Collection getCollection(String label) { return collections.get(label); }
    public Person getPerson(String label) { return people.get(label); }

    public boolean personExists(String label) { return (people.keySet().contains(label)); }
    public boolean subjectExists(String label) { return (subjects.keySet().contains(label)); }

    public Item getItem(String label) {
        if (items.get(label) != null) return items.get(label);
        else {
            for (String s : items.keySet()) {
                if (s.contains(label)) return items.get(s);
                else if (s.contains(label.replace("\"", "\'"))) return items.get(s);
            }
            return null;
        }
    }

    public Subject getSubject(String label) {
        return subjects.get(label);
    }

    public void printOntologyCypher() {

        try {
            FileWriter file = new FileWriter("cypher.txt");
            BufferedWriter writer = new BufferedWriter(file);
            for (Subject x : subjects.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: subjects");
            for (Collection x : collections.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: collections");
            for (Item x : items.values()) { writer.write(x.getCypherCreate()+'\n'); }
            System.out.println("done: items");
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
