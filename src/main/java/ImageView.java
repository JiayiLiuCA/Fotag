
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageView extends JPanel implements Observer {
    private Model model;
    private ImageModel imageModel;
    private JLabel imageLabel;
    private ArrayList<star> stars = new ArrayList<star>();

    private JLabel imageName;
    private JLabel imageDate;
    private JPanel imageMetaData;
    private JPanel imageRating;

    private Image greyStar = null;
    private Image yellowStar = null;

    public ImageView(ImageModel imageModel) {
        this.model = imageModel.getModel();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.imageModel = imageModel;
        imageModel.addObserver(this);
        LoadImage();
    }
    public void stopBeingObserved() {
        this.imageModel.removeObserver(this);
    }

    public void setImageLabel(Image image) {
        this.imageLabel = new JLabel();
        this.imageLabel.setIcon(new ImageIcon(image));
        this.imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JLabel pic = new JLabel();
                pic.setIcon(new ImageIcon(image.getScaledInstance(500,500,Image.SCALE_SMOOTH)));
                JDialog dialog = new JDialog();
                dialog.setUndecorated(true);
                dialog.add(pic);
                dialog.pack();
                dialog.setVisible(true);
                dialog.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        dialog.dispose();
                    }
                });
            }
        });
        this.add(imageLabel);


        imageMetaData = new JPanel();
        imageMetaData.setLayout(new BoxLayout(imageMetaData,BoxLayout.Y_AXIS));


        imageRating = new JPanel();
        imageRating.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        for (int i = 0; i < 5; i++) {
            star s = new star(i + 1);
            imageRating.add(s);
            stars.add(s);
        }

        imageDate = new JLabel(imageModel.getImageDate());
        imageName = new JLabel(imageModel.getImageName());
        imageName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                imageModel.setRating(0);
                imageModel.getModel().notifyObservers();
                //System.out.println("set rating to 0");
            }
        });

        imageMetaData.add(imageRating);
        imageMetaData.add(imageDate);
        imageMetaData.add(imageName);
        this.add(imageMetaData);

    }



    private class star extends JLabel {
        private int index;
        star(int i) {
            this.index = i;
            this.setBackground(Color.GRAY);
            if (i > imageModel.getRating()) {
                this.setIcon(new ImageIcon(greyStar));
            }
            else {
                this.setIcon(new ImageIcon(yellowStar));
            }
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    drawStars(index);
                    imageModel.setRating(index);
                    model.notifyObservers();

                }

            });
        }
    }


    public void drawStars (int index) {
        imageModel.setRating(index);
        for (int i = 0; i < 5; i++) {
            if (i < index) {
                stars.get(i).setIcon(new ImageIcon(yellowStar));
            }
            else {
                stars.get(i).setIcon(new ImageIcon(greyStar));
            }

        }
    }

    private void LoadImage() {
        BufferedImage img = null;

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


    @Override
    public void update(Object observable) {

    }
}
