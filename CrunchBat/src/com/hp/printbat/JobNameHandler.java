package com.hp.printbat;

/**
 * Created by Juhan on 2016/7/25.
 */
public class JobNameHandler {
    private static final String TAG = JobNameHandler.class.getSimpleName();

    private String mPrinterName;
    private String mFileName;
    private String mPaperSize;
    private PrintQuality mPrintQuality;
    private PaperType mPaperType;
    private int mPageNumber;

    public String getPrinterName() {
        return mPrinterName;
    }

    public void setPrinterName(String printerName) {
        mPrinterName = printerName;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getPaperSize() {
        return mPaperSize;
    }

    public void setPaperSize(String paperSize) {
        mPaperSize = paperSize;
    }

    public PrintQuality getPrintQuality() {
        return mPrintQuality;
    }

    public void setPrintQuality(PrintQuality printQuality) {
        mPrintQuality = printQuality;
    }

    public PaperType getPaperType() {
        return mPaperType;
    }

    public void setPaperType(PaperType paperType) {
        mPaperType = paperType;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
    }

    public void decode(String jobName) {
        String[] nameHolder = jobName.split("__");
        String[] holder = new String[10];
        mPrinterName = nameHolder[0];
        for(String name : nameHolder){
            if(name.contains("_")){
                if(name.split("_").length >= 4) {
                    holder = name.split("_");
                }
            }
        }
        // Find the paper size
        for(String property : holder) {

        }
    }
}
