package com.thyrocare.utils.app;

import com.thyrocare.models.api.request.OrderBookingRequestModel;
import com.thyrocare.models.data.Venupunture_Temporary_ImageModel;

import java.util.ArrayList;

public class VenuPuntureUtils {

    public static void AddVenupumtureInTempGlobalArry(String encodedVanipunctureImg, int BenID, String name, String Age, String Gender, String TRFImagePath) {

        for (int i = 0; i < BundleConstants.TempVenuImageArylist.size(); i++) {
            if (BundleConstants.TempVenuImageArylist.get(i).getBenID() == BenID){
                BundleConstants.TempVenuImageArylist.remove(i);
            }
        }
            Venupunture_Temporary_ImageModel model = new Venupunture_Temporary_ImageModel();
            model.setVenupuntureBase64string(encodedVanipunctureImg);
            model.setBenID(BenID);
            model.setBenname(name);
            model.setBenAge(Age);
            model.setBenGender(Gender);
            model.setTRFImagePath(TRFImagePath);
            BundleConstants.TempVenuImageArylist.add(model);

    }

    public static void ClearVenupumtureTempGlobalArry() {
        if (BundleConstants.TempVenuImageArylist != null){
            BundleConstants.TempVenuImageArylist = null;
        }
        BundleConstants.TempVenuImageArylist = new ArrayList<>();
    }

    public static void DeleteBenFromVenupumtureTempGlobalArry(int BenID) {

        boolean benremoved = false;
        for (int i = 0; i < BundleConstants.TempVenuImageArylist.size(); i++) {
            if (BundleConstants.TempVenuImageArylist.get(i).getBenID() == BenID) {
                BundleConstants.TempVenuImageArylist.remove(BundleConstants.TempVenuImageArylist.get(i));
                benremoved = true;
                break;
            }
        }
        if (benremoved) {
            System.out.println("Ben details removed from Temporary Venupunture Arraylist");
        } else {
            System.out.println("Ben details not found in Temporary Venupunture Arraylist");
        }
    }


    public static void UpdateBenID_IN_VenupumtureTempGlobalArry(String OldBenID, String NewBenID) {

        int oldid = 0;
        int newid = 0;
        try {
            oldid = Integer.parseInt(OldBenID);
            newid = Integer.parseInt(NewBenID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean benIDUpdated = false;
        for (int i = 0; i < BundleConstants.TempVenuImageArylist.size(); i++) {
            if (BundleConstants.TempVenuImageArylist.get(i).getBenID() == oldid) {
                BundleConstants.TempVenuImageArylist.get(i).setBenID(newid);
                benIDUpdated = true;
                break;
            }
        }
        if (benIDUpdated) {
            System.out.println("Ben ID Updated Successfully in Temporary Venupunture Arraylist");
        } else {
            System.out.println("Ben details not found in Temporary Venupunture Arraylist");
        }
    }

    public static OrderBookingRequestModel ADD_ALL_VenupumturesInMainBookingRequestModel(OrderBookingRequestModel MainModel) {

        OrderBookingRequestModel finalModel = new OrderBookingRequestModel();

        if (MainModel != null) {

            for (int i = 0; i < BundleConstants.TempVenuImageArylist.size(); i++) {
                if (!InputUtils.isNull(BundleConstants.TempVenuImageArylist.get(i).getVenupuntureBase64string())) {
                    if (MainModel.getBendtl() != null && MainModel.getBendtl().size() > 0) {
                        for (int j = 0; j < MainModel.getBendtl().size(); j++) {
                            if (MainModel.getBendtl().get(j).getBenId() == BundleConstants.TempVenuImageArylist.get(i).getBenID()) {
                                MainModel.getBendtl().get(j).setVenepuncture(BundleConstants.TempVenuImageArylist.get(i).getVenupuntureBase64string());
                                break;
                            }
                        }
                    }

                    if (MainModel.getOrdbooking() != null && MainModel.getOrdbooking().getOrddtl() != null && MainModel.getOrdbooking().getOrddtl().size() > 0) {
                        for (int j = 0; j < MainModel.getOrdbooking().getOrddtl().size(); j++) {
                            boolean isVenupuntureAdded = false;
                            if (MainModel.getOrdbooking().getOrddtl().get(j).getBenMaster() != null) {
                                for (int k = 0; k < MainModel.getOrdbooking().getOrddtl().get(j).getBenMaster().size(); k++) {
                                    if (MainModel.getOrdbooking().getOrddtl().get(j).getBenMaster().get(k).getBenId() == BundleConstants.TempVenuImageArylist.get(i).getBenID()) {
                                        MainModel.getOrdbooking().getOrddtl().get(j).getBenMaster().get(k).setVenepuncture(BundleConstants.TempVenuImageArylist.get(i).getVenupuntureBase64string());
                                        isVenupuntureAdded = true;
                                        break;
                                    }
                                }
                            }
                            if (isVenupuntureAdded) {
                                break;
                            }
                        }
                    }


                    if (MainModel.getOrddtl() != null && MainModel.getOrddtl().size() > 0) {
                        for (int j = 0; j < MainModel.getOrddtl().size(); j++) {
                            boolean isVenupuntureAdded = false;
                            if (MainModel.getOrddtl().get(j).getBenMaster() != null && MainModel.getOrddtl().get(j).getBenMaster().size() > 0) {
                                for (int k = 0; k < MainModel.getOrddtl().get(j).getBenMaster().size(); k++) {
                                    if (MainModel.getOrddtl().get(j).getBenMaster().get(k).getBenId() == BundleConstants.TempVenuImageArylist.get(i).getBenID()) {
                                        MainModel.getOrddtl().get(j).getBenMaster().get(k).setVenepuncture(BundleConstants.TempVenuImageArylist.get(i).getVenupuntureBase64string());
                                        isVenupuntureAdded = true;
                                        break;
                                    }
                                }
                            }
                            if (isVenupuntureAdded) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return MainModel;
    }
}
