package ru.vsu.cs.dorofeyeva_s_v.curve;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class BezierCurveController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private ArrayList<Point2D> points = new ArrayList<>();
    private int selectedPointIndex = -1; // индекс точки, выбранной для перемещения
    private static final int POINT_RADIUS = 5;
    private static final int DRAG_RADIUS = 10;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(this::handleMouseClicked);
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(event -> selectedPointIndex = -1);
    }

    private void handleMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && selectedPointIndex == -1) {
            // если рядом нет существующей точки, добавляем новую
            int nearbyIndex = findNearbyPointIndex(event.getX(), event.getY());
            if (nearbyIndex == -1) {
                Point2D clickPoint = new Point2D(event.getX(), event.getY());
                points.add(clickPoint);
                redrawCanvas();
            }
        }
    }

    private void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            // находим индекс ближайшей к нажатию точки
            selectedPointIndex = findNearbyPointIndex(event.getX(), event.getY());
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (selectedPointIndex != -1) {
            // обновляем координаты только выбранной точки
            Point2D newPoint = new Point2D(event.getX(), event.getY());
            points.set(selectedPointIndex, newPoint);  // перезаписываем только выбранную точку
            redrawCanvas();
        }
    }

    private int findNearbyPointIndex(double x, double y) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).distance(x, y) <= DRAG_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private void redrawCanvas() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Point2D p : points) {
            graphicsContext.fillOval(
                    p.getX() - POINT_RADIUS, p.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        if (points.size() > 1) {
            BezierCurve b = new BezierCurve(points);
            b.drawBezierCurve(graphicsContext);
        }
    }
}
