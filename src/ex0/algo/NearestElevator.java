package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.Comparator;
import java.util.PriorityQueue;

public class NearestElevator implements ElevatorAlgo {


    Comparator<Node> scoreComperator = new Comparator<Node>(){
        @Override
        public int compare(Node n1, Node n2) {
            return (int) (n1.callScore - n2.callScore);
        }
    };
    private Building _building;
    private PriorityQueue<Node>[] callsPr;

    public NearestElevator(Building b) {
        _building = b;
        int size = this._building.numberOfElevetors();
        this.callsPr = new PriorityQueue[size];
        for(int i = 0; i <size;i++){
            callsPr[i] = new PriorityQueue<Node>(scoreComperator);
        }
    }

    @Override
    public Building getBuilding() {
        return _building;
    }

    @Override
    public String algoName() {
        return "Ex0_NearestElevator";
    }


    /** Auxiliary method for allocateAnElevator function start here: */
    private double suitabilityScore(CallForElevator c, Elevator e){

        int currentFloor = e.getPos();
        int distance = Math.abs(c.getSrc() - currentFloor);
        int floors = this._building.maxFloor() - this._building.minFloor();
        double fs = floors + 2;
        double time= calcTime(c,e);

        if(inMyDirection(c,e)){
            if (c.getSrc() == e.getPos()){
                if (c.getType() == e.getState()){
                    fs = 1;
                }
                else{
                    if (callsPr[e.getID()].peek() != null)
                        fs = 1+ Math.abs(callsPr[e.getID()].peek().c.getDest() - c.getDest());
                    else
                        fs = 1;
                }
            }
            else if(c.getType() == e.getState()){
                fs = 1 + distance;
            }
            else {
                fs = callsPr[e.getID()].size();
            }
        }
        if (this._building.numberOfElevetors() > 1 && busiestEl(e)){
            fs = fs + callsPr[e.getID()].size();
        }
        return fs * time;
    }

    private boolean busiestEl(Elevator e){
        int length = callsPr.length, maxSize = -1, elevator = 0;
        for(int i = 0; i< length; i++){
            if (callsPr[i].size() > maxSize){
                maxSize = callsPr[i].size();
                elevator = i;
            }
        }
        if (e.getID() == elevator)
            return true;
        return false;
    }

    private double calcTime(CallForElevator c, Elevator e){

        int distSrc = Math.abs(e.getPos()-c.getSrc());
        int distDest = Math.abs(c.getSrc() - c.getDest());
        double speed = e.getSpeed();
        double openTime = e.getTimeForOpen();
        double closeTime = e.getTimeForClose();
        double startTime = e.getStartTime();
        double stopTime = e.getStopTime();
        double timeSrc = startTime + distSrc/speed + stopTime + openTime + closeTime;
        double timeDest = startTime + distDest/speed + stopTime + openTime + closeTime;

        if(e.getState() == c.getType()){
            timeSrc = timeSrc - startTime;
        }
        else{
            timeSrc = timeSrc - startTime + stopTime;
        }
        if (distSrc == 0){
            timeSrc = openTime + closeTime;
        }

        return timeSrc + timeDest;
    }

    private boolean inMyDirection(CallForElevator c, Elevator e){

        int currentElFloor = e.getPos();
        int callFloor = c.getSrc();

        if (currentElFloor == callFloor)
            return true;

        if (e.getState() == 0 && callsPr[e.getID()].size() == 0)
            return true;

        if(currentElFloor < callFloor){
            if(c.getDest() < currentElFloor){
                return false;
            }
            return true;
        }
        else{
            if(c.getDest() > currentElFloor){
                return false;
             }
            return true;
        }
    }

    private int findMin(FinalScore[] array) {
        double  currmin = array[0].getFinalScore();
        int minElevator = 0;
        for(int i = 1; i < array.length;i++){
            if (array[i].getFinalScore() < currmin){
                currmin = array[i].getFinalScore();
                minElevator = array[i].getElevatorNumber();
            }
        }

        return minElevator;
    }

    private void addToQueue(CallForElevator c, Elevator e){
        int numOfElev = e.getID();
        double callScore = calcTime(c,e);
        Node n = new Node(callScore,c);
        callsPr[numOfElev].add(n);

    }

        /** and ENDS here: */


    @Override
    public int allocateAnElevator(CallForElevator c) {

        int k = this._building.numberOfElevetors();
        FinalScore[] array = new FinalScore[k];

        for(int i = 0; i < k; i++){        //inserting scores for each elevator
            array[i] = new FinalScore(suitabilityScore(c,this._building.getElevetor(i)), i);
        }
        int bestElevator = findMin(array);
        addToQueue(c,this._building.getElevetor(bestElevator));
        cmdElevator(bestElevator);
        return bestElevator;
    }



    @Override
    public void cmdElevator(int elev) {

            if (callsPr[elev].peek() != null) {
                    if (callsPr[elev].peek().c.getSrc() == this._building.getElevetor(elev).getPos()) {
                        this._building.getElevetor(elev).stop(this._building.getElevetor(elev).getPos());
                        this._building.getElevetor(elev).goTo(callsPr[elev].remove().c.getDest());
                    } else {
                        this._building.getElevetor(elev).goTo(callsPr[elev].peek().c.getSrc());
                        this._building.getElevetor(elev).goTo(callsPr[elev].peek().c.getDest());
                    }
            }
    }


/**
 * if (callsPr[elev].peek() != null) {
        int src1 = callsPr[elev].peek().getSrc();
        int dest1 = callsPr[elev].remove().getDest();
        _building.getElevetor(elev).goTo(src1);
        _building.getElevetor(elev).goTo(dest1);
        if (callsPr[elev].peek() != null) {
            int src2 = callsPr[elev].peek().getSrc();
            int dest2 = callsPr[elev].peek().getDest();

            if (src1 < src2 && src2 < dest1) {
                if (src1 - dest1 < 0) {

                    callsPr[elev].remove();
                    _building.getElevetor(elev).stop(src1);
                    if (src2 < dest2 && dest2 < dest1) {
                        _building.getElevetor(elev).goTo(dest1);
                        _building.getElevetor(elev).stop(dest2);
                    }
                    else {
                        _building.getElevetor(elev).goTo(dest2);
                        _building.getElevetor(elev).stop(dest1);
                     }
                 }
                else {
                    callsPr[elev].remove();
                    _building.getElevetor(elev).stop(src1);
                    if (dest1 < dest2 && dest2 < src2) {
                        _building.getElevetor(elev).goTo(dest1);
                        _building.getElevetor(elev).stop(dest2);
                    }
                    else {
                         _building.getElevetor(elev).goTo(dest2);
                         _building.getElevetor(elev).stop(dest1);
                        }
                }
             }
         }
 }
  */



}
