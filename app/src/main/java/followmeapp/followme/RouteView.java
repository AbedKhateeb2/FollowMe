package followmeapp.followme;

/**
 * Created by abed on 1/9/2015.
 */
public class RouteView {
    String name;
    String imageURL;
    String area;
    String length;
    String duration;
    String type;
    String date;
    String objectID;
    String owner;
    public RouteView(String name, String imageURL, String area, String length, String duration, String type, String date, String owner, String objectId) {
        this.name = name;
        this.imageURL = imageURL;
        this.area = area;
        this.length = length;
        this.duration = duration;
        this.type = type;
        this.date = date;
        this.owner = owner;
        this.objectID = objectId;
    }
}
