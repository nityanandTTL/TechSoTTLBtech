package com.dhb.dao.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.data.KitsCountModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.InputUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Orion on 24/11/15.
 */
public class OrderDetailsDao {

    private SQLiteDatabase db;


    // DB TABLE NAME

    private String TABLE_NAME = "order_details";


    // DB TABLE COLUMN INFO

    private String ORDER_NO = "OrderNo";
    private String VISIT_ID = "VisitId";
    private String APPOINTMENT_DATE = "AppointmentDate";
    private String SLOT = "Slot";
    private String RESPONSE = "Response";
    private String BRAND_ID = "BrandId";
    private String SLOT_ID = "SlotId";
    private String ADDRESS = "Address";
    private String PINCODE = "Pincode";
    private String MOBILE = "Mobile";
    private String EMAIL = "Email";
    private String PAY_TYPE = "PayType";
    private String AMOUNT_DUE = "AmountDue";
    private String AMOUNT_PAYABLE = "AmountPayable";
    private String MARGIN = "Margin";
    private String DISCOUNT = "Discount";
    private String REFCODE = "Refcode";
    private String PROJ_ID = "ProjId";
    private String REPORT_HC = "ReportHC";
    private String KITS = "kits";
    private String DISTANCE = "Distance";
    private String EST_INCOME = "EstIncome";
    private String LATITUDE = "Latitude";
    private String LONGITUDE = "Longitude";
    private String STATUS = "Status";
    private String IS_TEST_EDIT = "isTestEdit";
    private String IS_ADD_BEN = "isAddBen";
    private String CREATED_AT = "createdAt";
    private String CREATED_BY = "createdBy";
    private String UPDATED_AT = "updatedAt";
    private String UPDATED_BY = "updatedBy";
    private String RECORD_STATUS = "recordStatus";
    private String SYNC_STATUS = "syncStatus";
    private String SYNC_ACTION = "syncAction";


    //changes_22june2017
    //private String APPT_DATE = "AppointmentDate";
    //changes_22june2017

    // Constructors

    public OrderDetailsDao() {
    }

    public OrderDetailsDao(SQLiteDatabase db) {
        this.db = db;
    }

    // getters and setters


    // get model from cursor

