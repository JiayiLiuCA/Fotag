import javax.swing.*;

public class ImageCollectionView extends JPanel implements Observer {
    private Model model;
    public ImageCollectionView(Model model) {
        this.model = model;
        model.addObserver(this);
    }

    @Override
    public void update(Object observable) {

    }
}

