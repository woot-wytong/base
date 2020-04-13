package com.wyt.woot_base.traffic;

public class TrafficInfo {

    private long startTime;
    private long stopTime;
    private long startFlow;
    private long stopFlow;
    private long allFlow;
    private long allTime;
    private double avgFlow;
    private double maxFlow;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public long getStartFlow() {
        return startFlow;
    }

    public void setStartFlow(long startFlow) {
        this.startFlow = startFlow;
    }

    public long getStopFlow() {
        return stopFlow;
    }

    public void setStopFlow(long stopFlow) {
        this.stopFlow = stopFlow;
    }

    public long getAllFlow() {
        return allFlow;
    }

    public void setAllFlow(long allFlow) {
        this.allFlow = allFlow;
    }

    public long getAllTime() {
        return allTime;
    }

    public void setAllTime(long allTime) {
        this.allTime = allTime;
    }

    public double getAvgFlow() {
        return avgFlow;
    }

    public void setAvgFlow(double avgFlow) {
        this.avgFlow = avgFlow;
    }

    public double getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(double maxFlow) {
        this.maxFlow = maxFlow;
    }

    public double getMaxFlow(SpeedUnit unit) {
        double max = maxFlow;
        switch (unit) {
            case Mbitps:
                max = Double.parseDouble(SpeedUnit.Mbitps.formatValue(maxFlow / 128));
                break;
            case Kbitps:
                max = Double.parseDouble(SpeedUnit.Kbitps.formatValue(maxFlow * 8));
                break;
            case MBps:
                max = Double.parseDouble(SpeedUnit.MBps.formatValue(maxFlow / 1024));
                break;
            case KBps:
                max = Double.parseDouble(SpeedUnit.KBps.formatValue(maxFlow));
                break;
        }
        return max;
    }

    public double getAvgFlow(SpeedUnit unit){
        double avg = avgFlow;
        switch (unit) {
            case Mbitps:
                avg = Double.parseDouble(SpeedUnit.Mbitps.formatValue(avgFlow / 128));
                break;
            case Kbitps:
                avg = Double.parseDouble(SpeedUnit.Kbitps.formatValue(avgFlow * 8));
                break;
            case MBps:
                avg = Double.parseDouble(SpeedUnit.MBps.formatValue(avgFlow / 1024));
                break;
            case KBps:
                avg = Double.parseDouble(SpeedUnit.KBps.formatValue(avgFlow));
                break;
        }
        return avg;
    }

}
