/**
 * 位移目标对象 <br>
 * 移动目的坐标
 * 
 * @ClassName: MoveTarget
 * @Description: 位移目标对象
 * @author: Bruce Young
 * @date: 2020年02月02日 17:47
 */
public class MoveTarget {
	private int x;
	private int y;
	private boolean arrived = false;// 是否到达目标点

	public MoveTarget(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	/**获取抵达状态
	 * @return
	 */
	public boolean isArrived() {
		return arrived;
	}


	/**
	 * 设置抵达状态
	 * @param arrived
	 */
	public void setArrived(boolean arrived) {
	
		this.arrived = arrived;
	}
}
