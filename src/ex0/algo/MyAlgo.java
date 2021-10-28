package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.LinkedList;

public class MyAlgo implements ElevatorAlgo{

    private Building _building;
    private LinkedList<NodeD>[] callList;

    public MyAlgo(Building b){
        this._building = b;
        int size = this._building.numberOfElevetors();
        this.callList = new LinkedList[size];
        for (int i = 0; i < size; i++){
            callList[i] = new LinkedList<NodeD>();
        }
    }



    @Override
    public Building getBuilding() {
        return this._building;
    }

    @Override
    public String algoName() {
        return "Ex0_MyAlgo";
    }

    @Override
    public int allocateAnElevator(CallForElevator c) {
        int bestElevator = 0;
        NodeD src = new NodeD(c.getSrc(),c);
        NodeD dest = new NodeD(c.getDest(),c);
        double minTime = Integer.MAX_VALUE;

        for (int i = 0; i < callList.length; i++ ){
            double time = calcTime(src.floor, dest.floor, callList[i],this._building.getElevetor(i));
            if(time < minTime){
                minTime = time;
                bestElevator = i;
            }
        }

        addCall(src.floor,dest.floor,c,callList[bestElevator],this._building.getElevetor(bestElevator));
        cmdElevator(bestElevator);
        return bestElevator;
    }



    private double calcTime(int src, int dest, LinkedList<NodeD> road, Elevator e){
        double time = 0;
        double timeToSrc = 0, timeToDest = 0;
        double startTime = e.getStartTime(), stopTime = e.getStopTime(), openTime = e.getTimeForOpen(), closeTime = e.getTimeForClose(), speed = e.getSpeed();
        int pos = e.getPos();
        if (road.size() == 0){
            int distanceSrc = Math.abs(src - pos), distanceDest = Math.abs(dest - src);
            timeToSrc = startTime + distanceSrc/speed + stopTime + openTime + closeTime;
            timeToDest = startTime + distanceDest/speed + stopTime + openTime + closeTime;
            return time = timeToSrc + timeToDest;
        }

        /////// Find place to src Node and compute its time

        int index = 0, floorSrc = 0;
        boolean findSrc = false;
        for(index = 0; index<road.size()-1 && !findSrc;index++){
            if (road.get(index).floor > road.get(index+1).floor && road.get(index).floor >= src && src >= road.get(index + 1).floor){
                floorSrc += Math.abs(src - road.get(index).floor);
                index++;
                findSrc = true;
                break;
            }
            if (road.get(index).floor < road.get(index+1).floor && road.get(index).floor <= src && src <= road.get(index + 1).floor){
                floorSrc += Math.abs(src - road.get(index).floor);
                index++;
                findSrc = true;
                break;
            }
            if (road.get(index).floor == src){
                index++;
                floorSrc += 0;
                findSrc = true;
                break;
            }
            if (road.get(index).floor > road.get(index + 1).floor && road.get(index).floor <= src){
                floorSrc += Math.abs(road.get(index).floor - road.get(index+1).floor);
                index++;
            }
            else if (road.get(index).floor < road.get(index + 1).floor && road.get(index).floor >= src){
                floorSrc += Math.abs(road.get(index).floor - road.get(index+1).floor);
                index++;
            }
        }

        timeToSrc = floorSrc/speed + index*(startTime + stopTime + openTime + closeTime);

        if (index == road.size() - 1 && !findSrc){
            findSrc = true;
            floorSrc += Math.abs(road.get(index).floor - src);
            index++;

            int dist = Math.abs(src - dest);
            timeToDest = dist/speed + startTime + stopTime + openTime + closeTime;
            return time = timeToSrc + timeToDest;
        }

        //////////// Find a place for dest and compute its time
        boolean findDest = false;
        int floorDest = 0, index2 = index;
        for (index2 = index; index2 < road.size() - 1 && !findDest;index2++){
            if (road.get(index2).floor > road.get(index2+1).floor && road.get(index2).floor >= dest && dest >= road.get(index2 + 1).floor){
                floorDest += Math.abs(dest - road.get(index2).floor);
                index2++;
                findDest = true;
                break;
            }
            if (road.get(index2).floor < road.get(index2+1).floor && road.get(index2).floor <= dest && dest <= road.get(index2 + 1).floor){
                floorDest += Math.abs(dest - road.get(index2).floor);
                index2++;
                findDest = true;
                break;
            }
            if (road.get(index2).floor == dest){
                index2++;
                floorDest += 0;
                findDest = true;
                break;
            }
            if (road.get(index2).floor > road.get(index2 + 1).floor && road.get(index2).floor <= dest){
                floorDest += Math.abs(road.get(index2).floor - road.get(index2+1).floor);
                index2++;
            }
            else if (road.get(index2).floor < road.get(index2 + 1).floor && road.get(index2).floor >= dest){
                floorDest += Math.abs(road.get(index2).floor - road.get(index2+1).floor);
                index2++;
            }
        }

        if (index2 == road.size()-1 && !findDest){
            findDest = true;
            floorDest += Math.abs(road.get(index2).floor);
            index2++;
        }

        timeToDest = floorDest/speed + (index2-index)*(startTime + stopTime + openTime + closeTime);

        time = timeToSrc + timeToSrc;

        return time;
    }

