package common;


import application.MyApp;

import java.io.File;



public class Constants {

    public static final String PATH_CACHE="http-cache";
    public static final String PATH_IMG= MyApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "image";
    public static final String APP_KEY="06090cd9a8910c1cc0e312cc37c5d440";
    public static final int  CODE_CALENDAR=101;
    public static final String EID="eid";
    public static final String HISTORY_BEAN="history_bean";
    public static final String DATE="date";
    public static final int NUM_PAGE=16;
    public static final String URL_IMG="img_url";
    public static final String DATA="data";
    public static final String POSITION="position";

}
