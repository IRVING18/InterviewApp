package demo.layout.com.testapplicatiion.view;

import android.animation.TypeEvaluator;
import android.graphics.Point;

public class PointEvaluator implements TypeEvaluator {

  @Override
  public Object evaluate(float fraction, Object startValue, Object endValue) {
    Point startPoint = (Point) startValue;
    Point endPoint = (Point) endValue;
    int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
    int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
    Point point = new Point(x, y);
    return point;
  }
}