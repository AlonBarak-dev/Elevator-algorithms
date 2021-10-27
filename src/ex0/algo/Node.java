package ex0.algo;

import ex0.CallForElevator;

public class Node {

    double callScore;
    CallForElevator c;

    public Node(double data, CallForElevator c){
        this.c = c;
        this.callScore = data;
    }
}
