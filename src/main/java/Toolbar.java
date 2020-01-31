
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Toolbar extends JPanel implements Observer {

    private Model model;

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JLabel labelFotag;

    private JPanel rating;

    private JButton gridMode;
    private JButton listMode;
    private JButton upload;
    private JButton reset;


    private static Image gridModeIcon = null;
    private static Image listModeIcon = null;
    private static Image uploadIcon = null;
    private static Image resetIcon = null;
    private static Image yellowStar = null;
    private static Image greyStar = null;

    public ArrayList<star> stars = new ArrayList<star>();


    public Toolbar(Model model) {
        LoadImage();
        this.model = model;
        model.addObserver(this);

        this.setBackground(Color.GRAY);
        this.setOpaque(true);
        this.setLayout(new BorderLayout());
        //this.setPreferredSize(new Dimension(800,100));

        //label
        labelFotag = new JLabel("Fotag!");
        labelFotag.setFont(new Font("Comic Sans MS",Font.BOLD,35));
        labelFotag.setHorizontalAlignment(JLabel.CENTER);

        //leftPanel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.X_AXIS));
        leftPanel.setBackground(Color.GRAY);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        gridMode = new JButton(new ImageIcon(gridModeIcon));
        gridMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //System.out.println("---------");
                //System.out.println("click grid");
                //System.out.println("---------");
                model.changeToGridView();
            }
        });

        listMode = new JButton(new ImageIcon(listModeIcon));
        listMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //System.out.println("---------");
                //System.out.println("click list");
                //System.out.println("---------");
                model.changeToListView();
            }
        });

        leftPanel.add(gridMode);
        leftPanel.add(listMode);

        //eastPanel
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.X_AXIS));
        rightPanel.setBackground(Color.GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        upload = new JButton(new ImageIcon(uploadIcon));
        upload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                int result = chooser.showOpenDialog(null);
                File files[] = null;
                if (result == JFileChooser.APPROVE_OPTION) {
                    files = chooser.getSelectedFiles();
                }
                else {
                    return;
                }
                if (files.length == 0) {
                    return;
                }
                for (File file:files) {

                    ImageModel imageModel = new ImageModel(model,file.getAbsolutePath());
                    imageModel.setImageName(file.getName());
                    try {
                        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                        imageModel.setImageDate(attr.creationTime().toString());
                    }
                    catch (Exception e1) {};

                    //System.out.println(imageModel.getImagePath());
                    model.addImageModel(imageModel);

                }
                model.notifyObservers(); //added models, call updates
            }
        });

        reset = new JButton(new ImageIcon(resetIcon));
        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                drawStars(0); //reset rating
                model.setRating(0);
            }
        });

        rating = new JPanel();
        rating.setBackground(Color.GRAY);
        rating.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        for (int i = 0; i < 5; i++) {
            star s = new star(i + 1);
            rating.add(s);
            stars.add(s);
        }

        rightPanel.add(rating);
        rightPanel.add(reset);
        rightPanel.add(upload);

        this.add(labelFotag,BorderLayout.CENTER);
        this.add(leftPanel,BorderLayout.WEST);
        this.add(rightPanel,BorderLayout.EAST);

    }

    private void LoadImage() {
        BufferedImage img = null;
        if (gridModeIcon == null) {
            try {
                img = ImageIO.read(new File("src/main/java/gridModeIcon.png"));
            }
            catch (IOException e) {
                //System.out.println("gridModeIcon.png does not exist");
            }
            gridModeIcon = img.getScaledInstance(35, 37, Image.SCALE_SMOOTH);

        }
        if (listModeIcon == null) {
            try {
                img = ImageIO.read(new File("src/main/java/listModeIcon.png"));
            }
            catch (IOException e) {}
            listModeIcon = img.getScaledInstance(35,37,Image.SCALE_SMOOTH);
        }
        if (uploadIcon == null) {
            try {
                img = ImageIO.read(new File("src/main/java/uploadIcon.png"));
            }
            catch (IOException e) {}
            uploadIcon = img.getScaledInstance(35,37,Image.SCALE_SMOOTH);
        }
        if (resetIcon == null) {
            try {
                img = ImageIO.read(new File("src/main/java/resetIcon.png"));
            }
            catch (IOException e) {}
            resetIcon = img.getScaledInstance(35,37,Image.SCALE_SMOOTH);
        }
        if (greyStar == null) {
            try {
                img = ImageIO.read(new File("src/main/java/greyStar.png"));
            }
            catch (IOException e) {}
            greyStar = img.getScaledInstance(15,15,Image.SCALE_SMOOTH);
        }
        if (yellowStar == null) {
            try {
                img = ImageIO.read(new File("src/main/java/yellowStar.png"));
            }
            catch (IOException e) {}
            yellowStar = img.getScaledInstance(15,15,Image.SCALE_SMOOTH);
        }
    }

    private class star extends JLabel {
        private int index;
        star(int i) {
            this.index = i;
            this.setBackground(Color.GRAY);
            this.setIcon(new ImageIcon(greyStar));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    drawStars(index); //MVC?----------
                    model.setRating(index);
                    model.notifyObservers();
                }

            });
        }
    }


    public void drawStars (int index) {
        model.setRating(index);
        for (int i = 0; i < 5; i++) {
            if (i < index) {
                stars.get(i).setIcon(new ImageIcon(yellowStar));
            }
            else {
                stars.get(i).setIcon(new ImageIcon(greyStar));
            }
        }
    }

    @Override
    public void update(Object observable) {

    }
}
