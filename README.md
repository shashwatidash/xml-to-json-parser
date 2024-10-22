# XML To Json Parser
It is a parser/converter program which parses the xml DOM tree and by recursively traversing its child nodes, creates a corresponding JsonObject.

## Technologies Used

- **Java 8+**: Core programming language.
- **JSON-Java (org.json)**: For JSON processing.
- **DOM Parser (javax.xml.parsers)**: For XML parsing.
- **Maven**: Dependency management and project build tool.

## Getting Started
1. Clone the repository
   ```bash
   git clone https://github.com/shashwatidash/xml-to-json-parser.git
   cd xml-to-json-parser
2. Build the project using maven, then run
   ```bash
   mvn clean install

## Future Improvements
- **JUnit Testing**: Add unit and integration tests using JUnit for better coverage.
- **Custom Logging**: Add a more robust logging framework like Log4j or SLF4J for better debug logs.
- **Configurable Input/Output Paths**: Allow input and output file paths to be provided as command-line arguments.
- **Performance Optimization**: Improve performance for handling large XML files
