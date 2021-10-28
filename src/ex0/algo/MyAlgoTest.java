package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;
import ex0.simulator.Simulator_A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyAlgoTest {
    Building b1;
    MyAlgo algo1;
    Elevator elev1;
    CallForElevator call;

    Building b3;
    MyAlgo algo3;
    Elevator elev3;
    CallForElevator call3;

    public MyAlgoTest(){
        Simulator_A.initData(0, null);
        b1 = Simulator_A.getBuilding();
        algo1 = new MyAlgo(b1);
        elev1 = b1.getElevetor(0);
        call = new CallForElevator() {
            @Override
            public int getState() {
                return 0;
            }

            @Override
            public double getTime(int state) {
                return 0;
            }

            @Override
            public int getSrc() {
                return 10;
            }

            @Override
            public int getDest() {
                return 50;
            }

            @Override
            public int getType() {
                return 1;
            }

            @Override
            public int allocatedTo() {
                return 0;
            }
        };


        Simulator_A.initData(9,null);
        b3 = Simulator_A.getBuilding();
        algo3 = new MyAlgo(b1);
        elev3 = b1.getElevetor(0);
        call3 = new CallForElevator() {
            @Override
            public int getState() {
                return 0;
            }

            @Override
            public double getTime(int state) {
                return 0;
            }

            @Override
            public int getSrc() {
                return 10;
            }

            @Override
            public int getDest() {
                return 50;
            }

            @Override
            public int getType() {
                return 1;
            }

            @Override
            public int allocatedTo() {
                return 0;
            }
        };


    }


    @Test
    void getBuildingTest(){
        Building b2 = algo1.getBuilding();
        assertEquals(b2.numberOfElevetors(),b1.numberOfElevetors());
        assertEquals(b2.minFloor(), b1.minFloor());
        assertEquals(b2.maxFloor(), b1.maxFloor());
    }

    @Test
    void algoNameTest(){
        assertEquals("Ex0_MyAlgo", algo1.algoName());
    }

    @Test
    void allocateAnElevatorTest(){
        int floor = algo1.allocateAnElevator(call);
        assertEquals(0,floor);



    }



}