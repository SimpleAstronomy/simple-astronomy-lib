/*
 *  Copyright 2011 Brad Parks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bradsbrain.simpleastronomy;

import static com.bradsbrain.simpleastronomy.BaseUtils.sinDegrees;

import java.util.Calendar;

public class MoonPosition {
    // some handy constants
    private double EPOCH = 2447891.5; // 1990 January 0.0
    private double MEAN_LONGITUDE_AT_EPOCH = 318.351648;
    private double MEAN_LONGITUDE_OF_PERIGREE_AT_EPOCH = 36.340410;

    /**
     * The True Longitude
     */
    private double trueOrbitalLongitude;

    /**
     * This is from section 65, page 144
     *
     * @param cal the calendar date for which to compute the moon position
     */
    public MoonPosition(Calendar cal) {
        Calendar myCal = BaseUtils.getSafeLocalCopy(cal.getTimeInMillis());
        double daysSince = BaseUtils.exactDaysSince(myCal, EPOCH);

        // l
        double moonMeanLongitude = computeMeanLongitude(daysSince);
        // M m
        double moonMeanAnomaly = computeMeanAnomaly(daysSince, moonMeanLongitude);

        SunPosition sunPos = new SunPosition(myCal);

        MoonCorrections corrections = new MoonCorrections(moonMeanLongitude, moonMeanAnomaly, sunPos.getEclipticLongitude(), sunPos.getMeanAnomaly());
        trueOrbitalLongitude = corrections.getCorrectedLongitude() - corrections.getVariationCorrection();
    }

    public double getTrueLongitude() {
        return trueOrbitalLongitude;
    }

    /**
     * Compute the Moon Mean Longitude	l
     *
     * @param daysSince
     * @return
     */
    private double computeMeanLongitude(double daysSince) {
        double moonMeanLongitude = 13.1763966 * daysSince + MEAN_LONGITUDE_AT_EPOCH;
        return BaseUtils.adjustTo360Range(moonMeanLongitude);
    }

    /**
     * Compute the Moon Mean Anomaly	M m
     *
     * @param daysSince
     * @param moonMeanLongitude
     * @return
     */
    private double computeMeanAnomaly(double daysSince, double moonMeanLongitude) {
        double moonMeanAnomaly = moonMeanLongitude
                - (0.1114041 * daysSince)
                - MEAN_LONGITUDE_OF_PERIGREE_AT_EPOCH;
        return BaseUtils.adjustTo360Range(moonMeanAnomaly);
    }

    /**
     * Private internal class used for lots of computations and corrections
     */
    private static class MoonCorrections {
        private double moonMeanLongitude;
        private double moonMeanAnomaly;
        private double sunLongitude;
        private double sunMeanAnomaly;

        MoonCorrections(double moonMeanLongitude, double moonMeanAnomaly,
                        double sunLongitude, double sunMeanAnomaly) {
            this.moonMeanAnomaly = moonMeanAnomaly;
            this.sunMeanAnomaly = sunMeanAnomaly;
            this.moonMeanLongitude = moonMeanLongitude;
            this.sunLongitude = sunLongitude;
        }

        /**
         * V
         *
         * @return
         */
        public double getVariationCorrection() {
            return 0.6583 * sinDegrees(2 * (getCorrectedLongitude() - sunLongitude));
        }

        /**
         * l'
         *
         * @return
         */
        public double getCorrectedLongitude() {
            return moonMeanLongitude
                    + getEvictionCorrection()
                    + getCorrectionForEquationCentre()
                    - getAnnualEquationCorrection()
                    + getYetAnotherCorrectionTerm();
        }

        /**
         * A 4
         *
         * @return
         */
        private double getYetAnotherCorrectionTerm() {
            return 0.214 * sinDegrees(2 * getMoonCorrectedAnomaly());
        }

        /**
         * E c
         *
         * @return
         */
        private double getCorrectionForEquationCentre() {
            return 6.2886 * sinDegrees(getMoonCorrectedAnomaly());
        }

        /**
         * M' m
         *
         * @return
         */
        private double getMoonCorrectedAnomaly() {
            return moonMeanAnomaly + getEvictionCorrection() - getAnnualEquationCorrection() - getUnnamedThirdCorrection();
        }

        /**
         * E v
         *
         * @return
         */
        public double getEvictionCorrection() {
            double C = moonMeanLongitude - sunLongitude;
            return 1.2739 * sinDegrees(2.0 * C - moonMeanAnomaly);
        }

        /**
         * A e
         *
         * @return
         */
        public double getAnnualEquationCorrection() {
            return 0.1858 * sinDegrees(sunMeanAnomaly);
        }

        /**
         * A 3
         *
         * @return
         */
        public double getUnnamedThirdCorrection() {
            return 0.37 * sinDegrees(sunMeanAnomaly);
        }
    }


}
