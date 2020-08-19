package com.thyrocare.btechapp.NewScreenDesigns.Utils;

public class URLs {


//    public static String MainUrl = "https://techso.thyrocare.cloud/techsoapi/";  // Live url
    public static String MainUrl = "http://techsostng.thyrocare.cloud/techsoapi/";  // Staging url


    public  static String MainURLtoCheck = "https://techso.thyrocare.cloud/techsoapi/";

    public static final String B2C_APIs = MainUrl.equalsIgnoreCase(MainURLtoCheck) ? "https://www.thyrocare.com/APIs/" : "https://www.thyrocare.com/API_BETA/";
    public static final String B2C_API = MainUrl.equalsIgnoreCase(MainURLtoCheck) ? "https://www.thyrocare.com/API/" : "https://www.thyrocare.com/API_BETA/";
    public static final String B2B_API = MainUrl.equalsIgnoreCase(MainURLtoCheck) ? "https://www.thyrocare.com/API/B2B/" : "https://www.thyrocare.com/API_BETA/B2B/";

}
