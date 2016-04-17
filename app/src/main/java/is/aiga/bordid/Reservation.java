package is.aiga.bordid;

/**
 * Created by Arnthor on 17.4.2016.
 */
public class Reservation {

    private String ReservationId;
    private String CustId;
    private String RestaurantId;
    private int NumSeats;
    private String ReservationDate;
    private Restaurant Restaurant;

    public is.aiga.bordid.Restaurant getRestaurant() {
        return Restaurant;
    }

    public void setRestaurant(is.aiga.bordid.Restaurant restaurant) {
        Restaurant = restaurant;
    }

    public String getReservationId() {
        return ReservationId;
    }

    public void setReservationId(String reservationId) {
        ReservationId = reservationId;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    public int getNumSeats() {
        return NumSeats;
    }

    public void setNumSeats(int numSeats) {
        NumSeats = numSeats;
    }

    public String getReservationDate() {
        return ReservationDate;
    }

    public void setReservationDate(String reservationDate) {
        ReservationDate = reservationDate;
    }
}
