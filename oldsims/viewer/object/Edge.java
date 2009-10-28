package viewer.object;

import rescuecore.RescueConstants;

public abstract class Edge extends MotionlessObject {
  public Edge(int id) { super(id); }

  private int m_head;
  private int m_tail;
  private int m_length;

  public PointObject head() { return (PointObject) WORLD.get(m_head); }
  public PointObject tail() { return (PointObject) WORLD.get(m_tail); }
  public int length() { return m_length; }

  public void setHead(int value)   { m_head = value; }
  public void setTail(int value)   { m_tail = value; }
  public void setLength(int value) { m_length = value; }

  public void input(String property, int[] value) {
      if ("HEAD".equals(property)) {
          setHead(value[0]);
      }
      if ("TAIL".equals(property)) {
          setTail(value[0]);
      }
      if ("LENGTH".equals(property)) {
          setLength(value[0]);
      }
      super.input(property, value);
  }

  public int x() { return (head().x() + tail().x()) / 2; }
  public int y() { return (head().y() + tail().y()) / 2; }

  public boolean isAdjacentTo(MotionlessObject obj) { return m_head == obj.id  || m_tail == obj.id; }
}
