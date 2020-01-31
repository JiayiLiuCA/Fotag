
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private ArrayList<ImageModel> imageModels;
    private String layout;
    private int rating;
    private int numImage;


    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList<Observer>();
        this.imageModels =  new ArrayList<ImageModel>();
        this.layout = "grid"; //grid view;
        this.rating = 0;
        this.numImage = 0;

        File f = new File("save.txt");
        if (f.exists()) {
            try {
                Scanner input = new Scanner(f);
                while (input.hasNextLine()) {
                    String imagePath = input.nextLine();
                    String imageRating = input.nextLine();

                    File file = new File(imagePath);

                    ImageModel imageModel = new ImageModel(this,imagePath);
                    imageModel.setRating(Integer.valueOf(imageRating));
                    imageModel.setImageName(file.getName());

                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    imageModel.setImageDate(attr.creationTime().toString());
                    this.addImageModel(imageModel);
                }

            }
            catch (IOException e) {}
        }

    }

    public String  getLayout() {
        return this.layout;
    }

    public void changeToGridView() {
        //System.out.println("ToGrid");
        System.out.println(layout);
        if (this.layout.equals("list")) {
            this.layout = "grid";
            notifyObservers();
        }
    }

    public void changeToListView() {
        //System.out.println("ToList");
        System.out.println(layout);
        if (this.layout.equals("grid")) {
            this.layout = "list";
            notifyObservers();
        }
    }

    public void setRating(int i) {
        this.rating = i;
        notifyObservers();
    }
    public int getRating() {
        return rating;
    }

    public void addImageModel(ImageModel imageModel) {
        this.imageModels.add(imageModel);
        //notifyObservers();
    }

    public ArrayList<ImageModel> getImageModels() {
        return this.imageModels;
    }

    public void setNumImage(int numImage) {
        this.numImage = numImage;
    }
    public int getNumImage() {
        return this.numImage;
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
        for (ImageModel imageModel: this.imageModels) {
            imageModel.notifyObservers();
        }
    }
}
