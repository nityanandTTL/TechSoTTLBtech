package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.util.List;

public class FAQandANSArray {


    /**
     * RESPONSE : SUCCESS
     * promoterFaqList : [{"Answers":"<p>Test entry.<br>.<br>.<br>please ignore it<\/p>","Questions":"test Entry"}]
     */

    private String RESPONSE;
    private List<PromoterFaqListBean> promoterFaqList;

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public List<PromoterFaqListBean> getPromoterFaqList() {
        return promoterFaqList;
    }

    public void setPromoterFaqList(List<PromoterFaqListBean> promoterFaqList) {
        this.promoterFaqList = promoterFaqList;
    }

    public static class PromoterFaqListBean {
        /**
         * Answers : <p>Test entry.<br>.<br>.<br>please ignore it</p>
         * Questions : test Entry
         */

        private String Answers;
        private String Questions;

        public String getAnswers() {
            return Answers;
        }

        public void setAnswers(String Answers) {
            this.Answers = Answers;
        }

        public String getQuestions() {
            return Questions;
        }

        public void setQuestions(String Questions) {
            this.Questions = Questions;
        }
    }
}
