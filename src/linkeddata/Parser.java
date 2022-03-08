package linkeddata;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class Parser {

    //locations of data files
    private String owlFile;
    private String idFile;

    //structure where ontology data will be stored as objects
    private Ontology ontology;

    //sorted lines from ontology csv
    private List<String> collectLines = new ArrayList<>();
    private List<String> itemLines = new ArrayList<>();
    private List<String> personLines = new ArrayList<>();
    private List<String> subjectLines = new ArrayList<>();

    //map iri labels to lists initiated above
    private Map<String, List<String>> categories =  new HashMap<>();

    /**
     * CONSTRUCTOR
     * @param owlFile file path for ontology data
     * @param idFile file path for wikidata id data
     */
    Parser(String owlFile, String idFile){
        this.owlFile = owlFile;
        this.idFile = idFile;
        ontology = new Ontology();

        //match types of lines to substrings present in the object iri
        categories.put("/subject", subjectLines);
        categories.put("227.", itemLines); //belfer
        categories.put("140.", itemLines); //becker
        categories.put("185.", itemLines); //koppel
        categories.put("/person", personLines);
        categories.put("/collection", collectLines);
    }

    Ontology getOntology() { return ontology; }

    /**
     * read data files and transform data into Ontology class instance
     */
    void parseAll(){
        readIDs();
        readOwl();
        parseSubjects();
        parseCollections();
        parseItems();
        parsePeople();
    }

    /**
     * read in /t delineated file with ontology and reconciliation data
     */
    private void readOwl(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.owlFile));
            String string;
            while ((string = reader.readLine()) != null) {
                //search for key string in entity iri, add line to appropriate list
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

    /**
     * read csv with wikidata label/id dictionary
     */
    private void readIDs(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.idFile));
            String string;
            while ((string = reader.readLine()) != null) {
                String[] split = string.split(",");
                //read in type and make new facet of proper type
                switch(split[2]) {
                    case "Country":
                        ontology.addFacet(split[1], new Country(split[0], split[1]));
                        break;
                    case "Occupation":
                        ontology.addFacet(split[1], new Occupation(split[0], split[1]));
                        break;
                    case "Sex":
                        ontology.addFacet(split[1], new Sex(split[0], split[1]));
                        break;

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: idFile not found.");
        } catch (IOException e) {
            System.out.println("Could not read idFile.");
        }
    }

    /**
     * split single string into list of strings
     * @param str string with entries delimited by *
     * @return list of entries from str parameter
     */
    private List<String> divideEntries(String str){
        String[] list = str.split("\\*");
        return new ArrayList<>(Arrays.asList(list));
    }

    /**
     * transform subjects from ontology into Subject objects
     */
    private void parseSubjects(){
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

    /**
     * transform collection lines from ontology file into Collection objects
     */
    private void parseCollections(){
        for (String line : collectLines) {
            //break up line from text file
            String[] split = line.split("\t");
            String label = split[15];
            List<String> subjectStr = divideEntries(split[11]);
            //separate out subjects and connect collection to subject objects
            List<Subject> subjects = new ArrayList<>();
            for (String s : subjectStr) {
                //get named subject object or create new one if necessary
                Subject sub = ontology.getSubject(s);
                if (sub == null) {
                    Subject subject = new Subject(s, "N/A");
                    ontology.addObject(label, subject);
                    subjects.add(subject);
                } else {
                    subjects.add(sub);
                }

            }
            //create object with values from ontology text file
            Collection coll = new Collection(label, split[0], subjects, split[8], split[9],
                    split[10], split[13], split[12]);
            ontology.addObject(label, coll);
        }
    }

    /**
     * transform items from ontology into Item objects
     */
    private void parseItems(){
        for (String line : itemLines) {
            String[] split = line.split("\t");
            Item item = new Item(split[15], split[0], ontology.getCollection(split[1]), split[8],
                    split[9], split[10], ontology.getSubject(split[11]), split[12], split[13], split[14]);
            ontology.addObject(split[15], item);
        }
    }

    /**
     * get references to entity objects from a list of entity labels
     * @param itemStrs list of entity labels
     * @return list of entity objects
     */
    private List<Entity> extractEntities(List<String> itemStrs) {
        List<Entity> entities = new ArrayList<>();
        for (String label : itemStrs) {
            Entity toAdd;
            //determine if the entity is a subject or item and add appropriately
            if (ontology.subjectExists(label)) toAdd = ontology.getSubject(label);
            else toAdd = ontology.getItem(label);
            entities.add(toAdd);
        }
        return entities;
    }

    /**
     * get references to facet objects from list of wikidata ids
     * @param facetString string with wikidata ids connected by vertical pipe
     * @return list of facet objects corresponding to ids in string
     */
    private List<Facet> extractFacets(String facetString) {
        String[] facetIds = facetString.split("\\|");
        List<Facet> facets = new ArrayList<>();
        for (String id : facetIds) {
            Facet f = ontology.getFacet(id);
            //do not add if facet does not exist (if string is blank or 0)
            if (f!=null) facets.add(f);
        }
        return facets;
    }

    /**
     * transform people from ontology into Person objects
     */
    private void parsePeople(){
        for (String line : personLines) {
            //split up values from ontology spreadsheet and divide combined cell entries
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

            //check if person has wikidata match, add properties if exists
            if (split[16].equals("0")) {
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
                //only connect objects if property has a value
                if (split[21].equals("")) sex = null;
                else sex = ontology.getFacet(split[21]);
                if (split[20].equals("")) countries = null;
                else countries = extractFacets(split[20]);
                if (split.length<23) occupations = null;
                else occupations = extractFacets(split[22]);
            }

            //there are repeated people in the ontology file, so only add the person once but add additional info
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

}
