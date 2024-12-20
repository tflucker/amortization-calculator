# amortization-calculator

Java project to show a sample amortization schedule for a mortgage.  Uses Java Swing for a basic GUI.  

### Future Updates
* ~~Add new field for "Additional Principal Only Payment", update logic to account for this~~
* ~~In success dialog, output how long it would take to payoff the loan~~
* ~~Add error handling, to account for invalid inputs~~
* ~~Convert console output into Excel file using Apache POI~~
* ~~Update test coverage to be greater than 80%~~
* ~~Add additional comments + javadocs~~

## Prerequisites
* Java 17
* Maven 3.9.X (should work for lower versions)

## How to Run it Locally
* clone the project locally
* Import into IDE
* Run Maven build job, `mvn clean install`
* Run the `src/main/java/com/tim/amortization/Application.java` file as a Java Application
* A file will be generated in the base directory of this project if you click the "Calculate" button.

## Example Data for GUI
* Principal - 600,000
* Interest (percentage) - 6.5
* Mortgage Length (years)- 30
* Additional Principal Payment - 1000.00
