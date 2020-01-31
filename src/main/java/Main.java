import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        Canvas canvas = new Canvas(model);
        Toolbar toolbar = new Toolbar(model);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(canvas,BorderLayout.CENTER);
        panel.add(toolbar,BorderLayout.NORTH);

        //On the screen
        view.add(panel);
        view.setVisible(true);



    }
}
