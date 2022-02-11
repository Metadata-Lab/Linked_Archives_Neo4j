package linkeddata;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser("all_owl.txt", "all_ids.txt");
        parser.parseAll();

        Ontology ontology = parser.getOntology();
        ontology.printOntologyCypher();

    }
}
