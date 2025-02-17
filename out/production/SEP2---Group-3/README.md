# JavaFX Setup + Initialize:

## Clone + Project Structure
Link GitHub account to your IntelliJ
Top left, projects tab, get from version control
Select your github account and the shared repo.

Search JavaFX online and download the zip.
Unzip it in a directory under the same drive as your IntelliJ projects.
In the menu tab (4 lines) to the left of the projects tab, select Project Structure.
Under libraries, select the + icon and select Java.
Browse for your unzipped JavaFX folder, find \lib\... and select all the .jar files.


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