    private void addCall(int src, int dest,CallForElevator c ,LinkedList<NodeD> road, Elevator e){

        if (road.size() == 0){
            road.addFirst(new NodeD(src,c));
            road.addLast(new NodeD(dest,c));
            return;
        }
        int index = 0;
        boolean findSrc = false;
        for(index = 0; index<road.size()-1 && !findSrc;index++){
            if (road.get(index).floor > road.get(index+1).floor && road.get(index).floor >= src && src >= road.get(index + 1).floor){
                index++;
                findSrc = true;
                road.add(index,new NodeD(src,c));
                break;
            }
            if (road.get(index).floor < road.get(index+1).floor && road.get(index).floor <= src && src <= road.get(index + 1).floor){
                index++;
                findSrc = true;
                road.add(index,new NodeD(src,c));
                break;
            }
            if (road.get(index).floor == src){
                index++;
                findSrc = true;
                road.add(index,new NodeD(src,c));
                break;
            }
            if (road.get(index).floor > road.get(index + 1).floor && road.get(index).floor <= src){
                index++;
            }
            else if (road.get(index).floor < road.get(index + 1).floor && road.get(index).floor >= src){
                index++;
            }
        }


        if (index == road.size() - 1 && !findSrc){
            findSrc = true;
            index++;
            road.add(index,new NodeD(src,c));
            road.add(index+1,new NodeD(dest,c));
            return;
        }

        //////////// Find a place for dest and compute its time
        boolean findDest = false;
        int  index2 = index;
        for (index2 = index; index2 < road.size() - 1 && !findDest;index2++){
            if (road.get(index2).floor > road.get(index2+1).floor && road.get(index2).floor >= dest && dest >= road.get(index2 + 1).floor){
                index2++;
                findDest = true;
                road.add(index2,new NodeD(dest,c));
                break;
            }
            if (road.get(index2).floor < road.get(index2+1).floor && road.get(index2).floor <= dest && dest <= road.get(index2 + 1).floor){
                index2++;
                findDest = true;
                road.add(index2,new NodeD(dest,c));
                break;
            }
            if (road.get(index2).floor == dest){
                index2++;
                findDest = true;
                road.add(index2,new NodeD(dest,c));
                break;
            }
            if (road.get(index2).floor > road.get(index2 + 1).floor && road.get(index2).floor <= dest){
                index2++;
            }
            else if (road.get(index2).floor < road.get(index2 + 1).floor && road.get(index2).floor >= dest){
                index2++;
            }
        }

        if (index2 == road.size()-1 && !findDest){
            findDest = true;
            index2++;
            road.add(index2,new NodeD(dest,c));
        }
        return;



    }
    
    @Override
    public void cmdElevator(int elev) {


        if (callList[elev].peek() != null && _building.getElevetor(elev).getState() != Elevator.ERROR) {

            if (callList[elev].peek().c.getState() == 1 && _building.getElevetor(elev).getState() == Elevator.LEVEL) {

                _building.getElevetor(elev).goTo(callList[elev].peek().c.getSrc());
            }
            else if (callList[elev].peek().c.getState() == 2 && _building.getElevetor(elev).getState() == Elevator.LEVEL) {

                _building.getElevetor(elev).goTo(callList[elev].peek().c.getDest());
            }
            else if (callList[elev].peek().c.getState() == 3) {

                callList[elev].remove();
            }
        }

    }
}































































/**  if (road.size() == 1){
 int dirc2 = 0;
 if (e.getPos() > road.peek())
 dirc2 = -1;
 else
 dirc2 = 1;

 if (dirc == dirc2 && dirc == 1){        //case: same dirction -> UP
 if (src <= road.peek()) {       // src is below the peek
 if (src >= e.getPos()) {        //src is above the elevator
 timeSrc = e.getStartTime() + e.getSpeed() / (Math.abs(e.getPos() - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 if (dest <= road.peek())    // dest is below the peek
 timeDest = e.getStartTime() + e.getSpeed() / (Math.abs(dest - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 else {       //dest is above the peek
 timeDest = e.getStartTime() + e.getSpeed() / (Math.abs(road.peek() - dest)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 timeDest += e.getStartTime() + e.getSpeed() / (Math.abs(road.peek() - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 }
 time = timeSrc + timeDest;
 return time;
 }
 else{

 }
 }
 else{      // src is above the peek
 timeSrc = e.getStartTime() + e.getSpeed()/(Math.abs(e.getPos()-road.peek())) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 timeSrc = timeSrc + e.getStartTime() + e.getSpeed()/(Math.abs(road.peek()-src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 timeDest = e.getStartTime() + e.getSpeed() / (Math.abs(dest - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 }
 }
 else if (dirc == dirc2 && dirc == -1) {         // case: same direction -> DOWN
 if (src >= road.peek() ) {       // src is above the peek
 timeSrc = e.getStartTime() + e.getSpeed() / (Math.abs(e.getPos() - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 }
 else {      // src is under the peek
 timeSrc = e.getStartTime() + e.getSpeed() / (Math.abs(e.getPos() - road.peek())) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 timeSrc = timeSrc + e.getStartTime() + e.getSpeed() / (Math.abs(road.peek() - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 }
 }
 else{           // different directions
 timeSrc = e.getStartTime() + e.getSpeed() / (Math.abs(e.getPos() - road.peek())) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 timeSrc = timeSrc + e.getStartTime() + e.getSpeed() / (Math.abs(road.peek() - src)) + e.getStopTime() + e.getTimeForOpen() + e.getTimeForClose();
 }
 }
 */