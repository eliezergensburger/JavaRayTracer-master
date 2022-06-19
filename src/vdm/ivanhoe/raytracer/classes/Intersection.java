package vdm.ivanhoe.raytracer.classes;

import vdm.ivanhoe.raytracer.interfaces.Intersectable;

/**
 *  Intersection
 */
public class Intersection {
    private Intersectable intersectable = null;
    private Ray ray = null;
    private double dist = 0;

    Intersection(Intersectable intersectable, Ray ray, double dist) {
        this.intersectable = intersectable;
        this.ray = ray;
        this.dist = dist;
    }

    public Intersectable getThing() {
        return intersectable;
    }

    public Ray getRay() {
        return ray;
    }

    public double getDist() {
        return dist;
    }
}
