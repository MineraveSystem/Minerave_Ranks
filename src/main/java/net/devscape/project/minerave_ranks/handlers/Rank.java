package net.devscape.project.minerave_ranks.handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rank {

    private String name;
    private int weight;
    private String prefix;
    private String suffix;
    private String color;
    private boolean isAdmin;

    public Rank(String name, int weight, String prefix, String suffix, String color, boolean isAdmin) {
        this.name = name;
        this.weight = weight;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.isAdmin = isAdmin;
    }
}
