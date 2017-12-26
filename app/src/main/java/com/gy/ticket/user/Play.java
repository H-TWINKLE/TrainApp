package com.gy.ticket.user;

import java.util.List;

/**
 * Created by TWINKLE on 2017/12/26.
 */

public class Play {

    private List<SuggestBean> suggest;

    public List<SuggestBean> getSuggest() {
        return suggest;
    }

    public void setSuggest(List<SuggestBean> suggest) {
        this.suggest = suggest;
    }

    public static class SuggestBean {
        /**
         * fold : 438
         * price : 100.0
         * projectId : 43897
         * projectName : Mr.x Room Break（密室）长宁店
         * showTime : 2017-12-26 23:58:00
         * startTime : 2017-12-26 23:58:00
         * tag : 1007.16510.93972.;rankmodel#desc
         * venue : 《MR.X》长宁店
         */

        private int fold;
        private String price;
        private int projectId;
        private String projectName;
        private String showTime;
        private String startTime;
        private String tag;
        private String venue;

        public int getFold() {
            return fold;
        }

        public void setFold(int fold) {
            this.fold = fold;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getVenue() {
            return venue;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }
    }
}
