package project.mealPlan.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InitializationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean operationsExecuted = false;

    public boolean isOperationsExecuted() {
        return this.operationsExecuted;
    }

    public void setOperationsExecuted(boolean b) {
        this.operationsExecuted = b;
    }
}
