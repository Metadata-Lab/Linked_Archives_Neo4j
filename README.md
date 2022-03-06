# Linked_Archives_Neo4j

## File Guide

### Code

Comments to be added in individual files for more details!

`main.java` - runs the process of transforming ontology and reconciliation into Cypher statements.

`parser.java` - reads data text files and translates into ontology java object structure. 

`insert_cypher.py` - connects to Neo4j database and runs Cypher statements output by `main.java`.

#### Data Structures

`ontology.java` - container object for all metadata.

`entity.java` - abstract class for ontology objects (collection, item, person, subject) 

`facet.java` - abstract class for wikidata objects (country, occupation, sex).

### Data

`all_owl.xlsx` - workbook with all ontology and reconciliation data

`all_owl.txt` - the metadata to read into Java (1st sheet of .xlsx file)

`all_ids` - wikidata object ids and labels to read into java (3rd sheet of .xlsx file)

`cypher.txt` - Cypher queries/statements for inserting Belfer collection into Neo4j. 
Note: complete file (all three collections) too large to upload raw file to github.

`cypher.txt.zip` - compressed cypher.txt including all three collections.

`linked_archives.dump` - dump file for the final Neo4j database with metadata and wikidata facets for all three collections. 

