package followmeapp.followme;

/**
 * Created by abed on 1/9/2015.
 */
public class Database {

     static public RouteView getRoutes(int position){
         String imageURL = "https://maps.googleapis.com/maps/api/staticmap?size=1000x500&path=weight:5%7Ccolor:blue%7Cenc:_p~iF~ps|U_ulLnnqC_mqNvxq`@_p~iF~ps|U";
         return new RouteView("Abed Apart",imageURL,"Haifa,Isreal","3.5 KM" ,"37:13 Min","Walking","15:38:25 Friday, January 8, 2015");
     }
    static public int getRoutesSize(){
        return 3;
    }

}
