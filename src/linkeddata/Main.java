package linkeddata;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser("belfer_owl.txt", "belfer_ids.txt");
        parser.parseAll();

        Ontology ontology = parser.getOntology();
        ontology.printOntologyCypher();

    }
}
