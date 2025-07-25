package CompetitionManagement;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<String> participants;

    public Team(String name) {
        this.name = name;
        this.participants = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<String> getParticipants() { return participants; }

    public void addParticipant(String name) {
        participants.add(name);
    }
}
