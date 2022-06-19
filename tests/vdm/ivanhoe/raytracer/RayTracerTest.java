package vdm.ivanhoe.raytracer;

import org.junit.jupiter.api.Test;
import vdm.ivanhoe.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static vdm.ivanhoe.Main.defaultScene;
import static vdm.ivanhoe.Main.getSecond;

class RayTracerTest {
    private static final int WIDTH =800;
    private static final int HEIGHT = 800;
    long startNanoSeconds, endNanoSeconds;
    RayTracer rt = new RayTracer();

    @Test
    void renderToJFrame() {

            String title = "JavaRayTracer by Ivan \"VanDamM\" Kalininskiy (2017)";


            //using Jframe
            JFrame frame = new JFrame(title);
            frame.setSize(WIDTH,HEIGHT);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);

            Graphics graphics = frame.getGraphics();
            startNanoSeconds = System.nanoTime();
            rt.renderToJFrame(defaultScene(), graphics, WIDTH, HEIGHT);
            endNanoSeconds = System.nanoTime();
            graphics.dispose();
            System.out.println("Result : " + getSecond(startNanoSeconds, endNanoSeconds) + " seconds");
//            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

    }

    @Test
    void renderToImage() {
        //writing to file
        BufferedImage bufferedImage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        startNanoSeconds = System.nanoTime();
        rt.renderToImage(defaultScene(), g2d,WIDTH,HEIGHT,bufferedImage,"nice");
        endNanoSeconds = System.nanoTime();
        g2d.dispose();
        System.out.println("Result : " + getSecond(startNanoSeconds, endNanoSeconds) + " seconds");


    }
}