package ru.vsu.cs.dorofeyeva_s_v.curve;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class BezierCurve {
    private final ArrayList<Point2D> points;
    private final int n;

    public BezierCurve(ArrayList<Point2D> points) {
        this.points = points;
        this.n = points.size() - 1;
    }

    public void drawBezierCurve(GraphicsContext graphicsContext) {
        int quantitySteps = 100;
        double[] xPoints = new double[quantitySteps];
        double[] yPoints = new double[quantitySteps];

        for (int i = 0; i < quantitySteps; i++) {
            double t = (double) i / (quantitySteps - 1);
            xPoints[i] = calculateСoordinate(points, t, true);
            yPoints[i] = calculateСoordinate(points, t, false);
        }

        graphicsContext.setLineWidth(3);
        graphicsContext.strokePolyline(xPoints, yPoints, quantitySteps);
    }

    private double calculateСoordinate(ArrayList<Point2D> points, double t, boolean isX) {
        double sum = 0;
        if (isX) {
            for (int k = 0; k <= n; k++) {
                sum += points.get(k).getX() * c(k, n) * Math.pow(t, k) * Math.pow(1 - t, n - k);
            }
        } else {
            for (int k = 0; k <= n; k++) {
                sum += points.get(k).getY() * c(k, n) * Math.pow(t, k) * Math.pow(1 - t, n - k);
            }
        }
        return sum;
    }

    private static double c(int k, int n) {
        if (k > n - k) {
            k = n - k;
        }

        double c = 1;
        for (int i = 1; i <= k; i++) {
            c = c * (n - (k - i)) / i;
        }
        return c;
    }
}