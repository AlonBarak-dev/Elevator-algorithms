package ex0.algo;

public class FinalScore {

    private double finalScore;
    private int ElevatorNumber;

    public FinalScore(double finalScore, int elevatorNumber){
        this.finalScore = finalScore;
        this.ElevatorNumber = elevatorNumber;
    }

    public double getFinalScore(){
        return this.finalScore;
    }

    public void setFinalScore(double newFinalScore){
        this.finalScore = newFinalScore;
    }

    public int getElevatorNumber(){
        return this.ElevatorNumber;
    }

    public void setElevatorNumber(int newElevatorNumber){
        this.ElevatorNumber = newElevatorNumber;
    }

}
