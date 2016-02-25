package is.aiga.bordid;

/**
 * Created by Tasteless on 25.2.2016.
 */
public class RestaurantOwner extends User {

    private String restaurantId;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
