package linkeddata;

public class Main {

    public static void main(String[] args) {

        //read in data
        Parser parser = new Parser("all_owl.txt", "all_ids.txt");
        parser.parseAll();

        //output cypher
        Ontology ontology = parser.getOntology();
        ontology.printOntologyCypher();

    }
}
