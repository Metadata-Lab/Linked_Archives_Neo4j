package linkeddata;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Parser {

    String owlFile;
    String idFile;
    Ontology ontology;

    List<String> collectLines = new ArrayList<>();
    List<String> itemLines = new ArrayList<>();
    List<String> personLines = new ArrayList<>();
    List<String> subjectLines = new ArrayList<>();

    Map<String, List<String>> categories =  new HashMap<>();

    public Parser(String owlFile, String idFile){
        this.owlFile = owlFile;
        this.idFile = idFile;
        ontology = new Ontology();

        categories.put("/subject", subjectLines);
        categories.put("227.", itemLines); //belfer
        categories.put("140.", itemLines); //becker
        categories.put("185.", itemLines); //koppel
        categories.put("/person", personLines);
        categories.put("/collection", collectLines);
    }

    public Ontology getOntology() { return ontology; }

    public void readOwl(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.owlFile));
            String string;
            while ((string = reader.readLine()) != null) {
                int tab = string.indexOf('\t');
                String splice = string.substring(0, tab);

                for (String s : categories.keySet()) {
                    if (splice.contains(s)) {
                        categories.get(s).add(string.replace("\"", ""));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: owlFile not found.");
        } catch (IOException e) {
            System.out.println("Could not read owlFile.");
        }
    }

    public void readIDs(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.idFile));
            String string;
            while ((string = reader.readLine()) != null) {
                String[] split = string.split(",");
                if (split[2].equals("Country")) {
                    Country c = new Country(split[1], split[0]);
                    ontology.addFacet(split[0], c);
                }
                else if (split[2].equals("Occupation")) {
                    Occupation o = new Occupation(split[1], split[0]);
                    ontology.addFacet(split[0], o);
                }
                else if (split[2].equals("Sex")) {
                    Sex s = new Sex(split[1], split[0]);
                    ontology.addFacet(split[0], s);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: idFile not found.");
        } catch (IOException e) {
            System.out.println("Could not read idFile.");
        }
    }

    public void parseAll(){
        readIDs();
        readOwl();
        parseSubjects();
        parseCollections();
        parseItems();
        parsePeople();
    }

    /*
     Property           Index
     "IRI"              0
     "is_part_of"       1
     "is_related_to"    2
     "name"             3
     "firstname"        4
     "lastname"         5
     "middleinitial"    6
     "role"             7
     "date"             8
     "description"      9
     "identifier"       10
     "subject"          11
     "title"            12
     "type"             13
     "mediaType"        14
     "label"            15
     */

    List<String> divideEntries(String str){
        String[] list = str.split("\\*");
        List<String> items = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String item = list[i];
            items.add(item);

        }
        return items;
    }

    public void parseCollections(){
        for (String line : collectLines) {
            String[] split = line.split("\t");
            String label = split[15];
            List<String> subjectStr = divideEntries(split[11]);
            List<Subject> subjects = new ArrayList<>();
            for (String s : subjectStr) {
                Subject sub = ontology.getSubject(s);
                if (sub == null) {
                    Subject subject = new Subject(s, "N/A");
                    ontology.addObject(label, subject);
                    subjects.add(subject);
                } else {
                    subjects.add(sub);
                }

            }
            Collection coll = new Collection(label, split[0], subjects, split[8], split[9],
                    split[10], split[13], split[12]);
            ontology.addObject(label, coll);
        }
    }

    public void parseItems(){
        for (String line : itemLines) {
            String[] split = line.split("\t");
            Item item = new Item(split[15], split[0], ontology.getCollection(split[1]), split[8],
                    split[9], split[10], ontology.getSubject(split[11]), split[12], split[13], split[14]);
            ontology.addObject(split[15], item);
        }
    }

    List<Entity> extractEntities(List<String> itemStrs) {
        List<Entity> entities = new ArrayList<>();
        for (String label : itemStrs) {
            Entity toAdd;
            if (ontology.subjectExists(label)) toAdd = ontology.getSubject(label);
            else toAdd = ontology.getItem(label);
            entities.add(toAdd);
        }
        return entities;
    }

    List<Facet> extractFacets(String facetString) {
        String[] facetIds = facetString.split("|");
        List<Facet> facets = new ArrayList<>();
        for (String id : facetIds) {
            Facet f = ontology.getFacet(id);
            if (f!=null) facets.add(f);
        }
        return facets;
    }

    public void parsePeople(){
        for (String line : personLines) {
            String[] split = line.split("\t");
            String label = split[3];
            List<String> roles = divideEntries(split[7]);
            List<String> relStr = divideEntries(split[2]);
            List<Entity> entities = extractEntities(relStr);

            Facet sex;
            List<Facet> countries;
            List<Facet> occupations;
            String wikiname;
            String wikiid;
            String birthdate;
            String deathdate;

            if (split[16].equals("0.0")) {
                wikiname = null;
                wikiid = null;
                birthdate = null;
                deathdate = null;
                sex = null;
                countries = null;
                occupations = null;
            } else {
                wikiname =  split[16];
                wikiid = split[17];
                birthdate = split[18];
                deathdate = split[19];
                if (split[21].equals("")) sex = null;
                else sex = ontology.getFacet(split[21]);
                if (split[20].equals("")) countries = null;
                else countries = extractFacets(split[20]);
                if (split.length<23) occupations = null;
                else occupations = extractFacets(split[22]);
            }

            if (ontology.personExists(label)) {
                Person person = ontology.getPerson(label);
                person.addIRI(split[0]);
                person.addRoles(roles);
                person.addRelated(entities);
            } else {
                Person person = new Person(label, split[0], roles, entities, wikiname, wikiid, birthdate,
                        deathdate, sex, countries, occupations);
                ontology.addObject(label, person);
            }
        }
    }

    public void parseSubjects(){
        for (String line : subjectLines) {
            String[] split = line.split("\t");
            String label = split[0].substring(split[0].lastIndexOf("/")+1);
            if (ontology.subjectExists(label)) {
                Subject subject = ontology.getSubject(label);
                subject.addIRI(split[0]);
            } else {
                Subject subject = new Subject(label, split[0]);
                ontology.addObject(label, subject);
            }
        }
    }
}
