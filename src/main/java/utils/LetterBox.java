package utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class LetterBox {
    public Scene scene(final Parent elements, final double width, final double height) {
        Group scalableGroup = new Group(elements); // Allows all the content inside of this group to scale dynamically.
        StackPane wrapper = new StackPane(scalableGroup); // Centers the scalable content inside the window.
        Scene scene = new Scene(wrapper);
        scene.setRoot(wrapper);

        /*Binds the group to the minimum dimension of the current scene such that the elements are always perfectly visible*/
        DoubleBinding scale = Bindings.createDoubleBinding(() ->
                Math.min(scene.getWidth() / width, scene.getHeight() / height),
                scene.widthProperty(),
                scene.heightProperty());

        scalableGroup.scaleXProperty().bind(scale);
        scalableGroup.scaleYProperty().bind(scale);

        return scene;
    }
}
