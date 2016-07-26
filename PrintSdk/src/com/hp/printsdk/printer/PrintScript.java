package com.hp.printsdk.printer;

public class PrintScript {
    private static final String TAG = PrintScript.class.getSimpleName();

    /**
     * These variables can be edited
     */
    private String mGhostScript = "gs";
    private String mIjsServer = "-sIjsServer=hpijs";
    private String mIjsParams = "-sIjsParams=\"Quality:Quality=2,Quality:ColorMode=2,Quality:MediaType=2,Quality:PenSet=2\" ";
    private String mResolution = "-r300x300 ";
    private String mOutputFile = "-sOutputFile=\"/dev/usb/lp0\" ";
    private String mInputFile = "$1 ";

    /**
     * Total command contains parts of these constants
     */
    private static final String DEVICE = "-sDEVICE=ijs ";
    private static final String IJS_OUTPUTFD = "-dIjsUseOutputFD ";
    private static final String DEVICE_MANUFACTURER = "-sDeviceManufacturer=\"HEWLETT-PACKARD\" ";
    private static final String DEVICE_MODEL = "-sDeviceModel=\"deskjet 5550\" ";
    private static final String DEFAULT_PARAM = "-dPDFFitPage -dNOPAUSE -dSAFER ";
    private static final String DEFAULT_TAIL = "-c quit";

    private String mPrintCommand = mGhostScript + DEVICE + mIjsServer + mIjsParams +
            IJS_OUTPUTFD + DEVICE_MANUFACTURER + DEVICE_MODEL + mResolution + DEFAULT_PARAM +
            mOutputFile + mInputFile + DEFAULT_TAIL;

    private String mFilePath = "";

    public PrintScript(String filePath) {
        mFilePath = filePath;
        mGhostScript = mFilePath + "gs ";
        mIjsServer = "-sIjsServer=" + mFilePath + "hpijs ";
    }

    public void setPrintQuality(int printQuality) {
        setParam("Quality", printQuality);
    }

    public void setColorMode(int colorMode) {
        setParam("ColorMode", colorMode);
    }

    public void setMediaType(int mediaType) {
        setParam("MediaType", mediaType);
    }

    public void setPenSet(int penSet) {
        setParam("PenSet", penSet);
    }

    public void setIjsParams(int printQuality, int colorMode, int mediaType, int penSet) {
        setPrintQuality(printQuality);
        setColorMode(colorMode);
        setMediaType(mediaType);
        setPenSet(penSet);
        Log.d(TAG, "IjsParams: " + mIjsParams);
    }

    private void setParam(String ijsParams, int param) {
        ijsParams = ijsParams + "=";
        int i;
        if ((i = mIjsParams.indexOf(ijsParams)) != -1) {
            i += ijsParams.length();
            mIjsParams = (mIjsParams.substring(0, i) + param + mIjsParams.substring(i + 1));
        } else {
            Log.e(TAG, "Wrong parameter! - " + ijsParams);
        }
        updateTotalCmd();
    }

    public void setResolution(int resolution) {
        mResolution = ("-r" + resolution + "x" + resolution + " ");
        updateTotalCmd();
        Log.d(TAG, "Resolution: " + mResolution);
    }

    public void setOutput(String output) {
        mOutputFile = ("-sOutputFile=\"" + output + "\" ");
        updateTotalCmd();
        Log.d(TAG, "OutputFile: " + mOutputFile);
    }

    public void setInputFile(String inputFile) {
        mInputFile = (inputFile + " ");
        updateTotalCmd();
        Log.d(TAG, "InputFile: " + mInputFile);
    }

    public String getPrintCommand() {
        return mPrintCommand;
    }

    private void updateTotalCmd() {
        mPrintCommand = mGhostScript + DEVICE + mIjsServer + mIjsParams +
                IJS_OUTPUTFD + DEVICE_MANUFACTURER + DEVICE_MODEL + mResolution + DEFAULT_PARAM +
                mOutputFile + mInputFile + DEFAULT_TAIL;
    }

    public void exec() {
        ShellUtils.execCommand(mPrintCommand, true);
    }
}

