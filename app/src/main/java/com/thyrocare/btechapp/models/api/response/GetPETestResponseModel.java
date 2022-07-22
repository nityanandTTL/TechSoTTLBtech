package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GetPETestResponseModel implements Parcelable {
    public static final Creator<GetPETestResponseModel> CREATOR = new Creator<GetPETestResponseModel>() {
        @Override
        public GetPETestResponseModel createFromParcel(Parcel in) {
            return new GetPETestResponseModel(in);
        }

        @Override
        public GetPETestResponseModel[] newArray(int size) {
            return new GetPETestResponseModel[size];
        }
    };
    /**
     * error : null
     * status : 1
     * data : [{"id":44,"type":"test","name":"Complete Blood Count (CBC)","price":"175","desc":"","tube_type":"edta","dos_code":"Complete Blood Count (CBC)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"400","discount_percent":"56.25","partner_master_test_id":""},{"id":86,"type":"test","name":"Glycosylated Hemoglobin (HbA1C)","price":"270","desc":"","tube_type":"edta","dos_code":"Glycosylated Hemoglobin (HbA1C)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"600","discount_percent":"55.00","partner_master_test_id":""},{"id":30,"type":"test","name":"Blood Group","price":"95","desc":"","tube_type":"serum","dos_code":"Blood Group","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"450","discount_percent":"78.89","partner_master_test_id":""},{"id":31,"type":"test","name":"Fasting Blood Sugar (FBS)","price":"60","desc":"","tube_type":"fluoride","dos_code":"Fasting Blood Sugar (FBS)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"350","discount_percent":"82.86","partner_master_test_id":""},{"id":33,"type":"test","name":"Random Blood Sugar (RBS)","price":"60","desc":"","tube_type":"fluoride","dos_code":"Random Blood Sugar (RBS)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"350","discount_percent":"82.86","partner_master_test_id":""},{"id":71,"type":"test","name":"Erythrocyte Sedimentation Rate (ESR)","price":"75","desc":"","tube_type":"edta","dos_code":"Erythrocyte Sedimentation Rate (ESR)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"200","discount_percent":"62.50","partner_master_test_id":""},{"id":90,"type":"test","name":"Hemoglobin (Hb)","price":"99","desc":"","tube_type":"edta","dos_code":"Hemoglobin (Hb)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"150","discount_percent":"34.00","partner_master_test_id":""},{"id":108,"type":"test","name":"Malarial Parasite","price":"130","desc":"","tube_type":"edta","dos_code":"Malarial Parasite","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"200","discount_percent":"35.00","partner_master_test_id":""},{"id":87,"type":"test","name":"Hepatitis B Surface Antigen (HBsAg)","price":"250","desc":"","tube_type":"serum","dos_code":"Hepatitis B Surface Antigen (HBsAg)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"500","discount_percent":"50.00","partner_master_test_id":""},{"id":664,"type":"test","name":"CRP (Qualitative)","price":"250","desc":"","tube_type":"plain","dos_code":"CRP (Qualitative)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"400","discount_percent":"37.50","partner_master_test_id":""},{"id":88,"type":"test","name":"Hepatitis C Virus (HCV) - Quantitative","price":"410","desc":"","tube_type":"serum","dos_code":"Hepatitis C Virus (HCV) - Quantitative","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"600","discount_percent":"31.67","partner_master_test_id":""},{"id":91,"type":"test","name":"HIV I & II","price":"350","desc":"","tube_type":"Plain","dos_code":"HIV I & II","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1100","discount_percent":"68.18","partner_master_test_id":""},{"id":107,"type":"test","name":"Malarial Antigen Test","price":"300","desc":"","tube_type":"edta","dos_code":"Malarial Antigen Test","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"400","discount_percent":"25.00","partner_master_test_id":""},{"id":123,"type":"test","name":"Rheumatoid Factor (RA Factor) - Quantitative","price":"175","desc":"","tube_type":"serum","dos_code":"Rheumatoid Factor (RA Factor) - Quantitative","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"350","discount_percent":"50.00","partner_master_test_id":""},{"id":153,"type":"test","name":"VDRL Test","price":"135","desc":"","tube_type":"serum","dos_code":"VDRL Test","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"250","discount_percent":"46.00","partner_master_test_id":""},{"id":156,"type":"test","name":"Widal test (Slide method)","price":"150","desc":"","tube_type":"plain","dos_code":"Widal test (Slide method)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"200","discount_percent":"25.00","partner_master_test_id":""},{"id":4,"type":"test","name":"Albumin","price":"95","desc":"","tube_type":"plain","dos_code":"Albumin","lab_dos_name":"","partner_lab_test_id":"SALB","mrp":"200","discount_percent":"52.50","partner_master_test_id":""},{"id":10,"type":"test","name":"Amylase","price":"295","desc":"","tube_type":"serum","dos_code":"Amylase","lab_dos_name":"","partner_lab_test_id":"AMYL","mrp":"400","discount_percent":"26.25","partner_master_test_id":""},{"id":34,"type":"test","name":"Blood urea nitrogen (BUN)","price":"100","desc":"","tube_type":"plain","dos_code":"Blood urea nitrogen (BUN)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"200","discount_percent":"50.00","partner_master_test_id":""},{"id":39,"type":"test","name":"Serum Calcium (Ca)","price":"120","desc":"","tube_type":"serum","dos_code":"Serum Calcium (Ca)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"300","discount_percent":"60.00","partner_master_test_id":""},{"id":50,"type":"test","name":"Total Cholesterol","price":"100","desc":"","tube_type":"serum","dos_code":"Total Cholesterol","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"350","discount_percent":"71.43","partner_master_test_id":""},{"id":59,"type":"test","name":"Creatinine","price":"105","desc":"","tube_type":"serum","dos_code":"Creatinine","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"350","discount_percent":"70.00","partner_master_test_id":""},{"id":32,"type":"test","name":"Post Prandial Blood Sugar (PPBS)","price":"65","desc":"","tube_type":"fluoride","dos_code":"Post Prandial Blood Sugar (PPBS)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"200","discount_percent":"67.50","partner_master_test_id":""},{"id":98,"type":"test","name":"Iron Study","price":"280","desc":"","tube_type":"serum","dos_code":"Iron Study","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"550","discount_percent":"49.09","partner_master_test_id":""},{"id":103,"type":"test","name":"Lipase","price":"340","desc":"","tube_type":"serum","dos_code":"Lipase","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"700","discount_percent":"51.43","partner_master_test_id":""},{"id":106,"type":"test","name":"Magnesium","price":"240","desc":"","tube_type":"serum","dos_code":"Magnesium","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"495","discount_percent":"51.52","partner_master_test_id":""},{"id":109,"type":"test","name":"Serum Phosphorus (Ph)","price":"120","desc":"","tube_type":"serum","dos_code":"Serum Phosphorus (Ph)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"250","discount_percent":"52.00","partner_master_test_id":""},{"id":111,"type":"test","name":"Potassium, serum","price":"160","desc":"","tube_type":"serum","dos_code":"Potassium, serum","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"300","discount_percent":"46.67","partner_master_test_id":""},{"id":129,"type":"test","name":"Aspartate Aminotransferase (AST/SGOT)","price":"100","desc":"","tube_type":"plain","dos_code":"Aspartate Aminotransferase (AST/SGOT)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"350","discount_percent":"71.43","partner_master_test_id":""},{"id":130,"type":"test","name":"Alanine Transaminase (SGPT/ALT)","price":"115","desc":"","tube_type":"plain","dos_code":"Alanine Transaminase (SGPT/ALT)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"200","discount_percent":"42.50","partner_master_test_id":""},{"id":131,"type":"test","name":"Serum Sodium (Na)","price":"110","desc":"","tube_type":"serum","dos_code":"Serum Sodium (Na)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"250","discount_percent":"56.00","partner_master_test_id":""},{"id":145,"type":"test","name":"Serum Uric Acid","price":"149","desc":"","tube_type":"serum","dos_code":"Serum Uric Acid","lab_dos_name":"","partner_lab_test_id":"URIC","mrp":"200","discount_percent":"25.50","partner_master_test_id":""},{"id":151,"type":"test","name":"Urine Routine & Microscopy (Urine R/M)","price":"100","desc":"","tube_type":"urine container","dos_code":"Urine Routine & Microscopy (Urine R/M)","lab_dos_name":"","partner_lab_test_id":"COMPLETE URINE ANALYSIS","mrp":"350","discount_percent":"71.43","partner_master_test_id":""},{"id":150,"type":"test","name":"Urine for microalbuminuria","price":"410","desc":"","tube_type":"Urine Container","dos_code":"Urine for microalbuminuria","lab_dos_name":"","partner_lab_test_id":"UALB","mrp":"600","discount_percent":"31.67","partner_master_test_id":""},{"id":46,"type":"test","name":"CEA","price":"450","desc":"","tube_type":"serum","dos_code":"CEA","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"900","discount_percent":"50.00","partner_master_test_id":""},{"id":77,"type":"test","name":"Free T3","price":"190","desc":"","tube_type":"serum","dos_code":"Free T3","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"330","discount_percent":"42.42","partner_master_test_id":""},{"id":36,"type":"test","name":"Cancer Antigen 125 (CA 125) for Women","price":"500","desc":"","tube_type":"serum","dos_code":"Cancer Antigen 125 (CA 125) for Women","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1000","discount_percent":"50.00","partner_master_test_id":""},{"id":78,"type":"test","name":"Free T4","price":"150","desc":"","tube_type":"serum","dos_code":"Free T4","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"330","discount_percent":"54.55","partner_master_test_id":""},{"id":80,"type":"test","name":"FSH","price":"220","desc":"","tube_type":"serum","dos_code":"FSH","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"550","discount_percent":"60.00","partner_master_test_id":""},{"id":97,"type":"test","name":"Insulin fasting","price":"450","desc":"","tube_type":"serum","dos_code":"Insulin fasting","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"880","discount_percent":"48.86","partner_master_test_id":""},{"id":102,"type":"test","name":"Luteinising Hormone (LH)","price":"300","desc":"","tube_type":"serum","dos_code":"Luteinising Hormone (LH)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"500","discount_percent":"40.00","partner_master_test_id":""},{"id":114,"type":"test","name":"Prolactin","price":"220","desc":"","tube_type":"serum","dos_code":"Prolactin","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"600","discount_percent":"63.33","partner_master_test_id":""},{"id":119,"type":"test","name":"Prostate Specific Antigen (PSA) - Total","price":"295","desc":"","tube_type":"serum","dos_code":"Prostate Specific Antigen (PSA) - Total","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"750","discount_percent":"60.67","partner_master_test_id":""},{"id":134,"type":"test","name":"T3","price":"110","desc":"","tube_type":"serum","dos_code":"T3","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"250","discount_percent":"56.00","partner_master_test_id":""},{"id":135,"type":"test","name":"T4","price":"110","desc":"","tube_type":"serum","dos_code":"T4","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"250","discount_percent":"56.00","partner_master_test_id":""},{"id":137,"type":"test","name":"Testosterone","price":"290","desc":"","tube_type":"serum","dos_code":"Testosterone","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"600","discount_percent":"51.67","partner_master_test_id":""},{"id":142,"type":"test","name":"Thyroid Stimulating Hormone (TSH)","price":"130","desc":"","tube_type":"serum","dos_code":"Thyroid Stimulating Hormone (TSH)","lab_dos_name":"","partner_lab_test_id":"TSH","mrp":"350","discount_percent":"62.86","partner_master_test_id":""},{"id":154,"type":"test","name":"Vitamin B12","price":"360","desc":"","tube_type":"serum","dos_code":"Vitamin B12","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1300","discount_percent":"72.31","partner_master_test_id":""},{"id":14,"type":"test","name":"Anti Nuclear Antibody/Factor (ANA/ANF)","price":"400","desc":"","tube_type":"serum","dos_code":"Anti Nuclear Antibody/Factor (ANA/ANF)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"825","discount_percent":"51.52","partner_master_test_id":""},{"id":37,"type":"test","name":"CA 15.3","price":"700","desc":"","tube_type":"Plain","dos_code":"CA 15.3","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1320","discount_percent":"46.97","partner_master_test_id":""},{"id":38,"type":"test","name":"CA 19.9","price":"450","desc":"","tube_type":"serum","dos_code":"CA 19.9","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1320","discount_percent":"65.91","partner_master_test_id":""},{"id":12,"type":"test","name":"Anti CCP antibody","price":"550","desc":"","tube_type":"serum","dos_code":"Anti CCP antibody","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1485","discount_percent":"62.96","partner_master_test_id":""},{"id":35,"type":"test","name":"C Peptide","price":"590","desc":"","tube_type":"serum","dos_code":"C Peptide","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1265","discount_percent":"53.36","partner_master_test_id":""},{"id":95,"type":"test","name":"High Sensitivity C-Reactive Protein (hs-CRP)","price":"270","desc":"","tube_type":"serum","dos_code":"High Sensitivity C-Reactive Protein (hs-CRP)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"700","discount_percent":"61.43","partner_master_test_id":""},{"id":67,"type":"test","name":"DHEAS","price":"800","desc":"","tube_type":"serum","dos_code":"DHEAS","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1100","discount_percent":"27.27","partner_master_test_id":""},{"id":72,"type":"test","name":"Estradiol/Estrogen (E2)","price":"290","desc":"","tube_type":"Plain","dos_code":"Estradiol/Estrogen (E2)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"660","discount_percent":"56.06","partner_master_test_id":""},{"id":73,"type":"test","name":"Ferritin","price":"340","desc":"","tube_type":"serum","dos_code":"Ferritin","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"800","discount_percent":"57.50","partner_master_test_id":""},{"id":75,"type":"test","name":"Folic acid","price":"500","desc":"","tube_type":"serum","dos_code":"Folic acid","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1100","discount_percent":"54.55","partner_master_test_id":""},{"id":79,"type":"test","name":"Free testosterone","price":"590","desc":"","tube_type":"serum","dos_code":"Free testosterone","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1595","discount_percent":"63.01","partner_master_test_id":""},{"id":92,"type":"test","name":"HLA B27","price":"800","desc":"","tube_type":"edta","dos_code":"HLA B27","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1430","discount_percent":"44.06","partner_master_test_id":""},{"id":94,"type":"test","name":"Homocysteine","price":"595","desc":"","tube_type":"serum","dos_code":"Homocysteine","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"900","discount_percent":"33.89","partner_master_test_id":""},{"id":18,"type":"test","name":"Anti TPO (Thyroid peroxidase antibodies)","price":"720","desc":"","tube_type":"serum","dos_code":"Anti TPO (Thyroid peroxidase antibodies)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1265","discount_percent":"43.08","partner_master_test_id":""},{"id":155,"type":"test","name":"Vitamin D3 (25-OH Cholecalciferol)","price":"599","desc":"","tube_type":"serum","dos_code":"Vitamin D3 (25-OH Cholecalciferol)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1600","discount_percent":"62.56","partner_master_test_id":""},{"id":144,"type":"test","name":"Serum Urea","price":"95","desc":"","tube_type":"serum","dos_code":"Serum Urea","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"300","discount_percent":"68.33","partner_master_test_id":""},{"id":115,"type":"test","name":"Protein creatinine ratio (Urine)","price":"600","desc":"","tube_type":"Urine container","dos_code":"Protein creatinine ratio (Urine)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"660","discount_percent":"9.09","partner_master_test_id":""},{"id":29,"type":"test","name":"Bilirubin (T+D)","price":"145","desc":"","tube_type":"serum","dos_code":"Bilirubin (T+D)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"720","discount_percent":"79.86","partner_master_test_id":""},{"id":126,"type":"test","name":"Rubella IgG","price":"450","desc":"","tube_type":"Plain","dos_code":"Rubella IgG","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"715","discount_percent":"37.06","partner_master_test_id":""},{"id":620,"type":"test","name":"Covid Antibody Test","price":"449","desc":"","tube_type":"plain","dos_code":"Covid Antibody Test","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"1990","discount_percent":"77.44","partner_master_test_id":""},{"id":70,"type":"test","name":"EGFR","price":"250","desc":"","tube_type":"Plain","dos_code":"EGFR","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"275","discount_percent":"9.09","partner_master_test_id":""},{"id":27,"type":"test","name":"Beta HCG total","price":"350","desc":"","tube_type":"serum","dos_code":"Beta HCG total","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"605","discount_percent":"42.15","partner_master_test_id":""},{"id":133,"type":"test","name":"Stool analysis","price":"120","desc":"","tube_type":"stool container","dos_code":"Stool analysis","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"220","discount_percent":"45.45","partner_master_test_id":""},{"id":1,"type":"test","name":"17 OH Progesterone","price":"650","desc":"","tube_type":"serum","dos_code":"17 OH Progesterone","lab_dos_name":"","partner_lab_test_id":"17OH","mrp":"1100","discount_percent":"40.91","partner_master_test_id":""},{"id":63,"type":"test","name":"Dengue NS 1 antigen","price":"490","desc":"","tube_type":"serum","dos_code":"Dengue NS 1 antigen","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"715","discount_percent":"31.47","partner_master_test_id":""},{"id":68,"type":"test","name":"Dihydrotestosteron (DHT)","price":"2400","desc":"","tube_type":"Plain","dos_code":"Dihydrotestosteron (DHT)","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"3300","discount_percent":"27.27","partner_master_test_id":""},{"id":120,"type":"test","name":"PSA free","price":"560","desc":"","tube_type":"serum","dos_code":"PSA free","lab_dos_name":"","partner_lab_test_id":"NA","mrp":"990","discount_percent":"43.43","partner_master_test_id":""},{"id":5,"type":"profile","name":"Thyroid Profile - Total (T3, T4 & TSH)","price":"199","desc":"","tube_type":"plain","dos_code":"Thyroid Profile - Total (T3, T4 & TSH)","lab_dos_name":"T3-T4-TSH","partner_lab_test_id":"T3-T4-TSH","mrp":"650","discount_percent":"69.38","partner_master_test_id":""},{"id":289,"type":"profile","name":"Lipid Profile","price":"270","desc":"","tube_type":"plain","dos_code":"Lipid Profile","lab_dos_name":"","partner_lab_test_id":"","mrp":"650","discount_percent":"58.46","partner_master_test_id":""},{"id":290,"type":"profile","name":"Liver Function Test (LFT)","price":"260","desc":"","tube_type":"plain","dos_code":"Liver Function Test (LFT)","lab_dos_name":"","partner_lab_test_id":"","mrp":"750","discount_percent":"65.33","partner_master_test_id":""},{"id":335,"type":"profile","name":"Renal/Kidney Function Test (RFT/KFT)","price":"250","desc":"","tube_type":"plain","dos_code":"Renal/Kidney Function Test (RFT/KFT)","lab_dos_name":"","partner_lab_test_id":"","mrp":"750","discount_percent":"66.67","partner_master_test_id":""},{"id":332,"type":"profile","name":"Serum Electrolytes","price":"240","desc":"","tube_type":"plain","dos_code":"Serum Electrolytes","lab_dos_name":"","partner_lab_test_id":"","mrp":"600","discount_percent":"60.00","partner_master_test_id":""},{"id":2325,"type":"package","name":"Essential Vitamin Package","price":"675","desc":"","tube_type":"plain","dos_code":"Essential Vitamin Package","lab_dos_name":"","partner_lab_test_id":"","mrp":"1990","discount_percent":"66.08","partner_master_test_id":""},{"id":2165,"type":"package","name":"Advanced Full Body Checkup","price":"899","desc":"","tube_type":"plain,fluoride,edta,urine container","dos_code":"Advanced Full Body Checkup","lab_dos_name":"COMPLETE URINE ANALYSIS,FBS,HEMOGRAM - 6 PART (DIFF),KIDPRO,LIPID PROFILE,LIVER FUNCTION TESTS,T3-T4-TSH","partner_lab_test_id":"PROJ1019956","mrp":"2550","discount_percent":"64.75","partner_master_test_id":""},{"id":2166,"type":"package","name":"Comprehensive Full Body Checkup with Vitamin D & B12","price":"1299","desc":"","tube_type":"plain,fluoride,edta,urine container","dos_code":"Comprehensive Full Body Checkup with Vitamin D & B12","lab_dos_name":"","partner_lab_test_id":"","mrp":"4190","discount_percent":"69.00","partner_master_test_id":""},{"id":2167,"type":"package","name":"Advanced Full Body Checkup with Covid Antibody","price":"1299","desc":"","tube_type":"plain,fluoride,edta,urine container","dos_code":"Advanced Full Body Checkup with Covid Antibody","lab_dos_name":"","partner_lab_test_id":"","mrp":"3490","discount_percent":"62.78","partner_master_test_id":""},{"id":2168,"type":"package","name":"Basic Health Checkup","price":"849","desc":"","tube_type":"plain,fluoride,edta","dos_code":"Basic Health Checkup","lab_dos_name":"","partner_lab_test_id":"","mrp":"2050","discount_percent":"58.59","partner_master_test_id":""},{"id":2169,"type":"package","name":"Master Checkup with Cancer & Arthritis Screening (Male)","price":"1499","desc":"","tube_type":"plain,fluoride,edta,urine container","dos_code":"Master Checkup with Cancer & Arthritis Screening (Male)","lab_dos_name":"","partner_lab_test_id":"","mrp":"4990","discount_percent":"69.96","partner_master_test_id":""},{"id":2170,"type":"package","name":"Master Checkup with Cancer & Arthritis Screening (Female)","price":"1499","desc":"","tube_type":"plain,fluoride,edta,urine container","dos_code":"Master Checkup with Cancer & Arthritis Screening (Female)","lab_dos_name":"","partner_lab_test_id":"","mrp":"4990","discount_percent":"69.96","partner_master_test_id":""},{"id":2171,"type":"package","name":"Comprehensive Full Body Checkup","price":"1099","desc":"","tube_type":"plain,fluoride,edta,urine container","dos_code":"Comprehensive Full Body Checkup","lab_dos_name":"","partner_lab_test_id":"","mrp":"2990","discount_percent":"63.24","partner_master_test_id":""},{"id":2172,"type":"package","name":"Preliminary Health Checkup","price":"599","desc":"","tube_type":"plain,edta","dos_code":"Preliminary Health Checkup","lab_dos_name":"","partner_lab_test_id":"","mrp":"1490","discount_percent":"59.80","partner_master_test_id":""},{"id":2173,"type":"package","name":"Healthy Lung & Body Checkup","price":"1199","desc":"","tube_type":"plain,edta","dos_code":"Healthy Lung & Body Checkup","lab_dos_name":"","partner_lab_test_id":"","mrp":"2990","discount_percent":"59.90","partner_master_test_id":""},{"id":2174,"type":"package","name":"Comprehensive Full Body Checkup with Vitamin D","price":"1299","desc":"","tube_type":"plain,edta","dos_code":"Comprehensive Full Body Checkup with Vitamin D","lab_dos_name":"","partner_lab_test_id":"","mrp":"3690","discount_percent":"64.80","partner_master_test_id":""},{"id":null,"type":"package","name":"Winter Care Health Checkup","price":"799","desc":"","tube_type":"plain,fluoride,edta","dos_code":"Winter Care Health Checkup","lab_dos_name":"","partner_lab_test_id":"","mrp":"2499","discount_percent":"68.03","partner_master_test_id":""},{"id":2290,"type":"package","name":"Covid - 19 Health Checkup (Post Recovery)","price":"1299","desc":"","tube_type":"edta,plain","dos_code":"Covid - 19 Health Checkup (Post Recovery)","lab_dos_name":"","partner_lab_test_id":"","mrp":"3499","discount_percent":"62.88","partner_master_test_id":""},{"id":2284,"type":"package","name":"Covid-19 Total Antibody (IgG+IgM)","price":"850","desc":"","tube_type":"plain","dos_code":"Covid-19 Total Antibody (IgG+IgM)","lab_dos_name":"","partner_lab_test_id":"","mrp":"2490","discount_percent":"65.86","partner_master_test_id":""},{"id":2293,"type":"package","name":"Healthy 2021 Full Body Checkup","price":"1049","desc":"","tube_type":"plain,edta,urine container,fluoride","dos_code":"Healthy 2021 Full Body Checkup","lab_dos_name":"","partner_lab_test_id":"","mrp":"4190","discount_percent":"74.96","partner_master_test_id":""}]
     */

    private String error;
    private Integer status;
    private ArrayList<DataDTO> data;

    protected GetPETestResponseModel(Parcel in) {
        error = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<DataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<DataDTO> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
    }

    public static class DataDTO implements Parcelable {
        public static final Creator<DataDTO> CREATOR = new Creator<DataDTO>() {
            @Override
            public DataDTO createFromParcel(Parcel in) {
                return new DataDTO(in);
            }

            @Override
            public DataDTO[] newArray(int size) {
                return new DataDTO[size];
            }
        };
        /**
         * id : 44
         * type : test
         * name : Complete Blood Count (CBC)
         * price : 175
         * desc :
         * tube_type : edta
         * dos_code : Complete Blood Count (CBC)
         * lab_dos_name :
         * partner_lab_test_id : NA
         * mrp : 400
         * discount_percent : 56.25
         * partner_master_test_id :
         */

        private Integer id;
        private String type;
        private String name;
        private String price;
        private String desc;
        private String tube_type;
        private String dos_code;
        private String lab_dos_name;
        private String partner_lab_test_id;
        private String mrp;
        private String discount_percent;
        private String partner_master_test_id;
        private Integer test_dos_id;

        protected DataDTO(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            type = in.readString();
            name = in.readString();
            price = in.readString();
            desc = in.readString();
            tube_type = in.readString();
            dos_code = in.readString();
            lab_dos_name = in.readString();
            partner_lab_test_id = in.readString();
            mrp = in.readString();
            discount_percent = in.readString();
            partner_master_test_id = in.readString();
            test_dos_id = in.readInt();
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTube_type() {
            return tube_type;
        }

        public void setTube_type(String tube_type) {
            this.tube_type = tube_type;
        }

        public String getDos_code() {
            return dos_code;
        }

        public void setDos_code(String dos_code) {
            this.dos_code = dos_code;
        }

        public String getLab_dos_name() {
            return lab_dos_name;
        }

        public void setLab_dos_name(String lab_dos_name) {
            this.lab_dos_name = lab_dos_name;
        }

        public String getPartner_lab_test_id() {
            return partner_lab_test_id;
        }

        public void setPartner_lab_test_id(String partner_lab_test_id) {
            this.partner_lab_test_id = partner_lab_test_id;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getDiscount_percent() {
            return discount_percent;
        }

        public void setDiscount_percent(String discount_percent) {
            this.discount_percent = discount_percent;
        }

        public String getPartner_master_test_id() {
            return partner_master_test_id;
        }

        public void setPartner_master_test_id(String partner_master_test_id) {
            this.partner_master_test_id = partner_master_test_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
            dest.writeString(type);
            dest.writeString(name);
            dest.writeString(price);
            dest.writeString(desc);
            dest.writeString(tube_type);
            dest.writeString(dos_code);
            dest.writeString(lab_dos_name);
            dest.writeString(partner_lab_test_id);
            dest.writeString(mrp);
            dest.writeString(discount_percent);
            dest.writeString(partner_master_test_id);
            dest.writeInt(test_dos_id);
        }

        public Integer getTest_dos_id() {
            return test_dos_id;
        }

        public void setTest_dos_id(Integer test_dos_id) {
            this.test_dos_id = test_dos_id;
        }
    }
}
