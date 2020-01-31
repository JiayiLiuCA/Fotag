import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageModel {
    private List<Observer> observers;
    private String imagePath;
    private int rating;
    private String imageName;
    private String imageDate;
    private Model model;

    private Image greyStar = null;
    private Image yellowStar = null;

    public ImageModel(Model model, String path) {
        this.observers = new ArrayList<Observer>();
        this.model = model;
        this.rating = 0;
        this.imagePath = path;

    }



    public int getRating() {
        return this.rating;
    }
    public void setRating(int i) {
        this.rating = i;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getImageName() {
        return this.imageName;
    }

    public void setImageDate(String imageDate) {
        this.imageDate = imageDate;
    }
    public String getImageDate() {
        return this.imageDate;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }
    public Model getModel () {
        return this.model;
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
        //System.out.println("in");
        for (Observer observer : this.observers) {

            observer.update(this);
        }
    }
}