    private OrderDetailsModel getModelFromCursor(Cursor cursor) {
        OrderDetailsModel orderDetailsModel = new OrderDetailsModel();

        //changes_22june2017
        //orderDetailsModel.setAppointmentDate(cursor.getString(cursor.getColumnIndex(APPT_DATE)));
        //changes_22june2017

        orderDetailsModel.setVisitId(cursor.getString(cursor.getColumnIndex(VISIT_ID)));
        orderDetailsModel.setSlot(cursor.getString(cursor.getColumnIndex(SLOT)));
        orderDetailsModel.setAppointmentDate(cursor.getString(cursor.getColumnIndex(APPOINTMENT_DATE)));
        orderDetailsModel.setSlotId(cursor.getInt(cursor.getColumnIndex(SLOT_ID)));
        orderDetailsModel.setBrandId(cursor.getInt(cursor.getColumnIndex(BRAND_ID)));
        orderDetailsModel.setOrderNo(cursor.getString(cursor.getColumnIndex(ORDER_NO)));
        orderDetailsModel.setResponse(cursor.getString(cursor.getColumnIndex(RESPONSE)));
        orderDetailsModel.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
        orderDetailsModel.setPincode(cursor.getString(cursor.getColumnIndex(PINCODE)));
        orderDetailsModel.setMobile(cursor.getString(cursor.getColumnIndex(MOBILE)));
        orderDetailsModel.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
        orderDetailsModel.setPayType(cursor.getString(cursor.getColumnIndex(PAY_TYPE)));
        orderDetailsModel.setAmountDue(cursor.getInt(cursor.getColumnIndex(AMOUNT_DUE)));
        orderDetailsModel.setAmountPayable(cursor.getInt(cursor.getColumnIndex(AMOUNT_PAYABLE)));
        orderDetailsModel.setMargin(cursor.getInt(cursor.getColumnIndex(MARGIN)));
        orderDetailsModel.setDiscount(cursor.getInt(cursor.getColumnIndex(DISCOUNT)));
        orderDetailsModel.setEstIncome(cursor.getFloat(cursor.getColumnIndex(EST_INCOME)));
        orderDetailsModel.setDistance(cursor.getInt(cursor.getColumnIndex(DISTANCE)));
        orderDetailsModel.setRefcode(cursor.getString(cursor.getColumnIndex(REFCODE)));
        orderDetailsModel.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
        orderDetailsModel.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
        orderDetailsModel.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
        orderDetailsModel.setProjId(cursor.getString(cursor.getColumnIndex(PROJ_ID)));
        orderDetailsModel.setReportHC(cursor.getInt(cursor.getColumnIndex(REPORT_HC)));
        orderDetailsModel.setTestEdit(cursor.getString(cursor.getColumnIndex(IS_TEST_EDIT)).equals("1"));
        orderDetailsModel.setAddBen(cursor.getString(cursor.getColumnIndex(IS_ADD_BEN)).equals("1"));

        TypeToken<ArrayList<KitsCountModel>> tokenBarcode = new TypeToken<ArrayList<KitsCountModel>>() {
        };
        ArrayList<KitsCountModel> kitsArr = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(KITS)), tokenBarcode.getType());

        orderDetailsModel.setKits(kitsArr);

        orderDetailsModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        orderDetailsModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        orderDetailsModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        orderDetailsModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        orderDetailsModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        orderDetailsModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        orderDetailsModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));
        return orderDetailsModel;
    }

    public ContentValues getContentValuesFromModel(OrderDetailsModel orderDetailsModel) {

        ContentValues values = new ContentValues();

        //changes_22june2017
        //values.put(APPT_DATE, orderDetailsModel.getAppointmentDate());
        //changes_22june2017

        values.put(ORDER_NO, orderDetailsModel.getOrderNo());
        values.put(VISIT_ID, orderDetailsModel.getVisitId());
        values.put(SLOT, orderDetailsModel.getSlot());
        values.put(APPOINTMENT_DATE, orderDetailsModel.getAppointmentDate());
        values.put(SLOT_ID, orderDetailsModel.getSlotId());
        values.put(RESPONSE, orderDetailsModel.getResponse());
        values.put(BRAND_ID, orderDetailsModel.getBrandId());
        values.put(ADDRESS, orderDetailsModel.getAddress());
        values.put(PINCODE, orderDetailsModel.getPincode());
        values.put(MOBILE, orderDetailsModel.getMobile());
        values.put(EMAIL, orderDetailsModel.getEmail());
        values.put(PAY_TYPE, orderDetailsModel.getPayType());
        values.put(AMOUNT_DUE, orderDetailsModel.getAmountDue());
        values.put(AMOUNT_PAYABLE, orderDetailsModel.getAmountPayable());
        values.put(MARGIN, orderDetailsModel.getMargin());
        values.put(DISCOUNT, orderDetailsModel.getDiscount());
        values.put(DISTANCE, orderDetailsModel.getDistance());
        values.put(EST_INCOME, orderDetailsModel.getEstIncome());
        values.put(REFCODE, orderDetailsModel.getRefcode());
        values.put(PROJ_ID, orderDetailsModel.getProjId());
        values.put(REPORT_HC, orderDetailsModel.getReportHC());
        values.put(IS_TEST_EDIT, orderDetailsModel.isTestEdit() ? "1" : "0");
        values.put(IS_ADD_BEN, orderDetailsModel.isAddBen() ? "1" : "0");
        values.put(STATUS, orderDetailsModel.getStatus());
        values.put(LATITUDE, orderDetailsModel.getLatitude());
        values.put(LONGITUDE, orderDetailsModel.getLongitude());
        values.put(KITS, new Gson().toJson(orderDetailsModel.getKits()));
        values.put(CREATED_AT, orderDetailsModel.getCreatedAt());
        values.put(CREATED_BY, orderDetailsModel.getCreatedBy());
        values.put(UPDATED_AT, orderDetailsModel.getUpdatedAt());
        values.put(UPDATED_BY, orderDetailsModel.getUpdatedBy());
        values.put(RECORD_STATUS, orderDetailsModel.getRecordStatus());
        values.put(SYNC_STATUS, orderDetailsModel.getSyncStatus());
        values.put(SYNC_ACTION, orderDetailsModel.getSyncAction());
        return values;
    }

    public ArrayList<OrderDetailsModel> getModelsFromOrderNo(String orderNo) {

        ArrayList<OrderDetailsModel> orderDetailsModelArr = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ORDER_NO + "=? ";
        String[] whereParams = new String[]{orderNo};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())) {
            do {
                OrderDetailsModel orderDetailsModel = getModelFromCursor(cursor);
                if (orderDetailsModel != null) {
                    BeneficiaryDetailsDao beneficiaryDetailsDao = new BeneficiaryDetailsDao(db);
                    orderDetailsModel.setBenMaster(beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo()));
                    orderDetailsModelArr.add(orderDetailsModel);
                }
            } while ((cursor.moveToNext()));
            cursor.close();
        }
        return orderDetailsModelArr;
    }

    public ArrayList<OrderDetailsModel> getModelsFromVisitId(String visitId) {

        ArrayList<OrderDetailsModel> orderDetailsModelsArr = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + VISIT_ID + "=?";
        String[] whereParams = new String[]{visitId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())) {
            do {
                OrderDetailsModel orderDetailsModel = getModelFromCursor(cursor);
                if (orderDetailsModel != null) {
                    BeneficiaryDetailsDao beneficiaryDetailsDao = new BeneficiaryDetailsDao(db);
                    orderDetailsModel.setBenMaster(beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo()));
                    orderDetailsModelsArr.add(orderDetailsModel);
                }
            } while ((cursor.moveToNext()));
            cursor.close();
        }
        return orderDetailsModelsArr;
    }

    public OrderDetailsModel getModelFromId(String orderNo) {
        OrderDetailsModel orderDetailsModel = null;
        if (!InputUtils.isNull(orderNo)) {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ORDER_NO + " = '" + orderNo + "' limit 1";
            Logger.debug("Query - " + query);
            String[] whereParams = new String[]{};
            Cursor cursor = this.db.rawQuery(query, whereParams);
            if (cursor != null && (cursor.moveToFirst())) {
                do {
                    orderDetailsModel = getModelFromCursor(cursor);
                    BeneficiaryDetailsDao beneficiaryDetailsDao = new BeneficiaryDetailsDao(db);
                    orderDetailsModel.setBenMaster(beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo()));
                } while ((cursor.moveToNext()));
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetailsModel;
    }

    // update if exists or insert barcode model to db

    public boolean insertOrUpdate(OrderDetailsModel model) {
        ContentValues contentValues = new ContentValues();
        OrderDetailsModel barcodeDetailsModel;
        if (model != null && !InputUtils.isNull(model.getOrderNo())) {
            barcodeDetailsModel = getModelFromId(model.getOrderNo());
            if (barcodeDetailsModel != null) {
                contentValues = this.getContentValuesFromModel(model);
                int updateValue = (int) db.update(TABLE_NAME, contentValues, ORDER_NO + "=?", new String[]{model.getOrderNo()});
                Logger.debug("insertOrUpdateOrderDetailsModel: update : " + updateValue);
            } else {
                contentValues = this.getContentValuesFromModel(model);
                int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
                Logger.debug("insertOrUpdateOrderDetailsModel: insert : " + insertValue);
            }
        }
        return false;
    }


    public boolean updateOrderNo(String oldOrderNo, OrderDetailsModel model) {
        ContentValues contentValues = new ContentValues();
        OrderDetailsModel barcodeDetailsModel;
        if (model != null && !InputUtils.isNull(model.getOrderNo())) {
            barcodeDetailsModel = getModelFromId(oldOrderNo);
            if (barcodeDetailsModel != null) {
                contentValues = this.getContentValuesFromModel(model);
                int updateValue = (int) db.update(TABLE_NAME, contentValues, ORDER_NO + "=?", new String[]{oldOrderNo});
                Logger.debug("insertOrUpdateOrderDetailsModel - UpdateOrderNo: update : " + updateValue);
            } else {
                return false;
            }
        }
        return false;
    }

    public long getLastUpdatedAt() {

        long updatedDate = 0;
        String query = "SELECT " + UPDATED_AT + " FROM " + TABLE_NAME
                + " WHERE	UPDATED_AT = (SELECT max( " + UPDATED_AT + ") FROM " + TABLE_NAME + ")"
                + " LIMIT 1";
        String[] whereParams = new String[]{};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())) {

            do {
                updatedDate = cursor.getLong(cursor.getColumnIndex(UPDATED_AT));
            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return updatedDate;
    }

    public void deleteAll() {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void deleteByVisitId(String visitId) {
        String queryS = "SELECT " + ORDER_NO + " FROM " + TABLE_NAME + " WHERE " + VISIT_ID + " = ?";
        Logger.debug("Query - " + queryS);
        String[] whereParamsS = new String[]{visitId};
        Cursor cursorS = this.db.rawQuery(queryS, whereParamsS);
        if (cursorS != null && cursorS.moveToFirst()) {
            do {
                String orderNo = cursorS.getString(cursorS.getColumnIndex(ORDER_NO));
                BeneficiaryDetailsDao dao = new BeneficiaryDetailsDao(db);
                dao.deleteByOrderNo(orderNo);
            } while (cursorS.moveToNext());
        }
        if (cursorS != null && !cursorS.isClosed()) {
            cursorS.close();
        }
        String query = "" + VISIT_ID + " = ?";
        Logger.debug("Query - DELETE FROM " + TABLE_NAME + " WHERE " + query);
        String[] whereParams = new String[]{visitId};
        int deleteValue = db.delete(TABLE_NAME, query, whereParams);
        Logger.debug("DeleteOrderDetailsByVisitId: " + deleteValue);
    }

    public void deleteByOrderNo(String orderNo) {
        String query = ORDER_NO + " = ?";
        Logger.debug("Query - DELETE FROM " + TABLE_NAME + " WHERE " + query);
        String[] whereParams = new String[]{orderNo};
        BeneficiaryDetailsDao dao = new BeneficiaryDetailsDao(db);
        dao.deleteByOrderNo(orderNo);
        int deleteValue = db.delete(TABLE_NAME, query, whereParams);
        Logger.debug("DeleteOrderDetailsByOrderNo: " + deleteValue);
    }

    public ArrayList<OrderVisitDetailsModel> getAllModels() {
        ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelsArr = new ArrayList<>();
        String query = "SELECT DISTINCT(" + VISIT_ID + ") FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + " = 'A'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[]{};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())) {
            do {
                String visitId = cursor.getString(cursor.getColumnIndex(VISIT_ID));
                if (!InputUtils.isNull(visitId)) {
                    ArrayList<OrderDetailsModel> orderDetailsModelsArr = new ArrayList<>();
                    orderDetailsModelsArr = getModelsFromVisitId(visitId);
                    ArrayList<OrderDetailsModel> orderDetailsModels = orderDetailsModelsArr;
                    for (OrderDetailsModel orderDetailsModel :
                            orderDetailsModels) {
                        if (orderDetailsModel.getStatus().equalsIgnoreCase("RELEASED")
                                || orderDetailsModel.getStatus().equalsIgnoreCase("CANCELLED")) {
                            orderDetailsModelsArr.remove(orderDetailsModel);
                            deleteByOrderNo(orderDetailsModel.getOrderNo());
                        }
                    }
                    if (orderDetailsModelsArr.size() > 0) {
                        OrderVisitDetailsModel orderVisitDetailsModel = new OrderVisitDetailsModel();
                        orderVisitDetailsModel.setResponse(orderDetailsModelsArr.get(0).getResponse());
                        orderVisitDetailsModel.setSlot(orderDetailsModelsArr.get(0).getSlot());
                        orderVisitDetailsModel.setSlotId(orderDetailsModelsArr.get(0).getSlotId());
                        orderVisitDetailsModel.setVisitId(orderDetailsModelsArr.get(0).getVisitId());
                        orderVisitDetailsModel.setDistance(orderDetailsModelsArr.get(0).getDistance());
                        orderVisitDetailsModel.setEstIncome(orderDetailsModelsArr.get(0).getEstIncome());
                        orderVisitDetailsModel.setAppointmentDate(orderDetailsModelsArr.get(0).getAppointmentDate());
                        orderVisitDetailsModel.setAllOrderdetails(orderDetailsModelsArr);
                        orderVisitDetailsModelsArr.add(orderVisitDetailsModel);
                    }
                }
            } while ((cursor.moveToNext()));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return orderVisitDetailsModelsArr;
    }

    public OrderVisitDetailsModel getOrderVisitModel(String visitId) {
        OrderVisitDetailsModel orderVisitDetailsModel = new OrderVisitDetailsModel();
        if (!InputUtils.isNull(visitId)) {
            ArrayList<OrderDetailsModel> orderDetailsModelsArr = new ArrayList<>();
            orderDetailsModelsArr = getModelsFromVisitId(visitId);
            ArrayList<OrderDetailsModel> orderDetailsModels = orderDetailsModelsArr;
            for (OrderDetailsModel orderDetailsModel :
                    orderDetailsModels) {
                if (orderDetailsModel.getStatus().equalsIgnoreCase("RELEASED")
                        || orderDetailsModel.getStatus().equalsIgnoreCase("CANCELLED")) {
                    orderDetailsModelsArr.remove(orderDetailsModel);
                    deleteByOrderNo(orderDetailsModel.getOrderNo());
                }
            }
            if (orderDetailsModelsArr.size() > 0) {
                orderVisitDetailsModel = new OrderVisitDetailsModel();
                orderVisitDetailsModel.setResponse(orderDetailsModelsArr.get(0).getResponse());
                orderVisitDetailsModel.setSlot(orderDetailsModelsArr.get(0).getSlot());
                orderVisitDetailsModel.setSlotId(orderDetailsModelsArr.get(0).getSlotId());
                orderVisitDetailsModel.setVisitId(orderDetailsModelsArr.get(0).getVisitId());
                orderVisitDetailsModel.setDistance(orderDetailsModelsArr.get(0).getDistance());
                orderVisitDetailsModel.setAppointmentDate(orderDetailsModelsArr.get(0).getAppointmentDate());
                orderVisitDetailsModel.setAllOrderdetails(orderDetailsModelsArr);
            }
        }
        return orderVisitDetailsModel;
    }
}