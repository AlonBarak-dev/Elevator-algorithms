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
        Simulator_A.initData(1, null);
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
                return 0;
            }

            @Override
            public int getDest() {
                return 5;
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
        algo3 = new MyAlgo(b3);
        elev3 = b3.getElevetor(9);
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
                return -2;
            }

            @Override
            public int getDest() {
                return 20;
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
        int numEl1 = algo1.allocateAnElevator(call);
        if (numEl1 > b1.numberOfElevetors() || numEl1 < 0)
            fail();

        int numEl2 = algo3.allocateAnElevator(call3);
        if (numEl2 > b3.numberOfElevetors() || numEl2 < 0)
            fail();

    }


    @Test
    void cmdElevator(){
        int elev = algo1.allocateAnElevator(call);
        algo1.cmdElevator(elev);
        int state = b1.getElevetor(elev).getState();
        assertEquals(1,state);

        int elevv = algo3.allocateAnElevator(call3);
        algo3.cmdElevator(elevv);
        int state2 = b3.getElevetor(elevv).getState();
        assertEquals(1,state2);

    }


}