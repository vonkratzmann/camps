package au.com.mysites.camps.models;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import au.com.mysites.camps.R;
import au.com.mysites.camps.util.Constants;

import static au.com.mysites.camps.util.AppContextProvider.getContext;

/**
 * Site POJO.
 */

@IgnoreExtraProperties
public class Site {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_STREET = "street";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_POPULARITY = "numRatings";
    public static final String FIELD_AVG_RATING = "avgRating";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_CATEGORY = "category";

    private String name;
    private String street;
    private String city;
    private String state;

    private int numRatings;
    private double avgRating;
    private int price;
    private String dateCreated;

    //GPS coordinates
    private String latitude;
    private String longitude;

    //Facilities
    private boolean free;
    private boolean dumpPoint;
    private boolean mobile;
    private boolean playEquipment;
    private boolean scenic;
    private boolean showers;
    private boolean swimming;
    private boolean toilets;
    private boolean tvReception;
    private boolean waterDrinking;

    //contains file path name in Firebase storage where photo is stored
    private String sitePhoto;

    //contains file path name in Firebase storage where thumbnail is stored
    private String thumbnail;

    private ArrayList<Comment> comments;

    /*  facility types */
    public enum Facility {
        FREE, DUMPPOINT, MOBILE, PLAYEQUIPMENT, SCENIC, SHOWERS, SWIMMING, TOILETS,
        TVRECEPTION, WATER
    }

    public Site() {
        //initialise comments
        comments = new ArrayList<Comment>();
        //add a date created
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATEFORMAT);
        dateCreated = dateFormat.format(new Date());
    }

    @SuppressLint("StringFormatInvalid")
    public Site(String name, String street, String city, String state,
                String sitePhoto, String thumbnail,
                int numRatings, double avgRating, int price,
                String latitude, String longitude,
                boolean free, boolean dumpPoint, boolean mobile,
                boolean playEquipment, boolean scenic, boolean showers,
                boolean swimming, boolean toilets, boolean tvReception,
                boolean drinkingWater) {
        //initialise comments
        comments = new ArrayList<Comment>();

        this.name = name;
        this.street = street;
        this.state = state;
        this.city = city;
        this.numRatings = numRatings;
        this.avgRating = avgRating;
        this.price = price;

        //set date to today
        String format = getContext().getString(R.string.dateformat, Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Date now = new Date();
        this.dateCreated = sdf.format(now);

        //GPS coordinates
        this.latitude = latitude;
        this.longitude = longitude;

        //Facilities
        this.free = free;
        this.dumpPoint = dumpPoint;
        this.mobile = mobile;
        this.playEquipment = playEquipment;
        this.scenic = scenic;
        this.showers = showers;
        this.swimming = swimming;
        this.toilets = toilets;
        this.tvReception = tvReception;
        this.waterDrinking = drinkingWater;

        this.thumbnail = thumbnail;
        this.sitePhoto = sitePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String s) {
        dateCreated = s;
    }

    /**
     * Get the longitude of the activities as degrees:minutes:seconds string
     *
     * @return String   return longitude as degrees:minutes:seconds
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Set the longitude for the activities from deg:min:sec string
     *
     * @param longitude longitude of the activities
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Get the latitude of the activities as deg:min:sec string
     *
     * @return String   return latitude as deg:min:sec string
     */

    public String getLatitude() {
        return this.latitude;
    }

    /**
     * Set the latitude for the activities from deg:min:sec string
     *
     * @param latitude latitude to be set for the activities
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSitePhoto() {
        return sitePhoto;
    }

    public void setSitePhoto(String sitePhoto) {
        this.sitePhoto = sitePhoto;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isDumpPoint() {
        return dumpPoint;
    }

    public void setDumpPoint(boolean dumpPoint) {
        this.dumpPoint = dumpPoint;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isPlayEquipment() {
        return playEquipment;
    }

    public void setPlayEquipment(boolean playEquipment) {
        this.playEquipment = playEquipment;
    }

    public boolean isScenic() {
        return scenic;
    }

    public void setScenic(boolean scenic) {
        this.scenic = scenic;
    }

    public boolean isShowers() {
        return showers;
    }

    public void setShowers(boolean showers) {
        this.showers = showers;
    }

    public boolean isSwimming() {
        return swimming;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public boolean isToilets() {
        return toilets;
    }

    public void setToilets(boolean toilets) {
        this.toilets = toilets;
    }

    public boolean isTvReception() {
        return tvReception;
    }

    public void setTvReception(boolean tvReception) {
        this.tvReception = tvReception;
    }

    public boolean isWaterDrinking() {
        return waterDrinking;
    }

    public void setWaterDrinking(boolean waterDrinking) {
        this.waterDrinking = waterDrinking;
    }

    /* getter and setters for comments */

    @Exclude
    //Excluded from loading as a nested object, as loaded into a sub-collection in separate code
    public ArrayList<Comment> getComments() {

        return this.comments;
    }

    @Exclude
    //Excluded from setting as a nested object, as set into a sub-collection in separate code
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Set or add a single comment
     *
     * @param c a single comment
     */

    public void addComment(Comment c) {
        comments.add(c);
    }

    /**
     * Insert a single comment
     *
     * @param c         a single comment
     * @param commentNo position in the list to insert the comment
     */

    public void insertComment(int commentNo, Comment c) {
        comments.set(commentNo, c);
    }

    /**
     * Get a single comment from the ArrayList of Comments
     *
     * @param i comment to be returned
     * @return a comment
     */

    public Comment getComment(int i) {

        return comments.get(i);
    }

    /**
     * Delete a single comment from the ArrayList of Comments
     *
     * @param i comment to be deleted
     */

    /* Package Private */
    public void deleteComment(int i) {

        comments.remove(i);
    }

    /**
     * Checking if facility is present by returning true if it is and false if it is not
     * as facilities stored as boolean just return the value of the facility
     *
     * @param type The type of facility
     * @return true if the facility is present or false if the facility is not available
     */


    public boolean checkIfFacilityPresent(Facility type) {

        boolean result;

        switch (type) {
            case FREE:
                result = free;
                break;
            case DUMPPOINT:
                result = dumpPoint;
                break;
            case MOBILE:
                result = mobile;
                break;
            case PLAYEQUIPMENT:
                result = playEquipment;
                break;
            case SCENIC:
                result = scenic;
                break;
            case SHOWERS:
                result = showers;
                break;
            case SWIMMING:
                result = swimming;
                break;
            case TOILETS:
                result = toilets;
                break;
            case TVRECEPTION:
                result = tvReception;
                break;
            case WATER:
                result = waterDrinking;
                break;
            default:
                result = false;
        }
        return result;
    }


    /**
     * Set a facility to be available if the available parameter is true, or not available if the parameter is false
     *
     * @param myFacility Enum type Facility
     * @param available  specifies if the facility is available
     */
    public void setFacility(Facility myFacility, boolean available) {
        switch (myFacility) {
            case FREE:
                free = available;
                break;
            case DUMPPOINT:
                dumpPoint = available;
                break;
            case MOBILE:
                mobile = available;
                break;
            case PLAYEQUIPMENT:
                playEquipment = available;
                break;
            case SCENIC:
                scenic = available;
                break;
            case SHOWERS:
                showers = available;
                break;
            case SWIMMING:
                swimming = available;
                break;
            case TOILETS:
                toilets = available;
                break;
            case TVRECEPTION:
                tvReception = available;
                break;
            case WATER:
                waterDrinking = available;
                break;
        }
    }

}