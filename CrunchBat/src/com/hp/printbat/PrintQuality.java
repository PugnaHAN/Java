package com.hp.printbat;

/**
 * Created by Juhan on 2016/7/25.
 */
public enum PrintQuality {
    Normal("Normal"),
    Best("Best"),
    Draft("Draft"),
    MaxDpi("MaxDpi"),
    Unknown("Unknown");

    private String mQualityString;
    private int mQualityValue;

    PrintQuality(String quality) {
        mQualityString = quality;
        switch (quality) {
            case "Normal":
                mQualityValue = 0;
                break;
            case "Best":
                mQualityValue = 1;
                break;
            case "Draft":
                mQualityValue = 2;
                break;
            case "MaxDpi":
                mQualityValue = 3;
                break;
            default:
                mQualityValue = 4;
                break;
        }
    }

    PrintQuality(int value) {
        if(value <= 3 && value >= 0) {
            mQualityValue = value;
        } else {
            mQualityValue = 4;
        }
        switch (value) {
            case 0:
                mQualityString = "Normal";
                break;
            case 1:
                mQualityString = "Best";
                break;
            case 2:
                mQualityString = "Draft";
                break;
            case 3:
                mQualityString = "MaxDpi";
                break;
            default:
                mQualityString = "Unknown";
                break;
        }
    }

    @Override
    public String toString() {
        return this.mQualityString;
    }
}
