package vdm.ivanhoe;

import vdm.ivanhoe.raytracer.*;
import vdm.ivanhoe.raytracer.classes.*;
import vdm.ivanhoe.raytracer.classes.Color;
import vdm.ivanhoe.raytracer.interfaces.Intersectable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 *  JavaRayTracer
 * (c) 2017, code by Ivan "VanDamM" Kalininskiy
 * modified by E.G. 2022
 */
public class Main {

    private static final int WIDTH =800;
    private static final int HEIGHT = 800;

    public static void main(String[] args) {
        String title = "JavaRayTracer by Ivan \"VanDamM\" Kalininskiy (2017)";



        RayTracer rt = new RayTracer();
        long startNanoSeconds, endNanoSeconds;

        //using Jframe
        ImageFrame frame = new ImageFrame(title, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Graphics graphics = frame.getGraphics();
        startNanoSeconds = System.nanoTime();
        rt.renderToJFrame(defaultScene(), graphics, WIDTH, HEIGHT);
        endNanoSeconds = System.nanoTime();
        graphics.dispose();

        System.out.println("Result 1 : " + getSecond(startNanoSeconds, endNanoSeconds) + " seconds");
          //writing to file
        BufferedImage bufferedImage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        startNanoSeconds = System.nanoTime();
        rt.renderToImage(defaultScene(), g2d,WIDTH,HEIGHT,bufferedImage,"nice");
        endNanoSeconds = System.nanoTime();
        g2d.dispose();
        System.out.println("Result 2 : " + getSecond(startNanoSeconds, endNanoSeconds) + " seconds");
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

    }

    /**
     *  With convert NanoSecond To Second
     */
    public static double getSecond(long startNanoSeconds, long endNanoSeconds) {
        return (double) (endNanoSeconds - startNanoSeconds) / 1000000000.0;
    }

    /**
     *  Return Scene with 2 shiny Sphere, 1 checkerboard Plane and 4 lights
     */
    public static Scene defaultScene() {
        Surfaces surface = new Surfaces();
        Surfaces.CheckerBoardSurface checkerBoardSurface = surface.new CheckerBoardSurface();
        Surfaces.ShinySurface shinySurface = surface.new ShinySurface();

        // Array with One checkerboard plane and Two Sphere
        Intersectable[] intersectables = {
                new Plane(new Vector(0.0, 1.0, 0.0), 0.0, checkerBoardSurface),
                new Sphere(new Vector(-0.5, 1.0, -0.25), 1.0, shinySurface),
                new Sphere(new Vector(-1.0, 0.5, 1.5), 0.5, shinySurface)
        };

        // Colored dot lights array
        Light[] lights = {
                new Light(new Vector(-2.0, 2.5, 0.0), new Color(0.49, 0.07, 0.07)),
                new Light(new Vector(1.5, 2.5, 1.5), new Color(0.07, 0.07, 0.49)),
                new Light(new Vector(1.5, 2.5, -1.5), new Color(0.07, 0.49, 0.071)),
                new Light(new Vector(0.0, 3.5, 0.0), new Color(0.21, 0.21, 0.35))
        };

        return new Scene(intersectables, lights, new Camera(new Vector(3, 2, 4.0), new Vector(-1.0, 0.5, 0.0)));
    }

    static class ImageFrame extends JFrame {
        ImageFrame(String title, int width, int height) {
            setTitle(title);
            setSize(width, height);
        }
    }
}
