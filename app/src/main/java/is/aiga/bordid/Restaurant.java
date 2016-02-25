package is.aiga.bordid;

/**
 * Created by Tasteless on 25.2.2016.
 */
public class Restaurant {

    private String name;
    private String address;
    private String city;
    private String logo;
    private int zip;
    private int numTables;
    private RestaurantType types;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getNumTables() {
        return numTables;
    }

    public void setNumTables(int numTables) {
        this.numTables = numTables;
    }

    public RestaurantType getTypes() {
        return types;
    }

    public void setTypes(RestaurantType types) {
        this.types = types;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
