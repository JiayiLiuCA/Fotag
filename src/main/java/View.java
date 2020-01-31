

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class View extends JFrame implements Observer {

    private Model model;

    /**
     * Create a new View.
     */
    public View(Model model) {
        // Set up the window.
        this.setTitle("LULU GALLERY");
        this.setMinimumSize(new Dimension(430, 340));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                model.notifyObservers();
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                String imageData = "";
                for (ImageModel imageModel:model.getImageModels()) {
                    String path = imageModel.getImagePath();
                    String rating = Integer.toString(imageModel.getRating());
                    imageData = imageData + path + '\n' + rating + '\n';
                }
                File file = new File("save.txt");
                //System.out.println("init save.txt");
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                        //System.out.println("creating save.txt");
                    }
                    FileWriter fw = new FileWriter(file.getAbsolutePath());
                    //System.out.println("save.txt path :" + file.getAbsolutePath());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(imageData);
                    bw.close();
                }
                catch (IOException e1) {};
            }
        });

        //setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        //System.out.println("Model changed!");
    }
}
