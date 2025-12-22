package no.mika.taskviz.waypoints;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

class TaskDefinitionFormatV2 {

    public String taskType;

    public int version;

    @JsonProperty("t")
    public List<TurnpointV2> turnpoints;

    @JsonProperty("s")
    public StartTypeV2 startType;

    @JsonProperty(value = "g")
    public GoalTypeV2 goalType;

    @JsonProperty("e")
    public int earthModel;

    @JsonProperty("to")
    public String takeoffOpenTime;

    @JsonProperty("tc")
    public String takeoffCloseTime;

    public static class TurnpointV2 {
        @JsonProperty(value = "z", required = true)
        public String coordinates;

        @JsonProperty(value = "n", required = true)
        public String name;

        @JsonProperty("d")
        public String description;

        @JsonProperty("t")
        public int type;
    }

    public static class StartTypeV2 {
        @JsonProperty(value = "g", required = true)
        public List<String> times;

        @JsonProperty("d")
        public int entryOrExit;

        @JsonProperty(value = "t", required = true)
        public int type;
    }

    public static class GoalTypeV2 {
        @JsonProperty("d")
        public String deadline;

        @JsonProperty("t")
        public int type;
    }
}
