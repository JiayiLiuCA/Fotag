
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Canvas extends JPanel implements Observer {

    private Model model;
    private JScrollPane gridView;
    private JScrollPane listView;

    private JPanel gridPanel;
    private JPanel listPanel;
    private GridBagConstraints gridGBC = new GridBagConstraints();
    private GridBagConstraints listGBC = new GridBagConstraints();

    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

    private int listColumn;

    private CardLayout cardLayout;

    public Canvas (Model model) {
        this.model = model;
        model.addObserver(this);

        //this.setBackground(Color.DARK_GRAY);
        //this.setOpaque(true);

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        setGridView();

        gridView = new JScrollPane(gridPanel);
        gridView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        gridView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //gridView.getViewport().setBackground(Color.LIGHT_GRAY);
        //gridView.setLayout();
        //gridView.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


        //list layout
        listPanel = new JPanel();
        listPanel.setLayout(new GridBagLayout());
        listPanel.setBackground(Color.WHITE);
        listGBC.fill = GridBagConstraints.HORIZONTAL;
        listGBC.insets = new Insets(0,10,0,100);
        listGBC.gridy = 0;
        listGBC.weightx = 1;
        listGBC.weighty = 1;
        //listGBC.gridwidth = 10;
        //listGBC.gridheight = 1;

        listView = new JScrollPane(listPanel);
        listView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);



        this.add(gridView,"grid");
        this.add(listView,"list");

    }

    public void setGridView() {
        this.gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);

        gridGBC.fill = GridBagConstraints.NONE;
        gridGBC.weightx = 1;
        gridGBC.weighty = 1;
        gridGBC.insets = new Insets(10,10,10,10);
        gridGBC.gridx = 0;
        gridGBC.gridy = 0;


    }


    public void addImageView(ImageModel imageModel) {
        // create imageView and add it to Canvas
        ImageView imageView = new ImageView(imageModel);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imageModel.getImagePath()));
        }
        catch (IOException e) {}
        String currentLayout = this.model.getLayout();
        int width, height;
        if (currentLayout.equals("list")) {
            width = 160;
            height = 160;
            imageView.setLayout(new BoxLayout(imageView,BoxLayout.X_AXIS));
        }
        else {
            width = 90;
            height = 90;
            imageView.setLayout(new BoxLayout(imageView,BoxLayout.Y_AXIS));
            imageView.setPreferredSize(new Dimension(180,180));
        }

        Image image = img.getScaledInstance(width, height, Image.SCALE_FAST);
        imageView.setImageLabel(image);
        this.imageViews.add(imageView);
    }

    public void addViewToPanel() {
        if (this.model.getLayout().equals("list")) {
            for (ImageView imageView : imageViews) {
                this.listPanel.setPreferredSize(new Dimension(500, listColumn * 200 + 200));
                this.listPanel.add(imageView, listGBC);
                this.listPanel.revalidate();
                this.listPanel.repaint();
                listGBC.gridy++;
                listColumn++;
                //System.out.println("column: " + listColumn);
            }
        }
        else{
            //System.out.println("Size: " + this.model.getImageModels().size());
            for(int i = 0; i < this.imageViews.size(); i++) {
                int width = this.getParent().getParent().getWidth();
                //System.out.println("width : " + width);
                int attr;
                if (width == 0 || width > 600) {
                    attr = 3;
                }
                else if (width <= 600 && width > 450) {
                    attr = 2;
                }
                else {
                    attr = 1;
                }
                this.gridPanel.setPreferredSize(new Dimension(width-50, (1+(i+1)/attr)*200));
                this.gridPanel.add(imageViews.get(i), gridGBC);
                this.gridPanel.revalidate();
                this.gridPanel.repaint();
                this.gridGBC.gridx = (i+1)%attr;
                this.gridGBC.gridy = (i+1)/attr;
            /*
            if (listColumn/3 == 0) {
                gridGBC.gridx = 0;
                gridGBC.gridy ++;
            }
            else {
                gridGBC.gridx ++;
            }
            */
            }
        }
    }

    @Override
    public void update(Object observable) {

        ArrayList<ImageModel> imageModels = model.getImageModels();
        this.listGBC.gridy = 0;
        this.gridGBC.gridx = 0;
        this.gridGBC.gridy = 0;

        listColumn = 0;


        this.listPanel.removeAll();
        this.gridPanel.removeAll();

        for (ImageView imageView:imageViews) {
            imageView.stopBeingObserved();
            imageView.removeAll();
            imageView = null;
            imageViews.remove(imageView);
        }


        //imageViews.removeAll(imageViews);
        //imageViews = null;
        imageViews = new ArrayList<ImageView>();
        //System.out.println("ViewSize: " + this.imageViews.size());

        this.revalidate();
        this.repaint();


        //System.out.println("Canvas Notify");
        for (ImageModel imageModel:imageModels) {
            //System.out.println(i);
            if (this.model.getRating() == 0) {
                this.addImageView(imageModel);

            }
            else if (this.model.getRating() == imageModel.getRating()) {
                this.addImageView(imageModel);
            }
            else {
                //do nothing
            }
        }
        this.addViewToPanel();
        this.cardLayout.show(this,this.model.getLayout());
    }
}
