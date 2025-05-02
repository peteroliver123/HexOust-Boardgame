# JavaFX Setup + Initialize:

## Clone + Project Structure
Link GitHub account to your IntelliJ
Top left, projects tab, get from version control
Select your gitHub account and the shared repo.

Import it as Maven if prompted, but it should be recognized due to the pom.xml

## VM Options (If running any JavaFX code gives missing module errors)
Under the tab for selecting which files to run, select Edit Configurations.
Add a new configuration, select Application.
Under Build and Run, select Edit Options and select Add VM Options.
Inside the new VM options field, insert this line of code (Make sure to edit the target field)

```
--module-path "<INSERT THE LITERAL PATH TO \lib\ FOR JAVAFX" --add-modules javafx.controls,javafx.fxml
```

Save and apply.

Note: You will need to do this for any class at all that incorporates JavaFX and renders the stage.
To avoid it, create a java. or java.utils.Main class that runs the javafx class using Application.launch.

## Fat-Jar Compiling
To compile into a jar with the dependencies included, simply open the terminal and run:
```
mvn clean package
```
The pom.xml has everything needed (Compiler + Assembly plugins with configurations set) and the compiler makes use
maven shade to compile the necessary dependencies into the jar, thus reducing file size.

<span style="color:red">NOTE: Do not modify structure. Maven shade or just maven in general expects a set location for the root directories. Any modifications or unusual structure can break the compilation and there is no way to make it adjust other than to restructure.</span>