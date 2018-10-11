package org.usfirst.frc.team7157.drive;

public class DriveChar{
    private double ks, kv, ka; //static voltage, voltage/speed unit, voltage/unit of acc

    public DriveChar(double s, double v, double a) {
        ks = s;
        kv = v;
        ka = a;
    }

    public double getVapp(double speed, double accel) {
        return kv*speed + ka * accel + ks;
    }
}