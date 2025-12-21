package no.mika.taskviz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TaskDefinitionFormatV2 {

    public String taskType;

    public int version;

    @JsonProperty("t")
    public List<Turnpoint> turnpoints;

    @JsonProperty("s")
    public StartType startType;

    @JsonProperty(value = "g")
    public GoalType goalType;

    @JsonProperty("e")
    public int earthModel;

    @JsonProperty("to")
    public String takeoffOpenTime;

    @JsonProperty("tc")
    public String takeoffCloseTime;

    public static class Turnpoint {
        @JsonProperty(value = "z", required = true)
        public String coordinates;

        @JsonProperty(value = "n", required = true)
        public String name;

        @JsonProperty("d")
        public String description;

        @JsonProperty("t")
        public int type;
    }

    public static class StartType {
        @JsonProperty(value = "g", required = true)
        public List<String> times;

        @JsonProperty("d")
        public int entryOrExit;

        @JsonProperty(value = "t", required = true)
        public int type;
    }

    public static class GoalType {
        @JsonProperty("d")
        public String deadline;

        @JsonProperty("t")
        public int type;
    }
}
