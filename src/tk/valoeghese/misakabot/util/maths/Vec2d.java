package tk.valoeghese.misakabot.util.maths;

public class Vec2d {
	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public final double x, y;

	public Vec2d add(Vec2d other) {
		return this.add(other.x, other.y);
	}

	public Vec2d add(double x, double y) {
		return x == 0 && y == 0 ? this : new Vec2d(this.x + x, this.y + y);
	}

	public Vec2d sub(Vec2d other) {
		return this.add(other.x, other.y);
	}

	public Vec2d sub(double x, double y) {
		return x == 0 && y == 0 ? this : new Vec2d(this.x - x, this.y - y);
	}

	public Vec2d mult(double scalar) {
		return scalar == 1.0D ? this : new Vec2d(this.x * scalar, this.y * scalar);
	}

	public Vec2d mult(Vec2d other) {
		return this.mult(other.x, other.y);
	}

	public Vec2d mult(double x, double y) {
		return x == 1.0D && y == 1.0D ? this : new Vec2d(this.x * x, this.y * y);
	}

	public Vec2d div(double scalar) {
		return scalar == 1.0D ? this : new Vec2d(this.x / scalar, this.y / scalar);
	}

	public Vec2d div(Vec2d other) {
		return this.div(other.x, other.y);
	}

	public Vec2d div(double x, double y) {
		return x == 1.0D && y == 1.0D ? this : new Vec2d(this.x / x, this.y / y);
	}

	public boolean equals(Object other) {
		if (other instanceof Vec2d) {
			Vec2d v2 = ((Vec2d) other);
			return v2.x == this.x && v2.y == this.y;
		} else {
			return false;
		}
	}
}
