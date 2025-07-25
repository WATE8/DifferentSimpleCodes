package CompetitionManagement;


import java.util.*;

public class Event {
    private String title;
    private String date;
    private String location;
    private List<Team> teams = new ArrayList<>();
    private List<String> prizes = new ArrayList<>();
    private Set<String> subscribers = new HashSet<>();

    public Event(String title, String date, String location) {
        this.title = title;
        this.date = date;
        this.location = location;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getLocation() { return location; }

    public List<Team> getTeams() { return teams; }
    public void addTeam(Team team) { teams.add(team); }

    public List<String> getPrizes() { return prizes; }
    public void setPrizes(List<String> prizes) { this.prizes = prizes; }

    public Set<String> getSubscribers() { return subscribers; }
}
