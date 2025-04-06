# JavaFX Setup + Initialize:

## Clone + Project Structure
Link GitHub account to your IntelliJ
Top left, projects tab, get from version control
Select your github account and the shared repo.

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
To avoid it, create a utils. or utils.Main class that runs the javafx class using Application.launch.

## Fat-Jar Compiling
To compile into a jar with the dependencies included, simply open the terminal and run:
```
mvn package
```
The pom.xml has everything needed (Compiler + Assembly plugins with configurations set)