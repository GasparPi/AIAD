package agents;

import jade.core.Agent;

public class Employee extends Agent {
    private int id;

    public Employee(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
