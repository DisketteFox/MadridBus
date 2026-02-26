package dev.diskettefox.madridbus.api;

import com.google.gson.annotations.SerializedName;

public class TimeRequest {
    @SerializedName("cultureInfo")
    private String cultureInfo = "ES";

    @SerializedName("Text_StopRequired_YN")
    private String textStopRequiredYN = "Y";

    @SerializedName("Text_EstimationsRequired_YN")
    private String textEstimationsRequiredYN = "Y";

    @SerializedName("Text_IncidencesRequired_YN")
    private String textIncidencesRequiredYN = "N";

    @SerializedName("DateTime_Referenced_Incidencies_YYYYMMDD")
    private String dateTimeReferencedIncidenciesYYYYMMDD = "20260219";

    public TimeRequest() {

    }

    public static TimeRequest get() {
        return new TimeRequest();
    }
}