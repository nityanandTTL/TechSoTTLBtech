package com.thyrocare.models.api.request;

import java.util.ArrayList;

/**
 * Created by e5233@thyrocare.com on 23/5/18.
 */

public class UpdateMaterial {

    public String BtechId;
    public ArrayList<AllMaterialStock> allMaterialStock;

    public static class AllMaterialStock {

        public String materialID;
        public String virtualStock;
        public String actualStock;
        public String remarks;
    }
}
